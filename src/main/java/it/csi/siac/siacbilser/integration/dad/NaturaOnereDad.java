/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.SiacDOnereTipoRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDOnereTipo;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siaccommonser.integration.dad.base.BaseDadImpl;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siacfin2ser.model.NaturaOnere;

// TODO: Auto-generated Javadoc
/**
 * Classe di DAD per la Natura Onere.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class NaturaOnereDad extends BaseDadImpl {
	
	/** The siac d onere tipo repository. */
	@Autowired
	private SiacDOnereTipoRepository siacDOnereTipoRepository;

	
	/**
	 * Effettua la ricerca dei codici bollo per un Ente.
	 *
	 * @param ente the ente
	 * @return the list
	 */
	public List<NaturaOnere> ricercaNatureOnere(Ente ente) {
		
		Date inizioDataAttuale =  new Date();
		inizioDataAttuale = DateUtils.setHours(inizioDataAttuale, 0);
		inizioDataAttuale = DateUtils.setMinutes(inizioDataAttuale, 0);
		inizioDataAttuale = DateUtils.setSeconds(inizioDataAttuale, 0);
		inizioDataAttuale = DateUtils.setMilliseconds(inizioDataAttuale, 0);
		
		List<SiacDOnereTipo> elencoNatureOnereDB = siacDOnereTipoRepository.findNatureOnereByEnte(ente.getUid(), inizioDataAttuale);
		if(elencoNatureOnereDB == null) {
			return new ArrayList<NaturaOnere>();
		}
		
		return convertiLista(elencoNatureOnereDB, NaturaOnere.class, BilMapId.SiacDOnereTipo_NaturaOnere);
	}

	public NaturaOnere findNaturaOnereByUid(Integer uid) {
		SiacDOnereTipo siacDOnereTipo = siacDOnereTipoRepository.findOne(uid);
		return mapNotNull(siacDOnereTipo, NaturaOnere.class, BilMapId.SiacDOnereTipo_NaturaOnere);
	}
}
