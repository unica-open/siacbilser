/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.csi.siac.siacbilser.integration.entity.SiacDCausale;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCausaleFamTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDModelloEnum;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;

/**
 * The Interface CausaleDao.
 */
public interface CausaleDao extends Dao<SiacDCausale, Integer> {
	
	/**
	 * Crea una SiacDCausale.
	 *
	 * @param c la SiacDCausale da inserire
	 * 
	 * @return la SiacDCausale inserita
	 */
	SiacDCausale create(SiacDCausale c);

	/**
	 * Aggiorna una SiacDCausale.
	 *
	 * @param c la SiacDCausale da aggiornare
	 * 
	 * @return la SiacDCausale aggiornata
	 */
	SiacDCausale update(SiacDCausale c);
	
	/**
	 * Cancella una SiacDCausale.
	 *
	 * @param c la SiacDCausale da cancellare
	 * 
	 */
	void delete(SiacDCausale c);

	/**
	 * Effettua la ricerca sintetica paginata con i filtri passati come parametro.
	 *
	 * @param enteProprietarioId uid dell'ente proprietario
	 * @param causFamTipoCode the caus fam tipo code
	 * @param struttId the strutt id
	 * @param causTipoId the caus tipo id
	 * @param statoOperativoCausale the stato operativo causale
	 * @param elemId the elem id
	 * @param movgestTsId the movgest ts id
	 * @param soggettoId the soggetto id
	 * @param attoammId the attoamm id
	 * @param modpagId the modpag id
	 * @param sedeSecondariaId the sede secondaria id
	 * @param pageable the pageable
	 * 
	 * @return la lista paginata di SiacDCausale
	 */
	Page<SiacDCausale> ricercaSinteticaCausale(int enteProprietarioId, 
			SiacDCausaleFamTipoEnum causFamTipoCode,
			String causCode,
			String causDesc,
			Integer struttId,
			Integer causTipoId,
			Boolean statoOperativoCausale,
			Integer elemId, 
			Integer movgestId,
			Integer movgestTsId,
			Integer soggettoId,
			Integer attoammId,
			Integer modpagId,
			Integer sedeSecondariaId,
		    Pageable pageable);
	
	/**
	 * Ricerca le causali a partire da Ente, Tipo Causale e Modello.
	 * 
	 * @param enteProprietarioId the ente proprietario id
	 * @param onereId the pnere id
	 * @param siacDModelloEnum the siac d modello enum
	 * 
	 * @return the siac d causales
	 */
	List<SiacDCausale> ricercaCausaliByEnteProprietarioAndTipoCausaleAndModello(Integer enteProprietarioId, Integer onereId, SiacDModelloEnum siacDModelloEnum);


}
