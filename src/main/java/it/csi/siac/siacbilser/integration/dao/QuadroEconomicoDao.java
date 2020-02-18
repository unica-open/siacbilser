/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.csi.siac.siacbilser.integration.entity.SiacTQuadroEconomico;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDQuadroEconomicoParteEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDQuadroEconomicoStatoEnum;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;


/**
 * The Interface QuadroEconomicoDao.
 *
 * @author Domenico
 */
public interface QuadroEconomicoDao extends Dao<SiacTQuadroEconomico, Integer> {
	
	
	/**
	 * Creates the.
	 *
	 * @param r the r
	 * @return the siac t gsa classif
	 */
	SiacTQuadroEconomico create(SiacTQuadroEconomico r);


	SiacTQuadroEconomico update(SiacTQuadroEconomico r);
	
	/**
	 * Ricerca sintetica conto.
	 *
	 * @param uidEnte the uid ente
	 * @param siacDAmbitoEnum the siac D ambito enum
	 * @param codice the codice
	 * @param descrizione the descrizione
	 * @param uidQuadroEconomicoPadre the uid gsa classif padre
	 * @param livello the livello
	 * @param siacDQuadroEconomicoStatoEnum the siac D gsa classif stato enum
	 * @param pageable the pageable
	 * @return the page
	 */
	Page<SiacTQuadroEconomico> ricercaSinteticaQuadroEconomico(Integer uidEnte,  String codice, String descrizione, 
			SiacDQuadroEconomicoStatoEnum siacDQuadroEconomicoStatoEnum,SiacDQuadroEconomicoParteEnum siacDQuadroEconomicoParteEnum, Pageable pageable);
	
	/**
	 * Ricerca sintetica conto.
	 *
	 * @param uidEnte the uid ente
	 * @param siacDAmbitoEnum the siac D ambito enum
	 * @param codice the codice
	 * @param descrizione the descrizione
	 * @param uidQuadroEconomicoPadre the uid gsa classif padre
	 * @param livello the livello
	 * @param siacDQuadroEconomicoStatoEnum the siac D gsa classif stato enum
	 * @param pageable the pageable
	 * @return the page
	 */
	List<SiacTQuadroEconomico> ricercaQuadroEconomico(Integer uidEnte,  String codice, String descrizione, SiacDQuadroEconomicoStatoEnum siacDQuadroEconomicoStatoEnum,SiacDQuadroEconomicoParteEnum siacDQuadroEconomicoParteEnum);
	/**
	 * 
	 * @param quadroEconomicoCode
	 * @param parteCode
	 * @param quadroEconomicoPadreId
	 * @param enteProprietarioId
	 * @return
	 */
	public List<SiacTQuadroEconomico> findSiacTQuadroEconomicoValidoByCodeAndParte(String quadroEconomicoCode,String parteCode, Integer quadroEconomicoPadreId , Integer enteProprietarioId);

}
