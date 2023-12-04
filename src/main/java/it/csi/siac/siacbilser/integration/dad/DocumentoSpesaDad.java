/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.integration.dao.DocumentoDao;
import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.dao.SiacDCodicebolloRepository;
import it.csi.siac.siacbilser.integration.dao.SiacDDocTipoRepository;
import it.csi.siac.siacbilser.integration.dao.SiacRDocStatoRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTDocRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTOrdineRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDCodicebollo;
import it.csi.siac.siacbilser.integration.entity.SiacDDocStato;
import it.csi.siac.siacbilser.integration.entity.SiacDDocTipo;
import it.csi.siac.siacbilser.integration.entity.SiacDSiopeDocumentoTipo;
import it.csi.siac.siacbilser.integration.entity.SiacRDocOnere;
import it.csi.siac.siacbilser.integration.entity.SiacRDocStato;
import it.csi.siac.siacbilser.integration.entity.SiacTBil;
import it.csi.siac.siacbilser.integration.entity.SiacTDoc;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdocIva;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocFamTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDRelazTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDSplitreverseIvaTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTAttrEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.base.Converters;
import it.csi.siac.siacbilser.model.TipologiaAttributo;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.model.CodiceBollo;
import it.csi.siac.siacfin2ser.model.DettaglioOnere;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.DocumentoSpesaModelDetail;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.PreDocumentoSpesa;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfin2ser.model.TipoFamigliaDocumento;
import it.csi.siac.siacfin2ser.model.TipoIvaSplitReverse;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.ordinativo.SubOrdinativoPagamento;
import it.csi.siac.siacfinser.model.siopeplus.SiopeDocumentoTipo;

