/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.subdoc;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDAttoAmmStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocFamTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDProvCassaTipoEnum;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;
import it.csi.siac.siaccommonser.integration.entity.SiacTBase;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.entity.SiacTDocFin;

/**
 * Versione customizzata dell'originale SubdocumentoDao di bilancio, ci serve solo per la ricerca sub documenti
 * per creare una carta contabile da documento..
 *
 * @author Claudio Picco
 */
public interface SubdocumentoDaoCustom extends Dao<SiacTSubdoc, Integer> {

	
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
	public Page<SiacTSubdoc> ricercaSinteticaSubdocumenti(
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
			List<String> tipiDocDaEscludere,
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
			BigDecimal importoLiquidabileEsatto,
			Boolean rilevatiIvaConRegistrazioneONonRilevantiIva, 
			Boolean collegatoALiquidazioneInStatoValidoConProvvDefinitivoConDispPagareMaggioreDiSubdocImporto,
			Collection<SiacDAttoAmmStatoEnum> siacDAttoAmmStatoEnums,
			Boolean associatoAdOrdinativo,
			
			Boolean collegatoAMovimento,
			Boolean collegatoACarteContabili,
			Boolean escludiGiaPagatiDaOrdinativoSpesa,
			
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
	 * @param pageable the pageable
	 * @return the big decimal
	 */
	public BigDecimal ricercaSinteticaSubdocumentiTotaleImporti(
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
			List<String> tipiDocDaEscludere,
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
			BigDecimal importoLiquidabileEsatto,
			Boolean rilevatiIvaConRegistrazioneONonRilevantiIva, 
			Boolean collegatoALiquidazioneInStatoValidoConProvvDefinitivoConDispPagareMaggioreDiSubdocImporto,
			Collection<SiacDAttoAmmStatoEnum> siacDAttoAmmStatoEnums,
			Boolean associatoAdOrdinativo,
			
			Boolean collegatoAMovimento,
			Boolean collegatoACarteContabili,
			Boolean escludiGiaPagatiDaOrdinativoSpesa,
			
			Pageable pageable);
	
	
	public <ST extends SiacTBase>  List<ST> ricercaBySiacTDocFinMassive(List<SiacTDocFin> listaInput, String nomeEntity);

	public boolean giaPagatoDaOrdinativoSpesa(Integer subdocId, DatiOperazioneDto datiOperazione);
}
