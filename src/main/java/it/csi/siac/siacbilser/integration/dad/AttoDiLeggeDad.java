/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.model.AttoDiLegge;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacattser.model.ric.RicercaLeggi;
import it.csi.siac.siacbilser.integration.dao.SiacDAttoLeggeTipoRepository;
import it.csi.siac.siacbilser.integration.dao.SiacRBilElemAttoLeggeRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTAttoLeggeRepository;
import it.csi.siac.siacbilser.integration.dao.attodilegge.SiacRBilElemAttoLeggeDao;
import it.csi.siac.siacbilser.integration.dao.attodilegge.SiacTAttoLeggeDao;
import it.csi.siac.siacbilser.integration.dao.elementobilancio.CapitoloDao;
import it.csi.siac.siacbilser.integration.entity.SiacDAttoLeggeTipo;
import it.csi.siac.siacbilser.integration.entity.SiacRBilElemAttoLegge;
import it.csi.siac.siacbilser.integration.entity.SiacTAttoLegge;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entitymapping.AttMapId;
import it.csi.siac.siacbilser.integration.exception.AttoAnnullatoException;
import it.csi.siac.siacbilser.integration.exception.AttoNonTrovatoException;
import it.csi.siac.siacbilser.integration.exception.CapitoloNonTrovatoException;
import it.csi.siac.siacbilser.integration.exception.RelazioneAttoCapitoloNonTrovatoException;
import it.csi.siac.siacbilser.integration.exception.RelazioneEsistenteException;
import it.csi.siac.siacbilser.model.AttoDiLeggeCapitolo;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.ric.RicercaAttiDiLeggeCapitolo;
import it.csi.siac.siaccorser.model.Entita.StatoEntita;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginataImpl;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

