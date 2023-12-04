/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.List;

import it.csi.siac.siaccommonser.integration.dao.base.Dao;


/**
 * The Interface CronoprogrammaDao.
 */
public interface ControlloImportiImpegniVincolatiDao extends Dao<String, Integer> {

	List<Object[]> controlloImportiImpegniVincolati(String listAttoAllId);

}
