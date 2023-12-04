/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacDMovgestStato;
import it.csi.siac.siacbilser.integration.entity.SiacDSiopeAssenzaMotivazione;
import it.csi.siac.siacbilser.integration.entity.SiacTAttoAmm;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgest;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgestT;

/**
 * The Interface SiacTMovgestTRepository.
 */

public interface SiacTMovgestTRepository extends JpaRepository<SiacTMovgestT, Integer> {
	
	
	/**
	 * Find siac t movgest testata by siac t movgest id.
	 *
	 * @param movgestId the movgest id
	 * @return the list
	 */
	@Query("FROM SiacTMovgestT m "
			+ " WHERE m.siacTMovgest.movgestId = :movgestId "
			+ " AND m.siacDMovgestTsTipo.movgestTsTipoCode = 'T' "
			+ " AND m.dataCancellazione IS NULL ")
	List<SiacTMovgestT> findSiacTMovgestTestataBySiacTMovgestId(@Param("movgestId") Integer movgestId);

	
	@Query( " SELECT c.classifCode " 
			+ " FROM SiacTClass c " 
			+ " WHERE c.dataCancellazione IS NULL " 
			+ " AND EXISTS ( FROM c.siacRMovgestClasses rmg "
			+ "			  	 WHERE rmg.dataCancellazione IS NULL "
			+ "			  	 AND rmg.siacTMovgestT.siacTMovgest.movgestId = :movgestId "
			+ "			   	 AND rmg.siacTMovgestT.siacTMovgestIdPadre IS NULL) "
			+ "	AND " 
			+ " c.siacDClassTipo.classifTipoCode IN ( :listaCodici) "
			)
	String findCodiceClassificatoreByUidImpegnoECodiceTipo(@Param("movgestId")Integer uid, @Param("listaCodici")Collection<String> listaCodici);
	
	
	@Query( " FROM SiacTClass c " 
			+ " WHERE c.dataCancellazione IS NULL " 
			+ " AND EXISTS ( FROM c.siacRMovgestClasses rmg "
			+ "			  	 WHERE rmg.dataCancellazione IS NULL "
			+ "			  	 AND rmg.siacTMovgestT.siacTMovgest.movgestId = :movgestId "
			+ "			   	 AND rmg.siacTMovgestT.siacTMovgestIdPadre IS NULL" //questo forza che sia un papa'.
			+ "            ) "
			+ "	AND " 
			+ " c.siacDClassTipo.classifTipoCode IN ( :listaCodici) "
			)
	List<SiacTClass> findSiacTClassByMovGestIdECodiciTipo(@Param("movgestId")Integer uid, @Param("listaCodici")Collection<String> listaCodici);
	
	@Query("SELECT rmc.siacTClass "
			+ " FROM SiacRMovgestClass rmc "
			+ " WHERE rmc.dataCancellazione IS NULL "
			+ " AND rmc.siacTClass.dataCancellazione IS NULL "
			+ " AND rmc.siacTMovgestT.movgestTsId = :movgestTsId "
			+ " AND rmc.siacTClass.siacDClassTipo.classifTipoCode IN (:classifTipoCodes) "
			)
	List<SiacTClass> findSiacTClassByMovgestTsIdECodiciTipo(@Param("movgestTsId")Integer uid, @Param("classifTipoCodes")Collection<String> listaCodici);
	
//	@Query( " SELECT DISTINCT r.siacTClass "
//			+ " FROM SiacRMovgestClass r " 
//			+ " WHERE r.dataCancellazione IS NULL " 
//			+ " AND r.siacTMovgestT.dataCancellazione IS NULL "
//			+ " AND r.siacTMovgestT.siacTMovgest.dataCancellazione IS NULL "
//			+ " AND (r.siacTMovgestT.siacTMovgest.movgestId = :movgestId OR r.siacTMovgestT.movgestTsId = :movgestTsId) "
//			+ " AND r.siacTClass.siacDClassTipo.classifTipoCode IN ( :listaCodici) "
//			)
//	List<SiacTClass> findSiacTClassByMovGestOSubMovGestIdECodiciTipo(@Param("movgestId")Integer movgestId, @Param("movgestTsId")Integer movgestTsId, @Param("listaCodici")List<String> listaCodici);
	
