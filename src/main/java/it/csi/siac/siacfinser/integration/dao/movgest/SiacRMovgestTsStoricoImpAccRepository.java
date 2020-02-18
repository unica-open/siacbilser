/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.movgest;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsStoricoImpAccFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsFin;

/**
 * The Interface SiacRMovgestTsStoricoImpAccRepository.
 */
public interface SiacRMovgestTsStoricoImpAccRepository extends JpaRepository<SiacRMovgestTsStoricoImpAccFin, Integer> {
	
	@Query( " FROM SiacRMovgestTsStoricoImpAccFin o "
			+ " WHERE o.movgestTsRStoricoId = :movgestTsRStoricoId  "
			+ " AND o.dataCancellazione IS NULL ")
	public SiacRMovgestTsStoricoImpAccFin findSiacRMovgestTsStoricoImpAccById(@Param("movgestTsRStoricoId") Integer movgestTsRStoricoId);

	@Query( " SELECT o.siacTMovgestT.siacTMovgest "
			+ " FROM SiacRMovgestTsStoricoImpAccFin o "
			+ " WHERE o.movgestTsRStoricoId = :movgestTsRStoricoId  "
			+ " AND  o.dataCancellazione IS NULL "
			+ " AND o.siacTMovgestT.siacTMovgest.dataCancellazione IS NULL ")
	public SiacTMovgestFin findSiacTMovgestByMovgestTsRStoricoId(@Param("movgestTsRStoricoId") Integer movgestTsRStoricoId);
	
	@Query( " SELECT o.siacTMovgestT "
			+ " FROM SiacRMovgestTsStoricoImpAccFin o "
			+ " WHERE o.movgestTsRStoricoId = :movgestTsRStoricoId  "
			+ " AND o.dataCancellazione IS NULL "
			+ " AND o.siacTMovgestT.dataCancellazione IS NULL"
			+ " AND o.siacTMovgestT.siacDMovgestTsTipo.movgestTsTipoCode = 'S' ")
	public SiacTMovgestTsFin findSiacTMovgestTsByMovgestTsRStoricoId(@Param("movgestTsRStoricoId") Integer movgestTsRStoricoId);
	

	@Query( " SELECT o "
			+ " FROM SiacRMovgestTsStoricoImpAccFin o "
			+ " WHERE o.dataCancellazione IS NULL "
			+ " AND o.siacTMovgestT.siacTMovgest.movgestId = :movgestId "
			+ " AND o.siacTMovgestT.dataCancellazione IS NULL"
			+ " AND o.siacTMovgestT.siacDMovgestTsTipo.movgestTsTipoCode IN ( :movgestTsTipoCodes) ")
	public List<SiacRMovgestTsStoricoImpAccFin> findAllSiacRMovgestTsStoricoImpAccFinByMovgestId(@Param("movgestId") Integer movgestId, @Param("movgestTsTipoCodes") List<String> movgestTsTipoCodesDaIncludere);
	
	@Query( " SELECT o "
			+ " FROM SiacRMovgestTsStoricoImpAccFin o "
			+ " WHERE o.dataCancellazione IS NULL "
			+ " AND o.siacTMovgestT.movgestTsId = :movgestTsIdImp "
			+ " AND o.siacTMovgestT.siacTMovgest.siacTBil.bilId = :bilId"
			+ " AND o.siacTMovgestT.dataCancellazione IS NULL"
			+ " AND EXISTS( "
			+ "   FROM SiacTMovgestTsFin acc "
			+ "   WHERE acc.dataCancellazione IS NULL "
			+ "   AND acc.movgestTsId = :movgestTsIdAcc "
			+ "   AND acc.siacTMovgest.movgestAnno = o.movgestAnnoAcc "
			+ "   AND acc.siacTMovgest.movgestNumero = o.movgestNumeroAcc "
			+ "   AND acc.siacTMovgest.siacTBil.bilId = :bilId"
			+ "  )"
			)
	public List<SiacRMovgestTsStoricoImpAccFin> findSiacRMovgestTsStoricoImpAccFinByMovgestTsIdImpAndMovgestTsIdAndBilId(@Param("movgestTsIdImp") Integer movgestTsIdImp, @Param("movgestTsIdAcc") Integer movgestTsIdAcc, @Param("bilId") Integer bilId);
	
	@Query( " SELECT o "
			+ " FROM SiacRMovgestTsStoricoImpAccFin o "
			+ " WHERE o.dataCancellazione IS NULL "
			+ " AND o.siacTMovgestT.siacTMovgest.siacTBil.bilId = :bilId"
			+ " AND o.siacTMovgestT.dataCancellazione IS NULL"
			+ " AND EXISTS( "
			+ "   FROM SiacTMovgestTsFin acc "
			+ "   WHERE acc.dataCancellazione IS NULL "
			+ "   AND acc.movgestTsId = :movgestTsIdAcc "
			+ "   AND acc.siacTMovgest.movgestAnno = o.movgestAnnoAcc "
			+ "   AND acc.siacTMovgest.movgestNumero = o.movgestNumeroAcc "
			+ "  ) "
			+ " AND EXISTS( "
			+ "   FROM SiacTMovgestTsFin imp "
			+ "   WHERE imp.dataCancellazione IS NULL "
			+ "   AND imp.movgestTsId = :movgestTsIdImp "
			+ "   AND imp.siacTMovgest.movgestAnno = o.siacTMovgestT.siacTMovgest.movgestAnno "
			+ "   AND imp.siacTMovgest.movgestNumero = o.siacTMovgestT.siacTMovgest.movgestNumero "
			+ "  )"
			)
	public List<SiacRMovgestTsStoricoImpAccFin> findSiacRMovgestTsStoricoImpAccFinInBilSuccessivoByMovgestTsIdImpAndMovgestTsIdAccBilPrecedente(@Param("movgestTsIdImp") Integer movgestTsIdImp, @Param("movgestTsIdAcc") Integer movgestTsIdAcc, @Param("bilId") Integer bilIdAnnoSuccessivo);

}