/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.base;

public interface JpaManagementDao {
	
	public void flush();
	public void flushAndClear();

}
