/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTPrimaNotaRateiRisconti;

/**
 * The Interface SiacTPrimaNotaRateiRiscontiRepository.
 */
public interface SiacTPrimaNotaRateiRiscontiRepository extends JpaRepository<SiacTPrimaNotaRateiRisconti, Integer> {

	@Query( " SELECT t.pnotaId "                                                       +
			" FROM SiacTPrimaNota t "                                                  + 
		    " WHERE t.dataCancellazione IS NULL "                                      +
		    // non deve essere in stato annullato
		    " AND NOT EXISTS ( FROM t.siacRPrimaNotaStatos rs"                         +   
			"			      WHERE rs.dataCancellazione IS NULL "                     +	
			"			      AND rs.siacDPrimaNotaStato.pnotaStatoCode = 'A' "        +
			" 			     ) "                                                       +
			//deve essere collegata al risconto e di tipo risconto
		    " AND EXISTS ( FROM t.siacTMovEps mep, SiacREventoRegMovfin rerm "         +   
			" 			   WHERE mep.dataCancellazione IS NULL "                       +
			" 			   AND rerm.dataCancellazione IS NULL "                        +
			" 			   AND mep.siacTRegMovfin = rerm.siacTRegMovfin  "             +
			" 		       AND rerm.campoPkId = :pnotarr_id "                          +
			"			   AND rerm.siacDEvento.siacDCollegamentoTipo.collegamentoTipoCode = :collegamentoTipoCode " +
			" 			 ) "
			)
	Integer findSiacTPrimaNotaRateoRiscontoBySiacTPrimaNotaRateiRiscontiId(@Param("pnotarr_id") Integer pnotarrid, @Param("collegamentoTipoCode") String collegamentoTipoCode );
	
}
