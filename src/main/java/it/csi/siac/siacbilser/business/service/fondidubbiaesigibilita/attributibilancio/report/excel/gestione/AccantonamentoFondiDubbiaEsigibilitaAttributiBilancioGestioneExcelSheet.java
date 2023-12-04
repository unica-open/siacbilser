/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.attributibilancio.report.excel.gestione;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.service.excel.base.ExcelReportColumn;
import it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.attributibilancio.report.excel.BaseAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioExcelSheet;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioGestioneExcelSheet 
	extends BaseAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioExcelSheet<AccantonamentoFondiDubbiaEsigibilitaGestioneExcelRow> {

	private Object[] headerParameters;

	public AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioGestioneExcelSheet(
		int quinquennioRiferimento, int annoBilancio, List<Object[]> dettagli) {

		headerParameters = new Object [] {
				quinquennioRiferimento,
				annoBilancio,
				annoBilancio + 1,
				annoBilancio + 2
		};
		
		for (Object[] o : dettagli) {
			
			AccantonamentoFondiDubbiaEsigibilitaGestioneExcelRow riga = new AccantonamentoFondiDubbiaEsigibilitaGestioneExcelRow();

			riga.setVersione((Integer) o[0]);
			riga.setCapitolo((String) o[7]);
			riga.setArticolo((String) o[8]);
			riga.setUeb((String) o[9]);
			riga.setTitolo((String) o[10]);
			riga.setTipologia((String) o[11]);
			riga.setCategoria((String) o[12]);
			riga.setStrutturaAmministrativoContabile((String) o[13]);
			riga.setIncassoContoCompetenza((BigDecimal) o[14]);
			riga.setAccertatoContoCompetenza((BigDecimal) o[15]);
			//SIAC-8513
			riga.setPercentualeIncassoGestione(setBigDecimalPrecision((BigDecimal) o[16]));
			riga.setPercentualeAccantonamento(setBigDecimalPrecision((BigDecimal) o[17]));
			riga.setTipoAccantonamentoPrecedente((String) o[18]);
			riga.setPercentualeAccantonamentoPrecedente(setBigDecimalPrecision((BigDecimal) o[19]));
			riga.setPercentualeMinimaAccantonamento(setBigDecimalPrecision((BigDecimal) o[20]));
			riga.setPercentualeEffettivaAccantonamento(setBigDecimalPrecision((BigDecimal) o[21]));
			//
			riga.setAccantonamentoFcde0((BigDecimal) o[25]);
			riga.setAccantonamentoFcde1((BigDecimal) o[26]);
			riga.setAccantonamentoFcde2((BigDecimal) o[27]);
			
			//SIAC-8768
			riga.setStanziato((BigDecimal) o[22]);
			riga.setStanziato1((BigDecimal) o[23]);
			riga.setStanziato2((BigDecimal) o[24]);
			riga.setStanziatoSenzaVariazioni((BigDecimal) o[29]);
			riga.setStanziatoSenzaVariazioni1((BigDecimal) o[30]);
			riga.setStanziatoSenzaVariazioni2((BigDecimal) o[31]);
			riga.setDeltaVariazioni((BigDecimal) o[32]);
			riga.setDeltaVariazioni1((BigDecimal) o[33]);
			riga.setDeltaVariazioni2((BigDecimal) o[34]);
			
			rows.add(riga);
		}	
	}

	@Override
	protected ExcelReportColumn[] getColumnTitles() {
		return AccantonamentoFondiDubbiaEsigibilitaGestioneExcelReportColumn.values();
	}

	@Override
	public Object[] getHeaderParameters() {
		return headerParameters;
	}
	
}
