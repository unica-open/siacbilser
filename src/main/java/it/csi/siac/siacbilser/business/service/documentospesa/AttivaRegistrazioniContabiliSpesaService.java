/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentospesa;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dad.DocumentoDad;
import it.csi.siac.siacbilser.integration.dad.DocumentoSpesaDad;
import it.csi.siac.siacbilser.model.TipologiaAttributo;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AttivaRegistrazioniContabiliSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AttivaRegistrazioniContabiliSpesaResponse;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.TipoIvaSplitReverse;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;

/**
 * Attiva le registrazioni contabili (GEN e PCC) impostando il FlagContabilizzaGenPcc=TRUE sul documento di spesa.
 *
 * @author Domenico
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AttivaRegistrazioniContabiliSpesaService extends CheckedAccountBaseService<AttivaRegistrazioniContabiliSpesa, AttivaRegistrazioniContabiliSpesaResponse> {
	
		
	@Autowired
	private DocumentoDad documentoDad;
	@Autowired
	private DocumentoSpesaDad documentoSpesaDad;
	
	private DocumentoSpesa doc;
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getDocumentoSpesa(), "documento spesa");
		checkEntita(req.getBilancio(), "bilancio");
		doc = req.getDocumentoSpesa();
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		documentoDad.setEnte(ente);
		documentoDad.setLoginOperazione(loginOperazione);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public AttivaRegistrazioniContabiliSpesaResponse executeService(AttivaRegistrazioniContabiliSpesa serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		checkAttivazioneGiaEffettuata();
		checkStatoDocumento();
		// SIAC-6160
		checkImportiSplitReverse();
		
		documentoDad.aggiornaFlagContabilizzaGenPcc(doc, Boolean.TRUE);
		
		//Imposta il valore del flagAttivaRegistrazioneResidui con quello passato dall'esterno.
		documentoDad.aggiornaAttributo(doc, TipologiaAttributo.FLAG_DISABILITA_REGISTRAZIONE_RESIDUI, doc.getFlagDisabilitaRegistrazioneResidui());
		
		AggiornaStatoDocumentoDiSpesaResponse resASDS = aggiornaStatoDocumento();
		doc = resASDS.getDocumentoSpesa();
		
		
//		boolean condizioniAttivazioneGENePCCSoddisfatte = (resASDS.getRegistrazioniMovFinInserite() != null && !resASDS.getRegistrazioniMovFinInserite().isEmpty()) ||
//				(resASDS.getRegistrazioniComunicazioniPCC() != null && !resASDS.getRegistrazioniComunicazioniPCC().isEmpty()) ;
		
		boolean condizioniAttivazioneSoddisfatte = impostaFlagCondizioniAttivazioneSoddisfatte(resASDS.isCondizioneDiAttivazioneGENSoddisfatta(), resASDS.isCondizioneDiAttivazionePCCSoddisfatta());
		
		if(!condizioniAttivazioneSoddisfatte){
			documentoDad.aggiornaFlagContabilizzaGenPcc(doc, Boolean.FALSE);
			res.addErrore(ErroreFin.CONDIZIONI_GEN_PCC_NON_SODDISFATTE.getErrore());
		}
		res.setDocumentoSpesa(doc);
		res.setRegistrazioniMovFinInserite(resASDS.getRegistrazioniMovFinInserite());
	}

	/**
	 * Quando viene richiesta la contabilizzazione di un documento di spesa, prima di eseguire l'azione &eacute; necessario
	 * verificare che la somma degli importi split/Revese indicati sulle quote della fattura, divisi per tipologia
	 * (split istituzionale, split commerciale, reverse change) coincidano con le ritenute split/reverse presenti
	 * sugli oneri sempre raggruppate per tipologia.
	 * <p>
	 * Se i totali non quadrano prevedere un errore BLOCCANTE: &quot;Contabilizzazione impossibile: Totali Iva split-reverse-esente non congruenti&quot;.
	 */
	private void checkImportiSplitReverse() {
		Map<TipoIvaSplitReverse, BigDecimal> importiSplitReverseQuote = documentoSpesaDad.getImportiSplitReverseQuoteDocumento(doc);
		Map<TipoIvaSplitReverse, BigDecimal> importiSplitReverseRitenute = documentoSpesaDad.getImportiSplitReverseRitenuteDocumento(doc);
		
		for(TipoIvaSplitReverse tirs : TipoIvaSplitReverse.values()) {
			BigDecimal importoQuote = defaultImporto(importiSplitReverseQuote.get(tirs));
			BigDecimal importoRitenute = defaultImporto(importiSplitReverseRitenute.get(tirs));
			
			if(importoQuote.compareTo(importoRitenute) != 0) {
				throw new BusinessException(ErroreFin.TOTALE_SPLIT_REVERSE_NON_CONGRUENTI.getErrore("(tipo iva: " + tirs.getDescrizione()
					+ ", importo quote: " + Utility.formatCurrencyAsString(importoQuote)
					+ ", importo ritenute: " + Utility.formatCurrencyAsString(importoRitenute) + ")"));
			}
		}
		
	}

	/**
	 * Defaults the value to 0
	 * @param value the value to default
	 * @return the value if not null or a default value of 0
	 */
	private BigDecimal defaultImporto(BigDecimal value) {
		return value != null ? value : BigDecimal.ZERO;
	}

	private void checkAttivazioneGiaEffettuata() {
		Boolean flagContabilizzaGenPccAttuale = documentoDad.findFlagContabilizzaGenPccDocumento(doc);
		if(Boolean.TRUE.equals(flagContabilizzaGenPccAttuale)){
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("l'attivazione delle registrazioni contabili per questo documento [uid: "+doc.getUid()+"] e' gia' stata effettuata."));
		}
	}

	private boolean impostaFlagCondizioniAttivazioneSoddisfatte(boolean condizioniAttivazioneGENSoddisfatte, boolean condizioniAttivazionePCCSoddisfatte) {
		
		boolean flagAttivaGen = Boolean.TRUE.equals(doc.getTipoDocumento().getFlagAttivaGEN());
		boolean flagComunicaPCC = Boolean.TRUE.equals(doc.getTipoDocumento().getFlagComunicaPCC());
		if(flagAttivaGen && flagComunicaPCC){
			return condizioniAttivazioneGENSoddisfatte && condizioniAttivazionePCCSoddisfatte;
		}
		if(flagAttivaGen){
			return condizioniAttivazioneGENSoddisfatte;
		}
		if(flagComunicaPCC){
			return condizioniAttivazionePCCSoddisfatte;
		}
		return false;
	}

	private AggiornaStatoDocumentoDiSpesaResponse aggiornaStatoDocumento() {
		AggiornaStatoDocumentoDiSpesa reqASDS = new AggiornaStatoDocumentoDiSpesa();
		reqASDS.setRichiedente(req.getRichiedente());
		reqASDS.setDocumentoSpesa(doc);
		reqASDS.setBilancio(req.getBilancio());
		//SIAC-5333
		reqASDS.setSaltaInserimentoPrimaNota(req.getSaltaInserimentoPrimaNota());
		AggiornaStatoDocumentoDiSpesaResponse resASDS = serviceExecutor.executeServiceSuccess(AggiornaStatoDocumentoDiSpesaService.class, reqASDS);
		return resASDS;
	}
	
	private void checkStatoDocumento() {
		StatoOperativoDocumento stato = documentoDad.findStatoOperativoDocumento(doc.getUid());
		if(StatoOperativoDocumento.INCOMPLETO.equals(stato) || StatoOperativoDocumento.ANNULLATO.equals(stato) || StatoOperativoDocumento.EMESSO.equals(stato)){
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("il documento [uid: "+doc.getUid()+"] e' in stato " + stato.getDescrizione()));
		}
		
	}
	

}
