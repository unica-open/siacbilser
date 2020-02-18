/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.primanota.movimento;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.csi.siac.siacbilser.business.service.base.ServiceExecutor;
import it.csi.siac.siacbilser.business.service.documento.DocumentoServiceCallGroup;
import it.csi.siac.siacbilser.business.service.primanota.InseriscePrimaNotaAutomaticaService.MovimentoDettaglioConContoTipoOperazione;
import it.csi.siac.siacbilser.business.utility.BilUtilities;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDEventoEnum;
import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoIva;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;

/**
 * SubdocumentoMovimentoHandler.
 * 
 * 
 */
public abstract class SubdocumentoSubdocumentoMovimentoHandler<D extends Documento<S, SI>, S extends Subdocumento<D, SI>, SI extends SubdocumentoIva<D, S, SI>> extends MovimentoHandler<S> {
//<SD extends Subdocumento<?,?>> extends MovimentoHandler<SD> {
	protected LogUtil log = new LogUtil(this.getClass());
	
	protected DocumentoServiceCallGroup documentoServiceCallGroup;
	
	private Map<Integer, BigDecimal[]> imponibiliEImposteDaImpostarePerSubdocumento;
	

	public SubdocumentoSubdocumentoMovimentoHandler(ServiceExecutor serviceExecutor, Richiedente richiedente, Ente ente, Bilancio bilancio) {
		super(serviceExecutor, richiedente, ente, bilancio);
		this.documentoServiceCallGroup = new DocumentoServiceCallGroup(serviceExecutor, richiedente, ente, bilancio);
	}

	

	@Override
	public void inizializzaDatiMovimenti(List<RegistrazioneMovFin> registrazioniMovFin) {
//		inizializzaImponibileEImposta(registrazioniMovFin);
		
	}


//	private /*<D extends Documento<S, SI>, SI extends SubdocumentoIva<D, S, SI>, S extends Subdocumento<D, SI>>*/ void inizializzaImponibileEImposta(List<RegistrazioneMovFin> registrazioniMovFin) {
//		Entita movimento = registrazioniMovFin.get(0).getMovimentoCollegato();
//		@SuppressWarnings("unchecked")
////		S subdocumento = (S)movimento;
////		imponibiliEImposteDaImpostarePerSubdocumento = calcolaImponibiliEImposteDaImpostare(subdocumento.getDocumento());
//	}
	
