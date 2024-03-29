/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.primanota.movimento;

import java.math.BigDecimal;
import java.util.List;

import it.csi.siac.siacbilser.business.service.base.ServiceExecutor;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dad.ModificaMovimentoGestioneBilDad;
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
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesa;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.OperazioneSegnoConto;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;

/**
 * ModificaMovimentoGestioneSpesaMovimentoHandler.
 * 
 * @author Domenico
 */
public class ModificaMovimentoGestioneSpesaMovimentoHandler extends MovimentoHandler<ModificaMovimentoGestioneSpesa> {
	private LogSrvUtil log = new LogSrvUtil(this.getClass());
	
	private SiacTMovgestTRepository siacTMovgestTRepository;
	private ModificaMovimentoGestioneBilDad modificaMovimentoGestioneDad;
	

	public ModificaMovimentoGestioneSpesaMovimentoHandler(ServiceExecutor serviceExecutor, Richiedente richiedente, Ente ente, Bilancio bilancio) {
		super(serviceExecutor, richiedente, ente, bilancio);
		
		this.siacTMovgestTRepository = Utility.getBeanViaDefaultName(serviceExecutor.getAppCtx(), SiacTMovgestTRepository.class);
		
		this.modificaMovimentoGestioneDad = serviceExecutor.getAppCtx().getBean(ModificaMovimentoGestioneBilDad.class);		
		modificaMovimentoGestioneDad.setEnte(ente);
	}

	@Override
	public void caricaMovimento(RegistrazioneMovFin registrazioneMovFin) {
//		ModificaMovimentoGestioneSpesa modifica = (ModificaMovimentoGestioneSpesa)registrazioneMovFin.getMovimento();
	}
	
	@Override
	public void caricaCapitolo(RegistrazioneMovFin registrazioneMovFin) {
		ModificaMovimentoGestioneSpesa modifica = (ModificaMovimentoGestioneSpesa) registrazioneMovFin.getMovimento();
		CapitoloUscitaGestione capitolo = new CapitoloUscitaGestione();
		if(modifica.getSubImpegno() != null){
			int idCapitolo = siacTMovgestTRepository.findBilElemIdByMovgestTsId(modifica.getSubImpegno().getUid());
			capitolo.setUid(idCapitolo);
			modifica.getSubImpegno().setCapitoloUscitaGestione(capitolo);
		}else{
			int idCapitolo = siacTMovgestTRepository.findBilElemIdByMovgestId(modifica.getImpegno().getUid());
			capitolo.setUid(idCapitolo);
			modifica.getImpegno().setCapitoloUscitaGestione(capitolo);
		}
	}
	

	@Override
	public void inizializzaDatiMovimenti(List<RegistrazioneMovFin> registrazioniMovFin) {
		//TODO Verificare se servono dei dati in più per l'impegno e caricarli qui se necessario!
		
	}


	@Override
	public void impostaImportiListaMovimentiDettaglio(MovimentoEP movimentoEP) {
		final String methodName = "impostaImportiListaMovimentiDettaglio";
		
		ModificaMovimentoGestioneSpesa modifica = (ModificaMovimentoGestioneSpesa)movimentoEP.getRegistrazioneMovFin().getMovimento();
		
		BigDecimal importoModifica = ottieniImporto(modifica);
		
		
		List<MovimentoDettaglio> listaMovimentoDettaglio = movimentoEP.getListaMovimentoDettaglio();
		if(listaMovimentoDettaglio.size()!=2){
			log.info(methodName, "inserimento automatico PrimaNota Integrata abbandonato. Per ModificaMovimentoGestione viene gestito solo il caso in cui ho 2 conti uno in dare ed uno in avere. In questo caso non ho 2 conti!");
			throw new BusinessException(ErroreCore.OPERAZIONE_ABBANDONATA.getErrore("inserimento automatico PrimaNota Integrata"));
		}
		
		for(MovimentoDettaglio movimentoDettaglio : listaMovimentoDettaglio){
			if(OperazioneSegnoConto.DARE.equals(movimentoDettaglio.getSegno())){
				movimentoDettaglio.setImporto(importoModifica);
			} else if (OperazioneSegnoConto.AVERE.equals(movimentoDettaglio.getSegno())){
				movimentoDettaglio.setImporto(importoModifica);
			}
		}
		
	}
	
