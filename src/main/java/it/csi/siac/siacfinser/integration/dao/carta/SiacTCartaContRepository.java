/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.carta;

import java.sql.Timestamp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacTCartacontFin;

public interface SiacTCartaContRepository extends JpaRepository<SiacTCartacontFin, Integer> {
	String condizione = " ( (cartaCont.dataInizioValidita < :dataInput)  AND (cartaCont.dataFineValidita IS NULL OR :dataInput < cartaCont.dataFineValidita) AND cartaCont.dataCancellazione IS NULL ) ";

	@Query(" FROM SiacTCartacontFin cartaCont " +
		   " WHERE cartaCont.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND " + 
		   "       cartaCont.cartacNumero = :cartaContNumero AND " + 
		   "       cartaCont.siacTBil.siacTPeriodo.anno = :cartaContAnno AND " + condizione)
	public SiacTCartacontFin findCartaContByAnnoCartaNumeroCarta(@Param("enteProprietarioId") Integer enteProprietarioId,
				                                              @Param("cartaContNumero") Integer cartaContNumero,
				                                              @Param("cartaContAnno") String cartaContAnno,
				                                              @Param("dataInput") Timestamp dataInput);
}