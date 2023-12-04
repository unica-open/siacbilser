/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import java.util.List;

import it.csi.siac.siacbilser.integration.entity.SiacTIvaAttivita;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;

/**
 * The Interface GruppoAttivitaIvaDao.
 */
public interface AttivitaIvaDao extends Dao<SiacTIvaAttivita, Integer> {
	
	
	/**
	 * Cerca attivita iva filtrando con i parametri passati in input.
	 * 
	 * @param enteProprietarioId uid dell'ente proprietario
	 * @param ivaattCode codice attivita iva
	 * @param ivaattDesc descrizione attivita iva
	 * 
	 * @return la lista di SiacTIvaAttivita trovata
	 */
	List<SiacTIvaAttivita> ricercaAttivitaIva(Integer enteProprietarioId, 
													String ivaattCode, 
													String ivaattDesc);

	
}
