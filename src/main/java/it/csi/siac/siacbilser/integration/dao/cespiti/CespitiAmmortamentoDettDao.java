/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.cespiti;

import it.csi.siac.siacbilser.integration.entity.SiacTCespitiAmmortamento;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;


/**
 * The Interface CategoriaCespitiDao.
 *
 * @author Antonino
 */
public interface CespitiAmmortamentoDettDao extends Dao<SiacTCespitiAmmortamento, Integer> {
	
	
	/**
	 * Creates the.
	 *
	 * @param r the r
	 * @return the siac t gsa classif
	 */
	SiacTCespitiAmmortamento create(SiacTCespitiAmmortamento r);


	SiacTCespitiAmmortamento update(SiacTCespitiAmmortamento r);

	/*
	Page<SiacTGsaClassif> ricercaSinteticaGsaClassif(Integer uidEnte, SiacDAmbitoEnum siacDAmbitoEnum, String codice, String descrizione, 
			SiacDGsaClassifStatoEnum siacDGsaClassifStatoEnum, Pageable pageable);
	*/

}
