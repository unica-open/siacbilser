/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.mutuo;

import java.math.BigDecimal;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.utility.mutuo.MutuoUtil;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.InserisciVariazioneMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.InserisciVariazioneMutuoResponse;
import it.csi.siac.siacbilser.model.mutuo.MutuoModelDetail;
import it.csi.siac.siacbilser.model.mutuo.RataMutuo;
import it.csi.siac.siacbilser.model.mutuo.StatoMutuo;
import it.csi.siac.siacbilser.model.mutuo.TipoTassoMutuo;
import it.csi.siac.siacbilser.model.mutuo.TipoVariazioneMutuo;
import it.csi.siac.siacbilser.model.mutuo.VariazioneMutuo;
import it.csi.siac.siaccommon.util.collections.CollectionUtil;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisciVariazioneMutuoService extends BaseMutuoService<InserisciVariazioneMutuo, InserisciVariazioneMutuoResponse> {
	
	private VariazioneMutuo variazioneMutuo; 

	@Override
	@Transactional
	public InserisciVariazioneMutuoResponse executeService(InserisciVariazioneMutuo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		variazioneMutuo = req.getVariazioneMutuo();
		checkNotNull(variazioneMutuo, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("variazioneMutuo"));
		mutuo = variazioneMutuo.getMutuo();
		checkNotNull(mutuo, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("mutuo"));
		checkNotNull(variazioneMutuo.getRataMutuo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("rataMutuo"));
		checkNotNull(variazioneMutuo.getTipoVariazioneMutuo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipoVariazione"));
		checkNotNull(variazioneMutuo.getRataMutuo().getAnno(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno rataMutuo"));
		checkNotNull(variazioneMutuo.getRataMutuo().getNumeroRataAnno(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numeroRataAnno"));
		checkCondition(
				!TipoVariazioneMutuo.VariazioneTasso.equals(variazioneMutuo.getTipoVariazioneMutuo()) || variazioneMutuo.getAnnoFineAmmortamento() == null,
				ErroreCore.VALORE_NON_CONSENTITO.getErrore("annoFineAmmortamento",": deve essere NULL"), true);		
		checkCondition(
				!TipoVariazioneMutuo.VariazioneTasso.equals(variazioneMutuo.getTipoVariazioneMutuo()) || variazioneMutuo.getNumeroRataFinale() == null,
				ErroreCore.VALORE_NON_CONSENTITO.getErrore("numeroRataFinale",": deve essere NULL"), true);		
		checkCondition(
				!TipoVariazioneMutuo.VariazionePiano.equals(variazioneMutuo.getTipoVariazioneMutuo()) || variazioneMutuo.getRataMutuo().getImportoTotale() == null,
				ErroreCore.VALORE_NON_CONSENTITO.getErrore("importoRata",": deve essere NULL"), true);
		checkCondition(
				!TipoVariazioneMutuo.VariazionePiano.equals(variazioneMutuo.getTipoVariazioneMutuo()) || (variazioneMutuo.getAnnoFineAmmortamento() != null && variazioneMutuo.getAnnoFineAmmortamento().compareTo(0)>0),
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("annoFineAmmortamento"), true);
		checkCondition(
				!TipoVariazioneMutuo.VariazionePiano.equals(variazioneMutuo.getTipoVariazioneMutuo()) || (variazioneMutuo.getNumeroRataFinale() != null && variazioneMutuo.getNumeroRataFinale().compareTo(0)>0),
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numeroRataFinale"), true); 
		checkCondition(
				!TipoVariazioneMutuo.VariazioneTasso.equals(variazioneMutuo.getTipoVariazioneMutuo()) || (variazioneMutuo.getRataMutuo().getImportoTotale() != null && variazioneMutuo.getRataMutuo().getImportoTotale().compareTo(BigDecimal.ZERO)>0),
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("importoRata"), true); // TODO puo' essere 0 ? 
	}
	
	@Override
	protected void execute() {
		
		super.execute();
		
		checkStato();
		
		checkTipoTassoMutuoTipoVariazioneMutuo();
		
		checkRataMutuo();
		
		inserisciVariazione();
	}

	private void checkRataMutuo() {
		RataMutuo rataMutuo = mutuoDad.ricercaRataMutuo(variazioneMutuo.getRataMutuo(), variazioneMutuo.getMutuo().getUid());
		checkBusinessCondition(rataMutuo != null, ErroreCore.ENTITA_NON_TROVATA.getErrore("rataMutuo"));
		checkBusinessCondition(!rataMutuo.isScaduta(), ErroreCore.VALORE_NON_VALIDO.getErrore("rataMutuo", rataMutuo.getAnno()+"/"+rataMutuo.getNumeroRataAnno()));
		variazioneMutuo.getRataMutuo().setNumeroRataPiano(rataMutuo.getNumeroRataPiano());
		variazioneMutuo.getRataMutuo().setDebitoIniziale(rataMutuo.getDebitoIniziale());
		variazioneMutuo.getRataMutuo().setDebitoResiduo(rataMutuo.getDebitoResiduo());
		variazioneMutuo.getRataMutuo().setDataScadenza(rataMutuo.getDataScadenza());
	}
	
	private void checkStato() {
		checkBusinessCondition(StatoMutuo.Definitivo.equals(mutuoCorrente.getStatoMutuo()), ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("il mutuo", mutuoCorrente.getStatoMutuo().getDescrizione()));
	}
	private void checkTipoTassoMutuoTipoVariazioneMutuo() {
		checkBusinessCondition(
				(TipoVariazioneMutuo.VariazioneTasso.equals(variazioneMutuo.getTipoVariazioneMutuo()) && TipoTassoMutuo.Variabile.equals(mutuoCorrente.getTipoTasso()))
				|| (TipoVariazioneMutuo.VariazionePiano.equals(variazioneMutuo.getTipoVariazioneMutuo()) && TipoTassoMutuo.Fisso.equals(mutuoCorrente.getTipoTasso()))
				, ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore(variazioneMutuo.getTipoVariazioneMutuo().getDescrizione() + " non applicabile a mutuo a tasso "+ mutuoCorrente.getTipoTasso().getDescrizione()));
	}
	
	private void checkTassoInteresse() {
		checkBusinessCondition(mutuoCorrente.getTassoInteresse().signum()>=0, ErroreCore.VALORE_NON_CONSENTITO.getErrore("importo",": il tasso calcolato risulterebbe negativo"));
	}

	private VariazioneMutuo inserisciVariazione() {
		final String methodName = "inserisciMutuo";

		if (TipoVariazioneMutuo.VariazioneTasso.equals(variazioneMutuo.getTipoVariazioneMutuo())) {
			mutuoCorrente = mutuoDad.ricercaDettaglioMutuo(mutuoCorrente, MutuoModelDetail.Stato, MutuoModelDetail.PianoAmmortamento, MutuoModelDetail.PeriodoRimborso);
			
			variazioneMutuo.setNumeroRataFinale(CollectionUtil.getLast(mutuoCorrente.getElencoRate()).getNumeroRataPiano());
			
		
			mutuoCorrente.setTassoInteresse(MutuoUtil.calcTassoInteresse(variazioneMutuo.getRataMutuo().getImportoTotale(), variazioneMutuo.getRataMutuo().getDebitoIniziale(), 
					mutuoCorrente.getPeriodoRimborso(), variazioneMutuo.getNumeroRataFinale() - variazioneMutuo.getRataMutuo().getNumeroRataPiano() + 1).multiply(BigDecimal.valueOf(100)));
			
			checkTassoInteresse();
			
			mutuoCorrente.setTassoInteresseEuribor(mutuoCorrente.getTassoInteresse().subtract(mutuoCorrente.getTassoInteresseSpread()));
		}
		variazioneMutuo.setMutuo(mutuoCorrente);
		VariazioneMutuo variazioneMutuoInserita = mutuoDad.createVariazione(variazioneMutuo);
		
		log.debug(methodName, "Inserita variazione con uid " + variazioneMutuoInserita.getUid());
		
		return variazioneMutuoInserita;
	}
}
