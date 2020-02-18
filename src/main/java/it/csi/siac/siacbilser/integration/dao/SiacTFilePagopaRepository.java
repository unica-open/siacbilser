/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTEnteProprietario;
import it.csi.siac.siacbilser.integration.entity.SiacTFilePagopa;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public interface SiacTFilePagopaRepository extends JpaRepository<SiacTFilePagopa, Integer> {
	
	@Query("FROM SiacTFilePagopa fp "
			+ " WHERE fp.siacTEnteProprietario=:siacTEnteProprietario "
			+ " AND fp.siacDFilePagopaStato.filePagopaStatoCode=:filePagopaStatoCode "
			+ " AND fp.dataCancellazione IS NULL ")
	public List<SiacTFilePagopa> findByStatoCode(
			@Param("siacTEnteProprietario") SiacTEnteProprietario siacTEnteProprietario, 
			@Param("filePagopaStatoCode") String filePagopaStatoCode
	);
	
	@Modifying
	@Query("UPDATE SiacTFilePagopa fp "
			+ " SET fp.siacDFilePagopaStato = ("
			+ "		SELECT fps FROM SiacDFilePagopaStato fps WHERE fps.filePagopaStatoCode=:filePagopaStatoCode "
			+ " 	AND fps.siacTEnteProprietario=fp.siacTEnteProprietario "
			+ "		AND fps.dataCancellazione IS NULL ) "
			+ " WHERE fp.filePagopaId=:filePagopaId")
	public void aggiornaStato(
			@Param("filePagopaId") Integer filePagopaId,
			@Param("filePagopaStatoCode") String filePagopaStatoCode
	);

	@Query("FROM SiacTFilePagopa fp "
			+ " WHERE fp.siacTEnteProprietario=:siacTEnteProprietario "
			+ " AND fp.filePagopaCode=:filePagopaCode ")
	public SiacTFilePagopa findByCode(
			@Param("siacTEnteProprietario") SiacTEnteProprietario siacTEnteProprietario, 
			@Param("filePagopaCode") String filePagopaCode
	);
	
	
	@Query("FROM SiacTFilePagopa fp "
			+ " WHERE fp.siacTEnteProprietario=:siacTEnteProprietario "
			+ " AND fp.filePagopaIdFlusso=:filePagopaIdFlusso")
	SiacTFilePagopa findByFlussoId(
			@Param("siacTEnteProprietario") SiacTEnteProprietario siacTEnteProprietario, 
			@Param("filePagopaIdFlusso") String filePagopaIdFlusso
	);
}
