/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTEnteProprietario;

/**
 * The Interface SiacTEnteProprietarioRepository.
 */
public interface SiacTEnteProprietarioRepository extends JpaRepository<SiacTEnteProprietario, Integer>{
	
	
	/**
	 * Ottiene l'Ente SIAC a partire dal codice Ente del sistema esterno
	 * 
	 * @param extsysEnteCode
	 * @param extsysCode
	 * @return l'Ente del sistema esterno
	 */
	@Query(" SELECT rsee.siacTEnteProprietario "
			+ " FROM SiacRSistemaEsternoEnte rsee "
			+ " WHERE rsee.dataCancellazione IS NULL "
			+ " AND rsee.extsysEnteCode = :extsysEnteCode "
			+ " AND rsee.siacDSistemaEsterno.extsysCode = :extsysCode ")
	List<SiacTEnteProprietario> findByExtsysEnteCodeAndExtsysCode(@Param("extsysEnteCode") String extsysEnteCode, @Param("extsysCode") String extsysCode);
	
	
	
	/**
	 * Ottiene l'Ente a partire dal codice fiscale
	 */
	@Query("SELECT e FROM SiacTEnteProprietario e "
			+ " WHERE e.codiceFiscale=:codiceFiscale AND e.dataCancellazione IS NULL " +
			" AND e.dataFineValidita IS NULL ")
	SiacTEnteProprietario ricercaEnte(
			@Param("codiceFiscale") String codiceFiscale);

	
	/**
	 * Ottiene l'Ente del sistema esterno a partire dal codice Ente SIAC
	 * 
	 * @param extsysEnteCode
	 * @param extsysCode
	 * @return l'Ente del sistema esterno
	 */
	@Query(" SELECT rsee.extsysEnteCode "
			+ " FROM SiacRSistemaEsternoEnte rsee "
			+ " WHERE rsee.dataCancellazione IS NULL "
			+ " AND  rsee.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND rsee.siacDSistemaEsterno.extsysCode = :extsysCode ")
	List<String> findExtsysEnteCodeByEnteUidAndExtsysCode(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("extsysCode") String extsysCode);
	
	
	 /**
     * @deprecated
     * This method is no longer used.
     * <p> See {@link it.csi.siac.siaccorser.model.ParametroConfigurazioneEnteEnum}.
     */	
	@Deprecated
	@Query(" SELECT config.siacDConfigTipo.configTipoCode, config.configEnteValore "
			+ " FROM SiacTConfigEnte config "
			+ " WHERE config.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId"
			+ " AND config.dataCancellazione IS NULL"
			+ " AND ( :configTipoCodes IS NULL  "
			+ "  OR config.siacDConfigTipo.configTipoCode IN (:configTipoCodes)"
			+ " )"
	)
	List<Object[]> caricaConfigurazioniByEnte(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("configTipoCodes") List<String> configTipoCodes);
	
	
}
