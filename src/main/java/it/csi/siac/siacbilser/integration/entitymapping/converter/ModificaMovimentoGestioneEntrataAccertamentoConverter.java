/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTMovgest;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgestT;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneEntrata;

/**
 * The Class ModificaMovimentoGestioneEntrataAccertamentoConverter.
 */
@Component
public class ModificaMovimentoGestioneEntrataAccertamentoConverter extends ModificaMovimentoGestioneMovimentoGestioneConverter<Accertamento, SubAccertamento, ModificaMovimentoGestioneEntrata> {

	/**
	 * Instantiates a new modifica movimento gestione entrata - impegno converter.
	 */
	public ModificaMovimentoGestioneEntrataAccertamentoConverter() {
		super(ModificaMovimentoGestioneEntrata.class);
	}
	
	@Override
	protected Accertamento convertiMovimentoGestione(SiacTMovgestT siacTMovgestT) {
		SiacTMovgest siacTMovgest = siacTMovgestT.getSiacTMovgest();
		Accertamento a = new Accertamento();
		a.setUid(siacTMovgest.getUid());
		a.setAnnoMovimento(siacTMovgest.getMovgestAnno());
		a.setNumero(siacTMovgest.getMovgestNumero());
		
		// SIAC-4467
		impostaSoggetto(siacTMovgestT, a);
		aggiungiInformazioniPianoDeiConti(siacTMovgestT, a);
		
		return a;
	}
	
	@Override
	protected SubAccertamento convertiSubMovimentoGestione(SiacTMovgestT siacTMovgestT) {
		SiacTMovgest siacTMovgest = siacTMovgestT.getSiacTMovgest();
		SubAccertamento sa = new SubAccertamento();
		sa.setUid(siacTMovgestT.getUid());
		sa.setNumero(new BigDecimal(siacTMovgestT.getMovgestTsCode()));
		sa.setAnnoAccertamentoPadre(siacTMovgest.getMovgestAnno());
		sa.setNumeroAccertamentoPadre(siacTMovgest.getMovgestNumero());
		
		// SIAC-4467
		impostaSoggetto(siacTMovgestT, sa);
		aggiungiInformazioniPianoDeiConti(siacTMovgestT, sa);
		
		return sa;
	}
	
	@Override
	protected void setMovimentoGestione(ModificaMovimentoGestioneEntrata dest, Accertamento mg) {
		dest.setAccertamento(mg);
	}
	@Override
	protected void setSubMovimentoGestione(ModificaMovimentoGestioneEntrata dest, SubAccertamento smg) {
		dest.setSubAccertamento(smg);
	}

}
