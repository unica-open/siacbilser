/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.mutuo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.csi.siac.siacbilser.integration.entity.SiacTMutuo;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;

public interface SiacTMutuoDao extends Dao<SiacTMutuo, Integer> {

	Page<SiacTMutuo> ricercaSinteticaMutuo(Integer enteProprietarioId, Integer mutuoNumero, String mutuoTipoTassoCode,
			Integer attoammId, Integer attoammAnno, Integer attoammNumero, Integer attoammTipoId, Integer attoammSacId,
			String mutuoOggetto, Integer soggettoId, String mutuoStatoCode, Integer mutuoPeriodoRimborsoId,
			Pageable pageable);
}
