/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.causale;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.integration.dad.CausaleEPDad;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaMinimaCausale;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaMinimaCausaleResponse;
import it.csi.siac.siacgenser.model.CausaleEP;

/**
 * Ricerca sintetica di una Causale
 * 
 * @author Domenico
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaMinimaCausaleService extends CheckedAccountBaseService<RicercaMinimaCausale, RicercaMinimaCausaleResponse> {
	
	//DADs
	@Autowired
	private CausaleEPDad causaleEPDad;
	@Autowired
	private BilancioDad bilancioDad;
	
	
	private Bilancio bilancio;

	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getCausaleEP(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("CausaleEP"));
		checkNotNull(req.getCausaleEP().getAmbito(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Ambito causaleEP"));
		checkNotNull(req.getCausaleEP().getStatoOperativoCausaleEP(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Stato operativo causaleEP"));
		checkNotNull(req.getCausaleEP().getTipoCausale(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Tipo causaleEP"));
		checkEntita(req.getBilancio(), "bilancio", false);
		checkCondition(req.getCausaleEP().getEventi() != null && !req.getCausaleEP().getEventi().isEmpty(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Evento causaleEP"));
		checkEntita(req.getCausaleEP().getEventi().get(0), "Evento causaleEP");
		
		req.getCausaleEP().setEnte(ente);
		
		this.bilancio = req.getBilancio();
	}
	
	
	@Override
	@Transactional(readOnly=true)
	public RicercaMinimaCausaleResponse executeService(RicercaMinimaCausale serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	
	@Override
	protected void init() {
		super.init();
		causaleEPDad.setEnte(ente);
		causaleEPDad.setLoginOperazione(loginOperazione);
		
		bilancioDad.setEnteEntity(ente);
	}
	
	@Override
	protected void execute() {
		caricaBilancio();
		
		Date inizioAnno = Utility.primoGiornoDellAnno(bilancio.getAnno());
		req.getCausaleEP().setDataInizioValiditaFiltro(inizioAnno);
		
		List<CausaleEP> causaliEP = causaleEPDad.ricercaMinimaCausaleEP(req.getCausaleEP());
		res.setCausali(causaliEP);
		res.setCardinalitaComplessiva(causaliEP.size());
		
	}
	
	protected void caricaBilancio() {
		Bilancio bil = bilancioDad.getBilancioByUid(bilancio.getUid());
		if(bil == null){
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("Bilancio", "uid: "+ bilancio.getUid()));
		}
		this.bilancio = bil;
	}

}
