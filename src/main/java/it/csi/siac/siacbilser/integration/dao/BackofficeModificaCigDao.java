/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import it.csi.siac.siacbilser.integration.entity.SiacTMovgest;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;

/**
 * The interface BackofficeModificaCigDao
 */
public interface BackofficeModificaCigDao extends Dao<SiacTMovgest, Integer> {
	
	// Evolutive BackofficeModificaCigRemedy - String numeroRemedy
	Integer backofficeModificaCigMovgest(int uid, Integer uidTipoDebito, String cig, Integer uidMotivazioneAssenzaCig, String numeroRemedy);

	Integer backofficeModificaCigCollegati(int uid, Integer uidTipoDebito, String cig, Integer uidMotivazioneAssenzaCig, String numeroRemedy);
	
	
}
