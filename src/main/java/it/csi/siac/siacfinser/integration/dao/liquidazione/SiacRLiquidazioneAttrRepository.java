/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.liquidazione;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacRLiquidazioneAttrFin;

public interface SiacRLiquidazioneAttrRepository extends JpaRepository<SiacRLiquidazioneAttrFin, Integer> {
	String condizione = " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND  dataCancellazione IS NULL ) ";
	
	@Query("FROM SiacRLiquidazioneAttrFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND" +
			" siacTLiquidazione.liqId = :idLiq AND "+condizione)
	public List<SiacRLiquidazioneAttrFin> findByEnteAndLiquidazione(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("dataInput") Timestamp  dataInput, @Param("idLiq") Integer idLiq);

	@Query("FROM SiacRLiquidazioneAttrFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND" +
			" siacTLiquidazione.liqId = :idLiq AND siacTAttr.attrCode = :code AND "+condizione)
	public List<SiacRLiquidazioneAttrFin> findValidoByILiqAndCode(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("dataInput") Timestamp  dataInput, @Param("idLiq") Integer idLiq, @Param("code") String code);
	
	@Query("FROM SiacRLiquidazioneAttrFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND" +
			" siacTLiquidazione.liqId = :idLiq AND "+condizione)
	public List<SiacRLiquidazioneAttrFin> findAllValidiByILiq(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("dataInput") Timestamp  dataInput, @Param("idLiq") Integer idLiq);
}