/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.mutuo.report.excel.movimentogestione;

import java.util.List;

import it.csi.siac.siacbilser.business.service.excel.base.ExcelReportColumn;
import it.csi.siac.siacbilser.model.mutuo.AccertamentoAssociatoMutuo;
import it.csi.siac.siaccommon.util.CoreUtil;
import it.csi.siac.siacfinser.model.Accertamento;


public class AccertamentiAssociatiMutuoExcelSheet extends MovimentiGestioneAssociatiMutuoExcelSheet<AccertamentiAssociatiMutuoExcelRow, Accertamento> {

	public AccertamentiAssociatiMutuoExcelSheet(List<AccertamentoAssociatoMutuo> elencoAccertamentiAssociati) {
		
		for (AccertamentoAssociatoMutuo accertamentoAssociatoMutuo : elencoAccertamentiAssociati) {
			AccertamentiAssociatiMutuoExcelRow riga = new AccertamentiAssociatiMutuoExcelRow();

			map(accertamentoAssociatoMutuo, riga);

			riga.setCodTitolo(accertamentoAssociatoMutuo.getAccertamento().getCapitoloEntrataGestione() == null ?
				null : accertamentoAssociatoMutuo.getAccertamento().getCapitoloEntrataGestione().getTitoloEntrata().getCodice());
			riga.setElencoSubAccertamenti(CoreUtil.getOption(accertamentoAssociatoMutuo.getAccertamento().getTotaleSubAccertamenti() > 0, "Sì", "No"));
			riga.setImportoIncassato(accertamentoAssociatoMutuo.getAccertamento().getImportoIncassato());
			
			rows.add(riga);
		}
	}

	@Override
	protected ExcelReportColumn[] getColumnTitles() {
		return AccertamentiAssociatiMutuoExcelReportColumn.values();
	}

	@Override
	protected String getTitle() {
		return "Accertamenti associati";
	}
}