/**
 * The Class AttoDiLeggeDad.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class AttoDiLeggeDad extends ExtendedBaseDadImpl {
	
	/** The atto di legge dao. */
	@Autowired
	private SiacTAttoLeggeDao attoDiLeggeDao;
	
	/** The siac t atto legge repository. */
	@Autowired
	private SiacTAttoLeggeRepository siacTAttoLeggeRepository;
	
	/** The siac r bil elem atto legge repository. */
	@Autowired
	private SiacRBilElemAttoLeggeRepository siacRBilElemAttoLeggeRepository;
	
	/** The siac d atto legge tipo repository. */
	@Autowired
	private SiacDAttoLeggeTipoRepository siacDAttoLeggeTipoRepository;
	
	/** The capitolo dao impl. */
	@Autowired
	private CapitoloDao capitoloDaoImpl;
	
	/** The r bil elem atto legge dao. */
	@Autowired
	private SiacRBilElemAttoLeggeDao rBilElemAttoLeggeDao;


	/**
	 * Creates the.
	 *
	 * @param attoDiLegge the atto di legge
	 * @param tipoAtto the tipo atto
	 * @return the atto di legge
	 */
	public AttoDiLegge create(AttoDiLegge attoDiLegge, TipoAtto tipoAtto) {

		SiacTAttoLegge attoDiLeggeDB = buidSiacTAttoLegge(attoDiLegge, tipoAtto);

		if (log.isDebugEnabled()) {			
			log.debug("create","Inserimento di: " + ToStringBuilder.reflectionToString(attoDiLeggeDB, ToStringStyle.MULTI_LINE_STYLE));
		}
		
		SiacDAttoLeggeTipo tipoAttoDB = siacDAttoLeggeTipoRepository.findOne(tipoAtto.getUid());
		
		//Non ha senso: il numero viene passato obbligatoriamente dall'app.
//		if (SiacDAttoLeggeTipoEnum.LeggeRegionale.getCodice().equals(tipoAttoDB.getAttoleggeTipoCode())) {
//			attoDiLeggeDB.setAttoleggeNumero(staccaNumeroAtto(attoDiLegge.getAnno(), SiacTNumeroAttoEnum.ATTO_DI_LEGGE));
//		}
		
		attoDiLeggeDB.setSiacTEnteProprietario(siacTEnteProprietario);
		attoDiLeggeDB.setSiacDAttoLeggeTipo(tipoAttoDB);
		attoDiLeggeDB.setAttoleggeId(null);
		
		attoDiLeggeDB = attoDiLeggeDao.create(attoDiLeggeDB);
		
		return mapToAttoDiLegge(attoDiLeggeDB);

	}
	
	
	/**
	 * Update.
	 *
	 * @param attoDiLegge the atto di legge
	 * @param tipoAtto the tipo atto
	 * @return the atto di legge
	 * @throws AttoNonTrovatoException the atto non trovato exception
	 */
	public AttoDiLegge update(AttoDiLegge attoDiLegge, TipoAtto tipoAtto) throws AttoNonTrovatoException {
		
		SiacTAttoLegge attoDiLeggeDB = recuperaAttoLegge(attoDiLegge.getUid());
		
		if (attoDiLeggeDB == null){
			throw new AttoNonTrovatoException();
		}
		
		if (tipoAtto != null && tipoAtto.getUid() != 0) {
		
			SiacDAttoLeggeTipo tipoAttoDB = siacDAttoLeggeTipoRepository.findOne(tipoAtto.getUid());
			
			if (tipoAttoDB != null) {
				attoDiLeggeDB.setSiacDAttoLeggeTipo(tipoAttoDB);
			}
		}

		attoDiLeggeDB.setAttoleggeArticolo(attoDiLegge.getArticolo());
		attoDiLeggeDB.setAttoleggeComma(attoDiLegge.getComma());
		attoDiLeggeDB.setAttoleggePunto(attoDiLegge.getPunto());
		attoDiLeggeDB.setDataModificaAggiornamento(new Date());
		
		return mapToAttoDiLegge(attoDiLeggeDB);
	}
	



	/**
	 * Delete.
	 *
	 * @param uid the uid
	 * @return the atto di legge
	 * @throws AttoNonTrovatoException the atto non trovato exception
	 * @throws AttoAnnullatoException the atto annullato exception
	 */
	public AttoDiLegge delete (Integer uid) throws AttoNonTrovatoException, AttoAnnullatoException {
		
		SiacTAttoLegge attoDiLeggeDB = recuperaAttoLegge(uid);
		
		if (attoDiLeggeDB == null){
			throw new AttoNonTrovatoException();
		}
		
		if (attoDiLeggeDB.getDataCancellazione() != null) {
			throw new AttoAnnullatoException();
		}
		
		attoDiLeggeDB.setDataCancellazione(new Date());
		attoDiLeggeDB.setDataFineValidita(new Date());
		attoDiLeggeDB.setDataModificaAggiornamento(new Date());
		
		return mapToAttoDiLegge(attoDiLeggeDB); 
	}
	
	
	/**
	 * Recupera atto legge.
	 *
	 * @param uid the uid
	 * @return the siac t atto legge
	 */
	private SiacTAttoLegge recuperaAttoLegge(Integer uid) {
		
		return attoDiLeggeDao.findById(uid);
		
	}



	/**
	 * Ricerca.
	 *
	 * @param ricercaLeggi the ricerca leggi
	 * @param pp the pp
	 * @return the lista paginata
	 */
	public ListaPaginata<AttoDiLegge> ricerca(RicercaLeggi ricercaLeggi, ParametriPaginazione pp) {
		
						
		Page<SiacTAttoLegge> attiTrovati = siacTAttoLeggeRepository.ricercaAtto(siacTEnteProprietario, 
				mapToString(ricercaLeggi.getAnno()), 
				ricercaLeggi.getComma(), 
				ricercaLeggi.getArticolo(), 
				ricercaLeggi.getNumero(), 
				ricercaLeggi.getPunto(), 
				(ricercaLeggi.getTipoAtto() != null)?ricercaLeggi.getTipoAtto().getUid():0, 
				toPageable(pp, new Sort(Direction.ASC,"attoleggeAnno","attoleggeNumero", "attoleggeArticolo", "attoleggeComma"))
				);
		
		ListaPaginataImpl<AttoDiLegge> result = new ListaPaginataImpl<AttoDiLegge>();
		result.setPaginaCorrente(attiTrovati.getNumber());
		result.setTotaleElementi((int) attiTrovati.getTotalElements());		
		result.setTotalePagine( attiTrovati.getTotalPages());		
			
		for (SiacTAttoLegge siacTAttoLegge : attiTrovati.getContent()) {			
			log.debug("ricerca", "Atto: uid: "+siacTAttoLegge.getUid() 
					+" anno: "+ siacTAttoLegge.getAttoleggeAnno() +" articolo: "+ siacTAttoLegge.getAttoleggeArticolo() 
					+" comma: "+ siacTAttoLegge.getAttoleggeComma() + " punto: "+ siacTAttoLegge.getAttoleggePunto());
			AttoDiLegge attoDiLegge = mapToAttoDiLegge(siacTAttoLegge);
			result.add(attoDiLegge);
		}
		
		log.debug("ricerca", "dimensione result: "+result.size());
		return result;
	
	}
	

	/**
	 * Ricerca puntuale.
	 *
	 * @param attoDiLegge the atto di legge
	 * @param pp the pp
	 * @return the lista paginata
	 */
	public ListaPaginata<AttoDiLegge> ricercaPuntuale(RicercaLeggi attoDiLegge, ParametriPaginazione pp) {
		
		ListaPaginataImpl<AttoDiLegge> result = new ListaPaginataImpl<AttoDiLegge>();
		
		SiacTAttoLegge attoTrovato = siacTAttoLeggeRepository.findOne(attoDiLegge.getUid());
		
		if (attoTrovato == null){
			return result;
		}
		
		result.add(mapToAttoDiLegge(attoTrovato));
		
		return result;
	}
	


	/**
	 * Map to atto di legge.
	 *
	 * @param atto the atto
	 * @return the atto di legge
	 */
	private AttoDiLegge mapToAttoDiLegge(SiacTAttoLegge atto) {
		
		AttoDiLegge attoMappato =  mapNotNull(atto, AttoDiLegge.class, AttMapId.SiacTAttoLegge);
		
		if (atto.getDataCancellazione() != null){
			attoMappato.setStato(StatoEntita.CANCELLATO);
		}else{
			attoMappato.setStato(StatoEntita.VALIDO);
		}
		TipoAtto tipoAtto = new TipoAtto();
		tipoAtto.setUid(atto.getSiacDAttoLeggeTipo().getUid());
		tipoAtto.setCodice(atto.getSiacDAttoLeggeTipo().getAttoleggeTipoCode());
		tipoAtto.setDescrizione(atto.getSiacDAttoLeggeTipo().getAttoleggeTipoDesc());
		
		attoMappato.setTipoAtto(tipoAtto);
		
		return attoMappato;
	}
	
	/**
	 * Ricerca relazioni capitolo.
	 *
	 * @param criterioRicerca the criterio ricerca
	 * @return the list
	 */
	public List<AttoDiLeggeCapitolo> ricercaRelazioniCapitolo(RicercaAttiDiLeggeCapitolo criterioRicerca) {
		
		List<SiacRBilElemAttoLegge> elencoRelazioniDB = siacRBilElemAttoLeggeRepository.findByElemIdAndBilId(criterioRicerca.getCapitolo().getUid(), criterioRicerca.getBilancio().getUid());
		
		List<AttoDiLeggeCapitolo> elencoRelazioniTrovate = new ArrayList<AttoDiLeggeCapitolo>(elencoRelazioniDB.size());
		
		for (SiacRBilElemAttoLegge relazioneDB : elencoRelazioniDB) {
			
			AttoDiLeggeCapitolo relazione = map(relazioneDB, AttoDiLeggeCapitolo.class, AttMapId.SiacRBilElemAttoLegge_AttoDiLeggeCapitolo);
			if (relazioneDB.getDataCancellazione() != null){
				relazione.setStato(StatoEntita.CANCELLATO);
			}else{
				relazione.setStato(StatoEntita.VALIDO);
			}
			relazione.setAttoDiLegge(mapToAttoDiLegge(relazioneDB.getSiacTAttoLegge()));
			
			elencoRelazioniTrovate.add(relazione);
		}
		
		return elencoRelazioniTrovate;
		
	}
	
	/**
	 * Ricerca puntuale relazioni capitolo.
	 *
	 * @param criterioRicerca the criterio ricerca
	 * @return the list
	 */
	public List<AttoDiLeggeCapitolo> ricercaPuntualeRelazioniCapitolo(RicercaAttiDiLeggeCapitolo criterioRicerca) {
		
		SiacRBilElemAttoLegge relazioneDB = siacRBilElemAttoLeggeRepository.findOne(criterioRicerca.getUid());
		
		if (relazioneDB == null){
			return new ArrayList<AttoDiLeggeCapitolo>();
		}
		
		List<AttoDiLeggeCapitolo> elencoRelazioniTrovate = new ArrayList<AttoDiLeggeCapitolo>(1);
		
		AttoDiLeggeCapitolo relazione = map(relazioneDB, AttoDiLeggeCapitolo.class, AttMapId.SiacRBilElemAttoLegge_AttoDiLeggeCapitolo);
		if (relazioneDB.getDataCancellazione() != null){
			relazione.setStato(StatoEntita.CANCELLATO);
		} else {
			relazione.setStato(StatoEntita.VALIDO);
		}
		relazione.setAttoDiLegge(mapToAttoDiLegge(relazioneDB.getSiacTAttoLegge()));
		
		elencoRelazioniTrovate.add(relazione);
		
		return elencoRelazioniTrovate;
		
	}
	
	/**
	 * Ricerca puntuale by id.
	 *
	 * @param uid the uid
	 * @return the atto di legge
	 */
	public AttoDiLegge ricercaPuntualeById (Integer uid) {
		return mapToAttoDiLegge(attoDiLeggeDao.findById(uid));
	}
	
	
	
	/**
	 * Esistono collegamenti bil.
	 *
	 * @param uid the uid
	 * @return true, if successful
	 */
	public boolean esistonoCollegamentiBil(int uid) {
		
		SiacTAttoLegge atto = attoDiLeggeDao.findById(uid);

		return atto.getSiacRBilElemAttoLegges() != null && !atto.getSiacRBilElemAttoLegges().isEmpty();

	}
	
	/**
	 * Gets the tipo atto da codice.
	 *
	 * @param codice the codice
	 * @return the tipo atto da codice
	 */
	public SiacDAttoLeggeTipo getTipoAttoDaCodice(String codice) {
		
		List<SiacDAttoLeggeTipo> result = siacTAttoLeggeRepository.ricercaTipoAtto(codice);
		
		if (result == null || result.isEmpty()){
			throw new IllegalArgumentException("Codice Tipo Atto " + codice +" non trovato");
		}
		
		return result.get(0);
	}
	
	
	/**
	 * Buid siac t atto legge.
	 *
	 * @param attoDiLegge the atto di legge
	 * @param tipoAtto the tipo atto
	 * @return the siac t atto legge
	 */
	private SiacTAttoLegge buidSiacTAttoLegge(AttoDiLegge attoDiLegge, TipoAtto tipoAtto) {
		SiacTAttoLegge attoLegge = new SiacTAttoLegge();
		attoDiLegge.setLoginOperazione(loginOperazione);
		attoLegge.setLoginOperazione(loginOperazione);
		map(attoDiLegge, attoLegge, AttMapId.SiacTAttoLegge);		
		attoLegge.setLoginOperazione(loginOperazione);
		return attoLegge;
	}


	/**
	 * Gets the atto di legge dao.
	 *
	 * @return the atto di legge dao
	 */
	public SiacTAttoLeggeDao getAttoDiLeggeDao() {
		return attoDiLeggeDao;
	}


	/**
	 * Sets the atto di legge dao.
	 *
	 * @param attoDiLeggeDao the new atto di legge dao
	 */
	public void setAttoDiLeggeDao(SiacTAttoLeggeDao attoDiLeggeDao) {
		this.attoDiLeggeDao = attoDiLeggeDao;
	}

	/**
	 * Gets the siac t atto legge repository.
	 *
	 * @return the siac t atto legge repository
	 */
	public SiacTAttoLeggeRepository getSiacTAttoLeggeRepository() {
		return siacTAttoLeggeRepository;
	}



	/**
	 * Sets the siac t atto legge repository.
	 *
	 * @param siacTAttoLeggeRepository the new siac t atto legge repository
	 */
	public void setSiacTAttoLeggeRepository(
			SiacTAttoLeggeRepository siacTAttoLeggeRepository) {
		this.siacTAttoLeggeRepository = siacTAttoLeggeRepository;
	}


	/**
	 * Gets the siac r bil elem atto legge repository.
	 *
	 * @return the siac r bil elem atto legge repository
	 */
	public SiacRBilElemAttoLeggeRepository getSiacRBilElemAttoLeggeRepository() {
		return siacRBilElemAttoLeggeRepository;
	}


	/**
	 * Sets the siac r bil elem atto legge repository.
	 *
	 * @param siacRBilElemAttoLeggeRepository the new siac r bil elem atto legge repository
	 */
	public void setSiacRBilElemAttoLeggeRepository(
			SiacRBilElemAttoLeggeRepository siacRBilElemAttoLeggeRepository) {
		this.siacRBilElemAttoLeggeRepository = siacRBilElemAttoLeggeRepository;
	}


	/**
	 * Creates the relazione atto capitolo.
	 *
	 * @param attoDiLegge the atto di legge
	 * @param attoDiLeggeCapitolo the atto di legge capitolo
	 * @param capitolo the capitolo
	 * @return the atto di legge capitolo
	 * @throws AttoNonTrovatoException the atto non trovato exception
	 * @throws CapitoloNonTrovatoException the capitolo non trovato exception
	 * @throws RelazioneEsistenteException the relazione esistente exception
	 */
	public AttoDiLeggeCapitolo createRelazioneAttoCapitolo(
			AttoDiLegge attoDiLegge, AttoDiLeggeCapitolo attoDiLeggeCapitolo,
			@SuppressWarnings("rawtypes") Capitolo capitolo) throws AttoNonTrovatoException, CapitoloNonTrovatoException, RelazioneEsistenteException {
		
		SiacRBilElemAttoLegge relazionePrecedente = rBilElemAttoLeggeDao.findRelazioneAttoDiLeggeCapitoloById(attoDiLegge.getUid(), capitolo.getUid());
		

		if (relazionePrecedente!=null && relazionePrecedente.getDataCancellazione()==null) {
				SiacTAttoLegge siacTAttoLegge = relazionePrecedente.getSiacTAttoLegge();
				SiacTBilElem siacTBilElem = relazionePrecedente.getSiacTBilElem();
				
				String msg; 
				try{
					msg = String.format("Relazione: %s/%s - %s/%s/%s/%s", siacTAttoLegge.getAttoleggeAnno(), siacTAttoLegge.getAttoleggeNumero(),
							siacTBilElem.getSiacTBil().getSiacTPeriodo().getAnno(), siacTBilElem.getElemCode(), siacTBilElem.getElemCode2(),
							siacTBilElem.getElemCode3());
				}catch(NullPointerException npe){
					 msg = "Relazione";
				}
				throw new RelazioneEsistenteException(msg);
			
		}
		
		SiacRBilElemAttoLegge relazioneDaInserire = map(attoDiLeggeCapitolo, SiacRBilElemAttoLegge.class, AttMapId.SiacRBilElemAttoLegge_AttoDiLeggeCapitolo);
				
		
		relazioneDaInserire.setSiacTEnteProprietario(siacTEnteProprietario);
		relazioneDaInserire.setLoginOperazione(loginOperazione);
		
		relazioneDaInserire.setUid(null);
		
		SiacTBilElem capitoloDB = capitoloDaoImpl.findById(capitolo.getUid());
		
		if (capitoloDB == null){
			throw new CapitoloNonTrovatoException();
		}
		
		relazioneDaInserire.setSiacTBilElem(capitoloDB);
		
		SiacTAttoLegge attoDB = attoDiLeggeDao.findById(attoDiLegge.getUid());
		
		if (attoDB == null){
			throw new AttoNonTrovatoException();
		}
		relazioneDaInserire.setSiacTAttoLegge(attoDB);
		
		relazioneDaInserire.setDataModificaInserimento(new Date());
		
		
		if (relazionePrecedente!= null && relazionePrecedente.getDataCancellazione() != null) {	
			relazioneDaInserire.setUid(relazionePrecedente.getUid());
			relazioneDaInserire.setDataFineValidita(null);
			relazioneDaInserire.setDataCancellazione(null);
			relazioneDaInserire = rBilElemAttoLeggeDao.update(relazioneDaInserire);
		} else {
			rBilElemAttoLeggeDao.create(relazioneDaInserire);
		}
		
		
		return map(relazioneDaInserire, AttoDiLeggeCapitolo.class, AttMapId.SiacRBilElemAttoLegge_AttoDiLeggeCapitolo);
	}


