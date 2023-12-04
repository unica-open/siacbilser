/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.entity.SiacDDocFamTipo;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgestT;
import it.csi.siac.siacbilser.integration.entity.SiacTPredoc;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocFamTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.base.Converters;
import it.csi.siac.siaccommon.model.ModelDetailEnum;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.model.PreDocumentoSpesa;
import it.csi.siac.siacfin2ser.model.PreDocumentoSpesaModelDetail;
import it.csi.siac.siacfin2ser.model.StatoOperativoPreDocumento;
import it.csi.siac.siacfin2ser.model.TipoCausale;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo;

/**
 * Data access delegate di un PreDocumentoSpesa .
 *
 * @author Domenico
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class PreDocumentoSpesaDad extends PreDocumentoDad<PreDocumentoSpesa, PreDocumentoSpesaModelDetail> {
	
	@Override
	public PreDocumentoSpesa findPreDocumentoById(Integer uid) {
		final String methodName = "findPreDocumentoSpesaById";
		log.debug(methodName, "uid: "+ uid);
		SiacTPredoc siacTPredoc = preDocumentoDao.findById(uid);
		if(siacTPredoc == null) {
			log.debug(methodName, "Impossibile trovare il PreDocumentoSpesa con id: " + uid);
		}
		return  mapNotNull(siacTPredoc, PreDocumentoSpesa.class, BilMapId.SiacTPredoc_PreDocumentoSpesa);
	}
	
	@Override
	public PreDocumentoSpesa findPreDocumentoByIdModelDetail(Integer uid, PreDocumentoSpesaModelDetail... modelDetails) {
		final String methodName = "findPreDocumentoSpesaById";
		log.debug(methodName, "uid: "+ uid);
		SiacTPredoc siacTPredoc = preDocumentoDao.findById(uid);
		if(siacTPredoc == null) {
			log.debug(methodName, "Impossibile trovare il PreDocumentoSpesa con id: " + uid);
		}
		return mapNotNull(siacTPredoc, PreDocumentoSpesa.class, BilMapId.SiacTPredoc_PreDocumentoSpesa_ModelDetail, Converters.byModelDetails(modelDetails));
	}
	
	/**
	 * Builds the siac t predoc.
	 *
	 * @param documento the documento
	 * @return the siac t predoc
	 */
	protected SiacTPredoc buildSiacTPredoc(PreDocumentoSpesa documento) {
		SiacTPredoc siacTPredoc = new SiacTPredoc();
		siacTPredoc.setLoginOperazione(loginOperazione);
		documento.setLoginOperazione(loginOperazione);
		map(documento, siacTPredoc, BilMapId.SiacTPredoc_PreDocumentoSpesa);
		siacTPredoc.setSiacDDocFamTipo(eef.getEntity(SiacDDocFamTipoEnum.Spesa, ente.getUid(), SiacDDocFamTipo.class));
		return siacTPredoc;
	}
	
	@Override
	public PreDocumentoSpesa ricercaPuntualePreDocumento(PreDocumentoSpesa preDoc) {
		final String methodName = "ricercaPuntualePreDocumentoSpesa";
		
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
				false,
				null,
				false,
				preDoc.getStatoOperativoPreDocumento()!=null?preDoc.getStatoOperativoPreDocumento().getCodice():null,
				null,
				null,
				null,
				null,
				false,
				null,
				null,
				false,
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
			log.debug(methodName, "Nessun predocumento trovato con chiave: " + preDoc.getNumero() + preDoc.getStatoOperativoPreDocumento().getDescrizione() );
			return null;
		}
		
		SiacTPredoc siacTPredoc = lista.getContent().get(0);
		
		return mapNotNull(siacTPredoc, PreDocumentoSpesa.class, BilMapId.SiacTPredoc_PreDocumentoSpesa_Base);
	}
	
	/**
	 * Ricerca sintetica pre documento.
	 *
	 * @param preDoc the pre doc
	 * @param tipoCausale the tipo causale
	 * @param dataCompetenzaDa the data competenza da
	 * @param dataCompetenzaA the data competenza a
	 * @param causaleSpesaMancante the causale spesa mancante
	 * @param contoTesoreriaMancante the conto tesoreria mancante
	 * @param soggettoMancante the soggetto mancante
	 * @param provvedimentoMancante the provvedimento mancante
	 * @param parametriPaginazione the parametri paginazione
	 * @return the lista paginata
	 */
	public ListaPaginata<PreDocumentoSpesa> ricercaSinteticaPreDocumento(
			PreDocumentoSpesa preDoc, TipoCausale tipoCausale,  Date dataCompetenzaDa,
			Date dataCompetenzaA, Boolean causaleSpesaMancante,
			Boolean contoTesoreriaMancante, Boolean soggettoMancante,
			Boolean provvedimentoMancante, Boolean nonAnnullati,
			Ordinativo ordinativo,
			ParametriPaginazione parametriPaginazione) {
	
			
		Page<SiacTPredoc> lista = preDocumentoDao.ricercaSinteticaPreDocumento(
				preDoc.getEnte().getUid(),
				SiacDDocFamTipoEnum.Spesa,
				preDoc.getNumero(),
				preDoc.getDatiAnagraficiPreDocumento()!=null?preDoc.getDatiAnagraficiPreDocumento().getRagioneSociale():null,
				preDoc.getDatiAnagraficiPreDocumento()!=null?preDoc.getDatiAnagraficiPreDocumento().getCognome():null,
				preDoc.getDatiAnagraficiPreDocumento()!=null?preDoc.getDatiAnagraficiPreDocumento().getNome():null,
				preDoc.getDatiAnagraficiPreDocumento()!=null?preDoc.getDatiAnagraficiPreDocumento().getCodiceFiscale():null,
				preDoc.getDatiAnagraficiPreDocumento()!=null?preDoc.getDatiAnagraficiPreDocumento().getPartitaIva():null,
				preDoc.getPeriodoCompetenza(),
				preDoc.getImporto() != null ? preDoc.getImporto() : null,
				dataCompetenzaDa,
				dataCompetenzaA,
				null,
				null,
				mapToUidIfNotZero(preDoc.getStrutturaAmministrativoContabile()),
				mapToUidIfNotZero(preDoc.getCausaleSpesa()),				
				mapToUidIfNotZero(tipoCausale),
				causaleSpesaMancante,
				mapToUidIfNotZero(preDoc.getContoTesoreria()),
				contoTesoreriaMancante,
				preDoc.getStatoOperativoPreDocumento()!=null?preDoc.getStatoOperativoPreDocumento().getCodice():null,
				mapToUidIfNotZero(preDoc.getCapitoloUscitaGestione()),
				mapToUidIfNotZero(preDoc.getImpegno()),
				mapToUidIfNotZero(preDoc.getSubImpegno()),
				mapToUidIfNotZero(preDoc.getSoggetto()),
				soggettoMancante,
				mapToUidIfNotZero(preDoc.getProvvisorioDiCassa()),
				mapToUidIfNotZero(preDoc.getAttoAmministrativo()),
				provvedimentoMancante,
				(preDoc.getSubDocumento()!=null && preDoc.getSubDocumento().getDocumento()!=null)?preDoc.getSubDocumento().getDocumento().getAnno():null,
				(preDoc.getSubDocumento()!=null && preDoc.getSubDocumento().getDocumento()!=null)?preDoc.getSubDocumento().getDocumento().getNumero():null,
				(preDoc.getSubDocumento()!=null && preDoc.getSubDocumento().getDocumento()!=null && preDoc.getSubDocumento().getDocumento().getTipoDocumento()!=null)?mapToUidIfNotZero(preDoc.getSubDocumento().getDocumento().getTipoDocumento()):null,		
				null,
				null,
				nonAnnullati,
				ordinativo != null && ordinativo.getAnno() != null ? ordinativo.getAnno() : null,
				ordinativo != null ? toBigDecimal(ordinativo.getNumero()) : null,
				mapToUidIfNotZero(preDoc.getElencoDocumentiAllegato()),
				preDoc.getElencoDocumentiAllegato() != null ? preDoc.getElencoDocumentiAllegato().getAnno() : null,
				preDoc.getElencoDocumentiAllegato() != null ? preDoc.getElencoDocumentiAllegato().getNumero() : null,
				null,
				null,
				toPageable(parametriPaginazione));
		
		return toListaPaginata(lista, PreDocumentoSpesa.class, BilMapId.SiacTPredoc_PreDocumentoSpesa_Base);
		
	}
	
	/**
	 * Ricerca sintetica pre documento.
	 *
	 * @param preDoc the pre doc
	 * @param tipoCausale the tipo causale
	 * @param dataCompetenzaDa the data competenza da
	 * @param dataCompetenzaA the data competenza a
	 * @param causaleSpesaMancante the causale spesa mancante
	 * @param contoTesoreriaMancante the conto tesoreria mancante
	 * @param soggettoMancante the soggetto mancante
	 * @param provvedimentoMancante the provvedimento mancante
	 * @param parametriPaginazione the parametri paginazione
	 * @param modelDetails the model details
	 * @return the lista paginata
	 */
	public ListaPaginata<PreDocumentoSpesa> ricercaSinteticaPreDocumentoModelDetail(
			PreDocumentoSpesa preDoc, TipoCausale tipoCausale, Date dataCompetenzaDa,
			Date dataCompetenzaA, Boolean causaleSpesaMancante,
			Boolean contoTesoreriaMancante, Boolean soggettoMancante,
			Boolean provvedimentoMancante,
			Boolean nonAnnullati, Ordinativo ordinativo,
			ParametriPaginazione parametriPaginazione, ModelDetailEnum... modelDetails) {
	
			
		Page<SiacTPredoc> lista = preDocumentoDao.ricercaSinteticaPreDocumento(
				preDoc.getEnte().getUid(),
				SiacDDocFamTipoEnum.Spesa,
				preDoc.getNumero(),
				preDoc.getDatiAnagraficiPreDocumento() != null ? preDoc.getDatiAnagraficiPreDocumento().getRagioneSociale() : null,
				preDoc.getDatiAnagraficiPreDocumento() != null ? preDoc.getDatiAnagraficiPreDocumento().getCognome() : null,
				preDoc.getDatiAnagraficiPreDocumento() != null ? preDoc.getDatiAnagraficiPreDocumento().getNome() : null,
				preDoc.getDatiAnagraficiPreDocumento() != null ? preDoc.getDatiAnagraficiPreDocumento().getCodiceFiscale() : null,
				preDoc.getDatiAnagraficiPreDocumento() != null ? preDoc.getDatiAnagraficiPreDocumento().getPartitaIva() : null,
				preDoc.getPeriodoCompetenza(),
				preDoc.getImporto() != null ? preDoc.getImporto() : null,
				dataCompetenzaDa,
				dataCompetenzaA,
				null,
				null,
				mapToUidIfNotZero(preDoc.getStrutturaAmministrativoContabile()),
				mapToUidIfNotZero(preDoc.getCausaleSpesa()),
				mapToUidIfNotZero(tipoCausale),
				causaleSpesaMancante,
				mapToUidIfNotZero(preDoc.getContoTesoreria()),
				contoTesoreriaMancante,
				preDoc.getStatoOperativoPreDocumento() != null ? preDoc.getStatoOperativoPreDocumento().getCodice() : null,
				mapToUidIfNotZero(preDoc.getCapitoloUscitaGestione()),
				mapToUidIfNotZero(preDoc.getImpegno()),
				mapToUidIfNotZero(preDoc.getSubImpegno()),
				mapToUidIfNotZero(preDoc.getSoggetto()),
				soggettoMancante,
				mapToUidIfNotZero(preDoc.getProvvisorioDiCassa()),
				mapToUidIfNotZero(preDoc.getAttoAmministrativo()),
				provvedimentoMancante,
				preDoc.getSubDocumento() != null && preDoc.getSubDocumento().getDocumento() != null ? preDoc.getSubDocumento().getDocumento().getAnno() : null,
				preDoc.getSubDocumento() != null && preDoc.getSubDocumento().getDocumento() != null ? preDoc.getSubDocumento().getDocumento().getNumero() : null,
				preDoc.getSubDocumento() != null && preDoc.getSubDocumento().getDocumento() != null && preDoc.getSubDocumento().getDocumento().getTipoDocumento() != null ? mapToUidIfNotZero(preDoc.getSubDocumento().getDocumento().getTipoDocumento()) : null,		
				null,
				null,
				nonAnnullati,
				ordinativo != null && ordinativo.getAnno() != null ? ordinativo.getAnno() : null,
				ordinativo != null ? toBigDecimal(ordinativo.getNumero()) : null,
				mapToUidIfNotZero(preDoc.getElencoDocumentiAllegato()),
				preDoc.getElencoDocumentiAllegato() != null ? preDoc.getElencoDocumentiAllegato().getAnno() : null,
				preDoc.getElencoDocumentiAllegato() != null ? preDoc.getElencoDocumentiAllegato().getNumero() : null,
				null,
				null,
				toPageable(parametriPaginazione));
		
		return toListaPaginata(lista, PreDocumentoSpesa.class, BilMapId.SiacTPredoc_PreDocumentoSpesa_ModelDetail, modelDetails);
		
	}
	
	
	/**
	 * Ricerca sintetica pre documento importo totale.
	 *
	 * @param doc the doc
	 * @param attoAmministrativo the atto amministrativo
	 * @param impegno the impegno
	 * @param rilevanteIva the rilevante iva
	 * @param parametriPaginazione the parametri paginazione
	 * @return the big decimal
	 */	
	public BigDecimal ricercaSinteticaPreDocumentoSpesaImportoTotale(PreDocumentoSpesa preDoc, TipoCausale tipoCausale, Date dataCompetenzaDa,
			Date dataCompetenzaA, Boolean causaleSpesaMancante, Boolean contoTesoreriaMancante, Boolean soggettoMancante,
			Boolean provvedimentoMancante, Boolean nonAnnullati, Ordinativo ordinativo) {
		
		
		BigDecimal importoTotale = preDocumentoDao.ricercaSinteticaPreDocumentoImportoTotale(
				preDoc.getEnte().getUid(),
				SiacDDocFamTipoEnum.Spesa,
				preDoc.getNumero(),
				preDoc.getDatiAnagraficiPreDocumento()!=null?preDoc.getDatiAnagraficiPreDocumento().getRagioneSociale():null,
				preDoc.getDatiAnagraficiPreDocumento()!=null?preDoc.getDatiAnagraficiPreDocumento().getCognome():null,
				preDoc.getDatiAnagraficiPreDocumento()!=null?preDoc.getDatiAnagraficiPreDocumento().getNome():null,
				preDoc.getDatiAnagraficiPreDocumento()!=null?preDoc.getDatiAnagraficiPreDocumento().getCodiceFiscale():null,
				preDoc.getDatiAnagraficiPreDocumento()!=null?preDoc.getDatiAnagraficiPreDocumento().getPartitaIva():null,
				preDoc.getPeriodoCompetenza(),
				preDoc.getImporto() != null ? preDoc.getImporto() : null,
				dataCompetenzaDa,
				dataCompetenzaA,
				null,
				null,
				mapToUidIfNotZero(preDoc.getStrutturaAmministrativoContabile()),
				mapToUidIfNotZero(preDoc.getCausaleSpesa()),				
				mapToUidIfNotZero(tipoCausale),
				causaleSpesaMancante,
				mapToUidIfNotZero(preDoc.getContoTesoreria()),
				contoTesoreriaMancante,
				preDoc.getStatoOperativoPreDocumento()!=null?preDoc.getStatoOperativoPreDocumento().getCodice():null,
				mapToUidIfNotZero(preDoc.getCapitoloUscitaGestione()),
				mapToUidIfNotZero(preDoc.getImpegno()),
				mapToUidIfNotZero(preDoc.getSubImpegno()),
				mapToUidIfNotZero(preDoc.getSoggetto()),
				soggettoMancante,
				mapToUidIfNotZero(preDoc.getProvvisorioDiCassa()),
				mapToUidIfNotZero(preDoc.getAttoAmministrativo()),
				provvedimentoMancante,
				(preDoc.getSubDocumento()!=null && preDoc.getSubDocumento().getDocumento()!=null)?preDoc.getSubDocumento().getDocumento().getAnno():null,
				(preDoc.getSubDocumento()!=null && preDoc.getSubDocumento().getDocumento()!=null)?preDoc.getSubDocumento().getDocumento().getNumero():null,
				(preDoc.getSubDocumento()!=null && preDoc.getSubDocumento().getDocumento()!=null && preDoc.getSubDocumento().getDocumento().getTipoDocumento()!=null)?mapToUidIfNotZero(preDoc.getSubDocumento().getDocumento().getTipoDocumento()):null,
				null,
				null,
				nonAnnullati,
				ordinativo != null && ordinativo.getAnno() != null ? ordinativo.getAnno() : null,
				ordinativo != null ? toBigDecimal(ordinativo.getNumero()) : null,
				mapToUidIfNotZero(preDoc.getElencoDocumentiAllegato()),
				preDoc.getElencoDocumentiAllegato() != null ? preDoc.getElencoDocumentiAllegato().getAnno() : null,
				preDoc.getElencoDocumentiAllegato() != null ? preDoc.getElencoDocumentiAllegato().getNumero() : null,
				null
				);

		return importoTotale;
	}
	
	@Override
	public PreDocumentoSpesa findDocumentiCollegatiByIdPreDocumento(Integer uid) {
//		final String methodName = "findDocumentiCollegatiByIdPreDocumento";		
//		log.debug(methodName, "uid: "+ uid);
//		SiacTPredoc siacTPredoc = preDocumentoDao.findById(uid);
//		if(siacTPredoc == null) {
//			log.debug(methodName, "Impossibile trovare il PreDocumentoSpesa con id: " + uid);
//		}
//		return mapNotNull(siacTPredoc, PreDocumentoSpesa.class, BilMapId.SiacTPredoc_PreDocumentoSpesa_Collegati);
		return null;
	}

	public Integer findUidImpegnoPreDocumentoSpesaById(Integer uid) {
		final String methodName = "findUidImpegnoOSubImpegnoPreDocumentoSpesaById";		
		log.debug(methodName, "uid: "+ uid);
		SiacTMovgestT siacTMovgestT = siacTPredocNumRepository.findMovgestTSByIdPredoc(uid);
		if(siacTMovgestT == null) {
			log.debug(methodName, "Returning null. Impossibile trovare il movimento gestione legato al preDocumentoSpesa con id: " + uid);
			return null;
		}
//		restituisco l'uid solo se è legato ad un impegno, altrimenti null
		if(siacTMovgestT.getSiacTMovgestIdPadre() == null){
			return siacTMovgestT.getSiacTMovgest().getMovgestId();
		}else{
			return null;
		}
		
	}
	
	public Integer findUidSubImpegnoPreDocumentoSpesaById(Integer uid) {
		final String methodName = "findUidImpegnoOSubImpegnoPreDocumentoSpesaById";		
		log.debug(methodName, "uid: "+ uid);
		SiacTMovgestT siacTMovgestT = siacTPredocNumRepository.findMovgestTSByIdPredoc(uid);
		if(siacTMovgestT == null) {
			log.debug(methodName, "Returning null. Impossibile trovare il movimento gestione legato al preDocumentoSpesa con id: " + uid);
			return null;
		}
//		restituisco l'uid solo se è legato ad un subimpegno, altrimenti null
		if(siacTMovgestT.getSiacTMovgestIdPadre() != null){
			return siacTMovgestT.getUid();
		}else{
			return null;
		}
		
	}

	public void associaImpegnoByElencoAndStatiOperativi(Impegno impegno, SubImpegno subImpegno, StatoOperativoPreDocumento statoDaImpostare, Integer eldocId, StatoOperativoPreDocumento... statiOperativi) {
		// Mi creo un predocumento fittizio per avere la r comoda
		SiacTPredoc template = buildSiacTPredocTemporaneo(impegno, subImpegno, statoDaImpostare);
		
		// Aggiornamento dei predoc
		List<String> predocStatoCodes = toPredocStatoCodes(statiOperativi);
		preDocumentoDao.associaMovgestByEldocIdAndPredocStato(template, eldocId, null, null, predocStatoCodes);
	}

	private SiacTPredoc buildSiacTPredocTemporaneo(Impegno impegno, SubImpegno subImpegno, StatoOperativoPreDocumento statoDaImpostare) {
		PreDocumentoSpesa tmp = new PreDocumentoSpesa();
		tmp.setImpegno(impegno);
		tmp.setSubImpegno(subImpegno);
		tmp.setStatoOperativoPreDocumento(statoDaImpostare);
		tmp.setEnte(ente);
		SiacTPredoc stp = buildSiacTPredoc(tmp);
		return stp;
	}
	
	public List<PreDocumentoSpesa> findPreDocumentoByElencoIdAndStatiOperativi(Integer uidElenco, Integer uidImpegno, Integer uidSubImpegno,
			Collection<StatoOperativoPreDocumento> statiOperativi, PreDocumentoSpesaModelDetail... modelDetails) {
		List<String> predocStatoCodes = toPredocStatoCodes(statiOperativi);
		List<SiacTPredoc> siacTPredocs = preDocumentoDao.findByEldocIdAndPredocStato(uidElenco, uidImpegno, uidSubImpegno, predocStatoCodes);
		
		return convertiLista(siacTPredocs, PreDocumentoSpesa.class, BilMapId.SiacTPredoc_PreDocumentoSpesa, Converters.byModelDetails(modelDetails));
	}

}
