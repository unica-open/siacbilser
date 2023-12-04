/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.soggetto;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacDSoggettoClasse;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsSogclasseFin;
import it.csi.siac.siacfinser.integration.entity.SiacTClassFin;
import it.csi.siac.siacfinser.integration.entity.SiacTModpagFin;
import it.csi.siac.siacfinser.integration.entity.SiacTModpagModFin;
import it.csi.siac.siacfinser.integration.entity.SiacTSoggettoFin;

public interface SiacTSoggettoFinRepository extends JpaRepository<SiacTSoggettoFin, Integer> {
	
	String condizione = " ( (sogg.dataInizioValidita < :dataInput)  AND (sogg.dataFineValidita IS NULL OR :dataInput < sogg.dataFineValidita) AND sogg.dataCancellazione IS NULL ) ";
	String condizione2 = " ( (sogg.dataInizioValidita < :dataInput)  AND (sogg.dataFineValidita IS NULL OR :dataInput < sogg.dataFineValidita) AND sogg.dataCancellazione IS NULL ) ";	
	String condizione3 = " ( (stm.dataInizioValidita < :dataInput)  AND (stm.dataFineValidita IS NULL OR :dataInput < stm.dataFineValidita) AND stm.dataCancellazione IS NULL ) ";
	String condizione4 = " ( (sr.dataInizioValidita < :dataInput)  AND (sr.dataFineValidita IS NULL OR :dataInput < sr.dataFineValidita) AND sr.dataCancellazione IS NULL ) ";
	String condizione5 = " ( (sog_r.dataInizioValidita < :dataInput)  AND (sog_r.dataFineValidita IS NULL OR :dataInput < sog_r.dataFineValidita) AND sog_r.dataCancellazione IS NULL ) ";
	String condizione6 = " ( (srsr.dataInizioValidita < :dataInput)  AND (srsr.dataFineValidita IS NULL OR :dataInput < srsr.dataFineValidita) AND srsr.dataCancellazione IS NULL ) ";
	
	//condizione 3 per RICERCA_SOGGETTO_MOD_PAG -> condizione 7 per far vedere le mdp scadute
	String condizione7 = " ( (stm.dataInizioValidita < :dataInput)  AND stm.dataCancellazione IS NULL ) ";
	//condizione 4 per RICERCA_SOGGETTO_MOD_PAG -> condizione 8 per far vedere le mdp scadute
	String condizione8 = " ( (sr.dataInizioValidita < :dataInput)  AND sr.dataCancellazione IS NULL ) ";
	
	/**
	 * Non e' chiaro se funziona bene.
	 */
	/*
    static final String RICERCA_SOGGETTO_PK = "SELECT sogg " +
            "FROM SiacTSoggettoFin sogg " +
            "WHERE sogg.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND sogg.soggettoCode = :soggCode "+
            " AND sogg.soggettoId NOT IN (SELECT srsr.siacTSoggetto2.soggettoId FROM SiacRSoggettoRelazFin srsr WHERE srsr.dataFineValidita IS NULL "+
			" AND srsr.siacTEnteProprietario.enteProprietarioId = sogg.siacTEnteProprietario.enteProprietarioId "+
			" AND srsr.siacDRelazTipo.siacTEnteProprietario.enteProprietarioId = sogg.siacTEnteProprietario.enteProprietarioId "+
			" AND srsr.siacDRelazTipo.relazTipoCode = :tipoCodSedeSecondaria  AND srsr.siacDRelazTipo.dataFineValidita IS NULL) ";*/
     

	static final String RICERCA_SOGGETTO_MOD_PAG = "SELECT stm " +
	                                               "FROM SiacTSoggettoFin s, " +
			                                       "     SiacTModpagFin stm " +
	                                               "WHERE s.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND " + 
	                                               "      s.soggettoId = :soggettoId AND " +
	                                               "      stm.siacTSoggetto.soggettoId = s.soggettoId AND " + condizione7;
	
