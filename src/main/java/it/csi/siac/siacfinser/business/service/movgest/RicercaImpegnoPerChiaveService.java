/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.movgest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.business.service.common.RicercaAttributiMovimentoGestioneService;
import it.csi.siac.siacfinser.business.service.util.Utility;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveResponse;
import it.csi.siac.siacfinser.integration.dad.MutuoDad;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.mutuo.Mutuo;
import it.csi.siac.siacfinser.model.mutuo.VoceMutuo;


@Deprecated
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaImpegnoPerChiaveService extends RicercaAttributiMovimentoGestioneService<RicercaImpegnoPerChiave, RicercaImpegnoPerChiaveResponse> {
	
	@Autowired
	MutuoDad mutuoDad;
	
	@Override
	protected void init() {
		final String methodName = "RicercaImpegnoPerChiaveService : init()";
		log.debug(methodName, "- Begin");
	}	
	
	@Override
	public void execute() {
		final String methodName = "RicercaImpegnoPerChiaveService : execute()";
		log.debug(methodName, "- Begin");
		log.debug(methodName, "- req.getpRicercaImpegnoK() " + req.getpRicercaImpegnoK());
		
		//1. Vengono letti i valori ricevuti in input dalla request
		String annoEsercizio = req.getpRicercaImpegnoK().getAnnoEsercizio().toString();
		Integer annoImpegno = req.getpRicercaImpegnoK().getAnnoImpegno();
		BigDecimal numeroImpegno = req.getpRicercaImpegnoK().getNumeroImpegno();
		Richiedente richiedente = req.getRichiedente();
		Ente ente = req.getEnte();
		Impegno impegno = null;

		//2. Si richiama il metodo interno di ricerca per chiave per impegni o accertamenti:
		impegno = (Impegno) impegnoDad.ricercaMovimentoPk(richiedente, ente, annoEsercizio, annoImpegno, numeroImpegno, Constanti.MOVGEST_TIPO_IMPEGNO, true);
		
		if(null!=impegno){
			//si invoca il metodo completaDatiRicercaImpegnoPk che si occupa di vestire i dati ottenuti:
			impegno = completaDatiRicercaImpegnoPk(richiedente, impegno, annoEsercizio);
			
			
			// completo con i MUTUI
			if(impegno.getListaVociMutuo()!=null && !impegno.getListaVociMutuo().isEmpty()){
				List<String> listaNumeriMutuo =  listaNumeriMutuo(impegno.getListaVociMutuo());
				impegno.setElencoMutui(getListaMutuiAssociati(listaNumeriMutuo));
			}		
			
			//Jira 1887 se il mutuo non è associato all'impegno 
			if(impegno.getElencoSubImpegni()!=null && !impegno.getElencoSubImpegni().isEmpty()){
				
				for (int i=0; i < impegno.getElencoSubImpegni().size(); i++) {

					List<String> listaNumeriMutuo =  listaNumeriMutuo(((SubImpegno)impegno.getElencoSubImpegni().get(i)).getListaVociMutuo());
					((SubImpegno)impegno.getElencoSubImpegni().get(i)).setElencoMutui(getListaMutuiAssociati(listaNumeriMutuo));
				
					//System.out.println("elencoVociMutuo nel service: " + ((SubImpegno)impegno.getElencoSubImpegni().get(i)).getElencoMutui());
				}
				
				
				
			}	
			//componiamo la respose esito positivo:
			res.setImpegno(impegno);
			res.setBilancio(impegno.getCapitoloUscitaGestione().getBilancio());
			res.setCapitoloUscitaGestione(impegno.getCapitoloUscitaGestione());
			res.setEsito(Esito.SUCCESSO);
			Utility.logXmlTypeObject(res, "WAWA");
		} else {
			//componiamo la respose esito negativo:
			res.setBilancio(null);
			res.setCapitoloUscitaGestione(null);
			res.setImpegno(null);
			res.setEsito(Esito.FALLIMENTO);
		}
	}
	
	
	
	@Override
	@Transactional(timeout=360, readOnly=true)
	public RicercaImpegnoPerChiaveResponse executeService(RicercaImpegnoPerChiave serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		final String methodName = "RicercaImpegnoPerChiaveService : checkServiceParam()";
	
		//dati di input presi da request:
		Integer annoEsercizio = req.getpRicercaImpegnoK().getAnnoEsercizio();
		Integer annoImpegno = req.getpRicercaImpegnoK().getAnnoImpegno();
		BigDecimal numeroImpegno = req.getpRicercaImpegnoK().getNumeroImpegno();
		Ente ente = req.getRichiedente().getAccount().getEnte();

		if(null==req.getpRicercaImpegnoK()){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("RICERCA_IMPEGNO_K"));			
		} else if(null==annoEsercizio && null==annoImpegno && null==numeroImpegno && null==ente){
			checkCondition(false, ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore("NESSUN PARAMETRO INIZIALIZZATO"));
		}  else if(null==annoEsercizio){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ANNO_ESERCIZIO"));
		} else if(null==annoImpegno){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ANNO_IMPEGNO"));
		} else if(null==numeroImpegno){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("NUMERO_IMPEGNO"));
		} else if(null==ente){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ENTE"));
		}
	}	
	
	
	private List<Mutuo> getListaMutuiAssociati(List<String> listaNumeriMutuo){
		
		List<Mutuo> elencoMutui = new ArrayList<Mutuo>();
		if(null!=listaNumeriMutuo && !listaNumeriMutuo.isEmpty()){
			
			for (String numeroMutuo : listaNumeriMutuo) {
				elencoMutui.add(mutuoDad.ricercaMutuo(req.getEnte().getUid(), numeroMutuo, getNow()));
			}
		}
		
		return elencoMutui;	
	}
	
	private List<String> listaNumeriMutuo(List<VoceMutuo> vociMutuo){
		List<String> listaNumeriMutuo = new ArrayList<String>();
		HashMap<String, String> mappa = new HashMap<String, String>();
		
		for (VoceMutuo voceMutuo : vociMutuo) {
		   mappa.put(voceMutuo.getNumeroMutuo(), voceMutuo.getNumeroMutuo());	
		}
		
		Set<Entry<String, String>> setMappa = mappa.entrySet();
		Iterator<Entry<String, String>> itMappa = setMappa.iterator();
		
		while(itMappa.hasNext()){
			
			Entry<String, String> hm = itMappa.next();
			listaNumeriMutuo.add((String)hm.getKey());
		}
		
		return listaNumeriMutuo;
		
	}
}
