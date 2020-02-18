/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.List;

import it.csi.siac.siacbilser.integration.entity.SiacTCronop;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;


/**
 * The Interface CronoprogrammaDao.
 */
public interface CronoprogrammaDao extends Dao<SiacTCronop, Integer> {

	/**
	 * Crea una SiacTCronop.
	 *
	 * @param c la SiacTCronop da inserire
	 * 
	 * @return la SiacTCronop inserita
	 */
	SiacTCronop create(SiacTCronop c);
	
	/**
	 * Aggiorna una SiacTCronop.
	 *
	 * @param c la SiacTCronop da aggiornare
	 * 
	 * @return la SiacTCronop aggiornata
	 */
	SiacTCronop update(SiacTCronop c);
	
	/**
	 * Calcolo fpv entrata tramite la function fnc_siac_fpv_entrata_previsione
	 *
	 * @param uidCronoprogramma the uid cronoprogramma
	 * @param anno the anno
	 * @return the list
	 */
	List<Object[]> calcoloFpvEntrataPrevisione(Integer uidCronoprogramma);
	
	/**
	 * Calcolo fpv entrata tramite la function fnc_siac_fpv_entrata_previsione
	 *
	 * @param uidCronoprogramma the uid cronoprogramma
	 * @param anno the anno
	 * @return the list
	 */
	List<Object[]> calcoloFpvEntrataGestione(Integer uidCronoprogramma);

	/**
	 * Calcolo fpv entrata tramite la function fnc_siac_fpv_entrata_previsione
	 *
	 * @param uidCronoprogramma the uid cronoprogramma
	 * @param anno the anno
	 * @return the list
	 */
	List<Object[]> calcoloFpvSpesaPrevisione(Integer uidCronoprogramma);
	
	/**
	 * Calcolo fpv entrata tramite la function fnc_siac_fpv_entrata_previsione
	 *
	 * @param uidCronoprogramma the uid cronoprogramma
	 * @param anno the anno
	 * @return the list
	 */
	List<Object[]> calcoloFpvSpesaGestione(Integer uidCronoprogramma);

	/**
	 * Ricerca cronoprogramma by progetto.
	 *
	 * @param programmaId the programma id
	 * @param programmaCode the programma code
	 * @param programmaTipoCode the programma tipo code
	 * @param enteProprietarioId the ente proprietario id
	 * @return the list
	 */
	public List<SiacTCronop> ricercaCronoprogrammaByProgetto(Integer programmaId, String programmaCode, String programmaTipoCode, String anno, Integer enteProprietarioId);

}
