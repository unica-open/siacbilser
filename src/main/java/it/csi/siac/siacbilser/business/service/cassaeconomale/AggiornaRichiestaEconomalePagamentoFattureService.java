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

import it.csi.siac.siacbilser.integration.dad.CassaEconomaleDad;
import it.csi.siac.siacbilser.integration.dad.SoggettoDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoSpesaDad;
import it.csi.siac.siacbilser.integration.dad.TipoGiustificativoDad;
import it.csi.siac.siacbilser.integration.dad.ValutaDad;
import it.csi.siac.siaccecser.frontend.webservice.msg.AggiornaRichiestaEconomale;
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
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesaModelDetail;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfin2ser.model.Valuta;
import it.csi.siac.siacfinser.frontend.webservice.SoggettoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiaveResponse;
import it.csi.siac.siacfinser.model.codifiche.ModalitaAccreditoSoggetto;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSoggettoK;
import it.csi.siac.siacfinser.model.ric.SorgenteDatiSoggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaRichiestaEconomalePagamentoFattureService extends AggiornaRichiestaEconomaleService {
	
	@Autowired
	private SubdocumentoDad subdocumentoDad;
	@Autowired
	private SubdocumentoSpesaDad subdocumentoSpesaDad;
	@Autowired
	private ValutaDad valutaDad;
	@Autowired
	private SoggettoDad soggettoDad;
	@Autowired
	private CassaEconomaleDad cassaEconomaleDad;
	@Autowired
	private TipoGiustificativoDad tipoGiustificativoDad;
	@Autowired
	private SoggettoService soggettoService;
	
	private Soggetto soggettoCassa;
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
		super.init();
	}
	
	@Override
	protected void preAggiornamentoRichiestaEconomale() {
		calcolaImportoRichiestaEconomale();
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
	protected void postAggiornamentoRichiestaEconomale() {
		String methodName = "postAggiornamentoRichiestaEconomale";
		
		for(SubdocumentoSpesa s : richiestaEconomale.getSubdocumenti()){
			
			RichiestaEconomale richiestaPagamento = richiestaEconomaleDad.findRichiestePagamentoBySubdocumento(s);
			BigDecimal importoSplitReverse = subdocumentoDad.getImportoSplitReverseSubdocumento(s);
			
			if(importoSplitReverse == null || importoSplitReverse.compareTo(BigDecimal.ZERO) == 0){
				return;
			}
			caricaSoggettoCassa();
			if(richiestaPagamento == null || richiestaPagamento.getUid() == 0){
				log.debug(methodName, "non e' ancora presente una richiesta pagamento per la quota con uid " +s.getUid() + ", la inserisco.");
				inserisciNuovaRichiestaPagamento(s);
			}else{
				log.debug(methodName, "e' gia' presente una richiesta pagamento per la quota con uid " +s.getUid() + ", la aggiorno.");
				aggiornaRichiestaEsistente(richiestaPagamento);
			}
		}
	}

	@Override
	protected boolean isCondizioneDiAttivazioneGENSoddisfatta() {
		String methodName = "isCondizioneDiAttivazioneGENSoddisfatta";
		log.debug(methodName, "Le richieste di Pagamento Fatture non attivano GEN. Returning false.");
		return false;
	}
	

//	--------CALCOLO IMPORTO
	
	private void calcolaImportoRichiestaEconomale() {
		BigDecimal importo = subdocumentoDad.getImportoSubdocumenti(richiestaEconomale.getSubdocumenti());
		BigDecimal importoTotaleSplitReverse = subdocumentoDad.getImportoSplitReverseSubdocumenti(richiestaEconomale.getSubdocumenti());
		tipoDocumento = subdocumentoDad.findTipoDocumentoBySubdocumento(richiestaEconomale.getSubdocumenti().get(0));
		importo = importo.subtract(importoTotaleSplitReverse);
		
		if (tipoDocumento.isNotaCredito()) {
			importo = importo.negate();
		}
		richiestaEconomale.setImporto(importo);
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
//		reqRSK.setCodificaAmbito(Ambito.AMBITO_FIN.getSuffix());
		reqRSK.setEnte(ente);
		reqRSK.setRichiedente(req.getRichiedente());
		reqRSK.setParametroSoggettoK(parametroSoggettoK);
		reqRSK.setDataOra(req.getDataOra());
		reqRSK.setSorgenteDatiSoggetto(SorgenteDatiSoggetto.SIAC);
//		RicercaSoggettoPerChiaveResponse resRSK = serviceExecutor.executeServiceSuccess(RicercaSoggettoPerChiaveService.class, reqRSK);
		RicercaSoggettoPerChiaveResponse resRSK = soggettoService.ricercaSoggettoPerChiave(reqRSK);
		return resRSK.getSoggetto();
	}
	
	
//	--------AGGIORNAMENTO RICHIESTE
	/**
	 * Richiamo al servizio AggiornaRichiestaEconomalePagamentoService
	 * 
	 * @param richiestaPagamento richiesta di pagamento da aggiornare
	 */
	private void aggiornaRichiestaEsistente(RichiestaEconomale richiestaPagamento) {
		String methodName = "aggiornaRichiestaEsistente";
		//gli unici dati che possono essre cambiati sono quelli relativi al soggetto della cassa. Gli altri dati dipendono dalla quota che, essendo gia' pagata in cec, non e' più modificabile
		//Se non è cambiato il soggetto, non aggiorno nulla
		if(soggettoCassa.getUid() == richiestaPagamento.getSoggetto().getUid()){
			log.debug(methodName, "Il soggetto della cassa non e' cambiato, niente da aggiornare.");
			return;
		}
		log.debug(methodName, "Il soggetto della cassa e' cambiato, aggiorno la richiesta.");
		AggiornaRichiestaEconomale reqPAG = new AggiornaRichiestaEconomale();
		popolaRichiestaEconomaleAggiornamento(richiestaPagamento);
		reqPAG.setRichiedente(req.getRichiedente());
		reqPAG.setRichiestaEconomale(richiestaPagamento);
		
		serviceExecutor.executeServiceSuccess(AggiornaRichiestaEconomalePagamentoService.class, reqPAG);
		
	}
	
	
	private void popolaRichiestaEconomaleAggiornamento(RichiestaEconomale richiestaPagamento) {
		
		Movimento movimento = richiestaPagamento.getMovimento();
		impostaModalitaPagamento(movimento);
		richiestaPagamento.setMovimento(movimento);
		richiestaPagamento.setSoggetto(soggettoCassa);
	}

//	--------INSERIENTO NUOVE RICIHIESTE
	
	/**
	 * Richiamo al servizio InserisceRichiestaEconomalePagamentoService
	 * 
	 * @param ss quota per cui inserire la richiesta di pagamento
	 */
	private void inserisciNuovaRichiestaPagamento(SubdocumentoSpesa subdocSpesa) {
		SubdocumentoSpesa ss = subdocumentoSpesaDad.findSubdocumentoSpesaById(subdocSpesa.getUid(), SubdocumentoSpesaModelDetail.ImpegnoSubimpegno);
		InserisceRichiestaEconomale reqPAG = new InserisceRichiestaEconomale();
		reqPAG.setRichiedente(req.getRichiedente());
		reqPAG.setRichiestaEconomale(popolaRichiestaEconomale(ss));
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
		giustificativi.add(giustificativo);
		richiestaPagamento.setGiustificativi(giustificativi);
		
		List<SubdocumentoSpesa> subdocumenti = new ArrayList<SubdocumentoSpesa>();
		subdocumenti.add(ss);
		richiestaPagamento.setSubdocumenti(subdocumenti);
		
		Movimento movimento = new Movimento();
		impostaModalitaPagamento(movimento);
		movimento.setDataMovimento(new Date());
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
		TipoDiCassa tipoCassa = cassaEconomaleDad.findTipoCassa(richiestaEconomale.getCassaEconomale());
		log.debug("impostaModalitaPagamento", tipoCassa);
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
	
	
	
}
