/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitoloentrataprevisione;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaMassivaCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaMassivaCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloEntrataPrevisioneDad;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.ImportiCapitoloEP;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloEPrev;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginataImpl;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

/**
 * The Class RicercaSinteticaMassivaCapitoloEntrataPrevisioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSinteticaMassivaCapitoloEntrataPrevisioneService extends CheckedAccountBaseService<RicercaSinteticaMassivaCapitoloEntrataPrevisione, RicercaSinteticaMassivaCapitoloEntrataPrevisioneResponse> {
	
	/** The Constant RISULTATI_PAGINA_REMOTE. */
	private static final int RISULTATI_PAGINA_REMOTE = 10;
	
	/** The capitolo entrata previsione dad. */
	@Autowired
	private CapitoloEntrataPrevisioneDad capitoloEntrataPrevisioneDad;
	
	/** The ricerca sintetica capitolo entrata previsione service. */
	@Autowired
	private RicercaSinteticaCapitoloEntrataPrevisioneService ricercaSinteticaCapitoloEntrataPrevisioneService;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"), false);
		
		RicercaSinteticaCapitoloEPrev criteri = req.getRicercaSinteticaCapitoloEPrev();
		
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
		capitoloEntrataPrevisioneDad.setEnte(req.getEnte());		
		capitoloEntrataPrevisioneDad.setLoginOperazione(loginOperazione);		
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional(readOnly= true)
	public RicercaSinteticaMassivaCapitoloEntrataPrevisioneResponse executeService(RicercaSinteticaMassivaCapitoloEntrataPrevisione serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		final String methodName = "execute";
		
		ListaPaginata<CapitoloEntrataPrevisione> listaCapitoloEntrataPrevisione = new ListaPaginataImpl<CapitoloEntrataPrevisione>(new ArrayList<CapitoloEntrataPrevisione>(req.getParametriPaginazione().getElementiPerPagina()));
		countRisultatiAggregati(listaCapitoloEntrataPrevisione);
		
		int numeroRisultatiLocali = 0;
		int paginaServizioRemoto = req.getPaginaRemote();
		int posizioneAttualePagina = req.getPosizionePaginaRemote();
		CapitoloEntrataPrevisione capitoloAggregato = null;
		
		ImportiCapitoloEP totaleImporti = capitoloEntrataPrevisioneDad.importiRicercaSintetica(req.getRicercaSinteticaCapitoloEPrev());
		res.setTotaleImporti(totaleImporti);
		
		while (numeroRisultatiLocali <= req.getParametriPaginazione().getElementiPerPagina()) {
			
			RicercaSinteticaCapitoloEntrataPrevisioneResponse ricercaRes = chiamaServizioRicercaSintetica(paginaServizioRemoto);
			
			//esauriti risultati: uscita
			if (ricercaRes.getCapitoli() == null || ricercaRes.getCapitoli().isEmpty()) {
				break;
			}
			
			while (posizioneAttualePagina < ricercaRes.getCapitoli().size()) {
				
				CapitoloEntrataPrevisione capitoloCorrente = ricercaRes.getCapitoli().get(posizioneAttualePagina);
				
				log.debug(methodName, capitoloCorrente.getUid() + " " + capitoloCorrente.getNumeroCapitolo() + " " + capitoloCorrente.getNumeroArticolo() + " " + capitoloCorrente.getNumeroUEB());
				
				//rottura di livello: prendo il primo capitolo per cui non sommo
				if (verificaCambioCapitolo(capitoloAggregato, capitoloCorrente)) {
					
					numeroRisultatiLocali ++;
					
					//qui devo saltare l'incremento: faccio un bel return?
					if (numeroRisultatiLocali == req.getParametriPaginazione().getElementiPerPagina()+1) {
						restituisciRisultatoAggregazione(
								listaCapitoloEntrataPrevisione,
								paginaServizioRemoto, posizioneAttualePagina);
						return;
					}
					
					capitoloAggregato = capitoloCorrente;

					listaCapitoloEntrataPrevisione.add(capitoloAggregato);
					
				} else {
					//capitolo da sommare
					sommaImporti(capitoloAggregato, capitoloCorrente);
				}
				posizioneAttualePagina ++;
			}
			
			paginaServizioRemoto ++;
			posizioneAttualePagina = 0;
		}
		
		restituisciRisultatoAggregazione(listaCapitoloEntrataPrevisione, paginaServizioRemoto, posizioneAttualePagina);
		
	}
	
	/**
	 * Count risultati aggregati.
	 *
	 * @param listaCapitoli the lista capitoli
	 * @return the long
	 */
	private Long countRisultatiAggregati(ListaPaginata<CapitoloEntrataPrevisione> listaCapitoli) {
		Long numeroRisultatiAggregati = capitoloEntrataPrevisioneDad.countRicercaSinteticaCapitoloEntrataPrevisione(req.getRicercaSinteticaCapitoloEPrev(), req.getParametriPaginazione());
		((ListaPaginataImpl<CapitoloEntrataPrevisione>)listaCapitoli).setTotaleElementi(numeroRisultatiAggregati.intValue());
		((ListaPaginataImpl<CapitoloEntrataPrevisione>)listaCapitoli).setTotalePagine(Utility.calcolaTotalePagine(numeroRisultatiAggregati.intValue(),req.getParametriPaginazione().getElementiPerPagina()));
		return numeroRisultatiAggregati;
	}

	/**
	 * Somma importi.
	 *
	 * @param capitoloAggregato the capitolo aggregato
	 * @param capitoloCorrente the capitolo corrente
	 */
	private void sommaImporti(CapitoloEntrataPrevisione capitoloAggregato,
			CapitoloEntrataPrevisione capitoloCorrente) {
		
		//XXX
		ImportiCapitoloEP importiSommati0 = sommaImporti(capitoloAggregato.getImportiCapitoloEP(), capitoloCorrente.getImportiCapitoloEP());
		ImportiCapitoloEP importiSommati1 = sommaImporti(capitoloAggregato.getListaImportiCapitolo().get(1), capitoloCorrente.getListaImportiCapitolo().get(1));
		ImportiCapitoloEP importiSommati2 = sommaImporti(capitoloAggregato.getListaImportiCapitolo().get(2), capitoloCorrente.getListaImportiCapitolo().get(2));
		
		capitoloAggregato.setImportiCapitolo(importiSommati0);
		// Pulisco la lista e la riscrivo
		capitoloAggregato.getListaImportiCapitolo().clear();
		capitoloAggregato.getListaImportiCapitolo().add(importiSommati0);
		capitoloAggregato.getListaImportiCapitolo().add(importiSommati1);
		capitoloAggregato.getListaImportiCapitolo().add(importiSommati2);
		/*
		if(capitoloAggregato.getImportiCapitoloEP()==null){
			capitoloAggregato.setImportiCapitoloEP(new ImportiCapitoloEP());
		}
		
		if(capitoloCorrente.getImportiCapitoloEP()==null){
			return;
		}
		
		capitoloAggregato.getImportiCapitoloEP().addStanziamento(capitoloCorrente.getImportiCapitoloEP().getStanziamento());
		capitoloAggregato.getImportiCapitoloEP().addStanziamentoCassa(capitoloCorrente.getImportiCapitoloEP().getStanziamentoCassa());
		capitoloAggregato.getImportiCapitoloEP().addStanziamentoCassaIniziale(capitoloCorrente.getImportiCapitoloEP().getStanziamentoCassaIniziale());
		capitoloAggregato.getImportiCapitoloEP().addStanziamentoIniziale(capitoloCorrente.getImportiCapitoloEP().getStanziamentoIniziale());
		capitoloAggregato.getImportiCapitoloEP().addStanziamentoProposto(capitoloCorrente.getImportiCapitoloEP().getStanziamentoProposto());
		capitoloAggregato.getImportiCapitoloEP().addStanziamentoResiduo(capitoloCorrente.getImportiCapitoloEP().getStanziamentoResiduo());
		capitoloAggregato.getImportiCapitoloEP().addStanziamentoResiduoIniziale(capitoloCorrente.getImportiCapitoloEP().getStanziamentoResiduoIniziale());
		capitoloAggregato.getImportiCapitoloEP().addDiCuiAccertato(capitoloCorrente.getImportiCapitoloEP().getDiCuiAccertato());
		//capitoloAggregato.getImportiCapitoloEP().addDiCuiAccertatoAnnoPrec(capitoloCorrente.getImportiCapitoloEP().getDiCuiAccertatoAnnoPrec());*/
	}

	// XXX
	/**
	 * Somma importi.
	 *
	 * @param aggregato the aggregato
	 * @param corrente the corrente
	 * @return the importi capitolo ep
	 */
	private ImportiCapitoloEP sommaImporti(ImportiCapitoloEP aggregato, ImportiCapitoloEP corrente) {
		if(aggregato == null){
			aggregato = new ImportiCapitoloEP();
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
		aggregato.addDiCuiAccertatoAnno1(corrente.getDiCuiAccertatoAnno1());
		aggregato.addDiCuiAccertatoAnno2(corrente.getDiCuiAccertatoAnno2());
		aggregato.addDiCuiAccertatoAnno3(corrente.getDiCuiAccertatoAnno3());
		//aggregato.addDiCuiAccertatoAnnoPrec(corrente.getDiCuiAccertatoAnnoPrec());
		return aggregato;
	}

	/**
	 * Restituisci risultato aggregazione.
	 *
	 * @param listaCapitoloEntrataPrevisione the lista capitolo entrata previsione
	 * @param paginaServizioRemoto the pagina servizio remoto
	 * @param posizioneAttualePagina the posizione attuale pagina
	 */
	private void restituisciRisultatoAggregazione(
			ListaPaginata<CapitoloEntrataPrevisione> listaCapitoloEntrataPrevisione,
			int paginaServizioRemoto, int posizioneAttualePagina) {
		
		res.setCapitoli(listaCapitoloEntrataPrevisione);
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
	 * @return the ricerca sintetica capitolo entrata previsione response
	 */
	private RicercaSinteticaCapitoloEntrataPrevisioneResponse chiamaServizioRicercaSintetica(
			int paginaServizioRemoto) {
		RicercaSinteticaCapitoloEntrataPrevisione ricercaReq = new RicercaSinteticaCapitoloEntrataPrevisione();
		ricercaReq.setEnte(req.getEnte());
		ricercaReq.setRichiedente(req.getRichiedente());
		ricercaReq.setDataOra(new Date());
		ricercaReq.setCalcolaTotaleImporti(Boolean.FALSE);
		ParametriPaginazione parametriPaginazione = new ParametriPaginazione();
		parametriPaginazione.setElementiPerPagina(RISULTATI_PAGINA_REMOTE);
		parametriPaginazione.setNumeroPagina(paginaServizioRemoto);
		ricercaReq.setParametriPaginazione(parametriPaginazione);
		ricercaReq.setRicercaSinteticaCapitoloEPrev(req.getRicercaSinteticaCapitoloEPrev());
		
		RicercaSinteticaCapitoloEntrataPrevisioneResponse ricercaRes = executeExternalService(ricercaSinteticaCapitoloEntrataPrevisioneService, ricercaReq);
		return ricercaRes;
	}

}
