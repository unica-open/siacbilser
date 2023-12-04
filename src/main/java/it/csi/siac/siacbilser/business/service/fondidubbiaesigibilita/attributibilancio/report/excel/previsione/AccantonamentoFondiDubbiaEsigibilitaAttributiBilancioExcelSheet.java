/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.attributibilancio.report.excel.previsione;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.service.excel.base.ExcelReportColumn;
import it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.attributibilancio.report.excel.BaseAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioExcelSheet;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioExcelSheet 
	extends BaseAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioExcelSheet<AccantonamentoFondiDubbiaEsigibilitaExcelRow> {

	private Object[] headerParameters;

	public AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioExcelSheet(
			int quinquennioRiferimento, int annoBilancio, List<Object[]> dettagli) {

		headerParameters = new Object [] {
				quinquennioRiferimento - 4,
				quinquennioRiferimento - 3,
				quinquennioRiferimento - 2,
				quinquennioRiferimento - 1,
				quinquennioRiferimento - 0,
				annoBilancio,
				annoBilancio + 1,
				annoBilancio + 2
			};
		
		for (Object[] o : dettagli) {
			
			AccantonamentoFondiDubbiaEsigibilitaExcelRow riga = new AccantonamentoFondiDubbiaEsigibilitaExcelRow();
			
			riga.setVersione((Integer) o[0]);
			riga.setCapitolo((String) o[7]);
			riga.setArticolo((String) o[8]);
			riga.setUeb((String) o[9]);
			riga.setTitolo((String) o[10]);
			riga.setTipologia((String) o[11]);
			riga.setCategoria((String) o[12]);
			riga.setStrutturaAmministrativoContabile((String) o[13]);
			riga.setIncassi4((BigDecimal) o[14]);
			riga.setAccertamenti4((BigDecimal) o[15]);
			riga.setIncassi3((BigDecimal) o[16]);
			riga.setAccertamenti3((BigDecimal) o[17]);
			riga.setIncassi2((BigDecimal) o[18]);
			riga.setAccertamenti2((BigDecimal) o[19]);
			riga.setIncassi1((BigDecimal) o[20]);
			riga.setAccertamenti1((BigDecimal) o[21]);
			riga.setIncassi0((BigDecimal) o[22]);
			riga.setAccertamenti0((BigDecimal) o[23]);
			// FIXME: impostare la formula corretta
//			riga.setMediaSempliceTotali("MIN(100, SUM(F%ROW%,H%ROW%,J%ROW%,L%ROW%,N%ROW%) / SUM(G%ROW%,I%ROW%,K%ROW%,M%ROW%,O%ROW%) * 100)");
			//SIAC-8513
			riga.setMediaSempliceTotali(setBigDecimalPrecision((BigDecimal) o[24]));
			riga.setMediaSempliceRapporti(setBigDecimalPrecision((BigDecimal) o[25]));
			riga.setMediaPonderataTotali(setBigDecimalPrecision((BigDecimal) o[26]));
			riga.setMediaPonderataRapporti(setBigDecimalPrecision((BigDecimal) o[27]));
			riga.setMediaUtente(setBigDecimalPrecision((BigDecimal) o[28]));
			riga.setPercentualeMinima(setBigDecimalPrecision((BigDecimal) o[29]));
			riga.setPercentualeEffettiva(setBigDecimalPrecision((BigDecimal) o[30]));
			//
			riga.setStanziamento0((BigDecimal) o[31]);
			riga.setStanziamento1((BigDecimal) o[32]);
			riga.setStanziamento2((BigDecimal) o[33]);
			riga.setAccantonamentoFCDE0((BigDecimal) o[34]);
			riga.setAccantonamentoFCDE1((BigDecimal) o[35]);
			riga.setAccantonamentoFCDE2((BigDecimal) o[36]);
			
			rows.add(riga);
		}	
	}

	@Override
	protected ExcelReportColumn[] getColumnTitles() {
		return AccantonamentoFondiDubbiaEsigibilitaExcelReportColumn.values();
	}

	
	@Override
	public Object[] getHeaderParameters() {
		return headerParameters;
	}

}
