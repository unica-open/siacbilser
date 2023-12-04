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

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.utility.mutuo.filter.RataMutuoNonScadutaFilter;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.InserisciVariazioneMassivaTassoMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.InserisciVariazioneMassivaTassoMutuoResponse;
import it.csi.siac.siacbilser.integration.dad.MutuoDad;
import it.csi.siac.siacbilser.model.mutuo.Mutuo;
import it.csi.siac.siacbilser.model.mutuo.MutuoModelDetail;
import it.csi.siac.siacbilser.model.mutuo.RataMutuo;
import it.csi.siac.siacbilser.model.mutuo.StatoMutuo;
import it.csi.siac.siacbilser.model.mutuo.TipoTassoMutuo;
import it.csi.siac.siacbilser.model.mutuo.TipoVariazioneMutuo;
import it.csi.siac.siacbilser.model.mutuo.VariazioneMutuo;
import it.csi.siac.siaccommon.util.collections.CollectionUtil;
import it.csi.siac.siaccommon.util.number.NumberUtil;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisciVariazioneMassivaTassoMutuoService extends CheckedAccountBaseService<InserisciVariazioneMassivaTassoMutuo, InserisciVariazioneMassivaTassoMutuoResponse> {

	private @Autowired MutuoDad mutuoDad;
	
	
	private VariazioneMutuo variazioneMutuo;
	private Mutuo mutuo;
	private Mutuo mutuoCorrente;
	
	@Override
	@Transactional
	public InserisciVariazioneMassivaTassoMutuoResponse executeService(InserisciVariazioneMassivaTassoMutuo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkCondition(req.getElencoIdMutui() != null && !req.getElencoIdMutui().isEmpty(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("elencoIdMutui"), true);
		checkCondition(NumberUtil.isValidAndGreaterThanZero(req.getTassoInteresseEuribor()), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tassoInteresseEuribor"), true);
	}
	
	@Override
	protected void init() {
		mutuoDad.setEnte(ente);
		mutuoDad.setLoginOperazione(loginOperazione);
	}	
	
	@Override
	protected void execute() {
		
		for ( int mutuoId : req.getElencoIdMutui()) {
			
			variazioneMutuo = new VariazioneMutuo();
			variazioneMutuo.setTipoVariazioneMutuo(TipoVariazioneMutuo.VariazioneTasso);
			variazioneMutuo.setTassoInteresseEuribor(req.getTassoInteresseEuribor());
			
			mutuo = new Mutuo();
			mutuo.setUid(mutuoId);
			
			checkMutuo();
			
			checkStato();
			
			checkTipoTassoMutuoTipoVariazioneMutuo();
			
			checkRataMutuo();
			
			inserisciVariazione();
		}
	}
	
	protected void checkMutuo() {
		mutuoCorrente = mutuoDad.ricercaMutuo(mutuo, MutuoModelDetail.Stato, MutuoModelDetail.PeriodoRimborso, MutuoModelDetail.PianoAmmortamento);
		checkBusinessCondition(mutuoCorrente != null, ErroreCore.ENTITA_NON_TROVATA.getErrore("mutuo"));
		checkBusinessCondition(mutuoCorrente.isEntitaValida(), ErroreCore.VALORE_NON_VALIDO.getErrore(mutuo.getNumero()));
		mutuoCorrente.setTassoInteresseEuribor(variazioneMutuo.getTassoInteresseEuribor());
		mutuoCorrente.setTassoInteresse(variazioneMutuo.getTassoInteresseEuribor().add(mutuoCorrente.getTassoInteresseSpread()));
		variazioneMutuo.setMutuo(mutuoCorrente);
	}
	
	private void checkRataMutuo() {
		RataMutuo primaRataNonScaduta = CollectionUtil.getFirst(CollectionUtil.filter(mutuoCorrente.getElencoRate(), new RataMutuoNonScadutaFilter()));
		checkBusinessCondition(primaRataNonScaduta != null, ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("il mutuo "+mutuoCorrente.getNumero()+ " non ha alcuna rata in scadenza"));
		primaRataNonScaduta.setImportoTotale(null);
		variazioneMutuo.setRataMutuo(primaRataNonScaduta);
	}
	
	private void checkStato() {
		checkBusinessCondition(StatoMutuo.Definitivo.equals(mutuoCorrente.getStatoMutuo()), ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("il mutuo " + mutuoCorrente.getNumero(), mutuoCorrente.getStatoMutuo().getDescrizione()));
	}
	private void checkTipoTassoMutuoTipoVariazioneMutuo() {
		checkBusinessCondition(
				(TipoVariazioneMutuo.VariazioneTasso.equals(variazioneMutuo.getTipoVariazioneMutuo()) && TipoTassoMutuo.Variabile.equals(mutuoCorrente.getTipoTasso()))
				|| (TipoVariazioneMutuo.VariazionePiano.equals(variazioneMutuo.getTipoVariazioneMutuo()) && TipoTassoMutuo.Fisso.equals(mutuoCorrente.getTipoTasso()))
				, ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore(variazioneMutuo.getTipoVariazioneMutuo().getDescrizione() + " non applicabile a mutuo a tasso " + mutuoCorrente.getTipoTasso().getDescrizione()));
	}

	private VariazioneMutuo inserisciVariazione() {
		final String methodName = "inserisciVariazione";
	
		variazioneMutuo.setNumeroRataFinale(CollectionUtil.getLast(mutuoCorrente.getElencoRate()).getNumeroRataPiano());
		
		VariazioneMutuo variazioneMutuoInserita = mutuoDad.createVariazione(variazioneMutuo);
		
		log.debug(methodName, "Inserita variazione con uid " + variazioneMutuoInserita.getUid());
		
		return variazioneMutuoInserita;
	}
}
