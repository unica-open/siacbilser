/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.gruppoattivitaiva;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.ProrataEChiusuraGruppoIvaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaGruppoAttivitaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaGruppoAttivitaIvaEProrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaGruppoAttivitaIvaEProrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaProrataEChiusuraGruppoIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceProrataEChiusuraGruppoIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceProrataEChiusuraGruppoIvaResponse;
import it.csi.siac.siacfin2ser.model.GruppoAttivitaIva;
import it.csi.siac.siacfin2ser.model.ProRataEChiusuraGruppoIva;

// TODO: Auto-generated Javadoc
/**
 * The Class AggiornaGruppoAttivitaIvaEProrataService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaGruppoAttivitaIvaEProrataService extends CheckedAccountBaseService<AggiornaGruppoAttivitaIvaEProrata, AggiornaGruppoAttivitaIvaEProrataResponse> {

	/** The aggiorna gruppo attivita iva service. */
	@Autowired
	private AggiornaGruppoAttivitaIvaService aggiornaGruppoAttivitaIvaService;
	
	/** The aggiorna prorata e chiusura gruppo iva service. */
	@Autowired
	private AggiornaProrataEChiusuraGruppoIvaService aggiornaProrataEChiusuraGruppoIvaService;
	
	/** The inserisce prorata e chiusura gruppo iva service. */
	@Autowired
	private InserisceProrataEChiusuraGruppoIvaService inserisceProrataEChiusuraGruppoIvaService;
	
	/** The prorata e chiusura gruppo iva dad. */
	@Autowired
	private ProrataEChiusuraGruppoIvaDad prorataEChiusuraGruppoIvaDad;

	/** The gruppo. */
	private GruppoAttivitaIva gruppo;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		checkEntita(req.getGruppoAttivitaIva(), "gruppo attivita iva");
		gruppo = req.getGruppoAttivitaIva();
		
		checkNotNull(gruppo.getCodice(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("codice gruppo Iva"), false);
		checkNotNull(gruppo.getDescrizione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("descrizione gruppo Iva"), false);
		checkNotNull(gruppo.getTipoChiusura(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo chiusura gruppo Iva"), false);
		checkNotNull(gruppo.getAnnualizzazione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("annualizzazione gruppo Iva"), false);
		
		checkEntita(gruppo.getEnte(), "ente gruppo", false);
		
		checkCondition(!gruppo.getListaProRataEChiusuraGruppoIva().isEmpty(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("lista prorata e chiusura gruppo iva"), false);
		
		for(ProRataEChiusuraGruppoIva prorata : gruppo.getListaProRataEChiusuraGruppoIva()){
			checkNotNull(prorata, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("prorata gruppo Iva"), false);
			checkNotNull(prorata.getAnnoEsercizio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno esercizio prorata gruppo Iva"), false);
			checkNotNull(prorata.getPercentualeProRata(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("percentuale prorata gruppo Iva"), false);
			
			checkEntita(prorata.getEnte(), "ente prorata", false);
		}
		
		
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional
	@Override
	public AggiornaGruppoAttivitaIvaEProrataResponse executeService(AggiornaGruppoAttivitaIvaEProrata serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		
		aggiornaGruppoAttivitaIva();
		
		for(ProRataEChiusuraGruppoIva prorata : gruppo.getListaProRataEChiusuraGruppoIva()){
			
			GruppoAttivitaIva gai = new GruppoAttivitaIva();
			gai.setUid(gruppo.getUid());
			prorata.setGruppoAttivitaIva(gai);
			
			Integer uid = prorataEChiusuraGruppoIvaDad.ricercaUidProrataLegataAlGruppoPerUnAnno(prorata, gruppo);
			
			if(uid!=null && uid!=0){
				prorata.setUid(uid);
				aggiornaProrataEChiusuraGruppoIva(prorata);
			}else{
				inserisceProrataEChiusuraGruppoIva(prorata);
			}
			
		}		
		
		res.setGruppoAttivitaIva(gruppo);
	}


	/**
	 * Aggiorna gruppo attivita iva.
	 */
	private void aggiornaGruppoAttivitaIva() {
		AggiornaGruppoAttivitaIva reqIGAI = new AggiornaGruppoAttivitaIva();
		reqIGAI.setRichiedente(req.getRichiedente());
		reqIGAI.setGruppoAttivitaIva(gruppo);
		executeExternalServiceSuccess(aggiornaGruppoAttivitaIvaService, reqIGAI);
	}
	
	/**
	 * Aggiorna prorata e chiusura gruppo iva.
	 *
	 * @param prorata the prorata
	 */
	private void aggiornaProrataEChiusuraGruppoIva(ProRataEChiusuraGruppoIva prorata) {
		AggiornaProrataEChiusuraGruppoIva reqIPECGI = new AggiornaProrataEChiusuraGruppoIva();
		reqIPECGI.setRichiedente(req.getRichiedente());
		reqIPECGI.setProRataEChiusuraGruppoIva(prorata);
		executeExternalServiceSuccess(aggiornaProrataEChiusuraGruppoIvaService, reqIPECGI);
		
	}

	/**
	 * Inserisce prorata e chiusura gruppo iva.
	 *
	 * @param prorata the prorata
	 */
	private void inserisceProrataEChiusuraGruppoIva(ProRataEChiusuraGruppoIva prorata) {
		InserisceProrataEChiusuraGruppoIva reqIPECGI = new InserisceProrataEChiusuraGruppoIva();
		reqIPECGI.setRichiedente(req.getRichiedente());
		reqIPECGI.setProRataEChiusuraGruppoIva(prorata);
		InserisceProrataEChiusuraGruppoIvaResponse resIPECGI = executeExternalServiceSuccess(inserisceProrataEChiusuraGruppoIvaService, reqIPECGI);
		prorata.setUid(resIPECGI.getProRataEChiusuraGruppoIva().getUid());
		
	}


}
