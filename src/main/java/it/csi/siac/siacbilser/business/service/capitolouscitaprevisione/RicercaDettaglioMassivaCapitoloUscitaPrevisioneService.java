/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitolouscitaprevisione;

import java.util.Date;
import java.util.List;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioMassivaCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloUscitaPrevisioneDad;
import it.csi.siac.siacbilser.integration.dad.ClassificatoriDad;
import it.csi.siac.siacbilser.integration.dad.ImportiCapitoloDad;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.model.CapitoloMassivaUscitaPrevisione;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.ImportiCapitoloUP;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloUPrev;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaDettaglioMassivaCapitoloUscitaPrevisioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDettaglioMassivaCapitoloUscitaPrevisioneService extends CheckedAccountBaseService<RicercaDettaglioMassivaCapitoloUscitaPrevisione, RicercaDettaglioMassivaCapitoloUscitaPrevisioneResponse> {
		
	/** The Constant RISULTATI_PAGINA_REMOTE. */
	private static final int RISULTATI_PAGINA_REMOTE = 10;
	
	/** The mapper. */
	@Autowired
	private Mapper mapper;

	/** The capitolo uscita previsione dad. */
	@Autowired
	private CapitoloUscitaPrevisioneDad capitoloUscitaPrevisioneDad;
	
	/** The classificatori dad. */
	@Autowired
	private ClassificatoriDad classificatoriDad; //siacTClassDao
	
	/** The importi capitolo dad. */
	@Autowired
	private ImportiCapitoloDad importiCapitoloDad;
	
	/** The ricerca sintetica capitolo uscita previsione service. */
	@Autowired
	private RicercaSinteticaCapitoloUscitaPrevisioneService ricercaSinteticaCapitoloUscitaPrevisioneService;
	
	/** The ricerca dettaglio capitolo uscita previsione service. */
	@Autowired
	private RicercaDettaglioCapitoloUscitaPrevisioneService ricercaDettaglioCapitoloUscitaPrevisioneService;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getRicercaSinteticaCapitoloUPrev().getAnnoCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno capitolo"));
		checkNotNull(req.getRicercaSinteticaCapitoloUPrev().getNumeroCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero capitolo"));
		checkNotNull(req.getRicercaSinteticaCapitoloUPrev().getNumeroArticolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero articolo"));
		checkNotNull(req.getRicercaSinteticaCapitoloUPrev().getAnnoEsercizio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno esercizio"));
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {		
		capitoloUscitaPrevisioneDad.setEnte(req.getEnte());		
		capitoloUscitaPrevisioneDad.setLoginOperazione(loginOperazione);		
		importiCapitoloDad.setEnte(req.getEnte());
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional(readOnly= true)
	public RicercaDettaglioMassivaCapitoloUscitaPrevisioneResponse executeService(RicercaDettaglioMassivaCapitoloUscitaPrevisione serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		final String methodName = "execute";

		int pagina = 0;
		
		RicercaSinteticaCapitoloUscitaPrevisioneResponse ricercaRes = null;		
		CapitoloMassivaUscitaPrevisione capitoloAggregato = null;
		
		do {
		
			ricercaRes = chiamaServizioRicercaSintetica(pagina);
			
			for (CapitoloUscitaPrevisione capitoloCorrente : ricercaRes.getCapitoli()) {
					
				//appena entrato?
				RicercaDettaglioCapitoloUscitaPrevisioneResponse capitoloCorrenteDettaglio = chiamaServizioDettaglio(capitoloCorrente);				
				log.debug(methodName, capitoloCorrente.getUid() + " " + capitoloCorrente.getNumeroCapitolo() + " " + capitoloCorrente.getNumeroArticolo() + " " + capitoloCorrente.getNumeroUEB());
				
				if (capitoloAggregato == null) {
					capitoloAggregato = getDettaglioMassivoDaResponseDettaglio(capitoloCorrente, capitoloCorrenteDettaglio);
				} else {
					//sommo
					//devo ciclare gli importi del capitolo corrente e trovare il corrispondente nell'aggregato
					for (ImportiCapitoloUP importiDaSommare : capitoloCorrenteDettaglio.getListaImportiCapitoloUP()) {
						ImportiCapitoloUP importoAggregato = cercaImportoCorrispondente(importiDaSommare, capitoloAggregato.getListaImportiCapitoloUP());
						//una volta trovata la corrispondenza devo sommare
						sommaImporti(importoAggregato, importiDaSommare);
					}
				}
				capitoloAggregato.addCapitolo(capitoloCorrenteDettaglio.getCapitoloUscitaPrevisione());
					
			}
			pagina ++;
		} while (!ricercaRes.getCapitoli().isEmpty());
		
		res.setCapitoloMassivaUscitaPrevisione(capitoloAggregato);
		res.setEsito(Esito.SUCCESSO);
	}

	/**
	 * Cerca importo corrispondente.
	 *
	 * @param importiRicerca the importi ricerca
	 * @param listaImportiCapitoloUP the lista importi capitolo up
	 * @return the importi capitolo up
	 */
	private ImportiCapitoloUP cercaImportoCorrispondente(ImportiCapitoloUP importiRicerca,
			List<ImportiCapitoloUP> listaImportiCapitoloUP) {
		
		for (ImportiCapitoloUP importoDaSommare : listaImportiCapitoloUP) {
			if (importoDaSommare.getAnnoCompetenza().equals(importiRicerca.getAnnoCompetenza())) {
				return importoDaSommare;
			}
		}
		
		ImportiCapitoloUP imp = new ImportiCapitoloUP();
		imp.setAnnoCompetenza(importiRicerca.getAnnoCompetenza());
		listaImportiCapitoloUP.add(imp);		
		return imp;	
	}

	/**
	 * Gets the dettaglio massivo da response dettaglio.
	 *
	 * @param capitoloCorrente the capitolo corrente
	 * @param dettaglio the dettaglio
	 * @return the dettaglio massivo da response dettaglio
	 */
	private CapitoloMassivaUscitaPrevisione getDettaglioMassivoDaResponseDettaglio(
			CapitoloUscitaPrevisione capitoloCorrente, RicercaDettaglioCapitoloUscitaPrevisioneResponse dettaglio) {
		
		CapitoloMassivaUscitaPrevisione capitoloAggregato = mapper.map(dettaglio.getCapitoloUscitaPrevisione(), CapitoloMassivaUscitaPrevisione.class, BilMapId.CapitoloMassivaUscitaPrevisione_CapitoloUscitaPrevisione.name());
		
//		capitoloAggregato.setClassificazioneCofog(dettaglio.getCapitoloUscitaPrevisione().getClassificazioneCofog());
//		capitoloAggregato.setElementoPianoDeiConti(dettaglio.getCapitoloUscitaPrevisione().getElementoPianoDeiConti());
//		capitoloAggregato.setFaseEStatoAttualeBilancio(dettaglio.getFaseEStatoAttualeBilancio());
//		capitoloAggregato.setBilancio(dettaglio.getBilancio());
//		capitoloAggregato.setImportiCapitoloUG(dettaglio.getImportiCapitoloUG());
//		capitoloAggregato.setListaAttiDiLeggeCapitolo(dettaglio.getListaAttiDiLeggeCapitolo());
//		capitoloAggregato.setListaClassificatori(dettaglio.getListaClassificatori());
//		ArrayList<ImportiCapitoloUP> copia = new ArrayList<ImportiCapitoloUP>(dettaglio.getListaImportiCapitoloUP().size());
//		for (ImportiCapitoloUP importoDaClonare : dettaglio.getListaImportiCapitoloUP()) {
//			copia.add(mapper.map(importoDaClonare, ImportiCapitoloUP.class));
//		}
//		capitoloAggregato.setListaImportiCapitoloUP(copia);
//		capitoloAggregato.setListaVincoliUEPrev(dettaglio.getListaVincoliUEPrev());
//		capitoloAggregato.setMacroaggregato(dettaglio.getCapitoloUscitaPrevisione().getMacroaggregato());
//		capitoloAggregato.setMissione(dettaglio.getCapitoloUscitaPrevisione().getMissione());
//		capitoloAggregato.setProgramma(dettaglio.getCapitoloUscitaPrevisione().getProgramma());
//		capitoloAggregato.setStrutturaAmministrativoContabile(dettaglio.getCapitoloUscitaPrevisione().getStrutturaAmministrativoContabile());
//		capitoloAggregato.setTipoFinanziamento(dettaglio.getTipoFinanziamento());
//		capitoloAggregato.setTipoFondo(dettaglio.getTipoFondo());
//		capitoloAggregato.setTitoloSpesa(dettaglio.getCapitoloUscitaPrevisione().getTitoloSpesa());
		
		
		return capitoloAggregato;
		
	}

	/**
	 * Chiama servizio dettaglio.
	 *
	 * @param capitoloCorrente the capitolo corrente
	 * @return the ricerca dettaglio capitolo uscita previsione response
	 */
	private RicercaDettaglioCapitoloUscitaPrevisioneResponse chiamaServizioDettaglio(
			CapitoloUscitaPrevisione capitoloCorrente) {
		RicercaDettaglioCapitoloUscitaPrevisione richiesta = new RicercaDettaglioCapitoloUscitaPrevisione();
		richiesta.setEnte(req.getEnte());
		richiesta.setRichiedente(req.getRichiedente());
		richiesta.setDataOra(new Date());
		RicercaDettaglioCapitoloUPrev ricercaDett = new RicercaDettaglioCapitoloUPrev();
		ricercaDett.setChiaveCapitolo(capitoloCorrente.getUid());
		richiesta.setRicercaDettaglioCapitoloUPrev(ricercaDett);
		
		RicercaDettaglioCapitoloUscitaPrevisioneResponse ricercaRes = executeExternalService(ricercaDettaglioCapitoloUscitaPrevisioneService, richiesta);
		return ricercaRes;
	}

	/**
	 * Somma importi.
	 *
	 * @param importiCapitoloAggregato the importi capitolo aggregato
	 * @param daSommare the da sommare
	 */
	private void sommaImporti(ImportiCapitoloUP importiCapitoloAggregato,
			ImportiCapitoloUP daSommare) {
		importiCapitoloAggregato.addStanziamento(daSommare.getStanziamento());
		importiCapitoloAggregato.addStanziamentoCassa(daSommare.getStanziamentoCassa());
		importiCapitoloAggregato.addStanziamentoCassaIniziale(daSommare.getStanziamentoCassaIniziale());
		importiCapitoloAggregato.addStanziamentoIniziale(daSommare.getStanziamentoIniziale());
		importiCapitoloAggregato.addStanziamentoProposto(daSommare.getStanziamentoProposto());
		importiCapitoloAggregato.addStanziamentoResiduo(daSommare.getStanziamentoResiduo());
		importiCapitoloAggregato.addStanziamentoResiduoIniziale(daSommare.getStanziamentoResiduoIniziale());
		importiCapitoloAggregato.addDiCuiImpegnatoAnno1(daSommare.getDiCuiImpegnatoAnno1());
		importiCapitoloAggregato.addDiCuiImpegnatoAnno2(daSommare.getDiCuiImpegnatoAnno2());
		importiCapitoloAggregato.addDiCuiImpegnatoAnno3(daSommare.getDiCuiImpegnatoAnno3());
		
//		importiCapitoloAggregato.addDiCuiImpegnatoAnnoPrec(daSommare.getDiCuiImpegnatoAnnoPrec());
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
		ParametriPaginazione parametriPaginazione = new ParametriPaginazione();
		parametriPaginazione.setElementiPerPagina(RISULTATI_PAGINA_REMOTE);
		parametriPaginazione.setNumeroPagina(paginaServizioRemoto);
		ricercaReq.setParametriPaginazione(parametriPaginazione);
		ricercaReq.setRicercaSinteticaCapitoloUPrev(req.getRicercaSinteticaCapitoloUPrev());
		
		RicercaSinteticaCapitoloUscitaPrevisioneResponse ricercaRes = executeExternalService(ricercaSinteticaCapitoloUscitaPrevisioneService, ricercaReq);
		return ricercaRes;
	}

}
