/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.soggetto;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacDAccreditoTipoFin;

public interface SiacDTipoAccreditoRepository extends JpaRepository<SiacDAccreditoTipoFin, Integer>{
	
	String condizione = " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND  dataCancellazione IS NULL ) ";
	
	String condizioneConJoinGruppo = " ( (d.dataInizioValidita < :dataInput)  AND (d.dataFineValidita IS NULL OR :dataInput < d.dataFineValidita) AND  d.dataCancellazione IS NULL ) ";
	
	
	@Query("from SiacDAccreditoTipoFin where siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND "+condizione)
	public List<SiacDAccreditoTipoFin> findTipoAccrediti(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("dataInput") Timestamp  dataInput);
	
	
	@Query("from SiacDAccreditoTipoFin where siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND "+condizione+" AND accreditoTipoCode = :code ")
	public List<SiacDAccreditoTipoFin> findTipoAccreditoValidoByCode(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("code") String code,@Param("dataInput") Timestamp  dataInput);
 
	@Query("from SiacDAccreditoTipoFin where siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND "+condizione + "ORDER BY accreditoTipoDesc")
	public List<SiacDAccreditoTipoFin> findTipoAccreditiOrdered(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("dataInput") Timestamp  dataInput);
	
	
	
//	select d from siac.Siac_D_Accredito_Tipo d, siac.siac_d_accredito_gruppo g 
//	where g.ente_Proprietario_Id = d.ente_Proprietario_Id and *
//	g.accredito_gruppo_id = d.accredito_gruppo_id and
//	d.ente_Proprietario_Id = 1 AND d.accredito_Tipo_Code = 'CO'
	
	@Query("select d from SiacDAccreditoTipoFin d, SiacDAccreditoGruppoFin g where "+
	       "g.siacTEnteProprietario.enteProprietarioId = d.siacTEnteProprietario.enteProprietarioId AND "+
		   "g.accreditoGruppoId = d.siacDAccreditoGruppo.accreditoGruppoId AND "+
	       "d.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND "+
		   condizioneConJoinGruppo+" AND d.accreditoTipoCode = :code ")
	public List<SiacDAccreditoTipoFin> findTipoAccreditoValidoPerGruppoByCode(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("code") String code,@Param("dataInput") Timestamp  dataInput);
 
	
}
