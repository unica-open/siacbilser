/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.FatturaFELDao;
import it.csi.siac.siacbilser.integration.dao.SiacRDocSirfelRepository;
import it.csi.siac.siacbilser.integration.dao.SirfelDTipoDocumentoRepository;
import it.csi.siac.siacbilser.integration.dao.SirfelTFatturaNumRepository;
import it.csi.siac.siacbilser.integration.dao.SirfelTFatturaRepository;
import it.csi.siac.siacbilser.integration.dao.SirfelTPrestatoreNumRepository;
import it.csi.siac.siacbilser.integration.dao.TipoDocumentoFELDao;
import it.csi.siac.siacbilser.integration.entity.SiacDBilElemDetCompTipo;
import it.csi.siac.siacbilser.integration.entity.SiacDDocTipo;
import it.csi.siac.siacbilser.integration.entity.SiacRDocSirfel;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entity.SiacTDoc;
import it.csi.siac.siacbilser.integration.entity.SiacTEnteProprietario;
import it.csi.siac.siacbilser.integration.entity.SirfelDTipoDocumento;
import it.csi.siac.siacbilser.integration.entity.SirfelDTipoDocumentoPK;
import it.csi.siac.siacbilser.integration.entity.SirfelTCassaPrevidenziale;
import it.csi.siac.siacbilser.integration.entity.SirfelTCassaPrevidenzialePK;
import it.csi.siac.siacbilser.integration.entity.SirfelTCausale;
import it.csi.siac.siacbilser.integration.entity.SirfelTCausalePK;
import it.csi.siac.siacbilser.integration.entity.SirfelTDatiGestionali;
import it.csi.siac.siacbilser.integration.entity.SirfelTDatiGestionaliPK;
import it.csi.siac.siacbilser.integration.entity.SirfelTDettaglioPagamento;
import it.csi.siac.siacbilser.integration.entity.SirfelTDettaglioPagamentoPK;
import it.csi.siac.siacbilser.integration.entity.SirfelTFattura;
import it.csi.siac.siacbilser.integration.entity.SirfelTFatturaNum;
import it.csi.siac.siacbilser.integration.entity.SirfelTFatturaPK;
import it.csi.siac.siacbilser.integration.entity.SirfelTFattureCollegate;
import it.csi.siac.siacbilser.integration.entity.SirfelTFattureCollegatePK;
import it.csi.siac.siacbilser.integration.entity.SirfelTOrdineAcquisto;
import it.csi.siac.siacbilser.integration.entity.SirfelTOrdineAcquistoDettaglio;
import it.csi.siac.siacbilser.integration.entity.SirfelTOrdineAcquistoDettaglioPK;
import it.csi.siac.siacbilser.integration.entity.SirfelTOrdineAcquistoPK;
import it.csi.siac.siacbilser.integration.entity.SirfelTPagamento;
import it.csi.siac.siacbilser.integration.entity.SirfelTPagamentoPK;
import it.csi.siac.siacbilser.integration.entity.SirfelTPortaleFatture;
import it.csi.siac.siacbilser.integration.entity.SirfelTProtocollo;
import it.csi.siac.siacbilser.integration.entity.SirfelTRiepilogoBeni;
import it.csi.siac.siacbilser.integration.entity.SirfelTRiepilogoBeniPK;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SirfelDTipoDocumentoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.integration.entitymapping.FelMapId;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.MacrotipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.PropostaDefaultComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.SottotipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.StatoTipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.TipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.TipoDocFEL;
import it.csi.siac.siacbilser.model.TipoGestioneComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.ric.RicercaPuntualeCapitoloEPrev;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoComponenteImportiCapitoloModelDetail;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.sirfelser.model.CassaPrevidenzialeFEL;
import it.csi.siac.sirfelser.model.CausaleFEL;
import it.csi.siac.sirfelser.model.DatiGestionaliFEL;
import it.csi.siac.sirfelser.model.DettaglioOrdineAcquistoFEL;
import it.csi.siac.sirfelser.model.DettaglioPagamentoFEL;
import it.csi.siac.sirfelser.model.FatturaFEL;
import it.csi.siac.sirfelser.model.FattureCollegateFEL;
import it.csi.siac.sirfelser.model.OrdineAcquistoFEL;
import it.csi.siac.sirfelser.model.PagamentoFEL;
import it.csi.siac.sirfelser.model.PortaleFattureFEL;
import it.csi.siac.sirfelser.model.ProtocolloFEL;
import it.csi.siac.sirfelser.model.RiepilogoBeniFEL;
import it.csi.siac.sirfelser.model.StatoAcquisizioneFEL;

