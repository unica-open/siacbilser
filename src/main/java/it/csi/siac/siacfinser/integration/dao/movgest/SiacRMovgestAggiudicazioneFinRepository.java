/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.movgest;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacRMovgestAggiudicazioneFin;
import it.csi.siac.siacfinser.integration.entity.SiacTAttoAmmFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestFin;

/**
 * The Interface SiacRMovgestTsStoricoImpAccRepository.
 */
public interface SiacRMovgestAggiudicazioneFinRepository extends JpaRepository<SiacRMovgestAggiudicazioneFin, Integer> {
	
	@Query(" SELECT agg.siacTMovgestDa "
			+ " FROM SiacRMovgestAggiudicazioneFin agg "
			+ " WHERE agg.dataCancellazione IS NULL "
			+ " AND agg.siacTMovgestA.dataCancellazione IS NULL "
			+ " AND agg.siacTMovgestDa.dataCancellazione IS NULL"
			+ " AND agg.siacTModifica.dataCancellazione IS NULL "
			+ " AND agg.siacTMovgestA.movgestId = :movgestId " 
			)
	public List<SiacTMovgestFin> findSiacRMovgestDaByMovgestTsA(@Param("movgestId") Integer movgestId);

	
	
	@Query(" SELECT agg.siacTAttoAmm  "
			+ " FROM SiacRMovgestAggiudicazioneFin agg "
			+ " WHERE agg.dataCancellazione IS NULL "
			+ " AND agg.siacTMovgestA.dataCancellazione IS NULL "
			+ " AND agg.siacTMovgestDa.dataCancellazione IS NULL"
			+ " AND agg.siacTModifica.dataCancellazione IS NULL "
			+ " AND agg.siacTMovgestA.movgestId = :movgestId " 
			)
	public List<SiacTAttoAmmFin> findSiacSiacTAttoAmmByMovgestTsA(@Param("movgestId") Integer movgestId);


	@Query(" SELECT agg.siacTModifica.modId "
			+ " FROM SiacRMovgestAggiudicazioneFin agg "
			+ " WHERE agg.dataCancellazione IS NULL "
			+ " AND agg.siacTMovgestA.dataCancellazione IS NULL "
			+ " AND agg.siacTMovgestDa.dataCancellazione IS NULL"
			+ " AND agg.siacTMovgestDa.movgestId = :movgestId "
			+ " AND agg.siacTModifica.dataCancellazione IS NULL "
			+ " AND EXISTS ("
			+ "     FROM SiacRModificaStatoFin rstato "
			+ "     WHERE rstato.dataCancellazione IS NULL "
			+ "     AND agg.siacTModifica = rstato.siacTModifica "
			+ "     AND rstato.siacDModificaStato.modStatoCode <> 'A' "
			+ "  )"
			)
	public List<Integer> getUidsModificheAggiudicazioneValideCollegate(@Param("movgestId") Integer movgestId);


	@Query(" SELECT  agg.siacTMovgestA "
			+ " FROM SiacRMovgestAggiudicazioneFin agg "
			+ " WHERE agg.dataCancellazione IS NULL "
			+ " AND agg.siacTMovgestA.dataCancellazione IS NULL "
			+ " AND agg.siacTMovgestDa.dataCancellazione IS NULL"
			+ " AND agg.siacTModifica.modId = :modId "
			+ " AND agg.siacTModifica.dataCancellazione IS NULL "
			+ " AND NOT EXISTS ("
			+ "     FROM SiacRMovgestTsStatoFin rstato "
			+ "     WHERE rstato.dataCancellazione IS NULL "
			+ "     AND agg.siacTMovgestA = rstato.siacTMovgestT.siacTMovgest "
			+ "     AND rstato.siacDMovgestStato.movgestStatoCode = 'A' "
			+ "  )"
			+ " AND EXISTS ("
			+ "     FROM  SiacTMovgestTsDetFin tmtd "
			+ "     WHERE tmtd.dataCancellazione IS NULL "
			+ "     AND tmtd.siacDMovgestTsDetTipo.movgestTsDetTipoCode = 'A' "
			+ "     AND tmtd.movgestTsDetImporto > 0 "
			+ "     AND tmtd.siacTMovgestT.siacTMovgest = agg.siacTMovgestA "
			+ "  )"
			)
	public List<SiacTMovgestFin> findSiacTMovgestAValidiByModId(@Param("modId") Integer modId);
}