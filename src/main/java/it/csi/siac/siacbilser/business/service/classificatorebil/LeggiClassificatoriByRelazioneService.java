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
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByRelazione;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByRelazioneResponse;
import it.csi.siac.siacbilser.integration.dad.ClassificatoriDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Codifica;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

// TODO: Auto-generated Javadoc
/**
 * 
 * Ricerca l'elenco dei classificatori in relazione ad un classificatore specifico.
 * Vedi tabella: siac_r_class
 * 
 * @author Domenico
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LeggiClassificatoriByRelazioneService 
	extends CheckedAccountBaseService<LeggiClassificatoriByRelazione, LeggiClassificatoriByRelazioneResponse> {

	
	/** The classificatori dad. */
	@Autowired
	private ClassificatoriDad classificatoriDad;
	
	/** The dozer bean mapper. */
	@Autowired
	protected Mapper dozerBeanMapper;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		checkCondition(req.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente"));
		
		checkNotNull(req.getIdClassif(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("id classificatore"));
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional(readOnly=true)
	public LeggiClassificatoriByRelazioneResponse executeService(LeggiClassificatoriByRelazione serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		
		List<Codifica> classificatori; 
		if(req.isFromAToB()){
			classificatori = classificatoriDad.ricercaClassificatoriBByClassificatoreA(req.getIdClassif());
		} else {
			classificatori = classificatoriDad.ricercaClassificatoriBByClassificatoreB(req.getIdClassif());
		}
		
		res.setClassificatori(classificatori);
		
		
		
		

//		List<CodificaDto> dtos = codificaBilDao.findCodificheByIdPadre(req.getAnno(), req.getIdEnteProprietario(), req.getIdPadre());
//	
//		if (dtos == null || dtos.isEmpty()) {
//			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("classificatori", "anno: "+req.getAnno()+" idPadre: "+req.getIdPadre()));
//			res.setEsito(Esito.FALLIMENTO);
//			return;
//		}
//		
//		for (CodificaDto c : dtos) {
//			TipologiaClassificatore tipo = TipologiaClassificatore.fromCodice(c.getTipoClassificatore().getCodice());
//			if(tipo==null){
//				continue;
//			}
//
//			switch (tipo) {
//			case PROGRAMMA:
//				res.getClassificatoriProgramma().add(dozerBeanMapper.map(c, Programma.class));
//				break;
//			case MACROAGGREGATO:
//				res.getClassificatoriMacroaggregato().add(dozerBeanMapper.map(c, Macroaggregato.class));
//				break;
//			case CLASSIFICAZIONE_COFOG:
//			case DIVISIONE_COFOG:
//			case GRUPPO_COFOG:
//			case CLASSE_COFOG:
//				res.getClassificatoriClassificazioneCofog().add(dozerBeanMapper.map(c,ClassificazioneCofog.class));
//				break;
//			case TIPOLOGIA:
//				res.getClassificatoriTipologiaTitolo().add(dozerBeanMapper.map(c, TipologiaTitolo.class));
//				break;
//			case CATEGORIA:
//				res.getClassificatoriCategoriaTipologiaTitolo().add(dozerBeanMapper.map(c, CategoriaTipologiaTitolo.class));
//				break;
//			default:
//				break;
//			}
//		}
	}

}
