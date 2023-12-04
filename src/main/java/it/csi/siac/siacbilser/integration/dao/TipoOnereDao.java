/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.csi.siac.siacbilser.integration.entity.SiacDOnere;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;

/**
 * The Interface DocumentoDao.
 */
public interface TipoOnereDao extends Dao<SiacDOnere, Integer> {
	
	/**
	 * Crea una SiacDOnere.
	 *
	 * @param o la SiacDOnere da inserire
	 * @return la SiacDOnere inserita
	 */
	SiacDOnere create(SiacDOnere o);

	/**
	 * Aggiorna una SiacDOnere.
	 *
	 * @param o a SiacDOnere da aggiornare
	 * @return la SiacDOnere aggiornata
	 */
	SiacDOnere update(SiacDOnere o);

	Page<SiacDOnere> ricercaSinteticaTipiOnere(Integer onereTipoId, Integer enteProprietrioId, Boolean corsoDiValidita, Pageable pageable);

	

}
