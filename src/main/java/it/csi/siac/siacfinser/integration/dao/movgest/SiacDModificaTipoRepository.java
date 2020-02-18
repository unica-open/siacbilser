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

import it.csi.siac.siacfinser.integration.entity.SiacDModificaTipoFin;

public interface SiacDModificaTipoRepository extends JpaRepository<SiacDModificaTipoFin, Integer> {
	
	String condizione = " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND dataCancellazione IS NULL ) ";

	@Query("FROM SiacDModificaTipoFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND " + condizione )
	public List<SiacDModificaTipoFin> findListaSiacDModificaTipo(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("dataInput") Timestamp  dataInput);
	
	@Query("FROM SiacDModificaTipoFin WHERE modTipoCode = :code AND siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND "+condizione)
	public List<SiacDModificaTipoFin> findValidoByCode(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("dataInput") Timestamp  dataInput,@Param("code") String code);

	@Query("FROM SiacDModificaTipoFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND " + condizione + " ORDER BY modTipoDesc ASC ")
	public List<SiacDModificaTipoFin> findValidiByEnte(@Param("enteProprietarioId") Integer enteProprietarioId , @Param("dataInput") Timestamp dataInput);

	@Query("FROM SiacDModificaTipoFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND " +
			" (upper(modTipoDesc) like 'ROR%' or upper(modTipoDesc) like 'ECO%') AND " + condizione + " ORDER BY modTipoDesc ASC ")
	public List<SiacDModificaTipoFin> findListaMotiviRORValidiByEnte(@Param("enteProprietarioId") Integer enteProprietarioId , @Param("dataInput") Timestamp dataInput);

}