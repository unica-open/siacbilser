/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.cespiti;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.csi.siac.siacbilser.integration.entity.SiacTCespitiElabAmmortamenti;
import it.csi.siac.siacbilser.integration.entity.SiacTCespitiElabAmmortamentiDett;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;


/**
 * The Interface CespitiBeneTipoDao.
 *
 * @author Antonino
 */
public interface AnteprimaAmmortamentoAnnuoCespiteDao extends Dao<SiacTCespitiElabAmmortamenti, Integer> {
	
	SiacTCespitiElabAmmortamenti create(SiacTCespitiElabAmmortamenti r);
	
	SiacTCespitiElabAmmortamenti update(SiacTCespitiElabAmmortamenti r);

	Page<SiacTCespitiElabAmmortamentiDett> ricercaSinteticaDettaglioAnteprimaAmmortamentoAnnuoCespite(int uidEnteProrietarioId, Integer anno, Pageable pageable);
	
	List<Object[]> inserisciAnteprimaAmmortamentoAnnuoCespite(Integer enteProprietarioId, Integer anno, String loginOperazione);
	
}
