/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitolouscitaprevisione;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaEliminabilitaCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaEliminabilitaCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloUscitaPrevisioneDad;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

// TODO: Auto-generated Javadoc
/**
 * The Class EliminaCapitoloUscitaPrevisioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EliminaCapitoloUscitaPrevisioneService 
	extends CheckedAccountBaseService<EliminaCapitoloUscitaPrevisione, EliminaCapitoloUscitaPrevisioneResponse> {

	/** The capitolo uscita previsione dad. */
	@Autowired
	private CapitoloUscitaPrevisioneDad capitoloUscitaPrevisioneDad;
	
	/** The verifica eliminabilita capitolo uscita previsione service. */
	@Autowired
	private VerificaEliminabilitaCapitoloUscitaPrevisioneService verificaEliminabilitaCapitoloUscitaPrevisioneService;
	
	 
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"), false);
		checkNotNull(req.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"));
		checkNotNull(req.getCapitoloUscitaPrev(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitoloUscitaPrevisione"));
		checkNotNull(req.getCapitoloUscitaPrev().getUid(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitoloUscitaPrevisione uid"));
	
		//parametri obbligatori per la verifica
		checkNotNull(req.getBilancio().getAnno(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno esercizio"), false);
		checkNotNull(req.getCapitoloUscitaPrev().getAnnoCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno capitolo"), false);
		checkNotNull(req.getCapitoloUscitaPrev().getNumeroCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero capitolo"), false);
		checkNotNull(req.getCapitoloUscitaPrev().getNumeroArticolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero articolo"), false);
		checkNotNull(req.getCapitoloUscitaPrev().getStatoOperativoElementoDiBilancio(),ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stato"), false);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		capitoloUscitaPrevisioneDad.setBilancio(req.getBilancio());
		capitoloUscitaPrevisioneDad.setEnte(req.getEnte());
		capitoloUscitaPrevisioneDad.setLoginOperazione(loginOperazione);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional
	public EliminaCapitoloUscitaPrevisioneResponse executeService(EliminaCapitoloUscitaPrevisione serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		if(!isEliminabileCapitoloUscitaPrevisione()) {
			res.setEsito(Esito.FALLIMENTO);
			res.addErrore(ErroreBil.CAPITOLO_NON_ELIMINABILE.getErrore("Fallimento verifica."));
			return;
		}
		
		capitoloUscitaPrevisioneDad.elimina(req.getCapitoloUscitaPrev());
		
	}
	
	/**
	 * Checks if is eliminabile capitolo uscita previsione.
	 *
	 * @return true, if is eliminabile capitolo uscita previsione
	 */
	private boolean isEliminabileCapitoloUscitaPrevisione() {
		VerificaEliminabilitaCapitoloUscitaPrevisione verificaRequest = new VerificaEliminabilitaCapitoloUscitaPrevisione();
		verificaRequest.setRichiedente(req.getRichiedente());
		verificaRequest.setEnte(req.getEnte());
		verificaRequest.setBilancio(req.getBilancio());
		verificaRequest.setCapitoloUscitaPrev(req.getCapitoloUscitaPrev());
		verificaRequest.setDataOra(new Date());
		
		
		VerificaEliminabilitaCapitoloUscitaPrevisioneResponse verificaResponse = executeExternalService(verificaEliminabilitaCapitoloUscitaPrevisioneService,verificaRequest);
		
//		if (verificaResponse.isEliminabilitaCapitolo()) {
//			if(verificaResponse.getCapitolo()!=null){
//				req.getCapitoloUscitaPrev().setUid(verificaResponse.getCapitolo().getUid());
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
