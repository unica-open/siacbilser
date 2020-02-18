/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dad;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.model.StatoOperativoAtti;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilser.integration.dad.ExtendedBaseDadImpl;
import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.dao.SiacRSubdocAttrRepository;
import it.csi.siac.siacbilser.integration.dao.SiacRSubdocLiquidazioneRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTLiquidazioneRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTSubdocNumRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTSubdocRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDAttoAmmStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocFamTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDProvCassaTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.dao.subdoc.SubdocumentoDaoCustom;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * Versione customizzata dell'originale SubdocumentoSpesaDad di bilancio, ci serve solo per la ricerca sub documenti
 * per creare una carta contabile da documento..
 *
 * @author Claudio Picco
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class SubdocumentoSpesaDadCustom extends ExtendedBaseDadImpl {
	
	private LogUtil log = new LogUtil(this.getClass());
	
	@Autowired
	private EnumEntityFactory eef;
	
	@Autowired
	private SubdocumentoDaoCustom subdocumentoDaoCustom;
	
	@Autowired
	private SiacTSubdocRepository siacTSubdocRepository;
	
	@Autowired
	private SiacTSubdocNumRepository siacTSubdocNumRepository;
	
	@Autowired
	private SiacRSubdocAttrRepository siacRSubdocAttrRepository;
	
	@Autowired
	private SiacRSubdocLiquidazioneRepository siacRSubdocLiquidazioneRepository;
	
	@Autowired
	private SiacTLiquidazioneRepository siacTLiquidazioneRepository;
	
	
	/**
	 * Ricerca subdocumenti spesa.
	 *
	 * @param ente the ente
	 * @param uidElenco the uid elenco
	 * @param annoElenco the anno elenco
	 * @param numeroElenco the numero elenco
	 * @param numeroElencoDa the numero elenco da
	 * @param numeroElencoA the numero elenco a
	 * @param annoProvvisorio the anno provvisorio
	 * @param numeroProvvisorio the numero provvisorio
	 * @param dataProvvisorio the data provvisorio
	 * @param tipoDocumento the tipo documento
	 * @param annoDocumento the anno documento
	 * @param numeroDocumento the numero documento
	 * @param dataEmissioneDocumento the data emissione documento
	 * @param numeroQuota the numero quota
	 * @param numeroMovimento the numero movimento
	 * @param annoMovimento the anno movimento
	 * @param soggetto the soggetto
	 * @param uidProvvedimento the uid provvedimento
	 * @param annoProvvedimento the anno provvedimento
	 * @param numeroProvvedimento the numero provvedimento
	 * @param tipoAtto the tipo atto
	 * @param struttAmmContabile the strutt amm contabile
	 * @param annoCapitolo the anno capitolo
	 * @param numeroCapitolo the numero capitolo
	 * @param numeroArticolo the numero articolo
	 * @param numeroUEB the numero ueb
	 * @param statiOperativoDocumento the stati operativo documento
	 * @param collegatoAMovimentoDelloStessoBilancio the collegato a movimento dello stesso bilancio
	 * @param associatoAProvvedimentoOAdElenco the associato a provvedimento o ad elenco
	 * @param importoDaPagareZero the importo da pagare zero
	 * @param importoDaPagareOIncassareMaggioreDiZero the importo da pagare o incassare maggiore di zero
	 * @param rilevatiIvaConRegistrazioneONonRilevantiIva the rilevati iva con registrazione o non rilevanti iva
	 * @param collegatoALiquidazioneInStatoValidoConProvvDefinitivoConDispPagareMaggioreDiSubdocImporto the collegato a liquidazione in stato valido con provv definitivo con disp pagare maggiore di subdoc importo
	 * @param statiOperativiAtti the stati operativi atti
	 * @param associatoAdOrdinativo the associato ad ordinativo
	 * @param parametriPaginazione the parametri paginazione
	 * @return the lista paginata
	 */
	public ListaPaginata<SubdocumentoSpesa> ricercaSubdocumentiSpesa(Ente ente, 
					Bilancio bilancio,
					Integer uidElenco,
					Integer annoElenco,
					Integer numeroElenco,
					Integer numeroElencoDa,
					Integer numeroElencoA,
					Integer annoProvvisorio,
					Integer numeroProvvisorio,
					Date dataProvvisorio,
					TipoDocumento tipoDocumento,
					List<String> tipiDocDaEscludere,
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
					Integer annoCapitolo,
					Integer numeroCapitolo,
					Integer numeroArticolo,
					Integer numeroUEB,
					List<StatoOperativoDocumento> statiOperativoDocumento,
					Boolean collegatoAMovimentoDelloStessoBilancio, 
					Boolean associatoAProvvedimentoOAdElenco, 
					Boolean importoDaPagareZero, 
					Boolean importoDaPagareOIncassareMaggioreDiZero,
					BigDecimal importoLiquidabileEsatto,
					Boolean rilevatiIvaConRegistrazioneONonRilevantiIva,
					Boolean collegatoALiquidazioneInStatoValidoConProvvDefinitivoConDispPagareMaggioreDiSubdocImporto,
					Collection<StatoOperativoAtti> statiOperativiAtti,
					Boolean associatoAdOrdinativo,
					
					Boolean collegatoAMovimento,
					Boolean collegatoACarteContabili,
					Boolean escludiGiaPagatiDaOrdinativoSpesa,
					
					ParametriPaginazione parametriPaginazione
			) {
			
		Set<SiacDAttoAmmStatoEnum> siacDAttoAmmStatoEnums = SiacDAttoAmmStatoEnum.byStatiOperativi(statiOperativiAtti);
		
		Page<SiacTSubdoc> lista = subdocumentoDaoCustom.ricercaSinteticaSubdocumenti(ente.getUid(), bilancio!=null?bilancio.getUid():null,
				EnumSet.of(SiacDDocFamTipoEnum.Spesa, SiacDDocFamTipoEnum.IvaSpesa),
				 uidElenco,
				 annoElenco,
				 numeroElenco,
				 numeroElencoDa,
				 numeroElencoA,
				 annoProvvisorio,
				 numeroProvvisorio != null ? new BigDecimal(numeroProvvisorio.toString()) : null,
				 dataProvvisorio,
				 SiacDProvCassaTipoEnum.Spesa,
				 tipoDocumento!=null?tipoDocumento.getUid():null,
				 tipiDocDaEscludere,
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
				 annoCapitolo,
				 numeroCapitolo,
		   		 numeroArticolo,
				 numeroUEB,
				 SiacDDocStatoEnum.byStatiOperativi(statiOperativoDocumento),
				 collegatoAMovimentoDelloStessoBilancio, 
				 associatoAProvvedimentoOAdElenco, 
				 importoDaPagareZero, 
				 importoDaPagareOIncassareMaggioreDiZero,
				 importoLiquidabileEsatto,
				 rilevatiIvaConRegistrazioneONonRilevantiIva,
				 collegatoALiquidazioneInStatoValidoConProvvDefinitivoConDispPagareMaggioreDiSubdocImporto,
				 siacDAttoAmmStatoEnums,
				 associatoAdOrdinativo,
				 
				 collegatoAMovimento,
				 collegatoACarteContabili,
				 escludiGiaPagatiDaOrdinativoSpesa,
				 
				 toPageable(parametriPaginazione)		 
				);
		
		return toListaPaginata(lista, SubdocumentoSpesa.class, BilMapId.SiacTSubdoc_SubdocumentoSpesa);
		
	}
	
	
	/**
	 * Ricerca subdocumenti spesa totale importi.
	 *
	 * @param ente the ente
	 * @param uidElenco the uid elenco
	 * @param annoElenco the anno elenco
	 * @param numeroElenco the numero elenco
	 * @param numeroElencoDa the numero elenco da
	 * @param numeroElencoA the numero elenco a
	 * @param annoProvvisorio the anno provvisorio
	 * @param numeroProvvisorio the numero provvisorio
	 * @param dataProvvisorio the data provvisorio
	 * @param tipoDocumento the tipo documento
	 * @param annoDocumento the anno documento
	 * @param numeroDocumento the numero documento
	 * @param dataEmissioneDocumento the data emissione documento
	 * @param numeroQuota the numero quota
	 * @param numeroMovimento the numero movimento
	 * @param annoMovimento the anno movimento
	 * @param soggetto the soggetto
	 * @param uidProvvedimento the uid provvedimento
	 * @param annoProvvedimento the anno provvedimento
	 * @param numeroProvvedimento the numero provvedimento
	 * @param tipoAtto the tipo atto
	 * @param struttAmmContabile the strutt amm contabile
	 * @param annoCapitolo the anno capitolo
	 * @param numeroCapitolo the numero capitolo
	 * @param numeroArticolo the numero articolo
	 * @param numeroUEB the numero ueb
	 * @param statiOperativoDocumento the stati operativo documento
	 * @param collegatoAMovimentoDelloStessoBilancio the collegato a movimento dello stesso bilancio
	 * @param associatoAProvvedimentoOAdElenco the associato a provvedimento o ad elenco
	 * @param importoDaPagareZero the importo da pagare zero
	 * @param importoDaPagareOIncassareMaggioreDiZero the importo da pagare o incassare maggiore di zero
	 * @param rilevatiIvaConRegistrazioneONonRilevantiIva the rilevati iva con registrazione o non rilevanti iva
	 * @param collegatoALiquidazioneInStatoValidoConProvvDefinitivoConDispPagareMaggioreDiSubdocImporto the collegato a liquidazione in stato valido con provv definitivo con disp pagare maggiore di subdoc importo
	 * @param statiOperativiAtti the stati operativi atti
	 * @param associatoAdOrdinativo the associato ad ordinativo
	 * @param parametriPaginazione the parametri paginazione
	 * @return the big decimal
	 */
	public BigDecimal ricercaSubdocumentiSpesaTotaleImporti(Ente ente, 
			Bilancio bilancio,
			Integer uidElenco,
			Integer annoElenco,
			Integer numeroElenco,
			Integer numeroElencoDa,
			Integer numeroElencoA,
			Integer annoProvvisorio,
			Integer numeroProvvisorio,
			Date dataProvvisorio,
			TipoDocumento tipoDocumento,
			List<String> tipiDocDaEscludere,
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
			Integer annoCapitolo,
			Integer numeroCapitolo,
			Integer numeroArticolo,
			Integer numeroUEB,
			List<StatoOperativoDocumento> statiOperativoDocumento,
			Boolean collegatoAMovimentoDelloStessoBilancio, 
			Boolean associatoAProvvedimentoOAdElenco, 
			Boolean importoDaPagareZero, 
			Boolean importoDaPagareOIncassareMaggioreDiZero,
			BigDecimal importoLiquidabileEsatto,
			Boolean rilevatiIvaConRegistrazioneONonRilevantiIva,
			Boolean collegatoALiquidazioneInStatoValidoConProvvDefinitivoConDispPagareMaggioreDiSubdocImporto,
			Collection<StatoOperativoAtti> statiOperativiAtti,
			Boolean associatoAdOrdinativo,
			
			
			Boolean collegatoAMovimento,
			Boolean collegatoACarteContabili,
			Boolean escludiGiaPagatiDaOrdinativoSpesa,
			
			ParametriPaginazione parametriPaginazione
	) {
		
		Set<SiacDAttoAmmStatoEnum> siacDAttoAmmStatoEnums = SiacDAttoAmmStatoEnum.byStatiOperativi(statiOperativiAtti);
	
			BigDecimal totale = subdocumentoDaoCustom.ricercaSinteticaSubdocumentiTotaleImporti(ente.getUid(), bilancio!=null?bilancio.getUid():null,
					EnumSet.of(SiacDDocFamTipoEnum.Spesa, SiacDDocFamTipoEnum.IvaSpesa),
					 uidElenco,
					 annoElenco,
					 numeroElenco,
					 numeroElencoDa,
					 numeroElencoA,
					 annoProvvisorio,
					 numeroProvvisorio != null ? new BigDecimal(numeroProvvisorio.toString()) : null,
					 dataProvvisorio,
					 SiacDProvCassaTipoEnum.Spesa,
					 tipoDocumento!=null?tipoDocumento.getUid():null,
					 tipiDocDaEscludere,
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
					 annoCapitolo,
					 numeroCapitolo,
					 numeroArticolo,
					 numeroUEB,
					 SiacDDocStatoEnum.byStatiOperativi(statiOperativoDocumento),
					 collegatoAMovimentoDelloStessoBilancio, 
					 associatoAProvvedimentoOAdElenco, 
					 importoDaPagareZero, 
					 importoDaPagareOIncassareMaggioreDiZero,
					 importoLiquidabileEsatto,
					 rilevatiIvaConRegistrazioneONonRilevantiIva,
					 collegatoALiquidazioneInStatoValidoConProvvDefinitivoConDispPagareMaggioreDiSubdocImporto,
					 siacDAttoAmmStatoEnums,
					 associatoAdOrdinativo,
					 
					 collegatoAMovimento,
					 collegatoACarteContabili,
					 escludiGiaPagatiDaOrdinativoSpesa,
					 
					 toPageable(parametriPaginazione)		 
					);
			
			return totale;
	
	}

	public boolean giaPagatoDaOrdinativoSpesa(Integer subdocId, DatiOperazioneDto datiOperazione){
		return subdocumentoDaoCustom.giaPagatoDaOrdinativoSpesa(subdocId, datiOperazione);
	}

}
