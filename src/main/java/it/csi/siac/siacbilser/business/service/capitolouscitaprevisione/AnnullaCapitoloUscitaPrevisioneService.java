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
// TODO: Auto-generated Javadoc

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloUscitaPrevisioneDad;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * The Class AnnullaCapitoloUscitaPrevisioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AnnullaCapitoloUscitaPrevisioneService extends CheckedAccountBaseService<AnnullaCapitoloUscitaPrevisione, AnnullaCapitoloUscitaPrevisioneResponse> {
	
	/** The capitolo uscita previsione dad. */
	@Autowired
	private CapitoloUscitaPrevisioneDad capitoloUscitaPrevisioneDad;
	
	/** The verifica annullabilita capitolo uscita previsione service. */
	@Autowired
	private VerificaAnnullabilitaCapitoloUscitaPrevisioneService verificaAnnullabilitaCapitoloUscitaPrevisioneService;
	
	 

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"), false);
		checkNotNull(req.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"));
		checkNotNull(req.getCapitoloUscitaPrev(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitoloUscitaPrevisione"));
		checkCondition(req.getCapitoloUscitaPrev().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitoloUscitaPrev uid"));
	
		//parametri obbligatori per la verifica
		checkCondition(req.getBilancio().getAnno()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno esercizio"), false);
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
	public AnnullaCapitoloUscitaPrevisioneResponse executeService(AnnullaCapitoloUscitaPrevisione serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		if(!isAnnullabileCapitoloUscitaPrevisione() ) {
			res.setEsito(Esito.FALLIMENTO);
			res.addErrore(ErroreBil.CAPITOLO_CHE_NON_SI_PUO_ANNULLARE.getErrore(""));
			return;
		}
		
		capitoloUscitaPrevisioneDad.annulla(req.getCapitoloUscitaPrev());
		
	}
	
	/**
	 * Checks if is annullabile capitolo uscita previsione.
	 *
	 * @return true, if is annullabile capitolo uscita previsione
	 */
	private boolean isAnnullabileCapitoloUscitaPrevisione() {
		VerificaAnnullabilitaCapitoloUscitaPrevisione verificaRequest = new VerificaAnnullabilitaCapitoloUscitaPrevisione();
		verificaRequest.setRichiedente(req.getRichiedente());
		verificaRequest.setEnte(req.getEnte());
		verificaRequest.setBilancio(req.getBilancio());
		verificaRequest.setCapitolo(req.getCapitoloUscitaPrev());
		verificaRequest.setDataOra(new Date());
		
		VerificaAnnullabilitaCapitoloUscitaPrevisioneResponse verificaResponse = executeExternalService(verificaAnnullabilitaCapitoloUscitaPrevisioneService,verificaRequest);
		
//		if (verificaResponse.isAnnullabilitaCapitolo()) {
//			if(verificaResponse.getCapitolo()!=null){
//				req.getCapitoloUscitaPrev().setUid(verificaResponse.getCapitolo().getUid());
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
