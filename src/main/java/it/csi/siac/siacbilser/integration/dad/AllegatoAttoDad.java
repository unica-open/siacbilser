/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.dozer.CustomConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.integration.dao.AllegatoAttoDao;
import it.csi.siac.siacbilser.integration.dao.ControlloImportiImpegniVincolatiDao;
import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.dao.LiquidazioneDao;
import it.csi.siac.siacbilser.integration.dao.SiacDAttoAllegatoChecklistRepository;
import it.csi.siac.siacbilser.integration.dao.SiacRAttoAllegatoElencoDocRepository;
import it.csi.siac.siacbilser.integration.dao.SiacRAttoAllegatoSogRepository;
import it.csi.siac.siacbilser.integration.dao.SiacRElencoDocSubdocRepository;
import it.csi.siac.siacbilser.integration.dao.SiacRSubdocAttoAmmRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTAttoAllegatoRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTSubdocRepository;
import it.csi.siac.siacbilser.integration.dao.SubdocumentoDao;
import it.csi.siac.siacbilser.integration.entity.SiacDAttoAllegatoStato;
import it.csi.siac.siacbilser.integration.entity.SiacRAttoAllegatoElencoDoc;
import it.csi.siac.siacbilser.integration.entity.SiacRAttoAllegatoSog;
import it.csi.siac.siacbilser.integration.entity.SiacRAttoAllegatoStato;
import it.csi.siac.siacbilser.integration.entity.SiacRElencoDocSubdoc;
import it.csi.siac.siacbilser.integration.entity.SiacRSubdocAttoAmm;
import it.csi.siac.siacbilser.integration.entity.SiacRSubdocMovgestT;
import it.csi.siac.siacbilser.integration.entity.SiacTAttoAllegato;
import it.csi.siac.siacbilser.integration.entity.SiacTAttoAmm;
import it.csi.siac.siacbilser.integration.entity.SiacTEnteProprietario;
import it.csi.siac.siacbilser.integration.entity.SiacTLiquidazione;
import it.csi.siac.siacbilser.integration.entity.SiacTSoggetto;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDAttoAllegatoStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocFamTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDSplitreverseIvaTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTAttrEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.AllegatoAttoStatoConverter;
import it.csi.siac.siacbilser.integration.entitymapping.converter.base.Converters;
import it.csi.siac.siacbilser.integration.entitymapping.mapper.SiacDAttoAllegatoChecklistAllegatoAttoChecklistMapper;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Messaggio;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.AllegatoAttoModelDetail;
import it.csi.siac.siacfin2ser.model.DatiSoggettoAllegato;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.StatoOperativoAllegatoAtto;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.allegatoattochecklist.Checklist;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * Data access delegate di un AllegatoAtto .
 *
 * @author Domenico
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class AllegatoAttoDad extends ExtendedBaseDadImpl {
	
	/** The pre documento dao. */
	@Autowired
	private AllegatoAttoDao allegatoAttoDao;
	
	@Autowired
	private ControlloImportiImpegniVincolatiDao controlloImportiImpegniVincolatiDao;
	
	/** The siac t liquidazione repository */
	@Autowired
	private LiquidazioneDao liquidazioneDao;
	
	/** The siac t liquidazione repository */
	@Autowired
	private SubdocumentoDao subdocumentoDao;
	
	/** The siac t subdoc repository */
	@Autowired
	private SiacTSubdocRepository siacTSubdocRepository;
	
	@Autowired
	private SiacRAttoAllegatoSogRepository siacRAttoAllegatoSogRepository;
	
	@Autowired
	private SiacTAttoAllegatoRepository siacTAttoAllegatoRepository;
	
	@Autowired
	private SiacDAttoAllegatoChecklistRepository siacDAttoAllegatoCheclistRepository;
	
	@Autowired
	private SiacRSubdocAttoAmmRepository siacRSubdocAttoAmmRepository;
	
	@Autowired
	private SiacRAttoAllegatoElencoDocRepository siacRAttoAllegatoElencoDocRepository;
	
	@Autowired
	private SiacRElencoDocSubdocRepository siacRElencoDocSubdocRepository;
	
	
	@Autowired
	private SiacDAttoAllegatoChecklistAllegatoAttoChecklistMapper siacDAttoAllegatoChecklistAllegatoAttoChecklistMapper;
	
	
	/** The eef. */
	@Autowired
	private EnumEntityFactory eef;
	
	/**
	 * Find allegato atto by id.
	 *
	 * @param uid the uid
	 * @return the allegato atto
	 */
	public AllegatoAtto findAllegatoAttoById(Integer uid) {
		final String methodName = "findAllegatoAttoById";		
		log.debug(methodName, "uid: "+ uid);
		SiacTAttoAllegato siacTAttoAllegato = allegatoAttoDao.findById(uid);
		if(siacTAttoAllegato == null) {
			log.debug(methodName, "Impossibile trovare il AllegatoAtto con id: " + uid);
		}
		AllegatoAtto ris = mapNotNull(siacTAttoAllegato, AllegatoAtto.class, BilMapId.SiacTAttoAllegato_AllegatoAtto);	
		return 	ris;
	}
	
	/**
	 * Find allegato atto by id.
	 *
	 * @param uid the uid
	 * @param converters the converters
	 * @return the allegato atto
	 */
	public AllegatoAtto findAllegatoAttoById(Integer uid, Class<? extends CustomConverter>... converters) {
		final String methodName = "findAllegatoAttoById";		
		log.debug(methodName, "uid: "+ uid);
		SiacTAttoAllegato siacTAttoAllegato = allegatoAttoDao.findById(uid);
		if(siacTAttoAllegato == null) {
			log.warn(methodName, "Impossibile trovare il AllegatoAtto con id: " + uid);
		}
		return  mapNotNull(siacTAttoAllegato, AllegatoAtto.class, BilMapId.SiacTAttoAllegato_AllegatoAtto_Base, converters);
		
	
	}
	
	/**
	 * Find allegato atto by id.
	 *
	 * @param uid the uid
	 * @param modelDetails the model details
	 * @return the allegato atto
	 */
	public AllegatoAtto findAllegatoAttoById(Integer uid, AllegatoAttoModelDetail... modelDetails) {
		final String methodName = "findAllegatoAttoById";		
		log.debug(methodName, "uid: "+ uid);
		SiacTAttoAllegato siacTAttoAllegato = allegatoAttoDao.findById(uid);
		if(siacTAttoAllegato == null) {
			log.warn(methodName, "Impossibile trovare il AllegatoAtto con id: " + uid);
		}
		return  mapNotNull(siacTAttoAllegato, AllegatoAtto.class, BilMapId.SiacTAttoAllegato_AllegatoAtto_Base, Converters.byModelDetails(modelDetails));
	
	}
	
	/**
	 * Find allegato atto by id.
	 *
	 * @param uids the uids
	 * @param modelDetails the model details
	 * @return the allegato atto
	 */
	public List<AllegatoAtto> findAllegatiAttoByIds(List<Integer> uids, AllegatoAttoModelDetail... modelDetails) {
		final String methodName = "findAllegatoAttoById";		
		List<SiacTAttoAllegato> siacTAttoAllegato = siacTAttoAllegatoRepository.findListaAllegatiByattoAlIds(uids);
		if(siacTAttoAllegato == null || siacTAttoAllegato.isEmpty()) {
			log.warn(methodName, "Impossibile trovare il AllegatoAtto per gli id forniti.");
		}
		return convertiLista(siacTAttoAllegato, AllegatoAtto.class, BilMapId.SiacTAttoAllegato_AllegatoAtto_Base, Converters.byModelDetails(modelDetails)); //  mapNotNull(siacTAttoAllegato, AllegatoAtto.class, BilMapId.SiacTAttoAllegato_AllegatoAtto_Base, Converters.byModelDetails(modelDetails));
	
	}
	
	
	public AllegatoAtto findAllegatoAttoByAttoAmministrativo(AttoAmministrativo attoAmministrativo, AllegatoAttoModelDetail... modelDetails) {
		
		List<SiacTAttoAllegato> siacTAttoAllegatos = siacTAttoAllegatoRepository.findAllegatiNonAnnullatiByAttoAmministrativo(attoAmministrativo.getUid());
		
		if(siacTAttoAllegatos==null || siacTAttoAllegatos.isEmpty()){
			//NON sono presenti AllegatoAtto con l'atto amministrativo passato come parametro.
			return null;
		}
		
		int size = siacTAttoAllegatos.size();
		
		if(size>1){
			//NON sono presenti AllegatoAtto con l'atto amministrativo passato come parametro.
			throw new IllegalStateException("Sono presenti "+size +" AllegatoAtto legati all'AttoAmministrativo con uid: "
					+ attoAmministrativo.getUid() + ". Dovrebbe essercene uno solo: verificare i dati su DB.");
		}
		
		return  mapNotNull(siacTAttoAllegatos.get(0), AllegatoAtto.class, BilMapId.SiacTAttoAllegato_AllegatoAtto_Base, Converters.byModelDetails(modelDetails));
	}
	
	public AllegatoAtto findAllegatoAttoByElencoDocumentiAllegato(ElencoDocumentiAllegato eda, AllegatoAttoModelDetail... modelDetails) {
		Set<String> attoalStatoCodes = new HashSet<String>();
		attoalStatoCodes.add(StatoOperativoAllegatoAtto.ANNULLATO.getCodice());
		attoalStatoCodes.add(StatoOperativoAllegatoAtto.RIFIUTATO.getCodice());
		
		List<SiacTAttoAllegato> siacTAttoAllegatos = siacTAttoAllegatoRepository.findSiacTAttoAllegatoByEldocIdAndNotByAttoalStatoCodes(eda.getUid(), attoalStatoCodes);
		
		if(siacTAttoAllegatos==null || siacTAttoAllegatos.isEmpty()){
			//Non sono presenti AllegatoAtto con l'atto amministrativo passato come parametro.
			return null;
		}
		
		for(SiacTAttoAllegato staa : siacTAttoAllegatos) {
			if(staa != null) {
				return mapNotNull(staa, AllegatoAtto.class, BilMapId.SiacTAttoAllegato_AllegatoAtto_Base, Converters.byModelDetails(modelDetails));
			}
		}
		
		return null;
	}
	
	/**
	 * Inserisci anagrafica allegato atto.
	 *
	 * @param allegatoAtto the documento
	 */
	public void inserisciAllegatoAtto(AllegatoAtto allegatoAtto) {		
		SiacTAttoAllegato siacRAttoAllegato = buildSiacTAttoAllegato(allegatoAtto);	
		siacRAttoAllegato.setLoginCreazione(loginOperazione);
		siacRAttoAllegato.setLoginModifica(loginOperazione);
		allegatoAttoDao.create(siacRAttoAllegato);
		allegatoAtto.setUid(siacRAttoAllegato.getUid());
	}	
	
	

	/**
	 * Aggiorna anagrafica allegato atto.
	 *
	 * @param documento the documento
	 */
	public void aggiornaAllegatoAtto(AllegatoAtto documento) {
		SiacTAttoAllegato siacRAttoAllegato = buildSiacTAttoAllegato(documento);	
		siacRAttoAllegato.setLoginModifica(loginOperazione);		
		allegatoAttoDao.update(siacRAttoAllegato);
		documento.setUid(siacRAttoAllegato.getUid());
	}	
	
	
	/**
	 * Builds the siac t atto allegato.
	 *
	 * @param allegatoAtto the documento
	 * @return the siac t predoc
	 */
	private SiacTAttoAllegato buildSiacTAttoAllegato(AllegatoAtto allegatoAtto) {
		SiacTAttoAllegato siacRAttoAllegato = new SiacTAttoAllegato();
		siacRAttoAllegato.setLoginOperazione(loginOperazione);
		allegatoAtto.setLoginOperazione(loginOperazione);
		map(allegatoAtto, siacRAttoAllegato, BilMapId.SiacTAttoAllegato_AllegatoAtto);
		return siacRAttoAllegato;
	}
	
	
	
	/**
	 * Ricerca puntuale allegato atto.
	 *
	 * @param allegatoAtto the pre doc
	 * @return the allegato atto
	 */
	public AllegatoAtto ricercaPuntualeAllegatoAtto(AllegatoAtto allegatoAtto) {
		final String methodName = "ricercaPuntualeAllegatoAtto";
		
		ParametriPaginazione parametriPaginazione = new ParametriPaginazione();		
		parametriPaginazione.setNumeroPagina(0);
		parametriPaginazione.setElementiPerPagina(1000);
		
		Page<SiacTAttoAllegato> lista = allegatoAttoDao.ricercaSinteticaAllegatoAtto(
				allegatoAtto.getEnte().getUid(),
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				toPageable(parametriPaginazione));

		if(lista.getContent().isEmpty()) {
			log.debug(methodName, "Nessun allegato atto trovato");
			return null;
		}
		
		SiacTAttoAllegato siacRAttoAllegato = lista.getContent().get(0);				
		
		return mapNotNull(siacRAttoAllegato, AllegatoAtto.class, BilMapId.SiacTAttoAllegato_AllegatoAtto_Base);
		
	}
	
	
	
	
	
	/**
	 * Ricerca sintetica allegato atto.
	 *
	 * @param allegatoAtto the allegato atto
	 * @param elencoDocumentiAllegato the elenco documenti allegato
	 * @param scadenzaDa the scadenza da
	 * @param scadenzaA the scadenza a
	 * @param parametriPaginazione the parametri paginazione 
	 * 
	 * @return the lista paginata
	 */
	/*
	public ListaPaginata<AllegatoAtto> ricercaSinteticaAllegatoAtto(AllegatoAtto allegatoAtto,ElencoDocumentiAllegato elencoDocumentiAllegato, Date scadenzaDa, Date scadenzaA, Boolean flagRitenute, ParametriPaginazione parametriPaginazione) {

		final List<DatiSoggettoAllegato> datiSoggettiAllegati = allegatoAtto.getDatiSoggettiAllegati();
		
		Soggetto soggetto = null;
		Impegno  impegno  = null;
		
		// Popolo il soggetto a partire dagli eventuali dati
		for(DatiSoggettoAllegato dsa : datiSoggettiAllegati) {
			if(dsa.getSoggetto() != null && dsa.getSoggetto().getUid() != 0) {
				soggetto = dsa.getSoggetto();
				break;
			}
		}
		
		Page<SiacTAttoAllegato> lista = allegatoAttoDao.ricercaSinteticaAllegatoAtto(
				allegatoAtto.getEnte().getUid(),
				allegatoAtto.getCausale(),
				allegatoAtto.getStatoOperativoAllegatoAtto() != null ? SiacDAttoAllegatoStatoEnum.byStatoOperativo(allegatoAtto.getStatoOperativoAllegatoAtto()) : null,
				// Date scadenza
				scadenzaDa,
				scadenzaA,
				// Provvedimento
				allegatoAtto.getAttoAmministrativo() != null ? allegatoAtto.getAttoAmministrativo().getUid() : null,
				// Soggetto
				soggetto != null ? soggetto.getUid() : null,
				// impegno
				impegno != null ? impegno.getUid() : null,
				// Elenco
				elencoDocumentiAllegato != null ? elencoDocumentiAllegato.getUid() : null,
				// Ritenute
				flagRitenute,
				// Paginazione
				toPageable(parametriPaginazione));
		
		return toListaPaginata(lista, AllegatoAtto.class, BilMapId.SiacTAttoAllegato_AllegatoAtto_Medium);
		
	}
	*/
	/**
	 * Ricerca sintetica allegato atto.
	 *
	 * @param allegatoAtto the allegato atto
	 * @param elencoDocumentiAllegato the elenco documenti allegato
	 * @param scadenzaDa the scadenza da
	 * @param scadenzaA the scadenza a
	 * @param flagRitenute il flag delle ritenute
	 * @param parametriPaginazione i parametri di paginazione
	 * @param modelDetails i model detail
	 * 
	 * @return the lista paginata
	 */
	public ListaPaginata<AllegatoAtto> ricercaSinteticaAllegatoAtto(AllegatoAtto allegatoAtto, ElencoDocumentiAllegato elencoDocumentiAllegato, Date scadenzaDa, Date scadenzaA,
			Boolean flagRitenute,Soggetto soggetto,Impegno impegno,SubImpegno subImpegno, List<StatoOperativoAllegatoAtto> statiOperativiFiltro,
			List<AttoAmministrativo> listaAttoAmministrativo, Bilancio bilancio, ParametriPaginazione parametriPaginazione, AllegatoAttoModelDetail... modelDetails ) {
		
		List<Integer> listaUidAttoAmm = new ArrayList<Integer>();
		for(AttoAmministrativo attoamm: listaAttoAmministrativo){
			listaUidAttoAmm.add(attoamm.getUid());
		}
		List<SiacDAttoAllegatoStatoEnum> siacDAttoAllegatoStatoEnums = SiacDAttoAllegatoStatoEnum.byListaStatiOperativi(statiOperativiFiltro);
		
		Page<SiacTAttoAllegato> lista = allegatoAttoDao.ricercaSinteticaAllegatoAtto(
				allegatoAtto.getEnte().getUid(),
				allegatoAtto.getCausale(),
				allegatoAtto.getStatoOperativoAllegatoAtto() != null ? SiacDAttoAllegatoStatoEnum.byStatoOperativo(allegatoAtto.getStatoOperativoAllegatoAtto()) : null,
				// Date scadenza
				scadenzaDa,
				scadenzaA,
				
				// Provvedimento
				//mapToUidIfNotZero(allegatoAtto.getAttoAmministrativo()),
				// SIAC-5660 Provvedimento
				listaUidAttoAmm,
				
				// Soggetto
				mapToUidIfNotZero(soggetto),
				// Impegno
				mapToUidIfNotZero(impegno),
				// SubImpegno
				mapToUidIfNotZero(subImpegno),
				// Elenco
				mapToUidIfNotZero(elencoDocumentiAllegato),
				// Ritenute
				flagRitenute,
				siacDAttoAllegatoStatoEnums.isEmpty()? null : siacDAttoAllegatoStatoEnums,
				// SIAC-6166
				bilancio != null && bilancio.getAnno() != 0 ? Integer.valueOf(bilancio.getAnno()) : null,
				allegatoAtto.getHasImpegnoConfermaDurc(),
				// Paginazione
				toPageable(parametriPaginazione));
		
		return toListaPaginata(lista, AllegatoAtto.class, BilMapId.SiacTAttoAllegato_AllegatoAtto_Base, modelDetails);
		
	}
	
	
	/**
	 * Aggiorna stato allegato atto.
	 *
	 * @param uidAllegatoAtto the uid allegato atto
	 * @param statoOperativoAllegatoAtto the stato operativo allegato atto
	 */
	public void aggiornaStatoAllegatoAtto(Integer uidAllegatoAtto, StatoOperativoAllegatoAtto statoOperativoAllegatoAtto) {
		SiacTAttoAllegato siacTAttoAllegato = allegatoAttoDao.findById(uidAllegatoAtto);	
		aggiornaStatoAllegatoAtto(siacTAttoAllegato, statoOperativoAllegatoAtto);	
	}


	/**
	 * Aggiorna stato allegato atto.
	 *
	 * @param siacTAttoAllegato the siac t predoc
	 * @param statoOperativoAllegatoAtto the stato operativo allegato atto
	 */
	private void aggiornaStatoAllegatoAtto(SiacTAttoAllegato siacTAttoAllegato, StatoOperativoAllegatoAtto statoOperativoAllegatoAtto) {
		Date dataCancellazione = new Date();
		if(siacTAttoAllegato.getSiacRAttoAllegatoStatos()==null){
			siacTAttoAllegato.setSiacRAttoAllegatoStatos(new ArrayList<SiacRAttoAllegatoStato>());
		}
		for(SiacRAttoAllegatoStato r : siacTAttoAllegato.getSiacRAttoAllegatoStatos()){
			r.setDataCancellazioneIfNotSet(dataCancellazione);					
		}
		Date now = new Date();
		SiacRAttoAllegatoStato siacRAttoAllegatoStato = new SiacRAttoAllegatoStato();
		SiacDAttoAllegatoStato siacDAttoAllegatoStato = eef.getEntity(SiacDAttoAllegatoStatoEnum.byStatoOperativo(statoOperativoAllegatoAtto), siacTAttoAllegato.getSiacTEnteProprietario().getUid());
		siacRAttoAllegatoStato.setSiacDAttoAllegatoStato(siacDAttoAllegatoStato);		
		siacRAttoAllegatoStato.setSiacTAttoAllegato(siacTAttoAllegato);			
		siacRAttoAllegatoStato.setSiacTEnteProprietario(siacTAttoAllegato.getSiacTEnteProprietario());
		siacRAttoAllegatoStato.setDataModificaInserimento(now);
		siacRAttoAllegatoStato.setLoginOperazione(loginOperazione);		
		
		siacTAttoAllegato.addSiacRAttoAllegatoStato(siacRAttoAllegatoStato);
		
	}

	/**
	 * Trova le liquidazioni per le quote legate a documenti di tipo AllegatoAtto.
	 * 
	 * @param allegatoAtto l'allegato di riferimento
	 * 
	 * @return le liquidazioni associate
	 */
	public List<Liquidazione> findLiquidazioneDiQuoteAllegatoByAllegatoAtto(AllegatoAtto allegatoAtto) {
		return findLiquidazioneDiQuoteAllegatoByAllegatoAtto(allegatoAtto, Boolean.TRUE);
	}
	
	/**
	 * Trova le liquidazioni per le quote legate a documenti di tipo AllegatoAtto.
	 * 
	 * @param allegatoAtto l'allegato di riferimento
	 * 
	 * @return le liquidazioni associate
	 */
	public List<Liquidazione> findLiquidazioneDiQuoteNonAllegatoByAllegatoAtto(AllegatoAtto allegatoAtto) {
		return findLiquidazioneDiQuoteAllegatoByAllegatoAtto(allegatoAtto, Boolean.FALSE);
	}
	
	/**
	 * Trova le liquidazioni per le quote legate all'allegato atto.
	 * 
	 * @param allegatoAtto l'allegato di riferimento
	 * @param tipoDocumentoAllegatoAtto se il tipo del documento debba essere allegato atto
	 * 
	 * @return le liquidazioni associate
	 */
	protected List<Liquidazione> findLiquidazioneDiQuoteAllegatoByAllegatoAtto(AllegatoAtto allegatoAtto, Boolean tipoDocumentoAllegatoAtto) {
		List<SiacTLiquidazione> siacTLiquidaziones = liquidazioneDao.findByAttoalId(allegatoAtto.getUid(), tipoDocumentoAllegatoAtto);
		return convertiLista(siacTLiquidaziones, Liquidazione.class, BilMapId.SiacTLiquidazione_Liquidazione);
	}
	
	/**
	 * Elimina il legame con le quote di tipo non-allegatoAtto.
	 * 
	 * @param allegatoAtto l'allegato atto
	 */
	public void eliminaLegameQuoteNonAllegato(AllegatoAtto allegatoAtto) {
		eliminaLegameQuoteAttoAmm(allegatoAtto, Boolean.FALSE);
	}
	
	/**
	 * Elimina il legame con le quote di tipo allegatoAtto.
	 * 
	 * @param allegatoAtto l'allegato atto
	 */
	public void eliminaLegameQuoteAllegato(AllegatoAtto allegatoAtto) {
		eliminaLegameQuoteAttoAmm(allegatoAtto, Boolean.TRUE);
		eliminaLegameQuoteMovGest(allegatoAtto, Boolean.TRUE);
	}
	
	
	/**
	 * Elimina il legame con le quote di tipo non-allegatoAtto.
	 * 
	 * @param allegatoAtto l'allegato atto
	 * @param tipoDocumentoAllegatoAtto se il tipo del documento debba essere allegato atto
	 */
	protected void eliminaLegameQuoteAttoAmm(AllegatoAtto allegatoAtto, Boolean tipoDocumentoAllegatoAtto) {
		final Date now = new Date();
		
		List<SiacRSubdocAttoAmm> siacRSubdocAttoAmms = subdocumentoDao.findSiacRSubdocAttoAmmByAttoAllegatoAndTipoDocumentoAllegato(allegatoAtto.getUid(),
				tipoDocumentoAllegatoAtto);
		if(siacRSubdocAttoAmms != null) {
			for(SiacRSubdocAttoAmm srsaa : siacRSubdocAttoAmms) {
				srsaa.setDataCancellazione(now);
			}
		}
	}
	
	private void eliminaLegameQuoteMovGest(AllegatoAtto allegatoAtto, Boolean tipoDocumentoAllegatoAtto) {
		final Date now = new Date();
		List<SiacRSubdocMovgestT> siacRSubdocMovgestTs = subdocumentoDao.findSiacRSubdocMovgestTByAttoAllegatoAndTipoDocumentoAllegato(allegatoAtto.getUid(),
				tipoDocumentoAllegatoAtto);
		if(siacRSubdocMovgestTs != null) {
			for(SiacRSubdocMovgestT srsmg : siacRSubdocMovgestTs) {
				srsmg.setDataCancellazione(now);
			}
		}
	}

	
	/**
	 * Trova i subdocumenti di spesa a partire dall'allegato atto.
	 * 
	 * @param allegatoAtto l'allegato atto
	 * 
	 * @return i subdocumenti
	 */
	public List<SubdocumentoSpesa> findSubdocumentiSpesaByAllegatoAtto(AllegatoAtto allegatoAtto) {
		List<SiacTSubdoc> siacTSubdocs = siacTSubdocRepository.findSiacTSubdocByAttoalIdAndDocFamTipoCodeIn(allegatoAtto.getUid(),
				Arrays.asList(SiacDDocFamTipoEnum.Spesa.getCodice(), SiacDDocFamTipoEnum.IvaSpesa.getCodice()));
		// Mi interessa solo l'uid. Dunque ignoro i mappers 'belli e buoni' e uso il mapper base
		return convertiLista(siacTSubdocs, SubdocumentoSpesa.class, BilMapId.SiacTSubdoc_SubdocumentoSpesa_Base);
	}
	
	public List<SubdocumentoSpesa> findSubdocumentiSpesaTipoAllegatoByAllegatoAtto(AllegatoAtto allegatoAtto) {
		final String codiceTipoDocumentoAllegato = "ALG";
		List<SiacTSubdoc> siacTSubdocs = siacTSubdocRepository.findSiacTSubdocByAttoalIdAndDocFamTipoCodeInAndDocTipoCode(allegatoAtto.getUid(),
				Arrays.asList(SiacDDocFamTipoEnum.Spesa.getCodice(), SiacDDocFamTipoEnum.IvaSpesa.getCodice()), codiceTipoDocumentoAllegato);
		// Mi interessa solo l'uid. Dunque ignoro i mappers 'belli e buoni' e uso il mapper base
		return convertiLista(siacTSubdocs, SubdocumentoSpesa.class, BilMapId.SiacTSubdoc_SubdocumentoSpesa_Base);
	}
	
	/**
	 * Trova i subdocumenti di spesa a partire dall'allegato atto.
	 * 
	 * @param allegatoAtto l'allegato atto
	 * 
	 * @return i subdocumenti
	 */
	public List<SubdocumentoEntrata> findSubdocumentiEntrataByAllegatoAtto(AllegatoAtto allegatoAtto) {
		List<SiacTSubdoc> siacTSubdocs = siacTSubdocRepository.findSiacTSubdocByAttoalIdAndDocFamTipoCodeIn(allegatoAtto.getUid(),
				Arrays.asList(SiacDDocFamTipoEnum.Entrata.getCodice(), SiacDDocFamTipoEnum.IvaEntrata.getCodice()));
		// Mi interessa solo l'uid. Dunque ignoro i mappers 'belli e buoni' e uso il mapper base
		return convertiLista(siacTSubdocs, SubdocumentoEntrata.class, BilMapId.SiacTSubdoc_SubdocumentoEntrata_Base);
	}
	
	public List<SubdocumentoEntrata> findSubdocumentiEntrataTipoAllegatoByAllegatoAtto(AllegatoAtto allegatoAtto) {
		final String codiceTipoDocumentoAllegato = "ALG";
		List<SiacTSubdoc> siacTSubdocs = siacTSubdocRepository.findSiacTSubdocByAttoalIdAndDocFamTipoCodeInAndDocTipoCode(allegatoAtto.getUid(),
				Arrays.asList(SiacDDocFamTipoEnum.Entrata.getCodice(), SiacDDocFamTipoEnum.IvaEntrata.getCodice()), codiceTipoDocumentoAllegato);
		// Mi interessa solo l'uid. Dunque ignoro i mappers 'belli e buoni' e uso il mapper base
		return convertiLista(siacTSubdocs, SubdocumentoEntrata.class, BilMapId.SiacTSubdoc_SubdocumentoEntrata_Base);
	}
	
	/**
	 * Ottiene le quote documento di spesa rilevanti ai fini IVA (flagRilevanteIVA = true) 
	 * non ancora collegate a liquidazione senza numero di registrazione iva valorizzato  (subdocNregIva).
	 *
	 * @param allegatoAtto the allegato atto
	 * @return the list
	 */
	public List<SubdocumentoSpesa> findSubdocumentiSpesaRilevantiIvaNonCollegatiALiquidazioneSenzaNregIvaByAllegatoAtto(AllegatoAtto allegatoAtto) {
		List<SiacTSubdoc> siacTSubdocs = siacTSubdocRepository.findSiacTSubdocByAttoalIdAndDocFamTipoCodeAndNotLiquidazioneAndNotNregIva(allegatoAtto.getUid(),Arrays.asList(SiacDDocFamTipoEnum.Spesa.getCodice(), SiacDDocFamTipoEnum.IvaSpesa.getCodice()));		
		return convertiLista(siacTSubdocs, SubdocumentoSpesa.class, BilMapId.SiacTSubdoc_SubdocumentoSpesa_Base);		
	}
	
	/**
	 * Ottiene le quota di documento di spesa associate all'allegato, non ancora collegata a liquidazione e con  importoDaPagare  > 0
	 *
	 * @param allegatoAtto the allegato atto
	 * @return the list
	 */
	public List<SubdocumentoSpesa> findSubdocumentiSpesaRilevantiIvaNonCollegatiALiquidazioneConImportoDaPagareMaggioreDiZeroNonSospesi(AllegatoAtto allegatoAtto) {
		List<SiacTSubdoc> siacTSubdocs = siacTSubdocRepository.findSiacTSubdocByAttoalIdAndDocFamTipoCodeAndNotLiquidazioneAndImportoDaPagareMaggioreDiZeroAndNonSospesi(allegatoAtto.getUid(),	Arrays.asList(SiacDDocFamTipoEnum.Spesa.getCodice(), SiacDDocFamTipoEnum.IvaSpesa.getCodice()));
		return convertiLista(siacTSubdocs, SubdocumentoSpesa.class, BilMapId.SiacTSubdoc_SubdocumentoSpesa /*_Base /* Mi serve tutto..*/);
		
	}
	
	
	/**
	 * conta le quota di documento di spesa associate all'allegato, non ancora collegata a liquidazione e con  importoDaPagare  > 0
	 *
	 * @param allegatoAtto the allegato atto
	 * @return the list
	 */
	//SIAC-5572 controllo che non ci siano subdocumenti sospesi 
	public Integer countSubdocumentiSpesaRilevantiIvaNonCollegatiALiquidazioneConImportoDaPagareMaggioreDiZero(AllegatoAtto allegatoAtto) {
		Long numEl = siacTSubdocRepository.countSiacTSubdocByAttoalIdAndDocFamTipoCodeAndNotLiquidazioneAndImportoDaPagareMaggioreDiZero(allegatoAtto.getUid(),Arrays.asList(SiacDDocFamTipoEnum.Spesa.getCodice(), SiacDDocFamTipoEnum.IvaSpesa.getCodice()));
		return numEl.intValue();
		
	}

	/**
	 * Ottiene le quote di entrata di un allegato atto NON collegate ad un movimento dello stesso bilancio su cui si sta operando. 
	 *
	 * @param allegatoAtto the allegato atto
	 * @param bilancio 
	 * @return the list
	 */
	public List<SubdocumentoEntrata> findSubdocumentiEntrataSenzaMovimentoDelloStessoBilancio(AllegatoAtto allegatoAtto, Bilancio bilancio) {
		List<SiacTSubdoc> siacTSubdocs = siacTSubdocRepository.findSiacTSubdocByAttoalIdAndDocFamTipoCodeAndNotMovimento(allegatoAtto.getUid(),
				Arrays.asList(SiacDDocFamTipoEnum.Entrata.getCodice(), SiacDDocFamTipoEnum.IvaEntrata.getCodice()), bilancio.getUid());
		return convertiLista(siacTSubdocs, SubdocumentoEntrata.class, BilMapId.SiacTSubdoc_SubdocumentoEntrata_Base);
	
	}
	
	/**
	 * Ottiene le quote di spesa di un allegato atto NON collegate ad un movimento dello stesso bilancio su cui si sta operando. 
	 *
	 * @param allegatoAtto the allegato atto
	 * @return the list
	 */
	public List<SubdocumentoSpesa> findSubdocumentiSpesaSenzaLiquidazioneESenzaMovimentoDelloStessoBilancio(AllegatoAtto allegatoAtto, Bilancio bilancio) {
		List<SiacTSubdoc> siacTSubdocs = siacTSubdocRepository.findSiacTSubdocByAttoalIdAndDocFamTipoCodeAndNotMovimentoAndNotLiquidazione(allegatoAtto.getUid(),
				Arrays.asList(SiacDDocFamTipoEnum.Spesa.getCodice(), SiacDDocFamTipoEnum.IvaSpesa.getCodice()), bilancio.getUid());
		return convertiLista(siacTSubdocs, SubdocumentoSpesa.class, BilMapId.SiacTSubdoc_SubdocumentoSpesa_Base);
	
	}
	
	
	/**
	 * Controlla l'esistenza di una relazione tra soggetto ed allegato.
	 *
	 * @param allegatoAtto the allegato atto
	 * @param soggetto the soggetto
	 * @return the esiste relazione soggetto allegato
	 */
	public Boolean esisteRelazioneSoggettoAllegato(AllegatoAtto allegatoAtto, Soggetto soggetto) {
		List<SiacRAttoAllegatoSog> siacRAttoAllegatoSogs =
				siacRAttoAllegatoSogRepository.findBySiacTAttoAllegatoAndSiacTSoggetto(allegatoAtto.getUid(), soggetto.getUid());
		return siacRAttoAllegatoSogs != null && !siacRAttoAllegatoSogs.isEmpty();
	}
	
	/**
	 * Controlla l'esistenza di una relazione tra soggetto ed allegato.
	 *
	 * @param uid the allegato atto
	 * @return the esiste relazione soggetto allegato
	 */
	public Boolean esisteRelazioneSoggettoAllegato(Integer uid) {
		SiacRAttoAllegatoSog siacRAttoAllegatoSog = siacRAttoAllegatoSogRepository.findOne(uid);
		return siacRAttoAllegatoSog != null;
	}
	
	/**
	 * Inserisce la relazione tra soggetto ed allegato atto.
	 * 
	 * @param datiSoggettoAllegato the dati soggetto allegato
	 */
	public void inserisciRelazioneSoggettoAllegato(DatiSoggettoAllegato datiSoggettoAllegato) {
		SiacRAttoAllegatoSog siacRAttoAllegatoSog = buildSiacRAttoAllegatoSog(datiSoggettoAllegato);
		
		SiacTAttoAllegato siacTAttoAllegato = new SiacTAttoAllegato();
		siacTAttoAllegato.setUid(datiSoggettoAllegato.getAllegatoAtto().getUid());
		siacRAttoAllegatoSog.setSiacTAttoAllegato(siacTAttoAllegato);
		
		siacRAttoAllegatoSog.setDataModificaInserimento(new Date());
		
		siacRAttoAllegatoSogRepository.saveAndFlush(siacRAttoAllegatoSog);
		datiSoggettoAllegato.setUid(siacRAttoAllegatoSog.getUid() == null ? 0 : siacRAttoAllegatoSog.getUid().intValue());
	}
	
	/**
	 * Builds the siac r atto allegato sog.
	 *
	 * @param datiSoggettoAllegato the dati soggetto allegato
	 * @return the siac r atto allegato sog
	 */
	private SiacRAttoAllegatoSog buildSiacRAttoAllegatoSog(DatiSoggettoAllegato datiSoggettoAllegato) {
		SiacRAttoAllegatoSog siacRAttoAllegatoSog = new SiacRAttoAllegatoSog();
		siacRAttoAllegatoSog.setLoginOperazione(loginOperazione);
		datiSoggettoAllegato.setLoginOperazione(loginOperazione);
		map(datiSoggettoAllegato, siacRAttoAllegatoSog, BilMapId.SiacRAttoAllegatoSog_DatiSoggettoAllegato);
		return siacRAttoAllegatoSog;
	}
	
	/**
	 * Aggiorna relazione soggetto allegato.
	 *
	 * @param datiSoggettoAllegato the dati soggetto allegato
	 */
	public void aggiornaRelazioneSoggettoAllegato(DatiSoggettoAllegato datiSoggettoAllegato) {
		SiacRAttoAllegatoSog siacRAttoAllegatoSog = buildSiacRAttoAllegatoSog(datiSoggettoAllegato);
		
		SiacTAttoAllegato siacTAttoAllegato = new SiacTAttoAllegato();
		siacTAttoAllegato.setUid(datiSoggettoAllegato.getAllegatoAtto().getUid());
		siacRAttoAllegatoSog.setSiacTAttoAllegato(siacTAttoAllegato);
		
		siacRAttoAllegatoSog.setDataModificaInserimento(new Date());
		
		siacRAttoAllegatoSogRepository.save(siacRAttoAllegatoSog);
		datiSoggettoAllegato.setUid(siacRAttoAllegatoSog.getUid() == null ? 0 : siacRAttoAllegatoSog.getUid().intValue());
	}
	
	public List<DatiSoggettoAllegato> findDatiSoggettoAllegatoByAllegatoAtto(AllegatoAtto allegatoAtto) {
		List<SiacRAttoAllegatoSog> siacRAttoAllegatoSogs = siacRAttoAllegatoSogRepository.findBySiacTAttoAllegato(allegatoAtto.getUid());
		return convertiLista(siacRAttoAllegatoSogs, DatiSoggettoAllegato.class, BilMapId.SiacRAttoAllegatoSog_DatiSoggettoAllegato);
	}
	
	public List<DatiSoggettoAllegato> findDatiSoggettoAllegatoByAllegatoAttoAndSoggetto(AllegatoAtto allegatoAtto, Soggetto soggetto) {
		List<SiacRAttoAllegatoSog> siacRAttoAllegatoSogs = siacRAttoAllegatoSogRepository.findBySiacTAttoAllegatoAndSiacTSoggetto(allegatoAtto.getUid(), soggetto.getUid());
		return convertiLista(siacRAttoAllegatoSogs, DatiSoggettoAllegato.class, BilMapId.SiacRAttoAllegatoSog_DatiSoggettoAllegato);
	}
	
	
	/**
	 * Find dati soggetto allegato by allegato and by subdoc id.
	 *
	 * @param uidAllegato the uid allegato
	 * @param uidSubdoc the uid subdoc
	 * @return the dati soggetto allegato
	 */
	public DatiSoggettoAllegato findDatiSoggettoAllegatoByAllegatoAndBySubdocId(int uidAllegato, int uidSubdoc) {
		SiacRAttoAllegatoSog siacRAttoAllegatoSog = siacRAttoAllegatoSogRepository.findByAttoAllegatoAndSubdocId(uidAllegato, uidSubdoc);
		return mapNotNull(siacRAttoAllegatoSog, DatiSoggettoAllegato.class, BilMapId.SiacRAttoAllegatoSog_DatiSoggettoAllegato);
	}
	
	/**
	 * Restituisce tutti gli Allegati atto collegati ad un provvisorio di cassa.
	 *
	 * @param provvCassaId the provv cassa id
	 * @return the list
	 */
	public List<AllegatoAtto> findAllegatiAttoDettaglioByProvvisorioDiCassa(int provvCassaId) {
		List<SiacTAttoAllegato> siacTAttoAllegatos = siacTAttoAllegatoRepository.findAllegatiAttoByProvvisorioDiCassa(provvCassaId);
		return convertiLista(siacTAttoAllegatos, AllegatoAtto.class, BilMapId.SiacTAttoAllegato_AllegatoAtto);
	}

	public void collegaAllegatoSubdocumento(AllegatoAtto allegatoAtto, Subdocumento<?, ?> subdoc) {
		SiacRSubdocAttoAmm siacRSubdocAttoAmm = new SiacRSubdocAttoAmm();
		
		SiacTAttoAmm siacTAttoAmm = new SiacTAttoAmm();
		siacTAttoAmm.setUid(allegatoAtto.getAttoAmministrativo().getUid());
		siacRSubdocAttoAmm.setSiacTAttoAmm(siacTAttoAmm);
		
		SiacTSubdoc siacTSubdoc = new SiacTSubdoc();
		siacTSubdoc.setUid(subdoc.getUid());
		siacRSubdocAttoAmm.setSiacTSubdoc(siacTSubdoc);
		
		siacRSubdocAttoAmm.setDataModificaInserimento(new Date());
		siacRSubdocAttoAmm.setLoginOperazione(loginOperazione);
		
		SiacTEnteProprietario siacTEnteProprietario = map(ente, SiacTEnteProprietario.class, BilMapId.SiacTEnteProprietario_Ente_Base);
		siacRSubdocAttoAmm.setSiacTEnteProprietario(siacTEnteProprietario);
		
		siacRSubdocAttoAmmRepository.saveAndFlush(siacRSubdocAttoAmm);
	}
	
	

	public void scollegaElenchiDaAllegato(AllegatoAtto allegatoAtto, final Date dataCancellazione) {
		List<SiacRAttoAllegatoElencoDoc> siacRAttoAllegatoElencoDocs = siacRAttoAllegatoElencoDocRepository.findBySiacTAttoAllegato(allegatoAtto.getUid());
		if(siacRAttoAllegatoElencoDocs != null) {
			for(SiacRAttoAllegatoElencoDoc sraaed : siacRAttoAllegatoElencoDocs) {
				sraaed.setDataCancellazioneIfNotSet(dataCancellazione);
			}
		}
	}
	
	public void scollegaQuoteDaElenco(ElencoDocumentiAllegato elenco, final Date dataCancellazione) {
		List<SiacRElencoDocSubdoc> siacRElencoDocSubdocs = siacRElencoDocSubdocRepository.findByElenco(elenco.getUid());
		if(siacRElencoDocSubdocs != null){
			for(SiacRElencoDocSubdoc sreds : siacRElencoDocSubdocs){
				sreds.setDataCancellazione(dataCancellazione);
			}
		}
	}

	public List<SubdocumentoSpesa> findSubdocumentiSpesaSospesiByAllegatoAtto(AllegatoAtto allegatoAtto) {
		List<SiacTSubdoc> siacTSubdocs = siacTSubdocRepository.findSiacTSubdocSospesiByAttoalIdAndDocFamTipoCodeIn(allegatoAtto.getUid(),
				Arrays.asList(SiacDDocFamTipoEnum.Spesa.getCodice(), SiacDDocFamTipoEnum.IvaSpesa.getCodice()));
		// Mi interessa solo l'uid. Dunque ignoro i mappers 'belli e buoni' e uso il mapper base
		return convertiLista(siacTSubdocs, SubdocumentoSpesa.class, BilMapId.SiacTSubdoc_SubdocumentoSpesa_Base);
	}

	public void aggiornaVersioneInvioFirma(AllegatoAtto allegatoAtto) {
		final String methodName = "aggiornaVersioneInvioFirma";
		SiacTAttoAllegato siacTAttoAllegato = siacTAttoAllegatoRepository.findOne(allegatoAtto.getUid());
		siacTAttoAllegato.setAttoalVersioneInvioFirma(allegatoAtto.getVersioneInvioFirmaNotNull());
		siacTAttoAllegato.setAttoalLoginInvioFirma(allegatoAtto.getUtenteVersioneInvioFirma());
		siacTAttoAllegato.setAttoalDataInvioFirma(allegatoAtto.getDataVersioneInvioFirma());
		siacTAttoAllegatoRepository.saveAndFlush(siacTAttoAllegato);
		log.debug(methodName, "Impostata versione "+allegatoAtto.getVersioneInvioFirmaNotNull());
	}

	public Long countDocumentiConRitenute(AllegatoAtto allegatoAtto) {
		Long count = siacTAttoAllegatoRepository.countDocumentiConRitenute(allegatoAtto.getUid());
		return count;
	}

	public Long countFattureNonALGoCCN(AllegatoAtto allegatoAtto) {
		Long count = siacTAttoAllegatoRepository.countFatture(allegatoAtto.getUid(), Arrays.asList("ALG", "CCN"));
		return count;
	}
	
	public Long countQuoteConSplitReverseIstituzionaleOCommerciale(AllegatoAtto allegatoAtto) {
		Long count = siacTAttoAllegatoRepository.countQuoteConSplitReverse(allegatoAtto.getUid(), Arrays.asList(SiacDSplitreverseIvaTipoEnum.SplitIstituzionale.getCodice(), SiacDSplitreverseIvaTipoEnum.SplitCommerciale.getCodice()));
		return count;
	}

	/**
	 * Conta quante quote siano associate ad un allegato
	 * @param allegato atto the allegato atto
	 * @return il count delle quote associate
	 */
	public Long countQuoteAssociateAdAllegato(AllegatoAtto allegatoAtto) {		
		Long count = siacTSubdocRepository.countSiacTSubdocByAttoalId(allegatoAtto.getUid());
		return count;
	}

	/**
	 * Conta quante quote spesa siano associate ad un allegato
	 * @param allegato atto the allegato atto
	 * @return il count delle quote spesa associate
	 */
	public Long countQuoteSpesaAssociateAdAllegato(AllegatoAtto allegatoAtto) {
		Long count = siacTSubdocRepository.countSiacTSubdocByAttoalIdAndDocFamTipoCodeIn(allegatoAtto.getUid(), Arrays.asList(SiacDDocFamTipoEnum.Spesa.getCodice(), SiacDDocFamTipoEnum.IvaSpesa.getCodice()));
		return count;
	}
	/**
	 * Find allegato atto by id. uguale a quella find by uid 
	 * @param uid the uid
	 * @return the allegato atto
	 */
	public AllegatoAtto findAnnoNumeroEVersioneFirmaAllegatoByUid(Integer uid) {
		final String methodName = "findAllegatoAttoById";		
		SiacTAttoAllegato siacTAttoAllegato = allegatoAttoDao.findById(uid);
		if(siacTAttoAllegato == null) {
			log.debug(methodName, "Impossibile trovare il AllegatoAtto con id: " + uid);
			return null;
		}
		AllegatoAtto aa = new AllegatoAtto();
		AttoAmministrativo aam = new AttoAmministrativo();
		if(siacTAttoAllegato.getSiacTAttoAmm()!=null && siacTAttoAllegato.getSiacTAttoAmm().getAttoammNumero() != null) {
			aam.setNumero(siacTAttoAllegato.getSiacTAttoAmm().getAttoammNumero().intValue());
		}
		if(siacTAttoAllegato.getSiacTAttoAmm()!=null && siacTAttoAllegato.getSiacTAttoAmm().getAttoammAnno() != null) {
			aam.setAnno(Integer.parseInt(siacTAttoAllegato.getSiacTAttoAmm().getAttoammAnno()));
		}
		
        aa.setAttoAmministrativo(aam);
        aa.setVersioneInvioFirma(siacTAttoAllegato.getAttoalVersioneInvioFirma());
        aa.setUid(siacTAttoAllegato.getAttoalId());
		return aa ;		
	}

	/**
	 * Ottiene il flag ritenute dell'allegato atto su base dati
	 * @param allegatoAtto l'allegato da cercare
	 * @return il flag dell'allegato
	 */
	public Boolean getFlagRitenute(AllegatoAtto allegatoAtto) {
		SiacTAttoAllegato staa = allegatoAttoDao.findById(allegatoAtto.getUid());
		return staa != null ? staa.getAttoalFlagRitenute() : null;
	}

	/**
	 * Ottiene i soggetti a partire dall'allegato atto
	 * @param allegatoAtto l'allegato atto per cui ottenere i soggetti
	 * @return
	 */
	public List<Soggetto> findSoggettiByAllegatoAtto(AllegatoAtto allegatoAtto) {
		List<SiacTSoggetto> siacTSoggettos = siacTAttoAllegatoRepository.findSiacTSoggettoByAttoalId(allegatoAtto.getUid());
		return convertiLista(siacTSoggettos, Soggetto.class, BilMapId.SiacTSoggetto_Soggetto);
	}

	public Long countOneriByAllegatoAtto(AllegatoAtto allegatoAtto) {
		return siacTAttoAllegatoRepository.countSiacDOnereByAttoalId(allegatoAtto.getUid());
	}

	public Date findDataUltimoInizioValiditaStato(AllegatoAtto allegatoAtto, StatoOperativoAllegatoAtto statoOperativoAllegatoAtto) {
		final String methodName = "findDataUltimoInizioValiditaStato";
		SiacDAttoAllegatoStatoEnum siacDAttoAllegatoStatoEnum = SiacDAttoAllegatoStatoEnum.byStatoOperativo(statoOperativoAllegatoAtto);
		List<SiacRAttoAllegatoStato> siacRAttoAllegatoStatos = siacTAttoAllegatoRepository.findSiacRAttoAllegatoStatoOrderedByDataCreazione(allegatoAtto.getUid());
		
		Date data = null;
		String ultimoCodice = null;
		
		for(SiacRAttoAllegatoStato raas : siacRAttoAllegatoStatos) {
			String codice = raas.getSiacDAttoAllegatoStato().getAttoalStatoCode();
			log.debug(methodName, "Allegato atto [uid: " + allegatoAtto.getUid() + "], stato [" + codice + "], data creazione [" + raas.getDataCreazione() + "]");
			
			if(!codice.equals(ultimoCodice)) {
				ultimoCodice = codice;
				 if(siacDAttoAllegatoStatoEnum.getCodice().equals(codice)) {
					 data = raas.getDataCreazione();
				 }
			}
		}
		return data;
	}
	
	public StatoOperativoAllegatoAtto getStatoOperativoAllegatoAttoBySubdoumento(Subdocumento<?, ?> subdoc) {
		List<SiacTAttoAllegato> siacTAttoAllegatos = siacTAttoAllegatoRepository.findSiacTAttoAllegatoBySubdocId(subdoc.getUid());
		if(siacTAttoAllegatos == null || siacTAttoAllegatos.isEmpty()) {
			return null;
		}
		SiacTAttoAllegato staa = siacTAttoAllegatos.get(0);
		AllegatoAttoStatoConverter converter = new AllegatoAttoStatoConverter();
		return converter.convertFrom(staa, null);
	}
	
	public StatoOperativoAllegatoAtto getStatoOperativoAllegatoAtto(AllegatoAtto allegatoAtto) {
		SiacTAttoAllegato siacTAttoAllegato = siacTAttoAllegatoRepository.findOne(allegatoAtto.getUid());
		if(siacTAttoAllegato == null) {
			return null;
		}
		AllegatoAttoStatoConverter converter = new AllegatoAttoStatoConverter();
		return converter.convertFrom(siacTAttoAllegato, null);
	}

	/**
	 * Carica dati sospensione per elenco.
	 *
	 * @param allegatoAtto the allegato atto
	 * @return the set[]
	 */
	public Set<?>[] caricaDatiSospensionePerAllegatoAtto(AllegatoAtto allegatoAtto) {
		List<Object[]> datiSospensioneByAllegatoAtto = siacRAttoAllegatoSogRepository.findDatiSospensioneByAllegatoAtto(allegatoAtto.getUid());
		
		// Inizializzo i set da passare al servizio 
		Set<Date> dateSospensione = new HashSet<Date>();
		
		Set<Date> dateRiattivazione= new HashSet<Date>();
		
		Set<String> causaliSospensioneAllegato= new HashSet<String>();
		
		// Ciclo sugli oggetti
		for (Object[] o : datiSospensioneByAllegatoAtto) {
			// Aggiungo i dati nei vari set
			causaliSospensioneAllegato.add((String) o[0]);
			dateRiattivazione.add((Date) o[1]);
			dateSospensione.add((Date) o[2]);
		}
		
		return new Set<?>[] {dateSospensione, dateRiattivazione, causaliSospensioneAllegato};
		
	}
	
	/**
	 * Carica dati sospensione per elenco.
	 *
	 * @param elencoDocumentiAllegato the allegato atto
	 * @return the set[]
	 */
	public Set<?>[] caricaDatiSospensionePerElenco(ElencoDocumentiAllegato elencoDocumentiAllegato) {
		List<Object[]> datiSospensioneByAllegatoAtto = siacRAttoAllegatoSogRepository.findDatiSospensioneAllegatoByElenco(elencoDocumentiAllegato.getUid());
		
		// Inizializzo i set da passare al servizio 
		Set<Date> dateSospensione = new HashSet<Date>();
		
		Set<Date> dateRiattivazione= new HashSet<Date>();
		
		Set<String> causaliSospensioneAllegato= new HashSet<String>();
		
		// Ciclo sugli oggetti
		for (Object[] o : datiSospensioneByAllegatoAtto) {
			// Aggiungo i dati nei vari set
			causaliSospensioneAllegato.add((String) o[0]);
			dateRiattivazione.add((Date) o[1]);
			dateSospensione.add((Date) o[2]);
		}
		
		return new Set<?>[] {dateSospensione, dateRiattivazione, causaliSospensioneAllegato};
		
	}
	
	/**
	 * Count impegni collegati con flagsoggetto durc.
	 *
	 * @param allegatoAtto the allegato atto
	 * @return the long
	 */
	public List<Integer> getUidsSubdocWithImpegnoConfermaDurc(AllegatoAtto allegatoAtto) {
		return siacTAttoAllegatoRepository.getUidsSubdocWithImpegniWithBooleanAttrCodeAndValueByAttoalId(allegatoAtto.getUid(), SiacTAttrEnum.FlagSoggettoDurc.getCodice(), "S");
	}
	
	//task-30
	public List<Integer> getUidsSubdocWithImpegnoConfermaDurc(AllegatoAtto allegatoAtto, List<Integer> idElenchiDocumentiAllegato) {
		return siacTAttoAllegatoRepository.getUidsSubdocWithImpegniWithBooleanAttrCodeAndValueByAttoalIdAndElenchiDocId(allegatoAtto.getUid(),idElenchiDocumentiAllegato, SiacTAttrEnum.FlagSoggettoDurc.getCodice(), "S");
	}

	
	
	public List<Messaggio>  controlloImportiImpegniVincolati(List<Integer> listaAllAttoId) {
		String listaId = "";
		String separatore = "";
		List<Messaggio> messaggi = new ArrayList<Messaggio>();
		
		for (Integer id : listaAllAttoId) {
			listaId = listaId + separatore + id ; 
			separatore = ",";
		}
		List<Object[]> lista = controlloImportiImpegniVincolatiDao.controlloImportiImpegniVincolati(listaId);
		if(lista == null || lista.isEmpty()) {
			return messaggi;
		}
		
		Map<String, List<String>> mappaAccertamentiSubdocumenti = new HashMap<String, List<String>>();
		
		for (Object[] risultati : lista) {
			Integer annoAccertamento = (Integer) risultati[6];
			BigDecimal numeroAccertamento = (BigDecimal) risultati[7];
			
			Integer docAnno 		= (Integer) risultati[0];
			String  docNumero 		= (String)  risultati[1];
			Integer subdocNumero 	= (Integer) risultati[2];
			Integer eldocAnno 		= (Integer) risultati[3];
			Integer elDocNumero 			= (Integer) risultati[4];
			
			popolaMappaAccertamentiSubdocumenti(mappaAccertamentiSubdocumenti, annoAccertamento,numeroAccertamento, docAnno, docNumero, subdocNumero, eldocAnno, elDocNumero);

//			BigDecimal importoRiscosso = (BigDecimal) risultati[8];	
//			String importoRiscossoString = importoRiscosso!= null? importoRiscosso.toPlainString() : "null";
		
		}
		for (String key : mappaAccertamentiSubdocumenti.keySet()) {
			StringBuilder messaggio = new StringBuilder();
			messaggio.append("Accertamento ")
			.append(key)
			.append(" : ");
			List<String> subdocs = mappaAccertamentiSubdocumenti.get(key);
			if(subdocs != null && !subdocs.isEmpty()) {
				messaggio.append(StringUtils.join(subdocs, ", "));
			}
			Messaggio messaggo  = new Messaggio(ErroreFin.WARNING_IMPORTO_VINCOLATO.getErrore().getCodice(),"Alcuni subdocumenti superano l'importo riscosso. " + messaggio);
			messaggi.add(messaggo);
		}
		
		
		return messaggi;
		
	}

	private void popolaMappaAccertamentiSubdocumenti(Map<String, List<String>> mappaAccertamentiSubdocumenti,	Integer annoAccertamento, BigDecimal numeroAccertamento, Integer docAnno, String docNumero,
			Integer subdocNumero, Integer eldocAnno, Integer eldocNumero) {
		final String nullString = "null"; 
		String annoAccertamentoString = annoAccertamento != null? annoAccertamento.toString() : nullString;
		String numeroAccertamentoString = numeroAccertamento != null? numeroAccertamento.toPlainString() : nullString;
		String key = annoAccertamentoString + "/" + numeroAccertamentoString;
		List<String> listaSubdocumentiCollegati = mappaAccertamentiSubdocumenti.get(key);
		if(listaSubdocumentiCollegati == null) {
			listaSubdocumentiCollegati = new ArrayList<String>();
		}
		String chiaveSubdoc = new StringBuilder().append("elenco ")
			.append(eldocAnno != null? eldocAnno.toString() : nullString)
			.append("/")
			.append(eldocNumero != null? eldocNumero.toString() : nullString)
			.append(", quota: ")
			.append(docAnno!= null? docAnno.toString() : nullString)
			.append("/")
			.append(docNumero!= null? docNumero.toString() : nullString)
			.append(" - ")
			.append(subdocNumero != null? subdocNumero : nullString).toString();
		listaSubdocumentiCollegati.add(chiaveSubdoc);
		mappaAccertamentiSubdocumenti.put(key, listaSubdocumentiCollegati);
	}

	public Checklist leggiChecklist() {
		Checklist allegatoAttoChecklist = new Checklist();
		
		siacDAttoAllegatoChecklistAllegatoAttoChecklistMapper.map(
				siacDAttoAllegatoCheclistRepository.findAllAttoAllegatoChecklist(ente.getUid()), 
				allegatoAttoChecklist
		);
		
		return allegatoAttoChecklist;
	}
	

}
