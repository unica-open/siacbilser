/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitoloentratagestione;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaMassivaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaMassivaCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloEntrataGestioneDad;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.ImportiCapitoloEG;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloEGest;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginataImpl;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

/**
 * The Class RicercaSinteticaMassivaCapitoloEntrataGestioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSinteticaMassivaCapitoloEntrataGestioneService extends CheckedAccountBaseService<RicercaSinteticaMassivaCapitoloEntrataGestione, RicercaSinteticaMassivaCapitoloEntrataGestioneResponse> {
	
	/** The Constant RISULTATI_PAGINA_REMOTE. */
	private static final int RISULTATI_PAGINA_REMOTE = 10;
	
//	@Autowired
//	private Mapper mapper;
	
	/** The capitolo entrata gestione dad. */
	@Autowired
	private CapitoloEntrataGestioneDad capitoloEntrataGestioneDad;
	
//	@Autowired
//	private ClassificatoriDad classificatoriDad; //siacTClassDao
	
	/** The ricerca sintetica capitolo entrata gestione service. */
	@Autowired
	private RicercaSinteticaCapitoloEntrataGestioneService ricercaSinteticaCapitoloEntrataGestioneService;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"), false);
		
		RicercaSinteticaCapitoloEGest criteri = req.getRicercaSinteticaCapitoloEntrata();
		
		checkNotNull(criteri, ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());
		
		boolean valorizzatoNumeroArticolo = criteri.getNumeroArticolo() != null && criteri.getNumeroArticolo() != 0;
		boolean valorizzatoNumeroCapitolo = criteri.getNumeroCapitolo() != null && criteri.getNumeroCapitolo() != 0;
		
		boolean valorizzatoSoloNumeroArticolo = valorizzatoNumeroArticolo && !valorizzatoNumeroCapitolo;
		
		checkCondition(!valorizzatoSoloNumeroArticolo, ErroreBil.CAPITOLO_OBBLIGATORIO_PER_ARTICOLO.getErrore());
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		capitoloEntrataGestioneDad.setEnte(req.getEnte());		
		capitoloEntrataGestioneDad.setLoginOperazione(loginOperazione);	
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional(readOnly= true)
	public RicercaSinteticaMassivaCapitoloEntrataGestioneResponse executeService(RicercaSinteticaMassivaCapitoloEntrataGestione serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		
		ListaPaginata<CapitoloEntrataGestione> listaCapitoloEntrataGestione = new ListaPaginataImpl<CapitoloEntrataGestione>(new ArrayList<CapitoloEntrataGestione>(req.getParametriPaginazione().getElementiPerPagina()));
		countRisultatiAggregati(listaCapitoloEntrataGestione);
		
		int numeroRisultatiLocali = 0;
		int paginaServizioRemoto = req.getPaginaRemote();
		int posizioneAttualePagina = req.getPosizionePaginaRemote();
		CapitoloEntrataGestione capitoloAggregato = null;
		
		ImportiCapitoloEG totaleImporti = capitoloEntrataGestioneDad.importiRicercaSintetica(req.getRicercaSinteticaCapitoloEntrata());
		res.setTotaleImporti(totaleImporti);
		
		while (numeroRisultatiLocali <= req.getParametriPaginazione().getElementiPerPagina()) {
			
			RicercaSinteticaCapitoloEntrataGestioneResponse ricercaRes = chiamaServizioRicercaSintetica(paginaServizioRemoto);
			
			//esauriti risultati: uscita
			if (ricercaRes.getCapitoli() == null || ricercaRes.getCapitoli().isEmpty()) {
				break;
			}
			
			while (posizioneAttualePagina < ricercaRes.getCapitoli().size()) {
				
				CapitoloEntrataGestione capitoloCorrente = ricercaRes.getCapitoli().get(posizioneAttualePagina);
				
				log.debug("execute", capitoloCorrente.getUid() + " " + capitoloCorrente.getNumeroCapitolo() + " " + capitoloCorrente.getNumeroArticolo() + " " + capitoloCorrente.getNumeroUEB());
				
				//rottura di livello: prendo il primo capitolo per cui non sommo
				if (verificaCambioCapitolo(capitoloAggregato, capitoloCorrente)) {
					
					numeroRisultatiLocali ++;
					
					//qui devo saltare l'incremento: faccio un bel return?
					if (numeroRisultatiLocali == req.getParametriPaginazione().getElementiPerPagina()+1) {
						restituisciRisultatoAggregazione(
								listaCapitoloEntrataGestione,
								paginaServizioRemoto, posizioneAttualePagina);
						return;
					}
					
					capitoloAggregato = capitoloCorrente;

					listaCapitoloEntrataGestione.add(capitoloAggregato);
					
				} else {
					//capitolo da sommare
					sommaImporti(capitoloAggregato, capitoloCorrente);
				}
				posizioneAttualePagina ++;
			}
			
			paginaServizioRemoto ++;
			posizioneAttualePagina = 0;
		}
		
		restituisciRisultatoAggregazione(listaCapitoloEntrataGestione, paginaServizioRemoto, posizioneAttualePagina);
		
	}
	
	/**
	 * Count risultati aggregati.
	 *
	 * @param listaCapitoli the lista capitoli
	 * @return the long
	 */
	private Long countRisultatiAggregati(ListaPaginata<CapitoloEntrataGestione> listaCapitoli) {
		Long numeroRisultatiAggregati = capitoloEntrataGestioneDad.countRicercaSinteticaCapitoloEntrataGestione(req.getRicercaSinteticaCapitoloEntrata(), req.getParametriPaginazione());
		((ListaPaginataImpl<CapitoloEntrataGestione>)listaCapitoli).setTotaleElementi(numeroRisultatiAggregati.intValue());
		((ListaPaginataImpl<CapitoloEntrataGestione>)listaCapitoli).setTotalePagine(Utility.calcolaTotalePagine(numeroRisultatiAggregati.intValue(),req.getParametriPaginazione().getElementiPerPagina()));
		return numeroRisultatiAggregati;
	}
	
	/**
	 * Somma importi.
	 *
	 * @param capitoloAggregato the capitolo aggregato
	 * @param capitoloCorrente the capitolo corrente
	 */
	private void sommaImporti(CapitoloEntrataGestione capitoloAggregato,
			CapitoloEntrataGestione capitoloCorrente) {
		
		//XXX
		ImportiCapitoloEG importiSommati0 = sommaImporti(capitoloAggregato.getImportiCapitoloEG(), capitoloCorrente.getImportiCapitoloEG());
		ImportiCapitoloEG importiSommati1 = sommaImporti(capitoloAggregato.getListaImportiCapitolo().get(1), capitoloCorrente.getListaImportiCapitolo().get(1));
		ImportiCapitoloEG importiSommati2 = sommaImporti(capitoloAggregato.getListaImportiCapitolo().get(2), capitoloCorrente.getListaImportiCapitolo().get(2));
		
		capitoloAggregato.setImportiCapitolo(importiSommati0);
		// Pulisco la lista e la riscrivo
		capitoloAggregato.getListaImportiCapitolo().clear();
		capitoloAggregato.getListaImportiCapitolo().add(importiSommati0);
		capitoloAggregato.getListaImportiCapitolo().add(importiSommati1);
		capitoloAggregato.getListaImportiCapitolo().add(importiSommati2);
		/*
		if(capitoloAggregato.getImportiCapitoloEG()==null){
			capitoloAggregato.setImportiCapitoloEG(new ImportiCapitoloEG());
		}
		
		if(capitoloCorrente.getImportiCapitoloEG()==null){
			return;
		}
		
		capitoloAggregato.getImportiCapitoloEG().addStanziamento(capitoloCorrente.getImportiCapitoloEG().getStanziamento());
		capitoloAggregato.getImportiCapitoloEG().addStanziamentoCassa(capitoloCorrente.getImportiCapitoloEG().getStanziamentoCassa());
		capitoloAggregato.getImportiCapitoloEG().addStanziamentoCassaIniziale(capitoloCorrente.getImportiCapitoloEG().getStanziamentoCassaIniziale());
		capitoloAggregato.getImportiCapitoloEG().addStanziamentoIniziale(capitoloCorrente.getImportiCapitoloEG().getStanziamentoIniziale());
		capitoloAggregato.getImportiCapitoloEG().addStanziamentoProposto(capitoloCorrente.getImportiCapitoloEG().getStanziamentoProposto());
		capitoloAggregato.getImportiCapitoloEG().addStanziamentoResiduo(capitoloCorrente.getImportiCapitoloEG().getStanziamentoResiduo());
		capitoloAggregato.getImportiCapitoloEG().addStanziamentoResiduoIniziale(capitoloCorrente.getImportiCapitoloEG().getStanziamentoResiduoIniziale());
		capitoloAggregato.getImportiCapitoloEG().addStanziamentoAsset(capitoloCorrente.getImportiCapitoloEG().getStanziamentoAsset());
		capitoloAggregato.getImportiCapitoloEG().addStanziamentoCassaAsset(capitoloCorrente.getImportiCapitoloEG().getStanziamentoCassaAsset());
		capitoloAggregato.getImportiCapitoloEG().addStanziamentoResAsset(capitoloCorrente.getImportiCapitoloEG().getStanziamentoResAsset());*/
	}

	// XXX
	/**
	 * Somma importi.
	 *
	 * @param aggregato the aggregato
	 * @param corrente the corrente
	 * @return the importi capitolo eg
	 */
	private ImportiCapitoloEG sommaImporti(ImportiCapitoloEG aggregato, ImportiCapitoloEG corrente) {
		if(aggregato==null){
			aggregato = new ImportiCapitoloEG();
		}
		
		if(corrente == null){
			return aggregato;
		}
		
		aggregato.addStanziamento(corrente.getStanziamento());
		aggregato.addStanziamentoCassa(corrente.getStanziamentoCassa());
		aggregato.addStanziamentoCassaIniziale(corrente.getStanziamentoCassaIniziale());
		aggregato.addStanziamentoIniziale(corrente.getStanziamentoIniziale());
		aggregato.addStanziamentoProposto(corrente.getStanziamentoProposto());
		aggregato.addStanziamentoResiduo(corrente.getStanziamentoResiduo());
		aggregato.addStanziamentoResiduoIniziale(corrente.getStanziamentoResiduoIniziale());
		aggregato.addStanziamentoAsset(corrente.getStanziamentoAsset());
		aggregato.addStanziamentoCassaAsset(corrente.getStanziamentoCassaAsset());
		aggregato.addStanziamentoResAsset(corrente.getStanziamentoResAsset());
		return aggregato;
	}

	/**
	 * Restituisci risultato aggregazione.
	 *
	 * @param listaCapitoloEntrataGestione the lista capitolo entrata gestione
	 * @param paginaServizioRemoto the pagina servizio remoto
	 * @param posizioneAttualePagina the posizione attuale pagina
	 */
	private void restituisciRisultatoAggregazione(
			ListaPaginata<CapitoloEntrataGestione> listaCapitoloEntrataGestione,
			int paginaServizioRemoto, int posizioneAttualePagina) {
		
		res.setCapitoli(listaCapitoloEntrataGestione);
		res.setEsito(Esito.SUCCESSO);
		res.setPaginaRemote(paginaServizioRemoto);
		res.setPosizionePaginaRemote(posizioneAttualePagina);
	}

	/**
	 * Verifica cambio capitolo.
	 *
	 * @param capitoloAggregato the capitolo aggregato
	 * @param capitoloCorrente the capitolo corrente
	 * @return true, if successful
	 */
	@SuppressWarnings("rawtypes")
	private boolean verificaCambioCapitolo(
		    Capitolo capitoloAggregato,
			Capitolo capitoloCorrente) {
		return capitoloAggregato == null || !capitoloAggregato.getAnnoCapitolo().equals(capitoloCorrente.getAnnoCapitolo())
			|| !capitoloAggregato.getNumeroCapitolo().equals(capitoloCorrente.getNumeroCapitolo())
			|| !capitoloAggregato.getNumeroArticolo().equals(capitoloCorrente.getNumeroArticolo());
	}

	/**
	 * Chiama servizio ricerca sintetica.
	 *
	 * @param paginaServizioRemoto the pagina servizio remoto
	 * @return the ricerca sintetica capitolo entrata gestione response
	 */
	private RicercaSinteticaCapitoloEntrataGestioneResponse chiamaServizioRicercaSintetica(
			int paginaServizioRemoto) {
		RicercaSinteticaCapitoloEntrataGestione ricercaReq = new RicercaSinteticaCapitoloEntrataGestione();
		ricercaReq.setEnte(req.getEnte());
		ricercaReq.setRichiedente(req.getRichiedente());
		ricercaReq.setDataOra(new Date());
		ricercaReq.setCalcolaTotaleImporti(Boolean.FALSE);
		ParametriPaginazione parametriPaginazione = new ParametriPaginazione();
		parametriPaginazione.setElementiPerPagina(RISULTATI_PAGINA_REMOTE);
		parametriPaginazione.setNumeroPagina(paginaServizioRemoto);
		ricercaReq.setParametriPaginazione(parametriPaginazione);
		ricercaReq.setRicercaSinteticaCapitoloEntrata(req.getRicercaSinteticaCapitoloEntrata());
		
		RicercaSinteticaCapitoloEntrataGestioneResponse ricercaRes = executeExternalService(ricercaSinteticaCapitoloEntrataGestioneService, ricercaReq);
		return ricercaRes;
	}

}
