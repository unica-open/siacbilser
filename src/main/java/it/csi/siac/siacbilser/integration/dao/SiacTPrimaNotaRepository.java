/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacDEvento;
import it.csi.siac.siacbilser.integration.entity.SiacTPdceConto;
import it.csi.siac.siacbilser.integration.entity.SiacTPrimaNota;
import it.csi.siac.siacbilser.integration.entity.SiacTRegMovfin;

/**
 * The Interface SiacTPrimaNotaRepository.
 */
public interface SiacTPrimaNotaRepository extends JpaRepository<SiacTPrimaNota, Integer> {

	@Query(" FROM SiacTPrimaNota t " + 
			" WHERE t.dataCancellazione IS NULL " +
			" AND t.pnotaProgressivogiornale IS NOT NULL " +  //deve essere stata validata
			" AND EXISTS ( FROM t.siacRPrimaNotaStatos rs" +   //deve essere in stato annullato
			"			   WHERE rs.dataCancellazione IS NULL " +	
			"			   AND rs.siacDPrimaNotaStato.pnotaStatoCode = 'A' " +
			" 			  ) " +
			" AND t.siacDAmbito = ( SELECT t2.siacDAmbito " +  //deve essere dello stesso ambito delle prima nota di riferimento
			"						FROM SiacTPrimaNota t2 " +
			"   					WHERE t2.pnotaId = :pnotaId) " +
			" AND EXISTS ( FROM t.siacTMovEps mep " +   //deve essere collegata allo stesso movimentoS delle prima nota di riferimento
			" 			   WHERE mep.dataCancellazione IS NULL " +
			" 			   AND EXISTS ( FROM  mep.siacTRegMovfin.siacREventoRegMovfins rermf " +
			" 							WHERE rermf.dataCancellazione IS NULL " +
			" 							AND rermf.campoPkId IN ( SELECT r.campoPkId " +
			"													 FROM SiacREventoRegMovfin r " +
			"													 WHERE r.dataCancellazione IS NULL " +
			"													 AND EXISTS ( FROM r.siacTRegMovfin.siacTMovEps mep2 " +
			"																  WHERE mep2.dataCancellazione IS NULL " +
			"																  AND mep2.siacTPrimaNota.pnotaId = :pnotaId " +
			"																) " +
			"													)" +
			"							AND rermf.siacDEvento.siacDEventoTipo.eventoTipoId IN ( SELECT r2.siacDEvento.siacDEventoTipo.eventoTipoId " +
			"																				 	FROM SiacREventoRegMovfin r2 " +
			"																				 	WHERE r2.dataCancellazione IS NULL " +
			"																				 	AND EXISTS ( FROM r2.siacTRegMovfin.siacTMovEps mep3 " +
			"																  									WHERE mep3.dataCancellazione IS NULL " +
			"																 									AND mep3.siacTPrimaNota.pnotaId = :pnotaId " +
			"																								) " +
			"																					)" +
			"							) " +
			" 			  ) "
			)
	List<SiacTPrimaNota> findPrimeNoteDefinitiveAnnullateByPrimaNota(@Param("pnotaId") Integer pNotaId);

	@Query(" FROM SiacTRegMovfin rmf" +
			" WHERE rmf.dataCancellazione IS NULL " +
			" AND EXISTS ( FROM rmf.siacRRegMovfinStatos rmfs " +
			"				WHERE rmfs.dataCancellazione IS NULL" +
			"				AND rmfs.siacDRegMovfinStato.regmovfinStatoCode not in ('A') " +
			"  			  ) " +
			" AND EXISTS ( FROM rmf.siacTMovEps mep" +
			"  			   WHERE mep.dataCancellazione IS NULL " +
			"  			   AND mep.siacTPrimaNota.pnotaId = :pnotaId " +
			"			  )")
	SiacTRegMovfin findRegMovFinNonAnnullataByPrimaNota(@Param("pnotaId") Integer pNotaId);

	
	@Query(" SELECT COALESCE(SUM(med.movepDetImporto),0) "
			+ " FROM SiacTMovEpDet med"
			+ " WHERE med.siacTMovEp.siacTPrimaNota.pnotaId = :pnotaId "
			+ " AND med.movepDetSegno = :movepDetSegno " )
	BigDecimal findSumImportoMovimentiEP(@Param("pnotaId") Integer pnotaId, @Param("movepDetSegno") String movepDetSegno);

	
	@Query(" SELECT tmed.siacTPdceConto "
			+ " FROM SiacTMovEpDet tmed "
			+ " WHERE tmed.dataCancellazione IS NULL "
			+ " AND tmed.siacTMovEp.dataCancellazione IS NULL "
			+ " AND tmed.siacTMovEp.siacTPrimaNota.pnotaId = :pnotaId "
			+ " AND tmed.siacTPdceConto.dataCancellazione IS NULL "
			+ " AND tmed.siacTPdceConto.siacDPdceContoTipo.pdceCtTipoCode = 'CES' "
			+ " AND tmed.siacTPdceConto.siacTPdceFamTree.siacDPdceFam.pdceFamCode = 'AP' ")
	List<SiacTPdceConto> findSiacTPdceContoInv(@Param("pnotaId") Integer pnotaId);
	
	@Query(" SELECT rpn.siacTPrimaNotaFiglio "
			+ " FROM SiacRPrimaNota rpn "
			+ " WHERE rpn.dataCancellazione IS NULL "
			+ " AND rpn.siacTPrimaNotaFiglio.dataCancellazione IS NULL "
			+ " AND rpn.siacTPrimaNotaPadre.pnotaId = :pnotaId "
			+ " AND rpn.siacTPrimaNotaFiglio.siacDAmbito.ambitoCode = 'AMBITO_INV' ")
	List<SiacTPrimaNota> findSiacTPrimaNotaInv(@Param("pnotaId") Integer pnotaId);
	
	@Query(" SELECT rerm.siacDEvento "
			+ " FROM SiacREventoRegMovfin rerm, SiacTMovEp tme " 
			+ " WHERE rerm.dataCancellazione IS NULL " 
			+ " AND rerm.siacTRegMovfin = tme.siacTRegMovfin " 
			+ " AND tme.siacTPrimaNota.pnotaId = :pnotaId " 
			+ " AND rerm.siacTRegMovfin.dataCancellazione IS NULL " 			
			)
	List<SiacDEvento> findEventoPrimaNotaIntegrata(@Param("pnotaId") Integer pnotaId);
	
	@Query(" SELECT rec.siacDEvento "
			+ " FROM SiacREventoCausale rec, SiacTMovEp tme " 
			+ " WHERE rec.dataCancellazione IS NULL " 
			+ " AND rec.siacTCausaleEp = tme.siacTCausaleEp " 
			+ " AND tme.siacTPrimaNota.pnotaId = :pnotaId " 
			+ " AND rec.siacDEvento.dataCancellazione IS NULL " 
			+ " AND rec.dataInizioValidita <= :dataInput " 
			+ " AND (rec.dataFineValidita IS NULL OR :dataInput <=  rec.dataFineValidita) "
			)
	List<SiacDEvento> findEventoPrimaNotaLibera(@Param("pnotaId") Integer pnotaId, @Param("dataInput") Date dataInizioValiditaFiltro);
	
	
	
}
