/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.cassaeconomale.rendiconto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.csi.siac.siacattser.frontend.webservice.msg.InserisceProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.InserisceProvvedimentoResponse;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.StatoOperativoAtti;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilser.business.service.allegatoatto.InserisceAllegatoAttoService;
import it.csi.siac.siacbilser.business.service.allegatoatto.InserisceElencoConDocumentiConQuoteService;
import it.csi.siac.siacbilser.business.service.base.Helper;
import it.csi.siac.siacbilser.business.service.base.ServiceExecutor;
import it.csi.siac.siacbilser.business.service.provvedimento.InserisceProvvedimentoService;
import it.csi.siac.siacbilser.business.service.stampa.cassaeconomale.rendiconto.model.StampaRendicontoCassaCapitoloMovimenti;
import it.csi.siac.siacbilser.integration.dad.AttoAmministrativoDad;
import it.csi.siac.siacbilser.model.exception.HelperException;
import it.csi.siac.siaccecser.model.TipoStampa;
import it.csi.siac.siaccommonser.util.log.LogSrvUtil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceElencoResponse;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.StatoOperativoAllegatoAtto;
import it.csi.siac.siacfin2ser.model.StatoOperativoElencoDocumenti;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;

/**
 * Helper per la gestione dell'allegato atto
 * @author Marchino Alessandro
 */
public class StampaRendicontoCassaAllegatoAttoHelper implements Helper<Void> {
	
	private final LogSrvUtil log = new LogSrvUtil(getClass());
	
	private final Richiedente richiedente;
	private final Ente ente;
	private final Bilancio bilancio;
	private final TipoStampa tipoStampa;
	private final Iterable<StampaRendicontoCassaCapitoloMovimenti> listaStampaRendicontoCassaCapitoloMovimenti;
	private final Date dataOra;
	
	private final Soggetto soggetto;
	private final ModalitaPagamentoSoggetto modalitaPagamentoSoggetto;
	private final StrutturaAmministrativoContabile strutturaAmministrativoContabile;
	private final String causale;
	private final Boolean anticipiSpesaDaInserire;
	
	// Pseudo-autowired fields
	private final ServiceExecutor serviceExecutor;
	private final AttoAmministrativoDad attoAmministrativoDad;
	
	// Instance fields
	private AttoAmministrativo attoAmministrativo;
	private AllegatoAtto allegatoAtto;
	private ElencoDocumentiAllegato elencoDocumentiAllegato;

	/**
	 * Helper constructor
	 * @param richiedente il richiedente
	 * @param ente l'ente
	 * @param bilancio il bilancio
	 * @param tipoStampa il tipo di stampa
	 * @param listaStampaRendicontoCassaCapitoloMovimenti i dati di stampa
	 * @param soggetto il soggetto
	 * @param modalitaPagamentoSoggetto la modalit&agrave; di pagamento del soggetto
	 * @param strutturaAmministrativoContabile la struttura amministrativo contabile
	 * @param causale la causale
	 * @param anticipiSpesaDaInserire se gli anticipi spesa siano da inserire nell'allegato
	 * @param serviceExecutor l'executor dei servizi
	 * @param attoAmministrativoDad il dad del provvedimento
	 */
	public StampaRendicontoCassaAllegatoAttoHelper(Richiedente richiedente, Ente ente, Bilancio bilancio, TipoStampa tipoStampa,
			Iterable<StampaRendicontoCassaCapitoloMovimenti> listaStampaRendicontoCassaCapitoloMovimenti,
			Soggetto soggetto, ModalitaPagamentoSoggetto modalitaPagamentoSoggetto,
			StrutturaAmministrativoContabile strutturaAmministrativoContabile, String causale,
			Boolean anticipiSpesaDaInserire, ServiceExecutor serviceExecutor, AttoAmministrativoDad attoAmministrativoDad) {
		this.richiedente = richiedente;
		this.ente = ente;
		this.bilancio = bilancio;
		this.tipoStampa = tipoStampa;
		this.listaStampaRendicontoCassaCapitoloMovimenti = listaStampaRendicontoCassaCapitoloMovimenti;
		this.soggetto = soggetto;
		this.modalitaPagamentoSoggetto = modalitaPagamentoSoggetto;
		this.strutturaAmministrativoContabile = strutturaAmministrativoContabile;
		this.causale = causale;
		this.anticipiSpesaDaInserire = anticipiSpesaDaInserire;
		this.serviceExecutor = serviceExecutor;
		this.attoAmministrativoDad = attoAmministrativoDad;
		
		this.dataOra = new Date();
	}

	/**
	 * @return the attoAmministrativo
	 */
	public AttoAmministrativo getAttoAmministrativo() {
		return attoAmministrativo;
	}

	/**
	 * @return the allegatoAtto
	 */
	public AllegatoAtto getAllegatoAtto() {
		return allegatoAtto;
	}

