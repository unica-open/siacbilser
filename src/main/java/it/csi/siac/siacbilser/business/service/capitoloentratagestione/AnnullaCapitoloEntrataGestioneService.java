/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitoloentratagestione;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloEntrataGestioneDad;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

// TODO: Auto-generated Javadoc
/**
 * The Class AnnullaCapitoloEntrataGestioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AnnullaCapitoloEntrataGestioneService 
	extends CheckedAccountBaseService<AnnullaCapitoloEntrataGestione, AnnullaCapitoloEntrataGestioneResponse> {

	/** The capitolo entrata gestione dad. */
	@Autowired
	private CapitoloEntrataGestioneDad capitoloEntrataGestioneDad;
	
	/** The verifica annullabilita capitolo entrata gestione service. */
	@Autowired
	private VerificaAnnullabilitaCapitoloEntrataGestioneService verificaAnnullabilitaCapitoloEntrataGestioneService;

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"), true);
		checkCondition(req.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"), false);
		checkNotNull(req.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"));
		checkCondition(req.getBilancio().getAnno()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno esercizio"), false);
		
		//parametri obbligatori per la verifica
		checkNotNull(req.getCapitoloEntrataGest(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitoloEntrataGestione"));
		checkCondition(req.getCapitoloEntrataGest().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitoloEntrataGestione uid"));		
		checkNotNull(req.getCapitoloEntrataGest().getAnnoCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno capitolo"), false);
		checkNotNull(req.getCapitoloEntrataGest().getNumeroCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero capitolo"), false);
		checkNotNull(req.getCapitoloEntrataGest().getNumeroArticolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero articolo"), false);
		checkNotNull(req.getCapitoloEntrataGest().getStatoOperativoElementoDiBilancio(),ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stato"), false);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		capitoloEntrataGestioneDad.setBilancio(req.getBilancio());
		capitoloEntrataGestioneDad.setEnte(req.getEnte());
		capitoloEntrataGestioneDad.setLoginOperazione(loginOperazione);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional
	public AnnullaCapitoloEntrataGestioneResponse executeService(AnnullaCapitoloEntrataGestione serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		if(!verificaAnnullabilitaCapitoloEntrataPrevisione() ) {
			res.setEsito(Esito.FALLIMENTO);
			res.addErrore(ErroreBil.CAPITOLO_CHE_NON_SI_PUO_ANNULLARE.getErrore(""));
			return;
		}
		
		capitoloEntrataGestioneDad.annulla(req.getCapitoloEntrataGest());
		
	}
	
	/**
	 * Verifica annullabilita capitolo entrata previsione.
	 *
	 * @return true, if successful
	 */
	private boolean verificaAnnullabilitaCapitoloEntrataPrevisione() {
		VerificaAnnullabilitaCapitoloEntrataGestione verificaRequest = new VerificaAnnullabilitaCapitoloEntrataGestione();
		verificaRequest.setRichiedente(req.getRichiedente());
		verificaRequest.setEnte(req.getEnte());
		verificaRequest.setBilancio(req.getBilancio());
		verificaRequest.setCapitolo(req.getCapitoloEntrataGest());
		verificaRequest.setDataOra(new Date());
		
		VerificaAnnullabilitaCapitoloEntrataGestioneResponse verificaResponse = executeExternalService(verificaAnnullabilitaCapitoloEntrataGestioneService, verificaRequest);		
		
		
//		if (verificaResponse.isAnnullabilitaCapitolo()) {
//			if(verificaResponse.getCapitolo()!=null){
//				req.getCapitoloEntrataGest().setUid(verificaResponse.getCapitolo().getUid());
//			}
//			return true;
//		}	
//		
//		res.setErrori(verificaResponse.getErrori());
//		return false;	
		
		res.setErrori(verificaResponse.getErrori());
		return verificaResponse.isAnnullabilitaCapitolo();
	}
}
