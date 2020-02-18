/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.csi.siac.siacbilser.integration.entity.SiacTIvaRegistro;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;

/**
 * The Interface RegistroIvaDao.
 */
public interface RegistroIvaDao extends Dao<SiacTIvaRegistro, Integer> {
	
	/**
	 * Crea una SiacTIvaRegistro
	 *
	 * @param siacTIvaRegistro la siacTIvaRegistro da inserire
	 * @return la siacTIvaRegistro inserita
	 */
	SiacTIvaRegistro create(SiacTIvaRegistro siacTIvaRegistro);

	/**
	 * Aggiorna una SiacTIvaRegistro
	 *
	 * @param siacTIvaRegistro la siacTIvaRegistro da aggiornare
	 * @return la siacTIvaRegistro aggiornata
	 */
	SiacTIvaRegistro update(SiacTIvaRegistro siacTIvaRegistro);
	
	
	/**
	 * Elimina una SiacTIvaRegistro
	 *
	 * @param siacTIvaRegistro la siacTIvaRegistro da eliminare
	 */
	void delete(SiacTIvaRegistro siacTIvaRegistro);

	
	/**
	 * Ricerca sintetica registro iva.
	 *
	 * @param enteProprietarioId uid dell'ente propriotetario
	 * @param ivagruId uid del gruppo iva
	 * @param ivaregTipoId uid del tipo registro iva
	 * @param ivaregCode codice del registro iva
	 * @param ivaregDesc descrizione del registro iva
	 * @param pageable  pageable
	 * @return lista paginata di SiacTIvaRegistro
	 */
	Page<SiacTIvaRegistro> ricercaSinteticaRegistroIva(Integer enteProprietarioId, 
												Integer ivagruId,
												Integer ivaregTipoId,
												String ivaregCode,
												String ivaregDesc,
												Pageable pageable);

	List<SiacTIvaRegistro> findByEnteProprietarioEGruppoETipo(Integer enteProprietarioId,
												Integer ivagruId,
												Integer ivaregTipoId,
												Collection<Integer> ivaattIds);

	
}
