/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTSubdocIvaProtDefNum;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdocIvaProtDefNumNoVersion;

/**
 * Repository per l'entity SiacTSubdocIvaProtDefNum.
 *
 */
public interface SiacTSubdocIvaProtDefNumRepository extends JpaRepository<SiacTSubdocIvaProtDefNum, Integer> {
	
	
	/**
	 * Ricerca SiacTSubdocIvaProtDefNum per registro e periodo
	 * 
	 * @param ivaregId uid del registro
	 * @param periodoId uid del periodo
	 * 
	 * @return SiacTSubdocIvaProtDefNum
	 */
	@Query("FROM SiacTSubdocIvaProtDefNum "
			+ "WHERE siacTIvaRegistro.ivaregId = :ivaregId "
			+ "AND siacTPeriodo.periodoId = :periodoId "
			+ " AND dataCancellazione IS NULL "
			)
	SiacTSubdocIvaProtDefNum findByIvaRegPeriodo(@Param("ivaregId") Integer ivaregId,@Param("periodoId") Integer periodoId);
	

	/**
	 * Ricerca SiacTSubdocIvaProtDefNumNoVersion per registro e periodo
	 * 
	 * @param ivaregId uid del registro
	 * @param periodoId uid del periodo
	 * 
	 * @return SiacTSubdocIvaProtDefNumNoVersion
	 */
	@Query("FROM SiacTSubdocIvaProtDefNumNoVersion "
			+ "WHERE siacTIvaRegistro.ivaregId = :ivaregId "
			+ "AND siacTPeriodo.periodoId = :periodoId "
			+ " AND dataCancellazione IS NULL "
			)
	SiacTSubdocIvaProtDefNumNoVersion findByIvaRegPeriodoNoVersion(@Param("ivaregId") Integer ivaregId,@Param("periodoId") Integer periodoId);


	/**
	 *  Ricerca SiacTSubdocIvaProtDefNum per registro, anno, tipo periodo e ente proprietatario
	 * 
	 * @param ivaregId uid del registro iva
	 * @param anno l'anno di esercizio 
	 * @param periodoTipoCode codice del tipo periodo
	 * @param enteProprietarioId uid dell'ente proprietario
	 * 
	 * @return  SiacTSubdocIvaProtDefNum
	 */
	@Query("FROM SiacTSubdocIvaProtDefNum "
			+ " WHERE siacTIvaRegistro.ivaregId = :ivaregId "
			+ " AND siacTPeriodo.anno = :anno "
			+ " AND siacTPeriodo.siacDPeriodoTipo.periodoTipoCode = :periodoTipoCode "
			+ " AND siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND dataCancellazione IS NULL "
			)
	SiacTSubdocIvaProtDefNum findByIvaRegAnnoEsercizioPeriodoTipoCodiceEnte(@Param("ivaregId") Integer ivaregId, @Param("anno") String anno,
			@Param("periodoTipoCode") String periodoTipoCode, @Param("enteProprietarioId") Integer enteProprietarioId);
	
}
