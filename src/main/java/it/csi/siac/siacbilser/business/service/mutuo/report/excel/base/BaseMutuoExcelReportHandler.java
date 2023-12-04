/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.mutuo.report.excel.base;

import it.csi.siac.siacbilser.business.service.excel.base.BaseExcelReportHandler;
import it.csi.siac.siacbilser.model.mutuo.Mutuo;

public abstract class BaseMutuoExcelReportHandler
	<R extends BaseMutuoExcelRow> 
		extends BaseExcelReportHandler {
	
	protected Mutuo mutuo;

	
	public void setMutuo(Mutuo mutuo) {
		this.mutuo = mutuo;
	}
}

