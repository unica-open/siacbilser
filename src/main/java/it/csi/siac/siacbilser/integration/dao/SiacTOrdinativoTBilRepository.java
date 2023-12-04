/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTOrdinativoT;

public interface SiacTOrdinativoTBilRepository extends JpaRepository<SiacTOrdinativoT, Integer> {
	
	
//	@Query(" SELECT ot.ordTsId FROM SiacTOrdinativoT ot"
//			+ " WHERE ot.dataCancellazione IS NULL "
//			+ " AND ot.siacTOrdinativo.ordId = :ordId "
//			+ " AND ot.ordTsDesc = :ordTsDesc ")
//	Integer findUidSubOrdinativoByOrdinativoAndDesc(@Param("ordId") Integer ordId, @Param("ordTsDesc") String ordTsDesc);
	
	
	
	@Query(" SELECT tot FROM SiacTOrdinativoT tot "
			+ " JOIN tot.siacRLiquidazioneOrds rlo "
			+ " JOIN rlo.siacTLiquidazione.siacRLiquidazioneMovgests rlm "
			+ " JOIN rlm.siacTMovgestT tmt "
			+ " WHERE tot.siacTOrdinativo.siacDOrdinativoTipo.ordTipoCode='P' "
			+ " AND tot.siacTEnteProprietario.enteProprietarioId=:idEnte "
			+ " AND tot.siacTOrdinativo.siacTBil.siacTPeriodo.anno=CAST(:annoBilancio AS string) "
			+ "	AND tmt.siacTMovgest.siacTBil=tot.siacTOrdinativo.siacTBil "
			+ "	AND EXISTS ( "
			+ " 	SELECT 1 FROM tmt.siacRMovgestTsProgrammas rmtp "
			+ " 	WHERE rmtp.siacTProgramma.programmaCode=:codiceProgetto "
			+ "		AND rmtp.siacTProgramma.siacTBil=tot.siacTOrdinativo.siacTBil "
			+ " 	AND rmtp.dataCancellazione IS NULL "
			+ " 	AND rmtp.dataInizioValidita < CURRENT_TIMESTAMP "
			+ " 	AND (rmtp.dataFineValidita IS NULL OR rmtp.dataFineValidita > CURRENT_TIMESTAMP) "
			+ " ) "
			+ " AND (CAST(:cup AS string) IS NULL OR EXISTS ( "
			+ "		SELECT 1 FROM rlo.siacTLiquidazione.siacRLiquidazioneAttrs rla "
			+ " 	WHERE rla.siacTAttr.attrCode='cup' "
			+ "		AND rla.testo=:cup"
			+ " 	AND rla.dataCancellazione IS NULL "
			+ " 	AND rla.dataInizioValidita < CURRENT_TIMESTAMP "
			+ " 	AND (rla.dataFineValidita IS NULL OR rla.dataFineValidita > CURRENT_TIMESTAMP) "
			+ "	)) "
			+ " AND (CAST(:cig AS string) IS NULL OR EXISTS ( "
			+ "		SELECT 1 FROM rlo.siacTLiquidazione.siacRLiquidazioneAttrs rla2 "
			+ "     WHERE rla2.siacTAttr.attrCode='cig' "
			+ "		AND rla2.testo=:cig"
			+ " 	AND rla2.dataCancellazione IS NULL "
			+ " 	AND rla2.dataInizioValidita < CURRENT_TIMESTAMP "
			+ " 	AND (rla2.dataFineValidita IS NULL OR rla2.dataFineValidita > CURRENT_TIMESTAMP) "
			+ "	)) "
			+ " AND tot.siacTOrdinativo.dataCancellazione IS NULL "
			+ " AND tot.siacTOrdinativo.dataInizioValidita < CURRENT_TIMESTAMP "
			+ " AND (tot.siacTOrdinativo.dataFineValidita IS NULL OR tot.siacTOrdinativo.dataFineValidita > CURRENT_TIMESTAMP) "
			+ " AND tot.dataCancellazione IS NULL "
			+ " AND tot.dataInizioValidita < CURRENT_TIMESTAMP "
			+ " AND (tot.dataFineValidita IS NULL OR tot.dataFineValidita > CURRENT_TIMESTAMP) "
			+ " AND rlo.dataCancellazione IS NULL "
			+ " AND rlo.dataInizioValidita < CURRENT_TIMESTAMP "
			+ " AND (rlo.dataFineValidita IS NULL OR rlo.dataFineValidita > CURRENT_TIMESTAMP) "
			+ " AND rlm.dataCancellazione IS NULL "
			+ " AND rlm.dataInizioValidita < CURRENT_TIMESTAMP "
			+ " AND (rlm.dataFineValidita IS NULL OR rlm.dataFineValidita > CURRENT_TIMESTAMP) "
			+ " AND tmt.dataCancellazione IS NULL "
			+ " AND tmt.dataInizioValidita < CURRENT_TIMESTAMP "
			+ " AND (tmt.dataFineValidita IS NULL OR tmt.dataFineValidita > CURRENT_TIMESTAMP) "
)
	List<SiacTOrdinativoT> findSiacTOrdinativoTPagamento(
			@Param("idEnte") Integer idEnte,
			@Param("annoBilancio") Integer annoBilancio,
			@Param("cup") String cup,
			@Param("codiceProgetto") String codiceProgetto,
			@Param("cig") String cig			
	);	
}