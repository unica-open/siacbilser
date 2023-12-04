/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitolouscitagestione;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.capitolo.AggiornamentoMassivoCapitoloBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaCapitoloDiUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaCapitoloDiUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaMassivoCapitoloDiUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaMassivoCapitoloDiUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.TipologiaAttributo;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloUGest;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

/**
 * The Class AggiornamentoMassivoCapitoloUscitaGestioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornamentoMassivoCapitoloUscitaGestioneService extends AggiornamentoMassivoCapitoloBaseService<AggiornaMassivoCapitoloDiUscitaGestione, AggiornaMassivoCapitoloDiUscitaGestioneResponse, CapitoloUscitaGestione> {
	
	/** The ricerca sintetica capitolo uscita gestione service. */
	@Autowired
	private RicercaSinteticaCapitoloUscitaGestioneService ricercaSinteticaCapitoloUscitaGestioneService;
	
	/** The aggiorna capitolo di uscita gestione service. */
	@Autowired
	private AggiornaCapitoloDiUscitaGestioneNoCheckService aggiornaCapitoloDiUscitaGestioneService;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getCapitoloUscitaGestione().getAnnoCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno capitolo"));
		checkNotNull(req.getCapitoloUscitaGestione().getNumeroCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero capitolo"));
		checkNotNull(req.getCapitoloUscitaGestione().getNumeroArticolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero articolo"));
	}
	
	@Override
	protected void init() {
		bilancio = req.getBilancio();
		numeroCapitolo = req.getCapitoloUscitaGestione().getNumeroCapitolo();
		numeroArticolo = req.getCapitoloUscitaGestione().getNumeroArticolo();
		tipoCapitolo = TipoCapitolo.CAPITOLO_USCITA_GESTIONE;
	}

	@Transactional
	public AggiornaMassivoCapitoloDiUscitaGestioneResponse executeService(AggiornaMassivoCapitoloDiUscitaGestione serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		ottieniClassificatoriNonAggiornabili();
		ottieniAttributiNonAggiornabili();

		int pagina = 0;
		
		RicercaSinteticaCapitoloUscitaGestioneResponse ricercaRes = null;
		
		CapitoloUscitaGestione capitoloModificato = req.getCapitoloUscitaGestione();
		
		do {//ciclo le pagine

			ricercaRes = chiamaServizioRicercaSintetica(pagina);
			
			for (CapitoloUscitaGestione capitoloCorrente : ricercaRes.getCapitoli()) {
				
				travasaDatiModificabili(capitoloModificato, capitoloCorrente);
				
				AggiornaCapitoloDiUscitaGestione richiestaAggiornamento = componiRichiestaAggiornamentoSingolo(capitoloCorrente);
				
				AggiornaCapitoloDiUscitaGestioneResponse rispostaAggiornamento = executeExternalServiceSuccess(aggiornaCapitoloDiUscitaGestioneService, richiestaAggiornamento);
				
				if (rispostaAggiornamento.isFallimento()) {
					res.setEsito(Esito.FALLIMENTO);
					res.setErrori(rispostaAggiornamento.getErrori());
					throw new BusinessException("Esecuzione servizio interno AggiornaCapitoloDiUscitaGestioneService richiamato da "+ getServiceName() + " terminata con esito Fallimento." 
							+ "\nErrori riscontrati da AggiornaCapitoloDiUscitaGestioneService: {" + rispostaAggiornamento.getDescrizioneErrori().replaceAll("\n", "\n\t") + "}.");
				}
			}
			pagina ++;
		} while (!ricercaRes.getCapitoli().isEmpty());
		
		res.setCapitoloUscitaGestione(capitoloModificato);
		res.setEsito(Esito.SUCCESSO);
	}

	/**
	 * Componi richiesta aggiornamento singolo.
	 *
	 * @param capitoloCorrente the capitolo corrente
	 * @return the aggiorna capitolo di uscita gestione
	 */
	private AggiornaCapitoloDiUscitaGestione componiRichiestaAggiornamentoSingolo(CapitoloUscitaGestione capitoloCorrente) {
		AggiornaCapitoloDiUscitaGestione richiestaAggiornamento = new AggiornaCapitoloDiUscitaGestione();
		
		richiestaAggiornamento.setRichiedente(req.getRichiedente());
		richiestaAggiornamento.setCapitoloUscitaGestione(capitoloCorrente);
		richiestaAggiornamento.setDataOra(new Date());
		
		return richiestaAggiornamento;
	}

	@Override
	protected void travasaDatiModificabili(CapitoloUscitaGestione capitoloModificato, CapitoloUscitaGestione capitoloCorrente) {
		super.travasaDatiModificabili(capitoloModificato, capitoloCorrente);
		
		// Classificatori
		capitoloCorrente.setMissione(travasaClassificatoreModificabile(capitoloModificato.getMissione(), capitoloCorrente.getMissione(),
			TipologiaClassificatore.MISSIONE));
		capitoloCorrente.setProgramma(travasaClassificatoreModificabile(capitoloModificato.getProgramma(), capitoloCorrente.getProgramma(),
			TipologiaClassificatore.PROGRAMMA));
		capitoloCorrente.setClassificazioneCofog(travasaClassificatoreModificabile(capitoloModificato.getClassificazioneCofog(), capitoloCorrente.getClassificazioneCofog(),
			TipologiaClassificatore.CLASSIFICAZIONE_COFOG, TipologiaClassificatore.DIVISIONE_COFOG, TipologiaClassificatore.GRUPPO_COFOG, TipologiaClassificatore.CLASSE_COFOG));
		capitoloCorrente.setClassificazioneCofogProgramma(travasaClassificatoreModificabile(capitoloModificato.getClassificazioneCofogProgramma(), capitoloCorrente.getClassificazioneCofogProgramma(),
				TipologiaClassificatore.CLASSIFICAZIONE_COFOG, TipologiaClassificatore.DIVISIONE_COFOG, TipologiaClassificatore.GRUPPO_COFOG, TipologiaClassificatore.CLASSE_COFOG));
		capitoloCorrente.setTitoloSpesa(travasaClassificatoreModificabile(capitoloModificato.getTitoloSpesa(), capitoloCorrente.getTitoloSpesa(),
				TipologiaClassificatore.TITOLO_SPESA));
		capitoloCorrente.setMacroaggregato(travasaClassificatoreModificabile(capitoloModificato.getMacroaggregato(), capitoloCorrente.getMacroaggregato(),
				TipologiaClassificatore.MACROAGGREGATO));
		capitoloCorrente.setPerimetroSanitarioSpesa(travasaClassificatoreModificabile(capitoloModificato.getPerimetroSanitarioSpesa(), capitoloCorrente.getPerimetroSanitarioSpesa(),
			TipologiaClassificatore.PERIMETRO_SANITARIO_SPESA));
		capitoloCorrente.setRicorrenteSpesa(travasaClassificatoreModificabile(capitoloModificato.getRicorrenteSpesa(), capitoloCorrente.getRicorrenteSpesa(),
			TipologiaClassificatore.RICORRENTE_SPESA));
		capitoloCorrente.setSiopeSpesa(travasaClassificatoreModificabile(capitoloModificato.getSiopeSpesa(), capitoloCorrente.getSiopeSpesa(),
			TipologiaClassificatore.SIOPE_SPESA, TipologiaClassificatore.SIOPE_SPESA_I, TipologiaClassificatore.SIOPE_SPESA_II, TipologiaClassificatore.SIOPE_SPESA_III));
		capitoloCorrente.setTransazioneUnioneEuropeaSpesa(travasaClassificatoreModificabile(capitoloModificato.getTransazioneUnioneEuropeaSpesa(), capitoloCorrente.getTransazioneUnioneEuropeaSpesa(),
			TipologiaClassificatore.TRANSAZIONE_UE_SPESA));
		capitoloCorrente.setPoliticheRegionaliUnitarie(travasaClassificatoreModificabile(capitoloModificato.getPoliticheRegionaliUnitarie(), capitoloCorrente.getPoliticheRegionaliUnitarie(),
				TipologiaClassificatore.POLITICHE_REGIONALI_UNITARIE));
		
		// Attributi
		capitoloCorrente.setFlagAssegnabile(travasaAttributoModificabile(capitoloModificato.getFlagAssegnabile(), capitoloCorrente.getFlagAssegnabile(),
			TipologiaAttributo.FLAG_ASSEGNABILE));
//		capitoloCorrente.setFlagFondoPluriennaleVinc(travasaAttributoModificabile(capitoloModificato.getFlagFondoPluriennaleVinc(), capitoloCorrente.getFlagFondoPluriennaleVinc(),
//				TipologiaAttributo.FLAG_FONDO_PLURIENNALE_VINC));
		capitoloCorrente.setFlagFondoSvalutazioneCred(travasaAttributoModificabile(capitoloModificato.getFlagFondoSvalutazioneCred(), capitoloCorrente.getFlagFondoSvalutazioneCred(),
				TipologiaAttributo.FLAG_FONDO_SVALUTAZIONE_CREDITI));
		capitoloCorrente.setFlagTrasferimentiOrgComunitari(travasaAttributoModificabile(capitoloModificato.getFlagTrasferimentiOrgComunitari(), capitoloCorrente.getFlagTrasferimentiOrgComunitari(),
				TipologiaAttributo.FLAG_TRASFERIMENTO_ORGANI_COMUNITARI));
		capitoloCorrente.setFunzDelegateRegione(travasaAttributoModificabile(capitoloModificato.getFunzDelegateRegione(), capitoloCorrente.getFunzDelegateRegione(),
				TipologiaAttributo.FLAG_FUNZIONI_DELEGATE));
		
		// Non aggiornabili
		capitoloCorrente.setImportiCapitoloUG(capitoloCorrente.getImportiCapitoloUG());
	}

	/**
	 * Chiama servizio ricerca sintetica.
	 *
	 * @param paginaServizioRemoto the pagina servizio remoto
	 * @return the ricerca sintetica capitolo uscita gestione response
	 */
	private RicercaSinteticaCapitoloUscitaGestioneResponse chiamaServizioRicercaSintetica(int paginaServizioRemoto) {
		RicercaSinteticaCapitoloUscitaGestione ricercaReq = new RicercaSinteticaCapitoloUscitaGestione();
		ricercaReq.setEnte(req.getEnte());
		ricercaReq.setRichiedente(req.getRichiedente());
		ricercaReq.setDataOra(new Date());
		ParametriPaginazione parametriPaginazione = new ParametriPaginazione();
		parametriPaginazione.setElementiPerPagina(RISULTATI_PAGINA_REMOTE);
		parametriPaginazione.setNumeroPagina(paginaServizioRemoto);
		ricercaReq.setParametriPaginazione(parametriPaginazione);
		
		RicercaSinteticaCapitoloUGest parametriRicerca = new RicercaSinteticaCapitoloUGest();
		parametriRicerca.setAnnoCapitolo(req.getCapitoloUscitaGestione().getAnnoCapitolo());
		parametriRicerca.setNumeroCapitolo(req.getCapitoloUscitaGestione().getNumeroCapitolo());
		parametriRicerca.setNumeroArticolo(req.getCapitoloUscitaGestione().getNumeroArticolo());
		parametriRicerca.setAnnoEsercizio(req.getBilancio().getAnno());
		parametriRicerca.setStatoOperativo(StatoOperativoElementoDiBilancio.VALIDO); //Aggiorna solo i capitoli in stato valido!
		ricercaReq.setRicercaSinteticaCapitoloUGest(parametriRicerca);
		
		RicercaSinteticaCapitoloUscitaGestioneResponse ricercaRes = executeExternalService(ricercaSinteticaCapitoloUscitaGestioneService, ricercaReq);
		return ricercaRes;
	}

}
