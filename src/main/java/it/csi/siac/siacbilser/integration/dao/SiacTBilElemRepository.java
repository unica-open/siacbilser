/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacDBilElemCategoria;
import it.csi.siac.siacbilser.integration.entity.SiacDBilElemStato;
import it.csi.siac.siacbilser.integration.entity.SiacRBilElemAttr;
import it.csi.siac.siacbilser.integration.entity.SiacRBilElemClass;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElemDet;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.SiacTPeriodo;

// TODO: Auto-generated Javadoc
/**
 * The Interface SiacTBilElemRepository.
 */
public interface SiacTBilElemRepository extends JpaRepository<SiacTBilElem, Integer>{
	
	/**
	 * Ricerca puntuale capitolo.
	 *
	 * @param enteDto the ente dto
	 * @param annoBilancio the anno bilancio
	 * @param elemTipoCode the elem tipo code
	 * @param numeroCapitolo the numero capitolo
	 * @param numeroArticolo the numero articolo
	 * @param numeroUEB the numero ueb
	 * @param stato the stato
	 * @param pageable the pageable
	 * @return the page
	 */
	@Query(" FROM SiacTBilElem tbe "
			+ " WHERE tbe.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND tbe.dataCancellazione IS NULL "
			+ " AND tbe.siacTBil.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND tbe.siacTBil.siacTPeriodo.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND tbe.siacDBilElemTipo.elemTipoCode = :elemTipoCode "
			+ " AND ( "
			+ "     :annoBilancio = '' "
			+ "     OR :annoBilancio IS NULL "
			+ "     OR tbe.siacTBil.siacTPeriodo.anno = :annoBilancio "
			+ " ) "
			+ " AND ( "
			+ "     :numeroCapitolo = '' "
			+ "     OR :numeroCapitolo IS NULL "
			+ "     OR tbe.elemCode = :numeroCapitolo "
			+ " ) "
			+ " AND ( "
			+ "     :numeroArticolo = '' "
			+ "     OR :numeroArticolo IS NULL  "
			+ "     OR tbe.elemCode2 =  :numeroArticolo "
			+ " ) "
			+ " AND ( "
			+ "     :numeroUEB = '' "
			+ "     OR :numeroUEB IS NULL "
			+ "     OR tbe.elemCode3 =  :numeroUEB "
			+ " ) "
			+ " AND ( "
			+ "     :stato = '' "
			+ "     OR :stato IS NULL "
			+ "     OR EXISTS ( "
			+ "         FROM tbe.siacRBilElemStatos rbes "
			+ "         WHERE rbes.siacDBilElemStato.elemStatoCode = :stato "
			+ "         AND rbes.dataCancellazione IS NULL "
			+ "     ) "
			+ " ) ")
	Page<SiacTBilElem> ricercaPuntualeCapitolo(@Param("enteProprietarioId") Integer enteProprietarioId, 
			@Param("annoBilancio")String annoBilancio,
			@Param("elemTipoCode") String elemTipoCode,
			//@Param("annoCapitolo") String annoCapitolo,
			@Param("numeroCapitolo") String numeroCapitolo,
			@Param("numeroArticolo") String numeroArticolo,
			@Param("numeroUEB") String numeroUEB,
			@Param("stato") String stato,
			Pageable pageable);
		
	
	/**
	 * Ricerca i classificatori associati ad un elemento di bilancio specifico partendo dal suo codice classificatore.
	 *
	 * @param elemId the elem id
	 * @param codice the codice
	 * @return the list
	 */
	@Query("SELECT c FROM SiacTClass c, SiacRBilElemClass r, SiacRBilElemTipoClassTip rt " +	
			"WHERE  r.siacTClass.classifId = c.classifId " +
			"AND r.siacTBilElem.elemId = :elemId " +
			"AND r.dataCancellazione is null " +
			"AND c.classifCode.siacDClassTipo.classifTipoCode = :codice " +
			"AND c.classifCode.siacDClassTipo =  rt.siacDClassTipo " + 
			"AND r.siacTBilElem.siacDBilElemTipo = rt.siacDBilElemTipo " +
			"AND rt.dataCancellazione is null "
			)
	List<SiacTClass> ricercaClassificatoriByClassTipo(@Param("elemId") Integer elemId, @Param("codice")  String codice);
	
	
	/**
	 * Ricerca i classificatori associati ad un elemento di bilancio specifico partendo dal suo codice famiglia.
	 *
	 * @param elemId the elem id
	 * @param codiceFam the codice fam
	 * @return the list
	 */
	@Query("SELECT c FROM SiacTClass c, SiacRBilElemClass r " + //, SiacRBilElemTipoClassTip rt "+ //, SiacDClassFam td,  " +
			//" , SiacRClassFamTree t " +
			"WHERE r.siacTClass.classifId = c.classifId " +
//			"AND t.siacTClassFiglio.classifId = r.siacTClass.classifId " +
			"AND r.siacTBilElem.elemId = :elemId " +
			"AND r.dataCancellazione is null " +
			"AND c.dataCancellazione is null " +
			//"AND rt.dataCancellazione is null " +
			
			"AND EXISTS (FROM c.siacRClassFamTreesFiglio rc "+
			"			WHERE rc.siacTClassFamTree.siacDClassFam.classifFamCode = :codiceFam"
			+ "         AND rc.dataCancellazione IS NULL ) "//+
			
			// Prima era : "AND t.siacTClassFamTree.classifFamTreeId = td.classifFamId "+
			//"AND td.classifFamCode = :codiceFam " + 
			
			//"AND c.siacDClassTipo = rt.siacDClassTipo " + 
			//"AND r.siacTBilElem.siacDBilElemTipo = rt.siacDBilElemTipo "
			
			)
	/*
	 *  FIXME: mettere a posto il controllo di legame tra tipo di capitolo e tipo di classificatore.
	 *  La versione precedente alla data 04/09/2014 (lasciata commentata) controlla che esista un legame su SiacRBilElemTipoClassTip.
	 *  Problemi nella soluzione precedentemente adottata:
	 *    1. Nel caso in cui la persistenza del dato inserisca un classificatore non legato (possibile in quanto l'inserimento non effettua controlli in tal senso)
	 *       il dato non viene reperito dalla presente query. Il dato e' inserito ma effettivamente  irrangiungibile.
	 *  
	 *  La nuova versione non effettua il controllo di coerenza del dato. Aggiungere il controllo di coerenza quando si sara' sicuri di non poter inserire
	 *  un dato non corretto
	 */
	List<SiacTClass> ricercaClassificatoriByClassFam(@Param("elemId") Integer elemId, @Param("codiceFam")  String codiceFam);



	
	
