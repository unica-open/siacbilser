/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitolouscitagestione;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaEliminabilitaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaEliminabilitaCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloUscitaGestioneDad;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

// TODO: Auto-generated Javadoc
/**
 * The Class EliminaCapitoloUscitaGestioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EliminaCapitoloUscitaGestioneService 
	extends CheckedAccountBaseService<EliminaCapitoloUscitaGestione, EliminaCapitoloUscitaGestioneResponse> {

	/** The capitolo uscita gestione dad. */
	@Autowired
	private CapitoloUscitaGestioneDad capitoloUscitaGestioneDad;
	
	/** The verifica eliminabilita capitolo uscita gestione service. */
	@Autowired
	private VerificaEliminabilitaCapitoloUscitaGestioneService verificaEliminabilitaCapitoloUscitaGestioneService;
	
	 
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"), false);
		checkNotNull(req.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"));
		checkNotNull(req.getCapitoloUscitaGest(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitoloUscitaGestione"));
		checkNotNull(req.getCapitoloUscitaGest().getUid(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitoloUscitaGestione uid"));
		
		//parametri obbligatori per la verifica
		checkNotNull(req.getBilancio().getAnno(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno esercizio"), false);
		checkNotNull(req.getCapitoloUscitaGest().getAnnoCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno capitolo"), false);
		checkNotNull(req.getCapitoloUscitaGest().getNumeroCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero capitolo"), false);
		checkNotNull(req.getCapitoloUscitaGest().getNumeroArticolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero articolo"), false);
		checkNotNull(req.getCapitoloUscitaGest().getStatoOperativoElementoDiBilancio(),ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stato"), false);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		capitoloUscitaGestioneDad.setBilancio(req.getBilancio());
		capitoloUscitaGestioneDad.setEnte(req.getEnte());
		capitoloUscitaGestioneDad.setLoginOperazione(loginOperazione);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional
	public EliminaCapitoloUscitaGestioneResponse executeService(EliminaCapitoloUscitaGestione serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		if(!verificaEliminabilitaCapitoloUscitaGestione() ) {
			res.setEsito(Esito.FALLIMENTO);
			res.addErrore(ErroreBil.CAPITOLO_NON_ELIMINABILE.getErrore("Fallimento verifica."));
			return;
		}
		
		capitoloUscitaGestioneDad.elimina(req.getCapitoloUscitaGest());
		
	}
	
	/**
	 * Verifica eliminabilita capitolo uscita gestione.
	 *
	 * @return true, if successful
	 */
	private boolean verificaEliminabilitaCapitoloUscitaGestione() {
		VerificaEliminabilitaCapitoloUscitaGestione verificaRequest = new VerificaEliminabilitaCapitoloUscitaGestione();
		verificaRequest.setRichiedente(req.getRichiedente());
		verificaRequest.setEnte(req.getEnte());
		verificaRequest.setBilancio(req.getBilancio());
		verificaRequest.setCapitoloUscitaGest(req.getCapitoloUscitaGest());
		verificaRequest.setDataOra(new Date());
		
		VerificaEliminabilitaCapitoloUscitaGestioneResponse verificaResponse = executeExternalService(verificaEliminabilitaCapitoloUscitaGestioneService,verificaRequest);
		
//		if (verificaResponse.isEliminabilitaCapitolo()) {
//			if(verificaResponse.getCapitolo()!=null){
//				req.getCapitoloUscitaGest().setUid(verificaResponse.getCapitolo().getUid());
//			}
//			return true;
//		}
//		
//		res.setErrori(verificaResponse.getErrori());
//		return false;	
		
		res.setErrori(verificaResponse.getErrori());
		return verificaResponse.isEliminabilitaCapitolo();
	}
}
