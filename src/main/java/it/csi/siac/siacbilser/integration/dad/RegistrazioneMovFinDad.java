/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/*
 * 
 */
package it.csi.siac.siacbilser.integration.dad;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.dao.RegistrazioneMovFinDao;
import it.csi.siac.siacbilser.integration.dao.SiacDEventoRepository;
import it.csi.siac.siacbilser.integration.dao.SiacDEventoTipoRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTRegMovfinRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDCollegamentoTipo;
import it.csi.siac.siacbilser.integration.entity.SiacDEvento;
import it.csi.siac.siacbilser.integration.entity.SiacDRegMovfinStato;
import it.csi.siac.siacbilser.integration.entity.SiacRRegMovfinStato;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.SiacTPdceConto;
import it.csi.siac.siacbilser.integration.entity.SiacTRegMovfin;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDAmbitoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCollegamentoTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDEventoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDRegMovFinStatoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.GenMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.RegistrazioneMovFinAmbitoConverter;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccecser.model.TipoRichiestaEconomale;
import it.csi.siac.siaccommon.util.cache.Cache;
import it.csi.siac.siaccommon.util.cache.MapCache;
import it.csi.siac.siaccommon.util.cache.initializer.ListCacheElementInitializer;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginataImpl;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;
import it.csi.siac.siacgenser.model.RegistrazioneMovFinModelDetail;
import it.csi.siac.siacgenser.model.StatoOperativoRegistrazioneMovFin;
import it.csi.siac.siacgenser.model.TipoCollegamento;
import it.csi.siac.siacgenser.model.TipoEvento;