	/**
	 * Ritrova l'ex capitolo del tipo specificato come parametro.
	 *
	 * @param elemId the elem id
	 * @param elemTipoCode the elem tipo code
	 * @return the siac t bil elem
	 */
	@Query("SELECT brt.siacTBilElemOld " +
          "   FROM   SiacRBilElemRelTempo brt "+
          "   WHERE  brt.siacTBilElem.elemId = :elemId "+
          "   AND brt.siacTBilElemOld.siacDBilElemTipo.elemTipoCode=  :elemTipoCode " + //elemTipoCode: SiacDBilElemTipoEnum.CapitoloUscitaGestione	
          "   AND brt.dataCancellazione is null "
	           )
	List<SiacTBilElem> findRelTempoByElemTipoCode(@Param("elemId") Integer elemId, @Param("elemTipoCode") String elemTipoCode);
	
	
	
	/**
	 * Ritrova ad esempio un "ex gestione" a partire da un capitolo uscita previsione.
	 *
	 * @param elemId the elem id
	 * @param elemTipoCode the elem tipo code del capitolo ex
	 * @return the siac t bil elem
	 */
	@Query("SELECT ce " +
          "   FROM   SiacTBilElem c, SiacTBilElem ce "+
          "   WHERE  c.elemId = :elemId "+
          "   AND c.elemCode = ce.elemCode " +
          "   AND c.elemCode2 = ce.elemCode2 " +
          "   AND c.elemCode3 = ce.elemCode3 " +
          "	  AND (CAST(c.siacTBil.siacTPeriodo.anno as int) - 1) = CAST(ce.siacTBil.siacTPeriodo.anno as int) " +  //cast(a.id as string)
          "   AND ce.siacDBilElemTipo.elemTipoCode = :elemTipoCode " +
          "   AND ce.dataCancellazione IS NULL "+
          "   AND c.siacTEnteProprietario.enteProprietarioId = ce.siacTEnteProprietario.enteProprietarioId "
	           )
	List<SiacTBilElem> findCapitoloExByIdAndTipoCode(@Param("elemId") Integer elemId, @Param("elemTipoCode") String elemTipoCode);
	
	
	
