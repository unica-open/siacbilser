/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.integration.dao.PrimaNotaDao;
import it.csi.siac.siacbilser.integration.dao.SiacDEventoRepository;
import it.csi.siac.siacbilser.integration.dao.cespiti.DismissioneCespiteDao;
import it.csi.siac.siacbilser.integration.dao.cespiti.SiacTCespitiDismissioniRepository;
import it.csi.siac.siacbilser.integration.dao.cespiti.SiacTCespitiElencoDismissioniNumRepository;
import it.csi.siac.siacbilser.integration.dao.cespiti.jpql.EntitaCollegatePrimaNotaInventarioJpqlEnum;
import it.csi.siac.siacbilser.integration.entity.SiacDCausaleEpTipo;
import it.csi.siac.siacbilser.integration.entity.SiacDEvento;
import it.csi.siac.siacbilser.integration.entity.SiacRCespitiDismissioniPrimaNota;
import it.csi.siac.siacbilser.integration.entity.SiacTCespitiAmmortamentoDett;
import it.csi.siac.siacbilser.integration.entity.SiacTCespitiDismissioni;
import it.csi.siac.siacbilser.integration.entity.SiacTCespitiElencoDismissioniNum;
import it.csi.siac.siacbilser.integration.entity.SiacTEnteProprietario;
import it.csi.siac.siacbilser.integration.entity.SiacTPrimaNota;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCausaleEpTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDPrimaNotaStatoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.CespMapId;
import it.csi.siac.siacbilser.integration.entitymapping.GenMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.base.Converters;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siaccespser.model.DismissioneCespite;
import it.csi.siac.siaccespser.model.DismissioneCespiteModelDetail;
import it.csi.siac.siaccespser.model.StatoDismissioneCespite;
import it.csi.siac.siaccommonser.integration.entity.SiacTBase;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.PrimaNotaModelDetail;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;
import it.csi.siac.siacgenser.model.TipoCausale;