	/**
	 * Calcola imponibili e imposte da impostare per subdocumento.
	 *
	 * @param documento the documento
	 * @return the map 
	 */
	private /*<D extends Documento<S, SI>, SI extends SubdocumentoIva<D, S, SI>, S extends Subdocumento<D, SI>>*/ Map<Integer, BigDecimal[]> calcolaImponibiliEImposteDaImpostare(D documento, List<SI> listaSubdocumentoIvaNota) {
		String methodName = "calcolaImponibiliEImposteDaImpostare";
		
		Map<Integer, BigDecimal[]> res = new HashMap<Integer, BigDecimal[]>();
		
		//Inizializzo gli importi per ogni subdocumento.
		for(S subdocumento : documento.getListaSubdocumenti()) {
			res.put(subdocumento.getUid(), new BigDecimal[] { subdocumento.getImportoDaDedurreNotNull(),subdocumento.getImportoDaDedurreNotNull()});
		}
		
		BigDecimal totaleImponibileIva = BigDecimal.ZERO;
		BigDecimal totaleImpostaIva = BigDecimal.ZERO;
		BigDecimal totaleQuoteRilevantiIva = BigDecimal.ZERO;
		BigDecimal totaleImponibileIvaImpostato = BigDecimal.ZERO;
		BigDecimal totaleImpostaIvaImpostato = BigDecimal.ZERO;
		
		for(SubdocumentoIva<?,?,?> si : listaSubdocumentoIvaNota) {
			totaleImponibileIva = totaleImponibileIva.add(si.getTotaleImponibileMovimentiIva().abs());
			totaleImpostaIva = totaleImpostaIva.add(si.getTotaleImpostaMovimentiIva().abs());
		}
		log.debug(methodName, "Totale dati iva sul documento: totale imponibile = " + totaleImponibileIva + " --- totale imposta = " + totaleImpostaIva);
		
		// Calcolo totale note rilevanti iva
		int uidUltimaQuotaRilevanteIva = 0;
		for(S subdocumento : documento.getListaSubdocumenti()) {
			if(Boolean.TRUE.equals(subdocumento.getFlagRilevanteIVA())) {
				uidUltimaQuotaRilevanteIva = subdocumento.getUid();
				totaleQuoteRilevantiIva = totaleQuoteRilevantiIva.add(subdocumento.getImportoDaDedurreNotNull());
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
				BigDecimal imponibile = subdocumento.getImportoDaDedurreNotNull().divide(totaleQuoteRilevantiIva, MathContext.DECIMAL128).multiply(totaleImponibileIva).setScale(BilUtilities.MATH_CONTEXT_TWO_HALF_DOWN.getPrecision(), BilUtilities.MATH_CONTEXT_TWO_HALF_DOWN.getRoundingMode());
				BigDecimal imposta = subdocumento.getImportoDaDedurreNotNull().divide(totaleQuoteRilevantiIva, MathContext.DECIMAL128).multiply(totaleImpostaIva).setScale(BilUtilities.MATH_CONTEXT_TWO_HALF_DOWN.getPrecision(), BilUtilities.MATH_CONTEXT_TWO_HALF_DOWN.getRoundingMode());
				
				totaleImponibileIvaImpostato = totaleImponibileIvaImpostato.add(imponibile);
				totaleImpostaIvaImpostato = totaleImpostaIvaImpostato.add(imposta);
				
				log.debug(methodName, "Subdocumento<?,?> " + subdocumento.getNumero() + " --- imponibile da impostare = " + imponibile + " --- imposta da impostare = " + imposta
						+ " --- incrementale imponibile = " + totaleImponibileIvaImpostato + " --- incrementale imposta = " + totaleImpostaIvaImpostato);
				
				res.put(subdocumento.getUid(), new BigDecimal[]{imponibile, imposta});
			}
		}
		
		
		impostaImponibileImpostaRimanenteDelSudocumentoFatturaSuNota(res, totaleImponibileIva, totaleImpostaIva, totaleImponibileIvaImpostato, totaleImpostaIvaImpostato, uidUltimaQuotaRilevanteIva);
		
		
		return res;
		
	}



	/**
	 * Analizza quanto la somma degli importi delle quote con flag rilevante iva = si e gli importi fino ad adesso impostati nella movimentazione su generale. 
	 * Se vi sono delle discrepanze, quanto rimane viene aggiunto sull'ultima quota della nota di credito.
	 * 
	 * <br/><i>Esempio</i> <br/>
	 * Si consideri una fattura di entrata (<i>2019-FTV-A</i>) con una solo quota. Tale quota ha importo = 24,40, imponibile 20,00, imposta 4,40 e flagRilevanteIva = true.
	 * Si consideri inoltre una nota di credito (<i>2019-NCV-B</i>) con una sola quota di importo 12,20 collegata alla fattura attiva. 
	 * L'importo da dedurre sulla quota della fattura sar&agrave; pertanto ancvh'esso 12,20.
	 * 
	 * Secondo i calcoli che precedenti, alla quota della <i>2019-NCV-B</i> presenta:
	 * <ul>
	 * 		<li> imponibile = importoDaDedurreSubdocumento2019-FTV-A * totaleImponibileIva /totaleQuoteRilevantiIva  <i> 12,20*20/24,40 = <b>12,20</b></i> </li>
	 * 		<li> imposta = importoDaDedurreSubdocumento2019-FTV-A * totaleImpostaIva /totaleQuoteRilevantiIva <i> 12,20*4,40/24,40 = <b>2,20<b></i> </li> 
	 * </ul>
	 * 
	 * Il subdocumento della fattura 2019-FTV-A ha per&ograve; un imponibile di 20,00, maggiore dell'imponibile associato tramite il calcolo precedente alla nota di credito (12,20).
	 * Mancano infatti 7,80 euro che aggiungo al'imponibile ad una quota della nota di credito. 
	 * 
	 * <br> <b>NB: tutti gli importi di cui sopra sono importi a partire dai quali impostare ui conti in dare/avere. 
	 * Su base dati entrambe le quote mantengono il loro importi.</b>
	 * 
	 * @param listaWrapper the lista wrapper
	 * @param totaleImponibileIva the totale imponibile iva
	 * @param totaleImpostaIva the totale imposta iva
	 * @param totaleImponibileIvaImpostato the totale imponibile iva impostato
	 * @param totaleImpostaIvaImpostato the totale imposta iva impostato
	 * @param indiceUltimaQuotaRilevanteIva the indice ultima quota rilevante iva
	 */
	private void impostaImponibileImpostaRimanenteDelSudocumentoFatturaSuNota(Map<Integer, BigDecimal[]> res, BigDecimal totaleImponibileIva, BigDecimal totaleImpostaIva,
			BigDecimal totaleImponibileIvaImpostato, BigDecimal totaleImpostaIvaImpostato,int uidUltimaQuotaRilevanteIva) {
		final String methodName ="impostaImponibileImpostaRimanenteDelSudocumentoFatturaSuNota";
		/*comportamento rivisto in vase alla SIAC-6948,. mantengo il codice commentato perchè ci sono dubbi su quale sia la soluzione corretta.
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
		}*/
	}