	/**
	 * Ritrova ad esempio un "equivalente gestione" a partire da un capitolo uscita previsione.
	 *
	 * @param elemId the elem id
	 * @param elemTipoCode the elem tipo code del capitolo equivalente
	 * @return the siac t bil elem
	 */
	@Query("SELECT ce " +
          "   FROM   SiacTBilElem c, SiacTBilElem ce "+
          "   WHERE  c.elemId = :elemId "+
          "   AND c.elemCode = ce.elemCode " +
          "   AND c.elemCode2 = ce.elemCode2 " +
          "   AND c.elemCode3 = ce.elemCode3 " +
          "	  AND CAST(c.siacTBil.siacTPeriodo.anno as int) = CAST(ce.siacTBil.siacTPeriodo.anno as int) " +  //cast(a.id as string)
          "   AND ce.siacDBilElemTipo.elemTipoCode = :elemTipoCode " +
          "   AND ce.dataCancellazione IS NULL " +
          "   AND c.siacTEnteProprietario.enteProprietarioId = ce.siacTEnteProprietario.enteProprietarioId "
	           )
	List<SiacTBilElem> findCapitoloEquivalenteByIdAndTipoCode(@Param("elemId") Integer elemId, @Param("elemTipoCode") String elemTipoCode);
	
	
	/**
	 * Ricerca l'id della relazione tra un elemento di bilancio ed un classificatore della famiglia specificata come parametro.
	 *
	 * @param elemId the elem id
	 * @param famigliaCode the famiglia code
	 * @return the integer
	 */
	@Query(	
//			"SELECT ec.elemClassifId " +
//			"FROM SiacRBilElemClass ec " +
//			"WHERE ec.siacTBilElem.elemId = :elemId "+
//			"AND EXISTS ( SELECT cft "+
//			"		     FROM ec.siacTClass.siacRClassFamTreesFiglio cft "+
//			"		     WHERE cft.siacTClassFamTree.siacDClassFam.classifFamCode = :famigliaCode "+
//			"		) "
			
			" SELECT DISTINCT ec.elemClassifId " +
			" FROM SiacRBilElemClass ec, SiacRClassFamTree cft " +
			" WHERE ec.siacTBilElem.elemId = :elemId " +
			" AND cft.siacTClassFiglio = ec.siacTClass " +
			" AND cft.siacTClassFamTree.siacDClassFam.classifFamCode = :famigliaCode "
			//NON deve escludere quelli con dataCancellazione valorizzata
		) 
	Integer findRBilElemClassIdByElemIdAndFamiglia(@Param("elemId") Integer elemId, @Param("famigliaCode") String famigliaCode);
	
	
	@Query(	
//			"SELECT ec.siacTClass " +
//			"FROM SiacRBilElemClass ec " +
//			"WHERE ec.siacTBilElem.elemId = :elemId "+
//			"AND EXISTS ( SELECT cft "+
//			"		     FROM ec.siacTClass.siacRClassFamTreesFiglio cft "+
//			"		     WHERE cft.siacTClassFamTree.siacDClassFam.classifFamCode = :famigliaCode "+
//			"		) " +
//			"AND ec.dataCancellazione IS NULL "
			
			" SELECT DISTINCT ec.siacTClass " +
			" FROM SiacRBilElemClass ec, SiacRClassFamTree cft " +
			" WHERE ec.siacTBilElem.elemId = :elemId "+
			" AND cft.siacTClassFiglio = ec.siacTClass " +
			" AND cft.siacTClassFamTree.siacDClassFam.classifFamCode = :famigliaCode "+
			" AND ec.dataCancellazione IS NULL "
			
		) 
	SiacTClass findTClassIdByElemIdAndFamiglia(@Param("elemId") Integer elemId, @Param("famigliaCode") String famigliaCode);
	