	private BigDecimal ottieniImporto(ModificaMovimentoGestioneSpesa modifica) {
		final String methodName = "ottieniImporto";
		
		BigDecimal importoModifica = modificaMovimentoGestioneDad.estraiImportoAttualeModifica(modifica);//siacTModificaRepository.findMovgestTsDetImportoModByModIdAndMovgestTsDetTipoCode(modifica.getUid(), SiacDMovgestTsDetTipoEnum.Attuale.getCodice());
		if(importoModifica!=null){
			log.info(methodName, "importoModifica: "+importoModifica);
			return importoModifica;
		}
		
		log.debug(methodName, "Non si tratta di una modifica di Importo. Prendo l'importo dal sub o dall'impegno.");
		
		//Se sono qui NON si tratta di una modifica di Importo.... prendo quindi l'importo dal sub  o dall'impegno.   
		
		//se non c'è cerco l'importo del subImpegno
		if(modifica.getSubImpegno()!=null && modifica.getSubImpegno().getUid()!=0){
			BigDecimal importoAttuale = siacTMovgestTRepository.findImportoByMovgestTsId(modifica.getSubImpegno().getUid(), SiacDMovgestTsDetTipoEnum.Attuale.getCodice());
			if(importoAttuale!=null){
				log.debug(methodName, "Restituisco importo attuale subimpegno: "+importoAttuale);
				return importoAttuale;
			}
		}
		
		//se non c'è cerco l'importo dell'impegno
		if(modifica.getImpegno()!=null && modifica.getImpegno().getUid()!=0){
			BigDecimal importoAttuale = siacTMovgestTRepository.findImportoByMovgestId(modifica.getImpegno().getUid(), SiacDMovgestTsDetTipoEnum.Attuale.getCodice());
			if(importoAttuale!=null){
				log.debug(methodName, "Restituisco importo attuale impegno: "+importoAttuale);
				return importoAttuale;
			}
		}
		
		throw new BusinessException(ErroreCore.OPERAZIONE_ABBANDONATA.getErrore("Impossibile ottenere l'importo della modifica [uid: " + modifica.getUid() + "]"));
	}
	

	@Override
	public String ottieniDescrizionePrimaNota(RegistrazioneMovFin registrazioneMovFin) { 
		Entita movimento = registrazioneMovFin.getMovimento();
		ModificaMovimentoGestioneSpesa modifica = (ModificaMovimentoGestioneSpesa)movimento;
		String descrizione = modifica.getDescrizioneModificaMovimentoGestione() != null ? modifica.getDescrizioneModificaMovimentoGestione() : "";
		return String.format("Mod Spesa %s %s", modifica.getNumeroModificaMovimentoGestione(), descrizione);
	}

	@Override
	public Soggetto getSoggetto(RegistrazioneMovFin registrazioneMovFin) { 
		Entita movimento = registrazioneMovFin.getMovimento();
		ModificaMovimentoGestioneSpesa modifica = (ModificaMovimentoGestioneSpesa) movimento;
		//SIAC-7497
		Soggetto soggettoModifica = modificaMovimentoGestioneDad.estraiSoggettoDellaModifica(modifica);
		return soggettoModifica;
	}

	@Override
	public String ottieniDescrizioneMovimentoEP(RegistrazioneMovFin registrazioneMovFin) { 
		Entita movimento = registrazioneMovFin.getMovimento();
		ModificaMovimentoGestioneSpesa modifica = (ModificaMovimentoGestioneSpesa) movimento;
		return "MODIFICA MOVIMENTO GESTIONE SPESA " + modifica.getDescrizioneModificaMovimentoGestione(); 
	}

	@Override
	public Capitolo<?, ?> getCapitolo(RegistrazioneMovFin registrazioneMovFin) {
		Entita movimento = registrazioneMovFin.getMovimento();
		ModificaMovimentoGestioneSpesa modifica = (ModificaMovimentoGestioneSpesa) movimento;
		if(modifica.getSubImpegno() != null){
			return modifica.getSubImpegno().getCapitoloUscitaGestione();
		}
		return modifica.getImpegno().getCapitoloUscitaGestione();
	}

	
	@Override
	public BigDecimal getImportoMovimento(RegistrazioneMovFin registrazioneMovFin) {
		ModificaMovimentoGestioneSpesa modifica = (ModificaMovimentoGestioneSpesa)registrazioneMovFin.getMovimento();
		return ottieniImporto(modifica);
	}
	
}
