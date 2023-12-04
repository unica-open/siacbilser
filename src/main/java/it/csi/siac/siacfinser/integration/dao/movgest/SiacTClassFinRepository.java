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

import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacfinser.integration.entity.SiacTClassFin;

public interface SiacTClassFinRepository extends JpaRepository<SiacTClassFin, Integer> {
	
	String condizione = " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND dataCancellazione IS NULL ) ";
	
	// SIAC-5736: l'inizio validita' deve essere considerato con l'uguale in quanto i classificatori vengono inseriti al primo millisecondo dell'anno
	String condizioneValidoInRange = " ( "
			+ " (   (dataInizioValidita <= :inizioRange AND dataFineValidita IS NULL) "
			+ " OR (dataInizioValidita <= :inizioRange AND dataFineValidita > :inizioRange)"
			+ " OR (dataInizioValidita >= :inizioRange AND dataInizioValidita <= :fineRange)  )"
			+ "  AND dataCancellazione IS NULL ) ";
	
	String condizioneInJoin = " ( (st.dataInizioValidita < :dataInput)  AND (st.dataFineValidita IS NULL OR :dataInput <= st.dataFineValidita) AND  st.dataCancellazione IS NULL ) ";
	
	// SIAC-5736: l'inizio validita' deve essere considerato con l'uguale in quanto i classificatori vengono inseriti al primo millisecondo dell'anno
	String condizioneValidoInRangeInJoin = " ( "
			+ " (   (st.dataInizioValidita <= :inizioRange AND st.dataFineValidita IS NULL) "
			+ " OR (st.dataInizioValidita <= :inizioRange AND st.dataFineValidita > :inizioRange)"
			+ " OR (st.dataInizioValidita >= :inizioRange AND st.dataInizioValidita <= :fineRange)  )"
			+ "  AND st.dataCancellazione IS NULL ) ";
	
	String findByTipoAndEnte = "FROM SiacTClassFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND " +
			"  siacDClassTipo.classifTipoCode = :classifTipoCode ";
	
	String findByTipoCodesAndEnteAndSelezionato = "SELECT st FROM SiacTClassFin st WHERE st.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND " +
			"  st.siacDClassTipo.classifTipoCode IN (:classifTipoCodes) AND st.classifCode = :codeSelezionato ";
	
	@Query("FROM SiacTClassFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND LOWER(classifCode)=:code AND " +
			"  siacDClassTipo.classifTipoId = :idClassTipo AND " + condizione )
	public List<SiacTClassFin> findByCodeAndTipoAndEnte(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("dataInput") Timestamp  dataInput,
			@Param("code") String code,@Param("idClassTipo") Integer idClassTipo);
	
	@Query("FROM SiacTClassFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND classifDesc=:desc " 
			+ " AND dataInizioValidita < CURRENT_TIMESTAMP "
			+ " AND (dataFineValidita IS NULL OR CURRENT_TIMESTAMP < dataFineValidita) "
			+ " AND dataCancellazione IS NULL ")
	public List<SiacTClassFin> findByDescAndEnte(
			@Param("enteProprietarioId") Integer enteProprietarioId,
			@Param("desc") String desc);
	
	@Query(findByTipoAndEnte + " AND " + condizione )
	public List<SiacTClassFin> findByTipoAndEnte(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("dataInput") Timestamp  dataInput,
			@Param("classifTipoCode") String classifTipoCode);
	
	@Query(findByTipoAndEnte + " AND " + condizioneValidoInRange )
	public List<SiacTClassFin> findByTipoAndEnte(@Param("enteProprietarioId") Integer enteProprietarioId,
			@Param("classifTipoCode") String classifTipoCode,@Param("inizioRange") Timestamp  inizioRange, @Param("fineRange") Timestamp  fineRange);
	
	
	@Query(findByTipoCodesAndEnteAndSelezionato + " AND " + condizioneInJoin )
	public List<SiacTClassFin> findByTipoCodesAndEnteAndSelezionato(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("dataInput") Timestamp  dataInput,
			@Param("classifTipoCodes") List<String> classifTipoCodes,@Param("codeSelezionato") String codeSelezionato);
	
	@Query(findByTipoCodesAndEnteAndSelezionato + " AND " + condizioneValidoInRangeInJoin )
	public List<SiacTClassFin> findByTipoCodesAndEnteAndSelezionato(@Param("enteProprietarioId") Integer enteProprietarioId,
			@Param("classifTipoCodes") List<String> classifTipoCodes,@Param("codeSelezionato") String codeSelezionato,@Param("inizioRange") Timestamp  inizioRange, @Param("fineRange") Timestamp  fineRange);

	/**
	 * SIAC-8229
	 * 
	 * <p>Cerco la struttura amministrativa tramite movgest e anno bilancio.</p>
	 * 
	 * @param annoMovimento
	 * @param numeroMovimento
	 * @param annoBilancio
	 * @param codiceTipoMovimento
	 * @return
	 */
	@Query( " SELECT stc " +
			" FROM SiacTClassFin stc " + 
			" JOIN stc.siacTEnteProprietario step " + 
			" JOIN stc.siacRMovgestClass srmc " + 
			" JOIN stc.siacDClassTipo sdct " + 
			" JOIN srmc.siacTMovgestT stmt " + 
			" JOIN stmt.siacTMovgest stm " + 
			" JOIN stm.siacDMovgestTipo sdmt " + 
			" JOIN stm.siacTBil stb " + 
			" JOIN stb.siacTPeriodo stp " + 
			" WHERE stm.movgestAnno = :annoMovimento " + 
			" AND stm.movgestNumero = :numeroMovimento " + 
			" AND stp.anno < :annoBilancio " + 
			" AND sdmt.movgestTipoCode = :codiceTipoMovimento " + 
			" AND sdct.classifTipoCode IN ('CDC', 'CDR')  " + 
			" AND srmc.dataCancellazione IS NULL " +
			" AND stm.dataCancellazione IS NULL ")
	public List<SiacTClassFin> findSiacTClassFromSiacTMovgestAndSiacTPeriodo(
			@Param("annoMovimento") Integer annoMovimento,
			@Param("numeroMovimento") BigDecimal numeroMovimento,
			@Param("annoBilancio") String annoBilancio,
			@Param("codiceTipoMovimento") String codiceTipoMovimento
			);
	
	@Query(findByTipoCodesAndEnteAndSelezionato + " AND st.dataFineValidita IS NULL AND st.dataCancellazione IS NULL ) " )
	public List<SiacTClassFin> findByTipoCodesAndEnteAndSelezionato(@Param("enteProprietarioId") Integer enteProprietarioId,
			@Param("classifTipoCodes") List<String> classifTipoCodes,@Param("codeSelezionato") String codeSelezionato);

	
	@Query("SELECT cl "+
			" FROM SiacRClassFamTreeFin cft, SiacTClassFin cl "+
			" WHERE cft.siacTClass1.classifId = :classifId "
			+ " AND cft.classifIdPadre = cl.classifId "
			+ " AND cft.dataCancellazione IS NULL " 
	)
	public SiacTClassFin findPadreClassificatoreByClassifId(@Param("classifId") Integer classifId );

}