/**
 *Gestione per i tipi documenti della fatturazione elettronica
 * 
 * @author FL
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class TipoDocumentoFELDad extends ExtendedBaseDadImpl {
	
	@Autowired
	private SirfelTFatturaRepository sirfelTFatturaRepository;
	@Autowired
	private SirfelDTipoDocumentoRepository sirfelDTipoDocumentoRepository;
	@Autowired
	private SiacRDocSirfelRepository siacRDocSirfelRepository;
	@Autowired
	private SirfelTFatturaNumRepository sirfelTFatturaNumRepository;
 
	
	@Autowired
	private FatturaFELDao fatturaFELDao;

	@Autowired
	private TipoDocumentoFELDao tipoDocumentoFELDao;

	
	/**
	 * Conta i documenti presenti in fattura
	 * @param codice del tipo documento
	 * @return il numero di record collegati
	 */
	public Long countFatturaBySirfelDTipoDocumento(String codice) {
		return sirfelDTipoDocumentoRepository.countFatturaBySirfelDTipoDocumento(codice);
	}
	
	public List<TipoDocFEL> ricercaTipoDocumentoFEL(TipoDocFEL tipoDocFEL) {
		List<SirfelDTipoDocumento> sirfelDTipoDocumentos = sirfelDTipoDocumentoRepository.findSirfelDTipoDocumentoByEnte(siacTEnteProprietario.getEnteProprietarioId());
		
		List<TipoDocFEL> elencoTipoDocReturn = new ArrayList<TipoDocFEL>(sirfelDTipoDocumentos.size());
		
		for (SirfelDTipoDocumento tipoDocDB : sirfelDTipoDocumentos) {			
			TipoDocFEL tipoDocToAdd = map(tipoDocDB, TipoDocFEL.class,BilMapId.SirfelDTipoDocumento_TipoDocFEL); 
			elencoTipoDocReturn.add(tipoDocToAdd);
		}
		return elencoTipoDocReturn;
		
	}
	
	
	public TipoDocFEL inserisceTipoDocumentoFEL(TipoDocFEL tipoDocFEL) {
		SirfelDTipoDocumento sirfelDTipoDocumento = new SirfelDTipoDocumento();
		SirfelDTipoDocumentoPK sirfelDTipoDocumentoPK = new SirfelDTipoDocumentoPK();
		sirfelDTipoDocumentoPK.setEnteProprietarioId(siacTEnteProprietario.getEnteProprietarioId());
		
		sirfelDTipoDocumento.setId(sirfelDTipoDocumentoPK);
		map(tipoDocFEL, sirfelDTipoDocumento , BilMapId.SirfelDTipoDocumento_TipoDocFEL);
		
		
		sirfelDTipoDocumento.setFlagBilancio("S");
		SirfelDTipoDocumento sirfelDTipoDocumentoIns = tipoDocumentoFELDao.create(sirfelDTipoDocumento);
		
		tipoDocFEL.setCodice(sirfelDTipoDocumentoIns.getCodice());
		
		return tipoDocFEL;
	}
	
	
	
	public TipoDocFEL aggiornaTipoDocumentoFEL(TipoDocFEL tipoDocFEL) {
		
		SirfelDTipoDocumento sirfelDTipoDocumento = new SirfelDTipoDocumento();
		SirfelDTipoDocumentoPK sirfelDTipoDocumentoPK = new SirfelDTipoDocumentoPK();
		sirfelDTipoDocumentoPK.setEnteProprietarioId(siacTEnteProprietario.getEnteProprietarioId());
		sirfelDTipoDocumentoPK.setCodice(tipoDocFEL.getCodice());
		sirfelDTipoDocumento.setId(sirfelDTipoDocumentoPK);
		
		map(tipoDocFEL, sirfelDTipoDocumento, BilMapId.SirfelDTipoDocumento_TipoDocFEL);
		sirfelDTipoDocumento.setFlagBilancio("S");
		 
		tipoDocumentoFELDao.update(sirfelDTipoDocumento);

		return tipoDocFEL;
	}
	
	
	
	public TipoDocFEL eliminaTipoDocumentoFEL(TipoDocFEL tipoDocFEL) {
		
		SirfelDTipoDocumento sirfelDTipoDocumento = new SirfelDTipoDocumento();
		
		SirfelDTipoDocumentoPK sirfelDTipoDocumentoPK = new SirfelDTipoDocumentoPK();
		sirfelDTipoDocumentoPK.setEnteProprietarioId(siacTEnteProprietario.getEnteProprietarioId());
		sirfelDTipoDocumentoPK.setCodice(tipoDocFEL.getCodice());
				
		sirfelDTipoDocumento.setId(sirfelDTipoDocumentoPK);
		//Eliminazione fisica dell'elemento
		sirfelDTipoDocumentoRepository.delete(sirfelDTipoDocumento);

		return tipoDocFEL;
	}
	
	
	
	
	
	public ListaPaginata<TipoDocFEL> ricercaSinteticaTipoDocumentoFEL(
			TipoDocFEL tipoDocFEL,
		 
			ParametriPaginazione parametriPaginazione) {
		
		Page<SirfelDTipoDocumento> sirfelDTipoDocumentoPagedList = tipoDocumentoFELDao.ricercaPaginataTipoDocumentoFEL(
				siacTEnteProprietario.getEnteProprietarioId(), 
				tipoDocFEL.getCodice(),
				tipoDocFEL.getDescrizione(),
				tipoDocFEL.getTipoDocContabiliaEntrata(),
				tipoDocFEL.getTipoDocContabiliaSpesa(),
				toPageable(parametriPaginazione));
		 
		return toListaPaginata(sirfelDTipoDocumentoPagedList, TipoDocFEL.class, BilMapId.SirfelDTipoDocumento_TipoDocFEL);
	}
	
	
	
	public TipoDocFEL ricercaDettaglioTipoDocumentoFEL(TipoDocFEL tipoDocFEL) {
		
		SirfelDTipoDocumento sirfelDTipoDocumento = findTipoDocumentoFEL(tipoDocFEL);
		
		if (sirfelDTipoDocumento!=null) {
			SirfelDTipoDocumentoPK sirfelDTipoDocumentoPK = new SirfelDTipoDocumentoPK();
			sirfelDTipoDocumentoPK.setEnteProprietarioId(siacTEnteProprietario.getEnteProprietarioId());
			sirfelDTipoDocumentoPK.setCodice(tipoDocFEL.getCodice());
			sirfelDTipoDocumento.setId(sirfelDTipoDocumentoPK);
			
			map(sirfelDTipoDocumento, tipoDocFEL, BilMapId.SirfelDTipoDocumento_TipoDocFEL);
		}
		return tipoDocFEL;
	}

	
	private SirfelDTipoDocumento findTipoDocumentoFEL(TipoDocFEL tipoDocFEL) {
//		SirfelDTipoDocumentoPK pk = new SirfelDTipoDocumentoPK();
//		pk.setCodice(tipoDocFEL.getCodice());
//		pk.setEnteProprietarioId(siacTEnteProprietario.getEnteProprietarioId());
//		SirfelDTipoDocumento sirfelDTipoDocumento = sirfelTFatturaRepository.findSirfelDTipoDocumentoBySirfelDTipoDocumentoPK(pk);
		//
		return sirfelDTipoDocumentoRepository.findByEnteECodice(siacTEnteProprietario.getEnteProprietarioId(), tipoDocFEL.getCodice());
	}
	
	/**
	 * Ricerca puntuale capitolo entrata previsione.
	 *
	 * @param criteriRicerca the criteri ricerca
	 * @return the capitolo entrata previsione
	 */
	public TipoDocFEL ricercaPuntualeTipoDocumentoFEL(TipoDocFEL tipoDocFE) {
		final String methodName = "ricercaPuntualeCapitoloEntrataPrevisione";
		
		 
		Page<SirfelDTipoDocumento> result = sirfelDTipoDocumentoRepository.ricercaPuntualeTipoDoc(ente.getUid(), 
				  tipoDocFE.getCodice()  , 
				 new PageRequest(0, 1));
		
		SirfelDTipoDocumento tipodoc; 
		
		try {			
			tipodoc =  result.getContent().get(0);			
		} catch (RuntimeException re){
			log.warn(methodName, "bilElem non trovato. Returning null. ", re);
			tipodoc = null;
		}
		
		
		log.debug(methodName, "result: "+tipodoc);
		return mapNotNull(tipodoc, TipoDocFEL.class, BilMapId.SirfelDTipoDocumento_TipoDocFEL);
	}
	
	public FatturaFEL ricercaDettaglioFatturaFEL(Integer idFattura) {
		
		SirfelTFatturaPK sirfelTFatturaPK = new SirfelTFatturaPK();
		sirfelTFatturaPK.setEnteProprietarioId(ente.getUid());
		sirfelTFatturaPK.setIdFattura(idFattura);
		
		SirfelTFattura sirfelTFattura = sirfelTFatturaRepository.findOne(sirfelTFatturaPK);
		//SirfelTFattura sirfelTFattura = sirfelTFatturaRepository.findByIdFatturaEEnte(idFattura, ente.getUid());
		
		// XXX: l'entity pare non tirare su la sola lista dei riepiloghi beni. Come workaround la tiriamo su separatamente
//		List<SirfelTRiepilogoBeni> sirfelTRiepilogoBenis = sirfelTFatturaRepository.findSirfelTRiepilogoBenisByIdFatturaEEnte(idFattura, ente.getUid());
		//sirfelTFattura.setSirfelTRiepilogoBenis(sirfelTRiepilogoBenis);
		
		return mapNotNull(sirfelTFattura, FatturaFEL.class, FelMapId.SirfelTFattura_FatturaFEL);
	}




	public void aggiornaStatoFattura(FatturaFEL fatturaFEL, StatoAcquisizioneFEL statoAcquisizioneFEL) {
		SirfelTFatturaPK sirfelTFatturaPK = new SirfelTFatturaPK();
		sirfelTFatturaPK.setEnteProprietarioId(ente.getUid());
		sirfelTFatturaPK.setIdFattura(fatturaFEL.getIdFattura());
		
		SirfelTFattura sirfelTFattura = sirfelTFatturaRepository.findOne(sirfelTFatturaPK);
		sirfelTFattura.setStatoFattura(statoAcquisizioneFEL.getCodice());
		sirfelTFatturaRepository.saveAndFlush(sirfelTFattura);
	}


	public void impostaDataCaricamentoFattura(FatturaFEL fatturaFEL, Date dataCaricamento) {
		SirfelTFatturaPK sirfelTFatturaPK = new SirfelTFatturaPK();
		sirfelTFatturaPK.setIdFattura(fatturaFEL.getIdFattura());
		sirfelTFatturaPK.setEnteProprietarioId(ente.getUid());
		SirfelTFattura sirfelTFattura = sirfelTFatturaRepository.findOne(sirfelTFatturaPK);
		sirfelTFattura.setDataCaricamento(dataCaricamento);
		sirfelTFatturaRepository.saveAndFlush(sirfelTFattura);
	}
	

	public void inserisciRelazioneDocumentoFattura(DocumentoSpesa docSpesa, FatturaFEL fatturaFEL) {
		Date now = new Date();
		SiacRDocSirfel siacRDocSirfel = new SiacRDocSirfel();
		
		//fattura
		SirfelTFatturaPK sirfelTFatturaPK = new SirfelTFatturaPK();
		sirfelTFatturaPK.setEnteProprietarioId(ente.getUid());
		sirfelTFatturaPK.setIdFattura(fatturaFEL.getIdFattura());
		SirfelTFattura sirfelTFattura = sirfelTFatturaRepository.findOne(sirfelTFatturaPK);
		siacRDocSirfel.setSirfelTFattura(sirfelTFattura);
		//documento
		SiacTDoc siacTDoc = new SiacTDoc();
		siacTDoc.setUid(docSpesa.getUid());
		siacRDocSirfel.setSiacTDoc(siacTDoc);
		//date e login
		siacRDocSirfel.setDataModificaInserimento(now);
		siacRDocSirfel.setLoginOperazione(loginOperazione);
		siacRDocSirfelRepository.save(siacRDocSirfel);
	}
	
	
	public StatoAcquisizioneFEL ricercaStatoFattura(Integer idFattura) {
		// Costruisco la chiave
		SirfelTFatturaPK sirfelTFatturaPK = new SirfelTFatturaPK();
		sirfelTFatturaPK.setEnteProprietarioId(ente.getUid());
		sirfelTFatturaPK.setIdFattura(idFattura);
		
		SirfelTFattura sirfelTFattura = sirfelTFatturaRepository.findOne(sirfelTFatturaPK);
		return StatoAcquisizioneFEL.byCodice(sirfelTFattura.getStatoFattura());
	}

