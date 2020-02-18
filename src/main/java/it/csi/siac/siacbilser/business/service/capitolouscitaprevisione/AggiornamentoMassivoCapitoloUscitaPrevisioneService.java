/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitolouscitaprevisione;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.capitolo.AggiornamentoMassivoCapitoloBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaCapitoloDiUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaCapitoloDiUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaMassivoCapitoloDiUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaMassivoCapitoloDiUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.TipologiaAttributo;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloUPrev;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

/**
 * The Class AggiornamentoMassivoCapitoloUscitaPrevisioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornamentoMassivoCapitoloUscitaPrevisioneService extends AggiornamentoMassivoCapitoloBaseService<AggiornaMassivoCapitoloDiUscitaPrevisione, AggiornaMassivoCapitoloDiUscitaPrevisioneResponse, CapitoloUscitaPrevisione> {
	
	/** The ricerca sintetica capitolo uscita previsione service. */
	@Autowired
	private RicercaSinteticaCapitoloUscitaPrevisioneService ricercaSinteticaCapitoloUscitaPrevisioneService;
	
	/** The aggiorna capitolo di uscita previsione service. */
	@Autowired
	private AggiornaCapitoloDiUscitaPrevisioneNoCheckService aggiornaCapitoloDiUscitaPrevisioneService;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getCapitoloUscitaPrevisione().getAnnoCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno capitolo"));
		checkNotNull(req.getCapitoloUscitaPrevisione().getNumeroCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero capitolo"));
		checkNotNull(req.getCapitoloUscitaPrevisione().getNumeroArticolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero articolo"));
		checkCondition(req.getBilancio().getAnno()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno capitolo"));		
	}
	
	@Override
	protected void init() {
		bilancio = req.getBilancio();
		numeroCapitolo = req.getCapitoloUscitaPrevisione().getNumeroCapitolo();
		numeroArticolo = req.getCapitoloUscitaPrevisione().getNumeroArticolo();
		tipoCapitolo = TipoCapitolo.CAPITOLO_USCITA_PREVISIONE;
	}
	
	@Transactional
	public AggiornaMassivoCapitoloDiUscitaPrevisioneResponse executeService(AggiornaMassivoCapitoloDiUscitaPrevisione serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		ottieniClassificatoriNonAggiornabili();
		ottieniAttributiNonAggiornabili();
		int pagina = 0;
		
		RicercaSinteticaCapitoloUscitaPrevisioneResponse ricercaRes = null;
		
		CapitoloUscitaPrevisione capitoloModificato = req.getCapitoloUscitaPrevisione();
		
		do {//ciclo le pagine

			ricercaRes = chiamaServizioRicercaSintetica(pagina);
			
			for (CapitoloUscitaPrevisione capitoloCorrente : ricercaRes.getCapitoli()) {
				
				travasaDatiModificabili(capitoloModificato, capitoloCorrente);
				
				AggiornaCapitoloDiUscitaPrevisione richiestaAggiornamento = componiRichiestaAggiornamentoSingolo(capitoloCorrente);
				
				AggiornaCapitoloDiUscitaPrevisioneResponse rispostaAggiornamento = executeExternalServiceSuccess(aggiornaCapitoloDiUscitaPrevisioneService, richiestaAggiornamento);
				
				if (rispostaAggiornamento.isFallimento()) {
					//res.setEsito(Esito.FALLIMENTO);
					res.setErrori(rispostaAggiornamento.getErrori());
					throw new BusinessException("Esecuzione servizio interno AggiornaCapitoloDiUscitaPrevisioneService richiamato da "+ getServiceName() + " terminata con esito Fallimento." 
							+ "\nErrori riscontrati da AggiornaCapitoloDiUscitaPrevisioneService: {" + rispostaAggiornamento.getDescrizioneErrori().replaceAll("\n", "\n\t") + "}.");
				}
			}
			pagina ++;
		} while (!ricercaRes.getCapitoli().isEmpty());
		
		res.setCapitoloUscitaPrevisione(capitoloModificato);
		res.setEsito(Esito.SUCCESSO);
	}

	/**
	 * Componi richiesta aggiornamento singolo.
	 *
	 * @param capitoloCorrente the capitolo corrente
	 * @return the aggiorna capitolo di uscita previsione
	 */
	private AggiornaCapitoloDiUscitaPrevisione componiRichiestaAggiornamentoSingolo(CapitoloUscitaPrevisione capitoloCorrente) {
		AggiornaCapitoloDiUscitaPrevisione richiestaAggiornamento = new AggiornaCapitoloDiUscitaPrevisione();
		richiestaAggiornamento.setDataOra(new Date());
		richiestaAggiornamento.setRichiedente(req.getRichiedente());
		richiestaAggiornamento.setCapitoloUscitaPrevisione(capitoloCorrente);
		
		return richiestaAggiornamento;
	}

	@Override
	protected void travasaDatiModificabili(CapitoloUscitaPrevisione capitoloModificato, CapitoloUscitaPrevisione capitoloCorrente) {
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
		capitoloCorrente.setFlagPerMemoria(travasaAttributoModificabile(capitoloModificato.getFlagPerMemoria(), capitoloCorrente.getFlagPerMemoria(),
				TipologiaAttributo.FLAG_PER_MEMORIA));
		capitoloCorrente.setFlagTrasferimentiOrgComunitari(travasaAttributoModificabile(capitoloModificato.getFlagTrasferimentiOrgComunitari(), capitoloCorrente.getFlagTrasferimentiOrgComunitari(),
				TipologiaAttributo.FLAG_TRASFERIMENTO_ORGANI_COMUNITARI));
		capitoloCorrente.setFunzDelegateRegione(travasaAttributoModificabile(capitoloModificato.getFunzDelegateRegione(), capitoloCorrente.getFunzDelegateRegione(),
				TipologiaAttributo.FLAG_FUNZIONI_DELEGATE));
	}

	/**
	 * Chiama servizio ricerca sintetica.
	 *
	 * @param paginaServizioRemoto the pagina servizio remoto
	 * @return the ricerca sintetica capitolo uscita previsione response
	 */
	private RicercaSinteticaCapitoloUscitaPrevisioneResponse chiamaServizioRicercaSintetica(int paginaServizioRemoto) {
		RicercaSinteticaCapitoloUscitaPrevisione ricercaReq = new RicercaSinteticaCapitoloUscitaPrevisione();
		ricercaReq.setEnte(req.getEnte());
		ricercaReq.setRichiedente(req.getRichiedente());
		ricercaReq.setDataOra(new Date());
		ParametriPaginazione parametriPaginazione = new ParametriPaginazione();
		parametriPaginazione.setElementiPerPagina(RISULTATI_PAGINA_REMOTE);
		parametriPaginazione.setNumeroPagina(paginaServizioRemoto);
		ricercaReq.setParametriPaginazione(parametriPaginazione);
		
		RicercaSinteticaCapitoloUPrev parametriRicerca = new RicercaSinteticaCapitoloUPrev();
		parametriRicerca.setAnnoCapitolo(req.getCapitoloUscitaPrevisione().getAnnoCapitolo());
		parametriRicerca.setNumeroCapitolo(req.getCapitoloUscitaPrevisione().getNumeroCapitolo());
		parametriRicerca.setNumeroArticolo(req.getCapitoloUscitaPrevisione().getNumeroArticolo());
		parametriRicerca.setAnnoEsercizio(req.getBilancio().getAnno());
		parametriRicerca.setStatoOperativo(StatoOperativoElementoDiBilancio.VALIDO); //Aggiorna solo i capitoli in stato valido!
		ricercaReq.setRicercaSinteticaCapitoloUPrev(parametriRicerca);
		
		RicercaSinteticaCapitoloUscitaPrevisioneResponse ricercaRes = executeExternalService(ricercaSinteticaCapitoloUscitaPrevisioneService, ricercaReq);
		return ricercaRes;
	}

}
