/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.variazionibilancio.report.excel;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.service.excel.base.ExcelReportColumn;
import it.csi.siac.siacbilser.business.service.excel.base.ExcelSheet;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class VariazioneBilancioExcelSheet extends ExcelSheet<VariazioneImportoCapitoloExcelRow> {

	private Object[] headerParameters;

	public VariazioneBilancioExcelSheet(int annoBilancio, List<Object[]> dettagli) {
		headerParameters = new Object[] {
				Integer.valueOf(annoBilancio),
				Integer.valueOf(annoBilancio + 1),
				Integer.valueOf(annoBilancio + 2)
		};
		
		for (Object[] o : dettagli) {
			
			VariazioneImportoCapitoloExcelRow riga = new VariazioneImportoCapitoloExcelRow();
			
			riga.setVariazioneNum((Integer) o[35]);
			// SIAC-6883
//			riga.setVariazioneAnno((String) o[36]);
			
			riga.setStatoVariazione((String) o[0]);
			riga.setAnnoCapitolo((String) o[1]);
			riga.setNumeroCapitolo((String) o[2]);
			riga.setNumeroArticolo((String) o[3]);
			riga.setTipoCapitolo((String) o[5]);
			riga.setDescrizioneCapitolo((String) o[6]);
			// Classificatori spesa
			riga.setMissioneCapitolo((String) o[8]);
			riga.setProgrammaCapitolo((String) o[9]);
			riga.setTitoloCapitoloSpesa((String) o[10]);
			riga.setMacroaggregatoCapitolo((String) o[11]);
			// Classificatori entrata
			riga.setTitoloCapitoloEntrata((String) o[12]);
			riga.setTipologiaCapitoloEntrata((String) o[13]);
			riga.setCategoriaTipologiaTitoloCapitoloEntrata((String) o[14]);
			//SIAC-6468
			riga.setTipologiaFinanziamento((String) o[33]);
			riga.setStrutturaAmministrativaResponsabile((String) o[34]);

			// Stanziamenti capitolo
			riga.setStanziamentoCapitolo((BigDecimal) o[24]);
			riga.setStanziamentoResiduoCapitolo((BigDecimal) o[25]);
			riga.setStanziamentoCassaCapitolo((BigDecimal) o[26]);
			riga.setStanziamentoCapitoloAnno1((BigDecimal) o[27]);
			riga.setStanziamentoCapitoloAnno2((BigDecimal) o[30]);
			// Stanziamenti variazione
			riga.setStanziamentoVariazione((BigDecimal) o[15]);
			riga.setStanziamentoResiduoVariazione((BigDecimal) o[16]);
			riga.setStanziamentoCassaVariazione((BigDecimal) o[17]);
			riga.setStanziamentoVariazioneAnno1((BigDecimal) o[18]);
			riga.setStanziamentoVariazioneAnno2((BigDecimal) o[21]);
			
			rows.add(riga);
		}	
	}

	@Override
	protected ExcelReportColumn[] getColumnTitles() {
		return VariazioneImportoCapitoloExcelReportColumn.values();
	}

	@Override
	public Object[] getHeaderParameters() {
		return headerParameters;
	}

	@Override
	protected String getTitle() {
		return "Variazione di bilancio";
	}
	
}
