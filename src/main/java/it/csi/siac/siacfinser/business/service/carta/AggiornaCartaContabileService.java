/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.carta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.StringUtilsFin;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaCartaContabile;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaCartaContabileResponse;
import it.csi.siac.siacfinser.integration.dad.CartaContabileDad;
import it.csi.siac.siacfinser.integration.dad.CommonDad;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoGestioneCartaContabileDto;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.model.carta.CartaContabile;
import it.csi.siac.siacfinser.model.carta.PreDocumentoCarta;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaCartaContabileService extends AbstractGestioneCartaContabileService<AggiornaCartaContabile, AggiornaCartaContabileResponse> {

	@Autowired
	CartaContabileDad cartaContabileDad;

	@Autowired
	CommonDad commonDad;

	@Autowired
	DocumentoSpesaService documentoSpesaService;

	@Override
	protected void init() {
		final String methodName = "AggiornaCartaContabileService : init()";
		log.debug(methodName, " - Begin");

	}
	
	@Override
	@Transactional
	public AggiornaCartaContabileResponse executeService(AggiornaCartaContabile serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		String methodName = "AggiornaCartaContabileService - execute()";
		log.debug(methodName, " - Begin");

		Richiedente richiedente = req.getRichiedente();
		Ente ente = req.getEnte();
		Bilancio bilancio = req.getBilancio();
		CartaContabile cartaContabile = req.getCartaContabile();
		

		DatiOperazioneDto datiOperazione = commonDad.inizializzaDatiOperazione(ente, req.getRichiedente(),Operazione.MODIFICA, bilancio.getAnno());

		// INIZIO - Estrazione documenti dei subdocumentispesa
		if (cartaContabile != null
				&& cartaContabile.getListaPreDocumentiCarta() != null
				&& cartaContabile.getListaPreDocumentiCarta().size() > 0) {
			for (PreDocumentoCarta preDocumentoCarta : cartaContabile
					.getListaPreDocumentiCarta()) {
				if (preDocumentoCarta.getListaSubDocumentiSpesaCollegati() != null
						&& preDocumentoCarta
								.getListaSubDocumentiSpesaCollegati()
								.size() > 0) {
					for (SubdocumentoSpesa subdocumentoSpesaInput : preDocumentoCarta
							.getListaSubDocumentiSpesaCollegati()) {
						if (subdocumentoSpesaInput.getDocumento() != null
								&& subdocumentoSpesaInput.getDocumento().getAnno() != null
								&& subdocumentoSpesaInput.getDocumento().getNumero() != null
								&& subdocumentoSpesaInput.getDocumento().getTipoDocumento() != null
								&& subdocumentoSpesaInput.getDocumento().getSoggetto() != null) {

							RicercaDettaglioDocumentoSpesa req = new RicercaDettaglioDocumentoSpesa();
							req.setRichiedente(richiedente);
							req.setDocumentoSpesa(subdocumentoSpesaInput.getDocumento());

							RicercaDettaglioDocumentoSpesaResponse response = documentoSpesaService.ricercaDettaglioDocumentoSpesa(req);
							if (response.isFallimento()) {
								res.setErrori(response.getErrori());
								res.setEsito(Esito.FALLIMENTO);
								res.setCartaContabile(null);
								return;
							}

							subdocumentoSpesaInput.setDocumento(response.getDocumento());
						} else {
							// ERRORE no documento coll a subdocumento spesa
						}

					}

				}
			}
		}
		// FINE - EEstrazione documenti dei subdocumentispesa

		EsitoGestioneCartaContabileDto esitoAggiornaCartaContabile = cartaContabileDad.aggiornaCartaContabile(richiedente, CostantiFin.AMBITO_FIN, ente, bilancio,
						cartaContabile, datiOperazione);

		if (esitoAggiornaCartaContabile.getListaErrori() != null
				&& esitoAggiornaCartaContabile.getListaErrori().size() > 0) {
			res.setErrori(esitoAggiornaCartaContabile.getListaErrori());
			res.setCartaContabile(null);
			res.setEsito(Esito.FALLIMENTO);
			TransactionAspectSupport.currentTransactionStatus()
					.setRollbackOnly();
			return;
		}

		if (esitoAggiornaCartaContabile.getCartaContabile() != null) {
			res.setCartaContabile(esitoAggiornaCartaContabile
					.getCartaContabile());
			res.setEsito(Esito.SUCCESSO);
		} else {
			res.setCartaContabile(null);
			res.setEsito(Esito.FALLIMENTO);
		}

	}

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		final String methodName = "InserisceCartaContabileService : checkServiceParam()";
		log.debug(methodName, " - Begin");

		Ente ente = req.getEnte();
		Bilancio bilancio = req.getBilancio();
		Richiedente richiedente = req.getRichiedente();
		CartaContabile cartaContabile = req.getCartaContabile();

		String elencoParamentriNonInizializzati = "";

		if (richiedente == null) {
			if (elencoParamentriNonInizializzati.length() > 0)
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati
						+ ", RICHIEDENTE";
			else
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati
						+ "RICHIEDENTE";
		}

		if (ente == null) {
			if (elencoParamentriNonInizializzati.length() > 0)
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati
						+ ", ENTE";
			else
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati
						+ "ENTE";
		}

		if (bilancio == null) {
			if (elencoParamentriNonInizializzati.length() > 0)
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati
						+ ", BILANCIO";
			else
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati
						+ "BILANCIO";
		} else {
			if (bilancio.getAnno() == 0) {
				if (elencoParamentriNonInizializzati.length() > 0)
					elencoParamentriNonInizializzati = elencoParamentriNonInizializzati
							+ ", ANNO_DI_ESERCIZIO";
				else
					elencoParamentriNonInizializzati = elencoParamentriNonInizializzati
							+ "ANNO_DI_ESERCIZIO";
			}
		}

		if (cartaContabile == null) {
			if (elencoParamentriNonInizializzati.length() > 0)
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati
						+ ", CARTA_CONTABILE";
			else
				elencoParamentriNonInizializzati = elencoParamentriNonInizializzati
						+ "CARTA_CONTABILE";
		} else {
			
			
			//Controllo sulle righe:
			
			for (PreDocumentoCarta preDocumentoCarta : cartaContabile.getListaPreDocumentiCarta()) {
//				SIAC-5713
//				if(isRigaDaMovimenti(preDocumentoCarta)){
					//questo controllo solo per righe da movimenti
					if(preDocumentoCarta.getContoTesoreria()==null ||
							StringUtilsFin.isEmpty(preDocumentoCarta.getContoTesoreria().getCodice())){
						if(elencoParamentriNonInizializzati.length() > 0){
							elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + ", CONTO_TESORIERE_PREDOCUMENTO";
						}else{
							elencoParamentriNonInizializzati = elencoParamentriNonInizializzati + "CONTO_TESORIERE_PREDOCUMENTO";
						}	
					}
			}
			
		}

		if (!StringUtilsFin.isEmpty(elencoParamentriNonInizializzati))
			checkCondition(false,
					ErroreCore.PARAMETRO_NON_INIZIALIZZATO
							.getErrore(elencoParamentriNonInizializzati));
	}

}
