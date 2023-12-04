/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.ordinativo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacDContotesoreriaFin;
import it.csi.siac.siacfinser.integration.entity.SiacROrdinativoContotesNoDispFin;

public interface SiacROrdinativoContotesNoDispFinRepository extends JpaRepository<SiacROrdinativoContotesNoDispFin, Integer>  {
	
	@Query(" FROM SiacROrdinativoContotesNoDispFin rconto "
			+ " WHERE rconto.dataCancellazione IS NULL"
			+ " AND rconto.siacTOrdinativo.ordId = :ordId "
			+ " AND rconto.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId"
			)
	List<SiacROrdinativoContotesNoDispFin> findValidiByOrdinativoId(@Param("ordId")Integer ordId, @Param("enteProprietarioId") Integer enteProprietarioId);
	
	@Query( " SELECT rconto.siacDContoTesoreria "
			+ " FROM SiacROrdinativoContotesNoDispFin rconto "
			+ " WHERE rconto.dataCancellazione IS NULL"
			+ " AND rconto.siacTOrdinativo.ordId = :ordId "
			+ " AND rconto.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId"
			)
	List<SiacDContotesoreriaFin> findContoTesoreriaSenzaCapienzaByOrdinativoId(@Param("ordId")Integer ordId, @Param("enteProprietarioId") Integer enteProprietarioId);
	
}













