/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.csi.siac.siacbilser.integration.entity.SiacRConciliazioneBeneficiario;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;


/**
 * The Interface ConciliazionePerBeneficiarioDao.
 *
 * @author Marchino Alessandro
 * @version 1.0.0 - 27/10/2015
 */
public interface ConciliazionePerBeneficiarioDao extends Dao<SiacRConciliazioneBeneficiario, Integer> {
	
	SiacRConciliazioneBeneficiario create(SiacRConciliazioneBeneficiario r);

	SiacRConciliazioneBeneficiario update(SiacRConciliazioneBeneficiario r);

	void elimina(SiacRConciliazioneBeneficiario siacRConciliazioneBeneficiario);

	Page<SiacRConciliazioneBeneficiario> ricercaSinteticaConciliazioniPerBeneficiario(
			int enteProprietarioId, 
			Integer soggettoId,
			String elemTipoCode,
			String annoCapitolo,
			String numeroCapitolo, 
			String numeroArticolo, 
			String numeroUEB, 
			Pageable pageable);
	
}
