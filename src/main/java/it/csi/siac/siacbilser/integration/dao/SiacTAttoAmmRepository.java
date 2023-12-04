/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacRAttoAmmStatoInOut;
import it.csi.siac.siacbilser.integration.entity.SiacTAttoAmm;
import it.csi.siac.siacbilser.integration.entity.SiacTEnteProprietario;

// TODO: Auto-generated Javadoc
/**
 * The Interface SiacTAttoAmmRepository.
 */

public interface SiacTAttoAmmRepository extends JpaRepository<SiacTAttoAmm, Integer> {
	
	/**
	 * Ricerca provvedimento senza filtri di ricerca
	 * 
	 * @param ente the ente
	 * @param anno the anno
	 * @param oggetto the oggetto
	 * @param numero the numero
	 * @param note the note
	 * @param uidTipoAtto the uid tipo atto
	 * @param attoammId the attoamm id
	 * @param uidStrutt the uid strutt
	 * @param attoammStatoDesc the attoamm stato desc
	 * @param conQuotaAssociata the con quota associata
	 * @return
	 */
	@Query("FROM SiacTAttoAmm atto "
			+ " WHERE atto.dataCancellazione IS NULL "
			+ " AND atto.siacTEnteProprietario = :ente "
			+ " AND (( :attoammId = -1 ) OR atto.attoammId = :attoammId ) "
			+ " AND (( :anno = '' OR :anno IS NULL ) OR atto.attoammAnno = :anno ) "
			//SIAC-8355
			+ " AND (( :oggetto = '' OR :oggetto IS NULL ) OR UPPER(atto.attoammOggetto) LIKE CONCAT('%', UPPER(:oggetto), '%') ) "
			+ " AND (( :note = '' OR :note IS NULL ) OR atto.attoammNote = :note ) "
			+ " AND (( :numero = -1 ) OR atto.attoammNumero = :numero ) "
			+ " AND (( :uidTipoAtto = -1) OR atto.siacDAttoAmmTipo.attoammTipoId = :uidTipoAtto ) "
			+ " AND (( :uidStrutt = -1) OR EXISTS ( "
			+ "     FROM SiacRAttoAmmClass classificatori "
			+ "     WHERE classificatori.siacTAttoAmm = atto "
			+ "     AND classificatori.dataCancellazione IS NULL "
			+ "     AND classificatori.siacTClass.classifId = :uidStrutt "
			+ " )) "
			// SIAC-5419: aggiunto stato diverso da A-ANNULLATO
			+ " AND ((:attoammStatoDesc = '' OR :attoammStatoDesc IS NULL) OR EXISTS ( "
			+ "     FROM atto.siacRAttoAmmStatos raas "
			+ "     WHERE raas.siacDAttoAmmStato.attoammStatoDesc = :attoammStatoDesc "
			+ "     AND raas.dataCancellazione IS NULL "
			+ "     AND raas.siacDAttoAmmStato.dataCancellazione IS NULL "
			+ " )) "
			+ " AND (( :conQuotaAssociata = false) OR EXISTS ( "
			+ "     FROM atto.siacRSubdocAttoAmms rsaa, SiacRDocStato rds "
			+ "     WHERE rds.siacTDoc = rsaa.siacTSubdoc.siacTDoc "
			+ "     AND rsaa.dataCancellazione IS NULL "
			+ "     AND rds.dataCancellazione IS NULL "
			+ "     AND rds.siacDDocStato.docStatoCode <> 'A' "
			+ " ))"
			//SIAC-6929 per ricerca filtrata
			+"AND ((:bloccoRagioneria IS NULL) OR atto.attoammBlocco = CAST(CAST(:bloccoRagioneria AS string) AS boolean))"
			+"AND ((:inseritoManualmente IS NULL) OR atto.attoammProvenienza = CAST(:inseritoManualmente AS string))"
			+ " ORDER BY atto.attoammAnno, atto.attoammNumero ")
	List<SiacTAttoAmm> ricercaProvvedimento(@Param("ente") SiacTEnteProprietario ente, @Param("anno") String anno,
			@Param("oggetto") String oggetto, @Param("numero") Integer numero, 
			@Param("note") String note, @Param("uidTipoAtto") Integer uidTipoAtto, 
			@Param("attoammId") Integer attoammId, @Param("uidStrutt") Integer uidStrutt,
			@Param("attoammStatoDesc") String attoammStatoDesc, @Param("conQuotaAssociata") Boolean conQuotaAssociata,
			//SIAC-6929
			@Param("bloccoRagioneria") Boolean bloccoRagioneria, @Param("inseritoManualmente") String inseritoManualmente);
	
