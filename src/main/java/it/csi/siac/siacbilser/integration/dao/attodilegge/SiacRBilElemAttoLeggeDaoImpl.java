/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.attodilegge;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacRBilElemAttoLeggeRepository;
import it.csi.siac.siacbilser.integration.entity.SiacRBilElemAttoLegge;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;

// TODO: Auto-generated Javadoc
/**
 * The Class SiacRBilElemAttoLeggeDaoImpl.
 */
@Component
public class SiacRBilElemAttoLeggeDaoImpl extends JpaDao<SiacRBilElemAttoLegge, Integer> implements SiacRBilElemAttoLeggeDao {
		
	/** The siac r bil elem atto legge repository. */
	@Autowired
	private SiacRBilElemAttoLeggeRepository siacRBilElemAttoLeggeRepository;

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.integration.dao.attodilegge.SiacRBilElemAttoLeggeDao#create(it.csi.siac.siacbilser.integration.entity.SiacRBilElemAttoLegge)
	 */
	@Override
	public SiacRBilElemAttoLegge create(SiacRBilElemAttoLegge relazioneAttoLeggeCapitolo) {
		Date now = new Date();
		relazioneAttoLeggeCapitolo.setDataModificaInserimento(now);
	
		super.save(relazioneAttoLeggeCapitolo);
		
		return relazioneAttoLeggeCapitolo;
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.dao.base.JpaDao#update(java.lang.Object)
	 */
	@Override
	public SiacRBilElemAttoLegge update(SiacRBilElemAttoLegge attoLegge) {
		Date now = new Date();
		attoLegge.setDataModificaAggiornamento(now);
		
		super.update(attoLegge);
		
		return attoLegge;
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.integration.dao.attodilegge.SiacRBilElemAttoLeggeDao#findRelazioneAttoDiLeggeCapitoloById(java.lang.Integer, java.lang.Integer)
	 */
	public SiacRBilElemAttoLegge findRelazioneAttoDiLeggeCapitoloById(Integer uidAttoDiLegge, Integer uidCapitolo) {
		List<SiacRBilElemAttoLegge> elencoTrovati = siacRBilElemAttoLeggeRepository.findByElemIdAndAttoleggeId(uidCapitolo, uidAttoDiLegge);		
		
		SiacRBilElemAttoLegge result = null;
		if (elencoTrovati != null && !elencoTrovati.isEmpty()) {
			result = elencoTrovati.get(0);
		
		}
		return result;
	}
}
