/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import it.csi.siac.siacbilser.integration.entity.SiacTBilElemDet;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;

public interface ImportiCapitoloDao extends Dao<SiacTBilElemDet, Integer> {
	
	SiacTBilElemDet create(SiacTBilElemDet s);
}
