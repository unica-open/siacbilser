/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.classificatorebil;

import java.util.ArrayList;
import java.util.List;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoElementoBil;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoElementoBilResponse;
import it.csi.siac.siacbilser.integration.dao.CodificaBilDao;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDClassTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.Codifica;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

// TODO: Auto-generated Javadoc
//TODO da rivedere ...
//vedere dad
//spostare dao / repository (query)
//rivedere mapper
//rivedere case

/**
 * The Class LeggiClassificatoriGenericiByTipoElementoBilService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LeggiClassificatoriGenericiByTipoElementoBilService 
	extends CheckedAccountBaseService<LeggiClassificatoriGenericiByTipoElementoBil, LeggiClassificatoriGenericiByTipoElementoBilResponse>{
	
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
		checkCondition(req.getIdEnteProprietario() > 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Ente Proprietario"),false);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional(readOnly=true)
	public LeggiClassificatoriGenericiByTipoElementoBilResponse executeService(LeggiClassificatoriGenericiByTipoElementoBil serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		
		List<SiacTClass> siacTClasses = codificaBilDao.findCodificheGenericiTipoElemBilancio(req.getAnno(),
				req.getIdEnteProprietario(),req.getTipoElementoBilancio(), req.getTipologiaClassificatore() == null ? null : req.getTipologiaClassificatore().name());

		if (siacTClasses == null || siacTClasses.isEmpty()) {
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Classificatori generici", req.getTipoElementoBilancio() + " del " +  (req.getAnno())));
			res.setEsito(Esito.FALLIMENTO);
			return;
		}
		
		res.setClassificatori(new ArrayList<ClassificatoreGenerico>());
			
		for (SiacTClass c : siacTClasses) {

			SiacDClassTipoEnum siacDClassTipoEnum = SiacDClassTipoEnum.byCodiceEvenNull(c.getSiacDClassTipo().getClassifTipoCode());

			if(siacDClassTipoEnum==null){
				continue; //Escludo tutti i classificatori non mappati in SiacDClassTipoEnum!!!
			}
			
			Codifica codifica = siacDClassTipoEnum.getCodificaInstance();
			
			if(!ClassificatoreGenerico.class.isInstance(codifica)){
				continue; //Escluto tutti i classificatori NON generici!
			}
			
			ClassificatoreGenerico classificatore = (ClassificatoreGenerico) codifica;
			dozerBeanMapper.map(c, classificatore, BilMapId.SiacTClass_ClassificatoreGenerico.name());
			res.getClassificatori().add(classificatore);
			
			//Classificatori Generici:
			//1-10 e 31-35 Spesa
			//36-50        Entrata

		}
	}
}