/**
 * Data access delegate di una RegistrazioneMovFin.
 * 
 * @author Valentina
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class RegistrazioneMovFinDad extends ExtendedBaseDadImpl {
	
	//Repositories...
	@Autowired
	private RegistrazioneMovFinDao registrazioneMovFinDao;
//	@Autowired
//	private SiacRRegMovfinStatoRepository siacRRegMovfinStatoRepository;
	@Autowired
	private SiacTRegMovfinRepository siacTRegMovfinRepository;
	@Autowired
	private SiacDEventoTipoRepository siacDEventoTipoRepository;
	@Autowired
	private SiacDEventoRepository siacDEventoRepository;
	
	//Components...
	@Autowired
	private EnumEntityFactory eef;
	@PersistenceContext
	private EntityManager em;
	
	/**
	 * Find registrazione id.
	 *
	 * @param uid the uid
	 * @return the documento spesa
	 */
	public RegistrazioneMovFin findRegistrazioneMovFinById(Integer uid) {
		final String methodName = "findRegistrazioneMovFinById";		
		log.debug(methodName, "uid: "+ uid);
		SiacTRegMovfin siacTRegMovfin = registrazioneMovFinDao.findById(uid);
		log.debug(methodName, (siacTRegMovfin==null?"Impossibile trovare":"trovata") + " RegistrazioneMovFin con id: " + uid);
		return  mapNotNull(siacTRegMovfin, RegistrazioneMovFin.class, GenMapId.SiacTRegMovfin_RegistrazioneMovFin);
	}
	
	public RegistrazioneMovFin findRegistrazioneMovFinByIdBase(int uid) {
		final String methodName = "findRegistrazioneMovFinById";		
		log.debug(methodName, "uid: "+ uid);
		SiacTRegMovfin siacTRegMovfin = registrazioneMovFinDao.findById(uid);
		log.debug(methodName, (siacTRegMovfin==null?"Impossibile trovare":"trovata") + " RegistrazioneMovFin con id: " + uid);
		return  mapNotNull(siacTRegMovfin, RegistrazioneMovFin.class, GenMapId.SiacTRegMovfin_RegistrazioneMovFin_Base);
	}
	
	public Ambito findAmbitoByRegistrazioneMovFin(Integer uid) {
		// Ottimizzare leggendo direttamente l'ambito?
		SiacTRegMovfin siacTRegMovfin = registrazioneMovFinDao.findById(uid);
		RegistrazioneMovFin tmp = new RegistrazioneMovFin();
		RegistrazioneMovFinAmbitoConverter conv = new RegistrazioneMovFinAmbitoConverter();
		conv.convertFrom(siacTRegMovfin, tmp);
		return tmp.getAmbito();
	}
	
	
	
	public void inserisciRegistrazioneMovFin(RegistrazioneMovFin registrazioneMovFin) {		
		String methodName = "inserisciRegistrazioneMovFin";
		SiacTRegMovfin siacTRegMovfin = buildSiacTRegMovfin(registrazioneMovFin);	
		registrazioneMovFinDao.create(siacTRegMovfin);
		registrazioneMovFin.setUid(siacTRegMovfin.getUid());
		
		log.info(methodName, "inserita RegistrazioneMovFin con uid: " + registrazioneMovFin.getUid());
	}
	
	

	public void aggiornaAnagraficaRegistrazioneMovFin(RegistrazioneMovFin registrazioneMovFin) {
		SiacTRegMovfin siacTRegMovfin = buildSiacTRegMovfin(registrazioneMovFin);	
		registrazioneMovFinDao.update(siacTRegMovfin);
		registrazioneMovFin.setUid(siacTRegMovfin.getUid());
	}	
	
	
	private SiacTRegMovfin buildSiacTRegMovfin(RegistrazioneMovFin registrazioneMovFin) {
		SiacTRegMovfin siacTRegMofin = new SiacTRegMovfin();
		siacTRegMofin.setLoginOperazione(loginOperazione);
		registrazioneMovFin.setLoginOperazione(loginOperazione);
		map(registrazioneMovFin, siacTRegMofin, GenMapId.SiacTRegMovfin_RegistrazioneMovFin);	
		return siacTRegMofin;
	}
	
	
	
	
	//------------------------------------------

	
	/**
	 * Aggiorna lo stato operativo di una registrazione
	 * 
	 * @param uidRegistrazioneMovFin
	 * @param statoOperativoRegistrazioneMovFin
	 */
	public void aggiornaStatoRegistrazioneMovFin(Integer uidRegistrazioneMovFin, StatoOperativoRegistrazioneMovFin statoOperativoRegistrazioneMovFin) {
		
//		SiacTRegMovfin siacTRegMovfin = registrazioneMovFinDao.findById(uidRegistrazioneMovFin);
		SiacTRegMovfin siacTRegMovfin = em.find(SiacTRegMovfin.class, uidRegistrazioneMovFin);
		
		if(siacTRegMovfin == null){
			throw new IllegalArgumentException("Impossibile trovare la registrazione con uid: " +uidRegistrazioneMovFin);
		}
		Date dataCancellazione = new Date();
		if(siacTRegMovfin.getSiacRRegMovfinStatos() != null){
			for(SiacRRegMovfinStato r: siacTRegMovfin.getSiacRRegMovfinStatos()){
				r.setDataCancellazioneIfNotSet(dataCancellazione);					
			}
		}
		
		Date now = new Date();
		SiacRRegMovfinStato siacRRegMovfinStato = new SiacRRegMovfinStato();
		SiacDRegMovfinStato siacDRegMovfinStato = eef.getEntity(SiacDRegMovFinStatoEnum.byStatoOperativo(statoOperativoRegistrazioneMovFin), siacTRegMovfin.getSiacTEnteProprietario().getUid());
		siacRRegMovfinStato.setSiacDRegMovfinStato(siacDRegMovfinStato);
		siacRRegMovfinStato.setSiacTRegMovfin(siacTRegMovfin);			
		siacRRegMovfinStato.setSiacTEnteProprietario(siacTRegMovfin.getSiacTEnteProprietario());
		siacRRegMovfinStato.setDataInizioValidita(now);
		siacRRegMovfinStato.setDataCreazione(now);
		siacRRegMovfinStato.setDataModifica(now);
		siacRRegMovfinStato.setLoginOperazione(loginOperazione);		
		
//		siacRRegMovfinStatoRepository.saveAndFlush(siacRRegMovfinStato);
//		siacTRegMovfinRepository.saveAndFlush(siacTRegMovfin);
		em.persist(siacRRegMovfinStato);
		em.persist(siacTRegMovfin);
		em.flush();
		em.clear();
	}

	
	/**
	 * Associa un contoEP alla registrazione
	 * 
	 * @param uidRegistrazioneMovFin
	 * @param uidConto
	 */
	public void assegnaContoEP(Integer uidRegistrazioneMovFin, Integer uidConto) {
		SiacTRegMovfin siacTRegMovfin = registrazioneMovFinDao.findById(uidRegistrazioneMovFin);
		if(siacTRegMovfin == null){
			throw new IllegalArgumentException("Impossibile trovare la registrazione con uid: " +uidRegistrazioneMovFin);
		}
		SiacTPdceConto siacTPdceConto = new SiacTPdceConto();
		siacTPdceConto.setUid(uidConto);
		siacTRegMovfin.setSiacTPdceConto(siacTPdceConto);
		registrazioneMovFinDao.save(siacTRegMovfin);
	}

	/**
	 * ricerca sintetica registrazione mov fin
	 * 
	 * (utilizzato dal servizio RicercaSinteticaRegistrazioneMovFinService).
	 *
	 * @param registrazioneMovFin the registrazione mov fin
	 * @param tipoEvento the tipo evento
	 * @param evento the evento
	 * @param eventoRegistazioneIniziale the evento registazione iniziale
	 * @param idDocumento the id documento
	 * @param annoMovimento the anno movimento
	 * @param numeroMovimento the numero movimento
	 * @param numeroSubdocumento the numero subdocumento
	 * @param dataRegistrazioneDa the data registrazione da
	 * @param dataRegistrazioneA the data registrazione a
	 * @param capitolo the capitolo
	 * @param soggetto the soggetto
	 * @param movgest the movgest
	 * @param submovgest the submovgest
	 * @param attoAmministrativo the atto amministrativo
	 * @param strutturaAmministrativoContabile the struttura amministrativo contabile
	 * @param statoOperativoRegistrazioneMovFinDaEscludere the stato operativo registrazione mov fin da escludere
	 * @return the lista paginata
	 */
	public List<RegistrazioneMovFin> ricercaSinteticaRegistrazioneMovFin(
			RegistrazioneMovFin registrazioneMovFin,
			TipoEvento tipoEvento,
			Evento evento,
			Evento eventoRegistazioneIniziale,
			Integer idDocumento,
			Integer annoMovimento,
			String numeroMovimento,
			Integer numeroSubdocumento,
			Date dataRegistrazioneDa,
			Date dataRegistrazioneA,
			Capitolo<?,?> capitolo,
			Soggetto soggetto,
			MovimentoGestione movgest,
			MovimentoGestione submovgest,
			AttoAmministrativo attoAmministrativo,
			StrutturaAmministrativoContabile strutturaAmministrativoContabile,
			List<StatoOperativoRegistrazioneMovFin> statoOperativoRegistrazioneMovFinDaEscludere
			) {
		
		List<RegistrazioneMovFin> result = new ArrayList<RegistrazioneMovFin>();
		
		int numeroPagina = 0; //prima pagina e' 0.
		int elementiPerPagina = 50; //a gruppi di 50 elementi per pagina.
		int totaleElementi;
		
		do {
			ParametriPaginazione parametriPaginazione = new ParametriPaginazione(numeroPagina, elementiPerPagina);
			ListaPaginata<RegistrazioneMovFin> l = ricercaSinteticaRegistrazioneMovFin(registrazioneMovFin, tipoEvento, evento, eventoRegistazioneIniziale,
					null, idDocumento, annoMovimento, numeroMovimento, numeroSubdocumento, dataRegistrazioneDa, dataRegistrazioneA,
					statoOperativoRegistrazioneMovFinDaEscludere, capitolo, soggetto, movgest, submovgest, attoAmministrativo, strutturaAmministrativoContabile, parametriPaginazione);
			result.addAll(l);
			totaleElementi = l.getTotaleElementi();
			numeroPagina++;
		} while(result.size() < totaleElementi);

		
		return result;
	}
	
	/**
	 * Ricerca sintetica registrazione mov fin.
	 *
	 * @param registrazioneMovFin the registrazione mov fin
	 * @param tipoCollegamento the tipo collegamento
	 * @param idDocumento the id documento
	 * @param idMovimento the id movimento
	 * @param statiOperativiRegistrazioneMovFinDaEscludere the stati operativi registrazione mov fin da escludere
	 * @param parametriPaginazione the parametri paginazione
	 * @return the lista paginata
	 */
	public ListaPaginata<RegistrazioneMovFin> ricercaSinteticaRegistrazioneMovFin(
			RegistrazioneMovFin registrazioneMovFin,
			TipoCollegamento tipoCollegamento,
			Integer idDocumento,
			List<Integer> idMovimento,
			List<StatoOperativoRegistrazioneMovFin> statiOperativiRegistrazioneMovFinDaEscludere,
			ParametriPaginazione parametriPaginazione) {
		
		Map<Collection<String>, Collection<Integer>> idMovimentoByEventoCode = null;
		if(idMovimento != null && !idMovimento.isEmpty()) {
			idDocumento = null;
			
			idMovimentoByEventoCode = new HashMap<Collection<String>, Collection<Integer>>();
			SiacDCollegamentoTipoEnum sdcte = SiacDCollegamentoTipoEnum.byTipoCollegamento(tipoCollegamento);
			List<SiacDEvento> siacDEventos = siacDEventoRepository.findBySiacDCollegamentoTipoAndEnteProprietario(sdcte.getCodice(), ente.getUid());
			
			List<String> codes = new ArrayList<String>();
			for(SiacDEvento sde : siacDEventos) {
				codes.add(sde.getEventoCode());
			}
			
			idMovimentoByEventoCode.put(codes, idMovimento);
		}
		
		Page<SiacTRegMovfin> siacTRegMovfins = registrazioneMovFinDao.ricercaSinteticaRegistrazioneMovFin(
				ente.getUid(),
				registrazioneMovFin.getBilancio().getUid(),
				SiacDAmbitoEnum.byAmbitoEvenNull(registrazioneMovFin.getAmbito()),
				null,
				null,
				idMovimentoByEventoCode,
				SiacDCollegamentoTipoEnum.byTipoCollegamentoEvenNull(tipoCollegamento),
				idDocumento,
				null,
				null,
				SiacDRegMovFinStatoEnum.byStatoOperativoEvenNull(registrazioneMovFin.getStatoOperativoRegistrazioneMovFin()),
				SiacDRegMovFinStatoEnum.byStatoOperativoEvenNull(statiOperativiRegistrazioneMovFinDaEscludere),
				registrazioneMovFin.getElementoPianoDeiContiAggiornato() != null ? registrazioneMovFin.getElementoPianoDeiContiAggiornato().getUid() : null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				toPageable(parametriPaginazione)
				);
		
		return toListaPaginata(siacTRegMovfins, RegistrazioneMovFin.class, GenMapId.SiacTRegMovfin_RegistrazioneMovFin);
	}

	private List<Integer> findListaIdMovimenti(List<SiacDCollegamentoTipo> siacDCollegamentoTipos, boolean acceptEmpty, Integer annoMovimento, String numeroMovimento,
			Integer numeroSubmovimento) {
		String methodName = "findListaIdMovimenti";
		
		List<Integer> idMovimenti = new ArrayList<Integer>();
		for (SiacDCollegamentoTipo siacDCollegamentoTipo :siacDCollegamentoTipos){
			SiacDCollegamentoTipoEnum tipoCollegamentoEnum = SiacDCollegamentoTipoEnum.byCollegamentoTipoCode(siacDCollegamentoTipo.getCollegamentoTipoCode());
			List<Integer> idms = registrazioneMovFinDao.findListaIdMovimento(ente.getUid(), tipoCollegamentoEnum, annoMovimento, numeroMovimento, numeroSubmovimento);
			log.debug(methodName, "idMovimenti trovati per " + tipoCollegamentoEnum + ":" + idms);
			idMovimenti.addAll(idms);
		}
		
		if(idMovimenti.isEmpty() && !acceptEmpty){
			String stringaSubmovimento = numeroSubmovimento != null ? " e numero submovimento "+ numeroSubmovimento : "";
			
			List<String> collegamentiTipos = new ArrayList<String>(); 
			for (SiacDCollegamentoTipo siacDCollegamentoTipo :siacDCollegamentoTipos){
				collegamentiTipos.add(siacDCollegamentoTipo.getCollegamentoTipoCode());
			}
			String movimentiTipo = StringUtils.join(collegamentiTipos, ", ");
			
			throw new BusinessException(ErroreBil.ERRORE_GENERICO.getErrore("Nessun movimento di tipo " + movimentiTipo + " trovato per anno "+ annoMovimento +" e numero "+ numeroMovimento + stringaSubmovimento));
		}
		return idMovimenti;
	}

	private Map<SiacDCollegamentoTipo, List<SiacDEvento>> findCollegamentoTipoByEventoOTipoEvento(List<SiacDEvento> siacDEventos) {
		Cache<String, List<SiacDEvento>> tmp = new MapCache<String, List<SiacDEvento>>();
		ListCacheElementInitializer<String, SiacDEvento> cei = new ListCacheElementInitializer<String, SiacDEvento>();
		for(SiacDEvento sde : siacDEventos) {
			String key = sde.getSiacDCollegamentoTipo().getCollegamentoTipoCode();
			List<SiacDEvento> eventos = tmp.get(key, cei);
			eventos.add(sde);
		}
		
		Map<SiacDCollegamentoTipo, List<SiacDEvento>> res = new HashMap<SiacDCollegamentoTipo, List<SiacDEvento>>();
		for(Entry<String, List<SiacDEvento>> entry : tmp.entrySet()) {
			SiacDEvento sde = entry.getValue().get(0);
			res.put(sde.getSiacDCollegamentoTipo(), entry.getValue());
		}
		return res;
	}
	
	/**
	 * Ricerca sintetica di una registrazione mov fin.
	 *
	 * @param registrazioneMovFin the registrazione mov fin
	 * @param tipoEvento the tipo evento
	 * @param evento the evento
	 * @param eventoRegistrazioneIniziale the evento registrazione iniziale
	 * @param tipoCollegamento the tipo collegamento
	 * @param idDocumento the id documento
	 * @param annoMovimento the anno movimento
	 * @param numeroMovimento the numero movimento
	 * @param numeroSubmovimento the numero submovimento
	 * @param dataRegistrazioneDa the data registrazione da
	 * @param dataRegistrazioneA the data registrazione A
	 * @param statoOperativoRegistrazioneMovFinDaEscludere the stato operativo registrazione mov fin da escludere
	 * @param parametriPaginazione the parametri paginazione
	 * 
	 * @return la lista paginata ottenuta
	 */
	public ListaPaginata<RegistrazioneMovFin> ricercaSinteticaRegistrazioneMovFin(
			RegistrazioneMovFin registrazioneMovFin, 
			TipoEvento tipoEvento,
			Evento evento,
			Evento eventoRegistrazioneIniziale,
			TipoCollegamento tipoCollegamento,
			Integer idDocumento,
			Integer annoMovimento,
			String numeroMovimento,
			Integer numeroSubmovimento,
			Date dataRegistrazioneDa, 
			Date dataRegistrazioneA, 
			List<StatoOperativoRegistrazioneMovFin> statoOperativoRegistrazioneMovFinDaEscludere,
			ParametriPaginazione parametriPaginazione
			) {
		return ricercaSinteticaRegistrazioneMovFin(registrazioneMovFin, 
				tipoEvento,
				evento,
				eventoRegistrazioneIniziale,
				tipoCollegamento,
				idDocumento,
				annoMovimento,
				numeroMovimento,
				numeroSubmovimento,
				dataRegistrazioneDa, 
				dataRegistrazioneA, 
				statoOperativoRegistrazioneMovFinDaEscludere,
				null, //filtro per capitolo
				null, //filtro per soggetto
				null, //filtro per movgest
				null, //filtro per submovimento
				null, //filtro per attoAmministrativo,
				null, //filtro per StrutturaAmministrativoContabile strutturaAmministrativoContabile,
				parametriPaginazione
				);
	}

	/**
	 * Ricerca sintetica registrazione mov fin.
	 *
	 * @param registrazioneMovFin the registrazione mov fin
	 * @param tipoEvento the tipo evento
	 * @param evento the evento
	 * @param eventoRegistrazioneIniziale the evento registrazione iniziale
	 * @param tipoCollegamento the tipo collegamento
	 * @param idDocumento the id documento
	 * @param annoMovimento the anno movimento
	 * @param numeroMovimento the numero movimento
	 * @param numeroSubmovimento the numero submovimento
	 * @param dataRegistrazioneDa the data registrazione da
	 * @param dataRegistrazioneA the data registrazione A
	 * @param statoOperativoRegistrazioneMovFinDaEscludere the stato operativo registrazione mov fin da escludere
	 * @param capitolo the capitolo
	 * @param soggetto the soggetto
	 * @param movgest the movgest
	 * @param submovgest the submovgest
	 * @param parametriPaginazione the parametri paginazione
	 * @return the lista paginata
	 */
	public ListaPaginata<RegistrazioneMovFin> ricercaSinteticaRegistrazioneMovFin(
			RegistrazioneMovFin registrazioneMovFin, 
			TipoEvento tipoEvento,
			Evento evento,
			Evento eventoRegistrazioneIniziale,
			TipoCollegamento tipoCollegamento,
			Integer idDocumento,
			Integer annoMovimento,
			String numeroMovimento,
			Integer numeroSubmovimento,
			Date dataRegistrazioneDa, 
			Date dataRegistrazioneA, 
			List<StatoOperativoRegistrazioneMovFin> statoOperativoRegistrazioneMovFinDaEscludere,
			Capitolo<?,?> capitolo,
			Soggetto soggetto,
			MovimentoGestione movgest,
			MovimentoGestione submovgest,
			AttoAmministrativo attoAmministrativo,
			StrutturaAmministrativoContabile strutturaAmministrativoContabile,
			ParametriPaginazione parametriPaginazione
			) {
	
		
		Set<String> eventoCodes = new HashSet<String>();
		List<SiacDEvento> siacDEventos = new ArrayList<SiacDEvento>();
		if(evento != null && evento.getUid() != 0) {
			SiacDEvento sde = siacDEventoRepository.findOne(evento.getUid());
			siacDEventos.add(sde);
			eventoCodes.add(sde.getEventoCode());
		}
		
		if(eventoRegistrazioneIniziale != null && StringUtils.isNotBlank(eventoRegistrazioneIniziale.getCodice())){
			SiacDEventoEnum siacDEventoEnum = SiacDEventoEnum.byEventoCodice(eventoRegistrazioneIniziale.getCodice());
			List<SiacDEventoEnum> eventiDaConsiderare = SiacDEventoEnum.byCollegamentoTipoECassaEconomale(siacDEventoEnum.getSiacDCollegamentoTipoEnum(), siacDEventoEnum.isCassaEconomale());
			
			for(SiacDEventoEnum sdee: eventiDaConsiderare){
				eventoCodes.add(sdee.getCodice());
			}
			siacDEventos.addAll(siacDEventoRepository.findByEnteProprietarioIdAndEventoCodes(ente.getUid(), eventoCodes));
		} else if(evento == null && tipoEvento != null && tipoEvento.getUid() != 0) {
			// SIAC-4024
			siacDEventos.addAll(siacDEventoTipoRepository.findSiacDEventoByEventoTipoId(tipoEvento.getUid()));
			for(SiacDEvento sde : siacDEventos) {
				eventoCodes.add(sde.getEventoCode());
			}
		}
		Map<Collection<String>, Collection<Integer>> idMovimentoByEventoCode = null;
		// Se ho il numero ma NON gli eventi lancio un'eccezione
		if(StringUtils.isNotBlank(numeroMovimento) && annoMovimento != null && annoMovimento != 0) {
			if(siacDEventos.isEmpty()) {
				throw new IllegalArgumentException("Necessario selezionare un evento per ricercare per chiave");
			}
			idDocumento = null;
			
			idMovimentoByEventoCode = new HashMap<Collection<String>, Collection<Integer>>();
			Map<SiacDCollegamentoTipo, List<SiacDEvento>> collegamenti = findCollegamentoTipoByEventoOTipoEvento(siacDEventos);
			
			for(Entry<SiacDCollegamentoTipo, List<SiacDEvento>> entry : collegamenti.entrySet()) {
				List<String> codes = new ArrayList<String>();
				for(SiacDEvento sde : entry.getValue()) {
					codes.add(sde.getEventoCode());
				}
				
				List<Integer> uids = findListaIdMovimenti(Arrays.asList(entry.getKey()), true, annoMovimento, numeroMovimento, numeroSubmovimento);
				if(!codes.isEmpty() && !uids.isEmpty()) {
					idMovimentoByEventoCode.put(codes, uids);
				}
				
			}
		}
		
		if(idMovimentoByEventoCode != null && idMovimentoByEventoCode.isEmpty()) {
			//ho specificato anno e numero ma non ho trovato nulla. NON proseguo con la ricerca
			return new ListaPaginataImpl<RegistrazioneMovFin>();
		}
		Set<SiacDCollegamentoTipoEnum> siacDCollegamentoTipoEnums =  findTipiCollegamento(tipoEvento, evento);
		
		
		Page<SiacTRegMovfin> siacTRegMovfins = registrazioneMovFinDao.ricercaSinteticaRegistrazioneMovFin(
				ente.getUid(),
				registrazioneMovFin.getBilancio().getUid(),
				SiacDAmbitoEnum.byAmbitoEvenNull(registrazioneMovFin.getAmbito()),
				evento!=null?evento.getUid():null,
				idMovimentoByEventoCode == null ? eventoCodes : null,
				idMovimentoByEventoCode,
				SiacDCollegamentoTipoEnum.byTipoCollegamentoEvenNull(tipoCollegamento),
				idDocumento,
				dataRegistrazioneDa,
				dataRegistrazioneA,
				SiacDRegMovFinStatoEnum.byStatoOperativoEvenNull(registrazioneMovFin.getStatoOperativoRegistrazioneMovFin()),
				SiacDRegMovFinStatoEnum.byStatoOperativoEvenNull(statoOperativoRegistrazioneMovFinDaEscludere),
				registrazioneMovFin.getElementoPianoDeiContiAggiornato() != null ? registrazioneMovFin.getElementoPianoDeiContiAggiornato().getUid() : null,
				//SIAC-5290
				capitolo != null && capitolo.getUid() != 0 ? capitolo.getUid() : null,
				soggetto != null &&  soggetto.getUid()!= 0? soggetto.getUid() : null,
				movgest != null && movgest.getUid()!= 0 && (submovgest == null || submovgest.getUid() ==0)? movgest.getUid() : null,
				submovgest != null && submovgest.getUid() !=0? submovgest.getUid() : null,
				//SIAC-5799		
				attoAmministrativo!=null && attoAmministrativo.getUid() !=0? attoAmministrativo.getUid() : null,	
				//SIAC-5944
			    strutturaAmministrativoContabile!=null && strutturaAmministrativoContabile.getUid() !=0? strutturaAmministrativoContabile.getUid() : null,	
						
				siacDCollegamentoTipoEnums,				
				toPageable(parametriPaginazione)
				);
		
		return toListaPaginata(siacTRegMovfins, RegistrazioneMovFin.class, GenMapId.SiacTRegMovfin_RegistrazioneMovFin);
	}


	
	
	
	
	public RegistrazioneMovFin ricercaDettaglioRegistrazioneMovFin(int uidRegistrazione) {
		SiacTRegMovfin siacTRegMovfin = registrazioneMovFinDao.findById(uidRegistrazione);
		return mapNotNull(siacTRegMovfin, RegistrazioneMovFin.class , GenMapId.SiacTRegMovfin_RegistrazioneMovFin);
	}

	
	/**
	 * Cerca le registrazioni di tutti gli eventuali accertamenti collegati all'impegno passato in input
	 * @param uidMovimentoGestione
	 */
	public List<RegistrazioneMovFin> findRegistrazioniCollegateAdAccertamentiCollegatiAdImpegno(Integer uidMovimentoGestione) {
		//verificato che per ora in SiacRMovgestTs si trovano gli accertamenti nella colonna siacTMovgestT2 e gli impegni nella colonna siacTMovgestT1
		List<SiacTRegMovfin> siacTRegMovfins = siacTRegMovfinRepository.findRegistrazioniCollegateAdAccertamentiCollegatiAdImpegno(uidMovimentoGestione);
		return convertiLista(siacTRegMovfins, RegistrazioneMovFin.class, GenMapId.SiacTRegMovfin_RegistrazioneMovFin);
	}
	
	
	/**
	 * Determina l'Evento in base ai parametri di ricerca.
	 *
	 * @param tipoCollegamento il tipo collegamento
	 * @param isAggiornamentoRegistrazioneMovFin true se si sta aggiornando una registrazioneMovFin, false se si sta inserendo
	 * @param isRilevanteIva true se si sta agendo su Entita rilevanti iva
	 * @param isTipoIvaPromisqua 
	 * @param isResiduo 
	 * @return the evento
	 */
	public Evento ricercaEventoCensito(TipoCollegamento tipoCollegamento, boolean isAggiornamentoRegistrazioneMovFin, boolean isRilevanteIva, boolean isTipoIvaPromisqua, boolean isResiduo) {
		
		SiacDCollegamentoTipoEnum siacDCollegamentoTipoEnum = SiacDCollegamentoTipoEnum.byTipoCollegamento(tipoCollegamento);
		
		SiacDEventoEnum siacDEventoEnum= SiacDEventoEnum.byCollegamentoTipoAggiornamentoEIVA(siacDCollegamentoTipoEnum, isAggiornamentoRegistrazioneMovFin, isRilevanteIva, isTipoIvaPromisqua, isResiduo);
		SiacDEvento siacDEvento = eef.getEntity(siacDEventoEnum, ente.getUid());
		
		Evento evento = new Evento();
		map(siacDEvento, evento, GenMapId.SiacDEvento_Evento);
		return evento;
	}
	
	public Evento ricercaEventoCensitoNotaCredito(TipoCollegamento tipoCollegamento, boolean isAggiornamentoRegistrazioneMovFin, boolean isRilevanteIva, boolean isTipoIvaPromisqua, boolean isResiduo) {
		
		SiacDCollegamentoTipoEnum siacDCollegamentoTipoEnum = SiacDCollegamentoTipoEnum.byTipoCollegamento(tipoCollegamento);
		
		SiacDEventoEnum siacDEventoEnum = SiacDEventoEnum.byCollegamentoTipoAggiornamentoENotaCredito(siacDCollegamentoTipoEnum, isAggiornamentoRegistrazioneMovFin, isRilevanteIva, isTipoIvaPromisqua, isResiduo);
		SiacDEvento siacDEvento = eef.getEntity(siacDEventoEnum, ente.getUid());
		
		Evento evento = new Evento();
		map(siacDEvento, evento, GenMapId.SiacDEvento_Evento);
		return evento;
	}

	public Evento ricercaEventoCensitoCassaEconomale(TipoCollegamento tipoCollegamento, boolean isAggiornamentoRegistrazioneMovFin) {
		
		SiacDCollegamentoTipoEnum siacDCollegamentoTipoEnum = SiacDCollegamentoTipoEnum.byTipoCollegamento(tipoCollegamento);
		
		SiacDEventoEnum siacDEventoEnum= SiacDEventoEnum.byCollegamentoTipoAggiornamentoECassaEconomale(siacDCollegamentoTipoEnum, isAggiornamentoRegistrazioneMovFin);
		SiacDEvento siacDEvento = eef.getEntity(siacDEventoEnum, ente.getUid());
		
		Evento evento = new Evento();
		map(siacDEvento, evento, GenMapId.SiacDEvento_Evento);
		return evento;
	}
	
	public Evento ricercaEventoCensitoCassaEconomale(TipoCollegamento tipoCollegamento, TipoRichiestaEconomale tipoRichiestaEconomale, boolean isAggiornamentoRegistrazioneMovFin) {
		
		SiacDCollegamentoTipoEnum siacDCollegamentoTipoEnum = SiacDCollegamentoTipoEnum.byTipoCollegamento(tipoCollegamento);
		
		SiacDEventoEnum siacDEventoEnum= SiacDEventoEnum.byCollegamentoTipoAggiornamentoECassaEconomale(siacDCollegamentoTipoEnum, tipoRichiestaEconomale.getCodice(), isAggiornamentoRegistrazioneMovFin);
		SiacDEvento siacDEvento = eef.getEntity(siacDEventoEnum, ente.getUid());
		
		Evento evento = new Evento();
		map(siacDEvento, evento, GenMapId.SiacDEvento_Evento);
		return evento;
	}
	
	
	public Evento ricercaEventoCensitoRisconti(TipoCollegamento tipoCollegamento, boolean isRateo, boolean isInserimento, boolean isAnnoCorrente) {
			
		SiacDCollegamentoTipoEnum siacDCollegamentoTipoEnum = SiacDCollegamentoTipoEnum.byTipoCollegamento(tipoCollegamento);
		
		SiacDEventoEnum siacDEventoEnum= SiacDEventoEnum.byCollegamentoTipoRateiRisconti(siacDCollegamentoTipoEnum, isRateo, isInserimento, isAnnoCorrente);
		SiacDEvento siacDEvento = eef.getEntity(siacDEventoEnum, ente.getUid());
		
		Evento evento = new Evento();
		map(siacDEvento, evento, GenMapId.SiacDEvento_Evento);
		return evento;
	}
	
	public Evento ricercaEventoCensitoRateoRisconti(TipoCollegamento tipoCollegamento, boolean isRateo, boolean isInserimento, boolean isAnnoCorrente) {
		
		SiacDCollegamentoTipoEnum siacDCollegamentoTipoEnum = SiacDCollegamentoTipoEnum.byTipoCollegamento(tipoCollegamento);
		
		SiacDEventoEnum siacDEventoEnum= SiacDEventoEnum.byCollegamentoTipoRateiRisconti(siacDCollegamentoTipoEnum, isRateo, isInserimento, isAnnoCorrente);
		SiacDEvento siacDEvento = eef.getEntity(siacDEventoEnum, ente.getUid());
		
		Evento evento = new Evento();
		map(siacDEvento, evento, GenMapId.SiacDEvento_Evento);
		return evento;
	}
	


