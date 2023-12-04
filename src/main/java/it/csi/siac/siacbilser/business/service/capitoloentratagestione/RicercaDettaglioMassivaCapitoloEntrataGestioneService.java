/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitoloentratagestione;

import java.util.Date;
import java.util.List;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloEntrataGestioneDad;
import it.csi.siac.siacbilser.integration.dad.ImportiCapitoloDad;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloMassivaEntrataGestione;
import it.csi.siac.siacbilser.model.ImportiCapitoloEG;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloEGest;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaDettaglioMassivaCapitoloEntrataGestioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDettaglioMassivaCapitoloEntrataGestioneService extends CheckedAccountBaseService<RicercaDettaglioMassivaCapitoloEntrataGestione, RicercaDettaglioMassivaCapitoloEntrataGestioneResponse> {
	
	/** The Constant RISULTATI_PAGINA_REMOTE. */
	private static final int RISULTATI_PAGINA_REMOTE = 10;
	
	/** The mapper. */
	@Autowired
	private Mapper mapper;

	/** The capitolo entrata gestione dad. */
	@Autowired
	private CapitoloEntrataGestioneDad capitoloEntrataGestioneDad;
	
//	@Autowired
//	private ClassificatoriDad classificatoriDad; //siacTClassDao
	
	/** The importi capitolo dad. */
@Autowired
	private ImportiCapitoloDad importiCapitoloDad;
	
	/** The ricerca sintetica capitolo entrata gestione service. */
	@Autowired
	private RicercaSinteticaCapitoloEntrataGestioneService ricercaSinteticaCapitoloEntrataGestioneService;
	
	/** The ricerca dettaglio capitolo entrata gestione service. */
	@Autowired
	private RicercaDettaglioCapitoloEntrataGestioneService ricercaDettaglioCapitoloEntrataGestioneService;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getRicercaSinteticaCapitoloEGest().getAnnoCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno capitolo"));
		checkNotNull(req.getRicercaSinteticaCapitoloEGest().getNumeroCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero capitolo"));
		checkNotNull(req.getRicercaSinteticaCapitoloEGest().getNumeroArticolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero articolo"));
		checkNotNull(req.getRicercaSinteticaCapitoloEGest().getAnnoEsercizio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno esercizio"));
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {		
		capitoloEntrataGestioneDad.setEnte(req.getEnte());		
		capitoloEntrataGestioneDad.setLoginOperazione(loginOperazione);		
		importiCapitoloDad.setEnte(req.getEnte());
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional(readOnly= true)
	public RicercaDettaglioMassivaCapitoloEntrataGestioneResponse executeService(RicercaDettaglioMassivaCapitoloEntrataGestione serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {

		int pagina = 0;
		
		RicercaSinteticaCapitoloEntrataGestioneResponse ricercaRes = null;
		
		CapitoloMassivaEntrataGestione capitoloAggregato = null;
		
		do {
		
			ricercaRes = chiamaServizioRicercaSintetica(pagina);
			
			for (CapitoloEntrataGestione capitoloCorrente : ricercaRes.getCapitoli()) {
					
				//appena entrato?
				RicercaDettaglioCapitoloEntrataGestioneResponse capitoloCorrenteDettaglio = chiamaServizioDettaglio(capitoloCorrente);
				
				log.debug("execute", capitoloCorrente.getUid() + " " + capitoloCorrente.getNumeroCapitolo() + " " + capitoloCorrente.getNumeroArticolo() + " " + capitoloCorrente.getNumeroUEB());
				
				if (capitoloAggregato == null) {
					capitoloAggregato = getDettaglioMassivoDaResponseDettaglio(capitoloCorrente, capitoloCorrenteDettaglio);
				} else {
					//sommo
					//devo ciclare gli importi del capitolo corrente e trovare il corrispondente nell'aggregato
					for (ImportiCapitoloEG importiDaSommare : capitoloCorrenteDettaglio.getListaImportiCapitoloEG()) {
						ImportiCapitoloEG importoAggregato = cercaImportoCorrispondente(importiDaSommare, capitoloAggregato.getListaImportiCapitoloEG());
						//una volta trovata la corrispondenza devo sommare
						sommaImporti(importoAggregato, importiDaSommare);
					}
				}
				capitoloAggregato.addCapitolo(capitoloCorrenteDettaglio.getCapitoloEntrataGestione());
					
			}
			pagina ++;
		} while (!ricercaRes.getCapitoli().isEmpty());
		
		res.setCapitoloMassivaEntrataGestione(capitoloAggregato);
		res.setEsito(Esito.SUCCESSO);
	}

	/**
	 * Cerca importo corrispondente.
	 *
	 * @param importiRicerca the importi ricerca
	 * @param listaImportiCapitolo the lista importi capitolo
	 * @return the importi capitolo eg
	 */
	private ImportiCapitoloEG cercaImportoCorrispondente(ImportiCapitoloEG importiRicerca,
			List<ImportiCapitoloEG> listaImportiCapitolo) {
		
		for (ImportiCapitoloEG importoDaSommare : listaImportiCapitolo) {
			if (importoDaSommare.getAnnoCompetenza().equals(importiRicerca.getAnnoCompetenza())) {
				return importoDaSommare;
			}
		}
		
		ImportiCapitoloEG imp = new ImportiCapitoloEG();
		imp.setAnnoCompetenza(importiRicerca.getAnnoCompetenza());
		listaImportiCapitolo.add(imp);		
		return imp;			
	}

	/**
	 * Gets the dettaglio massivo da response dettaglio.
	 *
	 * @param capitoloCorrente the capitolo corrente
	 * @param dettaglio the dettaglio
	 * @return the dettaglio massivo da response dettaglio
	 */
	private CapitoloMassivaEntrataGestione getDettaglioMassivoDaResponseDettaglio(
			CapitoloEntrataGestione capitoloCorrente, RicercaDettaglioCapitoloEntrataGestioneResponse dettaglio) {
		
		CapitoloMassivaEntrataGestione capitoloAggregato = mapper.map(dettaglio.getCapitoloEntrataGestione(), CapitoloMassivaEntrataGestione.class, BilMapId.CapitoloMassivaEntrataGestione_CapitoloEntrataGestione.name());
		
//		capitoloAggregato.setCategoriaTipologiaTitolo(dettaglio.getCapitoloEntrataGestione().getCategoriaTipologiaTitolo());
//		capitoloAggregato.setElementoPianoDeiConti(dettaglio.getCapitoloEntrataGestione().getElementoPianoDeiConti());
//		capitoloAggregato.setFaseEStatoAttualeBilancio(dettaglio.getFaseEStatoAttualeBilancio());
//		capitoloAggregato.setBilancio(dettaglio.getBilancio());
//		capitoloAggregato.setImportiCapitoloEP(dettaglio.getImportiCapitoloEP());
//		capitoloAggregato.setListaAttiDiLeggeCapitolo(dettaglio.getListaAttiDiLeggeCapitolo());
//		capitoloAggregato.setListaClassificatori(dettaglio.getListaClassificatori());
//		ArrayList<ImportiCapitoloEG> copia = new ArrayList<ImportiCapitoloEG>(dettaglio.getListaImportiCapitoloEG().size());
//		for (ImportiCapitoloEG importoDaClonare : dettaglio.getListaImportiCapitoloEG()) {
//			copia.add(mapper.map(importoDaClonare, ImportiCapitoloEG.class));
//		}
//		capitoloAggregato.setListaImportiCapitoloEG(copia);
//		capitoloAggregato.setListaVincoliUEGestione(dettaglio.getListaVincoliUEGestione());
//		capitoloAggregato.setStrutturaAmministrativoContabile(dettaglio.getCapitoloEntrataGestione().getStrutturaAmministrativoContabile());
//		capitoloAggregato.setTipoFinanziamento(dettaglio.getTipoFinanziamento());
//		capitoloAggregato.setTipoFondo(dettaglio.getTipoFondo());
//		capitoloAggregato.setTitoloEntrata(dettaglio.getCapitoloEntrataGestione().getTitoloEntrata());
		
		
		return capitoloAggregato;
		
	}

	/**
	 * Chiama servizio dettaglio.
	 *
	 * @param capitoloCorrente the capitolo corrente
	 * @return the ricerca dettaglio capitolo entrata gestione response
	 */
	private RicercaDettaglioCapitoloEntrataGestioneResponse chiamaServizioDettaglio(
			CapitoloEntrataGestione capitoloCorrente) {
		RicercaDettaglioCapitoloEntrataGestione richiesta = new RicercaDettaglioCapitoloEntrataGestione();
		richiesta.setEnte(req.getEnte());
		richiesta.setRichiedente(req.getRichiedente());
		richiesta.setDataOra(new Date());
		RicercaDettaglioCapitoloEGest ricercaDett = new RicercaDettaglioCapitoloEGest();
		ricercaDett.setChiaveCapitolo(capitoloCorrente.getUid());
		richiesta.setRicercaDettaglioCapitoloEGest(ricercaDett);
		
		RicercaDettaglioCapitoloEntrataGestioneResponse ricercaRes = executeExternalService(ricercaDettaglioCapitoloEntrataGestioneService, richiesta);
		return ricercaRes;
	}

	/**
	 * Somma importi.
	 *
	 * @param importiCapitoloAggregato the importi capitolo aggregato
	 * @param daSommare the da sommare
	 */
	private void sommaImporti(ImportiCapitoloEG importiCapitoloAggregato,
			ImportiCapitoloEG daSommare) {
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
	 * @return the ricerca sintetica capitolo entrata gestione response
	 */
	private RicercaSinteticaCapitoloEntrataGestioneResponse chiamaServizioRicercaSintetica(
			int paginaServizioRemoto) {
		RicercaSinteticaCapitoloEntrataGestione ricercaReq = new RicercaSinteticaCapitoloEntrataGestione();
		ricercaReq.setEnte(req.getEnte());
		ricercaReq.setRichiedente(req.getRichiedente());
		ricercaReq.setDataOra(new Date());
		ParametriPaginazione parametriPaginazione = new ParametriPaginazione();
		parametriPaginazione.setElementiPerPagina(RISULTATI_PAGINA_REMOTE);
		parametriPaginazione.setNumeroPagina(paginaServizioRemoto);
		ricercaReq.setParametriPaginazione(parametriPaginazione);
		ricercaReq.setRicercaSinteticaCapitoloEntrata(req.getRicercaSinteticaCapitoloEGest());
		
		RicercaSinteticaCapitoloEntrataGestioneResponse ricercaRes = executeExternalService(ricercaSinteticaCapitoloEntrataGestioneService, ricercaReq);
		return ricercaRes;
	}

}
