/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.csi.siac.siacbilser.integration.entity.SiacRSubdocAttoAmm;
import it.csi.siac.siacbilser.integration.entity.SiacRSubdocMovgestT;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdocSospensione;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDAttoAmmStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocFamTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDProvCassaTipoEnum;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;

/**
 * The Interface SubdocumentoDao.
 */
public interface SubdocumentoDao extends Dao<SiacTSubdoc, Integer> {
	
	/**
	 * Crea la SiacTSubdoc.
	 *
	 * @param c la SiacTSubdoc da inserire
	 * @return SiacTSubdoc inserita
	 */
	SiacTSubdoc create(SiacTSubdoc c);

	
	/**
	 * Aggiorna la SiacTSubdoc.
	 *
	 * @param c la SiacTSubdoc da aggiornare
	 * @return SiacTSubdoc aggiornata
	 */
	SiacTSubdoc update(SiacTSubdoc c);
	
	/**
	 * Trova i SiacRSubdocAttoAmm di un dato siacTAttoAllegato.
	 * 
	 * @param attoalId
	 * @param siacDDocTipoAllegato
	 * @return
	 */
	List<SiacRSubdocAttoAmm> findSiacRSubdocAttoAmmByAttoAllegatoAndTipoDocumentoAllegato(Integer attoalId, Boolean siacDDocTipoAllegato);
	
	List<SiacRSubdocMovgestT> findSiacRSubdocMovgestTByAttoAllegatoAndTipoDocumentoAllegato(Integer attoalId, Boolean tipoDocumentoAllegatoAtto);

	
	
