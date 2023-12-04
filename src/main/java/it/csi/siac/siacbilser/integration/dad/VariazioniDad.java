/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.dao.SiacDVariazioneTipoRepository;
import it.csi.siac.siacbilser.integration.dao.SiacRBilElemClassRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTBilElemDetVarRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTBilElemRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTPeriodoRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTVariazioneNumRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTVariazioneRepository;
import it.csi.siac.siacbilser.integration.dao.VariazioneImportiCapitoloDao;
import it.csi.siac.siacbilser.integration.dao.componenteimporticapitolo.DettaglioVariazioneComponenteImportiCapitoloDao;
import it.csi.siac.siacbilser.integration.dao.componenteimporticapitolo.SiacTBilElemDetVarCompRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDVariazioneTipo;
import it.csi.siac.siacbilser.integration.entity.SiacRVariazioneStato;
import it.csi.siac.siacbilser.integration.entity.SiacTBil;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElemDetVar;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElemDetVarComp;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElemVar;
import it.csi.siac.siacbilser.integration.entity.SiacTPeriodo;
import it.csi.siac.siacbilser.integration.entity.SiacTVariazione;
import it.csi.siac.siacbilser.integration.entity.SiacTVariazioneNum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemDetTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDClassTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDVariazioneStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDVariazioneTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTBilElemDetVarElemDetFlagEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.VariazioneImportiCapitoloConverter;
import it.csi.siac.siacbilser.integration.entitymapping.converter.base.Converters;
import it.csi.siac.siacbilser.integration.utility.function.SimpleJDBCFunctionInvoker;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.DettaglioVariazioneCodificaCapitolo;
import it.csi.siac.siacbilser.model.DettaglioVariazioneComponenteImportoCapitolo;
import it.csi.siac.siacbilser.model.DettaglioVariazioneComponenteImportoCapitoloModelDetail;
import it.csi.siac.siacbilser.model.DettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.model.RiepilogoDatiVariazioneImportoCapitoloAnno;
import it.csi.siac.siacbilser.model.RiepilogoDatiVariazioneStatoIdVariazione;
import it.csi.siac.siacbilser.model.StatoOperativoVariazioneBilancio;
import it.csi.siac.siacbilser.model.StornoUEB;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.TipoVariazione;
import it.csi.siac.siacbilser.model.VariazioneCodificaCapitolo;
import it.csi.siac.siacbilser.model.VariazioneDiBilancio;
import it.csi.siac.siacbilser.model.VariazioneImportoCapitolo;
import it.csi.siac.siacbilser.model.VariazioneImportoCapitoloModelDetail;
import it.csi.siac.siacbilser.model.VariazioneImportoSingoloCapitolo;
import it.csi.siac.siacbilser.model.ric.SegnoImporti;
import it.csi.siac.siacbilser.model.utils.WrapperComponenteImportiCapitoloVariatiInVariazione;
import it.csi.siac.siacbilser.model.utils.WrapperImportiCapitoloVariatiInVariazione;
import it.csi.siac.siaccommon.util.cache.Cache;
import it.csi.siac.siaccommon.util.cache.CacheElementInitializer;
import it.csi.siac.siaccommon.util.cache.MapCache;
import it.csi.siac.siaccommon.util.cache.initializer.ListCacheElementInitializer;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

