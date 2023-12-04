/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitolouscitagestione;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaMassivaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaMassivaCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloUscitaGestioneDad;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.ImportiCapitoloUG;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloUGest;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginataImpl;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

/**
 * The Class RicercaSinteticaMassivaCapitoloUscitaGestioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSinteticaMassivaCapitoloUscitaGestioneService extends CheckedAccountBaseService<RicercaSinteticaMassivaCapitoloUscitaGestione, RicercaSinteticaMassivaCapitoloUscitaGestioneResponse> {
	
	/** The Constant RISULTATI_PAGINA_REMOTE. */
	private static final int RISULTATI_PAGINA_REMOTE = 10;

	/** The capitolo uscita gestione dad. */
	@Autowired
	private CapitoloUscitaGestioneDad capitoloUscitaGestioneDad;
	
	
	/** The ricerca sintetica capitolo uscita gestione service. */
	@Autowired
	private RicercaSinteticaCapitoloUscitaGestioneService ricercaSinteticaCapitoloUscitaGestioneService;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"), false);
		
		RicercaSinteticaCapitoloUGest criteri = req.getRicercaSinteticaCapitoloUGest();
		
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
		capitoloUscitaGestioneDad.setEnte(req.getEnte());		
		capitoloUscitaGestioneDad.setLoginOperazione(loginOperazione);		
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional(readOnly= true)
	public RicercaSinteticaMassivaCapitoloUscitaGestioneResponse executeService(RicercaSinteticaMassivaCapitoloUscitaGestione serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		final String methodName = "execute";
		
		ListaPaginata<CapitoloUscitaGestione> listaCapitoloUscitaGestione = new ListaPaginataImpl<CapitoloUscitaGestione>(new ArrayList<CapitoloUscitaGestione>(req.getParametriPaginazione().getElementiPerPagina()));
		countRisultatiAggregati(listaCapitoloUscitaGestione);
		
		
		int numeroRisultatiLocali = 0;
		int paginaServizioRemoto = req.getPaginaRemote();
		int posizioneAttualePagina = req.getPosizionePaginaRemote();
		CapitoloUscitaGestione capitoloAggregato = null;
		
		ImportiCapitoloUG totaleImporti = capitoloUscitaGestioneDad.importiRicercaSintetica(req.getRicercaSinteticaCapitoloUGest());
		res.setTotaleImporti(totaleImporti);
		
		while (numeroRisultatiLocali <= req.getParametriPaginazione().getElementiPerPagina()) {
			
			RicercaSinteticaCapitoloUscitaGestioneResponse ricercaRes = chiamaServizioRicercaSintetica(paginaServizioRemoto);
			
			//esauriti risultati: uscita
			if (ricercaRes.getCapitoli() == null || ricercaRes.getCapitoli().isEmpty()) {
				break;
			}
			
			while (posizioneAttualePagina < ricercaRes.getCapitoli().size()) {
				
				CapitoloUscitaGestione capitoloCorrente = ricercaRes.getCapitoli().get(posizioneAttualePagina);
				
				log.debug(methodName, capitoloCorrente.getUid() + " " + capitoloCorrente.getNumeroCapitolo() + " " + capitoloCorrente.getNumeroArticolo() + " " + capitoloCorrente.getNumeroUEB());
				
				//rottura di livello: prendo il primo capitolo per cui non sommo
				if (verificaCambioCapitolo(capitoloAggregato, capitoloCorrente)) {
					
					numeroRisultatiLocali ++;
					
					//qui devo saltare l'incremento: faccio un bel return?
					if (numeroRisultatiLocali == req.getParametriPaginazione().getElementiPerPagina()+1) {
						restituisciRisultatoAggregazione(
								listaCapitoloUscitaGestione,
								paginaServizioRemoto, posizioneAttualePagina);
						return;
					}
					
					capitoloAggregato = capitoloCorrente;

					listaCapitoloUscitaGestione.add(capitoloAggregato);
					
				} else {
					//capitolo da sommare
					sommaImporti(capitoloAggregato, capitoloCorrente);
				}
				posizioneAttualePagina ++;
			}
			
			paginaServizioRemoto ++;
			posizioneAttualePagina = 0;
		}
		
		restituisciRisultatoAggregazione(listaCapitoloUscitaGestione, paginaServizioRemoto, posizioneAttualePagina);
		
	}
	
	/**
	 * Count risultati aggregati.
	 *
	 * @param listaCapitoli the lista capitoli
	 * @return the long
	 */
	private Long countRisultatiAggregati(ListaPaginata<CapitoloUscitaGestione> listaCapitoli) {
		Long numeroRisultatiAggregati = capitoloUscitaGestioneDad.countRicercaSinteticaCapitoloUscitaGestione(req.getRicercaSinteticaCapitoloUGest(), req.getParametriPaginazione());
		((ListaPaginataImpl<CapitoloUscitaGestione>)listaCapitoli).setTotaleElementi(numeroRisultatiAggregati.intValue());
		((ListaPaginataImpl<CapitoloUscitaGestione>)listaCapitoli).setTotalePagine(Utility.calcolaTotalePagine(numeroRisultatiAggregati.intValue(),req.getParametriPaginazione().getElementiPerPagina()));
		return numeroRisultatiAggregati;
	}

	/**
	 * Somma importi.
	 *
	 * @param capitoloAggregato the capitolo aggregato
	 * @param capitoloCorrente the capitolo corrente
	 */
	private void sommaImporti(CapitoloUscitaGestione capitoloAggregato, CapitoloUscitaGestione capitoloCorrente) {
		
		ImportiCapitoloUG importiSommati0 = sommaImporti(capitoloAggregato.getImportiCapitolo(), capitoloCorrente.getImportiCapitoloUG());
		ImportiCapitoloUG importiSommati1 = sommaImporti(capitoloAggregato.getListaImportiCapitolo().get(1), capitoloCorrente.getListaImportiCapitolo().get(1));
		ImportiCapitoloUG importiSommati2 = sommaImporti(capitoloAggregato.getListaImportiCapitolo().get(2), capitoloCorrente.getListaImportiCapitolo().get(2));
		
		capitoloAggregato.setImportiCapitolo(importiSommati0);
		// Pulisco la lista e la riscrivo
		capitoloAggregato.getListaImportiCapitolo().clear();
		capitoloAggregato.getListaImportiCapitolo().add(importiSommati0);
		capitoloAggregato.getListaImportiCapitolo().add(importiSommati1);
		capitoloAggregato.getListaImportiCapitolo().add(importiSommati2);
		/*
		if(capitoloAggregato.getImportiCapitoloUG()==null){
			capitoloAggregato.setImportiCapitoloUG(new ImportiCapitoloUG());
		}
		
		if(capitoloCorrente.getImportiCapitoloUG()==null){
			return;
		}
		
		capitoloAggregato.getImportiCapitoloUG().addStanziamento(capitoloCorrente.getImportiCapitoloUG().getStanziamento());
		capitoloAggregato.getImportiCapitoloUG().addStanziamentoCassa(capitoloCorrente.getImportiCapitoloUG().getStanziamentoCassa());
		capitoloAggregato.getImportiCapitoloUG().addStanziamentoCassaIniziale(capitoloCorrente.getImportiCapitoloUG().getStanziamentoCassaIniziale());
		capitoloAggregato.getImportiCapitoloUG().addStanziamentoIniziale(capitoloCorrente.getImportiCapitoloUG().getStanziamentoIniziale());
		capitoloAggregato.getImportiCapitoloUG().addStanziamentoProposto(capitoloCorrente.getImportiCapitoloUG().getStanziamentoProposto());
		capitoloAggregato.getImportiCapitoloUG().addStanziamentoResiduo(capitoloCorrente.getImportiCapitoloUG().getStanziamentoResiduo());
		capitoloAggregato.getImportiCapitoloUG().addStanziamentoResiduoIniziale(capitoloCorrente.getImportiCapitoloUG().getStanziamentoResiduoIniziale());
		capitoloAggregato.getImportiCapitoloUG().addStanziamentoAsset(capitoloCorrente.getImportiCapitoloUG().getStanziamentoAsset());
		capitoloAggregato.getImportiCapitoloUG().addStanziamentoCassaAsset(capitoloCorrente.getImportiCapitoloUG().getStanziamentoCassaAsset());
		capitoloAggregato.getImportiCapitoloUG().addStanziamentoResAsset(capitoloCorrente.getImportiCapitoloUG().getStanziamentoResAsset());*/
	}

	/**
	 * Somma importi.
	 *
	 * @param aggregato the aggregato
	 * @param corrente the corrente
	 * @return the importi capitolo ug
	 */
	private ImportiCapitoloUG sommaImporti(ImportiCapitoloUG aggregato, ImportiCapitoloUG corrente) {
		if(aggregato==null){
			aggregato = new ImportiCapitoloUG();
		}
		
		if(corrente==null){
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
	 * @param listaCapitoloUscitaGestione the lista capitolo uscita gestione
	 * @param paginaServizioRemoto the pagina servizio remoto
	 * @param posizioneAttualePagina the posizione attuale pagina
	 */
	private void restituisciRisultatoAggregazione(
			ListaPaginata<CapitoloUscitaGestione> listaCapitoloUscitaGestione,
			int paginaServizioRemoto, int posizioneAttualePagina) {
		res.setCapitoli(listaCapitoloUscitaGestione);
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
	private boolean verificaCambioCapitolo(
			CapitoloUscitaGestione capitoloAggregato,
			CapitoloUscitaGestione capitoloCorrente) {
		return capitoloAggregato == null || !capitoloAggregato.getAnnoCapitolo().equals(capitoloCorrente.getAnnoCapitolo())
			|| !capitoloAggregato.getNumeroCapitolo().equals(capitoloCorrente.getNumeroCapitolo())
			|| !capitoloAggregato.getNumeroArticolo().equals(capitoloCorrente.getNumeroArticolo());
	}

	/**
	 * Chiama servizio ricerca sintetica.
	 *
	 * @param paginaServizioRemoto the pagina servizio remoto
	 * @return the ricerca sintetica capitolo uscita gestione response
	 */
	private RicercaSinteticaCapitoloUscitaGestioneResponse chiamaServizioRicercaSintetica(
			int paginaServizioRemoto) {
		RicercaSinteticaCapitoloUscitaGestione ricercaReq = new RicercaSinteticaCapitoloUscitaGestione();
		ricercaReq.setEnte(req.getEnte());
		ricercaReq.setRichiedente(req.getRichiedente());
		ricercaReq.setDataOra(new Date());
		ricercaReq.setCalcolaTotaleImporti(Boolean.FALSE);
		ParametriPaginazione parametriPaginazione = new ParametriPaginazione();
		parametriPaginazione.setElementiPerPagina(RISULTATI_PAGINA_REMOTE);
		parametriPaginazione.setNumeroPagina(paginaServizioRemoto);
		ricercaReq.setParametriPaginazione(parametriPaginazione);
		ricercaReq.setRicercaSinteticaCapitoloUGest(req.getRicercaSinteticaCapitoloUGest());
		
		RicercaSinteticaCapitoloUscitaGestioneResponse ricercaRes = executeExternalService(ricercaSinteticaCapitoloUscitaGestioneService, ricercaReq);
		return ricercaRes;
	}

}
