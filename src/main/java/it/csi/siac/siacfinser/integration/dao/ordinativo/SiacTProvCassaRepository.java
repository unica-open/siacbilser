/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.ordinativo;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacROrdinativoModpagFin;
import it.csi.siac.siacfinser.integration.entity.SiacTProvCassaFin;

public interface SiacTProvCassaRepository extends JpaRepository<SiacTProvCassaFin, Integer> {
	String condizione = " ((dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND dataCancellazione IS NULL) ";
	String condizioneProv = " ( (pdc.dataInizioValidita < :dataInput)  AND (pdc.dataFineValidita IS NULL OR :dataInput < pdc.dataFineValidita) AND pdc.dataCancellazione IS NULL ) ";
	
	@Query("FROM SiacTProvCassaFin WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND "+condizione)
	public List<SiacROrdinativoModpagFin> findListaSiacTProvCassa(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("dataInput") Timestamp  dataInput);
	
	@Query("FROM SiacTProvCassaFin pdc WHERE pdc.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND pdc.provcAnno = :anno AND pdc.provcNumero = :numero AND pdc.siacDProvCassaTipo.provcTipoCode = :tipoProvvisorio AND" + condizioneProv)
	public SiacTProvCassaFin findProvvisorioDiCassaValidoByAnnoNumero(@Param("enteProprietarioId") Integer enteProprietarioId,
			 						  @Param("anno") Integer anno,
			 						 @Param("numero") BigDecimal numero,
			 						  @Param("dataInput") Timestamp dataInput,
			 						 @Param("tipoProvvisorio") String tipoProvvisorio);
	
	
	@Query("FROM SiacTProvCassaFin pdc WHERE provcId IN (:listaIds) ")
	public List<SiacTProvCassaFin> findByListIds(@Param("listaIds")List<Integer> listaIds);
	
//	@Query("FROM SiacTProvCassaFin pdc WHERE pdc.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ")
//	public List<SiacTProvCassaFin> findAllByEnte(@Param("enteProprietarioId") Integer enteProprietarioId);
//	
//	@Query("FROM SiacTProvCassaFin pdc WHERE pdc.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND " + condizioneProv)
//	public List<SiacTProvCassaFin> findAllByEnteValidi(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("dataInput") Timestamp dataInput);
	
}