	@Override
	public void impostaImportiListaMovimentiDettaglio(MovimentoEP movimentoEP) {
		impostaImportiScrittureSubdocumento(movimentoEP);
	}
	
	
	
	private /*<D extends Documento<S, SI>, S extends Subdocumento<D, SI>, SI extends SubdocumentoIva<D, S, SI>>*/ void impostaImportiScrittureSubdocumento(MovimentoEP movimentoEP) {
		String methodName = "impostaImportiScrittureSubdocumento";
		Entita movimentoCollegato = movimentoEP.getRegistrazioneMovFin().getMovimentoCollegato();
		Entita movimento = movimentoEP.getRegistrazioneMovFin().getMovimento();
		
		@SuppressWarnings("unchecked")
		S subdocumento = (S)movimentoCollegato;
		@SuppressWarnings("unchecked")
		S subdocumentoNotaCredito = (S)movimento;
		D notaCredito = subdocumentoNotaCredito.getDocumento();
		
		//in questo caso non ho iva, le scritture saranno come quelle del documento collegato ma con gli importi da dedurre
		SiacDEventoEnum siacDEventoEnum = SiacDEventoEnum.byEventoCodice(movimentoEP.getRegistrazioneMovFin().getEvento().getCodice());
		if(!siacDEventoEnum.isIva() /*!Boolean.TRUE.equals(subdocumento.getFlagRilevanteIVA())*/) {  //CASO 1 e 2
			log.debug(methodName, "il subdocumento: " + subdocumento.getUid() + "NON  ha subdocumento iva e non e' rilevante iva");
			// Copio semplicemente i valori
//			imponibiliEImposteDaImpostarePerSubdocumento = calcolaImponibiliEImposteDaImpostare(subdocumento.getDocumento(), new ArrayList<SI>(), true);
			calcolaDatiScritturaNoIvaNoRilevante(movimentoEP, subdocumento);
			return;
		}
		
		//CASO 6
		if(subdocumento.getSubdocumentoIva() != null && subdocumento.getSubdocumentoIva().getListaNoteDiCredito() != null && !subdocumento.getSubdocumentoIva().getListaNoteDiCredito().isEmpty()){
			log.debug(methodName, "il subdocumento: " + subdocumento.getUid() + " ha subdocumento iva con uid: " + subdocumento.getSubdocumentoIva().getUid() + " e nota credito iva");
			
			SubdocumentoIva<?, ?, ?> notaCreditoIva = subdocumento.getSubdocumentoIva().getListaNoteDiCredito().get(0);
			calcolaDatiScrittureIva(movimentoEP, notaCreditoIva);
			return;
		}
		
		List<SI> listaIvaNota = new ArrayList<SI>();
		if(subdocumentoNotaCredito.getSubdocumentoIva() != null){// CASO 3 - 4
			log.debug(methodName, " la quota della nota credito ha i dati iva ");
			listaIvaNota.add(subdocumentoNotaCredito.getSubdocumentoIva());
//			imponibiliEImposteDaImpostarePerSubdocumento = calcolaImponibiliEImposteDaImpostare(subdocumento.getDocumento(), Arrays.asList(subdocumentoNotaCredito.getSubdocumentoIva()));
//			calcolaDatiScritturaNoIvaRilevante(movimentoEP, subdocumento);
		}else if(notaCredito.getListaSubdocumentoIva()!= null && !notaCredito.getListaSubdocumentoIva().isEmpty()){
			log.debug(methodName, " la nota credito ha i dati iva ");
			listaIvaNota.addAll(notaCredito.getListaSubdocumentoIva());
		}else if (subdocumento.getDocumento().getListaSubdocumentoIva() != null && !subdocumento.getDocumento().getListaSubdocumentoIva().isEmpty()){ //CASO 5 
			log.debug(methodName, " note credito iva - situazione 3");
			listaIvaNota.addAll(subdocumento.getDocumento().getListaSubdocumentoIva().get(0).getListaNoteDiCredito());
//			List<SI> list = subdocumento.getDocumento().getListaSubdocumentoIva().get(0).getListaNoteDiCredito();
//			imponibiliEImposteDaImpostarePerSubdocumento = calcolaImponibiliEImposteDaImpostare(subdocumento.getDocumento(), list);
//			calcolaDatiScritturaNoIvaRilevante(movimentoEP, subdocumento);
		}
		imponibiliEImposteDaImpostarePerSubdocumento = calcolaImponibiliEImposteDaImpostare(subdocumento.getDocumento(), listaIvaNota);
		calcolaDatiScritturaNoIvaRilevante(movimentoEP, subdocumento);
		
	}


