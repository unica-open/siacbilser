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

import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.InserisciAssociazioneProgettiMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.InserisciAssociazioneProgettiMutuoResponse;
import it.csi.siac.siacbilser.integration.dad.ProgettoDad;
import it.csi.siac.siacbilser.model.Progetto;
import it.csi.siac.siacbilser.model.StatoOperativoProgetto;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.model.ProgettoModelDetail;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisciAssociazioneProgettiMutuoService extends BaseInserisciEliminaAssociazioneProgettiMutuoService<InserisciAssociazioneProgettiMutuo, InserisciAssociazioneProgettiMutuoResponse> {
	
	protected @Autowired ProgettoDad progettoDad;
	
	@Override
	@Transactional
	public InserisciAssociazioneProgettiMutuoResponse executeService(InserisciAssociazioneProgettiMutuo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		
		super.execute();
		
		Progetto progetto; 
		for (int progettoId : req.getElencoIdProgetti()) {
			
			progetto = new Progetto();
			progetto.setUid(progettoId);
			
			progetto = progettoDad.ricercaProgetto(progetto, ProgettoModelDetail.Stato);
			checkStatoOperativoProgetto(progetto);
			
			checkBusinessCondition(!mutuoDad.esisteAssociazioneMutuoProgetto(mutuo, progetto), 
					ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Il progetto "+progetto.getCodice()+" e' gia' associato al mutuo"));
 
			mutuoDad.createAssociazioneProgettoMutuo(mutuo, progettoId);
		}
	}

	private void checkStatoOperativoProgetto(Progetto progetto) {
		checkBusinessCondition(!StatoOperativoProgetto.ANNULLATO.equals(progetto.getStatoOperativoProgetto()), 
				ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("il progetto "+progetto.getCodice(), progetto.getStatoOperativoProgetto().getCodice()));
	}
}