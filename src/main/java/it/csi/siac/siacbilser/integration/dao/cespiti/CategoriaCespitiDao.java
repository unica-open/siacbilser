/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.cespiti;



import java.math.BigDecimal;
import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.csi.siac.siacbilser.integration.entity.SiacDCespitiCategoria;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;


/**
 * The Interface CespitiCategoriaDao.
 *
 * @author Antonino
 */
public interface CategoriaCespitiDao extends Dao<SiacDCespitiCategoria, Integer> {
	
	SiacDCespitiCategoria create(SiacDCespitiCategoria c);

	SiacDCespitiCategoria update(SiacDCespitiCategoria c);
	
	Page<SiacDCespitiCategoria> ricercaSinteticaCategoriaCespiti(int enteProprietarioId, String cescatCode,String cescatDesc, BigDecimal aliquotaAnnua, Integer cescatCalcoloTipoId, Boolean escludiAnnullati, String ambitoCode, Date dataValidita, Pageable pageable);

	SiacDCespitiCategoria delete(int uidCategoriaCespiti, String loginOperazione);

}
