/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.mutuo.report.excel.pianoammortamento;

import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.service.excel.base.ExcelReportColumn;
import it.csi.siac.siacbilser.business.service.excel.base.ExcelSheet;
import it.csi.siac.siacbilser.model.mutuo.RataMutuo;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PianoAmmortamentoMutuoExcelSheet extends ExcelSheet<PianoAmmortamentoMutuoExcelRow> {
	
	public PianoAmmortamentoMutuoExcelSheet(List<RataMutuo> elencoRate) {
		for(RataMutuo rata : elencoRate) {
			PianoAmmortamentoMutuoExcelRow riga = new PianoAmmortamentoMutuoExcelRow();
			
			riga.setAnno(rata.getAnno());
			riga.setNumeroRata(rata.getNumeroRataPiano());
			riga.setNumeroRataAnno(rata.getNumeroRataAnno());
			riga.setDataScadenza(rata.getDataScadenza());
			riga.setImportoRata(rata.getImportoTotale());
			riga.setQuotaCapitale(rata.getImportoQuotaCapitale());
			riga.setQuotaInteressi(rata.getImportoQuotaInteressi());
			riga.setQuotaOneri(rata.getImportoQuotaOneri());
			riga.setDebitoIniziale(rata.getDebitoIniziale());
			riga.setDebitoResiduo(rata.getDebitoResiduo());
			
			rows.add(riga);
		}
	}

	@Override
	protected ExcelReportColumn[] getColumnTitles() {
		return PianoAmmortamentoMutuoExcelReportColumn.values();
	}
	
	@Override
	protected String getTitle() {
		return "Piano ammortamento";
	}

}