	static final String RICERCA_SOGGETTO_MOD_PAG_CESSIONI = "SELECT stm " + 
			"FROM SiacTSoggettoFin s, " +
			"     SiacTModpagFin stm, " +
			"	  SiacRSoggettoRelazFin sz, " +
			"	  SiacRSoggrelModpagFin sr	" +
			"WHERE s.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND " + 
			"      s.soggettoId = :soggettoId AND " +
			"      sz.siacTSoggetto1.soggettoId = s.soggettoId AND"+
			"	   sr.siacTModpag.modpagId = stm.modpagId	AND" +
			"	   sr.siacRSoggettoRelaz.soggettoRelazId = sz.soggettoRelazId AND " + condizione8;

	static final String RICERCA_MOD_PAG_CESSIONI_PK = "SELECT stm " + 
			"FROM SiacTSoggettoFin s, " +
			"     SiacTModpagFin stm, " +
			"	  SiacRSoggettoRelazFin sz, " +
			"	  SiacRSoggrelModpagFin sr	" +
			"WHERE sz.soggettoRelazId = :idSoggRelaz AND " +	
			"      sz.siacTSoggetto1.soggettoId = s.soggettoId AND "+	
			"	   sr.siacTModpag.modpagId = stm.modpagId AND " +
			"	   sr.siacRSoggettoRelaz.soggettoRelazId = sz.soggettoRelazId";
	
	static final String RICERCA_MOD_PAG_CESSIONI_MOD_PAG_PK = "SELECT stm " + 
			"FROM SiacTSoggettoFin s, " +
			"     SiacTModpagFin stm, " +
			"	  SiacRSoggettoRelazFin sz, " +
			"	  SiacRSoggrelModpagFin sr	" +
			"WHERE stm.modpagId = :idModPag AND " +	
			"      sz.siacTSoggetto1.soggettoId = s.soggettoId AND "+	
			"	   sr.siacTModpag.modpagId = stm.modpagId AND " +
			"	   sr.siacRSoggettoRelaz.soggettoRelazId = sz.soggettoRelazId";
	
	// cerca per codice mdp e soggetto
	static final String RICERCA_MOD_PAG_CESSIONI_CODICE_PK = "SELECT stm " + 
			"FROM SiacTSoggettoFin s, " +
			"     SiacTModpagFin stm, " +
		//	"	  SiacRSoggettoRelazFin sz, " +
			"	  SiacRSoggrelModpagFin sr, " +
			"	  SiacRModpagOrdineFin ord "  + // con la tabella ordine
			"WHERE s.soggettoCode = :soggCode AND " +	
		//	"      sz.siacTSoggetto1.soggettoId = s.soggettoId AND "+	
			"	   sr.siacTModpag.modpagId = stm.modpagId AND " +
		//	"	   sr.siacRSoggettoRelaz.soggettoRelazId = sz.soggettoRelazId AND "+
			"      ord.siacRSoggrelModpag.soggrelmpagId = sr.soggrelmpagId AND "+
			"      ord.ordine = :modPagCodice ";
	
	
	static final String RICERCA_MOD_PAG_MOD_CESSIONI_PK = "SELECT stm " + 
			"FROM SiacTSoggettoFin s, " +
			"     SiacTModpagFin stm, " +
			"	  SiacRSoggettoRelazFin sz, " +
			"	  SiacRSoggrelModpagFin sr	," +
			"	  SiacRSoggettoRelazModFin szmod ," +
			"	  SiacRSoggrelModpagModFin srmod	" +
			"WHERE sz.soggettoRelazId = :idSoggRelaz AND " +	
			"      sz.siacTSoggetto1.soggettoId = s.soggettoId AND "+	
			"	   sr.siacTModpag.modpagId = stm.modpagId AND " +
			"	   sr.siacRSoggettoRelaz.soggettoRelazId = sz.soggettoRelazId AND "
			+ "	   sz.soggettoRelazId = szmod.siacRSoggettoRelaz.soggettoRelazId AND "
			+ "	   srmod.siacRSoggettoRelazMod.soggettoRelazModId = szmod.soggettoRelazModId";
	
