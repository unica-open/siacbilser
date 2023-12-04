/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.movgest;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacTCronopElemFin;

public interface SiacTCronopElemFinRepository extends JpaRepository<SiacTCronopElemFin, Integer> {
	
	//SIAC-7610
	@Query(value =
			" SELECT DISTINCT stce.* " + 
			" FROM siac_t_programma stp " +
			" JOIN siac_r_movgest_ts_programma srmtp on ( stp.programma_id = srmtp.programma_id ) " +
			" JOIN siac_t_cronop stc on ( srmtp.programma_id = stc.programma_id) " +
			" JOIN siac_t_ente_proprietario step on ( stc.ente_proprietario_id = step.ente_proprietario_id  and step.ente_proprietario_id = :enteProprietarioUid )" +
			" JOIN siac_t_cronop_elem stce on ( stc.cronop_id = stce.cronop_id ) " +
			" JOIN siac_d_bil_elem_tipo sdbet on ( stce.elem_tipo_id = sdbet.elem_tipo_id ) " +
			" WHERE stc.cronop_id = :cronopId " + 
			" AND sdbet.elem_tipo_code = :tipoCapitolo ",
			nativeQuery = true)
	List<SiacTCronopElemFin> findSiacTCronopElemByProgettoIdAndTipoCapitolo(@Param("cronopId") Integer cronopId, 
			@Param("tipoCapitolo") String tipoCapitolo,
			@Param("enteProprietarioUid") Integer enteProprietarioUid);
	
}