//	
//	public TipoRichiestaEconomale ricercaTipoRichiestaEconomaleAssociataAlSubdoc(SubdocumentoSpesa subdoc) {
//		SiacTSubdoc siacTSubdoc
//		return null;
//	}
	
	public Evento ricercaEventoCensitoCassaEconomaleENotaCredito(TipoCollegamento tipoCollegamento, boolean isAggiornamentoRegistrazioneMovFin, boolean isNotaCredito) {
		
		SiacDCollegamentoTipoEnum siacDCollegamentoTipoEnum = SiacDCollegamentoTipoEnum.byTipoCollegamento(tipoCollegamento);
		
		SiacDEventoEnum siacDEventoEnum= SiacDEventoEnum.byCollegamentoTipoAggiornamentoECassaEconomaleENotaCredito(siacDCollegamentoTipoEnum, isAggiornamentoRegistrazioneMovFin, isNotaCredito);
		SiacDEvento siacDEvento = eef.getEntity(siacDEventoEnum, ente.getUid());
		
		Evento evento = new Evento();
		map(siacDEvento, evento, GenMapId.SiacDEvento_Evento);
		return evento;
	}
	
	
	public Evento ricercaEventoCensitoMovimentoFinanziaria(TipoCollegamento tipoCollegamento, String codiceEvento) {
		
		SiacDCollegamentoTipoEnum siacDCollegamentoTipoEnum = SiacDCollegamentoTipoEnum.byTipoCollegamento(tipoCollegamento);
		SiacDEventoEnum siacDEventoEnum= SiacDEventoEnum.byCollegamentoTipoECodice(siacDCollegamentoTipoEnum, codiceEvento);
		SiacDEvento siacDEvento = eef.getEntity(siacDEventoEnum, ente.getUid());
		
		Evento evento = new Evento();
		map(siacDEvento, evento, GenMapId.SiacDEvento_Evento);
		return evento;
	}
	
	/**
	 * Aggiorna il classificatore elementoPianoDeiContiAggiornato in registrazioneMovfin
	 * 
	 * @param registrazioneMovFin
	 */
	public void aggiornaElementoPianoDeiContiAggiornato(RegistrazioneMovFin registrazioneMovFin) {
		SiacTRegMovfin siacTRegMovfin = siacTRegMovfinRepository.findOne(registrazioneMovFin.getUid());
		SiacTClass siacTClassAgg = new SiacTClass();
		siacTClassAgg.setUid(registrazioneMovFin.getElementoPianoDeiContiAggiornato().getUid());
		siacTRegMovfin.setSiacTClass1(siacTClassAgg);
		siacTRegMovfinRepository.flush();
	}
	
	public Long countRegistrazioniByUIdsAndStato(Set<Integer> uidsRegistrazioniMovFin, StatoOperativoRegistrazioneMovFin... stati) {
		final String methodName = "countRegistrazioniByUIdsAndStato";
		
		List<String> codiciStato = new ArrayList<String>();
		for(StatoOperativoRegistrazioneMovFin stato : stati){
			codiciStato.add(SiacDRegMovFinStatoEnum.byStatoOperativo(stato).getCodice());
		}
		
		Long count = siacTRegMovfinRepository.countSiacTRegistrazioneMovFinByUIdsAndStato(uidsRegistrazioniMovFin, codiciStato);
		log.debug(methodName, "result:" + count);
		return count;
	}

	//SIAC-5290
	private Set<SiacDCollegamentoTipoEnum> findTipiCollegamento(TipoEvento tipoEvento, Evento evento) {
		Set<SiacDCollegamentoTipoEnum> res = new HashSet<SiacDCollegamentoTipoEnum>();
		
		if(entitaHasUid(evento)) {
			// Ricerca per evento
			SiacDEvento siacDEvento = siacDEventoRepository.findOne(evento.getUid());
			SiacDCollegamentoTipo siacDCollegamentoTipo = siacDEvento.getSiacDCollegamentoTipo();
			SiacDCollegamentoTipoEnum tipoCollegamentoEnum = SiacDCollegamentoTipoEnum.byCollegamentoTipoCode(siacDCollegamentoTipo.getCollegamentoTipoCode());
			
			res.add(tipoCollegamentoEnum);
		} else if (entitaHasUid(tipoEvento)) {
			List<SiacDCollegamentoTipo> siacDCollegamentoTipos = siacDEventoRepository.findSiacDCollegamentoTipoByEventoTipoId(tipoEvento.getUid());
			Set<SiacDCollegamentoTipoEnum> siacDCollegamentoTipoEnums = new HashSet<SiacDCollegamentoTipoEnum>();
			
			for(SiacDCollegamentoTipo siacDCollegamentoTipo : siacDCollegamentoTipos) {
				siacDCollegamentoTipoEnums.add(SiacDCollegamentoTipoEnum.byCollegamentoTipoCode(siacDCollegamentoTipo.getCollegamentoTipoCode()));
			}
			
			res.addAll(siacDCollegamentoTipoEnums);
		}
		return res;
	}

	public Evento findEventoByRegistrazioneMovFin(RegistrazioneMovFin registrazioneMovFin) {
		List<SiacDEvento> siacDEventos = siacTRegMovfinRepository.findSiacDEventoByRegmovfinId(registrazioneMovFin.getUid());
		for(SiacDEvento de : siacDEventos) {
			return map(de, Evento.class, GenMapId.SiacDEvento_Evento);
		}
		return null;
	}
	
	public List<RegistrazioneMovFin> findRegistrazioniByPrimaNota(PrimaNota primaNota, List<StatoOperativoRegistrazioneMovFin> statiDaEscludere, RegistrazioneMovFinModelDetail... modelDetails){
		List<SiacTRegMovfin> siacTRegMovfins = registrazioneMovFinDao.ricercaRegistrazionByPrimaNota(primaNota.getUid(), SiacDRegMovFinStatoEnum.byStatoOperativoEvenNull(statiDaEscludere));
		return convertiLista(siacTRegMovfins, RegistrazioneMovFin.class, GenMapId.SiacTRegMovfin_RegistrazioneMovFin_Minimal, modelDetails);	
	}
	
}
