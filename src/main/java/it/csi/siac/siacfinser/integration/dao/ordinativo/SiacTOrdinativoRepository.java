/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.ordinativo;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacTOrdinativoFin;

public interface SiacTOrdinativoRepository extends JpaRepository<SiacTOrdinativoFin, Integer> {
	String condizione = " ( (ord.dataInizioValidita < :dataInput)  AND (ord.dataFineValidita IS NULL OR :dataInput < ord.dataFineValidita) AND ord.dataCancellazione IS NULL ) ";
	
	@Query(" FROM SiacTOrdinativoFin ord WHERE ord.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND " +
	       "                                ord.ordNumero = :ordNumero AND " + 
	       "                                ord.ordAnno = :ordAnno AND " +
		   "                                ord.siacDOrdinativoTipo.ordTipoCode = :codeTipoOrdinativo")
	public SiacTOrdinativoFin findOrdinativoByAnnoAndNumeroAndTipo(@Param("enteProprietarioId") Integer enteProprietarioId,
																@Param("ordAnno") Integer ordAnno,
			                                                    @Param("ordNumero") BigDecimal ordNumero,		 						                                
			                                                    @Param("codeTipoOrdinativo") String codeTipoOrdinativo);
	
	@Query(" FROM SiacTOrdinativoFin ord WHERE ord.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND " +
           "                                ord.ordNumero = :ordNumero AND " +
	       "                                ord.ordAnno = :ordAnno AND " +
		   "                                ord.siacDOrdinativoTipo.ordTipoCode = :codeTipoOrdinativo AND " + condizione)
	public SiacTOrdinativoFin findOrdinativoValidoByAnnoAndNumeroAndTipo(@Param("enteProprietarioId") Integer enteProprietarioId,
																	  @Param("ordAnno") Integer ordAnno,
			 						                                  @Param("ordNumero") BigDecimal ordNumero,
			 						                                  @Param("codeTipoOrdinativo") String codeTipoOrdinativo,
			 						                                  @Param("dataInput") Timestamp dataInput);
	
	
	
	
	@Query(" FROM SiacTOrdinativoFin ord WHERE ord.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND " +
		   "                                ord.ordAnno = :ordAnno AND " +
		   "                                ord.siacTBil.siacTPeriodo.anno = :annoEsercizio AND " +
		   "                                ord.ordNumero = :ordNumero AND "  +
		   "                                ord.siacDOrdinativoTipo.ordTipoCode = :codeTipoOrdinativo")
	public SiacTOrdinativoFin findOrdinativoByCodeAndAnno(@Param("enteProprietarioId") Integer enteProprietarioId,
													   @Param("ordAnno") int ordAnno,
													   @Param("annoEsercizio") String annoBil,
				                                       @Param("ordNumero") BigDecimal ordNumero,
				                                       @Param("codeTipoOrdinativo") String codeTipoOrdinativo);
	
	
	@Modifying
	@Query("UPDATE SiacTOrdinativoFin o SET o.dataModifica=CURRENT_TIMESTAMP, o.ordDaTrasmettere=:daTrasmettere WHERE o.ordId=:idOrdinativo")
	void setDaTrasmettere(
			@Param("idOrdinativo") Integer idOrdinativo,
			@Param("daTrasmettere") Boolean daTrasmettere
	);
}