/**
 * Data access delegate di un DocumentoSpesa .
 *
 * @author Domenico
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class DocumentoSpesaDad extends ExtendedBaseDadImpl {
	
	/** The documento dao. */
	@Autowired
	private DocumentoDao documentoDao;
	
	/** The eef. */
	@Autowired
	private EnumEntityFactory eef;
	
	/** The siac r doc stato repository. */
	@Autowired
	private SiacRDocStatoRepository siacRDocStatoRepository;
	
	/** The siac d doc tipo repository. */
	@Autowired
	private SiacDDocTipoRepository siacDDocTipoRepository;
	
	@Autowired
	private SiacTDocRepository siacTDocRepository;

	/** The siac d codicebollo repository. */
	@Autowired
	private SiacDCodicebolloRepository siacDCodicebolloRepository;
	
	@Autowired
	private SiacTOrdineRepository siacTOrdineRepository;
	
	/**
	 * Find documento spesa by id utilizzando il mapping completo
	 *
	 * @param uid the uid
	 * @return the documento spesa
	 */
	public  DocumentoSpesa findDocumentoSpesaById(Integer uid) {
		return findDocumentoSpesaById(uid, BilMapId.SiacTDoc_DocumentoSpesa);
	}

	/**
	 * Find documento spesa by id utilizzando il mapping medio
	 *
	 * @param uid the uid
	 * @return the documento spesa
	 */
	public DocumentoSpesa findDocumentoSpesaByIdMedium(Integer uid) {
		return findDocumentoSpesaById(uid, BilMapId.SiacTDoc_DocumentoSpesa_Medium);
	}


	/**
	 * Find documento spesa by id utilizzando il mapping minimal
	 *
	 * @param uid the uid
	 * @return the documento spesa
	 */
	public DocumentoSpesa findDocumentoSpesaByIdMinimal(Integer uid) {
		return findDocumentoSpesaById(uid, BilMapId.SiacTDoc_DocumentoSpesa_Minimal);
	}
	
	
	/**
	 * Find documento spesa by id.
	 *
	 * @param uid the uid
	 * @return the documento spesa
	 */
	private  DocumentoSpesa findDocumentoSpesaById(Integer uid, BilMapId bilMapId_SiacTDoc_DocumentoSpesa) {
		final String methodName = "findDocumentoSpesaById";		
		log.debug(methodName, "uid: "+ uid);
		SiacTDoc siacTDoc = documentoDao.findById(uid);
		if(siacTDoc == null) {
			log.debug(methodName, "Impossibile trovare il DocumentoSpesa con id: " + uid);
		}else{
			log.debug(methodName, "siacTDoc trovata con uid: " + siacTDoc.getUid());
		}
		log.debug(methodName, "mapId: "+bilMapId_SiacTDoc_DocumentoSpesa);
		return  mapNotNull(siacTDoc, DocumentoSpesa.class, bilMapId_SiacTDoc_DocumentoSpesa);
	}
	
	/**
	 * Find documento spesa by id.
	 *
	 * @param uid the uid
	 * @return the documento spesa
	 */
	public DocumentoSpesa findDocumentoSpesaById(Integer uid, DocumentoSpesaModelDetail...modelDetails) {
		return findDocumentoSpesaById(uid, BilMapId.SiacTDoc_DocumentoSpesa_Minimal, modelDetails);
	}
	
	/**
	 * Find documento spesa by id.
	 *
	 * @param uid the uid
	 * @return the documento spesa
	 */
	public DocumentoSpesa findDocumentoSpesaByIdModelDetail(Integer uid, DocumentoSpesaModelDetail...modelDetails) {
		return findDocumentoSpesaById(uid, BilMapId.SiacTDoc_DocumentoSpesa_ModelDetail, modelDetails);
	}
	
	/**
	 * Find documento spesa by id.
	 *
	 * @param uid the uid
	 * @return the documento spesa
	 */
	private DocumentoSpesa findDocumentoSpesaById(Integer uid, BilMapId mapId, DocumentoSpesaModelDetail...modelDetails) {
		final String methodName = "findDocumentoSpesaById[modelDetails]";		
		log.debug(methodName, "uid: "+ uid);
		SiacTDoc siacTDoc = documentoDao.findById(uid);
		if(siacTDoc == null) {
			log.debug(methodName, "Impossibile trovare il DocumentoSpesa con id: " + uid);
		}else{
			log.debug(methodName, "siacTDoc trovata con uid: " + siacTDoc.getUid());
		}
		return mapNotNull(siacTDoc, DocumentoSpesa.class, mapId, Converters.byModelDetails(modelDetails));
	}
	
	/**
	 * Effettua un flush ed un clear dell'EntityManager.
	 * 
	 */
	public void flushAndClear(){
		documentoDao.flushAndClear();
	}
	
	
	/**
	 * Find documento spesa by id after flush and clear.
	 *
	 * @param uid the uid
	 * @return the documento spesa
	 */
	public DocumentoSpesa findDocumentoSpesaByIdAfterFlushAndClear(Integer uid) {
		documentoDao.flushAndClear();
		return findDocumentoSpesaById(uid);
	}
	
	
	/**
	 * Inserisci anagrafica documento spesa.
	 *
	 * @param documento the documento
	 */
	//@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void inserisciAnagraficaDocumentoSpesa(DocumentoSpesa documento) {		
		SiacTDoc siacTDoc = buildSiacTDoc(documento);	
		siacTDoc.setLoginCreazione(loginOperazione);
		siacTDoc.setLoginModifica(loginOperazione);
		documentoDao.create(siacTDoc);
		documento.setUid(siacTDoc.getUid());
	}	
	
	

	/**
	 * Aggiorna anagrafica documento spesa.
	 *
	 * @param documento the documento
	 */
	public void aggiornaAnagraficaDocumentoSpesa(DocumentoSpesa documento) {
		SiacTDoc siacTDoc = buildSiacTDoc(documento);	
		siacTDoc.setLoginModifica(loginOperazione);
		documentoDao.update(siacTDoc);
		documento.setUid(siacTDoc.getUid());
	}	
	
	
	/**
	 * Builds the siac t doc.
	 *
	 * @param documento the documento
	 * @return the siac t doc
	 */
	private SiacTDoc buildSiacTDoc(DocumentoSpesa documento) {
		SiacTDoc siacTDoc = new SiacTDoc();
		siacTDoc.setLoginOperazione(loginOperazione);
		documento.setLoginOperazione(loginOperazione);
		map(documento, siacTDoc, BilMapId.SiacTDoc_DocumentoSpesa);		
		return siacTDoc;
	}
	
	
	/**
	 * Ricerca puntuale documento spesa.
	 *
	 * @param doc the doc
	 * @param statoOperativoDocumentoDaEscludere the stato operativo documento da escludere
	 * @return the documento spesa
	 */
	public DocumentoSpesa ricercaPuntualeDocumentoSpesa(DocumentoSpesa doc, StatoOperativoDocumento statoOperativoDocumentoDaEscludere) {
		SiacTDoc siacTDoc = ricercaPuntualeDocumento(doc, statoOperativoDocumentoDaEscludere, SiacDDocFamTipoEnum.Spesa);
		return mapNotNull(siacTDoc, DocumentoSpesa.class, BilMapId.SiacTDoc_DocumentoSpesa_Base);
	}
	
	/**
	 * Ricerca puntuale documento spesa.
	 *
	 * @param doc the doc
	 * @param statoOperativoDocumentoDaEscludere the stato operativo documento da escludere
	 * @param modelDetails the model details
	 * @return the documento spesa
	 */
	public DocumentoSpesa ricercaPuntualeDocumentoSpesa(DocumentoSpesa doc, StatoOperativoDocumento statoOperativoDocumentoDaEscludere, DocumentoSpesaModelDetail... modelDetails) {
		SiacTDoc siacTDoc = ricercaPuntualeDocumento(doc, statoOperativoDocumentoDaEscludere, SiacDDocFamTipoEnum.Spesa);
		return mapNotNull(siacTDoc, DocumentoSpesa.class, BilMapId.SiacTDoc_DocumentoSpesa_ModelDetail, Converters.byModelDetails(modelDetails));
	}
	
	/**
	 * Ricerca puntuale documento iva spesa (testata).
	 *
	 * @param doc the doc
	 * @param statoOperativoDocumentoDaEscludere the stato operativo documento da escludere
	 * @return the documento spesa
	 */
	public DocumentoSpesa ricercaPuntualeDocumentoIvaSpesa(DocumentoSpesa doc, StatoOperativoDocumento statoOperativoDocumentoDaEscludere) {
		SiacTDoc siacTDoc = ricercaPuntualeDocumento(doc, statoOperativoDocumentoDaEscludere, SiacDDocFamTipoEnum.IvaSpesa);
		return mapNotNull(siacTDoc, DocumentoSpesa.class, BilMapId.SiacTDoc_DocumentoSpesa_Base);
	}
	
	
	private SiacTDoc ricercaPuntualeDocumento(DocumentoSpesa doc, StatoOperativoDocumento statoOperativoDocumentoDaEscludere, SiacDDocFamTipoEnum tipoDoc) {
		final String methodName = "ricercaPuntualeDocumentoSpesa";
		
		ParametriPaginazione parametriPaginazione = new ParametriPaginazione();		
		parametriPaginazione.setNumeroPagina(0);
		parametriPaginazione.setElementiPerPagina(1);
		
		Page<SiacTDoc> lista = documentoDao.ricercaSinteticaDocumento(
				doc.getEnte().getUid(),
				tipoDoc,
				doc.getAnno(),
				null,
				doc.getNumero(),
				null, //doc.getDataEmissione(),
				null, //doc.getDataOperazione()
				doc.getTipoDocumento()!=null?doc.getTipoDocumento().getUid():null,
				doc.getStatoOperativoDocumento()!=null?SiacDDocStatoEnum.byStatoOperativo(doc.getStatoOperativoDocumento()):null,
				statoOperativoDocumentoDaEscludere!=null?SiacDDocStatoEnum.byStatoOperativo(statoOperativoDocumentoDaEscludere):null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				doc.getSoggetto() != null ? doc.getSoggetto().getUid() : null,
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
				
				//SIAC-6780
				null,
				
				toPageable(parametriPaginazione));

		if(lista.getContent().isEmpty()) {
			log.debug(methodName, "Nessun documento trovato con chiave:"+doc.getDescAnnoNumeroUidTipoDocUidSoggettoStato());
			return null;
		}
		
		return lista.getContent().get(0);		
	}
	
	
	/**
	 * Ricerca sintetica documento spesa.
	 * 
	 * @param doc
	 * @param attoAmministrativo
	 * @param impegno
	 * @param rilevanteIva
	 * @param elencoDocumenti
	 * @param liquidazione
	 * @param bilancioLiquidazione
	 * @param collegatoCEC
	 * @param contabilizzaGenPcc 
	 * @param preDocumentoSpesa 
	 * @param parametriPaginazione
	 * @return documenti
	 */
	public ListaPaginata<DocumentoSpesa> ricercaSinteticaDocumentoSpesa(DocumentoSpesa doc, AttoAmministrativo attoAmministrativo, Impegno impegno, Boolean rilevanteIva,
			ElencoDocumentiAllegato elencoDocumenti, Liquidazione liquidazione, Bilancio bilancioLiquidazione, Boolean collegatoCEC, Boolean contabilizzaGenPcc, PreDocumentoSpesa preDocumentoSpesa, ParametriPaginazione parametriPaginazione) {
			
		Page<SiacTDoc> lista = documentoDao.ricercaSinteticaDocumento(
				doc.getEnte().getUid(),
				SiacDDocFamTipoEnum.Spesa,
				doc.getAnno(),
				StringUtils.trimToNull(doc.getNumero()),
				null,
				doc.getDataEmissione(),
				null, //doc.getDataOperazione()
				doc.getTipoDocumento()!=null?doc.getTipoDocumento().getUid():null,
				doc.getStatoOperativoDocumento()!=null?SiacDDocStatoEnum.byStatoOperativo(doc.getStatoOperativoDocumento()):null,
				null,
				rilevanteIva,
				impegno != null ? impegno.getUid() : null, 
				null,
				mapToUidIfNotZero(attoAmministrativo),
				attoAmministrativo != null && attoAmministrativo.getAnno() != 0 ? String.valueOf(attoAmministrativo.getAnno()) : null, 
				attoAmministrativo != null && attoAmministrativo.getNumero() != 0 ? attoAmministrativo.getNumero() : null,
				attoAmministrativo != null ? mapToUidIfNotZero(attoAmministrativo.getTipoAtto()) : null,
				//TODO sac		
				attoAmministrativo != null ?  mapToUidIfNotZero(attoAmministrativo.getStrutturaAmmContabile()) : null,		
		

				doc.getSoggetto() != null ? doc.getSoggetto().getUid() : null,
				elencoDocumenti != null ? elencoDocumenti.getAnno() : null,
				elencoDocumenti != null ? elencoDocumenti.getNumero() : null,
				
				liquidazione != null ?  liquidazione.getAnnoLiquidazione() : null,
				liquidazione != null ?  liquidazione.getNumeroLiquidazione() : null,
				bilancioLiquidazione!= null ?  bilancioLiquidazione.getUid() : null,
				bilancioLiquidazione!= null ?  bilancioLiquidazione.getAnno()+"" : null,
				collegatoCEC,
				doc.getAnnoRepertorio(),
				doc.getDataRepertorio(),
				doc.getNumeroRepertorio(),
				doc.getRegistroRepertorio(),
				contabilizzaGenPcc,
				doc.getStatoSDI(),
				//SIAC-6780
				preDocumentoSpesa != null? preDocumentoSpesa.getNumero() : null,
				
				toPageable(parametriPaginazione));
		
		return toListaPaginata(lista, DocumentoSpesa.class, BilMapId.SiacTDoc_DocumentoSpesa);
		
	}
	
	
	/**
	 * Ricerca sintetica documento spesa.
	 * 
	 * @param doc
	 * @param attoAmministrativo
	 * @param impegno
	 * @param rilevanteIva
	 * @param elencoDocumenti
	 * @param liquidazione
	 * @param bilancioLiquidazione
	 * @param collegatoCEC
	 * @param contabilizzaGenPcc 
	 * @param preDocumentoSpesa 
	 * @param parametriPaginazione
	 * @return documenti
	 */
	public ListaPaginata<DocumentoSpesa> ricercaSinteticaModulareDocumentoSpesa(DocumentoSpesa doc, AttoAmministrativo attoAmministrativo, Impegno impegno, Boolean rilevanteIva,
			ElencoDocumentiAllegato elencoDocumenti, Liquidazione liquidazione, Bilancio bilancioLiquidazione, Boolean collegatoCEC, Boolean contabilizzaGenPcc, PreDocumentoSpesa preDocumentoSpesa, ParametriPaginazione parametriPaginazione,
			DocumentoSpesaModelDetail... documentoSpesaModelDetails) {
			
		Page<SiacTDoc> lista = documentoDao.ricercaSinteticaDocumento(
				doc.getEnte().getUid(),
				SiacDDocFamTipoEnum.Spesa,
				doc.getAnno(),
				StringUtils.trimToNull(doc.getNumero()),
				null,
				doc.getDataEmissione(),
				null, //doc.getDataOperazione()
				doc.getTipoDocumento()!=null?doc.getTipoDocumento().getUid():null,
				doc.getStatoOperativoDocumento()!=null?SiacDDocStatoEnum.byStatoOperativo(doc.getStatoOperativoDocumento()):null,
				null,
				rilevanteIva,
				impegno != null ? impegno.getUid() : null, 
				null,
				mapToUidIfNotZero(attoAmministrativo),
				attoAmministrativo != null && attoAmministrativo.getAnno() != 0 ? String.valueOf(attoAmministrativo.getAnno()) : null, 
				attoAmministrativo != null && attoAmministrativo.getNumero() != 0 ? attoAmministrativo.getNumero() : null,
				attoAmministrativo != null ? mapToUidIfNotZero(attoAmministrativo.getTipoAtto()) : null,
				//TODO sac		
				attoAmministrativo != null ?  mapToUidIfNotZero(attoAmministrativo.getStrutturaAmmContabile()) : null,		
		
				doc.getSoggetto() != null ? doc.getSoggetto().getUid() : null,
				elencoDocumenti != null ? elencoDocumenti.getAnno() : null,
				elencoDocumenti != null ? elencoDocumenti.getNumero() : null,
				liquidazione != null ?  liquidazione.getAnnoLiquidazione() : null,
				liquidazione != null ?  liquidazione.getNumeroLiquidazione() : null,
				bilancioLiquidazione!= null ?  bilancioLiquidazione.getUid() : null,
				bilancioLiquidazione!= null ?  bilancioLiquidazione.getAnno()+"" : null,
				collegatoCEC,
				doc.getAnnoRepertorio(),
				doc.getDataRepertorio(),
				doc.getNumeroRepertorio(),
				doc.getRegistroRepertorio(),
				contabilizzaGenPcc,
				doc.getStatoSDI(),
				//SIAC-6780
				preDocumentoSpesa != null? preDocumentoSpesa.getNumero() : null,
				
				toPageable(parametriPaginazione));
		
		return toListaPaginata(lista, DocumentoSpesa.class, BilMapId.SiacTDoc_DocumentoSpesa_ModelDetail, documentoSpesaModelDetails);
	}
	
	
	/**
	 * Ricerca sintetica testata documento spesa.
	 *
	 * @param doc the doc
	 * @param attoAmministrativo the atto amministrativo
	 * @param impegno the impegno
	 * @param rilevanteIva the rilevante iva
	 * @param parametriPaginazione the parametri paginazione
	 * @return the lista paginata
	 */
	public ListaPaginata<DocumentoSpesa> ricercaSinteticaTestataDocumentoSpesa(DocumentoSpesa doc, AttoAmministrativo attoAmministrativo, Impegno impegno, Boolean rilevanteIva, ParametriPaginazione parametriPaginazione) {
			
		Page<SiacTDoc> lista = documentoDao.ricercaSinteticaDocumento(
				doc.getEnte().getUid(),
				SiacDDocFamTipoEnum.IvaSpesa,
				doc.getAnno(),
				StringUtils.trimToNull(doc.getNumero()),
				null,
				doc.getDataEmissione(),
				null, //doc.getDataOperazione()
				doc.getTipoDocumento()!=null?doc.getTipoDocumento().getUid():null,
				doc.getStatoOperativoDocumento()!=null?SiacDDocStatoEnum.byStatoOperativo(doc.getStatoOperativoDocumento()):null,
				null,
				rilevanteIva,
				impegno != null ? impegno.getUid() : null, 
				null,
				mapToUidIfNotZero(attoAmministrativo),
				attoAmministrativo != null && attoAmministrativo.getAnno() != 0 ? String.valueOf(attoAmministrativo.getAnno()) : null, 
				attoAmministrativo != null && attoAmministrativo.getNumero() != 0 ? attoAmministrativo.getNumero() : null,
				attoAmministrativo != null ? mapToUidIfNotZero(attoAmministrativo.getTipoAtto()) : null,
				//TODO sac		
				attoAmministrativo != null ?  mapToUidIfNotZero(attoAmministrativo.getStrutturaAmmContabile()) : null,		
		
				doc.getSoggetto() != null ? doc.getSoggetto().getUid() : null,
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
				//SIAC-6780
				null,
				toPageable(parametriPaginazione));
		
		return toListaPaginata(lista, DocumentoSpesa.class, BilMapId.SiacTDoc_DocumentoSpesa);
		
	}
	
	
	
	/**
	 * Ricerca sintetica documento spesa importo totale.
	 *
	 * @param doc the doc
	 * @param attoAmministrativo the atto amministrativo
	 * @param impegno the impegno
	 * @param rilevanteIva the rilevante iva
	 * @param preDocumentoSpesa 
	 * @param parametriPaginazione the parametri paginazione
	 * @return the big decimal
	 */
	public BigDecimal ricercaSinteticaDocumentoSpesaImportoTotale(DocumentoSpesa doc, AttoAmministrativo attoAmministrativo, Impegno impegno, Boolean rilevanteIva,
			ElencoDocumentiAllegato elencoDocumenti, Liquidazione liquidazione, Bilancio bilancioLiquidazione, Boolean collegatoCEC, Boolean contabilizzaGenPcc, PreDocumentoSpesa preDocumentoSpesa, ParametriPaginazione parametriPaginazione) {
		
		BigDecimal importoTotale = documentoDao.ricercaSinteticaDocumentoImportoTotale(
				doc.getEnte().getUid(),
				SiacDDocFamTipoEnum.Spesa,
				doc.getAnno(),
				StringUtils.trimToNull(doc.getNumero()),
				null,
				doc.getDataEmissione(),
				null, //doc.getDataOperazione()
				doc.getTipoDocumento()!=null?doc.getTipoDocumento().getUid():null,
				doc.getStatoOperativoDocumento()!=null?SiacDDocStatoEnum.byStatoOperativo(doc.getStatoOperativoDocumento()):null,
				null,
				rilevanteIva,
				impegno != null ? impegno.getUid() : null,
				null,
				mapToUidIfNotZero(attoAmministrativo),
				attoAmministrativo != null && attoAmministrativo.getAnno() != 0 ? String.valueOf(attoAmministrativo.getAnno()) : null, 
				attoAmministrativo != null && attoAmministrativo.getNumero() != 0 ? attoAmministrativo.getNumero() : null,
				attoAmministrativo != null ? mapToUidIfNotZero(attoAmministrativo.getTipoAtto()) : null,
				//TODO sac		
				attoAmministrativo != null ?  mapToUidIfNotZero(attoAmministrativo.getStrutturaAmmContabile()) : null,		

				doc.getSoggetto() != null ? doc.getSoggetto().getUid() : null, 
				elencoDocumenti != null ? elencoDocumenti.getAnno() : null,
				elencoDocumenti != null ? elencoDocumenti.getNumero() : null,
				liquidazione != null ?  liquidazione.getAnnoLiquidazione() : null,
				liquidazione != null ?  liquidazione.getNumeroLiquidazione() : null,
				bilancioLiquidazione!= null ?  bilancioLiquidazione.getUid() : null,
				bilancioLiquidazione!= null ?  bilancioLiquidazione.getAnno()+"" : null,
				collegatoCEC,
				doc.getAnnoRepertorio(),
				doc.getDataRepertorio(),
				doc.getNumeroRepertorio(),
				doc.getRegistroRepertorio(),
				contabilizzaGenPcc,
				doc.getStatoSDI(),
				//SIAC-6780
				preDocumentoSpesa != null? preDocumentoSpesa.getNumero() : null,
				toPageable(parametriPaginazione));
		
		return importoTotale;
		
	}
	
	
	/**
	 * Ricerca sintetica testata documento spesa importo totale.
	 *
	 * @param doc the doc
	 * @param attoAmministrativo the atto amministrativo
	 * @param impegno the impegno
	 * @param rilevanteIva the rilevante iva
	 * @param parametriPaginazione the parametri paginazione
	 * @return the big decimal
	 */
	public BigDecimal ricercaSinteticaTestataDocumentoSpesaImportoTotale(DocumentoSpesa doc, AttoAmministrativo attoAmministrativo, Impegno impegno, Boolean rilevanteIva, ParametriPaginazione parametriPaginazione) {
		
		BigDecimal importoTotale = documentoDao.ricercaSinteticaDocumentoImportoTotale(
				doc.getEnte().getUid(),
				SiacDDocFamTipoEnum.IvaSpesa,
				doc.getAnno(),
				StringUtils.trimToNull(doc.getNumero()),
				null,
				doc.getDataEmissione(),
				null, //doc.getDataOperazione()
				doc.getTipoDocumento()!=null?doc.getTipoDocumento().getUid():null,
				doc.getStatoOperativoDocumento()!=null?SiacDDocStatoEnum.byStatoOperativo(doc.getStatoOperativoDocumento()):null,
				null,
				rilevanteIva,
				impegno != null ? impegno.getUid() : null,
				null,
				mapToUidIfNotZero(attoAmministrativo),
				attoAmministrativo != null && attoAmministrativo.getAnno() != 0 ? String.valueOf(attoAmministrativo.getAnno()) : null, 
				attoAmministrativo != null && attoAmministrativo.getNumero() != 0 ? attoAmministrativo.getNumero() : null,
				attoAmministrativo != null ? mapToUidIfNotZero(attoAmministrativo.getTipoAtto()) : null,
				//TODO sac		
				attoAmministrativo != null ?  mapToUidIfNotZero(attoAmministrativo.getStrutturaAmmContabile()) : null,		
	
				doc.getSoggetto() != null ? doc.getSoggetto().getUid() : null, 
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
				null,
				toPageable(parametriPaginazione));
		
		return importoTotale;
		
	}
	
	
	
	
	/**
	 * Aggiorna stato documento spesa.
	 *
	 * @param uidDocumento the uid documento
	 * @param statoOperativoDocumento the stato operativo documento
	 */
	public void aggiornaStatoDocumentoSpesa(Integer uidDocumento, StatoOperativoDocumento statoOperativoDocumento) {
		SiacTDoc siacTDoc = documentoDao.findById(uidDocumento);
		
		Date dataCancellazione = new Date();
		for(SiacRDocStato r : siacTDoc.getSiacRDocStatos()){
			r.setDataCancellazioneIfNotSet(dataCancellazione);					
		}
		Date now = new Date();
		SiacRDocStato siacRDocStato = new SiacRDocStato();
		SiacDDocStato siacDDocStato = eef.getEntity(SiacDDocStatoEnum.byStatoOperativo(statoOperativoDocumento), siacTDoc.getSiacTEnteProprietario().getUid());
		siacRDocStato.setSiacDDocStato(siacDDocStato);		
		siacRDocStato.setSiacTDoc(siacTDoc);			
		siacRDocStato.setSiacTEnteProprietario(siacTDoc.getSiacTEnteProprietario());
		siacRDocStato.setDataInizioValidita(now);
		siacRDocStato.setDataCreazione(now);
		siacRDocStato.setDataModifica(now);
		siacRDocStato.setLoginOperazione(loginOperazione);		
		
		siacRDocStatoRepository.saveAndFlush(siacRDocStato);
		
	}
	
	/**
	 * Find documenti collegati by id documento.
	 *
	 * @param uid the uid
	 * @return the documento spesa
	 */
	public DocumentoSpesa findDocumentiCollegatiByIdDocumento(Integer uid) {
		final String methodName = "findDocumentiCollegatiByIdDocumento";		
		log.debug(methodName, "uid: "+ uid);
		SiacTDoc siacTDoc = documentoDao.findById(uid);
		if(siacTDoc == null) {
			log.debug(methodName, "Impossibile trovare il DocumentoSpesa con id: " + uid);
		}
		return mapNotNull(siacTDoc, DocumentoSpesa.class, BilMapId.SiacTDoc_DocumentoSpesa_Collegati);
	}
	
	
	
	
	
	/**
	 * Find codice bollo esente.
	 *
	 * @return the codice bollo
	 */
	public CodiceBollo findCodiceBolloEsente() {
		CodiceBollo cb = findCodiceBolloByCode("99");
		if(cb == null) {
			throw new IllegalStateException("Nessun codice bollo di tipo esente con codice \"99\" presente su base dati.");
		}
		return cb;
	}
	
	
	/**
	 * Find codice bollo by code.
	 *
	 * @param codice the codice
	 * @return the codice bollo
	 */
	public CodiceBollo findCodiceBolloByCode(String codice) {
		SiacDCodicebollo siacDCodicebollo = siacDCodicebolloRepository.findCodiciBolloByCodice(codice, ente.getUid());		
		return mapNotNull(siacDCodicebollo, CodiceBollo.class, BilMapId.SiacDCodicebollo_CodiceBollo);	
	}
	

	/**
	 * Find tipo documento by id.
	 *
	 * @param uid the uid
	 * @return the tipo documento
	 */
	public TipoDocumento findTipoDocumentoById(int uid) {
		SiacDDocTipo siacDDocTipo = siacDDocTipoRepository.findOne(uid);
		TipoDocumento tipoDocumento = mapNotNull(siacDDocTipo, TipoDocumento.class, BilMapId.SiacDDocTipo_TipoDocumento);
		return tipoDocumento;
	}
	
	
	/**
	 * Ottiene il tipo documento Allegato Atto
	 * @return il tipo documento
	 */
	public TipoDocumento findTipoDocumentoAllegatoAtto() {
		TipoDocumento td = findTipoDocumentoByCodiceEFamiglia("ALG", TipoFamigliaDocumento.SPESA);
		if(td == null) {
			throw new IllegalStateException("Nessun tipo di documento per la spesa per l'allegato atto con codice \"ALG\" presente su base dati.");
		}
		return td;
	}

	/**
	 * Find tipo documento by codice e famiglia.
	 *
	 * @param codice the codice
	 * @param tipoFamigliaDocumento the tipo famiglia documento
	 * @return the tipo documento
	 */
	public TipoDocumento findTipoDocumentoByCodiceEFamiglia(String codice, TipoFamigliaDocumento tipoFamigliaDocumento) {
		String codiceFamiglia = SiacDDocFamTipoEnum.byTipoFamigliaDocumento(tipoFamigliaDocumento).getCodice();
		SiacDDocTipo siacDDocTipo = siacDDocTipoRepository.findByCodiceTipoCodiceFamTipoAndEnteProprietario(codice, codiceFamiglia, ente.getUid());
		TipoDocumento tipoDocumento = mapNotNull(siacDDocTipo, TipoDocumento.class, BilMapId.SiacDDocTipo_TipoDocumento);
		return tipoDocumento;
	}




	public void impostaFlagSulleQuote(int uidDocumento,TipologiaAttributo tipologiaAttributo, Boolean flag) {
		siacDDocTipoRepository.impostaFlagSulleQuote(uidDocumento, SiacTAttrEnum.byTipologiaAttributo(tipologiaAttributo).getCodice(),
				flag!=null?mapToString(flag.booleanValue()):null, ente.getUid());
		
		
	}


	public List<DettaglioOnere> findRitenuteCollegateBySubdordinativo(SubOrdinativoPagamento sop) {
//		if(sop == null || sop.getUid() == 0) {
//			return Long.valueOf(0L);
//		}
//		Long res = siacTDocRepository.countSiacRDocOnereByOrdTsIs(sop.getUid());
//		return res;
		
		List<SiacRDocOnere> siacRDocOneres = siacTDocRepository.findSiacRDocOnereByOrdTsIs(sop.getUid());
		return convertiLista(siacRDocOneres, DettaglioOnere.class, BilMapId.SiacRDocOnere_DettaglioOnere);
	}

	public BigDecimal calcolaImportoTotaleSubdocumenti(DocumentoSpesa doc) {
		final String methodName = "calcolaImportoTotaleSubdocumenti";
		
		BigDecimal result = siacTDocRepository.sumSubdocImportoByDocId(doc.getUid());
		
		log.debug(methodName, "result: "+ result + " [docUid: "+doc.getUid()+"]");
		return result;
	}
	
	public BigDecimal calcolaImportoTotaleMenoImportoDaDedurreSubdocumenti(DocumentoSpesa doc) {
		final String methodName = "calcolaImportoTotaleMenoImportoDaDedurreSubdocumenti";
		
		BigDecimal result = siacTDocRepository.sumSubdocImportoMenoImportoDaDedurreByDocId(doc.getUid());
		
		log.debug(methodName, "result: "+ result + " [docUid: "+doc.getUid()+"]");
		return result;
	}
	
	public BigDecimal calcolaImportoTotaleNoteCollegateSpesaNonAnnullate(DocumentoSpesa doc) {
		final String methodName = "calcolaImportoTotaleNoteCollegateSpesaNonAnnullate";
		
		BigDecimal result = siacTDocRepository.sumSubdocNoteCreditoColegateByDocId(doc.getUid());
		
		log.debug(methodName, "result: "+ result + " [docUid: "+doc.getUid()+"]");
		return result;
	}
	
	public BigDecimal calcolaImportoTotaleDaDedurreSuFatturaNoteCollegateSpesaNonAnnullate(DocumentoSpesa doc) {
		final String methodName = "calcolaImportoTotaleDaDedurreSuFatturaNoteCollegateSpesaNonAnnullate";
		
		BigDecimal result = siacTDocRepository.sumImportoDaDedurreSuFatturaNoteCreditoColegateByDocId(doc.getUid());
		
		log.debug(methodName, "result: "+ result + " [docUid: "+doc.getUid()+"]");
		return result;
	}
	
	public Long countQuoteSenzaOrdinativo(DocumentoSpesa doc) {
		final String methodName = "countQuoteSenzaOrdinativo";
		
		Long result = siacTDocRepository.countSubdocsSenzaOrdinativoByDocId(doc.getUid());
		
		log.debug(methodName, "result: "+ result + " [docUid: "+doc.getUid()+"]");
		return result;
	}
	
	public Long countQuoteEscludendoQuotaAZero(DocumentoSpesa doc) {
		final String methodName = "countQuoteEscludendoQuotaAZero";

		Long result = siacTDocRepository.countSubdocsEscludendoQuotaAZeroByDocId(doc.getUid());
		
		log.debug(methodName, "result: "+ result + " [docUid: "+doc.getUid()+"]");
		return result;
	}
	
	public Long countQuoteSenzaLiquidazione(DocumentoSpesa doc) {
		final String methodName = "countQuoteSenzaLiquidazione";

		Long result = siacTDocRepository.countSubdocsSenzaLiquidazioneEscludendoQuotaAZeroByDocId(doc.getUid());
		
		log.debug(methodName, "result: "+ result + " [docUid: "+doc.getUid()+"]");
		return result;
	}

	public Long countQuoteSenzaImpegnoOSubImpegno(DocumentoSpesa doc) {
		final String methodName = "countQuoteSenzaImpegnoOSubImpegno";

		Long result = siacTDocRepository.countSubdocsSenzaImpegnoOSubImpegnoEscludendoQuotaAZeroByDocId(doc.getUid());
		
		log.debug(methodName, "result: "+ result + " [docUid: "+doc.getUid()+"]");
		return result;
	}
	
	public Long countQuoteRilevantiIva(DocumentoSpesa doc) {
		final String methodName = "countQuoteRilevantiIva";

		Long result = siacTDocRepository.countSubdocsRilevantiIvaByDocId(doc.getUid());
		
		log.debug(methodName, "result: "+ result + " [docUid: "+doc.getUid()+"]");
		return result;
	}
	
	public Long countQuoteRilevantiIvaSenzaNumeroRegistrazione(DocumentoSpesa doc) {
		final String methodName = "countQuoteRilevantiIvaSenzaNumeroRegistrazione";

		Long result = siacTDocRepository.countSubdocsRilevantiIvaSenzaNumeroRegistrazioneByDocId(doc.getUid());
		
		log.debug(methodName, "result: "+ result + " [docUid: "+doc.getUid()+"]");
		return result;
	}


	

	public BigDecimal calcolaImportoImponibileTotaleOneriPerTipoIvaSplitReverse(DocumentoSpesa doc,  TipoIvaSplitReverse tipoIvaSplitReverse) {
		final String methodName = "calcolaImportoImponibileTotaleOneriPerTipoIvaSplitReverse";
		
		SiacDSplitreverseIvaTipoEnum siacDTipoIvaSplitReverse = SiacDSplitreverseIvaTipoEnum.byTipoIvaSplitReverse(tipoIvaSplitReverse);
		BigDecimal result = siacTDocRepository.sumImportoOneriSplitReverseByDocIdAndTipoSplitreverse(doc.getUid(), siacDTipoIvaSplitReverse.getCodice());
		
		log.debug(methodName, "result: "+ result + " [docUid: "+doc.getUid()+" tipoIvaSplitReverse: "+tipoIvaSplitReverse+"]");
		return result;
	}
	
	public Map<TipoIvaSplitReverse, BigDecimal> getImportiSplitReverseQuoteDocumento(DocumentoSpesa doc) {
		List<Object[]> tmp = siacTDocRepository.sumImportoOneriSplitReverseSubdocByDocId(doc.getUid());
		Map<TipoIvaSplitReverse, BigDecimal> res = new EnumMap<TipoIvaSplitReverse, BigDecimal>(TipoIvaSplitReverse.class);
		
		for(Object[] obj : tmp) {
			TipoIvaSplitReverse tipoIvaSplitReverse = SiacDSplitreverseIvaTipoEnum.byCodice((String)obj[0]).getTipoIvaSplitReverse();
			res.put(tipoIvaSplitReverse, (BigDecimal)obj[1]);
		}
		
		return res;
	}
	
	public Map<TipoIvaSplitReverse, BigDecimal> getImportiSplitReverseRitenuteDocumento(DocumentoSpesa doc) {
		List<Object[]> tmp = siacTDocRepository.sumImportoOneriSplitReverseRitenuteByDocId(doc.getUid());
		Map<TipoIvaSplitReverse, BigDecimal> res = new EnumMap<TipoIvaSplitReverse, BigDecimal>(TipoIvaSplitReverse.class);
		
		for(Object[] obj : tmp) {
			TipoIvaSplitReverse tipoIvaSplitReverse = SiacDSplitreverseIvaTipoEnum.byCodice((String)obj[0]).getTipoIvaSplitReverse();
			res.put(tipoIvaSplitReverse, (BigDecimal)obj[1]);
		}
		
		return res;
	}
	
	
