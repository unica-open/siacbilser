/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.cespiti;

import it.csi.siac.siacbilser.integration.entity.SiacDCespitiClassificazioneGiuridica;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;


/**
 * The Interface CespitiClassificazioneGiuridicaDao.
 *
 * @author Antonino
 */
public interface CespitiClassificazioneGiuridicaDao extends Dao<SiacDCespitiClassificazioneGiuridica, Integer> {
	
	
	/**
	 * Creates the.
	 *
	 * @param r the r
	 * @return the siac t gsa classif
	 */
	SiacDCespitiClassificazioneGiuridica create(SiacDCespitiClassificazioneGiuridica r);


	SiacDCespitiClassificazioneGiuridica update(SiacDCespitiClassificazioneGiuridica r);

	/*
	Page<SiacTGsaClassif> ricercaSinteticaGsaClassif(Integer uidEnte, SiacDAmbitoEnum siacDAmbitoEnum, String codice, String descrizione, 
			SiacDGsaClassifStatoEnum siacDGsaClassifStatoEnum, Pageable pageable);
	*/

}
