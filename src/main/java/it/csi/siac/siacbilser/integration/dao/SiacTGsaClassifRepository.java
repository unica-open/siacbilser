/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTGsaClassif;

/**
 * The Interface SiacTPdceContoRepository.
 */
public interface SiacTGsaClassifRepository extends JpaRepository<SiacTGsaClassif, Integer> {
	
	
	/**
	 * Find conti figlio con figli.
	 *
	 * @param gsaClassifCode the gsa classif code
	 * @param enteProprietarioId the ente proprietario id
	 * @return the list
	 */
	@Query(" SELECT c FROM SiacTGsaClassif c" +
			" WHERE c.dataCancellazione IS NULL " + 
			" AND c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "+
			" AND c.gsaClassifCode = :gsaClassifCode " + 
			" AND EXISTS( " +
			"     FROM SiacRGsaClassifStato rgcs "+ 
			"     WHERE rgcs.dataCancellazione IS NULL " +
			"     AND rgcs.siacTGsaClassif = c " +
			"     AND rgcs.siacDGsaClassifStato.gsaClassifStatoCode = 'V' " + 
			" ) ")
	SiacTGsaClassif findSiacTGsaClassifValidoByCode(@Param("gsaClassifCode") String gsaClassifCode, @Param("enteProprietarioId") Integer enteProprietarioId);
	
	/**
	 * Find siac T gsa classif by id.
	 *
	 * @param gsaClassifId the gsa classif id
	 * @param enteProprietarioId the ente proprietario id
	 * @return the siac T gsa classif
	 */
	@Query(" SELECT c FROM SiacTGsaClassif c" +
			" WHERE c.dataCancellazione IS NULL " + 
			" AND c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "+
			" AND c.gsaClassifId = :gsaClassifId "+
			" AND EXISTS( " +
			"     FROM SiacRGsaClassifStato rgcs "+ 
			"     WHERE rgcs.dataCancellazione IS NULL " +
			"     AND rgcs.siacTGsaClassif = c " +
			"     AND rgcs.siacDGsaClassifStato.gsaClassifStatoCode = 'V' " + 
			" ) "
			)
	SiacTGsaClassif findSiacTGsaClassifValidoById(@Param("gsaClassifId") Integer gsaClassifId, @Param("enteProprietarioId") Integer enteProprietarioId);
	
	/**
	 * Find siac T gsa classif figli by id padre.
	 *
	 * @param gsaClassifId the gsa classif id
	 * @return the list
	 */
	@Query(" FROM SiacTGsaClassif c" +
			" WHERE c.dataCancellazione IS NULL " + 
			" AND c.siacTGsaClassifPadre.gsaClassifId = :gsaClassifId " +
			" AND EXISTS( " +
			"     FROM SiacRGsaClassifStato rgcs "+ 
			"     WHERE rgcs.dataCancellazione IS NULL " +
			"     AND rgcs.siacTGsaClassif = c " +
			"     AND rgcs.siacDGsaClassifStato.gsaClassifStatoCode = :gsaClassifStatoCode " + 
			" ) ")
	List<SiacTGsaClassif> findSiacTGsaClassifFigliByIdPadreAndGsaClassifStatoCode(@Param("gsaClassifId") Integer gsaClassifId, @Param("gsaClassifStatoCode") String gsaClassifStatoCode);
	
	/**
	 * Find siac T gsa classif figli by id padre.
	 *
	 * @param gsaClassifId the gsa classif id
	 * @return the list
	 */
	@Query(" SELECT c FROM SiacTGsaClassif c" +
			" WHERE c.dataCancellazione IS NULL " + 
			" AND c.siacTGsaClassifPadre.gsaClassifId = :gsaClassifId ")
	List<SiacTGsaClassif> findSiacTGsaClassifFigliByIdPadre(@Param("gsaClassifId") Integer gsaClassifId);
	
	/**
	 * Count siac T gsa classif figli validi by id padre.
	 *
	 * @param gsaClassifId the gsa classif id
	 * @return the long
	 */
	@Query(" SELECT COALESCE( COUNT(c), 0) " +
			" FROM SiacTGsaClassif c" +
			" WHERE c.dataCancellazione IS NULL " + 
			" AND  c.siacTGsaClassifPadre.gsaClassifId = :gsaClassifId " + 
			" AND EXISTS( " +
			"     FROM SiacRGsaClassifStato rgcs "+ 
			"     WHERE rgcs.dataCancellazione IS NULL " +
			"     AND rgcs.siacTGsaClassif = c " +
			"     AND rgcs.siacDGsaClassifStato.gsaClassifStatoCode = 'V' " + 
			" )")
	Long countSiacTGsaClassifFigliValidiByIdPadre(@Param("gsaClassifId") Integer gsaClassifId);	

	
	
	
}
