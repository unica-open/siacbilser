/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.primanota.movimento;

import java.math.BigDecimal;
import java.util.List;

import it.csi.siac.siacbilser.business.service.base.ServiceExecutor;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dao.SiacTModificaBilRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTMovgestTRepository;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDMovgestTsDetTipoEnum;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneEntrata;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.OperazioneSegnoConto;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;

/**
 * ModificaMovimentoGestioneEntrataMovimentoHandler.
 * 
 * @author Domenico
 */
public class ModificaMovimentoGestioneEntrataMovimentoHandler extends MovimentoHandler<ModificaMovimentoGestioneEntrata> {
	private LogUtil log = new LogUtil(this.getClass());
	
	private SiacTMovgestTRepository siacTMovgestTRepository;
	private SiacTModificaBilRepository siacTModificaRepository;
	
	

	public ModificaMovimentoGestioneEntrataMovimentoHandler(ServiceExecutor serviceExecutor, Richiedente richiedente, Ente ente, Bilancio bilancio) {
		super(serviceExecutor, richiedente, ente, bilancio);
		
		this.siacTMovgestTRepository = Utility.getBeanViaDefaultName(serviceExecutor.getAppCtx(), SiacTMovgestTRepository.class);
		this.siacTModificaRepository = Utility.getBeanViaDefaultName(serviceExecutor.getAppCtx(), SiacTModificaBilRepository.class);
		
	}

	@Override
	public void caricaMovimento(RegistrazioneMovFin registrazioneMovFin) {
//		ModificaMovimentoGestioneEntrata modifica = (ModificaMovimentoGestioneEntrata)registrazioneMovFin.getMovimento();
	}
	
	@Override
	public void caricaCapitolo(RegistrazioneMovFin registrazioneMovFin) {
		ModificaMovimentoGestioneEntrata modifica = (ModificaMovimentoGestioneEntrata) registrazioneMovFin.getMovimento();
		CapitoloEntrataGestione capitolo = new CapitoloEntrataGestione();
		if(modifica.getSubAccertamento() != null){
			int idCapitolo = siacTMovgestTRepository.findBilElemIdByMovgestTsId(modifica.getSubAccertamento().getUid());
			capitolo.setUid(idCapitolo);
			modifica.getSubAccertamento().setCapitoloEntrataGestione(capitolo);
		}else{
			int idCapitolo = siacTMovgestTRepository.findBilElemIdByMovgestId(modifica.getAccertamento().getUid());
			capitolo.setUid(idCapitolo);
			modifica.getAccertamento().setCapitoloEntrataGestione(capitolo);
		}
	}

	@Override
	public void inizializzaDatiMovimenti(List<RegistrazioneMovFin> registrazioniMovFin) {
		//TODO Verificare se servono dei dati in più per l'impegno e caricarli qui se necessario!
		
	}


	@Override
	public void impostaImportiListaMovimentiDettaglio(MovimentoEP movimentoEP) {
		final String methodName = "impostaImportiListaMovimentiDettaglio";
		
		ModificaMovimentoGestioneEntrata modifica = (ModificaMovimentoGestioneEntrata)movimentoEP.getRegistrazioneMovFin().getMovimento();
		
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
	
	private BigDecimal ottieniImporto(ModificaMovimentoGestioneEntrata modifica) {
		final String methodName = "ottieniImporto";
		
		BigDecimal importoModifica = siacTModificaRepository.findMovgestTsDetImportoModByModIdAndMovgestTsDetTipoCode(modifica.getUid(), SiacDMovgestTsDetTipoEnum.Attuale.getCodice());
		if(importoModifica!=null){
			log.debug(methodName, "importoModifica: "+importoModifica);
			return importoModifica;
		}
		
		log.debug(methodName, "Non si tratta di una modifica di Importo. Prendo l'importo dal sub o dall'impegno.");
		
		//se non c'è cerco l'importo del subImpegno
		if(modifica.getSubAccertamento()!=null && modifica.getSubAccertamento().getUid()!=0){
			BigDecimal importoAttuale = siacTMovgestTRepository.findImportoByMovgestTsId(modifica.getSubAccertamento().getUid(), SiacDMovgestTsDetTipoEnum.Attuale.getCodice());
			if(importoAttuale!=null){
				log.debug(methodName, "Restituisco importo attuale subaccertamento: "+importoAttuale);
				return importoAttuale;
			}
		}
		
		//se non c'è cerco l'importo dell'impegno
		if(modifica.getAccertamento()!=null && modifica.getAccertamento().getUid()!=0){
			BigDecimal importoAttuale = siacTMovgestTRepository.findImportoByMovgestId(modifica.getAccertamento().getUid(), SiacDMovgestTsDetTipoEnum.Attuale.getCodice());
			if(importoAttuale!=null){
				log.debug(methodName, "Restituisco importo attuale accertamento: "+importoAttuale);
				return importoAttuale;
			}
		}
		
		throw new BusinessException(ErroreCore.OPERAZIONE_ABBANDONATA.getErrore("Impossibile ottenere l'importo della modifica [uid: " + modifica.getUid() + "]"));
	}
	

	@Override
	public String ottieniDescrizionePrimaNota(RegistrazioneMovFin registrazioneMovFin) { 
		Entita movimento = registrazioneMovFin.getMovimento();
		ModificaMovimentoGestioneEntrata modifica = (ModificaMovimentoGestioneEntrata)movimento;
		String descrizione = modifica.getDescrizioneModificaMovimentoGestione() != null ? modifica.getDescrizioneModificaMovimentoGestione() : "";
		return String.format("Mod Entrata %s %s", modifica.getNumeroModificaMovimentoGestione(), descrizione);
	}

	@Override
	public Soggetto getSoggetto(RegistrazioneMovFin registrazioneMovFin) { 
		Entita movimento = registrazioneMovFin.getMovimento();
		ModificaMovimentoGestioneEntrata modifica = (ModificaMovimentoGestioneEntrata) movimento;
		return modifica.getSoggetto();
	}

	@Override
	public String ottieniDescrizioneMovimentoEP(RegistrazioneMovFin registrazioneMovFin) { 
		Entita movimento = registrazioneMovFin.getMovimento();
		ModificaMovimentoGestioneEntrata modifica = (ModificaMovimentoGestioneEntrata) movimento;
		return "MODIFICA MOVIMENTO GESTIONE ENTRATA " + modifica.getDescrizioneModificaMovimentoGestione(); 
	}

	@Override
	public Capitolo<?, ?> getCapitolo(RegistrazioneMovFin registrazioneMovFin) {
		Entita movimento = registrazioneMovFin.getMovimento();
		ModificaMovimentoGestioneEntrata modifica = (ModificaMovimentoGestioneEntrata) movimento;
		if(modifica.getSubAccertamento() != null){
			return modifica.getSubAccertamento().getCapitoloEntrataGestione();

		}
		return modifica.getAccertamento().getCapitoloEntrataGestione();
	}


	@Override
	public BigDecimal getImportoMovimento(RegistrazioneMovFin registrazioneMovFin) {
		ModificaMovimentoGestioneEntrata modifica = (ModificaMovimentoGestioneEntrata)registrazioneMovFin.getMovimento();
		return ottieniImporto(modifica);
	}
	
}
