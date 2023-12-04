/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.csi.siac.siacbilser.integration.entity.SiacTPdceConto;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDAmbitoEnum;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;


/**
 * The Interface ContoDao.
 *
 * @author Domenico
 */
public interface ContoDao extends Dao<SiacTPdceConto, Integer> {
	
	
	/**
	 * Creates the.
	 *
	 * @param r the r
	 * @return the siac t pdce conto
	 */
	SiacTPdceConto create(SiacTPdceConto r);


	SiacTPdceConto update(SiacTPdceConto r);
	
	/**
	 * Annulla.
	 *
	 * @param r the r
	 */
	void annulla(SiacTPdceConto r);
	
	
	/**
	 * Annulla.
	 *
	 * @param siacTPdceConto the siacTPdceConto
	 */
	void elimina(SiacTPdceConto siacTPdceConto);


	/**
	 * Ricerca sintetica conto.
	 *
	 * @param uidEnte the uid ente
	 * @param anno the anno
	 * @param uidClassePiano the uid classe piano
	 * @param codiceInterno the codice interno
	 * @param codice the codice
	 * @param uidContoPadre the uid conto padre
	 * @param pageable the pageable
	 * @return the page
	 */
	Page<SiacTPdceConto> ricercaSinteticaConto(Integer uidEnte, SiacDAmbitoEnum siacDAmbitoEnum, Integer anno, Integer uidClassePiano, String codiceInterno, String codice, 
			Integer uidContoPadre, Integer livello, Integer classifId, Boolean ammortamento, Boolean contoFoglia, Boolean attivo, Integer uidTipoConto, String codiceTipoConto,Pageable pageable);


	/**
	 * Ricerca tutti i figli di un conto.
	 *
	 * @param uidConto the uid conto
	 * @return the list
	 */
	List<SiacTPdceConto> ricercaFigliRicorsiva(Integer uidConto);


	
	
}
