/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SirfelDRegimeFiscale;
import it.csi.siac.siacbilser.integration.entity.SirfelDRegimeFiscalePK;
import it.csi.siac.siacbilser.integration.entity.SirfelTPrestatore;
import it.csi.siac.siacbilser.integration.entity.SirfelTPrestatorePK;

/**
 * The Interface SirfelTPrestatoreRepository.
 */
public interface SirfelTPrestatoreRepository extends JpaRepository<SirfelTPrestatore, SirfelTPrestatorePK> {
	
	@Query(" FROM SirfelTPrestatore tp "
			+ " WHERE tp.codicePaese = :codicePaese "
			+ " AND tp.codicePrestatore = :codicePrestatore " 
			+ " AND tp.id.enteProprietarioId = :enteProprietarioId ")
	public List<SirfelTPrestatore> findByCodicePaeseAndCodicePrestatoreAndEnte(@Param("codicePaese") String codicePaese, @Param("codicePrestatore") String codicePrestatore,
			@Param("enteProprietarioId") Integer enteProprietarioId);
	
	@Query(" FROM SirfelDRegimeFiscale drf "
			+ " WHERE drf.id = :id")
	public SirfelDRegimeFiscale findRegimeFiscaleBySirfelDRegimeFiscalePK(@Param("id") SirfelDRegimeFiscalePK id);
	
	@Query(" SELECT COALESCE(MAX(tp.id.idPrestatore), 0) "
			+ " FROM SirfelTPrestatore tp "
			+ " WHERE tp.id.enteProprietarioId = :enteProprietarioId ")
	public Integer getMaxIdPrestatoreByEnteProprietarioId(@Param("enteProprietarioId") Integer enteProprietarioId);
}