	@Query( " SELECT DISTINCT r.siacTClass "
			+ " FROM SiacRMovgestClass r, SiacRSubdocMovgestT rsmt " 
			+ " WHERE r.dataCancellazione IS NULL " 
			+ " AND rsmt.dataCancellazione IS NULL "
			+ " AND r.siacTMovgestT = rsmt.siacTMovgestT "
			+ " AND r.siacTMovgestT.dataCancellazione IS NULL "
			+ " AND r.siacTMovgestT.siacTMovgest.dataCancellazione IS NULL "
			+ " AND rsmt.siacTSubdoc.subdocId = :subdocId "
			+ " AND r.siacTClass.siacDClassTipo.classifTipoCode IN ( :listaCodici) "
			)
	List<SiacTClass> findSiacTClassMovGestBySubdocIdECodiciTipo(@Param("subdocId")Integer subdocId, @Param("listaCodici")List<String> listaCodici);
	
	
	
	@Query( " SELECT c.classifCode " 
			+ " FROM SiacTClass c " 
			+ " WHERE c.dataCancellazione IS NULL " 
			+ " AND EXISTS ( FROM c.siacRMovgestClasses rmg "
			+ "			  	 WHERE rmg.dataCancellazione IS NULL "
			+ "			  	 AND rmg.siacTMovgestT.movgestTsId = :movgestTsId) "
			+ "	AND " 
			+ " c.siacDClassTipo.classifTipoCode IN ( :listaCodici) "
			)
	String findCodiceClassificatoreByUidSubImpegnoECodiceTipo(@Param("movgestTsId")Integer uid, @Param("listaCodici")List<String> listaCodici);

	
	
	
	@Query("SELECT det.movgestTsDetImporto " +
			  "FROM SiacTMovgestTsDet det "+
		      " WHERE det.dataFineValidita IS NULL  " +
		      " AND det.dataCancellazione IS NULL " +
		      " AND det.siacDMovgestTsDetTipo.movgestTsDetTipoCode = :tipoImporto "+
		      " AND det.siacTMovgestT.siacTMovgest.movgestId = :movgestId " +
		      " AND det.siacTMovgestT.siacDMovgestTsTipo.movgestTsTipoCode = 'T'  "
		      )
	BigDecimal findImportoByMovgestId(@Param("movgestId") Integer movgestId,
				                      @Param("tipoImporto") String tipoImporto);
	
	
	@Query("SELECT det.movgestTsDetImporto " +
			  "FROM SiacTMovgestTsDet det "+
		      " WHERE det.dataFineValidita IS NULL  " +
		      " AND det.dataCancellazione IS NULL " +
		      " AND det.siacDMovgestTsDetTipo.movgestTsDetTipoCode = :tipoImporto "+
		      " AND det.siacTMovgestT.movgestTsId = :movgestTsId "
		      )
	BigDecimal findImportoByMovgestTsId(@Param("movgestTsId") Integer movgestTsId,
				                      @Param("tipoImporto") String tipoImporto);
	
	
	
