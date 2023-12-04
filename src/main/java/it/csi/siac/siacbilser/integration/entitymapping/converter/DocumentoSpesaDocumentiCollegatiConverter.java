/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.dao.SiacTDocRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDDocFamTipo;
import it.csi.siac.siacbilser.integration.entity.SiacDRelazTipo;
import it.csi.siac.siacbilser.integration.entity.SiacRDoc;
import it.csi.siac.siacbilser.integration.entity.SiacTDoc;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocFamTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDRelazTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoRelazione;


/**
 * The Class DocumentoSpesaDocumentiCollegatiConverter.
 */
@Component
public class DocumentoSpesaDocumentiCollegatiConverter extends ExtendedDozerConverter<DocumentoSpesa, SiacTDoc > {
	
	/** The eef. */
	@Autowired
	private EnumEntityFactory eef;
	
	@Autowired
	private SiacTDocRepository repository;
	

	/**
	 * Instantiates a new documento spesa documenti collegati converter.
	 */
	public DocumentoSpesaDocumentiCollegatiConverter() {
		super(DocumentoSpesa.class, SiacTDoc.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public DocumentoSpesa convertFrom(SiacTDoc siacTDoc, DocumentoSpesa documento) {
		final String methodName = "convertFrom";
		
		log.debug(methodName, "documento.uid: "+ documento.getUid());
		
		if(siacTDoc.getSiacRDocsFiglio()!=null) {
			for(SiacRDoc r: siacTDoc.getSiacRDocsFiglio()){
				if(r.getDataCancellazione()==null) {
					//SiacTDoc siacTDocPadre = r.getSiacTDocPadre();
					SiacTDoc siacTDocPadre = repository.findOne(r.getSiacTDocPadre().getUid());
					log.debug(methodName, "siacTDocPadre.getSiacDDocTipo().uid  " + siacTDocPadre.getSiacDDocTipo().getUid());
					
					SiacDDocFamTipo siacDDocFamTipo = repository.findFamTipoByDocTipo(siacTDocPadre.getSiacDDocTipo().getUid());
					String docFamTipoCode = siacDDocFamTipo.getDocFamTipoCode();
					
					log.debug(methodName, "docFamTipoCode " + docFamTipoCode);
					
					TipoRelazione tipoRelazione = SiacDRelazTipoEnum.byCodice(r.getSiacDRelazTipo().getRelazTipoCode()).getTipoRelazione();
					
					if(TipoRelazione.NOTA_CREDITO.equals(tipoRelazione) && r.getDocImportoDaDedurre() == null){
						throw new BusinessException("Il documento ["+siacTDoc.getUid() +"] ha una relazione di tipo nota di credito ma il campo 'Importo da dedurre su fattura' non e' valorizzato.");	
					}
					
					if(SiacDDocFamTipoEnum.Entrata.getCodice().equals(docFamTipoCode)) {
						DocumentoEntrata docEntrata = mapNotNull(siacTDocPadre, DocumentoEntrata.class, BilMapId.SiacTDoc_DocumentoEntrata_Base);
						docEntrata.setTipoRelazione(tipoRelazione);
						docEntrata.setImportoDaDedurreSuFattura(r.getDocImportoDaDedurre());
						documento.addDocumentoEntrataPadre(docEntrata);
						 
					}else if(SiacDDocFamTipoEnum.Spesa.getCodice().equals(docFamTipoCode)) {
						DocumentoSpesa docSpesa = mapNotNull(siacTDocPadre, DocumentoSpesa.class, BilMapId.SiacTDoc_DocumentoSpesa_Base);
						docSpesa.setTipoRelazione(tipoRelazione);
						docSpesa.setImportoDaDedurreSuFattura(r.getDocImportoDaDedurre());
						documento.addDocumentoSpesaPadre(docSpesa);
					}
				}
			}
		}
		
		if(siacTDoc.getSiacRDocsPadre()!=null) {
			for(SiacRDoc r: siacTDoc.getSiacRDocsPadre()){
				if(r.getDataCancellazione()==null) {
					//SiacTDoc siacTDocFiglio = r.getSiacTDocFiglio();
					SiacTDoc siacTDocFiglio = repository.findOne(r.getSiacTDocFiglio().getUid());
					log.debug(methodName, "siacTDocFiglio.getSiacDDocTipo().uid  " + siacTDocFiglio.getSiacDDocTipo().getUid());
					
					SiacDDocFamTipo siacDDocFamTipo = repository.findFamTipoByDocTipo(siacTDocFiglio.getSiacDDocTipo().getUid());
					String docFamTipoCode = siacDDocFamTipo.getDocFamTipoCode();
					
					log.debug(methodName, "docFamTipoCode: " + docFamTipoCode);
					
					TipoRelazione tipoRelazione = SiacDRelazTipoEnum.byCodice(r.getSiacDRelazTipo().getRelazTipoCode()).getTipoRelazione();
					if(TipoRelazione.NOTA_CREDITO.equals(tipoRelazione) && r.getDocImportoDaDedurre() == null){
						throw new BusinessException("la nota di credito [uid: "+siacTDocFiglio.getUid() +"] collegata al documento ["+siacTDoc.getUid() +"] deve avere il campo 'Importo da dedurre su fattura' valorizzato.");	
					}
					
					if(SiacDDocFamTipoEnum.Entrata.getCodice().equals(docFamTipoCode)) {
						DocumentoEntrata docEntrata = mapNotNull(siacTDocFiglio, DocumentoEntrata.class, BilMapId.SiacTDoc_DocumentoEntrata_Base);
						docEntrata.setTipoRelazione(tipoRelazione);
						docEntrata.setImportoDaDedurreSuFattura(r.getDocImportoDaDedurre());
						documento.addDocumentoEntrataFiglio(docEntrata);
						 
					}else if(SiacDDocFamTipoEnum.Spesa.getCodice().equals(docFamTipoCode)) {
						DocumentoSpesa docSpesa = mapNotNull(siacTDocFiglio, DocumentoSpesa.class, BilMapId.SiacTDoc_DocumentoSpesa_Base);
						docSpesa.setTipoRelazione(tipoRelazione);
						docSpesa.setImportoDaDedurreSuFattura(r.getDocImportoDaDedurre());
						documento.addDocumentoSpesaFiglio(docSpesa);
					}
				}
			}
		}
		
		
		log.debug(methodName, "fine");
		
		return documento;
	}



	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTDoc convertTo(DocumentoSpesa documento, SiacTDoc dest) {	
		
		for(DocumentoSpesa doc : documento.getListaDocumentiSpesaFiglio()){
			addFiglio(dest, doc);
		}
		
		for(DocumentoEntrata doc : documento.getListaDocumentiEntrataFiglio()){
			addFiglio(dest, doc);
		}
		
		for(DocumentoSpesa doc : documento.getListaDocumentiSpesaPadre()){
			if(doc != null &&  doc.getUid()>0) {
				addPadre(dest, doc);
			}
		}
		
		for(DocumentoEntrata doc : documento.getListaDocumentiEntrataPadre()){
			if(doc != null &&  doc.getUid()>0) {
				addPadre(dest, doc);
			}
		}		
		
		return dest;	
	}
	
	

	/**
	 * Aggiunge una relazione in cui questo documento (parametro dest) è padre, mentre il figlio è il documento passato nel parametro doc.
	 *
	 * @param dest the dest
	 * @param doc the doc
	 */
	private void addFiglio(SiacTDoc dest, Documento<?, ?> doc) {
		if(doc==null || doc.getUid()<=0){
			return;
		}
		
		
		SiacRDoc r = new SiacRDoc();
		r.setSiacTDocPadre(dest);
		
		
//		SiacTDoc siacTDoc = repository.findOne(doc.getUid());
		SiacTDoc siacTDoc = new SiacTDoc();
		siacTDoc.setUid(doc.getUid());
		r.setSiacTDocFiglio(siacTDoc);
		
		r.setSiacTDocPadre(dest);
		r.setLoginOperazione(dest.getLoginOperazione());
		r.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		r.setDocImportoDaDedurre(doc.getImportoDaDedurreSuFattura());
		
		if(doc.getTipoRelazione()==null){ 
			//se non è specificato il tipoi di relazione di default è DocumentoSubordinato
			doc.setTipoRelazione(TipoRelazione.SUBORDINATO);
		}
		
		SiacDRelazTipo siacDRelazTipo = eef.getEntity(SiacDRelazTipoEnum.byTipoRelazione(doc.getTipoRelazione()), dest.getSiacTEnteProprietario().getUid());
		r.setSiacDRelazTipo(siacDRelazTipo);
		
		dest.addSiacRDocsPadre(r);
	}
	
	/**
	 * Aggiunge una relazione in cui questo documento (parametro dest) è figlio, mentre il padre è il documento passato nel parametro doc.
	 *
	 * @param dest t
	 * @param bigDecimal he dest
	 * @param doc the doc
	 */
	private void addPadre(SiacTDoc dest, Documento<?, ?> doc) {
		if(doc==null || doc.getUid()<=0){
			return;
		}
		
		SiacRDoc r = new SiacRDoc();
		r.setSiacTDocPadre(dest);
		r.setDocImportoDaDedurre(doc.getImportoDaDedurreSuFattura());
		
		SiacTDoc siacTDoc = new SiacTDoc();
		siacTDoc.setUid(doc.getUid());
		r.setSiacTDocPadre(siacTDoc);
		
		r.setSiacTDocFiglio(dest);
		r.setLoginOperazione(dest.getLoginOperazione());
		r.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		
		
		if(doc.getTipoRelazione()==null){ 
			//se non è specificato il tipoi di relazione di default è DocumentoSubordinato
			doc.setTipoRelazione(TipoRelazione.SUBORDINATO);
		}
		
		
		SiacDRelazTipo siacDRelazTipo = eef.getEntity(SiacDRelazTipoEnum.byTipoRelazione(doc.getTipoRelazione()), dest.getSiacTEnteProprietario().getUid());
		r.setSiacDRelazTipo(siacDRelazTipo);
		
		
		dest.addSiacRDocsFiglio(r); 
	}
	
	
	



	

}
