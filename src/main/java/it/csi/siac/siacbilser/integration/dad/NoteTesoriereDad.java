/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.SiacDNoteTesoriereRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDNoteTesoriere;
import it.csi.siac.siaccommonser.integration.dad.base.BaseDadImpl;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siacfin2ser.model.NoteTesoriere;

// TODO: Auto-generated Javadoc
/**
 * Classe di DAD per le Note Tesoriere.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class NoteTesoriereDad extends BaseDadImpl {
	
	/** The siac d note tesoriere repository. */
	@Autowired
	private SiacDNoteTesoriereRepository siacDNoteTesoriereRepository;

	
	/**
	 * Effettua la ricerca dei codici bollo per un Ente.
	 *
	 * @param ente the ente
	 * @return the list
	 */
	public List<NoteTesoriere> ricercaNoteTesoriere(Ente ente) {
		
		List<SiacDNoteTesoriere> elencoDB = siacDNoteTesoriereRepository.findNoteTesoriereByEnte(ente.getUid());
		if(elencoDB == null) {
			return new ArrayList<NoteTesoriere>();
		}
		
		List<NoteTesoriere> elencoReturn = new ArrayList<NoteTesoriere>(elencoDB.size());
		
		for (SiacDNoteTesoriere elemDB : elencoDB) {
			
			NoteTesoriere notaTesoriereToAdd = mapNoteTesoriere(elemDB);
			elencoReturn.add(notaTesoriereToAdd);
		}
		return elencoReturn;
	}


	/**
	 * Map note tesoriere.
	 *
	 * @param codBolloDB the cod bollo db
	 * @return the note tesoriere
	 */
	private NoteTesoriere mapNoteTesoriere(SiacDNoteTesoriere codBolloDB) {
		NoteTesoriere toAdd = new NoteTesoriere();
		toAdd.setUid(codBolloDB.getUid());
		toAdd.setCodice(codBolloDB.getNotetesCode());
		toAdd.setDescrizione(codBolloDB.getNotetesDesc());
		return toAdd;
	}
}