	static final String RICERCA_MOD_PAG_PK = "SELECT stm "
												+ "FROM SiacTSoggettoFin s , "
												+ "		SiacTModpagFin stm "
												+ "WHERE s.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND "
												+ "		 s.soggettoCode = :soggCode AND "
												+ "		 stm.siacTSoggetto.soggettoId = s.soggettoId AND "
												+ "		 stm.modpagId = :modPagId";
	// jira-1578 modPagCodice
	static final String RICERCA_MOD_PAG_CODICE_PK =   "SELECT stm "
													+ "FROM SiacTSoggettoFin s , "
													+ "		SiacTModpagFin stm, "
													+ "		SiacRModpagOrdineFin ord " // con la tabella ordine
													+ "WHERE s.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND "
													+ "		 s.soggettoCode = :soggCode AND "
													+ "		 stm.siacTSoggetto.soggettoId = s.soggettoId AND "
													+ "		 ord.siacTModpag.modpagId = stm.modpagId AND "
													+ "      ord.ordine = :modPagCodice";

	static final String RICERCA_MOD_PAG_MOD_PK = "SELECT mdpMod "
												+ "FROM SiacTSoggettoFin s, "
												+ "		SiacTModpagFin mdp ,  "
												+ "		SiacTModpagModFin mdpMod "
												+ "WHERE s.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND "
												+ "		 s.soggettoCode = :soggCode AND "
												+ "		 mdp.siacTSoggetto.soggettoId = s.soggettoId AND "
												+ "		 mdp.modpagId = :modPagId AND "
												+ "      mdpMod.siacTModpag.modpagId = mdp.modpagId AND "
												+ "		 mdpMod.siacTSoggetto.soggettoId = s.soggettoId";

	static final String RICERCA_SEDI_SOGGETTO = "SELECT s1 " +
									            "FROM SiacTSoggettoFin s1, " + 
									            "     SiacTSoggettoFin s2, " +
									            "     SiacRSoggettoRelazFin sr, " +
									            "     SiacDRelazTipoFin rt " +
									            "WHERE s1.siacTEnteProprietario.enteProprietarioId = s2.siacTEnteProprietario.enteProprietarioId AND " +
									            "      s1.siacTEnteProprietario.enteProprietarioId = sr.siacTEnteProprietario.enteProprietarioId AND " +
									            "      sr.siacTEnteProprietario.enteProprietarioId = rt.siacTEnteProprietario.enteProprietarioId AND " +
									            "      s1.soggettoId = sr.siacTSoggetto2.soggettoId AND " +
									            "      s2.soggettoId = sr.siacTSoggetto1.soggettoId AND " +									            
                                                "      sr.siacDRelazTipo.relazTipoId = rt.relazTipoId AND " +
                                                "      rt.relazTipoCode = :relazTipoCod AND " +									            
									            "      s1.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND " +
									            "      s2.soggettoId = :soggettoId " +
									            "ORDER BY s1.dataCreazione";

	static final String RICERCA_SEDE_SOGGETTO_PK = "SELECT sog_2 " +
	                                               "FROM SiacTSoggettoFin sog_1, " +
			                                       "     SiacTSoggettoFin sog_2, " +
	                                               "     SiacRSoggettoRelazFin sog_r, " +
			                                       "     SiacDRelazTipoFin rel_t " +
	                                               "WHERE sog_1.siacTEnteProprietario.enteProprietarioId = sog_2.siacTEnteProprietario.enteProprietarioId AND " +
	                                               "      sog_1.siacTEnteProprietario.enteProprietarioId = sog_r.siacTEnteProprietario.enteProprietarioId AND " +
	                                               "      sog_r.siacTEnteProprietario.enteProprietarioId = rel_t.siacTEnteProprietario.enteProprietarioId AND " +
	                                               "      sog_1.soggettoId = sog_r.siacTSoggetto1.soggettoId AND " +
	                                               "      sog_2.soggettoId = sog_r.siacTSoggetto2.soggettoId AND " +
	                                               "      sog_r.siacDRelazTipo.relazTipoId = rel_t.relazTipoId AND " +
	                                               "      rel_t.relazTipoCode = :relazTipoCod AND " +
	                                               "      sog_1.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND " +
	                                               "      sog_2.soggettoId = :sedeSecondariaId AND " +
	                                               "      sog_2.soggettoCode = :soggettoCode";

