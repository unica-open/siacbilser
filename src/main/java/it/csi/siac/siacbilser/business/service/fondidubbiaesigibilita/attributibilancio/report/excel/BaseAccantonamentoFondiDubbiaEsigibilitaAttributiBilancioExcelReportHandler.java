/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.attributibilancio.report.excel;

import it.csi.siac.siacbilser.business.service.excel.base.BaseExcelReportHandler;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio;

/**
 * Report handler per la stampa dell'accantonamento fondi dubbia esigibilita
 * @author interlogic
 * @version 1.0.0 - 05/05/2021
 */
public abstract class BaseAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioExcelReportHandler extends BaseExcelReportHandler {
	
	protected AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio accantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
	

	private boolean isRisultatoLimitato;
	private boolean isColumnAutosize;
	

	public void setAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(
			AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio accantonamentoFondiDubbiaEsigibilitaAttributiBilancio) {
		this.accantonamentoFondiDubbiaEsigibilitaAttributiBilancio = accantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
	}


/* TODO	
	public byte[] toBytes() {
		final String methodName = "toBytes";
		// XXX: valutare se mantenere ovvero togliere
		if(isRisultatoLimitato){
			rowCount++;
			Row warnRow = sheet.createRow(rowCount);
			warnRow.setHeightInPoints(30f);
			Cell cell = warnRow.createCell(0);
			cell.setCellValue("Attenzione! Risultato limitato a " + (rowCount - 2) + " elementi.");
			cell.setCellStyle(styles.get("cell_warn"));
		}
		
		// Autosize delle colonne...
		if(isColumnAutosize) {
			for (int j = 0; j < getColumns().size(); j++) {
				sheet.autoSizeColumn(j, false);
			}
		}
		
		return super.toBytes();
	}
*/
	
	
}