	/**
	 * Ricerca l'id della relazione tra un elemento di bilancio ed un classificatore del tipo specificato come parametro.
	 *
	 * @param elemId the elem id
	 * @param classifTipoCode the classif tipo code
	 * @return the integer
	 */
	@Query(	"SELECT ec.elemClassifId " +
			"FROM SiacRBilElemClass ec " +
			"WHERE ec.siacTBilElem.elemId = :elemId "+
			"AND ec.siacTClass.siacDClassTipo.classifTipoCode = :classifTipoCode "
			//NON deve escludere quelli con dataCancellazione valorizzata
		) 
	Integer findRBilElemClassIdByElemIdAndTipoCode(@Param("elemId") Integer elemId, @Param("classifTipoCode") String classifTipoCode);

	@Query(	"SELECT ec.siacTClass " +
			"FROM SiacRBilElemClass ec " +
			"WHERE ec.siacTBilElem.elemId = :elemId "+
			"AND ec.siacTClass.siacDClassTipo.classifTipoCode = :classifTipoCode "+
			"AND ec.dataCancellazione IS NULL "
		) 
	SiacTClass findTClassIdByElemIdAndTipoCode(@Param("elemId") Integer elemId, @Param("classifTipoCode") String classifTipoCode);
	

	/**
	 * Ricerca l'id di un elemento di dettaglio (importo) del tipo passato come parametro (elemDetTipoCode) asssociato ad un elemento di bilancio (elemId).
	 *
	 * @param elemId the elem id
	 * @param elemDetTipoCode the elem det tipo code
	 * @param periodo the periodo
	 * @return the integer
	 */
	@Query("SELECT ed.elemDetId " +
			" FROM SiacTBilElemDet ed " +
			" WHERE ed.siacTBilElem.elemId = :elemId " +
			" AND ed.siacDBilElemDetTipo.elemDetTipoCode = :elemDetTipoCode " +
			" AND ed.siacTPeriodo = :periodo " + 
			" AND ed.dataCancellazione IS NULL "
			//NON deve escludere quelli con dataCancellazione valorizzata
			)
	Integer findIdElemDetAssiociatoACapitoloByTipo( @Param("elemId")  Integer elemId, @Param("elemDetTipoCode") String elemDetTipoCode, @Param("periodo") SiacTPeriodo periodo);
	/**
	 * Ricerca l'id di un elemento di dettaglio (importo) del tipo passato come parametro (elemDetTipoCode) asssociato ad un elemento di bilancio (elemId).
	 *
	 * @param elemId the elem id
	 * @param elemDetTipoCode the elem det tipo code
	 * @param periodo the periodo
	 * @return the integer
	 */
	@Query(" FROM SiacTBilElemDet ed " +
			" WHERE ed.siacTBilElem.elemId = :elemId " +
			" AND ed.siacDBilElemDetTipo.elemDetTipoCode = :elemDetTipoCode " +
			" AND ed.siacTPeriodo.anno = :anno " + 
			" AND ed.dataCancellazione IS NULL ")
	SiacTBilElemDet findSiacTBilElemDetAssiociatoACapitoloByTipo( @Param("elemId") Integer elemId, @Param("elemDetTipoCode") String elemDetTipoCode, @Param("anno") String anno);
	
	
	/**
	 * Find id bil elem attr associato a capitolo by tipo attr code.
	 *
	 * @param elemId the elem id
	 * @param attrCode the attr code
	 * @return the integer
	 */
	@Query("SELECT a.bilElemAttrId " +
			"FROM SiacRBilElemAttr a " +
			"WHERE a.siacTBilElem.elemId = :elemId " +
			"AND a.siacTAttr.attrCode = :attrCode "
			//NON deve escludere quelli con dataCancellazione valorizzata
			)
	Integer findIdBilElemAttrAssociatoACapitoloByTipoAttrCode( @Param("elemId") Integer elemId, @Param("attrCode") String attrCode);
	
	
	@Query("FROM SiacRBilElemAttr a " +
			"WHERE a.siacTBilElem.elemId = :elemId " +
			"AND a.siacTAttr.attrCode = :attrCode " +
			"AND a.dataCancellazione IS NULL "
			)
	SiacRBilElemAttr findBilElemAttrByTipoAttrCode( @Param("elemId") Integer elemId, @Param("attrCode") String attrCode);
	