	static final String RICERCA_SOGGETTI = "SELECT s " +
	                                       "FROM SiacTSoggettoFin s JOIN s.siacTPersonaFisica " +
                                           "WHERE s.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId";
	
//	@Query(RICERCA_SOGGETTO_PK)
//	public SiacTSoggettoFin ricercaSoggetto(@Param("enteProprietarioId") Integer enteProprietarioId,
//			                             @Param("soggCode") String soggCode,
//			                             @Param("tipoCodSedeSecondaria") String tipoCodSedeSecondaria);
	
	
	@Query("SELECT sogg " +
            "FROM SiacTSoggettoFin sogg " +
            "WHERE sogg.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND sogg.soggettoCode = :soggCode AND "+ condizione +
            " AND sogg.siacDAmbito.ambitoCode=:codiceAmbito " +
            " AND sogg.soggettoId NOT IN (SELECT srsr.siacTSoggetto2.soggettoId FROM SiacRSoggettoRelazFin srsr WHERE " + condizione6 + 
			" AND srsr.siacTEnteProprietario.enteProprietarioId = sogg.siacTEnteProprietario.enteProprietarioId "+
			" AND srsr.siacDRelazTipo.siacTEnteProprietario.enteProprietarioId = sogg.siacTEnteProprietario.enteProprietarioId "+
			" AND srsr.siacDRelazTipo.relazTipoCode = :tipoCodSedeSecondaria ) ")
	public SiacTSoggettoFin ricercaSoggettoNoSeSede(@Param("codiceAmbito") String codiceAmbito,
												@Param("enteProprietarioId") Integer enteProprietarioId,
			                                     @Param("soggCode") String soggCode,
			                                     @Param("tipoCodSedeSecondaria") String tipoCodSedeSecondaria, 
			                                     @Param("dataInput") Timestamp  dataInput);
	
	
	
	@Query("SELECT sogg " +
            "FROM SiacTSoggettoFin sogg " +
            "WHERE sogg.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
            " and sogg.soggettoId IN " +
			" (SELECT sattr.siacTSoggetto.soggettoId FROM SiacRSoggettoAttrFin sattr " +
			" WHERE sattr.testo=:matricola AND sattr.siacTEnteProprietario.enteProprietarioId=:enteProprietarioId AND sattr.siacTAttr.attrId IN " +
			" (SELECT attr.attrId FROM SiacTAttr attr WHERE attr.attrCode='Matricola' AND attr.siacTEnteProprietario.enteProprietarioId=:enteProprietarioId  ) )" +
            " AND "+ condizione +
            " AND sogg.siacDAmbito.ambitoCode=:codiceAmbito " +
            " AND sogg.soggettoId NOT IN (SELECT srsr.siacTSoggetto2.soggettoId FROM SiacRSoggettoRelazFin srsr WHERE " + condizione6 + 
			" AND srsr.siacTEnteProprietario.enteProprietarioId = sogg.siacTEnteProprietario.enteProprietarioId "+
			" AND srsr.siacDRelazTipo.siacTEnteProprietario.enteProprietarioId = sogg.siacTEnteProprietario.enteProprietarioId "+
			" AND srsr.siacDRelazTipo.relazTipoCode = :tipoCodSedeSecondaria ) ")
	public SiacTSoggettoFin ricercaSoggettoNoSeSedeByMatricola(
			@Param("codiceAmbito") String codiceAmbito,
			@Param("enteProprietarioId") Integer enteProprietarioId,
			@Param("matricola") String matricola,
			@Param("tipoCodSedeSecondaria") String tipoCodSedeSecondaria, 
			@Param("dataInput") Timestamp  dataInput);
	
	
	
	
	@Query(RICERCA_SEDE_SOGGETTO_PK)
	public SiacTSoggettoFin ricercaSedeSecondariaPerChiave(@Param("enteProprietarioId") Integer enteProprietarioId,
			                                            @Param("soggettoCode") String soggettoCode,
			                                            @Param("sedeSecondariaId") Integer sedeSecondariaId,
			                                            @Param("relazTipoCod") String relazTipoCod);
	
