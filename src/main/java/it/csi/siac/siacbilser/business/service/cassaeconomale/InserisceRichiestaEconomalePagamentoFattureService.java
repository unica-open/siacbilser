/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cassaeconomale;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacbilser.business.service.registrocomunicazionipcc.InserisciRegistroComunicazioniPCCService;
import it.csi.siac.siacbilser.integration.dad.RegistroComunicazioniPCCDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoSpesaDad;
import it.csi.siac.siacbilser.integration.dad.TipoGiustificativoDad;
import it.csi.siac.siacbilser.integration.dad.ValutaDad;
import it.csi.siac.siacbilser.model.StatoOperativoMovimentoGestione;
import it.csi.siac.siaccecser.frontend.webservice.msg.InserisceRichiestaEconomale;
import it.csi.siac.siaccecser.model.Giustificativo;
import it.csi.siac.siaccecser.model.ModalitaPagamentoCassa;
import it.csi.siac.siaccecser.model.ModalitaPagamentoDipendente;
import it.csi.siac.siaccecser.model.Movimento;
import it.csi.siac.siaccecser.model.RichiestaEconomale;
import it.csi.siac.siaccecser.model.StatoOperativoRichiestaEconomale;
import it.csi.siac.siaccecser.model.TipoDiCassa;
import it.csi.siac.siaccecser.model.TipoGiustificativo;
import it.csi.siac.siaccecser.model.TipologiaGiustificativo;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.errore.TipoErrore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisciRegistroComunicazioniPCC;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisciRegistroComunicazioniPCCResponse;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.RegistroComunicazioniPCC;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesaModelDetail;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfin2ser.model.TipoOperazionePCC;
import it.csi.siac.siacfin2ser.model.Valuta;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.frontend.webservice.SoggettoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiaveResponse;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.codifiche.ModalitaAccreditoSoggetto;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSoggettoK;
import it.csi.siac.siacfinser.model.ric.SorgenteDatiSoggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceRichiestaEconomalePagamentoFattureService extends InserisceRichiestaEconomaleService {
	
	@Autowired 
	private SubdocumentoDad subdocumentoDad;
	@Autowired
	private SubdocumentoSpesaDad subdocumentoSpesaDad;
	@Autowired
	private RegistroComunicazioniPCCDad registroComunicazioniPCCDad;
	@Autowired
	private TipoGiustificativoDad tipoGiustificativoDad;
	@Autowired
	private ValutaDad valutaDad;
	@Autowired
	private SoggettoService soggettoService;
	
	private Soggetto soggettoCassa;
	private BigDecimal importoTotaleSplitReverse;
	private TipoDocumento tipoDocumento;
	
	
	@Override
	protected void checkServiceParamRichiestaEconomale() throws ServiceParamError{
		checkCondition(StringUtils.isNotBlank(richiestaEconomale.getDescrizioneDellaRichiesta()), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("descrizione della spesa"), false);
		
		// Se la lista non e' presente, blocco l'elaborazione
		checkNotNull(richiestaEconomale.getSubdocumenti(), "quote");
		checkCondition(!richiestaEconomale.getSubdocumenti().isEmpty(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("quote"), false);
		int idx = 1;
		for(SubdocumentoSpesa ss : richiestaEconomale.getSubdocumenti()) {
			checkEntita(ss, "quota " + (idx++), false);
		}
	}
	
	@Override
	protected void init() {
		codiceTipoRichiesta = "PAGAMENTO_FATTURE";
		cassaEconomaleDad.setEnte(ente);
		tipoGiustificativoDad.setEnte(ente);
		valutaDad.setEnte(ente);
		registroComunicazioniPCCDad.setEnte(ente);
		super.init();
	}
	
	@Override
	protected void preInserisceRichiestaEconomale() {
	
		BigDecimal importo = subdocumentoDad.getImportoSubdocumenti(richiestaEconomale.getSubdocumenti());
		importoTotaleSplitReverse = subdocumentoDad.getImportoSplitReverseSubdocumenti(richiestaEconomale.getSubdocumenti());
		tipoDocumento = subdocumentoDad.findTipoDocumentoBySubdocumento(richiestaEconomale.getSubdocumenti().get(0));
		importo = importo.subtract(importoTotaleSplitReverse);
		
		if (tipoDocumento.isNotaCredito()) {
			importo = importo.negate();
		}
		richiestaEconomale.setImporto(importo);
		checkImpegno();
	}

	@Override
	protected void postInserisceRichiestaEconomale() {
		
		//inserisco una richiesta di pagamento per ogni quota con importo split reverse diverso da zero
		if(importoTotaleSplitReverse.compareTo(BigDecimal.ZERO) != 0){
			caricaSoggettoCassa();
			for(SubdocumentoSpesa ss: richiestaEconomale.getSubdocumenti()){
				inserisciRichiestaPagamento(ss);
			}
		}
		//per tutte le quote imposto il flag pagato in cec e la data di pagamento
		subdocumentoDad.impostaPagamentoCEC(richiestaEconomale.getSubdocumenti(), richiestaEconomale.getMovimento()!=null && richiestaEconomale.getMovimento().getDataMovimento()!=null ? richiestaEconomale.getMovimento().getDataMovimento() : new Date());
		
		//per ogni quota inserisco un record nel registroPCC se il tipo docuemnto ha il flagcomunicaPCC
		gestisciRegistroPCC();
	}
	
	@Override
	protected void determinaStato() {
		if(Boolean.TRUE.equals(datiEconomoPresenti)){
			richiestaEconomale.setStatoOperativoRichiestaEconomale(StatoOperativoRichiestaEconomale.EVASA);
		}else{
			richiestaEconomale.setStatoOperativoRichiestaEconomale(StatoOperativoRichiestaEconomale.PRENOTATA);
		}
	}
	
	@Override
	protected boolean isCondizioneDiAttivazioneGENSoddisfatta() {
		String methodName = "isCondizioneDiAttivazioneGENSoddisfatta";
		log.debug(methodName, "Le richieste di Pagamento Fatture non attivano GEN. Returning false.");
		return false;
	}
	
	@Override
	protected void checkFormaliImpegno(Impegno impegno) {
		
		TipoErrore tipoErroreDefinitivo;
		if(impegnoOSubImpegno instanceof SubImpegno){
			tipoErroreDefinitivo = ErroreFin.SUBIMPEGNO_NON_IN_STATO_DEFINITIVO;
		} else {
			tipoErroreDefinitivo = ErroreFin.IMPEGNO_NON_IN_STATO_DEFINITIVO;
		}
		
		// Stato definitivo
		if(!StatoOperativoMovimentoGestione.DEFINITIVO.getCodice().equals(impegno.getStatoOperativoMovimentoGestioneSpesa())) {
			throw new BusinessException(tipoErroreDefinitivo.getErrore());
		}
	
	}
	
//	----- SOGGETTO
	
	/**
	 * Carica il dettaglio del soggetto legato alla cassa
	 */
	private void caricaSoggettoCassa() {
		Soggetto s = soggettoDad.findSoggettoByCassaEconomale(richiestaEconomale.getCassaEconomale());
		if(s == null || StringUtils.isBlank(s.getCodiceSoggetto())){
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA_SINGOLO_MSG.getErrore("Soggetto associato alla cassa economale"));
		}
		s = ricercaSoggettoPerChiave(s.getCodiceSoggetto());
		soggettoCassa = s;
	}

	/**
	 * Richiamo al servizio ricercaSoggettoPerChiave
	 * 
	 * @param codice codice del soggetto
	 * @return il soggetto trovato
	 */
	private Soggetto ricercaSoggettoPerChiave(String codice) {
		RicercaSoggettoPerChiave reqRSK = new RicercaSoggettoPerChiave();
		ParametroRicercaSoggettoK parametroSoggettoK = new ParametroRicercaSoggettoK();
		parametroSoggettoK.setCodice(codice);
		reqRSK.setEnte(ente);
		reqRSK.setRichiedente(req.getRichiedente());
		reqRSK.setParametroSoggettoK(parametroSoggettoK);
		reqRSK.setDataOra(req.getDataOra());
		reqRSK.setSorgenteDatiSoggetto(SorgenteDatiSoggetto.SIAC);
//		RicercaSoggettoPerChiaveResponse resRSK = serviceExecutor.executeServiceSuccess(RicercaSoggettoPerChiaveService.class, reqRSK);
		RicercaSoggettoPerChiaveResponse resRSK = soggettoService.ricercaSoggettoPerChiave(reqRSK);
		return resRSK.getSoggetto();
	}
	
	
//	----- RICHIESTA PAGAMENTO
	
	/**
	 * Richiamo al servizio InserisceRichiestaEconomalePagamentoService
	 * 
	 * @param ss quota per cui inserire la richiesta di pagamento
	 */
	private void inserisciRichiestaPagamento(SubdocumentoSpesa ss) {
		BigDecimal importoSplitReverese = subdocumentoDad.getImportoSplitReverseSubdocumento(ss);
		if(importoSplitReverese == null || importoSplitReverese.compareTo(BigDecimal.ZERO) == 0){
			//inserisco una richiesta di pagamento solo per le quote che hanno importo splitReverse
			return;
		}
		SubdocumentoSpesa subdoc = subdocumentoSpesaDad.findSubdocumentoSpesaById(ss.getUid(), SubdocumentoSpesaModelDetail.ImpegnoSubimpegno);
		InserisceRichiestaEconomale reqPAG = new InserisceRichiestaEconomale();
		reqPAG.setRichiedente(req.getRichiedente());
		reqPAG.setRichiestaEconomale(popolaRichiestaEconomale(subdoc));
		serviceExecutor.executeServiceSuccess(InserisceRichiestaEconomalePagamentoService.class, reqPAG);
	}
	
	/**
	 *  Popolamento della richiesta pagamento da inserire
	 *  
	 * @param ss quota per cui inserire la richiesta di pagamento
	 * @return la richiesta da inserire
	 */
	private RichiestaEconomale popolaRichiestaEconomale(SubdocumentoSpesa ss) {
		RichiestaEconomale richiestaPagamento = new RichiestaEconomale();
		richiestaPagamento.setSoggetto(soggettoCassa);
		richiestaPagamento.setCodiceBeneficiario(soggettoCassa.getCodiceSoggetto());
		richiestaPagamento.setMatricola(soggettoCassa.getMatricola());
		richiestaPagamento.setNome(soggettoCassa.getNome());
		richiestaPagamento.setCognome(soggettoCassa.getCognome());
		richiestaPagamento.setCodiceFiscale(soggettoCassa.getCodiceFiscale());
		richiestaPagamento.setDescrizioneDellaRichiesta("Pagamento ritenuta di fattura di cassa economale " + ss.getDocumento().getAnno() + " - " + ss.getDocumento().getNumero() + " - " + ss.getNumero());
		richiestaPagamento.setImpegno(ss.getImpegno());
		richiestaPagamento.setSubImpegno(ss.getSubImpegno());
		richiestaPagamento.setFlagPagamentoRitenutaSuFattura(Boolean.TRUE);
		richiestaPagamento.setBilancio(richiestaEconomale.getBilancio());
		richiestaPagamento.setCassaEconomale(richiestaEconomale.getCassaEconomale());
		richiestaPagamento.setImporto(ss.getImportoSplitReverseNotNull());
		
		List<Giustificativo> giustificativi = new ArrayList<Giustificativo>();
		Giustificativo giustificativo = new Giustificativo();
		giustificativo.setTipoGiustificativo(determinaTipoGiustificativo());
		giustificativo.setImportoGiustificativo(ss.getImportoSplitReverseNotNull());
		giustificativo.setValuta(determinaValuta());
		giustificativo.setDataEmissione(new Date());
		giustificativi.add(giustificativo);
		richiestaPagamento.setGiustificativi(giustificativi);
		
		List<SubdocumentoSpesa> subdocumenti = new ArrayList<SubdocumentoSpesa>();
		subdocumenti.add(ss);
		richiestaPagamento.setSubdocumenti(subdocumenti);
		
		Movimento movimento = new Movimento();
		impostaModalitaPagamento(movimento);
		movimento.setDataMovimento(richiestaEconomale.getMovimento().getDataMovimento());
		richiestaPagamento.setMovimento(movimento);
		
		return richiestaPagamento;
	}

	private Valuta determinaValuta() {
		List<Valuta> valutas = valutaDad.findByCodice("EUR");
		if(valutas == null || valutas.isEmpty()) {
			throw new BusinessException("Impossibile reperire una valuta di codice EUR");
		}
		
		return valutas.get(0);
	}

	private TipoGiustificativo determinaTipoGiustificativo() {
		TipoGiustificativo tipoGiustificativo = new TipoGiustificativo();
		tipoGiustificativo.setCassaEconomale(richiestaEconomale.getCassaEconomale());
		tipoGiustificativo.setTipologiaGiustificativo(TipologiaGiustificativo.PAGAMENTO);
		tipoGiustificativo.setCodice("RIT_SPLIT_REVERSE");
		List<TipoGiustificativo> result = tipoGiustificativoDad.findTipoGiustificativoByCodiceETipologia(tipoGiustificativo);
		if(result == null || result.isEmpty()){
			throw new BusinessException(" Impossibile reperire un tipo giustificativo di tipo RIT_SPLIT_REVERSE");
		}
		return result.get(0);
	}

	private void impostaModalitaPagamento(Movimento movimento) {
		TipoDiCassa tipoCassa = richiestaEconomale.getCassaEconomale().getTipoDiCassa();
		ModalitaPagamentoSoggetto modalitaPagamentoSoggetto = null;
		ModalitaPagamentoCassa modalitaPagamentoCassa = null;
		
		if(TipoDiCassa.CONTANTI.equals(tipoCassa)){
			modalitaPagamentoCassa = trovaModalitaPagamentoCassaByTipoCassa(TipoDiCassa.CONTANTI);
			modalitaPagamentoSoggetto = trovaPrimaModalitaContanti();
			
		}else{
			modalitaPagamentoCassa = trovaModalitaPagamentoCassaByTipoCassa(TipoDiCassa.CONTO_CORRENTE_BANCARIO);
			modalitaPagamentoSoggetto = trovaPrimaModalitaContoCorrente();
			movimento.setDettaglioPagamento(modalitaPagamentoSoggetto.getIban());
		}

		ModalitaPagamentoDipendente modalitaPagamentoDipendente = trovaModalitaPagamentoDipendenteByModalitaAccredito(modalitaPagamentoSoggetto.getModalitaAccreditoSoggetto());
		movimento.setModalitaPagamentoCassa(modalitaPagamentoCassa);
		movimento.setModalitaPagamentoSoggetto(modalitaPagamentoSoggetto);
		movimento.setModalitaPagamentoDipendente(modalitaPagamentoDipendente);
		
	}

	private ModalitaPagamentoDipendente trovaModalitaPagamentoDipendenteByModalitaAccredito(ModalitaAccreditoSoggetto modalitaAccreditoSoggetto) {
		ModalitaPagamentoDipendente modalitaPagamentoDipendente = cassaEconomaleDad.findModalitaPagamentoDipendenteByModalitaAccredito(modalitaAccreditoSoggetto);
		return modalitaPagamentoDipendente;
	}

	private ModalitaPagamentoCassa trovaModalitaPagamentoCassaByTipoCassa(TipoDiCassa tipoCassa) {
		return cassaEconomaleDad.findModalitaPagamentoCassaByCodice(tipoCassa.getCodice());
	}

	private ModalitaPagamentoSoggetto trovaPrimaModalitaContanti() {
		List<ModalitaPagamentoSoggetto> listaModalitaPagamento = soggettoCassa.getModalitaPagamentoList();
		for(ModalitaPagamentoSoggetto mps : listaModalitaPagamento){
			if("CON".equals(mps.getModalitaAccreditoSoggetto().getCodice())){
				return mps;
			}
		}
		throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("il soggetto associato alla cassa economale non ha modalita' di pagamento di tipo CONTANTI"));
	}
	
	private ModalitaPagamentoSoggetto trovaPrimaModalitaContoCorrente() {
		List<ModalitaPagamentoSoggetto> listaModalitaPagamento = soggettoCassa.getModalitaPagamentoList();
		for(ModalitaPagamentoSoggetto mps : listaModalitaPagamento){
			if("CB".equals(mps.getModalitaAccreditoSoggetto().getCodice()) || "CCB".equals(mps.getModalitaAccreditoSoggetto().getCodice())){
				return mps;
			}
		}
		throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("il soggetto associato alla cassa economale non ha modalita' di pagamento di tipo CONTO CORRENTE BANCARIO"));
	}

	
