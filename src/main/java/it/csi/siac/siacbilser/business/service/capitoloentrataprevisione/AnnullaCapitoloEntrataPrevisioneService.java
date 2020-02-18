/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitoloentrataprevisione;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloEntrataPrevisioneDad;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

// TODO: Auto-generated Javadoc
/**
 * The Class AnnullaCapitoloEntrataPrevisioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AnnullaCapitoloEntrataPrevisioneService 
	extends CheckedAccountBaseService<AnnullaCapitoloEntrataPrevisione, AnnullaCapitoloEntrataPrevisioneResponse> {

	/** The capitolo entrata previsione dad. */
	@Autowired
	private CapitoloEntrataPrevisioneDad capitoloEntrataPrevisioneDad;
	
	/** The verifica annullabilita capitolo entrata previsione service. */
	@Autowired
	private VerificaAnnullabilitaCapitoloEntrataPrevisioneService verificaAnnullabilitaCapitoloEntrataPrevisioneService;
	
	

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"), false);
		checkNotNull(req.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"));
		checkNotNull(req.getCapitoloEntrataPrev(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitoloEntrataPrevisione"));
		checkNotNull(req.getCapitoloEntrataPrev().getUid(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitoloEntrataPrev uid"));
		
		//parametri obbligatori per la verifica
		checkNotNull(req.getBilancio().getAnno(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno esercizio"), false);
		checkNotNull(req.getCapitoloEntrataPrev().getAnnoCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno capitolo"), false);
		checkNotNull(req.getCapitoloEntrataPrev().getNumeroCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero capitolo"), false);
		checkNotNull(req.getCapitoloEntrataPrev().getNumeroArticolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero articolo"), false);
		checkNotNull(req.getCapitoloEntrataPrev().getStatoOperativoElementoDiBilancio(),ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stato"), false);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		capitoloEntrataPrevisioneDad.setBilancio(req.getBilancio());
		capitoloEntrataPrevisioneDad.setEnte(req.getEnte());
		capitoloEntrataPrevisioneDad.setLoginOperazione(loginOperazione);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional
	public AnnullaCapitoloEntrataPrevisioneResponse executeService(AnnullaCapitoloEntrataPrevisione serviceRequest) {
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
		
		capitoloEntrataPrevisioneDad.annulla(req.getCapitoloEntrataPrev());
		
	}
	
	/**
	 * Verifica annullabilita capitolo entrata previsione.
	 *
	 * @return true, if successful
	 */
	private boolean verificaAnnullabilitaCapitoloEntrataPrevisione() {
		VerificaAnnullabilitaCapitoloEntrataPrevisione verificaRequest = new VerificaAnnullabilitaCapitoloEntrataPrevisione();
		verificaRequest.setRichiedente(req.getRichiedente());
		verificaRequest.setEnte(req.getEnte());
		verificaRequest.setBilancio(req.getBilancio());
		verificaRequest.setCapitolo(req.getCapitoloEntrataPrev());
		verificaRequest.setDataOra(new Date());
		
		VerificaAnnullabilitaCapitoloEntrataPrevisioneResponse verificaResponse = executeExternalService(verificaAnnullabilitaCapitoloEntrataPrevisioneService,verificaRequest);
		
//		if (verificaResponse.isAnnullabilitaCapitolo()) {
//			if(verificaResponse.getCapitolo()!=null){
//				req.getCapitoloEntrataPrev().setUid(verificaResponse.getCapitolo().getUid());
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