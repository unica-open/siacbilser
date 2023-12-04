/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.csi.siac.siacbilser.integration.entity.SirfelTBase;
import it.csi.siac.siacbilser.integration.entity.SirfelTFattura;
import it.csi.siac.siacbilser.integration.entity.SirfelTFatturaPK;
import it.csi.siac.siacbilser.integration.entity.enumeration.SirfelDTipoDocumentoEnum;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;

/**
 * The Interface DocumentoDao.
 */
public interface FatturaFELDao extends Dao<SirfelTFattura, SirfelTFatturaPK> {

	Page<SirfelTFattura> ricercaSinteticaFatturaFEL(
			int enteProprietarioId,
			//SIAC-7557
			//SirfelDTipoDocumentoEnum sirfelDTipoDocumentoEnum,
			String codiceTipoDocFEL,
			String codicePrestatore,
			String numero, 
			String codiceDestinatario, 
			Date dataDa, 
			Date dataA,
			String statoFattura, 
			Pageable pageable);

	<T extends SirfelTBase<?>> T create(T obj);
	
	void clean();
	
}
