/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.mutuo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.InserisciAssociazioneMovimentiGestioneMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.InserisciAssociazioneMovimentiGestioneMutuoResponse;
import it.csi.siac.siacbilser.integration.dad.MovimentoGestioneBilDad;
import it.csi.siac.siacbilser.model.StatoOperativoMovimentoGestione;
import it.csi.siac.siacbilser.model.movimentogestione.MovimentoGestioneModelDetail;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.model.MovimentoGestione;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisciAssociazioneMovimentiGestioneMutuoService extends BaseInserisciEliminaAssociazioneMovimentiGestioneMutuoService<InserisciAssociazioneMovimentiGestioneMutuo, InserisciAssociazioneMovimentiGestioneMutuoResponse> {
	
	protected @Autowired MovimentoGestioneBilDad<?> movimentoGestioneBilDad;
	
	@Override
	@Transactional
	public InserisciAssociazioneMovimentiGestioneMutuoResponse executeService(InserisciAssociazioneMovimentiGestioneMutuo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		
		super.execute();
		
		MovimentoGestione movimentoGestione; 
		for (int movimentoGestioneId : req.getElencoIdMovimentiGestione()) {
			
			movimentoGestione = new MovimentoGestione();
			movimentoGestione.setUid(movimentoGestioneId);
			movimentoGestione = movimentoGestioneBilDad.ricercaMovimentoGestione(movimentoGestione, MovimentoGestioneModelDetail.Stato);
			
			checkStatoOperativoMovimentoGestione(movimentoGestione);
			checkBusinessCondition(!mutuoDad.esisteAssociazioneMutuoMovimentoGestione(mutuo, movimentoGestione), 
					ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Il movimento "+identificativoMovimentoGestione(movimentoGestione)+" e' gia' associato al mutuo"));
 
			mutuoDad.createAssociazioneMovimentoGestioneMutuo(mutuo, movimentoGestioneId);
		}
	}

	private void checkStatoOperativoMovimentoGestione(MovimentoGestione movimentoGestione) {
		checkBusinessCondition(StatoOperativoMovimentoGestione.DEFINITIVO.equals(movimentoGestione.getStatoOperativo()), 
				ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("il movimento "+identificativoMovimentoGestione(movimentoGestione), movimentoGestione.getStatoOperativo().getCodice()));
	}

	private String identificativoMovimentoGestione(MovimentoGestione movimentoGestione) {
		return movimentoGestione.getAnnoMovimento()+"/"+movimentoGestione.getNumeroBigDecimal();
		
	}
}