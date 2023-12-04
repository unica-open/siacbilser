/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.primanota.movimento;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.csi.siac.siacbilser.business.service.base.ServiceExecutor;
import it.csi.siac.siacbilser.business.service.documento.DocumentoServiceCallGroup;
import it.csi.siac.siacbilser.business.service.primanota.InseriscePrimaNotaAutomaticaService.MovimentoDettaglioConContoTipoOperazione;
import it.csi.siac.siacbilser.business.utility.BilUtilities;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDEventoEnum;
import it.csi.siac.siaccommonser.util.log.LogSrvUtil;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoIva;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;

/**
 * SubdocumentoMovimentoHandler.
 * 
 * 
 */
public abstract class SubdocumentoMovimentoHandler<D extends Documento<S, SI>, S extends Subdocumento<D, SI>, SI extends SubdocumentoIva<D, S, SI>> extends MovimentoHandler<S> {
//<SD extends Subdocumento<?,?>> extends MovimentoHandler<SD> {
	protected LogSrvUtil log = new LogSrvUtil(this.getClass());
	
	protected DocumentoServiceCallGroup documentoServiceCallGroup;
	
	private Map<Integer, BigDecimal[]> imponibiliEImposteDaImpostarePerSubdocumento;
	

	public SubdocumentoMovimentoHandler(ServiceExecutor serviceExecutor, Richiedente richiedente, Ente ente, Bilancio bilancio) {
		super(serviceExecutor, richiedente, ente, bilancio);
		this.documentoServiceCallGroup = new DocumentoServiceCallGroup(serviceExecutor, richiedente, ente, bilancio);
	}

	

	@Override
	public void inizializzaDatiMovimenti(List<RegistrazioneMovFin> registrazioniMovFin) {
		inizializzaImponibileEImposta(registrazioniMovFin);
		
	}


	private /*<D extends Documento<S, SI>, SI extends SubdocumentoIva<D, S, SI>, S extends Subdocumento<D, SI>>*/ void inizializzaImponibileEImposta(List<RegistrazioneMovFin> registrazioniMovFin) {
		Entita movimento = registrazioniMovFin.get(0).getMovimento();
		@SuppressWarnings("unchecked")
		S subdocumento = (S)movimento;
		imponibiliEImposteDaImpostarePerSubdocumento = calcolaImponibiliEImposteDaImpostare(subdocumento.getDocumento());
	}
	
