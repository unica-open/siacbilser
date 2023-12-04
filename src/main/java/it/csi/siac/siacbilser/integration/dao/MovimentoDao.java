/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.csi.siac.siacbilser.integration.entity.SiacTMovimento;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;

/**
 * The Interface MovimentoDao.
 * 
 * @author Marchino Alessandro
 */
public interface MovimentoDao extends Dao<SiacTMovimento, Integer> {
	
	/**
	 * Ricerca sintetica del movimento per la stampa
	 * @param movtDataInizio la data di inizio dei movimenti
	 * @param movtDataFine la data di fine dei movimenti
	 * @param cassaeconId l'id della cassa economale
	 * @param enteProprietarioId l'id dell'ente
	 * @param bilId l'id del bilancio
	 * @param pageable il paginatore
	 * @return la pagina dei movimenti
	 */
	Page<SiacTMovimento> ricercaSinteticaMovimentoStampa(
			Date movtDataInizio,
			Date movtDataFine,
			Integer cassaeconId,
			Integer enteProprietarioId,
			Integer bilId,
			Pageable pageable);
	
	/**
	 * Ricerca sintetica del totale dei movimenti per la stampa.
	 *
	 * @param movtDataInizio la data di inizio dei movimenti
	 * @param movtDataFine la data di fine dei movimenti
	 * @param cassaeconId l'id della cassa economale
	 * @param enteProprietarioId l'id dell'ente
	 * @param bilId l'id del bilancio
	 * @param codiciStatoDaEscludere the codici stato da escludere
	 * @return il totale
	 */
	BigDecimal totaleRicercaSinteticaMovimentoStampa(
			Date movtDataInizio,
			Date movtDataFine,
			Integer cassaeconId,
			Integer enteProprietarioId,
			Integer bilId, 
			List<String> codiciStatoDaEscludere);

	
}