	/**
	 * Ricerca sintetica subdocumenti.
	 *
	 * @param enteProprietarioId the ente proprietario id
	 * @param tipoFam the tipo fam
	 * @param eldocId the eldoc id
	 * @param eldocAnno the eldoc anno
	 * @param eldocNumero the eldoc numero
	 * @param eldocNumeroDa the eldoc numero da
	 * @param eldocNumeroA the eldoc numero a
	 * @param provcAnno the provc anno
	 * @param provcNumero the provc numero
	 * @param provcDataEmissione the provc data emissione
	 * @param tipoProvvisorio the tipo provvisorio
	 * @param docTipoId the doc tipo id
	 * @param docAnno the doc anno
	 * @param docNumero the doc numero
	 * @param docDataEmissione the doc data emissione
	 * @param subdocNumero the subdoc numero
	 * @param movgestNumero the movgest numero
	 * @param movgestAnno the movgest anno
	 * @param soggettoId the soggetto id
	 * @param attoammId the attoamm id
	 * @param attoammAnno the attoamm anno
	 * @param attoammNumero the attoamm numero
	 * @param attoammTipoId the attoamm tipo id
	 * @param uidStruttAmm the uid strutt amm
	 * @param statiDocumento the stati documento
	 * @param collegatoAMovimentoDelloStessoBilancio the collegato a movimento dello stesso bilancio
	 * @param associatoAProvvedimentoOAdElenco the associato a provvedimento o ad elenco
	 * @param importoDaPagareZero the importo da pagare zero
	 * @param importoDaPagareOIncassareMaggioreDiZero the importo da pagare o incassare maggiore di zero
	 * @param rilevatiIvaConRegistrazioneONonRilevantiIva the rilevati iva con registrazione o non rilevanti iva
	 * @param collegatoALiquidazioneInStatoValidoConProvvDefinitivoConDispPagareMaggioreDiSubdocImporto the collegato a liquidazione in stato valido con provv definitivo con disp pagare maggiore di subdoc importo
	 * @param pageable the pageable
	 * @return the page
	 */
	Page<SiacTSubdoc> ricercaSinteticaSubdocumenti(
			int enteProprietarioId,
			Integer bilId,
			Collection<SiacDDocFamTipoEnum> tipoFam,
			Integer eldocId,
			Integer eldocAnno,
			Integer eldocNumero,
			Integer eldocNumeroDa,
			Integer eldocNumeroA,
			Integer provcAnno,
			BigDecimal provcNumero,
			Date provcDataEmissione,
			SiacDProvCassaTipoEnum tipoProvvisorio,
			Integer docTipoId,
			Integer docAnno,
			String docNumero,
			Date docDataEmissione,
			Integer subdocNumero,
			BigDecimal movgestNumero,
			Integer movgestAnno,
			String  soggettoCode,
			Integer attoammId,
			String attoammAnno,
			Integer attoammNumero,
			Integer attoammTipoId,
			Integer uidStruttAmm,
			Integer annoCapitolo, //???
			Integer elemCode,
			Integer elemCode2,
			Integer elemCode3,
			Set<SiacDDocStatoEnum> statiDocumento,
			Boolean collegatoAMovimentoDelloStessoBilancio, 
			Boolean associatoAProvvedimentoOAdElenco, 
			Boolean importoDaPagareZero, 
			Boolean importoDaPagareOIncassareMaggioreDiZero,
			Boolean rilevatiIvaConRegistrazioneONonRilevantiIva, 
			Boolean collegatoALiquidazioneInStatoValidoConProvvDefinitivoConDispPagareMaggioreDiSubdocImporto,
			Collection<SiacDAttoAmmStatoEnum> siacDAttoAmmStatoEnums,
			Boolean associatoAdOrdinativo,
			Boolean conFlagConvalidaManuale,
			List<Integer> listProvvisorioDiCassaUid,
			Pageable pageable);
	
	
	/**
	 * Ricerca sintetica subdocumenti totale importi.
	 *
	 * @param enteProprietarioId the ente proprietario id
	 * @param tipoFam the tipo fam
	 * @param eldocId the eldoc id
	 * @param eldocAnno the eldoc anno
	 * @param eldocNumero the eldoc numero
	 * @param eldocNumeroDa the eldoc numero da
	 * @param eldocNumeroA the eldoc numero a
	 * @param provcAnno the provc anno
	 * @param provcNumero the provc numero
	 * @param provcDataEmissione the provc data emissione
	 * @param tipoProvv the tipo provv
	 * @param docTipoId the doc tipo id
	 * @param docAnno the doc anno
	 * @param docNumero the doc numero
	 * @param docDataEmissione the doc data emissione
	 * @param subdocNumero the subdoc numero
	 * @param movgestNumero the movgest numero
	 * @param movgestAnno the movgest anno
	 * @param soggettoId the soggetto id
	 * @param attoammId the attoamm id
	 * @param attoammAnno the attoamm anno
	 * @param attoammNumero the attoamm numero
	 * @param attoammTipoId the attoamm tipo id
	 * @param uidStruttAmm the uid strutt amm
	 * @param statiDocumento the stati documento
	 * @param collegatoAMovimentoDelloStessoBilancio the collegato a movimento dello stesso bilancio
	 * @param associatoAProvvedimentoOAdElenco the associato a provvedimento o ad elenco
	 * @param importoDaPagareOIncassareZero the importo da pagare o incassare zero
	 * @param importoDaPagareOIncassareMaggioreDiZero the importo da pagare o incassare maggiore di zero
	 * @param rilevatiIvaConRegistrazioneONonRilevantiIva the rilevati iva con registrazione o non rilevanti iva
	 * @param collegatoALiquidazioneInStatoValidoConProvvDefinitivoConDispPagareMaggioreDiSubdocImporto the collegato a liquidazione in stato valido con provv definitivo con disp pagare maggiore di subdoc importo
	 * @return the big decimal
	 */
	BigDecimal ricercaSinteticaSubdocumentiTotaleImporti(
			int enteProprietarioId,
			Integer bilId,
			Collection<SiacDDocFamTipoEnum> tipoFam,
			Integer eldocId,
			Integer eldocAnno,
			Integer eldocNumero,
			Integer eldocNumeroDa,
			Integer eldocNumeroA,
			Integer provcAnno,
			BigDecimal provcNumero,
			Date provcDataEmissione,
			SiacDProvCassaTipoEnum tipoProvv,
			Integer docTipoId,
			Integer docAnno,
			String docNumero,
			Date docDataEmissione,
			Integer subdocNumero,
			BigDecimal movgestNumero,
			Integer movgestAnno,
			String  soggettoCode,
			Integer attoammId,
			String attoammAnno,
			Integer attoammNumero,
			Integer attoammTipoId,
			Integer uidStruttAmm,
			Integer annoCapitolo, //???
			Integer elemCode,
			Integer elemCode2,
			Integer elemCode3,
			Set<SiacDDocStatoEnum> statiDocumento,
			Boolean collegatoAMovimentoDelloStessoBilancio, 
			Boolean associatoAProvvedimentoOAdElenco, 
			Boolean importoDaPagareOIncassareZero,
			Boolean importoDaPagareOIncassareMaggioreDiZero,
			Boolean rilevatiIvaConRegistrazioneONonRilevantiIva, 
			Boolean collegatoALiquidazioneInStatoValidoConProvvDefinitivoConDispPagareMaggioreDiSubdocImporto,
			Collection<SiacDAttoAmmStatoEnum> siacDAttoAmmStatoEnums,
			Boolean associatoAdOrdinativo,
			Boolean conFlagConvalidaManuale,
			List<Integer> listProvvisorioDiCassaUid
			);
	
