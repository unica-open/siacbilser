/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.dao.RichiestaEconomaleDao;
import it.csi.siac.siacbilser.integration.dao.SiacDRichiestaEconTipoRepository;
import it.csi.siac.siacbilser.integration.dao.SiacDTrasportoMezzoRepository;
import it.csi.siac.siacbilser.integration.dao.SiacRAccreditoTipoCassaEconRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTCassaEconStampaRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTClassDao;
import it.csi.siac.siacbilser.integration.dao.SiacTMovimentoNumRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTRichiestaEconNumRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTRichiestaEconRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTRichiestaEconSospesaNumRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDGiustificativoStato;
import it.csi.siac.siacbilser.integration.entity.SiacDRichiestaEconStato;
import it.csi.siac.siacbilser.integration.entity.SiacDTrasportoMezzo;
import it.csi.siac.siacbilser.integration.entity.SiacRAccreditoTipoCassaEcon;
import it.csi.siac.siacbilser.integration.entity.SiacRGiustificativoStato;
import it.csi.siac.siacbilser.integration.entity.SiacRRichiestaEconStato;
import it.csi.siac.siacbilser.integration.entity.SiacTCassaEcon;
import it.csi.siac.siacbilser.integration.entity.SiacTCassaEconStampaValore;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.SiacTEnteProprietario;
import it.csi.siac.siacbilser.integration.entity.SiacTGiustificativoDet;
import it.csi.siac.siacbilser.integration.entity.SiacTMovimentoNum;
import it.csi.siac.siacbilser.integration.entity.SiacTRichiestaEcon;
import it.csi.siac.siacbilser.integration.entity.SiacTRichiestaEconNum;
import it.csi.siac.siacbilser.integration.entity.SiacTRichiestaEconSospesaNum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDGiustificativoStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDRichiestaEconStatoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.integration.entitymapping.CecMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.RichiestaEconomaleImpegnoConverter;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siaccecser.model.CassaEconomale;
import it.csi.siac.siaccecser.model.Giustificativo;
import it.csi.siac.siaccecser.model.MezziDiTrasporto;
import it.csi.siac.siaccecser.model.ModalitaPagamentoDipendente;
import it.csi.siac.siaccecser.model.RendicontoRichiesta;
import it.csi.siac.siaccecser.model.RichiestaEconomale;
import it.csi.siac.siaccecser.model.RichiestaEconomaleModelDetail;
import it.csi.siac.siaccecser.model.StatoOperativoGiustificativi;
import it.csi.siac.siaccecser.model.StatoOperativoRichiestaEconomale;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;

