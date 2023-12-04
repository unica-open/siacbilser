/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.rendiconto;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.rendiconto.AggiornaAccantonamentoFondiDubbiaEsigibilitaRendiconto;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.rendiconto.AggiornaAccantonamentoFondiDubbiaEsigibilitaRendicontoResponse;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaBase;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaRendiconto;
import it.csi.siac.siacbilser.model.fcde.modeldetail.AccantonamentoFondiDubbiaEsigibilitaRendicontoModelDetail;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;

/**
 * Inserimento dei fondi a dubbia e difficile esazione
 * 
 * @author Marchino Alessandro
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaAccantonamentoFondiDubbiaEsigibilitaRendicontoService extends BaseCUDAccantonamentoFondiDubbiaEsigibilitaRendicontoService<AggiornaAccantonamentoFondiDubbiaEsigibilitaRendiconto, AggiornaAccantonamentoFondiDubbiaEsigibilitaRendicontoResponse> {
	
	@Override
	@Transactional
	public AggiornaAccantonamentoFondiDubbiaEsigibilitaRendicontoResponse executeService(AggiornaAccantonamentoFondiDubbiaEsigibilitaRendiconto serviceRequest) {
		//eseguo il servizio
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		// SIAC-8356 se la lista e' vuota si ritorna un warning
//		checkNotNull(req.getListaAccantonamentoFondiDubbiaEsigibilitaRendiconto(), "lista accantonamenti");
//		checkCondition(!req.getListaAccantonamentoFondiDubbiaEsigibilitaRendiconto().isEmpty(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("lista accantonamenti"));
		if(CollectionUtils.isNotEmpty(req.getListaAccantonamentoFondiDubbiaEsigibilitaRendiconto())) {
			int i = 1;
			for(AccantonamentoFondiDubbiaEsigibilitaRendiconto afde : req.getListaAccantonamentoFondiDubbiaEsigibilitaRendiconto()) {
				checkEntita(afde, "accantonamento numero " + i++);
			}
		}
	}
	
	@Override
	protected void execute() {
		// SIAC-8356 se la lista e' vuota si ricalcolano gli accantonamenti
		if(CollectionUtils.isEmpty(req.getListaAccantonamentoFondiDubbiaEsigibilitaRendiconto())) {
			popolaResponseConAccantonamenti();
			// lo setto per operare un distinguo successivamente
			res.setEsito(Esito.FALLIMENTO);
			return;
		}
		
		// Lettura bilancio di un accantonamento
		loadAttributiBilancio(req.getListaAccantonamentoFondiDubbiaEsigibilitaRendiconto().get(0));
		
		Utility.MDTL.addModelDetails(
			AccantonamentoFondiDubbiaEsigibilitaRendicontoModelDetail.CapitoloEntrataGestione,
			AccantonamentoFondiDubbiaEsigibilitaRendicontoModelDetail.TipoMedia,
			AccantonamentoFondiDubbiaEsigibilitaRendicontoModelDetail.Tipo,
			AccantonamentoFondiDubbiaEsigibilitaRendicontoModelDetail.AttributiBilancio);

		for(AccantonamentoFondiDubbiaEsigibilitaRendiconto afde : req.getListaAccantonamentoFondiDubbiaEsigibilitaRendiconto()) {
			// Caricamento dati salvati
			AccantonamentoFondiDubbiaEsigibilitaRendiconto accantonamentoCurrent = accantonamentoFondiDubbiaEsigibilitaRendicontoDad.findById(afde);
			// Ripopolamento dell'accantonamento
			mergeDatiAccantonamento(accantonamentoCurrent, afde);
			accantonamentoFondiDubbiaEsigibilitaRendicontoDad.update(accantonamentoCurrent);
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
	private void mergeDatiAccantonamento(AccantonamentoFondiDubbiaEsigibilitaRendiconto current, AccantonamentoFondiDubbiaEsigibilitaRendiconto afde) {
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
		current.setTipoMediaPrescelta(afde.getTipoMediaPrescelta());
		
		// Dati calcolati
		current.setPercentualeAccantonamentoFondi(calcolaPercentuale(current.getNumeratore(), current.getDenominatore()));
		current.setPercentualeAccantonamentoFondi1(calcolaPercentuale(current.getNumeratore1(), current.getDenominatore1()));
		current.setPercentualeAccantonamentoFondi2(calcolaPercentuale(current.getNumeratore2(), current.getDenominatore2()));
		current.setPercentualeAccantonamentoFondi3(calcolaPercentuale(current.getNumeratore3(), current.getDenominatore3()));
		current.setPercentualeAccantonamentoFondi4(calcolaPercentuale(current.getNumeratore4(), current.getDenominatore4()));
		
		current.setMediaSempliceTotali(calcolaMediaSempliceTotali(current, riscossioneVirtuosa));
		current.setMediaSempliceRapporti(calcolaMediaSempliceRapporti(current, riscossioneVirtuosa));
		current.setMediaPonderataTotali(calcolaMediaPonderataTotali(current, riscossioneVirtuosa));
		current.setMediaPonderataRapporti(calcolaMediaPonderataRapporti(current, riscossioneVirtuosa));
		current.setNote(calcolaNote(current));
		
		//SIAC-8560
		impostaAccantonamento(current, afde);
	}
	
	private void impostaAccantonamento(AccantonamentoFondiDubbiaEsigibilitaRendiconto current, AccantonamentoFondiDubbiaEsigibilitaRendiconto afde) {
		if(afde == null) return;
		
		BigDecimal residuoCapitolo = afde.getResiduoFinaleCapitolo() == null ? BigDecimal.ZERO : afde.getResiduoFinaleCapitolo();
		
		switch (current.getTipoMediaPrescelta()) {
			case SEMPLICE_TOTALI:
				current.setAccantonamento(calcolaAccantonamentoEffettivo(residuoCapitolo, current.getMediaSempliceTotali() == null ? BigDecimal.ZERO : current.getMediaSempliceTotali()));
				break;
			case SEMPLICE_RAPPORTI:
				current.setAccantonamento(calcolaAccantonamentoEffettivo(residuoCapitolo, current.getMediaSempliceRapporti() == null ? BigDecimal.ZERO : current.getMediaSempliceRapporti()));
				break;
			case PONDERATA_TOTALI:
				current.setAccantonamento(calcolaAccantonamentoEffettivo(residuoCapitolo, current.getMediaPonderataTotali() == null ? BigDecimal.ZERO : current.getMediaPonderataTotali()));
				break;
			case PONDERATA_RAPPORTI:
				current.setAccantonamento(calcolaAccantonamentoEffettivo(residuoCapitolo, current.getMediaPonderataRapporti() == null ? BigDecimal.ZERO : current.getMediaPonderataRapporti()));
				break;
			case UTENTE:
				//SIAC-8393
				current.setAccantonamento(afde.getAccantonamento());
				break;
			default:
				break;
		}
		
	}
	
}
