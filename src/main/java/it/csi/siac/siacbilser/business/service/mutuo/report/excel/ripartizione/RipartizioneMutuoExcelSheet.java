/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.mutuo.report.excel.ripartizione;

import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.service.excel.base.ExcelReportColumn;
import it.csi.siac.siacbilser.business.service.excel.base.ExcelSheet;
import it.csi.siac.siacbilser.model.mutuo.RipartizioneMutuo;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RipartizioneMutuoExcelSheet extends ExcelSheet<RipartizioneMutuoExcelRow> {
	
	public RipartizioneMutuoExcelSheet(List<RipartizioneMutuo> elencoRipartizioni) {

		for (RipartizioneMutuo ripartizione : elencoRipartizioni) {
		
			RipartizioneMutuoExcelRow riga = new RipartizioneMutuoExcelRow();
			
			riga.setRipartizioneTipo(ripartizione.getTipoRipartizioneMutuo().getDescrizione());
			riga.setCapitolo(ripartizione.getCapitolo().getAnnoCapitoloArticolo());
			riga.setImporto(ripartizione.getRipartizioneImporto());
			riga.setPercentuale(ripartizione.getRipartizionePercentuale());
			
			rows.add(riga);
		}
	}

	@Override
	protected ExcelReportColumn[] getColumnTitles() {
		return RipartizioneMutuoExcelReportColumn.values();
	}

	@Override
	protected String getTitle() {
		return "Ripartizione su capitoli";
	}
}
