/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.ordinativo;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacROrdinativoClassFin;

public interface SiacROrdinativoClassRepository extends JpaRepository<SiacROrdinativoClassFin, Integer>  {
	
	
	String condizione = " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND  dataCancellazione IS NULL ) ";
	
	String condizioneInJoin = " ( (sr.dataInizioValidita < :dataInput)  AND (sr.dataFineValidita IS NULL OR :dataInput < sr.dataFineValidita) AND  sr.dataCancellazione IS NULL ) ";
	
	@Query("FROM SiacROrdinativoClassFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND " + condizione)
	public List<SiacROrdinativoClassFin> findListaSiacROrdinativoClassByEnteValidi(@Param("enteProprietarioId") Integer enteProprietarioId,
			                                                                    @Param("dataInput") Timestamp  dataInput);
	
	@Query("FROM SiacROrdinativoClassFin " + 
		   " WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND " +
		   " siacTClass.siacDClassTipo.classifTipoId = :idClassTipo AND " + 
		   " siacTOrdinativo.ordId = :idOrdinativo AND " + condizione)
	public List<SiacROrdinativoClassFin> findByTipoAndEnteAndOrdinativoId(@Param("enteProprietarioId") Integer enteProprietarioId,
			                                                           @Param("dataInput") Timestamp  dataInput,
			                                                           @Param("idClassTipo") Integer idClassTipo,
			                                                           @Param("idOrdinativo") Integer idOrdinativo);
	
	@Query("FROM SiacROrdinativoClassFin " + 
			   " WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND " +
			   " siacTOrdinativo.ordId = :idOrdinativo AND " + condizione)
		public List<SiacROrdinativoClassFin> findAllValidiByOrdinativoId(@Param("enteProprietarioId") Integer enteProprietarioId,
				                                                           @Param("dataInput") Timestamp  dataInput,
				                                                           @Param("idOrdinativo") Integer idOrdinativo);
	
	@Query(" SELECT sr FROM SiacROrdinativoClassFin sr, SiacTClassFin st " + 
		   " WHERE sr.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND " +
		   " st.siacDClassTipo.classifTipoCode IN (:classifTipoCodes)  AND " + 
		   " sr.siacTClass.classifId = st.classifId AND " + 
		   " sr.siacTOrdinativo.ordId = :idOrdinativo AND " + condizioneInJoin)
	public List<SiacROrdinativoClassFin> findByTipoCodesAndEnteAndOrdinativoId(@Param("enteProprietarioId") Integer enteProprietarioId,
																			@Param("dataInput") Timestamp  dataInput,
																			@Param("classifTipoCodes") List<String> classifTipoCodes,
																			@Param("idOrdinativo") Integer idOrdinativo);
	
	@Modifying
	@Query(" UPDATE SiacROrdinativoClassFin oc "
			+ " SET oc.dataCancellazione=CURRENT_TIMESTAMP, "
			+ " oc.dataModifica=CURRENT_TIMESTAMP,"
			+ " oc.loginOperazione=:loginOperazione " 
			+ " WHERE oc.siacTOrdinativo.ordId=:idOrdinativo "
			+ " AND EXISTS "
			+ " 	(SELECT 1 FROM SiacTClassFin c "
			+ "		 WHERE c=oc.siacTClass "
			+ "      AND c.siacDClassTipo.classifTipoCode=:idClassTipoCodice) ")
	public void removeValidSiacROrdinativoClass(
		@Param("idOrdinativo") Integer idOrdinativo,
		@Param("idClassTipoCodice") String idClassTipoCodice,
		@Param("loginOperazione") String loginOperazione
    );
}













