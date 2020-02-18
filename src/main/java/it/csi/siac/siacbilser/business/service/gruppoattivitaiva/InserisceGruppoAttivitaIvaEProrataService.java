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
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceGruppoAttivitaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceGruppoAttivitaIvaEProrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceGruppoAttivitaIvaEProrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceGruppoAttivitaIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceProrataEChiusuraGruppoIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceProrataEChiusuraGruppoIvaResponse;
import it.csi.siac.siacfin2ser.model.GruppoAttivitaIva;
import it.csi.siac.siacfin2ser.model.ProRataEChiusuraGruppoIva;

// TODO: Auto-generated Javadoc
/**
 * The Class InserisceGruppoAttivitaIvaEProrataService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceGruppoAttivitaIvaEProrataService extends CheckedAccountBaseService<InserisceGruppoAttivitaIvaEProrata, InserisceGruppoAttivitaIvaEProrataResponse> {

	/** The inserisce gruppo attivita iva service. */
	@Autowired
	private InserisceGruppoAttivitaIvaService inserisceGruppoAttivitaIvaService;
	
	/** The inserisce prorata e chiusura gruppo iva service. */
	@Autowired
	private InserisceProrataEChiusuraGruppoIvaService inserisceProrataEChiusuraGruppoIvaService;

	/** The gruppo. */
	private GruppoAttivitaIva gruppo;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getGruppoAttivitaIva(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("gruppo attivita Iva"));
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
	public InserisceGruppoAttivitaIvaEProrataResponse executeService(InserisceGruppoAttivitaIvaEProrata serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		
		inserisceGruppoAttivitaIva();
		
		for(ProRataEChiusuraGruppoIva prorata : gruppo.getListaProRataEChiusuraGruppoIva()){
			GruppoAttivitaIva gai = new GruppoAttivitaIva();
			gai.setUid(gruppo.getUid());
			prorata.setGruppoAttivitaIva(gai);
			inserisceProrataEChiusuraGruppoIva(prorata);
		}		
		
		res.setGruppoAttivitaIva(gruppo);
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

	/**
	 * Inserisce gruppo attivita iva.
	 */
	private void inserisceGruppoAttivitaIva() {
		InserisceGruppoAttivitaIva reqIGAI = new InserisceGruppoAttivitaIva();
		reqIGAI.setRichiedente(req.getRichiedente());
		reqIGAI.setGruppoAttivitaIva(gruppo);
		InserisceGruppoAttivitaIvaResponse resIGAI = executeExternalServiceSuccess(inserisceGruppoAttivitaIvaService, reqIGAI, ErroreCore.ENTITA_PRESENTE.getCodice());
		if(resIGAI.verificatoErrore(ErroreCore.ENTITA_PRESENTE)){
			throw new BusinessException(ErroreCore.ENTITA_PRESENTE.getErrore("gruppo attivita' iva ", gruppo.getCodice()), Esito.FALLIMENTO);
		}
		GruppoAttivitaIva gruppoInserito =  resIGAI.getGruppoAttivitaIva();
		gruppo.setUid(gruppoInserito.getUid());
	}


}