	/**
	 * Calcola imponibili e imposte da impostare per subdocumento.
	 *
	 * @param documento the documento
	 * @return the map 
	 */
	private /*<D extends Documento<S, SI>, SI extends SubdocumentoIva<D, S, SI>, S extends Subdocumento<D, SI>>*/ Map<Integer, BigDecimal[]> calcolaImponibiliEImposteDaImpostare(D documento) {
		String methodName = "calcolaImponibiliEImposteDaImpostare";
		
		Map<Integer, BigDecimal[]> res = new HashMap<Integer, BigDecimal[]>();
		
		//Inizializzo gli importi per ogni subdocumento.
		for(S subdocumento : documento.getListaSubdocumenti()) {
			res.put(subdocumento.getUid(), new BigDecimal[] { subdocumento.getImportoNotNull(),subdocumento.getImportoNotNull()});
		}
		
		BigDecimal totaleImponibileIva = BigDecimal.ZERO;
		BigDecimal totaleImpostaIva = BigDecimal.ZERO;
		BigDecimal totaleQuoteRilevantiIva = BigDecimal.ZERO;
		BigDecimal totaleImponibileIvaImpostato = BigDecimal.ZERO;
		BigDecimal totaleImpostaIvaImpostato = BigDecimal.ZERO;
		
		
		List<SI> listaSubdocumentoIva = documento.getListaSubdocumentoIva();
		
		for(SubdocumentoIva<?,?,?> si : listaSubdocumentoIva) {
			totaleImponibileIva = totaleImponibileIva.add(si.getTotaleImponibileMovimentiIva().add(si.getTotaleImpostaIndetraibileMovimentiIva()));
			totaleImpostaIva = totaleImpostaIva.add(si.getTotaleImpostaDetraibileMovimentiIva());
		}
		log.debug(methodName, "Totale dati iva sul documento: totale imponibile = " + totaleImponibileIva + " --- totale imposta = " + totaleImpostaIva);
		
		// Calcolo totale note rilevanti iva
		int uidUltimaQuotaRilevanteIva = 0;
		for(S subdocumento : documento.getListaSubdocumenti()) {
			if(Boolean.TRUE.equals(subdocumento.getFlagRilevanteIVA())) {
				uidUltimaQuotaRilevanteIva = subdocumento.getUid();
				totaleQuoteRilevantiIva = totaleQuoteRilevantiIva.add(subdocumento.getImportoNotNull());
			}
		}
		log.debug(methodName, "Totale degli importi per le note rilevanti iva = " + totaleQuoteRilevantiIva);
		
		if(uidUltimaQuotaRilevanteIva == 0) {
			log.debug(methodName, "Nessuna quota rilevante iva per cui impostare i valori");
			return res;
		}
		
		
		// Impostazione dei dati
		for(S subdocumento : documento.getListaSubdocumenti()) {
			if(Boolean.TRUE.equals(subdocumento.getFlagRilevanteIVA())) {
				BigDecimal imponibile = subdocumento.getImportoNotNull().divide(totaleQuoteRilevantiIva, MathContext.DECIMAL128).multiply(totaleImponibileIva).setScale(BilUtilities.MATH_CONTEXT_TWO_HALF_DOWN.getPrecision(), BilUtilities.MATH_CONTEXT_TWO_HALF_DOWN.getRoundingMode());
				BigDecimal imposta = subdocumento.getImportoNotNull().divide(totaleQuoteRilevantiIva, MathContext.DECIMAL128).multiply(totaleImpostaIva).setScale(BilUtilities.MATH_CONTEXT_TWO_HALF_DOWN.getPrecision(), BilUtilities.MATH_CONTEXT_TWO_HALF_DOWN.getRoundingMode());
				
				totaleImponibileIvaImpostato = totaleImponibileIvaImpostato.add(imponibile);
				totaleImpostaIvaImpostato = totaleImpostaIvaImpostato.add(imposta);
				
				log.debug(methodName, "Subdocumento<?,?> " + subdocumento.getNumero() + " --- imponibile da impostare = " + imponibile + " --- imposta da impostare = " + imposta
						+ " --- incrementale imponibile = " + totaleImponibileIvaImpostato + " --- incrementale imposta = " + totaleImpostaIvaImpostato);
				
				res.put(subdocumento.getUid(), new BigDecimal[]{imponibile, imposta});
			}
		}
		
		
		BigDecimal differenzaImponibile = totaleImponibileIva.subtract(totaleImponibileIvaImpostato);
		BigDecimal differenzaImposta = totaleImpostaIva.subtract(totaleImpostaIvaImpostato);
		
		log.debug(methodName, "Totale imponibile = " + totaleImponibileIva + " --- totale imposta = " + totaleImpostaIva
				+ " --- imponibile impostato = " + totaleImponibileIvaImpostato + " --- imposta impostato = " + totaleImpostaIvaImpostato
				+ " --- differenza imponibile = "  + differenzaImponibile + " --- differenza impota = " + differenzaImposta);
		
		if(differenzaImponibile.signum() > 0 || differenzaImposta.signum() > 0) {
			// Devo impostare le differenze
			BigDecimal[] imponibileEImpostaUltimaQuotaRilevanteiva = res.get(uidUltimaQuotaRilevanteIva);
			BigDecimal oldImponibile = imponibileEImpostaUltimaQuotaRilevanteiva[0];
			BigDecimal oldImposta = imponibileEImpostaUltimaQuotaRilevanteiva[1];
			
			BigDecimal newImponibile = oldImponibile.add(differenzaImponibile).setScale(BilUtilities.MATH_CONTEXT_TWO_HALF_DOWN.getPrecision(), BilUtilities.MATH_CONTEXT_TWO_HALF_DOWN.getRoundingMode());
			BigDecimal newImposta = oldImposta.add(differenzaImposta).setScale(BilUtilities.MATH_CONTEXT_TWO_HALF_DOWN.getPrecision(), BilUtilities.MATH_CONTEXT_TWO_HALF_DOWN.getRoundingMode());
			
			res.put(uidUltimaQuotaRilevanteIva, new BigDecimal[]{newImponibile, newImposta});
			
			
			log.debug(methodName, "Subdocumento<?,?> uid:" + uidUltimaQuotaRilevanteIva + " --- vecchio imponibile = " + oldImponibile + " --- vecchia imposta = " + oldImposta
					+ " --- nuovo imponibile = " + newImponibile + " --- nuova imposta = " + newImposta);
		}
		
		
		return res;
		
	}

	@Override
	public void impostaImportiListaMovimentiDettaglio(MovimentoEP movimentoEP) {
		impostaImportiScrittureSubdocumento(movimentoEP);
	}
	
	
	
	private /*<D extends Documento<S, SI>, S extends Subdocumento<D, SI>, SI extends SubdocumentoIva<D, S, SI>>*/ void impostaImportiScrittureSubdocumento(MovimentoEP movimentoEP) {
		
		Entita movimento = movimentoEP.getRegistrazioneMovFin().getMovimento();
		
		@SuppressWarnings("unchecked")
		S subdocumento = (S)movimento;
		
		//registrazione senza iva
		SiacDEventoEnum siacDEventoEnum = SiacDEventoEnum.byEventoCodice(movimentoEP.getRegistrazioneMovFin().getEvento().getCodice());
		if(!siacDEventoEnum.isIva() /*!Boolean.TRUE.equals(subdocumento.getFlagRilevanteIVA())*/) {
			// Copio semplicemente i valori
			calcolaDatiScritturaNoIvaNoRilevante(movimentoEP, subdocumento);
			return;
		}
		
		//Iva su singola quota
		if(subdocumento.getSubdocumentoIva() != null) {
			// Ho i dati iva: effettuo il calcolo
			calcolaDatiScrittureIva(movimentoEP, subdocumento);
			return;
		}
		
		// Iva su intero documento
		calcolaDatiScritturaNoIvaRilevante(movimentoEP, subdocumento);
	}