	@Override
	public Void helpExecute() {
		final String methodName = "helpExecute";
		if(!TipoStampa.DEFINITIVA.equals(tipoStampa)) {
			log.debug(methodName, "Stampa in bozza, non inserisco l'allegato");
			return null;
		}
		
		try {
			inserisciProvvedimentoAutomatico();
			inserisciAnagraficaAttoAllegato();
			inserisciElenco();
		} catch(RuntimeException e) {
//			log.warn(methodName, "Errore nell'inserimento dell'allegato atto: " + e.getMessage() + ". Ignoro il dato e proseguo con l'elaborazione", e);
			// SIAC-5166: necessario escalare il livello dell'eccezione causa difetto configurazione server
			log.error(methodName, "Errore nell'inserimento dell'allegato atto: " + e.getMessage() + ". Ignoro il dato e proseguo con l'elaborazione", e);
			// Forzo a null
			allegatoAtto = null;
			throw new HelperException(e);
		}
		return null;
	}

	/**
	 * Inserimento del provvedimento automatico cui sar&agrave; collegato l'allegato atto
	 */
	private void inserisciProvvedimentoAutomatico() {
		final String methodName = "inserisciProvvedimentoAutomatico";
		InserisceProvvedimento req = new InserisceProvvedimento();
		
		req.setDataOra(dataOra);
		req.setRichiedente(richiedente);
		req.setEnte(ente);
		if(strutturaAmministrativoContabile != null && strutturaAmministrativoContabile.getUid() != 0) {
			req.setStrutturaAmministrativoContabile(strutturaAmministrativoContabile);
		}
		TipoAtto tipoAtto = getTipoAttoALG();
		req.setTipoAtto(tipoAtto);
		
		AttoAmministrativo aa = new AttoAmministrativo();
		aa.setAnno(bilancio.getAnno());
		aa.setStatoOperativo(StatoOperativoAtti.DEFINITIVO);
		aa.setOggetto(causale);
		req.setAttoAmministrativo(aa);
		
		InserisceProvvedimentoResponse res = serviceExecutor.executeServiceSuccess(InserisceProvvedimentoService.class, req);
		attoAmministrativo = res.getAttoAmministrativoInserito();
		// FIXME: mancano dei dati in response. Li injetto qua
		popolaAttoAmministrativo(tipoAtto);
		
		log.info(methodName, "Inserito atto amministrativo con uid " + attoAmministrativo.getUid());
	}

	/**
	 * Popolamento dell'atto amministrativo in risposta
	 * @param tipoAtto il tipo atto ALG
	 */
	private void popolaAttoAmministrativo(TipoAtto tipoAtto) {
		attoAmministrativo.setEnte(ente);
		attoAmministrativo.setTipoAtto(tipoAtto);
		if(strutturaAmministrativoContabile != null && strutturaAmministrativoContabile.getUid() != 0) {
			attoAmministrativo.setStrutturaAmmContabile(strutturaAmministrativoContabile);
		}
	}

	/**
	 * Ottiene il tipo atto con codice ALG
	 * @return il tipo atto ALG
	 */
	private TipoAtto getTipoAttoALG() {
		TipoAtto tipoAtto = attoAmministrativoDad.getTipoAttoALG(ente);
		if(tipoAtto == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Tipo atto", "ALG"));
		}
		return tipoAtto;
	}

	/**
	 * Inserimento dell'anagrafica dell'allegato atto
	 */
	private void inserisciAnagraficaAttoAllegato() {
		final String methodName = "inserisciAnagraficaAttoAllegato";
		InserisceAllegatoAtto req = new InserisceAllegatoAtto();
		
		req.setDataOra(dataOra);
		req.setRichiedente(richiedente);
		req.setBilancio(bilancio);
		
		req.setAllegatoAtto(new AllegatoAtto());
		req.getAllegatoAtto().setEnte(ente);
		req.getAllegatoAtto().setCausale(causale);
		req.getAllegatoAtto().setAttoAmministrativo(attoAmministrativo);
		req.getAllegatoAtto().setStatoOperativoAllegatoAtto(StatoOperativoAllegatoAtto.DA_COMPLETARE);
		req.getAllegatoAtto().setDatiSensibili(Boolean.FALSE);
		
		InserisceAllegatoAttoResponse res = serviceExecutor.executeServiceSuccess(InserisceAllegatoAttoService.class, req);
		allegatoAtto = res.getAllegatoAtto();
		log.info(methodName, "Inserito allegato atto con uid " + allegatoAtto.getUid());
	}
	
	/**
	 * Inserimento dell'elenco per gli impegni
	 */
	private void inserisciElenco() {
		final String methodName = "inserisciElenco";
		InserisceElenco req = new InserisceElenco();
		
		req.setDataOra(dataOra);
		req.setRichiedente(richiedente);
		req.setBilancio(bilancio);
		
		req.setElencoDocumentiAllegato(new ElencoDocumentiAllegato());
		req.getElencoDocumentiAllegato().setAllegatoAtto(allegatoAtto);
		req.getElencoDocumentiAllegato().setAnno(bilancio.getAnno());
		req.getElencoDocumentiAllegato().setEnte(ente);
		req.getElencoDocumentiAllegato().setStatoOperativoElencoDocumenti(StatoOperativoElencoDocumenti.BOZZA);
		
		req.getElencoDocumentiAllegato().setSubdocumenti(new ArrayList<Subdocumento<?, ?>>());
		
		if(Boolean.TRUE.equals(anticipiSpesaDaInserire)) {
			popolaSubdocumentiConAnticipoSpesa(req.getElencoDocumentiAllegato().getSubdocumenti());
		} else {
			popolaSubdocumentiSenzaAnticipoSpesa(req.getElencoDocumentiAllegato().getSubdocumenti());
		}
		
		InserisceElencoResponse resIE = serviceExecutor.executeServiceSuccess(InserisceElencoConDocumentiConQuoteService.class, req);
		elencoDocumentiAllegato = resIE.getElencoDocumentiAllegato();
		log.info(methodName, "Inserito elenco con uid " + elencoDocumentiAllegato.getUid());
	}
	
