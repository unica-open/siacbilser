/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.dao.SiacDDocTipoRepository;
import it.csi.siac.siacbilser.integration.dao.SiacRDocRepository;
import it.csi.siac.siacbilser.integration.dao.SiacRDocStatoRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTDocNumRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTDocRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTSubdocRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDDocStato;
import it.csi.siac.siacbilser.integration.entity.SiacDDocTipo;
import it.csi.siac.siacbilser.integration.entity.SiacDRelazTipo;
import it.csi.siac.siacbilser.integration.entity.SiacRDoc;
import it.csi.siac.siacbilser.integration.entity.SiacRDocAttr;
import it.csi.siac.siacbilser.integration.entity.SiacRDocStato;
import it.csi.siac.siacbilser.integration.entity.SiacRSubdocAttoAmm;
import it.csi.siac.siacbilser.integration.entity.SiacTAccount;
import it.csi.siac.siacbilser.integration.entity.SiacTAttoAmm;
import it.csi.siac.siacbilser.integration.entity.SiacTAttr;
import it.csi.siac.siacbilser.integration.entity.SiacTDoc;
import it.csi.siac.siacbilser.integration.entity.SiacTDocNum;
import it.csi.siac.siacbilser.integration.entity.SiacTEnteProprietario;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDRelazTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTAttrEnum;
import it.csi.siac.siacbilser.model.TipologiaAttributo;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.TipoRelazione;

