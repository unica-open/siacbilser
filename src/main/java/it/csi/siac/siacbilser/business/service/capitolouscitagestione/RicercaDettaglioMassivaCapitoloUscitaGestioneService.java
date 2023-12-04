/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitolouscitagestione;

import java.util.Date;
import java.util.List;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloUscitaGestioneDad;
import it.csi.siac.siacbilser.integration.dad.ImportiCapitoloDad;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.model.CapitoloMassivaUscitaGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.ImportiCapitoloUG;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloUGest;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaDettaglioMassivaCapitoloUscitaGestioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDettaglioMassivaCapitoloUscitaGestioneService extends CheckedAccountBaseService<RicercaDettaglioMassivaCapitoloUscitaGestione, RicercaDettaglioMassivaCapitoloUscitaGestioneResponse> {
	
	/** The Constant RISULTATI_PAGINA_REMOTE. */
	private static final int RISULTATI_PAGINA_REMOTE = 10;
	
	/** The mapper. */
	@Autowired
	private Mapper mapper;

	/** The capitolo uscita gestione dad. */
	@Autowired
	private CapitoloUscitaGestioneDad capitoloUscitaGestioneDad;
	
	/** The importi capitolo dad. */
	@Autowired
	private ImportiCapitoloDad importiCapitoloDad;
	
	/** The ricerca sintetica capitolo uscita gestione service. */
	@Autowired
	private RicercaSinteticaCapitoloUscitaGestioneService ricercaSinteticaCapitoloUscitaGestioneService;
	
	/** The ricerca dettaglio capitolo uscita gestione service. */
	@Autowired
	private RicercaDettaglioCapitoloUscitaGestioneService ricercaDettaglioCapitoloUscitaGestioneService;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getRicercaSinteticaCapitoloUGest().getAnnoCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno capitolo"));
		checkNotNull(req.getRicercaSinteticaCapitoloUGest().getNumeroCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero capitolo"));
		checkNotNull(req.getRicercaSinteticaCapitoloUGest().getNumeroArticolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero articolo"));
		checkNotNull(req.getRicercaSinteticaCapitoloUGest().getAnnoEsercizio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno esercizio"));
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {		
		capitoloUscitaGestioneDad.setEnte(req.getEnte());		
		capitoloUscitaGestioneDad.setLoginOperazione(loginOperazione);		
		importiCapitoloDad.setEnte(req.getEnte());
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional(readOnly= true)
	public RicercaDettaglioMassivaCapitoloUscitaGestioneResponse executeService(RicercaDettaglioMassivaCapitoloUscitaGestione serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		final String methodName = "execute";

		int pagina = 0;
		
		RicercaSinteticaCapitoloUscitaGestioneResponse ricercaRes = null;
		
		CapitoloMassivaUscitaGestione capitoloAggregato = null;
		
		do {
		
			ricercaRes = chiamaServizioRicercaSintetica(pagina);
			
			for (CapitoloUscitaGestione capitoloCorrente : ricercaRes.getCapitoli()) {
					
				//appena entrato?
				RicercaDettaglioCapitoloUscitaGestioneResponse capitoloCorrenteDettaglio = chiamaServizioDettaglio(capitoloCorrente);
				
				log.debug(methodName, capitoloCorrente.getUid() + " " + capitoloCorrente.getNumeroCapitolo() + " " + capitoloCorrente.getNumeroArticolo() + " " + capitoloCorrente.getNumeroUEB());
				
				if (capitoloAggregato == null) {
					capitoloAggregato = getDettaglioMassivoDaResponseDettaglio(capitoloCorrente, capitoloCorrenteDettaglio);
				} else {
					//sommo
					//devo ciclare gli importi del capitolo corrente e trovare il corrispondente nell'aggregato
					for (ImportiCapitoloUG importiDaSommare : capitoloCorrenteDettaglio.getListaImportiCapitoloUG()) {
						ImportiCapitoloUG importoAggregato = cercaImportoCorrispondente(importiDaSommare, capitoloAggregato.getListaImportiCapitoloUG());
						//una volta trovata la corrispondenza devo sommare
						sommaImporti(importoAggregato, importiDaSommare);
					}
				}
				capitoloAggregato.addCapitolo(capitoloCorrenteDettaglio.getCapitoloUscita());
					
			}
			pagina ++;
		} while (!ricercaRes.getCapitoli().isEmpty());
		
		res.setCapitoloMassivaUscitaGestione(capitoloAggregato);
		res.setEsito(Esito.SUCCESSO);
	}

	/**
	 * Cerca importo corrispondente.
	 *
	 * @param importiRicerca the importi ricerca
	 * @param listaImportiCapitoloUG the lista importi capitolo ug
	 * @return the importi capitolo ug
	 */
	private ImportiCapitoloUG cercaImportoCorrispondente(ImportiCapitoloUG importiRicerca,
			List<ImportiCapitoloUG> listaImportiCapitoloUG) {
		
		for (ImportiCapitoloUG importoDaSommare : listaImportiCapitoloUG) {
			if (importoDaSommare.getAnnoCompetenza().equals(importiRicerca.getAnnoCompetenza())) {
				return importoDaSommare;
			}
		}
		
		ImportiCapitoloUG imp = new ImportiCapitoloUG();
		imp.setAnnoCompetenza(importiRicerca.getAnnoCompetenza());
		listaImportiCapitoloUG.add(imp);		
		return imp;		
	}

	/**
	 * Gets the dettaglio massivo da response dettaglio.
	 *
	 * @param capitoloCorrente the capitolo corrente
	 * @param dettaglio the dettaglio
	 * @return the dettaglio massivo da response dettaglio
	 */
	private CapitoloMassivaUscitaGestione getDettaglioMassivoDaResponseDettaglio(
			CapitoloUscitaGestione capitoloCorrente, RicercaDettaglioCapitoloUscitaGestioneResponse dettaglio) {
		
		CapitoloMassivaUscitaGestione capitoloAggregato = mapper.map(dettaglio.getCapitoloUscita(), CapitoloMassivaUscitaGestione.class, BilMapId.CapitoloMassivaUscitaGestione_CapitoloUscitaGestione.name());
		
//		capitoloAggregato.setClassificazioneCofog(dettaglio.getCapitoloUscita().getClassificazioneCofog());
//		capitoloAggregato.setElementoPianoDeiConti(dettaglio.getCapitoloUscita().getElementoPianoDeiConti());
//		capitoloAggregato.setFaseEStatoAttualeBilancio(dettaglio.getFaseEStatoAttualeBilancio());
//		capitoloAggregato.setBilancio(dettaglio.getBilancio());
//		capitoloAggregato.setImportiCapitoloUP(dettaglio.getImportiCapitoloUP());
//		capitoloAggregato.setListaAttiDiLeggeCapitolo(dettaglio.getListaAttiDiLeggeCapitolo());
//		capitoloAggregato.setListaClassificatori(dettaglio.getListaClassificatori());
//		ArrayList<ImportiCapitoloUG> copia = new ArrayList<ImportiCapitoloUG>(dettaglio.getListaImportiCapitoloUG().size());
//		for (ImportiCapitoloUG importoDaClonare : dettaglio.getListaImportiCapitoloUG()) {
//			copia.add(mapper.map(importoDaClonare, ImportiCapitoloUG.class));
//		}
//		capitoloAggregato.setListaImportiCapitoloUG(copia);
//		capitoloAggregato.setListaVincoliUEGest(dettaglio.getListaVincoliUEGest());
//		capitoloAggregato.setMacroaggregato(dettaglio.getCapitoloUscita().getMacroaggregato());
//		capitoloAggregato.setMissione(dettaglio.getCapitoloUscita().getMissione());
//		capitoloAggregato.setProgramma(dettaglio.getCapitoloUscita().getProgramma());
//		capitoloAggregato.setStrutturaAmministrativoContabile(dettaglio.getCapitoloUscita().getStrutturaAmministrativoContabile());
//		capitoloAggregato.setTipoFinanziamento(dettaglio.getTipoFinanziamento());
//		capitoloAggregato.setTipoFondo(dettaglio.getTipoFondo());
//		capitoloAggregato.setTitoloSpesa(dettaglio.getCapitoloUscita().getTitoloSpesa());
		
		
		return capitoloAggregato;
		
	}

	/**
	 * Chiama servizio dettaglio.
	 *
	 * @param capitoloCorrente the capitolo corrente
	 * @return the ricerca dettaglio capitolo uscita gestione response
	 */
	private RicercaDettaglioCapitoloUscitaGestioneResponse chiamaServizioDettaglio(
			CapitoloUscitaGestione capitoloCorrente) {
		RicercaDettaglioCapitoloUscitaGestione richiesta = new RicercaDettaglioCapitoloUscitaGestione();
		richiesta.setEnte(req.getEnte());
		richiesta.setRichiedente(req.getRichiedente());
		richiesta.setDataOra(new Date());
		RicercaDettaglioCapitoloUGest ricercaDett = new RicercaDettaglioCapitoloUGest();
		ricercaDett.setChiaveCapitolo(capitoloCorrente.getUid());
		richiesta.setRicercaDettaglioCapitoloUGest(ricercaDett);
		
		RicercaDettaglioCapitoloUscitaGestioneResponse ricercaRes = executeExternalService(ricercaDettaglioCapitoloUscitaGestioneService, richiesta);
		return ricercaRes;
	}

	/**
	 * Somma importi.
	 *
	 * @param importiCapitoloAggregato the importi capitolo aggregato
	 * @param daSommare the da sommare
	 */
	private void sommaImporti(ImportiCapitoloUG importiCapitoloAggregato,
			ImportiCapitoloUG daSommare) {
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
	 * @return the ricerca sintetica capitolo uscita gestione response
	 */
	private RicercaSinteticaCapitoloUscitaGestioneResponse chiamaServizioRicercaSintetica(
			int paginaServizioRemoto) {
		RicercaSinteticaCapitoloUscitaGestione ricercaReq = new RicercaSinteticaCapitoloUscitaGestione();
		ricercaReq.setEnte(req.getEnte());
		ricercaReq.setRichiedente(req.getRichiedente());
		ricercaReq.setDataOra(new Date());
		ParametriPaginazione parametriPaginazione = new ParametriPaginazione();
		parametriPaginazione.setElementiPerPagina(RISULTATI_PAGINA_REMOTE);
		parametriPaginazione.setNumeroPagina(paginaServizioRemoto);
		ricercaReq.setParametriPaginazione(parametriPaginazione);
		ricercaReq.setRicercaSinteticaCapitoloUGest(req.getRicercaSinteticaCapitoloUGest());
		
		RicercaSinteticaCapitoloUscitaGestioneResponse ricercaRes = executeExternalService(ricercaSinteticaCapitoloUscitaGestioneService, ricercaReq);
		return ricercaRes;
	}

}
