/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.movgest;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsFin;

public interface SiacTMovgestTsRepository extends JpaRepository<SiacTMovgestTsFin, Integer> {
	
	String condizione =  " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND dataCancellazione IS NULL ) ";
	String orderBy =  " ORDER BY TO_NUMBER(movgestTsCode, '999999999999') ";
	
	@Query("FROM SiacTMovgestTsFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND "+condizione)
	public List<SiacTMovgestTsFin> findListaSiacTMovgestTs(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("dataInput") Timestamp  dataInput);
	
	@Query("FROM SiacTMovgestTsFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND movgestTsIdPadre = :idPadre AND " + condizione + orderBy)
	public List<SiacTMovgestTsFin> findListaSiacTMovgestTsFigli(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("dataInput") Timestamp  dataInput, @Param("idPadre") Integer idPadre);
	
	@Query("FROM SiacTMovgestTsFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND siacTMovgest.movgestId = :idMovgest AND " +
			" movgestTsIdPadre IS null AND "+condizione)
	public List<SiacTMovgestTsFin> findMovgestTsByMovgest(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("dataInput") Timestamp  dataInput, @Param("idMovgest") Integer idMovgest);
	
	@Query("FROM SiacTMovgestTsFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND movgestTsId = :idMovgestTs AND " +condizione)
	public List<SiacTMovgestTsFin> findSubMovgestTsByMovgest(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("dataInput") Timestamp  dataInput, @Param("idMovgestTs") Integer idMovgest);
	
	@Query("FROM SiacTMovgestTsFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND movgestTsIdPadre = :idPadre AND movgestTsCode = :code AND "+condizione)
	public List<SiacTMovgestTsFin> findFiglioByCode(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("dataInput") Timestamp  dataInput, @Param("idPadre") Integer idPadre,@Param("code") String code);
	
	@Query("FROM SiacTMovgestTsFin WHERE movgestTsId IN (:listaId) AND " + condizione)
	public List<SiacTMovgestTsFin> findValidiById(@Param("listaId") List<Integer> listaId,@Param("dataInput") Timestamp  dataInput);
	
	@Query("FROM SiacTMovgestTsFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND movgestTsId = :idMovgest AND " +condizione)
	public SiacTMovgestTsFin findMovgestTsByMovgestTsId(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("dataInput") Timestamp  dataInput, @Param("idMovgest") Integer idMovgest);
	
	@Query("FROM SiacTMovgestTsFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND siacTMovgest.movgestId= :idMovgest AND movgestTsCode = :code AND"
			+ " movgestTsIdPadre is not null AND " +condizione)
	public List<SiacTMovgestTsFin> findSubMovgestTsByCodeAndMovgestId(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("dataInput") Timestamp  dataInput, @Param("idMovgest") Integer idMovgest, @Param("code") String code);

	/**
	 * Find siac t movgest testata by siac t movgest id.
	 *
	 * @param movgestId the movgest id
	 * @return the list
	 */
	@Query("FROM SiacTMovgestTsFin m "
			+ " WHERE m.siacTMovgest.movgestId = :movgestId "
			+ " AND m.siacDMovgestTsTipo.movgestTsTipoCode = 'T' "
			+ " AND m.dataCancellazione IS NULL "
			+ " AND m.dataFineValidita IS NULL ")
	List<SiacTMovgestTsFin> findSiacTMovgestTestataBySiacTMovgestId(@Param("movgestId") Integer movgestId);
	
	
	@Query("FROM SiacTMovgestTsFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND siacTMovgest.movgestId = :idMovgest AND siacTMovgest.dataCancellazione IS NULL ")
	public List<SiacTMovgestTsFin> findMovgestTsByTMovgest(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("idMovgest") Integer idMovgest);
	
}