/**
 * Data access delegate di un Documento.
 **/
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class DocumentoDad extends ExtendedBaseDadImpl {
	
	@Autowired
	private SiacTDocRepository siacTDocRepository;
	@Autowired
	private SiacTSubdocRepository siacTSubdocRepository;
	@Autowired
	private SiacTDocNumRepository siacTDocNumRepository;
	@Autowired
	private SiacRDocRepository siacRDocRepository;
	/** The siac r doc stato repository. */
	@Autowired
	private SiacRDocStatoRepository siacRDocStatoRepository;
	@Autowired
	private SiacDDocTipoRepository siacDDocTipoRepository;
	
	
	/** The eef. */
	@Autowired
	private EnumEntityFactory eef;
	
	
	public <S extends Subdocumento<?, ?>> void updateImportoDocumento(BigDecimal importoDaAggiungere, BigDecimal importoDaSottrarre, S subdocumento) {
		SiacTDoc siacTDoc = siacTDocRepository.findBySubdocId(subdocumento.getUid());
		updateImportoDocumento(importoDaAggiungere != null ? importoDaAggiungere : BigDecimal.ZERO,
			importoDaSottrarre != null ? importoDaSottrarre : BigDecimal.ZERO,
			siacTDoc);
		siacTDocRepository.flush();
	}
	
	public <D extends Documento<?, ?>> void updateImportoDocumento(BigDecimal importoDaAggiungere, BigDecimal importoDaSottrarre, D documento) {
		SiacTDoc siacTDoc = siacTDocRepository.findOne(documento.getUid());
		updateImportoDocumento(importoDaAggiungere != null ? importoDaAggiungere : BigDecimal.ZERO,
			importoDaSottrarre != null ? importoDaSottrarre : BigDecimal.ZERO,
			siacTDoc);
		siacTDocRepository.flush();
	}
	
	private void updateImportoDocumento(BigDecimal importoDaAggiungere, BigDecimal importoDaSottrarre, SiacTDoc siacTDoc) {
		final String methodName = "updateImportoDocumento";
		BigDecimal importoAttuale = siacTDoc.getDocImporto();
		if(importoAttuale == null) {
			importoAttuale = BigDecimal.ZERO;
		}
		
		BigDecimal importoNuovo = importoAttuale.add(importoDaAggiungere).subtract(importoDaSottrarre);
		log.info(methodName, "Modifico importo del Documento [uid: "+siacTDoc.getUid()+"]: importoAttuale: " + importoAttuale + " importoNuovo: " + importoNuovo);
		
		siacTDoc.setDocImporto(importoNuovo);
		
	}
	
	/**
	 * Conta il numero di subdocumenti corrispondenti all'uid del documento fornito.
	 * 
	 * @param uid l'uid del documento collegato
	 * 
	 * @return il numero di subdocumenti collegati
	 */
	public Long countSubdocumentiByUidDocumento(Integer uid) {
		Long count = siacTDocRepository.countSiacTSubdocByDocId(uid);
		return count;
	}
	
	public Boolean determinaFlagRilevanteIva(Integer uidDocumento) {
		List<SiacTSubdoc> lista = siacTSubdocRepository.findQuoteRilevantiIvaByIdDocumento(uidDocumento);
		return lista != null && !lista.isEmpty();
	}
	
	
	public Boolean findFlagContabilizzaGenPccDocumento(Documento<?,?> doc) {
		return  siacTDocRepository.findFlagContabilizzaGenPccByDocId(doc.getUid());
	}


	public void aggiornaAttoAmministrativo(int uidSubdoc, AttoAmministrativo attoAmministrativo) {
		SiacTSubdoc siacTSubdoc = siacTSubdocRepository.findOne(uidSubdoc);
		
		if(siacTSubdoc==null) {
			throw new IllegalArgumentException("Impossibile trovare subdocumento con uid: "+uidSubdoc);
		}
		
		Date now = new Date();
		if(siacTSubdoc.getSiacRSubdocAttoAmms()!=null) {
			for(SiacRSubdocAttoAmm r : siacTSubdoc.getSiacRSubdocAttoAmms()){
				r.setDataCancellazioneIfNotSet(now);
			}			
		}
		
		if(attoAmministrativo != null) {
	
			SiacTAttoAmm siacTAttoAmm = new SiacTAttoAmm();
			siacTAttoAmm.setUid(attoAmministrativo.getUid());
			SiacRSubdocAttoAmm siacRSubdocAttoAmm = new SiacRSubdocAttoAmm();
			siacRSubdocAttoAmm.setSiacTAttoAmm(siacTAttoAmm);
			siacRSubdocAttoAmm.setSiacTSubdoc(siacTSubdoc);
			siacRSubdocAttoAmm.setSiacTEnteProprietario(siacTSubdoc.getSiacTEnteProprietario());
			siacRSubdocAttoAmm.setLoginOperazione(loginOperazione);
			
			siacRSubdocAttoAmm.setDataModificaInserimento(now);
			
			siacTSubdoc.addSiacRSubdocAttoAmm(siacRSubdocAttoAmm);
		}
		
	}
	
	
	/**
	 * Ottiene il numero di un nuovo Documento per l'anno specificato.
	 *
	 * @param annoDocumento the anno documento
	 * @return numero progressivo nell'anno
	 */
	@Transactional(propagation=Propagation.MANDATORY)
	public String staccaNumeroDocumento(Integer annoDocumento,Integer docTipoId) {
		final String methodName = "staccaNumeroDocumento";
		
		SiacTDocNum siacTDocNum = siacTDocNumRepository.findByAnno(annoDocumento,docTipoId, ente.getUid());
		
		Date now = new Date();		
		if(siacTDocNum == null) {
			siacTDocNum = new SiacTDocNum();
			siacTDocNum.setDocAnno(annoDocumento);	
			
			SiacTEnteProprietario siacTEnteProprietario = new SiacTEnteProprietario();
			siacTEnteProprietario.setUid(ente.getUid());
			siacTDocNum.setSiacTEnteProprietario(siacTEnteProprietario);
			
			siacTDocNum.setDataCreazione(now);
			siacTDocNum.setDataInizioValidita(now);
			siacTDocNum.setLoginOperazione(loginOperazione);

			SiacDDocTipo siacDDocTipo = siacDDocTipoRepository.findOne(docTipoId);
			siacTDocNum.setSiacDDocTipo(siacDDocTipo);
			
			siacTDocNum.setDocNumero(1000); //La numerazione parte da 1000
			
		}
		
		siacTDocNum.setDataModifica(now);	
		
		siacTDocNumRepository.saveAndFlush(siacTDocNum);		
		Integer numeroDoc = siacTDocNum.getDocNumero();
		
		String result = "SIAC"+ numeroDoc;
		log.info(methodName, "returning result: "+ result + " per l'anno "+ annoDocumento);
		return result;
	}
	
	
	public void aggiornaFlagContabilizzaGenPcc(@SuppressWarnings("rawtypes") Documento doc, Boolean flagContabilizzaGenPcc) {
		String methodName = "aggiornaFlagContabilizzaGenPcc";
		log.debug(methodName , "aggiorno il flagContabilizzaGenPcc del doc con uid : " + doc.getUid() + "con valore: " + flagContabilizzaGenPcc);
		SiacTDoc siacTDoc = siacTDocRepository.findOne(doc.getUid());
		siacTDoc.setDocContabilizzaGenpcc(flagContabilizzaGenPcc);
		siacTDocRepository.saveAndFlush(siacTDoc);
		doc.setContabilizzaGenPcc(flagContabilizzaGenPcc);
	}
	
	public void aggiornaAttributo(@SuppressWarnings("rawtypes") Documento doc, TipologiaAttributo tipologiaAttributo, Object valore) {
		String methodName = "aggiornaAttributo";
		
		SiacTAttrEnum attrEnum = SiacTAttrEnum.byTipologiaAttributo(tipologiaAttributo);
		if(valore!=null && !attrEnum.getFieldType().equals(valore.getClass())){
			throw new IllegalArgumentException("Il tipo previsto per l'attributo "+attrEnum 
					+" e' "+attrEnum.getFieldType().getSimpleName() +" tipo passato: "+valore.getClass());
		}
		
		SiacTDoc siacTDoc = siacTDocRepository.findOne(doc.getUid());
		
		Date now = new Date();
		
		for (SiacRDocAttr rDocAttr : siacTDoc.getSiacRDocAttrs()) {
			if(rDocAttr.getDataCancellazione()==null 
					&& attrEnum.getCodice().equals(rDocAttr.getSiacTAttr().getAttrCode())){
				rDocAttr.setDataCancellazione(now);
				break;
			}
		}
		
		SiacRDocAttr r = new SiacRDocAttr();
		r.setDataModificaInserimento(now);
		r.setSiacTAttr(eef.getEntity(attrEnum, siacTDoc.getSiacTEnteProprietario().getUid(), SiacTAttr.class));
		r.setSiacTEnteProprietario(siacTDoc.getSiacTEnteProprietario());
		//r.setSiacTDoc(siacTDoc);
		r.setLoginOperazione(loginOperazione);
		
		impostaValoreAttributo(r, attrEnum, valore);
		
		siacTDoc.addSiacRDocAttr(r);
		
		siacTDocRepository.saveAndFlush(siacTDoc);
		log.debug(methodName, "aggiornato attibuto "+attrEnum+" con valore "+valore + " per il documento con uid: "+siacTDoc.getUid());
	}

	private void impostaValoreAttributo(SiacRDocAttr r, SiacTAttrEnum attrEnum, Object valore) {
		if(Boolean.class.equals(attrEnum.getFieldType())){
			r.setBoolean_(Boolean.TRUE.equals(valore)?"S":"N");
		} else if(String.class.equals(attrEnum.getFieldType())){
			r.setTesto((String)valore);
		} else if(Date.class.equals(attrEnum.getFieldType())){
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ITALY); 
			r.setTesto(valore!=null?df.format(valore):null);
		} else if(BigDecimal.class.equals(attrEnum.getFieldType())){
			r.setNumerico((BigDecimal)valore);
		} else if(Integer.class.equals(attrEnum.getFieldType())){
			r.setNumerico(valore!=null?new BigDecimal((Integer)valore):null);
		}
	}
	

    public void collegaDocumenti(Documento<?,?> docFiglio, Documento<?,?> docPadre, TipoRelazione tipoRelazione) {
		SiacRDoc siacRDoc = buildSiacRDoc(docFiglio, docPadre, tipoRelazione);
		siacRDocRepository.saveAndFlush(siacRDoc);
	}
	
	public void scollegaDocumenti(Documento<?,?> docFiglio, Documento<?,?> docPadre, TipoRelazione tipoRelazione) {
		SiacDRelazTipoEnum siacDRelazTipoEnum = SiacDRelazTipoEnum.byTipoRelazione(tipoRelazione);
		SiacRDoc siacRDoc = siacRDocRepository.findRelazione(docFiglio.getUid(), docPadre.getUid(), siacDRelazTipoEnum.getCodice());
		if(siacRDoc == null){
			throw new IllegalArgumentException("Relazione " +tipoRelazione.getCodice()+ " tra il doc : " +docFiglio.getUid()+ " e il doc: " +docPadre.getUid()+ " non presente in archivio.");
		}
		siacRDoc.setDataCancellazioneIfNotSet(new Date());
		siacRDocRepository.flush();
	}

	private SiacRDoc buildSiacRDoc(Documento<?,?> docFiglio, Documento<?,?> docPadre, TipoRelazione tipoRelazione) {
		SiacRDoc siacRDoc = new SiacRDoc();
		Date now = new Date();
		
		SiacTDoc siacTDoc = new SiacTDoc();
		SiacTDoc siacTDocPadre = new SiacTDoc();
		SiacTEnteProprietario siacTEnteProprietario = new SiacTEnteProprietario();
		siacTDoc.setUid(docFiglio.getUid());
		siacTDocPadre.setUid(docPadre.getUid());
		siacTEnteProprietario.setUid(ente.getUid());
		SiacDRelazTipoEnum siacDRelazTipoEnum = SiacDRelazTipoEnum.byTipoRelazione(tipoRelazione);
		SiacDRelazTipo siacDRelazTipo = eef.getEntity(siacDRelazTipoEnum, ente.getUid(), SiacDRelazTipo.class);
		
		siacRDoc.setSiacTDocFiglio(siacTDoc);
		siacRDoc.setSiacTDocPadre(siacTDocPadre);
		siacRDoc.setSiacDRelazTipo(siacDRelazTipo);
		siacRDoc.setDocImportoDaDedurre(docFiglio.getImportoDaDedurreSuFattura()); //ImportoDaDedurreSuFattura e ' valorizzato solo per TipoRelazione NCD. Il docFiglio e' la NCD. 
		siacRDoc.setLoginOperazione(loginOperazione);
		siacRDoc.setSiacTEnteProprietario(siacTEnteProprietario);
		siacRDoc.setDataModificaInserimento(now);
		
		return siacRDoc;
	}

	public boolean isRelazionePresente(Documento<?,?> docFiglio, Documento<?,?> docPadre, TipoRelazione tipoRelazione) {
		SiacDRelazTipoEnum siacDRelazTipoEnum = SiacDRelazTipoEnum.byTipoRelazione(tipoRelazione);
		SiacRDoc siacRDoc = siacRDocRepository.findRelazione(docFiglio.getUid(), docPadre.getUid(), siacDRelazTipoEnum.getCodice());
		return siacRDoc != null;
	}

	public Long countDocumentiPadre(Integer uidDocFiglio, SiacDRelazTipoEnum siacDRelazTipoEnum) {
		return siacRDocRepository.countDocumentiPadre(uidDocFiglio, siacDRelazTipoEnum.getCodice());
	}
	
	public Long countDocumentiFigli(Integer uidDocPadre) {
		return siacRDocRepository.countDocumentiFigli(uidDocPadre);
	}
	
	
	/**
	 * Aggiorna stato documento.
	 *
	 * @param uidDocumento the uid documento
	 * @param statoOperativoDocumento the stato operativo documento
	 */
	public void aggiornaStatoDocumento(Integer uidDocumento, StatoOperativoDocumento statoOperativoDocumento) {
		SiacTDoc siacTDoc = siacTDocRepository.findOne(uidDocumento);
		
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
	
	public boolean isCollegatoCEC(Documento<?,?> doc){
		SiacTDoc siacTDoc = siacTDocRepository.findOne(doc.getUid());
		return Boolean.TRUE.equals(siacTDoc.getDocCollegatoCec());
	}
	
	public StatoOperativoDocumento findStatoOperativoDocumento(Integer uidDocumento) {
		SiacDDocStato siacDDocStato = siacTDocRepository.findStatoByDocId(uidDocumento);
		SiacDDocStatoEnum siacDDocStatoEnum = SiacDDocStatoEnum.byCodice(siacDDocStato.getDocStatoCode());
		return siacDDocStatoEnum.getStatoOperativo();
	}
	
	public BigDecimal findImportoDocumento(Documento<?, ?> doc) {
		final String methodName = "findImportoDocumento";
		BigDecimal result = siacTDocRepository.findImportoDocById(doc.getUid());
		log.debug(methodName, "importo documento [uid:"+doc.getUid()+"]: "+ result); 
		if(result == null){
			throw new IllegalArgumentException("Impossibile trovare l'importo per il documento con uid:"+doc.getUid());
		}
		return result;
	}
	
	public BigDecimal findImportoNettoDocumento(Integer uid) {
		final String methodName = "findImportoDocumento";
		List<BigDecimal> result = siacTDocRepository.findImportoNettoDocById(uid);
		if(result == null || result.isEmpty()){
			throw new IllegalArgumentException("Impossibile trovare l'importo per il documento con uid:" + uid);
		}
		if(result.size() > 1) {
			throw new IllegalStateException("Il documento con uid " + uid + " ha dei dati incongruenti su base dati");
		}
		BigDecimal importo = result.get(0);
		log.debug(methodName, "importo documento [uid:" + uid + "]: "+ importo); 
		return importo;
	}
	
	public List<String> findNumeroDocumentoLikeNumero(Documento<?, ?> documento) {
		Collection<String> stati = new ArrayList<String>();
		for(SiacDDocStatoEnum sddse : SiacDDocStatoEnum.values()) {
			stati.add(sddse.getCodice());
		}
		stati.remove(SiacDDocStatoEnum.Annullato.getCodice());
		
		return siacTDocRepository.findDocNumeroByEnteAndAnnoAndLikeNumeroAndTipoDocAndStato(documento.getEnte().getUid(), documento.getAnno(),
				documento.getNumero(), documento.getTipoDocumento().getCodice(), documento.getTipoDocumento().getTipoFamigliaDocumento().getCodice(), stati, documento.getSoggetto().getUid());
	}

	public BigDecimal calcolaTotaleImportoDaDedurreSuFattura(int uidNotaDiCredito) {
		String methodName = "calcolaTotaleImportoDadedurreSuFattura";
		BigDecimal result = siacTDocRepository.sumImportoDaDedurreSuFattureByDocFiglioUid(uidNotaDiCredito);
		log.debug(methodName, "result: "+ result);
		return result;
	}
	
	//SIAC-6988 Inizio FL
	public String  findStatoSDIDocumento(Documento<?,?> doc) {
		
		SiacTDoc siacTDoc = siacTDocRepository.findOne(doc.getUid());
		if(siacTDoc==null) {
			throw new IllegalArgumentException("Impossibile trovare Doc con uid: "+doc.getUid());
		}
		return siacTDoc.getStatoSDI();
		
	}
	//SIAC-6988 Fine FL
	
	
}
