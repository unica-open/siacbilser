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
import it.csi.siac.siaccecser.model.RichiestaEconomale;
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
public class RichiestaEconomaleMovimentoHandler extends MovimentoHandler<RichiestaEconomale> {
	private LogUtil log = new LogUtil(this.getClass());
	
	private ImpegnoBilDad impegnoBilDad; 
	

	public RichiestaEconomaleMovimentoHandler(ServiceExecutor serviceExecutor, Richiedente richiedente, Ente ente, Bilancio bilancio) {
		super(serviceExecutor, richiedente, ente, bilancio);
		this.impegnoBilDad = serviceExecutor.getAppCtx().getBean("impegnoBilDad", ImpegnoBilDad.class);
	}

	@Override
	public void caricaMovimento(RegistrazioneMovFin registrazioneMovFin) {
		RichiestaEconomale richiestaEconomale = (RichiestaEconomale)registrazioneMovFin.getMovimento();
		impegnoBilDad.popolaInformazioniSoggetto(richiestaEconomale.getImpegno());
		
		//Se servono dei dati in più per l'impegno e caricarli qui!
	}
	

	@Override
	public void caricaCapitolo(RegistrazioneMovFin registrazioneMovFin) {
		RichiestaEconomale richiestaEconomale = (RichiestaEconomale)registrazioneMovFin.getMovimento();
		if(richiestaEconomale.getImpegno() != null && richiestaEconomale.getImpegno().getCapitoloUscitaGestione() != null && richiestaEconomale.getImpegno().getCapitoloUscitaGestione().getUid() != 0){
			return;
		}
		impegnoBilDad.popolaInformazioniCapitolo(richiestaEconomale.getImpegno());
	}

	@Override
	public void inizializzaDatiMovimenti(List<RegistrazioneMovFin> registrazioniMovFin) {
		//TODO Verificare se servono dei dati in più per l'impegno e caricarli qui se necessario!
		
	}


	

	@Override
	public void impostaImportiListaMovimentiDettaglio(MovimentoEP movimentoEP) {
		final String methodName = "impostaImportiListaMovimentiDettaglio";
		
		RichiestaEconomale richiestaEconomale = (RichiestaEconomale)movimentoEP.getRegistrazioneMovFin().getMovimento();
		
		List<MovimentoDettaglio> listaMovimentoDettaglio = movimentoEP.getListaMovimentoDettaglio();
		if(listaMovimentoDettaglio.size()!=2){
			log.info(methodName, "inserimento automatico PrimaNota Integrata abbandonato. Viene gestito solo il caso in cui ho 2 conti uno in dare ed uno in avere. In questo caso non ho 2 conti!");
			throw new BusinessException(ErroreCore.OPERAZIONE_ABBANDONATA.getErrore("inserimento automatico PrimaNota Integrata"));
		}
		
		for(MovimentoDettaglio movimentoDettaglio : listaMovimentoDettaglio){
			if(OperazioneSegnoConto.DARE.equals(movimentoDettaglio.getSegno())){
				movimentoDettaglio.setImporto(richiestaEconomale.getImporto());
			} else if (OperazioneSegnoConto.AVERE.equals(movimentoDettaglio.getSegno())){
				movimentoDettaglio.setImporto(richiestaEconomale.getImporto());
			}
		}
		
	}
	

	@Override
	public String ottieniDescrizionePrimaNota(RegistrazioneMovFin registrazioneMovFin) {
		Entita movimento = registrazioneMovFin.getMovimento();
		RichiestaEconomale richiestaEconomale = (RichiestaEconomale)movimento;
		String descrizione = richiestaEconomale.getDescrizioneDellaRichiesta() != null ? richiestaEconomale.getDescrizioneDellaRichiesta() : "";
		return String.format("RichEcon %s %s", richiestaEconomale.getMovimento().getNumeroMovimento(), descrizione);
	}

	@Override
	public Soggetto getSoggetto(RegistrazioneMovFin registrazioneMovFin) {
		Entita movimento = registrazioneMovFin.getMovimento();
		RichiestaEconomale richiestaEconomale = (RichiestaEconomale)movimento;
		return richiestaEconomale.getImpegno().getSoggetto(); //Soggetto di ambito FIN
		//return richiestaEconomale.getSoggetto(); //Soggetto di ambito CEC
	}

	@Override
	public String ottieniDescrizioneMovimentoEP(RegistrazioneMovFin registrazioneMovFin) {
		Entita movimento = registrazioneMovFin.getMovimento();
		RichiestaEconomale richiestaEconomale = (RichiestaEconomale)movimento;
		return "MOVIMENTO ASSOCIATO ALLA RICHIESTA ECONOMALE  " + richiestaEconomale.getNumeroRichiesta() + " " + richiestaEconomale.getDescrizioneDellaRichiesta(); 
	}

	@Override
	public Capitolo<?, ?> getCapitolo(RegistrazioneMovFin registrazioneMovFin) {
		Entita movimento = registrazioneMovFin.getMovimento();
		RichiestaEconomale richiestaEconomale = (RichiestaEconomale)movimento;
		return richiestaEconomale.getImpegno().getCapitoloUscitaGestione();
	}


	@Override
	public BigDecimal getImportoMovimento(RegistrazioneMovFin registrazioneMovFin) {
		RichiestaEconomale richiestaEconomale = (RichiestaEconomale)registrazioneMovFin.getMovimento();
		return richiestaEconomale.getImporto();
	}
	
}