	static final String CARICA_SEDE = "SELECT sog_2 " +
            "FROM SiacTSoggettoFin sog_1, " +
            "     SiacTSoggettoFin sog_2, " +
            "     SiacRSoggettoRelazFin sog_r, " +
            "     SiacDRelazTipoFin rel_t " +
            "WHERE sog_1.siacTEnteProprietario.enteProprietarioId = sog_2.siacTEnteProprietario.enteProprietarioId AND " +
            "      sog_1.siacTEnteProprietario.enteProprietarioId = sog_r.siacTEnteProprietario.enteProprietarioId AND " +
            "      sog_r.siacTEnteProprietario.enteProprietarioId = rel_t.siacTEnteProprietario.enteProprietarioId AND " +
            "      sog_1.soggettoId = sog_r.siacTSoggetto1.soggettoId AND " +
            "      sog_2.soggettoId = sog_r.siacTSoggetto2.soggettoId AND " +
            "      sog_r.siacDRelazTipo.relazTipoId = rel_t.relazTipoId AND " +
            "      rel_t.relazTipoCode = :relazTipoCod AND " +
            "      sog_1.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND " +
            "      sog_2.soggettoId = :sedeSecondariaId AND " + condizione5;

	@Query(CARICA_SEDE)
	public List<SiacTSoggettoFin> caricaSoloSeSede(@Param("enteProprietarioId") Integer enteProprietarioId, 
			                                    @Param("sedeSecondariaId") Integer sedeSecondariaId,
			                                    @Param("relazTipoCod") String relazTipoCod,
			                                    @Param("dataInput") Timestamp  dataInput);

	@Query(RICERCA_MOD_PAG_PK)
	public SiacTModpagFin ricercaModalitaPagamentoPerChiaveDef(@Param("enteProprietarioId") Integer enteProprietarioId,
            												@Param("soggCode") String soggettoCode,
            												@Param("modPagId") Integer modPagId);
	
	// ricerca mdp classica per codice della mdp
	@Query(RICERCA_MOD_PAG_CODICE_PK)
	public SiacTModpagFin ricercaModalitaPagamentoPerChiaveCodiceDef(@Param("enteProprietarioId") Integer enteProprietarioId,
            													  @Param("soggCode") String soggettoCode,
            												      @Param("modPagCodice") Integer mdpCode);
	
	@Query(RICERCA_MOD_PAG_MOD_PK)
	public SiacTModpagModFin ricercaModalitaPagamentoPerChiaveMod(@Param("enteProprietarioId") Integer enteProprietarioId,
            												   @Param("soggCode") String soggettoCode,
            												   @Param("modPagId") Integer modPagId);
	
	@Query(RICERCA_MOD_PAG_CESSIONI_PK)
	public SiacTModpagFin ricercaModalitaPagamentoCessionePerChiaveDef(@Param("idSoggRelaz") Integer idSoggRelaz);
	
	@Query(RICERCA_MOD_PAG_CESSIONI_MOD_PAG_PK)
	public SiacTModpagFin ricercaModalitaPagamentoCessionePerChiaveModPag(@Param("idModPag") Integer idModPag);
	
	
	// cerca mdp cessione per codice
	@Query(RICERCA_MOD_PAG_CESSIONI_CODICE_PK)
	public SiacTModpagFin ricercaModalitaPagamentoCessionePerChiaveCodiceDef(@Param("soggCode") String soggettoCode, 
																		  @Param("modPagCodice") Integer mdpCode);
	
