/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacDAccreditoTipo;
import it.csi.siac.siacbilser.integration.entity.SiacDModpagStato;
import it.csi.siac.siacbilser.integration.entity.SiacRSoggettoRelaz;
import it.csi.siac.siacbilser.integration.entity.SiacTAbi;
import it.csi.siac.siacbilser.integration.entity.SiacTCab;
import it.csi.siac.siacbilser.integration.entity.SiacTModpag;

// TODO: Auto-generated Javadoc
/**
 * The Interface SiacTModpagRepository.
 */
public interface SiacTModpagRepository extends JpaRepository<SiacTModpag, Integer> {
	
	/**
	 * Cerca conti correnti per ente.
	 *
	 * @param enteId the ente id
	 * @return the list
	 */ 
	
	@Query("FROM SiacTModpag mp " 
			+ " WHERE mp.dataCancellazione is NULL " 
			+ " AND mp.dataInizioValidita < CURRENT_TIMESTAMP "
			+ " AND ( mp.dataFineValidita is NULL or mp.dataFineValidita > CURRENT_TIMESTAMP) "
			
			+ " AND mp.siacTSoggetto.dataCancellazione is NULL "
			+ " AND mp.siacTSoggetto.dataInizioValidita < CURRENT_TIMESTAMP "
			+ " AND ( mp.siacTSoggetto.dataFineValidita is NULL "
			+ "     OR mp.siacTSoggetto.dataFineValidita > CURRENT_TIMESTAMP) "
			
			+ " AND EXISTS (FROM SiacRSoggettoEnteProprietario sep " 
			+ " WHERE sep.dataCancellazione is NULL " 
			+ " AND sep.dataInizioValidita < CURRENT_TIMESTAMP "
			+ " AND ( sep.dataFineValidita is NULL or sep.dataFineValidita > CURRENT_TIMESTAMP) "
			+ " AND sep.soggettoEntePropId = mp.siacTSoggetto.soggettoId"
			+ " AND sep.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId"
			+ " ) ")
	List<SiacTModpag> findByEnteProprietario(@Param("enteProprietarioId") Integer enteId);
	
	
	@Query("FROM SiacDModpagStato mps " 
			+ " WHERE mps.dataCancellazione is NULL " 
			+ " AND EXISTS ( FROM mps.siacRModpagStatos rmps "
			+ "              WHERE rmps.dataCancellazione IS NULL "
			+ "              AND rmps.siacTModpag.modpagId = :modpagId "
			+ " ) ")
	SiacDModpagStato findStatoModPagByIdModPag( @Param("modpagId") Integer modpagId);
	
	
	@Query("FROM SiacTModpag mp " 
			+ " WHERE mp.dataCancellazione is NULL " 
			+ " AND ( mp.dataFineValidita is NULL or mp.dataFineValidita > CURRENT_TIMESTAMP) "
			+ " AND ( mp.dataScadenza is NULL or mp.dataScadenza > CURRENT_TIMESTAMP) "
			
			+ " AND mp.siacTSoggetto.dataCancellazione is NULL "
			+ " AND ( mp.siacTSoggetto.dataFineValidita is NULL "
			+ "    		 OR mp.siacTSoggetto.dataFineValidita > CURRENT_TIMESTAMP ) "
			+ " AND mp.siacTSoggetto.soggettoId = :soggettoId "
		
			+ " AND EXISTS (FROM mp.siacRModpagStatos mps " 
			+ " 			WHERE mps.dataCancellazione is NULL " 
			+ " 			AND ( mps.dataFineValidita is NULL or mps.dataFineValidita > CURRENT_TIMESTAMP) "
			+ " 			AND mps.siacDModpagStato.modpagStatoCode = 'VALIDO' "
			+ " )"
			+ " AND mp.siacDAccreditoTipo.accreditoTipoCode NOT IN ('CSI','CSC')"
			+ " ORDER BY mp.siacDAccreditoTipo.accreditoPriorita DESC ")
	List<SiacTModpag> findModPagValidaBySoggetto(@Param("soggettoId") Integer soggettoId);
	
