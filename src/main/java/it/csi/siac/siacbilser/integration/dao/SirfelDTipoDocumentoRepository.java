/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import it.csi.siac.siacbilser.integration.entity.SirfelDTipoDocumento;
import it.csi.siac.siacbilser.integration.entity.SirfelDTipoDocumentoPK;

/**
 * The Interface SirfelTFatturaRepository.
 */
public interface SirfelDTipoDocumentoRepository extends  JpaRepository<SirfelDTipoDocumento, SirfelDTipoDocumentoPK> { 

	@Query(" FROM SirfelDTipoDocumento f" +
			"  WHERE f.id.enteProprietarioId = :enteProprietarioId" +
			 " ORDER BY  f.id.codice")
	public List<SirfelDTipoDocumento> findSirfelDTipoDocumentoByEnte(@Param("enteProprietarioId") Integer enteProprietarioId);
	

	/**
	 * Ricerca puntuale tipo Documento.
	 *
	 * @param enteDto the ente dto
	 * @param codice the codice
	 * @param pageable the pageable
	 * @return the page
	 */
	@Query(" FROM SirfelDTipoDocumento tbe "
			+ " WHERE  tbe.id.enteProprietarioId = :enteProprietarioId "
			+ " AND tbe.id.codice = :codice "
			+ " ) ")
	Page<SirfelDTipoDocumento> ricercaPuntualeTipoDoc(@Param("enteProprietarioId") Integer enteProprietarioId, 
			@Param("codice")String codice,
			Pageable pageable);
	
	
	
	
	@Query(" FROM SirfelDTipoDocumento tbe "
			+ " WHERE  tbe.id.enteProprietarioId = :enteProprietarioId "
			+ " AND UPPER(tbe.id.codice) = UPPER(:codice) "
			+ " ) ")
	SirfelDTipoDocumento findByEnteECodice(@Param("enteProprietarioId")Integer enteProprietarioId, @Param("codice") String codice);
	
	 
	
	
	@Query(" SELECT COALESCE(COUNT(stbedc), 0) "
			+ " FROM SirfelTFattura stbedc "
			+ " WHERE stbedc.tipoDocumento = :codice ")
	Long countFatturaBySirfelDTipoDocumento(@Param("codice") String codice);
	
	//SIAC-7557-VK findSirfetlDTipoDocSirfetlByTipoDocCont
		@Query(" FROM SirfelDTipoDocumento f" +
				" WHERE f.id.enteProprietarioId = :enteProprietarioId" + 
				" AND f.siacDDocTipoE.docTipoId = :idTipoDoc")
		public List<SirfelDTipoDocumento> findSirfelDTipoDocumentoByEnteAndTipoDocE(@Param("enteProprietarioId") Integer enteProprietarioId,
				@Param("idTipoDoc") Integer idTipoDoc);
		
		
		
		//SIAC-7557-VK findSirfetlDTipoDocSirfetlByTipoDocCont
			@Query(" FROM SirfelDTipoDocumento f" +
					" WHERE f.id.enteProprietarioId = :enteProprietarioId" + 
					" AND f.id.codice = :tipoDocFel")
			public List<SirfelDTipoDocumento> findSirfelDTipoDocumentoByEnteAndCodFel(@Param("enteProprietarioId") Integer enteProprietarioId,
					@Param("tipoDocFel") String tipoDocFel);
}
