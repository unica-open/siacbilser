/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.mutuo.report.excel.progetto;

import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.service.excel.base.ExcelReportColumn;
import it.csi.siac.siacbilser.business.service.excel.base.ExcelSheet;
import it.csi.siac.siacbilser.model.mutuo.ProgettoAssociatoMutuo;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ProgettiAssociatiMutuoExcelSheet extends ExcelSheet<ProgettiAssociatiMutuoExcelRow> {
	
	public ProgettiAssociatiMutuoExcelSheet(List<ProgettoAssociatoMutuo> elencoProgettiAssociati) {
		for(ProgettoAssociatoMutuo rec : elencoProgettiAssociati) {
			
			ProgettiAssociatiMutuoExcelRow riga = new ProgettiAssociatiMutuoExcelRow();
			
			riga.setCodice(rec.getProgetto().getCodice());
			riga.setAmbito(rec.getProgetto().getTipoAmbito()!=null ? rec.getProgetto().getTipoAmbito().getDescrizione() : null);
			riga.setProvvedimento(rec.getProgetto().getAttoAmministrativo() != null ? rec.getProgetto().getAttoAmministrativo().getDescrizioneCompleta() : null);
			riga.setValoreIniziale(rec.getImportoIniziale());
			riga.setValoreAttuale(rec.getProgetto().getValoreComplessivo());
			
			rows.add(riga);
		}
	}

	@Override
	protected ExcelReportColumn[] getColumnTitles() {
		return ProgettiAssociatiMutuoExcelReportColumn.values();
	}
	
	@Override
	protected String getTitle() {
		return "Progetti associati al mutuo";
	}
}