	private void calcolaDatiScrittureIva(MovimentoEP movimentoEP, Subdocumento<?,?> subdocumento) {
		
		BigDecimal imponibile = subdocumento.getSubdocumentoIva().getTotaleImponibileMovimentiIva();
		BigDecimal impostaIndetraibile = subdocumento.getSubdocumentoIva().getTotaleImpostaIndetraibileMovimentiIva();
		imponibile = imponibile.add(impostaIndetraibile).setScale(BilUtilities.MATH_CONTEXT_TWO_HALF_DOWN.getPrecision(), BilUtilities.MATH_CONTEXT_TWO_HALF_DOWN.getRoundingMode());
		
		BigDecimal imposta = subdocumento.getSubdocumentoIva().getTotaleImpostaDetraibileMovimentiIva();
		// Se non corrispondono al totale della quota, lascio perdere
				
		for(MovimentoDettaglio movimentoDettaglio : movimentoEP.getListaMovimentoDettaglio()) {
			setImportoInElementoScrittura(imponibile, imposta, (MovimentoDettaglioConContoTipoOperazione) movimentoDettaglio, subdocumento.getDocumento().getTipoDocumento());
		}
	}
	
	protected abstract void setImportoInElementoScrittura(BigDecimal imponibile, BigDecimal imposta, MovimentoDettaglioConContoTipoOperazione movimentoDettaglio, TipoDocumento tipoDocumento);
	
	
	private /*<D extends Documento<S, SI>, S extends Subdocumento<D, SI>, SI extends SubdocumentoIva<D, S, SI>>*/ void calcolaDatiScritturaNoIvaNoRilevante(MovimentoEP movimentoEP, S subdocumento) {
		
		for(MovimentoDettaglio movimentoDettaglioConContoTipoOperazione : movimentoEP.getListaMovimentoDettaglio()) {
			// Imposto l'imponibile come importo
//			BigDecimal[] imponibileEImposta = imponibiliEImposteDaImpostarePerSubdocumento.get(subdocumento.getUid());
//			if(imponibileEImposta==null){
//				continue;
//			}
			movimentoDettaglioConContoTipoOperazione.setImporto(subdocumento.getImporto());
		}
	}
	
	
	private /*<D extends Documento<S, SI>, S extends Subdocumento<D, SI>, SI extends SubdocumentoIva<D, S, SI>>*/ void calcolaDatiScritturaNoIvaRilevante(MovimentoEP movimentoEP, 
			//List<MovimentoDettaglioConContoTipoOperazione> listaMovimentoDettaglioConTipoOperazione, 
			S subdocumento){
		
		BigDecimal[] imponibileEImposta = imponibiliEImposteDaImpostarePerSubdocumento.get(subdocumento.getUid());
		if(imponibileEImposta==null){
			return;
		}
		for(MovimentoDettaglio movimentoDettaglioConContoTipoOperazione : movimentoEP.getListaMovimentoDettaglio()) {
			
			setImportoInElementoScrittura(imponibileEImposta[0], imponibileEImposta[1], (MovimentoDettaglioConContoTipoOperazione)movimentoDettaglioConContoTipoOperazione, subdocumento.getDocumento().getTipoDocumento());
		}
	}

	@Override
	public String ottieniDescrizionePrimaNota(RegistrazioneMovFin registrazioneMovFin) {
		Entita movimento = registrazioneMovFin.getMovimento();
		Documento<?, ?> documento = ((Subdocumento<?,?>)movimento).getDocumento(); //La prende dal documento associato!
		return String.format("%s %s/%s %s", documento.getTipoDocumento().getCodice(),documento.getAnno(),documento.getNumero(),documento.getDescrizione());
	}

	@Override
	public Soggetto getSoggetto(RegistrazioneMovFin registrazioneMovFin) {
		Entita movimento = registrazioneMovFin.getMovimento();
		Documento<?,?> documento = ((Subdocumento<?,?>) movimento).getDocumento(); //La prende dal documento associato!
		return documento.getSoggetto();
	}

	@Override
	public String ottieniDescrizioneMovimentoEP(RegistrazioneMovFin registrazioneMovFin) {
		Entita movimento = registrazioneMovFin.getMovimento();
		Subdocumento<?,?> subdoc = (Subdocumento<?,?>) movimento;
		return "MOVIMENTO ASSOCIATO A QUOTA DOCUMENTO "+ subdoc.getDescrizione();
	}
	

	
}
