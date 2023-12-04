/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.attributibilancio.report.excel.rendiconto;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.service.excel.base.ExcelReportColumn;
import it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.attributibilancio.report.excel.BaseAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioExcelSheet;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioRendicontoExcelSheet 
	extends BaseAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioExcelSheet<AccantonamentoFondiDubbiaEsigibilitaRendicontoExcelRow> {

	private Object[] headerParameters;

	public AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioRendicontoExcelSheet(
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
			
			AccantonamentoFondiDubbiaEsigibilitaRendicontoExcelRow riga = new AccantonamentoFondiDubbiaEsigibilitaRendicontoExcelRow();

			riga.setVersione((Integer) o[0]);
			riga.setCapitolo((String) o[7]);
			riga.setArticolo((String) o[8]);
			riga.setUeb((String) o[9]);
			riga.setTitolo((String) o[10]);
			riga.setTipologia((String) o[11]);
			riga.setCategoria((String) o[12]);
			riga.setStrutturaAmministrativoContabile((String) o[13]);
			riga.setResidui4((BigDecimal) o[14]);
			riga.setIncassiContoResidui4((BigDecimal) o[15]);
			riga.setResidui3((BigDecimal) o[16]);
			riga.setIncassiContoResidui3((BigDecimal) o[17]);
			riga.setResidui2((BigDecimal) o[18]);
			riga.setIncassiContoResidui2((BigDecimal) o[19]);
			riga.setResidui1((BigDecimal) o[20]);
			riga.setIncassiContoResidui1((BigDecimal) o[21]);
			riga.setResidui0((BigDecimal) o[22]);
			riga.setIncassiContoResidui0((BigDecimal) o[23]);
			//SIAC-8513
			riga.setMediaSempliceTotali(setBigDecimalPrecision((BigDecimal) o[24]));
			riga.setMediaSempliceRapporti(setBigDecimalPrecision((BigDecimal) o[25]));
			riga.setMediaPonderataTotali(setBigDecimalPrecision((BigDecimal) o[26]));
			riga.setMediaPonderataRapporti(setBigDecimalPrecision((BigDecimal) o[27]));
			riga.setMediaUtente(setBigDecimalPrecision((BigDecimal) o[28]));
			riga.setPercentualeMinima(setBigDecimalPrecision((BigDecimal) o[29]));
			riga.setPercentualeEffettiva(setBigDecimalPrecision((BigDecimal) o[30]));
			//
			riga.setResiduiFinali((BigDecimal) o[31]);
//			riga.setResiduiFinali1((BigDecimal) o[32]);
//			riga.setResiduiFinali2((BigDecimal) o[33]);
			riga.setAccantonamentoFCDE((BigDecimal) o[32]);
//			riga.setAccantonamentoFCDE1((BigDecimal) o[35]);
//			riga.setAccantonamentoFCDE2((BigDecimal) o[36]);
			
			rows.add(riga);
		}	
	}

	@Override
	protected ExcelReportColumn[] getColumnTitles() {
		return AccantonamentoFondiDubbiaEsigibilitaRendicontoExcelReportColumn.values();
	}


	@Override
	public Object[] getHeaderParameters() {
		return headerParameters;
	}
	

}
