/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.math.BigDecimal;

import it.csi.siac.siacbilser.integration.entity.SiacTProvCassa;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;

/**
 * The Interface DocumentoDao.
 */
public interface ProvvisorioDiCassaBilDao extends Dao<SiacTProvCassa, Integer> {
	
	
	BigDecimal calcolaImportoDaRegolarizzare(Integer provcId);
}
