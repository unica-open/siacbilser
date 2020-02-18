/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.classificatorebil;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiTreeSiope;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiTreeSiopeResponse;
import it.csi.siac.siacbilser.integration.dao.CodificaBilDao;
import it.csi.siac.siacbilser.integration.entity.SiacDClassTipo;
import it.csi.siac.siacbilser.integration.entity.SiacRClassFamTree;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDClassFamEnum;
import it.csi.siac.siacbilser.model.SiopeEntrata;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.TipoClassificatore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * The Class LeggiTreeSiopeEntrataService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LeggiTreeSiopeEntrataService extends CheckedAccountBaseService<LeggiTreeSiope, LeggiTreeSiopeResponse> {
	
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
		//checkNotNull(req.getFamigliaTreeCodice(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("famigliaTreeCodice"),false);
		checkCondition(req.getIdCodificaPadre() > 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("IdCodificaPadre"),false);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional(readOnly=true)
	public LeggiTreeSiopeResponse executeService(LeggiTreeSiope serviceRequest){
		return super.executeService(serviceRequest);
	}
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		List<SiacTClass> siacTClasses = codificaBilDao.findTreeByCodiceFamiglia(req.getAnno(), req.getIdEnteProprietario(), SiacDClassFamEnum.SiopeEntrata.getCodice(), req.getIdCodificaPadre(), false);

		if (siacTClasses == null || siacTClasses.isEmpty()) {
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("siope entrata"));
			res.setEsito(Esito.FALLIMENTO);
			return;
		}
			
		List<SiopeEntrata> result = new ArrayList<SiopeEntrata>();
		result = convertTreeDtoSiopeEntrata(siacTClasses, result, new SiopeEntrata());	
		res.setCardinalitaComplessiva(result.size());
		res.setTreeSiopeEntrata(result);
		
	}
	
	
	//@Transactional(propagation=Propagation.MANDATORY)
	/**
	 * Convert tree dto siope entrata.
	 *
	 * @param dtos the dtos
	 * @param list the list
	 * @param padre the padre
	 * @return the list
	 */
	public  List<SiopeEntrata> convertTreeDtoSiopeEntrata(List<SiacTClass> siacTClasses,
			List<SiopeEntrata> list, SiopeEntrata padre) {

		for (SiacTClass siacTClass : siacTClasses) {

			SiopeEntrata obj = new SiopeEntrata();

			obj.setUid(siacTClass.getUid());
			obj.setCodice(siacTClass.getClassifCode());
			obj.setDescrizione(siacTClass.getClassifDesc());
			SiacDClassTipo siacDClassTipo = siacTClass.getSiacDClassTipo();
			if(siacDClassTipo==null){
				throw new BusinessException("Impossibile trovare il tipo per il classificatore con uid: "+ siacTClass.getUid());
			}
			TipoClassificatore tipoClassificatore = new TipoClassificatore();
			tipoClassificatore.setUid(siacDClassTipo.getUid());
			tipoClassificatore.setCodice(siacDClassTipo.getClassifTipoCode());
			tipoClassificatore.setDescrizione(siacDClassTipo.getClassifTipoDesc());
			obj.setTipoClassificatore(tipoClassificatore);	

			if (siacTClass.getSiacRClassFamTreesPadre() != null && !siacTClass.getSiacRClassFamTreesPadre().isEmpty()) {
				padre = obj;
				 
				List<SiacRClassFamTree> siacRClassFamTreesPadre = siacTClass.getSiacRClassFamTreesPadre();
				List<SiacTClass> figli = new ArrayList<SiacTClass>();
				
				for(SiacRClassFamTree srcftp : siacRClassFamTreesPadre) {
					figli.add(srcftp.getSiacTClassFiglio());
				}

				List<SiopeEntrata> siopi = new ArrayList<SiopeEntrata>();
				checkIncoerenzaClassificatoreFiglioDiSeStesso(siacRClassFamTreesPadre);
				siopi = convertTreeDtoSiopeEntrata(figli, siopi, padre);
				padre.setFigli(siopi);
				list.add(padre);

			} else {
				list.add(obj);
			}

		}
		return list;
	}
	
	
	private void checkIncoerenzaClassificatoreFiglioDiSeStesso(List<SiacRClassFamTree> siacRClassFamTreesPadre) {
		if(siacRClassFamTreesPadre!=null) {
			for (SiacRClassFamTree srcft : siacRClassFamTreesPadre) {
				SiacTClass figlio = srcft.getSiacTClassFiglio();
				SiacTClass padre = srcft.getSiacTClassPadre();
				if(figlio!=null && padre != null && figlio.getUid().equals(padre.getUid())){
					log.error("convertTreeDtoSiopeEntrata","FIGLIO: codice:" + figlio.getClassifCode() + " id:"+figlio.getUid()+  " desc:"+figlio.getClassifDesc());
					log.error("convertTreeDtoSiopeEntrata","PADRE:  codice:" + padre.getClassifCode() + " id:"+padre.getUid()+  " desc:"+padre.getClassifDesc());
					throw new IllegalStateException("Il padre ed il figlio del classificatore coincidono! Verificare sulla tabele siac_r_class_fam_tree il classificatore con id: "+ figlio.getUid());
				}					
			}
		}
	}

}