	@Query(RICERCA_MOD_PAG_MOD_CESSIONI_PK)
	public SiacTModpagFin ricercaModalitaPagamentoCessionePerChiaveMod(@Param("idSoggRelaz") Integer idSoggRelaz);
	
	@Query(RICERCA_SOGGETTO_MOD_PAG)
	public List<SiacTModpagFin> ricercaModPag(@Param("enteProprietarioId") Integer enteProprietarioId,
			                               @Param("soggettoId") Integer soggettoId,
			                               @Param("dataInput") Timestamp  dataInput);
	
	@Query(RICERCA_SOGGETTO_MOD_PAG_CESSIONI)
	public List<SiacTModpagFin> ricercaModPagCessioni(@Param("enteProprietarioId") Integer enteProprietarioId,
			                                       @Param("soggettoId") Integer soggettoId,
			                                       @Param("dataInput") Timestamp  dataInput);
	
	@Query(RICERCA_SEDI_SOGGETTO)
	public List<SiacTSoggettoFin> ricercaSedi(@Param("enteProprietarioId") Integer enteProprietarioId,
			                               @Param("soggettoId") Integer soggettoId,
			                               @Param("relazTipoCod") String relazTipoCod);
	
	@Query(RICERCA_SOGGETTI)
	public List<SiacTSoggettoFin> ricercaSoggetti(@Param("enteProprietarioId") Integer enteProprietarioId);
	
	@Query("SELECT sogg.soggettoCode FROM SiacTSoggettoFin sogg WHERE sogg.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND sogg.siacDAmbito.ambitoId = :ambito")
	public List<String> findCodeByEnteAndAmbito(@Param("enteProprietarioId") Integer enteProprietarioId,
			                                    @Param("ambito") Integer ambito);
	
	@Query("SELECT sogg FROM SiacTSoggettoFin sogg WHERE sogg.siacTEnteProprietario.enteProprietarioId = :idEnte and sogg.codiceFiscale = :codiceFiscale and "+condizione)
	public List<SiacTSoggettoFin> findSoggettoByCodiceFiscale(@Param("codiceFiscale") String codiceFiscale,
			                                               @Param("idEnte") Integer idEnte,
			                                               @Param("dataInput") Timestamp  dataInput);
	
	@Query("SELECT sogg FROM SiacTSoggettoFin sogg WHERE sogg.siacTEnteProprietario.enteProprietarioId = :idEnte and sogg.partitaIva = :partitaIva and "+condizione2)
	public List<SiacTSoggettoFin> findSoggettoByPartitaIva(@Param("partitaIva") String partitaIva,
			                                            @Param("idEnte") Integer idEnte,
			                                            @Param("dataInput") Timestamp  dataInput);
	
//	@Query("SELECT sogg FROM SiacTSoggettoFin sogg WHERE sogg.siacTEnteProprietario.enteProprietarioId = :idEnte " +
//			" and sogg.siacDAmbito.ambitoId = :ambito and sogg.soggettoCode = :code AND "+
//			"(sogg.codiceFiscale IS NOT NULL OR sogg.partitaIva IS NOT NULL OR sogg.codiceFiscaleEstero is not null) AND "+condizione)
	@Query(" SELECT sogg FROM SiacTSoggettoFin sogg " + 
           " WHERE sogg.siacTEnteProprietario.enteProprietarioId = :idEnte AND " +
	       " sogg.siacDAmbito.ambitoId = :ambito AND " + 
           " sogg.soggettoCode = :code AND " + condizione +
           " AND sogg.soggettoId NOT IN (SELECT srsr.siacTSoggetto2.soggettoId FROM SiacRSoggettoRelazFin srsr WHERE " + condizione6 + 
		   " AND srsr.siacTEnteProprietario.enteProprietarioId = sogg.siacTEnteProprietario.enteProprietarioId " +
		   " AND srsr.siacDRelazTipo.siacTEnteProprietario.enteProprietarioId = sogg.siacTEnteProprietario.enteProprietarioId " +
		   " AND srsr.siacDRelazTipo.relazTipoCode = :tipoCodSedeSecondaria )")
	public List<SiacTSoggettoFin> findSoggettoByCodeAndAmbitoAndEnte(@Param("code") String code,
			                                                      @Param("idEnte") Integer idEnte,
			                                                      @Param("dataInput") Timestamp  dataInput,
			                                                      @Param("ambito") Integer ambito,
			                                                      @Param("tipoCodSedeSecondaria") String tipoCodSedeSecondaria);
	