	@Query("FROM SiacTMovgest m "
			+ " WHERE m.dataCancellazione IS NULL "
			+ " AND EXISTS ( FROM SiacTMovgestT mt "
			+ " 			 WHERE mt.siacTMovgest = m "
			+ " 			 AND mt.dataCancellazione IS NULL "
			+ "        		 AND EXISTS ( FROM mt.siacRSubdocMovgestTs r"
			+ "                 		  WHERE r.siacTSubdoc.subdocId = :subdocId "
			+ "							  AND r.dataCancellazione IS NULL) "	 
			+ "				 )" )
	SiacTMovgest findSiacTMovgestBySubdoc(@Param("subdocId") Integer subdocId);

	
	@Query("FROM SiacTMovgestT m "
			+ " WHERE m.siacDMovgestTsTipo.movgestTsTipoCode = 'S' "
			+ " AND m.dataCancellazione IS NULL "
			+ " AND EXISTS ( FROM m.siacRSubdocMovgestTs r"
			+ "				 WHERE r.siacTSubdoc.subdocId = :subdocId "
			+ "				 AND r.dataCancellazione IS NULL)" )
	SiacTMovgestT findSiacTMovgestTSSubimpegnoBySubdoc(@Param("subdocId") Integer subdocId);

	@Query(" SELECT rmts.siacDMovgestStato "
			+ "FROM SiacRMovgestTsStato rmts "
			+ " WHERE rmts.dataCancellazione IS NULL "
			+ " AND rmts.siacTMovgestT.siacTMovgest.movgestId = :movgestId ")
	List<SiacDMovgestStato> findSiacDMovgestTsStatoByMovgestId(@Param("movgestId") Integer movgestId);
	
	@Query(" SELECT rmts.siacDMovgestStato "
			+ "FROM SiacRMovgestTsStato rmts "
			+ " WHERE rmts.dataCancellazione IS NULL "
			+ " AND rmts.siacTMovgestT.movgestTsId = :movgestTsId ")
	List<SiacDMovgestStato> findSiacDMovgestTsStatoByMovgestTsId(@Param("movgestTsId") Integer movgestTsId);
	

	@Query(" SELECT r.boolean_ " //valori contenuti: 'S', 'N'
			+ " FROM SiacRMovgestTsAttr r "
			+ " WHERE r.siacTAttr.attrCode = :attrCode "
			+ " AND r.siacTMovgestT.siacTMovgest.movgestId = :movgestId"
			+ " AND r.siacTMovgestT.siacDMovgestTsTipo.movgestTsTipoCode = 'T' "
			+ " AND r.dataCancellazione IS NULL ")
	List<String> findBooleanAttrValues(@Param("movgestId") Integer movgestId, @Param("attrCode") String attrCode);
	
	/*@Query(" SELECT r.testo "
			+ " FROM SiacRMovgestTsAttr r "
			+ " WHERE r.siacTAttr.attrCode = :attrCode "
			+ " AND r.siacTMovgestT.siacTMovgest.movgestId = :movgestId"
			+ " AND r.dataCancellazione IS NULL "
			+ " AND r.siacTMovgestT.siacDMovgestTsTipo.movgestTsTipoCode = :movgestTsTipoCode ")
	String findTestoAttrValue(@Param("movgestId") Integer movgestId, @Param("attrCode") String attrCode, @Param("movgestTsTipoCode") String movgestTsTipoCode);*/
	
	@Query(" SELECT r.testo "
			+ " FROM SiacRMovgestTsAttr r "
			+ " WHERE r.siacTAttr.attrCode = :attrCode "
			+ " AND r.siacTMovgestT.siacTMovgest.movgestId = :movgestId"
			+ " AND r.dataCancellazione IS NULL "
			//SIAC-8125
			+ " AND r.siacTMovgestT.dataCancellazione IS NULL "
			+ " AND r.siacTMovgestT.siacDMovgestTsTipo.movgestTsTipoCode = 'T' ")
	String findTestoAttrValueBySiacTMovgestId(@Param("movgestId") Integer movgestId, @Param("attrCode") String attrCode);
	
	@Query(" SELECT r.testo "
			+ " FROM SiacRMovgestTsAttr r "
			+ " WHERE r.siacTAttr.attrCode = :attrCode "
			+ " AND r.siacTMovgestT.movgestTsId = :movgestTsId"
			+ " AND r.dataCancellazione IS NULL " 
			+ " AND r.siacTMovgestT.siacDMovgestTsTipo.movgestTsTipoCode = 'S' ")
	String findTestoAttrValueBySiacTMovgestTsId(@Param("movgestTsId") Integer movgestTsId, @Param("attrCode") String attrCode);
	