	private void calcolaDatiScrittureIva(MovimentoEP movimentoEP, SubdocumentoIva<?,?,?> subdocumentoIva) {
		
		String methodName = "calcolaDatiScrittureIva";
		
		BigDecimal imponibile = subdocumentoIva.getTotaleImponibileMovimentiIva().abs();
		BigDecimal imposta = subdocumentoIva.getTotaleImpostaMovimentiIva().abs();
		// Se non corrispondono al totale della quota, lascio perdere
		
		log.debug(methodName, "imponibile del subdoc iva " + subdocumentoIva.getUid() + " : " + imponibile);
		log.debug(methodName, "imposta del subdoc iva" + subdocumentoIva.getUid() + " : " + imposta);
				
		for(MovimentoDettaglio movimentoDettaglio : movimentoEP.getListaMovimentoDettaglio()) {
			setImportoInElementoScrittura(imponibile, imposta, (MovimentoDettaglioConContoTipoOperazione) movimentoDettaglio);
		}
	}
	
	protected abstract void setImportoInElementoScrittura(BigDecimal imponibile, BigDecimal imposta, MovimentoDettaglioConContoTipoOperazione movimentoDettaglio);
	
	
	private /*<D extends Documento<S, SI>, S extends Subdocumento<D, SI>, SI extends SubdocumentoIva<D, S, SI>>*/ void calcolaDatiScritturaNoIvaNoRilevante(MovimentoEP movimentoEP, S subdocumento) {
		
		for(MovimentoDettaglio movimentoDettaglioConContoTipoOperazione : movimentoEP.getListaMovimentoDettaglio()) {
			// Imposto l'imponibile come importo
//			BigDecimal[] imponibileEImposta = imponibiliEImposteDaImpostarePerSubdocumento.get(subdocumento.getUid());
//			if(imponibileEImposta==null){
//				continue;
//			}
			movimentoDettaglioConContoTipoOperazione.setImporto(subdocumento.getImportoDaDedurreNotNull());
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
			
			setImportoInElementoScrittura(imponibileEImposta[0], imponibileEImposta[1], (MovimentoDettaglioConContoTipoOperazione)movimentoDettaglioConContoTipoOperazione);
		}
	}

	@Override
	public String ottieniDescrizionePrimaNota(RegistrazioneMovFin registrazioneMovFin) {
		Entita movimento = registrazioneMovFin.getMovimento();
		Documento<?, ?> documento = ((Subdocumento<?,?>)movimento).getDocumento(); //La prende dal documento associato!
		return String.format("%s %s/%s %s Nota Credito", documento.getTipoDocumento().getCodice(),documento.getAnno(),documento.getNumero(),documento.getDescrizione());
	}

	@Override
	public Soggetto getSoggetto(RegistrazioneMovFin registrazioneMovFin) {
		Entita movimento = registrazioneMovFin.getMovimentoCollegato();
		Documento<?,?> documento = ((Subdocumento<?,?>) movimento).getDocumento(); //La prende dal documento associato!
		return documento.getSoggetto();
	}

	@Override
	public String ottieniDescrizioneMovimentoEP(RegistrazioneMovFin registrazioneMovFin) {
		Entita movimento = registrazioneMovFin.getMovimento();
		Subdocumento<?,?> subdoc = (Subdocumento<?,?>) movimento;
		return "MOVIMENTO ASSOCIATO A NOTA CREDITO "+ subdoc.getDescrizione();
	}
	

	
}
