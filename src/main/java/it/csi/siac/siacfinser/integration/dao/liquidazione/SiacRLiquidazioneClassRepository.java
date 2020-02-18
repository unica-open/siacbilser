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

import it.csi.siac.siacfinser.integration.entity.SiacRLiquidazioneClassFin;

public interface SiacRLiquidazioneClassRepository extends JpaRepository<SiacRLiquidazioneClassFin, Integer> {
	
	String condizioneInJoin = " ( (sr.dataInizioValidita < :dataInput)  AND (sr.dataFineValidita IS NULL OR :dataInput < sr.dataFineValidita) AND  sr.dataCancellazione IS NULL ) ";
	
	String condizione = " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND  dataCancellazione IS NULL ) ";
	
	@Query("SELECT sr FROM SiacRLiquidazioneClassFin sr, SiacTClassFin st WHERE sr.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND " +
			"  st.siacDClassTipo.classifTipoCode IN (:classifTipoCodes)  AND sr.siacTClass.classifId = st.classifId AND sr.siacTLiquidazione.liqId = :idLiq AND " + condizioneInJoin )
	public List<SiacRLiquidazioneClassFin> findByTipoCodesAndEnteAndLiquidazione(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("dataInput") Timestamp  dataInput,
			@Param("classifTipoCodes") List<String> classifTipoCodes,@Param("idLiq") Integer idLiq);
	
	@Query("FROM SiacRLiquidazioneClassFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND " +
			" siacTClass.siacDClassTipo.classifTipoId = :idClassTipo AND siacTLiquidazione.liqId = :idLiq AND "+condizione)
	public List<SiacRLiquidazioneClassFin> findByTipoAndEnteAndLiquidazione(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("dataInput") Timestamp  dataInput,
			@Param("idClassTipo") Integer idClassTipo, @Param("idLiq") Integer idLiq);
	
	@Query("FROM SiacRLiquidazioneClassFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND " +
			" siacTLiquidazione.liqId = :idLiq AND "+condizione)
	public List<SiacRLiquidazioneClassFin> findAllValidiByLiquidazione(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("dataInput") Timestamp  dataInput, @Param("idLiq") Integer idLiq);
}