	@Query(" SELECT r.numerico "
			+ " FROM SiacRMovgestTsAttr r "
			+ " WHERE r.siacTAttr.attrCode = :attrCode "
			+ " AND r.siacTMovgestT.siacTMovgest.movgestId = :movgestId "
			+ " AND r.dataCancellazione IS NULL ")
	BigDecimal findNumericoAttrValue(@Param("movgestId") Integer movgestId, @Param("attrCode") String attrCode);
	
	@Query(" SELECT c.elemId " 
	        + " FROM SiacTBilElem c "
			+ " WHERE c.dataCancellazione IS NULL "
			+ " AND EXISTS ( FROM c.siacRMovgestBilElems rmg "
			+ "     		 WHERE rmg.dataCancellazione IS NULL "
			+ "     		 AND rmg.siacTMovgest.movgestId = :movgestId "
			+ " 			) "
			)
	public int findBilElemIdByMovgestId(@Param("movgestId")Integer movgestId);
	
	@Query(" SELECT c.elemId " 
	        + " FROM SiacTBilElem c "
			+ " WHERE c.dataCancellazione IS NULL "
			+ " AND EXISTS ( FROM c.siacRMovgestBilElems rmg "
			+ "     		 WHERE rmg.dataCancellazione IS NULL "
			+ "     		 AND rmg.siacTMovgest.movgestId = (SELECT m.siacTMovgest.movgestId "
			+ "										           FROM SiacTMovgestT m "
			+ "										           WHERE m.movgestTsId = :movgestTsId  "
			+ "										) "
			+ " 			) "
			)
	public int findBilElemIdByMovgestTsId(@Param("movgestTsId")Integer movgestTsId);
	
	
	
	
	// SIAC-5632
	@Query(value = " SELECT COALESCE(SUM(totd.ord_ts_det_importo), 0) "
			+ " FROM siac_t_ordinativo tor, "
			+ "     siac_t_ordinativo_ts tot, "
			+ "     siac_t_ordinativo_ts_det totd, "
			+ "     siac_r_ordinativo_stato ros, "
			+ "     siac_d_ordinativo_stato dos, "
			+ "     siac_r_liquidazione_ord rlo, "
			+ "     siac_r_liquidazione_stato rls, "
			+ "     siac_d_liquidazione_stato dls, "
			+ "     siac_r_liquidazione_movgest rlm, "
			+ "     siac_d_ordinativo_ts_det_tipo dotdt "
			+ " WHERE tor.ord_id = tot.ord_id "
			+ " AND totd.ord_ts_id = tot.ord_ts_id "
			+ " AND ros.ord_id = tor.ord_id "
			+ " AND ros.ord_stato_id = dos.ord_stato_id "
			+ " AND rlo.sord_id = tot.ord_ts_id "
			+ " AND rls.liq_id = rlo.liq_id "
			+ " AND dls.liq_stato_id = rls.liq_stato_id "
			+ " AND rlm.liq_id = rlo.liq_id "
			+ " AND dotdt.ord_ts_det_tipo_id = totd.ord_ts_det_tipo_id "
			+ " AND dos.ord_stato_code <> 'A' "
			+ " AND dls.liq_stato_code = 'V' "
			+ " AND rlm.movgest_ts_id = :movgestTsId "
			+ " AND dotdt.ord_ts_det_tipo_code='A' "
			+ " AND tor.data_cancellazione IS NULL "
			+ " AND tot.data_cancellazione IS NULL "
			+ " AND totd.data_cancellazione IS NULL "
			+ " AND ros.data_cancellazione IS NULL "
			+ " AND dos.data_cancellazione IS NULL "
			+ " AND rlo.data_cancellazione IS NULL "
			+ " AND rls.data_cancellazione IS NULL "
			+ " AND dls.data_cancellazione IS NULL "
			+ " AND rlm.data_cancellazione IS NULL "
			+ " AND dotdt.data_cancellazione IS NULL "
			+ " AND :dataInput BETWEEN ros.validita_inizio AND COALESCE (ros.validita_fine, :dataInput) "
			+ " AND :dataInput BETWEEN rlo.validita_inizio AND COALESCE (rlo.validita_fine, :dataInput) "
			+ " AND :dataInput BETWEEN rls.validita_inizio AND COALESCE (rls.validita_fine, :dataInput) "
			+ " AND :dataInput BETWEEN rlm.validita_inizio AND COALESCE (rlm.validita_fine, :dataInput) ",
		nativeQuery = true)
	BigDecimal computeTotaleSubordinativiBySiacTMovgestTs(@Param("movgestTsId") Integer movgestTsId, @Param("dataInput") Date dataInput);
	
