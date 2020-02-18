/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.cespiti;


import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.csi.siac.siacbilser.integration.dao.cespiti.jpql.EntitaCollegatePrimaNotaInventarioJpqlEnum;
import it.csi.siac.siacbilser.integration.entity.SiacTCespitiVariazione;
import it.csi.siac.siaccespser.model.VariazioneCespite;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;


/**
 * The Interface CespitiBeneTipoDao.
 *
 * @author Antonino
 */
public interface VariazioneCespiteDao extends Dao<SiacTCespitiVariazione, Integer> {

	SiacTCespitiVariazione create(SiacTCespitiVariazione r);
	SiacTCespitiVariazione update(SiacTCespitiVariazione r);
	SiacTCespitiVariazione delete(Integer cesVarId, String loginOperazione);
	Page<SiacTCespitiVariazione> ricercaSinteticaVariazioneCespite(
			Integer enteProprietarioId,
			// Variazione cespite
			String cesVarAnno,
			Date cesVarData,
			String cesVarDesc,
			Boolean flgTipoVariazioneIncr,
			// Cespite
			Integer cesId,
			String cesCode,
			String cesDesc,
			Boolean soggettoBeniCulturali,
			Boolean flgDonazioneRinvenimento,
			String numInventario,
			Date dataIngressoInventario,
			// Tipo bene
			Integer cesBeneTipoId,
			// Classificazione giuridica
			String cesClassGiuCode,
			Pageable pageable);
	
	public VariazioneCespite getVariazioneCespiteDaPrimaNotaTramiteJpql(Integer uidEntitaCollegata, Integer uidCespiteCollegatoAdEntitaGenerante, Integer uidPrimaNota, Integer enteProprietarioId, EntitaCollegatePrimaNotaInventarioJpqlEnum jpqlEnum);
	
	public void aggiornaStatoVariazione(SiacTCespitiVariazione siacTCEspitiVariazione);
}
