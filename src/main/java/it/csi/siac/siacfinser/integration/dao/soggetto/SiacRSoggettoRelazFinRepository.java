/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.soggetto;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoRelazFin;

public interface SiacRSoggettoRelazFinRepository extends JpaRepository<SiacRSoggettoRelazFin, Integer> {

	String condizione = " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND  dataCancellazione IS NULL ) ";
	String condizione_rel = " ( (rel.dataInizioValidita < :dataInput)  AND (rel.dataFineValidita IS NULL OR :dataInput < rel.dataFineValidita) AND  rel.dataCancellazione IS NULL ) ";

	//condizionre_rel -> condizione_rel_scad per visualizzare cessioni scadute
	String condizione_rel_scad = " ( rel.dataInizioValidita < :dataInput  AND  rel.dataCancellazione IS NULL ) ";
	
	static final String RICERCA_SOGG_RELAZ_PK = "SELECT rel " +
            "FROM SiacRSoggettoRelazFin rel , "
            + " SiacRSoggrelModpagFin rmod " +
            " WHERE rel.siacTSoggetto1.soggettoId = :idSoggettoDa "
            + "AND rel.siacTSoggetto2.soggettoId = :idSoggettoA  "
            + "AND rmod.siacRSoggettoRelaz.soggettoRelazId = rel.soggettoRelazId "
            + "AND rmod.siacTModpag.modpagId = :idModpag" + " AND " + condizione_rel_scad;	//prima condizione_rel
	
	static final String RICERCA_SOGG_REL_PK = "SELECT rel "
			+ "FROM SiacRSoggettoRelazFin rel , "
			+ "SiacRSoggrelModpagFin rmod "
			+ "WHERE rel.siacTSoggetto1.soggettoId = :idSoggettoDa "
			+ "AND rmod.siacRSoggettoRelaz.soggettoRelazId = rel.soggettoRelazId "
			+ "AND rmod.siacTModpag.modpagId = :idModpag" + " AND " + condizione_rel_scad;	//prima condizione_rel

	
	@Query("from SiacRSoggettoRelazFin where siacTSoggetto1.soggettoId = :idSoggetto AND "+condizione+" AND siacDRelazTipo.relazTipoCode = :tipoRelazione")
	List<SiacRSoggettoRelazFin> findBySoggettoETipo(@Param("idSoggetto") Integer idSoggetto, @Param("tipoRelazione") String tipoRelazione,@Param("dataInput") Timestamp  dataInput);
	
	@Query("FROM SiacRSoggettoRelazFin WHERE siacTSoggetto2.soggettoId = :idSoggettoA AND "+condizione+" AND siacDRelazTipo.relazTipoCode = :tipoRelazione AND siacTEnteProprietario.enteProprietarioId = :idEnte")
	List<SiacRSoggettoRelazFin> findRelazioniBySoggettoAETipo(@Param("idSoggettoA") Integer idSoggettoA, @Param("tipoRelazione") String tipoRelazione,@Param("dataInput") Timestamp  dataInput, @Param("idEnte") Integer idEnte);
	
	@Query("from SiacRSoggettoRelazFin where siacTSoggetto1.soggettoId = :idSoggettoDa AND siacTSoggetto2.soggettoId = :idSoggettoA AND dataFineValidita IS NULL AND siacDRelazTipo.relazTipoCode = :tipoRelazione AND siacTEnteProprietario.enteProprietarioId = :idEnte")
	SiacRSoggettoRelazFin findValidaBySoggettiETipo(@Param("idSoggettoDa") Integer idSoggettoDa, @Param("idSoggettoA") Integer idSoggettoA, @Param("tipoRelazione") String tipoRelazione, @Param("idEnte") Integer idEnte);

	@Query(RICERCA_SOGG_RELAZ_PK)
	SiacRSoggettoRelazFin findValidaBySoggettiEModpag(@Param("idSoggettoDa") Integer idSoggettoDa, @Param("idSoggettoA") Integer idSoggettoA, @Param("idModpag") Integer idModpag,@Param("dataInput") Timestamp  dataInput);
	
	@Query(RICERCA_SOGG_REL_PK)
	SiacRSoggettoRelazFin findValidaBySoggettoEModpag(@Param("idSoggettoDa") Integer idSoggettoDa, @Param("idModpag") Integer idModpag,@Param("dataInput") Timestamp  dataInput);
	
	@Query(RICERCA_SOGG_REL_PK)
	List<SiacRSoggettoRelazFin> findValidaBySoggettoEModpag_TEMP(@Param("idSoggettoDa") Integer idSoggettoDa, @Param("idModpag") Integer idModpag,@Param("dataInput") Timestamp  dataInput);
	
}