	@Query("FROM SiacRBilElemClass a " +
			"WHERE a.siacTBilElem.elemId = :elemId " +
			"AND a.siacTClass.siacDClassTipo.classifTipoCode IN (:classifTipoCodes) " +
			"AND a.dataCancellazione IS NULL ")
	SiacRBilElemClass findBilElemClassByTipoClassCodes( @Param("elemId") Integer elemId, @Param("classifTipoCodes") Collection<String> classifTipoCodes);
	
	
	/**
	 * Find elem tipo code by elem id.
	 *
	 * @param elemId the elem id
	 * @return the string
	 */
	@Query("SELECT c.siacDBilElemTipo.elemTipoCode " +
			"FROM  SiacTBilElem c  " +
			"WHERE c.elemId = :elemId "
			)
	String findElemTipoCodeByElemId( @Param("elemId") Integer elemId);
	
	
	

	/**
	 * Count capitoli.
	 *
	 * @param enteProprietarioId the ente proprietario id
	 * @param annoBilancio the anno bilancio
	 * @param elemTipoCode the elem tipo code
	 * @param numeroCapitolo the numero capitolo
	 * @param numeroArticolo the numero articolo
	 * @param numeroUEB the numero ueb
	 * @return the long
	 */
	@Query(" SELECT COUNT(*) " 
			+ " FROM  SiacTBilElem c  " 
			+ " WHERE c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " 
			+ " AND c.siacTBil.siacTPeriodo.anno = :annoBilancio "	
			+ " AND c.dataCancellazione IS NULL "
			+ " AND c.siacDBilElemTipo.elemTipoCode = :elemTipoCode "
			+ " AND ( :numeroCapitolo = '' OR :numeroCapitolo IS NULL  "
			+ "       OR c.elemCode = :numeroCapitolo ) "
			+ " AND ( :numeroArticolo = '' OR :numeroArticolo IS NULL  "
			+ "       OR c.elemCode2 =  :numeroArticolo) "
			+ " AND ( :numeroUEB = '' OR :numeroUEB IS NULL "
			+ "       OR c.elemCode3 =  :numeroUEB) "
			
			)
	Long countCapitoli(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("annoBilancio") String annoBilancio, 
			@Param("elemTipoCode") String elemTipoCode,
			@Param("numeroCapitolo") String numeroCapitolo, @Param("numeroArticolo") String numeroArticolo, @Param("numeroUEB") String numeroUEB);
	
	
	/**
	 * Find capitoli elem ids.
	 *
	 * @param enteProprietarioId the ente proprietario id
	 * @param annoBilancio the anno bilancio
	 * @param elemTipoCode the elem tipo code
	 * @param numeroCapitolo the numero capitolo
	 * @param numeroArticolo the numero articolo
	 * @param numeroUEB the numero ueb
	 * @param elemStatoCode the elem stato code
	 * @return the list
	 */
	@Query(" SELECT c.elemId " 
			+ " FROM  SiacTBilElem c  " 
			+ " WHERE c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " 
			+ " AND c.siacTBil.siacTPeriodo.anno = :annoBilancio "	
			+ " AND c.dataCancellazione IS NULL "
			+ " AND c.siacDBilElemTipo.elemTipoCode = :elemTipoCode "
			+ " AND ( :numeroCapitolo = '' OR :numeroCapitolo IS NULL  "
			+ "       OR c.elemCode = :numeroCapitolo ) "
			+ " AND ( :numeroArticolo = '' OR :numeroArticolo IS NULL  "
			+ "       OR c.elemCode2 =  :numeroArticolo) "
			+ " AND ( :numeroUEB = '' OR :numeroUEB IS NULL "
			+ "       OR c.elemCode3 =  :numeroUEB) "
			+ " AND ( :elemStatoCode = '' OR :elemStatoCode IS NULL  "
			+ "              OR EXISTS (SELECT sb "
			+ "                         FROM   c.siacRBilElemStatos sb "
			+ "                         WHERE  sb.siacDBilElemStato.elemStatoCode = :elemStatoCode ) ) "
			
			)
	List<Integer> findCapitoliElemIds(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("annoBilancio") String annoBilancio, @Param("elemTipoCode") String elemTipoCode,
			@Param("numeroCapitolo") String numeroCapitolo, @Param("numeroArticolo") String numeroArticolo, @Param("numeroUEB") String numeroUEB, @Param("elemStatoCode") String elemStatoCode);
	
	
	@Query(" SELECT cup " 
	        + " FROM SiacTBilElem cup "
			+ " WHERE cup.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND cup.dataCancellazione IS NULL "
			+ " AND cup.siacTBil.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND cup.siacTBil.siacTPeriodo.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND cup.siacTBil.siacTPeriodo.anno = :annoBilancio "
			+ " AND cup.elemCode = :numeroCapitolo "
			+ " AND cup.elemCode2 =  :numeroArticolo "
			+ " AND cup.siacDBilElemTipo.elemTipoCode = :elemTipoCode "
			+ " AND EXISTS ( "
			+ "     FROM cup.siacRBilElemStipendioCodices rbesc "
			+ "     WHERE rbesc.siacDStipendioCodice.stipcodeCode  = :stipcodeCode "
			+ "     AND rbesc.siacDStipendioCodice.dataCancellazione IS NULL "
			+ "     AND rbesc.dataCancellazione IS NULL "
			+ " ) "
			+ " ORDER BY cup.elemCode3 "
			)
	List<SiacTBilElem> ricercaCapitoliFromStipendio(@Param("enteProprietarioId") Integer enteProprietarioId, 
			@Param("annoBilancio")String annoBilancio,
			@Param("numeroCapitolo") String numeroCapitolo,
			@Param("numeroArticolo") String numeroArticolo,
	        @Param("stipcodeCode") String stipcodeCode,
	        @Param("elemTipoCode") String elemTipoCode);
	

