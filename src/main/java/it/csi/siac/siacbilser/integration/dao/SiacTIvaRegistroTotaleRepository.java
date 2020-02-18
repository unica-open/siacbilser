/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTIvaRegistroTotale;

/**
 * Repository per l'entity SiacTSubdocIvaProtProvNum.
 *
 */
public interface SiacTIvaRegistroTotaleRepository extends JpaRepository<SiacTIvaRegistroTotale, Integer> {

	/**
	 * Trova una SiacTIvaRegistroTotale corrispondente a parametri passati in input.
	 * 
	 * @param ivaregId uid del registro iva
	 * @param ivaaliquotaId uid dell'aliquota iva
	 * @param periodoId id del periodo
	 * @param enteProprietarioId uid dell'ente proprietario
	 * 
	 * @return la SiacTIvaRegistroTotale trovata
	 */ 
	@Query( "FROM SiacTIvaRegistroTotale t " +
			" WHERE t.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
			" AND t.dataCancellazione IS NULL " +
			" AND t.siacTIvaRegistro.ivaregId = :ivaregId " +
			" AND t.siacTIvaAliquota.ivaaliquotaId = :ivaaliquotaId " +
			" AND t.siacTPeriodo.periodoId = :periodoId"
			)
	SiacTIvaRegistroTotale findByRegistroAndAliquotaIvaAndPeriodoAndEnte(@Param("ivaregId") Integer ivaregId,
			@Param("ivaaliquotaId") Integer ivaaliquotaId, @Param("periodoId") Integer periodoId, @Param("enteProprietarioId") Integer enteProprietarioId);
	
	
//	/**
//	 * Calcola i totali di totimponibiledef, totivadef, totimponibileprovv, totivaprovv per i registri di un dato gruppo,
//	 * di un dato tipo e con una aliquota richiesta.
//	 * 
//	 * @param enteProprietarioId uid dell'ente proprietario
//	 * @param ivagruId uid del gruppo iva
//	 * @param ivaregTipoCode codice del tipo di registro
//	 * @param ivaaliquotaId uid dall'aliquota iva
//	 * 
//	 * @return i totali calcolati
//	 */
//	@Query( " SELECT "
//			+ "COALESCE(SUM(rtot.totimponibiledef),0) AS totimpdef, "
//			+ "COALESCE(SUM(rtot.totivadef),0) AS totivadef, "
//			+ "COALESCE(SUM(rtot.totimponibileprovv),0) AS totimpprovv, "
//			+ "COALESCE(SUM(rtot.totivaprovv),0) AS totivaprovv" +
//			" FROM SiacTIvaRegistroTotale rtot, SiacTIvaRegistro ir "+
//			" WHERE rtot.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
//			" AND rtot.dataCancellazione IS NULL " + 
//			" AND rtot.siacTIvaRegistro.siacDIvaRegistroTipo.ivaregTipoCode = :ivaregTipoCode " +
//			" AND rtot.siacTIvaAliquota.ivaaliquotaId = :ivaaliquotaId " +
//			" AND rtot.siacTIvaRegistro = ir " +
//			" AND EXISTS ( " +
//			"   FROM ir.siacRIvaRegistroGruppos rgruppo " +
//			" 	WHERE rgruppo.siacTIvaGruppo.ivagruId = :ivagruId " + 
//			"	AND rgruppo.dataCancellazione IS NULL " + 
//			" ) " )
//	Object[] calcolaTotaliByIvaGruppoAndIvaTipoAndIvaAliquota(@Param("enteProprietarioId") Integer enteProprietarioId,
//			@Param("ivagruId") Integer ivagruId, @Param("ivaregTipoCode") String ivaregTipoCode, @Param("ivaaliquotaId") Integer ivaaliquotaId);


/**
* Calcola i totali di totimponibiledef, totivadef, totimponibileprovv, totivaprovv per i registri di un dato gruppo,
* di un dato tipo e con una aliquota richiesta. e un preciso anno
* 
* @param enteProprietarioId uid dell'ente proprietario
* @param ivagruId uid del gruppo iva
* @param ivaregTipoCode codice del tipo di registro
* @param ivaaliquotaId uid dall'aliquota iva
* @param anno di riferimento
* 
* @return i totali calcolati
*/
@Query( " SELECT "
//		+ "COALESCE(SUM(rtot.totimponibiledef),0) AS totimpdef, "
//		+ "COALESCE(SUM(rtot.totivadef),0) AS totivadef, "
//		+ "COALESCE(SUM(rtot.totimponibileprovv),0) AS totimpprovv, "
//		+ "COALESCE(SUM(rtot.totivaproavv),0) AS totivaprovv" +
		
		+ "rtot.totimponibiledef AS totimpdef, "
		+ "rtot.totivadef AS totivadef, "
		+ "rtot.totimponibileprovv AS totimpprovv, "
		+ "rtot.totivaprovv AS totivaprovv, "
		+ "rtot.siacTPeriodo.periodoId AS periodoid " +
		
		" FROM SiacTIvaRegistroTotale rtot, SiacTIvaRegistro ir "+
		" WHERE rtot.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
		" AND rtot.dataCancellazione IS NULL " + 
		" AND rtot.siacTIvaRegistro.siacDIvaRegistroTipo.ivaregTipoCode = :ivaregTipoCode " +
		" AND rtot.siacTIvaAliquota.ivaaliquotaId = :ivaaliquotaId " +
		" AND rtot.siacTIvaRegistro = ir " +
		" AND EXISTS ( " +
		"   FROM ir.siacRIvaRegistroGruppos rgruppo " +
		" 	WHERE rgruppo.siacTIvaGruppo.ivagruId = :ivagruId " + 
		"	AND rgruppo.dataCancellazione IS NULL " + 
		" ) "
		+ " AND rtot.siacTPeriodo IN ( "
		+ "     FROM SiacTPeriodo tp "
		+ "     WHERE tp.anno = :anno "
		+ " ) "
		+ "ORDER BY rtot.siacTPeriodo.dataFine DESC "
		//+ "LIMIT 1 "
		)
List<Object[]> calcolaTotaliByIvaGruppoAndIvaTipoAndIvaAliquotaAndAnno(@Param("enteProprietarioId") Integer enteProprietarioId,
		@Param("ivagruId") Integer ivagruId, @Param("ivaregTipoCode") String ivaregTipoCode, @Param("ivaaliquotaId") Integer ivaaliquotaId,
		@Param("anno") String anno, Pageable page);


}
