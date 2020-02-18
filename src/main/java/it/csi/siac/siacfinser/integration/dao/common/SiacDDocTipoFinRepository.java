/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacDDocTipoFin;

public interface SiacDDocTipoFinRepository extends JpaRepository<SiacDDocTipoFin, Integer>{
	
	String condizione = " ((dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND dataCancellazione IS NULL) ";
	
	@Query("FROM SiacDDocTipoFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND " +
	       "                        docTipoCode = :codiceTipoDocumento AND siacDDocFamTipo.docFamTipoCode = :codiceFamiglia AND " + condizione)
	public SiacDDocTipoFin findDDocTipoValidoByEnteAndCodAndCodFam(@Param("enteProprietarioId") Integer enteProprietarioId,
			                                                    @Param("codiceTipoDocumento") String codiceTipoDocumento,
			                                                    @Param("codiceFamiglia") String codiceFamiglia,
			                                                    @Param("dataInput") Timestamp  dataInput);
	
	
	
	@Query("FROM SiacDDocTipoFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND " +
		       "                    siacDDocFamTipo.docFamTipoCode = :codiceFamiglia AND " + condizione + " ORDER BY docTipoCode ")
	public List<SiacDDocTipoFin> findDDocTipoValidiByEnteAndCodFam(@Param("enteProprietarioId") Integer enteProprietarioId,
			                                                    @Param("codiceFamiglia") String codiceFamiglia,
			                                                    @Param("dataInput") Timestamp  dataInput);
	
}