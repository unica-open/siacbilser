/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.carta;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfinser.StringUtils;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaCartaContabile;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaCartaContabileResponse;
import it.csi.siac.siacfinser.integration.dad.CartaContabileDad;
import it.csi.siac.siacfinser.integration.dad.CommonDad;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.entity.SiacTDocFin;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.model.carta.CartaContabile;
import it.csi.siac.siacfinser.model.errore.ErroreFin;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AnnullaCartaContabileService extends AbstractBaseService<AnnullaCartaContabile, AnnullaCartaContabileResponse> {

	@Autowired
	CartaContabileDad cartaContabileDad; 

	@Autowired
	CommonDad commonDad;
	
	@Autowired
	DocumentoSpesaService documentoSpesaService;

	@Override
	protected void init() {
		final String methodName="AnnullaCartaContabileService : init()";
		log.debug(methodName, " - Begin");
	}	
	
	
	@Override
	@Transactional
	public AnnullaCartaContabileResponse executeService(AnnullaCartaContabile serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	public void execute() {
		String methodName = "AnnullaCartaContabileService - execute()";
		log.debug(methodName, " - Begin");
		
		Ente ente = req.getEnte();
		Bilancio bilancio = req.getBilancio();
		Richiedente richiedente = req.getRichiedente();
		
		DatiOperazioneDto datiOperazione = commonDad.inizializzaDatiOperazione(ente, req.getRichiedente(), Operazione.ANNULLA, bilancio.getAnno());

		CartaContabile cartaContabileDaAnnullare = req.getCartaContabileDaAnnullare();

		/*
		 * Verifico i dati Trasmessi:
		 * 
		 * Transizione Stati: il passaggio dal vecchio al nuovo stato deve rispettare le transizioni descritte a par. 2.5.3,
		 * in caso contrario viene emesso l'errore:
		 * <FIN_ERR_055: Stato Movimento Impossibile (statoOld=statoOperativoCartaContabile,  statoNew= 'ANNULLATO',movimento=Carta Contabile')>
		 * 
		 * Documenti Collegati:
		 * 
		 * se la carta in annullamento e' collegata ad almeno un SubDocumentoSpesa relativo ad un Documento di tipo CNN (Carta Contabile)
		 * verificare che lo StatoOperativoDocumento sia diverso da:
		 * L-LIQUIDATO, PL-PARZIALMENTE LIQUIDATO, PE-PARZIALMENTE EMESSO o EM-EMESSO
		 * 
		 * e procedere all'annullamento del documento
		 * (vedi operazione 'Annulla Documento Spesa' del servizio 'SPES006 - Servizio Gestione Documento di Spesa')
		 * 
		 * e alla cancellazione della relazione con la carta, in caso invece il Documento abbia uno degli stati citati l'operazione
		 * viene bloccata con il seguente messaggio < FIN_INF_0260	entita' collegate (<entita'> ''documenti d tipo CCN liquidati',    <operazione>:  non e' possibile procedere con l'annullamento della carta.' > .
		 * 
		 *	se e' collegata ad un documento di tipo diverso cancellare la relazione SubDocumento e dettagli della carta.
		 */
		
		List<Errore> listaErrori = cartaContabileDad.verificaDatiTrasmessiAnnullamentoCartaContabile(ente, richiedente, bilancio, cartaContabileDaAnnullare, datiOperazione);

		if(listaErrori!=null && listaErrori.size()>0){
			res.setErrori(listaErrori);
			res.setEsito(Esito.FALLIMENTO);
			return;
		}

		/*
		 *  Estraggo l'elenco degli eventuali documenti di spesa da annullare
		 *  Li estraggo passando dai SubDocumenti di spesa legati alla carta contabile che si vuole annullare
		 */

		List<SiacTDocFin> elencoSiacTDocDaAnnullare = cartaContabileDad.estraiElencoSiacTDocDaAnnullare(ente, richiedente, bilancio, cartaContabileDaAnnullare, datiOperazione);
		
		/*
		 * Annullo la Carta:
		 * 
		 * L'operazione annulla in archivio la carta indicata nel parametro di input, annulla le sue righe (PreDocumentoCarta)
		 * e scollega tutti i SubDocumentoSpesa eventualmente collegati alle righe della carta contabile che si vuole annullare.
		 * 
		 */
		
		// annullo la carta e le sue righe
		boolean annullaCartaContabile = cartaContabileDad.annullaCartaContabile(ente, richiedente, bilancio, cartaContabileDaAnnullare, datiOperazione);
		
		if(!annullaCartaContabile){
			res.setErrori(Arrays.asList(ErroreFin.ERRORE_IN_CARTA_CONTABILE.getErrore("ANNULLAMENTO CARTA CONTABILE")));
			res.setEsito(Esito.FALLIMENTO);
			return;
		}

		/*
		 *  Annullo gli eventuali documenti di spesa precedentemente estratti
		 *  
		 */
		
		if(elencoSiacTDocDaAnnullare!=null && elencoSiacTDocDaAnnullare.size()>0){
			for(SiacTDocFin siacTDoc : elencoSiacTDocDaAnnullare){

				AnnullaDocumentoSpesa annullaDocumentoSpesa = new AnnullaDocumentoSpesa();
				annullaDocumentoSpesa.setRichiedente(richiedente);

				DocumentoSpesa documentoSpesaDaAnnullare = new DocumentoSpesa();

				documentoSpesaDaAnnullare.setEnte(ente);
				documentoSpesaDaAnnullare.setUid(siacTDoc.getDocId());
				
				// RM - Adeguamento siac_3041 PL 
				annullaDocumentoSpesa.setBilancio(req.getBilancio());
				
				annullaDocumentoSpesa.setDocumentoSpesa(documentoSpesaDaAnnullare);

				AnnullaDocumentoSpesaResponse annullaDocumentoSpesaResponse = documentoSpesaService.annullaDocumentoSpesa(annullaDocumentoSpesa);

				if(annullaDocumentoSpesaResponse.isFallimento()){
					res.setErrori(annullaDocumentoSpesaResponse.getErrori());
					res.setEsito(Esito.FALLIMENTO);
					return;
				}
			}
		}
		
		res.setErrori(null);
		res.setEsito(Esito.SUCCESSO);

	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {

		final String methodName="AnnullaCartaContabileService : checkServiceParam()";
		log.debug(methodName, " - Begin");

		Richiedente richiedente = req.getRichiedente();
		Ente ente = req.getEnte();
		Bilancio bilancio = req.getBilancio();
		CartaContabile cartaContabileDaAnnullare = req.getCartaContabileDaAnnullare();

		String elencoParametriNonInizializzati = "";

		/*
		 * Il servizio controlla che tutti i parametri di input siano stati inizializzati, in caso contrario viene segnalato
		 * l'errore COR_ERR_0003 con l'indicazione dei parametri mancanti.
		 */
		
		// verifico obbligatorieta' ente
		if(ente==null){
			if(elencoParametriNonInizializzati.length() > 0)
				elencoParametriNonInizializzati = elencoParametriNonInizializzati + ", ENTE";
			else
				elencoParametriNonInizializzati = elencoParametriNonInizializzati + "ENTE";
		}

		// verifico obbligatorieta' bilancio
		if(bilancio==null){
			if(elencoParametriNonInizializzati.length() > 0)
				elencoParametriNonInizializzati = elencoParametriNonInizializzati + ", BILANCIO";
			else
				elencoParametriNonInizializzati = elencoParametriNonInizializzati + "BILANCIO";
		} else {
			if(bilancio.getAnno()==0){
				if(elencoParametriNonInizializzati.length() > 0)
					elencoParametriNonInizializzati = elencoParametriNonInizializzati + ", ANNO_BILANCIO";
				else
					elencoParametriNonInizializzati = elencoParametriNonInizializzati + "ANNO_BILANCIO";
			}	
		}

		// verifico obbligatorieta' carta contabile da annullare
		if(cartaContabileDaAnnullare==null){
			if(elencoParametriNonInizializzati.length() > 0)
				elencoParametriNonInizializzati = elencoParametriNonInizializzati + ", CARTA_CONTABILE";
			else
				elencoParametriNonInizializzati = elencoParametriNonInizializzati + "CARTA_CONTABILE";
		} 
		
		// Nel caso in cui manchi uno o piu' parametri, viene restituito l'errore relativo
		if(!StringUtils.isEmpty(elencoParametriNonInizializzati)){
			res.setErrori(Arrays.asList(ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore(elencoParametriNonInizializzati)));
			res.setEsito(Esito.FALLIMENTO);
		}
	}
}