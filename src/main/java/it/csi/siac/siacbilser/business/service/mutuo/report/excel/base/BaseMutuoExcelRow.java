/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.mutuo.report.excel.base;

import javax.xml.bind.annotation.XmlType;

import it.csi.siac.siacbilser.business.service.excel.base.BaseExcelRow;
import it.csi.siac.siacbilser.model.BILDataDictionary;

@XmlType(namespace = BILDataDictionary.NAMESPACE)
public abstract class BaseMutuoExcelRow implements BaseExcelRow {
}
