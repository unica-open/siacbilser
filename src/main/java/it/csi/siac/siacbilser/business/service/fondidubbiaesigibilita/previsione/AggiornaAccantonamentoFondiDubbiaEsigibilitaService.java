/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.previsione;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.previsione.AggiornaAccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.previsione.AggiornaAccantonamentoFondiDubbiaEsigibilitaResponse;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaBase;
import it.csi.siac.siacbilser.model.fcde.modeldetail.AccantonamentoFondiDubbiaEsigibilitaModelDetail;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;

/**
 * Inserimento dei fondi a dubbia e difficile esazione
 * 
 * @author Marchino Alessandro
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaAccantonamentoFondiDubbiaEsigibilitaService extends BaseCUDAccantonamentoFondiDubbiaEsigibilitaService<AggiornaAccantonamentoFondiDubbiaEsigibilita, AggiornaAccantonamentoFondiDubbiaEsigibilitaResponse> {
	
	@Override
	@Transactional
	public AggiornaAccantonamentoFondiDubbiaEsigibilitaResponse executeService(AggiornaAccantonamentoFondiDubbiaEsigibilita serviceRequest) {
		//eseguo il servizio
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		// SIAC-8356 se la lista e' vuota si ritorna un warning
//		checkNotNull(req.getListaAccantonamentoFondiDubbiaEsigibilita(), "lista accantonamenti");
//		checkCondition(!req.getListaAccantonamentoFondiDubbiaEsigibilita().isEmpty(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("lista accantonamenti"));
		int i = 1;
		for(AccantonamentoFondiDubbiaEsigibilita afde : req.getListaAccantonamentoFondiDubbiaEsigibilita()) {
			checkEntita(afde, "accantonamento numero " + i++);
		}
	}
	
	@Override
	protected void execute() {
		// SIAC-8356 se la lista e' vuota si ricalcolano gli accantonamenti
		if(CollectionUtils.isEmpty(req.getListaAccantonamentoFondiDubbiaEsigibilita())) {
			popolaResponseConAccantonamenti();
			// lo setto per operare un distinguo successivamente
			res.setEsito(Esito.FALLIMENTO);
			return;
		}
		
		// Lettura bilancio di un accantonamento
		loadAttributiBilancio(req.getListaAccantonamentoFondiDubbiaEsigibilita().get(0));
		
		Utility.MDTL.addModelDetails(
			AccantonamentoFondiDubbiaEsigibilitaModelDetail.CapitoloEntrataPrevisione,
			AccantonamentoFondiDubbiaEsigibilitaModelDetail.TipoMedia,
			AccantonamentoFondiDubbiaEsigibilitaModelDetail.Tipo,
			AccantonamentoFondiDubbiaEsigibilitaModelDetail.AttributiBilancio);

		for(AccantonamentoFondiDubbiaEsigibilita afde : req.getListaAccantonamentoFondiDubbiaEsigibilita()) {
			// Caricamento dati salvati
			AccantonamentoFondiDubbiaEsigibilita accantonamentoCurrent = accantonamentoFondiDubbiaEsigibilitaDad.findById(afde);
			// Ripopolamento dell'accantonamento
			mergeDatiAccantonamento(accantonamentoCurrent, afde);
			accantonamentoFondiDubbiaEsigibilitaDad.update(accantonamentoCurrent);
		}
		// popolaResponse
		popolaResponseConAccantonamenti();
	}
	
	private void popolaResponseConAccantonamenti() {
		List<AccantonamentoFondiDubbiaEsigibilitaBase<?>> accantonamentiFondiDubbiaEsigibilita = caricaAccantonamenti(accantonamentoFondiDubbiaEsigibilitaAttributiBilancio);
		res.setListaAccantonamenti(accantonamentiFondiDubbiaEsigibilita);
	}
	
	/**
	 * Merge dei dati dell'accantonamento.
	 * Visto che alcuni dati sono calcolati ma comunque persistiti per motivi di performance, si effettua un merge dei dati modificabili e si ricalcolano quelli non modificabili
	 * @param current l'accantonamento corrente
	 * @param afde l'accantonamento fornito dal chiamante
	 */
	private void mergeDatiAccantonamento(AccantonamentoFondiDubbiaEsigibilita current, AccantonamentoFondiDubbiaEsigibilita afde) {
		boolean riscossioneVirtuosa = current.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().getRiscossioneVirtuosa();
		
		// Dati modificabili dall'utente
		current.setNumeratore(afde.getNumeratore());
		current.setNumeratore1(afde.getNumeratore1());
		current.setNumeratore2(afde.getNumeratore2());
		current.setNumeratore3(afde.getNumeratore3());
		current.setNumeratore4(afde.getNumeratore4());
		
		current.setDenominatore(afde.getDenominatore());
		current.setDenominatore1(afde.getDenominatore1());
		current.setDenominatore2(afde.getDenominatore2());
		current.setDenominatore3(afde.getDenominatore3());
		current.setDenominatore4(afde.getDenominatore4());

		current.setMediaUtente(afde.getMediaUtente());
		current.setTipoMediaPrescelta(afde.getTipoMediaPrescelta() != null ? afde.getTipoMediaPrescelta() : current.getTipoMediaPrescelta());
		
		// Dati calcolati
		current.setPercentualeAccantonamentoFondi(calcolaPercentuale(current.getNumeratore(), current.getDenominatore()));
		current.setPercentualeAccantonamentoFondi1(calcolaPercentuale(current.getNumeratore1(), current.getDenominatore1()));
		current.setPercentualeAccantonamentoFondi2(calcolaPercentuale(current.getNumeratore2(), current.getDenominatore2()));
		current.setPercentualeAccantonamentoFondi3(calcolaPercentuale(current.getNumeratore3(), current.getDenominatore3()));
		current.setPercentualeAccantonamentoFondi4(calcolaPercentuale(current.getNumeratore4(), current.getDenominatore4()));
		
		current.setMediaSempliceTotali(calcolaMediaSempliceTotali(current, riscossioneVirtuosa));
		current.setMediaSempliceRapporti(calcolaMediaSempliceRapporti(current, riscossioneVirtuosa));
		//SIAC-8519 si sbiancano sempre le medie ponderate per la gestione
		current.setMediaPonderataTotali(null);
		current.setMediaPonderataRapporti(null);
		current.setNote(calcolaNote(current));
	}
	
}