	/**
	 * Ricerca ricercaAttoByAnnoNumeroTipoAndSAC.
	 * 
	 * @param ente the ente
	 * @param anno the anno
	 * @param numero the numero
	 * @param uidTipoAtto the uid tipo atto
	 * @param uidStrutt the uid strutt
	 * @return
	 */
	@Query("FROM SiacTAttoAmm atto "
			+ " WHERE atto.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId"
			+ " AND atto.attoammAnno = :anno  "
			+ " AND atto.attoammNumero = :numero "
			+ " AND atto.siacDAttoAmmTipo.attoammTipoId = :uidTipoAtto  "
			+ " AND (( :uidStrutt IS NULL) OR EXISTS ( "
			+ "     SELECT siacTClass.classifId "
			+ "     FROM SiacRAttoAmmClass classificatori "
			+ "     WHERE classificatori.siacTAttoAmm = atto "
			+ "     AND classificatori.siacTClass.classifId = CAST(CAST(:uidStrutt AS string) AS integer) "
			+ " )) "
			 )
	SiacTAttoAmm ricercaAttoByAnnoNumeroTipoAndSAC(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("anno") String anno,
		    @Param("numero") Integer numero, 
		    @Param("uidTipoAtto") Integer uidTipoAtto,
		    @Param("uidStrutt") Integer uidStrutt
		    );
	/**
	 * Ricerca ricercaCodiceStatoOut.
	 * 
	 * @param ente the ente
	 * @param attoammStatoCode attoammStatoCode
	 * @return
	 */
	@Query("FROM SiacRAttoAmmStatoInOut r "
			+ " WHERE r.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId"
			+ " AND r.siacDAttoAmmStato.attoammStatoCode = :attoammStatoCode "
			+ " AND r.dataCancellazione is null " 
			+ " AND r.dataInizioValidita < CURRENT_TIMESTAMP "
			+ " AND ( r.dataFineValidita is null or r.dataFineValidita > CURRENT_DATE)"
			)
	List<SiacRAttoAmmStatoInOut> ricercaCodiceStatoOut(@Param("enteProprietarioId") Integer enteProprietarioId,
		    @Param("attoammStatoCode") String attoammStatoCode);
	
	@Query(" SELECT COALESCE(MAX(taa.attoammNumero + 1), 0) "
			+ " FROM SiacTAttoAmm taa "
			+ " WHERE taa.dataCancellazione IS NULL "
			+ " AND taa.attoammAnno = :attoammAnno "
			+ " AND taa.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND taa.siacDAttoAmmTipo.attoammTipoId = :attoammTipoId ")
	Integer findMaxAttoammNumeroByAttoammAnnoAndAttoammTipoIdAndEntePropretarioId(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("attoammAnno") String attoammAnno,
			@Param("attoammTipoId") Integer attoammTipoId);

	@Query(" SELECT r.siacTAttoAmm "
			+ " FROM SiacRSubdocAttoAmm r "
			+ " WHERE r.siacTSubdoc.subdocId = :subdocId "
			+ " AND r.dataCancellazione is null " 
			+ " AND r.dataInizioValidita < CURRENT_TIMESTAMP "
			+ " AND ( r.dataFineValidita is null or r.dataFineValidita > CURRENT_DATE)"
			+ " AND r.siacTSubdoc.dataCancellazione is null "
			+ " AND r.siacTAttoAmm.dataCancellazione is null "
			 )
	SiacTAttoAmm findAttoAmmBySubdocId(@Param("subdocId") Integer subdocId);
	
}

