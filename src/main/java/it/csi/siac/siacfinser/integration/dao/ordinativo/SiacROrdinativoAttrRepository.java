/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.ordinativo;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacROrdinativoAttrFin;

public interface SiacROrdinativoAttrRepository extends JpaRepository<SiacROrdinativoAttrFin, Integer> {
	String condizione = " ((dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND dataCancellazione IS NULL) ";

	@Query("FROM SiacROrdinativoAttrFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND " + condizione)
	public List<SiacROrdinativoAttrFin> findListaSiacROrdinativoAttrValidiByEnteId(@Param("enteProprietarioId") Integer enteProprietarioId,
			                                                                    @Param("dataInput") Timestamp  dataInput);
	
	@Query("FROM SiacROrdinativoAttrFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND " + 
	       "                               siacTOrdinativo.ordId = :idOrdinativo AND " + 
		   "                               siacTAttr.attrCode = :attrCode AND " + condizione)
	public List<SiacROrdinativoAttrFin> findListaSiacROrdinativoAttrValidiByEnteIdAndIdOrdinativoAndAttrCode(@Param("enteProprietarioId") Integer enteProprietarioId,
																							              @Param("idOrdinativo") Integer idOrdinativo,
																							              @Param("attrCode") String attrCode,
            																				              @Param("dataInput") Timestamp  dataInput);
	
	@Query("FROM SiacROrdinativoAttrFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND " + 
		       "                               siacTOrdinativo.ordId = :idOrdinativo AND " + condizione)
		public List<SiacROrdinativoAttrFin> findAllValidiByIdOrdinativo(@Param("enteProprietarioId") Integer enteProprietarioId,
																								              @Param("idOrdinativo") Integer idOrdinativo,
	            																				              @Param("dataInput") Timestamp  dataInput);
}