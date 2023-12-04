/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.csi.siac.siacbilser.integration.entity.SiacDBilElemDetCompTipo;
import it.csi.siac.siacbilser.integration.entity.SirfelDTipoDocumento;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;
import it.csi.siac.siacfin2ser.model.TipoDocumento;

public interface TipoDocumentoFELDao extends Dao<SirfelDTipoDocumento, Integer> {
	
	List<SirfelDTipoDocumento> ricercaTipoDocumentoFEL(
			Integer enteProprietarioId, 
			String codice,
			String descrizione
			);

	Page<SirfelDTipoDocumento> ricercaPaginataTipoDocumentoFEL(
			Integer enteProprietarioId, 
			String codice,
			String descrizione, 
			TipoDocumento tipoDocContabiliaEntrata,
			TipoDocumento tipoDocContabiliaSpesa,
			Pageable pageable);
}
