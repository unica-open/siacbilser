/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.AttivitaIvaRelazioneCapitoloDao;
import it.csi.siac.siacbilser.integration.dao.SiacTIvaAttivitaRepository;
import it.csi.siac.siacbilser.integration.entity.SiacRBilElemIvaAttivita;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entity.SiacTIvaAttivita;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacfin2ser.model.AttivitaIva;

/**
 * Data access delegate di un DocumentoSpesa .
 *
 * @author Domenico
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class AttivitaIvaDad extends ExtendedBaseDadImpl {
	
	/** The attivita iva relazione capitolo dao. */
	@Autowired
	private AttivitaIvaRelazioneCapitoloDao attivitaIvaRelazioneCapitoloDao;
	
	/** The siac t iva attivita repository. */
	@Autowired
	private SiacTIvaAttivitaRepository siacTIvaAttivitaRepository;
	
	/**
	 * Verifica relazione attivita iva capitolo.
	 *
	 * @param attivitaIva the attivita iva
	 * @param capitolo the capitolo
	 * @return true, if successful
	 */
	public boolean verificaRelazioneAttivitaIvaCapitolo(AttivitaIva attivitaIva, @SuppressWarnings("rawtypes") Capitolo capitolo){
		 List<SiacRBilElemIvaAttivita> listaSiacRBilElemIvaAttivita = 
				 siacTIvaAttivitaRepository.findRelazioneAttivitaIvaCapitolo(capitolo.getUid(), attivitaIva.getUid());
		 return !listaSiacRBilElemIvaAttivita.isEmpty();
	}
	
	/**
	 * Inserisci relazione attivita iva capitolo.
	 *
	 * @param attivitaIva the attivita iva
	 * @param capitolo the capitolo
	 */
	public void inserisciRelazioneAttivitaIvaCapitolo(AttivitaIva attivitaIva, @SuppressWarnings("rawtypes") Capitolo capitolo){
		
		SiacTIvaAttivita siacTIvaAttivita = buildSiacTIvaAttivita(attivitaIva);
		
		SiacTBilElem siacTBilElem = new SiacTBilElem();
		siacTBilElem.setElemId(capitolo.getUid());
		siacTBilElem.setUid(capitolo.getUid());
		
		SiacRBilElemIvaAttivita siacRBilElemIvaAttivita = buildSiacRBilElemIvaAttivita(siacTIvaAttivita, siacTBilElem);
		attivitaIvaRelazioneCapitoloDao.create(siacRBilElemIvaAttivita);
	}
	
	/**
	 * Ricerca attivita legate al capitolo.
	 *
	 * @param capitolo the capitolo
	 * @return the list
	 */
	public List<AttivitaIva> ricercaAttivitaLegateAlCapitolo( @SuppressWarnings("rawtypes") Capitolo capitolo){
		List<SiacTIvaAttivita> listaSiacTIvaAttivita= siacTIvaAttivitaRepository.findByCapitolo(capitolo.getUid());
		return convertiLista(listaSiacTIvaAttivita, AttivitaIva.class, BilMapId.SiacTIvaAttivita_AttivitaIva);
	}
	
	/**
	 * Elimina relazione attivita iva capitolo.
	 *
	 * @param attivitaIva the attivita iva
	 * @param capitolo the capitolo
	 */
	public void eliminaRelazioneAttivitaIvaCapitolo(AttivitaIva attivitaIva,  @SuppressWarnings("rawtypes") Capitolo capitolo){
		 List<SiacRBilElemIvaAttivita> listaSiacRBilElemIvaAttivita = 
				 siacTIvaAttivitaRepository.findRelazioneAttivitaIvaCapitolo(capitolo.getUid(),attivitaIva.getUid());
		 for(SiacRBilElemIvaAttivita siacRBilElemIvaAttivita : listaSiacRBilElemIvaAttivita){
			 attivitaIvaRelazioneCapitoloDao.delete(siacRBilElemIvaAttivita); 
		 }
	}

	/**
	 * Builds the siac t iva attivita.
	 *
	 * @param attivitaIva the attivita iva
	 * @return the siac t iva attivita
	 */
	private SiacTIvaAttivita buildSiacTIvaAttivita(AttivitaIva attivitaIva) {
		SiacTIvaAttivita siacTIvaAttivita = new SiacTIvaAttivita();
		siacTIvaAttivita.setLoginOperazione(loginOperazione);
		attivitaIva.setLoginOperazione(loginOperazione);
		map(attivitaIva, siacTIvaAttivita, BilMapId.SiacTIvaAttivita_AttivitaIva);
		return siacTIvaAttivita;
	}
	
	/**
	 * Builds the siac r bil elem iva attivita.
	 *
	 * @param siacTIvaAttivita the siac t iva attivita
	 * @param siacTBilElem the siac t bil elem
	 * @return the siac r bil elem iva attivita
	 */
	private SiacRBilElemIvaAttivita buildSiacRBilElemIvaAttivita(SiacTIvaAttivita siacTIvaAttivita, SiacTBilElem siacTBilElem) {
		SiacRBilElemIvaAttivita siacRBilElemIvaAttivita	= new SiacRBilElemIvaAttivita();
		siacRBilElemIvaAttivita.setSiacTBilElem(siacTBilElem);
		siacRBilElemIvaAttivita.setSiacTIvaAttivita(siacTIvaAttivita);
		siacRBilElemIvaAttivita.setSiacTEnteProprietario(siacTIvaAttivita.getSiacTEnteProprietario());
		siacRBilElemIvaAttivita.setLoginOperazione(siacTIvaAttivita.getLoginOperazione());
		return siacRBilElemIvaAttivita;
	}


}
