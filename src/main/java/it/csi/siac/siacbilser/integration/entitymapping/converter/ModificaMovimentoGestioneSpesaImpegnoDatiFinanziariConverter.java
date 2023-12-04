/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTMovgest;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgestT;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesa;

/**
 * The Class ModificaMovimentoGestioneSpesaImpegnoConverter.
 */
@Component
public class ModificaMovimentoGestioneSpesaImpegnoDatiFinanziariConverter extends ModificaMovimentoGestioneMovimentoGestioneDatiFinanziariConverter<Impegno, SubImpegno, ModificaMovimentoGestioneSpesa> {

	/**
	 * Instantiates a new modifica movimento gestione spesa - impegno converter.
	 */
	public ModificaMovimentoGestioneSpesaImpegnoDatiFinanziariConverter() {
		super(ModificaMovimentoGestioneSpesa.class);
	}
	
	@Override
	protected Impegno convertiMovimentoGestione(SiacTMovgestT siacTMovgestT) {
		SiacTMovgest siacTMovgest = siacTMovgestT.getSiacTMovgest();
		Impegno i = new Impegno();
		i.setUid(siacTMovgest.getUid());
		i.setAnnoMovimento(siacTMovgest.getMovgestAnno());
		i.setNumeroBigDecimal(siacTMovgest.getMovgestNumero());
		
		// SIAC-4467
		impostaSoggetto(siacTMovgestT, i);
		aggiungiInformazioniPianoDeiConti(siacTMovgestT, i);
		
		return i;
	}
	
	@Override
	protected SubImpegno convertiSubMovimentoGestione(SiacTMovgestT siacTMovgestT) {
		SiacTMovgest siacTMovgest = siacTMovgestT.getSiacTMovgest();
		SubImpegno si = new SubImpegno();
		si.setUid(siacTMovgestT.getUid());
		si.setNumeroBigDecimal(new BigDecimal(siacTMovgestT.getMovgestTsCode()));
		si.setIdMovimentoPadre(siacTMovgest.getUid());
		si.setAnnoImpegnoPadre(siacTMovgest.getMovgestAnno());
		si.setNumeroImpegnoPadre(siacTMovgest.getMovgestNumero());
		
		// SIAC-4467
		impostaSoggetto(siacTMovgestT, si);
		aggiungiInformazioniPianoDeiConti(siacTMovgestT, si);
		
		return si;
	}

	@Override
	protected void setMovimentoGestione(ModificaMovimentoGestioneSpesa dest, Impegno mg) {
		dest.setImpegno(mg);
	}

	@Override
	protected void setSubMovimentoGestione(ModificaMovimentoGestioneSpesa dest, SubImpegno smg) {
		dest.setSubImpegno(smg);
	}
	
}
