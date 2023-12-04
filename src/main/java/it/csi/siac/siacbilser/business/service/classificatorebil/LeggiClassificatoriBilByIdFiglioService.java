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
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriBilByIdFiglio;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriBilByIdFiglioResponse;
import it.csi.siac.siacbilser.integration.dao.CodificaBilDao;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDClassTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.model.CategoriaTipologiaTitolo;
import it.csi.siac.siacbilser.model.ClassificazioneCofog;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.Macroaggregato;
import it.csi.siac.siacbilser.model.Programma;
import it.csi.siac.siacbilser.model.TipologiaTitolo;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.ClassificatoreGerarchico;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

// TODO da rivedere ...
// vedere dad
// spostare dao / repository (query)
// rivedere mapper
// rivedere case

/**
 * The Class LeggiClassificatoriBilByIdFiglioService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LeggiClassificatoriBilByIdFiglioService 
	extends CheckedAccountBaseService<LeggiClassificatoriBilByIdFiglio, LeggiClassificatoriBilByIdFiglioResponse> {

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
		checkCondition(req.getAnno() > 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno"), false);
		checkCondition(req.getIdEnteProprietario() > 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente proprietario"), false);
		checkCondition(req.getIdFiglio() > 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("id figlio"), false);
	}	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional(readOnly=true)
	public LeggiClassificatoriBilByIdFiglioResponse executeService(LeggiClassificatoriBilByIdFiglio serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		List<SiacTClass> siacTClasses = codificaBilDao.findCodificheByIdFiglio(req.getAnno(), req.getIdEnteProprietario(), req.getIdFiglio());
	
		if (siacTClasses == null || siacTClasses.isEmpty()) {
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("classificatori", "anno: "+req.getAnno()+" idFiglio: "+req.getIdFiglio()));
		}
		
		for (SiacTClass c : siacTClasses) {
			//TipologiaClassificatore tipo = TipologiaClassificatore.fromCodice(c.getSiacDClassTipo().getClassifTipoCode());
			SiacDClassTipoEnum sdcte = SiacDClassTipoEnum.byCodice(c.getSiacDClassTipo().getClassifTipoCode());
			//if(tipo==null){
			if(sdcte==null){
				continue;
			}
			
			TipologiaClassificatore tipo = sdcte.getTipologiaClassificatore();
			ClassificatoreGerarchico classificatore = sdcte.getCodificaInstance();
			dozerBeanMapper.map(c, classificatore, BilMapId.SiacTClass_ClassificatoreGerarchico_Reduced.name());
			
			// TODO: Controllare se possa essere rifattorizzato in meglio
			//ClassificatoreGerarchico classificatore = dozerBeanMapper.map(c, ClassificatoreGerarchico.class, BilMapId.SiacTClass_ClassificatoreGerarchico_Reduced.name());

			switch (tipo) {
				case PROGRAMMA:
					res.getClassificatoriProgramma().add((Programma)classificatore);
					break;
				case TITOLO_SPESA:
					res.getClassificatoriTitoloSpesa().add((TitoloSpesa)classificatore);
					break;
				case MACROAGGREGATO:
					res.getClassificatoriMacroaggregato().add((Macroaggregato)classificatore);
					break;
				
				case CLASSIFICAZIONE_COFOG:
				case DIVISIONE_COFOG:
				case GRUPPO_COFOG:
				case CLASSE_COFOG:
					res.getClassificatoriClassificazioneCofog().add((ClassificazioneCofog)classificatore);
					break;
				case TITOLO_ENTRATA:
					res.getClassificatoriTitoloEntrata().add((TitoloEntrata)classificatore);
					break;
				case TIPOLOGIA:
					res.getClassificatoriTipologiaTitolo().add((TipologiaTitolo)classificatore);
					break;
				case CATEGORIA:
					res.getClassificatoriCategoriaTipologiaTitolo().add((CategoriaTipologiaTitolo)classificatore);
					break;
				case PDC_V:
				case PDC_IV:
				case PDC_III:
				case PDC_II:
				case PDC_I:
					res.getClassificatoriElementoPianoDeiConti().add((ElementoPianoDeiConti)classificatore);
					break;
				
				default:
					break;
			}
		}
	}

}