/**
 * The Class VariazioniDad.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class VariazioniDad extends ExtendedBaseDadImpl {
	
	/** The bilancio. */
	private Bilancio bilancio;
	
	/** The siac t variazione repository. */
	@Autowired
	private SiacTVariazioneRepository siacTVariazioneRepository;
	//
	/** The siac t variazione num repository. */
	@Autowired
	private SiacTVariazioneNumRepository siacTVariazioneNumRepository;
	
	/** The siac d variazione tipo repository. */
	@Autowired
	private SiacDVariazioneTipoRepository siacDVariazioneTipoRepository;
	
	@Autowired
	private SiacTBilElemRepository siacTBilElemRepository;
	
	@Autowired
	private SiacRBilElemClassRepository siacRBilElemClassRepository;
	
	@Autowired
	private SiacTBilElemDetVarRepository siacTBilElemDetVarRepository;
	@Autowired
	private SiacTBilElemDetVarCompRepository siacTBilElemDetVarCompRepository;
	@Autowired
	private SiacTPeriodoRepository siacTPeriodoRepository;

	/** The variazione importi capitolo dao. */
	@Autowired
	private VariazioneImportiCapitoloDao variazioneImportiCapitoloDao;
	@Autowired
	private DettaglioVariazioneComponenteImportiCapitoloDao dettaglioVariazioneComponenteImportiCapitoloDao;
	
	/** The eef. */
	@Autowired
	private EnumEntityFactory eef;
	
	@Autowired
	private SimpleJDBCFunctionInvoker fi;
	
	@Autowired
	private VariazioneImportiCapitoloConverter variazioneImportiCapitoloConverter;
	

	/**
	 * Instantiates a new variazioni dad.
	 */
	public VariazioniDad() {
		
	}
	
	/**
	 * Find storno ueb by uid.
	 *
	 * @param storno the storno
	 * @return the storno ueb
	 */
	@Transactional
	public StornoUEB findStornoUEBByUid(StornoUEB storno) {
		SiacTVariazione siacTVariazione = siacTVariazioneRepository.findOne(storno.getUid());
		return map(siacTVariazione, StornoUEB.class, BilMapId.SiacTVariazione_StornoUEB);
		
	}
	
	/**
	 * Find variazione importo capitolo by uid.
	 *
	 * @param uidVariazione the uid variazione
	 * @return the variazione importo capitolo
	 */
	@Transactional
	public VariazioneImportoCapitolo findVariazioneImportoCapitoloByUid(Integer uidVariazione) {
		SiacTVariazione siacTVariazione = siacTVariazioneRepository.findOne(uidVariazione);
		return mapNotNull(siacTVariazione, VariazioneImportoCapitolo.class, BilMapId.SiacTVariazione_VariazioneImportoCapitolo);
	}
	
	/**
	 * Find variazione importo capitolo by uid minimal.
	 *
	 * @param uidVariazione the uid variazione
	 * @return the variazione importo capitolo
	 */
	public VariazioneImportoCapitolo findVariazioneImportoCapitoloByUidModelDetail(Integer uidVariazione, VariazioneImportoCapitoloModelDetail... modelDetails) {
		SiacTVariazione siacTVariazione = siacTVariazioneRepository.findOne(uidVariazione);
		return mapNotNull(siacTVariazione, VariazioneImportoCapitolo.class, BilMapId.SiacTVariazione_VariazioneImportoCapitolo_Minimal, Converters.byModelDetails(modelDetails));
	}
	
	/**
	 * Find variazione codifica capitolo by uid.
	 *
	 * @param uidVariazione the uid variazione
	 * @return the variazione codifica capitolo
	 */
	@Transactional
	public VariazioneCodificaCapitolo findVariazioneCodificaCapitoloByUid(Integer uidVariazione) {
		SiacTVariazione siacTVariazione = siacTVariazioneRepository.findOne(uidVariazione);
		return mapNotNull(siacTVariazione, VariazioneCodificaCapitolo.class, BilMapId.SiacTVariazione_VariazioneCodificaCapitolo);
	}
	
	/**
	 * Inserisce una variazione di importo.
	 *
	 * @param storno the storno
	 */
	public void inserisciStorno(StornoUEB storno) {
		SiacTVariazione siacTVariazione = buildSiacTVariazioneStorno(storno/*variazioneImporto, attoAmministrativo, capitoloSorgente, capitoloDestinazione*/);		
		siacTVariazione.setSiacDVariazioneTipo(eef.getEntity(SiacDVariazioneTipoEnum.Storno, ente.getUid(), SiacDVariazioneTipo.class));
		variazioneImportiCapitoloDao.create(siacTVariazione);
		
		storno.setUid(siacTVariazione.getUid());
	}
	
	public void inserisciAnagraficaVariazioneImportoCapitolo(VariazioneImportoCapitolo variazione) {
		variazione.setListaDettaglioVariazioneImporto(null);
		SiacTVariazione siacTVariazione = buildSiacTVariazione(variazione);
		if(siacTVariazione.getSiacRVariazioneStatos()!=null){
			for(SiacRVariazioneStato r : siacTVariazione.getSiacRVariazioneStatos()){
				r.setSiacRBilElemClassVars(null);
				r.setSiacTBilElemDetVars(null);
				r.setSiacTBilElemVars(null);
			}
		}
		variazioneImportiCapitoloDao.create(siacTVariazione);
		variazione.setUid(siacTVariazione.getUid());
	}
	
	@Transactional
	public void aggiornaAnagraficaVariazioneImportoCapitolo(VariazioneImportoCapitolo variazione) {
		// Per evitare che sia mappata e che sia persa
		List<DettaglioVariazioneImportoCapitolo> tmpList = variazione.getListaDettaglioVariazioneImporto();
		variazione.setListaDettaglioVariazioneImporto(null);
		SiacTVariazione siacTVariazioneNew = buildSiacTVariazione(variazione);
		variazione.setListaDettaglioVariazioneImporto(tmpList);
		if(siacTVariazioneNew.getSiacRVariazioneStatos()!=null){
			for(SiacRVariazioneStato r : siacTVariazioneNew.getSiacRVariazioneStatos()){
				r.setSiacRBilElemClassVars(null);
				r.setSiacTBilElemDetVars(null);
				r.setSiacTBilElemVars(null);
				
			}
		}
		variazioneImportiCapitoloDao.updateAnagrafica(siacTVariazioneNew);
	}
	
	
	/**
	 * Aggiorna variazione codifica capitolo.
	 *
	 * @param variazione the variazione
	 */
	@Transactional
	public void aggiornaVariazioneCodificaCapitolo(VariazioneCodificaCapitolo variazione) {	
		SiacTVariazione siacTVariazioneNew = buildSiacTVariazione(variazione);
		variazioneImportiCapitoloDao.update(siacTVariazioneNew);
	}
	
	
	/**
	 * Aggiorna storno.
	 *
	 * @param storno the storno
	 */
	@Transactional
	public void aggiornaStorno(StornoUEB storno) {
		SiacTVariazione siacTVariazioneNew = buildSiacTVariazioneStorno(storno);
		siacTVariazioneNew.setSiacDVariazioneTipo(eef.getEntity(SiacDVariazioneTipoEnum.Storno, ente.getUid(), SiacDVariazioneTipo.class));
		variazioneImportiCapitoloDao.update(siacTVariazioneNew);
	}
	
	/**
	 * Inserisce una variazione di codifica.
	 *
	 * @param variazioneCodifica the variazione codifica
	 */
	public void inserisciVariazioneCodifica(VariazioneCodificaCapitolo variazioneCodifica) {
		
		SiacTVariazione siacTVariazione = buildSiacTVariazione(variazioneCodifica);	
		
		variazioneImportiCapitoloDao.create(siacTVariazione);
		
		variazioneCodifica.setUid(siacTVariazione.getUid());
	
	}


	/**
	 * Ottiene il numero di una nuova variazione.
	 * 
	 * @return numero variazione
	 */
	public Integer staccaNumeroVariazione() {
		final String methodName = "staccaNumeroVariazione";
		
		SiacTVariazioneNum siacTVariazioneNum = siacTVariazioneNumRepository.findByBilancioIdAndEnteProprietarioId(bilancio.getUid(), ente.getUid());
		
		Date now = new Date();
		if(siacTVariazioneNum == null){
			//Nel caso il contatore non sia stato inizializzato per il bilancio in questione viene creato un nuovo record sulla tabella SiacTVariazioneNum
			siacTVariazioneNum = new SiacTVariazioneNum();
			SiacTBil siacTBil = new SiacTBil();
			siacTBil.setBilId(bilancio.getUid());
			log.debug(methodName, "Collegamento al bilancio con uid: " + bilancio.getUid());
			siacTVariazioneNum.setSiacTBil(siacTBil);
			siacTVariazioneNum.setSiacTEnteProprietario(siacTEnteProprietario);
			siacTVariazioneNum.setDataCreazione(now);
			siacTVariazioneNum.setDataInizioValidita(now);
			siacTVariazioneNum.setLoginOperazione(loginOperazione);
			//La numerazione parte da 1
			siacTVariazioneNum.setVariazioneNum(Integer.valueOf(1));
		}
		
		siacTVariazioneNum.setDataModifica(now);
		
		log.debug(methodName, "SiacTVariazioneNum to save: " + ToStringBuilder.reflectionToString(siacTVariazioneNum));
		siacTVariazioneNumRepository.saveAndFlush(siacTVariazioneNum);
		
		Integer numeroVariazione = siacTVariazioneNum.getVariazioneNum();
		log.info(methodName, "returning numeroVariazione: "+ numeroVariazione);
		return numeroVariazione;
	}
	
	
	/**
	 * Builds the siac t variazione.
	 *
	 * @param variazione the variazione
	 * @return the siac t variazione
	 */
	private SiacTVariazione buildSiacTVariazione(VariazioneImportoCapitolo variazione) {
		SiacTVariazione siacTVariazione = new SiacTVariazione();
		siacTVariazione.setLoginOperazione(loginOperazione);
		variazione.setLoginOperazione(loginOperazione);
		map(variazione, siacTVariazione, BilMapId.SiacTVariazione_VariazioneImportoCapitolo);
		mapAttrs(variazione, siacTVariazione);
		return siacTVariazione;
	}
	
	
	/**
	 * Builds the siac t variazione.
	 *
	 * @param variazione the variazione
	 * @return the siac t variazione
	 */
	private SiacTVariazione buildSiacTVariazione(VariazioneCodificaCapitolo variazione) {
		
		SiacTVariazione siacTVariazione = new SiacTVariazione();
		siacTVariazione.setLoginOperazione(loginOperazione);
		variazione.setLoginOperazione(loginOperazione);
		map(variazione, siacTVariazione, BilMapId.SiacTVariazione_VariazioneCodificaCapitolo);
		mapAttrs(variazione, siacTVariazione);
		return siacTVariazione;
	}
	
	
	/**
	 *  
	 * Popola i flag di un Capitolo.
	 * Gli attributi di tipo boolean sono legati ad un elemento di bilancio dalla siac_r_bil_elem_attr
	 * 
	 * 
	 * Da SiacTBilElem  a Capitolo
	 *
	 * @param variazione the variazione
	 */
	public void populateAttrs(VariazioneDiBilancio variazione) {
		// Not done
	}


	/**
	 * Rimappa gli attributi (ad esempio campo Note) da VariazioneImportoCapitolo a SiacTBilElem.
	 *
	 * @param variazione the variazione
	 * @param siacTVariazione the siac t variazione
	 */
	protected void mapAttrs(VariazioneDiBilancio variazione, SiacTVariazione siacTVariazione) {
		// Does nothing
	}
	
	/**
	 * Builds the siac t variazione storno.
	 *
	 * @param storno the storno
	 * @return the siac t variazione
	 */
	private SiacTVariazione buildSiacTVariazioneStorno(StornoUEB storno) {
		SiacTVariazione siacTVariazione = mapNotNull(ente, SiacTVariazione.class, BilMapId.SiacTVariazione_Ente);
		siacTVariazione.setLoginOperazione(loginOperazione);
		mapNotNull(bilancio, siacTVariazione, BilMapId.SiacTVariazione_Bilancio);
		
		storno.setLoginOperazione(loginOperazione);
		//va lanciato dopo il mapping dell'ente
		map(storno, siacTVariazione, BilMapId.SiacTVariazione_StornoUEB);
		
		mapSiacTBilElemStorno(storno.getCapitoloSorgente(), storno.getCapitoloDestinazione(), siacTVariazione);
		
		storno.getAttoAmministrativo().setLoginOperazione(loginOperazione);
		mapNotNull(storno.getAttoAmministrativo(), siacTVariazione, BilMapId.SiacTVariazione_AttoAmministrativo);
		
		return siacTVariazione;
	}


	/**
	 * Map siac t bil elem storno.
	 *
	 * @param capitoloSorgente the capitolo sorgente
	 * @param capitoloDestinazione the capitolo destinazione
	 * @param siacTVariazione the siac t variazione
	 */
	private void mapSiacTBilElemStorno(Capitolo<?, ?> capitoloSorgente, Capitolo<?, ?> capitoloDestinazione, SiacTVariazione siacTVariazione) {
		if(capitoloSorgente.getUid() == 0 || capitoloDestinazione.getUid() == 0){
			return;
		}
		for(SiacRVariazioneStato siacRVariazioneStato : siacTVariazione.getSiacRVariazioneStatos()) {
			
			for (SiacTBilElemDetVar siacTBilElemDetVars : siacRVariazioneStato.getSiacTBilElemDetVars()) {
				SiacTBilElem siacTBilElem = new SiacTBilElem();
				if(siacTBilElemDetVars.getElemDetImporto()!=null && SiacTBilElemDetVarElemDetFlagEnum.Sorgente.getCodice().equals(siacTBilElemDetVars.getElemDetFlag())){
					siacTBilElem.setUid(capitoloSorgente.getUid());
				} else if(siacTBilElemDetVars.getElemDetImporto()!=null && SiacTBilElemDetVarElemDetFlagEnum.Destinazione.getCodice().equals(siacTBilElemDetVars.getElemDetFlag())){
					siacTBilElem.setUid(capitoloDestinazione.getUid());
				}
				siacTBilElemDetVars.setSiacTBilElem(siacTBilElem);
			}
		}
	}
	
	
	public StatoOperativoVariazioneBilancio findStatoOperativoVariazioneDiBilancio(VariazioneImportoCapitolo variazione) {
		final String methodName = "findStatoOperativoVariazioneDiBilancio";
		
		SiacTVariazione siacTVariazione = siacTVariazioneRepository.findOne(variazione.getUid());
		
		for(SiacRVariazioneStato siacRVariazioneStato : siacTVariazione.getSiacRVariazioneStatos()) {
			if(siacRVariazioneStato.getDataCancellazione()!=null){
				continue;
			}
			
			String variazioneStatoTipoCode = siacRVariazioneStato.getSiacDVariazioneStato().getVariazioneStatoTipoCode();
			log.debug(methodName, "VariazioneImportoCapitolo con uid: "+variazione.getUid() + " returning StatoOperativoVariazioneBilancio: "+variazioneStatoTipoCode);
			return SiacDVariazioneStatoEnum.byCodice(variazioneStatoTipoCode).getStatoOperativoVariazioneDiBilancio();
		}
		
		throw new IllegalStateException("Impossibile trovare lo StatoOperativoVariazioneBilancio associato alla VariazioneImportoCapitolo con uid: "+variazione.getUid());
		
	}
	
	/**
	 * Ricerca sintetica storno ueb.
	 *
	 * @param stornoUEB the storno ueb
	 * @param tipoCapitolo the tipo capitolo
	 * @param parametriPaginazione the parametri paginazione
	 * @return the lista paginata
	 */
	@Transactional
	public ListaPaginata<StornoUEB> ricercaSinteticaStornoUEB(StornoUEB stornoUEB, TipoCapitolo tipoCapitolo,  ParametriPaginazione parametriPaginazione) {
		return ricercaSinteticaStornoUEB(stornoUEB, SiacDBilElemTipoEnum.byTipoCapitoloListEvenNull(tipoCapitolo), parametriPaginazione);
	}
	
	
	/**
	 * Ricerca sintetica storno ueb entrata.
	 *
	 * @param stornoUEB the storno ueb
	 * @param parametriPaginazione the parametri paginazione
	 * @return the lista paginata
	 */
	@Transactional
	public ListaPaginata<StornoUEB> ricercaSinteticaStornoUEBEntrata(StornoUEB stornoUEB, ParametriPaginazione parametriPaginazione) {
		
		return ricercaSinteticaStornoUEB(stornoUEB, Arrays.asList(SiacDBilElemTipoEnum.CapitoloEntrataGestione), parametriPaginazione);
	}
	
	/**
	 * Ricerca sintetica storno ueb uscita.
	 *
	 * @param stornoUEB the storno ueb
	 * @param parametriPaginazione the parametri paginazione
	 * @return the lista paginata
	 */
	@Transactional
	public ListaPaginata<StornoUEB> ricercaSinteticaStornoUEBUscita(StornoUEB stornoUEB, ParametriPaginazione parametriPaginazione) {

		return ricercaSinteticaStornoUEB(stornoUEB, Arrays.asList(SiacDBilElemTipoEnum.CapitoloUscitaGestione), parametriPaginazione);
	}

	/**
	 * Ricerca sintetica storno ueb.
	 *
	 * @param stornoUEB the storno ueb
	 * @param bilElemsTipo the bil elems tipo
	 * @param parametriPaginazione the parametri paginazione
	 * @return the lista paginata
	 */
	private ListaPaginata<StornoUEB> ricercaSinteticaStornoUEB(StornoUEB stornoUEB,List<SiacDBilElemTipoEnum> bilElemsTipo,  ParametriPaginazione parametriPaginazione) {
		
		Page<SiacTVariazione> siacTVariazioni = variazioneImportiCapitoloDao.ricercaSinteticaVariazioneDiBilancio(Arrays.asList(SiacDVariazioneTipoEnum.Storno), ente.getUid(),""+bilancio.getAnno(),
				stornoUEB.getNumero(),
				stornoUEB.getDescrizione(),
				// SIAC-6884
				null, //dataAperturaProposta
				null, //dataChiusuraProposta
				null, //direzioneProponenteId
				SiacDVariazioneStatoEnum.byStatoOperativoVariazioneDiBilancioEvenNull(stornoUEB.getStatoOperativoVariazioneDiBilancio()),
				//stornoUEB.getNote(),
				//SiacDVariazioneStatoEnum.byStatoOperativoVariazioneDiBilancio(stornoUEB.getStatoOperativoVariazioneDiBilancio()),
				stornoUEB.getAttoAmministrativo()!=null?stornoUEB.getAttoAmministrativo().getUid():null,
				null,
				null,
				stornoUEB.getCapitoloSorgente()!=null?stornoUEB.getCapitoloSorgente().getUid():null,
				stornoUEB.getCapitoloDestinazione()!=null?stornoUEB.getCapitoloDestinazione().getUid():null,
				bilElemsTipo,
				null,
				null,
				//attoamministrativo variazione di bilancio
				null,
				null,
				false,
				toPageable(parametriPaginazione));
		
		return toListaPaginata(siacTVariazioni,StornoUEB.class, BilMapId.SiacTVariazione_StornoUEB);
	}
	
	/**
	 * Ricerca sintetica variazione importo capitolo.
	 *
	 * @param capitolo the capitolo
	 * @param parametriPaginazione the parametri paginazione
	 * @return the lista paginata
	 */
	public ListaPaginata<VariazioneImportoCapitolo> ricercaSinteticaVariazioneImportoCapitolo(Capitolo<?, ?> capitolo, ParametriPaginazione parametriPaginazione) {
		return ricercaSinteticaVariazioneImportoCapitolo(new VariazioneImportoCapitolo(), capitolo, null, null,false, parametriPaginazione, BilMapId.SiacTVariazione_VariazioneImportoCapitolo);
	}
		
	
	/**
	 * Ricerca sintetica variazione importo capitolo.
	 *
	 * @param variazione the variazione
	 * @param capitolo the capitolo
	 * @param tipiCapitolo the tipi capitolo
	 * @param limitaRisultatiDefinitiveODecentrate 
	 * @param parametriPaginazione the parametri paginazione
	 * @return the lista paginata
	 */
	public ListaPaginata<VariazioneImportoCapitolo> ricercaSinteticaVariazioneImportoCapitolo(VariazioneImportoCapitolo variazione, List<TipoCapitolo> tipiCapitolo, AttoAmministrativo attoAmministrativo, boolean limitaRisultatiDefinitiveODecentrate, ParametriPaginazione parametriPaginazione) {
		return ricercaSinteticaVariazioneImportoCapitolo(variazione, null, tipiCapitolo, attoAmministrativo, limitaRisultatiDefinitiveODecentrate, parametriPaginazione, BilMapId.SiacTVariazione_VariazioneImportoCapitolo_Base_Asincrone);
	}
	
	
	
	private ListaPaginata<VariazioneImportoCapitolo> ricercaSinteticaVariazioneImportoCapitolo(VariazioneImportoCapitolo variazione, Capitolo<?, ?> capitolo, List<TipoCapitolo> tipiCapitolo,
			AttoAmministrativo attoAmministrativo, boolean limitaRisultatiDefinitiveODecentrate, ParametriPaginazione parametriPaginazione, BilMapId siacTVariazione_VariazioneImportoCapitolo) {
		SiacDVariazioneTipoEnum tipoVariazione = SiacDVariazioneTipoEnum.byTipoVariazioneEvenNull(variazione.getTipoVariazione());
		Collection<SiacDVariazioneTipoEnum> sdvtes = new ArrayList<SiacDVariazioneTipoEnum>();
		if(tipoVariazione != null) {
			sdvtes.add(tipoVariazione);
		}
		
		Page<SiacTVariazione> siacTVariazioni = variazioneImportiCapitoloDao.ricercaSinteticaVariazioneDiBilancio(sdvtes, ente.getUid(),""+bilancio.getAnno(),
				variazione.getNumero(),
				variazione.getDescrizione(),
				// SIAC-6884
				variazione.getDataAperturaProposta(), //dataAperturaProposta
				variazione.getDataChiusuraProposta(), //dataChiusuraProposta
				((variazione.getDirezioneProponente() != null) ? variazione.getDirezioneProponente().getUid() : null), //direzioneProponenteId
				SiacDVariazioneStatoEnum.byStatoOperativoVariazioneDiBilancioEvenNull(variazione.getStatoOperativoVariazioneDiBilancio()),
				//stornoUEB.getNote(),
				//SiacDVariazioneStatoEnum.byStatoOperativoVariazioneDiBilancio(stornoUEB.getStatoOperativoVariazioneDiBilancio()),
				mapToUidIfNotZero(variazione.getAttoAmministrativo()),
				mapToUidIfNotZero(capitolo),
				null,
				null,
				null,
				SiacDBilElemTipoEnum.byTipoCapitoloListEvenNull(tipiCapitolo),
				null,
				null,
				mapToUidIfNotZero(variazione.getAttoAmministrativoVariazioneBilancio()),
				mapToUidIfNotZero(attoAmministrativo),
				limitaRisultatiDefinitiveODecentrate,
				toPageable(parametriPaginazione));
		
		return toListaPaginata(siacTVariazioni,VariazioneImportoCapitolo.class, siacTVariazione_VariazioneImportoCapitolo);
	}
	
	/**
	 * Ricerca sintetica variazione codifica capitolo.
	 *
	 * @param capitolo the capitolo
	 * @param parametriPaginazione the parametri paginazione
	 * @return the lista paginata
	 */
	public ListaPaginata<VariazioneCodificaCapitolo> ricercaSinteticaVariazioneCodificaCapitolo(Capitolo<?, ?> capitolo, ParametriPaginazione parametriPaginazione) {
		return ricercaSinteticaVariazioneCodificaCapitolo(new VariazioneCodificaCapitolo(), capitolo, null, parametriPaginazione);
	}
	
	/**
	 * Ricerca sintetica variazione codifica capitolo.
	 *
	 * @param variazione the variazione
	 * @param capitolo the capitolo
	 * @param tipiCapitolo the tipi capitolo
	 * @param parametriPaginazione the parametri paginazione
	 * @return the lista paginata
	 */
	public ListaPaginata<VariazioneCodificaCapitolo> ricercaSinteticaVariazioneCodificaCapitolo(VariazioneCodificaCapitolo variazione, Capitolo<?, ?> capitolo, List<TipoCapitolo> tipiCapitolo, ParametriPaginazione parametriPaginazione) {
		SiacDVariazioneTipoEnum tipoVariazione = SiacDVariazioneTipoEnum.byTipoVariazioneEvenNull(variazione.getTipoVariazione());
		Collection<SiacDVariazioneTipoEnum> sdvtes = new ArrayList<SiacDVariazioneTipoEnum>();
		if(tipoVariazione != null) {
			sdvtes.add(tipoVariazione);
		}
		
		Page<SiacTVariazione> siacTVariazioni = variazioneImportiCapitoloDao.ricercaSinteticaVariazioneDiBilancio(sdvtes, ente.getUid(),""+bilancio.getAnno(),
				variazione.getNumero(),
				variazione.getDescrizione(),
				// SIAC-6884
				variazione.getDataChiusuraProposta(),
				variazione.getDataAperturaProposta(),
				((variazione.getDirezioneProponente() != null) ? variazione.getDirezioneProponente().getUid() : null),  //direzioneProponenteId
				SiacDVariazioneStatoEnum.byStatoOperativoVariazioneDiBilancioEvenNull(variazione.getStatoOperativoVariazioneDiBilancio()),
				//stornoUEB.getNote(),
				//SiacDVariazioneStatoEnum.byStatoOperativoVariazioneDiBilancio(stornoUEB.getStatoOperativoVariazioneDiBilancio()),
				variazione.getAttoAmministrativo()!=null?variazione.getAttoAmministrativo().getUid():null,
				null,
				capitolo!=null?capitolo.getUid():null, //filtro del capitolo legato alla variazione di codifica
				null,
				null,
				null,
				SiacDBilElemTipoEnum.byTipoCapitoloListEvenNull(tipiCapitolo),
				null,
				null, //atto amministrativo varbil presente solo per variazioni di bilancio
				null,
				false,
				toPageable(parametriPaginazione));
		
		return toListaPaginata(siacTVariazioni,VariazioneCodificaCapitolo.class, BilMapId.SiacTVariazione_VariazioneCodificaCapitolo);
	}
	
	/**
	 * Ricerca i Tipi di Variazione afferenti ad un determinato ente.
	 *  
	 * @param enteVariazione l'ente rispetto cui cercare i Tipi di Variazione
	 * 
	 * @return i Tipi di Variazione collegati
	 */
	public List<TipoVariazione> ricercaTipoVariazione(Ente enteVariazione) {
		List<TipoVariazione> result = new ArrayList<TipoVariazione>();
		List<SiacDVariazioneTipo> siacDVariazioneTipi = siacDVariazioneTipoRepository.findByEnteProprietarioUid(enteVariazione.getUid());
		for(SiacDVariazioneTipo siacDVariazioneTipo : siacDVariazioneTipi) {
			result.add(SiacDVariazioneTipoEnum.byCodice(siacDVariazioneTipo.getVariazioneTipoCode()).getTipoVariazione());
		}
		return result;
	}

	public boolean checkCapitoloAssociatoAllaVariazione(int idCapitolo, int idVariazione) {
		final String methodName = "checkCapitoloAssociatoAllaVariazione";
		SiacTBilElem siacTBilElem = siacTVariazioneRepository.findCapitoloIfInVariazione(idCapitolo, idVariazione);
		log.debug(methodName, "controllo se il capitolo con uid "+ idCapitolo + " sia stato legato precedentemente alla variazione con uid " + idVariazione);
		return siacTBilElem != null;
	}
	
	/**
	 * Restituisce la mappa degli stanziamenti presenti nel capitolo in variazione
	 * @param idCapitoli gli id dei capitoli
	 * @param idVariazione l'id della variazione
	 * @return la mappa degli stanziamenti. Chiave: <code>&lt;ANNO&gt;_&lt;UID&gt;_&lt;TIPO&gt;</code>
	 */
	public Map<String, BigDecimal> findStanziamentoVariazioneByUidCapitoloAndUidVariazione(Collection<Integer> idCapitoli, int idVariazione) {
		Map<String,BigDecimal> mappa = new HashMap<String,BigDecimal>();
		if(idCapitoli == null || idCapitoli.isEmpty()) {
			return mappa;
		}
		
		List<Object[]> objs = siacTVariazioneRepository.findStanziamentoCapitoloInVariazione(idCapitoli, idVariazione);
		// 0 => elem_id
		// 1 => anno
		// 2 => elem_det_tipo_code
		// 3 => importo
		if(objs == null) {
			return mappa;
		}
		
		for(Object[] o : objs){
			mappa.put(o[1] + "_" + o[0] + "_" + o[2], (BigDecimal)o[3]);
		}
		return mappa;
	}
	
	/** dv.siacTBilElem.elemId, dv.siacTPeriodo.anno, dv.siacDBilElemDetTipo.elemDetTipoCode,dv.siacTBilElem.siacDBilElemTipo.elemTipoCode, dv.elemDetImporto 
	 * Find stanziamento variazione in diminuzione uid variazione. La mappa ritornata ha una chiave cosi' formata:
	 * <br>
	 * idCapitolo_annoVariato_tipoStanziamento_tipoCapitolo
	 * <br>
	 * Ad esempio il capitolo di spesa gestione con uid 1456 per cui la variazione nel 2022 apporta una modifica di +100 sullo stanziamnto avra' questa chiave:
	 * <br>
	 * 1456_CAP-UG_2022_STA
	 * 
	 *
	 * @param idVariazione the id variazione
	 * @param idCapitoliDaEscludere the id capitoli da escludere
	 * @return the map
	 */
