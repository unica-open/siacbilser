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

import it.csi.siac.siacbilser.integration.entity.SiacTModifica;

public interface SiacTModificaBilRepository extends JpaRepository<SiacTModifica, Integer> {
	
	@Query(" SELECT tmtdm.movgestTsDetImporto "
			+ " FROM SiacTMovgestTsDetMod tmtdm "
			+ " WHERE tmtdm.dataCancellazione IS NULL "
			+ " AND tmtdm.siacRModificaStato.dataCancellazione IS NULL "
			+ " AND tmtdm.siacRModificaStato.siacTModifica.modId = :modId "
			+ " AND tmtdm.siacDMovgestTsDetTipo.movgestTsDetTipoCode = :movgestTsDetTipoCode ")
	BigDecimal findMovgestTsDetImportoModByModIdAndMovgestTsDetTipoCode(@Param("modId") Integer modId, @Param("movgestTsDetTipoCode") String movgestTsDetTipoCode);
	
	@Query(" SELECT m FROM SiacTModifica m "
			+ " WHERE m.siacTAttoAmm.attoammNumero=:attoammNumero "
			+ " AND m.siacTAttoAmm.attoammAnno=:attoammAnno "
			+ " AND m.siacTAttoAmm.siacDAttoAmmTipo.attoammTipoId=:attoammTipoId "
			+ " AND m.siacTAttoAmm.dataCancellazione IS NULL "
			+ " AND m.dataCancellazione IS NULL "
			+ " AND (:attoammSacId IS NULL OR EXISTS ("
			+ "	SELECT 1 FROM m.siacTAttoAmm aa JOIN aa.siacRAttoAmmClasses x WHERE x.siacTClass.classifId=CAST(CAST(:attoammSacId AS string) AS integer)) "
			+ " ) "
			+ " AND m.siacTEnteProprietario.enteProprietarioId=:enteProprietarioId")
	List<SiacTModifica> findSiacTMovgestModificaBySiacTAttoAmm(
		@Param("attoammNumero") Integer attoammNumero, 
		@Param("attoammAnno") String attoammAnno,
		@Param("attoammTipoId") Integer attoammTipoId,
		@Param("attoammSacId") Integer attoammSacId,
		@Param("enteProprietarioId") Integer enteProprietarioId);
	
		

}