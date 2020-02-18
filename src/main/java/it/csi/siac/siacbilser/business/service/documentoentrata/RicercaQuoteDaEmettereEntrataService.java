/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentoentrata;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.model.StatoOperativoAtti;
import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoEntrataDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteDaEmettereEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteDaEmettereEntrataResponse;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;

/**
 * The Class RicercaQuoteDaEmettereSpesaService.
 * 
 * @author Domenico
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaQuoteDaEmettereEntrataService extends CheckedAccountBaseService<RicercaQuoteDaEmettereEntrata, RicercaQuoteDaEmettereEntrataResponse> {

	@Autowired
	private SubdocumentoEntrataDad subdocumentoEntrataDad;
	
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		checkCondition((req.getNumeroCapitolo()!= null && req.getNumeroArticolo() != null) || (req.getNumeroCapitolo() == null && req.getNumeroArticolo() == null),ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero capitolo e numero articolo"));
		
		checkCondition((req.getNumeroProvvedimento()!= null && req.getAnnoProvvedimento() != null) || (req.getNumeroProvvedimento() == null && req.getAnnoProvvedimento() == null),ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno provvedimento e numero provvedimento"));
		
		boolean numeroElencoValorizzato = req.getNumeroElencoDa() != null ||  req.getNumeroElencoA()!=null || req.getNumeroElenco()!=null;
		checkCondition((req.getAnnoElenco()!= null && numeroElencoValorizzato) || (req.getAnnoElenco() == null && !numeroElencoValorizzato),ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno elenco e numero elenco"));
		
		if(req.getNumeroElencoDa()!=null && req.getNumeroElencoA()!=null){
			checkCondition(req.getNumeroElencoDa().compareTo(req.getNumeroElencoA())<=0, ErroreCore.VALORE_NON_VALIDO.getErrore("anno elenco e numero elenco"));
		}
		
		checkParametriPaginazione(req.getParametriPaginazione());
		
	}
	
	@Override
	@Transactional(readOnly=true)
	public RicercaQuoteDaEmettereEntrataResponse executeService(RicercaQuoteDaEmettereEntrata serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		
		List<StatoOperativoDocumento> statiOperativiDocumento = new ArrayList<StatoOperativoDocumento>();
		//	StatoOperativoDocumento  <>  A – ANNULLATO o S-STORNATO o E – EMESSO
		final Set<StatoOperativoDocumento> statiOperativiSet = EnumSet.allOf(StatoOperativoDocumento.class);
		statiOperativiSet.remove(StatoOperativoDocumento.ANNULLATO);
		statiOperativiSet.remove(StatoOperativoDocumento.STORNATO);
		statiOperativiSet.remove(StatoOperativoDocumento.EMESSO);
		//XXX: forse è da aggiungere il filtro per stato incompleto: VALUTARE LE CONSEGUENZE
		statiOperativiDocumento.addAll(statiOperativiSet);
		
		//TODO se si dovesse filtrare per tipo IPA inserire il codice di calcolo dell'id 
		//req.getTipoDocumento().getCodice()
		
		ListaPaginata<SubdocumentoEntrata> listaSubdocumentiEntrata = subdocumentoEntrataDad.ricercaSubdocumentiEntrata(
				ente,
				null,
				req.getUidElenco(),
				req.getAnnoElenco(),
				req.getNumeroElenco(),
				req.getNumeroElencoDa(),
				req.getNumeroElencoA(),
				null,
				null,
				null,
				null,//TIPO documento
				null,
				null,
				null,
				null,
				null,
				null,
				req.getSoggetto(),
				req.getUidProvvedimento(),
				req.getAnnoProvvedimento(),
				req.getNumeroProvvedimento(),
				req.getTipoAtto(),
				req.getStruttAmmContabile(),
				req.getAnnoCapitolo(),
				req.getNumeroCapitolo(),
				req.getNumeroArticolo(),
				req.getNumeroUEB(),
				statiOperativiDocumento,
				null, //collegatoAMovimentoDelloStessoBilancio
				null, //associatoAProvvedimentoOAdElenco
				null,//importoDaPagareZero
				Boolean.TRUE, //importoDaPagareMaggioreDiZero(),
				null,//rilevatiIvaConRegistrazioneONonRilevantiIva(),		
				null, //collegatoALiquidazioneInStatoValidoConProvvDefinitivoConDispPagareMaggioreDiSubdocImporto
				EnumSet.of(StatoOperativoAtti.DEFINITIVO),
				Boolean.FALSE, // associatoAdOrdinativo
				Boolean.TRUE, // Collegato a atto convalidato o a nessun atto
				req.getListProvvisorioDiCassa(),
				req.getParametriPaginazione()
				);
		
		res.setListaSubdocumenti(listaSubdocumentiEntrata);

		BigDecimal totaleImporti = subdocumentoEntrataDad.ricercaSubdocumentiEntrataTotaleImporti(
				ente,
				null,
				req.getUidElenco(),
				req.getAnnoElenco(),
				req.getNumeroElenco(),
				req.getNumeroElencoDa(),
				req.getNumeroElencoA(),
				null,
				null,
				null,
				null,//tipo ducumento 
				null,
				null,
				null,
				null,
				null,
				null,
				req.getSoggetto(),
				req.getUidProvvedimento(),
				req.getAnnoProvvedimento(),
				req.getNumeroProvvedimento(),
				req.getTipoAtto(),
				req.getStruttAmmContabile(),
				req.getAnnoCapitolo(),
				req.getNumeroCapitolo(),
				req.getNumeroArticolo(),
				req.getNumeroUEB(),
				statiOperativiDocumento,
				null, //collegatoAMovimentoDelloStessoBilancio
				null, //associatoAProvvedimentoOAdElenco
				null,//importoDaPagareZero
				Boolean.TRUE, //importoDaPagareMaggioreDiZero(),
				null,//rilevatiIvaConRegistrazioneONonRilevantiIva(),		
				null, //collegatoALiquidazioneInStatoValidoConProvvDefinitivoConDispPagareMaggioreDiSubdocImporto
				EnumSet.of(StatoOperativoAtti.DEFINITIVO),
				Boolean.FALSE, // associatoAdOrdinativo
				Boolean.TRUE, // Collegato a atto convalidato o a nessun atto
				req.getListProvvisorioDiCassa()
				);
		
		res.setTotaleImporti(totaleImporti);
		
	}
	
}
