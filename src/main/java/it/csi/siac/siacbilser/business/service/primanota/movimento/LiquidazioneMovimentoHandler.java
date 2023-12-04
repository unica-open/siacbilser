/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.primanota.movimento;

import java.math.BigDecimal;
import java.util.List;

import it.csi.siac.siacbilser.business.service.base.ServiceExecutor;
import it.csi.siac.siacbilser.integration.dao.SiacTLiquidazioneRepository;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siaccommonser.util.log.LogSrvUtil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.OperazioneSegnoConto;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;

/**
 * LiquidazioneMovimentoHandler.
 * 
 * @author Domenico
 */
public class LiquidazioneMovimentoHandler extends MovimentoHandler<Liquidazione> {
	
	private SiacTLiquidazioneRepository siacTLiquidazioneRepository;
	private LogSrvUtil log = new LogSrvUtil(this.getClass());
	

	public LiquidazioneMovimentoHandler(ServiceExecutor serviceExecutor, Richiedente richiedente, Ente ente, Bilancio bilancio) {
		super(serviceExecutor, richiedente, ente, bilancio);
		
		this.siacTLiquidazioneRepository = serviceExecutor.getAppCtx().getBean(SiacTLiquidazioneRepository.class);
	}

	@Override
	public void caricaMovimento(RegistrazioneMovFin registrazioneMovFin) {
		//TODO Verificare se servono dei dati in più per la liquidazione e caricarli qui se necessario!
		
	}
	
	@Override
	public void caricaCapitolo(RegistrazioneMovFin registrazioneMovFin) {
		Liquidazione liquidazione = (Liquidazione)registrazioneMovFin.getMovimento();
		if(liquidazione.getImpegno() != null && liquidazione.getImpegno().getCapitoloUscitaGestione() != null && liquidazione.getImpegno().getCapitoloUscitaGestione().getUid() != 0){
			return;
		}
		int idCapitolo = siacTLiquidazioneRepository.findIdCapitoloByLiquidazione(liquidazione.getUid());
		CapitoloUscitaGestione capitolo = new CapitoloUscitaGestione();
		capitolo.setUid(idCapitolo);
		Impegno impegno = new Impegno();
		impegno.setCapitoloUscitaGestione(capitolo);
		liquidazione.setCapitoloUscitaGestione(capitolo);
		liquidazione.setImpegno(impegno);
		
	}


	@Override
	public void inizializzaDatiMovimenti(List<RegistrazioneMovFin> registrazioniMovFin) {
		//TODO Verificare se servono dei dati in più per la liquidazione e caricarli qui se necessario!
		
	}


	@Override
	public void impostaImportiListaMovimentiDettaglio(MovimentoEP movimentoEP) {
		final String methodName = "impostaImportiListaMovimentiDettaglio";
		
		Liquidazione liquidazione = (Liquidazione)movimentoEP.getRegistrazioneMovFin().getMovimento();
		
		List<MovimentoDettaglio> listaMovimentoDettaglio = movimentoEP.getListaMovimentoDettaglio();
		if(listaMovimentoDettaglio.size()!=2){
			log.info(methodName, "inserimento automatico PrimaNota Integrata abbandonato. Per le Liquidazioni viene gestito solo il caso in cui ho 2 conti uno in dare ed uno in avere. In questo caso non ho 2 conti!");
			throw new BusinessException(ErroreCore.OPERAZIONE_ABBANDONATA.getErrore("inserimento automatico PrimaNota Integrata"));
		}
		
		for(MovimentoDettaglio movimentoDettaglio : listaMovimentoDettaglio){
			if(OperazioneSegnoConto.DARE.equals(movimentoDettaglio.getSegno())){
				movimentoDettaglio.setImporto(liquidazione.getImportoLiquidazione()); // getImportoAttualeLiquidazione ???
			} else if (OperazioneSegnoConto.AVERE.equals(movimentoDettaglio.getSegno())){
				movimentoDettaglio.setImporto(liquidazione.getImportoLiquidazione()); // getImportoAttualeLiquidazione ???
			}
		}
		
	}
	

	@Override
	public String ottieniDescrizionePrimaNota(RegistrazioneMovFin registrazioneMovFin) { 
		
		Entita movimento = registrazioneMovFin.getMovimento();
	
		Liquidazione liquidazione = (Liquidazione) movimento;
		
		String descrizione = liquidazione.getDescrizioneLiquidazione() != null ? liquidazione.getDescrizioneLiquidazione() + getCausaleALGCollegato(liquidazione) : "";
		
		return String.format("Liq %s %s", liquidazione.getNumeroLiquidazione(), descrizione);
	}

	private String getCausaleALGCollegato(Liquidazione liquidazione){
		SubdocumentoSpesa subdocumentoSpesa = liquidazione.getSubdocumentoSpesa();

		if (subdocumentoSpesa != null) {
			DocumentoSpesa documentoSpesa = subdocumentoSpesa.getDocumento();

			if (documentoSpesa != null && documentoSpesa.getTipoDocumento().isAllegatoAtto()) {
				return String.format(" / %s", subdocumentoSpesa.getCausaleOrdinativo());
			}
		}

		return "";
	}

	@Override
	public Soggetto getSoggetto(RegistrazioneMovFin registrazioneMovFin) { Entita movimento = registrazioneMovFin.getMovimento();
		Liquidazione liquidazione = (Liquidazione) movimento;
		return liquidazione.getSoggettoLiquidazione();
	}

	@Override
	public String ottieniDescrizioneMovimentoEP(RegistrazioneMovFin registrazioneMovFin) { 
		Entita movimento = registrazioneMovFin.getMovimento();
		Liquidazione liquidazione = (Liquidazione) movimento;
		return "MOVIMENTO ASSOCIATO A LIQUIDAZIONE " + liquidazione.getDescrizioneLiquidazione(); 
	}

	@Override
	public Capitolo<?, ?> getCapitolo(RegistrazioneMovFin registrazioneMovFin) {
		Entita movimento = registrazioneMovFin.getMovimento();
		Liquidazione liquidazione = (Liquidazione) movimento;
		return liquidazione.getCapitoloUscitaGestione();
	}


	@Override
	public BigDecimal getImportoMovimento(RegistrazioneMovFin registrazioneMovFin) {
		Liquidazione liquidazione = (Liquidazione)registrazioneMovFin.getMovimento();
		return liquidazione.getImportoLiquidazione();
	}
	
}
