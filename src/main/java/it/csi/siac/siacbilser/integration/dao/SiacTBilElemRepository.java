/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.csi.siac.siacbilser.integration.entity.SiacDBilElemCategoria;
import it.csi.siac.siacbilser.integration.entity.SiacDBilElemStato;
import it.csi.siac.siacbilser.integration.entity.SiacRBilElemAttr;
import it.csi.siac.siacbilser.integration.entity.SiacRBilElemClass;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElemDet;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.SiacTPeriodo;
import it.csi.siac.siacbilser.model.VincoliCapitoloUEGest;

/**
 * The Interface SiacTBilElemRepository.
 */
@Repository
public interface SiacTBilElemRepository extends JpaRepository<SiacTBilElem, Integer>{
	
	public static final String EXISTS_FOR_SIAC_T_BIL_ELEM_FONDINO =  " AND EXISTS( "
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
			+ ")";
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
	
//	@Query("  FROM SiacTBilElem tbe "
//			+ " WHERE tbe.elemId = :elemId "
//			+ EXISTS_FOR_SIAC_T_BIL_ELEM_FONDINO)
//	SiacTBilElem findCapitoloByCategoriaEClassificatoreGenerico(@Param("elemId") Integer elemId, @Param("categoriaString") String categoriaString, @Param("classifTipoCode") String classifTipoCode, @Param("classifCode") String classifCode);
//	
	@Query( " SELECT tbe.elemId "
			+ "  FROM SiacTBilElem tbe "
			+ " WHERE tbe.elemId in (:elemIds) "
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
	List<Integer> findCapitoloByCategoriaEClassificatoreGenerico(@Param("elemIds") List<Integer> elemIds, @Param("categoriaString") String categoriaString, @Param("classifTipoCode") String classifTipoCode, @Param("classifCode") String classifCode);
	
	// SIAC-7858
	@Query(value = " WITH tmp AS ( "
			+ "    SELECT tp.anno, tmtd.movgest_ts_det_importo "
			+ "    FROM siac_t_bil_elem tbe_new "
			+ "    JOIN siac_t_bil_elem tbe ON ( tbe_new.elem_code = tbe.elem_code AND tbe_new.elem_code2 = tbe.elem_code2 AND tbe_new.elem_code3 = tbe.elem_code3 AND tbe_new.ente_proprietario_id = tbe.ente_proprietario_id AND tbe.data_cancellazione IS NULL ) "
			+ "    JOIN siac_t_bil tb ON ( tb.bil_id = tbe.bil_id ) "
			+ "    JOIN siac_t_periodo tp ON ( tp.periodo_id = tb.periodo_id ) "
			+ "    JOIN siac_d_bil_elem_tipo dbet ON ( dbet.elem_tipo_id = tbe.elem_tipo_id ) "
			+ "    JOIN siac_r_bil_elem_stato rbes ON ( rbes.elem_id = tbe.elem_id AND rbes.data_cancellazione IS NULL ) "
			+ "    JOIN siac_d_bil_elem_stato dbes ON ( dbes.elem_stato_id = rbes.elem_stato_id ) "
			+ "    JOIN siac_r_movgest_bil_elem rmbe ON ( rmbe.elem_id = tbe.elem_id AND rmbe.data_cancellazione IS NULL ) "
			+ "    JOIN siac_t_movgest tm ON ( tm.movgest_id = rmbe.movgest_id ) "
			+ "    JOIN siac_t_movgest_ts tmt ON ( tmt.movgest_id = tm.movgest_id ) "
			+ "    JOIN siac_r_movgest_ts_stato rmts ON ( rmts.movgest_ts_id = tmt.movgest_ts_id AND rmts.data_cancellazione IS NULL ) "
			+ "    JOIN siac_d_movgest_stato dms ON ( dms.movgest_stato_id = rmts.movgest_stato_id ) "
			+ "    JOIN siac_t_movgest_ts_det tmtd ON ( tmtd.movgest_ts_id = tmt.movgest_ts_id ) "
			+ "    JOIN siac_d_movgest_ts_det_tipo dmtdt ON ( dmtdt.movgest_ts_det_tipo_id = tmtd.movgest_ts_det_tipo_id ) "
			+ "    WHERE tbe_new.elem_id = :elemId "
			+ "    AND dbet.elem_tipo_code = 'CAP-EG' "
			+ "    AND CAST(tp.anno AS int) >= :annoMin "
			+ "    AND CAST(tp.anno AS int) <= :annoMax "
			+ "    AND dbes.elem_stato_code = 'VA' "
			+ "    AND tm.movgest_anno = CAST(tp.anno AS INTEGER) "
			+ "    AND dms.movgest_stato_code <> 'A' "
			+ "    AND dmtdt.movgest_ts_det_tipo_code = 'A' "
			+ " ) "
			+ " SELECT tp.anno, COALESCE(SUM(tmp.movgest_ts_det_importo), 0) "
			+ " FROM siac_t_bil_elem tbe_new "
			+ " JOIN siac_t_bil_elem tbe ON ( tbe_new.elem_code = tbe.elem_code AND tbe_new.elem_code2 = tbe.elem_code2 AND tbe_new.elem_code3 = tbe.elem_code3 AND tbe_new.ente_proprietario_id = tbe.ente_proprietario_id AND tbe.data_cancellazione IS NULL ) "
			+ " JOIN siac_t_bil tb ON ( tb.bil_id = tbe.bil_id ) "
			+ " JOIN siac_t_periodo tp ON ( tp.periodo_id = tb.periodo_id ) "
			+ " JOIN siac_d_bil_elem_tipo dbet ON ( dbet.elem_tipo_id = tbe.elem_tipo_id ) "
			+ " JOIN siac_r_bil_elem_stato rbes ON ( rbes.elem_id = tbe.elem_id AND rbes.data_cancellazione IS NULL ) "
			+ " JOIN siac_d_bil_elem_stato dbes ON ( dbes.elem_stato_id = rbes.elem_stato_id ) "
			+ " LEFT OUTER JOIN tmp ON ( tmp.anno = tp.anno ) "
			+ " WHERE tbe_new.elem_id = :elemId "
			+ " AND dbet.elem_tipo_code = 'CAP-EG' "
			+ " AND CAST(tp.anno AS int) >= :annoMin "
			+ " AND CAST(tp.anno AS int) <= :annoMax "
			+ " AND dbes.elem_stato_code = 'VA' "
			+ " GROUP BY tp.anno ",
		nativeQuery = true)
	List<Object[]> findImportoAccertatoPerAnno(@Param("elemId") Integer elemId, @Param("annoMin") Integer annoMin, @Param("annoMax") Integer annoMax);

	@Query(value = " WITH tmp AS ( "
			+ "    SELECT tp.anno, totd.ord_ts_det_importo "
			+ "    FROM siac_t_bil_elem tbe_new "
			+ "    JOIN siac_t_bil_elem tbe ON ( tbe_new.elem_code = tbe.elem_code AND tbe_new.elem_code2 = tbe.elem_code2 AND tbe_new.elem_code3 = tbe.elem_code3 AND tbe_new.ente_proprietario_id = tbe.ente_proprietario_id AND tbe.data_cancellazione is null ) "
			+ "    JOIN siac_t_bil tb ON ( tb.bil_id = tbe.bil_id ) "
			+ "    JOIN siac_t_periodo tp ON ( tp.periodo_id = tb.periodo_id ) "
			+ "    JOIN siac_d_bil_elem_tipo dbet ON ( dbet.elem_tipo_id = tbe.elem_tipo_id ) "
			+ "    JOIN siac_r_bil_elem_stato rbes ON ( rbes.elem_id = tbe.elem_id	and rbes.data_cancellazione is null ) "
			+ "    JOIN siac_d_bil_elem_stato dbes ON ( dbes.elem_stato_id = rbes.elem_stato_id ) "
			+ "    JOIN siac_r_movgest_bil_elem rmbe ON ( rmbe.elem_id = tbe.elem_id	and rmbe.data_cancellazione is null ) "
			+ "    JOIN siac_t_movgest tm ON ( tm.movgest_id = rmbe.movgest_id ) "
			+ "    JOIN siac_t_movgest_ts tmt ON ( tmt.movgest_id = tm.movgest_id ) "
			+ "    JOIN siac_r_movgest_ts_stato rmts ON ( rmts.movgest_ts_id = tmt.movgest_ts_id	and rmts.data_cancellazione is null ) "
			+ "    JOIN siac_d_movgest_stato dms ON ( dms.movgest_stato_id = rmts.movgest_stato_id ) "
			+ "    JOIN siac_r_ordinativo_ts_movgest_ts rotmt ON ( rotmt.movgest_ts_id = tmt.movgest_ts_id	and rotmt.data_cancellazione is null ) "
			+ "    JOIN siac_t_ordinativo_ts tot ON ( tot.ord_ts_id = rotmt.ord_ts_id ) "
			+ "    JOIN siac_t_ordinativo tor ON ( tor.ord_id = tot.ord_id ) "
			+ "    JOIN siac_r_ordinativo_stato ros ON ( ros.ord_id = tor.ord_id	and ros.data_cancellazione is null	and ros.validita_fine is null ) "
			+ "    JOIN siac_d_ordinativo_stato dos ON ( dos.ord_stato_id = ros.ord_stato_id ) "
			+ "    JOIN siac_t_ordinativo_ts_det totd ON ( totd.ord_ts_id = tot.ord_ts_id ) "
			+ "    JOIN siac_d_ordinativo_ts_det_tipo dotdt ON ( dotdt.ord_ts_det_tipo_id = totd.ord_ts_det_tipo_id ) "
			+ "    WHERE tbe_new.elem_id = :elemId "
			+ "    AND dbet.elem_tipo_code = 'CAP-EG' "
			+ "    AND CAST(tp.anno AS INTEGER) >= :annoMin "
			+ "    AND CAST(tp.anno AS INTEGER) <= :annoMax "
			+ "    AND dbes.elem_stato_code = 'VA' "
			+ "    AND tm.movgest_anno = CAST(tp.anno AS INTEGER) "
			+ "    AND dms.movgest_stato_code <> 'A' "
			+ "    AND tor.ord_anno = CAST(tp.anno AS INTEGER) "
			+ "    AND dos.ord_stato_code <> 'A' "
			+ "    AND dotdt.ord_ts_det_tipo_code = 'A' "
			+ " ) "
			+ " SELECT tp.anno, COALESCE(SUM(tmp.ord_ts_det_importo), 0) "
			+ " FROM siac_t_bil_elem tbe_new "
			+ " JOIN siac_t_bil_elem tbe ON ( tbe_new.elem_code = tbe.elem_code AND tbe_new.elem_code2 = tbe.elem_code2 AND tbe_new.elem_code3 = tbe.elem_code3 AND tbe_new.ente_proprietario_id = tbe.ente_proprietario_id AND tbe.data_cancellazione is null ) "
			+ " JOIN siac_t_bil tb ON ( tb.bil_id = tbe.bil_id ) "
			+ " JOIN siac_t_periodo tp ON ( tp.periodo_id = tb.periodo_id ) "
			+ " JOIN siac_d_bil_elem_tipo dbet ON ( dbet.elem_tipo_id = tbe.elem_tipo_id ) "
			+ " JOIN siac_r_bil_elem_stato rbes ON ( rbes.elem_id = tbe.elem_id	and rbes.data_cancellazione is null ) "
			+ " JOIN siac_d_bil_elem_stato dbes ON ( dbes.elem_stato_id = rbes.elem_stato_id ) "
			+ " LEFT OUTER JOIN tmp ON (tmp.anno = tp.anno) "
			+ " WHERE tbe_new.elem_id = :elemId "
			+ " AND dbet.elem_tipo_code = 'CAP-EG' "
			+ " AND CAST(tp.anno AS INTEGER) >= :annoMin "
			+ " AND CAST(tp.anno AS INTEGER) <= :annoMax "
			+ " AND dbes.elem_stato_code = 'VA' "
			+ " GROUP BY tp.anno",
		nativeQuery = true)
	List<Object[]> findImportoIncassatoPerAnno(@Param("elemId") Integer elemId, @Param("annoMin") Integer annoMin, @Param("annoMax") Integer annoMax);
	
	@Query(value = " SELECT tbeOld.siacTBil.siacTPeriodo.anno, SUM(tbed.elemDetImporto) "
			+ " FROM SiacTBilElem tbe, "
			+ "   SiacTBilElem tbeOld "
			+ " INNER JOIN tbeOld.siacRBilElemStatos rbes "
			+ " INNER JOIN tbeOld.siacTBilElemDets tbed "
			+ " WHERE tbe.elemId = :elemId "
			// tbeOld
			+ " AND tbe.elemCode = tbeOld.elemCode "
			+ " AND tbe.elemCode2 = tbeOld.elemCode2 "
			+ " AND tbe.elemCode3 = tbeOld.elemCode3 "
			+ " AND tbe.siacTEnteProprietario = tbeOld.siacTEnteProprietario "
			+ " AND tbeOld.siacDBilElemTipo.elemTipoCode = 'CAP-EG' "
			+ " AND CAST(tbeOld.siacTBil.siacTPeriodo.anno AS int) >= :annoMin "
			+ " AND CAST(tbeOld.siacTBil.siacTPeriodo.anno AS int) <= :annoMax "
			+ " AND tbeOld.dataCancellazione IS NULL "
			// rbes
			+ " AND rbes.dataCancellazione IS NULL "
			+ " AND rbes.siacDBilElemStato.elemStatoCode = 'VA' "
			// tbed
			+ " AND tbed.dataCancellazione IS NULL "
			+ " AND tbed.siacDBilElemDetTipo.elemDetTipoCode = 'SRI' "
			+ " AND tbed.siacTPeriodo = tbeOld.siacTBil.siacTPeriodo "
			+ " GROUP BY tbeOld.siacTBil.siacTPeriodo.anno "
	)
	List<Object[]> findImportoResiduoInizialeByCapitoloPerAnno(@Param("elemId") Integer elemId, @Param("annoMin") Integer annoMin, @Param("annoMax") Integer annoMax);
	
	@Query(value =" SELECT periodo.anno, COALESCE(SUM(det.movgestTsDetImporto), 0) " +
		" FROM SiacTMovgestT tmt, SiacTMovgestTsDet det, SiacRMovgestBilElem rcap, SiacTBilElem cap, SiacTPeriodo periodo " + 
		" WHERE det.siacTMovgestT = tmt " +
		" AND rcap.siacTMovgest = tmt.siacTMovgest " +
		" AND rcap.siacTBilElem = cap " +
		" AND cap.elemId IN ( :elemIds ) " +
		" AND cap.siacTBil.siacTPeriodo = periodo " +
		//" AND CAST(periodo.anno AS int) >= :annoMin " +
		//" AND CAST(periodo.anno AS int) <= :annoMax " +
		" AND det.siacDMovgestTsDetTipo.movgestTsDetTipoCode = 'I' " + //--:movgestTsDetTipoCode +
		" AND tmt.siacDMovgestTsTipo.movgestTsTipoCode = 'T' " +
		" AND tmt.siacTMovgest.siacDMovgestTipo.movgestTipoCode= :movgestTipocode " +
		" AND tmt.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
		" AND CAST (tmt.siacTMovgest.movgestAnno AS int) < CAST(periodo.anno AS int) " +
		" AND tmt.dataCancellazione IS NULL " +
		" AND det.dataCancellazione IS NULL " +
		" AND rcap.dataCancellazione IS NULL " +
		" AND cap.dataCancellazione IS NULL "  +
		" AND EXISTS ( "+
		" 	FROM SiacRMovgestTsStato rst  " +
		" 	WHERE rst.siacTMovgestT = tmt " +
		" 	AND rst.siacDMovgestStato.movgestStatoCode in ( :movgestStatoCodes ) " +
		" 	AND rst.dataCancellazione IS NULL "+
		" ) " +
		" GROUP BY periodo.anno "
		)

	List<Object[]> findImportoResiduoInizialeByMovimentoPerAnno(@Param("elemIds") List<Integer> elemIds, @Param("movgestTipocode") String movgestTipocode, @Param("movgestStatoCodes") List<String> movgestStatoCodes,@Param("enteProprietarioId") Integer enteProprietarioId);
	
	@Query(value = " WITH tmp AS ( "
			+ "    SELECT tp.anno, totd.ord_ts_det_importo "
			+ "    FROM siac_t_bil_elem tbe_new "
			+ "    JOIN siac_t_bil_elem tbe ON ( tbe_new.elem_code = tbe.elem_code AND tbe_new.elem_code2 = tbe.elem_code2 AND tbe_new.elem_code3 = tbe.elem_code3 AND tbe_new.ente_proprietario_id = tbe.ente_proprietario_id AND tbe.data_cancellazione IS NULL ) "
			+ "    JOIN siac_t_bil tb ON ( tb.bil_id = tbe.bil_id ) "
			+ "    JOIN siac_t_periodo tp ON ( tp.periodo_id = tb.periodo_id ) "
			+ "    JOIN siac_d_bil_elem_tipo dbet ON ( dbet.elem_tipo_id = tbe.elem_tipo_id ) "
			+ "    JOIN siac_r_bil_elem_stato rbes ON ( rbes.elem_id = tbe.elem_id AND rbes.data_cancellazione IS NULL ) "
			+ "    JOIN siac_d_bil_elem_stato dbes ON ( dbes.elem_stato_id = rbes.elem_stato_id ) "
			+ "    JOIN siac_r_movgest_bil_elem rmbe ON ( rmbe.elem_id = tbe.elem_id AND rmbe.data_cancellazione IS NULL ) "
			+ "    JOIN siac_t_movgest tm ON ( tm.movgest_id = rmbe.movgest_id ) "
			+ "    JOIN siac_t_movgest_ts tmt ON ( tmt.movgest_id = tm.movgest_id ) "
			+ "    JOIN siac_r_movgest_ts_stato rmts ON ( rmts.movgest_ts_id = tmt.movgest_ts_id AND rmts.data_cancellazione IS NULL ) "
			+ "    JOIN siac_d_movgest_stato dms ON ( dms.movgest_stato_id = rmts.movgest_stato_id ) "
			+ "    JOIN siac_r_ordinativo_ts_movgest_ts rotmt ON ( rotmt.movgest_ts_id = tmt.movgest_ts_id AND rotmt.data_cancellazione IS NULL ) "
			+ "    JOIN siac_t_ordinativo_ts tot ON ( tot.ord_ts_id = rotmt.ord_ts_id ) "
			+ "    JOIN siac_t_ordinativo tor ON ( tor.ord_id = tot.ord_id ) "
			+ "    JOIN siac_r_ordinativo_stato ros ON ( ros.ord_id = tor.ord_id AND ros.data_cancellazione IS NULL AND ros.validita_fine IS NULL ) "
			+ "    JOIN siac_d_ordinativo_stato dos ON ( dos.ord_stato_id = ros.ord_stato_id ) "
			+ "    JOIN siac_t_ordinativo_ts_det totd ON ( totd.ord_ts_id = tot.ord_ts_id ) "
			+ "    JOIN siac_d_ordinativo_ts_det_tipo dotdt ON ( dotdt.ord_ts_det_tipo_id = totd.ord_ts_det_tipo_id ) "
			+ "    WHERE tbe_new.elem_id = :elemId "
			+ "    AND dbet.elem_tipo_code = 'CAP-EG' "
			+ "    AND CAST(tp.anno AS int) >= :annoMin "
			+ "    AND CAST(tp.anno AS int) <= :annoMax "
			+ "    AND dbes.elem_stato_code = 'VA' "
			+ "    AND tm.movgest_anno < CAST(tp.anno AS INTEGER) "
			+ "    AND dms.movgest_stato_code <> 'A' "
			+ "    AND tor.ord_anno = CAST(tp.anno AS INTEGER) "
			+ "    AND dos.ord_stato_code <> 'A' "
			+ "    AND dotdt.ord_ts_det_tipo_code = 'A' "
			+ " ) "
			+ " SELECT tp.anno, COALESCE(SUM(tmp.ord_ts_det_importo), 0) "
			+ " FROM siac_t_bil_elem tbe_new "
			+ " JOIN siac_t_bil_elem tbe ON ( tbe_new.elem_code = tbe.elem_code AND tbe_new.elem_code2 = tbe.elem_code2 AND tbe_new.elem_code3 = tbe.elem_code3 AND tbe_new.ente_proprietario_id = tbe.ente_proprietario_id AND tbe.data_cancellazione IS NULL ) "
			+ " JOIN siac_t_bil tb ON ( tb.bil_id = tbe.bil_id ) "
			+ " JOIN siac_t_periodo tp ON ( tp.periodo_id = tb.periodo_id ) "
			+ " JOIN siac_d_bil_elem_tipo dbet ON ( dbet.elem_tipo_id = tbe.elem_tipo_id ) "
			+ " JOIN siac_r_bil_elem_stato rbes ON ( rbes.elem_id = tbe.elem_id AND rbes.data_cancellazione IS NULL ) "
			+ " JOIN siac_d_bil_elem_stato dbes ON ( dbes.elem_stato_id = rbes.elem_stato_id ) "
			+ " LEFT OUTER JOIN tmp ON ( tmp.anno = tp.anno ) "
			+ " WHERE tbe_new.elem_id = :elemId "
			+ " AND dbet.elem_tipo_code = 'CAP-EG' "
			+ " AND CAST(tp.anno AS int) >= :annoMin "
			+ " AND CAST(tp.anno AS int) <= :annoMax "
			+ " AND dbes.elem_stato_code = 'VA' "
			+ " GROUP BY tp.anno ",
		nativeQuery = true
	)
	List<Object[]> findImportoIncassatoResiduoPerAnno(@Param("elemId") Integer elemId, @Param("annoMin") Integer annoMin, @Param("annoMax") Integer annoMax);
	
	// TODO: nel caso le JOIN diventino OUTER modificare la query in nativa sulla falsa riga di quelle sopra - cambiare
	// task-140
	/* @Query(value = " SELECT tbeOld.siacTBil.siacTPeriodo.anno, SUM(totd.ordTsDetImporto) "
			+ " FROM SiacTBilElem tbe, "
			+ "   SiacTBilElem tbeOld "
			+ " INNER JOIN tbeOld.siacRBilElemStatos rbes "
			+ " INNER JOIN tbeOld.siacRMovgestBilElems rmbe "
			+ " INNER JOIN rmbe.siacTMovgest.siacTMovgestTs tmt "
			+ " INNER JOIN tmt.siacRMovgestTsStatos rmts "
			+ " INNER JOIN tmt.siacROrdinativoTsMovgestTs rotmt "
			+ " INNER JOIN rotmt.siacTOrdinativoT.siacTOrdinativo.siacROrdinativoStatos ros "
			+ " INNER JOIN rotmt.siacTOrdinativoT.siacTOrdinativoTsDets totd "
			+ " WHERE tbe.elemId = :elemId "
			// tbeOld
			+ " AND tbe.elemCode = tbeOld.elemCode "
			+ " AND tbe.elemCode2 = tbeOld.elemCode2 "
			+ " AND tbe.elemCode3 = tbeOld.elemCode3 "
			+ " AND tbe.siacTEnteProprietario = tbeOld.siacTEnteProprietario "
			+ " AND tbeOld.siacDBilElemTipo.elemTipoCode = 'CAP-EG' "
			+ " AND CAST(tbeOld.siacTBil.siacTPeriodo.anno AS int) >= :annoMin "
			+ " AND CAST(tbeOld.siacTBil.siacTPeriodo.anno AS int) <= :annoMax "
			+ " AND tbeOld.dataCancellazione IS NULL "
			// rbes
			+ " AND rbes.dataCancellazione IS NULL "
			+ " AND rbes.siacDBilElemStato.elemStatoCode = 'VA' "
			// rmbe
			+ " AND rmbe.dataCancellazione IS NULL "
			+ " AND rmbe.siacTMovgest.movgestAnno = CAST(tbeOld.siacTBil.siacTPeriodo.anno AS int) "
			// srmts
			+ " AND rmts.dataCancellazione IS NULL "
			+ " AND rmts.siacDMovgestStato.movgestStatoCode <> 'A' "
			// rotmt
			+ " AND rotmt.dataCancellazione IS NULL "
			+ " AND rotmt.siacTOrdinativoT.siacTOrdinativo.ordAnno = CAST(tbeOld.siacTBil.siacTPeriodo.anno AS int) "
			// ros
			+ " AND ros.dataCancellazione IS NULL "
			+ " AND ros.dataFineValidita IS NULL "
			+ " AND ros.siacDOrdinativoStato.ordinativoStatoCode <> 'A' "
			// totd
			+ " AND totd.siacDOrdinativoTsDetTipo.ordTsDetTipoCode = 'A' "
			+ " GROUP BY tbeOld.siacTBil.siacTPeriodo.anno "
	*/
	@Query(value = " select "
			+ " 	siac_t_periodo.anno as anno, "
			+ " 	sum(siac_t_ordinativo_ts_det.ord_ts_det_importo) as importo "
			+ " from siac_t_bil_elem "
			+ " inner join siac_t_bil_elem siac_t_bil_elem_other on siac_t_bil_elem.elem_code = siac_t_bil_elem_other.elem_code and siac_t_bil_elem.elem_code2 = siac_t_bil_elem_other.elem_code2 and siac_t_bil_elem.elem_code3 = siac_t_bil_elem_other.elem_code3 and siac_t_bil_elem.ente_proprietario_id = siac_t_bil_elem_other.ente_proprietario_id "
			+ " inner join siac_t_bil on siac_t_bil_elem_other.bil_id = siac_t_bil.bil_id "
			+ " inner join siac_t_periodo on siac_t_bil.periodo_id = siac_t_periodo.periodo_id "
			+ " inner join siac_r_bil_elem_stato on siac_t_bil_elem_other.elem_id = siac_r_bil_elem_stato.elem_id "
			+ " inner join siac_d_bil_elem_stato on siac_r_bil_elem_stato.elem_stato_id = siac_d_bil_elem_stato.elem_stato_id "
			+ " inner join siac_r_movgest_bil_elem on siac_t_bil_elem_other.elem_id = siac_r_movgest_bil_elem.elem_id "
			+ " inner join siac_t_movgest on siac_r_movgest_bil_elem.movgest_id = siac_t_movgest.movgest_id " 
			+ " inner join siac_t_movgest_ts on siac_t_movgest.movgest_id = siac_t_movgest_ts.movgest_id "
			+ " inner join siac_r_movgest_ts_stato on siac_t_movgest_ts.movgest_ts_id = siac_r_movgest_ts_stato.movgest_ts_id "
			+ " inner join siac_r_ordinativo_ts_movgest_ts on siac_t_movgest_ts.movgest_ts_id = siac_r_ordinativo_ts_movgest_ts.movgest_ts_id "
			+ " inner join siac_t_ordinativo_ts on siac_r_ordinativo_ts_movgest_ts.ord_ts_id = siac_t_ordinativo_ts.ord_ts_id "
			+ " inner join siac_t_ordinativo on siac_t_ordinativo_ts.ord_id = siac_t_ordinativo.ord_id "
			+ " inner join siac_r_ordinativo_stato on siac_t_ordinativo.ord_id = siac_r_ordinativo_stato.ord_id "
			+ " inner join siac_t_ordinativo_ts siac_t_ordinativo_ts_other on siac_r_ordinativo_ts_movgest_ts.ord_ts_id = siac_t_ordinativo_ts_other.ord_ts_id "
			+ " inner join siac_t_ordinativo_ts_det on siac_t_ordinativo_ts_other.ord_ts_id = siac_t_ordinativo_ts_det.ord_ts_id "
			+ " inner join siac_d_bil_elem_tipo on siac_t_bil_elem_other.elem_tipo_id = siac_d_bil_elem_tipo.elem_tipo_id "
			+ " inner join siac_d_movgest_stato on siac_r_movgest_ts_stato.movgest_stato_id = siac_d_movgest_stato.movgest_stato_id "
			+ " inner join siac_d_ordinativo_stato on siac_r_ordinativo_stato.ord_stato_id = siac_d_ordinativo_stato.ord_stato_id "
			+ " inner join siac_d_ordinativo_ts_det_tipo on siac_t_ordinativo_ts_det.ord_ts_det_tipo_id = siac_d_ordinativo_ts_det_tipo.ord_ts_det_tipo_id "
			+ " where 1=1 "
			+ " and siac_t_bil_elem.elem_id = :elemId "
			+ " and siac_d_bil_elem_tipo.elem_tipo_code = 'CAP-EG' "
			+ " and cast(siac_t_periodo.anno as int)>=  :annoMin "
			+ " and cast(siac_t_periodo.anno as int)<=  :annoMax "
			+ " and siac_t_bil_elem_other.data_cancellazione is null "
			+ " and siac_r_bil_elem_stato.data_cancellazione is null "
			+ " and siac_d_bil_elem_stato.elem_stato_code = 'VA' "
			+ " and siac_r_movgest_bil_elem.data_cancellazione is null "
			+ " and siac_t_movgest.movgest_anno = cast(siac_t_periodo.anno as int4) "
			+ " and siac_r_movgest_ts_stato.data_cancellazione is null "
			+ " and siac_d_movgest_stato.movgest_stato_code <> 'A' "
			+ " and siac_r_ordinativo_ts_movgest_ts.data_cancellazione is null "
			+ " and siac_t_ordinativo.ord_anno = cast(siac_t_periodo.anno as int) "
			+ " and siac_r_ordinativo_stato.data_cancellazione is null "
			+ " and siac_r_ordinativo_stato.validita_fine is null "
			+ " and siac_d_ordinativo_stato.ord_stato_code <> 'A' "
			+ " and siac_d_ordinativo_ts_det_tipo.ord_ts_det_tipo_code = 'A' "
			+ " group by siac_t_periodo.anno ",
			nativeQuery = true
	)
	List<Object[]> findImportoIncassatoCompetenzaPerAnno(@Param("elemId") Integer elemId, @Param("annoMin") Integer annoMin, @Param("annoMax") Integer annoMax);
	
	@Query(" FROM SiacTBilElem tbe "
			+ " WHERE tbe.siacTBil.bilId = :bilId "
			+ " AND tbe.siacDBilElemTipo.elemTipoCode = :elemTipoCode "
			+ " AND EXISTS ( "
			+ "   FROM tbe.siacRBilElemAttrs rbea "
			+ "   WHERE rbea.dataCancellazione IS NULL "
			+ "   AND rbea.boolean_ = 'S' "
			+ "   AND rbea.siacTAttr.attrCode = :attrCode "
			+ " ) "
			+ " AND NOT EXISTS ( "
			+ "   FROM tbe.siacTAccFondiDubbiaEsigs tafde "
			+ "   WHERE tafde.dataCancellazione IS NULL "
			// Aggiungo un controllo per ritornare tutti i capitoli di anagrafica non presenti sulla versione in lavorazione
			+ "	  AND EXISTS ("
			+ "		FROM tafde.siacTAccFondiDubbiaEsigBil stafdeb "
			+ "		JOIN stafdeb.siacDAccFondiDubbiaEsigTipo sdafdet "
			+ "		WHERE stafdeb.dataCancellazione IS NULL "
			+ "		AND stafdeb.afdeBilVersione = :versione "
			+ "		AND sdafdet.afdeTipoCode = :tipo "
			+ "	  ) "
			+ " ) ")
	List<SiacTBilElem> findCollegabiliFCDE(@Param("bilId") Integer bilId, 
			@Param("elemTipoCode") String elemTipoCode, 
			@Param("versione") Integer versione, 
			@Param("tipo") String tipo,
			@Param("attrCode") String attrCode);
	
	@Query(value = " SELECT tbe.* "
			+ " FROM siac_t_bil_elem tbe "
			+ " JOIN siac_r_bil_elem_class srbec ON tbe.elem_id = srbec.elem_id "
			+ " JOIN siac_r_class_fam_tree srcft ON srcft.classif_id = srbec.classif_id "
			+ " JOIN siac_t_class_fam_tree stcft ON srcft.classif_fam_tree_id = stcft.classif_fam_tree_id "
			+ " JOIN siac_d_class_fam sdcf ON sdcf.classif_fam_id = stcft.classif_fam_id "
			+ " JOIN siac_t_bil stb ON tbe.bil_id = stb.bil_id "
			+ " JOIN siac_d_bil_elem_tipo sdbet ON tbe.elem_tipo_id = sdbet.elem_tipo_id "
			+ " JOIN siac_t_ente_proprietario step ON tbe.ente_proprietario_id = step.ente_proprietario_id "
			+ " JOIN siac_t_class stc ON stc.classif_id = srbec.classif_id " // stc => CATEGORIA
			+ " JOIN siac_d_class_tipo sdct ON stc.classif_tipo_id = sdct.classif_tipo_id "
			+ " JOIN siac_t_class stc_tipologia ON stc_tipologia.classif_id = srcft.classif_id_padre " // stc_tipologia => TIPOLOGIA
			+ " JOIN siac_r_class_fam_tree srcft_tipologia ON stc_tipologia.classif_id = srcft_tipologia.classif_id "
			+ " JOIN siac_t_class stc_titolo ON stc_titolo.classif_id = srcft_tipologia.classif_id_padre " // stc_titolo => TITOLO
			+ " JOIN siac_r_class_fam_tree srcft_titolo ON stc_titolo.classif_id = srcft_titolo.classif_id "
			+ " WHERE stb.bil_id = :bilId "
			+ " AND tbe.data_cancellazione IS NULL "
			+ " AND sdbet.elem_tipo_code = :elemTipoCode "
			+ " AND sdcf.classif_fam_code = '00003' "
			+ " AND srcft.livello = 3 "
			+ " AND srbec.data_cancellazione IS NULL "
			+ " AND srcft.data_cancellazione IS NULL "
			+ " AND EXISTS ( "
			+ " 	SELECT 1 "
			+ " 	FROM siac_r_bil_elem_attr rbea "
			+ " 	JOIN siac_t_attr sta ON sta.attr_id = rbea.attr_id "
			+ " 	WHERE rbea.data_cancellazione IS NULL "
			+ " 	AND rbea.elem_id = tbe.elem_id "
			+ " 	AND rbea.\"boolean\" = 'S' "
			+ " 	AND sta.attr_code = :attrCode "
			+ " ) "
			//SIAC-8397 per FCDE sono amessi i titoli dall'1 al 5
			+ " AND stc_titolo.classif_code <= '5' "
			+ " AND NOT EXISTS ( "
			+ " 	SELECT 1"
			+ " 	FROM siac_t_acc_fondi_dubbia_esig tafde "
			+ " 	WHERE tafde.data_cancellazione IS NULL "
			+ " 	AND tafde.elem_id = tbe.elem_id "
			// 		Aggiungo un controllo per ritornare tutti i capitoli di anagrafica non presenti sulla versione in lavorazione
			+ " 	AND EXISTS ( "
			+ " 		SELECT 1 "
			+ " 		FROM siac_t_acc_fondi_dubbia_esig_bil stafdeb "
			+ " 		JOIN siac_d_acc_fondi_dubbia_esig_tipo sdafdet ON stafdeb.afde_tipo_id = sdafdet.afde_tipo_id "
			+ " 		WHERE stafdeb.data_cancellazione IS NULL "
			+ " 		AND tafde.afde_bil_id = stafdeb.afde_bil_id "
			+ " 		AND stafdeb.afde_bil_versione = :versione "
			+ " 		AND sdafdet.afde_tipo_code = :tipo "
			+ " 	) "
			+ " ) "
			+ " GROUP BY tbe.elem_id, tbe.elem_code, tbe.elem_code2, tbe.elem_code3, stc.classif_code "
			+ " ORDER BY stc.classif_code ASC, CAST(tbe.elem_code AS INTEGER) ASC, CAST(tbe.elem_code2 AS INTEGER) ASC, CAST(tbe.elem_code3 AS INTEGER) ASC "
			, nativeQuery = true)
	List<SiacTBilElem> findCollegabiliFCDENative(@Param("bilId") Integer bilId, 
			@Param("elemTipoCode") String elemTipoCode, 
			@Param("versione") Integer versione, 
			@Param("tipo") String tipo,
			@Param("attrCode") String attrCode);

	//SIAC-8856
	@Query(value= " SELECT vinc.vincolo_code "
			+"FROM siac_t_vincolo vinc, "
			+"siac_r_vincolo_bil_elem re, "
			+"siac_r_vincolo_stato rs, "
			+"siac_d_vincolo_Stato stato, "
            +"siac_r_saldo_vincolo_sotto_conto rconto, "
			+"siac_d_contotesoreria conto "
            +"WHERE re.elem_id = :elem_id "
			+"AND vinc.vincolo_id = re.vincolo_id "
			+"AND rs.vincolo_id = vinc.vincolo_id "
			+"AND stato.vincolo_stato_id = rs.vincolo_stato_id "
			+"AND stato.vincolo_stato_code = 'V' "
			+"AND rconto.vincolo_id = vinc.vincolo_id "
			+"AND conto.contotes_id = rconto.contotes_id "
			+"AND conto.contotes_code = :conto_code "
			+"AND rs.data_cancellazione IS NULL "
			+"AND rs.validita_fine IS NULL "
			+"AND vinc.data_cancellazione IS NULL "
			+"AND vinc.validita_fine IS NULL "
			+"AND rconto.data_cancellazione IS NULL "
			+"AND rconto.validita_fine IS NULL "
			, nativeQuery = true)
	List<VincoliCapitoloUEGest> codiceVincolo(@Param("elem_id") Integer elem_id, @Param("conto_code") String conto_code);
	
	
	//SIAC-8705
	@Query( "SELECT cap.elemId " +
			" FROM SiacTBilElem cap , SiacTPeriodo periodo" + 
			" WHERE cap.dataCancellazione IS NULL " +
			" AND cap.siacTBil.siacTPeriodo = periodo " +
			" AND CAST(periodo.anno AS int) >= :annoMin "+
			" AND CAST(periodo.anno AS int) <= :annoMax " + 
			" AND EXISTS ( " +
			"     FROM SiacTBilElem capOrigine " + 
			"     WHERE capOrigine.dataCancellazione IS NULL " +
			"     AND capOrigine.elemCode = cap.elemCode " +
			"     AND capOrigine.elemCode2 = cap.elemCode2 " +
			"     AND capOrigine.elemCode3 = cap.elemCode3 " +
			"     AND capOrigine.siacDBilElemTipo =  cap.siacDBilElemTipo " +
			"     AND capOrigine.elemId =  :elemId " +
			" )" 
			)
	List<Integer> findElemIdsNelQuinquennio(@Param("elemId") Integer elemId, @Param("annoMin") Integer annoMin, @Param("annoMax") Integer annoMax);
	
	@Query(value=" SELECT fnc_siac_acc_fondi_dubbia_esig_rendiconto_residuo_finale(:elemId)", 
			nativeQuery = true)
	BigDecimal getResiduoFinaleCapitolo(@Param("elemId") Integer elemId);
	
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
			)
		SiacTBilElem getSiacTBilElemByChiaveLogica();
	
	//task-86
	@Query(value= "SELECT COALESCE(MAX(CAST(stbe.elem_code AS integer)), 0) + 1"
					+" FROM siac_t_bil_elem stbe "
					+" WHERE stbe.ente_proprietario_id = :enteId "
					+" AND stbe.elem_code ~ '^[0-9]+$' "
					+" AND CAST(stbe.elem_code AS integer) < :limiteNumerazioneAutomaticaCapitolo "
			, nativeQuery = true)
	Integer getPropostaNumeroCapitolo(
				@Param("enteId") Integer enteId, 
				@Param("limiteNumerazioneAutomaticaCapitolo") Integer limiteNumerazioneAutomaticaCapitolo
	);
}
