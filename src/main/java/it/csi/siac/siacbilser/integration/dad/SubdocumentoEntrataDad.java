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
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.model.StatoOperativoAtti;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.dao.SiacRSubdocMovgestTRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTSubdocNumRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTSubdocRepository;
import it.csi.siac.siacbilser.integration.dao.SubdocumentoDao;
import it.csi.siac.siacbilser.integration.entity.SiacDSubdocTipo;
import it.csi.siac.siacbilser.integration.entity.SiacRSubdocMovgestT;
import it.csi.siac.siacbilser.integration.entity.SiacTDoc;
import it.csi.siac.siacbilser.integration.entity.SiacTEnteProprietario;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgestT;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdocIva;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdocNum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDAttoAmmStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocFamTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDProvCassaTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDRelazTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDSubdocTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.BooleanToStringManualeAutomaticoConverter;
import it.csi.siac.siacbilser.integration.entitymapping.converter.base.Converters;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.integration.entity.SiacTBase;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrataModelDetail;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * Data access delegate di un SubdocumentoEntrata .
 *
 * @author Domenico
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class SubdocumentoEntrataDad extends ExtendedBaseDadImpl {
	
	/** The subdocumento dao. */
	@Autowired
	private SubdocumentoDao subdocumentoDao;	
	
	/** The siac t subdoc repository. */
	@Autowired
	private SiacTSubdocRepository siacTSubdocRepository;
	
	/** The siac t subdoc num repository. */
	@Autowired
	private SiacTSubdocNumRepository siacTSubdocNumRepository;	
	
	/** The eef. */
	@Autowired
	private EnumEntityFactory eef;

	//SIAC-5937
	@Autowired
	private SiacRSubdocMovgestTRepository siacRSubdocMovgestTRepository;
	
	/**
	 * Ottiene un Subdocumento (o Quota) a partire dal suo uid.
	 *
	 * @param uid del subdocumento
	 * @return the subdocumento entrata
	 */
	public SubdocumentoEntrata findSubdocumentoEntrataById(Integer uid) {
		final String methodName = "findSubdocumentoEntrataById";		
		log.debug(methodName, "uid: "+ uid);
		SiacTSubdoc siacTSubdoc = subdocumentoDao.findById(uid);
		if(siacTSubdoc == null) {
			log.debug(methodName, "Impossibile trovare il SubdocumentoEntrata con id: " + uid);
		}
		return  mapNotNull(siacTSubdoc, SubdocumentoEntrata.class, BilMapId.SiacTSubdoc_SubdocumentoEntrata);
	}
	
	/**
	 * Ottiene un Subdocumento (o Quota) a partire dal suo uid. Permette di specificare le parti di modello desiderate.
	 *
	 * @param uid del subdocumento
	 * @return the subdocumento entrata
	 */
	public SubdocumentoEntrata findSubdocumentoEntrataById(Integer uid, SubdocumentoEntrataModelDetail... modelDetails) {
		final String methodName = "findSubdocumentoEntrataById[modelDetails]";		
		log.debug(methodName, "uid: "+ uid);
		SiacTSubdoc siacTSubdoc = subdocumentoDao.findById(uid);
		if(siacTSubdoc == null) {
			log.debug(methodName, "Impossibile trovare il SubdocumentoEntrata con id: " + uid);
		}else{
			log.debug(methodName, "siacTSubdoc trovata con uid: " + siacTSubdoc.getUid());
		}
		return  mapNotNull(siacTSubdoc, SubdocumentoEntrata.class, BilMapId.SiacTSubdoc_SubdocumentoEntrata_Base, Converters.byModelDetails(modelDetails));
	}
	
	/**
	 * Ottiene un Subdocumento (o Quota) a partire dal suo uid con i dati di base
	 *
	 * @param uid del subdocumento
	 * @return the subdocumento entrata
	 */
	public SubdocumentoEntrata findSubdocumentoEntrataBaseById(Integer uid) {
		final String methodName = "findSubdocumentoEntrataById";		
		log.debug(methodName, "uid: "+ uid);
		SiacTSubdoc siacTDoc = subdocumentoDao.findById(uid);
		if(siacTDoc == null) {
			log.debug(methodName, "Impossibile trovare il SubdocumentoEntrata con id: " + uid);
		}
		return  mapNotNull(siacTDoc, SubdocumentoEntrata.class, BilMapId.SiacTSubdoc_SubdocumentoEntrata_Base);
	}
	
	/**
	 * Conta i subdocumenti per id
	 *
	 * @param uid del subdocumento
	 * @return the subdocumento spesa
	 */
	public Long countSubdocumentiEntrataByIds(Collection<Integer> uids) {
		final String methodName = "countSubdocumentiEntrataByIds";
		log.debug(methodName, "uids: "+ uids);
		Collection<String> docFamTipoCodes = Arrays.asList(SiacDDocFamTipoEnum.Entrata.getCodice(), SiacDDocFamTipoEnum.IvaEntrata.getCodice());
		return siacTSubdocRepository.countBySubdocId(uids, docFamTipoCodes);
	}
	
	/**
	 * Ottiene la lista dei subdocumenti di un documento di Entrata.
	 *
	 * @param idDocumento the id documento
	 * @return lista dei subdocumenti
	 */
	public List<SubdocumentoEntrata> findSubdocumentiEntrataByIdDocumento(Integer idDocumento) {
		List<SiacTSubdoc> siacTSubdocs = siacTSubdocRepository.findSiacTSubdocByDocId(idDocumento);		
		return convertiLista(siacTSubdocs, SubdocumentoEntrata.class, BilMapId.SiacTSubdoc_SubdocumentoEntrata);
	
	}
	
	/**
	 * Ottiene la lista dei subdocumenti di un documento di Entrata.
	 *
	 * @param idDocumento the id documento
	 * @param parametriPaginazione i parametri di paginazione
	 * @return lista dei subdocumenti
	 */
	public ListaPaginata<SubdocumentoEntrata> findSubdocumentiEntrataByIdDocumento(Integer idDocumento, Boolean rilevanteIva, ParametriPaginazione parametriPaginazione, SubdocumentoEntrataModelDetail... modelDetails) {
		Order order = new Sort.Order(Direction.ASC, "subdocNumero");
		Sort sort = new Sort(order);
		
		Pageable pageable = toPageable(parametriPaginazione, sort);
		Page<SiacTSubdoc> siacTSubdocs = subdocumentoDao.ricercaSinteticaSubdocumentiByDocId(idDocumento, rilevanteIva, pageable);
		
		return toListaPaginata(siacTSubdocs, SubdocumentoEntrata.class, BilMapId.SiacTSubdoc_SubdocumentoEntrata_Base, modelDetails);
	}
	
	/**
	 * Ottiene il totale degli importi dei subdocumenti di un documento di spesa.
	 *
	 * @param idDocumento the id documento
	 * @return il totale degli importi
	 */
	public BigDecimal findTotaleImportoSubdocumentiEntrataByIdDocumento(Integer idDocumento) {
		return siacTSubdocRepository.sumSubdocImportoByDocId(idDocumento);
	
	}
	
	/**
	 * Ottiene la lista dei subdocumenti di un documento di entrata.
	 *
	 * @param idDocumento the id documento
	 * @return lista dei subdocumenti
	 */
	public List<SubdocumentoEntrata> findSubdocumentiEntrataByIdDocumento(Integer idDocumento, SubdocumentoEntrataModelDetail... subdocumentoEntrataModelDetail) {
		List<SiacTSubdoc> siacTSubdocs = siacTSubdocRepository.findSiacTSubdocByDocId(idDocumento);		
		
		return convertiLista(siacTSubdocs, SubdocumentoEntrata.class, BilMapId.SiacTSubdoc_SubdocumentoEntrata_Base, Converters.byModelDetails(subdocumentoEntrataModelDetail));
		
	}
	
	/**
	 * Ottiene la lista dei subdocumenti di un documento di entrata.
	 *
	 * @param idDocumento the id documento
	 * @return lista dei subdocumenti
	 */
	public List<SubdocumentoEntrata> findSubdocumentiEntrataModelDetailByIdDocumento(Integer idDocumento, SubdocumentoEntrataModelDetail... subdocumentoEntrataModelDetail) {
		List<SiacTSubdoc> siacTSubdocs = siacTSubdocRepository.findSiacTSubdocByDocId(idDocumento);
		return convertiLista(siacTSubdocs, SubdocumentoEntrata.class, BilMapId.SiacTSubdoc_SubdocumentoEntrata_ModelDetail, Converters.byModelDetails(subdocumentoEntrataModelDetail));
	
	}
	
	
	/**
	 * Ottiene il Documento a cui e' associato il subdocumento con l'id passato.
	 *
	 * @param idSubDocumento the id sub documento
	 * @return lista dei subdocumenti
	 */
	public DocumentoEntrata findDocumentoByIdSubdocumentoEntrata(Integer idSubDocumento) {
		SiacTSubdoc siacTSubdoc = siacTSubdocRepository.findOne(idSubDocumento);
		SiacTDoc siacTDoc = siacTSubdoc.getSiacTDoc();
		return mapNotNull(siacTDoc, DocumentoEntrata.class, BilMapId.SiacTDoc_DocumentoEntrata_Base);	
	}
	
	/**
	 * Inserisci anagrafica subdocumento entrata.
	 *
	 * @param documento the documento
	 */
	public void inserisciAnagraficaSubdocumentoEntrata(SubdocumentoEntrata documento) {		
		SiacTSubdoc siacTSubdoc = buildSiacTSubdoc(documento);	
		siacTSubdoc.setLoginCreazione(loginOperazione);
		siacTSubdoc.setLoginModifica(loginOperazione);
		subdocumentoDao.create(siacTSubdoc);
		documento.setUid(siacTSubdoc.getUid());
	}	
	
	

	/**
	 * Aggiorna anagrafica subdocumento entrata.
	 *
	 * @param documento the documento
	 */
	public void aggiornaAnagraficaSubdocumentoEntrata(SubdocumentoEntrata documento) {
		SiacTSubdoc siacTSubdoc = buildSiacTSubdoc(documento);	
		siacTSubdoc.setLoginModifica(loginOperazione);
		subdocumentoDao.update(siacTSubdoc);
		documento.setUid(siacTSubdoc.getUid());
		// SIAC-6484
		subdocumentoDao.flushAndClear();
	}	
	
	

	/**
	 * Aggiorna solo gli importi del subdocumento di entrata passato come parametro.
	 *
	 * @param subdoc the subdoc
	 */
	public void aggiornaImportoDaDedurreSubdocumentoEntrata(SubdocumentoEntrata subdoc) {
		final String methodName = "aggiornaImportiSubdocumentoEntrata";
		SiacTSubdoc siacTSubdoc = subdocumentoDao.findById(subdoc.getUid());
		if(siacTSubdoc==null){
			throw new IllegalArgumentException("Subdocumento con id "+subdoc.getUid() + " non presente in archivio.");
		}
		
		log.debug(methodName, "Aggiornamento quota con id: " + subdoc.getUid() + " importo: "+subdoc.getImporto() + " importoDaDedurre: "+ subdoc.getImportoDaDedurre());
		//Aggiornamento degli importi
		siacTSubdoc.setSubdocImportoDaDedurre(subdoc.getImportoDaDedurre());
		
		//Aggiornamento delle informazioni di login
		Date now = new Date();
		siacTSubdoc.setDataModifica(now);
		siacTSubdoc.setDataModificaAggiornamento(now);
		siacTSubdoc.setLoginModifica(loginOperazione);
		siacTSubdoc.setLoginOperazione(loginOperazione);
		
		subdocumentoDao.flush();
		
	}
	
	/**
	 * Aggiorna solo gli importi del subdocumento di entrata passato come parametro.
	 *
	 * @param subdoc the subdoc
	 */
	public void aggiornaImportoSubdocumentoEntrata(SubdocumentoEntrata subdoc) {
		final String methodName = "aggiornaImportiSubdocumentoEntrata";
		SiacTSubdoc siacTSubdoc = subdocumentoDao.findById(subdoc.getUid());
		if(siacTSubdoc==null){
			throw new IllegalArgumentException("Subdocumento con id "+subdoc.getUid() + " non presente in archivio.");
		}
		
		log.debug(methodName, "Aggiornamento quota con id: " + subdoc.getUid() + " importo: "+subdoc.getImporto() + " importoDaDedurre: "+ subdoc.getImportoDaDedurre());
		//Aggiornamento degli importi
		siacTSubdoc.setSubdocImporto(subdoc.getImporto());
		
		//Aggiornamento delle informazioni di login
		Date now = new Date();
		siacTSubdoc.setDataModifica(now);
		siacTSubdoc.setDataModificaAggiornamento(now);
		siacTSubdoc.setLoginModifica(loginOperazione);
		siacTSubdoc.setLoginOperazione(loginOperazione);
		
		subdocumentoDao.flush();
		
	}

	
	
	/**
	 * Builds the siac t subdoc.
	 *
	 * @param documento the documento
	 * @return the siac t subdoc
	 */
	private SiacTSubdoc buildSiacTSubdoc(SubdocumentoEntrata documento) {
		SiacTSubdoc siacTDoc = new SiacTSubdoc();
		siacTDoc.setLoginOperazione(loginOperazione);
		documento.setLoginOperazione(loginOperazione);
		map(documento, siacTDoc, BilMapId.SiacTSubdoc_SubdocumentoEntrata);		
		siacTDoc.setSiacDSubdocTipo(eef.getEntity(SiacDSubdocTipoEnum.Entrata, documento.getEnte().getUid(), SiacDSubdocTipo.class));
		return siacTDoc;
	}
	
	/**
	 * Elimina subdocumento entrata.
	 *
	 * @param subdocumento the subdocumento
	 */
	public void eliminaSubdocumentoEntrata(SubdocumentoEntrata subdocumento){
		SiacTSubdoc siacTSubdoc = new SiacTSubdoc();
		siacTSubdoc.setUid(subdocumento.getUid());
		siacTSubdoc.setLoginCancellazione(loginOperazione);
		subdocumentoDao.delete(siacTSubdoc);
		subdocumentoDao.flush();
	}
	
	
	/**
	 * Ottiene il numero di una nuova quota o subdocumento.
	 *
	 * @param uidDocumento the uid documento
	 * @return numero subdocumento
	 */
	@Transactional(propagation=Propagation.MANDATORY)
	public Integer staccaNumeroSubdocumento(Integer uidDocumento) {
		final String methodName = "staccaNumeroSubdocumento";
		
		SiacTSubdocNum siacTDocNum = siacTSubdocNumRepository.findByDocId(uidDocumento);
		
		Date now = new Date();		
		if(siacTDocNum == null) {			
			siacTDocNum = new SiacTSubdocNum();
			SiacTDoc siacTDoc = new SiacTDoc();
			siacTDoc.setDocId(uidDocumento);			
			siacTDocNum.setSiacTDoc(siacTDoc);
			SiacTEnteProprietario siacTEnteProprietario = new SiacTEnteProprietario();
			siacTEnteProprietario.setUid(ente.getUid());
			siacTDocNum.setSiacTEnteProprietario(siacTEnteProprietario);
			siacTDocNum.setDataCreazione(now);
			siacTDocNum.setDataInizioValidita(now);
			siacTDocNum.setLoginOperazione(loginOperazione);
			siacTDocNum.setSubdocNumero(1); //La numerazione parte da 1		
		}
		
		siacTDocNum.setDataModifica(now);	
		
		siacTSubdocNumRepository.saveAndFlush(siacTDocNum);
		
		Integer numeroSubdocumento = siacTDocNum.getSubdocNumero();
		log.info(methodName, "returning numeroSubdocumento: "+ numeroSubdocumento);
		return numeroSubdocumento;
	}
	
	/**
	 * Aggiorna importo da dedurre.
	 *
	 * @param subdocumento the subdocumento
	 */
	public void aggiornaImportoDaDedurre(SubdocumentoEntrata subdocumento) {
		SiacTSubdoc siacTSubdoc = subdocumentoDao.findById(subdocumento.getUid());		
		siacTSubdoc.setSubdocImportoDaDedurre(subdocumento.getImportoDaDedurre());
		
		subdocumentoDao.flush();
	}
	
	/**
	 * Aggiorna numero registrazione iva.
	 *
	 * @param subdocumento the subdocumento
	 */
	public void aggiornaNumeroRegistrazioneIva(SubdocumentoEntrata subdocumento) {
		SiacTSubdoc siacTSubdoc = subdocumentoDao.findById(subdocumento.getUid());		
		siacTSubdoc.setSubdocNregIva(subdocumento.getNumeroRegistrazioneIVA());
		
		subdocumentoDao.flush();
	}
	
	
	public ListaPaginata<SubdocumentoEntrata> ricercaSubdocumentiEntrata(Ente ente,
		Bilancio bilancio,
		Integer uidElenco,
		Integer annoElenco,
		Integer numeroElenco,
		Integer numeroElencoDa,
		Integer numeroElencoA,
		Integer annoProvvisorio,
		Integer numeroProvvisorio,
		Date dataProvvisorio,
		TipoDocumento tipoDocumento,
		Integer annoDocumento,
		String numeroDocumento,
		Date dataEmissioneDocumento,
		Integer numeroQuota,	
		BigDecimal numeroMovimento,
		Integer annoMovimento,
		Soggetto soggetto,
		Integer uidProvvedimento,
		Integer annoProvvedimento,
		Integer numeroProvvedimento,
		TipoAtto tipoAtto,
		StrutturaAmministrativoContabile struttAmmContabile,
		Integer annoCapitolo,
		Integer numeroCapitolo,
		Integer numeroArticolo,
		Integer numeroUEB,
		List<StatoOperativoDocumento> statiOperativoDocumento,
		Boolean collegatoAMovimentoDelloStessoBilancio, 
		Boolean associatoAProvvedimentoOAdElenco, 
		Boolean importoDaIncassareZero, 
		Boolean importoDaPagareOIncassareMaggioreDiZero,
		Boolean rilevatiIvaConRegistrazioneONonRilevantiIva,
		Boolean collegatoALiquidazioneInStatoValidoConProvvDefinitivoConDispPagareMaggioreDiSubdocImporto,
		Collection<StatoOperativoAtti> statiOperativiAtti,
		Boolean associatoAdOrdinativo,
		Boolean conFlagConvalidaManuale,
		List<ProvvisorioDiCassa> listProvvisorioDiCassa,
		ParametriPaginazione parametriPaginazione
		) {

		List<Integer> listProvvisorioDiCassaUid =  estraiIdProvCassa( listProvvisorioDiCassa);
		
		Set<SiacDAttoAmmStatoEnum> siacDAttoAmmStatoEnums = SiacDAttoAmmStatoEnum.byStatiOperativi(statiOperativiAtti);
		Page<SiacTSubdoc> lista = subdocumentoDao.ricercaSinteticaSubdocumenti(ente.getUid(),
				bilancio != null ? bilancio.getUid() : null,
				EnumSet.of(SiacDDocFamTipoEnum.Entrata, SiacDDocFamTipoEnum.IvaEntrata),
				uidElenco,
				annoElenco,
				numeroElenco,
				numeroElencoDa,
				numeroElencoA,
				annoProvvisorio,
				numeroProvvisorio != null ? new BigDecimal(numeroProvvisorio.toString()) : null,
				dataProvvisorio,
				SiacDProvCassaTipoEnum.Entrata,
				tipoDocumento!=null?tipoDocumento.getUid():null,
				annoDocumento,
				numeroDocumento,
				dataEmissioneDocumento,
				numeroQuota,	
				numeroMovimento,
				annoMovimento,
				soggetto!=null?soggetto.getCodiceSoggetto():null,
				uidProvvedimento,
				(annoProvvedimento != null && annoProvvedimento != 0) ? annoProvvedimento.toString() : null, //annoProvvedimento != null ? annoProvvedimento.toString() :null,
			    numeroProvvedimento,
			    tipoAtto!=null?tipoAtto.getUid():null,
			    struttAmmContabile!=null?struttAmmContabile.getUid():null,
			    annoCapitolo,
			    numeroCapitolo,
			    numeroArticolo,
			    numeroUEB,
			    SiacDDocStatoEnum.byStatiOperativi(statiOperativoDocumento),
	    		collegatoAMovimentoDelloStessoBilancio, 
				associatoAProvvedimentoOAdElenco, 
				importoDaIncassareZero, 
				importoDaPagareOIncassareMaggioreDiZero,
				rilevatiIvaConRegistrazioneONonRilevantiIva,
				collegatoALiquidazioneInStatoValidoConProvvDefinitivoConDispPagareMaggioreDiSubdocImporto,
				siacDAttoAmmStatoEnums,
				associatoAdOrdinativo,
				conFlagConvalidaManuale,
				listProvvisorioDiCassaUid,
			    toPageable(parametriPaginazione)		
				);

		return toListaPaginata(lista, SubdocumentoEntrata.class, BilMapId.SiacTSubdoc_SubdocumentoEntrata);

	}
	
	public ListaPaginata<SubdocumentoEntrata> ricercaSubdocumentiEntrataModelDetail(
			Ente ente,
			Bilancio bilancio,
			Integer uidElenco,
			Integer annoElenco,
			Integer numeroElenco,
			Integer numeroElencoDa,
			Integer numeroElencoA,
			Integer annoProvvisorio,
			Integer numeroProvvisorio,
			Date dataProvvisorio,
			TipoDocumento tipoDocumento,
			Integer annoDocumento,
			String numeroDocumento,
			Date dataEmissioneDocumento,
			Integer numeroQuota,	
			BigDecimal numeroMovimento,
			Integer annoMovimento,
			Soggetto soggetto,
			Integer uidProvvedimento,
			Integer annoProvvedimento,
			Integer numeroProvvedimento,
			TipoAtto tipoAtto,
			StrutturaAmministrativoContabile struttAmmContabile,
			Integer annoCapitolo,
			Integer numeroCapitolo,
			Integer numeroArticolo,
			Integer numeroUEB,
			List<StatoOperativoDocumento> statiOperativoDocumento,
			Boolean collegatoAMovimentoDelloStessoBilancio, 
			Boolean associatoAProvvedimentoOAdElenco, 
			Boolean importoDaIncassareZero, 
			Boolean importoDaPagareOIncassareMaggioreDiZero,
			Boolean rilevatiIvaConRegistrazioneONonRilevantiIva,
			Boolean collegatoALiquidazioneInStatoValidoConProvvDefinitivoConDispPagareMaggioreDiSubdocImporto,
			Collection<StatoOperativoAtti> statiOperativiAtti,
			Boolean associatoAdOrdinativo,
			Boolean conFlagConvalidaManuale,
			List<ProvvisorioDiCassa> listProvvisorioDiCassa,
			ParametriPaginazione parametriPaginazione,
			SubdocumentoEntrataModelDetail... modelDetails) {

		List<Integer> listProvvisorioDiCassaUid =  estraiIdProvCassa( listProvvisorioDiCassa);
		
		Set<SiacDAttoAmmStatoEnum> siacDAttoAmmStatoEnums = SiacDAttoAmmStatoEnum.byStatiOperativi(statiOperativiAtti);
		Page<SiacTSubdoc> lista = subdocumentoDao.ricercaSinteticaSubdocumenti(
				ente.getUid(),
				mapToUidIfNotZero(bilancio),
				EnumSet.of(SiacDDocFamTipoEnum.Entrata, SiacDDocFamTipoEnum.IvaEntrata),
				uidElenco,
				annoElenco,
				numeroElenco,
				numeroElencoDa,
				numeroElencoA,
				annoProvvisorio,
				numeroProvvisorio != null ? new BigDecimal(numeroProvvisorio.toString()) : null,
				dataProvvisorio,
				SiacDProvCassaTipoEnum.Entrata,
				mapToUidIfNotZero(tipoDocumento),
				annoDocumento,
				numeroDocumento,
				dataEmissioneDocumento,
				numeroQuota,	
				numeroMovimento,
				annoMovimento,
				soggetto != null ? soggetto.getCodiceSoggetto() : null,
				uidProvvedimento,
				annoProvvedimento != null && annoProvvedimento != 0 ? annoProvvedimento.toString() : null,
				numeroProvvedimento,
				mapToUidIfNotZero(tipoAtto),
				mapToUidIfNotZero(struttAmmContabile),
				annoCapitolo,
				numeroCapitolo,
				numeroArticolo,
				numeroUEB,
				SiacDDocStatoEnum.byStatiOperativi(statiOperativoDocumento),
				collegatoAMovimentoDelloStessoBilancio,
				associatoAProvvedimentoOAdElenco,
				importoDaIncassareZero,
				importoDaPagareOIncassareMaggioreDiZero,
				rilevatiIvaConRegistrazioneONonRilevantiIva,
				collegatoALiquidazioneInStatoValidoConProvvDefinitivoConDispPagareMaggioreDiSubdocImporto,
				siacDAttoAmmStatoEnums,
				associatoAdOrdinativo,
				conFlagConvalidaManuale,
				listProvvisorioDiCassaUid,
				toPageable(parametriPaginazione));

		return toListaPaginata(lista, SubdocumentoEntrata.class, BilMapId.SiacTSubdoc_SubdocumentoEntrata, modelDetails);

	}
	
	
	
	public BigDecimal ricercaSubdocumentiEntrataTotaleImporti(Ente ente, 
			Bilancio bilancio,
			Integer uidElenco,
			Integer annoElenco,
			Integer numeroElenco,
			Integer numeroElencoDa,
			Integer numeroElencoA,
			Integer annoProvvisorio,
			Integer numeroProvvisorio,
			Date dataProvvisorio,
			TipoDocumento tipoDocumento,
			Integer annoDocumento,
			String numeroDocumento,
			Date dataEmissioneDocumento,
			Integer numeroQuota,	
			BigDecimal numeroMovimento,
			Integer annoMovimento,
			Soggetto soggetto,
			Integer uidProvvedimento,
			Integer annoProvvedimento,
			Integer numeroProvvedimento,
			TipoAtto tipoAtto,
			StrutturaAmministrativoContabile struttAmmContabile,
			Integer annoCapitolo,
			Integer numeroCapitolo,
			Integer numeroArticolo,
			Integer numeroUEB,
			List<StatoOperativoDocumento> statiOperativoDocumento,
			Boolean collegatoAMovimentoDelloStessoBilancio, 
			Boolean associatoAProvvedimentoOAdElenco, 
			Boolean importoDaIncassareZero, 
			Boolean importoDaPagareOIncassareMaggioreDiZero,
			Boolean rilevatiIvaConRegistrazioneONonRilevantiIva,
			Boolean collegatoALiquidazioneInStatoValidoConProvvDefinitivoConDispPagareMaggioreDiSubdocImporto,
			Collection<StatoOperativoAtti> statiOperativiAtti,
			Boolean associatoAdOrdinativo,
			Boolean conFlagConvalidaManuale,
			List<ProvvisorioDiCassa> listProvvisorioDiCassa
			
			) {
		
			Set<SiacDAttoAmmStatoEnum> siacDAttoAmmStatoEnums = SiacDAttoAmmStatoEnum.byStatiOperativi(statiOperativiAtti);
			List<Integer> listProvvisorioDiCassaUid =  estraiIdProvCassa( listProvvisorioDiCassa);
		
			BigDecimal totale = subdocumentoDao.ricercaSinteticaSubdocumentiTotaleImporti(ente.getUid(),
					bilancio != null ? bilancio.getUid(): null,
					EnumSet.of(SiacDDocFamTipoEnum.Entrata, SiacDDocFamTipoEnum.IvaEntrata),
					uidElenco,
					annoElenco,
					numeroElenco,
					numeroElencoDa,
					numeroElencoA,
					annoProvvisorio,
					numeroProvvisorio != null ? new BigDecimal(numeroProvvisorio.toString()) : null,
					dataProvvisorio,
					SiacDProvCassaTipoEnum.Entrata,
					tipoDocumento!=null?tipoDocumento.getUid():null,
					annoDocumento,
					numeroDocumento,
					dataEmissioneDocumento,
					numeroQuota,	
					numeroMovimento,
					annoMovimento,
					soggetto!=null?soggetto.getCodiceSoggetto():null,
					uidProvvedimento,
					(annoProvvedimento != null && annoProvvedimento != 0) ? annoProvvedimento.toString() : null, //annoProvvedimento != null ? annoProvvedimento.toString() :null,
				    numeroProvvedimento,
				    tipoAtto!=null?tipoAtto.getUid():null,
				    struttAmmContabile!=null?struttAmmContabile.getUid():null,
				    annoCapitolo,
					numeroCapitolo,
					numeroArticolo,
					numeroUEB,
				    SiacDDocStatoEnum.byStatiOperativi(statiOperativoDocumento),
		    		collegatoAMovimentoDelloStessoBilancio, 
					associatoAProvvedimentoOAdElenco, 
					importoDaIncassareZero, 
					importoDaPagareOIncassareMaggioreDiZero,
					rilevatiIvaConRegistrazioneONonRilevantiIva,
					collegatoALiquidazioneInStatoValidoConProvvDefinitivoConDispPagareMaggioreDiSubdocImporto,
					siacDAttoAmmStatoEnums,
					associatoAdOrdinativo,
					conFlagConvalidaManuale,
					listProvvisorioDiCassaUid
					);

			return totale;

		}
	
	public Long countSubdocumentiEntrata(
			Ente ente,
			Bilancio bilancio,
			Collection<Integer> uidElencos,
			Integer annoElenco,
			Integer numeroElenco,
			Integer numeroElencoDa,
			Integer numeroElencoA,
			Integer annoProvvisorio,
			Integer numeroProvvisorio,
			Date dataProvvisorio,
			TipoDocumento tipoDocumento,
			Integer annoDocumento,
			String numeroDocumento,
			Date dataEmissioneDocumento,
			Integer numeroQuota,	
			BigDecimal numeroMovimento,
			Integer annoMovimento,
			Soggetto soggetto,
			Integer uidProvvedimento,
			Integer annoProvvedimento,
			Integer numeroProvvedimento,
			TipoAtto tipoAtto,
			StrutturaAmministrativoContabile struttAmmContabile,
			Integer annoCapitolo,
			Integer numeroCapitolo,
			Integer numeroArticolo,
			Integer numeroUEB,
			Collection<StatoOperativoDocumento> statiOperativoDocumento,
			Boolean collegatoAMovimentoDelloStessoBilancio, 
			Boolean associatoAProvvedimentoOAdElenco, 
			Boolean importoDaIncassareZero, 
			Boolean importoDaPagareOIncassareMaggioreDiZero,
			Boolean rilevatiIvaConRegistrazioneONonRilevantiIva,
			Boolean collegatoALiquidazioneInStatoValidoConProvvDefinitivoConDispPagareMaggioreDiSubdocImporto,
			Collection<StatoOperativoAtti> statiOperativiAtti,
			Boolean associatoAdOrdinativo,
			Boolean conFlagConvalidaManuale,
			List<ProvvisorioDiCassa> listProvvisorioDiCassa
			) {

		Set<SiacDAttoAmmStatoEnum> siacDAttoAmmStatoEnums = SiacDAttoAmmStatoEnum.byStatiOperativi(statiOperativiAtti);
		List<Integer> listProvvisorioDiCassaUid =  estraiIdProvCassa( listProvvisorioDiCassa);
		
		return subdocumentoDao.countSinteticaSubdocumenti(
				mapToUidIfNotZero(ente),
				mapToUidIfNotZero(bilancio),
				EnumSet.of(SiacDDocFamTipoEnum.Entrata, SiacDDocFamTipoEnum.IvaEntrata),
				uidElencos,
				annoElenco,
				numeroElenco,
				numeroElencoDa,
				numeroElencoA,
				annoProvvisorio,
				numeroProvvisorio != null ? new BigDecimal(numeroProvvisorio.toString()) : null,
				dataProvvisorio,
				SiacDProvCassaTipoEnum.Entrata,
				mapToUidIfNotZero(tipoDocumento),
				annoDocumento,
				numeroDocumento,
				dataEmissioneDocumento,
				numeroQuota,	
				numeroMovimento,
				annoMovimento,
				soggetto!=null?soggetto.getCodiceSoggetto():null,
				uidProvvedimento,
				(annoProvvedimento != null && annoProvvedimento.intValue() != 0) ? annoProvvedimento.toString() : null,
				numeroProvvedimento,
				mapToUidIfNotZero(tipoAtto),
				mapToUidIfNotZero(struttAmmContabile),
				annoCapitolo,
				numeroCapitolo,
				numeroArticolo,
				numeroUEB,
				SiacDDocStatoEnum.byStatiOperativi(statiOperativoDocumento),
				collegatoAMovimentoDelloStessoBilancio, 
				associatoAProvvedimentoOAdElenco, 
				importoDaIncassareZero, 
				importoDaPagareOIncassareMaggioreDiZero,
				rilevatiIvaConRegistrazioneONonRilevantiIva,
				collegatoALiquidazioneInStatoValidoConProvvDefinitivoConDispPagareMaggioreDiSubdocImporto,
				siacDAttoAmmStatoEnums,
				associatoAdOrdinativo,
				conFlagConvalidaManuale,
				listProvvisorioDiCassaUid
				);
	}
	
	public ListaPaginata<SubdocumentoEntrata> ricercaSubdocumentiEntrataPerProvvisorio(TipoDocumento tipoDocumento, Integer annoDocumento, String numeroDocumento, Date dataEmissioneDocumento,
			Integer numeroQuota, BigDecimal numeroMovimento,Integer annoMovimento, Soggetto soggetto, Integer annoElenco, Integer numeroElenco,Boolean flgEscludiSubDocCollegati, ParametriPaginazione parametriPaginazione) {
		
		Page<SiacTSubdoc> lista = subdocumentoDao.ricercaSinteticaSubdocumentiPerProvvisorio(
				ente.getUid(),
				EnumSet.of(SiacDDocFamTipoEnum.Entrata, SiacDDocFamTipoEnum.IvaEntrata),
				tipoDocumento != null ? tipoDocumento.getUid() : null,
				annoDocumento,
				numeroDocumento,
				dataEmissioneDocumento,
				numeroQuota,
				numeroMovimento,
				annoMovimento,
				soggetto != null ? soggetto.getUid() : null,
				soggetto != null ? soggetto.getCodiceSoggetto() : null,
				annoElenco,
				numeroElenco,
				flgEscludiSubDocCollegati != null ? flgEscludiSubDocCollegati : false,
			    toPageable(parametriPaginazione)		
				);

		return toListaPaginata(lista, SubdocumentoEntrata.class, BilMapId.SiacTSubdoc_SubdocumentoEntrata_Allegato);
	}

//	public BigDecimal ricercaSubdocumentiEntrataPerProvvisorioTotaleImporti(TipoDocumento tipoDocumento, Integer annoDocumento, String numeroDocumento, Date dataEmissioneDocumento,
//			Integer numeroQuota, BigDecimal numeroMovimento, Integer annoMovimento, Soggetto soggetto, Integer annoElenco, Integer numeroElenco, ParametriPaginazione parametriPaginazione) {
//		
//		BigDecimal totale = subdocumentoDao.ricercaSinteticaSubdocumentiPerProvvisorioTotaleImporti(
//				ente.getUid(),
//				EnumSet.of(SiacDDocFamTipoEnum.Entrata, SiacDDocFamTipoEnum.IvaEntrata),
//				tipoDocumento != null ? tipoDocumento.getUid() : null,
//				annoDocumento,
//				numeroDocumento,
//				dataEmissioneDocumento,
//				numeroQuota,
//				numeroMovimento,
//				annoMovimento,
//				soggetto != null ? soggetto.getUid() : null,
//				soggetto != null ? soggetto.getCodiceSoggetto() : null,
//				annoElenco,
//				numeroElenco,
//			    toPageable(parametriPaginazione)		
//				);
//		return totale;
//	}
	
	public BigDecimal ricercaSubdocumentiEntrataPerProvvisorioTotaleImporti(Collection<SubdocumentoEntrata> listaSubdocumenti) {
		if(listaSubdocumenti == null || listaSubdocumenti.isEmpty()) {
			return BigDecimal.ZERO;
		}
		Collection<Integer> uids = new ArrayList<Integer>();
		for(SubdocumentoEntrata se : listaSubdocumenti) {
			uids.add(se.getUid());
		}
		
		return subdocumentoDao.ricercaSinteticaSubdocumentiPerProvvisorioTotaleImporti(uids);
	}

	public Long contaDocumentiSpesaPadre(SubdocumentoEntrata subdoc) {
		Long count = siacTSubdocRepository.countSiacTDocPadre(subdoc.getUid(), SiacDRelazTipoEnum.Subdocumento.getCodice(),
				Arrays.asList(SiacDDocFamTipoEnum.Spesa.getCodice(), SiacDDocFamTipoEnum.IvaSpesa.getCodice()));
		return count;
	}
	
	public List<Integer> findUidSubdocumentiByUidDocumento(Integer docId) {
		List<SiacTSubdoc> siacTSubdocs = siacTSubdocRepository.findSiacTSubdocByDocId(docId);
		List<Integer> result = new ArrayList<Integer>();
		
		for(SiacTSubdoc siacTSubdoc : siacTSubdocs) {
			result.add(siacTSubdoc.getUid());
		}
		return result;
	}


	public BigDecimal findImportoSubdocumentoEntrataById(Integer uid) {
//		final String methodName = "findImportoSubdocumentoEntrataById";		
//		log.debug(methodName, "uid: "+ uid);
//		SiacTSubdoc siacTSuboc = subdocumentoDao.findById(uid);
//		if(siacTSuboc == null) {
//			log.debug(methodName, "Returning null. Impossibile trovare il SubdocumentoEntrata con id: " + uid);
//			return null;
//		}
//		
//		log.debug(methodName, "Returning "+siacTSuboc.getSubdocImporto()+". siacTSubdoc trovata con uid: " + siacTSuboc.getUid());
//		return siacTSuboc.getSubdocImporto();
		
		return siacTSubdocRepository.findImportoBySubdocId(uid);
		
	}
	
	public BigDecimal findImportoDaIncassareSubdocumentoEntrataById(Integer uid) {
//		final String methodName = "findImportoSubdocumentoEntrataById";		
//		log.debug(methodName, "uid: "+ uid);
//		SiacTSubdoc siacTSuboc = subdocumentoDao.findById(uid);
//		if(siacTSuboc == null) {
//			log.debug(methodName, "Returning null. Impossibile trovare il SubdocumentoEntrata con id: " + uid);
//			return null;
//		}
//		BigDecimal importo = siacTSuboc.getSubdocImporto() != null ? siacTSuboc.getSubdocImporto() : BigDecimal.ZERO;
//		BigDecimal importoDaDedurre = siacTSuboc.getSubdocImportoDaDedurre() != null ? siacTSuboc.getSubdocImportoDaDedurre() : BigDecimal.ZERO;
		
		BigDecimal importo = siacTSubdocRepository.findImportoBySubdocId(uid);
		BigDecimal importoDaDedurre = siacTSubdocRepository.findImportoDaDedurreBySubdocId(uid);
		return importo.subtract(importoDaDedurre);
		
	}
	
	
	/**
	 * Find importo da dedurre subdocumento spesa by id.
	 *
	 * @param uid the uid
	 * @return the big decimal
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public BigDecimal findImportoDaDedurreSubdocumentoEntrataByIdTxRequiresNew(Integer uid) {
		return findImportoDaDedurreSubdocumentoEntrataById(uid);
	}
	
	/**
	 * Find importo da dedurre subdocumento spesa by id.
	 *
	 * @param uid the uid
	 * @return the big decimal
	 */
	public BigDecimal findImportoDaDedurreSubdocumentoEntrataById(Integer uid) {
		final String methodName = "findImportoSubdocumentoEntrataById";		
		log.debug(methodName, "uid: "+ uid);
		SiacTSubdoc siacTSuboc = subdocumentoDao.findById(uid);
		if(siacTSuboc == null) {
			log.debug(methodName, "Returning null. Impossibile trovare il SubdocumentoSpesa con id: " + uid);
			return null;
		}
		
		log.debug(methodName, "Returning "+siacTSuboc.getSubdocImportoDaDedurre()+". siacTSubdoc trovata con uid: " + siacTSuboc.getUid());
		return siacTSuboc.getSubdocImportoDaDedurre();
		
	}

	


	public Integer findUidAccertamentoSubdocumentoEntrataById(int uid) {
		final String methodName = "findUidAccertamentoSubdocumentoEntrataById";		
		log.debug(methodName, "uid: "+ uid);
		SiacTMovgestT siacTMovgestT = siacTSubdocRepository.findMovgestTSByIdSubdoc(uid);
		if(siacTMovgestT == null) {
			log.debug(methodName, "Returning null. Impossibile trovare il movimento gestione legato al SubdocumentoEntrata con id: " + uid);
			return null;
		}
		//restituisco l'uid solo se il subdoc è legato ad un accertamento, altrimenti null
		if(siacTMovgestT.getSiacTMovgestIdPadre() == null){
			return siacTMovgestT.getSiacTMovgest().getMovgestId();
		}else{
			return null;
		}
	}

	
	public Integer findUidSubAccertamentoSubdocumentoEntrataById(int uid) {
		final String methodName = "findUidSubAccertamentoSubdocumentoEntrataById";		
		log.debug(methodName, "uid: "+ uid);
		SiacTMovgestT siacTMovgestT = siacTSubdocRepository.findMovgestTSByIdSubdoc(uid);
		if(siacTMovgestT == null) {
			log.debug(methodName, "Returning null. Impossibile trovare il movimento gestione legato al SubdocumentoEntrata con id: " + uid);
			return null;
		}
		//restituisco l'uid solo se il subdoc è legato ad un subAccertamento, altrimenti null
		if(siacTMovgestT.getSiacTMovgestIdPadre() != null){
			return siacTMovgestT.getUid();
		}else{
			return null;
		}
	}

	public List<SubdocumentoEntrata> findSubdocumentiEntrataByIdDocumentoAfterFlushAndClear(Integer uid) {
		subdocumentoDao.flushAndClear();
		return findSubdocumentiEntrataByIdDocumento(uid);
	}
	
	public void aggiornaImportoSubdocumentoEntrata(int uid, BigDecimal importoResiduo, BigDecimal importoDaDedurre) {
		SiacTSubdoc siacTSubdoc = subdocumentoDao.findById(uid);		
		siacTSubdoc.setSubdocImporto(importoResiduo);
		siacTSubdoc.setSubdocImportoDaDedurre(importoDaDedurre);
		subdocumentoDao.flush();
	}

	/**
	 * Esegue un Flush e un clear dell'EntityManager
	 */
	public void flushAndClear() {
		subdocumentoDao.flushAndClear();
	}
	
	/**
	 * Come {@link #computeKeySubdocImportoImpegnoFlagRilevanteIva(int)}
	 * ma esegue la query in un nuova transazione.
	 * 
	 * @param uid
	 * @return la chiave restituita da {@link #computeKeySubdocImportoImpegnoFlagRilevanteIva(int)}
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public String computeKeySubdocImportoAccertamentoFlagRilevanteIvaTxRequiresNew(int uid) {
		return computeKeySubdocImportoAccertamentoFlagRilevanteIva(uid);
	}

	/**
	 * Calcola una chiave per il subdocumento dato da i seguenti campi: Importo, accertamento/subaccertamento, FlagRilevanteIVA
	 * 
	 * @param uid
	 * @return Chiavi di esempio restituite: "1000.00_1_T_N" (Quando legato ad accertamento), "100.00_4_S_N" (Quando legato a subaccertamento)
	 */
	public String computeKeySubdocImportoAccertamentoFlagRilevanteIva(int uid) {
		final String methodName = "computeKeySubdocImportoAccertamentoFlagRilevanteIva";
		
		String result = subdocumentoDao.computeKeySubdocImportoMovimentoGestioneFlagRilevanteIva(uid);
		log.debug(methodName, "returning: "+result);
		return result;
	}
	
	
	/**
	 * Trova la nota credito iva associata al subdocumento
	 * 
	 * @param subdoc
	 * @return nota credito iva
	 */
	public SubdocumentoIvaEntrata findNotaCreditoIvaAssociata(SubdocumentoEntrata subdoc) {
		final String methodName = "findNotaCreditoIvaAssociata";
		SiacTSubdocIva siacTSubdocIva = siacTSubdocRepository.findSiacTSubdocIvaNotaCreditoIvaAssociata(subdoc.getUid());
		log.debug(methodName, "returning "+(siacTSubdocIva!=null?"uid: "+siacTSubdocIva.getUid():"null"));
		return mapNotNull(siacTSubdocIva, SubdocumentoIvaEntrata.class, BilMapId.SiacTSubdocIva_SubdocumentoIvaEntrata);
	}
	
	public SubdocumentoIvaEntrata findNotaCreditoIvaAssociataUid(SubdocumentoEntrata subdoc) {
		final String methodName = "findNotaCreditoIvaAssociata";
		SiacTSubdocIva siacTSubdocIva = siacTSubdocRepository.findSiacTSubdocIvaNotaCreditoIvaAssociata(subdoc.getUid());
		if(siacTSubdocIva==null){
			log.debug(methodName, "returning null");
			return null;
		}
		log.debug(methodName, "returning uid: "+siacTSubdocIva.getUid());
		
		SubdocumentoIvaEntrata result = new SubdocumentoIvaEntrata();
		result.setUid(siacTSubdocIva.getUid());
		return result;
	}

	/**
	 * Verifica se la quota non e' pagata.
	 * 
	 * @param subdoc
	 * @return true se la quota non e' pagata.
	 */
	public boolean isQuotaNonPagata(SubdocumentoEntrata subdoc) {
		final String methodName = "isQuotaNonPagata";
		SiacTSubdoc siacTSubdoc = siacTSubdocRepository.findSiacTSubdocSenzaOrdinativoAssociatoNonAnnullatoENonPagatoCEC(subdoc.getUid());
		boolean isQuotaNonPagata = siacTSubdoc != null;
		log.debug(methodName, "returning result: " + isQuotaNonPagata);
		return isQuotaNonPagata;
	}

	public Long countSubdocIvaCollegati(SubdocumentoEntrata subdoc) {
		return siacTSubdocRepository.countSiacTSubdocIvaBySubdocIdAndDocId(subdoc.getUid(), subdoc.getDocumento().getUid());
	}

	public void aggiornaFlagConvalidaManuale(SubdocumentoEntrata subdoc) {
		SiacTSubdoc siacTSubdoc = siacTSubdocRepository.findOne(subdoc.getUid());
		BooleanToStringManualeAutomaticoConverter conv = new BooleanToStringManualeAutomaticoConverter();
		siacTSubdoc.setSubdocConvalidaManuale(conv.convertTo(subdoc.getFlagConvalidaManuale()));
		siacTSubdocRepository.saveAndFlush(siacTSubdoc);
	}
	
	public Long countOrdinativiAssociatiAQuota(SubdocumentoEntrata subdocumentoEntrata){
		
		return siacTSubdocRepository.countSiacROrdinativoBySubdocId(subdocumentoEntrata.getUid());
	}
	
	/**
	 * Aggiorna il falg convalida manuale del subdoc.
	 *
	 * @param subdoc the subdoc
	 * @param bilancioAttuale the bilancio attuale
	 * @param bilancioSuccessivo the bilancio successivo
	 * @param now the now
	 */
	public void collegaScollegaMovimentoGestionePerDoppiaGestione(SubdocumentoEntrata subdoc, Bilancio bilancioAttuale, Bilancio bilancioSuccessivo, Date now) {
		final String methodName="collegaScollegaMovimentoGestionePerDoppiaGestione";
		
		Object[] chiaveMovgest = cercaChiaveMovgestCollegatoASubdoc(subdoc);
		if(chiaveMovgest == null) {
			log.error(methodName, "Impossibile determinare univocamente la chiave del movimento di gestione per il subdocumento [ uid: " + subdoc.getUid() +  " ].");
			throw new BusinessException("Impossibile determinare univocamente la chiave del movimento di gestione per il subdocumento [ uid: " + subdoc.getUid() +  " ].");
//			return;
		}
		Integer movgestAnno = (Integer) chiaveMovgest[0];
		BigDecimal movgestNumero = (BigDecimal) chiaveMovgest[1];
		String movgestTsCode = (String) chiaveMovgest[2];
		String movgestTsTipoCode = (String) chiaveMovgest[3];
				
		log.debug(methodName, "Cerco una relazione tra subdocumento [ uid: " + subdoc.getUid() +  " ] e il movimento gestione per il bilancio attuale [ uid: " + bilancioAttuale.getUid() + " ].");
		List<SiacRSubdocMovgestT> siacRSubdocMovgestTBilancioCorrentes = siacRSubdocMovgestTRepository.findRelazioneCancellataBySubdocIdAndBilIdAndChiaveMovgestOrderByValiditaFineDesc(subdoc.getUid(), movgestAnno, movgestNumero, movgestTsCode, movgestTsTipoCode,  bilancioAttuale.getUid());
		SiacRSubdocMovgestT siacRSubdocMovgestTBilancioCorrente = siacRSubdocMovgestTBilancioCorrentes != null && !siacRSubdocMovgestTBilancioCorrentes.isEmpty()? siacRSubdocMovgestTBilancioCorrentes.get(0) : null;		 
		if(siacRSubdocMovgestTBilancioCorrente == null ) {
			log.error(methodName, "non c'e' nessuna siacRSubdocMovgestTBilancioCorrente per il  subdocumento [ uid: " + subdoc.getUid() +  " ].");
//			throw new BusinessException("non c'e' nessuna siacRSubdocMovgestTBilancioCorrente per il  subdocumento [ uid: " + subdoc.getUid() +  " ].");
			return;
		}
		
		log.debug(methodName, "Cerco una relazione tra subdocumento [ uid: " + subdoc.getUid() +  " ] e il movimento gestione per il bilancio successivo [ uid: " + bilancioSuccessivo.getUid() + " ].");
		List<SiacRSubdocMovgestT> siacRSubdocMovgestTBilancioSuccessivos = siacRSubdocMovgestTRepository.findUltimaRelazioneValidaBySubdocIdAndBilIdAndChiaveMovgest(subdoc.getUid(), movgestAnno, movgestNumero, movgestTsCode, movgestTsTipoCode, bilancioSuccessivo.getUid());
		SiacRSubdocMovgestT siacRSubdocMovgestTBilancioSuccessivo = siacRSubdocMovgestTBilancioSuccessivos != null && siacRSubdocMovgestTBilancioSuccessivos.size() == 1? siacRSubdocMovgestTBilancioSuccessivos.get(0) : null;
		
		if(siacRSubdocMovgestTBilancioSuccessivo == null ) {
			log.error(methodName, "non c'e' nessuna siacRSubdocMovgestTBilancioSuccessivo per il  subdocumento [ uid: " + subdoc.getUid() +  " ].");
//			throw new BusinessException("non c'e' nessuna siacRSubdocMovgestTBilancioSuccessivo per il  subdocumento [ uid: " + subdoc.getUid() +  " ].");
			return;
		}
		
		log.debug(methodName, "Aggiorno la relazione [ uid: " + siacRSubdocMovgestTBilancioCorrente.getUid() +  " ] per il bilancio attuale [ uid: " + bilancioAttuale.getUid() + " ].");
		impostaFineValiditaCancellazioneLoginOperazione(null, null , now, siacRSubdocMovgestTBilancioCorrente);		
		siacRSubdocMovgestTRepository.saveAndFlush(siacRSubdocMovgestTBilancioCorrente);
		
		log.debug(methodName, "Aggiorno la relazione [ uid: " + siacRSubdocMovgestTBilancioSuccessivo.getUid() +  " ] per il bilancio successivo [ uid: " + bilancioSuccessivo.getUid() + " ].");
		
		impostaFineValiditaCancellazioneLoginOperazione(now, now, now, siacRSubdocMovgestTBilancioSuccessivo);
		siacRSubdocMovgestTRepository.saveAndFlush(siacRSubdocMovgestTBilancioSuccessivo);		
			
	}

	/**
	 * Cerca chiave movgest collegato A subdoc.
	 *
	 * @param subdoc the subdoc
	 * @return the object[]
	 */
	public Object[] cercaChiaveMovgestCollegatoASubdoc(SubdocumentoEntrata subdoc) {
		List<Object[]> chiaviMovgest = siacRSubdocMovgestTRepository.findChiaveMovimentoGestioneAssociatoBySubdocId(subdoc.getUid());
		return chiaviMovgest != null && chiaviMovgest.size() == 1 ? chiaviMovgest.get(0) : null;
	}

	/**
	 * @param now
	 * @param siacRSubdocMovgestTBilancioSuccessivo
	 */
	private void impostaFineValiditaCancellazioneLoginOperazione(Date dataCancellazione, Date dataFineValidita,Date dataModifica, SiacTBase siacTBase) {
		siacTBase.setDataFineValidita(dataFineValidita);
		siacTBase.setDataCancellazione(dataCancellazione);
		siacTBase.setDataModifica(dataModifica);
		siacTBase.setLoginOperazione(loginOperazione);
	}
	
	private List<Integer> estraiIdProvCassa(List<ProvvisorioDiCassa> listProvvisorioDiCassa) {		
		List<Integer> listProvvisorioDiCassaUid = new ArrayList<Integer>();
		if (listProvvisorioDiCassa!=null && !listProvvisorioDiCassa.isEmpty()){
			for(ProvvisorioDiCassa pdc : listProvvisorioDiCassa){		
				listProvvisorioDiCassaUid.add(pdc.getUid());
			}
		}
		return listProvvisorioDiCassaUid;
	}
	
}