	@Query("SELECT count(*) FROM SiacTSoggettoFin sogg WHERE sogg.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND sogg.siacDAmbito.ambitoId = :ambito")
	public Long countSoggettoByEnteAndAmbito(@Param("enteProprietarioId") Integer enteProprietarioId,
			                                 @Param("ambito") Integer ambito);
			
	@Query("SELECT max(to_number(trim(both ' ' from sogg.soggettoCode),'999999999999999')) + 1 FROM SiacTSoggettoFin sogg WHERE sogg.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND sogg.siacDAmbito.ambitoId = :ambito")
	public BigDecimal estraiMaxSoggettoCodePiuUnoByEnteAndAmbito(@Param("enteProprietarioId") Integer enteProprietarioId,
			                                                     @Param("ambito") Integer ambito);

	@Query(" SELECT s.soggettoCode FROM SiacTSoggettoFin s WHERE s.soggettoId=:soggettoId")
	public String ricercaCodiceSoggetto(@Param("soggettoId") Integer soggettoId);
	
	
	@Query("SELECT case when (count(sm) > 0)  then true else false end " +
			" from SiacRSubdocModpag sm WHERE sm.siacTModpag.modpagId=:modpagId "
			+ " AND EXISTS (SELECT ds FROM SiacRDocStato ds WHERE ds.siacTDoc=sm.siacTSubdoc.siacTDoc " +
			" AND ds.siacDDocStato.docStatoCode!='EM')")
	public boolean isModpagCollegataSubdocNonIncassati(@Param("modpagId") Integer modpagId);


	@Modifying()
	@Query("UPDATE SiacTSoggettoFin SET "
			+ " dataFineValiditaDurc=:dataFineValiditaDurc, "
			+ " tipoFonteDurc=:tipoFonteDurc, "
			+ " fonteDurc=:fonteDurc, "
			+ " fonteDurcAutomatica=:fonteDurcAutomatica, "
			+ " noteDurc=:noteDurc, "
			+ " dataModifica=CURRENT_TIMESTAMP, "
			+ " loginModifica=:loginModifica"
			+ " WHERE soggettoId=:soggettoId")
	public void aggiornaDatiDurcSoggetto(
			@Param("soggettoId") Integer idSoggetto, 
			@Param("dataFineValiditaDurc") Date dataFineValiditaDurc, 
			@Param("tipoFonteDurc") Character tipoFonteDurc,
			@Param("fonteDurc") SiacTClassFin fonteDurc, 
			@Param("fonteDurcAutomatica") String fonteDurcAutomatica, 
			@Param("noteDurc") String noteDurc,
			@Param("loginModifica") String loginModifica
	);
	
	
	//SIAC-7619
	@Query(" SELECT r "
			+ " FROM SiacDSoggettoClasse s "
			+ " JOIN s.siacRMovgestTsSogclasses r "
			+ " WHERE s.dataCancellazione is NULL "
			+ " AND r.dataCancellazione IS NULL "
			+ " AND r.siacTMovgestT.movgestTsId = :movgestTsId "
			+ " AND s.dataInizioValidita < CURRENT_TIMESTAMP "
			+ " AND (s.dataFineValidita is NULL or s.dataFineValidita > CURRENT_TIMESTAMP) ")
	List<SiacRMovgestTsSogclasseFin> findSiacRSoggettoClasseBySiacTMovgestTs(@Param("movgestTsId") Integer movgestTsId);
	
	
}