	@Query(" SELECT c " 
	        + " FROM SiacTBilElem c "
			+ " WHERE c.dataCancellazione IS NULL "
			+ " AND EXISTS ( FROM c.siacRMovgestBilElems rmg "
			+ "     		 WHERE rmg.dataCancellazione IS NULL "
			+ "     		 AND rmg.siacTMovgest.movgestId = :movgestId "
			+ " 			) "
			)
	SiacTBilElem findBilElemByMovGest(@Param("movgestId")Integer movgestId);
/*
	@Query("SELECT COALESCE(COUNT(r), 0)"
			+ " FROM SiacRMovgestBilElem r "
			+ " WHERE r.dataCancellazione IS NULL "
			+ " AND r.dataFineValidita IS NULL"
			+ " AND r.siacTMovgest.dataCancellazione IS NULL "
			+ " AND EXISTS ( "
			//+ "     FROM r.siacTMovgest.siacTMovgestTs t1, SiacTMovgestT tmt "
			//+ "     WHERE t1 = tmt "
			//+ "     AND tmt.dataCancellazione IS NULL "
			+ "     FROM r.siacTMovgest.siacTMovgestTs tmt "
			+ "     WHERE tmt.dataCancellazione IS NULL "
			+ "     AND tmt.dataFineValidita IS NULL "
			+ "     AND EXISTS ( "
			+ "         FROM tmt.siacRMovgestTsStatos st "
			+ "         WHERE st.dataCancellazione IS NULL "
			+ "         AND st.dataFineValidita IS NULL "
			+ "         AND st.siacDMovgestStato.movgestStatoCode <> 'A' "
			+ "     ) "
			+ " ) "
			+ " AND r.siacTBilElem.elemId = :elemId "
			)*/
	@Query("SELECT COALESCE(COUNT(tm), 0)"
			+ " FROM SiacTMovgest tm "
			+ " WHERE tm.dataCancellazione IS NULL "
			+ " AND tm.dataFineValidita IS NULL"
			+ " AND EXISTS ( "
			+ "     FROM tm.siacRMovgestBilElems rmbe "
			+ "     WHERE rmbe.dataCancellazione IS NULL "
			+ "     AND rmbe.dataFineValidita IS NULL "
			+ "     AND rmbe.siacTBilElem.elemId = :elemId "
			+ " ) "
			+ " AND EXISTS ( "
			+ "     FROM tm.siacTMovgestTs tmt "
			+ "     WHERE tmt.dataCancellazione IS NULL "
			+ "     AND tmt.dataFineValidita IS NULL "
			+ "     AND EXISTS ( "
			+ "         FROM tmt.siacRMovgestTsStatos st "
			+ "         WHERE st.dataCancellazione IS NULL "
			+ "         AND st.dataFineValidita IS NULL "
			+ "         AND st.siacDMovgestStato.movgestStatoCode <> 'A' "
			+ "     ) "
			+ " ) "
			)
	Long countMovgestNonAnnullatiByBilElemId(@Param("elemId")Integer elemId);

