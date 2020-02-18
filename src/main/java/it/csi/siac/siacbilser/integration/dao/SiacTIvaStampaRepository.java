/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTIvaStampa;
import it.csi.siac.siacbilser.integration.entity.SiacTPeriodo;

/**
 * The Interface SiacTIvaRegistroRepository.
 */
public interface SiacTIvaStampaRepository extends JpaRepository<SiacTIvaStampa, Integer> {
	
	
	/**
	 * Ricerca SiacTIvaStampa filtrando per registro iva, tipo di stampa iva e periodo
	 * 
	 * @param enteProprietarioId uid dell'ente proprietario
	 * @param ivaregId 			 uid del registro iva
	 * @param periodoId			 id periodo
	 * @param ivastTipoCode	     codice del tipo di stampa iva
	 * 
	 * @return la lista di SiacTIvaStampa trovata
	 */
	@Query( " SELECT s " +
			" FROM SiacTIvaStampa s " +
			" WHERE s.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
			" AND s.dataCancellazione IS NULL " +
			" AND EXISTS ( " +
			" 	FROM s.siacRIvaStampaRegistros sr " +
			" 	WHERE sr.siacTIvaRegistro.ivaregId = :ivaregId" +
			" 	AND sr.dataCancellazione IS NULL " +
			" ) " +
			" AND s.siacTPeriodo.periodoId = :periodoId " +
			" AND s.siacDIvaStampaTipo.ivastTipoCode = :ivastTipoCode")
	List<SiacTIvaStampa> findByRegistroEPeriodoETipoStampaIva(@Param("enteProprietarioId") Integer enteProprietarioId,
			@Param("ivaregId") Integer ivaregId, @Param("periodoId") Integer periodoId, @Param("ivastTipoCode") String ivastTipoCode);
	
	/**
	 * Ricerca SiacTIvaStampa filtrando per registro iva, tipo di stampa iva e periodo
	 * 
	 * @param enteProprietarioId uid dell'ente proprietario
	 * @param ivaregId 			 uid del registro iva
	 * @param periodoId			 id periodo
	 * @param ivastTipoCode	     codice del tipo di stampa iva
	 * 
	 * @return la lista di SiacTIvaStampa trovata
	 */
	@Query(" FROM SiacTIvaStampa tis "
			+ " WHERE tis.dataCancellazione IS NULL "
			+ " AND EXISTS ( "
			+ " 	FROM tis.siacRIvaStampaRegistros risr "
			+ " 	WHERE risr.siacTIvaRegistro.ivaregId = :ivaregId"
			+ " 	AND risr.dataCancellazione IS NULL "
			+ " ) "
			+ " AND EXISTS ( "
			+ "     FROM tis.siacRIvaStampaStatos riss "
			+ "     WHERE riss.dataCancellazione IS NULL "
			+ "     AND riss.siacDIvaStampaStato.ivastStatoCode = :ivastStatoCode "
			+ " ) "
			+ " AND tis.siacTPeriodo.periodoId = :periodoId "
			+ " AND tis.siacDIvaStampaTipo.ivastTipoCode = :ivastTipoCode")
	List<SiacTIvaStampa> findByRegistroEPeriodoETipoStampaIvaEStato(@Param("ivaregId") Integer ivaregId, @Param("periodoId") Integer periodoId,
			@Param("ivastTipoCode") String ivastTipoCode, @Param("ivastStatoCode") String ivastStatoCode);
	
	/**
	 * Ricerca SiacTIvaStampa filtrando per registro iva, tipo di stampa iva e periodo.
	 *
	 * @param ivaregId 			 uid del registro iva
	 * @param periodoId 		 id periodo
	 * @param ivastTipoCode      codice del tipo di stampa iva
	 * @param ivastStatoCode the ivast stato code
	 * @param anno the anno
	 * @return la lista di SiacTIvaStampa trovata
	 */
	@Query( " SELECT tis.siacTPeriodo "
			+ " FROM SiacTIvaStampa tis "
			+ " WHERE tis.dataCancellazione IS NULL "
			+ " AND EXISTS ( "
			+ " 	FROM tis.siacRIvaStampaRegistros risr "
			+ " 	WHERE risr.siacTIvaRegistro.ivaregId = :ivaregId"
			+ " 	AND risr.dataCancellazione IS NULL "
			+ " ) "
			+ " AND EXISTS ( "
			+ "     FROM tis.siacRIvaStampaStatos riss "
			+ "     WHERE riss.dataCancellazione IS NULL "
			+ "     AND riss.siacDIvaStampaStato.ivastStatoCode = :ivastStatoCode "
			+ " ) "
			+ " AND EXISTS (FROM tis.siacTIvaStampaValores r "       
			+ "             WHERE r.dataCancellazione IS NULL "
			+ "             AND   (r.flagincassati = 'S' OR  r.flagpagati = 'S')"
			+ "            )"                                      
			+ " AND tis.siacTPeriodo.anno = :anno "
			+ " AND tis.siacDIvaStampaTipo.ivastTipoCode = :ivastTipoCode "
			+ " ORDER BY tis.siacTPeriodo.siacDPeriodoTipo.periodoTipoCode desc "
			)
	List<SiacTPeriodo> findByRegistroTipoStampaIvaEAnnoEStatoPagatiOIncassati(@Param("ivaregId") Integer ivaregId, @Param("ivastTipoCode") String ivastTipoCode, 
			@Param("ivastStatoCode") String ivastStatoCode, @Param("anno") String anno);

}
