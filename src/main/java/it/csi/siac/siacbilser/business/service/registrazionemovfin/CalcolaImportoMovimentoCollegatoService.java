/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.registrazionemovfin;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.service.primanota.movimento.MovimentoHandler;
import it.csi.siac.siacbilser.integration.dad.RegistrazioneMovFinDad;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.frontend.webservice.msg.CalcolaImportoMovimentoCollegato;
import it.csi.siac.siacgenser.frontend.webservice.msg.CalcolaImportoMovimentoCollegatoResponse;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;

/**
 * Calcolo dell'importo del movimento collegato alla registrazione
 * @author Marchino Alessandro
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CalcolaImportoMovimentoCollegatoService extends CheckedAccountBaseService<CalcolaImportoMovimentoCollegato, CalcolaImportoMovimentoCollegatoResponse> {

	// Dads..
	@Autowired
	private RegistrazioneMovFinDad registrazioneMovFinDad;
	
	private RegistrazioneMovFin registrazioneMovFin;

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getRegistrazioneMovFin(), "registrazione");
	}

	@Override
	@Transactional
	public CalcolaImportoMovimentoCollegatoResponse executeService(CalcolaImportoMovimentoCollegato serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		registrazioneMovFin = registrazioneMovFinDad.findRegistrazioneMovFinByIdBase(req.getRegistrazioneMovFin().getUid());
		
		MovimentoHandler<? extends Entita> movimentoHandler = inizializzaMovimentoHandler();
		BigDecimal importoMovimento = movimentoHandler.getImportoMovimento(registrazioneMovFin);
		if(importoMovimento == null){
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("impossibile ottenere l'importo del movimento finanziario."));
		}
		res.setImporto(importoMovimento);
	}

	/**
	 * Inizializzazione dell'handler
	 * @return l'handler
	 */
	private MovimentoHandler<? extends Entita> inizializzaMovimentoHandler() {
		
		MovimentoHandler<? extends Entita> movimentoHandler = MovimentoHandler.getInstance(serviceExecutor,
				req.getRichiedente(),
				ente,
				registrazioneMovFin.getBilancio(),
				registrazioneMovFin.getMovimento().getClass(),
				registrazioneMovFin.getMovimentoCollegato() != null ? registrazioneMovFin.getMovimentoCollegato().getClass() : null);
		
		movimentoHandler.caricaMovimento(registrazioneMovFin);
		
		List<RegistrazioneMovFin> registrazioniMovFin = new ArrayList<RegistrazioneMovFin>();
		registrazioniMovFin.add(registrazioneMovFin);
		movimentoHandler.inizializzaDatiMovimenti(registrazioniMovFin);
		
		return movimentoHandler;
	}

}
