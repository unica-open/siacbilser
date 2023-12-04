/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.movgest;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacTClassFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestFin;

public interface SiacTMovgestRepository extends JpaRepository<SiacTMovgestFin, Integer> {
	
	String condizione = " ( (mg.dataInizioValidita < :dataInput)  AND (mg.dataFineValidita IS NULL OR :dataInput < mg.dataFineValidita) AND mg.dataCancellazione IS NULL ) ";

	
	static final String RICERCA_TMOVGEST_PK_NEW = " SELECT stm.* " + 
			" FROM siac.siac_t_movgest stm, siac.siac_t_ente_proprietario step, siac.siac_t_bil stb, siac.siac_t_periodo stp, siac.siac_d_movgest_tipo sdmt" +
			" WHERE stm.ente_proprietario_id = step.ente_proprietario_id and " +
			" stm.movgest_tipo_id = sdmt.movgest_tipo_id AND " +
			" stm.bil_id = stb.bil_id AND " +
			" stb.periodo_id = stp.periodo_id AND " +
			" step.ente_proprietario_id = :enteProprietarioId AND " +
			" stm.movgest_numero = :numeroMovimento AND " +
			" stm.movgest_anno = :annoMovimento AND " +
			" stp.anno = :annoEsercizio AND " +
			" sdmt.movgest_tipo_code = :tipoTMovGest AND " +					  
			" sdmt.validita_fine IS NULL AND " +
			" stb.validita_fine IS NULL AND " +
			" stp.validita_fine IS NULL AND " +
			" stm.data_cancellazione IS null AND " +
			" stp.data_cancellazione IS NULL AND " +
			" stb.data_cancellazione IS NULL AND " +
			" step.data_cancellazione IS NULL "
			;
	
	static final String RICERCA_TMOVGEST_PK =  "SELECT mg " +
										 	   "FROM SiacTMovgestFin mg " +
  								               "WHERE mg.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND " +
  								               "      mg.siacDMovgestTipo.siacTEnteProprietario.enteProprietarioId = mg.siacTEnteProprietario.enteProprietarioId AND " +
							                   "      mg.siacTBil.siacTEnteProprietario.enteProprietarioId = mg.siacTEnteProprietario.enteProprietarioId AND " +
								               "      mg.siacTBil.siacTPeriodo.siacTEnteProprietario.enteProprietarioId = mg.siacTEnteProprietario.enteProprietarioId AND " +
								               "      mg.movgestNumero = :numeroMovimento AND " +
								               "      mg.movgestAnno = :annoMovimento AND " +
								               "      mg.siacTBil.siacTPeriodo.anno = :annoEsercizio AND " +
								               "      mg.siacDMovgestTipo.movgestTipoCode = :tipoTMovGest AND " +					  
								               "      mg.siacDMovgestTipo.dataFineValidita IS NULL AND " +
								               "      mg.siacTBil.dataFineValidita IS NULL AND " +
								               "      mg.siacTBil.siacTPeriodo.dataFineValidita IS NULL AND "+
								               "      mg.dataCancellazione IS NULL";

	
	@Query(value=RICERCA_TMOVGEST_PK_NEW, nativeQuery = true)
	public SiacTMovgestFin ricercaSiacTMovgestPkToTest(@Param("enteProprietarioId") Integer enteProprietarioId,
			@Param("annoEsercizio") String annoEsercizio,
			@Param("annoMovimento") Integer annoMovimento,
			@Param("numeroMovimento") BigDecimal numeroMovimento,
			@Param("tipoTMovGest") String tipoTMovGest);
	
	@Query(RICERCA_TMOVGEST_PK)
	public SiacTMovgestFin ricercaSiacTMovgestPk(@Param("enteProprietarioId") Integer enteProprietarioId,
										 	  @Param("annoEsercizio") String annoEsercizio,
										      @Param("annoMovimento") Integer annoMovimento,
										      @Param("numeroMovimento") BigDecimal numeroMovimento,
										      @Param("tipoTMovGest") String tipoTMovGest);
	
