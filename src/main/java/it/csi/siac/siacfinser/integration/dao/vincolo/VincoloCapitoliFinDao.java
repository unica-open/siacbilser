/**
 * SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
 * SPDX-License-Identifier: EUPL-1.2
 */
package it.csi.siac.siacfinser.integration.dao.vincolo;

import java.util.List;

import it.csi.siac.siaccommonser.integration.dao.base.Dao;
import it.csi.siac.siacfinser.integration.entity.SiacTVincoloFin;

public interface VincoloCapitoliFinDao extends Dao<SiacTVincoloFin, Integer> {

	//SIAC-7349 Inizio  SR180 FL 02/04/2020
	public List<SiacTVincoloFin> ricercaSinteticaVincoloCapitoliByCapitolo(Integer enteProprietarioId, Integer uidCapitolo);
	//SIAC-7349 Fine  SR180 FL 02/04/2020
	
}
