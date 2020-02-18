/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitolouscitaprevisione;

import java.util.ArrayList;
import java.util.Date;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaMassivaCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaMassivaCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloUscitaPrevisioneDad;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.ImportiCapitoloUP;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloUPrev;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginataImpl;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaSinteticaMassivaCapitoloUscitaPrevisioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSinteticaMassivaCapitoloUscitaPrevisioneService extends CheckedAccountBaseService<RicercaSinteticaMassivaCapitoloUscitaPrevisione, RicercaSinteticaMassivaCapitoloUscitaPrevisioneResponse> {
	
	/** The mapper. */
	@Autowired
	private Mapper mapper;

	/** The ricerca sintetica capitolo uscita previsione service. */
	@Autowired
	private RicercaSinteticaCapitoloUscitaPrevisioneService ricercaSinteticaCapitoloUscitaPrevisioneService;
	
	/** The capitolo uscita previsione dad. */
	@Autowired
	private CapitoloUscitaPrevisioneDad capitoloUscitaPrevisioneDad;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"), false);
		
		RicercaSinteticaCapitoloUPrev criteri = req.getRicercaSinteticaCapitoloUPrev();
		
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
		capitoloUscitaPrevisioneDad.setEnte(req.getEnte());
		capitoloUscitaPrevisioneDad.setLoginOperazione(loginOperazione);	
		
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional(readOnly= true)
	public RicercaSinteticaMassivaCapitoloUscitaPrevisioneResponse executeService(RicercaSinteticaMassivaCapitoloUscitaPrevisione serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		final String methodName = "execute";
		
		ListaPaginata<CapitoloUscitaPrevisione> listaCapitoloUscitaPrevisione = new ListaPaginataImpl<CapitoloUscitaPrevisione>(new ArrayList<CapitoloUscitaPrevisione>(req.getParametriPaginazione().getElementiPerPagina()));
		countRisultatiAggregati(listaCapitoloUscitaPrevisione);
		
		
		int numeroRisultatiLocali = 0;
		int paginaServizioRemoto = req.getPaginaRemote();
		int posizioneAttualePagina = req.getPosizionePaginaRemote();
		CapitoloUscitaPrevisione capitoloAggregato = null;
		
		ImportiCapitoloUP totaleImporti = capitoloUscitaPrevisioneDad.importiRicercaSintetica(req.getRicercaSinteticaCapitoloUPrev());
		res.setTotaleImporti(totaleImporti);
		
		while (numeroRisultatiLocali <= req.getParametriPaginazione().getElementiPerPagina()) {
			
			RicercaSinteticaCapitoloUscitaPrevisioneResponse ricercaRes = chiamaServizioRicercaSintetica(paginaServizioRemoto);
			
			//esauriti risultati: uscita
			if (ricercaRes.getCapitoli() == null || ricercaRes.getCapitoli().isEmpty()) {
				break;
			}
			
			while (posizioneAttualePagina < ricercaRes.getCapitoli().size()) {
				
				CapitoloUscitaPrevisione capitoloCorrente = ricercaRes.getCapitoli().get(posizioneAttualePagina);
				
				log.debug(methodName, "capitoloCorrente: uid: "+capitoloCorrente.getUid() + " num: " + capitoloCorrente.getNumeroCapitolo() + " art: " + capitoloCorrente.getNumeroArticolo() + " ueb: " + capitoloCorrente.getNumeroUEB());
				
				
				//rottura di livello: prendo il primo capitolo per cui non sommo
				if (verificaCambioCapitolo(capitoloAggregato, capitoloCorrente)) {
					
					numeroRisultatiLocali ++;
					
					//qui devo saltare l'incremento: faccio un bel return?
					if (numeroRisultatiLocali == req.getParametriPaginazione().getElementiPerPagina()+1) {
						restituisciRisultatoAggregazione(
								listaCapitoloUscitaPrevisione,
								paginaServizioRemoto, posizioneAttualePagina);
						return;
					}
					
					capitoloAggregato = capitoloCorrente;

					listaCapitoloUscitaPrevisione.add(capitoloAggregato);
					
				} else {
					//capitolo da sommare
					sommaImporti(capitoloAggregato, capitoloCorrente);
				}
				posizioneAttualePagina ++;
			}
			
			paginaServizioRemoto ++;
			posizioneAttualePagina = 0;
		}
		
		restituisciRisultatoAggregazione(listaCapitoloUscitaPrevisione, paginaServizioRemoto, posizioneAttualePagina);
		
	}
		

	/**
	 * Count risultati aggregati.
	 *
	 * @param listaCapitoloUscitaPrevisione the lista capitolo uscita previsione
	 * @return the long
	 */
	private Long countRisultatiAggregati(ListaPaginata<CapitoloUscitaPrevisione> listaCapitoloUscitaPrevisione) {
		Long numeroRisultatiAggregati = capitoloUscitaPrevisioneDad.countRicercaSinteticaCapitoloUscitaPrevisione(req.getRicercaSinteticaCapitoloUPrev(), req.getParametriPaginazione());
		((ListaPaginataImpl<CapitoloUscitaPrevisione>)listaCapitoloUscitaPrevisione).setTotaleElementi(numeroRisultatiAggregati.intValue());
		((ListaPaginataImpl<CapitoloUscitaPrevisione>)listaCapitoloUscitaPrevisione).setTotalePagine(Utility.calcolaTotalePagine(numeroRisultatiAggregati.intValue(),req.getParametriPaginazione().getElementiPerPagina()));
		return numeroRisultatiAggregati;
	}

	/**
	 * Somma importi.
	 *
	 * @param capitoloAggregato the capitolo aggregato
	 * @param capitoloCorrente the capitolo corrente
	 */
	private void sommaImporti(CapitoloUscitaPrevisione capitoloAggregato,
			CapitoloUscitaPrevisione capitoloCorrente) {
		//XXX
		ImportiCapitoloUP importiSommati0 = sommaImporti(capitoloAggregato.getImportiCapitoloUP(), capitoloCorrente.getImportiCapitoloUP());
		ImportiCapitoloUP importiSommati1 = sommaImporti(capitoloAggregato.getListaImportiCapitolo().get(1), capitoloCorrente.getListaImportiCapitolo().get(1));
		ImportiCapitoloUP importiSommati2 = sommaImporti(capitoloAggregato.getListaImportiCapitolo().get(2), capitoloCorrente.getListaImportiCapitolo().get(2));
		
		capitoloAggregato.setImportiCapitolo(importiSommati0);
		// Pulisco la lista e la riscrivo
		capitoloAggregato.getListaImportiCapitolo().clear();
		capitoloAggregato.getListaImportiCapitolo().add(importiSommati0);
		capitoloAggregato.getListaImportiCapitolo().add(importiSommati1);
		capitoloAggregato.getListaImportiCapitolo().add(importiSommati2);
		
		/*
		if(capitoloAggregato.getImportiCapitoloUP()==null){
			capitoloAggregato.setImportiCapitoloUP(new ImportiCapitoloUP());
		}
		
		if(capitoloCorrente.getImportiCapitoloUP()==null){
			return;
		}
		
		capitoloAggregato.getImportiCapitoloUP().addStanziamento(capitoloCorrente.getImportiCapitoloUP().getStanziamento());
		capitoloAggregato.getImportiCapitoloUP().addStanziamentoCassa(capitoloCorrente.getImportiCapitoloUP().getStanziamentoCassa());
		capitoloAggregato.getImportiCapitoloUP().addStanziamentoCassaIniziale(capitoloCorrente.getImportiCapitoloUP().getStanziamentoCassaIniziale());
		capitoloAggregato.getImportiCapitoloUP().addStanziamentoIniziale(capitoloCorrente.getImportiCapitoloUP().getStanziamentoIniziale());
		capitoloAggregato.getImportiCapitoloUP().addStanziamentoProposto(capitoloCorrente.getImportiCapitoloUP().getStanziamentoProposto());
		capitoloAggregato.getImportiCapitoloUP().addStanziamentoResiduo(capitoloCorrente.getImportiCapitoloUP().getStanziamentoResiduo());
		capitoloAggregato.getImportiCapitoloUP().addStanziamentoResiduoIniziale(capitoloCorrente.getImportiCapitoloUP().getStanziamentoResiduoIniziale());
		capitoloAggregato.getImportiCapitoloUP().addDiCuiImpegnato(capitoloCorrente.getImportiCapitoloUP().getDiCuiImpegnato());
		//capitoloAggregato.getImportiCapitoloUP().addDiCuiImpegnatoAnnoPrec(capitoloCorrente.getImportiCapitoloUP().getDiCuiImpegnatoAnnoPrec());*/
	}
	
	

	// XXX
	/**
	 * Somma importi.
	 *
	 * @param importoAggregato the importo aggregato
	 * @param importoAttuale the importo attuale
	 * @return the importi capitolo up
	 */
	private ImportiCapitoloUP sommaImporti(ImportiCapitoloUP importoAggregato, ImportiCapitoloUP importoAttuale) {
		if(importoAggregato == null){
			importoAggregato = new ImportiCapitoloUP();
		}
		
		if(importoAttuale == null){
			return importoAggregato;
		}
		
		importoAggregato.addStanziamento(importoAttuale.getStanziamento());
		importoAggregato.addStanziamentoCassa(importoAttuale.getStanziamentoCassa());
		importoAggregato.addStanziamentoCassaIniziale(importoAttuale.getStanziamentoCassaIniziale());
		importoAggregato.addStanziamentoIniziale(importoAttuale.getStanziamentoIniziale());
		importoAggregato.addStanziamentoProposto(importoAttuale.getStanziamentoProposto());
		importoAggregato.addStanziamentoResiduo(importoAttuale.getStanziamentoResiduo());
		importoAggregato.addStanziamentoResiduoIniziale(importoAttuale.getStanziamentoResiduoIniziale());
		importoAggregato.addDiCuiImpegnatoAnno1(importoAttuale.getDiCuiImpegnatoAnno1());
		importoAggregato.addDiCuiImpegnatoAnno2(importoAttuale.getDiCuiImpegnatoAnno2());
		importoAggregato.addDiCuiImpegnatoAnno3(importoAttuale.getDiCuiImpegnatoAnno3());
		
		return importoAggregato;
	}

	/**
	 * Restituisci risultato aggregazione.
	 *
	 * @param listaCapitoloUscitaPrevisione the lista capitolo uscita previsione
	 * @param paginaServizioRemoto the pagina servizio remoto
	 * @param posizioneAttualePagina the posizione attuale pagina
	 */
	private void restituisciRisultatoAggregazione(
			ListaPaginata<CapitoloUscitaPrevisione> listaCapitoloUscitaPrevisione,
			int paginaServizioRemoto, int posizioneAttualePagina) {
		res.setCapitoli(listaCapitoloUscitaPrevisione);
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
			CapitoloUscitaPrevisione capitoloAggregato,
			CapitoloUscitaPrevisione capitoloCorrente) {
		return capitoloAggregato == null || !capitoloAggregato.getAnnoCapitolo().equals(capitoloCorrente.getAnnoCapitolo())
			|| !capitoloAggregato.getNumeroCapitolo().equals(capitoloCorrente.getNumeroCapitolo())
			|| !capitoloAggregato.getNumeroArticolo().equals(capitoloCorrente.getNumeroArticolo());
	}

	/**
	 * Chiama servizio ricerca sintetica.
	 *
	 * @param paginaServizioRemoto the pagina servizio remoto
	 * @return the ricerca sintetica capitolo uscita previsione response
	 */
	private RicercaSinteticaCapitoloUscitaPrevisioneResponse chiamaServizioRicercaSintetica(
			int paginaServizioRemoto) {
		RicercaSinteticaCapitoloUscitaPrevisione ricercaReq = new RicercaSinteticaCapitoloUscitaPrevisione();
		ricercaReq.setEnte(req.getEnte());
		ricercaReq.setRichiedente(req.getRichiedente());
		ricercaReq.setDataOra(new Date());
		ricercaReq.setCalcolaTotaleImporti(Boolean.FALSE);
		ParametriPaginazione parametriPaginazione = new ParametriPaginazione();
		parametriPaginazione.setElementiPerPagina(req.getParametriPaginazione().getElementiPerPagina() * 4);
		parametriPaginazione.setNumeroPagina(paginaServizioRemoto);
		ricercaReq.setParametriPaginazione(parametriPaginazione);
		ricercaReq.setRicercaSinteticaCapitoloUPrev(req.getRicercaSinteticaCapitoloUPrev());
		
		RicercaSinteticaCapitoloUscitaPrevisioneResponse ricercaRes = executeExternalService(ricercaSinteticaCapitoloUscitaPrevisioneService, ricercaReq);
		return ricercaRes;
	}

}
