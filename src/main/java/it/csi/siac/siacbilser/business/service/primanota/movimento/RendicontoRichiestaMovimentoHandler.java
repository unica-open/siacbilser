/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.primanota.movimento;

import java.math.BigDecimal;
import java.util.List;

import it.csi.siac.siacbilser.business.service.base.ServiceExecutor;
import it.csi.siac.siacbilser.integration.dad.ImpegnoBilDad;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siaccecser.model.RendicontoRichiesta;
import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
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
public class RendicontoRichiestaMovimentoHandler extends MovimentoHandler<RendicontoRichiesta> {
	private LogUtil log = new LogUtil(this.getClass());
	

	private ImpegnoBilDad impegnoBilDad; 
	
	public RendicontoRichiestaMovimentoHandler(ServiceExecutor serviceExecutor, Richiedente richiedente, Ente ente, Bilancio bilancio) {
		super(serviceExecutor, richiedente, ente, bilancio);
		this.impegnoBilDad = serviceExecutor.getAppCtx().getBean("impegnoBilDad", ImpegnoBilDad.class);
	}

	@Override
	public void caricaMovimento(RegistrazioneMovFin registrazioneMovFin) {
		RendicontoRichiesta rendicontoRichiesta = (RendicontoRichiesta)registrazioneMovFin.getMovimento();
		impegnoBilDad.popolaInformazioniSoggetto(rendicontoRichiesta.getRichiestaEconomale().getImpegno());
		
		//Se servono dei dati in più per l'impegno e caricarli qui!
		
	}
	
	@Override
	public void caricaCapitolo(RegistrazioneMovFin registrazioneMovFin) {
		RendicontoRichiesta rendicontoRichiesta = (RendicontoRichiesta)registrazioneMovFin.getMovimento();
		if(rendicontoRichiesta.getImpegno() != null && rendicontoRichiesta.getImpegno().getCapitoloUscitaGestione() != null && rendicontoRichiesta.getImpegno().getCapitoloUscitaGestione().getUid() != 0){
			return;
		}
		impegnoBilDad.popolaInformazioniCapitolo(rendicontoRichiesta.getImpegno());
	}

	@Override
	public void inizializzaDatiMovimenti(List<RegistrazioneMovFin> registrazioniMovFin) {
		//TODO Verificare se servono dei dati in più per l'impegno e caricarli qui se necessario!
		
	}


	@Override
	public void impostaImportiListaMovimentiDettaglio(MovimentoEP movimentoEP) {
		final String methodName = "impostaImportiListaMovimentiDettaglio";
		
		RendicontoRichiesta rendicontoRichiesta = (RendicontoRichiesta)movimentoEP.getRegistrazioneMovFin().getMovimento();
		
		List<MovimentoDettaglio> listaMovimentoDettaglio = movimentoEP.getListaMovimentoDettaglio();
		if(listaMovimentoDettaglio.size()!=2){
			log.info(methodName, "inserimento automatico PrimaNota Integrata abbandonato. Viene gestito solo il caso in cui ho 2 conti uno in dare ed uno in avere. In questo caso non ho 2 conti!");
			throw new BusinessException(ErroreCore.OPERAZIONE_ABBANDONATA.getErrore("inserimento automatico PrimaNota Integrata"));
		}
		
		BigDecimal importo = getImportoRendicontoRichiesta(rendicontoRichiesta);
		
		for(MovimentoDettaglio movimentoDettaglio : listaMovimentoDettaglio){
			if(OperazioneSegnoConto.DARE.equals(movimentoDettaglio.getSegno())){
				movimentoDettaglio.setImporto(importo);
			} else if (OperazioneSegnoConto.AVERE.equals(movimentoDettaglio.getSegno())){
				movimentoDettaglio.setImporto(importo);
			}
		}
		
	}

	private BigDecimal getImportoRendicontoRichiesta(RendicontoRichiesta rendicontoRichiesta) {
		BigDecimal importo;
		
		if(rendicontoRichiesta.getImportoIntegrato()!=null && !BigDecimal.ZERO.equals(rendicontoRichiesta.getImportoIntegrato())) {
			importo = rendicontoRichiesta.getImportoIntegrato();
		} else if(rendicontoRichiesta.getImportoRestituito()!=null && !BigDecimal.ZERO.equals(rendicontoRichiesta.getImportoRestituito())) {
			importo = rendicontoRichiesta.getImportoRestituito();
		} else {
			throw new BusinessException("Deve essere presente un importo integrato o un importo restituito. [integrato: " +rendicontoRichiesta.getImportoIntegrato() + " restituito:"+ rendicontoRichiesta.getImportoRestituito()+"]");
		}
		return importo;
	}
	

	@Override
	public String ottieniDescrizionePrimaNota(RegistrazioneMovFin registrazioneMovFin) {
		Entita movimento = registrazioneMovFin.getMovimento();
		RendicontoRichiesta rendicontoRichiesta = (RendicontoRichiesta)movimento;
		String descrizione = rendicontoRichiesta.getRichiestaEconomale().getDescrizioneDellaRichiesta() != null ? rendicontoRichiesta.getRichiestaEconomale().getDescrizioneDellaRichiesta() : "";
		return String.format("RendRich %s %s", rendicontoRichiesta.getMovimento().getNumeroMovimento(), descrizione);
	}

	@Override
	public Soggetto getSoggetto(RegistrazioneMovFin registrazioneMovFin) {
		Entita movimento = registrazioneMovFin.getMovimento();
		RendicontoRichiesta rendicontoRichiesta = (RendicontoRichiesta)movimento;
		return rendicontoRichiesta.getRichiestaEconomale().getImpegno().getSoggetto();
		//return rendicontoRichiesta.getRichiestaEconomale().getSoggetto();
	}

	@Override
	public String ottieniDescrizioneMovimentoEP(RegistrazioneMovFin registrazioneMovFin) {
		Entita movimento = registrazioneMovFin.getMovimento();
		RendicontoRichiesta rendicontoRichiesta = (RendicontoRichiesta)movimento;
		return "MOVIMENTO ASSOCIATO AL RENDICONTO DELLA RICHIESTA ECONOMALE " + rendicontoRichiesta.getRichiestaEconomale().getNumeroRichiesta();
	}

	@Override
	public Capitolo<?, ?> getCapitolo(RegistrazioneMovFin registrazioneMovFin) {
		Entita movimento = registrazioneMovFin.getMovimento();
		RendicontoRichiesta rendicontoRichiesta = (RendicontoRichiesta)movimento;
		return rendicontoRichiesta.getImpegno().getCapitoloUscitaGestione();
	}

	@Override
	public BigDecimal getImportoMovimento(RegistrazioneMovFin registrazioneMovFin) {
		RendicontoRichiesta rendicontoRichiesta = (RendicontoRichiesta)registrazioneMovFin.getMovimento();
		return getImportoRendicontoRichiesta(rendicontoRichiesta);
	}
	
}