//	----- REGISTRO PCC

	/**
	 * Richiama il metodo per l'inserimento nel registro PCC solo se il flag comunicaPCC e' TRUE
	 * 
	 */
	private void gestisciRegistroPCC(){
		if(!Boolean.TRUE.equals(tipoDocumento.getFlagComunicaPCC())){
			return;
		}
		for(SubdocumentoSpesa s : richiestaEconomale.getSubdocumenti()){
			inserisciRegistroPCC(s);
		}
	}

	/**
	 * Inserisce una registrazione PCC per la quota passata come parametro
	 * 
	 * @param subdoc la quota per cui inserire la registrazione PCC
	 */
	private void inserisciRegistroPCC(SubdocumentoSpesa subdoc) {
		
		RegistroComunicazioniPCC registroComunicazioniPCC = new RegistroComunicazioniPCC();
		
		// Imposto il minimo dei dati
		DocumentoSpesa documentoSpesa = subdocumentoSpesaDad.findDocumentoByIdSubdocumentoSpesa(subdoc.getUid());
		
		SubdocumentoSpesa subdocumentoSpesa = new SubdocumentoSpesa();
		subdocumentoSpesa.setUid(subdoc.getUid());
		
		TipoOperazionePCC tipoOperazionePCC = registroComunicazioniPCCDad.findTipoOperazionePCCByValue(TipoOperazionePCC.Value.ComunicazionePagamento);
		
		registroComunicazioniPCC.setEnte(ente);
		registroComunicazioniPCC.setDocumentoSpesa(documentoSpesa);
		registroComunicazioniPCC.setSubdocumentoSpesa(subdocumentoSpesa);
		registroComunicazioniPCC.setTipoOperazionePCC(tipoOperazionePCC);
		registroComunicazioniPCC.setDataEmissioneOrdinativo(new Date());
		registroComunicazioniPCC.setNumeroOrdinativo(richiestaEconomale.getMovimento().getNumeroMovimento());
		BigDecimal importo = subdocumentoDad.getImportoSubdocumento(subdocumentoSpesa);
		BigDecimal importoDaDedurre = subdocumentoDad.getImportoDaDedurreSubdocumento(subdocumentoSpesa);
		registroComunicazioniPCC.setImportoQuietanza(importo.subtract(importoDaDedurre));
		
		InserisciRegistroComunicazioniPCC request = new InserisciRegistroComunicazioniPCC();
		request.setRichiedente(req.getRichiedente());
		request.setRegistroComunicazioniPCC(registroComunicazioniPCC);
		
		InserisciRegistroComunicazioniPCCResponse response = serviceExecutor.executeServiceSuccess(InserisciRegistroComunicazioniPCCService.class, request);
		subdoc.getListaRegistriComunicazioniPCC().add(response.getRegistroComunicazioniPCC());
		
	}

	
}
