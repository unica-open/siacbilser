/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.GruppoAttivitaIvaDao;
import it.csi.siac.siacbilser.integration.dao.SiacTIvaGruppoRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTIvaGruppo;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.model.GruppoAttivitaIva;

// TODO: Auto-generated Javadoc
/**
 * Data access delegate di un DocumentoSpesa .
 *
 * @author Domenico
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class GruppoAttivitaIvaDad extends ExtendedBaseDadImpl {
	
	/** The gruppo attivita iva dao. */
	@Autowired
	private GruppoAttivitaIvaDao gruppoAttivitaIvaDao;
	
	/** The siac t iva gruppo repository. */
	@Autowired
	private SiacTIvaGruppoRepository siacTIvaGruppoRepository;
	
	/**
	 * Find gruppo attivita iva by id.
	 *
	 * @param uid the uid
	 * @param annoEsercizio the anno esercizio
	 * @return the gruppo attivita iva
	 */
	public GruppoAttivitaIva findGruppoAttivitaIvaByIdAndAnno(Integer uid, Integer annoEsercizio) {
		final String methodName = "findGruppoAttivitaIvaById";		
		log.debug(methodName, "uid: "+ uid);
		SiacTIvaGruppo siacTIvaGruppo = gruppoAttivitaIvaDao.findById(uid);
		if(siacTIvaGruppo == null) {
			log.debug(methodName, "Impossibile trovare il Gruppo Iva con id: " + uid);
			return null;
		}
		
		GruppoAttivitaIva gruppoAttivitaIva = new GruppoAttivitaIva();
		gruppoAttivitaIva.setAnnualizzazione(annoEsercizio);
		map(siacTIvaGruppo, gruppoAttivitaIva, BilMapId.SiacTIvaGruppo_GruppoAttivitaIva);
		return gruppoAttivitaIva;
	}
	
	/**
	 * Find gruppo attivita iva by codice.
	 *
	 * @param codice the codice
	 * @return the gruppo attivita iva
	 */
	public GruppoAttivitaIva findGruppoAttivitaIvaByCodice(String codice) {
		final String methodName = "findGruppoAttivitaIvaByCodice";		
		log.debug(methodName, "codice: "+ codice);
		SiacTIvaGruppo siacTIvaGruppo = siacTIvaGruppoRepository.findByCodice(codice);
		if(siacTIvaGruppo == null) {
			log.debug(methodName, "Impossibile trovare il Gruppo Iva con codice: " + codice);
		}
		return mapNotNull(siacTIvaGruppo, GruppoAttivitaIva.class, BilMapId.SiacTIvaGruppo_GruppoAttivitaIva_Minimal);
	}
	
	
	
	
	/**
	 * Inserisci gruppo attivita iva.
	 *
	 * @param gruppoAttivitaIva the gruppo attivita iva
	 */
	public void inserisciGruppoAttivitaIva(GruppoAttivitaIva gruppoAttivitaIva) {
		SiacTIvaGruppo siacTIvaGruppo = buildSiacTIvaGruppo(gruppoAttivitaIva);
		gruppoAttivitaIvaDao.create(siacTIvaGruppo);
		gruppoAttivitaIva.setUid(siacTIvaGruppo.getUid());
	}
	
	/**
	 * Aggiorna gruppo attivita iva.
	 *
	 * @param gruppoAttivitaIva the gruppo attivita iva
	 * @param annoEsercizio the anno esercizio
	 */
	public void aggiornaGruppoAttivitaIva(GruppoAttivitaIva gruppoAttivitaIva) {
		SiacTIvaGruppo siacTIvaGruppo = buildSiacTIvaGruppo(gruppoAttivitaIva);
		gruppoAttivitaIvaDao.update(siacTIvaGruppo);
		gruppoAttivitaIva.setUid(siacTIvaGruppo.getUid());
	}
	
	
	/**
	 * Ricerca sintetica gruppo attivita iva.
	 *
	 * @param gruppoAttivitaIva the gruppo attivita iva
	 * @param anno the anno
	 * @param parametriPaginazione the parametri paginazione
	 * @return the lista paginata
	 */
	public ListaPaginata<GruppoAttivitaIva> ricercaSinteticaGruppoAttivitaIva(GruppoAttivitaIva gruppoAttivitaIva,Integer anno, ParametriPaginazione parametriPaginazione) {
		
		Page<SiacTIvaGruppo> lista = gruppoAttivitaIvaDao.ricercaSinteticaGruppoAttivitaIva(
				gruppoAttivitaIva.getEnte().getUid(),
				gruppoAttivitaIva.getCodice(),
				gruppoAttivitaIva.getDescrizione(),
				gruppoAttivitaIva.getTipoAttivita()!=null?gruppoAttivitaIva.getTipoAttivita().getCodice():null,
				toPageable(parametriPaginazione));
		
		GruppoAttivitaIva baseInstance = new GruppoAttivitaIva();
		baseInstance.setAnnualizzazione(anno);
		
		return toListaPaginata(lista, baseInstance, BilMapId.SiacTIvaGruppo_GruppoAttivitaIva_Base);
		
	}
	
	/**
	 * Ricerca gruppo attivita iva.
	 *
	 * @param gruppoAttivitaIva the gruppo attivita iva
	 * @return the list
	 */
	public List<GruppoAttivitaIva> ricercaGruppoAttivitaIva(GruppoAttivitaIva gruppoAttivitaIva) {
		
		List<SiacTIvaGruppo> lista = siacTIvaGruppoRepository.findByEnteProprietario(gruppoAttivitaIva.getEnte().getUid());
		
		return convertiLista(lista, GruppoAttivitaIva.class, BilMapId.SiacTIvaGruppo_GruppoAttivitaIva_Minimal);
		
	}
	

	/**
	 * Elimina gruppo attivita iva.
	 *
	 * @param gruppo the gruppo
	 */
	public void eliminaGruppoAttivitaIva(GruppoAttivitaIva gruppo) {
		SiacTIvaGruppo siacTIvaGruppo = new SiacTIvaGruppo();
		siacTIvaGruppo.setUid(gruppo.getUid());
		gruppoAttivitaIvaDao.delete(siacTIvaGruppo);
		
	}
	
	/**
	 * Builds the siac t iva gruppo.
	 *
	 * @param gruppoAttivitaIva the gruppo attivita iva
	 * @param annoEsercizio the anno eserciziob
	 * @return the siac t iva gruppo
	 */
	private SiacTIvaGruppo buildSiacTIvaGruppo(GruppoAttivitaIva gruppoAttivitaIva) {
		SiacTIvaGruppo siacTIvaGruppo = new SiacTIvaGruppo();
		siacTIvaGruppo.setLoginOperazione(loginOperazione);
		gruppoAttivitaIva.setLoginOperazione(loginOperazione);
		map(gruppoAttivitaIva, siacTIvaGruppo, BilMapId.SiacTIvaGruppo_GruppoAttivitaIva);
		return siacTIvaGruppo;
	}

	/**
	 * Ottiene la lista delle annualizzazioni del gruppo attivita iva a partire dall'uid della stessa.
	 * 
	 * @param uid l'uid del gruppo
	 * @return le annualizzazioni
	 */
	public List<Integer> findAnnualizzazioniById(Integer uidGruppoAttivitaIva) {
		return siacTIvaGruppoRepository.findAnnualizzazioniByUid(uidGruppoAttivitaIva);
	}

}
