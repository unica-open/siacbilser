/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.movgest;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacTModificaFin;


public interface SiacTModificaRepository extends JpaRepository<SiacTModificaFin, Integer> {
	
	String condizione = " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND dataCancellazione IS NULL ) ";

	@Query("FROM SiacTModificaFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND " + condizione )
	public List<SiacTModificaFin> findListaSiacTModifica(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("dataInput") Timestamp  dataInput);
	
	@Query(" SELECT COALESCE(tmtdm.movgestTsDetImporto, 0) "
			+ " FROM SiacTMovgestTsDetModFin tmtdm "
			+ " WHERE tmtdm.dataCancellazione IS NULL "
			+ " AND tmtdm.siacRModificaStato.dataCancellazione IS NULL "
			+ " AND tmtdm.siacRModificaStato.siacTModifica.modId = :modId "
			+ " AND tmtdm.siacDMovgestTsDetTipo.movgestTsDetTipoCode = :movgestTsDetTipoCode ")
	public BigDecimal findMovgestTsDetImportoModByModIdAndMovgestTsDetTipoCode(@Param("modId") Integer modId, @Param("movgestTsDetTipoCode") String movgestTsDetTipoCode);
	
	
	@Query(" SELECT tmtdm.siacDModificaTipo.modTipoCode "
			+ " FROM SiacTModificaFin tmtdm "
			+ " WHERE tmtdm.dataCancellazione IS NULL "
			+ " AND tmtdm.modId = :modId ")
	public String findMovgestTsDetImportoModByModIdAndMovgestTsDetTipoCode(@Param("modId") Integer modId);

	

	@Query(" SELECT rmtdm.siacDAttoAmmStato.attoammStatoCode "
			+ " FROM SiacRAttoAmmStatoFin rmtdm, SiacTModificaFin mod"
			+ " WHERE rmtdm.dataCancellazione IS NULL "
			+ " AND mod.dataCancellazione IS NULL "
			+ " AND rmtdm.siacTAttoAmm = mod.siacTAttoAmm "
			+ " AND mod.modId = :modId ")
	public String findProvvedimentoStatoCode(@Param("modId") Integer modId);
	
	@Query(" SELECT mod.modId"
			+ " FROM SiacTModificaFin mod "
			+ " WHERE mod.dataCancellazione IS NULL "
			+ " AND EXISTS ("
			+ "   FROM SiacTMovgestTsDetModFin tmtdm "
			+ "   WHERE tmtdm.dataCancellazione IS NULL "
			+ "   AND tmtdm.siacTMovgestT.dataCancellazione IS NULL "
			+ "   AND tmtdm.siacTMovgestT.siacTMovgest.dataCancellazione IS NULL "
			+ "   AND tmtdm.siacRModificaStato.dataCancellazione IS NULL "
			+ "   AND tmtdm.siacRModificaStato.siacTModifica.modId = mod.modId "
			+ "   AND tmtdm.siacTMovgestT.siacTMovgest.movgestId = :movgestId"
			+ " )")
	public List<Integer> findListModIdImportoByMovgestId(@Param("movgestId") Integer movgestId);

	@Query(" SELECT DISTINCT modifica "
			+ " FROM SiacTMovgestTsDetModFin stmtdmf "
			+ " JOIN stmtdmf.siacRModificaStato stato "
			+ " JOIN stato.siacTModifica modifica "
			+ " WHERE stmtdmf.movgestTsDetModId = :uid "
			+ " AND stato.siacDModificaStato.modStatoCode = 'V' "
			+ " AND " + condizione )
	public SiacTModificaFin findSiacTModificaBySiacTMovgestTsDetMod(
			@Param("uid") Integer uidSiacTMovgestTsDetMod, 
			@Param("dataInput") Date now);
	
}