	/**
	 * Ricerca sintetica subdocumenti.
	 *
	 * @param enteProprietarioId the ente proprietario id
	 * @param tipoFam the tipo fam
	 * @param eldocId the eldoc id
	 * @param eldocAnno the eldoc anno
	 * @param eldocNumero the eldoc numero
	 * @param eldocNumeroDa the eldoc numero da
	 * @param eldocNumeroA the eldoc numero a
	 * @param provcAnno the provc anno
	 * @param provcNumero the provc numero
	 * @param provcDataEmissione the provc data emissione
	 * @param tipoProvvisorio the tipo provvisorio
	 * @param docTipoId the doc tipo id
	 * @param docAnno the doc anno
	 * @param docNumero the doc numero
	 * @param docDataEmissione the doc data emissione
	 * @param subdocNumero the subdoc numero
	 * @param movgestNumero the movgest numero
	 * @param movgestAnno the movgest anno
	 * @param soggettoId the soggetto id
	 * @param attoammId the attoamm id
	 * @param attoammAnno the attoamm anno
	 * @param attoammNumero the attoamm numero
	 * @param attoammTipoId the attoamm tipo id
	 * @param uidStruttAmm the uid strutt amm
	 * @param statiDocumento the stati documento
	 * @param collegatoAMovimentoDelloStessoBilancio the collegato a movimento dello stesso bilancio
	 * @param associatoAProvvedimentoOAdElenco the associato a provvedimento o ad elenco
	 * @param importoDaPagareZero the importo da pagare zero
	 * @param importoDaPagareOIncassareMaggioreDiZero the importo da pagare o incassare maggiore di zero
	 * @param rilevatiIvaConRegistrazioneONonRilevantiIva the rilevati iva con registrazione o non rilevanti iva
	 * @param collegatoALiquidazioneInStatoValidoConProvvDefinitivoConDispPagareMaggioreDiSubdocImporto the collegato a liquidazione in stato valido con provv definitivo con disp pagare maggiore di subdoc importo
	 * @param pageable the pageable
	 * @return the page
	 */
	Long countSinteticaSubdocumenti(
			int enteProprietarioId,
			Integer bilId,
			Collection<SiacDDocFamTipoEnum> tipoFam,
			Collection<Integer> eldocIds,
			Integer eldocAnno,
			Integer eldocNumero,
			Integer eldocNumeroDa,
			Integer eldocNumeroA,
			Integer provcAnno,
			BigDecimal provcNumero,
			Date provcDataEmissione,
			SiacDProvCassaTipoEnum tipoProvvisorio,
			Integer docTipoId,
			Integer docAnno,
			String docNumero,
			Date docDataEmissione,
			Integer subdocNumero,
			BigDecimal movgestNumero,
			Integer movgestAnno,
			String  soggettoCode,
			Integer attoammId,
			String attoammAnno,
			Integer attoammNumero,
			Integer attoammTipoId,
			Integer uidStruttAmm,
			Integer annoCapitolo, //???
			Integer elemCode,
			Integer elemCode2,
			Integer elemCode3,
			Set<SiacDDocStatoEnum> statiDocumento,
			Boolean collegatoAMovimentoDelloStessoBilancio, 
			Boolean associatoAProvvedimentoOAdElenco, 
			Boolean importoDaPagareZero, 
			Boolean importoDaPagareOIncassareMaggioreDiZero,
			Boolean rilevatiIvaConRegistrazioneONonRilevantiIva, 
			Boolean collegatoALiquidazioneInStatoValidoConProvvDefinitivoConDispPagareMaggioreDiSubdocImporto,
			Collection<SiacDAttoAmmStatoEnum> siacDAttoAmmStatoEnums,
			Boolean associatoAdOrdinativo,
			Boolean conFlagConvalidaManuale,
			List<Integer> listProvvisorioDiCassaUid
			);


	/**
	 * Flushes the data.
	 */
	void flush();



	Page<SiacTSubdoc> ricercaSinteticaSubdocumentiDaAssociare(
			int enteProprietarioId,
			Integer bilId,
			Collection<SiacDDocFamTipoEnum> tipoFam,
			Integer eldocId,
			Integer eldocAnno,
			Integer eldocNumero,
			Integer provcAnno,
			BigDecimal provcNumero,
			Date provcDataEmissione,
			SiacDProvCassaTipoEnum tipoProvv,
			Integer docTipoId,
			Integer docAnno,
			String docNumero,
			Date docDataEmissione,
			Integer subdocNumero,
			BigDecimal movgestNumero,
			Integer movgestAnno,
			String  soggettoCode,
			Integer attoammId,
			String attoammAnno,
			Integer attoammNumero,
			Integer attoammTipoId,
			Integer uidStruttAmm,
			Set<SiacDDocStatoEnum> statiDocumento,
			Boolean collegatoAMovimentoDelloStessoBilancio, 
			Boolean associatoAProvvedimentoOAdElenco, 
			Boolean importoDaPagareOIncassareZero, 
			Boolean rilevatiIvaConRegistrazioneONonRilevantiIva, 
			Pageable pageable);


