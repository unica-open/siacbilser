/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.soggetto;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacTModpagModFin;
import it.csi.siac.siacfinser.integration.entity.SiacTSoggettoModFin;

public interface SiacTSoggettoModRepository extends JpaRepository<SiacTSoggettoModFin, Integer> {
	
	String condizione = " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND  dataCancellazione IS NULL ) ";
	String condizione2 = " ( (rsogclas.dataInizioValidita < :dataInput)  AND (rsogclas.dataFineValidita IS NULL OR :dataInput < rsogclas.dataFineValidita) AND  rsogclas.dataCancellazione IS NULL ) ";
	String condizione3 = " ( (rformagiu.dataInizioValidita < :dataInput)  AND (rformagiu.dataFineValidita IS NULL OR :dataInput < rformagiu.dataFineValidita) AND  rformagiu.dataCancellazione IS NULL ) ";
	String condizione4 = " ( (rformagiu.dataInizioValidita < :dataInput)  AND (rformagiu.dataFineValidita IS NULL OR :dataInput < rformagiu.dataFineValidita) AND  rformagiu.dataCancellazione IS NULL ) ";
	String condizione5 = " ( (rsoggStato.dataInizioValidita < :dataInput)  AND (rsoggStato.dataFineValidita IS NULL OR :dataInput < rsoggStato.dataFineValidita) AND  rsoggStato.dataCancellazione IS NULL ) ";
	String condizione6 = " ( (sogg.dataInizioValidita < :dataInput)  AND (sogg.dataFineValidita IS NULL OR :dataInput < sogg.dataFineValidita) AND  sogg.dataCancellazione IS NULL ) ";
	String condizione7 = " ( (sogg.dataInizioValidita < :dataInput)  AND (sogg.dataFineValidita IS NULL OR :dataInput < sogg.dataFineValidita) AND  sogg.dataCancellazione IS NULL ) ";

	@Query("FROM SiacTSoggettoModFin WHERE siacTSoggetto.soggettoId = :idSoggetto AND "+condizione)
	public List<SiacTSoggettoModFin> findValidoBySoggettoId(@Param("idSoggetto") Integer idSoggetto,@Param("dataInput") Timestamp  dataInput);		

	@Query("FROM SiacTSoggettoModFin WHERE siacTSoggetto.soggettoId = :idSoggetto")
	public List<SiacTSoggettoModFin> findAllBySoggettoId(@Param("idSoggetto") Integer idSoggetto);

	@Query("FROM SiacTSoggettoModFin t WHERE t.sogModId = :idSoggettoMod")
	public List<SiacTSoggettoModFin> findAllBySoggettoModId(@Param("idSoggettoMod") Integer idSoggettoMod);

	static final String RICERCA_SOGGETTO_PK = "SELECT s " +
	                                          "FROM SiacTSoggettoModFin s " +
			                                  "WHERE s.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND " +
											  "      s.soggettoCode = :soggCode AND s.dataFineValidita is null AND (s.codiceFiscale IS NOT NULL OR s.partitaIva IS NOT NULL OR s.codiceFiscaleEstero is not null) ";
	
	@Query(RICERCA_SOGGETTO_PK)
	public SiacTSoggettoModFin ricercaSoggetto(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("soggCode") String soggCode);

	static final String RICERCA_SOGGETTO_MOD_PAG = "SELECT stm " +
	                                               "FROM SiacTSoggettoModFin s, " +
			                                       "     SiacTModpagModFin stm " +
	                                               "WHERE s.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND " + 
	                                               "      s.siacTSoggetto.soggettoId = :soggettoId AND " +
	                                               "      stm.siacTSoggetto.soggettoId = s.siacTSoggetto.soggettoId";

	@Query(RICERCA_SOGGETTO_MOD_PAG)	
	public List<SiacTModpagModFin> ricercaModPag(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("soggettoId") Integer soggettoId);

