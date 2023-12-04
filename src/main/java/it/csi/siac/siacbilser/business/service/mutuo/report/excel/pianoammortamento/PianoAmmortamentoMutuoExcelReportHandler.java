/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.mutuo.report.excel.pianoammortamento;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.service.excel.base.ExcelSheet;
import it.csi.siac.siacbilser.business.service.mutuo.report.excel.base.BaseMutuoExcelReportHandler;
import it.csi.siac.siaccommon.util.collections.ArrayUtil;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PianoAmmortamentoMutuoExcelReportHandler
	extends BaseMutuoExcelReportHandler<PianoAmmortamentoMutuoExcelRow> {
	
	@Override
	protected ExcelSheet<PianoAmmortamentoMutuoExcelRow>[] instantiateExcelSheets() {
		return ArrayUtil.toArray(
				new PianoAmmortamentoMutuoExcelSheet(mutuo.getElencoRate())
		);
	}


}
