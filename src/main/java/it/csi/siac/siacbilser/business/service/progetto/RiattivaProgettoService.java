/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.progetto;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RiattivaProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.RiattivaProgettoResponse;
import it.csi.siac.siacbilser.integration.dad.ProgettoDad;
import it.csi.siac.siacbilser.model.Progetto;
import it.csi.siac.siacbilser.model.StatoOperativoProgetto;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

// TODO: Auto-generated Javadoc
/**
 * The Class RiattivaProgettoService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RiattivaProgettoService extends CheckedAccountBaseService<RiattivaProgetto, RiattivaProgettoResponse> {

/** The progetto. */
private Progetto progetto;
	
	/** The progetto dad. */
	@Autowired
	private ProgettoDad progettoDad;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		progetto = req.getProgetto();
		
		checkNotNull(progetto, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("progetto"));
		checkCondition(progetto.getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid progetto"), false);
		
		checkNotNull(progetto.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente progetto"));
		checkCondition(progetto.getEnte().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente progetto"), false);
	}
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		progettoDad.setEnte(progetto.getEnte());
		progettoDad.setLoginOperazione(loginOperazione);
	}


	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional
	public RiattivaProgettoResponse executeService(RiattivaProgetto serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		// Controllo la coerenza dello stato operativo
		checkStatoOperativoProgettoCoerente();
		
		// Controllo che non vi sia un altro progetto già attivo
		checkAltroProgettoGiaAttivo();
		
		progettoDad.aggiornaStatoOperativoProgetto(progetto, StatoOperativoProgetto.VALIDO);
		progetto.setStatoOperativoProgetto(StatoOperativoProgetto.VALIDO);
		res.setProgetto(progetto);
	}
	
	/**
	 * Controlla che lo stato operativo del Progetto sia coerente con l'intenzione di riattivazione.
	 * <br>
	 * In particolare, il controllo effettuato &eacute; che tale stato non sia gi&agrave; pari a <code>VALIDO</code>.
	 */
	private void checkStatoOperativoProgettoCoerente() {
		StatoOperativoProgetto statoOperativoProgetto = progettoDad.findStatoOperativoByProgetto(progetto);
		if(StatoOperativoProgetto.VALIDO.equals(statoOperativoProgetto)) {
			throw new BusinessException(ErroreCore.TRANSAZIONE_DI_STATO_NON_POSSIBILE.getErrore(), Esito.FALLIMENTO);
		}
	}
	
	/**
	 * Controllo che non vi sia un altro progetto con medesimo codice e stato VALIDO gi&agrave; presente.
	 */
	private void checkAltroProgettoGiaAttivo() {
		// XXX: possibile farlo meglio?
		Progetto temp = progettoDad.findProgettoById(progetto.getUid());
		// Effettuo una ricerca puntuale basandomi sul progetto temporaneo a cui appongo lo stato VALIDO
		temp.setStatoOperativoProgetto(StatoOperativoProgetto.VALIDO);
		
		temp = progettoDad.ricercaPuntualeProgetto(temp);
		if(temp != null) {
			throw new BusinessException(ErroreCore.ENTITA_PRESENTE.getErrore("Progetto", StringUtils.defaultString(temp.getCodice())));
		}
	}

}