	static final String RICERCA_SEDI_SOGGETTO = "SELECT s1 " +
									            "FROM SiacTSoggettoModFin s1, " + 
									            "     SiacTSoggettoModFin s2, " +
									            "     SiacRSoggettoRelazModFin sr, " +
									            "     SiacDRelazTipoFin rt " +
									            "WHERE s1.siacTEnteProprietario.enteProprietarioId = s2.siacTEnteProprietario.enteProprietarioId AND " +
									            "      s1.siacTEnteProprietario.enteProprietarioId = sr.siacTEnteProprietario.enteProprietarioId AND " +
									            "      sr.siacTEnteProprietario.enteProprietarioId = rt.siacTEnteProprietario.enteProprietarioId AND " +
									            "      s1.siacTSoggetto.soggettoId = sr.siacTSoggetto2.soggettoId AND " +
									            "      s2.siacTSoggetto.soggettoId = sr.siacTSoggetto1.soggettoId AND " +									            
                                                "      sr.siacDRelazTipo.relazTipoId = rt.relazTipoId AND " +
                                                "      rt.relazTipoCode = 'SEDE_SECONDARIA' AND " +									            
									            "      s1.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND " +
									            "      s2.siacTSoggetto.soggettoId = :soggettoId";

	@Query(RICERCA_SEDI_SOGGETTO)
	public List<SiacTSoggettoModFin> ricercaSedi(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("soggettoId") Integer soggettoId);

	static final String RICERCA_SEDE_SOGGETTO_PK = "SELECT sog_2 " +
	                                               "FROM SiacTSoggettoModFin sog_1, " +
			                                       "     SiacTSoggettoModFin sog_2, " +
	                                               "     SiacRSoggettoRelazModFin sog_r, " +
			                                       "     SiacDRelazTipoFin rel_t " +
	                                               "WHERE sog_1.siacTEnteProprietario.enteProprietarioId = sog_2.siacTEnteProprietario.enteProprietarioId AND " +
	                                               "      sog_1.siacTEnteProprietario.enteProprietarioId = sog_r.siacTEnteProprietario.enteProprietarioId AND " +
	                                               "      sog_r.siacTEnteProprietario.enteProprietarioId = rel_t.siacTEnteProprietario.enteProprietarioId AND " +
	                                               "      sog_1.siacTSoggetto.soggettoId = sog_r.siacTSoggetto1.soggettoId AND " +
	                                               "      sog_2.siacTSoggetto.soggettoId = sog_r.siacTSoggetto2.soggettoId AND " +
	                                               "      sog_r.siacDRelazTipo.relazTipoId = rel_t.relazTipoId AND " +
	                                               "      rel_t.relazTipoCode = 'SEDE_SECONDARIA' AND " +
	                                               "      sog_1.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND " +
	                                               "      sog_2.siacTSoggetto.soggettoId = :sedeSecondariaId AND " +
	                                               "      sog_2.siacTSoggetto.soggettoCode = :soggettoCode";

	@Query(RICERCA_SEDE_SOGGETTO_PK)
	public SiacTSoggettoModFin ricercaSedeSecondariaPerChiave(@Param("enteProprietarioId") Integer enteProprietarioId,
			                                               @Param("soggettoCode") String soggettoCode,
			                                               @Param("sedeSecondariaId") Integer sedeSecondariaId);

	static final String RICERCA_SOGGETTI = "SELECT s " +
	                                       "FROM SiacTSoggettoModFin s JOIN s.siacTPersonaFisicaMods " +
                                           "WHERE s.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId";

    @Query(RICERCA_SOGGETTI)
	public List<SiacTSoggettoModFin> ricercaSoggetti(@Param("enteProprietarioId") Integer enteProprietarioId);

    /*
    //test: 
	@Query("Select sogg FROM " +
	"SiacTSoggettoModFin sogg "+
	"WHERE sogg.siacTPersonaFisica.siacTComune.comuneDesc = :descrizioneComune "+
			" and  sogg.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId")
	public List<SiacTSoggettoModFin> ricercaSoggettiByComune(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("descrizioneComune") String descrizioneComune);
	
	@Query("Select sogg FROM " +
			"SiacTSoggettoModFin sogg "+
			"WHERE sogg.siacTPersonaFisicaMods.sesso = :sesso "+
					" and sogg.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId")
	public List<SiacTSoggettoModFin> ricercaSoggettiBySesso(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("sesso") String sesso);	
	*/

	@Query("Select sogg FROM " +
	"SiacTSoggettoModFin sogg JOIN sogg.siacTPersonaFisicaMods pf "+
	"WHERE pf.siacTComune.comuneDesc = :descrizioneComune "+
			" and  sogg.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId")
	public List<SiacTSoggettoModFin> ricercaSoggettiByComune(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("descrizioneComune") String descrizioneComune);

	@Query("Select sogg FROM " +
			"SiacTSoggettoModFin sogg JOIN sogg.siacTPersonaFisicaMods pf "+
			"WHERE pf.sesso = :sesso "+
					" and sogg.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId")
	public List<SiacTSoggettoModFin> ricercaSoggettiBySesso(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("sesso") String sesso);

