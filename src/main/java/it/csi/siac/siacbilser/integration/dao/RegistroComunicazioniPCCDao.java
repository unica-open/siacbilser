/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import it.csi.siac.siacbilser.integration.entity.SiacTRegistroPcc;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;

public interface RegistroComunicazioniPCCDao extends Dao<SiacTRegistroPcc, Integer> {
	
	SiacTRegistroPcc create(SiacTRegistroPcc c);
	
}
