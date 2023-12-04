/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacDEvento;
import it.csi.siac.siacbilser.integration.entity.SiacTRegMovfin;

/**
 * The Interface SiacTRegMovfinRepository.
 */
public interface SiacTRegMovfinRepository extends JpaRepository<SiacTRegMovfin, Integer> {

	@Query(" FROM SiacTRegMovfin str" +
			" WHERE str.dataCancellazione IS NULL " +
			//registrazione in stato CONTABILIZZATO
			" AND EXISTS ( FROM str.siacRRegMovfinStatos rms " +
			"				WHERE rms.dataCancellazione IS NULL " +
			" 				AND rms.siacDRegMovfinStato.regmovfinStatoCode = 'C' " +
			" 			 )" +
			" AND EXISTS ( FROM str.siacREventoRegMovfins rer" +
			" 				WHERE rer.dataCancellazione IS NULL " +
							//collegata ad un evento di tipo accertamento
			" 				AND rer.siacDEvento.siacDEventoTipo.eventoTipoCode = 'A' " +
							//per un accertamento collegato al mio impegno
			"  				AND  (rer.campoPkId IN ( " +
												   //id del movimento gestione	
			"									   SELECT rm.siacTMovgestT2.siacTMovgest.movgestId " +
			"  									   FROM SiacRMovgestT rm " +
												   //record non cancellati	
			" 									   WHERE rm.dataCancellazione IS NULL" +
			" 									   AND (rm.dataFineValidita IS NULL OR rm.dataFineValidita > CURRENT_TIMESTAMP) " +
			"  									   AND rm.siacTMovgestT2.dataCancellazione IS NULL " +	
			" 									   AND (rm.siacTMovgestT2.dataFineValidita IS NULL OR rm.siacTMovgestT2.dataFineValidita > CURRENT_TIMESTAMP) " +
			"  									   AND rm.siacTMovgestT2.siacTMovgest.dataCancellazione IS NULL " +
			" 									   AND (rm.siacTMovgestT2.siacTMovgest.dataFineValidita IS NULL OR rm.siacTMovgestT2.siacTMovgest.dataFineValidita > CURRENT_TIMESTAMP) " +
												   // movimento ts di tipo testata	
			"  									   AND rm.siacTMovgestT2.siacDMovgestTsTipo.movgestTsTipoCode = 'T' " +
												   // che sia un accertamento
			"  									   AND rm.siacTMovgestT2.siacTMovgest.siacDMovgestTipo.movgestTipoCode = 'A' " +
												   // e che sia collegato all'impegno passato come parametro
			" 									   AND rm.siacTMovgestT1.siacTMovgest.movgestId = :movgestId " +
			"									 )"	+
			"   				) "	+
			"			)"
	)
	List<SiacTRegMovfin> findRegistrazioniCollegateAdAccertamentiCollegatiAdImpegno(@Param("movgestId")Integer movgestId);

	
	@Query(" SELECT COALESCE(COUNT(r),0) "
			+ " FROM SiacRRegMovfinStato r "
			+ " WHERE r.dataCancellazione IS NULL "
			+ " AND r.siacTRegMovfin.regmovfinId IN (:uidsRegMovfin) "
			+ " AND r.siacDRegMovfinStato.regmovfinStatoCode IN (:regmovfinStatoCode )  ")
	Long countSiacTRegistrazioneMovFinByUIdsAndStato(@Param("uidsRegMovfin") Set<Integer> uidsRegMovfin, @Param("regmovfinStatoCode") List<String> codiciStato);
	
	@Query(" SELECT rerm.siacDEvento "
			+ " FROM SiacREventoRegMovfin rerm "
			+ " WHERE rerm.dataCancellazione IS NULL "
			+ " AND rerm.siacTRegMovfin.regmovfinId = :regmovfinId ")
	List<SiacDEvento> findSiacDEventoByRegmovfinId(@Param("regmovfinId") Integer regmovfinId);
	
}