//	public BigDecimal calcolaImportoSplitReverseTotaleQuoteTipoIvaSplitReverse(DocumentoSpesa doc,  TipoIvaSplitReverse tipoIvaSplitReverse) {
//		final String methodName = "calcolaImportoSplitReverseTotaleQuoteTipoIvaSplitReverse";
//		
//		SiacDSplitreverseIvaTipoEnum siacDTipoIvaSplitReverse = SiacDSplitreverseIvaTipoEnum.byTipoIvaSplitReverse(tipoIvaSplitReverse);
//		BigDecimal result = siacTDocRepository.sumImportoSplitReverseQuoteByDocIdAndTipoSplitreverse(doc.getUid(), siacDTipoIvaSplitReverse.getCodice());
//		
//		log.debug(methodName, "result: "+ result + " [docUid: "+doc.getUid()+" tipoIvaSplitReverse: "+tipoIvaSplitReverse+"]");
//		return result;
//	}

	

	public List<DettaglioOnere> findRitenuteByDocumento(DocumentoSpesa ds) {
		SiacTDoc siacTDoc = documentoDao.findById(ds.getUid());
		List<SiacRDocOnere> siacRDocOneres = siacTDoc.getSiacRDocOneres();
		
		return convertiLista(siacRDocOneres, DettaglioOnere.class, BilMapId.SiacRDocOnere_DettaglioOnere);
	}
	
	public List<DettaglioOnere> findRitenuteNonSplitReverseByDocumento(DocumentoSpesa ds) {
		List<SiacRDocOnere> siacRDocOneres = siacTDocRepository.findSiacRDocOnereByDocIdNotSplitReverse(ds.getUid());
		return convertiLista(siacRDocOneres, DettaglioOnere.class, BilMapId.SiacRDocOnere_DettaglioOnere);
	}

	
	public BigDecimal calcolaTotaleDaPagareQuote(DocumentoSpesa documentoSpesa) {
		//SIAC-6048
		BigDecimal importoMenoImportoDaDedurre = siacTDocRepository.sumImportoMenoImportoDaDedurreSubdocNonACoperturaByDocId(documentoSpesa.getUid());
//		BigDecimal importo = siacTDocRepository.sumSubdocImportoByDocId(documentoSpesa.getUid());
		return importoMenoImportoDaDedurre;
	}

	public TipoDocumento findTipoDocumentoByIdDocumento(int uid) {
		SiacTDoc siacTDoc = documentoDao.findById(uid);
		if(siacTDoc == null){
			return null;
		}
		Integer tipoId = siacTDoc.getSiacDDocTipo().getDocTipoId();
		return findTipoDocumentoById(tipoId);
	}

	public Long countDocumentiPadreCollegati(DocumentoSpesa doc) {
		final String methodName = "countDocumentiPadreCollegati";
		
		Long result = siacTDocRepository.countSiacTDocPadreCollegati(doc.getUid());
		log.debug(methodName, "result: "+ result + " [docUid: "+doc.getUid()+"]");
		return result;
	}

	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public List<DocumentoSpesa> ricercaNoteCreditoSpesaCollegateEsclusivamenteAlDocumentoTxRequiresNew(int uidDoc, DocumentoSpesaModelDetail... modelDetails) {
		return ricercaNoteCreditoSpesaCollegateEsclusivamenteAlDocumento(uidDoc, modelDetails);
	}
	
	public List<DocumentoSpesa> ricercaNoteCreditoSpesaCollegateEsclusivamenteAlDocumento(int uidDoc, DocumentoSpesaModelDetail... modelDetails) {
		final String methodName = "ricercaNoteCreditoCollegateEsclusivamenteAlDocumento";
		
		List<SiacTDoc> siacTDocs = siacTDocRepository.findSiacTDocNoteCreditoSpesaColegateByDocId(uidDoc);
		log.debug(methodName, "NoteCredito trovate: "+ siacTDocs.size() + " ");
		
		return convertiLista(siacTDocs, DocumentoSpesa.class, BilMapId.SiacTDoc_DocumentoSpesa_Minimal, Converters.byModelDetails(modelDetails));
	}
	
	public List<DocumentoSpesa> ricercaNoteCreditoSpesaByDocumento(Integer uid, DocumentoSpesaModelDetail... modelDetails) {
		final String methodName = "ricercaNoteCreditoSpesaByDocumento";
		List<String> docFamTipoCodes = Arrays.asList(SiacDDocFamTipoEnum.Spesa.getCodice(), SiacDDocFamTipoEnum.IvaSpesa.getCodice());
		
		List<SiacTDoc> siacTDocs = siacTDocRepository.findSiacTDocNoteCreditoSpesaByDocId(uid, SiacDRelazTipoEnum.NotaCredito.getCodice(), SiacDDocStatoEnum.Annullato.getCodice(), docFamTipoCodes);
		log.debug(methodName, "NoteCredito trovate: " + siacTDocs.size());
		
		return convertiLista(siacTDocs, DocumentoSpesa.class, BilMapId.SiacTDoc_DocumentoSpesa_Minimal, Converters.byModelDetails(modelDetails));
	}

	public Bilancio findBilancioAssociatoAlDocumento(int docUid) {
		final String methodName = "findBilancioAssociatoAlDocumento";
		
		
		List<SiacTBil> siacTBils = siacTDocRepository.findSiacTBilByDocIdWithMovgestT(docUid);
		
		if(siacTBils.size()>1){
			log.warn(methodName, "Il documento con uid: "+ docUid + " ha Impegni con bilanci diversi associata. Provo ad escludere le quote con l'ordinativo. ");
			siacTBils = siacTDocRepository.findSiacTBilByDocIdWithMovgestTSenzaOrdinativo(docUid);
		}
		
		if(siacTBils.isEmpty()){
			siacTBils = siacTDocRepository.findSiacTBilByDocIdWithMovgestTEOrdinativoPiuRecente(docUid);
		}
		
//		if(siacTBils.isEmpty() ||siacTBils.size()>1){
//			log.warn(methodName, "Il documento con uid: "+ docUid + " non ha una un Impegno associato. Provo a dedurre il Bilancio dalla liquidazione se presente. ");
//			//Non ho trovato il bilancio associato all'impegno provo con la liquidazione se presente. (Cosa capitata in PRODUZIONE con dati sporchi!)
//			siacTBils = siacTDocRepository.findSiacTBilByDocIdWithLiquidazione(docUid);
//		}
		
		if(siacTBils.isEmpty() || siacTBils.size() > 1){
			log.error(methodName, "Impossibile dedurre un unico Bilancio associato al documento con uid "+docUid + ". Trovati: "+siacTBils.size() +". Returning null");
			return null;
		}
		
		SiacTBil siacTBil = siacTBils.get(0);
		
		Bilancio bilancio = mapNotNull(siacTBil, Bilancio.class, BilMapId.SiacTBil_Bilancio);

		log.debug(methodName, ""+(bilancio!=null?"Bilancio trovato [uid:"+bilancio.getUid()+"]":"Nessun Bilancio trovato") + " per il Documento con uid: "+docUid);
		return bilancio;
	}

	
	public List<SubdocumentoIvaSpesa> findNoteCreditoIvaAssociate(int docUid) {
		List<SiacTSubdocIva> siacTSubdocIvas = siacTDocRepository.findSiacTSubdocIvaNotaCreditoIvaAssociataAlDoc(docUid);
		return convertiLista(siacTSubdocIvas, SubdocumentoIvaSpesa.class, BilMapId.SiacTSubdocIva_SubdocumentoIvaSpesa);
	}

	public void caricaModelDetails(DocumentoSpesa doc, DocumentoSpesaModelDetail... modelDetails) {
		SiacTDoc siacTDoc = siacTDocRepository.findOne(doc.getUid());
		applyConverters(siacTDoc, doc, Converters.byModelDetails(modelDetails));
		
	}

	public Long countOrdiniAssociati(DocumentoSpesa doc) {
		return siacTOrdineRepository.countOrdiniByDocumentoSpesa(doc.getUid());
	}

	// SIAC-4749
	/**
	 * Conta il numero di subdocumenti aventi impegno/subimpegno corrispondenti all'uid del documento fornito.
	 * 
	 * @param uid l'uid del documento collegato
	 * 
	 * @return il numero di subdocumenti collegati con impegno/subimpegno
	 */
	public Long countSubdocumentiConMovimentoGestioneByUidDocumento(Integer uid) {
		return siacTDocRepository.countSiacTSubdocByDocIdHavingMovgestTs(uid);
	}

	// SIAC-4201
	public List<DocumentoSpesa> findPenaliByUidDocumento(Integer uid, DocumentoSpesaModelDetail... modelDetails) {
		List<SiacTDoc> siacTDocs = siacTDocRepository.findSiacTDocSubordinatiByDocIdAndTipoCollegamento(uid, SiacDRelazTipoEnum.Subdocumento.getCodice(), "PNL");
		return convertiLista(siacTDocs, DocumentoSpesa.class, BilMapId.SiacTDoc_DocumentoSpesa_ModelDetail, Converters.byModelDetails(modelDetails));
	}
	
	//SIAC-8301
	public SiopeDocumentoTipo getSiopeDocumentoTipoByDocId(DocumentoSpesa doc) {
		SiacDSiopeDocumentoTipo siacDDocumentoTipo = siacTDocRepository.findSiacDSiopeDocumentoTipoByDocId(doc.getUid(),ente.getUid());
		return mapNotNull(siacDDocumentoTipo, SiopeDocumentoTipo.class, BilMapId.SiacDSiopeDocumentoTipo_SiopeDocumentoTipo);
	}

}
