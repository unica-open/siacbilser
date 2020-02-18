/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTBilElemDetVar;


/**
 * The Interface SiacTBilElemDetVarRepository.
 */
public interface SiacTBilElemDetVarRepository extends JpaRepository<SiacTBilElemDetVar, Integer> {


	@Query(" FROM SiacTBilElemDetVar bedv " +
			" WHERE bedv.dataCancellazione IS NULL " +
			" AND bedv.siacRVariazioneStato.siacTVariazione.variazioneId = :variazioneId " +
			" AND bedv.siacRVariazioneStato.dataCancellazione IS NULL "  +
			" AND bedv.siacTBilElem.elemId = :elemId "
			)
	List<SiacTBilElemDetVar> findByVariazioneIdEBilElemId(@Param("variazioneId") Integer variazioneId, @Param("elemId") Integer elemId);
	
	@Query(" FROM SiacTBilElemDetVar bedv " +
			" WHERE bedv.dataCancellazione IS NULL " +
			" AND bedv.siacRVariazioneStato.siacTVariazione.variazioneId = :variazioneId " +
			" AND bedv.siacRVariazioneStato.dataCancellazione IS NULL "  +
			" AND bedv.siacTBilElem.elemId = :elemId " +
			" AND bedv.siacTPeriodo.anno = :anno " +
			" AND bedv.siacDBilElemDetTipo.elemDetTipoCode = :elemDetTipoCode "
			)
	List<SiacTBilElemDetVar> findByVariazioneIdEBilElemIdEAnnoEElemDetTipoCode(@Param("variazioneId") Integer variazioneId, @Param("elemId") Integer elemId, @Param("anno") String anno, @Param("elemDetTipoCode") String elemDetTipoCode);

	@Query(" SELECT tbedv.siacTPeriodo.anno, tbedv.siacDBilElemDetTipo.elemDetTipoCode, COALESCE(SUM(tbedv.elemDetImporto), 0) " +
			" FROM SiacTBilElemDetVar tbedv " +
			" WHERE tbedv.dataCancellazione IS NULL " +
			" AND tbedv.siacRVariazioneStato.siacTVariazione.variazioneId = :variazioneId " +
			" AND tbedv.siacRVariazioneStato.dataCancellazione IS NULL "  +
			" AND tbedv.siacTBilElem.siacDBilElemTipo.elemTipoCode IN (:elemTipoCodes) " +
			" GROUP BY tbedv.siacTPeriodo.anno, tbedv.siacDBilElemDetTipo.elemDetTipoCode "
			)
	List<Object[]> calcolaTotaleImportoVariazioneByTipoStanziamentoETipoCapitolo(@Param("variazioneId") Integer uidVariazione, @Param("elemTipoCodes") List<String> elemTipoCodes);
	
}
