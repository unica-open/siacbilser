/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.csi.siac.siacbilser.integration.entity.SiacTIvaGruppo;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;

/**
 * The Interface GruppoAttivitaIvaDao.
 */
public interface GruppoAttivitaIvaDao extends Dao<SiacTIvaGruppo, Integer> {
	
	/**
	 * Crea una siacTIvaGruppo
	 *
	 * @param siacTIvaGruppo la siacTIvaGruppo da inserire
	 * 
	 * @return la siacTIvaGruppo inserita
	 */
	SiacTIvaGruppo create(SiacTIvaGruppo siacTIvaGruppo);

	/**
	 * Aggiorna una siacTIvaGruppo
	 *
	 * @param siacTIvaGruppo la siacTIvaGruppo da aggiornare
	 * 
	 * @return la siacTIvaGruppo aggiornata
	 */
	SiacTIvaGruppo update(SiacTIvaGruppo siacTIvaGruppo);
	
	
	/**
	 * Elimina una siacTIvaGruppo
	 *
	 * @param siacTIvaGruppo la siacTIvaGruppo da eliminare
	 */
	void delete(SiacTIvaGruppo siacTIvaGruppo);

	/**
	 * Ricerca sintetica di un gruppo attivita iva.
	 *
	 * @param enteProprietarioId uid dell'ente proprietario
	 * @param ivagruproAnno anno esercizio
	 * @param ivagruCode codice del gruppo iva
	 * @param ivagruDesc descrizione del gruppo iva
	 * @param ivagruTipoCode codice del tipo gruppo iva
	 * @param pageable the pageable
	 * 
	 * @return la lista paginata di SiacTIvaGruppo
	 */
	Page<SiacTIvaGruppo> ricercaSinteticaGruppoAttivitaIva(Integer enteProprietarioId, 
												String ivagruCode,
												String ivagruDesc,
												String ivagruTipoCode,
												Pageable pageable);

	
}