	BigDecimal ricercaSinteticaSubdocumentiDaAssociareTotaleImporti(
			int enteProprietarioId,
			Integer bilId,
			Collection<SiacDDocFamTipoEnum> tipoFam,
			Integer eldocId,
			Integer eldocAnno,
			Integer eldocNumero,
			Integer provcAnno,
			BigDecimal provcNumero,
			Date provcDataEmissione,
			SiacDProvCassaTipoEnum tipoProvv,
			Integer docTipoId,
			Integer docAnno,
			String docNumero,
			Date docDataEmissione,
			Integer subdocNumero,
			BigDecimal movgestNumero,
			Integer movgestAnno,
			String  soggettoCode,
			Integer attoammId,
			String attoammAnno,
			Integer attoammNumero,
			Integer attoammTipoId,
			Integer uidStruttAmm,
			Set<SiacDDocStatoEnum> statiDocumento,
			Boolean collegatoAMovimentoDelloStessoBilancio, 
			Boolean associatoAProvvedimentoOAdElenco, 
			Boolean importoDaPagareOIncassareZero, 
			Boolean rilevatiIvaConRegistrazioneONonRilevantiIva, 
			Pageable pageable);


	/**
	 * Calcola una chiave per il subdocumento dato da i seguenti campi:
	 * 
	 * Importo quota
	 * Importo da dedurre quota
	 * impegno
	 * subimpegno
     * FlagRilevanteIVA
	 *
	 * @param uid
	 * @return la chiave.
	 */
	String computeKeySubdocImportoMovimentoGestioneFlagRilevanteIva(int uid);


	Page<SiacTSubdoc> ricercaSinteticaSubdocumentiPerProvvisorio(
			int enteProprietarioId, 
			Set<SiacDDocFamTipoEnum> tipoFam,
			Integer docTipoId,
			Integer docAnno, 
			String docNumero,
			Date docDataEmissione, 
			Integer subdocNumero,
			BigDecimal movgestNumero, 
			Integer movgestAnno, 
			Integer soggettoId,
			String soggettoCode,
			Integer eldocAnno,
			Integer eldocNumero,
			Boolean flgEscludiSubDocCollegati,
			Pageable pageable);
	
//	BigDecimal ricercaSinteticaSubdocumentiPerProvvisorioTotaleImporti(
//			int enteProprietarioId, 
//			EnumSet<SiacDDocFamTipoEnum> tipoFam,
//			Integer docTipoId,
//			Integer docAnno, 
//			String docNumero,
//			Date docDataEmissione, 
//			Integer subdocNumero,
//			BigDecimal movgestNumero, 
//			Integer movgestAnno, 
//			Integer soggettoId,
//			String soggettoCode,
//			Integer eldocAnno,
//			Integer eldocNumero,
//			Pageable pageable);
	
	BigDecimal ricercaSinteticaSubdocumentiPerProvvisorioTotaleImporti(Collection<Integer> uids);


	Page<SiacTSubdoc> ricercaSinteticaSubdocumentiByDocId(
		Integer docId,
		Boolean rilevanteIva,
		Pageable pageable);
	
	List<SiacTSubdoc> ricercaQuoteDaEmettere(Set<SiacDDocFamTipoEnum> docFamTipoCodeEnums,
			Set<SiacDAttoAmmStatoEnum> siacDAttoAmmStatoEnums,
			Set<SiacDDocStatoEnum> siacDDocStatoEnums,
			Integer attoammId,
			Integer attoammAnno,
			Integer attoammNumero,
			List<Integer> eldocIds,
			Integer eldocAnno,
			Integer eldocNumero,
			String convalidaManuale,
			Integer enteProprietarioId			
			);
	Long countQuoteDaEmettere(Set<SiacDDocFamTipoEnum> docFamTipoCodeEnums,
			Set<SiacDAttoAmmStatoEnum> siacDAttoAmmStatoEnums,
			Set<SiacDDocStatoEnum> siacDDocStatoEnums,
			Integer attoammId,
			Integer attoammAnno,
			Integer attoammNumero,
			List<Integer> eldocIds,
			Integer eldocAnno,
			Integer eldocNumero,
			String convalidaManuale,
			Integer enteProprietarioId			
			);
	
	void insertUpdateSiacTSubdocSospensione(Integer uid, List<SiacTSubdocSospensione> siacTSubdocSospensiones);
}