	/**
	 * Popolamento dei subdocumenti di spesa con gli anticipi di spesa
	 * @param subdocumenti i subdoc da popolare
	 */
	private void popolaSubdocumentiConAnticipoSpesa(List<Subdocumento<?, ?>> subdocumenti) {
		for(StampaRendicontoCassaCapitoloMovimenti srccm : listaStampaRendicontoCassaCapitoloMovimenti) {
			// Condizioni per ignorare l'impegno: non ha movimenti e l'importo e' pari a zero
			int totaleCapitoli = srccm.getNumMovimentiCapitoloASM() + srccm.getNumMovimentiCapitoloNOASM();
			BigDecimal importo = srccm.getImportoTotaleUsciteEntrateASM().add(srccm.getImportoTotaleUsciteEntrateNOASM());
			
			if(isSubdocumentoDaIgnorare(totaleCapitoli, importo)) {
				continue;
			}
			// Aggiungere
			SubdocumentoSpesa ss = inizializzaSubdocumentoSpesa(srccm, importo);
			subdocumenti.add(ss);
		}
	}
	
	/**
	 * Controlla se i subdocumenti non siano da generare
	 * @param numeroCapitoli il numero dei capitoli
	 * @param importo l'importo
	 * @return se sia da ignorare o meno la gestione
	 */
	private boolean isSubdocumentoDaIgnorare(int numeroCapitoli, BigDecimal importo) {
		return numeroCapitoli == 0 || BigDecimal.ZERO.compareTo(importo) == 0;
	}
	
	/**
	 * Inizializzazione del subdocumento di spesa
	 * @param srccm la stampa rendiconto
	 * @param importo l'importod a apporre
	 * @return il subdocumento di spesa
	 */
	private SubdocumentoSpesa inizializzaSubdocumentoSpesa(StampaRendicontoCassaCapitoloMovimenti srccm, BigDecimal importo) {
		SubdocumentoSpesa ss = new SubdocumentoSpesa();
		ss.setCausaleOrdinativo(causale);
		ss.setEnte(ente);
		ss.setImporto(importo);
		ss.setModalitaPagamentoSoggetto(modalitaPagamentoSoggetto);
		
		Impegno impegno = inizializzaImpegno(srccm);
		SubImpegno subImpegno = inizializzaSubImpegno(srccm);
		
		ss.setImpegno(impegno);
		ss.setSubImpegno(subImpegno);
		
		return ss;
	}

	/**
	 * Inizializzazione dell'impegno
	 * @param srccm la stampa del rendiconto
	 * @return l'impegno
	 */
	private Impegno inizializzaImpegno(StampaRendicontoCassaCapitoloMovimenti srccm) {
		Impegno impegno = null;
		if(srccm.getImpegno() != null && srccm.getImpegno().getUid() != 0) {
			impegno = new Impegno();
			impegno.setUid(srccm.getImpegno().getUid());
			impegno.setSoggetto(soggetto);
		}
		return impegno;
	}
	
	/**
	 * Inizializzazione del subimpegno
	 * @param srccm la stampa del rendiconto
	 * @return il subimpegno
	 */
	private SubImpegno inizializzaSubImpegno(StampaRendicontoCassaCapitoloMovimenti srccm) {
		SubImpegno subImpegno = null;
		if(srccm.getSubImpegno() != null) {
			subImpegno = new SubImpegno();
			subImpegno.setUid(srccm.getSubImpegno().getUid());
			subImpegno.setSoggetto(soggetto);
		}
		return subImpegno;
	}
	
	/**
	 * Popolamento dei subdocumenti di spesa senza gli anticipi di spesa
	 * @param subdocumenti i subdoc da popolare
	 */
	private void popolaSubdocumentiSenzaAnticipoSpesa(List<Subdocumento<?, ?>> subdocumenti) {
		for(StampaRendicontoCassaCapitoloMovimenti srccm : listaStampaRendicontoCassaCapitoloMovimenti) {
			int totaleCapitoli = srccm.getNumMovimentiCapitoloNOASM();
			BigDecimal importo = srccm.getImportoTotaleUsciteEntrateNOASM();
			
			if(isSubdocumentoDaIgnorare(totaleCapitoli, importo)) {
				continue;
			}
			// Aggiungere
			SubdocumentoSpesa ss = inizializzaSubdocumentoSpesa(srccm, importo);
			subdocumenti.add(ss);
		}
	}
}
