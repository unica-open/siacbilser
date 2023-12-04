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

import it.csi.siac.siacfinser.integration.entity.SiacDContotesoreriaFin;

public interface SiacDContotesoreriaFinRepository extends JpaRepository<SiacDContotesoreriaFin, Integer> {
	String condizione = " ( (dataInizioValidita < :dataInput)  AND (dataFineValidita IS NULL OR :dataInput < dataFineValidita) AND  dataCancellazione IS NULL ) order by contotesCode ASC ";
	
	static final String FIND_CONTOTESORERIA_BY_CODE ="FROM SiacDContotesoreriaFin "
            +"WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
            +"AND contotesCode = :code " 
            +"AND "+condizione;
	
	static final String FIND_CONTOTESORERIA_BY_ENTE ="FROM SiacDContotesoreriaFin "
            +"WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
            +"AND "+condizione;
	
	@Query(FIND_CONTOTESORERIA_BY_CODE)
	public SiacDContotesoreriaFin findContotesoreriaByCode(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("code") String code,@Param("dataInput") Timestamp  dataInput);

	@Query(FIND_CONTOTESORERIA_BY_ENTE)
	public List<SiacDContotesoreriaFin> findContotesoreriaByEnte(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("dataInput") Timestamp  dataInput);
	
	@Query(" FROM SiacDContotesoreriaFin conto"
            + " WHERE conto.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
            + " AND conto.contotesCode = :code "
            + " AND conto.vincolato = true "
            + " AND EXISTS( "
            + "   FROM SiacRSaldoVincoloSottoContoFin rsaldo, SiacRVincoloBilElemFin rcap "
            + "   WHERE rsaldo.siacTVincolo = rcap.siacTVincolo"
            + "   AND rsaldo.dataCancellazione IS NULL "
            + "   AND rcap.dataCancellazione IS NULL "
            + "   AND rsaldo.siacDContoTesoreria = conto"
            + "   AND rcap.siacTBilElem.elemId = :elemId "
            + " ) ")
	public SiacDContotesoreriaFin findContotesoreriaVincolatosuCapitoloByCode(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("code") String code,@Param("elemId") Integer  elemId);
	
	@Query(" FROM SiacDContotesoreriaFin conto"
            + " WHERE conto.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
            + " AND conto.contotesCode = :code "
            + " AND conto.vincolato = true ")
	public SiacDContotesoreriaFin findContotesoreriaVincolatoByCode(@Param("enteProprietarioId") Integer enteProprietarioId,@Param("code") String code);

	
	@Query(" FROM SiacDContotesoreriaFin conto"
            + " WHERE conto.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
            + " AND conto.perRipianamento = true "
            )
	public List<SiacDContotesoreriaFin> findContoTesoreriaPerRipianamentoByCode(@Param("enteProprietarioId") Integer enteProprietarioId);
}