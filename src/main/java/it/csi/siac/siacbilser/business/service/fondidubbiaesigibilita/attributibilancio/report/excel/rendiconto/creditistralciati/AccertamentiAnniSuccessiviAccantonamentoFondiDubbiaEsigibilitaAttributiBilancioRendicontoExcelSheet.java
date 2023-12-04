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
import it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.attributibilancio.report.excel.rendiconto.AccantonamentoFondiDubbiaEsigibilitaRendicontoExcelReportColumn;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AccertamentiAnniSuccessiviAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioRendicontoExcelSheet 
	extends BaseAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioExcelSheet
		<AccertamentiAnniSuccessiviAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioRendicontoExcelRow> {


	public AccertamentiAnniSuccessiviAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioRendicontoExcelSheet(
			List<Object[]> dettagli) {

		
		for (Object[] o : dettagli) {
			
			AccertamentiAnniSuccessiviAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioRendicontoExcelRow riga = 
					new AccertamentiAnniSuccessiviAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioRendicontoExcelRow();
			
			riga.setVersione((Integer) o[0]);
			riga.setCapitolo((String) o[8]);
			riga.setArticolo((String) o[9]);
			riga.setStrutturaAmministrativoContabile((String) o[11]);
			riga.setAnnoAccertamento((Integer) o[12]);
			riga.setNumeroAccertamento((BigDecimal) o[13]);
			riga.setDescrizioneAccertamento((String) o[14]);
			riga.setPianoDeiContiAccertamento((String) o[15]);
			
			riga.setTitoloAccertamento((String) o[16]);
			riga.setTipologiaAccertamento((String) o[17]);
			riga.setCategoriaAccertamento((String) o[18]);
			riga.setStrutturaAmministrativoContabileAccertamento((String) o[19]);
			riga.setCodRicorrente((String) o[20]);
			
			riga.setSoggettoDesc((String) o[21]);
			riga.setClasseSoggettoDesc((String) o[22]);
			riga.setAnnoProvvedimentoAccertamento((String) o[23]);
			riga.setNumeroProvvedimentoAccertamento((Integer) o[24]);
			riga.setTipoProvvedimentoAccertamento((String) o[25]);
			riga.setSacProvvedimentoAccertamento((String) o[26]);
			riga.setImportoAttuale((BigDecimal) o[27]);
			
			rows.add(riga);
		}	
	}

	@Override
	protected ExcelReportColumn[] getColumnTitles() {
		return AccantonamentoFondiDubbiaEsigibilitaRendicontoExcelReportColumn.values();
	}



	@Override
	protected String getTitle() {
		return super.getTitle().concat(" - Accertamenti anni successivi");
	}

}
