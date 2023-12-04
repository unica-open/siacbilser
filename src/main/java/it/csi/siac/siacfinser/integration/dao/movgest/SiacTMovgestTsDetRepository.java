/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.movgest;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsDetFin;

@Repository
public interface SiacTMovgestTsDetRepository extends JpaRepository<SiacTMovgestTsDetFin, Integer>  {
	
	
	String condizione = " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND  dataCancellazione IS NULL ) ";
	
	String condizioneDet = " det.dataFineValidita IS NULL  AND det.dataCancellazione IS NULL ";
	
	String selectFindImporto = "SELECT det.movgestTsDetImporto FROM SiacTMovgestTsDetFin det, SiacDMovgestTsDetTipoFin tipo "+
		      "WHERE det.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "+
		       "AND det.siacDMovgestTsDetTipo.movgestTsDetTipoId = tipo.movgestTsDetTipoId "+
		       "AND tipo.movgestTsDetTipoCode = :tipoImporto "+
		       "AND  det.siacTMovgestT.movgestTsId = :idMovgestTsDet AND "+condizioneDet;
	
	@Query("FROM SiacTMovgestTsDetFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
			" AND siacDMovgestTsDetTipo.movgestTsDetTipoCode = :tipoCode AND siacTMovgestT.movgestTsId = :idMovgestTs AND "+condizione)
	public List<SiacTMovgestTsDetFin> findValidoByTipo(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("dataInput") Timestamp  dataInput
			,@Param("tipoCode") String tipoCode, @Param("idMovgestTs") Integer idMovgestTs);
	
	@Query("FROM SiacTMovgestTsDetFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
			" AND siacTMovgestT.movgestTsId = :idMovgestTs AND "+condizione)
	public List<SiacTMovgestTsDetFin> findValidiByMovgestTs(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("dataInput") Timestamp  dataInput
			, @Param("idMovgestTs") Integer idMovgestTs);
	
	@Query(selectFindImporto)
	public BigDecimal findImporto(@Param("enteProprietarioId") Integer enteProprietarioId,			                             
								         @Param("tipoImporto") String tipoImporto, 
								         @Param("idMovgestTsDet") Integer idMovgestTsDet);
	//SIAC-8675
	@Query(selectFindImporto)
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public BigDecimal findImportoOutsideTransaction(@Param("enteProprietarioId") Integer enteProprietarioId,			                             
									         @Param("tipoImporto") String tipoImporto, 
									         @Param("idMovgestTsDet") Integer idMovgestTsDet);
	
}
