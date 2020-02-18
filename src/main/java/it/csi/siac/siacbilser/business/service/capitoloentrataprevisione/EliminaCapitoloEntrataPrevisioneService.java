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
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaEliminabilitaCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaEliminabilitaCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloEntrataPrevisioneDad;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

// TODO: Auto-generated Javadoc
/**
 * The Class EliminaCapitoloEntrataPrevisioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EliminaCapitoloEntrataPrevisioneService 
	extends CheckedAccountBaseService<EliminaCapitoloEntrataPrevisione, EliminaCapitoloEntrataPrevisioneResponse> {

	/** The capitolo entrata previsione dad. */
	@Autowired
	private CapitoloEntrataPrevisioneDad capitoloEntrataPrevisioneDad;
	
	/** The verifica eliminabilita capitolo entrata previsione service. */
	@Autowired
	private VerificaEliminabilitaCapitoloEntrataPrevisioneService verificaEliminabilitaCapitoloEntrataPrevisioneService;
	
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"), false);
		checkNotNull(req.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"));
		checkNotNull(req.getCapitoloEntrataPrev(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitoloEntrataPrevisione"));	
		checkNotNull(req.getCapitoloEntrataPrev().getUid(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitoloEntrataPrevisione uid"));	
		
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
	public EliminaCapitoloEntrataPrevisioneResponse executeService(EliminaCapitoloEntrataPrevisione serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		if(!verificaEliminabilitaCapitoloEntrataPrevisione() ) {
			res.setEsito(Esito.FALLIMENTO);
			res.addErrore(ErroreBil.CAPITOLO_NON_ELIMINABILE.getErrore("Fallimento verifica."));
			return;
		}	
		capitoloEntrataPrevisioneDad.elimina(req.getCapitoloEntrataPrev());		
	}
	
	/**
	 * Verifica eliminabilita capitolo entrata previsione.
	 *
	 * @return true, if successful
	 */
	private boolean verificaEliminabilitaCapitoloEntrataPrevisione() {
		VerificaEliminabilitaCapitoloEntrataPrevisione verificaRequest = new VerificaEliminabilitaCapitoloEntrataPrevisione();
		verificaRequest.setRichiedente(req.getRichiedente());
		verificaRequest.setEnte(req.getEnte());
		verificaRequest.setBilancio(req.getBilancio());
		verificaRequest.setCapitoloEntrataPrev(req.getCapitoloEntrataPrev());
		verificaRequest.setDataOra(new Date());
		
		VerificaEliminabilitaCapitoloEntrataPrevisioneResponse verificaResponse = executeExternalService(verificaEliminabilitaCapitoloEntrataPrevisioneService,verificaRequest);
		
//		if (verificaResponse.isEliminabilitaCapitolo()) {
//			if(verificaResponse.getCapitolo()!=null){
//				req.getCapitoloEntrataPrev().setUid(verificaResponse.getCapitolo().getUid());
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