	static final String RICERCA_TMOVGEST_PK_CAP =  "SELECT mg " +
										 	   "FROM SiacTMovgestFin mg " +
										 	   "JOIN mg.siacRMovgestBilElems rBilElems " +
										 	   "JOIN rBilElems.siacTBilElem tBilElem " +
										 	   "JOIN tBilElem.siacDBilElemTipo dBilElemTipo " +
									           "WHERE mg.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND " +
									           "      mg.siacDMovgestTipo.siacTEnteProprietario.enteProprietarioId = mg.siacTEnteProprietario.enteProprietarioId AND " +
									           "      mg.siacTBil.siacTEnteProprietario.enteProprietarioId = mg.siacTEnteProprietario.enteProprietarioId AND " +
									           "      mg.siacTBil.siacTPeriodo.siacTEnteProprietario.enteProprietarioId = mg.siacTEnteProprietario.enteProprietarioId AND " +
									           "      mg.movgestNumero = :numeroMovimento AND " +
									           "      mg.movgestAnno = :annoMovimento AND " +
									           "      mg.siacTBil.siacTPeriodo.anno = :annoEsercizio AND " +
									           "      mg.siacDMovgestTipo.movgestTipoCode = :tipoTMovGest AND " +					  
									           "      mg.siacDMovgestTipo.dataFineValidita IS NULL AND " +
									           "      mg.siacTBil.dataFineValidita IS NULL AND " +
									           "      mg.siacTBil.siacTPeriodo.dataFineValidita IS NULL AND "+
									           "      mg.dataCancellazione IS NULL AND"+
											   "      rBilElems.dataCancellazione IS NULL";
	
	@Query(RICERCA_TMOVGEST_PK_CAP)
	public SiacTMovgestFin ricercaSiacTMovgestPkWithCap(@Param("enteProprietarioId") Integer enteProprietarioId,
												@Param("annoEsercizio") String annoEsercizio,
												@Param("annoMovimento") Integer annoMovimento,
												@Param("numeroMovimento") BigDecimal numeroMovimento,
												@Param("tipoTMovGest") String tipoTMovGest);
	
//	@Query("FROM SiacTMovgestFin mg WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND "+condizione)
//	public List<SiacTMovgestFin> findListaTMovgest(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("dataInput") Timestamp  dataInput);
	
//	@Query("SELECT mg.movgestNumero FROM SiacTMovgestFin mg WHERE mg.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND mg.movgestAnno = :annoMovimento")
//	public List<BigDecimal> findNumeroByEnteAndAmbito(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("annoMovimento") Integer annoMovimento);
	
//	@Query("FROM SiacTMovgestFin mg WHERE mg.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND mg.movgestAnno = :anno AND mg.movgestNumero =:numero AND mg.siacDMovgestTipo.movgestTipoCode = :tipoMovimento AND mg.siacTBil.siacTPeriodo.anno = :bilancio ")
//	public List<SiacTMovgestFin> findByEnteAnnoNumeroBilancio(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("anno") Integer anno,@Param("numero") BigDecimal numero, @Param("tipoMovimento") String tipoMovimento, @Param("bilancio") String bilancio);
	
	@Query("FROM SiacTMovgestFin mg WHERE mg.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND mg.movgestAnno = :anno AND mg.movgestNumero =:numero AND mg.siacDMovgestTipo.movgestTipoCode = :tipoMovimento AND mg.siacTBil.siacTPeriodo.anno = :bilancio AND "+condizione)
	public List<SiacTMovgestFin> findByEnteAnnoNumeroBilancioValido(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("anno") Integer anno,@Param("numero") BigDecimal numero, @Param("tipoMovimento") String tipoMovimento, @Param("bilancio") String bilancio,@Param("dataInput") Timestamp  dataInput);
	
	
	/*
	 * METODO NON + CHIAMATO
	 */
	@Query("SELECT count(*) FROM SiacTMovgestFin mg WHERE mg.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND mg.movgestAnno = :annoMovimento")
	public Long countMovgestByEnteAndAnno(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("annoMovimento") Integer annoMovimento);
	
	
	/*
	 * METODO NON + CHIAMATO
	 */
	@Query("SELECT max(mg.movgestNumero) + 1 FROM SiacTMovgestFin mg WHERE mg.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND mg.movgestAnno = :annoMovimento")
	public BigDecimal estraiMaxNumMovgestPiuUnoByEnteAndAnno(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("annoMovimento") Integer annoMovimento);
	
