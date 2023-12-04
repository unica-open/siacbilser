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
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.OperazioneSegnoConto;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;

/**
 * SubAccertamentoMovimentoHandler.
 * 
 * @author Domenico
 */
public class SubAccertamentoMovimentoHandler extends MovimentoHandler<SubAccertamento> {
	private LogSrvUtil log = new LogSrvUtil(this.getClass());
	
	private SiacTMovgestTRepository siacTMovgestTRepository;
	private SoggettoDad soggettoDad;
	

	public SubAccertamentoMovimentoHandler(ServiceExecutor serviceExecutor, Richiedente richiedente, Ente ente, Bilancio bilancio) {
		super(serviceExecutor, richiedente, ente, bilancio);
		
		this.siacTMovgestTRepository = serviceExecutor.getAppCtx().getBean(SiacTMovgestTRepository.class);
		this.soggettoDad = serviceExecutor.getAppCtx().getBean(SoggettoDad.class);
	}

	@Override
	public void caricaMovimento(RegistrazioneMovFin registrazioneMovFin) {
		//TODO Verificare se servono dei dati in più per l'accertamento e caricarli qui se necessario!
		SubAccertamento subAccertamento = (SubAccertamento)registrazioneMovFin.getMovimento();
		BigDecimal importoIniziale = siacTMovgestTRepository.findImportoByMovgestTsId(subAccertamento.getUid(), SiacDMovgestTsDetTipoEnum.Iniziale.getCodice());
		subAccertamento.setImportoIniziale(importoIniziale);
		
		// SIAC-5605 e SIAC-7327
		if(subAccertamento.getSoggetto() == null || subAccertamento.getSoggetto().getUid() == 0) {
			// Carico il soggetto per tutelarmi
			Soggetto soggetto = soggettoDad.findSoggettoByIdSubMovimentoGestione(subAccertamento.getUid());
			// Il soggetto potrebbe essere null. Ignoro
			subAccertamento.setSoggetto(soggetto);
		}
		
	}
	
	@Override
	public void caricaCapitolo(RegistrazioneMovFin registrazioneMovFin){
		SubAccertamento subAccertamento = (SubAccertamento)registrazioneMovFin.getMovimento();
		if(subAccertamento.getCapitoloEntrataGestione() != null && subAccertamento.getCapitoloEntrataGestione().getUid() != 0){
			return;
		}
		int idCapitolo = siacTMovgestTRepository.findBilElemIdByMovgestTsId(subAccertamento.getUid());
		CapitoloEntrataGestione capitoloEntrataGestione = new CapitoloEntrataGestione();
		capitoloEntrataGestione.setUid(idCapitolo);
		subAccertamento.setCapitoloEntrataGestione(capitoloEntrataGestione);
	}

	@Override
	public void inizializzaDatiMovimenti(List<RegistrazioneMovFin> registrazioniMovFin) {
		//TODO Verificare se servono dei dati in più per l'accertamento e caricarli qui se necessario!
		
	}


	

	@Override
	public void impostaImportiListaMovimentiDettaglio(MovimentoEP movimentoEP) {
		final String methodName = "impostaImportiListaMovimentiDettaglio";
		
		SubAccertamento subAccertamento = (SubAccertamento)movimentoEP.getRegistrazioneMovFin().getMovimento();
		
		List<MovimentoDettaglio> listaMovimentoDettaglio = movimentoEP.getListaMovimentoDettaglio();
		if(listaMovimentoDettaglio.size()!=2){
			log.info(methodName, "inserimento automatico PrimaNota Integrata abbandonato. Per i subAccertamenti viene gestito solo il caso in cui ho 2 conti uno in dare ed uno in avere. In questo caso non ho 2 conti!");
			throw new BusinessException(ErroreCore.OPERAZIONE_ABBANDONATA.getErrore("inserimento automatico PrimaNota Integrata"));
		}
		
		for(MovimentoDettaglio movimentoDettaglio : listaMovimentoDettaglio){
			if(OperazioneSegnoConto.DARE.equals(movimentoDettaglio.getSegno())){
				movimentoDettaglio.setImporto(subAccertamento.getImportoIniziale());
			} else if (OperazioneSegnoConto.AVERE.equals(movimentoDettaglio.getSegno())){
				movimentoDettaglio.setImporto(subAccertamento.getImportoIniziale());
			}
		}
		
	}
	

	@Override
	public String ottieniDescrizionePrimaNota(RegistrazioneMovFin registrazioneMovFin) {
		Entita movimento = registrazioneMovFin.getMovimento();
		SubAccertamento subAcc = (SubAccertamento) movimento;
		String descrizione = subAcc.getDescrizione() != null ? subAcc.getDescrizione() : "";
		return String.format("Acc %s %s", subAcc.getNumeroBigDecimal(), descrizione);
	}

	@Override
	public Soggetto getSoggetto(RegistrazioneMovFin registrazioneMovFin) {
		Entita movimento = registrazioneMovFin.getMovimento();
		SubAccertamento subAcc = (SubAccertamento) movimento;
		return subAcc.getSoggetto();
	}

	@Override
	public String ottieniDescrizioneMovimentoEP(RegistrazioneMovFin registrazioneMovFin) {
		Entita movimento = registrazioneMovFin.getMovimento();
		SubAccertamento subAcc = (SubAccertamento) movimento;
		return "MOVIMENTO ASSOCIATO A SUB ACCERTAMENTO " + subAcc.getDescrizione(); 
	}

	@Override
	public Capitolo<?, ?> getCapitolo(RegistrazioneMovFin registrazioneMovFin) {
		Entita movimento = registrazioneMovFin.getMovimento();
		SubAccertamento subAcc = (SubAccertamento) movimento;
		return subAcc.getCapitoloEntrataGestione();
	}

	@Override
	public BigDecimal getImportoMovimento(RegistrazioneMovFin registrazioneMovFin) {
		SubAccertamento subAcc = (SubAccertamento)registrazioneMovFin.getMovimento();
		return subAcc.getImportoIniziale();
	}

	
}
