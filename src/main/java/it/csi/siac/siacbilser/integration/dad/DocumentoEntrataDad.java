/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
import it.csi.siac.siacbilser.integration.entity.SiacDCodicebollo;
import it.csi.siac.siacbilser.integration.entity.SiacDDocStato;
import it.csi.siac.siacbilser.integration.entity.SiacDDocTipo;
import it.csi.siac.siacbilser.integration.entity.SiacRDocStato;
import it.csi.siac.siacbilser.integration.entity.SiacTBil;
import it.csi.siac.siacbilser.integration.entity.SiacTDoc;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdocIva;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocFamTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTAttrEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.base.Converters;
import it.csi.siac.siacbilser.model.TipologiaAttributo;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.model.CodiceBollo;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoEntrataModelDetail;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.PreDocumentoEntrata;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfin2ser.model.TipoFamigliaDocumento;
import it.csi.siac.siacfinser.model.Accertamento;

/**
 * Data access delegate di un DocumentoEntrata.
 *
 * @author Domenico
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class DocumentoEntrataDad extends ExtendedBaseDadImpl {
	
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
	
	/** The siac d codicebollo repository. */
	@Autowired
	private SiacDCodicebolloRepository siacDCodicebolloRepository;
	
	/** The siac t doc repository */
	@Autowired
	private SiacTDocRepository siacTDocRepository;
	

	/**
	 * Find documento entrata by id utilizzando il mapping completo.
	 *
	 * @param uid the uid
	 * @return the documento entrata
	 */
	public DocumentoEntrata findDocumentoEntrataById(Integer uid) {
		return findDocumentoEntrataById(uid, BilMapId.SiacTDoc_DocumentoEntrata);
	}
	
	/**
	 * Find documento entrata by id utilizzando il mapping medium.
	 *
	 * @param docId the doc id
	 * @return the documento entrata
	 */
	public DocumentoEntrata findDocumentoEntrataByIdMedium(Integer docId) {
		return findDocumentoEntrataById(docId, BilMapId.SiacTDoc_DocumentoEntrata_Medium);
	}
	
	/**
	 * Find documento entrata by id utilizzando il mapping minimal
	 *
	 * @param uid the uid
	 * @return the documento entrata
	 */
	public DocumentoEntrata findDocumentoEntrataByIdMinimal(Integer uid) {
		return findDocumentoEntrataById(uid, BilMapId.SiacTDoc_DocumentoEntrata_Minimal);
	}
	
	/**
	 * Find documento entrata by id.
	 *
	 * @param uid the uid
	 * @return the documento entrata
	 */
	private  DocumentoEntrata findDocumentoEntrataById(Integer uid, BilMapId bilMapId_SiacTDoc_DocumentoEntrata) {
		final String methodName = "findDocumentoEntrataById";		
		log.debug(methodName, "uid: "+ uid);
		SiacTDoc siacTDoc = documentoDao.findById(uid);
		if(siacTDoc == null) {
			log.debug(methodName, "Impossibile trovare il DocumentoEntrata con id: " + uid);
		}else{
			log.debug(methodName, "siacTDoc trovata con uid: " + siacTDoc.getUid());
		}
		log.debug(methodName, "mapId: "+bilMapId_SiacTDoc_DocumentoEntrata);
		return  mapNotNull(siacTDoc, DocumentoEntrata.class, bilMapId_SiacTDoc_DocumentoEntrata);
	}
	
	
	/**
	 * Find documento entrata by id.
	 *
	 * @param uid the uid
	 * @return the documento entrata
	 */
	public DocumentoEntrata findDocumentoEntrataById(Integer uid, DocumentoEntrataModelDetail...modelDetails) {
		return findDocumentoEntrataById(uid, BilMapId.SiacTDoc_DocumentoEntrata_Minimal, modelDetails);
	}
	
	/**
	 * Find documento entrata by id.
	 *
	 * @param uid the uid
	 * @return the documento entrata
	 */
	public DocumentoEntrata findDocumentoEntrataByIdModelDetail(Integer uid, DocumentoEntrataModelDetail...modelDetails) {
		return findDocumentoEntrataById(uid, BilMapId.SiacTDoc_DocumentoEntrata_ModelDetail, modelDetails);
	}
	
	/**
	 * Find documento entrata by id.
	 *
	 * @param uid the uid
	 * @return the documento entrata
	 */
	private DocumentoEntrata findDocumentoEntrataById(Integer uid, BilMapId mapId, DocumentoEntrataModelDetail...modelDetails) {
		final String methodName = "findDocumentoEntrataById[modelDetails]";		
		log.debug(methodName, "uid: "+ uid);
		SiacTDoc siacTDoc = documentoDao.findById(uid);
		if(siacTDoc == null) {
			log.debug(methodName, "Impossibile trovare il DocumentoEntrata con id: " + uid);
		}else{
			log.debug(methodName, "siacTDoc trovata con uid: " + siacTDoc.getUid());
		}
		return mapNotNull(siacTDoc, DocumentoEntrata.class, mapId, Converters.byModelDetails(modelDetails));
	}
	
	/**
	 * Effettua un flush ed un clear dell'EntityManager.
	 * 
	 */
	public void flushAndClear() {
		documentoDao.flushAndClear();
	}
	
	/**
	 * Find documento entrata by id after flush and clear.
	 *
	 * @param uid the uid
	 * @return the documento entrata
	 */
	public DocumentoEntrata findDocumentoEntrataByIdAfterFlushAndClear(Integer uid) {
		documentoDao.flushAndClear();
		return findDocumentoEntrataById(uid);
	}
	

	/**
	 * Inserisci anagrafica documento entrata.
	 *
	 * @param documento the documento
	 */
	public void inserisciAnagraficaDocumentoEntrata(DocumentoEntrata documento) {
		SiacTDoc siacTDoc = buildSiacTDoc(documento);
		siacTDoc.setLoginCreazione(loginOperazione);
		siacTDoc.setLoginModifica(loginOperazione);
		documentoDao.create(siacTDoc);
		documento.setUid(siacTDoc.getUid());
	}

	/**
	 * Aggiorna anagrafica documento entrata.
	 *
	 * @param documento the documento
	 */
	public void aggiornaAnagraficaDocumentoEntrata(DocumentoEntrata documento) {
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
	private SiacTDoc buildSiacTDoc(DocumentoEntrata documento) {
		SiacTDoc siacTDoc = new SiacTDoc();
		siacTDoc.setLoginOperazione(loginOperazione);
		documento.setLoginOperazione(loginOperazione);
		map(documento, siacTDoc, BilMapId.SiacTDoc_DocumentoEntrata);
		return siacTDoc;
	}

	/**
	 * Ricerca sintetica documento entrata.
	 *
	 * @param doc the doc
	 * @param attoAmministrativo the atto amministrativo
	 * @param accertamento the accertamento
	 * @param rilevanteIva the rilevante iva
	 * @param parametriPaginazione the parametri paginazione
	 * @return the lista paginata
	 */
	public ListaPaginata<DocumentoEntrata> ricercaSinteticaDocumentoEntrata(DocumentoEntrata doc, AttoAmministrativo attoAmministrativo,
			Accertamento accertamento, Boolean rilevanteIva, ElencoDocumentiAllegato elencoDocumenti, Boolean contabilizzaGenPcc, PreDocumentoEntrata predocumentoEntrata, ParametriPaginazione parametriPaginazione) {

		Page<SiacTDoc> lista = documentoDao.ricercaSinteticaDocumento(doc.getEnte().getUid(),
				SiacDDocFamTipoEnum.Entrata,
				doc.getAnno(), 
				StringUtils.trimToNull(doc.getNumero()),
				null,
				doc.getDataEmissione(),
				doc.getDataOperazione(),
				doc.getTipoDocumento() != null ? doc.getTipoDocumento().getUid() : null,
				doc.getStatoOperativoDocumento() != null ? SiacDDocStatoEnum.byStatoOperativo(doc.getStatoOperativoDocumento()) : null, 
				null,
				rilevanteIva, 
				null, 
				accertamento != null ? accertamento.getUid() : null, 
				mapToUidIfNotZero(attoAmministrativo),
				attoAmministrativo != null && attoAmministrativo.getAnno() != 0 ? String.valueOf(attoAmministrativo.getAnno()) : null, 
				attoAmministrativo != null && attoAmministrativo.getNumero() != 0 ? attoAmministrativo.getNumero() : null,
				attoAmministrativo != null ? mapToUidIfNotZero(attoAmministrativo.getTipoAtto()) : null,
				//TODO sac		
				attoAmministrativo != null ?  mapToUidIfNotZero(attoAmministrativo.getStrutturaAmmContabile()) : null,		
		
				doc.getSoggetto() != null ? doc.getSoggetto().getUid() : null, 
				elencoDocumenti != null ? elencoDocumenti.getAnno() : null,
				elencoDocumenti != null ? elencoDocumenti.getNumero() : null,
				null,
				null,
				null,
				null,
				null,
				doc.getAnnoRepertorio(),
				doc.getDataRepertorio(),
				doc.getNumeroRepertorio(),
				doc.getRegistroRepertorio(),
				contabilizzaGenPcc,
				doc.getStatoSDI(),
				//SIAC-8245
				predocumentoEntrata != null ? predocumentoEntrata.getNumero() : null,
				toPageable(parametriPaginazione));

		return toListaPaginata(lista, DocumentoEntrata.class, BilMapId.SiacTDoc_DocumentoEntrata);

	}
	
	
	/**
	 * Ricerca sintetica documento entrata.
	 *
	 * @param doc the doc
	 * @param attoAmministrativo the atto amministrativo
	 * @param accertamento the accertamento
	 * @param rilevanteIva the rilevante iva
	 * @param parametriPaginazione the parametri paginazione
	 * @return the lista paginata
	 */
	public ListaPaginata<DocumentoEntrata> ricercaSinteticaDocumentoEntrata(DocumentoEntrata doc, AttoAmministrativo attoAmministrativo,
			Accertamento accertamento, Boolean rilevanteIva, ElencoDocumentiAllegato elencoDocumenti, Boolean contabilizzaGenPcc, PreDocumentoEntrata predocumentoEntrata, ParametriPaginazione parametriPaginazione,
			DocumentoEntrataModelDetail...documentoEntrataModelDetails) {

		//SIAC-6565-CR1215
		Page<SiacTDoc> lista = documentoDao.ricercaSinteticaDocumento(doc.getEnte().getUid(),
				SiacDDocFamTipoEnum.Entrata,
				doc.getAnno(), 
				StringUtils.trimToNull(doc.getNumero()),
				null,
				doc.getDataEmissione(),
				doc.getDataOperazione(),
				doc.getTipoDocumento() != null ? doc.getTipoDocumento().getUid() : null,
				doc.getStatoOperativoDocumento() != null ? SiacDDocStatoEnum.byStatoOperativo(doc.getStatoOperativoDocumento()) : null, 
				null,
				rilevanteIva, 
				null, 
				accertamento != null ? accertamento.getUid() : null, 
				mapToUidIfNotZero(attoAmministrativo),
				attoAmministrativo != null && attoAmministrativo.getAnno() != 0 ? String.valueOf(attoAmministrativo.getAnno()) : null, 
				attoAmministrativo != null && attoAmministrativo.getNumero() != 0 ? attoAmministrativo.getNumero() : null,
				attoAmministrativo != null ? mapToUidIfNotZero(attoAmministrativo.getTipoAtto()) : null,
				//TODO sac		
				attoAmministrativo != null ?  mapToUidIfNotZero(attoAmministrativo.getStrutturaAmmContabile()) : null,		
	
				
				doc.getSoggetto() != null ? doc.getSoggetto().getUid() : null, 
				elencoDocumenti != null ? elencoDocumenti.getAnno() : null,
				elencoDocumenti != null ? elencoDocumenti.getNumero() : null,
				null,
				null,
				null,
				null,
				null,
				doc.getAnnoRepertorio(),
				doc.getDataRepertorio(),
				doc.getNumeroRepertorio(),
				doc.getRegistroRepertorio(),
				contabilizzaGenPcc,
				doc.getStatoSDI(),
				//SIAC-6780
				predocumentoEntrata != null? predocumentoEntrata.getNumero() : null,
				toPageable(parametriPaginazione));

		return toListaPaginata(lista, DocumentoEntrata.class, BilMapId.SiacTDoc_DocumentoEntrata_ModelDetail, documentoEntrataModelDetails);

	}
	
	/**
	 * Ricerca sintetica testata documento entrata.
	 *
	 * @param doc the doc
	 * @param attoAmministrativo the atto amministrativo
	 * @param accertamento the accertamento
	 * @param rilevanteIva the rilevante iva
	 * @param parametriPaginazione the parametri paginazione
	 * @return the lista paginata
	 */
	public ListaPaginata<DocumentoEntrata> ricercaSinteticaTestataDocumentoEntrata(DocumentoEntrata doc, AttoAmministrativo attoAmministrativo,
			Accertamento accertamento, Boolean rilevanteIva, ParametriPaginazione parametriPaginazione) {

		Page<SiacTDoc> lista = documentoDao.ricercaSinteticaDocumento(doc.getEnte().getUid(),
				SiacDDocFamTipoEnum.IvaEntrata,
				doc.getAnno(), 
				StringUtils.trimToNull(doc.getNumero()),
				null,
				doc.getDataEmissione(),
				doc.getDataOperazione(),
				doc.getTipoDocumento() != null ? doc.getTipoDocumento().getUid() : null,
				doc.getStatoOperativoDocumento() != null ? SiacDDocStatoEnum.byStatoOperativo(doc.getStatoOperativoDocumento()) : null, 
				null,
				rilevanteIva, 
				null, 
				accertamento != null ? accertamento.getUid() : null, 
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

		return toListaPaginata(lista, DocumentoEntrata.class, BilMapId.SiacTDoc_DocumentoEntrata);

	}
	
	/**
	 * Ricerca sintetica documento entrata importo totale.
	 *
	 * @param doc the doc
	 * @param attoAmministrativo the atto amministrativo
	 * @param accertamento the accertamento
	 * @param rilevanteIva the rilevante iva
	 * @param parametriPaginazione the parametri paginazione
	 * @return the big decimal
	 */
	public BigDecimal ricercaSinteticaDocumentoEntrataImportoTotale(DocumentoEntrata doc, AttoAmministrativo attoAmministrativo,
			Accertamento accertamento, Boolean rilevanteIva, ElencoDocumentiAllegato elencoDocumenti, Boolean contabilizzaGenPcc, PreDocumentoEntrata preDocumentoEntrata,ParametriPaginazione parametriPaginazione) {
		
		BigDecimal importoTotale = documentoDao.ricercaSinteticaDocumentoImportoTotale(doc.getEnte().getUid(),
				SiacDDocFamTipoEnum.Entrata,
				doc.getAnno(),
				StringUtils.trimToNull(doc.getNumero()),
				null,
				doc.getDataEmissione(),
				doc.getDataOperazione(),
				doc.getTipoDocumento() != null ? doc.getTipoDocumento().getUid() : null,
				doc.getStatoOperativoDocumento() != null ? SiacDDocStatoEnum.byStatoOperativo(doc.getStatoOperativoDocumento()) : null,
				null,
				rilevanteIva, 
				null, 
				accertamento != null ? accertamento.getUid() : null,  

				mapToUidIfNotZero(attoAmministrativo),
				attoAmministrativo != null && attoAmministrativo.getAnno() != 0 ? String.valueOf(attoAmministrativo.getAnno()) : null, 
				attoAmministrativo != null && attoAmministrativo.getNumero() != 0 ? attoAmministrativo.getNumero() : null,
				attoAmministrativo != null ? mapToUidIfNotZero(attoAmministrativo.getTipoAtto()) : null,						
				//TODO sac		
				attoAmministrativo != null ?  mapToUidIfNotZero(attoAmministrativo.getStrutturaAmmContabile()) : null,		
				doc.getSoggetto() != null ? doc.getSoggetto().getUid() : null,
				elencoDocumenti != null ? elencoDocumenti.getAnno() : null,
				elencoDocumenti != null ? elencoDocumenti.getNumero() : null,
				null,
				null,
				null,
				null,
				null,
				doc.getAnnoRepertorio(),
				doc.getDataRepertorio(),
				doc.getNumeroRepertorio(),
				doc.getRegistroRepertorio(),
				contabilizzaGenPcc,
				doc.getStatoSDI(),
				//SIAC-6780
				preDocumentoEntrata!= null ? preDocumentoEntrata.getNumero() : null,
				toPageable(parametriPaginazione));
		
		return importoTotale;
		
	}	
	
	
	/**
	 * Ricerca sintetica testata documento entrata importo totale.
	 *
	 * @param doc the doc
	 * @param attoAmministrativo the atto amministrativo
	 * @param accertamento the accertamento
	 * @param rilevanteIva the rilevante iva
	 * @param parametriPaginazione the parametri paginazione
	 * @return the big decimal
	 */
	public BigDecimal ricercaSinteticaTestataDocumentoEntrataImportoTotale(DocumentoEntrata doc, AttoAmministrativo attoAmministrativo,
			Accertamento accertamento, Boolean rilevanteIva, ParametriPaginazione parametriPaginazione) {
		
		BigDecimal importoTotale = documentoDao.ricercaSinteticaDocumentoImportoTotale(doc.getEnte().getUid(),
				SiacDDocFamTipoEnum.IvaEntrata,
				doc.getAnno(),
				StringUtils.trimToNull(doc.getNumero()),
				null,
				doc.getDataEmissione(),
				doc.getDataOperazione(),
				doc.getTipoDocumento() != null ? doc.getTipoDocumento().getUid() : null,
				doc.getStatoOperativoDocumento() != null ? SiacDDocStatoEnum.byStatoOperativo(doc.getStatoOperativoDocumento()) : null,
				null,
				rilevanteIva, 
				null, 
				accertamento != null ? accertamento.getUid() : null,  
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
		
		return importoTotale;
		
	}
					
	
	/**
	 * Ricerca puntuale documento entrata.
	 *
	 * @param doc the doc
	 * @param statoOperativoDocumentoDaEscludere the stato operativo documento da escludere
	 * @return the documento entrata
	 */
	public DocumentoEntrata ricercaPuntualeDocumentoEntrata(DocumentoEntrata doc, StatoOperativoDocumento statoOperativoDocumentoDaEscludere) {
		SiacTDoc siacTDoc = ricercaPuntualeDocumentoEntrata(doc, statoOperativoDocumentoDaEscludere, SiacDDocFamTipoEnum.Entrata);
		return mapNotNull(siacTDoc, DocumentoEntrata.class, BilMapId.SiacTDoc_DocumentoEntrata_Base);
	}
	
	/**
	 * Ricerca puntuale documento entrata.
	 *
	 * @param doc the doc
	 * @param statoOperativoDocumentoDaEscludere the stato operativo documento da escludere
	 * @param modelDetails the model details
	 * @return the documento entrata
	 */
	public DocumentoEntrata ricercaPuntualeDocumentoEntrata(DocumentoEntrata doc, StatoOperativoDocumento statoOperativoDocumentoDaEscludere, DocumentoEntrataModelDetail... modelDetails) {
		SiacTDoc siacTDoc = ricercaPuntualeDocumentoEntrata(doc, statoOperativoDocumentoDaEscludere, SiacDDocFamTipoEnum.Entrata);
		return mapNotNull(siacTDoc, DocumentoEntrata.class, BilMapId.SiacTDoc_DocumentoEntrata_ModelDetail, Converters.byModelDetails(modelDetails));
	}
	
	/**
	 * Ricerca puntuale documento iva entrata (testata).
	 *
	 * @param doc the doc
	 * @param statoOperativoDocumentoDaEscludere the stato operativo documento da escludere
	 * @return the documento entrata
	 */
	public DocumentoEntrata ricercaPuntualeDocumentoIvaEntrata(DocumentoEntrata doc, StatoOperativoDocumento statoOperativoDocumentoDaEscludere) {
		SiacTDoc siacTDoc = ricercaPuntualeDocumentoEntrata(doc, statoOperativoDocumentoDaEscludere, SiacDDocFamTipoEnum.IvaEntrata);
		return mapNotNull(siacTDoc, DocumentoEntrata.class, BilMapId.SiacTDoc_DocumentoEntrata_Base);
	}
	
	
	private SiacTDoc ricercaPuntualeDocumentoEntrata(DocumentoEntrata doc, StatoOperativoDocumento statoOperativoDocumentoDaEscludere, SiacDDocFamTipoEnum tipoDoc) {
		final String methodName = "ricercaPuntualeDocumentoEntrata";
		
		ParametriPaginazione parametriPaginazione = new ParametriPaginazione();		
		parametriPaginazione.setNumeroPagina(0);
		parametriPaginazione.setElementiPerPagina(1);
		
		Page<SiacTDoc> lista = documentoDao.ricercaSinteticaDocumento(
				doc.getEnte().getUid(),
				tipoDoc,
				doc.getAnno(),
				null,//doc.getNumero(),
				doc.getNumero(),
				null, //doc.getDataEmissione(),
				null,//doc.getDataOperazione(),
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
				null,
				toPageable(parametriPaginazione));

		if(lista.getContent().isEmpty()) {
			log.debug(methodName, "Nessun documento trovato con chiave:"+doc.getDescAnnoNumeroUidTipoDocUidSoggettoStato());
			return null;
		}
		
		return lista.getContent().get(0);
	}

	/**
	 * Aggiorna stato documento entrata.
	 *
	 * @param uidDocumento the uid documento
	 * @param statoOperativoDocumento the stato operativo documento
	 */
	public void aggiornaStatoDocumentoEntrata(Integer uidDocumento, StatoOperativoDocumento statoOperativoDocumento) {
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
		
		siacRDocStatoRepository.save(siacRDocStato);
		
	}

	/**
	 * Find documenti collegati by id documento.
	 *
	 * @param uid the uid
	 * @return the documento entrata
	 */
	public DocumentoEntrata findDocumentiCollegatiByIdDocumento(Integer uid) {
		final String methodName = "findDocumentiCollegatiByIdDocumento";		
		log.debug(methodName, "uid: "+ uid);
		SiacTDoc siacTDoc = documentoDao.findById(uid);
		if(siacTDoc == null) {
			log.debug(methodName, "Impossibile trovare il DocumentoEntrata con id: " + uid);
		}
		return mapNotNull(siacTDoc, DocumentoEntrata.class, BilMapId.SiacTDoc_DocumentoEntrata_Collegati);
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
	 * @return
	 */
	public TipoDocumento findTipoDocumentoAllegatoAtto() {
		TipoDocumento td = findTipoDocumentoByCodiceEFamiglia("ALG", TipoFamigliaDocumento.ENTRATA);
		if(td == null) {
			throw new IllegalStateException("Nessun tipo di documento per l'entrata per l'allegato atto con codice \"ALG\" presente su base dati.");
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

	
	public void impostaFlagOrdinativoSulleQuote(int uidDocumento, Boolean flag) {
		siacDDocTipoRepository.impostaFlagSulleQuote(uidDocumento, SiacTAttrEnum.FlagOrdinativoSingolo.getCodice(),
				flag!=null?mapToString(flag.booleanValue()):null, ente.getUid());
		
	}
	

	/**
	 * Find codice bollo esente.
	 *
	 * @return the codice bollo
	 */
	public CodiceBollo findCodiceBolloEsente() {		
		return findCodiceBolloByCode("99");
	}
	
	
	/**
	 * Find codice bollo by code.
	 *
	 * @param codice the codice
	 * @return the codice bollo
	 */
	public CodiceBollo findCodiceBolloByCode(String codice) {
		SiacDCodicebollo siacDCodicebollo = siacDCodicebolloRepository.findCodiciBolloByCodice(codice, ente.getUid());		
		return map(siacDCodicebollo, CodiceBollo.class, BilMapId.SiacDCodicebollo_CodiceBollo);	
	}
	
	public void impostaFlagSulleQuote(int uidDocumento,TipologiaAttributo tipologiaAttributo, Boolean flag) {
		siacDDocTipoRepository.impostaFlagSulleQuote(uidDocumento, SiacTAttrEnum.byTipologiaAttributo(tipologiaAttributo).getCodice(),
				flag!=null?mapToString(flag.booleanValue()):null, ente.getUid());
		
	}
	
	
	/**
	 * Aggiorna stato SDI documento entrata.
	 * 
	 * @param uidDocumento the uid documento
	 * @param statoSDIDocumento the stato operativo documento
	 */
	public void aggiornaStatoSDIDocumentoEntrata(Integer uidDocumento, String statoSDIDocumento, String esitoStatoSDI, Date dataCambioStato) {
		SiacTDoc siacTDoc = documentoDao.findById(uidDocumento);
		
		siacTDoc.setStatoSDI(statoSDIDocumento);
		siacTDoc.setEsitoStatoSDI(esitoStatoSDI);
		//SIAC-7562 - 25/06/2020 - CM e GM
		if(dataCambioStato != null)
			siacTDoc.setDataCambioStatoFel(dataCambioStato);
		siacTDocRepository.saveAndFlush(siacTDoc);
		
	}
	
	/**
	 * Aggiorna numero documento di entrata ....SIAC-6677
	 * 
	 */
	public void aggiornaNumeroDocumentoEntrata(Integer uidDocumento, String numero) {
		
		SiacTDoc siacTDoc = documentoDao.findById(uidDocumento);
		
		if(siacTDoc.getDocNumeroPrimaAutoIva()==null){
			String numeroPrimaNumerazioneAutomaticaIva = siacTDoc.getDocNumero();
			siacTDoc.setDocNumeroPrimaAutoIva(numeroPrimaNumerazioneAutomaticaIva);
			siacTDoc.setDocNumero(numero);
			siacTDocRepository.saveAndFlush(siacTDoc);
		}
		
		
		
	}
	
	//######################################################################################################
	//################################################# NEW ################################################
	
	public BigDecimal calcolaImportoTotaleSubdocumenti(DocumentoEntrata doc) {
		final String methodName = "calcolaImportoTotaleSubdocumenti";
		
		BigDecimal result = siacTDocRepository.sumSubdocImportoByDocId(doc.getUid());
		
		log.debug(methodName, "result: "+ result + " [docUid: "+doc.getUid()+"]");
		return result;
	}
	
	public BigDecimal calcolaImportoTotaleMenoImportoDaDedurreSubdocumenti(DocumentoEntrata doc) {
		final String methodName = "calcolaImportoTotaleMenoImportoDaDedurreSubdocumenti";
		
		BigDecimal result = siacTDocRepository.sumSubdocImportoMenoImportoDaDedurreByDocId(doc.getUid());
		
		log.debug(methodName, "result: "+ result + " [docUid: "+doc.getUid()+"]");
		return result;
	}
	
	public BigDecimal calcolaImportoTotaleNoteCollegateEntrataNonAnnullate(DocumentoEntrata doc) {
		final String methodName = "calcolaImportoTotaleNoteCollegateEntrataNonAnnullate";
		
		BigDecimal result = siacTDocRepository.sumSubdocNoteCreditoColegateByDocId(doc.getUid());
		
		log.debug(methodName, "result: "+ result + " [docUid: "+doc.getUid()+"]");
		return result;
	}
	
	public BigDecimal calcolaImportoTotaleDaDedurreSuFatturaNoteCollegateEntrataNonAnnullate(DocumentoEntrata doc) {
		final String methodName = "calcolaImportoTotaleDaDedurreSuFatturaNoteCollegateEntrataNonAnnullate";
		
		BigDecimal result = siacTDocRepository.sumImportoDaDedurreSuFatturaNoteCreditoColegateByDocId(doc.getUid());
		
		log.debug(methodName, "result: "+ result + " [docUid: "+doc.getUid()+"]");
		return result;
	}
	
	public Long countQuoteSenzaOrdinativo(DocumentoEntrata doc) {
		final String methodName = "countQuoteSenzaOrdinativo";
		
		Long result = siacTDocRepository.countSubdocsSenzaOrdinativoByDocId(doc.getUid());
		
		log.debug(methodName, "result: "+ result + " [docUid: "+doc.getUid()+"]");
		return result;
	}
	
	public Long countQuoteEscludendoQuotaAZero(DocumentoEntrata doc) {
		final String methodName = "countQuoteEscludendoQuotaAZero";

		Long result = siacTDocRepository.countSubdocsEscludendoQuotaAZeroByDocId(doc.getUid());
		
		log.debug(methodName, "result: "+ result + " [docUid: "+doc.getUid()+"]");
		return result;
	}
	
	public Long countQuoteSenzaDeterminaDiIncasso(DocumentoEntrata doc) {
		final String methodName = "countQuoteSenzaLiquidazione";

		Long result = siacTDocRepository.countSubdocsSenzaDeterminaDiIncassoEscludendoQuotaAZeroByDocId(doc.getUid());
		
		log.debug(methodName, "result: "+ result + " [docUid: "+doc.getUid()+"]");
		return result;
	}

	public Long countQuoteSenzaAccertamentoOSubAccertamento(DocumentoEntrata doc) {
		final String methodName = "countQuoteSenzaAccertamentoOSubAccertamento";

		Long result = siacTDocRepository.countSubdocsSenzaAccertamentoOSubAccertamentoEscludendoQuotaAZeroByDocId(doc.getUid());
		
		log.debug(methodName, "result: "+ result + " [docUid: "+doc.getUid()+"]");
		return result;
	}
	
	public Long countQuoteRilevantiIva(DocumentoEntrata doc) {
		final String methodName = "countQuoteRilevantiIva";

		Long result = siacTDocRepository.countSubdocsRilevantiIvaByDocId(doc.getUid());
		
		log.debug(methodName, "result: "+ result + " [docUid: "+doc.getUid()+"]");
		return result;
	}
	
	public Long countQuoteRilevantiIvaSenzaNumeroRegistrazione(DocumentoEntrata doc) {
		final String methodName = "countQuoteRilevantiIvaSenzaNumeroRegistrazione";

		Long result = siacTDocRepository.countSubdocsRilevantiIvaSenzaNumeroRegistrazioneByDocId(doc.getUid());
		
		log.debug(methodName, "result: "+ result + " [docUid: "+doc.getUid()+"]");
		return result;
	}

	public TipoDocumento findTipoDocumentoByIdDocumento(int uid) {
		SiacTDoc siacTDoc = documentoDao.findById(uid);
		if(siacTDoc == null){
			return null;
		}
		Integer tipoId = siacTDoc.getSiacDDocTipo().getDocTipoId();
		return findTipoDocumentoById(tipoId);
	}

	public Long countDocumentiPadreCollegati(DocumentoEntrata doc) {
		final String methodName = "countDocumentiPadreCollegati";
		
		Long result = siacTDocRepository.countSiacTDocPadreCollegati(doc.getUid());
		log.debug(methodName, "result: "+ result + " [docUid: "+doc.getUid()+"]");
		return result;
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public List<DocumentoEntrata> ricercaNoteCreditoEntrataCollegateEsclusivamenteAlDocumentoTxRequiresNew(int uidDoc, DocumentoEntrataModelDetail... modelDetails) {
		return ricercaNoteCreditoEntrataCollegateEsclusivamenteAlDocumento(uidDoc, modelDetails);
	}
	
	public List<DocumentoEntrata> ricercaNoteCreditoEntrataCollegateEsclusivamenteAlDocumento(int uidDoc, DocumentoEntrataModelDetail... modelDetails) {
		final String methodName = "ricercaNoteCreditoCollegateEsclusivamenteAlDocumento";
		
		List<SiacTDoc> siacTDocs = siacTDocRepository.findSiacTDocNoteCreditoEntrataColegateByDocId(uidDoc);
		log.debug(methodName, "NoteCredito trovate: "+ siacTDocs.size() + " ");
		
		return convertiLista(siacTDocs, DocumentoEntrata.class, BilMapId.SiacTDoc_DocumentoEntrata_Minimal, Converters.byModelDetails(modelDetails));
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
		
		if(siacTBils.isEmpty() || siacTBils.size() > 1){
			log.error(methodName, "Impossibile dedurre un unico Bilancio associato al documento con uid "+docUid + ". Trovati: "+siacTBils.size() +". Returning null");
			return null;
		}
		
		SiacTBil siacTBil = siacTBils.get(0);
		
		Bilancio bilancio = mapNotNull(siacTBil, Bilancio.class, BilMapId.SiacTBil_Bilancio);

		log.debug(methodName, ""+(bilancio!=null?"Bilancio trovato [uid:"+bilancio.getUid()+"]":"Nessun Bilancio trovato") + " per il Documento con uid: "+docUid);
		return bilancio;
	}

	public List<SubdocumentoIvaEntrata> findNoteCreditoIvaAssociate(int docUid) {
		List<SiacTSubdocIva> siacTSubdocIvas = siacTDocRepository.findSiacTSubdocIvaNotaCreditoIvaAssociataAlDoc(docUid);
		return convertiLista(siacTSubdocIvas, SubdocumentoIvaEntrata.class, BilMapId.SiacTSubdocIva_SubdocumentoIvaEntrata);
	}

	public void caricaModelDetails(DocumentoEntrata doc, DocumentoEntrataModelDetail modelDetails) {
		SiacTDoc siacTDoc = siacTDocRepository.findOne(doc.getUid());
		applyConverters(siacTDoc, doc, Converters.byModelDetails(modelDetails));
		
	}

}
