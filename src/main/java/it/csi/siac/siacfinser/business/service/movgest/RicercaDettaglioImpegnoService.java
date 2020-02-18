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

import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.business.service.common.RicercaAttributiMovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaDettaglioImpegno;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaDettaglioImpegnoResponse;
import it.csi.siac.siacfinser.integration.dad.MutuoDad;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.mutuo.Mutuo;
import it.csi.siac.siacfinser.model.mutuo.VoceMutuo;


@Deprecated
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDettaglioImpegnoService extends RicercaAttributiMovimentoGestioneService<RicercaDettaglioImpegno,RicercaDettaglioImpegnoResponse> {
	
	@Autowired
	MutuoDad mutuoDad;
	
	@Override
	protected void init() {
		final String methodName = "RicercaDettaglioImpegnoService : init()";
		log.debug(methodName, "- Begin");
	}	
	
	
	@Override
	@Transactional(readOnly=true)
	public RicercaDettaglioImpegnoResponse executeService(RicercaDettaglioImpegno serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	public void execute() {
		final String methodName = "RicercaDettaglioImpegnoService : execute()";
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
		impegno = (Impegno) impegnoDad.ricercaImpegnoDettaglio(richiedente, ente, annoEsercizio, annoImpegno, numeroImpegno, Constanti.MOVGEST_TIPO_IMPEGNO, true, false);
		
		if(null!=impegno){
			//si invoca il metodo completaDatiRicercaImpegnoPk che si occupa di vestire i dati ottenuti:
			// filtrare senza classificatori
			CapitoloUscitaGestione capitolo = impegno.getCapitoloUscitaGestione();
			
			RicercaSinteticaCapitoloUscitaGestione capugReq = buildRequestPerRicercaSinteticaCapitoloUG(req.getEnte(), req.getRichiedente(), req.getpRicercaImpegnoK().getAnnoEsercizio(),
					capitolo.getAnnoCapitolo(), capitolo.getNumeroCapitolo(),
					capitolo.getNumeroArticolo(), capitolo.getNumeroUEB());
			
			// imposto filtri per classificatori e importi
			capugReq.setTipologieClassificatoriRichiesti(TipologiaClassificatore.MISSIONE, TipologiaClassificatore.PROGRAMMA, 
					TipologiaClassificatore.PDC, TipologiaClassificatore.PDC_I,
					TipologiaClassificatore.PDC_II, TipologiaClassificatore.PDC_III, TipologiaClassificatore.PDC_IV, 
					TipologiaClassificatore.PDC_V, TipologiaClassificatore.CDC, TipologiaClassificatore.CDR, 
					TipologiaClassificatore.TIPO_FINANZIAMENTO);
			
			// disponibilitaImpegnareAnno1-2-3 è il disponibile
			// lo stanziamento non è un derivato
			capugReq.setImportiDerivatiRichiesti(ImportiCapitoloEnum.disponibilitaImpegnareAnno1, ImportiCapitoloEnum.disponibilitaImpegnareAnno2,ImportiCapitoloEnum.disponibilitaImpegnareAnno3);
			
			RicercaSinteticaCapitoloUscitaGestioneResponse response = capitoloUscitaGestioneService.ricercaSinteticaCapitoloUscitaGestione(capugReq);
			capitolo = response.getCapitoli().get(0);
			
			impegno.setCapitoloUscitaGestione(capitolo);
			
			
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
				
				}
			}	
			
			//componiamo la respose esito positivo:
			res.setImpegno(impegno);
			res.setBilancio(impegno.getCapitoloUscitaGestione().getBilancio());
			res.setCapitoloUscitaGestione(impegno.getCapitoloUscitaGestione());
			res.setEsito(Esito.SUCCESSO);
			
			
		}// verificare cosa arriva al client se l'impegno è nullo!
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
