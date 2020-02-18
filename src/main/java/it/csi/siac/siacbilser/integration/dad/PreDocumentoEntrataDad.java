/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.entity.SiacDDocFamTipo;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgestT;
import it.csi.siac.siacbilser.integration.entity.SiacTPredoc;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocFamTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTPredocOrderByEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.base.Converters;
import it.csi.siac.siacbilser.model.ModelDetail;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.OrdinamentoPreDocumentoEntrata;
import it.csi.siac.siacfin2ser.model.CausaleEntrata;
import it.csi.siac.siacfin2ser.model.DecodificaEnum;
import it.csi.siac.siacfin2ser.model.PreDocumentoEntrata;
import it.csi.siac.siacfin2ser.model.PreDocumentoEntrataModelDetail;
import it.csi.siac.siacfin2ser.model.StatoOperativoPreDocumento;
import it.csi.siac.siacfin2ser.model.TipoCausale;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo;

/**
 * Data access delegate di un PreDocumentoEntrata.
 *
 * @author Domenico
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class PreDocumentoEntrataDad extends PreDocumentoDad<PreDocumentoEntrata, PreDocumentoEntrataModelDetail> {
	
	@Override
	public PreDocumentoEntrata findPreDocumentoById(Integer uid) {
		final String methodName = "findPreDocumentoEntrataById";
		log.debug(methodName, "uid: "+ uid);
		SiacTPredoc siacTPredoc = preDocumentoDao.findById(uid);
		if(siacTPredoc == null) {
			log.debug(methodName, "Impossibile trovare il PreDocumentoEntrata con id: " + uid);
		}
		return mapNotNull(siacTPredoc, PreDocumentoEntrata.class, BilMapId.SiacTPredoc_PreDocumentoEntrata);
	}
	
	@Override
	public PreDocumentoEntrata findPreDocumentoByIdModelDetail(Integer uid, PreDocumentoEntrataModelDetail... modelDetails) {
		final String methodName = "findPreDocumentoEntrataByIdModelDetail";
		log.debug(methodName, "uid: "+ uid);
		SiacTPredoc siacTPredoc = preDocumentoDao.findById(uid);
		if(siacTPredoc == null) {
			log.debug(methodName, "Impossibile trovare il PreDocumentoEntrata con id: " + uid);
		}
		return mapNotNull(siacTPredoc, PreDocumentoEntrata.class, BilMapId.SiacTPredoc_PreDocumentoEntrata, Converters.byModelDetails(modelDetails));
	}
	
	@Override
	protected SiacTPredoc buildSiacTPredoc(PreDocumentoEntrata documento) {
		SiacTPredoc siacTPredoc = new SiacTPredoc();
		siacTPredoc.setLoginOperazione(loginOperazione);
		documento.setLoginOperazione(loginOperazione);
		map(documento, siacTPredoc, BilMapId.SiacTPredoc_PreDocumentoEntrata);
		siacTPredoc.setSiacDDocFamTipo(eef.getEntity(SiacDDocFamTipoEnum.Entrata, ente.getUid(), SiacDDocFamTipo.class));
		return siacTPredoc;
	}
	
	/**
	 * Ricerca sintetica pre documento.
	 *
	 * @param preDoc the pre doc
	 * @param dataCompetenzaDa the data competenza da
	 * @param dataCompetenzaA the data competenza a
	 * @param dataTrasmissioneA the data trasmissione da
	 * @param dataTrasmissioneDa the data trasmissione a
	 * @param causaleEntrataMancante the causale entrata mancante
	 * @param soggettoMancante the soggetto mancante
	 * @param provvedimentoMancante the provvedimento mancante
	 * @param contoCorrenteMancante the conto corrente mancante
	 * @param parametriPaginazione the parametri paginazione
	 * @return the lista paginata
	 */
	public PreDocumentoEntrata ricercaPuntualePreDocumento(PreDocumentoEntrata preDoc) {
		final String methodName = "ricercaPuntualePreDocumentoEntrata";
		
		ParametriPaginazione parametriPaginazione = new ParametriPaginazione();
		parametriPaginazione.setNumeroPagina(0);
		parametriPaginazione.setElementiPerPagina(1000);
		
		Page<SiacTPredoc> lista = preDocumentoDao.ricercaSinteticaPreDocumento(
				preDoc.getEnte().getUid(),
				SiacDDocFamTipoEnum.Entrata,
				preDoc.getNumero(),
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
				Boolean.FALSE,
				null,
				Boolean.FALSE,
				preDoc.getStatoOperativoPreDocumento()!=null?preDoc.getStatoOperativoPreDocumento().getCodice():null,
				null,
				null,
				null,
				null,
				Boolean.FALSE,
				null,
				null,
				Boolean.FALSE,
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
			log.debug(methodName, "Nessun predocumento trovato con chiave: " +preDoc.getNumero() + preDoc.getStatoOperativoPreDocumento().getDescrizione());
			return null;
		}
		
		SiacTPredoc siacTPredoc = lista.getContent().get(0);
		
		return mapNotNull(siacTPredoc, PreDocumentoEntrata.class, BilMapId.SiacTPredoc_PreDocumentoEntrata_Base);
	}
	
	/**
	 * Ricerca sintetica pre documento.
	 *
	 * @param preDoc the pre doc
	 * @param dataCompetenzaDa the data competenza da
	 * @param dataCompetenzaA the data competenza a
	 * @param dataTrasmissioneA the data trasmissione da
	 * @param dataTrasmissioneDa the data trasmissione a
	 * @param causaleEntrataMancante the causale entrata mancante
	 * @param soggettoMancante the soggetto mancante
	 * @param provvedimentoMancante the provvedimento mancante
	 * @param contoCorrenteMancante the conto corrente mancante
	 * @param parametriPaginazione the parametri paginazione
	 * @return the lista paginata
	 */
	public ListaPaginata<PreDocumentoEntrata> ricercaSinteticaPreDocumento(PreDocumentoEntrata preDoc, TipoCausale tipoCausale, Date dataCompetenzaDa,
			Date dataCompetenzaA, Date dataTrasmissioneDa, Date dataTrasmissioneA, Boolean causaleEntrataMancante,
			 Boolean soggettoMancante, Boolean provvedimentoMancante,
			 Boolean contoCorrenteMancante, Boolean nonAnnullati, Ordinativo ordinativo,
			 OrdinamentoPreDocumentoEntrata ordinamentoPreDocumentoEntrata, ParametriPaginazione parametriPaginazione) {
		
		Set<SiacTPredocOrderByEnum> siacTPredocOrderBy = new LinkedHashSet<SiacTPredocOrderByEnum>();
		if(ordinamentoPreDocumentoEntrata != null) {
			siacTPredocOrderBy.add(SiacTPredocOrderByEnum.byOrdinamentoPreDocumentoEntrata(ordinamentoPreDocumentoEntrata));
		}
		siacTPredocOrderBy.add(SiacTPredocOrderByEnum.PREDOC_NUMERO);
			
		Page<SiacTPredoc> lista = preDocumentoDao.ricercaSinteticaPreDocumento(
				preDoc.getEnte().getUid(),
				SiacDDocFamTipoEnum.Entrata,
				preDoc.getNumero(),
				preDoc.getDatiAnagraficiPreDocumento()!=null?preDoc.getDatiAnagraficiPreDocumento().getRagioneSociale():null,
				preDoc.getDatiAnagraficiPreDocumento()!=null?preDoc.getDatiAnagraficiPreDocumento().getCognome():null,
				preDoc.getDatiAnagraficiPreDocumento()!=null?preDoc.getDatiAnagraficiPreDocumento().getNome():null,
				preDoc.getDatiAnagraficiPreDocumento()!=null?preDoc.getDatiAnagraficiPreDocumento().getCodiceFiscale():null,
				preDoc.getDatiAnagraficiPreDocumento()!=null?preDoc.getDatiAnagraficiPreDocumento().getPartitaIva():null,
				preDoc.getPeriodoCompetenza(),
				preDoc.getImporto(),
				dataCompetenzaDa,
				dataCompetenzaA,
				dataTrasmissioneDa,
				dataTrasmissioneA,
				mapToUidIfNotZero(preDoc.getStrutturaAmministrativoContabile()),
				mapToUidIfNotZero(preDoc.getCausaleEntrata()),				
				mapToUidIfNotZero(tipoCausale),
				causaleEntrataMancante,
				null, //contoTesorirID
				false, //contoTesoreriaMancante
				preDoc.getStatoOperativoPreDocumento()!=null?preDoc.getStatoOperativoPreDocumento().getCodice():null,
				mapToUidIfNotZero(preDoc.getCapitoloEntrataGestione()),
				mapToUidIfNotZero(preDoc.getAccertamento()),
				mapToUidIfNotZero(preDoc.getSubAccertamento()),
				mapToUidIfNotZero(preDoc.getSoggetto()),
				soggettoMancante,
				mapToUidIfNotZero(preDoc.getProvvisorioDiCassa()),
				mapToUidIfNotZero(preDoc.getAttoAmministrativo()),
				provvedimentoMancante,
				(preDoc.getSubDocumento()!=null && preDoc.getSubDocumento().getDocumento()!=null)?preDoc.getSubDocumento().getDocumento().getAnno():null,
				(preDoc.getSubDocumento()!=null && preDoc.getSubDocumento().getDocumento()!=null)?preDoc.getSubDocumento().getDocumento().getNumero():null,
				(preDoc.getSubDocumento()!=null && preDoc.getSubDocumento().getDocumento()!=null && preDoc.getSubDocumento().getDocumento().getTipoDocumento()!=null)?mapToUidIfNotZero(preDoc.getSubDocumento().getDocumento().getTipoDocumento()):null,
				preDoc.getContoCorrente() != null && preDoc.getContoCorrente().getUid() !=0 ? preDoc.getContoCorrente().getUid() : null,
				contoCorrenteMancante,
				nonAnnullati,
				// SIAC-4772
				ordinativo != null && ordinativo.getAnno() != null ? ordinativo.getAnno() : null,
				ordinativo != null ? toBigDecimal(ordinativo.getNumero()) : null,
				mapToUidIfNotZero(preDoc.getElencoDocumentiAllegato()),
				preDoc.getElencoDocumentiAllegato() != null ? preDoc.getElencoDocumentiAllegato().getAnno() : null,
				preDoc.getElencoDocumentiAllegato() != null ? preDoc.getElencoDocumentiAllegato().getNumero() : null,
				removeNullElements(siacTPredocOrderBy),
				toPageable(parametriPaginazione));
		
		return toListaPaginata(lista, PreDocumentoEntrata.class, BilMapId.SiacTPredoc_PreDocumentoEntrata_Base);
	}
	
	protected <A, B extends Collection<A>> B removeNullElements(B collection) {
		if(collection == null) {
			return null;
		}
		collection.removeAll(Collections.singleton(null));
		return collection;
	}
	
	/**
	 * Ricerca sintetica pre documento con model detail.
	 *
	 * @param preDoc the pre doc
	 * @param dataCompetenzaDa the data competenza da
	 * @param dataCompetenzaA the data competenza a
	 * @param dataTrasmissioneA the data trasmissione da
	 * @param dataTrasmissioneDa the data trasmissione a
	 * @param causaleEntrataMancante the causale entrata mancante
	 * @param soggettoMancante the soggetto mancante
	 * @param provvedimentoMancante the provvedimento mancante
	 * @param contoCorrenteMancante the conto corrente mancante
	 * @param parametriPaginazione the parametri paginazione
	 * @return the lista paginata
	 */
	public ListaPaginata<PreDocumentoEntrata> ricercaSinteticaPreDocumentoEntrataModelDetail(PreDocumentoEntrata preDoc, TipoCausale tipoCausale, Date dataCompetenzaDa,
			Date dataCompetenzaA, Date dataTrasmissioneDa, Date dataTrasmissioneA, Boolean causaleEntrataMancante,
			Boolean soggettoMancante, Boolean provvedimentoMancante,
			Boolean contoCorrenteMancante, Boolean nonAnnullati, Ordinativo ordinativo,
			OrdinamentoPreDocumentoEntrata ordinamentoPreDocumentoEntrata, ParametriPaginazione parametriPaginazione, ModelDetail... modelDetails) {
			
		Set<SiacTPredocOrderByEnum> siacTPredocOrderBy = new LinkedHashSet<SiacTPredocOrderByEnum>();
		if(ordinamentoPreDocumentoEntrata != null) {
			siacTPredocOrderBy.add(SiacTPredocOrderByEnum.byOrdinamentoPreDocumentoEntrata(ordinamentoPreDocumentoEntrata));
		}
		siacTPredocOrderBy.add(SiacTPredocOrderByEnum.PREDOC_NUMERO);
		
		Page<SiacTPredoc> lista = preDocumentoDao.ricercaSinteticaPreDocumento(
				preDoc.getEnte().getUid(),
				SiacDDocFamTipoEnum.Entrata,
				preDoc.getNumero(),
				preDoc.getDatiAnagraficiPreDocumento() != null ? preDoc.getDatiAnagraficiPreDocumento().getRagioneSociale() : null,
				preDoc.getDatiAnagraficiPreDocumento() != null ? preDoc.getDatiAnagraficiPreDocumento().getCognome() : null,
				preDoc.getDatiAnagraficiPreDocumento() != null ? preDoc.getDatiAnagraficiPreDocumento().getNome() : null,
				preDoc.getDatiAnagraficiPreDocumento() != null ? preDoc.getDatiAnagraficiPreDocumento().getCodiceFiscale() : null,
				preDoc.getDatiAnagraficiPreDocumento() != null ? preDoc.getDatiAnagraficiPreDocumento().getPartitaIva() : null,
				preDoc.getPeriodoCompetenza(),
				preDoc.getImporto() == null ? preDoc.getImporto() : null,
				dataCompetenzaDa,
				dataCompetenzaA,
				dataTrasmissioneDa,
				dataTrasmissioneA,
				mapToUidIfNotZero(preDoc.getStrutturaAmministrativoContabile()),
				mapToUidIfNotZero(preDoc.getCausaleEntrata()),
				mapToUidIfNotZero(tipoCausale),
				causaleEntrataMancante,
				null,
				false,
				preDoc.getStatoOperativoPreDocumento() != null ? preDoc.getStatoOperativoPreDocumento().getCodice() : null,
				mapToUidIfNotZero(preDoc.getCapitoloEntrataGestione()),
				mapToUidIfNotZero(preDoc.getAccertamento()),
				mapToUidIfNotZero(preDoc.getSubAccertamento()),
				mapToUidIfNotZero(preDoc.getSoggetto()),
				soggettoMancante,
				mapToUidIfNotZero(preDoc.getProvvisorioDiCassa()),
				mapToUidIfNotZero(preDoc.getAttoAmministrativo()),
				provvedimentoMancante,
				preDoc.getSubDocumento() != null && preDoc.getSubDocumento().getDocumento() != null ? preDoc.getSubDocumento().getDocumento().getAnno() : null,
				preDoc.getSubDocumento() != null && preDoc.getSubDocumento().getDocumento() != null ? preDoc.getSubDocumento().getDocumento().getNumero() : null,
				preDoc.getSubDocumento() != null && preDoc.getSubDocumento().getDocumento() != null && preDoc.getSubDocumento().getDocumento().getTipoDocumento() != null ? mapToUidIfNotZero(preDoc.getSubDocumento().getDocumento().getTipoDocumento()) : null,
				preDoc.getContoCorrente() != null && preDoc.getContoCorrente().getUid() !=0 ? preDoc.getContoCorrente().getUid() : null,
				contoCorrenteMancante,
				nonAnnullati,
				// SIAC-4772
				ordinativo != null && ordinativo.getAnno() != null ? ordinativo.getAnno() : null,
				ordinativo != null ? toBigDecimal(ordinativo.getNumero()) : null,
				mapToUidIfNotZero(preDoc.getElencoDocumentiAllegato()),
				preDoc.getElencoDocumentiAllegato() != null ? preDoc.getElencoDocumentiAllegato().getAnno() : null,
				preDoc.getElencoDocumentiAllegato() != null ? preDoc.getElencoDocumentiAllegato().getNumero() : null,
				siacTPredocOrderBy,
				toPageable(parametriPaginazione));
		
		return toListaPaginata(lista, PreDocumentoEntrata.class, BilMapId.SiacTPredoc_PreDocumentoEntrata_ModelDetail, modelDetails);
		
	}

	/**
	 * Ricerca sintetica importo pre documento.
	 *
	 * @param preDoc the pre doc
	 * @param dataCompetenzaDa the data competenza da
	 * @param dataCompetenzaA the data competenza a
	 * @param dataTrasmissioneA the data trasmissione da
	 * @param dataTrasmissioneDa the data trasmissione a
	 * @param causaleEntrataMancante the causale entrata mancante
	 * @param soggettoMancante the soggetto mancante
	 * @param provvedimentoMancante the provvedimento mancante
	 * @param contoCorrenteMancante the conto corrente mancante
	 * @param parametriPaginazione the parametri paginazione
	 * @return the lista paginata
	 */
	public BigDecimal ricercaSinteticaPreDocumentoImportoTotale(PreDocumentoEntrata preDoc, TipoCausale tipoCausale, Date dataCompetenzaDa,
			Date dataCompetenzaA, Date dataTrasmissioneDa, Date dataTrasmissioneA, Boolean causaleEntrataMancante, Boolean soggettoMancante, Boolean provvedimentoMancante,
			Boolean contoCorrenteMancante, Ordinativo ordinativo, Boolean nonAnnullati){
		
		
		BigDecimal importoTotale = preDocumentoDao.ricercaSinteticaPreDocumentoImportoTotale(
				preDoc.getEnte().getUid(),
				SiacDDocFamTipoEnum.Entrata,
				preDoc.getNumero(),
				preDoc.getDatiAnagraficiPreDocumento()!=null?preDoc.getDatiAnagraficiPreDocumento().getRagioneSociale():null,
				preDoc.getDatiAnagraficiPreDocumento()!=null?preDoc.getDatiAnagraficiPreDocumento().getCognome():null,
				preDoc.getDatiAnagraficiPreDocumento()!=null?preDoc.getDatiAnagraficiPreDocumento().getNome():null,
				preDoc.getDatiAnagraficiPreDocumento()!=null?preDoc.getDatiAnagraficiPreDocumento().getCodiceFiscale():null,
				preDoc.getDatiAnagraficiPreDocumento()!=null?preDoc.getDatiAnagraficiPreDocumento().getPartitaIva():null,
				preDoc.getPeriodoCompetenza(),
				preDoc.getImporto() == null ? preDoc.getImporto() : null,
				dataCompetenzaDa,
				dataCompetenzaA,
				dataTrasmissioneDa,
				dataTrasmissioneA,
				mapToUidIfNotZero(preDoc.getStrutturaAmministrativoContabile()),
				mapToUidIfNotZero(preDoc.getCausaleEntrata()),				
				mapToUidIfNotZero(tipoCausale),
				causaleEntrataMancante,
				null,
				false,
				preDoc.getStatoOperativoPreDocumento()!=null?preDoc.getStatoOperativoPreDocumento().getCodice():null,
				mapToUidIfNotZero(preDoc.getCapitoloEntrataGestione()),
				mapToUidIfNotZero(preDoc.getAccertamento()),
				mapToUidIfNotZero(preDoc.getSubAccertamento()),
				mapToUidIfNotZero(preDoc.getSoggetto()),
				soggettoMancante,
				mapToUidIfNotZero(preDoc.getProvvisorioDiCassa()),
				mapToUidIfNotZero(preDoc.getAttoAmministrativo()),
				provvedimentoMancante,
				(preDoc.getSubDocumento()!=null && preDoc.getSubDocumento().getDocumento()!=null)?preDoc.getSubDocumento().getDocumento().getAnno():null,
				(preDoc.getSubDocumento()!=null && preDoc.getSubDocumento().getDocumento()!=null)?preDoc.getSubDocumento().getDocumento().getNumero():null,
				(preDoc.getSubDocumento()!=null && preDoc.getSubDocumento().getDocumento()!=null && preDoc.getSubDocumento().getDocumento().getTipoDocumento()!=null)?mapToUidIfNotZero(preDoc.getSubDocumento().getDocumento().getTipoDocumento()):null,
				preDoc.getContoCorrente() != null && preDoc.getContoCorrente().getUid() !=0 ? preDoc.getContoCorrente().getUid() : null,
				contoCorrenteMancante,
				nonAnnullati,
				// SIAC-4772
				ordinativo != null && ordinativo.getAnno() != null ? ordinativo.getAnno() : null,
				ordinativo != null ? toBigDecimal(ordinativo.getNumero()) : null,
				mapToUidIfNotZero(preDoc.getElencoDocumentiAllegato()),
				preDoc.getElencoDocumentiAllegato() != null ? preDoc.getElencoDocumentiAllegato().getAnno() : null,
				preDoc.getElencoDocumentiAllegato() != null ? preDoc.getElencoDocumentiAllegato().getNumero() : null
				);
		
		return importoTotale;
	}
	
	protected <E extends DecodificaEnum> List<String> projectToCode(Collection<E> decodifiche) {
		List<String> res = new ArrayList<String>();
		if(decodifiche != null) {
			for(E de : decodifiche) {
				res.add(de.getCodice());
			}
		}
		return res;
	}
	
	@Override
	public PreDocumentoEntrata findDocumentiCollegatiByIdPreDocumento(Integer uid) {
//		final String methodName = "findDocumentiCollegatiByIdPreDocumento";		
//		log.debug(methodName, "uid: "+ uid);
//		SiacTPredoc siacTPredoc = preDocumentoDao.findById(uid);
//		if(siacTPredoc == null) {
//			log.debug(methodName, "Impossibile trovare il PreDocumentoEntrata con id: " + uid);
//		}
//		return mapNotNull(siacTPredoc, PreDocumentoEntrata.class, BilMapId.SiacTPredoc_PreDocumentoEntrata_Collegati);
		return null;
	}

	public Integer findUidSubAccertamentoPreDocumentoSpesaById(int uid) {
		final String methodName = "findUidImpegnoOSubImpegnoPreDocumentoSpesaById";		
		log.debug(methodName, "uid: "+ uid);
		SiacTMovgestT siacTMovgestT = siacTPredocNumRepository.findMovgestTSByIdPredoc(uid);
		if(siacTMovgestT == null) {
			log.debug(methodName, "Returning null. Impossibile trovare il movimento gestione legato al preDocumentoSpesa con id: " + uid);
			return null;
		}
//		restituisco l'uid solo se è legato ad un subaccertamento, altrimenti null
		if(siacTMovgestT.getSiacTMovgestIdPadre() != null){
			return siacTMovgestT.getUid();
		}else{
			return null;
		}
	}




	public Integer findUidAccertamentoPreDocumentoSpesaById(int uid) {
		final String methodName = "findUidImpegnoOSubImpegnoPreDocumentoSpesaById";		
		log.debug(methodName, "uid: "+ uid);
		SiacTMovgestT siacTMovgestT = siacTPredocNumRepository.findMovgestTSByIdPredoc(uid);
		if(siacTMovgestT == null) {
			log.debug(methodName, "Returning null. Impossibile trovare il movimento gestione legato al preDocumentoSpesa con id: " + uid);
			return null;
		}
//		restituisco l'uid solo se è legato ad un accertamento, altrimenti null
		if(siacTMovgestT.getSiacTMovgestIdPadre() == null){
			return siacTMovgestT.getSiacTMovgest().getMovgestId();
		}else{
			return null;
		}
	}
	
	public void associaAccertamentoByElencoAndStatiOperativi(Accertamento accertamento, SubAccertamento subAccertamento, StatoOperativoPreDocumento statoDaImpostare, Integer eldocId, StatoOperativoPreDocumento... statiOperativi) {
		// Mi creo un predocumento fittizio per avere la r comoda
		SiacTPredoc template = buildSiacTPredocTemporaneo(accertamento, subAccertamento, statoDaImpostare);
		
		// Aggiornamento dei predoc
		List<String> predocStatoCodes = toPredocStatoCodes(statiOperativi);
		preDocumentoDao.associaMovgestByEldocIdAndPredocStato(template, eldocId, null, null, predocStatoCodes);
	}

	private SiacTPredoc buildSiacTPredocTemporaneo(Accertamento impegno, SubAccertamento subAccertamento, StatoOperativoPreDocumento statoDaImpostare) {
		PreDocumentoEntrata tmp = new PreDocumentoEntrata();
		tmp.setAccertamento(impegno);
		tmp.setSubAccertamento(subAccertamento);
		tmp.setStatoOperativoPreDocumento(statoDaImpostare);
		tmp.setEnte(ente);
		SiacTPredoc stp = buildSiacTPredoc(tmp);
		return stp;
	}
	
	public List<PreDocumentoEntrata> findPreDocumentoByElencoIdAndStatiOperativi(Integer uidElenco, Integer uidImpegno, Integer uidSubImpegno,
			Collection<StatoOperativoPreDocumento> statiOperativi, PreDocumentoEntrataModelDetail... modelDetails) {
		List<String> predocStatoCodes = toPredocStatoCodes(statiOperativi);
		List<SiacTPredoc> siacTPredocs = preDocumentoDao.findByEldocIdAndPredocStato(uidElenco, uidImpegno, uidSubImpegno, predocStatoCodes);
		
		return convertiLista(siacTPredocs, PreDocumentoEntrata.class, BilMapId.SiacTPredoc_PreDocumentoEntrata_ModelDetail, Converters.byModelDetails(modelDetails));
	}

	public List<PreDocumentoEntrata> associaMovgestAttoAmmSoggettoByCausaleDataCompetenzaAndStatiOperativi(PreDocumentoEntrata tmp, Integer causEntId, Date dataCompetenzaDa, Date dataCompetenzaA, StatoOperativoPreDocumento... statiOperativi) {
		// Mi creo un predocumento fittizio per avere la r comoda
		SiacTPredoc template = buildSiacTPredoc(tmp);
		
		// Aggiornamento dei predoc
		List<String> predocStatoCodes = toPredocStatoCodes(statiOperativi);
		List<SiacTPredoc> siacTPredocs = preDocumentoDao.associaMovgestSoggettoAttoAmmByDataCompetenzaCausIdAndPredocStato(template, ente.getUid(), causEntId,
				dataCompetenzaDa, dataCompetenzaA, predocStatoCodes);
		return convertiLista(siacTPredocs, PreDocumentoEntrata.class, BilMapId.SiacTPredoc_PreDocumentoEntrata_ModelDetail);
	}
	
	public List<PreDocumentoEntrata> findPreDocumentiByCausaleDataCompetenzaStati(Integer causEntId, Date dataCompetenzaDa, Date dataCompetenzaA, Collection<StatoOperativoPreDocumento> statiOperativi, PreDocumentoEntrataModelDetail... modelDetails) {
		List<String> predocStatoCodes = toPredocStatoCodes(statiOperativi);
		List<SiacTPredoc> siacTPredocs = preDocumentoDao.findByEnteCausIdDataCompetenzaDaAPredocStato(ente.getUid(), SiacDDocFamTipoEnum.Entrata.getCodice(), causEntId, dataCompetenzaDa, dataCompetenzaA, predocStatoCodes);
		return convertiLista(siacTPredocs, PreDocumentoEntrata.class, BilMapId.SiacTPredoc_PreDocumentoEntrata_ModelDetail, Converters.byModelDetails(modelDetails));
	}

	public Map<StatoOperativoPreDocumento, BigDecimal> findImportoPreDocumentoByStatiOperativiGroupByStatoOperativo(Date dataCompetenzaDa, Date dataCompetenzaA, CausaleEntrata causaleEntrata, StatoOperativoPreDocumento[] statiOperativi) {
		List<String> predocStatoCodes = toPredocStatoCodes(statiOperativi);
		Map<String, BigDecimal> importi = preDocumentoDao.findImportiByPredocDataCompetenzaDaAAndCausaleEpAndPredocStato(mapToUidIfNotZero(ente), SiacDDocFamTipoEnum.Entrata.getCodice(),
				dataCompetenzaDa, dataCompetenzaA, mapToUidIfNotZero(causaleEntrata), predocStatoCodes);
		
		Map<StatoOperativoPreDocumento, BigDecimal> result = new HashMap<StatoOperativoPreDocumento, BigDecimal>();
		for(StatoOperativoPreDocumento sopd : StatoOperativoPreDocumento.values()) {
			BigDecimal importo = importi.get(sopd.getCodice());
			result.put(sopd, importo != null ? importo : BigDecimal.ZERO);
		}
		return result;
	}

	public Map<StatoOperativoPreDocumento, Long> countPreDocumentoByStatiOperativiGroupByStatoOperativo(Date dataCompetenzaDa, Date dataCompetenzaA, CausaleEntrata causaleEntrata, StatoOperativoPreDocumento[] statiOperativi) {
		List<String> predocStatoCodes = toPredocStatoCodes(statiOperativi);
		Map<String, Long> importi = preDocumentoDao.countByPredocDataCompetenzaDaAAndCausaleEpAndPredocStato(mapToUidIfNotZero(ente), SiacDDocFamTipoEnum.Entrata.getCodice(),
				dataCompetenzaDa, dataCompetenzaA, mapToUidIfNotZero(causaleEntrata), predocStatoCodes);
		
		Map<StatoOperativoPreDocumento, Long> result = new HashMap<StatoOperativoPreDocumento, Long>();
		for(StatoOperativoPreDocumento sopd : StatoOperativoPreDocumento.values()) {
			Long importo = importi.get(sopd.getCodice());
			result.put(sopd, importo != null ? importo : Long.valueOf(0));
		}
		return result;
	}

}
