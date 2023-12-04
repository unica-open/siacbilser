/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.carta;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacRCartacontDetModpagFin;

public interface SiacRCartacontDetModpagRepository extends JpaRepository<SiacRCartacontDetModpagFin, Integer> {
	String condizione = " ((dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND dataCancellazione IS NULL) ";
	
	@Query("FROM SiacRCartacontDetModpagFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND " + 
		       "                               siacTCartacontDet.cartacDetId = :idDetCarta AND " + 
			   "                               siacTModpag.modpagId = :idModPag AND " + condizione)
	public List<SiacRCartacontDetModpagFin> findValidoByIdDetCartaAndIdModPag(@Param("enteProprietarioId") Integer enteProprietarioId,
																	       @Param("idDetCarta") Integer idDetCarta,
																	       @Param("idModPag") Integer idModPag,
																	       @Param("dataInput") Timestamp  dataInput);
	
	@Query("FROM SiacRCartacontDetModpagFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND " + 
		       "                               siacTCartacontDet.cartacDetId = :idDetCarta AND "+ condizione)
	public List<SiacRCartacontDetModpagFin> findValidoByIdDetCarta(@Param("enteProprietarioId") Integer enteProprietarioId,
															       @Param("idDetCarta") Integer idDetCarta,
															       @Param("dataInput") Timestamp  dataInput);
}
