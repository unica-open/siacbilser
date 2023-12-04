/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.attributibilancio.report.excel;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.service.excel.base.ExcelReportColumn;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class IntestazioneAccantonamentoFondiDubbiaEsigibilitaExcelSheet 
	extends BaseAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioExcelSheet<IntestazioneAccantonamentoFondiDubbiaEsigibilitaExcelRow> {


	public IntestazioneAccantonamentoFondiDubbiaEsigibilitaExcelSheet(Object...dettaglio) {
			IntestazioneAccantonamentoFondiDubbiaEsigibilitaExcelRow riga = new IntestazioneAccantonamentoFondiDubbiaEsigibilitaExcelRow();

			riga.setVersione((Integer) dettaglio[0]);
			riga.setFaseAttributiBilancio((String) dettaglio[1]);
			riga.setStatoAttributiBilancio((String) dettaglio[2]);
			riga.setDataOraElaborazione((Date) dettaglio[3]);
			riga.setAnniEsercizio((String) dettaglio[4]);
			riga.setRiscossioneVirtuosa(Boolean.TRUE.equals(dettaglio[5]) ? "SI" : "NO");
			riga.setQuinquennioRiferimento((String) dettaglio[6]);
			riga.setAccantonamentoGradualeEntiLocali((BigDecimal) dettaglio[7]);
			
			rows.add(riga);
	}
	
	
	@Override
	protected ExcelReportColumn[] getColumnTitles() {
		return IntestazioneAccantonamentoFondiDubbiaEsigibilitaExcelReportColumn.values();
	}


	@Override
	protected String getTitle() {
		return "Intestazioni";
	}
	
}
