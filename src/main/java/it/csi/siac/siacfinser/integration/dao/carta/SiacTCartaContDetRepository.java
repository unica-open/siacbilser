/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.carta;

import java.sql.Timestamp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacTCartacontDetFin;

public interface SiacTCartaContDetRepository extends JpaRepository<SiacTCartacontDetFin, Integer> {
	String condizione = " ( (cartaDet.dataInizioValidita < :dataInput)  AND (cartaDet.dataFineValidita IS NULL OR :dataInput < cartaDet.dataFineValidita) AND cartaDet.dataCancellazione IS NULL ) ";

	
	@Query(" FROM SiacTCartacontDetFin cartaDet " +
	       " WHERE cartaDet.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND " + 
		   "       cartaDet.siacTCartacont.cartacNumero = :cartaContNumero AND " + 
	       "       cartaDet.siacTCartacont.siacTBil.siacTPeriodo.anno = :cartaContAnno AND " +
		   "       cartaDet.cartacDetNumero = :cartaContDetNumero AND " + condizione)
	public SiacTCartacontDetFin findCartaContDetByAnnoCartaNumeroCartaNumeroDetCarta(@Param("enteProprietarioId") Integer enteProprietarioId,
			                                                                      @Param("cartaContNumero") Integer cartaContNumero,
			                                                                      @Param("cartaContAnno") String cartaContAnno,
			                                                                      @Param("cartaContDetNumero") Integer cartaContDetNumero,
			                                                                      @Param("dataInput") Timestamp dataInput);
}