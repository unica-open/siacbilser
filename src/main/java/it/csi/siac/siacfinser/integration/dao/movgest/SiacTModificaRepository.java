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
	BigDecimal findMovgestTsDetImportoModByModIdAndMovgestTsDetTipoCode(@Param("modId") Integer modId, @Param("movgestTsDetTipoCode") String movgestTsDetTipoCode);
}