//	public Map<String, BigDecimal> findStanziamentoVariazioneInDiminuzioneUidVariazione(Integer idVariazione, Collection<Integer> idCapitoliDaEscludere) {
//	Map<String,BigDecimal> mappa = new HashMap<String,BigDecimal>();
	public Map<Integer, List<WrapperImportiCapitoloVariatiInVariazione>> findStanziamentoVariazioneInDiminuzioneUidVariazione(Integer idVariazione, Collection<Integer> idCapitoliDaEscludere) {
		Map<Integer, List<WrapperImportiCapitoloVariatiInVariazione>> mappa = new HashMap<Integer, List<WrapperImportiCapitoloVariatiInVariazione>>();
		
		List<Object[]> objs = variazioneImportiCapitoloDao.findStanziamentoCapitoloInVariazioneInDiminuzione(idVariazione,idCapitoliDaEscludere);
		// 0 => elem_id
		// 1 => anno
		// 2 => elem_det_tipo_code
		// 3 => importo
		if(objs == null) {
			return mappa;
		}
		
		for(Object[] o : objs){
//			mappa.put(o[1] + "_" + o[2] + "_" + o[3] + "_" + o[4], (BigDecimal)o[4]);
			Integer uidCapitolo = (Integer) o[0];
			if(mappa.get(uidCapitolo) == null) {
				mappa.put(uidCapitolo, new ArrayList<WrapperImportiCapitoloVariatiInVariazione>());
			}
			mappa.get(uidCapitolo).add(new WrapperImportiCapitoloVariatiInVariazione((String) o[1], (String) o[2], (String) o[3], (BigDecimal) o[4]));
		}
		return mappa;
	}
	
	public Capitolo<?, ?> popolaNumeriCapitolo(Capitolo<?, ?> capitolo) {
		Capitolo<?, ?> c = capitolo;
		SiacTBilElem siacTBilElem = siacTBilElemRepository.findOne(capitolo.getUid());
		capitolo.setNumeroCapitolo(Integer.valueOf(siacTBilElem.getElemCode()));
		capitolo.setNumeroArticolo(Integer.valueOf(siacTBilElem.getElemCode2()));
		capitolo.setNumeroUEB(Integer.valueOf(siacTBilElem.getElemCode3()));
		return c;
	}
	
	public List<Integer> calcolaNumeroVariazioni(Integer uidCapitolo, Collection<TipoVariazione> tipiVariazione) {
		Collection<String> variazioneTipoCodes = projectToCode(tipiVariazione);
		Collection<String> variazioneStatoTipoCodes = projectToCode(EnumSet.of(SiacDVariazioneStatoEnum.Definitiva, SiacDVariazioneStatoEnum.Annullata));
		
		return siacTVariazioneRepository.getVariazioneNumeroByBilElemIdAndVariazioneTipoCodesInAndVariazioneStatoTipoCodesNotIn(uidCapitolo, variazioneTipoCodes, variazioneStatoTipoCodes);
	}
	
	public List<Integer> calcolaNumeroVariazioniCodifiche(Integer uidCapitolo) {
		Collection<String> variazioneStatoTipoCodes = projectToCode(EnumSet.of(SiacDVariazioneStatoEnum.Definitiva, SiacDVariazioneStatoEnum.Annullata));
		
		return siacTVariazioneRepository.getVariazioneCodificheNumeroByBilElemIdAndCodesInAndVariazioneStatoTipoCodesNotIn(uidCapitolo, variazioneStatoTipoCodes);
	}
	
	/**Ricerca l'anagrafica di una variazione a partire dall'uid
	 * 
	 * @param uidVariazione uid della variazione
	 * @return  la VariazioneImportoCapitolo trovata, popolata solamente con l'anagrafica
	 */
	public VariazioneImportoCapitolo findAnagraficaVariazioneImportoCapitoloByUid(int uidVariazione) {
		SiacTVariazione siacTVariazione = siacTVariazioneRepository.findOne(uidVariazione);		
		return mapNotNull(siacTVariazione, VariazioneImportoCapitolo.class, BilMapId.SiacTVariazione_VariazioneImportoCapitolo_Medium);
	}

	/**
	 * Ricerca paginata dei dettagli di una variazione
	 * @param uidVariazione id della variazione da cercare
	 * @param parametriPaginazione 
	 * 
	 * @return la lista paginata di DettaglioVariazioneImportoCapitolo
	 */
	public ListaPaginata<DettaglioVariazioneImportoCapitolo> findDettagliVariazioneImportoCapitoloByUidVariazione(int uidVariazione, ParametriPaginazione parametriPaginazione) {
		String methodName = "findDettagliVariazioneImportoCapitoloByUidVariazione";
		
		//mi serve avere una lista paginata di SiacTBilElem
		Page<SiacTBilElem> siacTBilElems = variazioneImportiCapitoloDao.findCapitoliNellaVariazioneByUid(uidVariazione, toPageable(parametriPaginazione));
		log.debug(methodName, "trovati " + siacTBilElems.getTotalElements() + " capitoli per la variazione " + uidVariazione);
		
		//il capitolo puo' essere collegato a piu' variazioni, devo avere il riferimento di quella su cui sto lavorando
		for(SiacTBilElem siacTBilElem : siacTBilElems){
			List<SiacTBilElemDetVar> siacTBilElemDetVars = siacTBilElemDetVarRepository.findByVariazioneIdEBilElemId(uidVariazione, siacTBilElem.getUid());
			siacTBilElem.setSiacTBilElemDetVars(siacTBilElemDetVars);
		}
		return toListaPaginata(siacTBilElems, DettaglioVariazioneImportoCapitolo.class, BilMapId.SiacTBilElem_DettaglioVariazioneImportoCapitolo);
	}
	
	/**
	 * Ricerca paginata dei dettagli di una variazione
	 * @param uidVariazione id della variazione da cercare
	 * @param parametriPaginazione 
	 * 
	 * @return la lista paginata di DettaglioVariazioneImportoCapitolo
	 */
	public ListaPaginata<DettaglioVariazioneImportoCapitolo> findDettagliVariazionePrimoCapitoloByUidVariazione(int uidVariazione, ParametriPaginazione parametriPaginazione, int idCapitolo) {
		String methodName = "findDettagliVariazioneImportoCapitoloByUidVariazione";
		
		//mi serve avere una lista paginata di SiacTBilElem
		Page<SiacTBilElem> siacTBilElems = variazioneImportiCapitoloDao.findPrimoCapitoloNellaVariazioneByUid(uidVariazione, toPageable(parametriPaginazione), idCapitolo);
		log.debug(methodName, "trovati " + siacTBilElems.getTotalElements() + " capitoli per la variazione " + uidVariazione);
		
		//il capitolo puo' essere collegato a piu' variazioni, devo avere il riferimento di quella su cui sto lavorando
		for(SiacTBilElem siacTBilElem : siacTBilElems){
			List<SiacTBilElemDetVar> siacTBilElemDetVars = siacTBilElemDetVarRepository.findByVariazioneIdEBilElemId(uidVariazione, siacTBilElem.getUid());
			siacTBilElem.setSiacTBilElemDetVars(siacTBilElemDetVars);
		}
		return toListaPaginata(siacTBilElems, DettaglioVariazioneImportoCapitolo.class, BilMapId.SiacTBilElem_DettaglioVariazioneImportoCapitolo);
		
	}
	
	/**
	 * Aggiorna gli stati dei capitoli presenti nella variazione nel modo seguente:
	 * <ol>
	 *   <li>ANNULLA i capitoli nella afferenti ai dettagliVariazioneImportoCapitolo con flagAnnullaCapitolo==TRUE</li>
	 *   <li>Aggiorna lo stato a VALIDO per i capitoli nella variazione afferenti ai dettagliVariazioneImportoCapitolo con FlagNuovoCapitolo == TRUE</li>
	 * </ol>
	 * @param uidVariazione
	 */
	public void aggiornaStatoCapitoli(int uidVariazione) {
		final String methodName = "aggiornaStatoCapitoli";
		fi.invokeFunctionVoid("fnc_siac_aggiorna_stato_capitoli_variazione", uidVariazione);
		log.info(methodName, "Stato dei capitoli aggiornato correttemente tramite function.");
	}
	
	/**
	 * Applica gli importi della variazione agli importi del capitolo.
	 * 
	 * @param uidVariazione
	 */
	public void aggiornaImportiCapitolo(int uidVariazione) {
		String methodName = "aggiornaImportiCapitolo";
		fi.invokeFunctionVoid("fnc_siac_variazione_aggiorna_importo_capitoli", uidVariazione);
		log.info(methodName, "Importi del capitolo correttamente aggiornati tramite function.");
	}
	
	/**
	 * 	Controlla se tra i capitoli di spesa/entrata coinvolti nella variazione ci sono:
	 *  1) capitoli con missione o programma differenti
	 *  2) capitoli con titolo o tipologie differenti
	 *  
	 * @param uidVariazione
	 * @return True se differenti missione o programma per le spese o titolo o tipologie per le entrate
	 */
	public boolean isNecessarioAttoAmministrativo(int uidVariazione) {
		String methodName = "isNecessarioAttoAmministrativo";
		
		Boolean result = fi.invokeFunctionSingleResult("fnc_siac_varaizioni_necessario_atto_amm_per_definisci", Boolean.class, uidVariazione);
		log.info(methodName, "risultato della function: "+result);
		return Boolean.TRUE.equals(result);
	}
	
	/**
	 * Per ogni DettaglioVariazioneImporti (SiacTBilElemDetVar) deve controllare la disponibilitaVariare per l'anno
	 * di competenza della variazione. Ovvero deve reperire la disponibilitaVariareAnnoX del capitolo dove 
	 * X = annoCompetenzaVariazione - annoCompetenzaCapitolo + 1; (Stessa cosa per la disponbilitaVariareCassa solo se X = 1)
	 * Inoltre se il capitolo era gia' associato alla variazione (caso di aggiornamento variazione)
	 * l'importo dell disponibilitaVariareAnnoX va adeguato (SIAC-3044) aggiungento lo stanziamento (solo se lo stanziamento era negativo).
	 * Stessa cosa per lo stanziamentoCassa.
	 * 
	 * @param uidVariazione
	 * @return 'OK' se supera il controllo degli importi, stringa con messaggio in caso contrario.
	 * 
	 */
	public String checkImportiVariazione(int uidVariazione) {
		String methodName = "checkImportiVariazione";
		String result = fi.invokeFunctionSingleResult("fnc_siac_variazioni_check_importi", String.class, uidVariazione);
		log.info(methodName, "risultato della function: "+result);
		return result;
	}
	
	
	public boolean checkQuadraturaStanziamento(int uidVariazione) {
		String methodName = "checkQuadraturaStanziamento";
		Boolean result = fi.invokeFunctionSingleResult("fnc_siac_variazioni_quadratura_stanziamento", Boolean.class, uidVariazione);
		log.info(methodName, "risultato della function: "+result);
		return Boolean.TRUE.equals(result);
	}

	public boolean checkQuadraturaStanziamentoCassa(int uidVariazione) {
		String methodName = "checkQuadraturaStanziamentoCassa";
		Boolean result = fi.invokeFunctionSingleResult("fnc_siac_variazioni_quadratura_cassa", Boolean.class, uidVariazione);
		log.info(methodName, "risultato della function: "+result);
		return Boolean.TRUE.equals(result);
	}
	

	public List<Map<String, BigDecimal>> calcolaTotaliStanziamentiByUidVariazione(Integer uidVariazione) {
		List<String> codiciTipoCapitoloUscita = Arrays.asList(SiacDBilElemTipoEnum.CapitoloUscitaGestione.getCodice(), SiacDBilElemTipoEnum.CapitoloUscitaPrevisione.getCodice());
		List<String> codiciTipoCapitoloEntrata = Arrays.asList(SiacDBilElemTipoEnum.CapitoloEntrataGestione.getCodice(), SiacDBilElemTipoEnum.CapitoloEntrataPrevisione.getCodice());
		
		List<Map<String, BigDecimal>> result = new ArrayList<Map<String,BigDecimal>>();
		// Indice 0: spese
		result.add(parseTotaliStanziamentiByUidVariazioneAndTipoCapitolo(uidVariazione, codiciTipoCapitoloUscita));
		// Indice 1: entrate
		result.add(parseTotaliStanziamentiByUidVariazioneAndTipoCapitolo(uidVariazione, codiciTipoCapitoloEntrata));
		return result;
	}
	
	
	
	private Map<String, BigDecimal> parseTotaliStanziamentiByUidVariazioneAndTipoCapitolo(Integer uidVariazione, List<String> codiciTipoCapitoloUscita) {
		Map<String, BigDecimal> result = new HashMap<String, BigDecimal>();
		List<Object[]> data = siacTBilElemDetVarRepository.calcolaTotaleImportoVariazioneByTipoStanziamentoETipoCapitolo(uidVariazione, codiciTipoCapitoloUscita);
		if(data == null) {
			return result;
		}
		// anno - elemDetTipoCode - importo
		for(Object[] obj : data) {
			String key = obj[0] + "_" + obj[1];
			result.put(key, (BigDecimal) obj[2]);
		}
		return result;
	}

	public void inserisciDettaglioVariazioneImportoCapitolo(DettaglioVariazioneImportoCapitolo dettVic) {
		String methodName = "inserisciDettaglioVariazioneImportoCapitolo";
		SiacTVariazione siacTVariazione = siacTVariazioneRepository.findOne(dettVic.getVariazioneImportoCapitolo().getUid());
		
		Date now = new Date();
		
		SiacRVariazioneStato siacRVariazioneStato = findCurrentSiacRVariazioneStato(siacTVariazione);
		
		log.debug(methodName, "bilElemDetVars size: " + siacRVariazioneStato.getSiacTBilElemDetVars().size()); //NON togliere questo log.
		
		// SIAC-6883
		//Anno di competenza della variazione
		SiacTPeriodo periodo = siacTVariazione.getSiacTBil().getSiacTPeriodo();
		int anno = Integer.valueOf(periodo.getAnno());
		
		addSiacTBilElemDetVar(siacRVariazioneStato, siacTVariazione, periodo, dettVic.getStanziamento(), SiacDBilElemDetTipoEnum.Stanziamento, dettVic, now);
		addSiacTBilElemDetVar(siacRVariazioneStato, siacTVariazione, periodo, dettVic.getStanziamentoCassa(), SiacDBilElemDetTipoEnum.StanziamentoCassa, dettVic, now);
		addSiacTBilElemDetVar(siacRVariazioneStato, siacTVariazione, periodo, dettVic.getStanziamentoResiduo(), SiacDBilElemDetTipoEnum.StanziamentoResiduo, dettVic, now);
		
		SiacTPeriodo periodo1 = siacTPeriodoRepository.findByAnnoAndEnteProprietario(String.valueOf(anno + 1), ente.getUid());
		addSiacTBilElemDetVar(siacRVariazioneStato, siacTVariazione, periodo1, dettVic.getStanziamento1(), SiacDBilElemDetTipoEnum.Stanziamento, dettVic, now);
		SiacTPeriodo periodo2 = siacTPeriodoRepository.findByAnnoAndEnteProprietario(String.valueOf(anno + 2), ente.getUid());
		addSiacTBilElemDetVar(siacRVariazioneStato, siacTVariazione, periodo2, dettVic.getStanziamento2(), SiacDBilElemDetTipoEnum.Stanziamento, dettVic, now);
		
		siacTVariazioneRepository.saveAndFlush(siacTVariazione);
	}
	
	private void addSiacTBilElemDetVar(SiacRVariazioneStato siacRVariazioneStato, SiacTVariazione siacTVariazione, SiacTPeriodo siacTPeriodo, BigDecimal importo, SiacDBilElemDetTipoEnum tipo, DettaglioVariazioneImportoCapitolo dettVic, Date now) {
		final String methodName = "addSiacTBilElemDetVar";
		SiacTBilElemDetVar siacTBilElemDetVar = variazioneImportiCapitoloConverter.createSiacTBilElemDetVar(siacTVariazione, siacTPeriodo, importo, tipo, dettVic);
		siacTBilElemDetVar.setDataModificaInserimento(now);
		log.debug(methodName,"tipoImporto: " + tipo + ": " + importo);
		siacRVariazioneStato.addSiacTBilElemDetVar(siacTBilElemDetVar);
	}
	
	
	public void aggiornaDettaglioVariazioneImportoCapitolo(DettaglioVariazioneImportoCapitolo dettVic) {
		SiacTVariazione siacTVariazione = siacTVariazioneRepository.findOne(dettVic.getVariazioneImportoCapitolo().getUid());
		
		Date now = new Date();
		
		SiacRVariazioneStato siacRVariazioneStato = findCurrentSiacRVariazioneStato(siacTVariazione);
		
		// SIAC-6881: sposto i legami con le variazioni componenti capitolo
		Map<String, SiacTBilElemDetVar> mappaPerTipo = new HashMap<String, SiacTBilElemDetVar>();
		
		// Ottengo il vecchio legame al capitolo suddiviso per tipo
		List<SiacTBilElemDetVar> siacTBilElemDetVars = siacTVariazioneRepository.findDettagliVariazioneImportiByRVarStatoAndCapitolo(siacRVariazioneStato.getUid(), dettVic.getCapitolo().getUid());
		for(SiacTBilElemDetVar bilElemDetVar : siacTBilElemDetVars){
			mappaPerTipo.put(bilElemDetVar.getSiacTPeriodo().getAnno() + "_" + bilElemDetVar.getSiacDBilElemDetTipo().getElemDetTipoCode(), bilElemDetVar);
		}
		
		siacRVariazioneStato.setSiacTBilElemDetVars(new ArrayList<SiacTBilElemDetVar>());
		// SIAC-6883
		SiacTPeriodo periodo = siacTVariazione.getSiacTBil().getSiacTPeriodo();
		int anno = Integer.parseInt(periodo.getAnno());
		// Inserisco il nuovo legame al capitolo
		siacTVariazioneRepository.flush();
		siacRVariazioneStato.addSiacTBilElemDetVar(createSiacTBilElemDetVar(now, siacTVariazione, periodo, dettVic.getStanziamento(), SiacDBilElemDetTipoEnum.Stanziamento, dettVic, mappaPerTipo));
		siacRVariazioneStato.addSiacTBilElemDetVar(createSiacTBilElemDetVar(now, siacTVariazione, periodo, dettVic.getStanziamentoCassa(), SiacDBilElemDetTipoEnum.StanziamentoCassa, dettVic, mappaPerTipo));
		siacRVariazioneStato.addSiacTBilElemDetVar(createSiacTBilElemDetVar(now, siacTVariazione, periodo, dettVic.getStanziamentoResiduo(), SiacDBilElemDetTipoEnum.StanziamentoResiduo, dettVic, mappaPerTipo));
		
		SiacTPeriodo periodo1 = siacTPeriodoRepository.findByAnnoAndEnteProprietario(String.valueOf(anno + 1), ente.getUid());
		siacRVariazioneStato.addSiacTBilElemDetVar(createSiacTBilElemDetVar(now, siacTVariazione, periodo1, dettVic.getStanziamento1(), SiacDBilElemDetTipoEnum.Stanziamento, dettVic, mappaPerTipo));
		SiacTPeriodo periodo2 = siacTPeriodoRepository.findByAnnoAndEnteProprietario(String.valueOf(anno + 2), ente.getUid());
		siacRVariazioneStato.addSiacTBilElemDetVar(createSiacTBilElemDetVar(now, siacTVariazione, periodo2, dettVic.getStanziamento2(), SiacDBilElemDetTipoEnum.Stanziamento, dettVic, mappaPerTipo));
		
		for(SiacTBilElemDetVar tbedv : mappaPerTipo.values()) {
			tbedv.setDataCancellazioneIfNotSet(now);
		}
		
		siacTVariazioneRepository.saveAndFlush(siacTVariazione);
	}
	private SiacTBilElemDetVar createSiacTBilElemDetVar(Date now, SiacTVariazione siacTVariazione, SiacTPeriodo periodo, BigDecimal importo, SiacDBilElemDetTipoEnum tipo, DettaglioVariazioneImportoCapitolo dettaglio, Map<String, SiacTBilElemDetVar> mappaPerTipo) {
		final String methodName = "createSiacTBilElemDetVar";
		// SIAC-6883: la chiave comprende anche l'anno
		String mapKey = periodo.getAnno() + "_" + tipo.getCodice();
		SiacTBilElemDetVar siacTBilElemDetVar = mappaPerTipo.get(mapKey);
		if(siacTBilElemDetVar != null) {
			siacTBilElemDetVar.setDataModificaAggiornamento(now);
			siacTBilElemDetVar.setElemDetImporto(importo);
		} else {
			siacTBilElemDetVar = variazioneImportiCapitoloConverter.createSiacTBilElemDetVar(siacTVariazione, periodo, importo, tipo, dettaglio);
			siacTBilElemDetVar.setDataModificaInserimento(now);
		}
		mappaPerTipo.remove(mapKey);
		log.debug(methodName,"tipoImporto: " + tipo + ": " + importo);
		return siacTBilElemDetVar;
	}

	public void eliminaDettaglioVariazioneImportoCapitolo(DettaglioVariazioneImportoCapitolo dettVic) {
		SiacTVariazione siacTVariazione = siacTVariazioneRepository.findOne(dettVic.getVariazioneImportoCapitolo().getUid());
	
		SiacRVariazioneStato siacRVariazioneStato = findCurrentSiacRVariazioneStato(siacTVariazione);
		
		Date now = new Date();
		
		// SIAC-6881
		List<SiacTBilElemDetVarComp> siacTBilElemDetVarComps = siacTBilElemDetVarCompRepository.findByElemIdAndVariazioneId(dettVic.getCapitolo().getUid(), siacTVariazione.getUid());
		for(SiacTBilElemDetVarComp siacTBilElemDetVarComp : siacTBilElemDetVarComps) {
			siacTBilElemDetVarComp.setDataCancellazioneIfNotSet(now);
			// Se nuova componente, cancello quella temporanea creata
			if("N".equals(siacTBilElemDetVarComp.getElemDetFlag())) {
				siacTBilElemDetVarComp.getSiacTBilElemDetComp().setDataCancellazioneIfNotSet(now);
			}
		}
		
		//Cancello il vecchio legame al capitolo
		List<SiacTBilElemDetVar> siacTBilElemDetVars = siacTVariazioneRepository.findDettagliVariazioneImportiByRVarStatoAndCapitolo(siacRVariazioneStato.getUid(), dettVic.getCapitolo().getUid());
		for(SiacTBilElemDetVar bilElemDetVar : siacTBilElemDetVars){
			if(bilElemDetVar.getSiacTBilElem().getUid() == dettVic.getCapitolo().getUid()){
				bilElemDetVar.setDataCancellazioneIfNotSet(now);
			}
		}
		siacTVariazioneRepository.saveAndFlush(siacTVariazione);
	
	}

	/**
	 * Ottiene il SiacRVariazioneStato corrente associato alla SiacTVariazione
	 * @param siacTVariazione
	 * @return SiacRVariazioneStato
	 */
	private SiacRVariazioneStato findCurrentSiacRVariazioneStato(SiacTVariazione siacTVariazione) {
		for(SiacRVariazioneStato r : siacTVariazione.getSiacRVariazioneStatos()){
			if(r.getDataCancellazione()==null){
				return r;
			}
		}
		//Succede solo se il db e' corrotto.
		throw new IllegalStateException("Impossibile individuare lo stato attuale della variazione. Controllare legame su DB tra siac_t_variazione e siac_r_variazione_stato.");
	}

	/**
	 * Sets the bilancio.
	 *
	 * @param bilancio the new bilancio
	 */
	public void setBilancio(Bilancio bilancio) {
		this.bilancio = bilancio;
	}

	/**
	 * Popola i dettagli della variazione a partire dai residui del bilancio in corso.
	 * 
	 * @param variazione
	 */
	public void caricaResidui(VariazioneImportoCapitolo variazione) {
		String methodName = "caricaResidui";
		
		SiacTVariazione siacTVariazione = siacTVariazioneRepository.findOne(variazione.getUid());
		String annoBilancio = siacTVariazione.getSiacTBil().getSiacTPeriodo().getAnno();
		
		log.debug(methodName, "richiamo la function che popola i dettagli della variazione a partire dai residui per variazione.getUid(): " + variazione.getUid() + " e annoBilancio: " + annoBilancio);
		fi.invokeFunctionVoid("fnc_siac_inserisci_det_variazione_assestamento", variazione.getUid(), annoBilancio, loginOperazione);
		log.debug(methodName, "function terminata.");
	}

	/**
	 * Ottiene i dati delle variazioni di importo per anno, con importi positivi
	 * @param capitolo il capitolo
	 * @param bilancioVariazione il bilancio
	 * @return i dati delle variazioni
	 */
	public RiepilogoDatiVariazioneImportoCapitoloAnno findDatiVariazioneImportoCapitoloByAnnoPositive(Capitolo<?, ?> capitolo, Bilancio bilancioVariazione) {
		RiepilogoDatiVariazioneImportoCapitoloAnno res = new RiepilogoDatiVariazioneImportoCapitoloAnno();
		
		List<Object[]> objs = siacTVariazioneRepository.findTotalePositiveGroupedByImportoCapitolo(capitolo.getUid(), bilancioVariazione.getUid());
		map(objs, res, BilMapId.ListOfObjectArrayTotale_RiepilogoDatiVariazioneImportoCapitoloAnno);
		
		objs = siacTVariazioneRepository.findCountPositiveGroupedByImportoCapitolo(capitolo.getUid(), bilancioVariazione.getUid());
		map(objs, res, BilMapId.ListOfObjectArrayCount_RiepilogoDatiVariazioneImportoCapitoloAnno);
		
		return res;
	}
	
	// CONTABILIA-285 - INIZIO
	/**
	 * Ottiene i dati delle variazioni di importo per anno, con importi positivi
	 * @param capitolo il capitolo
	 * @param bilancioVariazione il bilancio
	 * @return i dati delle variazioni
	 */
	public RiepilogoDatiVariazioneImportoCapitoloAnno findDatiVariazioneImportoCapitoloByAnnoNeutre(Capitolo<?, ?> capitolo, Bilancio bilancioVariazione) {
		RiepilogoDatiVariazioneImportoCapitoloAnno res = new RiepilogoDatiVariazioneImportoCapitoloAnno();
		
		List<Object[]> objs = siacTVariazioneRepository.findTotaleNeutreGroupedByImportoCapitolo(capitolo.getUid(), bilancioVariazione.getUid());
		map(objs, res, BilMapId.ListOfObjectArrayTotale_RiepilogoDatiVariazioneImportoCapitoloAnno);
		
		objs = siacTVariazioneRepository.findCountNeutreGroupedByImportoCapitolo(capitolo.getUid(), bilancioVariazione.getUid());
		map(objs, res, BilMapId.ListOfObjectArrayCount_RiepilogoDatiVariazioneImportoCapitoloAnno);
		
		return res;
	}
	// CONTABILIA-285 - FINE
	
	
	/**
	 * Ottiene i dati delle variazioni di importo per anno, con importi negativi
	 * @param capitolo il capitolo
	 * @param bilancioVariazione il bilancio
	 * @return i dati delle variazioni
	 */
	public RiepilogoDatiVariazioneImportoCapitoloAnno findDatiVariazioneImportoCapitoloByAnnoNegative(Capitolo<?, ?> capitolo, Bilancio bilancioVariazione) {
		RiepilogoDatiVariazioneImportoCapitoloAnno res = new RiepilogoDatiVariazioneImportoCapitoloAnno();
		
		List<Object[]> objs = siacTVariazioneRepository.findTotaleNegativeGroupedByImportoCapitolo(capitolo.getUid(), bilancioVariazione.getUid());
		map(objs, res, BilMapId.ListOfObjectArrayTotale_RiepilogoDatiVariazioneImportoCapitoloAnno);
		
		objs = siacTVariazioneRepository.findCountNegativeGroupedByImportoCapitolo(capitolo.getUid(), bilancioVariazione.getUid());
		map(objs, res, BilMapId.ListOfObjectArrayCount_RiepilogoDatiVariazioneImportoCapitoloAnno);
		
		return res;
	}
	
	/**
	 * Ottiene i dati di variazione importo per singolo capitolo
	 * @param capitolo il capitolo
	 * @param bilancioVariazione il bilancio
	 * @param enteVariazione l'ente
	 * @param segnoVariazioneImporti il segno della variazione
	 * @param parametriPaginazione  i parametri di paginazione
	 * @return una lista paginata di dati di variazione per il capitolo fornito
	 */
	public ListaPaginata<VariazioneImportoSingoloCapitolo> ricercaSinteticaVariazioneImportoSingoloCapitolo(Capitolo<?, ?> capitolo, Bilancio bilancioVariazione, Ente enteVariazione,
			SegnoImporti segnoVariazioneImporti, ParametriPaginazione parametriPaginazione) {
		Collection<SiacDVariazioneTipoEnum> tipiVariazione = EnumSet.allOf(SiacDVariazioneTipoEnum.class);
		tipiVariazione.remove(SiacDVariazioneTipoEnum.VariazioneCodifica);
		
		Page<SiacTVariazione> siacTVariazioni = variazioneImportiCapitoloDao.ricercaSinteticaVariazioneDiBilancio(tipiVariazione,
				Integer.valueOf(enteVariazione.getUid()),
				Integer.toString(bilancioVariazione.getAnno()),
				null,
				null,
				// SIAC-6884
				null, //dataAperturaProposta
				null, //dataChiusuraProposta
				null, //direzioneProponenteId
				null,
				null,
				Integer.valueOf(capitolo.getUid()),
				null,
				null,
				null,
				SiacDBilElemTipoEnum.byTipoCapitoloListEvenNull(capitolo.getTipoCapitolo()),
				null,
				segnoVariazioneImporti != null ? segnoVariazioneImporti.getOperatore() : null,
				null, // atto amministrativo variazione di bilancio
				null,
				false,
				toPageable(parametriPaginazione));
		
		VariazioneImportoSingoloCapitolo template = new VariazioneImportoSingoloCapitolo();
		template.setCapitolo(capitolo);
		
		return toListaPaginata(siacTVariazioni, template, BilMapId.SiacTVariazione_VariazioneImportoSingoloCapitolo);
	}

	/**
	 * Ricerca il dettaglio variazione codifica capitolo di tutte le variazioni in stato definitivo afferenti al capitolo passato come parametro.
	 * 
	 * @param capitolo
	 * @return dettaglio variazione codifica capitolo
	 */
	public ListaPaginata<DettaglioVariazioneCodificaCapitolo> ricercaStoricoVariazioniCodificheCapitolo(Capitolo<?, ?> capitolo, ParametriPaginazione pp) {
		String methodName = "ricercaStoricoVariazioniCodificheCapitolo";
		Page<SiacTBilElemVar> siacTBilElemVars = siacTVariazioneRepository.findStoricoTBilelemVarsStatoDefinivo(capitolo.getUid(), toPageable(pp));
		log.debug(methodName, "Variazioni in DEFINITIVO trovate: "+ siacTBilElemVars.getTotalElements());
		return toListaPaginata(siacTBilElemVars, DettaglioVariazioneCodificaCapitolo.class, BilMapId.SiacTBilElemVar_DettaglioVariazioneCodificaCapitolo);
	}

	/**
	 * Conta i diversi classificatori di tipo PROGRAMMA
	 * @param uidVariazione l'uid della variazione
	 * @return il numero dei distinti classificatori di tipo 'PROGRAMMA' collegati alla variazione
	 */
	public Long countDifferentClassificatoriProgramma(Integer uidVariazione) {
		return siacRBilElemClassRepository.countDistinctClassifByVariazioneIdAndClassifTipoCode(uidVariazione, SiacDClassTipoEnum.Programma.getCodice());
	}
	
	/**
	 * Conta i diversi classificatori di tipo MISSIONE
	 * @param uidVariazione l'uid della variazione
	 * @return il numero dei distinti classificatori di tipo 'PROGRAMMA' collegati alla variazione
	 */
	public Long countDifferentClassificatoriMissione(Integer uidVariazione) {
		return variazioneImportiCapitoloDao.countDistinctClassifPadre(uidVariazione, SiacDClassTipoEnum.Missione.getCodice(), SiacDClassTipoEnum.Programma.getCodice());
	}
	
	/**
	 * Conta i diversi classificatori di tipo TITOLO SPESA
	 * @param uidVariazione l'uid della variazione
	 * @return il numero dei distinti classificatori di tipo 'PROGRAMMA' collegati alla variazione
	 */
	public Long countDifferentClassificatoriTitoloSpesa(Integer uidVariazione) {
		return variazioneImportiCapitoloDao.countDistinctClassifPadre(uidVariazione, SiacDClassTipoEnum.TitoloSpesa.getCodice(), SiacDClassTipoEnum.Macroaggregato.getCodice());
	}
	
	/**
	 * Conta i diversi classificatori di tipo TIPOLOGIA TITOLO
	 * @param uidVariazione l'uid della variazione
	 * @return il numero dei distinti classificatori di tipo 'PROGRAMMA' collegati alla variazione
	 */
	public Long countDifferentClassificatoriTipologiaTitolo(Integer uidVariazione) {
		return variazioneImportiCapitoloDao.countDistinctClassifPadre(uidVariazione, SiacDClassTipoEnum.Tipologia.getCodice(), SiacDClassTipoEnum.Categoria.getCodice());
	}
	
	/**
	 * Conta i diversi classificatori di tipo TITOLO ENTRATA
	 * @param uidVariazione l'uid della variazione
	 * @return il numero dei distinti classificatori di tipo 'PROGRAMMA' collegati alla variazione
	 */
	public Long countDifferentClassificatoriTitoloEntrata(Integer uidVariazione) {
		return variazioneImportiCapitoloDao.countDistinctClassifNonno(uidVariazione, SiacDClassTipoEnum.TitoloEntrata.getCodice(), SiacDClassTipoEnum.Tipologia.getCodice(), SiacDClassTipoEnum.Categoria.getCodice());
	}
	
	/**
	 * Aggiorna gli stati dei capitoli presenti nella variazione nel modo seguente:
	 * <ol>
	 *   <li>ANNULLA i capitoli nella afferenti ai dettagliVariazioneImportoCapitolo con flagAnnullaCapitolo==TRUE</li>
	 *   <li>Aggiorna lo stato a VALIDO per i capitoli nella variazione afferenti ai dettagliVariazioneImportoCapitolo con FlagNuovoCapitolo == TRUE</li>
	 * </ol>
	 * @param uidVariazione
	 */
	public List<Object[]> findAllDettagliVariazioneImportoCapitoloByUidVariazione(int uidVariazione) {
		final String methodName = "findAllDettagliVariazioneImportoCapitoloByUidVariazione";
		List<Object[]> result = fi.invokeFunctionToObjectArray("fnc_siac_capitoli_from_variazioni", Integer.valueOf(uidVariazione));
		log.info(methodName, "Ottenuti dettagli variazione via function. " + (result == null ? "NULL" : "Numero righe: " + result.size()));
		return result;
	}
	
	//SIAC-
	public DettaglioVariazioneImportoCapitolo findSingoloDettaglioVariazioneImportoCapitoloByUidVariazioneAndUidCapitolo(int uidVariazione, Capitolo<?,?> capitolo) {
		String methodName = "findSingoloDettaglioVariazioneImportoCapitoloByUidVariazioneAndUidCapitolo";
		String elemTipoCode = SiacDBilElemTipoEnum.byTipoCapitolo(capitolo.getTipoCapitolo()).getCodice();
		
		Page<SiacTBilElem> result = siacTBilElemRepository.ricercaPuntualeCapitolo(ente.getUid(), 
				mapToString(bilancio.getAnno()),
				elemTipoCode,
				mapToString(capitolo.getNumeroCapitolo()),
				mapToString(capitolo.getNumeroArticolo()),
				mapToString(capitolo.getNumeroUEB()),
				null,
				new PageRequest(0,1));
		if(result.getNumberOfElements() == 0) {
			log.debug(methodName, "Capitolo non esistente");
			return null;
		}
		
		SiacTBilElem siacTBilElem = result.getContent().get(0);
		
		return findSingoloDettaglioVariazioneImportoCapitoloByUidVariazioneAndSiacTBilElem(uidVariazione, siacTBilElem);
		
	}
	
	public DettaglioVariazioneImportoCapitolo findSingoloDettaglioVariazioneImportoCapitoloByUidVariazioneAndUidCapitolo(int uidVariazione, int uidCapitolo) {
		SiacTBilElem siacTBilElem = siacTBilElemRepository.findOne(uidCapitolo);
		if(siacTBilElem == null) {
			return null;
		}
		return findSingoloDettaglioVariazioneImportoCapitoloByUidVariazioneAndSiacTBilElem(uidVariazione, siacTBilElem);
	}

	private DettaglioVariazioneImportoCapitolo findSingoloDettaglioVariazioneImportoCapitoloByUidVariazioneAndSiacTBilElem(int uidVariazione, SiacTBilElem siacTBilElem) {
		String methodName = "findSingoloDettaglioVariazioneImportoCapitoloByUidVariazioneAndSiacTBilElem";
		List<SiacTBilElemDetVar> siacTBilElemDetVars = siacTBilElemDetVarRepository.findByVariazioneIdEBilElemId(uidVariazione, siacTBilElem.getUid());
		if(siacTBilElemDetVars == null || siacTBilElemDetVars.isEmpty()) {
			log.debug(methodName, "Capitolo non associato alla variazione");
			return null;
		}
		
		siacTBilElem.setSiacTBilElemDetVars(siacTBilElemDetVars);
		
		return map(siacTBilElem, DettaglioVariazioneImportoCapitolo.class, BilMapId.SiacTBilElem_DettaglioVariazioneImportoCapitolo);
	}

	public void inserisciDettaglioVariazioneComponenteImportoCapitolo(DettaglioVariazioneComponenteImportoCapitolo dettaglio) {
		SiacTBilElemDetVarComp siacTBilElemDetVarComp = buildSiacTBilElemDetVarComp(dettaglio);
		dettaglioVariazioneComponenteImportiCapitoloDao.create(siacTBilElemDetVarComp);
		dettaglio.setUid(siacTBilElemDetVarComp.getUid());
	}
	public void aggiornaDettaglioVariazioneComponenteImportoCapitolo(DettaglioVariazioneComponenteImportoCapitolo dettaglio) {
		SiacTBilElemDetVarComp siacTBilElemDetVarComp = buildSiacTBilElemDetVarComp(dettaglio);
		dettaglioVariazioneComponenteImportiCapitoloDao.update(siacTBilElemDetVarComp);
		dettaglio.setUid(siacTBilElemDetVarComp.getUid());
	}
	public void cancellaDettaglioVariazioneComponenteImportoCapitolo(DettaglioVariazioneComponenteImportoCapitolo dettaglio) {
		dettaglioVariazioneComponenteImportiCapitoloDao.deleteLogically(dettaglio.getUid());
	}
	public List<DettaglioVariazioneComponenteImportoCapitolo> ricercaDettaglioVariazioneComponenteImportoCapitolo(Integer uidVariazione, Integer uidCapitolo, DettaglioVariazioneComponenteImportoCapitoloModelDetail... DettaglioVariazioneComponenteImportoCapitoloModelDetails) {
		List<SiacTBilElemDetVarComp> siacTBilElemDetVarComps = siacTBilElemDetVarCompRepository.findByElemIdAndVariazioneId(uidCapitolo, uidVariazione);
		return convertiLista(siacTBilElemDetVarComps, DettaglioVariazioneComponenteImportoCapitolo.class, BilMapId.SiacTBilElemDetVarComp_ComponenteImportiCapitolo_ModelDetail, Converters.byModelDetails(DettaglioVariazioneComponenteImportoCapitoloModelDetails));
	}
	public Map<Integer, List<DettaglioVariazioneComponenteImportoCapitolo>> ricercaDettaglioVariazioneComponenteImportoCapitoloPerAnno(Integer uidVariazione, Integer uidCapitolo, DettaglioVariazioneComponenteImportoCapitoloModelDetail... DettaglioVariazioneComponenteImportoCapitoloModelDetails) {
		List<SiacTBilElemDetVarComp> siacTBilElemDetVarComps = siacTBilElemDetVarCompRepository.findByElemIdAndVariazioneId(uidCapitolo, uidVariazione);
		Cache<Integer, List<SiacTBilElemDetVarComp>> tmp = new MapCache<Integer, List<SiacTBilElemDetVarComp>>();
		CacheElementInitializer<Integer, List<SiacTBilElemDetVarComp>> cei = new ListCacheElementInitializer<Integer, SiacTBilElemDetVarComp>();
		for(SiacTBilElemDetVarComp stbedvc : siacTBilElemDetVarComps) {
			Integer anno = Integer.valueOf(stbedvc.getSiacTBilElemDetVar().getSiacTPeriodo().getAnno());
			List<SiacTBilElemDetVarComp> list = tmp.get(anno, cei);
			list.add(stbedvc);
		}
		
		Map<Integer, List<DettaglioVariazioneComponenteImportoCapitolo>> result = new HashMap<Integer, List<DettaglioVariazioneComponenteImportoCapitolo>>();
		Converters[] converters = Converters.byModelDetails(DettaglioVariazioneComponenteImportoCapitoloModelDetails);
		for(Entry<Integer, List<SiacTBilElemDetVarComp>> entry : tmp.entrySet()) {
			result.put(entry.getKey(), convertiLista(entry.getValue(), DettaglioVariazioneComponenteImportoCapitolo.class, BilMapId.SiacTBilElemDetVarComp_ComponenteImportiCapitolo_ModelDetail, converters));
		}
		return result;
	}
	/**
	 * Builds the SiacTBilElemDetComp.
	 *
	 * @param componenteImportiCapitolo the componente importi capitolo
	 * @return the siac t bil elem det comp
	 */
	private SiacTBilElemDetVarComp buildSiacTBilElemDetVarComp(DettaglioVariazioneComponenteImportoCapitolo dettaglioVariazioneComponenteImportoCapitolo) {
		SiacTBilElemDetVarComp siacTBilElemDetVarComp = new SiacTBilElemDetVarComp();
		
		siacTBilElemDetVarComp.setLoginOperazione(loginOperazione);
		siacTBilElemDetVarComp.setSiacTEnteProprietario(siacTEnteProprietario);
		dettaglioVariazioneComponenteImportoCapitolo.setLoginOperazione(loginOperazione);
		
		map(dettaglioVariazioneComponenteImportoCapitolo, siacTBilElemDetVarComp, BilMapId.SiacTBilElemDetVarComp_ComponenteImportiCapitolo);
		return siacTBilElemDetVarComp;
	}
	
	public Bilancio getBilancioByVariazione(Integer uidVariazione) {
		SiacTVariazione siacTVariazione = siacTVariazioneRepository.findOne(uidVariazione);
		if(siacTVariazione == null) {
			return null;
		}
		return mapNotNull(siacTVariazione.getSiacTBil(), Bilancio.class, BilMapId.SiacTBil_Bilancio);
	}

	public Integer getIdPrimoCapitoloAssociato(int uidVariazione, String tipoCapitolo, int annoBilancio) {
		return  variazioneImportiCapitoloDao.getFirstCapitoloIdByUidVariazioneTipoCap(uidVariazione,  tipoCapitolo, mapToString(annoBilancio,null));
	}
	
	public boolean isCapitoliComuniAdAltreVariazioni(Integer uidVariazione, List<Integer> uidVariaziones) {
		
		Long capitoli = variazioneImportiCapitoloDao.countCapitoliComuni(uidVariazione, uidVariaziones);
		System.out.println("numero capitoli in comune: " + capitoli);
		return capitoli != null && Long.valueOf(0L).compareTo(capitoli) <0;
	}
	
	
	//CONTABILIA-285
	/**
	 * Ottiene i dati di variazione importo per singolo capitolo
	 * @param capitolo il capitolo
	 * @param bilancioVariazione il bilancio
	 * @param enteVariazione l'ente
	 * @param segnoVariazioneImporti il segno della variazione
	 * @param parametriPaginazione  i parametri di paginazione
	 * @return una lista paginata di dati di variazione per il capitolo fornito
	 */
	public ListaPaginata<VariazioneImportoSingoloCapitolo> ricercaSinteticaVariazioneNeutreImportoSingoloCapitolo(Capitolo<?, ?> capitolo, Bilancio bilancioVariazione, Ente enteVariazione,
			SegnoImporti segnoVariazioneImporti, ParametriPaginazione parametriPaginazione) {
		Collection<SiacDVariazioneTipoEnum> tipiVariazione = EnumSet.allOf(SiacDVariazioneTipoEnum.class);
		tipiVariazione.remove(SiacDVariazioneTipoEnum.VariazioneCodifica);
		
		Page<SiacTVariazione> siacTVariazioni = variazioneImportiCapitoloDao.ricercaSinteticaVariazioneNeutreDiBilancio(tipiVariazione,
				Integer.valueOf(enteVariazione.getUid()),
				Integer.toString(bilancioVariazione.getAnno()),
				null,
				null,
				// SIAC-6884
				null, //dataAperturaProposta
				null, //dataChiusuraProposta
				null, //direzioneProponenteId
				null,
				null,
				Integer.valueOf(capitolo.getUid()),
				null,
				null,
				null,
				SiacDBilElemTipoEnum.byTipoCapitoloListEvenNull(capitolo.getTipoCapitolo()),
				null,
				segnoVariazioneImporti != null ? segnoVariazioneImporti.getOperatore() : null,
				null, // atto amministrativo variazione di bilancio
				null,
				toPageable(parametriPaginazione));
		
		VariazioneImportoSingoloCapitolo template = new VariazioneImportoSingoloCapitolo();
		template.setCapitolo(capitolo);
		
		return toListaPaginata(siacTVariazioni, template, BilMapId.SiacTVariazione_VariazioneImportoSingoloCapitolo);
	}
	
	
	/*
	 * SIAC-7735
	 */
	public List<RiepilogoDatiVariazioneStatoIdVariazione> findDatiVariazioneImportoCapitoloByAnnoNeutreVarId(Capitolo<?, ?> capitolo, Bilancio bilancioVariazione) {
		List<RiepilogoDatiVariazioneStatoIdVariazione> resList = new ArrayList<RiepilogoDatiVariazioneStatoIdVariazione>();
		
		List<Object[]> objs = siacTVariazioneRepository.findTotaleNeutreGroupedByImportoCapitoloVarId(capitolo.getUid(), bilancioVariazione.getUid());
		if(objs!= null && !objs.isEmpty()){
			for(Object o : objs){
				
				try{
					Object[] arr = (Object[]) o;
					if((arr[0] instanceof String)
							&& (arr[1] instanceof String)
							&& (arr[3] instanceof Integer)) {
						RiepilogoDatiVariazioneStatoIdVariazione res = new RiepilogoDatiVariazioneStatoIdVariazione();
						res.setAnno( (String)arr[0]);
						res.setTipo((String) arr[1]);
						res.setIdVariazione((Integer) arr[3]);
						resList.add(res);
					}
				}catch(Exception e){
					log.error("findDatiVariazioneImportoCapitoloByAnnoNeutreVarId", e.getMessage());
				}
			}
		}
		return resList;
	}

	public Map<Integer, List<WrapperComponenteImportiCapitoloVariatiInVariazione>> findStanziamentoComponentiVariazioneInDiminuzioneUidVariazione(int idVariazione, List<Integer> idCapitolispesa) {
		Map<Integer, List<WrapperComponenteImportiCapitoloVariatiInVariazione>> mappa = new HashMap<Integer, List<WrapperComponenteImportiCapitoloVariatiInVariazione>>();
		
		List<Object[]> objs = variazioneImportiCapitoloDao.findStanziamentoComponenteCapitoloInVariazioneInDiminuzione(idVariazione,idCapitolispesa);
		// 0 => elem_id
		// 1 => anno
		// 2 => elem_det_tipo_code
		// 3 => importo
		if(objs == null) {
			return mappa;
		}
		
		for(Object[] o : objs){
//			mappa.put(o[1] + "_" + o[2] + "_" + o[3] + "_" + o[4], (BigDecimal)o[4]);
			Integer uidCapitolo = (Integer) o[0];
			if(mappa.get(uidCapitolo) == null) {
				mappa.put(uidCapitolo, new ArrayList<WrapperComponenteImportiCapitoloVariatiInVariazione>());
			}
			mappa.get(uidCapitolo).add(new WrapperComponenteImportiCapitoloVariatiInVariazione((Integer) o[2],(String) o[1],  (String) o[4], (String) o[5], (BigDecimal) o[6], (String) o[3]));
		}
		return mappa;
	}
	
	public BigDecimal getSingoloDettaglioImporto(Integer uidCapitolo, Integer uidVariazione, String anno, String elemDetTipoCode) {
		return siacTVariazioneRepository.findSingoloStanziamentoCapitoloInVariazione(uidCapitolo, uidVariazione, anno, elemDetTipoCode);
	}
	
	

}