//	public List<PrestatoreFEL> ricercaPrestatoreFEL(PrestatoreFEL prestatoreFEL) {
//		List<SirfelTPrestatore> sirfelTPrestatores = sirfelTPrestatoreRepository.findByCodicePaeseAndCodicePrestatoreAndEnte(prestatoreFEL.getCodicePaese(), prestatoreFEL.getCodicePrestatore(),
//				prestatoreFEL.getEnte().getUid());
//		return convertiLista(sirfelTPrestatores, PrestatoreFEL.class, FelMapId.SirfelTPrestatore_PrestatoreFEL);
//	}
	
	//////////////////////////////////////////////////////////////////////////////
	/////////////////////////////// Inserimenti //////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////
	public void inserisciFatturaFEL(FatturaFEL fatturaFEL) {
		SirfelTFattura sirfelTFattura = buildSirfelTFattura(fatturaFEL);
		fatturaFELDao.create(sirfelTFattura);
		fatturaFEL.setIdFattura(sirfelTFattura.getIdFattura());
	}
	private SirfelTFattura buildSirfelTFattura(FatturaFEL fatturaFEL) {
		final String methodName = "buildSirfelTFattura";
		SirfelTFattura sirfelTFattura = map(fatturaFEL, SirfelTFattura.class, FelMapId.SirfelTFattura_FatturaFEL_Full_PrestatoreFEL);
		
		Integer idFattura = staccaIdFatturaFEL(fatturaFEL);
		SirfelTFatturaPK sirfelTFatturaPK = sirfelTFattura.getId();
		// Inizializzo la PK se non presente
		if(sirfelTFatturaPK == null) {
			log.debug(methodName, "Creazione della PK per SirfelTFattura");
			sirfelTFatturaPK = new SirfelTFatturaPK();
			sirfelTFatturaPK.setEnteProprietarioId(fatturaFEL.getEnte().getUid());
			sirfelTFattura.setId(sirfelTFatturaPK);
		}
		sirfelTFatturaPK.setIdFattura(idFattura);
		
		return sirfelTFattura;
	}
	private Integer staccaIdFatturaFEL(FatturaFEL fatturaFEL) {
		final String methodName = "staccaIdFatturaFEL";
		
		SirfelTFatturaNum sirfelTFatturaNum = sirfelTFatturaNumRepository.findByEnteProprietarioId(fatturaFEL.getEnte().getUid());
		
		Date now = new Date();
		if(sirfelTFatturaNum == null) {
			sirfelTFatturaNum = new SirfelTFatturaNum();
			SiacTEnteProprietario siacTEnteProprietario = new SiacTEnteProprietario();
			siacTEnteProprietario.setUid(fatturaFEL.getEnte().getUid());
			sirfelTFatturaNum.setSiacTEnteProprietario(siacTEnteProprietario);
			sirfelTFatturaNum.setDataModificaInserimento(now);
			// TODO: login operazione?
			sirfelTFatturaNum.setLoginOperazione("SIRFEL");
			
			Integer numero = trovaNumeroFattura(fatturaFEL);
			sirfelTFatturaNum.setNumero(numero);
		}
		
		sirfelTFatturaNum.setDataModifica(now);
		
		sirfelTFatturaNumRepository.saveAndFlush(sirfelTFatturaNum);
		Integer idFattura = sirfelTFatturaNum.getNumero();
		
		log.info(methodName, "returning idFattura: "+ idFattura);
		return idFattura;
	}
	private Integer trovaNumeroFattura(FatturaFEL fatturaFEL) {
		final String methodName = "trovaNumeroFattura";
		Integer maxSavedValue = sirfelTFatturaRepository.getMaxIdFatturaByEnteProprietarioId(fatturaFEL.getEnte().getUid());
		log.debug(methodName, "Id massimo per la fattura FEL dell'ente " + fatturaFEL.getEnte().getUid() + " su base dati: " + maxSavedValue);
		// La numerazione parte da 1
		int maxValue = Math.max(maxSavedValue + 1, 1);
		log.debug(methodName, "Id di inizio numerazione per la fattura FEL dell'ente " + fatturaFEL.getEnte().getUid() + ": " + maxValue);
		return maxValue;
	}
	
