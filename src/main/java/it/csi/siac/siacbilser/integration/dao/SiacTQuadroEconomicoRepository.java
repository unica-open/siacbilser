/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTQuadroEconomico;

/**
 * The Interface SiacTPdceContoRepository.
 */
public interface SiacTQuadroEconomicoRepository extends JpaRepository<SiacTQuadroEconomico, Integer> {
	
	
	/**
	 * Find conti figlio con figli.
	 *
	 * @param quadroEconomicoCode the quadro Economico code
	 * @param enteProprietarioId the ente proprietario id
	 * @return the list
	 */
	@Query(" SELECT c FROM SiacTQuadroEconomico c" +
			" WHERE c.dataCancellazione IS NULL " + 
			" AND c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "+
			" AND c.quadroEconomicoCode = :quadroEconomicoCode " + 
			" AND EXISTS( " +
			"     FROM SiacRQuadroEconomicoStato rgcs "+ 
			"     WHERE rgcs.dataCancellazione IS NULL " +
			"     AND rgcs.siacTQuadroEconomico = c " +
			"     AND rgcs.siacDQuadroEconomicoStato.quadroEconomicoStatoCode = 'V' " + 
			" ) ")
	List<SiacTQuadroEconomico> findSiacTQuadroEconomicoValidoByCode(@Param("quadroEconomicoCode") String quadroEconomicoCode, @Param("enteProprietarioId") Integer enteProprietarioId);
	
	/**
	 * Find conti figlio con figli.
	 *
	 * @param quadroEconomicoCode the quadro Economico code
	 * @param enteProprietarioId the ente proprietario id
	 * @return the list
	
	@Query(" SELECT c FROM SiacTQuadroEconomico c" +
			" WHERE c.dataCancellazione IS NULL " + 
			" AND c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "+
			" AND c.quadroEconomicoCode = :quadroEconomicoCode " + 
			" AND c.siacDQuadroEconomicoParte.parteCode = :parteCode " + 
			
			" AND c.siacTQuadroEconomicoPadre.quadroEconomicoId = :quadroEconomicoPadreId " + 

			" AND EXISTS( " +
			"     FROM SiacRQuadroEconomicoStato rgcs "+ 
			"     WHERE rgcs.dataCancellazione IS NULL " +
			"     AND rgcs.siacTQuadroEconomico = c " +
			"     AND rgcs.siacDQuadroEconomicoStato.quadroEconomicoStatoCode = 'V' " + 
			" ) ")
	SiacTQuadroEconomico findSiacTQuadroEconomicoValidoByCodeAndParte(@Param("quadroEconomicoCode") String quadroEconomicoCode,@Param("parteCode") String parteCode, @Param("quadroEconomicoPadreId") Integer quadroEconomicoPadreId , @Param("enteProprietarioId") Integer enteProprietarioId);
	 */
	
	/**
	 * Find siac T quadro Economico by id.
	 *
	 * @param quadroEconomicoId the quadro Economico id
	 * @param enteProprietarioId the ente proprietario id
	 * @return the siac T quadro Economico
	 */
	@Query(" SELECT c FROM SiacTQuadroEconomico c" +
			" WHERE c.dataCancellazione IS NULL " + 
			" AND c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "+
			" AND c.quadroEconomicoId = :quadroEconomicoId "+
			" AND EXISTS( " +
			"     FROM SiacRQuadroEconomicoStato rgcs "+ 
			"     WHERE rgcs.dataCancellazione IS NULL " +
			"     AND rgcs.siacTQuadroEconomico = c " +
			"     AND rgcs.siacDQuadroEconomicoStato.quadroEconomicoStatoCode = 'V' " + 
			" ) "
			)
	SiacTQuadroEconomico findSiacTQuadroEconomicoValidoById(@Param("quadroEconomicoId") Integer quadroEconomicoId, @Param("enteProprietarioId") Integer enteProprietarioId);
	
	/**
	 * Find siac T quadro Economico figli by id padre.
	 *
	 * @param quadroEconomicoId the quadro Economico id
	 * @return the list
	 */
	@Query(" FROM SiacTQuadroEconomico c" +
			" WHERE c.dataCancellazione IS NULL " + 
			" AND c.siacTQuadroEconomicoPadre.quadroEconomicoId = :quadroEconomicoId " +
			" AND EXISTS( " +
			"     FROM SiacRQuadroEconomicoStato rgcs "+ 
			"     WHERE rgcs.dataCancellazione IS NULL " +
			"     AND rgcs.siacTQuadroEconomico = c " +
			"     AND rgcs.siacDQuadroEconomicoStato.quadroEconomicoStatoCode = :quadroEconomicoStatoCode " + 
			" ) ")
	List<SiacTQuadroEconomico> findSiacTQuadroEconomicoFigliByIdPadreAndQuadroEconomicoStatoCode(@Param("quadroEconomicoId") Integer quadroEconomicoId, @Param("quadroEconomicoStatoCode") String quadroEconomicoStatoCode);
	
	/**
	 * Find siac T quadro Economico figli by id padre.
	 *
	 * @param quadroEconomicoId the quadro Economico id
	 * @return the list
	 */
	@Query(" SELECT c FROM SiacTQuadroEconomico c" +
			" WHERE c.dataCancellazione IS NULL " + 
			" AND c.siacTQuadroEconomicoPadre.quadroEconomicoId = :quadroEconomicoId ")
	List<SiacTQuadroEconomico> findSiacTQuadroEconomicoFigliByIdPadre(@Param("quadroEconomicoId") Integer quadroEconomicoId);
	
	/**
	 * Count siac T quadro Economico figli validi by id padre.
	 *
	 * @param quadroEconomicoId the quadro Economico id
	 * @return the long
	 */
	@Query(" SELECT COALESCE( COUNT(c), 0) " +
			" FROM SiacTQuadroEconomico c" +
			" WHERE c.dataCancellazione IS NULL " + 
			" AND  c.siacTQuadroEconomicoPadre.quadroEconomicoId = :quadroEconomicoId " + 
			" AND EXISTS( " +
			"     FROM SiacRQuadroEconomicoStato rgcs "+ 
			"     WHERE rgcs.dataCancellazione IS NULL " +
			"     AND rgcs.siacTQuadroEconomico = c " +
			"     AND rgcs.siacDQuadroEconomicoStato.quadroEconomicoStatoCode = 'V' " + 
			" )")
	Long countSiacTQuadroEconomicoFigliValidiByIdPadre(@Param("quadroEconomicoId") Integer quadroEconomicoId);	

	
	
	
}
