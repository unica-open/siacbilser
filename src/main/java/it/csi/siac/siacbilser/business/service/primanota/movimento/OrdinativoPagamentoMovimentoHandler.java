/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.primanota.movimento;

import java.math.BigDecimal;
import java.util.List;

import it.csi.siac.siacbilser.business.service.base.ServiceExecutor;
import it.csi.siac.siacbilser.integration.dao.SiacTOrdinativoBilRepository;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siaccommonser.util.log.LogSrvUtil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoPagamento;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.OperazioneSegnoConto;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;

/**
 * Mandato.
 * 
 * @author Domenico
 */
public class OrdinativoPagamentoMovimentoHandler extends MovimentoHandler<OrdinativoPagamento> {
	private LogSrvUtil log = new LogSrvUtil(this.getClass());
	
	private SiacTOrdinativoBilRepository siacTOrdinativoBilRepository;
	

	public OrdinativoPagamentoMovimentoHandler(ServiceExecutor serviceExecutor, Richiedente richiedente, Ente ente, Bilancio bilancio) {
		super(serviceExecutor, richiedente, ente, bilancio);
		
		this.siacTOrdinativoBilRepository = serviceExecutor.getAppCtx().getBean(SiacTOrdinativoBilRepository.class);
	}

	@Override
	public void caricaMovimento(RegistrazioneMovFin registrazioneMovFin) {
		//TODO Verificare se servono dei dati in più per la liquidazione e caricarli qui se necessario!
		
	}

	@Override
	public void caricaCapitolo(RegistrazioneMovFin registrazioneMovFin) {
		OrdinativoPagamento ord = (OrdinativoPagamento) registrazioneMovFin.getMovimento();
		if(ord.getCapitoloUscitaGestione() != null && ord.getCapitoloUscitaGestione().getUid() != 0){
			return;
		}
		int idCapitolo = siacTOrdinativoBilRepository.findIdCapitoloByOrdinativo(ord.getUid());
		CapitoloUscitaGestione capitolo = new CapitoloUscitaGestione();
		capitolo.setUid(idCapitolo);
		ord.setCapitoloUscitaGestione(capitolo);
	}
	
	@Override
	public void inizializzaDatiMovimenti(List<RegistrazioneMovFin> registrazioniMovFin) {
		//TODO Verificare se servono dei dati in più per la liquidazione e caricarli qui se necessario!
		
	}

	@Override
	public void impostaImportiListaMovimentiDettaglio(MovimentoEP movimentoEP) {
		final String methodName = "impostaImportiListaMovimentiDettaglio";
		
		OrdinativoPagamento ord = (OrdinativoPagamento)movimentoEP.getRegistrazioneMovFin().getMovimento();
		
		List<MovimentoDettaglio> listaMovimentoDettaglio = movimentoEP.getListaMovimentoDettaglio();
		if(listaMovimentoDettaglio.size()!=2){
			log.info(methodName, "inserimento automatico PrimaNota Integrata abbandonato. Per gli Ordinativi viene gestito solo il caso in cui ho 2 conti uno in dare ed uno in avere. In questo caso non ho 2 conti!");
			throw new BusinessException(ErroreCore.OPERAZIONE_ABBANDONATA.getErrore("inserimento automatico PrimaNota Integrata"));
		}
		
		for(MovimentoDettaglio movimentoDettaglio : listaMovimentoDettaglio){
			if(OperazioneSegnoConto.DARE.equals(movimentoDettaglio.getSegno())){
				movimentoDettaglio.setImporto(ord.getImportoOrdinativo());
			} else if (OperazioneSegnoConto.AVERE.equals(movimentoDettaglio.getSegno())){
				movimentoDettaglio.setImporto(ord.getImportoOrdinativo());
			}
		}
		
	}
	

	@Override
	public String ottieniDescrizionePrimaNota(RegistrazioneMovFin registrazioneMovFin) { 
		Entita movimento = registrazioneMovFin.getMovimento();
		OrdinativoPagamento ord = (OrdinativoPagamento)movimento;
		String descrizione = ord.getDescrizione() != null ? ord.getDescrizione() : "";
		return String.format("Ord %s %s", ord.getNumero(), descrizione);
	}

	@Override
	public Soggetto getSoggetto(RegistrazioneMovFin registrazioneMovFin) { 
		Entita movimento = registrazioneMovFin.getMovimento();
		OrdinativoPagamento ord = (OrdinativoPagamento) movimento;
		return ord.getSoggetto();
	}

	@Override
	public String ottieniDescrizioneMovimentoEP(RegistrazioneMovFin registrazioneMovFin) { 
		Entita movimento = registrazioneMovFin.getMovimento();
		OrdinativoPagamento ord = (OrdinativoPagamento) movimento;
		return "MOVIMENTO ASSOCIATO A ORDINATIVO " + ord.getDescrizione(); 
	}

	@Override
	public Capitolo<?, ?> getCapitolo(RegistrazioneMovFin registrazioneMovFin) {
		Entita movimento = registrazioneMovFin.getMovimento();
		OrdinativoPagamento ord = (OrdinativoPagamento) movimento;
		return ord.getCapitoloUscitaGestione();
	}
	
	@Override
	public BigDecimal getImportoMovimento(RegistrazioneMovFin registrazioneMovFin) {
		OrdinativoPagamento ord = (OrdinativoPagamento)registrazioneMovFin.getMovimento();
		return ord.getImportoOrdinativo();
	}
	
}
