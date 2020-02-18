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
		//List<CodificaDto> dtos = codificaBilDao.findCodificheByTipoElemBilancio(req.getAnno(),req.getIdEnteProprietario(),req.getTipoElementoBilancio());
		
		List<SiacTClass> siacTClasses = codificaBilDao.findCodificheGenericiTipoElemBilancio(req.getAnno(),req.getIdEnteProprietario(),req.getTipoElementoBilancio());

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

			
			/*//E' sempre un piacere togliere switch da 39 case!!!
			switch (tipo) {
			case TIPO_FINANZIAMENTO: 
				res.getClassificatoriTipoFinanziamento().add(dozerBeanMapper.map(classificatore, TipoFinanziamento.class));
				break;
			case TIPO_FONDO:
				res.getClassificatoriTipoFondo().add(dozerBeanMapper.map(classificatore, TipoFondo.class));
				break;
			case RICORRENTE_ENTRATA:
				res.getClassificatoriRicorrenteEntrata().add(dozerBeanMapper.map(classificatore,RicorrenteEntrata.class));
				break;
			case RICORRENTE_SPESA:
				res.getClassificatoriRicorrenteSpesa().add(dozerBeanMapper.map(classificatore,RicorrenteSpesa.class));
				break;
			case PERIMETRO_SANITARIO_ENTRATA:
				res.getClassificatoriPerimetroSanitarioEntrata().add(dozerBeanMapper.map(classificatore,PerimetroSanitarioEntrata.class));
				break;
			case PERIMETRO_SANITARIO_SPESA:
				res.getClassificatoriPerimetroSanitarioSpesa().add(dozerBeanMapper.map(classificatore,PerimetroSanitarioSpesa.class));
				break;				
			case TRANSAZIONE_UE_ENTRATA:
				res.getClassificatoriTransazioneUnioneEuropeaEntrata().add(dozerBeanMapper.map(classificatore,TransazioneUnioneEuropeaEntrata.class));
				break;
			case TRANSAZIONE_UE_SPESA:
				res.getClassificatoriTransazioneUnioneEuropeaSpesa().add(dozerBeanMapper.map(classificatore,TransazioneUnioneEuropeaSpesa.class));
				break;
			case POLITICHE_REGIONALI_UNITARIE:
				res.getClassificatoriPoliticheRegionaliUnitarie().add(dozerBeanMapper.map(classificatore,PoliticheRegionaliUnitarie.class));
				break;				
			case CLASSIFICATORE_1:
//				res.getClassificatoriGenerici1().add(dozerBeanMapper.map(classificatore, ClassificatoreGenerico.class));
				res.getClassificatoriGenerici1().add(classificatore);
				break;
			case CLASSIFICATORE_2:
//				res.getClassificatoriGenerici2().add(dozerBeanMapper.map(classificatore, ClassificatoreGenerico.class));
				res.getClassificatoriGenerici2().add(classificatore);
				break;
			case CLASSIFICATORE_3:
//				res.getClassificatoriGenerici3().add(dozerBeanMapper.map(classificatore, ClassificatoreGenerico.class));
				res.getClassificatoriGenerici3().add(classificatore);
				break;
			case CLASSIFICATORE_4:
//				res.getClassificatoriGenerici4().add(dozerBeanMapper.map(classificatore, ClassificatoreGenerico.class));
				res.getClassificatoriGenerici4().add(classificatore);
				break;
			case CLASSIFICATORE_5:
//				res.getClassificatoriGenerici5().add(dozerBeanMapper.map(classificatore, ClassificatoreGenerico.class));
				res.getClassificatoriGenerici5().add(classificatore);
				break;
			case CLASSIFICATORE_6:
//				res.getClassificatoriGenerici6().add(dozerBeanMapper.map(classificatore, ClassificatoreGenerico.class));
				res.getClassificatoriGenerici6().add(classificatore);
				break;
			case CLASSIFICATORE_7:
//				res.getClassificatoriGenerici7().add(dozerBeanMapper.map(classificatore, ClassificatoreGenerico.class));
				res.getClassificatoriGenerici7().add(classificatore);
				break;
			case CLASSIFICATORE_8:
//				res.getClassificatoriGenerici8().add(dozerBeanMapper.map(classificatore, ClassificatoreGenerico.class));
				res.getClassificatoriGenerici8().add(classificatore);
				break;
			case CLASSIFICATORE_9:
//				res.getClassificatoriGenerici9().add(dozerBeanMapper.map(classificatore, ClassificatoreGenerico.class));
				res.getClassificatoriGenerici9().add(classificatore);
				break;
			case CLASSIFICATORE_10:
//				res.getClassificatoriGenerici10().add(dozerBeanMapper.map(classificatore, ClassificatoreGenerico.class));
				res.getClassificatoriGenerici10().add(classificatore);
				break;
			case CLASSIFICATORE_31:
//				res.getClassificatoriGenerici10().add(dozerBeanMapper.map(classificatore, ClassificatoreGenerico.class));
				res.getClassificatoriGenerici31().add(classificatore);
				break;
			case CLASSIFICATORE_32:
				res.getClassificatoriGenerici32().add(classificatore);
				break;
			case CLASSIFICATORE_33:
				res.getClassificatoriGenerici33().add(classificatore);
				break;
			case CLASSIFICATORE_34:
				res.getClassificatoriGenerici34().add(classificatore);
				break;
			case CLASSIFICATORE_35:
				res.getClassificatoriGenerici35().add(classificatore);
				break;
			case CLASSIFICATORE_36:
				res.getClassificatoriGenerici36().add(classificatore);
				break;
			case CLASSIFICATORE_37:
				res.getClassificatoriGenerici37().add(classificatore);
				break;
			case CLASSIFICATORE_38:
				res.getClassificatoriGenerici38().add(classificatore);
				break;
			case CLASSIFICATORE_39:
				res.getClassificatoriGenerici39().add(classificatore);
				break;
			case CLASSIFICATORE_40:
				res.getClassificatoriGenerici40().add(classificatore);
				break;
			case CLASSIFICATORE_41:
				res.getClassificatoriGenerici41().add(classificatore);
				break;
			case CLASSIFICATORE_42:
				res.getClassificatoriGenerici42().add(classificatore);
				break;
			case CLASSIFICATORE_43:
				res.getClassificatoriGenerici43().add(classificatore);
				break;
			case CLASSIFICATORE_44:
				res.getClassificatoriGenerici44().add(classificatore);
				break;
			case CLASSIFICATORE_45:
				res.getClassificatoriGenerici45().add(classificatore);
				break;
			case CLASSIFICATORE_46:
				res.getClassificatoriGenerici46().add(classificatore);
				break;
			case CLASSIFICATORE_47:
				res.getClassificatoriGenerici47().add(classificatore);
				break;
			case CLASSIFICATORE_48:
				res.getClassificatoriGenerici48().add(classificatore);
				break;
			case CLASSIFICATORE_49:
				res.getClassificatoriGenerici49().add(classificatore);
				break;
			case CLASSIFICATORE_50:
				res.getClassificatoriGenerici50().add(classificatore);
				break;
			default:
				break;
			}
			
			*/
			
		
		}
	}
}
