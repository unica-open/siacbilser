/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.movgest;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacTMovgestFin;

public interface SiacTMovgestRepository extends JpaRepository<SiacTMovgestFin, Integer> {
	
	String condizione = " ( (mg.dataInizioValidita < :dataInput)  AND (mg.dataFineValidita IS NULL OR :dataInput < mg.dataFineValidita) AND mg.dataCancellazione IS NULL ) ";

	
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

	
	@Query(RICERCA_TMOVGEST_PK)
	public SiacTMovgestFin ricercaSiacTMovgestPk(@Param("enteProprietarioId") Integer enteProprietarioId,
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
	
}