	@Query(" SELECT rmtaa.siacTAttoAmm "
			+ " FROM SiacRMovgestTsAttoAmm rmtaa "
			+ " WHERE rmtaa.dataCancellazione IS NULL "
			+ " AND rmtaa.siacTMovgestT.dataCancellazione IS NULL "
			+ " AND rmtaa.siacTMovgestT.movgestTsId = :movgestTsId "
			+ " AND rmtaa.siacTMovgestT.dataInizioValidita < CURRENT_TIMESTAMP "
			+ " AND (rmtaa.siacTMovgestT.dataFineValidita IS NULL OR rmtaa.siacTMovgestT.dataFineValidita > CURRENT_TIMESTAMP) "
			)
	List<SiacTAttoAmm> findSiacTAttoAmmByMovgestTsId(@Param("movgestTsId")Integer movgestTsId);

	// SIAC-6036
	@Query(" SELECT tmt.siacDSiopeAssenzaMotivazione "
			+ " FROM SiacTMovgestT tmt "
			+ " WHERE tmt.siacTMovgest.movgestId = :movgestId "
			+ " 	AND tmt.dataCancellazione IS NULL "
			+ " 	AND tmt.siacDMovgestTsTipo.movgestTsTipoCode = 'T' "
			)
	SiacDSiopeAssenzaMotivazione findSiopeAssenzaMotivazioneByMovgestId(@Param("movgestId")Integer movgestId);
	
	@Query(" SELECT tmt.siacDSiopeAssenzaMotivazione "
			+ " FROM SiacTMovgestT tmt "
			+ " WHERE tmt.movgestTsId = :movgestTsId "
			+ " 	AND tmt.dataCancellazione IS NULL "
			+ " 	AND tmt.siacDMovgestTsTipo.movgestTsTipoCode = 'S' "
			)
	SiacDSiopeAssenzaMotivazione findSiopeAssenzaMotivazioneByMovgestTsId(@Param("movgestTsId")Integer movgestTsId);
	
	
	
