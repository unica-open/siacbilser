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
import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.model.SubImpegno;
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
public class SubImpegnoMovimentoHandler extends MovimentoHandler<SubImpegno> {
	private LogUtil log = new LogUtil(this.getClass());
	
	private SiacTMovgestTRepository siacTMovgestTRepository;
	private SoggettoDad soggettoDad;
	

	public SubImpegnoMovimentoHandler(ServiceExecutor serviceExecutor, Richiedente richiedente, Ente ente, Bilancio bilancio) {
		super(serviceExecutor, richiedente, ente, bilancio);
		
		this.siacTMovgestTRepository = serviceExecutor.getAppCtx().getBean(SiacTMovgestTRepository.class);
		this.soggettoDad = serviceExecutor.getAppCtx().getBean(SoggettoDad.class);
	}

	@Override
	public void caricaMovimento(RegistrazioneMovFin registrazioneMovFin) {
		SubImpegno subImpegno = (SubImpegno)registrazioneMovFin.getMovimento();
		BigDecimal importoIniziale = siacTMovgestTRepository.findImportoByMovgestTsId(subImpegno.getUid(), SiacDMovgestTsDetTipoEnum.Iniziale.getCodice());
		subImpegno.setImportoIniziale(importoIniziale);
		
		// SIAC-5605 e SIAC-7327
		if(subImpegno.getSoggetto() == null || subImpegno.getSoggetto().getUid() == 0) {
			// Carico il soggetto per tutelarmi
			Soggetto soggetto = soggettoDad.findSoggettoByIdSubMovimentoGestione(subImpegno.getUid());
			// Il soggetto potrebbe essere null. Ignoro
			subImpegno.setSoggetto(soggetto);
		}
	}
	
	@Override
	public void caricaCapitolo(RegistrazioneMovFin registrazioneMovFin){
		SubImpegno subImpegno = (SubImpegno)registrazioneMovFin.getMovimento();
		if(subImpegno.getCapitoloUscitaGestione() != null && subImpegno.getCapitoloUscitaGestione().getUid() != 0){
			return;
		}
		int idCapitolo = siacTMovgestTRepository.findBilElemIdByMovgestTsId(subImpegno.getUid());
		CapitoloUscitaGestione capitoloUscitaGestione = new CapitoloUscitaGestione();
		capitoloUscitaGestione.setUid(idCapitolo);
		subImpegno.setCapitoloUscitaGestione(capitoloUscitaGestione);
	}

	@Override
	public void inizializzaDatiMovimenti(List<RegistrazioneMovFin> registrazioniMovFin) {
		//TODO Verificare se servono dei dati in più per l'impegno e caricarli qui se necessario!
		
	}


	

	@Override
	public void impostaImportiListaMovimentiDettaglio(MovimentoEP movimentoEP) {
		final String methodName = "impostaImportiListaMovimentiDettaglio";
		
		SubImpegno impegno = (SubImpegno)movimentoEP.getRegistrazioneMovFin().getMovimento();
		
		List<MovimentoDettaglio> listaMovimentoDettaglio = movimentoEP.getListaMovimentoDettaglio();
		if(listaMovimentoDettaglio.size()!=2){
			log.info(methodName, "inserimento automatico PrimaNota Integrata abbandonato. Per gli Impegni viene gestito solo il caso in cui ho 2 conti uno in dare ed uno in avere. In questo caso non ho 2 conti!");
			throw new BusinessException(ErroreCore.OPERAZIONE_ABBANDONATA.getErrore("inserimento automatico PrimaNota Integrata"));
		}
		
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
		SubImpegno impegno = (SubImpegno)movimento;
		String descrizione = impegno.getDescrizione() != null ? impegno.getDescrizione() : "";
		return String.format("Imp %s %s", impegno.getNumero(), descrizione);
	}

	@Override
	public Soggetto getSoggetto(RegistrazioneMovFin registrazioneMovFin) { 
		Entita movimento = registrazioneMovFin.getMovimento();
		SubImpegno impegno = (SubImpegno) movimento;
		return impegno.getSoggetto();
	}

	@Override
	public String ottieniDescrizioneMovimentoEP(RegistrazioneMovFin registrazioneMovFin) { 
		Entita movimento = registrazioneMovFin.getMovimento();
		SubImpegno impegno = (SubImpegno) movimento;
		return "MOVIMENTO ASSOCIATO A SUB IMPEGNO " + impegno.getDescrizione(); 
	}

	@Override
	public Capitolo<?, ?> getCapitolo(RegistrazioneMovFin registrazioneMovFin) {
		Entita movimento = registrazioneMovFin.getMovimento();
		SubImpegno subimpegno = (SubImpegno) movimento;
		return subimpegno.getCapitoloUscitaGestione();
	}

	
	@Override
	public BigDecimal getImportoMovimento(RegistrazioneMovFin registrazioneMovFin) {
		SubImpegno subimpegno = (SubImpegno)registrazioneMovFin.getMovimento();
		return subimpegno.getImportoIniziale();
	}
	
}