/**
 * Classe di DAD per il Tipo Bene.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class DismissioneCespiteDad extends ExtendedBaseDadImpl {
	
	@Autowired
	private DismissioneCespiteDao dismissioneCespiteDao;
	
	@Autowired
	private SiacTCespitiDismissioniRepository siacTCespitiDismissioniRepository;
	@Autowired
	private SiacDEventoRepository siacDEventoRepository;
	@Autowired
	private SiacTCespitiElencoDismissioniNumRepository siacTCespitiElencoDismissioniNumRepository;
	@Autowired
	private PrimaNotaDao primaNotaDao;
	
	/**
	 * Inserisci cespite.
	 *
	 * @param dismissioneCespite the cespite
	 * @return the cespite
	 */
	public void inserisciDismissioneCespite(DismissioneCespite dismissioneCespite){
		dismissioneCespite.setEnte(ente);
		dismissioneCespite.setLoginCreazione(loginOperazione);
		dismissioneCespite.setLoginModifica(loginOperazione);
		
		SiacTCespitiDismissioni siacTDismissioneCespite = buildSiacTCespitiDismissioni(dismissioneCespite);
		
		dismissioneCespiteDao.create(siacTDismissioneCespite);
		dismissioneCespite.setUid(siacTDismissioneCespite.getUid());
	}
	
	/**
	 * Builds the siac T cespiti.
	 *
	 * @param dismissioneCespite the cespite
	 * @return the siac T cespiti
	 */
	private SiacTCespitiDismissioni buildSiacTCespitiDismissioni(DismissioneCespite dismissioneCespite) {
		SiacTCespitiDismissioni siacTDismissioneCespite = new SiacTCespitiDismissioni();
		map(dismissioneCespite,siacTDismissioneCespite,CespMapId.SiacTCespitiDismissioni_DismissioneCespite);
		siacTDismissioneCespite.setLoginOperazione(loginOperazione);
		return siacTDismissioneCespite;	
	}

	
	/**
	 * Stacca numero inventario.
	 *
	 * @param annoBilancio the anno esercizio
	 * @return il numero
	 */
	public Integer staccaNumeroElenco(Integer annoBilancio) {
		SiacTCespitiElencoDismissioniNum siacTCespitiElencoDismissioniNum = siacTCespitiElencoDismissioniNumRepository.findSiacTCespitiElencoDismissioniNumByAnnoAndEnte(annoBilancio,ente.getUid());
		
		Date now = new Date();

		if(siacTCespitiElencoDismissioniNum == null) {
			siacTCespitiElencoDismissioniNum = new SiacTCespitiElencoDismissioniNum();
			siacTCespitiElencoDismissioniNum.setElencoDismissioniAnno(annoBilancio);
			siacTCespitiElencoDismissioniNum.setElencoDismissioniNumero(Integer.valueOf(1));
			SiacTEnteProprietario siacTEnteProprietario = new SiacTEnteProprietario();
			siacTEnteProprietario.setUid(ente.getUid());
			siacTCespitiElencoDismissioniNum.setSiacTEnteProprietario(siacTEnteProprietario);
			siacTCespitiElencoDismissioniNum.setDataCreazione(now);
			siacTCespitiElencoDismissioniNum.setDataInizioValidita(now);
		}
		
		siacTCespitiElencoDismissioniNum.setLoginOperazione(loginOperazione);
		siacTCespitiElencoDismissioniNum.setDataModificaAggiornamento(now);
		siacTCespitiElencoDismissioniNumRepository.saveAndFlush(siacTCespitiElencoDismissioniNum);
		
		return siacTCespitiElencoDismissioniNum.getElencoDismissioniNumero();
	}

	/**
	 * Aggiorna cespite.
	 *
	 * @param dismissioneCespite the cespite
	 * @return the cespite
	 */
	public DismissioneCespite aggiornaDismissioneCespite(DismissioneCespite dismissioneCespite){
		dismissioneCespite.setEnte(ente);
		SiacTCespitiDismissioni siacTDismissioneCespite = buildSiacTCespitiDismissioni(dismissioneCespite);		
		dismissioneCespiteDao.update(siacTDismissioneCespite);
		return dismissioneCespite;
	}

	
	/**
	 * Carica prime note collegate A dismissione.
	 *
	 * @param dismissione the dismissione
	 * @param statoOperativo the stato operativo
	 * @param modelDetails the model details
	 * @return the list
	 */
	public List<PrimaNota> caricaPrimeNoteCollegateADismissione(DismissioneCespite dismissione, StatoOperativoPrimaNota statoOperativo, PrimaNotaModelDetail ...modelDetails) {

		List<SiacTPrimaNota> primeNoteCollegate = dismissioneCespiteDao.ricercaPrimeNoteGenerateDaDismissione(mapToUidIfNotZero(dismissione), null, SiacDPrimaNotaStatoEnum.byStatoOperativo(statoOperativo).getCodice());
		
		return convertiLista(primeNoteCollegate, PrimaNota.class, GenMapId.SiacTPrimaNota_PrimaNota_ModelDetail, Converters.byModelDetails(modelDetails));
	}

	public void eliminaDismissione(DismissioneCespite dismissione) {
		SiacTCespitiDismissioni siacTCespitiDismissioni = new SiacTCespitiDismissioni();
		siacTCespitiDismissioni.setUid(dismissione.getUid());
		siacTCespitiDismissioni.setLoginOperazione(loginOperazione);
		siacTCespitiDismissioni.setLoginCancellazione(loginOperazione);
		siacTCespitiDismissioni.setDataCancellazioneIfNotSet(new Date());
		dismissioneCespiteDao.delete(siacTCespitiDismissioni);
	}
	
	/**
	 * Find dettaglio cespite by id.
	 *
	 * @param dismissioneCespite the cespite
	 * @param modelDetails the model details
	 * @return the cespite
	 */
	public DismissioneCespite findDismissioneCespiteById(DismissioneCespite dismissioneCespite, DismissioneCespiteModelDetail... modelDetails) {
		final String methodName = "findDismissioneCespiteById";	
		int uid = dismissioneCespite.getUid();
		log.debug(methodName, "uid: "+ uid);
		SiacTCespitiDismissioni siacTDismissioneCespite = dismissioneCespiteDao.findById(dismissioneCespite.getUid());
		
		if(siacTDismissioneCespite == null) {
			log.warn(methodName, "Impossibile trovare il cespite cesito con id: " + uid);
		}
		
	
		if(modelDetails == null) {
			return mapNotNull(siacTDismissioneCespite, DismissioneCespite.class, CespMapId.SiacTCespitiDismissioni_DismissioneCespite);
		}
		return  mapNotNull(siacTDismissioneCespite, DismissioneCespite.class, CespMapId.SiacTCespitiDismissioni_DismissioneCespite_ModelDetail, Converters.byModelDetails(modelDetails));
	}
	

	/**
	 * Ricerca sintetica cespite.
	 * @param cespite 
	 *
	 * @param cespite the cespite
	 * @param tbc the tbc
	 * @param cgc the cgc
	 * @param ds the ds
	 * @param numeroInventarioDa the numero inventario da 
	 * @param numeroInventarioA the numero inventario a
	 * @param listaCespiteModelDetail the listacespite model detail
	 * @param parametriPaginazione the parametri paginazione
	 * @return the lista paginata
	 */
	public ListaPaginata<DismissioneCespite> ricercaSinteticaDismissioneCespite(DismissioneCespite dismissioneCespite, AttoAmministrativo attoAmm, Evento evento, CausaleEP causaleEP, 
			Cespite cespite, DismissioneCespiteModelDetail[] listaCespiteModelDetail, ParametriPaginazione parametriPaginazione){
		
		Page<SiacTCespitiDismissioni> listSiacTCespitiDismissioni = dismissioneCespiteDao.ricercaSinteticaCespite(
				Integer.valueOf(ente.getUid()),
				dismissioneCespite != null && dismissioneCespite.getAnnoElenco() != null && dismissioneCespite.getAnnoElenco() != 0? dismissioneCespite.getAnnoElenco() : null,
				dismissioneCespite != null && dismissioneCespite.getNumeroElenco() != null && dismissioneCespite.getNumeroElenco() != 0? dismissioneCespite.getNumeroElenco() : null,
				dismissioneCespite != null && StringUtils.isNotBlank(dismissioneCespite.getDescrizione())? dismissioneCespite.getDescrizione() : null,
				mapToUidIfNotZero(attoAmm),
				dismissioneCespite != null? dismissioneCespite.getDataCessazione() : null,
				mapToUidIfNotZero(evento),
				mapToUidIfNotZero(causaleEP),
				dismissioneCespite != null && StringUtils.isNotBlank(dismissioneCespite.getDescrizioneStatoCessazione())? dismissioneCespite.getDescrizioneStatoCessazione() : null,
				mapToUidIfNotZero(cespite),
				toPageable(parametriPaginazione)
		);
		return toListaPaginata(listSiacTCespitiDismissioni, DismissioneCespite.class, CespMapId.SiacTCespitiDismissioni_DismissioneCespite_ModelDetail, listaCespiteModelDetail);
	}


	/**
	 * Find evento by uid.
	 *
	 * @param evento the evento
	 * @return the evento
	 */
	public Evento findEventoByUid(Evento evento) {
		SiacDEvento siacDEvento = siacDEventoRepository.findOne(evento.getUid());
		return mapNotNull(siacDEvento, Evento.class, GenMapId.SiacDEvento_Evento);
	}
	
	/**
	 * Checks if is evento libero.
	 *
	 * @param evento the evento
	 * @return true, if is evento libero
	 */
	public boolean isEventoLibero(Evento evento){
		SiacDCausaleEpTipoEnum siacDCausaleEpTipoEnum = SiacDCausaleEpTipoEnum.byTipoCausale(TipoCausale.Libera);
		List<SiacDCausaleEpTipo> siacDCausaleEpTipos = siacDEventoRepository.findSiacDCausaleEpTipoEventoByUidEventoAndCausaleEpTipoCode(evento.getUid(), siacDCausaleEpTipoEnum.getCodice());
		return siacDCausaleEpTipos != null && !siacDCausaleEpTipos.isEmpty();
	}

	/**
	 * Dall'analisi:
	 * Lo stato della dismissione &grave; lo stato della prima nota collegata se presente. Nel caso di più prime note collegate perch&grave; sono stati collegati più cespiti, 
	 * deve essere indicato lo stato DEFINITIVO solo se tutte le prime note sono in stato definitivo, altrimenti si lascia l’indicazione di stato PROVVISORIO.
	 * 
	 *
	 * @param dismissioneCespite the dismissione cespite
	 * @return the stato dismissione cespite
	 */
	public StatoDismissioneCespite ottieniStatoDismissioneDaPrimeNoteCollegate(DismissioneCespite dismissioneCespite) {
		
		String codiceStatoProvvisorio = SiacDPrimaNotaStatoEnum.byStatoOperativo(StatoOperativoPrimaNota.PROVVISORIO).getCodice();
		Long primeNoteProvvisorieCollegate = siacTCespitiDismissioniRepository.countPrimeNoteCollegateByDismissioneIdAndCodeStatoPrimaNota(dismissioneCespite.getUid(), codiceStatoProvvisorio, ente.getUid());
		if(primeNoteProvvisorieCollegate != null && primeNoteProvvisorieCollegate.compareTo(0L) > 0) {
			return StatoDismissioneCespite.PROVVISORIO;
		}
		
		String codiceStatoDefinitivo = SiacDPrimaNotaStatoEnum.byStatoOperativo(StatoOperativoPrimaNota.DEFINITIVO).getCodice();
		Long primeNoteDefinitiveCollegate = siacTCespitiDismissioniRepository.countPrimeNoteCollegateByDismissioneIdAndCodeStatoPrimaNota(dismissioneCespite.getUid(), codiceStatoDefinitivo, ente.getUid());
		if(primeNoteDefinitiveCollegate != null && primeNoteDefinitiveCollegate.compareTo(0L) > 0) {
			return StatoDismissioneCespite.DEFINITIVO;
		}
		
		return StatoDismissioneCespite.NON_DEFINITO;
	}
	
	/**
	 * Aggiorna stato dismissione cespite.
	 *
	 * @param dismissione the dismissione
	 * @return the dismissione cespite
	 */
	public DismissioneCespite aggiornaStatoDismissioneCespite(DismissioneCespite dismissione) {
		final String methodName ="aggiornaStatoDismissioneCespite";
		
		StatoDismissioneCespite statoDismissione = ottieniStatoDismissioneDaPrimeNoteCollegate(dismissione);
		
		log.debug(methodName, "stato nuovo della dismissione = " + statoDismissione.getDescrizione());
		if(dismissione.getStatoDismissioneCespite() != null && dismissione.getStatoDismissioneCespite().equals(statoDismissione)) {
			return dismissione;
		}
	
		dismissione.setStatoDismissioneCespite(statoDismissione);
		SiacTCespitiDismissioni siacTDismissioneCespite = new SiacTCespitiDismissioni();
		map(dismissione,siacTDismissioneCespite,CespMapId.SiacTCespitiDismissioni_DismissioneCespite_ModelDetail, Converters.byModelDetails(DismissioneCespiteModelDetail.Stato));
		siacTDismissioneCespite.setLoginOperazione(loginOperazione);
		dismissioneCespiteDao.aggiornaStato(siacTDismissioneCespite);
		
		return dismissione;
	}

	/**
	 * Collega prime note A dismissione.
	 *
	 * @param dismissione the dismissione
	 * @param primeNoteCollegateADismissione the prime note collegate A dismissione
	 */
	public void collegaPrimaNotaADismissione(DismissioneCespite dismissione, Map<Integer, PrimaNota> mappaDettaglioAmmortamentoPrimaNota) {
		final String methodName ="collegaPrimaNotaADismissione";
		if(mappaDettaglioAmmortamentoPrimaNota == null || mappaDettaglioAmmortamentoPrimaNota.isEmpty()) {
			return;
		}
		SiacTCespitiDismissioni siacTDismissioneCespite = siacTCespitiDismissioniRepository.findOne(dismissione.getUid());
		
		if(siacTDismissioneCespite.getSiacRCespitiDismissioniPrimaNota()==null){
			siacTDismissioneCespite.setSiacRCespitiDismissioniPrimaNota(new ArrayList<SiacRCespitiDismissioniPrimaNota>());
		}
		
		for (Integer uidDettaglioAmmortamento : mappaDettaglioAmmortamentoPrimaNota.keySet()) {
			PrimaNota primaNota = mappaDettaglioAmmortamentoPrimaNota.get(uidDettaglioAmmortamento);
			if(primaNota == null) {
				continue;
			}
			SiacRCespitiDismissioniPrimaNota siacRCespitiDismissioniPrimaNota = new SiacRCespitiDismissioniPrimaNota();
			
			SiacTPrimaNota siacTPrimaNota = new SiacTPrimaNota();
			siacTPrimaNota.setUid(primaNota.getUid());
			siacRCespitiDismissioniPrimaNota.setSiacTPrimaNota(siacTPrimaNota);
			
			SiacTCespitiAmmortamentoDett siacTCespitiAmmortamentoDett = new SiacTCespitiAmmortamentoDett();
			siacTCespitiAmmortamentoDett.setUid(uidDettaglioAmmortamento);
			siacRCespitiDismissioniPrimaNota.setSiacTCespitiAmmortamentoDett(siacTCespitiAmmortamentoDett);
			
			siacRCespitiDismissioniPrimaNota.setSiacTCespitiDismissioni(siacTDismissioneCespite);
			siacRCespitiDismissioniPrimaNota.setSiacTEnteProprietario(siacTDismissioneCespite.getSiacTEnteProprietario());
			siacRCespitiDismissioniPrimaNota.setDataModificaInserimentoIfNotSet(new Date());
			siacRCespitiDismissioniPrimaNota.setLoginOperazione(loginOperazione);
			
			siacTDismissioneCespite.addSiacRCespitiDismissioniPrimaNota(siacRCespitiDismissioniPrimaNota);
		}
		
		log.debug(methodName, "sto per inserire " + siacTDismissioneCespite.getSiacRCespitiDismissioniPrimaNota().size() + " legami.");
		siacTCespitiDismissioniRepository.saveAndFlush(siacTDismissioneCespite);
	}
	
	/**
	 * Annulla prime note collegate A dismissione.
	 *
	 */
	public void annullaPrimeNoteCollegateADismissione(List<PrimaNota> primeNoteCollegate) {
		final String methodName = "annullaPrimeNoteCollegateADismissione";

		for (PrimaNota primaNota : primeNoteCollegate) {
			SiacTPrimaNota tp = new SiacTPrimaNota();
			tp.setUid(primaNota.getUid());
			tp.setLoginOperazione(loginOperazione);
			log.debug(methodName, "Annullo la prima nota, uid: " + tp.getUid());
			primaNotaDao.annulla(tp);
		}
	}
	
	/**
	 * Ottieni dismissione cespite prima nota.
	 *
	 * @param primaNota the prima nota
	 * @param modelDetails the model details
	 * @return the dismissione cespite
	 */
	public DismissioneCespite ottieniDismissioneCespitePrimaNota(PrimaNota primaNota, DismissioneCespiteModelDetail... modelDetails) {
		List<SiacTBase> siacTBases  = primaNotaDao.getEntitaCespiteTramiteJpql(null, null, primaNota.getUid(), ente.getUid(), EntitaCollegatePrimaNotaInventarioJpqlEnum.DismissioneCespite);
		if(siacTBases == null || siacTBases.isEmpty()) {
			return null;
		}
		SiacTCespitiDismissioni siacTCespitiDismissione = (SiacTCespitiDismissioni) siacTBases.get(0);
		return mapNotNull(siacTCespitiDismissione, DismissioneCespite.class, CespMapId.SiacTCespitiDismissioni_DismissioneCespite_ModelDetail, Converters.byModelDetails(modelDetails));
	}
	
}
