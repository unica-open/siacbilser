/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.primanota.movimento;

import java.math.BigDecimal;
import java.util.List;

import it.csi.siac.siacbilser.business.service.base.ServiceExecutor;
import it.csi.siac.siacbilser.integration.dad.SoggettoDad;
import it.csi.siac.siacbilser.integration.dao.SiacTMovgestTRepository;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDMovgestTsDetTipoEnum;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siaccommonser.util.log.LogSrvUtil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.OperazioneSegnoConto;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;

/**
 * AccertamentoMovimentoHandler.
 * 
 * @author Domenico
 */
public class AccertamentoMovimentoHandler extends MovimentoHandler<Accertamento> {
	private LogSrvUtil log = new LogSrvUtil(this.getClass());
	
	private SiacTMovgestTRepository siacTMovgestTRepository;
	private SoggettoDad soggettoDad;

	public AccertamentoMovimentoHandler(ServiceExecutor serviceExecutor, Richiedente richiedente, Ente ente, Bilancio bilancio) {
		//inizializzo i dati comuni
		super(serviceExecutor, richiedente, ente, bilancio);
		//inizializzo il repository per l'accertamento
		this.siacTMovgestTRepository = serviceExecutor.getAppCtx().getBean(SiacTMovgestTRepository.class);
		this.soggettoDad = serviceExecutor.getAppCtx().getBean(SoggettoDad.class);
	}

	@Override
	public void caricaMovimento(RegistrazioneMovFin registrazioneMovFin) {
		//TODO Verificare se servono dei dati in più per l'accertamento e caricarli qui se necessario!
		Accertamento accertamento = (Accertamento)registrazioneMovFin.getMovimento();
		BigDecimal importoIniziale = siacTMovgestTRepository.findImportoByMovgestId(accertamento.getUid(), SiacDMovgestTsDetTipoEnum.Iniziale.getCodice());
		accertamento.setImportoIniziale(importoIniziale);
		
		// SIAC-5605 e SIAC-7497
		if(accertamento.getSoggetto() == null || accertamento.getSoggetto().getUid() == 0) {
			// Carico il soggetto per tutelarmi
			Soggetto soggetto = soggettoDad.findSoggettoByIdMovimentoGestione(accertamento.getUid());
			// Il soggetto potrebbe essere null. Ignoro
			accertamento.setSoggetto(soggetto);
		}
	}
	
	@Override
	public void caricaCapitolo(RegistrazioneMovFin registrazioneMovFin){
		Accertamento accertamento = (Accertamento)registrazioneMovFin.getMovimento();
		if(accertamento.getCapitoloEntrataGestione() != null && accertamento.getCapitoloEntrataGestione().getUid() != 0){
			//l'accertamento non ha un capitolo associato 
			return;
		}
		int idCapitolo = siacTMovgestTRepository.findBilElemIdByMovgestId(accertamento.getUid());
		CapitoloEntrataGestione capitoloEntrataGestione = new CapitoloEntrataGestione();
		capitoloEntrataGestione.setUid(idCapitolo);
		accertamento.setCapitoloEntrataGestione(capitoloEntrataGestione);
	}

	@Override
	public void inizializzaDatiMovimenti(List<RegistrazioneMovFin> registrazioniMovFin) {
		//TODO Verificare se servono dei dati in più per l'accertamento e caricarli qui se necessario!
		
	}


	

	@Override
	public void impostaImportiListaMovimentiDettaglio(MovimentoEP movimentoEP) {
		final String methodName = "impostaImportiListaMovimentiDettaglio";
		
		Accertamento accertamento = (Accertamento)movimentoEP.getRegistrazioneMovFin().getMovimento();
		
		List<MovimentoDettaglio> listaMovimentoDettaglio = movimentoEP.getListaMovimentoDettaglio();
		//controllo che la dimensione della lista sia corretta
		if(listaMovimentoDettaglio.size()!=2){
			log.info(methodName, "inserimento automatico PrimaNota Integrata abbandonato. Per gli Accertamenti viene gestito solo il caso in cui ho 2 conti uno in dare ed uno in avere. In questo caso non ho 2 conti!");
			throw new BusinessException(ErroreCore.OPERAZIONE_ABBANDONATA.getErrore("inserimento automatico PrimaNota Integrata"));
		}
		//l'importo di ciascun dettaglio e', di default, pari all'importo del movimento
		for(MovimentoDettaglio movimentoDettaglio : listaMovimentoDettaglio){
			if(OperazioneSegnoConto.DARE.equals(movimentoDettaglio.getSegno())){
				movimentoDettaglio.setImporto(accertamento.getImportoIniziale());
			} else if (OperazioneSegnoConto.AVERE.equals(movimentoDettaglio.getSegno())){
				movimentoDettaglio.setImporto(accertamento.getImportoIniziale());
			}
		}
		
	}
	

	@Override
	public String ottieniDescrizionePrimaNota(RegistrazioneMovFin registrazioneMovFin) { 
		Entita movimento = registrazioneMovFin.getMovimento();
		Accertamento accertamento = (Accertamento) movimento;
		String descrizione = accertamento.getDescrizione() != null ? accertamento.getDescrizione() : "";
		return String.format("Acc %s %s", accertamento.getNumeroBigDecimal(), descrizione);
	}

	@Override
	public Soggetto getSoggetto(RegistrazioneMovFin registrazioneMovFin) { 
		Entita movimento = registrazioneMovFin.getMovimento();
		Accertamento accertamento = (Accertamento) movimento;
		return accertamento.getSoggetto();
	}

	@Override
	public String ottieniDescrizioneMovimentoEP(RegistrazioneMovFin registrazioneMovFin) { 
		Entita movimento = registrazioneMovFin.getMovimento();
		Accertamento accertamento = (Accertamento) movimento;
		return "MOVIMENTO ASSOCIATO AD ACCERTAMENTO " + accertamento.getDescrizione(); 
	}

	@Override
	public Capitolo<?, ?> getCapitolo(RegistrazioneMovFin registrazioneMovFin) {
		Entita movimento = registrazioneMovFin.getMovimento();
		Accertamento accertamento = (Accertamento) movimento;
		return accertamento.getCapitoloEntrataGestione();
	}

	@Override
	public BigDecimal getImportoMovimento(RegistrazioneMovFin registrazioneMovFin) {
		Accertamento accertamento = (Accertamento)registrazioneMovFin.getMovimento();
		//SIAC-5008: per l'evento inserimento si dovrebbe prendere l'importo dell'impegno/accertamento 'iniziale' e non l'attuale, perche' quest'ultimo risulta essere gia' decurtato delle modifiche. 
		return accertamento.getImportoIniziale();
	}

	

	
}