	@Query("FROM SiacTModpag mp " 
			+ " WHERE mp.dataCancellazione IS NULL "
			+ " AND mp.perStipendi = :perStipendi "
			+ " AND (mp.dataFineValidita IS NULL OR mp.dataFineValidita > CURRENT_TIMESTAMP) "
			+ " AND (mp.dataScadenza IS NULL OR mp.dataScadenza > CURRENT_TIMESTAMP) "
			+ " AND mp.siacTSoggetto.dataCancellazione is NULL "
			+ " AND (mp.siacTSoggetto.dataFineValidita IS NULL OR mp.siacTSoggetto.dataFineValidita > CURRENT_TIMESTAMP) "
			+ " AND mp.siacTSoggetto.soggettoId = :soggettoId "
			+ " AND EXISTS ( "
			+ "     FROM mp.siacRModpagStatos mps " 
			+ "     WHERE mps.dataCancellazione IS NULL " 
			+ "     AND (mps.dataFineValidita IS NULL OR mps.dataFineValidita > CURRENT_TIMESTAMP) "
			+ "     AND mps.siacDModpagStato.modpagStatoCode = 'VALIDO' "
			+ " ) "
			+ " AND mp.siacDAccreditoTipo.accreditoTipoCode NOT IN ('CSI','CSC') "
			+ " ORDER BY mp.siacDAccreditoTipo.accreditoPriorita DESC ")
	List<SiacTModpag> findModPagValidaBySoggettoAndPerStipendi(@Param("soggettoId") Integer soggettoId, @Param("perStipendi") Boolean perStipendi);

	@Query("FROM SiacTModpag mp " 
			+ " WHERE mp.dataCancellazione is NULL " 
			+ " AND ( mp.dataFineValidita is NULL or mp.dataFineValidita > CURRENT_TIMESTAMP) "
			+ " AND ( mp.dataScadenza is NULL or mp.dataScadenza > CURRENT_TIMESTAMP) "
			
			+ " AND mp.siacTSoggetto.dataCancellazione is NULL "
			+ " AND ( mp.siacTSoggetto.dataFineValidita is NULL "
			+ "    		 OR mp.siacTSoggetto.dataFineValidita > CURRENT_TIMESTAMP ) "
			+ " AND mp.siacTSoggetto.soggettoId = :soggettoId "
		
			+ " AND EXISTS (FROM mp.siacRModpagStatos mps " 
			+ " 			WHERE mps.dataCancellazione is NULL " 
			+ " 			AND ( mps.dataFineValidita is NULL or mps.dataFineValidita > CURRENT_TIMESTAMP) "
			+ " 			AND mps.siacDModpagStato.modpagStatoCode = 'VALIDO' "
			+ " )"
			+ " AND mp.siacDAccreditoTipo.accreditoTipoCode = :accreditoTipoCode"
			+ " ORDER BY mp.siacDAccreditoTipo.accreditoPriorita DESC ")
	List<SiacTModpag> findModPagValidaBySoggettoAndTipoAccredito(@Param("soggettoId") Integer soggettoId,@Param("accreditoTipoCode") String accreditoTipoCode);
	
	@Query("FROM SiacTModpag mp " 
			+ " WHERE mp.dataCancellazione IS NULL " 
			+ " AND mp.perStipendi = :perStipendi "
			+ " AND (mp.dataFineValidita IS NULL OR mp.dataFineValidita > CURRENT_TIMESTAMP) "
			+ " AND (mp.dataScadenza IS NULL OR mp.dataScadenza > CURRENT_TIMESTAMP) "
			+ " AND mp.siacTSoggetto.dataCancellazione IS NULL "
			+ " AND (mp.siacTSoggetto.dataFineValidita IS NULL OR mp.siacTSoggetto.dataFineValidita > CURRENT_TIMESTAMP ) "
			+ " AND mp.siacTSoggetto.soggettoId = :soggettoId "
			+ " AND EXISTS ( "
			+ "     FROM mp.siacRModpagStatos mps " 
			+ "     WHERE mps.dataCancellazione is NULL " 
			+ "     AND (mps.dataFineValidita IS NULL OR mps.dataFineValidita > CURRENT_TIMESTAMP) "
			+ "     AND mps.siacDModpagStato.modpagStatoCode = 'VALIDO' "
			+ " ) "
			+ " AND mp.siacDAccreditoTipo.accreditoTipoCode = :accreditoTipoCode "
			+ " ORDER BY mp.siacDAccreditoTipo.accreditoPriorita DESC ")
	List<SiacTModpag> findModPagValidaBySoggettoAndTipoAccreditoAndPerStipendi(@Param("soggettoId") Integer soggettoId,@Param("accreditoTipoCode") String accreditoTipoCode, @Param("perStipendi") Boolean perStipendi);
	
