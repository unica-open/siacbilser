/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.csi.siac.siacbilser.integration.entity.SiacTElencoDoc;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocFamTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDElencoDocStatoEnum;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;

/**
 * The Interface  ElencoDocumentiAllegatoAttoDao.
 */
public interface ElencoDocumentiAllegatoDao extends Dao<SiacTElencoDoc, Integer> {
	
	
	/**
	 * Crea una SiacTElencoDoc.
	 *
	 * @param c la SiacTElencoDoc da inserire
	 * 
	 * @return la SiacTElencoDoc inserita
	 */
	SiacTElencoDoc create(SiacTElencoDoc c);

	/**
	 * Aggiorna una SiacTElencoDoc.
	 *
	 * @param c la SiacTElencoDoc da aggiornare
	 * 
	 * @return la SiacTElencoDoc aggiornata
	 */
	SiacTElencoDoc update(SiacTElencoDoc c);

//	/**
//	 * Effettua la ricerca sintetica paginata con i filtri passati come parametro.
//	 *
//	 * @return la lista paginata di SiacTElencoDoc
//	 */
//	Page<SiacTElencoDoc> ricercaSinteticaElencoDocumentiAllegatoAtto(int enteProprietarioId, 
//
//			Pageable pageable);
	
	/**
	 * Effettua la ricerca puntuale con i filtri passati come parametro.
	 *
	 * @return SiacTElencoDoc
	 */
	SiacTElencoDoc ricercaPuntualeElencoDocumentiAllegatoAtto(int enteProprietarioId, 
			Integer uidAnnoEsercizio,
			Integer annoElenco,
			Integer numeroElenco,
			Integer uidAttoAmministrativo,
			List<SiacDElencoDocStatoEnum> stati
			);

	/**
	 * Effettua la ricerca sintetica a partire dai parametri in input.
	 * 
	 * @param uidEnte
	 * @param anno
	 * @param numero
	 * @param annoEsterno
	 * @param numeroEsterno
	 * @param dataTrasmissione
	 * @param siacDElencoDocStati
	 * @param uidAttoAmministrativo
	 * @param pageable
	 * @return
	 */
	Page<SiacTElencoDoc> ricercaSinteticaElenco(Integer uidEnte, Integer anno, Integer numero, Integer annoEsterno, String numeroEsterno,
			Date dataTrasmissione, Set<SiacDElencoDocStatoEnum> siacDElencoDocStati, Integer uidAttoAmministrativo, Integer numeroElencoDa, Integer numeroElencoA, Pageable pageable);

	/**
	 * Calcola il totale da pagare a partire dai parametri in input.
	 * 
	 * @param uidEnte
	 * @param anno
	 * @param numero
	 * @param annoEsterno
	 * @param numeroEsterno
	 * @param dataTrasmissione
	 * @param siacDElencoDocStati
	 * @param uidAttoAmministrativo
	 * @return
	 */
	BigDecimal calcolaTotaleDaPagare(Integer uidEnte, Integer anno, Integer numero, Integer annoEsterno, String numeroEsterno,
			Date dataTrasmissione, Set<SiacDElencoDocStatoEnum> siacDElencoDocStati, Integer uidAttoAmministrativo);
	
	/**
	 * Calcola il totale da incassare a partire dai parametri in input.
	 * 
	 * @param uidEnte
	 * @param anno
	 * @param numero
	 * @param annoEsterno
	 * @param numeroEsterno
	 * @param dataTrasmissione
	 * @param siacDElencoDocStati
	 * @param uidAttoAmministrativo
	 * @return
	 */
	BigDecimal calcolaTotaleDaIncassare(Integer uidEnte, Integer anno, Integer numero, Integer annoEsterno, String numeroEsterno,
			Date dataTrasmissione, Set<SiacDElencoDocStatoEnum> siacDElencoDocStati, Integer uidAttoAmministrativo);
	
	/**
	 * Calcola il totale delle entrate collegate a partire dai parametri in input.
	 * 
	 * @param uidEnte
	 * @param anno
	 * @param numero
	 * @param annoEsterno
	 * @param numeroEsterno
	 * @param dataTrasmissione
	 * @param siacDElencoDocStati
	 * @param uidAttoAmministrativo
	 * @param siacDDocFamTipi
	 * @return
	 */
	BigDecimal calcolaTotaleQuoteCollegate(Integer uidEnte, Integer anno, Integer numero, Integer annoEsterno, String numeroEsterno,
			Date dataTrasmissione, Set<SiacDElencoDocStatoEnum> siacDElencoDocStati, Integer uidAttoAmministrativo, Set<SiacDDocFamTipoEnum> siacDDocFamTipi);

	/**
	 * 
	 * @param uidElenco
	 * @param pageable
	 * @return
	 */
	Page<SiacTSubdoc> ricercaSinteticaQuoteElenco(Integer uidElenco,Integer soggettoId,String soggettoCode,Integer enteId, Pageable pageable);
	
}
