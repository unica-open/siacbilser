/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.classificatorebil;

import java.util.List;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBil;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBilResponse;
import it.csi.siac.siacbilser.integration.dao.CodificaBilDao;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDClassTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.model.Missione;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.ClassificatoreGerarchico;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

// TODO: Auto-generated Javadoc
//TODO da rivedere ...
//vedere dad
//spostare dao / repository (query)
//rivedere mapper
//rivedere case

/**
 * The Class LeggiClassificatoriByTipoElementoBilService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LeggiClassificatoriByTipoElementoBilService 
	extends CheckedAccountBaseService<LeggiClassificatoriByTipoElementoBil, LeggiClassificatoriByTipoElementoBilResponse>{
	
	/** The codifica bil dao. */
	@Autowired
	private CodificaBilDao codificaBilDao;
	
	/** The dozer bean mapper. */
	@Autowired
	protected Mapper dozerBeanMapper;
		
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkCondition(req.getAnno() > 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno"),false);
		checkCondition(req.getIdEnteProprietario() > 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("enteProprietarioId"),false);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional(readOnly=true)
	public LeggiClassificatoriByTipoElementoBilResponse executeService(LeggiClassificatoriByTipoElementoBil serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		List<SiacTClass> siacTClasses = codificaBilDao.findCodificheConLivelloByTipoElemBilancio(req.getAnno(), req.getIdEnteProprietario(), req.getTipoElementoBilancio());

		//log.debug("Dto trovati: " + dtos!=null?dtos.size():"null");
		
		if (siacTClasses == null || siacTClasses.isEmpty()) {
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("classificatori"));
			res.setEsito(Esito.FALLIMENTO);
			return;
		}
		
		for (SiacTClass c : siacTClasses) {						
			
			//log.debug("CodificaDto: "+ c);
			
			if(c != null && c.getSiacDClassTipo() != null){
				
				//log.debug("CodificaDto codice: "+ c.getTipoClassificatore().getCodice());
				
				//TipologiaClassificatore codice = TipologiaClassificatore.fromCodice(c.getSiacDClassTipo().getClassifTipoCode());
				SiacDClassTipoEnum sdcte = SiacDClassTipoEnum.byCodice(c.getSiacDClassTipo().getClassifTipoCode());
				
				if(sdcte==null){
					continue;
				}
				
				//ClassificatoreGerarchico classificatore = dozerBeanMapper.map(c, ClassificatoreGerarchico.class, BilMapId.SiacTClass_ClassificatoreGerarchico.name());
				// Non mi serve il livello
				TipologiaClassificatore tipo = sdcte.getTipologiaClassificatore();
				ClassificatoreGerarchico classificatore = sdcte.getCodificaInstance();
				dozerBeanMapper.map(c, classificatore, BilMapId.SiacTClass_ClassificatoreGerarchico_Reduced.name());
//				ClassificatoreGerarchico classificatore = dozerBeanMapper.map(c, ClassificatoreGerarchico.class, BilMapId.SiacTClass_ClassificatoreGerarchico_Reduced.name());
				
				switch (tipo) {
				case MISSIONE:
					res.getClassificatoriMissione().add((Missione)classificatore);
					break;
				case TITOLO_SPESA:
					res.getClassificatoriTitoloSpesa().add((TitoloSpesa)classificatore);
					break;
				case TITOLO_ENTRATA:
					res.getClassificatoriTitoloEntrata().add((TitoloEntrata)classificatore);
					break;
				default:
					break;
				}
			}
		}
	}
}
