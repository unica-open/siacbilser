/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.mutuo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.utility.mutuo.comparator.RataMutuoNumeroRataPianoComparator;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.AggiornaPianoAmmortamentoMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.AggiornaPianoAmmortamentoMutuoResponse;
import it.csi.siac.siacbilser.model.mutuo.RataMutuo;
import it.csi.siac.siacbilser.model.mutuo.StatoMutuo;
import it.csi.siac.siaccommon.util.collections.CollectionUtil;
import it.csi.siac.siaccommon.util.collections.Reductor;
import it.csi.siac.siaccommon.util.date.DateUtil;
import it.csi.siac.siaccommon.util.number.BigDecimalUtil;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaPianoAmmortamentoMutuoService extends BaseMutuoService<AggiornaPianoAmmortamentoMutuo, AggiornaPianoAmmortamentoMutuoResponse> {
	
	@Override
	@Transactional
	public AggiornaPianoAmmortamentoMutuoResponse executeService(AggiornaPianoAmmortamentoMutuo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		super.checkServiceParam();
		checkNotNull(req.getStatoMutuo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("statoMutuo"));
		checkNotNull(mutuo.getElencoRate(), ErroreCore.ENTITA_NON_COMPLETA.getErrore("mutuo", "elenco rate null"));
		checkCondition(!mutuo.getElencoRate().isEmpty(), ErroreCore.ENTITA_NON_COMPLETA.getErrore("mutuo", "elenco rate vuoto"));
	}

	@Override
	protected void execute() {
		
		super.execute();

		checkStato();
		checkDateScadenza();
		checkSommaMutuataIniziale();
		
		mutuo.setUid(mutuoCorrente.getUid());
		mutuo.setStatoMutuo(req.getStatoMutuo());
		mutuoDad.salvaPianoAmmortamentoMutuo(mutuo);

	}

	private void checkDateScadenza() {
		checkScadenzaPrimaRata();

		for (int i = 1; i < mutuo.getElencoRate().size(); i++) {
			RataMutuo rataMutuo = mutuo.getElencoRate().get(i);
			
			checkBusinessCondition(
				DateUtil.getYear(rataMutuo.getDataScadenza()).equals(rataMutuo.getAnno()), 
				ErroreCore.VALORE_NON_CONSENTITO.getErrore(
					String.format("'Data scadenza' della rata n. %d, %d/%d",
						rataMutuo.getNumeroRataPiano(),
						rataMutuo.getAnno(),
						rataMutuo.getNumeroRataAnno()
					),
					String.format("- deve essere nell'anno %d", rataMutuo.getAnno())
				)
			);
			
			RataMutuo rataMutuoPrec = CollectionUtil.getNth(mutuo.getElencoRate(), i-1);
			RataMutuo rataMutuoSucc = CollectionUtil.getNth(mutuo.getElencoRate(), i+1);
			
			Date minDate = DateUtils.addMonths(mutuoCorrente.getScadenzaPrimaRata(), (rataMutuo.getNumeroRataPiano()-2) * mutuoCorrente.getPeriodoRimborso().getNumeroMesi());
			Date maxDate = DateUtils.addMonths(minDate, 2 * mutuoCorrente.getPeriodoRimborso().getNumeroMesi());
			
			minDate = rataMutuoPrec == null ? minDate : DateUtil.max(rataMutuoPrec.getDataScadenza(), minDate);
			maxDate = rataMutuoSucc == null ? maxDate : DateUtil.min(rataMutuoSucc.getDataScadenza(), maxDate);

			checkBusinessCondition(DateUtil.dayAfterDay(rataMutuo.getDataScadenza(), minDate) && DateUtil.dayBeforeDay(rataMutuo.getDataScadenza(), maxDate), 
					ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore(
							String.format("la data di scadenza della rata %d %d/%d deve essere compresa tra il %s e il %s esclusi", 
									rataMutuo.getNumeroRataPiano(),
									rataMutuo.getAnno(),
									rataMutuo.getNumeroRataAnno(),
									DateUtil.formatDate(minDate),
									DateUtil.formatDate(maxDate)
			)));
		}
	}

	private void checkSommaMutuataIniziale() {
		checkBusinessCondition(
			BigDecimalUtil.equalWithinTolerance(
				CollectionUtil.reduce(mutuo.getElencoRate(), new Reductor<RataMutuo, BigDecimal>() {
					@Override
					public BigDecimal reduce(BigDecimal accumulator, RataMutuo current, int index) {
						return accumulator.add(BigDecimalUtil.getDefaultZero(current.getImportoQuotaCapitale()));
					}
				}, BigDecimal.ZERO),
				mutuoCorrente.getSommaMutuataIniziale(),
				1e-2), 
			ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("somma mutuata iniziale e totale importi quota capitale"));
	}

	private void checkStato() {
		checkBusinessCondition(!StatoMutuo.Annullato.equals(mutuoCorrente.getStatoMutuo()), 
				ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("il mutuo", mutuoCorrente.getStatoMutuo().getDescrizione()));
	}

	private void checkScadenzaPrimaRata() {
		checkBusinessCondition (!StatoMutuo.Bozza.equals(mutuoCorrente.getStatoMutuo()) || !DateUtil.beforeToday(mutuoCorrente.getScadenzaPrimaRata()),
				ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("il mutuo è in stato BOZZA con la prima rata già scaduta"));
		
		List<RataMutuo> elencoRateSort = CollectionUtil.getSortedList(mutuo.getElencoRate(), RataMutuoNumeroRataPianoComparator.class);
	
		checkBusinessCondition(
				DateUtil.daysEqual(mutuoCorrente.getScadenzaPrimaRata(), CollectionUtil.getFirst(elencoRateSort).getDataScadenza()),
				ErroreCore.INCONGRUENZA_NEI_PARAMETRI
					.getErrore("la scadenza della prima rata indicata nel mutuo non coincide con la scadenza della prima rata in elenco"));
	}
}