	@Query("Select sogg FROM " +
			" SiacTSoggettoModFin sogg, " +
			" SiacRSoggettoClasseModFin rsogclas " +
			" WHERE " +
			" rsogclas.siacDSoggettoClasse.soggettoClasseDesc = :classe "+
			" and "+condizione2 +
			" and rsogclas.siacTSoggetto.soggettoId = sogg.siacTSoggetto.soggettoId "+
			" and sogg.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId")
	public List<SiacTSoggettoModFin> ricercaSoggettiByClasse(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("classe") String classe,@Param("dataInput") Timestamp  dataInput);

	@Query("Select sogg FROM " +
			" SiacTSoggettoModFin sogg, " +
			" SiacRFormaGiuridicaFin rformagiu " +
			" WHERE " +
			" rformagiu.siacTFormaGiuridica.formaGiuridicaDesc = :descFormaGiuridica "+
			" and "+condizione3 +
			" and rformagiu.siacTSoggetto.soggettoId = sogg.siacTSoggetto.soggettoId "+
			" and sogg.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId")
	public List<SiacTSoggettoModFin> ricercaSoggettiByDescFormaGiuridica(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("descFormaGiuridica") String descFormaGiuridica,@Param("dataInput") Timestamp  dataInput);

	@Query("Select sogg FROM " +
			" SiacTSoggettoModFin sogg, " +
			" SiacRFormaGiuridicaFin rformagiu " +
			" WHERE " +
			" rformagiu.siacTFormaGiuridica.siacDFormaGiuridicaTipo.formaGiuridicaTipoId = :tipoFormaGiuridica "+
			" and "+condizione4 +
			" and rformagiu.siacTSoggetto.soggettoId = sogg.siacTSoggetto.soggettoId "+
			" and sogg.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId")
	public List<SiacTSoggettoModFin> ricercaSoggettiByTipoFormaGiuridica(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("tipoFormaGiuridica") Integer tipoFormaGiuridica,@Param("dataInput") Timestamp  dataInput);

	@Query("Select sogg FROM " +
			" SiacTSoggettoModFin sogg, " +
			" SiacRSoggettoStatoFin rsoggStato " +
			" WHERE " +
			" rsoggStato.siacDSoggettoStato.soggettoStatoId = :stato "+
			" and "+condizione5 +
			" and rsoggStato.siacTSoggetto.soggettoId = sogg.siacTSoggetto.soggettoId "+
			" and sogg.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId")
	public List<SiacTSoggettoModFin> ricercaSoggettiBySoggettoStato(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("stato") Integer stato,@Param("dataInput") Timestamp  dataInput);

    @Query("SELECT sogg.soggettoCode FROM SiacTSoggettoModFin sogg WHERE sogg.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
			"and sogg.siacDAmbito.ambitoId = :ambito")
    public List<String> findCodeByEnteAndAmbito(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("ambito") Integer ambito);

	@Query("SELECT count(*) FROM SiacTSoggettoModFin sogg WHERE sogg.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND sogg.siacDAmbito.ambitoId = :ambito")
	public Long countSoggettoModByEnteAndAmbito(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("ambito") Integer ambito);

	@Query("SELECT max(to_number(trim(both ' ' from sogg.soggettoCode),'999999999999999')) + 1 FROM SiacTSoggettoModFin sogg WHERE sogg.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND sogg.siacDAmbito.ambitoId = :ambito")
	public BigDecimal estraiMaxSoggettoModCodePiuUnoByEnteAndAmbito(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("ambito") Integer ambito);

	@Query("SELECT sogg FROM SiacTSoggettoModFin sogg WHERE sogg.siacTEnteProprietario.enteProprietarioId = :idEnte and sogg.codiceFiscale = :codiceFiscale and "+condizione6)
	public List<SiacTSoggettoModFin> findSoggettoByCodiceFiscale(@Param("codiceFiscale") String codiceFiscale, @Param("idEnte") Integer idEnte,@Param("dataInput") Timestamp  dataInput);

	@Query("SELECT sogg FROM SiacTSoggettoModFin sogg WHERE sogg.siacTEnteProprietario.enteProprietarioId = :idEnte and sogg.partitaIva = :partitaIva and "+condizione7)
	public List<SiacTSoggettoModFin> findSoggettoByPartitaIva(@Param("partitaIva") String partitaIva, @Param("idEnte") Integer idEnte,@Param("dataInput") Timestamp  dataInput);
}
