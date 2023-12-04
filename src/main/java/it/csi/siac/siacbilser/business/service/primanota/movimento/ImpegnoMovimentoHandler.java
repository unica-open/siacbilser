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
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siaccommonser.util.log.LogSrvUtil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.OperazioneSegnoConto;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;

/**
 * ImpegnoMovimentoHandler.
 * 
 * @author Domenico
 */
public class ImpegnoMovimentoHandler extends MovimentoHandler<Impegno> {
	private LogSrvUtil log = new LogSrvUtil(this.getClass());
	
	private SiacTMovgestTRepository siacTMovgestTRepository;
	private SoggettoDad soggettoDad;
	

	public ImpegnoMovimentoHandler(ServiceExecutor serviceExecutor, Richiedente richiedente, Ente ente, Bilancio bilancio) {
		//istanzio i dati comuni
		super(serviceExecutor, richiedente, ente, bilancio);
		//istanzio il repository necessario per ottenere i dati del movgest
		this.siacTMovgestTRepository = serviceExecutor.getAppCtx().getBean(SiacTMovgestTRepository.class);
		this.soggettoDad = serviceExecutor.getAppCtx().getBean(SoggettoDad.class);
	}

	@Override
	public void caricaMovimento(RegistrazioneMovFin registrazioneMovFin) {
		Impegno impegno = (Impegno)registrazioneMovFin.getMovimento();
		//SIAC-5008: per l'evento inserimento si dovrebbe prendere l'importo dell'impegno/accertamento 'iniziale' e non l'attuale, perche' quest'ultimo risulta essere gia' decurtato delle modifiche.
		BigDecimal importoIniziale = siacTMovgestTRepository.findImportoByMovgestId(impegno.getUid(), SiacDMovgestTsDetTipoEnum.Iniziale.getCodice());
		impegno.setImportoIniziale(importoIniziale);
		
		// SIAC-5605 e SIAC-7497
		if(impegno.getSoggetto() == null || impegno.getSoggetto().getUid() == 0) {
			// Carico il soggetto per tutelarmi
			Soggetto soggetto = soggettoDad.findSoggettoByIdMovimentoGestione(impegno.getUid());
			// Il soggetto potrebbe essere null. Ignoro
			impegno.setSoggetto(soggetto);
		}
		
	}
	
	@Override
	public void caricaCapitolo(RegistrazioneMovFin registrazioneMovFin){
		Impegno impegno = (Impegno)registrazioneMovFin.getMovimento();
		if(impegno.getCapitoloUscitaGestione() != null && impegno.getCapitoloUscitaGestione().getUid() != 0){
			//l'impegno non ha nessun capitolo associato
			return;
		}
		int idCapitolo = siacTMovgestTRepository.findBilElemIdByMovgestId(impegno.getUid());
		CapitoloUscitaGestione capitoloUscitaGestione = new CapitoloUscitaGestione();
		capitoloUscitaGestione.setUid(idCapitolo);
		impegno.setCapitoloUscitaGestione(capitoloUscitaGestione);
	}

	@Override
	public void inizializzaDatiMovimenti(List<RegistrazioneMovFin> registrazioniMovFin) {
		//TODO Verificare se servono dei dati in più per l'impegno e caricarli qui se necessario!
	}


	

	@Override
	public void impostaImportiListaMovimentiDettaglio(MovimentoEP movimentoEP) {
		final String methodName = "impostaImportiListaMovimentiDettaglio";
		
		Impegno impegno = (Impegno)movimentoEP.getRegistrazioneMovFin().getMovimento();
		
		List<MovimentoDettaglio> listaMovimentoDettaglio = movimentoEP.getListaMovimentoDettaglio();
		//controllo che esistano due dettagli
		if(listaMovimentoDettaglio.size()!=2){
			log.info(methodName, "inserimento automatico PrimaNota Integrata abbandonato. Per gli Impegni viene gestito solo il caso in cui ho 2 conti uno in dare ed uno in avere. In questo caso non ho 2 conti!");
			throw new BusinessException(ErroreCore.OPERAZIONE_ABBANDONATA.getErrore("inserimento automatico PrimaNota Integrata"));
		}
		
		//di default, l'importo di ciascun movimento dettaglio e' pari all'importo del movimento
		for(MovimentoDettaglio movimentoDettaglio : listaMovimentoDettaglio){
			if(OperazioneSegnoConto.DARE.equals(movimentoDettaglio.getSegno())){
				movimentoDettaglio.setImporto(impegno.getImportoIniziale());
			} else if (OperazioneSegnoConto.AVERE.equals(movimentoDettaglio.getSegno())){
				movimentoDettaglio.setImporto(impegno.getImportoIniziale());
			}
		}
		
	}
	

	@Override
	public String ottieniDescrizionePrimaNota(RegistrazioneMovFin registrazioneMovFin) { 
		Entita movimento = registrazioneMovFin.getMovimento();
		Impegno impegno = (Impegno)movimento;
		String descrizione = impegno.getDescrizione() != null ? impegno.getDescrizione() : "";
		return String.format("Imp %s %s", impegno.getNumeroBigDecimal(), descrizione);
	}

	@Override
	public Soggetto getSoggetto(RegistrazioneMovFin registrazioneMovFin) { 
		Entita movimento = registrazioneMovFin.getMovimento();
		Impegno impegno = (Impegno) movimento;
		return impegno.getSoggetto();
	}

	@Override
	public String ottieniDescrizioneMovimentoEP(RegistrazioneMovFin registrazioneMovFin) { 
		Entita movimento = registrazioneMovFin.getMovimento();
		Impegno impegno = (Impegno) movimento;
		return "MOVIMENTO ASSOCIATO AD IMPEGNO " + impegno.getDescrizione(); 
	}

	@Override
	public Capitolo<?, ?> getCapitolo(RegistrazioneMovFin registrazioneMovFin) {
		Entita movimento = registrazioneMovFin.getMovimento();
		Impegno impegno = (Impegno) movimento;
		return impegno.getCapitoloUscitaGestione();
	}

	@Override
	public BigDecimal getImportoMovimento(RegistrazioneMovFin registrazioneMovFin) {
		Impegno impegno = (Impegno)registrazioneMovFin.getMovimento();
		//SIAC-5008: per l'evento inserimento si dovrebbe prendere l'importo dell'impegno/accertamento 'iniziale' e non l'attuale, perche' quest'ultimo risulta essere gia' decurtato delle modifiche.
		return impegno.getImportoIniziale();
	}

	

	
}