	//SIAC-7532
	//SIAC-7863 devo eseguire il cast ad integer altrimenti confrontera' i caratteri 
	//ottenendo sempre 9 come numero massimo
	@Query(" SELECT max(cast(mvgt.movgestTsCode as integer)) "
			+ " FROM SiacTMovgestFin mg "
			+ " JOIN mg.siacTMovgestTs mvgt "
			+ " JOIN mvgt.siacDMovgestTsTipo mvgttipo "
			+ " WHERE mg.movgestAnno = :annoMovimento "
			+ " AND mg.movgestNumero = :numeroMovimento "
			+ " AND mvgttipo.movgestTsTipoCode = :tipoMovimentoTs "
			+ " AND mvgt.siacTMovgest.siacDMovgestTipo.movgestTipoCode = :tipoMovimento "
			+ " AND mvgt.siacTEnteProprietario.enteProprietarioId = :ente "
			+ " AND mvgt.dataCancellazione IS NULL ")
	public Integer getMaxNumeroSubByAnnoNumeroTipo(
			@Param("annoMovimento") Integer annoMovimento,
			@Param("numeroMovimento") BigDecimal numeroMovimento,
			@Param("tipoMovimento") String tipoMovimento,
			@Param("ente") Integer enteId,
			@Param("tipoMovimentoTs") String tipoMovimentoTs);





	@Query(value = "SELECT fnc_siac_verifica_importi_dopo_annullamento_modifica( "
			+ ":idEnte, "
			+ ":idBilancio, "
			+ ":codiceTipoMovimento, "
			+ ":annoMovimento, "
			+ ":numeroMovimento "
			+ ")", nativeQuery = true)
	String verificaImportiDopoAnnullamentoModifica(
		@Param("idEnte") int idEnte, 
		@Param("idBilancio") int idBilancio, 
		@Param("codiceTipoMovimento") String codiceTipoMovimento,
	    @Param("annoMovimento") Integer annoMovimento, 
		@Param("numeroMovimento") BigDecimal numeroMovimento
	);

	@Query(" SELECT mg.movgestAnno "
			+ " FROM SiacTMovgestFin mg "
			+ " WHERE mg.movgestNumero = :numeroMovimento "
			+ " AND mg.siacDMovgestTipo.movgestTipoCode = :codiceTipoMovimento "
			+ " AND mg.siacTEnteProprietario.enteProprietarioId = :idEnte "
			+ " AND mg.siacTBil.siacTPeriodo.anno = :annoBilancio "
			+ " AND mg.movgestAnno < CAST(:annoBilancio AS integer)"			
			+ " AND mg.dataCancellazione IS NULL "
			+ " AND mg.movgestId <> :movgestId "
			+ " AND EXISTS ("
			+ " 	FROM SiacRMovgestTsStatoFin rstato"
			+ "     WHERE rstato.dataCancellazione IS NULL"
			+ "     AND rstato.siacTMovgestT.siacTMovgest = mg "
			+ "     AND rstato.siacDMovgestStato.movgestStatoCode <> 'A'"
			+ ") "
			//eliminato perche' non vengono mostrati a video
			//+ "ORDER BY mg.movgestAnno"
			)
	//SIAC-8142
	public List<Integer> caricaAnniAccertamentiConStessoNumeroNelBilancio(@Param("movgestId") Integer movgestId, @Param("numeroMovimento") BigDecimal numeroMovimento, @Param("codiceTipoMovimento") String codiceTipoMovimento, @Param("annoBilancio") String annoBilancio, @Param("idEnte") int idEnte);
	
	@Query(" SELECT a.siacTClass " +
			" FROM SiacRBilElemClassFin a, SiacRMovgestBilElemFin b " +
			" WHERE a.siacTClass.siacDClassTipo.classifTipoCode IN (:classifTipoCodes) " +
			" AND a.dataCancellazione IS NULL "+
			" AND b.dataCancellazione IS NULL " + 
			" AND b.siacTBilElem.elemId = a.siacTBilElem.elemId " + 
			" AND b.siacTMovgest.movgestId = :movgestId ")
	SiacTClassFin findSiacTClassCapitoloClassByTipoClassCodes( @Param("movgestId") Integer movgestId, @Param("classifTipoCodes") Collection<String> classifTipoCodes);
	
}