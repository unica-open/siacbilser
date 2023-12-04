/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.attributibilancio.report.excel.rendiconto.creditistralciati;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.service.excel.base.ExcelReportColumn;
import it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.attributibilancio.report.excel.BaseAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioExcelSheet;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CreditiStralciatiAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioRendicontoExcelSheet 
	extends BaseAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioExcelSheet
		<CreditiStralciatiAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioRendicontoExcelRow> {


	public CreditiStralciatiAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioRendicontoExcelSheet(List<Object[]> dettagli) {

		
		for (Object[] o : dettagli) {
			
			CreditiStralciatiAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioRendicontoExcelRow riga = 
					new CreditiStralciatiAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioRendicontoExcelRow();
			
			riga.setVersione((Integer) o[0]);
			riga.setCapitolo((String) o[8]);
			riga.setArticolo((String) o[9]);
			riga.setAnnoAccertamento((Integer) o[11]);
			riga.setNumeroAccertamento((BigDecimal) o[12]);
			riga.setDescrizioneAccertamento((String) o[13]);
			riga.setPianoDeiConti((String) o[14]);
			riga.setSoggettoDesc((String) o[15]);
			riga.setClasseSoggettoDesc((String) o[16]);
			riga.setAnnoProvvedimentoAccertamento((String) o[17]);
			riga.setNumeroProvvedimentoAccertamento((Integer) o[18]);
			riga.setTipoProvvedimentoAccertamento((String) o[19]);
			riga.setSacProvvedimentoAccertamento((String) o[20]);
			riga.setDescrizioneModifica((String) o[21]);
			riga.setMotivoModifica((String) o[22]);
			riga.setImportoModifica((BigDecimal) o[23]);
			riga.setAnnoProvvedimentoModifica((String) o[24]);
			riga.setNumeroProvvedimentoModifica((Integer) o[25]);
			riga.setTipoProvvedimentoModifica((String) o[26]);
			riga.setSacProvvedimentoModifica((String) o[27]);
			
						
			rows.add(riga);
		}	
	}

	@Override
	protected ExcelReportColumn[] getColumnTitles() {
		return CreditiStralciatiAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioRendicontoExcelReportColumn.values();
	}

	@Override
	protected String getTitle() {
		return super.getTitle().concat(" - Crediti stralciati");
	}
	
}