//	//TODO DA SPOSTARE NEL DAO!
//	private SiacRBilElemAttoLegge findRelazioneAttoDiLeggeCapitoloById(AttoDiLegge attoDiLegge, Capitolo capitolo) {
//		List<SiacRBilElemAttoLegge> elencoTrovati = siacRBilElemAttoLeggeRepository.findByElemIdAndAttoleggeId(capitolo.getUid(), attoDiLegge.getUid());		
//		
//		SiacRBilElemAttoLegge result = null;
//		if (elencoTrovati != null && elencoTrovati.size() > 0) {
//			result = elencoTrovati.get(0);
//		
//		}
//		return result;
//	}
	
	/**
 * Find relazione atto di legge capitolo.
 *
 * @param attoDiLegge the atto di legge
 * @param capitolo the capitolo
 * @return the atto di legge capitolo
 */
public AttoDiLeggeCapitolo findRelazioneAttoDiLeggeCapitolo(AttoDiLegge attoDiLegge, @SuppressWarnings("rawtypes") Capitolo capitolo){
		SiacRBilElemAttoLegge siacRBilElemAttoLegge = rBilElemAttoLeggeDao.findRelazioneAttoDiLeggeCapitoloById(attoDiLegge.getUid(), capitolo.getUid());
		
		return map(siacRBilElemAttoLegge, AttoDiLeggeCapitolo.class, AttMapId.SiacRBilElemAttoLegge_AttoDiLeggeCapitolo);
	}

	/**
	 * Update relazione atto capitolo.
	 *
	 * @param attoDiLegge the atto di legge
	 * @param attoDiLeggeCapitolo the atto di legge capitolo
	 * @param capitolo the capitolo
	 * @return the atto di legge capitolo
	 * @throws RelazioneAttoCapitoloNonTrovatoException the relazione atto capitolo non trovato exception
	 */
	public AttoDiLeggeCapitolo updateRelazioneAttoCapitolo(
			AttoDiLegge attoDiLegge, AttoDiLeggeCapitolo attoDiLeggeCapitolo,
			@SuppressWarnings("rawtypes")Capitolo capitolo/*, boolean cancellazione*/) throws RelazioneAttoCapitoloNonTrovatoException {
		
		SiacRBilElemAttoLegge siacRBilElemAttoLegge = rBilElemAttoLeggeDao.findById(attoDiLeggeCapitolo.getUid());
		
		if (siacRBilElemAttoLegge == null){
			throw new RelazioneAttoCapitoloNonTrovatoException();
		}
		
		siacRBilElemAttoLegge.setDataModificaAggiornamento(new Date());		
		siacRBilElemAttoLegge.setFinanziamentoFine(attoDiLeggeCapitolo.getDataFineFinanz());
		siacRBilElemAttoLegge.setFinanziamentoInizio(attoDiLeggeCapitolo.getDataInizioFinanz());			
		siacRBilElemAttoLegge.setDescrizione(attoDiLeggeCapitolo.getDescrizione());		
		siacRBilElemAttoLegge.setGerarchia(attoDiLeggeCapitolo.getGerarchia());

		return map(siacRBilElemAttoLegge, AttoDiLeggeCapitolo.class, AttMapId.SiacRBilElemAttoLegge_AttoDiLeggeCapitolo);
		
	}
	
	/**
	 * Cancella relazione atto capitolo.
	 *
	 * @param attoDiLeggeCapitolo the atto di legge capitolo
	 * @return the atto di legge capitolo
	 * @throws RelazioneAttoCapitoloNonTrovatoException the relazione atto capitolo non trovato exception
	 */
	public AttoDiLeggeCapitolo cancellaRelazioneAttoCapitolo(
			/*AttoDiLegge attoDiLegge,*/ AttoDiLeggeCapitolo attoDiLeggeCapitolo
			/*, Capitolo capitolo, boolean cancellazione*/) throws RelazioneAttoCapitoloNonTrovatoException {
		
		SiacRBilElemAttoLegge siacRBilElemAttoLegge = rBilElemAttoLeggeDao.findById(attoDiLeggeCapitolo.getUid());
		
		if (siacRBilElemAttoLegge == null){
			throw new RelazioneAttoCapitoloNonTrovatoException();
		}
		
			
		siacRBilElemAttoLegge.setDataCancellazione(new Date());
		siacRBilElemAttoLegge.setDataFineValidita(new Date());


		return map(siacRBilElemAttoLegge, AttoDiLeggeCapitolo.class, AttMapId.SiacRBilElemAttoLegge_AttoDiLeggeCapitolo);
		
	}
	
 /*Il numero dell'atto di legge viene passato da fuori e mai "staccato" dal servzio!	
	private Integer staccaNumeroAtto(int anno, SiacTNumeroAttoEnum tipo) {
		final String methodName = "staccaNumeroVariazione";
		
		SiacTNumeroAtto siacTNumeroAtto = siacTNumeroAttoRepository.findByAnnoEnteTipo(anno, ente.getUid(), tipo.getCodice());
		
		Date now = new Date();		
		if(siacTNumeroAtto == null){
			//Nel caso il contatore non sia stato inizializzato per il bilancio in questione viene creato un nuovo record sulla tabella SiacTVariazioneNum
			siacTNumeroAtto = new SiacTNumeroAtto();
			siacTNumeroAtto.setSiacTEnteProprietario(siacTEnteProprietario);
			siacTNumeroAtto.setDataCreazione(now);
			siacTNumeroAtto.setDataInizioValidita(now);
			siacTNumeroAtto.setLoginOperazione(loginOperazione);
			siacTNumeroAtto.setAnnoAtto(anno);
			siacTNumeroAtto.setTipoNumerazione(tipo.getCodice());
			siacTNumeroAtto.setNumeroAtto(50000); //La numerazione parte da 50000
		}
		
		siacTNumeroAtto.setDataModifica(now);
		
		
		siacTNumeroAttoRepository.saveAndFlush(siacTNumeroAtto);
		
		Integer numeroAtto = siacTNumeroAtto.getNumeroAtto();
		log.info(methodName, "returning numero atto: "+ numeroAtto);
		return numeroAtto;
	}*/


	/**
 * Ricerca tipi.
 *
 * @return the list
 */
public List<TipoAtto> ricercaTipi() {

		List<SiacDAttoLeggeTipo> elencoDB = siacDAttoLeggeTipoRepository.elencoTipi(ente.getUid());
		
		List<TipoAtto> elencoTipi = new ArrayList<TipoAtto>(elencoDB.size());
		
		for (SiacDAttoLeggeTipo tipo : elencoDB) {
			TipoAtto tipoAtto = new TipoAtto(tipo.getAttoleggeTipoCode(), tipo.getAttoleggeTipoDesc());
			tipoAtto.setUid(tipo.getUid());
			
			elencoTipi.add(tipoAtto);
		}
		
		return elencoTipi;
	}
}
