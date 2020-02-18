/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitoloentratagestione;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.capitolo.AggiornamentoMassivoCapitoloBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaCapitoloDiEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaCapitoloDiEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaMassivoCapitoloDiEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaMassivoCapitoloDiEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.TipologiaAttributo;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloEGest;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

/**
 * The Class AggiornamentoMassivoCapitoloEntrataGestioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornamentoMassivoCapitoloEntrataGestioneService extends AggiornamentoMassivoCapitoloBaseService<AggiornaMassivoCapitoloDiEntrataGestione, AggiornaMassivoCapitoloDiEntrataGestioneResponse, CapitoloEntrataGestione> {
	
	/** The ricerca sintetica capitolo entrata gestione service. */
	@Autowired
	private RicercaSinteticaCapitoloEntrataGestioneService ricercaSinteticaCapitoloEntrataGestioneService;
	
	/** The aggiorna capitolo di entrata gestione service. */
	@Autowired
	private AggiornaCapitoloDiEntrataGestioneNoCheckService aggiornaCapitoloDiEntrataGestioneService;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getCapitoloEntrataGestione().getAnnoCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno capitolo"));
		checkNotNull(req.getCapitoloEntrataGestione().getNumeroCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero capitolo"));
		checkNotNull(req.getCapitoloEntrataGestione().getNumeroArticolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero articolo"));
	}
	
	@Override
	protected void init() {
		bilancio = req.getBilancio();
		numeroCapitolo = req.getCapitoloEntrataGestione().getNumeroCapitolo();
		numeroArticolo = req.getCapitoloEntrataGestione().getNumeroArticolo();
		tipoCapitolo = TipoCapitolo.CAPITOLO_ENTRATA_GESTIONE;
	}
	
	@Transactional
	public AggiornaMassivoCapitoloDiEntrataGestioneResponse executeService(AggiornaMassivoCapitoloDiEntrataGestione serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		ottieniClassificatoriNonAggiornabili();
		ottieniAttributiNonAggiornabili();
		
		int pagina = 0;
		
		RicercaSinteticaCapitoloEntrataGestioneResponse ricercaRes = null;
		
		CapitoloEntrataGestione capitoloModificato = req.getCapitoloEntrataGestione();
		
		do {//ciclo le pagine

			ricercaRes = chiamaServizioRicercaSintetica(pagina);
			
			for (CapitoloEntrataGestione capitoloCorrente : ricercaRes.getCapitoli()) {
				
				travasaDatiModificabili(capitoloModificato, capitoloCorrente);
				
				AggiornaCapitoloDiEntrataGestione richiestaAggiornamento = componiRichiestaAggiornamentoSingolo(capitoloCorrente);
				
				AggiornaCapitoloDiEntrataGestioneResponse rispostaAggiornamento = executeExternalServiceSuccess(aggiornaCapitoloDiEntrataGestioneService, richiestaAggiornamento);
				
				if (rispostaAggiornamento.isFallimento()) {
					res.setEsito(Esito.FALLIMENTO);
					res.setErrori(rispostaAggiornamento.getErrori());
					throw new BusinessException("Esecuzione servizio interno AggiornaCapitoloDiEntrataGestioneService richiamato da "+ getServiceName() + " terminata con esito Fallimento." 
							+ "\nErrori riscontrati da AggiornaCapitoloDiEntrataGestioneService: {" + rispostaAggiornamento.getDescrizioneErrori().replaceAll("\n", "\n\t") + "}.");
				}
			}
			pagina ++;
		} while (!ricercaRes.getCapitoli().isEmpty());
		
		res.setCapitoloEntrataGestione(capitoloModificato);
		res.setEsito(Esito.SUCCESSO);
	}

	/**
	 * Componi richiesta aggiornamento singolo.
	 *
	 * @param capitoloCorrente the capitolo corrente
	 * @return the aggiorna capitolo di entrata gestione
	 */
	private AggiornaCapitoloDiEntrataGestione componiRichiestaAggiornamentoSingolo(CapitoloEntrataGestione capitoloCorrente) {
		AggiornaCapitoloDiEntrataGestione richiestaAggiornamento = new AggiornaCapitoloDiEntrataGestione();
		
		richiestaAggiornamento.setRichiedente(req.getRichiedente());
		richiestaAggiornamento.setCapitoloEntrataGestione(capitoloCorrente);
		richiestaAggiornamento.setDataOra(new Date());
		
		return richiestaAggiornamento;
	}

	@Override
	protected void travasaDatiModificabili(CapitoloEntrataGestione capitoloModificato, CapitoloEntrataGestione capitoloCorrente) {
		super.travasaDatiModificabili(capitoloModificato, capitoloCorrente);
		
		// Classificatori
		capitoloCorrente.setCategoriaTipologiaTitolo(travasaClassificatoreModificabile(capitoloModificato.getCategoriaTipologiaTitolo(), capitoloCorrente.getCategoriaTipologiaTitolo(),
			TipologiaClassificatore.CATEGORIA));
		capitoloCorrente.setTipologiaTitolo(travasaClassificatoreModificabile(capitoloModificato.getTipologiaTitolo(), capitoloCorrente.getTipologiaTitolo(),
			TipologiaClassificatore.TIPOLOGIA));
		capitoloCorrente.setTitoloEntrata(travasaClassificatoreModificabile(capitoloModificato.getTitoloEntrata(), capitoloCorrente.getTitoloEntrata(),
			TipologiaClassificatore.TITOLO_ENTRATA));
		capitoloCorrente.setPerimetroSanitarioEntrata(travasaClassificatoreModificabile(capitoloModificato.getPerimetroSanitarioEntrata(), capitoloCorrente.getPerimetroSanitarioEntrata(),
			TipologiaClassificatore.PERIMETRO_SANITARIO_ENTRATA));
		capitoloCorrente.setRicorrenteEntrata(travasaClassificatoreModificabile(capitoloModificato.getRicorrenteEntrata(), capitoloCorrente.getRicorrenteEntrata(),
			TipologiaClassificatore.RICORRENTE_ENTRATA));
		capitoloCorrente.setSiopeEntrata(travasaClassificatoreModificabile(capitoloModificato.getSiopeEntrata(), capitoloCorrente.getSiopeEntrata(),
			TipologiaClassificatore.SIOPE_ENTRATA, TipologiaClassificatore.SIOPE_ENTRATA_I, TipologiaClassificatore.SIOPE_ENTRATA_II, TipologiaClassificatore.SIOPE_ENTRATA_III));
		capitoloCorrente.setTransazioneUnioneEuropeaEntrata(travasaClassificatoreModificabile(capitoloModificato.getTransazioneUnioneEuropeaEntrata(), capitoloCorrente.getTransazioneUnioneEuropeaEntrata(),
			TipologiaClassificatore.TRANSAZIONE_UE_ENTRATA));
		
		// Attributi
		capitoloCorrente.setFlagEntrateRicorrenti(travasaAttributoModificabile(capitoloModificato.getFlagEntrateRicorrenti(), capitoloCorrente.getFlagEntrateRicorrenti(),
			TipologiaAttributo.FLAG_ENTRATE_RICORRENTI));
		
		// Non aggiornabili
		capitoloCorrente.setImportiCapitoloEG(capitoloCorrente.getImportiCapitoloEG());
		capitoloCorrente.setPercEntrataRicorrente(capitoloModificato.getPercEntrataRicorrente());
	}

	/**
	 * Chiama servizio ricerca sintetica.
	 *
	 * @param paginaServizioRemoto the pagina servizio remoto
	 * @return the ricerca sintetica capitolo entrata gestione response
	 */
	private RicercaSinteticaCapitoloEntrataGestioneResponse chiamaServizioRicercaSintetica(int paginaServizioRemoto) {
		RicercaSinteticaCapitoloEntrataGestione ricercaReq = new RicercaSinteticaCapitoloEntrataGestione();
		ricercaReq.setEnte(req.getEnte());
		ricercaReq.setRichiedente(req.getRichiedente());
		ricercaReq.setDataOra(new Date());
		ParametriPaginazione parametriPaginazione = new ParametriPaginazione();
		parametriPaginazione.setElementiPerPagina(RISULTATI_PAGINA_REMOTE);
		parametriPaginazione.setNumeroPagina(paginaServizioRemoto);
		ricercaReq.setParametriPaginazione(parametriPaginazione);
		
		RicercaSinteticaCapitoloEGest parametriRicerca = new RicercaSinteticaCapitoloEGest();
		parametriRicerca.setAnnoCapitolo(req.getCapitoloEntrataGestione().getAnnoCapitolo());
		parametriRicerca.setNumeroCapitolo(req.getCapitoloEntrataGestione().getNumeroCapitolo());
		parametriRicerca.setNumeroArticolo(req.getCapitoloEntrataGestione().getNumeroArticolo());
		parametriRicerca.setAnnoEsercizio(req.getBilancio().getAnno());
		parametriRicerca.setStatoOperativo(StatoOperativoElementoDiBilancio.VALIDO); //Aggiorna solo i capitoli in stato valido!
		ricercaReq.setRicercaSinteticaCapitoloEntrata(parametriRicerca);
		
		RicercaSinteticaCapitoloEntrataGestioneResponse ricercaRes = executeExternalService(ricercaSinteticaCapitoloEntrataGestioneService, ricercaReq);
		return ricercaRes;
	}

}
