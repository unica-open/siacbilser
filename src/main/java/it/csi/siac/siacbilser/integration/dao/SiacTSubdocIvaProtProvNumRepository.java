/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTSubdocIvaProtProvNum;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdocIvaProtProvNumNoVersion;

/**
 * Repository per l'entity SiacTSubdocIvaProtProvNum.
 *
 */
public interface SiacTSubdocIvaProtProvNumRepository extends JpaRepository<SiacTSubdocIvaProtProvNum, Integer> {
	

	/**
	 * Ricerca SiacTSubdocIvaProtProvNum per registro e periodo
	 * 
	 * @param ivaregId uid del registro
	 * @param periodoId uid del periodo
	 * 
	 * @return SiacTSubdocIvaProtProvNum
	 */
	@Query("FROM SiacTSubdocIvaProtProvNum "
			+ " WHERE siacTIvaRegistro.ivaregId = :ivaregId "
			+ " AND siacTPeriodo.periodoId = :periodoId "
			+ " AND dataCancellazione IS NULL"
			)
	SiacTSubdocIvaProtProvNum findByIvaRegPeriodo(@Param("ivaregId") Integer ivaRegId,@Param("periodoId") Integer periodoId );
	
	
	/**
	 * Ricerca SiacTSubdocIvaProtProvNumNoVersion per registro e periodo
	 * 
	 * @param ivaregId uid del registro
	 * @param periodoId uid del periodo
	 * 
	 * @return SiacTSubdocIvaProtProvNumNoVersion
	 */
	@Query("FROM SiacTSubdocIvaProtProvNumNoVersion "
			+ " WHERE siacTIvaRegistro.ivaregId = :ivaregId "
			+ " AND siacTPeriodo.periodoId = :periodoId "
			+ " AND dataCancellazione IS NULL"
			)
	SiacTSubdocIvaProtProvNumNoVersion findByIvaRegPeriodoNoVersion(@Param("ivaregId") Integer ivaregId,@Param("periodoId") Integer periodoId );


	/**
	 *  Ricerca SiacTSubdocIvaProtProvNum per registro, anno, tipo periodo e ente proprietatario
	 * 
	 * @param ivaregId uid del registro iva
	 * @param anno l'anno di esercizio 
	 * @param periodoTipoCode codice del tipo periodo
	 * @param enteProprietarioId uid dell'ente proprietario
	 * 
	 * @return  SiacTSubdocIvaProtProvNum
	 */
	@Query("FROM SiacTSubdocIvaProtProvNum "
			+ " WHERE siacTIvaRegistro.ivaregId = :ivaregId "
			+ " AND siacTPeriodo.anno = :anno "
			+ " AND siacTPeriodo.siacDPeriodoTipo.periodoTipoCode = :periodoTipoCode "
			+ " AND siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND dataCancellazione IS NULL "
			)
	SiacTSubdocIvaProtProvNum findByIvaRegAnnoEsercizioPeriodoTipoCodiceEnte(@Param("ivaregId") Integer ivaregId, @Param("anno") String string,
			@Param("periodoTipoCode") String periodoTipoCode, @Param("enteProprietarioId") Integer enteProprietarioId);
	


	
}
