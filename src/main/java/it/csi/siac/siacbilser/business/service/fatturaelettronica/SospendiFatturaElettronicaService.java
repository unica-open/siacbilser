/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.fatturaelettronica;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.integration.dad.FatturaFELDad;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.FaseBilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.sirfelser.frontend.webservice.msg.SospendiFatturaElettronica;
import it.csi.siac.sirfelser.frontend.webservice.msg.SospendiFatturaElettronicaResponse;
import it.csi.siac.sirfelser.model.FatturaFEL;
import it.csi.siac.sirfelser.model.StatoAcquisizioneFEL;

/**
 * Sospensione di una fattura elettronica
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SospendiFatturaElettronicaService extends CheckedAccountBaseService<SospendiFatturaElettronica, SospendiFatturaElettronicaResponse> {
	
	private FatturaFEL fatturaFEL;
	
	@Autowired 
	private FatturaFELDad fatturaFELDad;
	@Autowired
	private BilancioDad bilancioDad;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getFatturaFEL(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("fattura"));
		fatturaFEL = req .getFatturaFEL();
		
		checkNotNull(fatturaFEL.getIdFattura(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("id fattura"), false);
		checkEntita(req.getBilancio(), "bilancio", false);
	}
	
	@Override
	protected void init() {
		fatturaFELDad.setEnte(ente);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional(readOnly=true)
	public SospendiFatturaElettronicaResponse executeService(SospendiFatturaElettronica serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		checkFaseOperativaCompatibile();
		checkStatoFattura();
		fatturaFELDad.aggiornaNote(fatturaFEL);
		fatturaFELDad.aggiornaStatoFattura(fatturaFEL, StatoAcquisizioneFEL.SOSPESA);
		fatturaFEL.setStatoAcquisizioneFEL(StatoAcquisizioneFEL.SOSPESA);
		res.setFatturaFEL(fatturaFEL);
	}

	/**
	 * Controlla che la fase operativa del bilancio sia compatibile con l'operazione
	 */
	private void checkFaseOperativaCompatibile() {
		Bilancio bilancio = bilancioDad.getBilancioByUid(req.getBilancio().getUid());
		if(bilancio == null) {
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("Bilancio", "uid " + req.getBilancio().getUid()));
		}
		if(bilancio.getFaseEStatoAttualeBilancio() == null || bilancio.getFaseEStatoAttualeBilancio().getFaseBilancio() == null) {
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("Fase di bilancio", "corrispondente al bilancio con uid " + bilancio.getUid()));
		}
		FaseBilancio faseBilancio = bilancio.getFaseEStatoAttualeBilancio().getFaseBilancio();
		// Controllo che il bilancio sia in fase ESERCIZIO_PROVVISORIO, GESTIONE, ASSESTAMENTO, PREDISPOSIZIONE_CONSUNTIVO
		boolean faseBilancioCompatibile = FaseBilancio.ESERCIZIO_PROVVISORIO.equals(faseBilancio)
				|| FaseBilancio.GESTIONE.equals(faseBilancio)
				|| FaseBilancio.ASSESTAMENTO.equals(faseBilancio)
				|| FaseBilancio.PREDISPOSIZIONE_CONSUNTIVO.equals(faseBilancio);
		if(!faseBilancioCompatibile) {
			throw new BusinessException(ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("Bilancio", faseBilancio.name()).getTesto());
		}
	}

	/**
	 * Lo stato della fattura 
	 */
	private void checkStatoFattura() {
		StatoAcquisizioneFEL statoAcquisizioneFEL = fatturaFELDad.ricercaStatoFattura(fatturaFEL.getIdFattura());
		if(!StatoAcquisizioneFEL.DA_ACQUISIRE.equals(statoAcquisizioneFEL)) {
			throw new BusinessException(ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("Fattura ", statoAcquisizioneFEL.getDescrizione()).getTesto());
		}
	}
	
}
