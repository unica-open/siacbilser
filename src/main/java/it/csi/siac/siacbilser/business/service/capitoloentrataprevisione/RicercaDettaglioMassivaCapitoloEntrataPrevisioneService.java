/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitoloentrataprevisione;

import java.util.Date;
import java.util.List;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloEntrataPrevisioneDad;
import it.csi.siac.siacbilser.integration.dad.ImportiCapitoloDad;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.CapitoloMassivaEntrataPrevisione;
import it.csi.siac.siacbilser.model.ImportiCapitoloEP;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloEPrev;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

/**
 * The Class RicercaDettaglioMassivaCapitoloEntrataPrevisioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDettaglioMassivaCapitoloEntrataPrevisioneService extends CheckedAccountBaseService<RicercaDettaglioMassivaCapitoloEntrataPrevisione, RicercaDettaglioMassivaCapitoloEntrataPrevisioneResponse> {
	
	/** The Constant RISULTATI_PAGINA_REMOTE. */
	private static final int RISULTATI_PAGINA_REMOTE = 10;
	
	/** The mapper. */
	@Autowired
	private Mapper mapper;

	/** The capitolo entrata previsione dad. */
	@Autowired
	private CapitoloEntrataPrevisioneDad capitoloEntrataPrevisioneDad;
	
	/** The importi capitolo dad. */
	@Autowired
	private ImportiCapitoloDad importiCapitoloDad;
	
	/** The ricerca sintetica capitolo entrata previsione service. */
	@Autowired
	private RicercaSinteticaCapitoloEntrataPrevisioneService ricercaSinteticaCapitoloEntrataPrevisioneService;
	
	/** The ricerca dettaglio capitolo entrata previsione service. */
	@Autowired
	private RicercaDettaglioCapitoloEntrataPrevisioneService ricercaDettaglioCapitoloEntrataPrevisioneService;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getRicercaSinteticaCapitoloEPrev().getAnnoCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno capitolo"));
		checkNotNull(req.getRicercaSinteticaCapitoloEPrev().getNumeroCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero capitolo"));
		checkNotNull(req.getRicercaSinteticaCapitoloEPrev().getNumeroArticolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero articolo"));
		checkNotNull(req.getRicercaSinteticaCapitoloEPrev().getAnnoEsercizio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno esercizio"));
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {		
		capitoloEntrataPrevisioneDad.setEnte(req.getEnte());		
		capitoloEntrataPrevisioneDad.setLoginOperazione(loginOperazione);		
		importiCapitoloDad.setEnte(req.getEnte());
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional(readOnly= true)
	public RicercaDettaglioMassivaCapitoloEntrataPrevisioneResponse executeService(RicercaDettaglioMassivaCapitoloEntrataPrevisione serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		final String methodName = "execute";

		int pagina = 0;
		
		RicercaSinteticaCapitoloEntrataPrevisioneResponse ricercaRes = null;
		
		CapitoloMassivaEntrataPrevisione capitoloAggregato = null;
		
		do {
		
			ricercaRes = chiamaServizioRicercaSintetica(pagina);
			
			for (CapitoloEntrataPrevisione capitoloCorrente : ricercaRes.getCapitoli()) {
					
				//appena entrato?
				RicercaDettaglioCapitoloEntrataPrevisioneResponse capitoloCorrenteDettaglio = chiamaServizioDettaglio(capitoloCorrente);
				
				log.debug(methodName, capitoloCorrente.getUid() + " " + capitoloCorrente.getNumeroCapitolo() + " " + capitoloCorrente.getNumeroArticolo() + " " + capitoloCorrente.getNumeroUEB());
				
				if (capitoloAggregato == null) {
					capitoloAggregato = getDettaglioMassivoDaResponseDettaglio(capitoloCorrente, capitoloCorrenteDettaglio);
				} else {
					//sommo
					//devo ciclare gli importi del capitolo corrente e trovare il corrispondente nell'aggregato
					for (ImportiCapitoloEP importiDaSommare : capitoloCorrenteDettaglio.getListaImportiCapitoloEP()) {
						ImportiCapitoloEP importoAggregato = cercaImportoCorrispondente(importiDaSommare, capitoloAggregato.getListaImportiCapitoloEP());
						//una volta trovata la corrispondenza devo sommare
						sommaImporti(importoAggregato, importiDaSommare);
					}
				}
				capitoloAggregato.addCapitolo(capitoloCorrenteDettaglio.getCapitoloEntrataPrevisione());
					
			}
			pagina ++;
		} while (!ricercaRes.getCapitoli().isEmpty());
		
		res.setCapitoloMassivaEntrataPrevisione(capitoloAggregato);
		res.setEsito(Esito.SUCCESSO);
	}

	/**
	 * Cerca importo corrispondente.
	 *
	 * @param importiRicerca the importi ricerca
	 * @param listaImportiCapitoloEP the lista importi capitolo ep
	 * @return the importi capitolo ep
	 */
	private ImportiCapitoloEP cercaImportoCorrispondente(ImportiCapitoloEP importiRicerca,
			List<ImportiCapitoloEP> listaImportiCapitoloEP) {
		
		for (ImportiCapitoloEP importoDaSommare : listaImportiCapitoloEP) {
			if (importoDaSommare.getAnnoCompetenza().equals(importiRicerca.getAnnoCompetenza())) {
				return importoDaSommare;
			}
		}
		
		ImportiCapitoloEP imp = new ImportiCapitoloEP();
		imp.setAnnoCompetenza(importiRicerca.getAnnoCompetenza());
		listaImportiCapitoloEP.add(imp);		
		return imp;	
	}

	/**
	 * Gets the dettaglio massivo da response dettaglio.
	 *
	 * @param capitoloCorrente the capitolo corrente
	 * @param dettaglio the dettaglio
	 * @return the dettaglio massivo da response dettaglio
	 */
	private CapitoloMassivaEntrataPrevisione getDettaglioMassivoDaResponseDettaglio(
			CapitoloEntrataPrevisione capitoloCorrente, RicercaDettaglioCapitoloEntrataPrevisioneResponse dettaglio) {
		
		CapitoloMassivaEntrataPrevisione capitoloAggregato = mapper.map(dettaglio.getCapitoloEntrataPrevisione(), CapitoloMassivaEntrataPrevisione.class, BilMapId.CapitoloMassivaEntrataPrevisione_CapitoloEntrataPrevisione.name());
		
//		capitoloAggregato.setCategoriaTipologiaTitolo(dettaglio.getCapitoloEntrataPrevisione().getCategoriaTipologiaTitolo());
//		capitoloAggregato.setElementoPianoDeiConti(dettaglio.getCapitoloEntrataPrevisione().getElementoPianoDeiConti());
//		capitoloAggregato.setFaseEStatoAttualeBilancio(dettaglio.getFaseEStatoAttualeBilancio());
//		capitoloAggregato.setBilancio(dettaglio.getBilancio());
//		capitoloAggregato.setImportiCapitoloEG(dettaglio.getImportiCapitoloEG());
//		capitoloAggregato.setListaAttiDiLeggeCapitolo(dettaglio.getListaAttiDiLeggeCapitolo());
//		capitoloAggregato.setListaClassificatori(dettaglio.getListaClassificatori());
//		ArrayList<ImportiCapitoloEP> copia = new ArrayList<ImportiCapitoloEP>(dettaglio.getListaImportiCapitoloEP().size());
//		for (ImportiCapitoloEP importoDaClonare : dettaglio.getListaImportiCapitoloEP()) {
//			copia.add(mapper.map(importoDaClonare, ImportiCapitoloEP.class));
//		}
//		capitoloAggregato.setListaImportiCapitoloEP(copia);
//		capitoloAggregato.setListaVincoliUEPrev(dettaglio.getListaVincoliUEPrev());
//		capitoloAggregato.setStruttAmmContabile(dettaglio.getStruttAmmContabile());
//		capitoloAggregato.setTipoFinanziamento(dettaglio.getTipoFinanziamento());
//		capitoloAggregato.setTipoFondo(dettaglio.getTipoFondo());
//		capitoloAggregato.setTitoloEntrata(dettaglio.getCapitoloEntrataPrevisione().getTitoloEntrata());
		
		
		return capitoloAggregato;
		
	}

	/**
	 * Chiama servizio dettaglio.
	 *
	 * @param capitoloCorrente the capitolo corrente
	 * @return the ricerca dettaglio capitolo entrata previsione response
	 */
	private RicercaDettaglioCapitoloEntrataPrevisioneResponse chiamaServizioDettaglio(
			CapitoloEntrataPrevisione capitoloCorrente) {
		RicercaDettaglioCapitoloEntrataPrevisione richiesta = new RicercaDettaglioCapitoloEntrataPrevisione();
		richiesta.setEnte(req.getEnte());
		richiesta.setRichiedente(req.getRichiedente());
		richiesta.setDataOra(new Date());
		RicercaDettaglioCapitoloEPrev ricercaDett = new RicercaDettaglioCapitoloEPrev();
		ricercaDett.setChiaveCapitolo(capitoloCorrente.getUid());
		richiesta.setRicercaDettaglioCapitoloEPrev(ricercaDett);
		
		RicercaDettaglioCapitoloEntrataPrevisioneResponse ricercaRes = executeExternalService(ricercaDettaglioCapitoloEntrataPrevisioneService, richiesta);
		return ricercaRes;
	}

	/**
	 * Somma importi.
	 *
	 * @param importiCapitoloAggregato the importi capitolo aggregato
	 * @param daSommare the da sommare
	 */
	private void sommaImporti(ImportiCapitoloEP importiCapitoloAggregato,
			ImportiCapitoloEP daSommare) {
		importiCapitoloAggregato.addStanziamento(daSommare.getStanziamento());
		importiCapitoloAggregato.addStanziamentoCassa(daSommare.getStanziamentoCassa());
		importiCapitoloAggregato.addStanziamentoCassaIniziale(daSommare.getStanziamentoCassaIniziale());
		importiCapitoloAggregato.addStanziamentoIniziale(daSommare.getStanziamentoIniziale());
		importiCapitoloAggregato.addStanziamentoProposto(daSommare.getStanziamentoProposto());
		importiCapitoloAggregato.addStanziamentoResiduo(daSommare.getStanziamentoResiduo());
		importiCapitoloAggregato.addStanziamentoResiduoIniziale(daSommare.getStanziamentoResiduoIniziale());
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
		ParametriPaginazione parametriPaginazione = new ParametriPaginazione();
		parametriPaginazione.setElementiPerPagina(RISULTATI_PAGINA_REMOTE);
		parametriPaginazione.setNumeroPagina(paginaServizioRemoto);
		ricercaReq.setParametriPaginazione(parametriPaginazione);
		ricercaReq.setRicercaSinteticaCapitoloEPrev(req.getRicercaSinteticaCapitoloEPrev());
		
		RicercaSinteticaCapitoloEntrataPrevisioneResponse ricercaRes = executeExternalService(ricercaSinteticaCapitoloEntrataPrevisioneService, ricercaReq);
		return ricercaRes;
	}

}