	@Query("SELECT COALESCE(COUNT(tm), 0)"
			+ " FROM SiacTMovgest tm "
			+ " WHERE tm.dataCancellazione IS NULL "
			+ " AND tm.dataFineValidita IS NULL"
			+ " AND EXISTS ( "
			+ "     FROM tm.siacRMovgestBilElems rmbe "
			+ "     WHERE rmbe.dataCancellazione IS NULL "
			+ "     AND rmbe.dataFineValidita IS NULL "
			+ "     AND rmbe.siacTBilElem.elemId IN (:elemIds) "
			+ " ) "
			+ " AND EXISTS ( "
			+ "     FROM tm.siacTMovgestTs tmt "
			+ "     WHERE tmt.dataCancellazione IS NULL "
			+ "     AND tmt.dataFineValidita IS NULL "
			+ "     AND EXISTS ( "
			+ "         FROM tmt.siacRMovgestTsStatos st "
			+ "         WHERE st.dataCancellazione IS NULL "
			+ "         AND st.dataFineValidita IS NULL "
			+ "         AND st.siacDMovgestStato.movgestStatoCode <> 'A' "
			+ "     ) "
			+ " ) "
			)
	Long countMovgestNonAnnullatiByBilElemIds(@Param("elemIds") List<Integer> elemIds);

	@Query("SELECT COALESCE(COUNT(tv), 0)"
			+ " FROM SiacTVincolo tv "
			+ " WHERE tv.dataCancellazione IS NULL "
			+ " AND EXISTS ( "
			+ "     FROM tv.siacRVincoloBilElems rvbe "
			+ "     WHERE rvbe.dataCancellazione IS NULL "
			+ "     AND rvbe.siacTBilElem.elemId IN (:elemIds) "
			+ " ) "
			+ " AND EXISTS ( "
			+ "     FROM tv.siacRVincoloStatos rvs "
			+ "     WHERE rvs.dataCancellazione IS NULL "
			+ "     AND rvs.siacDVincoloStato.vincoloStatoCode IN (:vincoloStatoCode) "
			+ " ) ")
	Long countSiacTVincoloNonAnnullatiByBilElemId(@Param("elemIds") Collection<Integer> elemIds, @Param("vincoloStatoCode") Collection<String> vincoloStatoCode);

	@Query("SELECT COALESCE(COUNT(tv), 0)"
			+ " FROM SiacTVariazione tv "
			+ " WHERE tv.dataCancellazione IS NULL "
			+ " AND tv.siacDVariazioneTipo.variazioneTipoCode <> 'CD' "
			+ " AND EXISTS ( "
			+ "     FROM SiacRVariazioneStato rvs, tv.siacRVariazioneStatos rvs2 "
			+ "     WHERE rvs = rvs2 "
			+ "     AND rvs.dataCancellazione IS NULL "
			+ "     AND rvs.siacDVariazioneStato.variazioneStatoTipoCode IN (:variazioneStatoTipoCodes) "
			+ "     AND EXISTS ( "
			+ "         FROM rvs.siacTBilElemDetVars tbedv "
			+ "         WHERE tbedv.dataCancellazione IS NULL "
			+ "         AND tbedv.siacTBilElem.elemId IN (:elemIds) "
			+ "     ) "
			+ " ) "
			)
	Long countSiacTVariazioniImportiByBilElemIdAndStatoCodes(@Param("elemIds")Collection<Integer> elemIds, @Param("variazioneStatoTipoCodes") Collection<String> variazioneStatoTipoCodes);
	
	@Query("SELECT COALESCE(COUNT(tv), 0)"
			+ " FROM SiacTVariazione tv "
			+ " WHERE tv.dataCancellazione IS NULL "
			+ " AND tv.siacDVariazioneTipo.variazioneTipoCode = 'CD' "
			+ " AND EXISTS ( "
			+ "     FROM SiacRVariazioneStato rvs, tv.siacRVariazioneStatos rvs2 "
			+ "     WHERE rvs = rvs2 "
			+ "     AND rvs.dataCancellazione IS NULL "
			+ "     AND rvs.siacDVariazioneStato.variazioneStatoTipoCode IN (:variazioneStatoTipoCodes) "
			+ "     AND EXISTS ( "
			+ "         FROM rvs.siacRBilElemClassVars rbecv "
			+ "         WHERE rbecv.dataCancellazione IS NULL "
			+ "         AND rbecv.siacTBilElem.elemId IN (:elemIds) "
			+ "     ) "
			+ " ) "
			)
	Long countSiacTVariazioniCodificheByBilElemIdAndStatoCodes(@Param("elemIds")Collection<Integer> elemIds, @Param("variazioneStatoTipoCodes") Collection<String> variazioneStatoTipoCodes);
	
