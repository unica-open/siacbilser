/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTIvaAliquota;

/**
 * The Interface SiacTIvaAliquotaRepository.
 */
public interface SiacTIvaAliquotaRepository extends JpaRepository<SiacTIvaAliquota, Integer> {
	
	/**
	 * Find by ente proprietario.
	 *
	 * @param enteProprietarioId the ente proprietario id
	 * @return the list
	 */
	@Query( " SELECT a " +
			" FROM SiacTIvaAliquota a " +
			" WHERE a.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
			" AND (a.dataFineValidita IS NULL OR a.dataFineValidita > CURRENT_TIMESTAMP) " +
			" AND a.dataCancellazione IS NULL " +
			" ORDER BY a.ivaaliquotaCode ")
	List<SiacTIvaAliquota> findByEnteProprietario(@Param("enteProprietarioId") Integer enteProprietarioId);
	
	


//	/**
//	 * Lista delle aliquote di tutti i subdocumenti iva appartenenti al tipo di registro iva del gruppo specificato
//	 * 
//	 * @param enteProprietarioId
//	 * @param ivagruId
//	 * @param ivaregTipoCode
//	 * @return
//	 */
//	//aliq->siacTIvamovs->siacRIvamovs->siacTSubdocIva->siacTIvaRegistro	
//	@Query( " SELECT a " +
//			" FROM SiacTIvaAliquota a " +
//			" WHERE a.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
//			" AND a.dataCancellazione IS NULL " +
//			" AND EXISTS ( " +
//			"	FROM a.siacTIvamovs ivamov "+
//			"   WHERE ivamov.dataCancellazione IS NULL " + 
//			"	AND EXISTS ( " +
//			"       SELECT rivamovs " +
//		    "       FROM ivamov.siacRIvamovs rivamovs, SiacTIvaRegistro sr " +
//			"   	WHERE rivamovs.dataCancellazione IS NULL " + 
//			"		AND rivamovs.siacTSubdocIva.siacTIvaRegistro.siacDIvaRegistroTipo.ivaregTipoCode = :ivaregTipoCode " +
//			"       AND sr = rivamovs.siacTSubdocIva.siacTIvaRegistro " +
//			"		AND EXISTS ( " +
//			"			FROM sr.siacRIvaRegistroGruppos rivagruppo " +
//			"			WHERE rivagruppo.dataCancellazione IS NULL " + 
//			"			AND rivagruppo.siacTIvaGruppo.ivagruId = :ivagruId " + 
//			"  		) " +
//			"  	) " +
//			" ) " )
//	List<SiacTIvaAliquota> findByIvaGruppoAndIvaTipo(@Param("enteProprietarioId") Integer enteProprietarioId,
//			@Param("ivagruId") Integer ivagruId, @Param("ivaregTipoCode") String ivaregTipoCode);
	
	/**
	 * Lista delle aliquote di tutti i subdocumenti iva appartenenti al tipo di registro iva del gruppo specificato dell'anno indicato
	 * 
	 * @param enteProprietarioId
	 * @param ivagruId
	 * @param ivaregTipoCode
	 * @param subdocivaAnno
	 * @return
	 */
	//aliq->siacTIvamovs->siacRIvamovs->siacTSubdocIva->siacTIvaRegistro	
	@Query( " SELECT a " +
			" FROM SiacTIvaAliquota a " +
			" WHERE a.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
			" AND a.dataCancellazione IS NULL " +
			" AND EXISTS ( " +
			"	FROM a.siacTIvamovs ivamov "+
			"   WHERE ivamov.dataCancellazione IS NULL " + 
			"	AND EXISTS ( " +
			"       SELECT rivamovs " +
		    "       FROM ivamov.siacRIvamovs rivamovs, SiacTIvaRegistro sr " +
			"   	WHERE rivamovs.dataCancellazione IS NULL " + 
			"		AND rivamovs.siacTSubdocIva.siacTIvaRegistro.siacDIvaRegistroTipo.ivaregTipoCode = :ivaregTipoCode " +
//			"		AND rivamovs.siacTSubdocIva.subdocivaAnno = :subdocivaAnno " +
			"		AND rivamovs.siacTSubdocIva.subdocivaDataProtDef >= :subdocivaDataProtDefDa " +
			"		AND rivamovs.siacTSubdocIva.subdocivaDataProtDef <= :subdocivaDataProtDefA " +
			"       AND sr = rivamovs.siacTSubdocIva.siacTIvaRegistro " +
			"		AND EXISTS ( " +
			"			FROM sr.siacRIvaRegistroGruppos rivagruppo " +
			"			WHERE rivagruppo.dataCancellazione IS NULL " + 
			"			AND rivagruppo.siacTIvaGruppo.ivagruId = :ivagruId " + 
			"  		) " +
			"  	) " +
			" ) " )
	List<SiacTIvaAliquota> findByIvaGruppoAndIvaTipoAndDateProtocolloDefDaA(@Param("enteProprietarioId") Integer enteProprietarioId,
			@Param("ivagruId") Integer ivagruId, @Param("ivaregTipoCode") String ivaregTipoCode, @Param("subdocivaDataProtDefDa") Date subdocivaDataProtDefDa,
			@Param("subdocivaDataProtDefA") Date subdocivaDataProtDefA);
	
	@Query( " SELECT a " +
			" FROM SiacTIvaAliquota a " +
			" WHERE a.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
			" AND a.dataCancellazione IS NULL " +
			" AND EXISTS ( " +
			"	FROM a.siacTIvamovs ivamov "+
			"   WHERE ivamov.dataCancellazione IS NULL " + 
			"	AND EXISTS ( " +
			"       SELECT rivamovs " +
		    "       FROM ivamov.siacRIvamovs rivamovs, SiacTIvaRegistro sr " +
			"   	WHERE rivamovs.dataCancellazione IS NULL " + 
			"		AND rivamovs.siacTSubdocIva.siacTIvaRegistro.siacDIvaRegistroTipo.ivaregTipoCode = :ivaregTipoCode " +
//			"		AND rivamovs.siacTSubdocIva.subdocivaAnno = :subdocivaAnno " +
			"		AND rivamovs.siacTSubdocIva.subdocivaDataProtProv >= :subdocivaDataProtProvDa " +
			"		AND rivamovs.siacTSubdocIva.subdocivaDataProtProv <= :subdocivaDataProtProvA " +
			"       AND sr = rivamovs.siacTSubdocIva.siacTIvaRegistro " +
			"		AND EXISTS ( " +
			"			FROM sr.siacRIvaRegistroGruppos rivagruppo " +
			"			WHERE rivagruppo.dataCancellazione IS NULL " + 
			"			AND rivagruppo.siacTIvaGruppo.ivagruId = :ivagruId " + 
			"  		) " +
			"  	) " +
			" ) " )
	List<SiacTIvaAliquota> findByIvaGruppoAndIvaTipoAndDateProtocolloProvDaA(@Param("enteProprietarioId") Integer enteProprietarioId,
			@Param("ivagruId") Integer ivagruId, @Param("ivaregTipoCode") String ivaregTipoCode, @Param("subdocivaDataProtProvDa") Date subdocivaDataProtProvDa,
			@Param("subdocivaDataProtProvA") Date subdocivaDataProtProvA);
}
