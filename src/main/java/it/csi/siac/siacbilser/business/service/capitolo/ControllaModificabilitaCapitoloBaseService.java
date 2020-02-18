/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitolo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaModificabilitaCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaModificabilitaCapitoloResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloUscitaPrevisioneDad;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;


/**
 * The Class ControllaModificabilitaCapitoloBaseService.
 *
 * @param <CREQ> the generic type
 * @param <CRES> the generic type
 * 
 * @author Domenico
 */
public abstract class ControllaModificabilitaCapitoloBaseService<CREQ extends ControllaModificabilitaCapitolo, CRES extends ControllaModificabilitaCapitoloResponse> extends CheckedAccountBaseService<CREQ, CRES> {
	
	/** The capitolo dad. */
	@Autowired
	protected CapitoloUscitaPrevisioneDad capitoloDad;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		capitoloDad.setBilancio(req.getBilancio());
		capitoloDad.setEnte(req.getEnte());
		capitoloDad.setLoginOperazione(loginOperazione);
	}
	
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));	
		checkCondition(req.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente uid"), false);	
		
//		checkNotNull(req.getNumeroCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero capitolo"), false);
//		checkNotNull(req.getNumeroArticolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero articolo"), false);
//		checkNotNull(req.getNumeroUEB(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero ueb")); //potrebbe essere facoltativo!
		
		checkNotNull(req.getTipoCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo capitolo"), false);
		
		checkNotNull(req.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"));
		checkCondition(req.getBilancio().getAnno()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno bilancio"),false);	
	}
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public CRES executeService(CREQ serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	protected void execute() {
				
		Long stessoNumCap = capitoloDad.countCapitoli(req.getTipoCapitolo(), req.getNumeroCapitolo(), null, null);
		res.setStessoNumCap(stessoNumCap);
		
		Long stessoNumCapArt = capitoloDad.countCapitoli(req.getTipoCapitolo(), req.getNumeroCapitolo(), req.getNumeroArticolo(), null);
		res.setStessoNumCapArt(stessoNumCapArt);
		
		if(stessoNumCap==0 || (req.isModalitaAggiornamento() && stessoNumCap==1)){ 
			//Numero capitolo non esiste
			//Non ci sono classificatori NON modificabili da settare.
			caricaLegamiANull();
			return;
		}
		
		if(stessoNumCapArt==0 || (req.isModalitaAggiornamento() && stessoNumCapArt==1)){ //Esiste solo numero capitolo uguale -> NON sono modficabili i classificatori legati al numero capiotolo
			caricaLegamiACapitolo();
			return;
		}
		
		if(req.getNumeroUEB()!=null && !req.isModalitaAggiornamento()){ //solo se sono in modalità inserimento controlo che il capitolo non esiste già
			Long stessoNumCapArtUeb = capitoloDad.countCapitoli(req.getTipoCapitolo(), req.getNumeroCapitolo(), req.getNumeroArticolo(), req.getNumeroUEB());
			if(stessoNumCapArtUeb >0){
				//Esiste numero capitolo, numero articolo e numero ueb uguale -> condizione di capitolo già presente. 
				throw new BusinessException(ErroreBil.CAPITOLO_GIA_PRESENTE.getErrore());
			}
		}
		
		caricaLegamiACapitoloArticolo();
	}

	/**
	 * Non ci sono legami da gestire in quanto il capitolo con quel numero non esiste. Implementazione vuota: può essere sovrascritta.
	 */
	public void caricaLegamiANull() {
		
	}


	/**
	 * Carica i legami nel caso in cui esista almeno un capitolo con solo lo stesso numero.
	 */
	public abstract void caricaLegamiACapitolo();

	
	/**
	 * Carica i legami nel caso in cui esista almeno un capitolo con lo stesso numero ed articolo.
	 */
	public abstract void caricaLegamiACapitoloArticolo();
	

}
