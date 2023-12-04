/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.cespiti;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.csi.siac.siacbilser.integration.entity.SiacTCespitiAmmortamentoDett;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;


/**
 * The Interface CespitiBeneTipoDao.
 *
 * @author Antonino
 */
public interface DettaglioAmmortamentoAnnuoCespiteDao extends Dao<SiacTCespitiAmmortamentoDett, Integer> {
	
	SiacTCespitiAmmortamentoDett create(SiacTCespitiAmmortamentoDett r);
	
	SiacTCespitiAmmortamentoDett update(SiacTCespitiAmmortamentoDett r);
	
	SiacTCespitiAmmortamentoDett delete(int uidCespiti, String loginOperazione);

	Page<SiacTCespitiAmmortamentoDett> ricercaSinteticaDettagliAmmortamentoAnnuo(int enteProprietarioId, Integer uidCespite, Pageable pageable);

	List<SiacTCespitiAmmortamentoDett> ricercaDettagliAmmortamentoAnnuoByCespite(int enteProprietarioId, Integer uidCespite);
	
}
