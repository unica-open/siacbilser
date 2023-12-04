/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacDAmbito;
import it.csi.siac.siacbilser.integration.entity.SiacDClassFam;
import it.csi.siac.siacbilser.integration.entity.SiacDPdceFam;
import it.csi.siac.siacbilser.integration.entity.SiacTPdceConto;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCausaleEpStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDPrimaNotaStatoEnum;

/**
 * The Interface SiacTPdceContoRepository.
 */
public interface SiacTPdceContoRepository extends JpaRepository<SiacTPdceConto, Integer> {
	
	
	/**
	 * Find conti figlio con figli.
	 *
	 * @param pdceContoIdPadre the pdce conto id padre
	 * @param enteProprietarioId the ente proprietario id
	 * @return the list
	 */
	@Query(  
			" SELECT count(c) FROM SiacTPdceConto c" +
			" WHERE c.dataCancellazione IS NULL " + 
			" AND c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "+
			" AND c.siacTPdceConto.pdceContoId = :pdceContoIdPadre " +
			" AND EXISTS (FROM SiacTPdceConto c2 "+ 
			"             WHERE c2.dataCancellazione IS NULL " +
			"             AND c2.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "+
			"             AND c2.siacTPdceConto.pdceContoId = c.pdceContoId ) "
			
			
			)
	Long countContiFiglioConFigli(@Param("pdceContoIdPadre") Integer pdceContoIdPadre, @Param("enteProprietarioId") Integer enteProprietarioId);

	
	/**
	 * Numero di causali associate ad almeno un conto tra quelli passati come parametro.
	 * 
	 * @param siacTPdceContos
	 * @return
	 */
	@Query( 
			" SELECT count(rcepc) FROM SiacRCausaleEpPdceConto rcepc, SiacTCausaleEp ce " + 
			" WHERE rcepc.dataCancellazione IS NULL " +
			" AND ce.dataCancellazione IS NULL " +
			" AND rcepc.siacTPdceConto IN (:siacTPdceContos) "+
			" AND rcepc.siacTCausaleEp = ce " +
			" AND EXISTS (FROM ce.siacRCausaleEpStatos ces "+ 
			"             WHERE ces.dataCancellazione IS NULL "+
			"             AND ces.siacDCausaleEpStato.causaleEpStatoCode <> '"+SiacDCausaleEpStatoEnum.codiceAnnullato+"'  " +
			"            ) " 
			)
	Long countCausaliNonAnnullateAssociateAiConti(@Param("siacTPdceContos") List<SiacTPdceConto> siacTPdceContos);
	
	/**
	 * Numero di prime note collegate ad almeno uno dei conti tra quelli passati come parametro.
	 * 
	 * @param siacTPdceContos
	 * @return
	 */
	@Query( 
			" SELECT count(tpn) FROM SiacTPrimaNota tpn" + 
			" WHERE tpn.dataCancellazione IS NULL " +
			" AND EXISTS (FROM tpn.siacRPrimaNotaStatos rpns "+ 
			"             WHERE rpns.dataCancellazione IS NULL "+
			"             AND rpns.siacDPrimaNotaStato.pnotaStatoCode <> '"+SiacDPrimaNotaStatoEnum.codiceAnnullato+"'  " +
			"            ) " +
			" AND EXISTS (FROM tpn.siacTMovEps tmep "+ 
			"             WHERE tmep.dataCancellazione IS NULL "+
			" 			  AND EXISTS (FROM tmep.siacTMovEpDets tmepd "+ 
			"             			  WHERE tmepd.dataCancellazione IS NULL "+
			"            			  AND tmepd.siacTPdceConto	IN (:siacTPdceContos)"+
			" 					 	 ) "+
			"            ) "
			)
	Long countPrimeNoteNonAnnullateAssociateAiConti(@Param("siacTPdceContos") List<SiacTPdceConto> siacTPdceContos);

	//SIAC-7892 aggiunti controlli su dataFineValidita
	@Query(" SELECT r.siacDClassFam " +
			" FROM SiacRPdceFamClassFam r " +
			" WHERE r.dataCancellazione IS NULL" +
			" AND r.siacDPdceFam.pdceFamId = :pdceFamId " +
			" AND ( r.dataFineValidita IS NULL OR r.dataFineValidita > CURRENT_TIMESTAMP ) " +
			" AND r.siacDPdceFam.dataCancellazione IS NULL " +
			" AND ( r.siacDPdceFam.dataFineValidita IS NULL OR r.siacDPdceFam.dataFineValidita > CURRENT_TIMESTAMP ) ")
	SiacDClassFam findClassFamByClassePiano(@Param("pdceFamId") Integer pdceFamId);


	@Query(" SELECT r. boolean_ " +
			" FROM SiacRPdceContoAttr r " +//TODO Occhio! Manca il filtro per la dataFineValidita!
			" WHERE r.dataCancellazione IS NULL" +
			" AND r.siacTPdceConto.pdceContoId = :pdceContoId " + 
			" AND r.siacTAttr.attrCode = 'pdce_conto_foglia' ")
	String findAttrContoFoglia(@Param("pdceContoId") int pdceContoId);


	@Query(" SELECT DISTINCT c.siacTPdceFamTree.siacDPdceFam " +
			" FROM SiacTPdceConto c " +
			" WHERE c.dataCancellazione IS NULL " +
			" AND c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
			" AND EXISTS ( FROM c.siacRPdceContoAttrs r " +
			"		 		WHERE r.dataCancellazione IS NULL" +
			" 				AND r.siacTAttr.attrCode = 'pdce_ammortamento' " +
			" 				AND r.boolean_ = 'S'" +
			" 			  ) "
			)
	List<SiacDPdceFam> findClassiPianoAmmortamento(@Param("enteProprietarioId") Integer enteProprietarioId);

	@Query(" SELECT tpc.siacDAmbito "
			+ " FROM SiacTPdceConto tpc "
			+ " WHERE tpc.pdceContoId = :pdceContoId ")
	SiacDAmbito getAmbitoByPdceContoId(@Param("pdceContoId") Integer pdceContoId);
	
	@Query(" SELECT tpc "
			+ " FROM SiacTPdceConto tpc "
			+ " WHERE tpc.pdceContoId = :pdceContoId ")
	SiacTPdceConto findSiacTPdceContoByUid(@Param("pdceContoId") Integer pdceContoId);
	
}