/**
 * Data access delegate di una richiesta economale
 *
 * 
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class RichiestaEconomaleDad extends ExtendedBaseDadImpl {
	
	@Autowired
	private RichiestaEconomaleDao richiestaEconomaleDao;
	@Autowired
	private EnumEntityFactory eef;
	@Autowired
	private SiacDTrasportoMezzoRepository siacDTrasportoMezzoRepository;
	@Autowired
	private SiacTClassDao siacTClassDao;
	@Autowired
	private SiacDRichiestaEconTipoRepository siacDRichiestaEconTipoRepository;
	@Autowired
	private SiacTRichiestaEconNumRepository siacTRichiestaEconNumRepository;
	@Autowired
	private SiacTRichiestaEconSospesaNumRepository siacTRichiestaEconSospesaNumRepository;
	@Autowired
	private SiacTRichiestaEconRepository siacTRichiestaEconRepository;
	@Autowired
	private SiacTMovimentoNumRepository siacTMovimentoNumRepository;
	@Autowired
	private SiacRAccreditoTipoCassaEconRepository siacRAccreditoTipoCassaEconRepository;
	@Autowired
	private SiacTCassaEconStampaRepository siacTCassaEconStampaRepository;
	
	@Autowired
	private RichiestaEconomaleImpegnoConverter richiestaEconomaleImpegnoConverter;

	/**
	 * Inserisce una richiesta economale con i dati passati in input
	 *
	 * @param richiestaEconomale la richiesta economale da inserire
	 */
	public void inserisceRichiestaEconomale(RichiestaEconomale richiestaEconomale) {
		SiacTRichiestaEcon siacTRichiestaEcon = buildSiacTRichiestaEcon(richiestaEconomale);
		richiestaEconomaleDao.create(siacTRichiestaEcon);
		richiestaEconomale.setUid(siacTRichiestaEcon.getUid());
	}
	

	/**
	 * Aggiorna una richiesta economale con i dati passati in input
	 *
	 * @param richiestaEconomale la richiesta economale da aggiornare
	 */
	public void aggiornaRichiestaEconomale(RichiestaEconomale richiestaEconomale) {
		SiacTRichiestaEcon siacTRichiestaEcon = buildSiacTRichiestaEcon(richiestaEconomale);
		richiestaEconomaleDao.update(siacTRichiestaEcon);
		richiestaEconomale.setUid(siacTRichiestaEcon.getUid());
	}
	
	
	/**
	 * Crea l'entity SiacTRichiestaEcon a partire da una RichiestaEconomale
	 *
	 * @param richiestaEconomale la richiesta economale
	 * 
	 * @return siacTRichiestaEcon l'entity SiacTRichiestaEcon
	 */
	private SiacTRichiestaEcon buildSiacTRichiestaEcon(RichiestaEconomale richiestaEconomale) {
		SiacTRichiestaEcon siacTRichiestaEcon = new SiacTRichiestaEcon();
		richiestaEconomale.setLoginOperazione(loginOperazione);
		richiestaEconomale.setEnte(ente);
		roundImporto(richiestaEconomale);
		map(richiestaEconomale,siacTRichiestaEcon, CecMapId.SiacTRichiestaEcon_RichiestaEconomale);
		return siacTRichiestaEcon;
	}

	private void roundImporto(RichiestaEconomale richiestaEconomale) {
		if(richiestaEconomale.getImporto()!=null){
			BigDecimal importo = Utility.roundCurrency(richiestaEconomale.getImporto());
			richiestaEconomale.setImporto(importo);
		}
	}
	
	
	/**
	 * Calcola il numero progressivo da assegnare ad una nuova richiesta economale in base all'anno e alla cassa economale di appartenenza
	 *
	 * @param anno anno di esercizio
	 * @param uidCassaEconomale uid della relativa cassa economale
	 * 
	 * @return numeroRichiestaEconomale
	 */
	public Integer staccaNumeroRichiestaEconomale(Integer anno, Integer uidCassaEconomale) {
		final String methodName = "staccaNumeroRichiestaEconomale";
		log.debug(methodName, loginOperazione);

		SiacTRichiestaEconNum siacTRichiestaEconNum = siacTRichiestaEconNumRepository.findByCassaEconId(uidCassaEconomale, ""+anno, ente.getUid());
		
		Date now = new Date();		
		if(siacTRichiestaEconNum == null) {			
			siacTRichiestaEconNum = new SiacTRichiestaEconNum();
			
			SiacTCassaEcon siacTCassaEcon = new SiacTCassaEcon();
			siacTCassaEcon.setCassaeconId(uidCassaEconomale);			
			siacTRichiestaEconNum.setSiacTCassaEcon(siacTCassaEcon);
			
			siacTRichiestaEconNum.setRiceconAnno("" +anno);
			
			SiacTEnteProprietario siacTEnteProprietario = new SiacTEnteProprietario();
			siacTEnteProprietario.setUid(ente.getUid());
			siacTRichiestaEconNum.setSiacTEnteProprietario(siacTEnteProprietario);
			siacTRichiestaEconNum.setDataCreazione(now);
			siacTRichiestaEconNum.setDataInizioValidita(now);
			//La numerazione parte da 1		
			siacTRichiestaEconNum.setRiceconNumero(1);
		}
		
		siacTRichiestaEconNum.setLoginOperazione(loginOperazione);	
		siacTRichiestaEconNum.setDataModifica(now);	
		
		siacTRichiestaEconNumRepository.saveAndFlush(siacTRichiestaEconNum);
		
		Integer numeroRichiestaEconomale = siacTRichiestaEconNum.getRiceconNumero();
		log.info(methodName, "returning numeroRichiestaEconomale: "+ numeroRichiestaEconomale);
		return numeroRichiestaEconomale;
	}
	
	
	/**
	 * Calcola il numero progressivo da assegnare ad un nuovo sospeso in base all'anno
	 *
	 * @param anno anno di esercizio
	 * 
	 * @return numeroSospeso
	 */
	public Integer staccaNumeroSospeso(Integer anno, CassaEconomale cassaEconomale) {
		final String methodName = "staccaNumeroSospeso";
		log.debug(methodName, loginOperazione);
		SiacTRichiestaEconSospesaNum siacTRichiestaEconSospesaNum = siacTRichiestaEconSospesaNumRepository.findByAnnoAndCassaId(""+anno, cassaEconomale.getUid(), ente.getUid());
		
		Date now = new Date();		
		if(siacTRichiestaEconSospesaNum == null) {			
			siacTRichiestaEconSospesaNum = new SiacTRichiestaEconSospesaNum();
			
			siacTRichiestaEconSospesaNum.setRieconsAnno("" +anno);
			
			SiacTCassaEcon siacTCassaEcon = new SiacTCassaEcon();
			siacTCassaEcon.setCassaeconId(cassaEconomale.getUid());
			siacTRichiestaEconSospesaNum.setSiacTCassaEcon(siacTCassaEcon);
			
			SiacTEnteProprietario siacTEnteProprietario = new SiacTEnteProprietario();
			siacTEnteProprietario.setUid(ente.getUid());
			siacTRichiestaEconSospesaNum.setSiacTEnteProprietario(siacTEnteProprietario);
			siacTRichiestaEconSospesaNum.setDataCreazione(now);
			siacTRichiestaEconSospesaNum.setDataInizioValidita(now);
			//La numerazione parte da 1		
			siacTRichiestaEconSospesaNum.setRieconsNumero(1); 
		}
		
		siacTRichiestaEconSospesaNum.setLoginOperazione(loginOperazione);	
		siacTRichiestaEconSospesaNum.setDataModifica(now);	
		
		siacTRichiestaEconSospesaNumRepository.saveAndFlush(siacTRichiestaEconSospesaNum);
		
		Integer numeroSospeso = siacTRichiestaEconSospesaNum.getRieconsNumero();
		log.info(methodName, "returning numeroSospeso: "+ numeroSospeso);
		return numeroSospeso;
	}
	
	
	/**
	 * Calcola il numero progressivo da assegnare ad un nuovo movimento in base all'anno
	 *
	 * @param anno anno di esercizio
	 * @param cassaEconomale la cassa per cui staccare il numero
	 * 
	 * @return numeroMovimento
	 */
	public Integer staccaNumeroMovimento(Integer anno, CassaEconomale cassaEconomale) {
		final String methodName = "staccaNumeroMovimento";
		log.debug(methodName, loginOperazione);
		SiacTMovimentoNum siacTMovimentoNum = siacTMovimentoNumRepository.findByAnnoAndCassaUid(""+anno, cassaEconomale.getUid(), ente.getUid());
		
		Date now = new Date();		
		if(siacTMovimentoNum == null) {			
			siacTMovimentoNum = new SiacTMovimentoNum();
			
			siacTMovimentoNum.setMovtAnno(""+anno);
			
			SiacTCassaEcon siacTCassaEcon = new SiacTCassaEcon();
			siacTCassaEcon.setCassaeconId(cassaEconomale.getUid());
			siacTMovimentoNum.setSiacTCassaEcon(siacTCassaEcon);
			
			SiacTEnteProprietario siacTEnteProprietario = new SiacTEnteProprietario();
			siacTEnteProprietario.setUid(ente.getUid());
			siacTMovimentoNum.setSiacTEnteProprietario(siacTEnteProprietario);
			siacTMovimentoNum.setDataCreazione(now);
			siacTMovimentoNum.setDataInizioValidita(now);
			//La numerazione parte da 1		
			siacTMovimentoNum.setMovtNumero(1);
		}
		
		siacTMovimentoNum.setLoginOperazione(loginOperazione);	
		siacTMovimentoNum.setDataModifica(now);	
		
		siacTMovimentoNumRepository.saveAndFlush(siacTMovimentoNum);
		
		Integer numeroMovimento = siacTMovimentoNum.getMovtNumero();
		log.info(methodName, "returning numeroMovimento: "+ numeroMovimento);
		return numeroMovimento;
	}
	

	/**
	 * Ricerca i mezzi di trasporto dal database.
	 * 
	 * @return la lista dei mezzi di trasporto
	 */
	public List<MezziDiTrasporto> ricercaMezziDiTrasporto() {
		List<SiacDTrasportoMezzo> siacDTrasportoMezzos = siacDTrasportoMezzoRepository.findByEnteProprietarioId(ente.getUid());
		List<MezziDiTrasporto> mezziDiTrasporto = convertiLista(siacDTrasportoMezzos, MezziDiTrasporto.class, BilMapId.SiacDTrasportoMezzo_MezziDiTrasporto);
		return mezziDiTrasporto;
	}

	
	public List<ClassificatoreGenerico> ricercaClassificatoriGenerici() {
		Calendar cal = Calendar.getInstance();
		int anno = cal.get(Calendar.YEAR);
		
		Collection<String> classificatoreTipoCodes = new HashSet<String>();
		classificatoreTipoCodes.add(TipologiaClassificatore.CLASSIFICATORE_51.name());
		classificatoreTipoCodes.add(TipologiaClassificatore.CLASSIFICATORE_52.name());
		classificatoreTipoCodes.add(TipologiaClassificatore.CLASSIFICATORE_53.name());
		
		List<SiacTClass> siacTClasses = siacTClassDao.findClassifByEnteAndClassifTipoCodes(ente.getUid(), anno, classificatoreTipoCodes);
		return convertiLista(siacTClasses, ClassificatoreGenerico.class, BilMapId.SiacTClass_ClassificatoreGenerico);
	}

	
	/**
	 * Ricerca l'uid di un tipo richiesta economale su database a partire dal codice del tipo e dall'ente.
	 * 
	 * @param codiceTipoRichiesta li codice del tipo richiesta da cercare
	 * 
	 * @return l'uid del tipo richiesta corrispondente al codice in input
	 */
	public Integer findIdTipoRichiestaByCodice(String codiceTipoRichiesta) {
		return siacDRichiestaEconTipoRepository.findUidTipoRichiestaByCodiceEEnte(codiceTipoRichiesta, ente.getUid());
		
	}

	
	/**
	 * Ricerca lo stato operativo di una richiesta economale su database a partire dall'uid della stessa.
	 * 
	 * @param uid l'uid per cui ricercare la richiesta
	 * 
	 * @return lo stato operativo della richiesta corrispondente all'uid, se presente
	 */
	public StatoOperativoRichiestaEconomale findStatoOperativo(Integer uidRichiestaEconomale) {
		SiacDRichiestaEconStato siacDRichiestaEconStato = siacTRichiestaEconRepository.findStatoByIdRichiesta(uidRichiestaEconomale);
		SiacDRichiestaEconStatoEnum siacDRichiestaEconStatoEnum = 
				SiacDRichiestaEconStatoEnum.byCodice(siacDRichiestaEconStato.getRiceconStatoCode());
		return siacDRichiestaEconStatoEnum.getStatoOperativo();
	}

	
	/**
	 * Ricerca la richiesta economale su database a partire dall'uid della stessa.
	 * 
	 * @param uid l'uid per cui ricercare la richiesta
	 * 
	 * @return la richiesta corrispondente all'uid, se presente
	 */
	public RichiestaEconomale findRichiestaEconomaleByUid(Integer uid) {
		SiacTRichiestaEcon siacTRichiestaEcon = richiestaEconomaleDao.findById(uid);
		return mapNotNull(siacTRichiestaEcon, RichiestaEconomale.class, CecMapId.SiacTRichiestaEcon_RichiestaEconomale);
	}
	
	
	/**
	 * Ricerca la richiesta economale su database a partire da richiesta economale e uid cassa economale
	 * 
	 * @param numeroCassa il numero della cassa economale
	 * @param uidCassa    l'uid della cassa
	 * 
	 * @return la richiesta corrispondente ai parametri, se presente
	 */
	public RichiestaEconomale findRichiestaEconomaleByNumeroAndUidCassa(Integer numeroRichiesta, Integer uidCassa,Integer uidBilancio) {
		SiacTRichiestaEcon siacTRichiestaEcon = siacTRichiestaEconRepository.findByRiceconNumeroAndCassaeconId(numeroRichiesta, uidCassa,uidBilancio);
		return mapNotNull(siacTRichiestaEcon, RichiestaEconomale.class, CecMapId.SiacTRichiestaEcon_RichiestaEconomale);
	}

	
	/**
	 * Ricerca su database le richieste economali corrispondenti ai parametri passati in input.
	 * 
	 * @param richiestaEconomale la richiesta economale da cercare
	 * @param dataCreazioneDa l'inizio del range delle date di creazione
	 * @param dataCreazioneA il termine del range delle date di creazione
	 * @param dataMovimentoDa l'inizio del range delle date del movimento
	 * @param dataMovimentoA il termine del range delle date del movimento
	 * @param parametriPaginazione parametri di paginazione per i risultati trovati
	 * 
	 * @return la lista paginata di richieste economali trovate
	 */
	public ListaPaginata<RichiestaEconomale> ricercaSinteticaRichiestaEconomale(RichiestaEconomale richiestaEconomale, Date dataCreazioneDa, Date dataCreazioneA, Date dataMovimentoDa, Date dataMovimentoA, ParametriPaginazione parametriPaginazione) {
		
		Page<SiacTRichiestaEcon> lista = ricercaSinteticaRichiestaEconomaleInner(richiestaEconomale, dataCreazioneDa,
				dataCreazioneA, dataMovimentoDa, dataMovimentoA, parametriPaginazione);
		
		return toListaPaginata(lista, RichiestaEconomale.class, CecMapId.SiacTRichiestaEcon_RichiestaEconomale_Medium);
	}
	
	/**
	 * Ricerca su database le richieste economali corrispondenti ai parametri passati in input.
	 * Rispetto a {@link #ricercaSinteticaRichiestaEconomale(RichiestaEconomale, Date, Date, Date, Date, ParametriPaginazione)} permette di specificare i ModelDetails da restituire.
	 * 
	 * @param richiestaEconomale la richiesta economale da cercare
	 * @param dataCreazioneDa l'inizio del range delle date di creazione
	 * @param dataCreazioneA il termine del range delle date di creazione
	 * @param dataMovimentoDa l'inizio del range delle date del movimento
	 * @param dataMovimentoA il termine del range delle date del movimento
	 * @param parametriPaginazione parametri di paginazione per i risultati trovati
	 * 
	 * @return la lista paginata di richieste economali trovate
	 */
	public ListaPaginata<RichiestaEconomale> ricercaSinteticaModulareRichiestaEconomale(RichiestaEconomale richiestaEconomale, Date dataCreazioneDa, Date dataCreazioneA, Date dataMovimentoDa, Date dataMovimentoA, ParametriPaginazione parametriPaginazione
			, RichiestaEconomaleModelDetail... richiestaEconomaleModelDetails ) {
		
		Page<SiacTRichiestaEcon> lista = ricercaSinteticaRichiestaEconomaleInner(richiestaEconomale, dataCreazioneDa,
				dataCreazioneA, dataMovimentoDa, dataMovimentoA, parametriPaginazione);
		
		return toListaPaginata(lista, RichiestaEconomale.class, CecMapId.SiacTRichiestaEcon_RichiestaEconomale_ModelDetail, richiestaEconomaleModelDetails);
	}
	

	private Page<SiacTRichiestaEcon> ricercaSinteticaRichiestaEconomaleInner(RichiestaEconomale richiestaEconomale,
			Date dataCreazioneDa, Date dataCreazioneA, Date dataMovimentoDa, Date dataMovimentoA,
			ParametriPaginazione parametriPaginazione) {
		Page<SiacTRichiestaEcon> lista = richiestaEconomaleDao.ricercaSinteticaRichiestaEconomale(
				ente.getUid(),
				richiestaEconomale.getBilancio().getUid(),
				richiestaEconomale.getTipoRichiestaEconomale() != null ? richiestaEconomale.getTipoRichiestaEconomale().getUid() : null,
				richiestaEconomale.getCassaEconomale() != null ? richiestaEconomale.getCassaEconomale().getUid() : null,
				richiestaEconomale.getNumeroRichiesta(),
				// SIAC-4497
				dataCreazioneDa,
				dataCreazioneA,
				// SIAC-4552
				dataMovimentoDa,
				dataMovimentoA,
				richiestaEconomale.getSospeso() != null ? richiestaEconomale.getSospeso().getNumeroSospeso() : null,
				richiestaEconomale.getMovimento() != null ? richiestaEconomale.getMovimento().getNumeroMovimento() : null,
				richiestaEconomale.getSoggetto() != null ? richiestaEconomale.getSoggetto().getUid() : null,
				richiestaEconomale.getMatricola(),
				richiestaEconomale.getDescrizioneDellaRichiesta(),
				richiestaEconomale.getStatoOperativoRichiestaEconomale() != null ? SiacDRichiestaEconStatoEnum.byStatoOperativo(richiestaEconomale.getStatoOperativoRichiestaEconomale()) : null,
				richiestaEconomale.getClassificatoriGenerici(),	
				toPageable(parametriPaginazione));
		return lista;
	}
	

	
	public BigDecimal ricercaSinteticaRichiestaEconomaleTotale(RichiestaEconomale richiestaEconomale, Date dataCreazioneDa, Date dataCreazioneA, Date dataMovimentoDa, Date dataMovimentoA) {
		
		BigDecimal totale = richiestaEconomaleDao.ricercaSinteticaRichiestaEconomaleTotale(
				ente.getUid(),
				richiestaEconomale.getBilancio().getUid(),
				richiestaEconomale.getTipoRichiestaEconomale() != null ? richiestaEconomale.getTipoRichiestaEconomale().getUid() : null,
				richiestaEconomale.getCassaEconomale() != null ? richiestaEconomale.getCassaEconomale().getUid() : null,
				richiestaEconomale.getNumeroRichiesta(),
				// SIAC-4497
				dataCreazioneDa,
				dataCreazioneA,
				// SIAC-4552
				dataMovimentoDa,
				dataMovimentoA,
				richiestaEconomale.getSospeso() != null ? richiestaEconomale.getSospeso().getNumeroSospeso() : null,
				richiestaEconomale.getMovimento() != null ? richiestaEconomale.getMovimento().getNumeroMovimento() : null,
				richiestaEconomale.getSoggetto() != null ? richiestaEconomale.getSoggetto().getUid() : null,
				richiestaEconomale.getMatricola(),
				richiestaEconomale.getDescrizioneDellaRichiesta(),
				richiestaEconomale.getStatoOperativoRichiestaEconomale() != null ? SiacDRichiestaEconStatoEnum.byStatoOperativo(richiestaEconomale.getStatoOperativoRichiestaEconomale()) : null,
				richiestaEconomale.getClassificatoriGenerici());
		
		return totale;
	}
	
	
	/**
	 * Aggiorna su database lo stato operativo di una richiesta economale
	 * 
	 * @param richiestaEconomale la richiesta economale da aggiornare
	 * @param statoOperativoRichiestaEconomale il nuovo stato operativo da salvare su database
	 */
	public void aggiornaStatoRichiestaEconomale(RichiestaEconomale richiestaEconomale, StatoOperativoRichiestaEconomale statoOperativoRichiestaEconomale) {
		
		Date now = new Date();
		SiacTRichiestaEcon siacTRichiestaEcon = siacTRichiestaEconRepository.findOne(richiestaEconomale.getUid());
		
		for(SiacRRichiestaEconStato r : siacTRichiestaEcon.getSiacRRichiestaEconStatos()){
			r.setDataCancellazioneIfNotSet(now);		
		}
		
		SiacRRichiestaEconStato siacRRichiestaEconStato = new SiacRRichiestaEconStato();
		SiacDRichiestaEconStato siacDRichiestaEconStato = eef.getEntity(SiacDRichiestaEconStatoEnum.byStatoOperativo(statoOperativoRichiestaEconomale), ente.getUid());
		siacRRichiestaEconStato.setSiacDRichiestaEconStato(siacDRichiestaEconStato);	
		siacRRichiestaEconStato.setSiacTRichiestaEcon(siacTRichiestaEcon);		
		siacRRichiestaEconStato.setSiacTEnteProprietario(siacTRichiestaEcon.getSiacTEnteProprietario());
		siacRRichiestaEconStato.setDataInizioValidita(now);
		siacRRichiestaEconStato.setDataCreazione(now);
		siacRRichiestaEconStato.setDataModifica(now);
		siacRRichiestaEconStato.setLoginOperazione(siacTRichiestaEcon.getLoginOperazione());
		siacTRichiestaEcon.getSiacRRichiestaEconStatos().add(siacRRichiestaEconStato);
		
		//se sto annullando la richiesta annullo anche i suoi giustificativi.
		if(StatoOperativoRichiestaEconomale.ANNULLATA.equals(statoOperativoRichiestaEconomale)){
			for(SiacTGiustificativoDet siacTgiustificativoDet : siacTRichiestaEcon.getSiacTGiustificativoDets()){
				if(siacTgiustificativoDet.getDataCancellazione() == null && siacTgiustificativoDet.getSiacTGiustificativo() == null){
					annullaGiustificativo(siacTgiustificativoDet, now);
				}
			}
			for(Giustificativo g : richiestaEconomale.getGiustificativi()){
				g.setStatoOperativoGiustificativi(StatoOperativoGiustificativi.ANNULLATO);
			}
		}
		
		richiestaEconomale.setStatoOperativoRichiestaEconomale(statoOperativoRichiestaEconomale);
		
	}

	
	private void annullaGiustificativo(SiacTGiustificativoDet siacTGiustificativoDet, Date now) {
		
		for(SiacRGiustificativoStato s : siacTGiustificativoDet.getSiacRGiustificativoStatos()){
			s.setDataCancellazioneIfNotSet(now);	
		}
		
		SiacRGiustificativoStato siacRGiustificativoStato = new SiacRGiustificativoStato();
		SiacDGiustificativoStato siacDGiustificativoStato = eef.getEntity(SiacDGiustificativoStatoEnum.byStatoOperativo(StatoOperativoGiustificativi.ANNULLATO), ente.getUid());
		siacRGiustificativoStato.setSiacDGiustificativoStato(siacDGiustificativoStato);
		siacRGiustificativoStato.setSiacTGiustificativoDet(siacTGiustificativoDet);
		siacRGiustificativoStato.setSiacTEnteProprietario(siacTGiustificativoDet.getSiacTEnteProprietario());
		siacRGiustificativoStato.setDataInizioValidita(now);
		siacRGiustificativoStato.setDataCreazione(now);
		siacRGiustificativoStato.setDataModifica(now);
		siacRGiustificativoStato.setLoginOperazione(siacTGiustificativoDet.getLoginOperazione());
		
		siacTGiustificativoDet.getSiacRGiustificativoStatos().add(siacRGiustificativoStato);
	}

	
	/**
	 * Aggiorna su database il campo descrizione di una richiesta economale
	 * 
	 * @param richiestaEconomale la richiesta economale da aggiornare
	 */
	public void aggiornaDescrizione(RichiestaEconomale richiestaEconomale) {
		SiacTRichiestaEcon siacTRichiestaEcon = siacTRichiestaEconRepository.findOne(richiestaEconomale.getUid());
		siacTRichiestaEcon.setRiceconDesc(richiestaEconomale.getDescrizioneDellaRichiesta());
		siacTRichiestaEcon.setDataModifica(new Date());
	}
	
	public ModalitaPagamentoDipendente findModalitaPagamentoDipendenteByUid(Integer uid) {
		SiacRAccreditoTipoCassaEcon siacRAccreditoTipoCassaEcon = siacRAccreditoTipoCassaEconRepository.findOne(uid);
		return mapNotNull(siacRAccreditoTipoCassaEcon, ModalitaPagamentoDipendente.class, CecMapId.SiacRAccreditoTipoCassaEcon_ModalitaPagamentoDipendente);
	}

	public CassaEconomale findCassaByRichiestaEconomale(RichiestaEconomale richiestaEconomale) {
		SiacTRichiestaEcon siacTRichiestaEcon = siacTRichiestaEconRepository.findOne(richiestaEconomale.getUid());
		return mapNotNull(siacTRichiestaEcon.getSiacTCassaEcon(), CassaEconomale.class, CecMapId.SiacTCassaEcon_CassaEconomale_Minimal);
	}
	
	public CassaEconomale findCassaByRendicontoRichiesta(RendicontoRichiesta rendicontoRichiesta) {
		SiacTRichiestaEcon siacTRichiestaEcon = siacTRichiestaEconRepository.findBySiacTGiustificativo(rendicontoRichiesta.getUid());
		return mapNotNull(siacTRichiestaEcon.getSiacTCassaEcon(), CassaEconomale.class, CecMapId.SiacTCassaEcon_CassaEconomale_Minimal);
	}

	public CapitoloUscitaGestione findCapitoloAssociatoAllaRichiesta(RichiestaEconomale richiestaEconomale) {
		SiacTRichiestaEcon siacTRichiestaEcon = siacTRichiestaEconRepository.findOne(richiestaEconomale.getUid());
		RichiestaEconomale re = new RichiestaEconomale();
		richiestaEconomaleImpegnoConverter.convertFrom(siacTRichiestaEcon, re);
		
		//Il converter popola sia impegno che subimpegno (quest'ultimo se presente) 
		//...ma solo per l'impegno popola il capitolo -> restituisco il capitolo dall'impegno.
		return re.getImpegno()!=null?re.getImpegno().getCapitoloUscitaGestione():null; 
		
		
	}

	public Integer findNumeroRendicontoStampato(Integer richiestaEconomaleId) {
		List<SiacTCassaEconStampaValore> siacTCassaEconStampaValores = siacTCassaEconStampaRepository.findSiacTCassaEconStampaValoreByRiceconIdAndEnteProprietarioId(richiestaEconomaleId, ente.getUid());
		if(siacTCassaEconStampaValores == null || siacTCassaEconStampaValores.isEmpty()) {
			return null;
		}
		
		SiacTCassaEconStampaValore siacTCassaEconStampaValore = siacTCassaEconStampaValores.get(0);
		
		return siacTCassaEconStampaValore.getRenNum();
	}

	public RichiestaEconomale findRichiestePagamentoBySubdocumento(SubdocumentoSpesa subdocumento) {
		SiacTRichiestaEcon siacTRichiestaEcon = siacTRichiestaEconRepository.findRichiestePagamentoBySubdocId(subdocumento.getUid(), "PAGAMENTO");
		return mapNotNull(siacTRichiestaEcon, RichiestaEconomale.class, CecMapId.SiacTRichiestaEcon_RichiestaEconomale);
	}

}
