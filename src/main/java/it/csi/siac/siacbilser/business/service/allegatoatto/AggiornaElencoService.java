/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.allegatoatto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.ElencoDocumentiAllegatoDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaElencoResponse;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.StatoOperativoElencoDocumenti;
import it.csi.siac.siacfin2ser.model.Subdocumento;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaElencoService extends CheckedAccountBaseService<AggiornaElenco,AggiornaElencoResponse> {
	
	private ElencoDocumentiAllegato elencoDocumentiAllegato;
	
	@Autowired
	private ElencoDocumentiAllegatoDad elencoDocumentiAllegatoDad;

	private Bilancio bilancio;

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		this.elencoDocumentiAllegato = req.getElencoDocumentiAllegato();
		
		checkNotNull(req.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"));
		checkCondition(req.getBilancio().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid bilancio"));
		this.bilancio = req.getBilancio();
		
		checkNotNull(elencoDocumentiAllegato, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("predocumento"));
		checkNotNull(elencoDocumentiAllegato.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		checkCondition(elencoDocumentiAllegato.getEnte().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente"));
		
		checkNotNull(elencoDocumentiAllegato.getAnno(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno elenco"));
		
		//l'allegato atto e' sorprendentemente facoltativo!
//		checkNotNull(elencoDocumentiAllegato.getAllegatoAtto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("allegato ato"));
//		checkCondition(elencoDocumentiAllegato.getAllegatoAtto().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid allegato atto"));
		
		checkNotNull(elencoDocumentiAllegato.getStatoOperativoElencoDocumenti(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stato operativo elenco documenti"));
		
		if (elencoDocumentiAllegato.getStatoOperativoElencoDocumenti() == null) {
			//Se non specificato lo stato operativo elenco di default è BOZZA
			elencoDocumentiAllegato.setStatoOperativoElencoDocumenti(StatoOperativoElencoDocumenti.BOZZA);
		}
		
		checkNotNull(elencoDocumentiAllegato.getSubdocumenti(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("subdocumenti"));
		checkCondition(!elencoDocumentiAllegato.getSubdocumenti().isEmpty(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("subdocumenti"));
		
		for(Subdocumento<?, ?> subdoc : elencoDocumentiAllegato.getSubdocumenti()){
			checkNotNull(subdoc, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("subdocumento"));
			checkCondition(subdoc.getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid subdocumento"));
		}
		
	}
	
	@Override
	@Transactional
	public AggiornaElencoResponse executeService(AggiornaElenco serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void init() {
		elencoDocumentiAllegatoDad.setLoginOperazione(loginOperazione);
		elencoDocumentiAllegatoDad.setEnte(elencoDocumentiAllegato.getEnte());
	}
	
	@Override
	protected void execute() {
		
		elencoDocumentiAllegatoDad.aggiornaElencoDocumentiAllegato(elencoDocumentiAllegato);		
		res.setElencoDocumentiAllegato(elencoDocumentiAllegato);
	}

}
