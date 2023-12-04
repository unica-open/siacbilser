/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.ordinativo;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacROrdinativoFin;

public interface SiacROrdinativoRepository extends JpaRepository<SiacROrdinativoFin, Integer> {
	
	String condizione = " ( (rord.dataInizioValidita < :dataInput)  AND (rord.dataFineValidita IS NULL OR :dataInput < rord.dataFineValidita) AND rord.dataCancellazione IS NULL ) ";
	
	//ORD 1 - ord_id_da
	String findValideByIdOrdUnoQuery = "FROM SiacROrdinativoFin rord WHERE (rord.siacTOrdinativo1.ordId = :ordinativoId) AND " + condizione; 

	@Query(findValideByIdOrdUnoQuery)
	public List<SiacROrdinativoFin> findValideByIdOrdUno(@Param("ordinativoId") Integer ordinativoId, @Param("dataInput") Timestamp dataInput);
	
	@Query(findValideByIdOrdUnoQuery)
	public List<SiacROrdinativoFin> findValideByIdOrdUno(@Param("ordinativoId") Integer ordinativoId, @Param("dataInput") Timestamp dataInput, Pageable page);

	//SIAC-8237 ORD 2 - ord_id_a
	String findValideByIdOrdDueQuery = "FROM SiacROrdinativoFin rord WHERE (rord.siacTOrdinativo2.ordId = :ordinativoId) AND " + condizione; 

	@Query(findValideByIdOrdDueQuery)
	public List<SiacROrdinativoFin> findValideByIdOrdDue(@Param("ordinativoId") Integer ordinativoId, @Param("dataInput") Timestamp dataInput);
	
	@Query(findValideByIdOrdDueQuery)
	public List<SiacROrdinativoFin> findValideByIdOrdDue(@Param("ordinativoId") Integer ordinativoId, @Param("dataInput") Timestamp dataInput, Pageable page);


	@Query("SELECT COUNT(*) " + findValideByIdOrdUnoQuery)
	public Long countValideByIdOrdUno(
			@Param("ordinativoId") Integer ordinativoId, 
			@Param("dataInput") Timestamp dataInput
	);
	
	@Modifying
	@Query("UPDATE SiacROrdinativoFin ro "
			+ " SET ro.dataCancellazione=CURRENT_TIMESTAMP "
			+ " WHERE ro.siacTOrdinativo1.ordId=:ordIdDa "
			+ " AND EXISTS ( "
			+ " 	SELECT 1 FROM ro.siacDRelazTipo rt "
			+ " 		WHERE rt.relazTipoCode=:codiceTipoRelazione "
			+ "			AND ro.siacTEnteProprietario=rt.siacTEnteProprietario "
			+ " )" 
	)
	public void removeByTipoRelazione(
			@Param("ordIdDa") Integer ordIdDa,
			@Param("codiceTipoRelazione") String codiceTipoRelazione
	);
}