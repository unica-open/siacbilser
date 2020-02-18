/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacDProgrammaTipo;
import it.csi.siac.siacbilser.integration.entity.SiacRCronopAttr;
import it.csi.siac.siacbilser.integration.entity.SiacTCronop;
import it.csi.siac.siacbilser.integration.entity.SiacTCronopElem;

// TODO: Auto-generated Javadoc
/**
 * The Interface SiacTCronopRepository.
 */
public interface SiacTCronopRepository extends JpaRepository<SiacTCronop, Integer> {

	/**
	 * Find siac t cronop by siac t progetto id.
	 *
	 * @param programmaId the programma id
	 * @return the list
	 */
	@Query(" SELECT c FROM SiacTCronop c "
			+ " WHERE c.siacTProgramma.programmaId = :programmaId "
			+ " AND c.dataCancellazione IS NULL ")
	List<SiacTCronop> findSiacTCronopBySiacTProgettoId(@Param("programmaId") Integer programmaId);
	
	@Query(" SELECT c "
			+ " FROM SiacTCronop c, SiacRCronopStato rcs "
			+ " WHERE rcs.siacTCronop = c "
			+ " AND c.siacTProgramma.programmaId = :programmaId "
			+ " AND c.siacTBil.bilId = :bilId "
			+ " AND rcs.siacDCronopStato.cronopStatoCode <> :cronopStatoCode "
			+ " AND c.dataCancellazione IS NULL "
			+ " AND rcs.dataCancellazione IS NULL ")
	List<SiacTCronop> findSiacTCronopBySiacTProgettoAndSiacTBilNotWithSiacDCronopStato(@Param("programmaId") Integer programmaId, @Param("bilId") Integer bilId, @Param("cronopStatoCode") String cronopStatoCode);

	
	/**
	 * Find cronoprogramma by codice and stato operativo and codice e stato progetto.
	 *
	 * @param cronopCode the cronop code
	 * @param cronopStatoCode the cronop stato code
	 * @param programmaCode the programma code
	 * @param programmaStatoCode the programma stato code
	 * @param enteProprietarioId the ente proprietario id
	 * @return the siac t cronop
	 */
	
	/**
	 * Find cronoprogramma by codice and stato operativo and codice E stato progetto.
	 *
	 * @param cronopCode the cronop code
	 * @param cronopStatoCode the cronop stato code
	 * @param programmaCode the programma code
	 * @param programmaStatoCode the programma stato code
	 * @param biId the bi id
	 * @param enteProprietarioId the ente proprietario id
	 * @return the siac T cronop
	 */
	@Query(" SELECT c FROM SiacTCronop c "
			+ " WHERE c.cronopCode = :cronopCode "			
			+ " AND EXISTS (SELECT sc FROM c.siacRCronopStatos sc"
			+ "				WHERE sc.siacDCronopStato.cronopStatoCode = :cronopStatoCode "
			+ "				AND sc.dataCancellazione IS NULL ) "
			+ " AND c.siacTProgramma.programmaCode = :programmaCode "			
			+ " AND EXISTS (SELECT sp FROM SiacRProgrammaStato sp "
			+ "				WHERE sp.siacDProgrammaStato.programmaStatoCode = :programmaStatoCode "
			+ "				AND sp.siacTProgramma = c.siacTProgramma "
			+ "				AND sp.dataCancellazione IS NULL ) "
//			+ " AND EXISTS (SELECT sp.programmaStatoRId FROM c.siacTProgramma.siacRProgrammaStatos sp "
//			+ "				WHERE sp.siacDProgrammaStato.programmaStatoCode = :programmaStatoCode "
//			+ "				AND sp.dataCancellazione IS NULL ) "
			+ " AND c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND c.dataCancellazione IS NULL "
			+ " AND c.siacTBil.bilId = :bilId ")
	List<SiacTCronop> findCronoprogrammaByCodiceAndStatoOperativoAndCodiceEStatoProgetto(@Param("cronopCode") String cronopCode, @Param("cronopStatoCode") String cronopStatoCode, 
			@Param("programmaCode") String programmaCode, @Param("programmaStatoCode") String programmaStatoCode, @Param("bilId") Integer biId, @Param("enteProprietarioId") Integer enteProprietarioId);
	
	
	/**
	 * Find cronoprogramma by codice and stato operativo and uid progetto.
	 *
	 * @param cronopCode the cronop code
	 * @param cronopStatoCode the cronop stato code
	 * @param programmaId the programma id
	 * @param bilId the bil id
	 * @param enteProprietarioId the ente proprietario id
	 * @return the siac t cronop
	 */
	@Query(" SELECT c FROM SiacTCronop c "
			+ " WHERE c.cronopCode = :cronopCode "			
			+ " AND EXISTS (SELECT sc FROM c.siacRCronopStatos sc"
			+ "				WHERE sc.siacDCronopStato.cronopStatoCode = :cronopStatoCode "
			+ "				AND sc.dataCancellazione IS NULL ) "
			+ " AND c.siacTProgramma.programmaId = :programmaId "
			+ " AND c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND c.dataCancellazione IS NULL "
			+ " AND c.siacTBil.bilId = :bilId ")
	List<SiacTCronop> findCronoprogrammaByCodiceAndStatoOperativoAndUidProgetto(@Param("cronopCode") String cronopCode, @Param("cronopStatoCode") String cronopStatoCode, 
			@Param("programmaId") Integer programmaId, @Param("bilId") Integer bilId, @Param("enteProprietarioId") Integer enteProprietarioId);
	
