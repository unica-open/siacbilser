/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.classificatorebil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiTreePianoDeiConti;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiTreePianoDeiContiResponse;
import it.csi.siac.siacbilser.integration.dao.CodificaBilDao;
import it.csi.siac.siacbilser.integration.entity.SiacDClassTipo;
import it.csi.siac.siacbilser.integration.entity.SiacRClassFamTree;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDClassFamEnum;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.TipoClassificatore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

// TODO: Auto-generated Javadoc
//TODO da rivedere ...
//vedere dad
//spostare dao / repository (query)
//rivedere mapper
//rivedere case

/**
 * The Class LeggiTreePianoDeiContiService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LeggiTreePianoDeiContiService 
extends CheckedAccountBaseService<LeggiTreePianoDeiConti, LeggiTreePianoDeiContiResponse>{
	
	/** The codifica bil dao. */
	@Autowired
	private CodificaBilDao codificaBilDao;
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkCondition(req.getAnno() > 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno"),false);
		checkCondition(req.getIdEnteProprietario() > 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("enteProprietarioId"),false);
		//checkCondition(req.getIdCodificaPadre() > 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("IdCodificaPadre"),false);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional(readOnly=true)
	public LeggiTreePianoDeiContiResponse executeService(LeggiTreePianoDeiConti serviceRequest){
		return super.executeService(serviceRequest);
	}
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		List<SiacTClass> siacTClass = codificaBilDao.findTreeByCodiceFamiglia(req.getAnno(), req.getIdEnteProprietario(), SiacDClassFamEnum.PianoDeiConti.getCodice(), req.getIdCodificaPadre(), false);
		

		if (siacTClass == null || siacTClass.isEmpty()) {
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("elemento piano dei conti"));
			res.setEsito(Esito.FALLIMENTO);
			return;
		}
		
		List<ElementoPianoDeiConti> result = new ArrayList<ElementoPianoDeiConti>();
		result = convertTreeDto(siacTClass, result, new ElementoPianoDeiConti());

		res.setCardinalitaComplessiva(result.size());
		res.setTreeElementoPianoDeiConti(result);
	}	
	
	//@Transactional(propagation=Propagation.MANDATORY)
	/**
	 * Convert tree dto.
	 *
	 * @param dtos the dtos
	 * @param list the list
	 * @param padre the padre
	 * @return the list
	 */
	public List<ElementoPianoDeiConti> convertTreeDto(List<SiacTClass> dtos, List<ElementoPianoDeiConti> list, ElementoPianoDeiConti padre) {
		
		//SIAC-8546 
		Date primoGiornoAnno = Utility.primoGiornoDellAnno(req.getAnno());
		
		for (SiacTClass dto : dtos) {

			//SIAC-8546
			if(!dto.isDataValiditaCompresa(primoGiornoAnno)) {
				continue;
			}
			
			ElementoPianoDeiConti obj = new ElementoPianoDeiConti();

			obj.setUid(dto.getUid());
			obj.setCodice(dto.getClassifCode());
			obj.setDescrizione(dto.getClassifDesc());
			SiacDClassTipo siacDClassTipo = dto.getSiacDClassTipo();
			if(siacDClassTipo==null){
				throw new BusinessException("Impossibile trovare il tipo per il classificatore con uid: "+ dto.getUid());
			}
			TipoClassificatore tipoClassificatore = new TipoClassificatore();
			tipoClassificatore.setUid(siacDClassTipo.getUid());
			tipoClassificatore.setCodice(siacDClassTipo.getClassifTipoCode());
			tipoClassificatore.setDescrizione(siacDClassTipo.getClassifTipoDesc());
			obj.setTipoClassificatore(tipoClassificatore);	
			

			if (dto.getSiacRClassFamTreesPadre() != null && !dto.getSiacRClassFamTreesPadre().isEmpty()) {
				padre = obj;
				
				List<SiacRClassFamTree> siacRClassFamTreesPadre = dto.getSiacRClassFamTreesPadre();
				List<SiacTClass> figli = new ArrayList<SiacTClass>();
				
				for(SiacRClassFamTree srcftp : siacRClassFamTreesPadre) {
					/*qui ci va una data compresa nell'anno di bilancio*/
					if(srcftp.getDataCancellazione() != null || !srcftp.isDataValiditaCompresa(primoGiornoAnno)){
						continue;
					}
					//SIAC-8546
					if(srcftp.getSiacTClassFiglio() != null && srcftp.getSiacTClassFiglio().isDataValiditaCompresa(primoGiornoAnno)) {
						figli.add(srcftp.getSiacTClassFiglio());
					}
				}

				List<ElementoPianoDeiConti> elemPdc = new ArrayList<ElementoPianoDeiConti>();
				//elemPdc.addAll(convertTreeDto(figli, elemPdc, padre));
				checkIncoerenzaClassificatoreFiglioDiSeStesso(siacRClassFamTreesPadre);
				elemPdc = convertTreeDto(figli, elemPdc, padre);
				padre.setElemPdc(elemPdc);
				list.add(padre);

			} else {
				list.add(obj);
			}

		}
		return list;
	}

	/**
	 * Questo check evita che il metodo ricorsivo convertTreeDto vada in StackOverflow!!.
	 *
	 * @param siacRClassFamTreesPadre the figli
	 */
	private void checkIncoerenzaClassificatoreFiglioDiSeStesso(List<SiacRClassFamTree> siacRClassFamTreesPadre) {
		if(siacRClassFamTreesPadre!=null) {
			for (SiacRClassFamTree srcft : siacRClassFamTreesPadre) {
				SiacTClass figlio = srcft.getSiacTClassFiglio();
				SiacTClass padre = srcft.getSiacTClassPadre();
				if(figlio!=null && padre != null && figlio.getUid().equals(padre.getUid())){
					log.error("convertTreeDto","FIGLIO: codice:" + figlio.getClassifCode() + " id:"+figlio.getUid()+  " desc:"+figlio.getClassifDesc());
					log.error("convertTreeDto","PADRE:  codice:" + padre.getClassifCode() + " id:"+padre.getUid()+  " desc:"+padre.getClassifDesc());
					throw new IllegalStateException("Il padre ed il figlio del classificatore coincidono! Verificare sulla tabele siac_r_class_fam_tree il classificatore con id: "+ figlio.getUid());
				}					
			}
		}
	}

}
