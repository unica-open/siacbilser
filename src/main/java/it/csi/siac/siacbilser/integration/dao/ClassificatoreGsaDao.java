/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.csi.siac.siacbilser.integration.entity.SiacTGsaClassif;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDAmbitoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDGsaClassifStatoEnum;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;


/**
 * The Interface GsaClassifDao.
 *
 * @author Domenico
 */
public interface ClassificatoreGsaDao extends Dao<SiacTGsaClassif, Integer> {
	
	
	/**
	 * Creates the.
	 *
	 * @param r the r
	 * @return the siac t gsa classif
	 */
	SiacTGsaClassif create(SiacTGsaClassif r);


	SiacTGsaClassif update(SiacTGsaClassif r);
	
	/**
	 * Ricerca sintetica conto.
	 *
	 * @param uidEnte the uid ente
	 * @param siacDAmbitoEnum the siac D ambito enum
	 * @param codice the codice
	 * @param descrizione the descrizione
	 * @param uidGsaClassifPadre the uid gsa classif padre
	 * @param livello the livello
	 * @param siacDGsaClassifStatoEnum the siac D gsa classif stato enum
	 * @param pageable the pageable
	 * @return the page
	 */
	Page<SiacTGsaClassif> ricercaSinteticaGsaClassif(Integer uidEnte, SiacDAmbitoEnum siacDAmbitoEnum, String codice, String descrizione, 
			SiacDGsaClassifStatoEnum siacDGsaClassifStatoEnum, Pageable pageable);
	
	/**
	 * Ricerca sintetica conto.
	 *
	 * @param uidEnte the uid ente
	 * @param siacDAmbitoEnum the siac D ambito enum
	 * @param codice the codice
	 * @param descrizione the descrizione
	 * @param uidGsaClassifPadre the uid gsa classif padre
	 * @param livello the livello
	 * @param siacDGsaClassifStatoEnum the siac D gsa classif stato enum
	 * @param pageable the pageable
	 * @return the page
	 */
	List<SiacTGsaClassif> ricercaGsaClassif(Integer uidEnte, SiacDAmbitoEnum siacDAmbitoEnum, String codice, String descrizione, SiacDGsaClassifStatoEnum siacDGsaClassifStatoEnum);
	
}
