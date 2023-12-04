/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/

package it.csi.siac.siacbilser.integration.dad;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.StatoOperativoAtti;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.dao.SiacRSubdocAttrRepository;
import it.csi.siac.siacbilser.integration.dao.SiacRSubdocLiquidazioneRepository;
import it.csi.siac.siacbilser.integration.dao.SiacRSubdocMovgestTRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTAttoAmmRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTLiquidazioneRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTSubdocNumRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTSubdocRepository;
import it.csi.siac.siacbilser.integration.dao.SubdocumentoDao;
import it.csi.siac.siacbilser.integration.entity.SiacDLiquidazioneStato;
import it.csi.siac.siacbilser.integration.entity.SiacDSiopeAssenzaMotivazione;
import it.csi.siac.siacbilser.integration.entity.SiacDSplitreverseIvaTipo;
import it.csi.siac.siacbilser.integration.entity.SiacDSubdocTipo;
import it.csi.siac.siacbilser.integration.entity.SiacRLiquidazioneStato;
import it.csi.siac.siacbilser.integration.entity.SiacRSubdocAttr;
import it.csi.siac.siacbilser.integration.entity.SiacRSubdocLiquidazione;
import it.csi.siac.siacbilser.integration.entity.SiacRSubdocMovgestT;
import it.csi.siac.siacbilser.integration.entity.SiacRSubdocSplitreverseIvaTipo;
import it.csi.siac.siacbilser.integration.entity.SiacTAttoAmm;
import it.csi.siac.siacbilser.integration.entity.SiacTAttr;
import it.csi.siac.siacbilser.integration.entity.SiacTCartacont;
import it.csi.siac.siacbilser.integration.entity.SiacTDoc;
import it.csi.siac.siacbilser.integration.entity.SiacTEnteProprietario;
import it.csi.siac.siacbilser.integration.entity.SiacTLiquidazione;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgestT;
import it.csi.siac.siacbilser.integration.entity.SiacTSoggetto;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdocIva;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdocNum;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdocSospensione;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDAttoAmmStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocFamTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDLiquidazioneStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDProvCassaTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDRelazTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDSplitreverseIvaTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDSubdocTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTAttrEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.BooleanToStringConverter;
import it.csi.siac.siacbilser.integration.entitymapping.converter.BooleanToStringManualeAutomaticoConverter;
import it.csi.siac.siacbilser.integration.entitymapping.converter.base.Converters;
import it.csi.siac.siacbilser.integration.entitymapping.converter.handler.SiacTAttrHandler;
import it.csi.siac.siacbilser.model.TipologiaAttributo;
import it.csi.siac.siaccommon.util.CoreUtil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.integration.entity.SiacTBase;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.DocumentoSpesaModelDetail;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.SospensioneSubdocumento;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesaModelDetail;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfin2ser.model.TipoIvaSplitReverse;
import it.csi.siac.siacfinser.model.carta.CartaContabile;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione.StatoOperativoLiquidazione;
import it.csi.siac.siacfinser.model.siopeplus.SiopeAssenzaMotivazione;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * Data access delegate di un SubdocumentoSpesa .
 *
 * @author Domenico
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class SubdocumentoSpesaDad extends ExtendedBaseDadImpl {
	
	private DateFormat dateFormat;
	
	@Autowired
	private EnumEntityFactory eef;
	
	@Autowired
	private SubdocumentoDao subdocumentoDao;
	
	@Autowired
	private SiacTSubdocRepository siacTSubdocRepository;
	
	@Autowired
	private SiacTSubdocNumRepository siacTSubdocNumRepository;
	
	@Autowired
	private SiacRSubdocAttrRepository siacRSubdocAttrRepository;
	
	@Autowired
	private SiacRSubdocLiquidazioneRepository siacRSubdocLiquidazioneRepository;
	
	@Autowired
	private SiacTLiquidazioneRepository siacTLiquidazioneRepository;
	
	@Autowired
	private SiacTAttoAmmRepository siacTAttoAmmRepository;
	//SIAC-5937
	@Autowired
	private SiacRSubdocMovgestTRepository siacRSubdocMovgestTRepository;
	
	
	
	/**
	 * Ottiene un Subdocumento (o Quota) a partire dal suo uid.
	 *
	 * @param uid del subdocumento
	 * @return the subdocumento spesa
	 */
	public SubdocumentoSpesa findSubdocumentoSpesaById(Integer uid) {
		final String methodName = "findSubdocumentoSpesaById";		
		log.debug(methodName, "uid: "+ uid);
		SiacTSubdoc siacTSuboc = subdocumentoDao.findById(uid);
		if(siacTSuboc == null) {
			log.debug(methodName, "Impossibile trovare il SubdocumentoSpesa con id: " + uid);
		}else{
			log.debug(methodName, "siacTSubdoc trovata con uid: " + siacTSuboc.getUid());
		}
		return  mapNotNull(siacTSuboc, SubdocumentoSpesa.class, BilMapId.SiacTSubdoc_SubdocumentoSpesa);
	}
	
	/**
	 * Ottiene un Subdocumento (o Quota) a partire dal suo uid.
	 *
	 * @param uid del subdocumento
	 * @return the subdocumento spesa
	 */
	public SubdocumentoSpesa findSubdocumentoSpesaPerEmissione(Integer uid) {
		final String methodName = "findSubdocumentoSpesaById";		
		log.debug(methodName, "uid: "+ uid);
		SiacTSubdoc siacTSuboc = subdocumentoDao.findById(uid);
		if(siacTSuboc != null) {
			log.debug(methodName, "siacTSubdoc trovata con uid: " + siacTSuboc.getUid());
			return  mapNotNull(siacTSuboc, SubdocumentoSpesa.class, BilMapId.SiacTSubdoc_SubdocumentoSpesa_Emettitore);
			
		}
		log.debug(methodName, "Impossibile trovare il SubdocumentoSpesa con id: " + uid);
		return null;		
	}
	
	/**
	 * Ottiene un Subdocumento (o Quota) a partire dal suo uid. Permette di specificare le parti di modello desiderate.
	 *
	 * @param uid del subdocumento
	 * @return the subdocumento spesa
	 */
	public SubdocumentoSpesa findSubdocumentoSpesaById(Integer uid, SubdocumentoSpesaModelDetail... modelDetails) {
		final String methodName = "findSubdocumentoSpesaById[modelDetails]";		
		log.debug(methodName, "uid: "+ uid);
		SiacTSubdoc siacTSubdoc = subdocumentoDao.findById(uid);
		if(siacTSubdoc == null) {
			log.debug(methodName, "Impossibile trovare il SubdocumentoSpesa con id: " + uid);
		}else{
			log.debug(methodName, "siacTSubdoc trovata con uid: " + siacTSubdoc.getUid());
		}
		return  mapNotNull(siacTSubdoc, SubdocumentoSpesa.class, BilMapId.SiacTSubdoc_SubdocumentoSpesa_Base, Converters.byModelDetails(modelDetails));
	}
	
	/**
	 * Ottiene un Subdocumento (o Quota) a partire dal suo uid con i suoi dati di base.
	 *
	 * @param uid del subdocumento
	 * @return the subdocumento spesa
	 */
	public SubdocumentoSpesa findSubdocumentoSpesaBaseById(Integer uid) {
		final String methodName = "findSubdocumentoSpesaById";		
		log.debug(methodName, "uid: "+ uid);
		SiacTSubdoc siacTSuboc = subdocumentoDao.findById(uid);
		if(siacTSuboc == null) {
			log.debug(methodName, "Impossibile trovare il SubdocumentoSpesa con id: " + uid);
		}else{
			log.debug(methodName, "siacTSubdoc trovata con uid: " + siacTSuboc.getUid());
		}
		return  mapNotNull(siacTSuboc, SubdocumentoSpesa.class, BilMapId.SiacTSubdoc_SubdocumentoSpesa_Base);
	}
	
	/**
	 * Conta i subdocumenti per id
	 *
	 * @param uid del subdocumento
	 * @return the subdocumento spesa
	 */
	public Long countSubdocumentiSpesaByIds(Collection<Integer> uids) {
		final String methodName = "countSubdocumentiSpesaByIds";
		log.debug(methodName, "uids: "+ uids);
		Collection<String> docFamTipoCodes = Arrays.asList(SiacDDocFamTipoEnum.Spesa.getCodice(), SiacDDocFamTipoEnum.IvaSpesa.getCodice());
		return siacTSubdocRepository.countBySubdocId(uids, docFamTipoCodes);
	}
	
	/**
	 * Ottiene la lista dei subdocumenti di un documento di spesa.
	 *
	 * @param idDocumento the id documento
	 * @return lista dei subdocumenti
	 */
	public List<SubdocumentoSpesa> findSubdocumentiSpesaByIdDocumento(Integer idDocumento) {
		List<SiacTSubdoc> siacTSubdocs = siacTSubdocRepository.findSiacTSubdocByDocId(idDocumento);		
		return convertiLista(siacTSubdocs, SubdocumentoSpesa.class, BilMapId.SiacTSubdoc_SubdocumentoSpesa);
	}
	
	/**
	 * Ottiene la lista dei subdocumenti di un documento di spesa.
	 *
	 * @param idDocumento the id documento
	 * @param parametriPaginazione i parametri di paginazione
	 * @return lista dei subdocumenti
	 */
	public ListaPaginata<SubdocumentoSpesa> findSubdocumentiSpesaByIdDocumento(Integer idDocumento, Boolean rilevanteIva, ParametriPaginazione parametriPaginazione, SubdocumentoSpesaModelDetail... modelDetails) {
		Order order = new Sort.Order(Direction.ASC, "subdocNumero");
		Sort sort = new Sort(order);
		
		Pageable pageable = toPageable(parametriPaginazione, sort);
		Page<SiacTSubdoc> siacTSubdocs = subdocumentoDao.ricercaSinteticaSubdocumentiByDocId(idDocumento, rilevanteIva, pageable);
		return toListaPaginata(siacTSubdocs, SubdocumentoSpesa.class, BilMapId.SiacTSubdoc_SubdocumentoSpesa_Base, modelDetails);
	
	}
	
	/**
	 * Ottiene il totale degli importi dei subdocumenti di un documento di spesa.
	 *
	 * @param idDocumento the id documento
	 * @return il totale degli importi
	 */
	public BigDecimal findTotaleImportoSubdocumentiSpesaByIdDocumento(Integer idDocumento) {
		return siacTSubdocRepository.sumSubdocImportoByDocId(idDocumento);
	
	}
	
	/**
	 * Ottiene la lista dei subdocumenti di un documento di spesa.
	 *
	 * @param idDocumento the id documento
	 * @return lista dei subdocumenti
	 */
	public List<SubdocumentoSpesa> findSubdocumentiSpesaBaseByIdDocumento(Integer idDocumento) {
		List<SiacTSubdoc> siacTSubdocs = siacTSubdocRepository.findSiacTSubdocByDocId(idDocumento);		
		return convertiLista(siacTSubdocs, SubdocumentoSpesa.class, BilMapId.SiacTSubdoc_SubdocumentoSpesa_Base);
	
	}
	
	/**
	 * Ottiene la lista dei subdocumenti di un documento di spesa.
	 *
	 * @param idDocumento the id documento
	 * @return lista dei subdocumenti
	 */
	public List<SubdocumentoSpesa> findSubdocumentiSpesaModelDetailByIdDocumento(Integer idDocumento, SubdocumentoSpesaModelDetail... subdocumentoSpesaModelDetail) {
		List<SiacTSubdoc> siacTSubdocs = siacTSubdocRepository.findSiacTSubdocByDocId(idDocumento);		
		return convertiLista(siacTSubdocs, SubdocumentoSpesa.class, BilMapId.SiacTSubdoc_SubdocumentoSpesa_ModelDetail, Converters.byModelDetails(subdocumentoSpesaModelDetail));
	
	}
	
	/**
	 * Ottiene la lista dei subdocumenti di un documento di spesa.
	 *
	 * @param idDocumento the id documento
	 * @return lista dei subdocumenti
	 */
	public List<SubdocumentoSpesa> findSubdocumentiSpesaByIdDocumento(Integer idDocumento, SubdocumentoSpesaModelDetail... subdocumentoSpesaModelDetail) {
		List<SiacTSubdoc> siacTSubdocs = siacTSubdocRepository.findSiacTSubdocByDocId(idDocumento);		
		
		return convertiLista(siacTSubdocs, SubdocumentoSpesa.class, BilMapId.SiacTSubdoc_SubdocumentoSpesa_Base, Converters.byModelDetails(subdocumentoSpesaModelDetail));
		
	}
	
	
	
	/**
	 * Ottiene il Documento a cui e' associato il subdocumento con l'id passato.
	 *
	 * @param idSubDocumento the id sub documento
	 * @return lista dei subdocumenti
	 */
	public DocumentoSpesa findDocumentoByIdSubdocumentoSpesa(Integer idSubDocumento) {
		SiacTSubdoc siacTSubdoc = siacTSubdocRepository.findOne(idSubDocumento);
		SiacTDoc siacTDoc = siacTSubdoc.getSiacTDoc();
		return mapNotNull(siacTDoc, DocumentoSpesa.class, BilMapId.SiacTDoc_DocumentoSpesa_Base);	
	}
	
	/**
	 * Ottiene il Documento a cui e' associato il subdocumento con l'id passato.
	 *
	 * @param idSubDocumento the id sub documento
	 * @return lista dei subdocumenti
	 */
	public DocumentoSpesa findDocumentoByIdSubdocumentoSpesaModelDetail(Integer idSubDocumento, DocumentoSpesaModelDetail... modelDetails) {
		SiacTSubdoc siacTSubdoc = siacTSubdocRepository.findOne(idSubDocumento);
		SiacTDoc siacTDoc = siacTSubdoc.getSiacTDoc();
		return mapNotNull(siacTDoc, DocumentoSpesa.class, BilMapId.SiacTDoc_DocumentoSpesa_ModelDetail, Converters.byModelDetails(modelDetails));
	}
	
	/**
	 * Inserisci anagrafica subdocumento spesa.
	 *
	 * @param documento the documento
	 */
	//@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void inserisciAnagraficaSubdocumentoSpesa(SubdocumentoSpesa documento) {		
		SiacTSubdoc siacTDoc = buildSiacTSubdoc(documento);	
		siacTDoc.setLoginCreazione(loginOperazione);
		siacTDoc.setLoginModifica(loginOperazione);
		subdocumentoDao.create(siacTDoc);
		documento.setUid(siacTDoc.getUid());
	}	
	
	/**
	 * Associa liquidazione.
	 *
	 * @param subdocumentoSpesa the subdocumento spesa
	 * @param liq the liq
	 */
	public void associaLiquidazione(SubdocumentoSpesa subdocumentoSpesa, Liquidazione liq){
		Date now = new Date();
		
		SiacTSubdoc siacTSubdoc = siacTSubdocRepository.findOne(subdocumentoSpesa.getUid());
		
		if(siacTSubdoc.getSiacRSubdocLiquidaziones() != null){
			for(SiacRSubdocLiquidazione siacRSubdocLiquidazione : siacTSubdoc.getSiacRSubdocLiquidaziones()){
				SiacTLiquidazione siacTLiquidazione = siacRSubdocLiquidazione.getSiacTLiquidazione();
				//cancello le precedenti relazioni, tranne quelle con le liquidazioni aggiornate
				for(SiacRLiquidazioneStato siacRLiquidazioneStato : siacTLiquidazione.getSiacRLiquidazioneStatos()){
					if(siacRLiquidazioneStato.getDataCancellazione() == null && siacRLiquidazioneStato.getDataFineValidita() == null &&
								!siacRLiquidazioneStato.getSiacDLiquidazioneStato().getLiqStatoCode().equals(SiacDLiquidazioneStatoEnum.Annullato.getCodice())){
						siacRSubdocLiquidazione.setDataCancellazioneIfNotSet(now);
						break;
					}
				}
			}
		}
		
		SiacTLiquidazione siacTLiquidazione = new SiacTLiquidazione();
		siacTLiquidazione.setUid(liq.getUid());
		SiacRSubdocLiquidazione siacRSubdocLiquidazione = new SiacRSubdocLiquidazione();
		siacRSubdocLiquidazione.setSiacTLiquidazione(siacTLiquidazione);
		siacRSubdocLiquidazione.setSiacTSubdoc(siacTSubdoc);
		siacRSubdocLiquidazione.setSiacTEnteProprietario(siacTSubdoc.getSiacTEnteProprietario());
		siacRSubdocLiquidazione.setLoginOperazione(siacTSubdoc.getLoginOperazione());
		
		siacRSubdocLiquidazione.setDataModificaInserimento(now);
		
		List<SiacRSubdocLiquidazione> siacRSubdocLiquidaziones = siacTSubdoc.getSiacRSubdocLiquidaziones() != null ? siacTSubdoc.getSiacRSubdocLiquidaziones() : new ArrayList<SiacRSubdocLiquidazione>();
		siacRSubdocLiquidaziones.add(siacRSubdocLiquidazione);		
		siacTSubdoc.setSiacRSubdocLiquidaziones(siacRSubdocLiquidaziones);
		
		siacTSubdocRepository.flush();
	}
	
	

	/**
	 * Aggiorna anagrafica subdocumento spesa.
	 *
	 * @param documento the documento
	 */
	public void aggiornaAnagraficaSubdocumentoSpesa(SubdocumentoSpesa documento) {
		SiacTSubdoc siacTDoc = buildSiacTSubdoc(documento);	
		siacTDoc.setLoginModifica(loginOperazione);
		subdocumentoDao.update(siacTDoc);
		documento.setUid(siacTDoc.getUid());
		// SIAC-6484
		subdocumentoDao.flushAndClear();
	}	
	
	
	/**
	 * Aggiorna solo gli importi del subdocumento di spesa passato come parametro.
	 *
	 * @param subdoc the subdoc
	 */
	public void aggiornaImportoSubdocumentoSpesa(SubdocumentoSpesa subdoc) {
		final String methodName = "aggiornaImportiSubdocumentoSpesa";
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
	 * Aggiorna solo gli importi del subdocumento di spesa passato come parametro.
	 *
	 * @param subdoc the subdoc
	 */
	public void aggiornaImportoDaDedurreSubdocumentoSpesa(SubdocumentoSpesa subdoc) {
		final String methodName = "aggiornaImportiSubdocumentoSpesa";
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
	 * Builds the siac t subdoc.
	 *
	 * @param subdocumento the subdocumento
	 * @return the siac t subdoc
	 */
	private SiacTSubdoc buildSiacTSubdoc(SubdocumentoSpesa subdocumento) {
		SiacTSubdoc siacTSubdoc = new SiacTSubdoc();
		siacTSubdoc.setLoginOperazione(loginOperazione);
		subdocumento.setLoginOperazione(loginOperazione);
		map(subdocumento, siacTSubdoc, BilMapId.SiacTSubdoc_SubdocumentoSpesa);		
		siacTSubdoc.setSiacDSubdocTipo(eef.getEntity(SiacDSubdocTipoEnum.Spesa, subdocumento.getEnte().getUid(), SiacDSubdocTipo.class));
		//siacTSubdoc.getSiacTDoc().setSubdocNumeroSeq(subdocumento.getNumero());
		return siacTSubdoc;
	}
	
	
	/**
	 * Elimina subdocumento spesa.
	 *
	 * @param subdocumento the subdocumento
	 */
	public void eliminaSubdocumentoSpesa(SubdocumentoSpesa subdocumento){
		SiacTSubdoc siacTSubdoc = new SiacTSubdoc();
		siacTSubdoc.setUid(subdocumento.getUid());
		siacTSubdoc.setLoginCancellazione(loginOperazione);
		subdocumentoDao.delete(siacTSubdoc);
		subdocumentoDao.flush();
	}
	
	
	
	
	/**
	 * Ricerca subdocumenti spesa.
	 *
	 * @param ente the ente
	 * @param uidElenco the uid elenco
	 * @param annoElenco the anno elenco
	 * @param numeroElenco the numero elenco
	 * @param numeroElencoDa the numero elenco da
	 * @param numeroElencoA the numero elenco a
	 * @param annoProvvisorio the anno provvisorio
	 * @param numeroProvvisorio the numero provvisorio
	 * @param dataProvvisorio the data provvisorio
	 * @param tipoDocumento the tipo documento
	 * @param annoDocumento the anno documento
	 * @param numeroDocumento the numero documento
	 * @param dataEmissioneDocumento the data emissione documento
	 * @param numeroQuota the numero quota
	 * @param numeroMovimento the numero movimento
	 * @param annoMovimento the anno movimento
	 * @param soggetto the soggetto
	 * @param uidProvvedimento the uid provvedimento
	 * @param annoProvvedimento the anno provvedimento
	 * @param numeroProvvedimento the numero provvedimento
	 * @param tipoAtto the tipo atto
	 * @param struttAmmContabile the strutt amm contabile
	 * @param annoCapitolo the anno capitolo
	 * @param numeroCapitolo the numero capitolo
	 * @param numeroArticolo the numero articolo
	 * @param numeroUEB the numero ueb
	 * @param statiOperativoDocumento the stati operativo documento
	 * @param collegatoAMovimentoDelloStessoBilancio the collegato a movimento dello stesso bilancio
	 * @param associatoAProvvedimentoOAdElenco the associato a provvedimento o ad elenco
	 * @param importoDaPagareZero the importo da pagare zero
	 * @param importoDaPagareOIncassareMaggioreDiZero the importo da pagare o incassare maggiore di zero
	 * @param rilevatiIvaConRegistrazioneONonRilevantiIva the rilevati iva con registrazione o non rilevanti iva
	 * @param collegatoALiquidazioneInStatoValidoConProvvDefinitivoConDispPagareMaggioreDiSubdocImporto the collegato a liquidazione in stato valido con provv definitivo con disp pagare maggiore di subdoc importo
	 * @param statiOperativiAtti the stati operativi atti
	 * @param associatoAdOrdinativo the associato ad ordinativo
	 * @param parametriPaginazione the parametri paginazione
	 * @return the lista paginata
	 */
	public ListaPaginata<SubdocumentoSpesa> ricercaSubdocumentiSpesa(Ente ente, 
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
					Boolean importoDaPagareZero, 
					Boolean importoDaPagareOIncassareMaggioreDiZero,
					Boolean rilevatiIvaConRegistrazioneONonRilevantiIva,
					Boolean collegatoALiquidazioneInStatoValidoConProvvDefinitivoConDispPagareMaggioreDiSubdocImporto,
					Collection<StatoOperativoAtti> statiOperativiAtti,
					Boolean associatoAdOrdinativo,
					Boolean conFlagConvalidaManuale,
					List<Integer> listProvCassaUid,
					ParametriPaginazione parametriPaginazione
			) {
			
		Set<SiacDAttoAmmStatoEnum> siacDAttoAmmStatoEnums = SiacDAttoAmmStatoEnum.byStatiOperativi(statiOperativiAtti);
		
		Page<SiacTSubdoc> lista = subdocumentoDao.ricercaSinteticaSubdocumenti(ente.getUid(), bilancio!=null?bilancio.getUid():null,
				EnumSet.of(SiacDDocFamTipoEnum.Spesa, SiacDDocFamTipoEnum.IvaSpesa),
				 uidElenco,
				 annoElenco,
				 numeroElenco,
				 numeroElencoDa,
				 numeroElencoA,
				 annoProvvisorio,
				 numeroProvvisorio != null ? new BigDecimal(numeroProvvisorio.toString()) : null,
				 dataProvvisorio,
				 SiacDProvCassaTipoEnum.Spesa,
				 tipoDocumento!=null?tipoDocumento.getUid():null,
				 annoDocumento,
				 numeroDocumento,
 				 dataEmissioneDocumento,
 				 numeroQuota,	
 				 numeroMovimento,
 				 annoMovimento,
 				 soggetto!=null?soggetto.getCodiceSoggetto():null,
 				 uidProvvedimento,
 				 (annoProvvedimento != null && annoProvvedimento != 0) ? annoProvvedimento.toString() :null,
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
				 importoDaPagareZero, 
				 importoDaPagareOIncassareMaggioreDiZero,
				 rilevatiIvaConRegistrazioneONonRilevantiIva,
				 collegatoALiquidazioneInStatoValidoConProvvDefinitivoConDispPagareMaggioreDiSubdocImporto,
				 siacDAttoAmmStatoEnums,
				 associatoAdOrdinativo,
				 conFlagConvalidaManuale,
				 listProvCassaUid,
				 toPageable(parametriPaginazione)		 
				);
		
		return toListaPaginata(lista, SubdocumentoSpesa.class, BilMapId.SiacTSubdoc_SubdocumentoSpesa);
		
	}
	
	/**
	 * Ricerca subdocumenti spesa.
	 *
	 * @param ente the ente
	 * @param uidElenco the uid elenco
	 * @param annoElenco the anno elenco
	 * @param numeroElenco the numero elenco
	 * @param numeroElencoDa the numero elenco da
	 * @param numeroElencoA the numero elenco a
	 * @param annoProvvisorio the anno provvisorio
	 * @param numeroProvvisorio the numero provvisorio
	 * @param dataProvvisorio the data provvisorio
	 * @param tipoDocumento the tipo documento
	 * @param annoDocumento the anno documento
	 * @param numeroDocumento the numero documento
	 * @param dataEmissioneDocumento the data emissione documento
	 * @param numeroQuota the numero quota
	 * @param numeroMovimento the numero movimento
	 * @param annoMovimento the anno movimento
	 * @param soggetto the soggetto
	 * @param uidProvvedimento the uid provvedimento
	 * @param annoProvvedimento the anno provvedimento
	 * @param numeroProvvedimento the numero provvedimento
	 * @param tipoAtto the tipo atto
	 * @param struttAmmContabile the strutt amm contabile
	 * @param annoCapitolo the anno capitolo
	 * @param numeroCapitolo the numero capitolo
	 * @param numeroArticolo the numero articolo
	 * @param numeroUEB the numero ueb
	 * @param statiOperativoDocumento the stati operativo documento
	 * @param collegatoAMovimentoDelloStessoBilancio the collegato a movimento dello stesso bilancio
	 * @param associatoAProvvedimentoOAdElenco the associato a provvedimento o ad elenco
	 * @param importoDaPagareZero the importo da pagare zero
	 * @param importoDaPagareOIncassareMaggioreDiZero the importo da pagare o incassare maggiore di zero
	 * @param rilevatiIvaConRegistrazioneONonRilevantiIva the rilevati iva con registrazione o non rilevanti iva
	 * @param collegatoALiquidazioneInStatoValidoConProvvDefinitivoConDispPagareMaggioreDiSubdocImporto the collegato a liquidazione in stato valido con provv definitivo con disp pagare maggiore di subdoc importo
	 * @param statiOperativiAtti the stati operativi atti
	 * @param associatoAdOrdinativo the associato ad ordinativo
	 * @param parametriPaginazione the parametri paginazione
	 * @return the lista paginata
	 */
	public ListaPaginata<SubdocumentoSpesa> ricercaSubdocumentiSpesaModelDetail(
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
			Boolean importoDaPagareZero, 
			Boolean importoDaPagareOIncassareMaggioreDiZero,
			Boolean rilevatiIvaConRegistrazioneONonRilevantiIva,
			Boolean collegatoALiquidazioneInStatoValidoConProvvDefinitivoConDispPagareMaggioreDiSubdocImporto,
			Collection<StatoOperativoAtti> statiOperativiAtti,
			Boolean associatoAdOrdinativo,
			Boolean conFlagConvalidaManuale,
			List<Integer> listProvCassaUid,
			ParametriPaginazione parametriPaginazione,
			SubdocumentoSpesaModelDetail... modelDetails) {
			
		Set<SiacDAttoAmmStatoEnum> siacDAttoAmmStatoEnums = SiacDAttoAmmStatoEnum.byStatiOperativi(statiOperativiAtti);
		
		Page<SiacTSubdoc> lista = subdocumentoDao.ricercaSinteticaSubdocumenti(
				ente.getUid(),
				mapToUidIfNotZero(bilancio),
				EnumSet.of(SiacDDocFamTipoEnum.Spesa, SiacDDocFamTipoEnum.IvaSpesa),
				uidElenco,
				annoElenco,
				numeroElenco,
				numeroElencoDa,
				numeroElencoA,
				annoProvvisorio,
				numeroProvvisorio != null ? new BigDecimal(numeroProvvisorio.toString()) : null,
				dataProvvisorio,
				SiacDProvCassaTipoEnum.Spesa,
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
				importoDaPagareZero,
				importoDaPagareOIncassareMaggioreDiZero,
				rilevatiIvaConRegistrazioneONonRilevantiIva,
				collegatoALiquidazioneInStatoValidoConProvvDefinitivoConDispPagareMaggioreDiSubdocImporto,
				siacDAttoAmmStatoEnums,
				associatoAdOrdinativo,
				conFlagConvalidaManuale,
				listProvCassaUid,
				toPageable(parametriPaginazione));
		
		return toListaPaginata(lista, SubdocumentoSpesa.class, BilMapId.SiacTSubdoc_SubdocumentoSpesa_ModelDetail, modelDetails);
	}
	
	
	/**
	 * Ricerca subdocumenti spesa totale importi.
	 *
	 * @param ente the ente
	 * @param uidElenco the uid elenco
	 * @param annoElenco the anno elenco
	 * @param numeroElenco the numero elenco
	 * @param numeroElencoDa the numero elenco da
	 * @param numeroElencoA the numero elenco a
	 * @param annoProvvisorio the anno provvisorio
	 * @param numeroProvvisorio the numero provvisorio
	 * @param dataProvvisorio the data provvisorio
	 * @param tipoDocumento the tipo documento
	 * @param annoDocumento the anno documento
	 * @param numeroDocumento the numero documento
	 * @param dataEmissioneDocumento the data emissione documento
	 * @param numeroQuota the numero quota
	 * @param numeroMovimento the numero movimento
	 * @param annoMovimento the anno movimento
	 * @param soggetto the soggetto
	 * @param uidProvvedimento the uid provvedimento
	 * @param annoProvvedimento the anno provvedimento
	 * @param numeroProvvedimento the numero provvedimento
	 * @param tipoAtto the tipo atto
	 * @param struttAmmContabile the strutt amm contabile
	 * @param annoCapitolo the anno capitolo
	 * @param numeroCapitolo the numero capitolo
	 * @param numeroArticolo the numero articolo
	 * @param numeroUEB the numero ueb
	 * @param statiOperativoDocumento the stati operativo documento
	 * @param collegatoAMovimentoDelloStessoBilancio the collegato a movimento dello stesso bilancio
	 * @param associatoAProvvedimentoOAdElenco the associato a provvedimento o ad elenco
	 * @param importoDaPagareZero the importo da pagare zero
	 * @param importoDaPagareOIncassareMaggioreDiZero the importo da pagare o incassare maggiore di zero
	 * @param rilevatiIvaConRegistrazioneONonRilevantiIva the rilevati iva con registrazione o non rilevanti iva
	 * @param collegatoALiquidazioneInStatoValidoConProvvDefinitivoConDispPagareMaggioreDiSubdocImporto the collegato a liquidazione in stato valido con provv definitivo con disp pagare maggiore di subdoc importo
	 * @param statiOperativiAtti the stati operativi atti
	 * @param associatoAdOrdinativo the associato ad ordinativo
	 * @param parametriPaginazione the parametri paginazione
	 * @return the big decimal
	 */
	public BigDecimal ricercaSubdocumentiSpesaTotaleImporti(Ente ente, 
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
			Boolean importoDaPagareZero, 
			Boolean importoDaPagareOIncassareMaggioreDiZero,
			Boolean rilevatiIvaConRegistrazioneONonRilevantiIva,
			Boolean collegatoALiquidazioneInStatoValidoConProvvDefinitivoConDispPagareMaggioreDiSubdocImporto,
			Collection<StatoOperativoAtti> statiOperativiAtti,
			Boolean associatoAdOrdinativo,
			Boolean conFlagConvalidaManuale,
			List<Integer> listProvCassaUid
			) {
		
		Set<SiacDAttoAmmStatoEnum> siacDAttoAmmStatoEnums = SiacDAttoAmmStatoEnum.byStatiOperativi(statiOperativiAtti);
	
			BigDecimal totale = subdocumentoDao.ricercaSinteticaSubdocumentiTotaleImporti(ente.getUid(), bilancio!=null?bilancio.getUid():null,
					EnumSet.of(SiacDDocFamTipoEnum.Spesa, SiacDDocFamTipoEnum.IvaSpesa),
					 uidElenco,
					 annoElenco,
					 numeroElenco,
					 numeroElencoDa,
					 numeroElencoA,
					 annoProvvisorio,
					 numeroProvvisorio != null ? new BigDecimal(numeroProvvisorio.toString()) : null,
					 dataProvvisorio,
					 SiacDProvCassaTipoEnum.Spesa,
					 tipoDocumento!=null?tipoDocumento.getUid():null,
					 annoDocumento,
					 numeroDocumento,
					 dataEmissioneDocumento,
					 numeroQuota,	
					 numeroMovimento,
					 annoMovimento,
					 soggetto!=null?soggetto.getCodiceSoggetto():null,
					 uidProvvedimento,
					 (annoProvvedimento != null && annoProvvedimento != 0) ? annoProvvedimento.toString() :null,
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
					 importoDaPagareZero, 
					 importoDaPagareOIncassareMaggioreDiZero,
					 rilevatiIvaConRegistrazioneONonRilevantiIva,
					 collegatoALiquidazioneInStatoValidoConProvvDefinitivoConDispPagareMaggioreDiSubdocImporto,
					 siacDAttoAmmStatoEnums,
					 associatoAdOrdinativo,
					 conFlagConvalidaManuale,
					 listProvCassaUid);
			return totale;
	
	}
	
	
	public ListaPaginata<SubdocumentoSpesa> ricercaSubdocumentiSpesaPerProvvisorio(TipoDocumento tipoDocumento, Integer annoDocumento, String numeroDocumento, Date dataEmissioneDocumento,
			Integer numeroQuota, BigDecimal numeroMovimento,Integer annoMovimento, Soggetto soggetto, Integer annoElenco, Integer numeroElenco, ParametriPaginazione parametriPaginazione) {
		
		Page<SiacTSubdoc> lista = subdocumentoDao.ricercaSinteticaSubdocumentiPerProvvisorio(
				ente.getUid(),
				EnumSet.of(SiacDDocFamTipoEnum.Spesa, SiacDDocFamTipoEnum.IvaSpesa),
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
				false,
			    toPageable(parametriPaginazione)		
				);

		return toListaPaginata(lista, SubdocumentoSpesa.class, BilMapId.SiacTSubdoc_SubdocumentoSpesa_Allegato);
	}

//	public BigDecimal ricercaSubdocumentiSpesaPerProvvisorioTotaleImporti(TipoDocumento tipoDocumento, Integer annoDocumento, String numeroDocumento, Date dataEmissioneDocumento,
//			Integer numeroQuota, BigDecimal numeroMovimento, Integer annoMovimento, Soggetto soggetto, Integer annoElenco, Integer numeroElenco, ParametriPaginazione parametriPaginazione) {
//		
//		BigDecimal totale = subdocumentoDao.ricercaSinteticaSubdocumentiPerProvvisorioTotaleImporti(
//				ente.getUid(),
//				EnumSet.of(SiacDDocFamTipoEnum.Spesa, SiacDDocFamTipoEnum.IvaSpesa),
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
	
	public BigDecimal ricercaSubdocumentiSpesaPerProvvisorioTotaleImporti(Collection<SubdocumentoSpesa> listaSubdocumenti) {
		if(listaSubdocumenti == null || listaSubdocumenti.isEmpty()) {
			return BigDecimal.ZERO;
		}
		
		Collection<Integer> uids = new ArrayList<Integer>();
		for(SubdocumentoSpesa ss : listaSubdocumenti) {
			uids.add(ss.getUid());
		}
		
		return subdocumentoDao.ricercaSinteticaSubdocumentiPerProvvisorioTotaleImporti(uids);
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
		log.debug(methodName, loginOperazione);
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
			//siacTDocNum.setLoginOperazione(loginOperazione);
			//La numerazione parte da 1		
			siacTDocNum.setSubdocNumero(1); 
		}
		
		siacTDocNum.setLoginOperazione(loginOperazione);	
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
	public void aggiornaImportoDaDedurre(SubdocumentoSpesa subdocumento) {
		SiacTSubdoc siacTSubdoc = subdocumentoDao.findById(subdocumento.getUid());		
		siacTSubdoc.setSubdocImportoDaDedurre(subdocumento.getImportoDaDedurre());
		subdocumentoDao.flush();
	}	
	
	/**
	 * Aggiorna numero registrazione iva.
	 *
	 * @param subdocumento the subdocumento
	 */
	public void aggiornaNumeroRegistrazioneIva(SubdocumentoSpesa subdocumento) {
		SiacTSubdoc siacTSubdoc = subdocumentoDao.findById(subdocumento.getUid());		
		siacTSubdoc.setSubdocNregIva(subdocumento.getNumeroRegistrazioneIVA());
		subdocumentoDao.flush();
	}
	
	/**
	 * Elimina il legame tra liquidazione e quota associata.
	 * 
	 * @param liquidazione the liquidazione
	 * @param dataCancellazione the data cancellazione
	 */
	public void eliminaLegameLiquidazioneQuota(Liquidazione liquidazione, final Date dataCancellazione) {
		List<SiacRSubdocLiquidazione> siacRSubdocLiquidaziones = siacRSubdocLiquidazioneRepository.findByLiqId(liquidazione.getUid());
		for(SiacRSubdocLiquidazione srsl : siacRSubdocLiquidaziones) {
			srsl.setDataCancellazioneIfNotSet(dataCancellazione);
		}
		siacRSubdocLiquidazioneRepository.flush();
	}
	
	
	/**
	 * Elimina il legame tra liquidazione e quota associata.
	 *
	 * @param liquidazione the liquidazione
	 * @param subdocumentoSpesa the subdocumento spesa
	 * @param dataCancellazione the data cancellazione
	 */
	public void eliminaLegameLiquidazioneQuota(Liquidazione liquidazione, SubdocumentoSpesa subdocumentoSpesa, final Date dataCancellazione) {
		List<SiacRSubdocLiquidazione> siacRSubdocLiquidaziones = siacRSubdocLiquidazioneRepository.findByLiqIdAndSubdocId(liquidazione.getUid(), subdocumentoSpesa.getUid());
		for(SiacRSubdocLiquidazione srsl : siacRSubdocLiquidaziones) {
			srsl.setDataCancellazioneIfNotSet(dataCancellazione);
			srsl.setDataFineValidita(dataCancellazione);
		}
		
		siacRSubdocLiquidazioneRepository.flush();
	}
	
	/**
	 * Carica la liquidazione.
	 *
	 * @param subdocumento the subdocumento
	 * @return the liquidazione
	 */
	public Liquidazione findLiquidazioneAssociataAlSubdocumento(SubdocumentoSpesa subdocumento) {
		final String methodName = "findLiquidazioneAssociataAlSubdocumento";
		SiacTSubdoc siacTSubdoc = siacTSubdocRepository.findOne(subdocumento.getUid());
		if(siacTSubdoc == null) {
			throw new IllegalArgumentException("Il subdocumento con uid:"+ subdocumento.getUid() + " non esiste.");
		}
		SiacTLiquidazione siacTLiquidazione = siacTLiquidazioneRepository.findNonAnnullataBySubdocId(subdocumento.getUid());
		Liquidazione liquidazione = mapNotNull(siacTLiquidazione, Liquidazione.class, BilMapId.SiacTLiquidazione_Liquidazione);
		log.info(methodName, (liquidazione != null ? "Trovata liquidazione con uid: "+ liquidazione.getUid()  : "Nessuna liquidazione trovata " )+ 
						" associata al subdoc con uid: " + subdocumento.getUid());
		return liquidazione;
	}
	
	/**
	 * Aggiorna gli attributi forniti per il subdocumento.
	 * 
	 * @param subdocumentoSpesa the subdocumento spesa
	 * @param attributi the attributi
	 */
	public void aggiornaAttributiSubdocumento(SubdocumentoSpesa subdocumentoSpesa, Map<TipologiaAttributo, Object> attributi) {
		final String methodName = "aggiornaAttributiSubdocumento";
		final Integer uid = subdocumentoSpesa.getUid();
		final Date now = new Date();
		// Per ogni attributo, ricerco il vecchio collegamento. Se non e' presente, allora inserisco ex-novo
		for(Map.Entry<TipologiaAttributo, Object> entry : attributi.entrySet()) {
			TipologiaAttributo tipologiaAttributo = entry.getKey();
			Object value = entry.getValue();
			
			SiacTAttrEnum siacTAttrEnum = SiacTAttrEnum.byTipologiaAttributo(tipologiaAttributo);
			SiacRSubdocAttr siacRSubdocAttr = siacTSubdocRepository.findAttributeBySiacTSubdocAndSiacTAttr(uid, siacTAttrEnum.getCodice());
			if(siacRSubdocAttr != null) {
				log.debug(methodName, "Attributo di tipo " + tipologiaAttributo.name() + " presente per il subdocumento con uid " + uid
						+ " con valore " + siacRSubdocAttrToValue(siacRSubdocAttr) + ". Aggiornamento con valore " + entry.getValue());
				siacRSubdocAttr.setDataCancellazioneIfNotSet(now);
			} else {
				log.debug(methodName, "Attributo di tipo " + tipologiaAttributo.name() + " non presente per il subdocumento con uid " + uid
						+ ". Necessario inserimento con valore " + entry.getValue());
				// valueToSiacRSubdocAttr(siacRSubdocAttr, siacTAttrEnum, value)
			}
			createSiacRSubdocAttr(uid, siacTAttrEnum, value, now);
			siacTSubdocRepository.flush();
		}
	}
	
	/**
	 * Computes the value of a given siacRSubdocAttr.
	 * 
	 * @param siacRSubdocAttr the siac r subdoc attr
	 * 
	 * @return the value
	 */
	private String siacRSubdocAttrToValue(SiacRSubdocAttr siacRSubdocAttr) {
		if(siacRSubdocAttr == null) {
			return null;
		}
		
		return "T:" + siacRSubdocAttr.getTesto() + "-N:" +siacRSubdocAttr.getNumerico()
				+ "-P:" + siacRSubdocAttr.getPercentuale() + "-B:" + siacRSubdocAttr.getBoolean_();
	}
	
	/**
	 * Sets the value to a given siacRSubdocAttr.
	 * 
	 * @param siacRSubdocAttr the siac r subdoc attr
	 * @param siacTAttrEnum the siac r attr enum
	 * @param value the value
	 * 
	 * @return the value
	 */
	private void valueToSiacRSubdocAttr(SiacRSubdocAttr siacRSubdocAttr, SiacTAttrEnum siacTAttrEnum, Object value) {
		final String methodName = "valueToSiacRSubdocAttr";
		if(siacRSubdocAttr == null || siacTAttrEnum == null) {
			log.debug(methodName, "No siacRSubdocAttr to set");
			return;
		}
		
		if (siacTAttrEnum.getSiacTAttrHandlerType() != null) {
			SiacTAttrHandler siacTAttrHandler = CoreUtil.instantiateNewClass(siacTAttrEnum.getSiacTAttrHandlerType());
			
			value = siacTAttrHandler.handleAttrValue(value);
		}
		
		if(Boolean.class.equals(siacTAttrEnum.getFieldType())){
			final BooleanToStringConverter btsc = new BooleanToStringConverter();
			String val = btsc.convertTo((Boolean) value);
			siacRSubdocAttr.setBoolean_(val);
			log.debug(methodName, "Set value " + val + " to boolean, by fieldType " + siacTAttrEnum.getFieldType());
		} else if(String.class.equals(siacTAttrEnum.getFieldType())){
			String val = value instanceof String ? (String) value : null;
			siacRSubdocAttr.setTesto(val);
			log.debug(methodName, "Set value " + val + " to testo, by fieldType " + siacTAttrEnum.getFieldType());
		} else if(Date.class.equals(siacTAttrEnum.getFieldType())){
			String val = value instanceof Date ? getDateFormat().format((Date) value) : null;
			siacRSubdocAttr.setTesto(val);
			log.debug(methodName, "Set value " + val + " to testo, by fieldType " + siacTAttrEnum.getFieldType());
		} else if(BigDecimal.class.equals(siacTAttrEnum.getFieldType())){
			BigDecimal val = value instanceof BigDecimal ? (BigDecimal) value : null;
			siacRSubdocAttr.setNumerico(val);
			log.debug(methodName, "Set value " + val + " to numerico, by fieldType " + siacTAttrEnum.getFieldType());
		} else if(Integer.class.equals(siacTAttrEnum.getFieldType())){
			BigDecimal val = value instanceof Integer ? new BigDecimal(value.toString()) : null;
			siacRSubdocAttr.setNumerico(val);
			log.debug(methodName, "Set value " + val + " to numerico, by fieldType " + siacTAttrEnum.getFieldType());
		}
	}
	
	/**
	 * Crea un nuovo siacRSubdocAttr.
	 * 
	 * @param subdocId the subdoc id
	 * @param siacTAttrEnum the siac t attr enum
	 * @param value the value
	 * @param dataInizioValidita the data inizio validita
	 */
	private void createSiacRSubdocAttr(Integer subdocId, SiacTAttrEnum siacTAttrEnum, Object value, Date dataInizioValidita) {
		SiacRSubdocAttr siacRSubdocAttr = new SiacRSubdocAttr();
		
		// SiacTSubdoc
		SiacTSubdoc siacTSubdoc = new SiacTSubdoc();
		siacTSubdoc.setUid(subdocId);
		siacRSubdocAttr.setSiacTSubdoc(siacTSubdoc);
		
		// SiacTAttr
		SiacTAttr siacTAttr = eef.getEntity(siacTAttrEnum, ente.getUid());
		siacRSubdocAttr.setSiacTAttr(siacTAttr);
		
		// SiacTEnteProprietario
		SiacTEnteProprietario siacTEnteProprietario = new SiacTEnteProprietario();
		siacTEnteProprietario.setUid(ente.getUid());
		siacRSubdocAttr.setSiacTEnteProprietario(siacTEnteProprietario);
		
		// Impostazione del valore
		valueToSiacRSubdocAttr(siacRSubdocAttr, siacTAttrEnum, value);
		
		siacRSubdocAttr.setDataModificaInserimento(dataInizioValidita);
		siacRSubdocAttr.setLoginOperazione(loginOperazione);
		
		// Salvataggio
		siacRSubdocAttrRepository.save(siacRSubdocAttr);
	}
	
	/**
	 * Gets the date format.
	 *
	 * @return the dateFormat
	 */
	private DateFormat getDateFormat() {
		if(dateFormat == null) {
			dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ITALY);
		}
		return dateFormat;
	}
	

	/**
	 * Aggiorna la liquidazione collegata al subdocumento 
	 * impostando il tipoConvalida uguale a quello del subdocumento 
	 * Inoltre aggiorna lo stato della liquidazine a in base al parametro passato, di default lo imposta a valido.
	 *
	 * @param subdoc the subdoc
	 * @param provvisorio 
	 * @return la liquidazione aggiornata (solo uid).
	 */
	private Liquidazione aggiornaLiquidazioneTipoConvalida(Subdocumento<?, ?> subdoc, SiacDLiquidazioneStatoEnum statoOperativoLiq) {
		Date now = new Date();
		SiacTSubdoc siacTSubdoc = siacTSubdocRepository.findOne(subdoc.getUid());
		if(siacTSubdoc == null) {
			throw new IllegalArgumentException("Il subdocumento con uid:"+ subdoc.getUid() + " non esiste.");
		}
		if(siacTSubdoc.getSiacRSubdocLiquidaziones()==null){
			throw new IllegalArgumentException("Il subdocumento con uid:"+ subdoc.getUid() + " non e' collegato ad una liquidazione.");
		}
		Liquidazione liquidazione = null;
		
		List<SiacTLiquidazione> siacTLiquidaziones = siacTLiquidazioneRepository.findNonAnnullateBySubdocId(subdoc.getUid());
		
		for(SiacTLiquidazione siacTLiquidazione : siacTLiquidaziones){
			//Aggiorno TipoConvalida sulla liquidazione.
			BooleanToStringManualeAutomaticoConverter btsmac = new BooleanToStringManualeAutomaticoConverter();
			String flag = btsmac.convertTo(subdoc.getFlagConvalidaManuale());
			siacTLiquidazione.setLiqConvalidaManuale(flag);
			
			if(statoOperativoLiq!=null){
				//Aggiorno lo Stato della liquidazione
				if(siacTLiquidazione.getSiacRLiquidazioneStatos()==null){
					siacTLiquidazione.setSiacRLiquidazioneStatos(new ArrayList<SiacRLiquidazioneStato>());
				}
				for(SiacRLiquidazioneStato rs : siacTLiquidazione.getSiacRLiquidazioneStatos()){
					rs.setDataCancellazioneIfNotSet(now);
					rs.setDataFineValiditaIfNotSet(now);
				}		 
				SiacRLiquidazioneStato siacRLiquidazioneStato = new SiacRLiquidazioneStato();
				SiacDLiquidazioneStato siacDLiquidazioneStato = eef.getEntity(statoOperativoLiq, ente.getUid());
				siacRLiquidazioneStato.setSiacDLiquidazioneStato(siacDLiquidazioneStato);
				siacRLiquidazioneStato.setLoginOperazione(loginOperazione);
				siacRLiquidazioneStato.setSiacTEnteProprietario(siacTLiquidazione.getSiacTEnteProprietario());
				siacRLiquidazioneStato.setDataModificaInserimento(now);
				siacTLiquidazione.addSiacRLiquidazioneStato(siacRLiquidazioneStato);
			}
			
			// Devo richiamarlo in quanto non ho il cascade fino a questo livello
			siacTLiquidazioneRepository.saveAndFlush(siacTLiquidazione);
			
			liquidazione = new Liquidazione();
			liquidazione.setUid(siacTLiquidazione.getUid());
		}
		
		
		
		
//		for(SiacRSubdocLiquidazione r : siacTSubdoc.getSiacRSubdocLiquidaziones()){
//			if(r.getDataCancellazione() != null || (r.getDataFineValidita() != null && r.getDataFineValidita().before(now))){
//				continue;
//			}
//			SiacTLiquidazione siacTLiquidazione = siacTLiquidazioneRepository.findOne(r.getSiacTLiquidazione().getUid());
//			
//			//Aggiorno TipoConvalida sulla liquidazione.
//			BooleanToStringManualeAutomaticoConverter btsmac = new BooleanToStringManualeAutomaticoConverter();
//			String flag = btsmac.convertTo(subdoc.getFlagConvalidaManuale());
//			siacTLiquidazione.setLiqConvalidaManuale(flag);
//			
//			if(statoOperativoLiq!=null){
//				//Aggiorno lo Stato della liquidazione
//				if(siacTLiquidazione.getSiacRLiquidazioneStatos()==null){
//					siacTLiquidazione.setSiacRLiquidazioneStatos(new ArrayList<SiacRLiquidazioneStato>());
//				}
//				for(SiacRLiquidazioneStato rs : siacTLiquidazione.getSiacRLiquidazioneStatos()){
//					rs.setDataCancellazioneIfNotSet(now);
//					rs.setDataFineValiditaIfNotSet(now);
//				}		 
//				SiacRLiquidazioneStato siacRLiquidazioneStato = new SiacRLiquidazioneStato();
//				SiacDLiquidazioneStato siacDLiquidazioneStato = eef.getEntity(statoOperativoLiq, ente.getUid());
//				siacRLiquidazioneStato.setSiacDLiquidazioneStato(siacDLiquidazioneStato);
//				siacRLiquidazioneStato.setLoginOperazione(loginOperazione);
//				siacRLiquidazioneStato.setSiacTEnteProprietario(siacTLiquidazione.getSiacTEnteProprietario());
//				siacRLiquidazioneStato.setDataModificaInserimento(now);
//				siacTLiquidazione.addSiacRLiquidazioneStato(siacRLiquidazioneStato);
//			}
//			
//			// Devo richiamarlo in quanto non ho il cascade fino a questo livello
//			siacTLiquidazioneRepository.saveAndFlush(siacTLiquidazione);
//			
//			liquidazione = new Liquidazione();
//			liquidazione.setUid(siacTLiquidazione.getUid());
//		}
		siacTSubdocRepository.flush();
		return liquidazione;
	}

	/**
	 * Aggiorna la liquidazione collegata al subdocumento 
	 * impostando il tipoConvalida uguale a quello del subdocumento 
	 * Inoltre aggiorna lo stato della liquidazine a Valido.
	 *
	 * @param subdoc the subdoc
	 * @param provvisorio 
	 * @return la liquidazione aggiornata (solo uid).
	 */
	public Liquidazione aggiornaLiquidazioneTipoConvalida(Subdocumento<?, ?> subdoc){
		return aggiornaLiquidazioneTipoConvalida(subdoc, SiacDLiquidazioneStatoEnum.Valido);
	}
	/**
	 * Aggiorna la liquidazione collegata al subdocumento 
	 * impostando il tipoConvalida uguale a quello del subdocumento 
	 * Inoltre aggiorna lo stato della liquidazine a Provvisorio.
	 * @param subdoc the subdoc
	 * @return la liquidazione aggiornata (solo uid).
	 *
	 */
	public Liquidazione aggiornaLiquidazioneTipoConvalidaAStatoProvvisorio(Subdocumento<?, ?> subdoc){
		return aggiornaLiquidazioneTipoConvalida(subdoc, SiacDLiquidazioneStatoEnum.Provvisorio);
	}
	
	public Liquidazione aggiornaLiquidazioneTipoConvalidaAStato(Subdocumento<?, ?> subdoc, StatoOperativoLiquidazione statoOperativoLiquidazione){
		return aggiornaLiquidazioneTipoConvalida(subdoc, SiacDLiquidazioneStatoEnum.byStatoOperativoLiquidazioneEvenNull(statoOperativoLiquidazione));
	}
	
	/**
	 * Conta documenti entrata padre.
	 *
	 * @param subdoc the subdoc
	 * @return the long
	 */
	public Long contaDocumentiEntrataPadre(SubdocumentoSpesa subdoc) {
		Long count = siacTSubdocRepository.countSiacTDocPadre(subdoc.getUid(), SiacDRelazTipoEnum.Subdocumento.getCodice(),
				Arrays.asList(SiacDDocFamTipoEnum.Entrata.getCodice(), SiacDDocFamTipoEnum.IvaEntrata.getCodice()));
		return count;
	}

	
	/**
	 * Find uid subdocumenti by uid documento.
	 *
	 * @param docId the doc id
	 * @return the list
	 */
	public List<Integer> findUidSubdocumentiByUidDocumento(Integer docId) {
		List<SiacTSubdoc> siacTSubdocs = siacTSubdocRepository.findSiacTSubdocByDocId(docId);
		List<Integer> result = new ArrayList<Integer>();
		
		for(SiacTSubdoc siacTSubdoc : siacTSubdocs) {
			result.add(siacTSubdoc.getUid());
		}
		return result;
	}

	/**
	 * Checks if is collegata a nota credito.
	 *
	 * @param subdoc the subdoc
	 * @return the boolean
	 */
	public Boolean isCollegataANotaCredito(SubdocumentoSpesa subdoc) {
		Long count = siacTSubdocRepository.countSiacTDocPadre(subdoc.getUid(), SiacDRelazTipoEnum.NotaCredito.getCodice(), SiacDDocFamTipoEnum.getCodices());
		return Boolean.valueOf(count.longValue() > 0L);
	}

	/**
	 * Cancella quote doc annullato.
	 *
	 * @param uidDocumento the uid documento
	 */
	public void cancellaQuoteDocAnnullato(Integer uidDocumento) {
		List<SiacTSubdoc> siacTSubdocs= siacTSubdocRepository.findSiacTSubdocByDocId(uidDocumento);
		Date now = new Date();
		if(siacTSubdocs != null){
			for(SiacTSubdoc s : siacTSubdocs){
				if(s.getDataCancellazione() == null){
					s.setDataCancellazione(now);
				}
			}
		}
		siacTSubdocRepository.flush();
		
	}

	/**
	 * Find importo subdocumento spesa by id.
	 *
	 * @param uid the uid
	 * @return the big decimal
	 */
	public BigDecimal findImportoSubdocumentoSpesaById(Integer uid) {
//		final String methodName = "findImportoSubdocumentoSpesaById";		
//		log.debug(methodName, "uid: "+ uid);
//		SiacTSubdoc siacTSuboc = subdocumentoDao.findById(uid);
//		if(siacTSuboc == null) {
//			log.debug(methodName, "Returning null. Impossibile trovare il SubdocumentoSpesa con id: " + uid);
//			return null;
//		}
//		
//		log.debug(methodName, "Returning "+siacTSuboc.getSubdocImporto()+". siacTSubdoc trovata con uid: " + siacTSuboc.getUid());
//		return siacTSuboc.getSubdocImporto();
		return siacTSubdocRepository.findImportoBySubdocId(uid);
		
	}
	
	/**
	 * Find importo da pagare subdocumento spesa by id.
	 *
	 * @param uid the uid
	 * @return the big decimal
	 */
	public BigDecimal findImportoDaPagareSubdocumentoSpesaById(Integer uid) {
//		final String methodName = "findImportoDaPagareSubdocumentoSpesaById";		
//		log.debug(methodName, "uid: "+ uid);
//		SiacTSubdoc siacTSuboc = subdocumentoDao.findById(uid);
//		if(siacTSuboc == null) {
//			log.debug(methodName, "Returning null. Impossibile trovare il SubdocumentoSpesa con id: " + uid);
//			return null;
//		}
//		BigDecimal importo = siacTSuboc.getSubdocImporto() != null ? siacTSuboc.getSubdocImporto() : BigDecimal.ZERO;
//		BigDecimal importoDaDedurre = siacTSuboc.getSubdocImportoDaDedurre() != null ? siacTSuboc.getSubdocImportoDaDedurre() : BigDecimal.ZERO;
//		return importo.subtract(importoDaDedurre);
		
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
	public BigDecimal findImportoDaDedurreSubdocumentoSpesaByIdTxRequiresNew(Integer uid) {
		return findImportoDaDedurreSubdocumentoSpesaById(uid);
	}
	
	/**
	 * Find importo da dedurre subdocumento spesa by id.
	 *
	 * @param uid the uid
	 * @return the big decimal
	 */
	public BigDecimal findImportoDaDedurreSubdocumentoSpesaById(Integer uid) {
//		final String methodName = "findImportoSubdocumentoSpesaById";		
//		log.debug(methodName, "uid: "+ uid);
//		SiacTSubdoc siacTSuboc = subdocumentoDao.findById(uid);
//		if(siacTSuboc == null) {
//			log.debug(methodName, "Returning null. Impossibile trovare il SubdocumentoSpesa con id: " + uid);
//			return null;
//		}
//		
//		log.debug(methodName, "Returning "+siacTSuboc.getSubdocImportoDaDedurre()+". siacTSubdoc trovata con uid: " + siacTSuboc.getUid());
//		return siacTSuboc.getSubdocImportoDaDedurre();
		
		return siacTSubdocRepository.findImportoDaDedurreBySubdocId(uid);
		
	}



	/**
	 * Find uid impegno subdocumento spesa by id.
	 *
	 * @param uid the uid
	 * @return the integer
	 */
	public Integer findUidImpegnoSubdocumentoSpesaById(int uid) {
		final String methodName = "findUidImpegnoSubdocumentoSpesaById";		
		log.debug(methodName, "uid: "+ uid);
		SiacTMovgestT siacTMovgestT = siacTSubdocRepository.findMovgestTSByIdSubdoc(uid);
		if(siacTMovgestT == null) {
			log.debug(methodName, "Returning null. Impossibile trovare il movimento gestione legato al SubdocumentoSpesa con id: " + uid);
			return null;
		}
		//restituisco l'uid solo se il subdoc è legato ad un impegno, altrimenti null
		if(siacTMovgestT.getSiacTMovgestIdPadre() == null){
			return siacTMovgestT.getSiacTMovgest().getMovgestId();
		}else{
			return null;
		}
	}

	
	/**
	 * Find uid sub impegno subdocumento spesa by id.
	 *
	 * @param uid the uid
	 * @return the integer
	 */
	public Integer findUidSubImpegnoSubdocumentoSpesaById(int uid) {
		final String methodName = "findUidSubImpegnoSubdocumentoSpesaById";		
		log.debug(methodName, "uid: "+ uid);
		SiacTMovgestT siacTMovgestT = siacTSubdocRepository.findMovgestTSByIdSubdoc(uid);
		if(siacTMovgestT == null) {
			log.debug(methodName, "Returning null. Impossibile trovare il movimento gestione legato al SubdocumentoSpesa con id: " + uid);
			return null;
		}
		//restituisco l'uid solo se il subdoc è legato ad un subimpegno, altrimenti null
		if(siacTMovgestT.getSiacTMovgestIdPadre() != null){
			return siacTMovgestT.getUid();
		}else{
			return null;
		}
	}
	
	/**
	 * 
	 * @param subdoc
	 * @return
	 */
	public AttoAmministrativo findProvvedimentoLegatoAllaQuota(SubdocumentoSpesa subdoc) {
		SiacTAttoAmm siacTAttoAmm = siacTAttoAmmRepository.findAttoAmmBySubdocId(subdoc.getUid());
		if(siacTAttoAmm != null){
			AttoAmministrativo attoAmministrativo = new AttoAmministrativo();
			attoAmministrativo.setUid(siacTAttoAmm.getUid());
			return attoAmministrativo;
		}
		return null;
	}

	/**
	 * Effettua un flush ed un clear dell'EntityManager.
	 * 
	 */
	public void flushAndClear() {
		subdocumentoDao.flushAndClear();
	}

	public List<SubdocumentoSpesa> findSubdocumentiSpesaByIdDocumentoAfterFlushAndClear(Integer uid) {
		subdocumentoDao.flushAndClear();
		return findSubdocumentiSpesaByIdDocumento(uid);
	}


	public Long countQuoteDocumentoNonEmesse(DocumentoSpesa documento) {
		return siacTSubdocRepository.countQuoteDocumentoNonEmesse(documento.getUid());
	}

	public void aggiornaImportoSubdocumentoSpesa(int uid, BigDecimal importoResiduo, BigDecimal importoDaDedurre) {
		SiacTSubdoc siacTSubdoc = subdocumentoDao.findById(uid);		
		siacTSubdoc.setSubdocImporto(importoResiduo);
		siacTSubdoc.setSubdocImportoDaDedurre(importoDaDedurre);
		subdocumentoDao.flush();
	}

	/**
	 * Come {@link #computeKeySubdocImportoImpegnoFlagRilevanteIva(int)}
	 * ma esegue la query in un nuova transazione.
	 * 
	 * @param uid
	 * @return la chiave restituita da {@link #computeKeySubdocImportoImpegnoFlagRilevanteIva(int)}
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public String computeKeySubdocImportoImpegnoFlagRilevanteIvaTxRequiresNew(int uid) {
		return computeKeySubdocImportoImpegnoFlagRilevanteIva(uid);
	}

	/**
	 * Calcola una chiave per il subdocumento dato da i seguenti campi: Importo, importo da dedurre, impegno/subimpegno, FlagRilevanteIVA
	 * 
	 * @param uid
	 * @return Chiavi di esempio restituite: "123_1000.00_10.0_1_T_N" (Quando legato ad impegno), "123_100.00_10.0_4_S_N" (Quando legato a subimpegno)
	 */
	public String computeKeySubdocImportoImpegnoFlagRilevanteIva(int uid) {
		final String methodName = "computeKeySubdocImportoImpegnoFlagRilevanteIva";
		
		String result = subdocumentoDao.computeKeySubdocImportoMovimentoGestioneFlagRilevanteIva(uid);
		log.debug(methodName, "returning: "+result);
		return result;
	}

	public void aggiornaImportoSplitQuota(SubdocumentoSpesa subdoc, BigDecimal importoSplit) {
		SiacTSubdoc siacTSubdoc = siacTSubdocRepository.findOne(subdoc.getUid());
		siacTSubdoc.setSubdocSplitreverseImporto(importoSplit);
		siacTSubdocRepository.saveAndFlush(siacTSubdoc);
	}
	

	public void aggiornaTipoSplitQuota(SubdocumentoSpesa subdoc, TipoIvaSplitReverse tipoIvaSplitReverse) {
		Date now = new Date();
		SiacTSubdoc siacTSubdoc = siacTSubdocRepository.findOne(subdoc.getUid());
		//invalido tutte le rel presenti
		for(SiacRSubdocSplitreverseIvaTipo siacR : siacTSubdoc.getSiacRSubdocSplitreverseIvaTipos()){
			siacR.setDataCancellazioneIfNotSet(now);
			siacR.setDataFineValiditaIfNotSet(now);
		}
		
		
		//aggiungo la nuova Relazione
		SiacRSubdocSplitreverseIvaTipo siacR = new SiacRSubdocSplitreverseIvaTipo();
			
		SiacDSplitreverseIvaTipo siacDSplitreverseIvaTipo = eef.getEntity(SiacDSplitreverseIvaTipoEnum.byTipoIvaSplitReverse(tipoIvaSplitReverse), ente.getUid());
		siacR.setSiacDSplitreverseIvaTipo(siacDSplitreverseIvaTipo);
		siacR.setSiacTSubdoc(siacTSubdoc);
		siacR.setLoginOperazione(loginOperazione);
		siacR.setSiacTEnteProprietario(siacTSubdoc.getSiacTEnteProprietario());
		siacR.setDataModificaInserimento(now);
		siacTSubdoc.addSiacRSubdocSplitreverseIvaTipo(siacR);
		
		siacTSubdocRepository.save(siacTSubdoc);
	}
	

	/**
	 * Trova la nota credito iva associata al subdocumento
	 * 
	 * @param subdoc
	 * @return nota credito iva
	 */
	public SubdocumentoIvaSpesa findNotaCreditoIvaAssociata(SubdocumentoSpesa subdoc) {
		final String methodName = "findNotaCreditoIvaAssociata";
		SiacTSubdocIva siacTSubdocIva = siacTSubdocRepository.findSiacTSubdocIvaNotaCreditoIvaAssociata(subdoc.getUid());
		log.debug(methodName, "returning "+(siacTSubdocIva!=null?"uid: "+siacTSubdocIva.getUid():"null"));
		return mapNotNull(siacTSubdocIva, SubdocumentoIvaSpesa.class, BilMapId.SiacTSubdocIva_SubdocumentoIvaSpesa);
	}
	
	public SubdocumentoIvaSpesa findNotaCreditoIvaAssociataUid(SubdocumentoSpesa subdoc) {
		final String methodName = "findNotaCreditoIvaAssociata";
		SiacTSubdocIva siacTSubdocIva = siacTSubdocRepository.findSiacTSubdocIvaNotaCreditoIvaAssociata(subdoc.getUid());
		if(siacTSubdocIva==null){
			log.debug(methodName, "returning null");
			return null;
		}
		log.debug(methodName, "returning uid: "+siacTSubdocIva.getUid());
		
		SubdocumentoIvaSpesa result = new SubdocumentoIvaSpesa();
		result.setUid(siacTSubdocIva.getUid());
		return result;
	}

	/**
	 * Verifica se la quota non e' pagata.
	 * 
	 * @param subdoc
	 * @return true se la quota non e' pagata.
	 */
	public boolean isQuotaNonPagata(SubdocumentoSpesa subdoc) {
		final String methodName = "isQuotaNonPagata";
		SiacTSubdoc siacTSubdoc = siacTSubdocRepository.findSiacTSubdocSenzaOrdinativoAssociatoNonAnnullatoENonPagatoCEC(subdoc.getUid());
		boolean isQuotaNonPagata = siacTSubdoc!=null;
		log.debug(methodName, "returning result: "+isQuotaNonPagata);
		return isQuotaNonPagata;
	}

	/**
	 * Verifica se e il soggetto è stato sospeso a livello di attoAllegato abbinato alla quota,
	 *  quindi anche in questo caso ha i dati di sospensione valorizzati e la data di riattivazione=NULL.
	 * 
	 * @param subdoc
	 */
	public boolean isSoggettoAttoSubdocSospeso(SubdocumentoSpesa subdoc,Integer uidSoggetto) {
		final String methodName = "isSoggettoAttoSubdocSospeso";
		log.debug(methodName, "cerco se il soggetto con uid "+uidSoggetto+" e' sospeso per il subdocumento con id "+subdoc.getUid());
		Long count = siacTSubdocRepository.findSiacTSubdocSoggettoAttoSospeso(subdoc.getUid(),uidSoggetto);
		return count !=null  && count >0 ;
	}

	public Long countSubdocIvaCollegati(SubdocumentoSpesa subdoc) {
		return siacTSubdocRepository.countSiacTSubdocIvaBySubdocIdAndDocId(subdoc.getUid(), subdoc.getDocumento().getUid());
	}

	/**
	 * Aggiorna il falg convalida manuale del subdoc.
	 * 
	 * @param subdoc
	 */
	public void aggiornaFlagConvalidaManuale(SubdocumentoSpesa subdoc) {
		String methodName = "aggiornaFlagConvalidaManuale";
		SiacTSubdoc siacTSubdoc = siacTSubdocRepository.findOne(subdoc.getUid());
		BooleanToStringManualeAutomaticoConverter conv = new BooleanToStringManualeAutomaticoConverter();
		String convalidaManuale = conv.convertTo(subdoc.getFlagConvalidaManuale());
		siacTSubdoc.setSubdocConvalidaManuale(convalidaManuale);
		siacTSubdocRepository.saveAndFlush(siacTSubdoc);
		log.debug(methodName, "convalidaManuale impostato a: "+convalidaManuale + " per la quota con uid:"+subdoc.getUid());
		
	}

	public List<SubdocumentoSpesa> findSubdocumentoSpesaByAllegatoAttoIdAndSoggettoId(AllegatoAtto allegatoAtto, Soggetto soggetto) {
		List<String> docFamTipoCodes = Arrays.asList(SiacDDocFamTipoEnum.Spesa.getCodice(), SiacDDocFamTipoEnum.IvaSpesa.getCodice());
		List<SiacTSubdoc> siacTSubdocs = siacTSubdocRepository.findSiacTSubdocByAttoalIdAndSoggettoIdAndDocFamTipoCodeIn(allegatoAtto.getUid(), soggetto.getUid(), docFamTipoCodes);
		return convertiLista(siacTSubdocs, SubdocumentoSpesa.class, BilMapId.SiacTSubdoc_SubdocumentoSpesa_ModelDetail);
	}

	public List<CartaContabile> findCartaContabileBySubdocId(Integer subdocId) {
		List<SiacTCartacont> siacTCartaconts = siacTSubdocRepository.findSiacTCartacontBySubdocId(subdocId);
		return convertiLista(siacTCartaconts, CartaContabile.class, BilMapId.SiacTCartacont_CartaContabile_BIL);
	}
	
	public List<SubdocumentoSpesa> ricercaSubdocumentiSpesaDaEmettereByAttoAmministrativo(Integer uidProvvedimento,	Integer annoProvvedimento, Integer numeroProvvedimento){
		Set<SiacDAttoAmmStatoEnum> siacDAttoAmmStatoEnums = getStatiOperativiAttiPerEmissione();
		
		List<StatoOperativoDocumento> statiOperativiDocumento = getStatiOperativiDocumentiPerEmissione();
		
		BooleanToStringManualeAutomaticoConverter converter = new BooleanToStringManualeAutomaticoConverter();
		
		List<SiacTSubdoc> quoteDaEmettere = subdocumentoDao.ricercaQuoteDaEmettere(EnumSet.of(SiacDDocFamTipoEnum.Spesa, SiacDDocFamTipoEnum.IvaSpesa),
				siacDAttoAmmStatoEnums,
				SiacDDocStatoEnum.byStatiOperativi(statiOperativiDocumento),
				uidProvvedimento != null? uidProvvedimento : null,
				annoProvvedimento,
				numeroProvvedimento,
				null,
				null,
				null,
				converter.convertTo(Boolean.FALSE),
				ente.getUid());
		
		return convertiLista(quoteDaEmettere, SubdocumentoSpesa.class, BilMapId.SiacTSubdoc_SubdocumentoSpesa_Emettitore);
	}
	
	public Long countSubdocumentiSpesaDaEmettereByAttoAmministrativo(Integer uidProvvedimento,	Integer annoProvvedimento, Integer numeroProvvedimento){
		Set<SiacDAttoAmmStatoEnum> siacDAttoAmmStatoEnums = getStatiOperativiAttiPerEmissione();
		
		List<StatoOperativoDocumento> statiOperativiDocumento = getStatiOperativiDocumentiPerEmissione();
		
		BooleanToStringManualeAutomaticoConverter converter = new BooleanToStringManualeAutomaticoConverter();
		
		return subdocumentoDao.countQuoteDaEmettere(EnumSet.of(SiacDDocFamTipoEnum.Spesa, SiacDDocFamTipoEnum.IvaSpesa),
				siacDAttoAmmStatoEnums,
				SiacDDocStatoEnum.byStatiOperativi(statiOperativiDocumento),
				uidProvvedimento != null? uidProvvedimento : null,
				annoProvvedimento,
				numeroProvvedimento,
				null,
				null,
				null,
				converter.convertTo(Boolean.FALSE),
				ente.getUid());
	}
	
	public List<SubdocumentoSpesa> ricercaSubdocumentiSpesaDaEmettereByEnte(Boolean convalidaManuale){
		Set<SiacDAttoAmmStatoEnum> siacDAttoAmmStatoEnums = getStatiOperativiAttiPerEmissione();
		
		List<StatoOperativoDocumento> statiOperativiDocumento = getStatiOperativiDocumentiPerEmissione();
		
		BooleanToStringManualeAutomaticoConverter converter = new BooleanToStringManualeAutomaticoConverter();
		
		List<SiacTSubdoc> quoteDaEmettere = subdocumentoDao.ricercaQuoteDaEmettere(EnumSet.of(SiacDDocFamTipoEnum.Spesa, SiacDDocFamTipoEnum.IvaSpesa),
				siacDAttoAmmStatoEnums,
				SiacDDocStatoEnum.byStatiOperativi(statiOperativiDocumento),
				null,
				null,
				null,
				null,
				null,
				null,
				convalidaManuale != null? converter.convertTo(convalidaManuale) : "M",
				ente.getUid());
		log.error("ricercaSubdocumentiSpesaDaEmettereByElenco", "quoteDaEmettere.size: " + (quoteDaEmettere!= null? quoteDaEmettere.size(): "null"));
		return convertiLista(quoteDaEmettere, SubdocumentoSpesa.class, BilMapId.SiacTSubdoc_SubdocumentoSpesa_Emettitore);
	}
	
	public Long countSubdocumentiSpesaDaEmettereByEnte(Boolean convalidaManuale){
		Set<SiacDAttoAmmStatoEnum> siacDAttoAmmStatoEnums = getStatiOperativiAttiPerEmissione();
		
		List<StatoOperativoDocumento> statiOperativiDocumento = getStatiOperativiDocumentiPerEmissione();
		
		BooleanToStringManualeAutomaticoConverter converter = new BooleanToStringManualeAutomaticoConverter();
		
		return subdocumentoDao.countQuoteDaEmettere(EnumSet.of(SiacDDocFamTipoEnum.Spesa, SiacDDocFamTipoEnum.IvaSpesa),
				siacDAttoAmmStatoEnums,
				SiacDDocStatoEnum.byStatiOperativi(statiOperativiDocumento),
				null,
				null,
				null,
				null,
				null,
				null,
				convalidaManuale != null? converter.convertTo(convalidaManuale) : "M",
				ente.getUid());
	}
	
	
	public List<SubdocumentoSpesa> ricercaSubdocumentiSpesaDaEmettereByElenco(List<ElencoDocumentiAllegato> elenchi, Boolean convalidaManuale){
		List<Integer> elencoUids = new ArrayList<Integer>();
		BooleanToStringManualeAutomaticoConverter converter = new BooleanToStringManualeAutomaticoConverter();
		
		Set<SiacDAttoAmmStatoEnum> siacDAttoAmmStatoEnums = getStatiOperativiAttiPerEmissione();
		
		List<StatoOperativoDocumento> statiOperativiDocumento = getStatiOperativiDocumentiPerEmissione();
		
		for (ElencoDocumentiAllegato elenco : elenchi) {
			if(elenco.getUid() != 0){
				elencoUids.add(elenco.getUid());
			}
		}
		
		List<SiacTSubdoc> quoteDaEmettere = subdocumentoDao.ricercaQuoteDaEmettere(EnumSet.of(SiacDDocFamTipoEnum.Spesa, SiacDDocFamTipoEnum.IvaSpesa),
				siacDAttoAmmStatoEnums,
				SiacDDocStatoEnum.byStatiOperativi(statiOperativiDocumento),
				null,
				null,
				null,
				elencoUids,
				null,
				null,
				convalidaManuale != null? converter.convertTo(convalidaManuale) : "M",
				ente.getUid());
		//MAPPING NUOVO!
		return convertiLista(quoteDaEmettere, SubdocumentoSpesa.class, BilMapId.SiacTSubdoc_SubdocumentoSpesa_Emettitore);
		//MAPPING VECCHIO!!!
		//return convertiLista(quoteDaEmettere, SubdocumentoSpesa.class, BilMapId.SiacTSubdoc_SubdocumentoSpesa);
	}
	
	public Long countSubdocumentiSpesaDaEmettereByElenco(List<ElencoDocumentiAllegato> elenchi, Boolean convalidaManuale){
		List<Integer> elencoUids = new ArrayList<Integer>();
		BooleanToStringManualeAutomaticoConverter converter = new BooleanToStringManualeAutomaticoConverter();
		
		Set<SiacDAttoAmmStatoEnum> siacDAttoAmmStatoEnums = getStatiOperativiAttiPerEmissione();
		
		List<StatoOperativoDocumento> statiOperativiDocumento = getStatiOperativiDocumentiPerEmissione();
		
		for (ElencoDocumentiAllegato elenco : elenchi) {
			if(elenco.getUid() != 0){
				elencoUids.add(elenco.getUid());
			}
		}
		
		return subdocumentoDao.countQuoteDaEmettere(EnumSet.of(SiacDDocFamTipoEnum.Spesa, SiacDDocFamTipoEnum.IvaSpesa),
				siacDAttoAmmStatoEnums,
				SiacDDocStatoEnum.byStatiOperativi(statiOperativiDocumento),
				null,
				null,
				null,
				elencoUids,
				null,
				null,
				convalidaManuale != null? converter.convertTo(convalidaManuale) : "M",
				ente.getUid());
	}
	
	/**
	 * @return
	 */
	private List<StatoOperativoDocumento> getStatiOperativiDocumentiPerEmissione() {
		final Set<StatoOperativoDocumento> statiOperativiSet = EnumSet.allOf(StatoOperativoDocumento.class);
		List<StatoOperativoDocumento> statiOperativiDocumento = new ArrayList<StatoOperativoDocumento>();
		statiOperativiSet.remove(StatoOperativoDocumento.ANNULLATO);
		statiOperativiSet.remove(StatoOperativoDocumento.STORNATO);
		statiOperativiSet.remove(StatoOperativoDocumento.EMESSO);
		statiOperativiDocumento.addAll(statiOperativiSet);
		return statiOperativiDocumento;
	}

	/**
	 * @return
	 */
	private Set<SiacDAttoAmmStatoEnum> getStatiOperativiAttiPerEmissione() {
		final Set<StatoOperativoAtti> statiOperativiAtti = EnumSet.allOf(StatoOperativoAtti.class);
		
		List<StatoOperativoAtti> statiOperativiAtto = new ArrayList<StatoOperativoAtti>();
		
		statiOperativiAtti.remove(StatoOperativoAtti.ANNULLATO);
		statiOperativiAtto.addAll(statiOperativiAtti);
		
		Set<SiacDAttoAmmStatoEnum> siacDAttoAmmStatoEnums = SiacDAttoAmmStatoEnum.byStatiOperativi(statiOperativiAtti);
		return siacDAttoAmmStatoEnums;
	}

	public void aggiornaSospensioni(SubdocumentoSpesa subdoc) {
		List<SiacTSubdocSospensione> siacTSubdocSospensiones = convertiLista(subdoc.getSospensioni(), SiacTSubdocSospensione.class, BilMapId.SiacTSubdocSospensione_SospensioneSubdocumento);
		// Aggiungo la login
		for(SiacTSubdocSospensione tss : siacTSubdocSospensiones) {
			tss.setLoginOperazione(loginOperazione);
		}
		subdocumentoDao.insertUpdateSiacTSubdocSospensione(subdoc.getUid(), siacTSubdocSospensiones);
	}
	
	public List<SospensioneSubdocumento> findSospensioni(SubdocumentoSpesa subdoc) {
		List<SiacTSubdocSospensione> siacTSubdocSospensiones = siacTSubdocRepository.findSiacTSubdocSospensionesBySubdocId(subdoc.getUid());
		return convertiLista(siacTSubdocSospensiones, SospensioneSubdocumento.class, BilMapId.SiacTSubdocSospensione_SospensioneSubdocumento);
	}

	public void aggiornaSiopeAssenzaMotivazione(SubdocumentoSpesa subdocumentoSpesa, SiopeAssenzaMotivazione siopeAssenzaMotivazione) {
		SiacTSubdoc siacTSubdoc = siacTSubdocRepository.findOne(subdocumentoSpesa.getUid());
		SiacDSiopeAssenzaMotivazione siacDSiopeAssenzaMotivazione = mapNotNull(siopeAssenzaMotivazione, SiacDSiopeAssenzaMotivazione.class, BilMapId.SiacDSiopeAssenzaMotivazione_SiopeAssenzaMotivazione);
		siacTSubdoc.setSiacDSiopeAssenzaMotivazione(siacDSiopeAssenzaMotivazione);
	}

	/**
	 * SIAC-4800
	 * <br/>
	 * Recupero le date di scadenza dei documenti
	 * <p>
	 * SIAC-5446
	 * <br/>
	 * Ottiene le date di scadenza (eventualmente dopo sospensione) delle quote collegate all'allegato atto fornito, purch&eacute; siano
	 * riferite a documento di tipo FAT-Fattura
	 * @param allegatoAtto l'allegato per cui trovare le date
	 * @return i subdoc con le date di scadenza
	 */
	public List<SubdocumentoSpesa> findSubdocWithDateScadenzaFattureByAllegatoAtto(AllegatoAtto allegatoAtto) {
		List<Object[]> scadenze = siacTSubdocRepository.findSubdocDataScadenzaByAllegatoAttoAndDocTipoCode(allegatoAtto.getUid(), "FAT", SiacTAttrEnum.DataScadenzaDopoSospensione.getCodice());
		List<SubdocumentoSpesa> res = new ArrayList<SubdocumentoSpesa>();
		// TODO: centralizzare con tutti i converters
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ITALY);
		
		for(Object[] objs : scadenze) {
			// subdoc_id, subdoc_data_scadenza, attr_testo
			SubdocumentoSpesa ss = new SubdocumentoSpesa();
			ss.setUid((Integer) objs[0]);
			ss.setDataScadenza((Date) objs[1]);
			ss.setDataScadenzaDopoSospensione(parseDataSospensioneDopoRiattivazione(objs[2], df));
			
			res.add(ss);
		}
		
		return res;
	}
	
	private Date parseDataSospensioneDopoRiattivazione(Object data, DateFormat df) {
		if(data == null) {
			return null;
		}
		try {
			return df.parse((String) data);
		} catch (ParseException e) {
			// Unparsable date: throw exception
			throw new IllegalArgumentException("Unparseable date " + data);
		}
	}
	
	//SIAC-5937
	/**
	 * Aggiorna il falg convalida manuale del subdoc.
	 *
	 * @param subdoc the subdoc
	 * @param bilancioAttuale the bilancio attuale
	 * @param bilancioSuccessivo the bilancio successivo
	 * @param now the now
	 */
	public void collegaScollegaLiquidazionePerDoppiaGestione(SubdocumentoSpesa subdoc, Bilancio bilancioAttuale, Bilancio bilancioSuccessivo, Date now) {
		final String methodName="collegaScollegaLiquidazionePerDoppiaGestione";
		Integer annoLiquidazione = subdoc.getLiquidazione().getAnnoLiquidazione();
		BigDecimal numeroLiquidazione = subdoc.getLiquidazione().getNumeroLiquidazione();
		log.debug(methodName, "Cerco una relazione tra subdocumento [ uid: " + subdoc.getUid() +  " ] e liquidazione per il bilancio attuale [ uid: " + bilancioAttuale.getUid() + " ].");
		List<SiacRSubdocLiquidazione> siacRSubdocLiquidazioneBilancioCorrentes = siacRSubdocLiquidazioneRepository.findRelazioniCancellataBySubdocIdAndBilIdOrderByValiditaFineDesc(subdoc.getUid(), annoLiquidazione, numeroLiquidazione, bilancioAttuale.getUid());
		SiacRSubdocLiquidazione siacRSubdocLiquidazioneBilancioCorrente = siacRSubdocLiquidazioneBilancioCorrentes != null && !siacRSubdocLiquidazioneBilancioCorrentes.isEmpty()? siacRSubdocLiquidazioneBilancioCorrentes.get(0) : null;		 
		if(siacRSubdocLiquidazioneBilancioCorrente == null ) {
			log.error(methodName, "non c'e' nessuna siacRSubdocLiquidazioneBilancioCorrente per il  subdocumento [ uid: " + subdoc.getUid() +  " ].");
//			throw new BusinessException("non c'e' nessuna siacRSubdocLiquidazioneBilancioCorrente per il  subdocumento [ uid: " + subdoc.getUid() +  " ].");
			return;
		}
		
		List<SiacRSubdocLiquidazione> siacRSubdocLiquidazioneBilancioSuccessivos = siacRSubdocLiquidazioneRepository.findUltimaRelazioneValidaBySubdocIdAndBilId(subdoc.getUid(), annoLiquidazione, numeroLiquidazione, bilancioSuccessivo.getUid());
		SiacRSubdocLiquidazione siacRSubdocLiquidazioneBilancioSuccessivo = siacRSubdocLiquidazioneBilancioSuccessivos != null && siacRSubdocLiquidazioneBilancioSuccessivos.size() == 1? siacRSubdocLiquidazioneBilancioSuccessivos.get(0) : null;
		
		if(siacRSubdocLiquidazioneBilancioSuccessivo == null ) {
			log.error(methodName, "non c'e' nessuna siacRSubdocLiquidazioneBilancioSuccessivo per il  subdocumento [ uid: " + subdoc.getUid() +  " ].");
			//throw new BusinessException("non c'e' nessuna siacRSubdocLiquidazioneBilancioSuccessivo per il  subdocumento [ uid: " + subdoc.getUid() +  " ].");
			return;
		}
		
		log.debug(methodName, "Aggiorno la relazione [ uid: " + siacRSubdocLiquidazioneBilancioCorrente.getUid() +  " ] per il bilancio attuale [ uid: " + bilancioAttuale.getUid() + " ].");
		impostaFineValiditaCancellazioneLoginOperazione(null, null,now,siacRSubdocLiquidazioneBilancioCorrente);		
		siacRSubdocLiquidazioneRepository.saveAndFlush(siacRSubdocLiquidazioneBilancioCorrente);
		
		log.debug(methodName, "Aggiorno la relazione [ uid: " + siacRSubdocLiquidazioneBilancioSuccessivo.getUid() +  " ] per il bilancio successivo [ uid: " + bilancioSuccessivo.getUid() + " ].");
		impostaFineValiditaCancellazioneLoginOperazione(now, now, now, siacRSubdocLiquidazioneBilancioSuccessivo);		
		siacRSubdocLiquidazioneRepository.saveAndFlush(siacRSubdocLiquidazioneBilancioSuccessivo);		
			
	}
	
	/**
	 * Aggiorna il falg convalida manuale del subdoc.
	 *
	 * @param subdoc the subdoc
	 * @param bilancioAttuale the bilancio attuale
	 * @param bilancioSuccessivo the bilancio successivo
	 * @param now the now
	 */
	public void collegaScollegaMovimentoGestionePerDoppiaGestione(SubdocumentoSpesa subdoc, Bilancio bilancioAttuale, Bilancio bilancioSuccessivo, Date now) {
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
		
		log.debug(methodName, "Cerco una relazione tra subdocumento [ uid: " + subdoc.getUid() +  " ] e il movimento gestione per il bilancio successivo [ uid: " + bilancioAttuale.getUid() + " ].");
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
	public Object[] cercaChiaveMovgestCollegatoASubdoc(SubdocumentoSpesa subdoc) {
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
	
	
	/**
	 * Gets the soggetti con durc scaduto by subdoc id.
	 *
	 * @param subdocumentiIds the subdocumenti ids
	 * @return the soggetti con durc scaduto by subdoc id
	 */
	public List<Soggetto> getSoggettiConDurcScadutoBySubdocId(List<Integer> subdocumentiIds){
		List<SiacTSoggetto> siacTSoggettos = siacTSubdocRepository.findSoggettiDurcScadutoBySubdocIds(subdocumentiIds);
		return convertiLista(siacTSoggettos, Soggetto.class, BilMapId.SiacTSoggetto_Soggetto);
	}
	
	 /* Gets the flag attiva gsa impegno collegato.
	 *
	 * @param uidSubdoc the uid subdoc
	 * @return the flag attiva gsa impegno collegato
	 */
	public Boolean getFlagAttivaGsaImpegnoCollegato(Integer uidSubdoc) {
		String methodName = "getFlagAttivaGsa";
		
		List<String> attrs = siacTSubdocRepository.findBooleanAttrValuesMovgestCollegato(uidSubdoc, SiacTAttrEnum.FlagAttivaGsa.getCodice());
		boolean tmp = false;
		String attr = null;
		for(Iterator<String> it = attrs.iterator(); it.hasNext() && !tmp;) {
			attr = it.next();
			tmp = "S".equals(attr);
		}
		Boolean result = Boolean.valueOf(tmp);
		
		log.debug(methodName, "Returning: "+result + " (for uidImpegno: "+uidSubdoc+" and 'attr' with value: "+attr+")");
		return result;
	}	

	/**
	 * Gets the data fine validita durc piu recente by subdoc ids.
	 *
	 * @param suddocIds the suddoc ids
	 * @return the data fine validita durc piu recente by subdoc ids
	 */
	public Map<String, Date> getDataFineValiditaDurcAndSoggettoCodePiuRecenteBySubdocIds(List<Integer> suddocIds){
		Map<String, Date> mappaSoggettoData = new HashMap<String, Date>();
		if(suddocIds == null || suddocIds.isEmpty()) {
			return mappaSoggettoData;
		}
		List<Object[]> dates =  siacTSubdocRepository.getDataFineValiditaDurcAndSoggettoCodeBySubdocIds(suddocIds);
		if(dates == null || dates.isEmpty()){
			return mappaSoggettoData;
		}
		Object[] obj = dates.get(0);
		String soggettoCode = (String) obj[1] != null? (String) obj[1] : "null";
		
		mappaSoggettoData.put(soggettoCode, (Date)obj[0]);
		return mappaSoggettoData;
	}
	
}
