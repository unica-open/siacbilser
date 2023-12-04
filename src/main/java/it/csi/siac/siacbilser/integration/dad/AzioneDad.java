/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.SiacTAzioneRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTAzione;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siaccommonser.integration.dad.base.BaseDadImpl;
import it.csi.siac.siaccorser.model.Azione;
import it.csi.siac.siaccorser.model.Ente;

/**
 * The Class AzioneDad.
 */
/**
 * @author Marchino Alessandro
 * @version 1.0.0 - 03/lug/2015
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class AzioneDad extends BaseDadImpl  {
	
	@Autowired
	private SiacTAzioneRepository siacTAzioneRepository;
	
	private Ente ente;

	/**
	 * @param ente the ente to set
	 */
	public void setEnte(Ente ente) {
		this.ente = ente;
	}
	
	public Azione getAzioneByNome(String nomeAzione) {
		SiacTAzione siacTAzione = siacTAzioneRepository.findByAzioneCodeAndEnteProprietarioId(nomeAzione, ente.getUid());
		return mapNotNull(siacTAzione, Azione.class, BilMapId.SiacTAzione_Azione);
	}
	
	public Azione getAzioneByNomeWithGruppo(String nomeAzione) {
		SiacTAzione siacTAzione = siacTAzioneRepository.findByAzioneCodeAndEnteProprietarioId(nomeAzione, ente.getUid());
		return mapNotNull(siacTAzione, Azione.class, BilMapId.SiacTAzione_Azione_ConGruppo);
	}

}
