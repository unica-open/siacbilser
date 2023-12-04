/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.integration.dao.ElencoDocumentiAllegatoDao;
import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.dao.SiacRAttoAllegatoElencoDocRepository;
import it.csi.siac.siacbilser.integration.dao.SiacRElencoDocSubdocRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTElencoDocNumRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTElencoDocRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTSubdocRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDDocTipo;
import it.csi.siac.siacbilser.integration.entity.SiacDElencoDocStato;
import it.csi.siac.siacbilser.integration.entity.SiacRAttoAllegatoElencoDoc;
import it.csi.siac.siacbilser.integration.entity.SiacRElencoDocStato;
import it.csi.siac.siacbilser.integration.entity.SiacRElencoDocSubdoc;
import it.csi.siac.siacbilser.integration.entity.SiacTAttoAllegato;
import it.csi.siac.siacbilser.integration.entity.SiacTBil;
import it.csi.siac.siacbilser.integration.entity.SiacTDoc;
import it.csi.siac.siacbilser.integration.entity.SiacTElencoDoc;
import it.csi.siac.siacbilser.integration.entity.SiacTElencoDocNum;
import it.csi.siac.siacbilser.integration.entity.SiacTEnteProprietario;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocFamTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDElencoDocStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDRelazTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.base.Converters;
import it.csi.siac.siaccommon.model.ModelDetailEnum;
import it.csi.siac.siaccommonser.util.dozer.MapId;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginataImpl;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegatoModelDetail;
import it.csi.siac.siacfin2ser.model.StatoOperativoElencoDocumenti;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoIva;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfin2ser.model.TipoFamigliaDocumento;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * Data access delegate di un ElencoDocumentiAllegato .
 *
 * @author Domenico
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class ElencoDocumentiAllegatoDad extends ExtendedBaseDadImpl {
	
	@Autowired
	private ElencoDocumentiAllegatoDao elencoDocumentiAllegatoDao;
	
	@Autowired
	private SiacTElencoDocNumRepository siacTElencoDocNumRepository;
	
	@Autowired
	private SiacTElencoDocRepository siacTElencoDocRepository;
	
	@Autowired
	private SiacRAttoAllegatoElencoDocRepository siacRAttoAllegatoElencoDocRepository;
	
	@Autowired
	private SiacRElencoDocSubdocRepository siacRElencoDocSubdocRepository;
	
	@Autowired
	private SiacTSubdocRepository siacTSubdocRepository;
	
	@Autowired
	private EnumEntityFactory eef;
	
	/**
	 * Find elenco documenti allegato completoby id.
	 *
	 * @param uid the uid
	 * @return the elenco documenti allegato
	 */
	public ElencoDocumentiAllegato findElencoDocumentiAllegatoCompletoById(Integer uid) {
		return findElencoDocumentiAllegatoByIdAndMapId(uid, BilMapId.SiacTElencoDoc_ElencoDocumentiAllegato_Completo);
	}
	
	
	/**
	 * Find elenco documenti allegato by id.
	 *
	 * @param uid the uid
	 * @return the elenco documenti allegato
	 */
	public ElencoDocumentiAllegato findElencoDocumentiAllegatoById(Integer uid) {
		return findElencoDocumentiAllegatoByIdAndMapId(uid, BilMapId.SiacTElencoDoc_ElencoDocumentiAllegato);
	}
	
	/**
	 * Find elenco documenti allegato base by id.
	 *
	 * @param uid the uid
	 * @return the elenco documenti allegato
	 */
	public ElencoDocumentiAllegato findElencoDocumentiAllegatoBaseById(Integer uid) {
		return findElencoDocumentiAllegatoByIdAndMapId(uid, BilMapId.SiacTElencoDoc_ElencoDocumentiAllegato_Base);
	}
	
	/**
	 * Find elenco documenti allegato light by id.
	 *
	 * @param uid the uid
	 * @return the elenco documenti allegato
	 */
	public ElencoDocumentiAllegato findElencoDocumentiAllegatoLightById(Integer uid) {
		return findElencoDocumentiAllegatoByIdAndMapId(uid, BilMapId.SiacTElencoDoc_ElencoDocumentiAllegato_Minimal_light);
	}
	
	/**
	 * Find elenco documenti allegato by id and map id.
	 *
	 * @param uid the uid
	 * @param mapId the map id
	 * @return the elenco documenti allegato
	 */
	private ElencoDocumentiAllegato findElencoDocumentiAllegatoByIdAndMapId(Integer uid, MapId mapId) {
		final String methodName = "findElencoDocumentiAllegatoByIdAndMapId";
		log.debug(methodName, "uid: "+ uid + ", mapId: " + mapId);
		SiacTElencoDoc siacTElencoDoc = elencoDocumentiAllegatoDao.findById(uid);
		if(siacTElencoDoc == null) {
			log.debug(methodName, "Impossibile trovare l'ElencoDocumentiAllegato con id: " + uid);
		}
		return mapNotNull(siacTElencoDoc, ElencoDocumentiAllegato.class, mapId);
	}
	
	
	
	
	/**
	 * Inserisci anagrafica elenco documenti allegato.
	 *
	 * @param elencoDocumentiAllegato the documento
	 */
	public void inserisciElencoDocumentiAllegato(ElencoDocumentiAllegato elencoDocumentiAllegato) {		
		SiacTElencoDoc siacTElencoDoc = buildSiacTElencoDoc(elencoDocumentiAllegato);	
		siacTElencoDoc.setLoginCreazione(loginOperazione);
		elencoDocumentiAllegatoDao.create(siacTElencoDoc);
		elencoDocumentiAllegato.setUid(siacTElencoDoc.getUid());
	}	
	
	

	/**
	 * Aggiorna anagrafica elenco documenti allegato.
	 *
	 * @param elencoDocumentiAllegato the documento
	 */
	public void aggiornaElencoDocumentiAllegato(ElencoDocumentiAllegato elencoDocumentiAllegato) {
		SiacTElencoDoc siacTElencoDoc = buildSiacTElencoDoc(elencoDocumentiAllegato);	
		siacTElencoDoc.setLoginModifica(loginOperazione);
		elencoDocumentiAllegatoDao.update(siacTElencoDoc);
		elencoDocumentiAllegato.setUid(siacTElencoDoc.getUid());
	}	
	
	
	/**
	 * 
	 */
	public ElencoDocumentiAllegato findElencoDocPiuRecenteByAllegato(AllegatoAtto allegatoAtto) {
		SiacTElencoDoc siacTElencoDoc = siacTElencoDocRepository.findPiuRecenteByIdAllegato(allegatoAtto.getUid());
		return mapNotNull(siacTElencoDoc, ElencoDocumentiAllegato.class, BilMapId.SiacTElencoDoc_ElencoDocumentiAllegato);
	}	
	
	
	/**
	 * Builds the siac t elenco doc.
	 *
	 * @param ElencoDocumentiAllegato the documento
	 * @return the siac t predoc
	 */
	private SiacTElencoDoc buildSiacTElencoDoc(ElencoDocumentiAllegato elencoDocumentiAllegato) {
		SiacTElencoDoc siacTElencoDoc = new SiacTElencoDoc();
		siacTElencoDoc.setLoginOperazione(loginOperazione);
		elencoDocumentiAllegato.setLoginOperazione(loginOperazione);
		map(elencoDocumentiAllegato, siacTElencoDoc, BilMapId.SiacTElencoDoc_ElencoDocumentiAllegato);
		return siacTElencoDoc;
	}
	
	
	
	/**
	 * Ricerca puntuale elenco documenti allegato.
	 *
	 * @param elencoDocumentiAllegato the pre doc
	 * @return the elenco documenti allegato
	 */
	public ElencoDocumentiAllegato ricercaPuntualeElencoDocumentiAllegato(ElencoDocumentiAllegato elencoDocumentiAllegato) {
		final String methodName = "ricercaPuntualeElencoDocumentiAllegato";
		
		ParametriPaginazione parametriPaginazione = new ParametriPaginazione();		
		parametriPaginazione.setNumeroPagina(0);
		parametriPaginazione.setElementiPerPagina(1000);
		
		SiacTElencoDoc siacTElencoDoc = elencoDocumentiAllegatoDao.ricercaPuntualeElencoDocumentiAllegatoAtto(
				elencoDocumentiAllegato.getEnte().getUid(),
				//TODO inserire parametri di ricerca
				null,
				null,
				null,
				null,
				null
				);

		if(siacTElencoDoc == null) {
			log.debug(methodName, "Nessun elenco documenti elenco documenti allegato trovato");
			return null;
		}		
		
		return mapNotNull(siacTElencoDoc, ElencoDocumentiAllegato.class, BilMapId.SiacTElencoDoc_ElencoDocumentiAllegato);
		
	}
	
	
	
	
	
//	/**
//	 * Ricerca sintetica elenco documenti allegato.
//	 *
//	 * @param ElencoDocumentiAllegato the elenco documenti allegato
//	 * @return the lista paginata
//	 */
//	public ListaPaginata<ElencoDocumentiAllegato> ricercaSinteticaElencoDocumentiAllegato(ElencoDocumentiAllegato ElencoDocumentiAllegato,
//			ParametriPaginazione parametriPaginazione) {
//	
//			
//		Page<SiacTElencoDoc> lista = elencoDocumentiAllegatoAttoDao.ricercaSinteticaElencoDocumentiAllegato(
//				ElencoDocumentiAllegato.getEnte().getUid(),
//				//ElencoDocumentiAllegato.getAttoAmministrativo()
//				toPageable(parametriPaginazione));
//		
//		return toListaPaginata(lista, ElencoDocumentiAllegato.class, BilMapId.SiacTElencoDoc_ElencoDocumentiAllegato_Base);
//		
//	}
	
	
	/**
	 * Aggiorna stato elenco documenti allegato.
	 *
	 */
	public void aggiornaStatoElencoDocumentiAllegato(Integer uidElencoDocumentiAllegato, StatoOperativoElencoDocumenti statoOperativoElencoDocumenti) {
		String methodName = "aggiornaStatoElencoDocumentiAllegato";
		
		SiacTElencoDoc siacTElencoDoc = elencoDocumentiAllegatoDao.findById(uidElencoDocumentiAllegato);		
		
		if(siacTElencoDoc.getSiacRElencoDocStatos()==null){
			siacTElencoDoc.setSiacRElencoDocStatos(new ArrayList<SiacRElencoDocStato>());
		}
		Date now = new Date();
		for(SiacRElencoDocStato siacRElencoDocStato : siacTElencoDoc.getSiacRElencoDocStatos()){
			siacRElencoDocStato.setDataCancellazioneIfNotSet(now);
		}
		
		SiacRElencoDocStato siacRElencoDocStato = new SiacRElencoDocStato();
		
		SiacDElencoDocStatoEnum stato = SiacDElencoDocStatoEnum.byStatoOperativo(statoOperativoElencoDocumenti);
		SiacDElencoDocStato siacDElencoDocStato = eef.getEntity(stato, siacTElencoDoc.getSiacTEnteProprietario().getUid(), SiacDElencoDocStato.class);
		
		log.debug(methodName, "Setting siacDElencoDocStato to: " + siacDElencoDocStato.getEldocStatoCode() + " [" + siacDElencoDocStato.getUid() + "]");
		
		siacRElencoDocStato.setSiacDElencoDocStato(siacDElencoDocStato);
		siacRElencoDocStato.setSiacTElencoDoc(siacTElencoDoc);
		siacRElencoDocStato.setSiacTEnteProprietario(siacTElencoDoc.getSiacTEnteProprietario());
		siacRElencoDocStato.setLoginOperazione(loginOperazione); 
		siacRElencoDocStato.setDataModificaInserimento(now);
		siacTElencoDoc.addSiacRElencoDocStato(siacRElencoDocStato);
		
		siacTElencoDocRepository.saveAndFlush(siacTElencoDoc);
		
	}

	

	
	
	/**
	 * Ottiene il numero di una nuova quota o Elenco.
	 *
	 * @param uidDocumento the uid documento
	 * @return numero Elenco
	 */
	// SIAC-5246: modificata la propagazione da MANDATORY a REQUIRES_NEW per evitare deadlock (possibile perdita di numeri)
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public Integer staccaNumeroElenco(Integer uidBilancio) {
		final String methodName = "staccaNumeroElenco";
		
		SiacTElencoDocNum siacTElencoDocNum = siacTElencoDocNumRepository.findByBilId(uidBilancio);
		
		Date now = new Date();		
		if(siacTElencoDocNum == null) {
			siacTElencoDocNum = new SiacTElencoDocNum();
			SiacTBil siacTDoc = new SiacTBil();
			siacTDoc.setBilId(uidBilancio);			
			siacTElencoDocNum.setSiacTBil(siacTDoc);
			SiacTEnteProprietario siacTEnteProprietario = new SiacTEnteProprietario();
			siacTEnteProprietario.setUid(ente.getUid());
			siacTElencoDocNum.setSiacTEnteProprietario(siacTEnteProprietario);
			siacTElencoDocNum.setDataCreazione(now);
			siacTElencoDocNum.setDataInizioValidita(now);
			siacTElencoDocNum.setLoginOperazione(loginOperazione);
			siacTElencoDocNum.setEldocNumero(1); //La numerazione parte da 1		
		}
		
		siacTElencoDocNum.setDataModifica(now);	
		
		siacTElencoDocNumRepository.saveAndFlush(siacTElencoDocNum);		
		Integer numeroElenco = siacTElencoDocNum.getEldocNumero();
		
		log.info(methodName, "returning numeroElenco: "+ numeroElenco);
		return numeroElenco;
	}

	
	

	


	/**
	 * Ricerca sintetica per l'elenco documenti.
	 * 
	 * @param elencoDocumentiAllegato the elenco documenti allegato
	 * @param attoAmministrativo the atto amministrativo
	 * @param listStatoOperativoElencoDocumenti the list stato operativo elenco documenti
	 * @param parametriPaginazione the parametri paginazione
	 * @return the ricerca sintetica
	 */
	public ListaPaginata<ElencoDocumentiAllegato> ricercaSinteticaElencoDocumentiAllegato(ElencoDocumentiAllegato elencoDocumentiAllegato,
			AttoAmministrativo attoAmministrativo, List<StatoOperativoElencoDocumenti> listStatoOperativoElencoDocumenti, Integer numeroElencoDa, Integer numeroElencoA, ParametriPaginazione parametriPaginazione, ModelDetailEnum... modelDetails) {
		ModelDetailEnum[] elencoDocumentiModelDetails = modelDetails == null ? 
				new ModelDetailEnum[] {
						ElencoDocumentiAllegatoModelDetail.Stato,
						ElencoDocumentiAllegatoModelDetail.TotaleDaPagareIncassare,
						ElencoDocumentiAllegatoModelDetail.TotaleQuoteSpesaEntrata,
						ElencoDocumentiAllegatoModelDetail.ContieneQuoteACopertura,
						ElencoDocumentiAllegatoModelDetail.SubdocumentiTotale,
						ElencoDocumentiAllegatoModelDetail.TotaleDaConvalidareSpesaEntrata
						} 
			: modelDetails;

		Page<SiacTElencoDoc> lista = elencoDocumentiAllegatoDao.ricercaSinteticaElenco(
				elencoDocumentiAllegato.getEnte().getUid(),
				elencoDocumentiAllegato.getAnno(),
				elencoDocumentiAllegato.getNumero(),
				elencoDocumentiAllegato.getAnnoSysEsterno(),
				elencoDocumentiAllegato.getNumeroSysEsterno(),
				elencoDocumentiAllegato.getDataTrasmissione(),
				listStatoOperativoElencoDocumenti != null ? SiacDElencoDocStatoEnum.byStatiOperativiElencoDocumenti(listStatoOperativoElencoDocumenti) : null,
				attoAmministrativo != null ? attoAmministrativo.getUid() : null,
				numeroElencoDa,
				numeroElencoA,
				toPageable(parametriPaginazione));
		return toListaPaginata(lista, ElencoDocumentiAllegato.class, BilMapId.SiacTElencoDoc_ElencoDocumentiAllegato_Minimal_light, elencoDocumentiModelDetails);   //(lista, ElencoDocumentiAllegato.class, BilMapId.SiacTBilElem_Capitolo_Minimal, Converters.byModelDetails(modelDetails));
		//return toListaPaginata(lista, ElencoDocumentiAllegato.class, BilMapId.SiacTElencoDoc_ElencoDocumentiAllegato_Base);
	}
	
	/**
	 * Calcola il totale da pagare per gli elenco documenti.
	 * 
	 * @param elencoDocumentiAllegato the elenco documenti allegato
	 * @param attoAmministrativo the atto amministrativo
	 * @param listStatoOperativoElencoDocumenti the list stato operativo elenco documenti
	 * @return the totale da pagare
	 */
	public BigDecimal calcolaTotaleDaPagare(ElencoDocumentiAllegato elencoDocumentiAllegato, AttoAmministrativo attoAmministrativo,
			List<StatoOperativoElencoDocumenti> listStatoOperativoElencoDocumenti) {
		BigDecimal totale = elencoDocumentiAllegatoDao.calcolaTotaleDaPagare(elencoDocumentiAllegato.getEnte().getUid(),
				elencoDocumentiAllegato.getAnno(),
				elencoDocumentiAllegato.getNumero(),
				elencoDocumentiAllegato.getAnnoSysEsterno(),
				elencoDocumentiAllegato.getNumeroSysEsterno(),
				elencoDocumentiAllegato.getDataTrasmissione(),
				SiacDElencoDocStatoEnum.byStatiOperativiElencoDocumenti(listStatoOperativoElencoDocumenti),
				attoAmministrativo != null ? attoAmministrativo.getUid() : null);
		return totale;
	}
	
	/**
	 * Calcola il totale da incassare per gli elenco documenti.
	 * 
	 * @param elencoDocumentiAllegato the elenco documenti allegato
	 * @param attoAmministrativo the atto amministrativo
	 * @param listStatoOperativoElencoDocumenti the list stato operativo elenco documenti
	 * @return the totale da incassare
	 */
	public BigDecimal calcolaTotaleDaIncassare(ElencoDocumentiAllegato elencoDocumentiAllegato, AttoAmministrativo attoAmministrativo,
			List<StatoOperativoElencoDocumenti> listStatoOperativoElencoDocumenti) {
		BigDecimal totale = elencoDocumentiAllegatoDao.calcolaTotaleDaIncassare(elencoDocumentiAllegato.getEnte().getUid(),
				elencoDocumentiAllegato.getAnno(),
				elencoDocumentiAllegato.getNumero(),
				elencoDocumentiAllegato.getAnnoSysEsterno(),
				elencoDocumentiAllegato.getNumeroSysEsterno(),
				elencoDocumentiAllegato.getDataTrasmissione(),
				SiacDElencoDocStatoEnum.byStatiOperativiElencoDocumenti(listStatoOperativoElencoDocumenti),
				attoAmministrativo != null ? attoAmministrativo.getUid() : null);
		return totale;
	}
	
	/**
	 * Calcola il totale delle entrate collegate per gli elenco documenti.
	 * 
	 * @param elencoDocumentiAllegato the elenco documenti allegato
	 * @param attoAmministrativo the atto amministrativo
	 * @param listStatoOperativoElencoDocumenti the list stato operativo elenco documenti
	 * @return the totale entrate collegate
	 */
	public BigDecimal calcolaTotaleEntrateCollegate(ElencoDocumentiAllegato elencoDocumentiAllegato, AttoAmministrativo attoAmministrativo,
			List<StatoOperativoElencoDocumenti> listStatoOperativoElencoDocumenti) {
		BigDecimal totale = elencoDocumentiAllegatoDao.calcolaTotaleQuoteCollegate(elencoDocumentiAllegato.getEnte().getUid(),
				elencoDocumentiAllegato.getAnno(),
				elencoDocumentiAllegato.getNumero(),
				elencoDocumentiAllegato.getAnnoSysEsterno(),
				elencoDocumentiAllegato.getNumeroSysEsterno(),
				elencoDocumentiAllegato.getDataTrasmissione(),
				SiacDElencoDocStatoEnum.byStatiOperativiElencoDocumenti(listStatoOperativoElencoDocumenti),
				attoAmministrativo != null ? attoAmministrativo.getUid() : null,
				SiacDDocFamTipoEnum.byTipoFamigliaDocumento(Arrays.asList(TipoFamigliaDocumento.ENTRATA, TipoFamigliaDocumento.IVA_ENTRATA)));
		return totale;
	}
	
	/**
	 * Calcola il totale delle spese collegate per gli elenco documenti.
	 * 
	 * @param elencoDocumentiAllegato the elenco documenti allegato
	 * @param attoAmministrativo the atto amministrativo
	 * @param listStatoOperativoElencoDocumenti the list stato operativo elenco documenti
	 * @return the totale spese collegate
	 */
	public BigDecimal calcolaTotaleSpeseCollegate(ElencoDocumentiAllegato elencoDocumentiAllegato, AttoAmministrativo attoAmministrativo,
			List<StatoOperativoElencoDocumenti> listStatoOperativoElencoDocumenti) {
		BigDecimal totale = elencoDocumentiAllegatoDao.calcolaTotaleQuoteCollegate(elencoDocumentiAllegato.getEnte().getUid(),
				elencoDocumentiAllegato.getAnno(),
				elencoDocumentiAllegato.getNumero(),
				elencoDocumentiAllegato.getAnnoSysEsterno(),
				elencoDocumentiAllegato.getNumeroSysEsterno(),
				elencoDocumentiAllegato.getDataTrasmissione(),
				SiacDElencoDocStatoEnum.byStatiOperativiElencoDocumenti(listStatoOperativoElencoDocumenti),
				attoAmministrativo != null ? attoAmministrativo.getUid() : null,
				SiacDDocFamTipoEnum.byTipoFamigliaDocumento(Arrays.asList(TipoFamigliaDocumento.SPESA, TipoFamigliaDocumento.IVA_SPESA)));
		return totale;
	}
	
	
	
	/**
	 * Controlla se esista l'associazione tra l'elenco e l'allegato.
	 * 
	 * @param elencoDocumentiAllegato the elencoDocumentiAllegato
	 * @param allegatoAtto the allegatoAtto
	 * 
	 * @return the esiste associazione elenco documenti allegato atto allegato atto 
	 */
	public Boolean esisteAssociazioneElencoDocumentiAllegatoAllegatoAtto(ElencoDocumentiAllegato elencoDocumentiAllegato, AllegatoAtto allegatoAtto) {
		List<SiacRAttoAllegatoElencoDoc> siacRAttoAllegatoElencoDocs =
				siacRAttoAllegatoElencoDocRepository.findBySiacTElencoDocAndSiacTAttoAllegato(elencoDocumentiAllegato.getUid(), allegatoAtto.getUid());
		return siacRAttoAllegatoElencoDocs != null && !siacRAttoAllegatoElencoDocs.isEmpty();
	}
	
	/**
	 * Controlla se esista l'associazione tra l'elenco e l'allegato.
	 * 
	 * @param elencoDocumentiAllegato the elencoDocumentiAllegato
	 * @param allegatoAtto the allegatoAtto
	 * 
	 * @return the esiste associazione elenco documenti allegato atto allegato atto 
	 */
	public Long countAssociazioniElencoDocumentiAllegatoAllegatoAtto(ElencoDocumentiAllegato elencoDocumentiAllegato) {
		return siacRAttoAllegatoElencoDocRepository.countBySiacTElencoDoc(elencoDocumentiAllegato.getUid());
	}
	
	/**
	 * Associa l'elenco di documenti all'allegatoAtto.
	 * 
	 * @param elencoDocumentiAllegato the elencoDocumentiAllegato
	 * @param allegatoAtto the allegatoAtto
	 */
	public void associaElencoDocumentiAllegatoAdAllegatoAtto(ElencoDocumentiAllegato elencoDocumentiAllegato, AllegatoAtto allegatoAtto) {
		SiacRAttoAllegatoElencoDoc siacRAttoAllegatoElencoDoc = new SiacRAttoAllegatoElencoDoc();
		
		SiacTElencoDoc siacTElencoDoc = new SiacTElencoDoc();
		siacTElencoDoc.setUid(elencoDocumentiAllegato.getUid());
		siacRAttoAllegatoElencoDoc.setSiacTElencoDoc(siacTElencoDoc);
		
		SiacTAttoAllegato siacTAttoAllegato = new SiacTAttoAllegato();
		siacTAttoAllegato.setUid(allegatoAtto.getUid());
		siacRAttoAllegatoElencoDoc.setSiacTAttoAllegato(siacTAttoAllegato);
		
		siacRAttoAllegatoElencoDoc.setDataModificaInserimento(new Date());
		siacRAttoAllegatoElencoDoc.setLoginOperazione(loginOperazione);
		
		SiacTEnteProprietario siacTEnteProprietario = map(ente, SiacTEnteProprietario.class, BilMapId.SiacTEnteProprietario_Ente_Base);
		siacRAttoAllegatoElencoDoc.setSiacTEnteProprietario(siacTEnteProprietario);
		
		siacRAttoAllegatoElencoDocRepository.saveAndFlush(siacRAttoAllegatoElencoDoc);
	}
	
	
	/**
	 * Disassocia l'elenco di documenti dall'allegatoAtto.
	 * 
	 * @param elencoDocumentiAllegato the elencoDocumentiAllegato
	 * @param allegatoAtto the allegatoAtto
	 */
	public void disassociaElencoDocumentiAllegatoAdAllegatoAtto(ElencoDocumentiAllegato elencoDocumentiAllegato, AllegatoAtto allegatoAtto) {
		List<SiacRAttoAllegatoElencoDoc> siacRAttoAllegatoElencoDocs =
				siacRAttoAllegatoElencoDocRepository.findBySiacTElencoDocAndSiacTAttoAllegato(elencoDocumentiAllegato.getUid(), allegatoAtto.getUid());
		if(siacRAttoAllegatoElencoDocs != null && !siacRAttoAllegatoElencoDocs.isEmpty()) {
			final Date now = new Date();
			for(SiacRAttoAllegatoElencoDoc siacRAttoAllegatoElencoDoc : siacRAttoAllegatoElencoDocs) {
				siacRAttoAllegatoElencoDoc.setDataCancellazioneIfNotSet(now);
			}
			siacRAttoAllegatoElencoDocRepository.save(siacRAttoAllegatoElencoDocs);
		}
		
	}
	
	/**
	 * Disassocia la quota dall'elenco.
	 * 
	 * @param elencoDocumentiAllegato the elencoDocumentiAllegato
	 * @param subdocumento the subdocumento
	 */
	public <S extends Subdocumento<?, ?>> void disassociaQuotaDaElenco(ElencoDocumentiAllegato elencoDocumentiAllegato, S subdocumento) {
		String methodName = "disassociaQuotaDaElenco";
		List<SiacRElencoDocSubdoc> siacRElencoDocSubdocs = siacTElencoDocRepository.findSiacRElencoDocSubdocsByEldocIdAndSubdocId(elencoDocumentiAllegato.getUid(), subdocumento.getUid());
		if(siacRElencoDocSubdocs == null || siacRElencoDocSubdocs.isEmpty()) {
			log.debug(methodName, "Relazione tra elenco con uid " + elencoDocumentiAllegato.getUid() + " e subdocumento con uid " + subdocumento.getUid()
					+ " inesistente");
			return;
		}
		final Date now = new Date();
		for(SiacRElencoDocSubdoc siacRElencoDocSubdoc : siacRElencoDocSubdocs) {
			siacRElencoDocSubdoc.setDataCancellazioneIfNotSet(now);
		}
		siacTElencoDocRepository.flush();
	}
	
	public Long countSubdocumentiCollegatiAdAttoAmministrativo(ElencoDocumentiAllegato elencoDocumentiAllegato) {
		return siacTElencoDocRepository.countSiacRElencoDocSubdocWithSiacTSubdocsWithSiacTAttoAmm(elencoDocumentiAllegato.getUid());
	}
	
	
	/**
	 * Trova tutti i subdocumenti appartenenti ad un documento che &egrave; in relazione di 
	 * subordinato con il documento dei subdocumenti passati come parametro.
	 *
	 * @param elencoDocumentiAllegato the elenco documenti allegato
	 * @return the list
	 */
	public List<Subdocumento<?,?>> findSubdocDocSubordinati(List<Subdocumento<?,?>> subdocumenti) {
		final String methodName = "findSubdocDocSubordinati";
		List<Integer> listaUid = getUids(subdocumenti);
		
		List<SiacTSubdoc> siacTSubdocsCollegati = siacTElencoDocRepository.findSubdocCollegatiByTipoRelazione(listaUid, SiacDRelazTipoEnum.Subdocumento.getCodice());
		
		List<Subdocumento<?,?>> subdocs = new ArrayList<Subdocumento<?,?>>();
		
		if(siacTSubdocsCollegati == null || siacTSubdocsCollegati.isEmpty()){
			log.debug(methodName, "nessun subdoc subordinato trovato");
		}else{
			for (SiacTSubdoc siacTSubdoc : siacTSubdocsCollegati) {
				Subdocumento subdoc = map(siacTSubdoc, Subdocumento.class);
				log.debug(methodName, "trovato subdoc subordinato con uid: " + subdoc.getUid());
				
				// Copio i dati del documento
				SiacTDoc siacTDoc = siacTSubdoc.getSiacTDoc();
				Documento doc = new Documento();
				doc.setAnno(siacTDoc.getDocAnno());
				doc.setNumero(siacTDoc.getDocNumero());
				subdoc.setDocumento(doc);
				log.debug(methodName, "trovato doc subordinato con uid: " + doc.getUid());
				
				// Copio i dati del tipo documento
				SiacDDocTipo siacDDocTipo = siacTDoc.getSiacDDocTipo();
				TipoDocumento tipoDocumento = map(siacDDocTipo, TipoDocumento.class, BilMapId.SiacDDocTipo_TipoDocumento);
				doc.setTipoDocumento(tipoDocumento);
				log.debug(methodName, "Tipo documento: " + tipoDocumento.getUid() + " --- " + tipoDocumento.getCodice());
				
				subdocs.add(subdoc);
			}
		}
		return subdocs;
		
	}

	/**
	 * Ottiene una lista degli Uid a partire da una lista di Entita
	 * @param listaEntita
	 * @return
	 */
	protected List<Integer> getUids(List<? extends Entita> listaEntita) { //TODO da spostare su BaseDadImpl
		List<Integer> result = new ArrayList<Integer>();
		for(Entita entita : listaEntita){
			result.add(entita.getUid());
		}
		return result;
	}
	
	public void collegaElencoSubdocumento(ElencoDocumentiAllegato elencoDocumentiAllegato, Subdocumento<?, ?> subdoc) {
		SiacRElencoDocSubdoc siacRElencoDocSubdoc = new SiacRElencoDocSubdoc();
		
		SiacTElencoDoc siacTElencoDoc = new SiacTElencoDoc();
		siacTElencoDoc.setUid(elencoDocumentiAllegato.getUid());
		siacRElencoDocSubdoc.setSiacTElencoDoc(siacTElencoDoc);
		
		SiacTSubdoc siacTSubdoc = new SiacTSubdoc();
		siacTSubdoc.setUid(subdoc.getUid());
		siacRElencoDocSubdoc.setSiacTSubdoc(siacTSubdoc);
		
		siacRElencoDocSubdoc.setDataModificaInserimento(new Date());
		siacRElencoDocSubdoc.setLoginOperazione(loginOperazione);
		
		SiacTEnteProprietario siacTEnteProprietario = map(ente, SiacTEnteProprietario.class, BilMapId.SiacTEnteProprietario_Ente_Base);
		siacRElencoDocSubdoc.setSiacTEnteProprietario(siacTEnteProprietario);
		
		siacRElencoDocSubdocRepository.saveAndFlush(siacRElencoDocSubdoc);
	}

	public List<Subdocumento<?, ?>> findSubdocByElencoDoc(int uid) {
		List<Subdocumento<?,?>> lista = new ArrayList<Subdocumento<?,?>>();
		List<SiacTSubdoc> siacTSubdocs = siacTSubdocRepository.findQuoteLegateAdElenco(uid);
		if(siacTSubdocs != null && !siacTSubdocs.isEmpty()){
			for(SiacTSubdoc siacTSubdoc: siacTSubdocs){
				Subdocumento<?,?> subdoc = new Subdocumento<Documento<?,?>, SubdocumentoIva<?,?,?>>();
				subdoc.setUid(siacTSubdoc.getUid());
				lista.add(subdoc);
			}
		}
		return lista;
	}

	public void eliminaElencoDocumenti(int uid) {
		SiacTElencoDoc siacTElencoDoc = siacTElencoDocRepository.findOne(uid);
		siacTElencoDoc.setDataCancellazioneIfNotSet(new Date());
		siacTElencoDocRepository.flush();
	}

	public List<ElencoDocumentiAllegato> findByAllegatoAttoUid(Integer uid) {
		List<SiacTElencoDoc> siacTElencoDocs = siacTElencoDocRepository.findByAttoalId(uid);
		return convertiLista(siacTElencoDocs, ElencoDocumentiAllegato.class, BilMapId.SiacTElencoDoc_ElencoDocumentiAllegato_Base);
	}

	



	/**
	 * Find elenco documenti allegato by id
	 *
	 * @param uid the uid
	 * @param mapId the map id
	 * @return the elenco documenti allegato
	 */
	public ListaPaginata<Subdocumento<?,?>> findElencoQuoteDocumentiAllegatoPageableById(Integer uid,Soggetto sog, ParametriPaginazione parametriPaginazione) {
		final String methodName = "findElencoDocumentiAllegatoPageableByIdAndMapId";
		log.debug(methodName, "uid: "+ uid );
		//SIAC-5589 aggiungere soggettofiltro soggetto
		Integer soggettoId = mapToUidIfNotZero(sog);
		Integer enteId= mapToUidIfNotZero(ente);

		String soggettoCode= null;
		if (sog!=null){
			 soggettoCode = sog.getCodiceSoggetto();
		}
		
		Page<SiacTSubdoc>  lista = elencoDocumentiAllegatoDao.ricercaSinteticaQuoteElenco(uid, soggettoId, soggettoCode, enteId,toPageable(parametriPaginazione));
		if(lista == null) {
			log.debug(methodName, "Impossibile trovare l'ElencoDocumentiAllegato con id: " + uid);
		}		
		return toListaSubdocPaginata(lista);
	}


	private ListaPaginata<Subdocumento<?, ?>> toListaSubdocPaginata(Page<SiacTSubdoc> lista) {

		
		final String methodName = "toListaSubdocPaginata";
		ListaPaginataImpl<Subdocumento<?, ?>> list = new ListaPaginataImpl<Subdocumento<?, ?>>();

		if (lista == null || !lista.hasContent()) {
			return list;
		}

		int elementsPerPage = 1 + (int) (lista.getTotalElements() / lista
				.getTotalPages());

		list.setPaginaCorrente(lista.getNumber());
		list.setTotaleElementi((int) lista.getTotalElements());
		list.setTotalePagine(lista.getTotalPages());
		list.setHasPaginaPrecedente(lista.hasPreviousPage());
		list.setHasPaginaSuccessiva(lista.hasNextPage());
		list.setNumeroElementoInizio(1 + lista.getNumber()
				* elementsPerPage);
		list.setNumeroElementoFine(lista.getNumber() * elementsPerPage
				+ lista.getNumberOfElements());

		for (SiacTSubdoc dto : lista.getContent()){
			String docFamTipoCode = dto.getSiacTDoc().getSiacDDocTipo().getSiacDDocFamTipo().getDocFamTipoCode();
			SiacDDocFamTipoEnum docFamTipoEnum = SiacDDocFamTipoEnum.byCodice(docFamTipoCode);
			
			if(SiacDDocFamTipoEnum.Spesa.equals(docFamTipoEnum) || SiacDDocFamTipoEnum.IvaSpesa.equals(docFamTipoEnum)){
				list.add(map(dto, SubdocumentoSpesa.class, BilMapId.SiacTSubdoc_SubdocumentoSpesa_Allegato));
			} else if(SiacDDocFamTipoEnum.Entrata.equals(docFamTipoEnum) || SiacDDocFamTipoEnum.IvaEntrata.equals(docFamTipoEnum)){			
				list.add(map(dto, SubdocumentoEntrata.class, BilMapId.SiacTSubdoc_SubdocumentoEntrata_Allegato));

			} else {
				throw new IllegalArgumentException("DocFamTipo non gestito: "+docFamTipoEnum);
			}
			
		}

		log.debug(methodName, "toListaPaginata -  PaginaCorrente: "
				+ list.getPaginaCorrente() + " TotaleElementi: "
				+ list.getTotaleElementi() + " TotalePagine: "
				+ list.getTotalePagine());

		return list;
		
	}
	/**
	 * Find elenco documenti allegato by id.
	 *
	 * @param uid the uid
	 * @return the elenco documenti allegato
	 */
	public ElencoDocumentiAllegato findElencoDocumentiAllegatoMinimalLightById(Integer uid) {
		return findElencoDocumentiAllegatoByIdAndMapId(uid, BilMapId.SiacTElencoDoc_ElencoDocumentiAllegato_Minimal_light);
	}
	
	/**
	 * Ricerca l'elenco corrispondente all'anno e al numero
	 * @param anno l'anno dell'elenco
	 * @param numero il numero dell'elenco
	 * @param ente l'ente proprietario dell'elenco
	 * @param modelDetails i model details
	 * @return l'elenco per dato anno e numero
	 */
	public ElencoDocumentiAllegato findElencoDocByAnnoNumero(Integer anno, Integer numero, Ente ente, ElencoDocumentiAllegatoModelDetail... modelDetails) {
		final String methodName = "findElencoDocByAnnoNumero";
		List<SiacTElencoDoc> siacTElencoDocs = siacTElencoDocRepository.findByEldocAnnoEldocNumeroEnteProprietarioId(anno, numero, ente.getUid());
		if(siacTElencoDocs.isEmpty()) {
			log.debug(methodName, "Nessun elenco corrispondente ad anno " + anno + ", numero " + numero + " per l'ente " + ente.getUid());
			return null;
		}
		return mapNotNull(siacTElencoDocs.get(0), ElencoDocumentiAllegato.class, BilMapId.SiacTElencoDoc_ElencoDocumentiAllegato_Minimal_light, Converters.byModelDetails(modelDetails));
	}

	/**
	 * Xst quote senza liquidazione by elenco id.
	 *
	 * @param uid the uid
	 * @return true, if successful
	 */
	public boolean xstQuoteSenzaLiquidazioneByElencoId(int uid) {
		int quoteSenzaLiq = siacTElencoDocRepository.countQuoteSpesaSenzaLiquidazioneByElencoId(uid).intValue();
		//log.info("xstQuoteSenzaLiquidazioneByElencoId", "ris "+ris);
		return quoteSenzaLiq != 0;
	}
	
	/**
	 * Find elenchi by atto amministrativo and stato elenco.
	 *
	 * @param allegato the allegato
	 * @param statoOperativo the stato operativo
	 * @param modelDetails the model details
	 * @return the list
	 */
	//SIAC-5584
	public List<ElencoDocumentiAllegato> findElenchiByAllegatoAttoAndStatoElenco(AllegatoAtto allegato, StatoOperativoElencoDocumenti statoOperativo, ModelDetailEnum... modelDetails){
		final String methodName = "findElenchiByAttoAmministrativoAndStatoElenco";
		List<SiacTElencoDoc> siacTElencoDocs = siacTElencoDocRepository.findEldocByAttoalIdStatoElenco(allegato.getUid(), SiacDElencoDocStatoEnum.byStatoOperativo(statoOperativo).getCodice());
		if(siacTElencoDocs.isEmpty()) {
			log.debug(methodName, "Nessun elenco corrispondente ad allegato con uid:  " + allegato.getUid());
			return null;
		}
		return convertiLista(siacTElencoDocs, ElencoDocumentiAllegato.class,BilMapId.SiacTElencoDoc_ElencoDocumentiAllegato_Minimal_light, Converters.byModelDetails(modelDetails));
	}

	
}