	/**
	 * Find cronopId by codice and stato operativo and uid progetto and bil id.
	 *
	 * @param cronopCode the cronop code
	 * @param cronopStatoCode the cronop stato code
	 * @param programmaId the programma id
	 * @param enteProprietarioId the ente proprietario id
	 * @param bilId the bil id
	 * @return the siac t cronop
	 */
	@Query(" SELECT c.cronopId "
			+ " FROM SiacTCronop c "
			+ " WHERE c.cronopCode = :cronopCode "
			+ " AND NOT EXISTS ( "
			+ "     SELECT sc "
			+ "     FROM c.siacRCronopStatos sc"
			+ "     WHERE sc.siacDCronopStato.cronopStatoCode = :cronopStatoCodeDaEscludere "
			+ "     AND sc.dataCancellazione IS NULL "
			+ " ) "
			+ " AND c.siacTProgramma.programmaId = :programmaId "
			+ " AND c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND c.siacTBil.bilId = :bilId "
			+ " AND c.dataCancellazione IS NULL ")
	List<Integer> cronopIdNotInStato(@Param("cronopCode") String cronopCode, @Param("cronopStatoCodeDaEscludere") String cronopStatoCodeDaEscludere, 
			@Param("programmaId") Integer programmaId, @Param("enteProprietarioId") Integer enteProprietarioId, @Param("bilId") Integer bilId);


	/**
	 * Find cronop elems by cronop id and elem tipo code.
	 *
	 * @param cronopId the cronop id
	 * @param elemTipoCode the elem tipo code
	 * @return the list
	 */
	@Query(" SELECT ce FROM SiacTCronopElem ce "
			+ " WHERE ce.siacDBilElemTipo.elemTipoCode = :elemTipoCode "
			+ " AND ce.siacTCronop.cronopId = :cronopId "
			+ " AND ce.dataCancellazione IS NULL ")
	List<SiacTCronopElem> findCronopElemsByCronopIdAndElemTipoCode(@Param("cronopId") Integer cronopId, @Param("elemTipoCode") String elemTipoCode);

	/**
	 * Trova gli attributi legati a un cronoprogramma di dato codice
	 * @param cronopId l'id del ceonoprogramma
	 * @param attrCodes i codici degli attributi
	 * @return gli attributi
	 */
	@Query(" FROM SiacRCronopAttr rca "
			+ " WHERE rca.siacTCronop.cronopId = :cronopId "
			+ " AND rca.dataCancellazione IS NULL "
			+ " AND rca.siacTAttr.attrCode IN (:attrCodes) ")
	List<SiacRCronopAttr> findCronopRAttrByCronopIdAndAttrCode(@Param("cronopId") Integer cronopId, @Param("attrCodes") List<String> attrCodes);
	
	@Query(" SELECT c.siacTProgramma.siacDProgrammaTipo FROM SiacTCronop c "
			+ " WHERE c.cronopId = :cronopId "
			+ " AND c.dataCancellazione IS NULL ")
	SiacDProgrammaTipo findSiacDProgrammaTipoByCronopId(@Param("cronopId") Integer cronopId);

