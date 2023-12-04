/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.mutuo.report.excel.movimentogestione;

import java.util.List;

import it.csi.siac.siacbilser.business.service.excel.base.ExcelReportColumn;
import it.csi.siac.siacbilser.model.mutuo.ImpegnoAssociatoMutuo;
import it.csi.siac.siaccommon.util.CoreUtil;
import it.csi.siac.siacfinser.model.Impegno;


public class ImpegniAssociatiMutuoExcelSheet extends MovimentiGestioneAssociatiMutuoExcelSheet<ImpegniAssociatiMutuoExcelRow, Impegno> {

	public ImpegniAssociatiMutuoExcelSheet(List<ImpegnoAssociatoMutuo> elencoImpegniAssociati) {
		
		for (ImpegnoAssociatoMutuo impegnoAssociatoMutuo : elencoImpegniAssociati) {
			ImpegniAssociatiMutuoExcelRow riga = new ImpegniAssociatiMutuoExcelRow();

			map(impegnoAssociatoMutuo, riga);

			riga.setCodMissione(impegnoAssociatoMutuo.getImpegno().getCapitoloUscitaGestione() == null ? 
				null : impegnoAssociatoMutuo.getImpegno().getCapitoloUscitaGestione().getMissione() == null ?
					null : impegnoAssociatoMutuo.getImpegno().getCapitoloUscitaGestione().getMissione().getCodice());
			riga.setCodProgramma(impegnoAssociatoMutuo.getImpegno().getCapitoloUscitaGestione() == null ? 
				null : impegnoAssociatoMutuo.getImpegno().getCapitoloUscitaGestione().getProgramma() == null ?
					null : impegnoAssociatoMutuo.getImpegno().getCapitoloUscitaGestione().getProgramma().getCodice());
			riga.setComponenteBilancio(impegnoAssociatoMutuo.getImpegno().getComponenteBilancioImpegno() == null ? 
				null : impegnoAssociatoMutuo.getImpegno().getComponenteBilancioImpegno().getDescrizioneTipoComponente());
			riga.setCig(impegnoAssociatoMutuo.getImpegno().getCig());
			riga.setCup(impegnoAssociatoMutuo.getImpegno().getCup());
			riga.setElencoSubImpegni(CoreUtil.getOption(impegnoAssociatoMutuo.getImpegno().getTotaleSubImpegni() > 0, "Sì", "No"));
			riga.setImportoLiquidato(impegnoAssociatoMutuo.getImpegno().getImportoLiquidato());
			
			rows.add(riga);
		}
	}

	@Override
	protected ExcelReportColumn[] getColumnTitles() {
		return ImpegniAssociatiMutuoExcelReportColumn.values();
	}

	@Override
	protected String getTitle() {
		return "Impegni associati";
	}
}
