/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.primanota.movimento;

import java.math.BigDecimal;
import java.util.List;

import it.csi.siac.siacbilser.business.service.base.ServiceExecutor;
import it.csi.siac.siacbilser.integration.dad.PrimaNotaDad;
import it.csi.siac.siacbilser.integration.utility.ApplicationContextHelper;
import it.csi.siac.siacbilser.model.Capitolo;
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
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;
import it.csi.siac.siacgenser.model.Risconto;

/**
 * RiscontoMovimentoHandler.
 * 
 * @author Domenico
 */
public class RiscontoMovimentoHandler extends MovimentoHandler<Risconto> {
	private LogUtil log = new LogUtil(this.getClass());
	
	private PrimaNotaDad primaNotaDad;
	private Capitolo<?, ?> capitolo;
	

	public RiscontoMovimentoHandler(ServiceExecutor serviceExecutor, Richiedente richiedente, Ente ente, Bilancio bilancio) {
		super(serviceExecutor, richiedente, ente, bilancio);
		// SIAC-6540: https://jira.spring.io/browse/SPR-7854
		this.primaNotaDad = ApplicationContextHelper.getBean(serviceExecutor.getAppCtx(), PrimaNotaDad.class);
	}

	@Override
	public void caricaMovimento(RegistrazioneMovFin registrazioneMovFin) {
		//Risconto risconto = (Risconto)registrazioneMovFin.getMovimento();
		//I dati della prima nota sono gia' presenti in risconto.getPrimaNota();
		
	}
	
	@Override
	public void caricaCapitolo(RegistrazioneMovFin registrazioneMovFin) {
		if(this.capitolo != null && this.capitolo.getUid() != 0){
			return;
		}
		Risconto rateo = (Risconto)registrazioneMovFin.getMovimento();
		PrimaNota primaNota = rateo.getPrimaNota();
		this.capitolo = primaNotaDad.findCapitoloByPrimaNotaRateoRisconto(primaNota);
	}


	@Override
	public void inizializzaDatiMovimenti(List<RegistrazioneMovFin> registrazioniMovFin) {
		//TODO Verificare se servono dei dati in più per il Risconto e caricarli qui se necessario!
		
	}


	

	@Override
	public void impostaImportiListaMovimentiDettaglio(MovimentoEP movimentoEP) {
		final String methodName = "impostaImportiListaMovimentiDettaglio";
		
		Risconto risconto = (Risconto)movimentoEP.getRegistrazioneMovFin().getMovimento();
		
		List<MovimentoDettaglio> listaMovimentoDettaglio = movimentoEP.getListaMovimentoDettaglio();
		if(listaMovimentoDettaglio.size()!=2){
			log.info(methodName, "inserimento automatico PrimaNota Integrata abbandonato. Per i Ratei/Risconti viene gestito solo il caso in cui ho 2 conti uno in dare ed uno in avere. In questo caso non ho 2 conti!");
			throw new BusinessException(ErroreCore.OPERAZIONE_ABBANDONATA.getErrore("inserimento automatico PrimaNota Integrata"));
		}
		
		for(MovimentoDettaglio movimentoDettaglio : listaMovimentoDettaglio){
			if(OperazioneSegnoConto.DARE.equals(movimentoDettaglio.getSegno())){
				movimentoDettaglio.setImporto(risconto.getImporto());
			} else if (OperazioneSegnoConto.AVERE.equals(movimentoDettaglio.getSegno())){
				movimentoDettaglio.setImporto(risconto.getImporto());
			}
		}
		
	}
	

	@Override
	public String ottieniDescrizionePrimaNota(RegistrazioneMovFin registrazioneMovFin) { Entita movimento = registrazioneMovFin.getMovimento();
		Risconto risconto = (Risconto)movimento;
		return String.format("Risconto %s su prima nota numero %s", risconto.getAnno(), risconto.getPrimaNota().getNumero());
	}

	@Override
	public Soggetto getSoggetto(RegistrazioneMovFin registrazioneMovFin) { 
		Entita movimento = registrazioneMovFin.getMovimento();
		Risconto risconto = (Risconto) movimento;
		return risconto.getPrimaNota().getSoggetto(); //NEW per CR
	}

	@Override
	public String ottieniDescrizioneMovimentoEP(RegistrazioneMovFin registrazioneMovFin) { Entita movimento = registrazioneMovFin.getMovimento();
		Risconto risconto = (Risconto) movimento;
		return "RISCONTO ANNO " + risconto.getAnno() + " PRIMA NOTA "+ risconto.getPrimaNota().getNumero(); 
	}

	@Override
	public Capitolo<?, ?> getCapitolo(RegistrazioneMovFin registrazioniMovFin) {
		return this.capitolo;
	}

	@Override
	public BigDecimal getImportoMovimento(RegistrazioneMovFin registrazioneMovFin) {
		Risconto risconto = (Risconto)registrazioneMovFin.getMovimento();
		return risconto.getImporto();
	}
	

	
}
