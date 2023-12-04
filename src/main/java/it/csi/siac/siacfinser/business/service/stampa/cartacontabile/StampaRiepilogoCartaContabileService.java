/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.stampa.cartacontabile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacbilser.business.service.stampa.base.ReportElaborationException;
import it.csi.siac.siacbilser.business.service.stampa.base.SyncReportBaseService;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.file.File;
import it.csi.siac.siacfinser.business.service.carta.RicercaCartaContabilePerChiaveService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaCartaContabilePerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaCartaContabilePerChiaveResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.StampaRiepilogoCartaContabile;
import it.csi.siac.siacfinser.frontend.webservice.msg.StampaRiepilogoCartaContabileResponse;
import it.csi.siac.siacfinser.model.carta.CartaContabile;
import it.csi.siac.siacfinser.model.ric.RicercaCartaContabileK;


@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class StampaRiepilogoCartaContabileService extends
SyncReportBaseService<StampaRiepilogoCartaContabile, StampaRiepilogoCartaContabileResponse, StampaCartaContabileReportHandler> {

	@Autowired
	RicercaCartaContabilePerChiaveService ricercaCartaContabilePerChiaveService;
	
	
	private RicercaCartaContabilePerChiaveResponse esitoRicerca;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getCartaContabile(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("carta contabile"));
		checkNotNull(req.getCartaContabile().getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio carta contabile"));
		checkNotNull(req.getCartaContabile().getNumero(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero carta contabile"));
		checkCondition(req.getCartaContabile().getNumero()>=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero carta contabile"));
		
		boolean esisteCarta = verificaEsistenzaCartaContabileIndicata();
		if(!esisteCarta){
			return;
		}
			
	}
	
	
	private boolean verificaEsistenzaCartaContabileIndicata(){
		RicercaCartaContabilePerChiaveResponse responseRicerca = ricaricaCartaContabile(); 
		if(responseRicerca==null || responseRicerca.getCartaContabile()==null){
			List<Errore> listaErrori = new ArrayList<Errore>();
			Errore errore = new Errore(ErroreCore.NESSUN_DATO_REPERITO.getCodice(), "ricercaCartaContabilePerChiaveService non ha restituito nulla");
			listaErrori.add(errore );
			res.setErrori(listaErrori );
			res.setEsito(Esito.FALLIMENTO);
			res.setReport(null);
			return false;
		} else if(responseRicerca.isFallimento()){
			res.setErrori(responseRicerca.getErrori());
			res.setEsito(Esito.FALLIMENTO);
			res.setReport(null);
			return false;
		}
		this.esitoRicerca = responseRicerca;
		return true;
	}
	
	
	private RicercaCartaContabilePerChiaveResponse ricaricaCartaContabile(){
		//DOBBIAMO RICARICARE LA CARTA CONTABILE, PER NON FIDARCI DI QUANTO CI PASSA IL FRONT END:
		RicercaCartaContabilePerChiave serviceRequestRicercaCarta = new RicercaCartaContabilePerChiave();
		serviceRequestRicercaCarta.setEnte(req.getEnte());
		serviceRequestRicercaCarta.setRichiedente(req.getRichiedente());
		serviceRequestRicercaCarta.setDataOra(new Date());
		RicercaCartaContabileK pRicercaCartaContabileK = new RicercaCartaContabileK();
		pRicercaCartaContabileK.setBilancio(req.getCartaContabile().getBilancio());
		pRicercaCartaContabileK.setCartaContabile(req.getCartaContabile());
		serviceRequestRicercaCarta.setpRicercaCartaContabileK(pRicercaCartaContabileK);
		serviceRequestRicercaCarta.setCercaMdpCessionePerChiaveModPag(true);
		RicercaCartaContabilePerChiaveResponse responseRicerca = ricercaCartaContabilePerChiaveService.executeService(serviceRequestRicercaCarta);
		return responseRicerca;
		//
	}
	
	@Override
	protected void initReportHandler() {
		// TODO Auto-generated method stub
		//da request leggo uid e lo metto nel reportHandler
		
		//TUTTO OK, settiamo i dati nell'handler:
		reportHandler.setRichiedente(req.getRichiedente());
		reportHandler.setEnte(req.getEnte());
		
		
		//settiamo la carta contabile:
	   CartaContabile cartaContabile = esitoRicerca.getCartaContabile();
	   reportHandler.setCartaContabile(cartaContabile);
		//
		
		reportHandler.setBilancio(req.getCartaContabile().getBilancio());
		
	}
	

	@Override
	protected void postElaborationSuccess() {
		File report = reportHandler.getGeneraReportResponse().getReport();
		res.setReport(report);
	}

	@Override
	protected void postElaborationError(ReportElaborationException e) {
		res.addErrori(reportHandler.getGeneraReportResponse().getErrori());
	}


	@Override
	protected void preStartElaboration()
	{
		// TODO Auto-generated method stub
		
	}



}
