/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilser.integration.dao.SiacDDocTipoRepository;
import it.csi.siac.siacbilser.integration.dao.SiacRSubdocAttoAmmRepository;
import it.csi.siac.siacbilser.integration.dao.SiacRSubdocMovgestTRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTSubdocRepository;
import it.csi.siac.siacbilser.integration.dao.SubdocumentoDao;
import it.csi.siac.siacbilser.integration.entity.SiacDDocTipo;
import it.csi.siac.siacbilser.integration.entity.SiacRPredocSubdoc;
import it.csi.siac.siacbilser.integration.entity.SiacRSubdocAttoAmm;
import it.csi.siac.siacbilser.integration.entity.SiacRSubdocMovgestT;
import it.csi.siac.siacbilser.integration.entity.SiacRSubdocProvCassa;
import it.csi.siac.siacbilser.integration.entity.SiacTProvCassa;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocFamTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDProvCassaTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDSubdocTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.BooleanToStringManualeAutomaticoConverter;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginataImpl;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfin2ser.model.TipoFamigliaDocumento;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * Data access delegate di un Subdocumento. Per le funzionalit&agrave; indipendenti dal tipo di documento.
 *
 * @author Alessandro Marchino
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class SubdocumentoDad extends ExtendedBaseDadImpl {
	
	
	/** The siac t subdoc repository. */
	@Autowired
	private SiacTSubdocRepository siacTSubdocRepository;
	
	@Autowired
	private SiacDDocTipoRepository siacDDocTipoRepository;
	
	@Autowired
	private SiacRSubdocAttoAmmRepository siacRSubdocAttoAmmRepository;
	
	@Autowired
	private SiacRSubdocMovgestTRepository siacRSubdocMovgestTRepository;
	
	@Autowired
	private SubdocumentoDao subdocumentoDao;
	
	public <S extends Subdocumento<?, ?>> Boolean findFlagConvalidaManuale(S subdoc) {
		SiacTSubdoc siacTSubdoc = siacTSubdocRepository.findOne(subdoc.getUid());
		if(siacTSubdoc == null) {
			throw new IllegalArgumentException("Subdocumento con uid " + subdoc.getUid() + " non presente in archivio");
		}
		
		BooleanToStringManualeAutomaticoConverter btsmac = new BooleanToStringManualeAutomaticoConverter();
		return btsmac.convertFrom(siacTSubdoc.getSubdocConvalidaManuale());
	}
	
	public <S extends Subdocumento<?, ?>> void updateFlagConvalidaManuale(S subdoc) {
		SiacTSubdoc siacTSubdoc = siacTSubdocRepository.findOne(subdoc.getUid());
		if(siacTSubdoc == null) {
			throw new IllegalArgumentException("Subdocumento con uid " + subdoc.getUid() + " non presente in archivio");
		}
		
		BooleanToStringManualeAutomaticoConverter btsmac = new BooleanToStringManualeAutomaticoConverter();
		String flag = btsmac.convertTo(subdoc.getFlagConvalidaManuale());
		siacTSubdoc.setSubdocConvalidaManuale(flag);
		siacTSubdocRepository.flush();
	}
	
	public <S extends Subdocumento<?, ?>> void cancellaRelazioneSubdocumentoAttoAmm(S subdocumento, AllegatoAtto allegatoAtto, Date dataCancellazione) {
		List<SiacRSubdocAttoAmm> siacRSubdocAttoAmms = siacRSubdocAttoAmmRepository.findBySubdocIdAndAttoalId(subdocumento.getUid(), allegatoAtto.getUid());
		for(SiacRSubdocAttoAmm siacRSubdocAttoAmm : siacRSubdocAttoAmms) {
			siacRSubdocAttoAmm.setDataCancellazioneIfNotSet(dataCancellazione);
		}
		siacRSubdocAttoAmmRepository.flush();
	}
	
	public <S extends Subdocumento<?, ?>> void cancellaRelazioniSubdocumentoAttoAmm(S subdocumento) {
		Date now = new Date();
		List<SiacRSubdocAttoAmm> siacRSubdocAttoAmms = siacRSubdocAttoAmmRepository.findBySubdocId(subdocumento.getUid());
		for(SiacRSubdocAttoAmm siacRSubdocAttoAmm : siacRSubdocAttoAmms) {
			siacRSubdocAttoAmm.setDataCancellazioneIfNotSet(now);
		}
		siacRSubdocAttoAmmRepository.flush();
	}
	
	public void cancellaRelazioneSubdocumentoMovimentoGestione(Subdocumento<?, ?> subdocumento, Date dataCancellazione) {
		List<SiacRSubdocMovgestT> siacRSubdocMovgestTs = siacRSubdocMovgestTRepository.findBySubdocId(subdocumento.getUid());
		for(SiacRSubdocMovgestT siacRSubdocMovgestT : siacRSubdocMovgestTs) {
			siacRSubdocMovgestT.setDataCancellazioneIfNotSet(dataCancellazione);
		}
		siacRSubdocMovgestTRepository.flush();
	}
	
	public <S extends Subdocumento<?, ?>> TipoDocumento findTipoDocumentoBySubdocumento(S subdocumento) {
		SiacDDocTipo siacDDocTipo = siacDDocTipoRepository.findBySubdocId(subdocumento.getUid());
		if(siacDDocTipo == null) {
			throw new IllegalArgumentException("Subdocumento con uid " + subdocumento.getUid() + " ha un legame con un tipo documento in archivio");
		}
		TipoDocumento tipoDocumento = mapNotNull(siacDDocTipo, TipoDocumento.class, BilMapId.SiacDDocTipo_TipoDocumento);
		return tipoDocumento;
	}
	
	public <S extends Subdocumento<?, ?>> Long countSubdocumentiConStessoPadre(S subdocumento) {
		Long count = siacTSubdocRepository.countSiacTSubdocsOfSiacTDocBySubdocId(subdocumento.getUid());
		return count;
	}

	public BigDecimal getImportoSubdocumento(Subdocumento<?, ?> subdocumento) {
		return siacTSubdocRepository.findImportoBySubdocId(subdocumento.getUid());
	}
	
	public BigDecimal getImportoDaDedurreSubdocumento(Subdocumento<?, ?> subdocumento) {
		return siacTSubdocRepository.findImportoDaDedurreBySubdocId(subdocumento.getUid());
	}
	
	/**
	 * Calcola la somma degli importi delle quote passate come parametro
	 * 
	 * @param subdocumenti le quote di cui calcolare l'importo
	 * @return l'importo totale delle quote
	 */
	public <S extends Subdocumento<?, ?>> BigDecimal getImportoSubdocumenti(List<S> subdocumenti) {
		Collection<Integer> subdocIds = new HashSet<Integer>();
		for(S subdoc : subdocumenti) {
			subdocIds.add(subdoc.getUid());
		}
		
		return siacTSubdocRepository.sumSubdocImportoBySubdocIds(subdocIds);
	}
	
	/**
	 * Calcola la somma degli importi da dedurre delle quote passate come parametro
	 * 
	 * @param subdocumenti le quote di cui calcolare l'importo dad dedurre
	 * @return l'importo totale delle quote
	 */
	public <S extends Subdocumento<?, ?>> BigDecimal getImportoDaDedurreSubdocumenti(List<S> subdocumenti) {
		Collection<Integer> subdocIds = new HashSet<Integer>();
		for(S subdoc : subdocumenti) {
			subdocIds.add(subdoc.getUid());
		}
		
		return siacTSubdocRepository.sumSubdocImportoDaDedurreBySubdocIds(subdocIds);
	}
	
	/**
	 * Calcola la somma degli importi splitReverse delle quote passate come parametro
	 * 
	 * @param subdocumenti le quote di cui calcolare l'importo splitReverse
	 * @return l'importo splitReverse totale delle quote
	 */
	public <S extends Subdocumento<?, ?>> BigDecimal getImportoSplitReverseSubdocumenti(List<S> subdocumenti) {
		Collection<Integer> subdocIds = new HashSet<Integer>();
		for(S subdoc : subdocumenti) {
			subdocIds.add(subdoc.getUid());
		}
		
		return siacTSubdocRepository.sumSubdocImportoSpliReverseBySubdocIds(subdocIds);
	}
	
	/**Verifica se una quota e' collegata ad un ordinativo non annullato
	 * 
	 * @param subdoc
	 * @return
	 */
	public boolean isSubdocCollegatoAdOrdinativoValido(Subdocumento<?, ?> subdoc) {
		SiacTSubdoc siacTSubdoc = siacTSubdocRepository.findSiacTSubdocConOrdinativoValidoAssociato(subdoc.getUid());
		return siacTSubdoc != null;
	}
	
	/**Restituisce il numero di quote collegate ad un allegato atto con flag canvalidaManuale valorizzato
	 * 
	 * @param allegatoAtto
	 * @return
	 */
	public Long countQuoteConvalidateByAllegatoAtto(AllegatoAtto allegatoAtto) {
		Long count = siacTSubdocRepository.countSiacTSubdocsConvalidateByAllegatoAttoUid(allegatoAtto.getUid());
		return count;
	}
	
	public <D extends Documento<?, ?>> void cancellaQuoteByUidDocumento(D doc, final Date dataCancellazione) {
		List<SiacTSubdoc> siacTSubdocs = siacTSubdocRepository.findSiacTSubdocByDocId(doc.getUid());
		for(SiacTSubdoc siacTSubdoc : siacTSubdocs) {
			siacTSubdoc.setDataCancellazioneIfNotSet(dataCancellazione);
		}
		siacTSubdocRepository.flush();
	}
	
	public void eliminaSubdocumento(int uid) {
		SiacTSubdoc siacTSubdoc = siacTSubdocRepository.findOne(uid);
		siacTSubdoc.setDataCancellazioneIfNotSet(new Date());
		siacTSubdocRepository.flush();
	}
	
	public void eliminaLegameSubdocPredoc(int uidSubdoc) {
		Date now = new Date();
		SiacTSubdoc siacTSubdoc = siacTSubdocRepository.findOne(uidSubdoc);
		if(siacTSubdoc.getSiacRPredocSubdocs() != null){
			for(SiacRPredocSubdoc siacRPredocSubdoc : siacTSubdoc.getSiacRPredocSubdocs()){
				siacRPredocSubdoc.setDataCancellazioneIfNotSet(now);
			}
		}
	}

	public ListaPaginata<Subdocumento<?,?>> ricercaSubdocumentiDaAssociare(
			Ente ente, 
			Bilancio bilancio,
			TipoFamigliaDocumento tipoFamigliaDocumento,
			Integer uidElenco,
			Integer annoElenco,
			Integer numeroElenco,
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
			List<StatoOperativoDocumento> statiOperativoDocumento,
			Boolean collegatoAMovimentoDelloStessoBilancio, 
			Boolean associatoAProvvedimentoOAdElenco, 
			Boolean importoDaPagareZero, 
			Boolean rilevatiIvaConRegistrazioneONonRilevantiIva,
			ParametriPaginazione parametriPaginazione) {
		
		
	
		Set<SiacDDocFamTipoEnum> enumSet = null;
		SiacDProvCassaTipoEnum siacDProvCassaTipoEnum = null;
		if(tipoFamigliaDocumento != null && tipoFamigliaDocumento.isSpesa()){
			enumSet = EnumSet.of(SiacDDocFamTipoEnum.Spesa, SiacDDocFamTipoEnum.IvaSpesa);
			siacDProvCassaTipoEnum = SiacDProvCassaTipoEnum.Spesa;
		}else if(tipoFamigliaDocumento != null && tipoFamigliaDocumento.isEntrata()){
			enumSet = EnumSet.of(SiacDDocFamTipoEnum.Entrata, SiacDDocFamTipoEnum.IvaEntrata);
			siacDProvCassaTipoEnum = SiacDProvCassaTipoEnum.Entrata;
		}
		
		Page<SiacTSubdoc> lista = subdocumentoDao.ricercaSinteticaSubdocumentiDaAssociare(ente.getUid(), bilancio!=null?bilancio.getUid():null,
				 enumSet,
				 uidElenco,
				 annoElenco,
				 numeroElenco,
				 annoProvvisorio,
				 numeroProvvisorio != null ? new BigDecimal(numeroProvvisorio.toString()) : null,
				 dataProvvisorio,
				 siacDProvCassaTipoEnum,
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
				 SiacDDocStatoEnum.byStatiOperativi(statiOperativoDocumento),
				 collegatoAMovimentoDelloStessoBilancio, 
				 associatoAProvvedimentoOAdElenco, 
				 importoDaPagareZero, 
				 rilevatiIvaConRegistrazioneONonRilevantiIva,
				 toPageable(parametriPaginazione)		 
				);
		
		return toListaPaginataEntrataSpesa(lista);
	}
	
	
	public BigDecimal ricercaSubdocumentiDaAssociareTotaleImporti(Ente ente, 
			Bilancio bilancio,
			TipoFamigliaDocumento tipoFamigliaDocumento,
			Integer uidElenco,
			Integer annoElenco,
			Integer numeroElenco,
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
			List<StatoOperativoDocumento> statiOperativoDocumento,
			Boolean collegatoAMovimentoDelloStessoBilancio, 
			Boolean associatoAProvvedimentoOAdElenco, 
			Boolean importoDaPagareZero, 
			Boolean rilevatiIvaConRegistrazioneONonRilevantiIva,
			ParametriPaginazione parametriPaginazione
			) {
		
		
		Set<SiacDDocFamTipoEnum> enumSet = null;
		SiacDProvCassaTipoEnum siacDProvCassaTipoEnum = null;
		if(tipoFamigliaDocumento != null && tipoFamigliaDocumento.isSpesa()){
			enumSet = EnumSet.of(SiacDDocFamTipoEnum.Spesa, SiacDDocFamTipoEnum.IvaSpesa);
			siacDProvCassaTipoEnum = SiacDProvCassaTipoEnum.Spesa;
		}else if(tipoFamigliaDocumento != null && tipoFamigliaDocumento.isEntrata()){
			enumSet = EnumSet.of(SiacDDocFamTipoEnum.Entrata, SiacDDocFamTipoEnum.IvaEntrata);
			siacDProvCassaTipoEnum = SiacDProvCassaTipoEnum.Entrata;
		}
		
		BigDecimal totale = subdocumentoDao.ricercaSinteticaSubdocumentiDaAssociareTotaleImporti(ente.getUid(), bilancio!=null?bilancio.getUid():null,
				 enumSet,
				 uidElenco,
				 annoElenco,
				 numeroElenco,
				 annoProvvisorio,
				 numeroProvvisorio != null ? new BigDecimal(numeroProvvisorio.toString()) : null,
				 dataProvvisorio,
				 siacDProvCassaTipoEnum,
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
				 SiacDDocStatoEnum.byStatiOperativi(statiOperativoDocumento),
				 collegatoAMovimentoDelloStessoBilancio, 
				 associatoAProvvedimentoOAdElenco, 
				 importoDaPagareZero, 
				 rilevatiIvaConRegistrazioneONonRilevantiIva,
				 toPageable(parametriPaginazione)		 
				);
		
		return totale;
		
	}
	
	//SIAC-6780	
	public ListaPaginata<Subdocumento<?, ?>> ricercaSubdocumentiDaAssociarePerCollegaDocumento(
			Ente ente, 
			TipoFamigliaDocumento tipoFamigliaDocumento,
			TipoDocumento tipoDocumento,
			Integer annoDocumento,
			String numeroDocumento,
			Soggetto soggetto,
			List<StatoOperativoDocumento> statiOperativoDocumento,
			BigDecimal predocImporto,
			BigDecimal numeroProvvisorio,
			ParametriPaginazione parametriPaginazione
			) {
		
		Set<SiacDDocFamTipoEnum> enumSet = null;
		SiacDProvCassaTipoEnum siacDProvCassaTipoEnum = null;
		if(tipoFamigliaDocumento != null && tipoFamigliaDocumento.isEntrata()){
			enumSet = EnumSet.of(SiacDDocFamTipoEnum.Entrata, SiacDDocFamTipoEnum.IvaEntrata);
			siacDProvCassaTipoEnum = SiacDProvCassaTipoEnum.Entrata;
		}
		
		Page<SiacTSubdoc> lista = subdocumentoDao.ricercaSinteticaSubdocumentiDaAssociarePerCollegaDocumento(
				ente.getUid(), 
				enumSet,
				siacDProvCassaTipoEnum,
				tipoDocumento!=null?tipoDocumento.getUid():null,
				annoDocumento,
				numeroDocumento,
				predocImporto,
				numeroProvvisorio,
				soggetto!=null?soggetto.getCodiceSoggetto():null,
				SiacDDocStatoEnum.byStatiOperativi(statiOperativoDocumento),
				toPageable(parametriPaginazione)	
				);
		
		return toListaPaginataEntrataSpesa(lista);
	}
	
	
	public BigDecimal ricercaSubdocumentiDaAssociareTotaleImportiPerCollegaDocumento(
			Ente ente, 
			TipoFamigliaDocumento tipoFamigliaDocumento,
			TipoDocumento tipoDocumento,
			Integer annoDocumento,
			String numeroDocumento,
			Soggetto soggetto,
			List<StatoOperativoDocumento> statiOperativoDocumento,
			BigDecimal predocImporto,
			BigDecimal numeroProvvisorio,
			ParametriPaginazione parametriPaginazione
			) {
		
		
		Set<SiacDDocFamTipoEnum> enumSet = null;
		SiacDProvCassaTipoEnum siacDProvCassaTipoEnum = null;
		if(tipoFamigliaDocumento != null && tipoFamigliaDocumento.isEntrata()){
			enumSet = EnumSet.of(SiacDDocFamTipoEnum.Entrata, SiacDDocFamTipoEnum.IvaEntrata);
			siacDProvCassaTipoEnum = SiacDProvCassaTipoEnum.Entrata;
		}
		
		BigDecimal totale = subdocumentoDao.ricercaSinteticaSubdocumentiDaAssociareTotaleImportiPerCollegaDocumento(
				ente.getUid(), 
				enumSet,
				siacDProvCassaTipoEnum,
				tipoDocumento!=null?tipoDocumento.getUid():null,
				annoDocumento,
				numeroDocumento,
				predocImporto,
				numeroProvvisorio,
 				soggetto!=null?soggetto.getCodiceSoggetto():null,
				SiacDDocStatoEnum.byStatiOperativi(statiOperativoDocumento),
				toPageable(parametriPaginazione)		 
				);
		
		return totale;
	}
	//
	
	private ListaPaginata<Subdocumento<?,?>> toListaPaginataEntrataSpesa(Page<SiacTSubdoc> pagedList){
		
		ListaPaginataImpl<Subdocumento<?,?>> list = new ListaPaginataImpl<Subdocumento<?,?>>();

		if (!pagedList.hasContent()) {
			return list;
		}

		int elementsPerPage = 1 + (int) (pagedList.getTotalElements() / pagedList.getTotalPages());

		list.setPaginaCorrente(pagedList.getNumber());
		list.setTotaleElementi((int) pagedList.getTotalElements());
		list.setTotalePagine(pagedList.getTotalPages());
		list.setHasPaginaPrecedente(pagedList.hasPreviousPage());
		list.setHasPaginaSuccessiva(pagedList.hasNextPage());
		list.setNumeroElementoInizio(1 + pagedList.getNumber() * elementsPerPage);
		list.setNumeroElementoFine(pagedList.getNumber() * elementsPerPage
				+ pagedList.getNumberOfElements());

		for (SiacTSubdoc siacTSubdoc : pagedList.getContent()){
			if(SiacDSubdocTipoEnum.Spesa.getCodice().equals(siacTSubdoc.getSiacDSubdocTipo().getSubdocTipoCode())){
				list.add(map(siacTSubdoc, SubdocumentoSpesa.class, BilMapId.SiacTSubdoc_SubdocumentoSpesa_Allegato));
			}else{
				list.add(map(siacTSubdoc, SubdocumentoEntrata.class, BilMapId.SiacTSubdoc_SubdocumentoEntrata_Allegato));
			}
		}
		
		return list;
	}

	/**
	 * Imposta a TRUE il flag pagatoInCEC e la data di pagamento CEC con la data attuale
	 * 
	 * @param subdocumenti quote che sono state pagate in cec
	 */
	public void impostaPagamentoCEC(List<SubdocumentoSpesa> subdocumenti, Date dataPagamento) {
//		final Date now = new Date();
		Collection<Integer> it = new HashSet<Integer>();
		for(SubdocumentoSpesa ss : subdocumenti) {
			ss.setDataPagamentoCEC(dataPagamento);
			ss.setPagatoInCEC(Boolean.TRUE);
			
			it.add(ss.getUid());
		}
		Iterable<SiacTSubdoc> siacTSubdocs = siacTSubdocRepository.findAll(it);
		if(siacTSubdocs == null) {
			return;
		}
		
		for(SiacTSubdoc sts : siacTSubdocs) {
			sts.setSubdocPagatoCec(Boolean.TRUE);
			sts.setSubdocDataPagamentoCecIfNotSet(dataPagamento);
		}
	}

	/**
	 * Cerca l'importo splitReverse della quota passata come parametro
	 * 
	 * @param subdocumento la quota di cui determinare l'importo splitReverse
	 * 
	 * @return l'importo splitReverse della quota
	 */
	public <S extends Subdocumento<?, ?>> BigDecimal getImportoSplitReverseSubdocumento(S subdocumento) {
		return siacTSubdocRepository.findImportoSplitReverseBySubdocId(subdocumento.getUid());
	}
	
	public void aggiornaProvvisorioDiCassa(int uidSubdoc, ProvvisorioDiCassa provvisorioCassa) {
		
		SiacTSubdoc siacTSubdoc = siacTSubdocRepository.findOne(uidSubdoc);
		
		if(siacTSubdoc==null) {
			throw new IllegalArgumentException("Impossibile trovare subdocumento con uid: "+uidSubdoc);
		}
		
		Date now = new Date();
		if(siacTSubdoc.getSiacRSubdocProvCassas()!=null) {
			for(SiacRSubdocProvCassa r : siacTSubdoc.getSiacRSubdocProvCassas()){
				r.setDataCancellazioneIfNotSet(now);
			}			
		}
		
		if(provvisorioCassa != null) {
	
			SiacTProvCassa siacTProvCassa = new SiacTProvCassa();
			siacTProvCassa.setUid(provvisorioCassa.getUid());
			SiacRSubdocProvCassa siacRSubdocProvCassa = new SiacRSubdocProvCassa();
			siacRSubdocProvCassa.setSiacTProvCassa(siacTProvCassa);
			siacRSubdocProvCassa.setSiacTSubdoc(siacTSubdoc);
			siacRSubdocProvCassa.setSiacTEnteProprietario(siacTSubdoc.getSiacTEnteProprietario());
			siacRSubdocProvCassa.setLoginOperazione(loginOperazione);
			
			siacRSubdocProvCassa.setDataModificaInserimento(now);
			
			siacTSubdoc.addSiacRSubdocProvCassa(siacRSubdocProvCassa);
		}
		
	}

	//Ora questo legame viene popolato dal servizio InserisceOrdinativoPagamentoService
//	public void aggiornaOrdinativo(Subdocumento<?,?> src, SubOrdinativo subOrdinativo) {
//		final String methodName = "aggiornaOrdinativo";
//		
//		if(subOrdinativo == null || subOrdinativo.getUid() == 0){
//			log.info(methodName, "SubOrdinativo non valorizzato. (null o uid=0)");
//			return;
//		}
//		
//		SiacTSubdoc dest = siacTSubdocRepository.findOne(src.getUid());
//		if(dest == null) {
//			String msg = "Il subdocumento con uid:"+ src.getUid() + " non esiste. Impossibile aggiorare l'ordinativo.";
//			log.error(methodName, msg);
//			throw new IllegalArgumentException(msg);
//		}
//		
//		SiacRSubdocOrdinativoT siacRSubdocOrdinativoT = new SiacRSubdocOrdinativoT();
//		
//		SiacTOrdinativoT siacTOrdinativoT = new SiacTOrdinativoT();
//		siacTOrdinativoT.setUid(subOrdinativo.getUid());
//		siacRSubdocOrdinativoT.setSiacTOrdinativoT(siacTOrdinativoT);
//		
//		siacRSubdocOrdinativoT.setSiacTSubdoc(dest);
//		siacRSubdocOrdinativoT.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
//		siacRSubdocOrdinativoT.setLoginOperazione(dest.getLoginOperazione());
//		siacRSubdocOrdinativoT.setDataModificaInserimento(new Date());
//		
//		dest.setSiacRSubdocOrdinativoTs(new ArrayList<SiacRSubdocOrdinativoT>());
//		dest.addSiacRSubdocOrdinativoT(siacRSubdocOrdinativoT);
//		
//		
////		SubdocumentoSpesaOrdinativoConverter c = new SubdocumentoSpesaOrdinativoConverter();
////		c.convertTo(src, dest);
//		siacTSubdocRepository.flush();
//		
//	}

	
	public List<Subdocumento<?, ?>> cercaSubdocNonResiduioResiduiConModificheRORM(Integer subDocUid, Integer annoEsercizio){
		List<SiacTSubdoc> listSiacTSubdoc = null;
		List<Subdocumento<?, ?>> list = new ListaPaginataImpl<Subdocumento<?, ?>>();
		
		//cerco i subdoc senza un residuo se ho un risultato posso andare ad emissione
		listSiacTSubdoc = subdocumentoDao.cercaSubdocSenzaResiduo(subDocUid, annoEsercizio);
		
		//se non ho risultati quindi e' possibile che sia un residuo
		if(CollectionUtils.isEmpty(listSiacTSubdoc)) {
			//se ho dei risultati vuol dire che il residuo possiede una modifica RORM quindi posso emettere
			listSiacTSubdoc = subdocumentoDao.cercaSubdocConModificheRORM(subDocUid, annoEsercizio);
		}
		
		for (SiacTSubdoc siacTSubdoc : listSiacTSubdoc){
			if(SiacDSubdocTipoEnum.Spesa.getCodice().equals(siacTSubdoc.getSiacDSubdocTipo().getSubdocTipoCode())){
				list.add(map(siacTSubdoc, SubdocumentoSpesa.class, BilMapId.SiacTSubdoc_SubdocumentoSpesa_Allegato));
			}else{
				list.add(map(siacTSubdoc, SubdocumentoEntrata.class, BilMapId.SiacTSubdoc_SubdocumentoEntrata_Allegato));
			}
		}
		
		return list;
	}

	public List<Subdocumento<?, ?>> cercaSubdocsConModificheRORM(List<Integer> subDocUids, Integer annoEsercizio){
		List<Subdocumento<?, ?>> list = new ListaPaginataImpl<Subdocumento<?, ?>>();
		
		List<SiacTSubdoc> listSiacTSubdoc = subdocumentoDao.cercaSubdocsConModificheRORM(subDocUids, annoEsercizio);
		
		for (SiacTSubdoc siacTSubdoc : listSiacTSubdoc){
			if(SiacDSubdocTipoEnum.Spesa.getCodice().equals(siacTSubdoc.getSiacDSubdocTipo().getSubdocTipoCode())){
				list.add(map(siacTSubdoc, SubdocumentoSpesa.class, BilMapId.SiacTSubdoc_SubdocumentoSpesa_Allegato));
			}else{
				list.add(map(siacTSubdoc, SubdocumentoEntrata.class, BilMapId.SiacTSubdoc_SubdocumentoEntrata_Allegato));
			}
		}
		
		return list;
	}
}
