/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.conto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dao.CodificaBilDao;
import it.csi.siac.siacbilser.integration.dao.SiacTPdceContoRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDClassFam;
import it.csi.siac.siacbilser.integration.entity.SiacDClassTipo;
import it.csi.siac.siacbilser.integration.entity.SiacRClassFamTree;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDClassFamEnum;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.TipoClassificatore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.frontend.webservice.msg.LeggiTreeCodiceBilancio;
import it.csi.siac.siacgenser.frontend.webservice.msg.LeggiTreeCodiceBilancioResponse;
import it.csi.siac.siacgenser.model.CodiceBilancio;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LeggiTreeCodiceBilancioService extends CheckedAccountBaseService<LeggiTreeCodiceBilancio, LeggiTreeCodiceBilancioResponse>{

	@Autowired
	private CodificaBilDao codificaBilDao;
	
	@Autowired
	private SiacTPdceContoRepository siacTPdceContoRepository;
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getClassePiano(), "classe piano");
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional(readOnly=true)
	public LeggiTreeCodiceBilancioResponse executeService(LeggiTreeCodiceBilancio serviceRequest){
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		SiacDClassFam siacDClassFam = siacTPdceContoRepository.findClassFamByClassePiano(req.getClassePiano().getUid());
		if (siacDClassFam == null ) {
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("SiacDClassFam per la classe piano con uid: " + req.getClassePiano().getUid()));
			res.setEsito(Esito.FALLIMENTO);
			return;
		}
		SiacDClassFamEnum siacDClassFamEnum = SiacDClassFamEnum.byCodice(siacDClassFam.getClassifFamCode());
		List<SiacTClass> siacTClass = codificaBilDao.findTreeByCodiceFamiglia(req.getAnno(), ente.getUid(), siacDClassFamEnum.getCodice(), req.getIdCodificaPadre(), true);
		if (siacTClass == null || siacTClass.isEmpty()) {
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("codifica di bilancio"));
			res.setEsito(Esito.FALLIMENTO);
			return;
		}
		List<CodiceBilancio> result = new ArrayList<CodiceBilancio>();
		log.debug("execute", "ho trovato " + siacTClass.size() + "elementi");
		result = convertTreeDto(siacTClass, result, new CodiceBilancio());

		res.setCardinalitaComplessiva(result.size());
		res.setTreeCodiciBilancio(result);
		
	}
	
	/**
	 * Convert tree dto.
	 *
	 * @param dtos the dtos
	 * @param list the list
	 * @param padre the padre
	 * @return the list
	 */
	public  List<CodiceBilancio> convertTreeDto(List<SiacTClass> dtos, List<CodiceBilancio> list, CodiceBilancio padre) {

		for (SiacTClass dto : dtos) {

			CodiceBilancio obj = new CodiceBilancio();

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
					figli.add(srcftp.getSiacTClassFiglio());
				}

				List<CodiceBilancio> codBilancio = new ArrayList<CodiceBilancio>();
				//elemPdc.addAll(convertTreeDto(figli, elemPdc, padre));
				checkIncoerenzaClassificatoreFiglioDiSeStesso(siacRClassFamTreesPadre);
				codBilancio = convertTreeDto(figli, codBilancio, padre);
				padre.setFigli(codBilancio);
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
