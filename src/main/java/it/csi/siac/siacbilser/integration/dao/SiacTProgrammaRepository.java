/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacDProgrammaStato;
import it.csi.siac.siacbilser.integration.entity.SiacDProgrammaTipo;
import it.csi.siac.siacbilser.integration.entity.SiacTBil;
import it.csi.siac.siacbilser.integration.entity.SiacTProgramma;

// TODO: Auto-generated Javadoc
/**
 * Repository JPA per il SiacTProgramma.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 04/02/2014
 *
 */
public interface SiacTProgrammaRepository extends JpaRepository<SiacTProgramma, Integer> {

	/**
	 * Ottiene un Progetto a partire dal codice, dallo Stato Operativo e dall'uid dell'Ente Proprietario.
	 * 
	 * @param progettoCodice               il codice del Progetto
	 * @param progettoStatoOperativoCodice il codice dello stato operativo del Progetto
	 * @param enteProprietarioId           l'uid dell'Ente Proprietario
	 * 
	 * @return il SiacTProgramma associato
	 */
	@Query( "SELECT p " +
			" FROM SiacTProgramma p " +
			// SIAC-4845
			" WHERE UPPER(p.programmaCode) = UPPER(:programmaCode) " +
			" AND p.dataCancellazione IS NULL " +
			" AND p.dataInizioValidita < CURRENT_TIMESTAMP " +
			" AND ( " +
			"    p.dataFineValidita IS NULL " +
			"    OR p.dataFineValidita > CURRENT_TIMESTAMP " +
			" ) " +
			" AND p.siacDProgrammaTipo.programmaTipoCode = :programmaTipoCode " +
			" AND p.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
			" AND p.siacTBil.bilId = :bilId " + 
			" AND EXISTS ( " +
			" 	SELECT ps " +
			" 	FROM p.siacRProgrammaStatos ps " +
			"	WHERE ps.siacDProgrammaStato.programmaStatoCode = :programmaStatoCode " +
			"	AND ps.dataCancellazione IS NULL " +
			" )")
	SiacTProgramma findByCodiceAndStatoOperativoProgettoAndEnteProprietarioId(@Param("programmaCode") String progettoCodice, 
			@Param("programmaStatoCode") String progettoStatoOperativoCodice,
			@Param("programmaTipoCode") String progettoTipoCode,
			@Param("bilId") Integer bilId,
			@Param("enteProprietarioId") Integer enteProprietarioId);

	/**
	 * Ottiene il SiacDProgrammaStato associato ad un SiacTProgramma con dato uid.
	 * 
	 * @param uid l'uid del SiacTProgramma
	 * 
	 * @return il SiacDProgrammaStato
	 */
	@Query( "SELECT s " +
			" FROM SiacDProgrammaStato s " +			
			" WHERE EXISTS ( " +
			" 	SELECT sp " +
			"	FROM s.siacRProgrammaStatos sp " +
			"	WHERE sp.siacTProgramma.programmaId = :programmaId " +
			" 	AND sp.dataCancellazione IS NULL " +
			//SIAC-7591 si setta a <=
			"   AND sp.dataInizioValidita <= CURRENT_TIMESTAMP " +
			"   AND ( " +
			"      sp.dataFineValidita IS NULL " +
			"      OR sp.dataFineValidita > CURRENT_TIMESTAMP " +
			"   ) " +
			" ) "+
			" AND s.dataCancellazione IS NULL ")
	SiacDProgrammaStato findStatoByIdProgetto(@Param("programmaId") Integer uid);
	
	@Query(" SELECT c.siacDProgrammaTipo FROM SiacTProgramma c "
			+ " WHERE c.programmaId = :programmaId "
			+ " AND c.dataCancellazione IS NULL ")
	SiacDProgrammaTipo findSiacDProgrammaTipoByCronopId(@Param("programmaId") Integer programmaId);
	
	@Query( "SELECT COALESCE(rpa.numerico, 0) "
			+" FROM SiacRProgrammaAttr rpa " 			
			+ " WHERE rpa.dataCancellazione IS NULL "
			+ " AND rpa.siacTAttr.attrCode = :attrCode "
			+ " AND rpa.siacTProgramma.programmaId = :programmaId "
			)	
	BigDecimal findBigDecimalAttrByIdProgetto(@Param("programmaId") Integer uid, @Param("attrCode") String attrCode);

	@Query(" SELECT c.siacTBil FROM SiacTProgramma c "
			+ " WHERE c.programmaId = :programmaId "
			+ " AND c.dataCancellazione IS NULL ")
	SiacTBil findBilancioProgetto(@Param("programmaId") Integer programmaId);

}