	@Query("SELECT COALESCE(COUNT(tm), 0)"
			+ " FROM SiacTMovgest tm "
			+ " WHERE tm.dataCancellazione IS NULL "
			+ " AND tm.dataFineValidita IS NULL"
			+ " AND EXISTS ( "
			+ "     FROM tm.siacRMovgestBilElems rmbe "
			+ "     WHERE rmbe.dataCancellazione IS NULL "
			+ "     AND rmbe.dataFineValidita IS NULL "
			+ "     AND rmbe.siacTBilElem.elemId IN (:elemIds) "
			+ " ) "
			+ " AND EXISTS ( "
			+ "     FROM tm.siacTMovgestTs tmt "
			+ "     WHERE tmt.dataCancellazione IS NULL "
			+ "     AND tmt.dataFineValidita IS NULL "
			+ "     AND EXISTS ( "
			+ "         FROM tmt.siacRMovgestTsStatos st "
			+ "         WHERE st.dataCancellazione IS NULL "
			+ "         AND st.dataFineValidita IS NULL "
			+ "         AND st.siacDMovgestStato.movgestStatoCode IN (:movgestStatoCodes) "
			+ "     ) "
			+ " ) "
			)
	Long countMovgestByBilElemIdsAndStatoCodes(@Param("elemIds") List<Integer> elemIds, @Param("movgestStatoCodes") Collection<String> movgestStatoCodes);

	@Query(" SELECT rbes.siacDBilElemStato "
			+ " FROM SiacRBilElemStato rbes "
			+ " WHERE rbes.dataCancellazione IS NULL "
			+ " AND rbes.dataFineValidita IS NULL "
			+ " AND rbes.siacTBilElem.elemId = :elemId ")
	List<SiacDBilElemStato> findSiacDBilElemStatosByElemId(@Param("elemId") Integer elemId);


	@Query(" SELECT rbec.siacDBilElemCategoria "
			+ " FROM SiacRBilElemCategoria rbec "
			+ " WHERE rbec.dataCancellazione IS NULL "
			+ " AND rbec.dataFineValidita IS NULL "
			+ " AND rbec.siacTBilElem.elemId = :elemId ")
	List<SiacDBilElemCategoria> findSiacDBilElemCategoriaByElemId(@Param("elemId") Integer elemId);
	

	@Query(" SELECT rbec.siacDBilElemCategoria "
			+ " FROM SiacRBilElemCategoria rbec "
			+ " WHERE rbec.dataCancellazione IS NULL "
			+ " AND rbec.dataFineValidita IS NULL "
			+ " AND rbec.siacTBilElem.elemId = :elemId ")
	List<Object[]> findCapitoliBuSubdocId(@Param("elemId") Integer elemId);

	@Query("  FROM SiacTBilElem tbe "
			+ " WHERE tbe.elemId = :elemId "
			+ " AND EXISTS( "
			+ "    FROM SiacRBilElemCategoria rbec "
			+ "    WHERE rbec.dataCancellazione IS NULL "
			+ "    AND rbec.dataFineValidita IS NULL "
			+ "    AND rbec.siacTBilElem = tbe"
			+ "    AND rbec.siacDBilElemCategoria.elemCatCode = :categoriaString"
			+ ") AND EXISTS("
			+ "    FROM SiacRBilElemClass rbecl "
			+ "    WHERE rbecl.dataCancellazione IS NULL "
			+ "    AND rbecl.dataFineValidita IS NULL "
			+ "    AND rbecl.siacTBilElem = tbe"
			+ "    AND rbecl.siacTClass.classifCode = :classifCode "
			+ "    AND rbecl.siacTClass.siacDClassTipo.classifTipoCode = :classifTipoCode "
			+ ")")
	SiacTBilElem findCapitoloByCategoriaEClassificatoreGenerico(@Param("elemId") Integer elemId, @Param("categoriaString") String categoriaString, @Param("classifTipoCode") String classifTipoCode, @Param("classifCode") String classifCode);
}
