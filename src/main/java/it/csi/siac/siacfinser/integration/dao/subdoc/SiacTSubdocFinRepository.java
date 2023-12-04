/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.subdoc;

import java.sql.Timestamp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacTSubdocFin;

public interface SiacTSubdocFinRepository extends JpaRepository<SiacTSubdocFin, Integer> {
	String condizione = " ( (subDoc.dataInizioValidita < :dataInput)  AND (subDoc.dataFineValidita IS NULL OR :dataInput < subDoc.dataFineValidita) AND subDoc.dataCancellazione IS NULL ) ";

	@Query(" FROM SiacTSubdocFin subDoc " +
	       " WHERE subDoc.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId AND " + 
		   "       subDoc.siacTDoc.docAnno = :annoDocumento AND " +
		   "       subDoc.siacTDoc.docNumero = :numeroDocumento AND " +
		   "       subDoc.siacTDoc.siacDDocTipo.docTipoCode = :docTipoCode AND " +
	       "       subDoc.subdocNumero = :numeroSubDocumento AND " + condizione)
	public SiacTSubdocFin findSubDocumentoByAnnoDocNumDocNumSubDoc(@Param("enteProprietarioId") Integer enteProprietarioId,
			                                                    @Param("annoDocumento") Integer annoDocumento,
			                                                    @Param("numeroDocumento") String numeroDocumento,
                                                                @Param("numeroSubDocumento") Integer numeroSubDocumento,
                                                                @Param("docTipoCode") String docTipoCode,
                                                                @Param("dataInput") Timestamp dataInput);
}