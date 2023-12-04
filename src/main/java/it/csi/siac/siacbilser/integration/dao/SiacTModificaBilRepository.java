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
import it.csi.siac.siacbilser.integration.entity.SiacTSoggetto;

public interface SiacTModificaBilRepository extends JpaRepository<SiacTModifica, Integer> {
	
	@Query(" SELECT tmtdm.movgestTsDetImporto "
			+ " FROM SiacTMovgestTsDetMod tmtdm "
			+ " WHERE tmtdm.dataCancellazione IS NULL "
			+ " AND tmtdm.siacRModificaStato.dataCancellazione IS NULL "
			+ " AND tmtdm.siacRModificaStato.siacTModifica.modId = :modId "
			+ " AND tmtdm.siacDMovgestTsDetTipo.movgestTsDetTipoCode = :movgestTsDetTipoCode ")
	BigDecimal findMovgestTsDetImportoModByModIdAndMovgestTsDetTipoCode(@Param("modId") Integer modId, @Param("movgestTsDetTipoCode") String movgestTsDetTipoCode);
	
//	@Query(" SELECT m FROM SiacTModifica m "
//			+ " WHERE m.siacTAttoAmm.attoammNumero=:attoammNumero "
//			+ " AND m.siacTAttoAmm.attoammAnno=:attoammAnno "
//			+ " AND m.siacTAttoAmm.siacDAttoAmmTipo.attoammTipoId=:attoammTipoId "
//			+ " AND m.siacTAttoAmm.dataCancellazione IS NULL "
//			+ " AND m.dataCancellazione IS NULL "
//			+ " AND (:attoammSacId IS NULL OR EXISTS ("
//			+ "	SELECT 1 FROM m.siacTAttoAmm aa JOIN aa.siacRAttoAmmClasses x WHERE x.siacTClass.classifId=CAST(CAST(:attoammSacId AS string) AS integer)) "
//			+ " ) "
//			+ " AND m.siacTEnteProprietario.enteProprietarioId=:enteProprietarioId")
	
	//SIAC-7383 Esclusione movimenti annullati
	@Query(" SELECT m FROM SiacTModifica m "
			+ " JOIN m.siacRModificaStatos ms "//
			+ " JOIN ms.siacDModificaStato dms "//
			+ " WHERE m.siacTAttoAmm.attoammNumero=:attoammNumero "
			+ " AND m.siacTAttoAmm.attoammAnno=:attoammAnno "
			+ " AND m.siacTAttoAmm.siacDAttoAmmTipo.attoammTipoId=:attoammTipoId "
			+ " AND m.siacTAttoAmm.dataCancellazione IS NULL "
			+ " AND m.dataCancellazione IS NULL "
			+ " AND ms.dataCancellazione IS NULL AND dms.modStatoCode != 'A'"
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
	
	
	@Query( " SELECT sogmod.siacTSoggetto2 "
			+ " FROM SiacRMovgestTsSogMod sogmod, SiacRModificaStato rstato "
			+ " WHERE sogmod.dataCancellazione 	IS NULL "
			+ " AND rstato.dataCancellazione IS NULL "
			+ " AND sogmod.siacRModificaStato = rstato "
			+ " AND rstato.siacTModifica.modId = :modId "
			+ " AND sogmod.siacTEnteProprietario.enteProprietarioId=:enteProprietarioId) "
			)
	List<SiacTSoggetto> findSiacTSoggettoBySiacRMovgestTsSogMod(@Param("modId") Integer modId, @Param("enteProprietarioId") Integer enteProprietarioId);
	
	
	@Query(" SELECT sogimp.siacTSoggetto "
			+ " FROM SiacRMovgestTsSog sogimp, SiacRModificaStato rstato, SiacTMovgestTsDetMod modimp "
			+ " WHERE sogimp.dataCancellazione 	IS NULL          "
			+ " AND rstato.dataCancellazione IS NULL "
			+ " AND modimp.dataCancellazione IS NULL "
			+ " AND modimp.siacTMovgestT.dataCancellazione IS NULL "
			+ " AND rstato.siacTModifica.dataCancellazione IS NULL "
			+ " AND modimp.siacRModificaStato = rstato  "
			+ " AND modimp.siacTMovgestT = sogimp.siacTMovgestT "
			+ " AND rstato.siacTModifica.modId = :modId "
			+ " AND sogimp.siacTEnteProprietario.enteProprietarioId=:enteProprietarioId "
			)
	List<SiacTSoggetto> findSiacTSoggettoBySiacRMovgestTsSog(@Param("modId") Integer modId, @Param("enteProprietarioId") Integer enteProprietarioId);
	
	
	
		

}