	@Query(" FROM SiacDAccreditoTipo dat "
			+ " WHERE dat.dataCancellazione IS NULL "
			+ " AND dat.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND dat.accreditoTipoCode = :accreditoTipoCode ")
	SiacDAccreditoTipo findSiacDAccreditoTipoByAccreditoTipoCodeAndEnteProprietarioId(@Param("accreditoTipoCode") String accreditoTipoCode, @Param("enteProprietarioId") Integer enteProprietarioId);

	@Query(" FROM SiacTAbi abi "
			+ " WHERE abi.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND abi.dataCancellazione IS NULL "
			+ " AND abi.dataFineValidita IS NULL "
			+ " AND abi.abiCode = :abiCode ")
	List<SiacTAbi> findAbiByCodice(@Param("abiCode") String abiCode, @Param("enteProprietarioId") Integer enteProprietarioId);
	
	@Query(" FROM SiacTCab cab "
			+ " WHERE cab.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND cab.dataCancellazione IS NULL "
			+ " AND cab.dataFineValidita IS NULL "
			+ " AND cab.cabCode = :cabCode "
			+ " AND cab.cabAbi = :cabAbi ")
	List<SiacTCab> findCabByCodice(@Param("cabAbi") String cabAbi, @Param("cabCode") String cabCode, @Param("enteProprietarioId") Integer enteProprietarioId);
	
	@Query(" SELECT rsm.siacRSoggettoRelaz "
			+ " FROM SiacRSoggrelModpag rsm "
			+ " WHERE rsm.dataCancellazione IS NULL "
			+ " AND rsm.siacRSoggettoRelaz.dataCancellazione IS NULL "
			+ " AND rsm.siacRSoggettoRelaz.siacTSoggetto1.soggettoId = :soggettoIdDa "
			+ " AND rsm.siacTModpag.modpagId = :modpagId ")
	List<SiacRSoggettoRelaz> findSiacRSoggettoRelazByModpagIdAndSoggettoIdDa(@Param("modpagId") Integer modpagId, @Param("soggettoIdDa") Integer soggettoIdDa);
	
	@Query(" SELECT rsm.siacRSoggettoRelaz "
			+ " FROM SiacRSoggrelModpag rsm "
			+ " WHERE rsm.dataCancellazione IS NULL "
			+ " AND rsm.siacRSoggettoRelaz.dataCancellazione IS NULL "
			+ " AND rsm.siacTModpag.modpagId = :modpagId "
			+ " AND ( rsm.siacRSoggettoRelaz.siacTSoggetto1.soggettoId = :soggettoIdDa "
			+ " 	OR EXISTS (FROM SiacRSoggettoRelaz rsedeSecond "
			+ " 		WHERE rsedeSecond.siacTSoggetto2 = rsm.siacRSoggettoRelaz.siacTSoggetto1.soggettoId "
			+ "         AND rsedeSecond.dataCancellazione IS NULL "
			+ "         AND rsedeSecond.siacDRelazTipo.relazTipoCode = 'SEDE_SECONDARIA' "
			+ "		)"
		    + " ) "
			)
	List<SiacRSoggettoRelaz> findSiacRSoggettoRelazByModpagIdAndSoggettoIdDaSedeSecondaria(@Param("modpagId") Integer modpagId, @Param("soggettoIdDa") Integer soggettoIdDa);
	
	@Query(" SELECT mp.siacDAccreditoTipo.accreditoTipoCode "
			+ " FROM SiacTModpag mp " 
			+ " WHERE mp.dataCancellazione IS NULL " 
			+ " AND (mp.dataFineValidita IS NULL OR mp.dataFineValidita > CURRENT_TIMESTAMP) "
			+ " AND mp.modpagId = :modpagId ")
	String findAccreditoTipoCodeByModPagId(@Param("modpagId") Integer modpagId);
	
	
}
