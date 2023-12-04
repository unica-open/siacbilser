/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentospesa;

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

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoSpesaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteDaEmettereSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteDaEmettereSpesaResponse;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;

/**
 * The Class RicercaQuoteDaEmettereSpesaService.
 * 
 * @author Domenico
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaQuoteDaEmettereSpesaService extends CheckedAccountBaseService<RicercaQuoteDaEmettereSpesa, RicercaQuoteDaEmettereSpesaResponse> {

	@Autowired
	private SubdocumentoSpesaDad subdocumentoSpesaDad;
	
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		checkCondition((req.getNumeroCapitolo()!= null && req.getNumeroArticolo() != null) || (req.getNumeroCapitolo() == null && req.getNumeroArticolo() == null),
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero capitolo e numero articolo"));
		
		checkCondition((req.getNumeroProvvedimento()!= null && req.getAnnoProvvedimento() != null) || (req.getNumeroProvvedimento() == null && req.getAnnoProvvedimento() == null),
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno provvedimento e numero provvedimento"));
		
		boolean numeroElencoValorizzato = req.getNumeroElencoDa() != null ||  req.getNumeroElencoA()!=null || req.getNumeroElenco()!=null;
		checkCondition((req.getAnnoElenco()!= null && numeroElencoValorizzato) || (req.getAnnoElenco() == null && !numeroElencoValorizzato),
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno elenco e numero elenco"));
		
		if(req.getNumeroElencoDa()!=null && req.getNumeroElencoA()!=null){
			checkCondition(req.getNumeroElencoDa().compareTo(req.getNumeroElencoA())<=0, ErroreCore.VALORE_NON_CONSENTITO.getErrore("anno elenco e numero elenco"));
		}
		
		checkParametriPaginazione(req.getParametriPaginazione());
		
	}
	
	@Override
	@Transactional(readOnly=true)
	public RicercaQuoteDaEmettereSpesaResponse executeService(RicercaQuoteDaEmettereSpesa serviceRequest) {
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
		statiOperativiDocumento.addAll(statiOperativiSet);
		
		ListaPaginata<SubdocumentoSpesa> listaSubdocumentiSpesa = subdocumentoSpesaDad.ricercaSubdocumentiSpesa(
				ente,
				null, //bilancio
				req.getUidElenco(),
				req.getAnnoElenco(),
				req.getNumeroElenco(),
				req.getNumeroElencoDa(),
				req.getNumeroElencoA(),
				null, //anno provvisorio
				null, //numero provvisorio
				null, //data provvisorio
				null, //tipo documento
				null, //anno documento
				null, //numero documento
				null, //data emissione documento
				null, //numero quota
				null, //numero movimento
				null, //anno movimento
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
				Boolean.TRUE, //collegatoALiquidazioneInStatoValidoConProvvDefinitivoConDispPagareMaggioreDiSubdocImporto
				null, // TODO: controllare - statiOperativiAtti
				null, // TODO: controllare - associatoAdOrdinativo
				Boolean.TRUE, // TODO: flag convalida manuale
				null, // lista provcassa uid
				req.getParametriPaginazione()
				);
		
		res.setListaSubdocumenti(listaSubdocumentiSpesa);
		
		BigDecimal totaleImporti = subdocumentoSpesaDad.ricercaSubdocumentiSpesaTotaleImporti(
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
				null,
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
				Boolean.TRUE, //collegatoALiquidazioneInStatoValidoConProvvDefinitivoConDispPagareMaggioreDiSubdocImporto
				null, // TODO: controllare
				null, // TODO: controllare
				Boolean.TRUE, // TODO: controllare
				null
				);
		
		res.setTotaleImporti(totaleImporti);
		
	}

}