//	public void inserisciPrestatoreFEL(PrestatoreFEL prestatoreFEL) {
//		SirfelTPrestatore sirfelTPrestatore = buildSirfelTPrestatore(prestatoreFEL);
//		fatturaFELDao.create(sirfelTPrestatore);
//		prestatoreFEL.setIdPrestatore(sirfelTPrestatore.getIdPrestatore());
//	}
////	private SirfelTPrestatore buildSirfelTPrestatore(PrestatoreFEL prestatoreFEL) {
//		final String methodName = "buildSirfelTPrestatore";
//		SirfelTPrestatore sirfelTPrestatore = map(prestatoreFEL, SirfelTPrestatore.class, FelMapId.SirfelTPrestatore_PrestatoreFEL);
//		
//		Integer id = staccaIdPrestatoreFEL(prestatoreFEL);
//		SirfelTPrestatorePK sirfelTPrestatorePK = sirfelTPrestatore.getId();
//		// Inizializzo la PK se non presente
//		if(sirfelTPrestatorePK == null) {
//			log.debug(methodName, "Creazione della PK per SirfelTPrestatore");
//			sirfelTPrestatorePK = new SirfelTPrestatorePK();
//			sirfelTPrestatorePK.setEnteProprietarioId(prestatoreFEL.getEnte().getUid());
//			sirfelTPrestatore.setId(sirfelTPrestatorePK);
//		}
//		sirfelTPrestatorePK.setIdPrestatore(id);
//		return sirfelTPrestatore;
//	}
//	private Integer staccaIdPrestatoreFEL(PrestatoreFEL prestatoreFEL) {
//		final String methodName = "staccaIdPrestatoreFEL";
//		
//		SirfelTPrestatoreNum sirfelTPrestatoreNum = sirfelTPrestatoreNumRepository.findByEnteProprietarioId(prestatoreFEL.getEnte().getUid());
//		
//		Date now = new Date();
//		if(sirfelTPrestatoreNum == null) {
//			sirfelTPrestatoreNum = new SirfelTPrestatoreNum();
//			SiacTEnteProprietario siacTEnteProprietario = new SiacTEnteProprietario();
//			siacTEnteProprietario.setUid(prestatoreFEL.getEnte().getUid());
//			sirfelTPrestatoreNum.setSiacTEnteProprietario(siacTEnteProprietario);
//			sirfelTPrestatoreNum.setDataModificaInserimento(now);
//			// TODO: login operazione?
//			sirfelTPrestatoreNum.setLoginOperazione("SIRFEL");
//			Integer numero = trovaNumeroPrestatore(prestatoreFEL);
//			sirfelTPrestatoreNum.setNumero(numero);
//		}
//		
//		sirfelTPrestatoreNum.setDataModifica(now);
//		
//		sirfelTPrestatoreNumRepository.saveAndFlush(sirfelTPrestatoreNum);
//		Integer idPrestatore = sirfelTPrestatoreNum.getNumero();
//		
//		log.info(methodName, "returning idPrestatore: "+ idPrestatore);
//		return idPrestatore;
//	}
//	private Integer trovaNumeroPrestatore(PrestatoreFEL prestatoreFEL) {
//		final String methodName = "trovaNumeroPrestatore";
//		Integer maxSavedValue = sirfelTPrestatoreRepository.getMaxIdPrestatoreByEnteProprietarioId(prestatoreFEL.getEnte().getUid());
//		log.debug(methodName, "Id massimo per il prestatore FEL dell'ente " + prestatoreFEL.getEnte().getUid() + " su base dati: " + maxSavedValue);
//		// La numerazione parte da 1
//		int maxValue = Math.max(maxSavedValue + 1, 1);
//		log.debug(methodName, "Id di inizio numerazione per il prestatore FEL dell'ente " + prestatoreFEL.getEnte().getUid() + ": " + maxValue);
//		return maxValue;
//	}
	
	public void inserisciCausaleFEL(CausaleFEL causaleFEL) {
		SirfelTCausale sirfelTCausale = buildSirfelTCausale(causaleFEL);
		fatturaFELDao.create(sirfelTCausale);
	}
	private SirfelTCausale buildSirfelTCausale(CausaleFEL causaleFEL) {
		final String methodName = "buildSirfelTCausale";
		SirfelTCausale sirfelTCausale = map(causaleFEL, SirfelTCausale.class, FelMapId.SirfelTCausale_CausaleFEL);
		// Inizializzo la PK se non presente
		if(sirfelTCausale.getId() == null) {
			log.debug(methodName, "Creazione della PK per SirfelTCausale");
			SirfelTCausalePK sirfelTCausalePK = new SirfelTCausalePK();
			sirfelTCausalePK.setEnteProprietarioId(causaleFEL.getEnte().getUid());
			sirfelTCausalePK.setIdFattura(causaleFEL.getFattura().getIdFattura());
			sirfelTCausalePK.setProgressivo(causaleFEL.getProgressivo());
			sirfelTCausale.setId(sirfelTCausalePK);
		}
		
		return sirfelTCausale;
	}
	
	public void inserisciCassaPrevidenzialeFEL(CassaPrevidenzialeFEL cassaPrevidenzialeFEL) {
		SirfelTCassaPrevidenziale sirfelTCassaPrevidenziale = buildSirfelTCassaPrevidenziale(cassaPrevidenzialeFEL);
		
		fatturaFELDao.create(sirfelTCassaPrevidenziale);
	}
	private SirfelTCassaPrevidenziale buildSirfelTCassaPrevidenziale(CassaPrevidenzialeFEL cassaPrevidenzialeFEL) {
		final String methodName = "buildSirfelTCassaPrevidenziale";
		SirfelTCassaPrevidenziale sirfelTCassaPrevidenziale = map(cassaPrevidenzialeFEL, SirfelTCassaPrevidenziale.class, FelMapId.SirfelTCassaPrevidenziale_CassaPrevidenzialeFEL);
		
		if(sirfelTCassaPrevidenziale.getId() == null) {
			log.debug(methodName, "Creazione della PK per SirfelTCassaPrevidenziale");
			SirfelTCassaPrevidenzialePK sirfelTCassaPrevidenzialePK = new SirfelTCassaPrevidenzialePK();
			sirfelTCassaPrevidenzialePK.setEnteProprietarioId(cassaPrevidenzialeFEL.getEnte().getUid());
			sirfelTCassaPrevidenzialePK.setIdFattura(cassaPrevidenzialeFEL.getFattura().getIdFattura());
			sirfelTCassaPrevidenzialePK.setProgressivo(cassaPrevidenzialeFEL.getProgressivo());
			sirfelTCassaPrevidenziale.setId(sirfelTCassaPrevidenzialePK);
		}
		
		return sirfelTCassaPrevidenziale;
	}
	
	public void inserisciFattureCollegateFEL(FattureCollegateFEL fatturaCollegata) {
		SirfelTFattureCollegate sirfelTFattureCollegate = buildSirfelTFattureCollegate(fatturaCollegata);
		fatturaFELDao.create(sirfelTFattureCollegate);
	}
	private SirfelTFattureCollegate buildSirfelTFattureCollegate(FattureCollegateFEL fatturaCollegata) {
		final String methodName = "buildSirfelTFattureCollegate";
		SirfelTFattureCollegate sirfelTFattureCollegate = map(fatturaCollegata, SirfelTFattureCollegate.class, FelMapId.SirfelTFattureCollegate_FattureCollegateFEL);
		
		if(sirfelTFattureCollegate.getId() == null) {
			log.debug(methodName, "Creazione della PK per SirfelTFattureCollegate");
			SirfelTFattureCollegatePK sirfelTFattureCollegatePK = new SirfelTFattureCollegatePK();
			sirfelTFattureCollegatePK.setEnteProprietarioId(fatturaCollegata.getEnte().getUid());
			sirfelTFattureCollegatePK.setIdFattura(fatturaCollegata.getFattura().getIdFattura());
			sirfelTFattureCollegatePK.setProgressivo(fatturaCollegata.getProgressivo());
			sirfelTFattureCollegate.setId(sirfelTFattureCollegatePK);
		}
		return sirfelTFattureCollegate;
	}
	
	public void inserisciRiepilogoBeniFEL(RiepilogoBeniFEL riepilogoBeni) {
		SirfelTRiepilogoBeni sirfelTRiepilogoBeni = buildSirfelTRiepilogoBeni(riepilogoBeni);
		fatturaFELDao.create(sirfelTRiepilogoBeni);
	}
	private SirfelTRiepilogoBeni buildSirfelTRiepilogoBeni(RiepilogoBeniFEL riepilogoBeni) {
		final String methodName = "buildSirfelTFattureCollegate";
		SirfelTRiepilogoBeni sirfelTRiepilogoBeni = map(riepilogoBeni, SirfelTRiepilogoBeni.class, FelMapId.SirfelTRiepilogoBeni_RiepilogoBeniFEL);
		
		if(sirfelTRiepilogoBeni.getId() == null) {
			log.debug(methodName, "Creazione della PK per SirfelTRiepilogoBeni");
			SirfelTRiepilogoBeniPK sirfelTRiepilogoBeniPK = new SirfelTRiepilogoBeniPK();
			sirfelTRiepilogoBeniPK.setEnteProprietarioId(riepilogoBeni.getEnte().getUid());
			sirfelTRiepilogoBeniPK.setIdFattura(riepilogoBeni.getFattura().getIdFattura());
			sirfelTRiepilogoBeniPK.setProgressivo(riepilogoBeni.getProgressivo());
			sirfelTRiepilogoBeni.setId(sirfelTRiepilogoBeniPK);
		}
		return sirfelTRiepilogoBeni;
	}
	
	public void inserisciDatiGestionaliFEL(DatiGestionaliFEL datiGestionali) {
		SirfelTDatiGestionali sirfelTDatiGestionali = buildSirfelTDatiGestionali(datiGestionali);
		fatturaFELDao.create(sirfelTDatiGestionali);
	}
	private SirfelTDatiGestionali buildSirfelTDatiGestionali(DatiGestionaliFEL datiGestionali) {
		final String methodName = "buildSirfelTDatiGestionali";
		SirfelTDatiGestionali sirfelTDatiGestionali = map(datiGestionali, SirfelTDatiGestionali.class, FelMapId.SirfelTDatiGestionali_DatiGestionaliFEL);
		
		if(sirfelTDatiGestionali.getId() == null) {
			log.debug(methodName, "Creazione della PK per SirfelTDatiGestionali");
			SirfelTDatiGestionaliPK sirfelTDatiGestionaliPK = new SirfelTDatiGestionaliPK();
			sirfelTDatiGestionaliPK.setEnteProprietarioId(datiGestionali.getEnte().getUid());
			sirfelTDatiGestionaliPK.setIdFattura(datiGestionali.getFattura().getIdFattura());
			sirfelTDatiGestionaliPK.setProgressivo(datiGestionali.getProgressivo());
			sirfelTDatiGestionali.setId(sirfelTDatiGestionaliPK);
		}
		return sirfelTDatiGestionali;
	}
	
	public void inserisciPagamentoFEL(PagamentoFEL pagamentoFEL) {
		SirfelTPagamento sirfelTPagamento = buildSirfelTPagamento(pagamentoFEL);
		fatturaFELDao.create(sirfelTPagamento);
	}
	private SirfelTPagamento buildSirfelTPagamento(PagamentoFEL pagamentoFEL) {
		final String methodName = "buildSirfelTPagamento";
		SirfelTPagamento sirfelTPagamento = map(pagamentoFEL, SirfelTPagamento.class, FelMapId.SirfelTPagamento_PagamentoFEL_FatturaFEL);
		
		if(sirfelTPagamento.getId() == null) {
			log.debug(methodName, "Creazione della PK per SirfelTPagamento");
			SirfelTPagamentoPK sirfelTPagamentoPK = new SirfelTPagamentoPK();
			sirfelTPagamentoPK.setEnteProprietarioId(pagamentoFEL.getEnte().getUid());
			sirfelTPagamentoPK.setIdFattura(pagamentoFEL.getFattura().getIdFattura());
			sirfelTPagamentoPK.setProgressivo(pagamentoFEL.getProgressivo());
			sirfelTPagamento.setId(sirfelTPagamentoPK);
		}
		return sirfelTPagamento;
	}
	
	public void inserisciDettaglioPagamentoFEL(DettaglioPagamentoFEL dettaglioPagamentoFEL) {
		SirfelTDettaglioPagamento sirfelTDettaglioPagamento = buildSirfelTDettaglioPagamento(dettaglioPagamentoFEL);
		fatturaFELDao.create(sirfelTDettaglioPagamento);
	}
	private SirfelTDettaglioPagamento buildSirfelTDettaglioPagamento(DettaglioPagamentoFEL dettaglioPagamentoFEL) {
		final String methodName = "buildSirfelTDettaglioPagamento";
		SirfelTDettaglioPagamento sirfelTDettaglioPagamento = map(dettaglioPagamentoFEL, SirfelTDettaglioPagamento.class, FelMapId.SirfelTDettaglioPagamento_DettaglioPagamentoFEL);
		
		if(sirfelTDettaglioPagamento.getId() == null) {
			log.debug(methodName, "Creazione della PK per SirfelTDettaglioPagamento");
			SirfelTDettaglioPagamentoPK sirfelTDettaglioPagamentoPK = new SirfelTDettaglioPagamentoPK();
			sirfelTDettaglioPagamentoPK.setEnteProprietarioId(dettaglioPagamentoFEL.getEnte().getUid());
			sirfelTDettaglioPagamentoPK.setIdFattura(dettaglioPagamentoFEL.getPagamento().getFattura().getIdFattura());
			sirfelTDettaglioPagamentoPK.setProgressivoDettaglio(dettaglioPagamentoFEL.getProgressivoDettaglio());
			sirfelTDettaglioPagamentoPK.setProgressivoPagamento(dettaglioPagamentoFEL.getPagamento().getProgressivo());
			sirfelTDettaglioPagamento.setId(sirfelTDettaglioPagamentoPK);
		}
		return sirfelTDettaglioPagamento;
	}
	
	public void inserisciPortaleFattureFEL(PortaleFattureFEL portaleFatture) {
		SirfelTPortaleFatture sirfelTPortaleFatture = buildSirfelTPortaleFatture(portaleFatture);
		fatturaFELDao.create(sirfelTPortaleFatture);
	}
	private SirfelTPortaleFatture buildSirfelTPortaleFatture(PortaleFattureFEL portaleFatture) {
		final String methodName = "buildSirfelTPortaleFatture";
		SirfelTPortaleFatture sirfelTPortaleFatture = map(portaleFatture, SirfelTPortaleFatture.class, FelMapId.SirfelTPortaleFatture_PortaleFattureFEL);
		
		if(sirfelTPortaleFatture.getId() == null) {
			log.debug(methodName, "Creazione della PK per SirfelTPortaleFatture");
			SirfelTFatturaPK sirfelTPortaleFatturePK = new SirfelTFatturaPK();
			sirfelTPortaleFatturePK.setEnteProprietarioId(portaleFatture.getEnte().getUid());
			sirfelTPortaleFatturePK.setIdFattura(portaleFatture.getFattura().getIdFattura());
			sirfelTPortaleFatture.setId(sirfelTPortaleFatturePK);
		}
		return sirfelTPortaleFatture;
	}
	
	public void inserisciProtocolloFEL(ProtocolloFEL protocolloFEL) {
		SirfelTProtocollo sirfelTProtocollo = buildSirfelTProtocollo(protocolloFEL);
		fatturaFELDao.create(sirfelTProtocollo);
	}
	private SirfelTProtocollo buildSirfelTProtocollo(ProtocolloFEL protocolloFEL) {
		final String methodName = "buildSirfelTProtocollo";
		SirfelTProtocollo sirfelTProtocollo = map(protocolloFEL, SirfelTProtocollo.class, FelMapId.SirfelTProtocollo_ProtocolloFEL);
		
		if(sirfelTProtocollo.getId() == null) {
			log.debug(methodName, "Creazione della PK per SirfelTProtocollo");
			SirfelTFatturaPK sirfelTProtocolloPK = new SirfelTFatturaPK();
			sirfelTProtocolloPK.setEnteProprietarioId(protocolloFEL.getEnte().getUid());
			sirfelTProtocolloPK.setIdFattura(protocolloFEL.getFattura().getIdFattura());
			sirfelTProtocollo.setId(sirfelTProtocolloPK);
		}
		return sirfelTProtocollo;
	}
	
	public void inserisciOrdineAcquistoFEL(OrdineAcquistoFEL ordineAcquistoFEL) {
		SirfelTOrdineAcquisto sirfelTOrdineAcquisto = buildSirfelTOrdineAcquisto(ordineAcquistoFEL);
		fatturaFELDao.create(sirfelTOrdineAcquisto);
	}
	private SirfelTOrdineAcquisto buildSirfelTOrdineAcquisto(OrdineAcquistoFEL ordineAcquistoFEL) {
		final String methodName = "buildSirfelTOrdineAcquisto";
		SirfelTOrdineAcquisto sirfelTOrdineAcquisto = map(ordineAcquistoFEL, SirfelTOrdineAcquisto.class);
		
		if(sirfelTOrdineAcquisto.getId() == null) {
			log.debug(methodName, "Creazione della PK per SirfelTOrdineAcquisto");
			SirfelTOrdineAcquistoPK sirfelTOrdineAcquistoPK = new SirfelTOrdineAcquistoPK();
			sirfelTOrdineAcquistoPK.setEnteProprietarioId(ordineAcquistoFEL.getEnte().getUid());
			sirfelTOrdineAcquistoPK.setIdFattura(ordineAcquistoFEL.getFatturaFEL().getIdFattura());
			sirfelTOrdineAcquistoPK.setNumeroDocumento(ordineAcquistoFEL.getNumeroDocumento());
			sirfelTOrdineAcquisto.setId(sirfelTOrdineAcquistoPK);
		}
		return sirfelTOrdineAcquisto;
	}
	
	public void inserisciDettaglioOrdineAcquistoFEL(DettaglioOrdineAcquistoFEL dettaglioOrdineAcquistoFEL) {
		SirfelTOrdineAcquistoDettaglio sirfelTOrdineAcquistoDettaglio = buildSirfelTOrdineAcquistoDettaglio(dettaglioOrdineAcquistoFEL);
		fatturaFELDao.create(sirfelTOrdineAcquistoDettaglio);
	}
	private SirfelTOrdineAcquistoDettaglio buildSirfelTOrdineAcquistoDettaglio(DettaglioOrdineAcquistoFEL dettaglioOrdineAcquistoFEL) {
		final String methodName = "buildSirfelTOrdineAcquistoDettaglio";
		SirfelTOrdineAcquistoDettaglio sirfelTOrdineAcquistoDettaglio = map(dettaglioOrdineAcquistoFEL, SirfelTOrdineAcquistoDettaglio.class);
		
		if(sirfelTOrdineAcquistoDettaglio.getId() == null) {
			log.debug(methodName, "Creazione della PK per SirfelTOrdineAcquisto");
			SirfelTOrdineAcquistoDettaglioPK sirfelTOrdineAcquistoDettaglioPK = new SirfelTOrdineAcquistoDettaglioPK();
			sirfelTOrdineAcquistoDettaglioPK.setEnteProprietarioId(dettaglioOrdineAcquistoFEL.getEnte().getUid());
			sirfelTOrdineAcquistoDettaglioPK.setIdFattura(dettaglioOrdineAcquistoFEL.getOrdineAcquistoFEL().getFatturaFEL().getIdFattura());
			sirfelTOrdineAcquistoDettaglioPK.setNumeroDocumento(dettaglioOrdineAcquistoFEL.getOrdineAcquistoFEL().getNumeroDocumento());
			sirfelTOrdineAcquistoDettaglioPK.setNumeroDettaglio(dettaglioOrdineAcquistoFEL.getNumeroDettaglio());
			
			sirfelTOrdineAcquistoDettaglio.setId(sirfelTOrdineAcquistoDettaglioPK);
		}
		return sirfelTOrdineAcquistoDettaglio;
	}
	
	/**
	 * Aggiornamento delle note della fattura FEL
	 * @param fatturaFEL
	 */
	public void aggiornaNote(FatturaFEL fatturaFEL) {
		SirfelTFatturaPK sirfelTFatturaPK = new SirfelTFatturaPK();
		sirfelTFatturaPK.setEnteProprietarioId(ente.getUid());
		sirfelTFatturaPK.setIdFattura(fatturaFEL.getIdFattura());
		
		SirfelTFattura sirfelTFattura = sirfelTFatturaRepository.findOne(sirfelTFatturaPK);
		if(sirfelTFattura == null) {
			throw new IllegalArgumentException("Nessuna fattura corrispondente all'id " + fatturaFEL.getIdFattura() + " per l'ente " + ente.getUid() + " presente su base dati");
		}
		sirfelTFattura.setNote(fatturaFEL.getNote());
		sirfelTFatturaRepository.save(sirfelTFattura);
	}
	

	/**
	 * SIAC-7557-VG
	 * Metodo per il recupero del tipo documento fel
	 * attraverso il tipo documento
	 */
	
	
	public List<SirfelDTipoDocumento> ricercaTipoDocFelByTipoDoc(DocumentoEntrata documentoEntrata){
		if(documentoEntrata!= null && documentoEntrata.getTipoDocumento()!= null && documentoEntrata.getEnte()!= null){
			List<SirfelDTipoDocumento> sirfelDTipoDocumento = sirfelDTipoDocumentoRepository.findSirfelDTipoDocumentoByEnteAndTipoDocE(documentoEntrata.getEnte().getUid(), documentoEntrata.getTipoDocumento().getUid());
			return sirfelDTipoDocumento;
		}else{
			return null;
		}
		
		
	}
	public void clean() {
		fatturaFELDao.clean();
	}
}
