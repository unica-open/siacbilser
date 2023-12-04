/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SirfelDModalitaPagamento;
import it.csi.siac.siacbilser.integration.entity.SirfelDModalitaPagamentoPK;
import it.csi.siac.siacbilser.integration.entity.SirfelDNatura;
import it.csi.siac.siacbilser.integration.entity.SirfelDNaturaPK;
import it.csi.siac.siacbilser.integration.entity.SirfelDTipoCassa;
import it.csi.siac.siacbilser.integration.entity.SirfelDTipoCassaPK;
import it.csi.siac.siacbilser.integration.entity.SirfelDTipoDocumento;
import it.csi.siac.siacbilser.integration.entity.SirfelDTipoDocumentoPK;
import it.csi.siac.siacbilser.integration.entity.SirfelTFattura;
import it.csi.siac.siacbilser.integration.entity.SirfelTFatturaPK;
import it.csi.siac.siacbilser.integration.entity.SirfelTRiepilogoBeni;

/**
 * The Interface SirfelTFatturaRepository.
 */
public interface SirfelTFatturaRepository extends JpaRepository<SirfelTFattura, SirfelTFatturaPK> {

	@Query(" FROM SirfelTRiepilogoBeni f" +
			" WHERE f.id.idFattura = :idFattura " +
			" AND f.id.enteProprietarioId = :enteProprietarioId")
	public List<SirfelTRiepilogoBeni> findSirfelTRiepilogoBenisByIdFatturaEEnte(@Param("idFattura") Integer idFattura, @Param("enteProprietarioId") Integer enteProprietarioId);
	
	@Query(" SELECT COALESCE(MAX(tf.id.idFattura), 0) "
			+ " FROM SirfelTFattura tf "
			+ " WHERE tf.id.enteProprietarioId = :enteProprietarioId ")
	public Integer getMaxIdFatturaByEnteProprietarioId(@Param("enteProprietarioId") Integer enteProprietarioId);

	@Query(" FROM SirfelDTipoCassa dtc "
			+ " WHERE dtc.id = :id ")
	public SirfelDTipoCassa findSirfelDTipoCassaBySirfelDTipoCassaPK(@Param("id") SirfelDTipoCassaPK id);

	@Query(" FROM SirfelDNatura dn "
			+ " WHERE dn.id = :id ")
	public SirfelDNatura findSirfelDNaturaBySirfelDNaturaPK(@Param("id") SirfelDNaturaPK idb);

	@Query(" FROM SirfelDTipoDocumento dtd "
			+ " WHERE dtd.id = :id ")
	public SirfelDTipoDocumento findSirfelDTipoDocumentoBySirfelDTipoDocumentoPK(@Param("id") SirfelDTipoDocumentoPK pk);
	
	@Query(" FROM SirfelDModalitaPagamento dmp "
			+ " WHERE dmp.id = :id ")
	public SirfelDModalitaPagamento findSirfelDModalitaPagamentoBySirfelDModalitaPagamentoPK(@Param("id") SirfelDModalitaPagamentoPK pk);
	
}
