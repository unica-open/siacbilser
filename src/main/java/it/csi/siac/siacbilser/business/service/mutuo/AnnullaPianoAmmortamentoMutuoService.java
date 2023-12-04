/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.mutuo;

import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.utility.mutuo.filter.RataMutuoScadutaFilter;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.AnnullaPianoAmmortamentoMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.AnnullaPianoAmmortamentoMutuoResponse;
import it.csi.siac.siacbilser.model.mutuo.MutuoModelDetail;
import it.csi.siac.siacbilser.model.mutuo.RataMutuo;
import it.csi.siac.siacbilser.model.mutuo.StatoMutuo;
import it.csi.siac.siaccommon.util.collections.CollectionUtil;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AnnullaPianoAmmortamentoMutuoService extends BaseMutuoService<AnnullaPianoAmmortamentoMutuo, AnnullaPianoAmmortamentoMutuoResponse> {
		
	@Override
	@Transactional
	public AnnullaPianoAmmortamentoMutuoResponse executeService(AnnullaPianoAmmortamentoMutuo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		
		super.execute();

		checkStato();
		
		checkPianoAmmortamento();
		
		mutuo.setStatoMutuo(mutuoCorrente.getStatoMutuo());
		mutuoDad.annullaPianoAmmortamento(mutuo);
	}
	
	private void checkStato() {
		checkBusinessCondition(!StatoMutuo.Annullato.equals(mutuoCorrente.getStatoMutuo()), ErroreCore.TRANSAZIONE_DI_STATO_NON_POSSIBILE.getErrore());
	}
	
	private void checkPianoAmmortamento() {
		mutuoCorrente = mutuoDad.ricercaMutuo(mutuo, MutuoModelDetail.Stato, MutuoModelDetail.PeriodoRimborso, MutuoModelDetail.PianoAmmortamento);
		List<RataMutuo> elencoRataMutuoScadute = CollectionUtil.filter(mutuoCorrente.getElencoRate(), new RataMutuoScadutaFilter());
		checkBusinessCondition(CollectionUtil.isEmpty(elencoRataMutuoScadute), ErroreCore.TRANSAZIONE_DI_STATO_NON_POSSIBILE.getErrore());
	}
}