	@Query(" SELECT mt.movgestTsId "
			+ " FROM SiacTMovgestT mt "
			+ " WHERE mt.dataCancellazione IS NULL "
			+ " AND mt.siacTMovgest.movgestAnno = :movgestAnno "
			+ " AND mt.siacTMovgest.movgestNumero = :movgestNumero "
			+ " AND mt.siacTMovgest.siacDMovgestTipo.movgestTipoCode = :movgestTipoCode "
			+ " AND mt.siacTMovgest.siacTBil.bilId = :bilId "
			+ " AND mt.movgestTsCode = :movgestTsCode")
	List<Integer> findUidMovgestTsByAnnoNumeroBilancio(
			@Param("movgestAnno") Integer movgestAnno,
			@Param("movgestNumero") BigDecimal movgestNumero,
			@Param("movgestTsCode") String movgestTsCode,
			@Param("movgestTipoCode") String movgestTipoCode,
			@Param("bilId") Integer bilId
	);
	
	
	
//	@Query(" SELECT mt FROM SiacTMovgestT mt "
//			+ " JOIN mt.siacTMovgest m "//
//			+ " JOIN m.siacTBil tBil "//
//			+ " JOIN tBil.siacTPeriodo tPeriodo "//
//			+ " JOIN mt.siacRMovgestTsAttoAmms mtaa "
//			+ " WHERE mtaa.siacTAttoAmm.attoammNumero=:attoammNumero "
//			+ " AND tPeriodo.anno = :annoBilancio "//
//			+ " AND mtaa.siacTAttoAmm.attoammAnno=:attoammAnno "
//			+ " AND mtaa.siacTAttoAmm.siacDAttoAmmTipo.attoammTipoId=:attoammTipoId "
//			+ " AND mtaa.siacTAttoAmm.dataCancellazione IS NULL "
//			+ " AND mtaa.dataCancellazione IS NULL "
//			+ " AND mt.siacDMovgestTsTipo.movgestTsTipoCode='S' "
//			+ " AND mt.dataCancellazione IS NULL "
//			+ " AND (:attoammSacId IS NULL OR EXISTS ("
//			+ " SELECT 1 FROM mtaa.siacTAttoAmm aa JOIN aa.siacRAttoAmmClasses x WHERE x.siacTClass.classifId=CAST(CAST(:attoammSacId AS string) AS integer)) "
//			+ " ) "
//			+ " AND mt.siacTEnteProprietario.enteProprietarioId=:enteProprietarioId")
	
	//SIAC-7383 Esclusione movimenti annullati
	@Query(" SELECT mt FROM SiacTMovgestT mt "
			+ " JOIN mt.siacTMovgest m "//
			+ " JOIN mt.siacRMovgestTsStatos ms "
			+ " JOIN ms.siacDMovgestStato dms "
			+ " JOIN m.siacTBil tBil "//
			+ " JOIN tBil.siacTPeriodo tPeriodo "//
			+ " JOIN mt.siacRMovgestTsAttoAmms mtaa "
			+ " WHERE mtaa.siacTAttoAmm.attoammNumero=:attoammNumero "
			+ " AND tPeriodo.anno = :annoBilancio "//
			+ " AND mtaa.siacTAttoAmm.attoammAnno=:attoammAnno "
			+ " AND mtaa.siacTAttoAmm.siacDAttoAmmTipo.attoammTipoId=:attoammTipoId "
			+ " AND mtaa.siacTAttoAmm.dataCancellazione IS NULL "
			+ " AND mtaa.dataCancellazione IS NULL "
			+ " AND mt.siacDMovgestTsTipo.movgestTsTipoCode='S' "
			+ " AND mt.dataCancellazione IS NULL "
			+ " AND ms.dataCancellazione IS NULL AND dms.movgestStatoCode != 'A'"
			+ " AND (:attoammSacId IS NULL OR EXISTS ("
			+ " SELECT 1 FROM mtaa.siacTAttoAmm aa JOIN aa.siacRAttoAmmClasses x WHERE x.siacTClass.classifId=CAST(CAST(:attoammSacId AS string) AS integer)) "
			+ " ) "
			+ " AND mt.siacTEnteProprietario.enteProprietarioId=:enteProprietarioId")
	List<SiacTMovgestT> findSiacTMovgestTsBySiacTAttoAmm(
		@Param("annoBilancio") String annoBilancio,// SIAC-7365
		@Param("attoammNumero") Integer attoammNumero,
		@Param("attoammAnno") String attoammAnno,
		@Param("attoammTipoId") Integer attoammTipoId,
		@Param("attoammSacId") Integer attoammSacId,
		@Param("enteProprietarioId") Integer enteProprietarioId);
		
	
}
