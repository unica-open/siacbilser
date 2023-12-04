/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitoloentrataprevisione;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.capitolo.AggiornamentoMassivoCapitoloBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaCapitoloDiEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaCapitoloDiEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaMassivoCapitoloDiEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaMassivoCapitoloDiEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.TipologiaAttributo;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloEPrev;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

/**
 * The Class AggiornamentoMassivoCapitoloEntrataPrevisioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornamentoMassivoCapitoloEntrataPrevisioneService extends AggiornamentoMassivoCapitoloBaseService<AggiornaMassivoCapitoloDiEntrataPrevisione, AggiornaMassivoCapitoloDiEntrataPrevisioneResponse, CapitoloEntrataPrevisione> {
	
	@Autowired
	private RicercaSinteticaCapitoloEntrataPrevisioneService ricercaSinteticaCapitoloEntrataPrevisioneService;
	@Autowired
	private AggiornaCapitoloDiEntrataPrevisioneNoCheckService aggiornaCapitoloDiEntrataPrevisioneService;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getCapitoloEntrataPrevisione().getAnnoCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno capitolo"));
		checkNotNull(req.getCapitoloEntrataPrevisione().getNumeroCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero capitolo"));
		checkNotNull(req.getCapitoloEntrataPrevisione().getNumeroArticolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero articolo"));
	}

	@Transactional
	public AggiornaMassivoCapitoloDiEntrataPrevisioneResponse executeService(AggiornaMassivoCapitoloDiEntrataPrevisione serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void init() {
		bilancio = req.getBilancio();
		numeroCapitolo = req.getCapitoloEntrataPrevisione().getNumeroCapitolo();
		numeroArticolo = req.getCapitoloEntrataPrevisione().getNumeroArticolo();
		tipoCapitolo = TipoCapitolo.CAPITOLO_ENTRATA_PREVISIONE;
	}
	
	@Override
	protected void execute() {
		ottieniClassificatoriNonAggiornabili();
		ottieniAttributiNonAggiornabili();

		int pagina = 0;
		
		RicercaSinteticaCapitoloEntrataPrevisioneResponse ricercaRes = null;
		
		CapitoloEntrataPrevisione capitoloModificato = req.getCapitoloEntrataPrevisione();
		
		do {//ciclo le pagine

			ricercaRes = chiamaServizioRicercaSintetica(pagina);
			
			for (CapitoloEntrataPrevisione capitoloCorrente : ricercaRes.getCapitoli()) {
				
				travasaDatiModificabili(capitoloModificato, capitoloCorrente);
				
				AggiornaCapitoloDiEntrataPrevisione richiestaAggiornamento = componiRichiestaAggiornamentoSingolo(capitoloCorrente);
				
				AggiornaCapitoloDiEntrataPrevisioneResponse rispostaAggiornamento = executeExternalServiceSuccess(aggiornaCapitoloDiEntrataPrevisioneService, richiestaAggiornamento);
				
				if (rispostaAggiornamento.isFallimento()) {
					res.setEsito(Esito.FALLIMENTO);
					res.setErrori(rispostaAggiornamento.getErrori());
					
					throw new BusinessException("Esecuzione servizio interno AggiornaCapitoloDiEntrataPrevisioneService richiamato da "+ getServiceName() + " terminata con esito Fallimento." 
							+ "\nErrori riscontrati da AggiornaCapitoloDiEntrataPrevisioneService: {" + rispostaAggiornamento.getDescrizioneErrori().replaceAll("\n", "\n\t") + "}.");
				}
			}
			pagina ++;
		} while (!ricercaRes.getCapitoli().isEmpty());
		
		res.setCapitoloEntrataPrevisione(capitoloModificato);
		res.setEsito(Esito.SUCCESSO);
	}

	/**
	 * Componi richiesta aggiornamento singolo.
	 *
	 * @param capitoloCorrente the capitolo corrente
	 * @return the aggiorna capitolo di entrata previsione
	 */
	private AggiornaCapitoloDiEntrataPrevisione componiRichiestaAggiornamentoSingolo(CapitoloEntrataPrevisione capitoloCorrente) {
		AggiornaCapitoloDiEntrataPrevisione richiestaAggiornamento = new AggiornaCapitoloDiEntrataPrevisione();
		
		richiestaAggiornamento.setCapitoloEntrataPrevisione(capitoloCorrente);
		richiestaAggiornamento.setDataOra(new Date());
		richiestaAggiornamento.setRichiedente(req.getRichiedente());
		
		return richiestaAggiornamento;
	}

	@Override
	protected void travasaDatiModificabili(CapitoloEntrataPrevisione capitoloModificato, CapitoloEntrataPrevisione capitoloCorrente) {
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
		capitoloCorrente.setImportiCapitoloEP(capitoloCorrente.getImportiCapitoloEP());
		capitoloCorrente.setPercEntrataRicorrente(capitoloModificato.getPercEntrataRicorrente());
	}
	
	/**
	 * Chiama servizio ricerca sintetica.
	 *
	 * @param paginaServizioRemoto the pagina servizio remoto
	 * @return the ricerca sintetica capitolo entrata previsione response
	 */
	private RicercaSinteticaCapitoloEntrataPrevisioneResponse chiamaServizioRicercaSintetica(int paginaServizioRemoto) {
		RicercaSinteticaCapitoloEntrataPrevisione ricercaReq = new RicercaSinteticaCapitoloEntrataPrevisione();
		ricercaReq.setEnte(req.getEnte());
		ricercaReq.setRichiedente(req.getRichiedente());
		ricercaReq.setDataOra(new Date());
		ParametriPaginazione parametriPaginazione = new ParametriPaginazione();
		parametriPaginazione.setElementiPerPagina(RISULTATI_PAGINA_REMOTE);
		parametriPaginazione.setNumeroPagina(paginaServizioRemoto);
		ricercaReq.setParametriPaginazione(parametriPaginazione);
		
		RicercaSinteticaCapitoloEPrev parametriRicerca = new RicercaSinteticaCapitoloEPrev();
		parametriRicerca.setAnnoCapitolo(req.getCapitoloEntrataPrevisione().getAnnoCapitolo());
		parametriRicerca.setNumeroCapitolo(req.getCapitoloEntrataPrevisione().getNumeroCapitolo());
		parametriRicerca.setNumeroArticolo(req.getCapitoloEntrataPrevisione().getNumeroArticolo());
		parametriRicerca.setAnnoEsercizio(req.getBilancio().getAnno());
		parametriRicerca.setStatoOperativo(StatoOperativoElementoDiBilancio.VALIDO); //Aggiorna solo i capitoli in stato valido!
		ricercaReq.setRicercaSinteticaCapitoloEPrev(parametriRicerca);
		
		RicercaSinteticaCapitoloEntrataPrevisioneResponse ricercaRes = executeExternalService(ricercaSinteticaCapitoloEntrataPrevisioneService, ricercaReq);
		return ricercaRes;
	}

}