	@Query( "SELECT rca.siacTCronop "
			+ " FROM SiacRCronopAttoAmm rca "
			+ " WHERE rca.dataCancellazione IS NULL "
			+ " AND rca.siacTAttoAmm.attoammId = :attoammId  "
			+ " AND rca.siacTCronop.dataCancellazione IS NULL"
			+ " AND EXISTS ( "
			+ "     FROM SiacRCronopStato rcs "
			+ "     WHERE rcs.dataCancellazione IS NULL "
			+ "     AND rcs.siacTCronop = rca.siacTCronop "
			+ "     AND rcs.siacDCronopStato.cronopStatoCode = :cronopStatoCode "
			+ " ) "
			)
	List<SiacTCronop> findCronopBySiacTAttoAmmIdAndStatoCode(@Param("attoammId") Integer attoammId, @Param("cronopStatoCode") String cronopStatoCode);

	@Query( " SELECT tc.cronopCode "
			+ " FROM SiacTCronop tc "
			+ " WHERE tc.dataCancellazione IS NULL  "
			+ " AND tc.siacTProgramma.programmaId = :programmaId "
			+ " AND :sommaImporti < ( "
			+ " 	SELECT COALESCE(SUM(tced.cronopElemDetImporto))"
			+ " 	FROM SiacTCronopElemDet tced "
			+ " 	WHERE tced.siacTCronopElem.siacTCronop = tc   "
			//metodo empirico per selezionare le spese, altrimenti dovrei tirare su un sacco di tabelle
			+ " 	AND tced.annoEntrata IS NOT NULL "
			+ " 	AND tced.siacTCronopElem.dataCancellazione IS NULL "
			+ " 	AND tced.dataCancellazione IS NULL "
			+ " 	GROUP BY tced.siacTCronopElem.siacTCronop.cronopId "
			+ " ) "
			)
	List<String> findCronopCodeConSpeseMaggioriImporto(@Param("programmaId") Integer programmaId, @Param("sommaImporti") BigDecimal sommaImporti);
	
	
	@Query(" SELECT c FROM SiacTCronop c "
			+ " JOIN c.siacRCronopAttoAmms caa "
			+ " WHERE caa.siacTAttoAmm.attoammNumero=:attoammNumero "
			+ " AND caa.siacTAttoAmm.attoammAnno=:attoammAnno "
			+ " AND caa.siacTAttoAmm.siacDAttoAmmTipo.attoammTipoId=:attoammTipoId "
			+ " AND caa.dataCancellazione IS NULL "
			+ " AND c.siacTProgramma.siacDProgrammaTipo.programmaTipoCode='G' "
			+ " AND c.dataCancellazione IS NULL "
			+ " AND (:attoammSacId IS NULL OR EXISTS ("
			+ "		SELECT 1 FROM caa.siacTAttoAmm aa JOIN aa.siacRAttoAmmClasses x WHERE x.siacTClass.classifId=CAST(CAST(:attoammSacId AS string) AS integer)) "
			+ " ) "
			+ " AND NOT EXISTS ( "
			+ "     FROM c.siacRCronopStatos rcs "
			+ "     WHERE rcs.dataCancellazione IS NULL "
			+ "     AND rcs.siacDCronopStato.cronopStatoCode='AN' "
			+ " ) "
			+ " AND NOT EXISTS ( "
			+ "     FROM c.siacTProgramma p "
			+ "     JOIN p.siacRProgrammaStatos ps "
			+ "     WHERE ps.dataCancellazione IS NULL "
			+ "     AND ps.siacDProgrammaStato.programmaStatoCode='AN' "
			+ " ) "
			+ " AND c.siacTEnteProprietario.enteProprietarioId=:enteProprietarioId")
	List<SiacTCronop> findSiacTCronopBySiacTAttoAmm(
			@Param("attoammNumero") Integer attoammNumero, 
			@Param("attoammAnno") String attoammAnno,
			@Param("attoammTipoId") Integer attoammTipoId,
			@Param("attoammSacId") Integer attoammSacId,
			@Param("enteProprietarioId") Integer enteProprietarioId);
	
}
