/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.movgest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaAccertamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaAccertamentoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaImpegno;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaImpegnoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaAggiornaMovimento;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaAggiornaMovimentoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaMovimentoEntrata;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaMovimentoEntrataResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaMovimentoSpesa;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaMovimentoSpesaResponse;
import it.csi.siac.siacfinser.integration.dad.ImpegnoOttimizzatoDad;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneEntrata;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestione.StatoOperativoModificaMovimentoGestione;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AnnullaAggiornaImpegnoService
		extends AbstractBaseService<AnnullaAggiornaMovimento, AnnullaAggiornaMovimentoResponse> {

	@Autowired
	AnnullaMovimentoSpesaService annullaImpegno;

	@Autowired
	AnnullaMovimentoEntrataService annullaAccertamento;

	@Autowired
	AggiornaImpegnoService aggiornaImpegno;
	
	@Autowired
	AggiornaAccertamentoService aggiornaAccertamento;
	
	@Autowired
	ImpegnoOttimizzatoDad movgestDad;

	private BigDecimal importoAttualeMovgest;

	private Integer movgestTsId;

	@Override
	@Transactional(timeout = 5000, readOnly = true)
	public AnnullaAggiornaMovimentoResponse executeService(AnnullaAggiornaMovimento serviceRequest) {

		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		// costruzione request da req del service
		ente = req.getEnte();

		AnnullaMovimentoSpesaResponse resAnn = new AnnullaMovimentoSpesaResponse();
		AnnullaMovimentoEntrataResponse resEnt = new AnnullaMovimentoEntrataResponse();
		AggiornaImpegnoResponse resAgg = new AggiornaImpegnoResponse();
		AggiornaAccertamentoResponse resAggAcc = new AggiornaAccertamentoResponse();
		
		//SIAC-7349 - SR167 - Start - MR - 01/06/2020: creo un array list di Integer, dove vado a settare gli UID delle modifiche annullate
		//per cambiare lo stato alle modifiche che vengono passate in request all'Aggiorna.
		//Essendo che le request per Annulla/Aggiorna vengono costruite lato FRONT-END, non avremo il cambio di stato automatico
		List<Integer> listaUidModivicheEntrataAnnullate = new ArrayList<Integer>();
		
		DatiOperazioneDto datiOperazione = commonDad.inizializzaDatiOperazione(ente, req.getRichiedente(), Operazione.MODIFICA, req.getAnnoBilancio());
		//SIAC-8647
		impostaDatiPerControlloAggiornamentoConcorrenteImporto(datiOperazione);
		
		if (req.isFlagAnnullaConsentito() == true) {
			AnnullaMovimentoSpesa reqAnn = req.getAnnullaMovimento();
			
			if (reqAnn != null) {
				/*
				 * SIAC-7689
				 * In caso di annullamento e aggiornamento del movimento di gestione su stessa transazione
				 * se prevista la doppia gestione e non è presente il movimento per
				 * l'anno di bilancio successivo, l'inserimento dello stesso vine fatto solo
				 * nel processo di aggiornamento e non in quell di annullamento settando a true
				 * il flag di skip.
				 */
				if(req.isFlagAggiornaConsentito() == true && reqAnn.getImpegno()!= null){
					reqAnn.getImpegno().setFlagSkipInsertMovDoppiaGestione(true);
				}
				resAnn = executeExternalServiceSuccess(annullaImpegno, reqAnn);

				if (resAnn.isFallimento()) {
					throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Errori da gestire"));
				}

			} else {
				AnnullaMovimentoEntrata reqEnt = req.getAnnullaMovimentoEntrata();
				/*
				 * SIAC-7689
				 * In caso di annullamento e aggiornamento del movimento di gestione su stessa transazione
				 * se prevista la doppia gestione e non è presente il movimento per
				 * l'anno di bilancio successivo, l'inserimento dello stesso vine fatto solo
				 * nel processo di aggiornamento e non in quell di annullamento settando a true
				 * il flag di skip.
				 */
				if(req.isFlagAggiornaConsentito() == true && reqEnt.getAccertamento()!= null){
					reqEnt.getAccertamento().setFlagSkipInsertMovDoppiaGestione(true);
				}
				//SIAC-7689 FINE 
				resEnt = executeExternalServiceSuccess(annullaAccertamento, reqEnt);

				if (resEnt.isFallimento()) {
					throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Errori da gestire"));
				}
				//SIAC-7349 - SR167 - Start - MR - 01/06/2020: creo un array list di Integer, dove vado a settare gli UID delle modifiche annullate
				//per cambiare lo stato alle modifiche che vengono passate in request all'Aggiorna.
				//Essendo che le request per Annulla/Aggiorna vengono costruite lato FRONT-END, non avremo il cambio di stato automatico
				//Se il servizio va a buon fine
				for(ModificaMovimentoGestioneEntrata mmge : reqEnt.getAccertamento().getListaModificheMovimentoGestioneEntrata()){
					listaUidModivicheEntrataAnnullate.add(mmge.getUid());
				}

			}
		}

		if (req.isFlagAggiornaConsentito() == true) {
			AggiornaImpegno reqAgg = req.getAggiornaImpegno();
			if(reqAgg != null){
				//SIAC-7689
				if(reqAgg.getImpegno()!= null){
					reqAgg.getImpegno().setFlagSkipInsertMovDoppiaGestione(false);
				}
				resAgg = executeExternalServiceSuccess(aggiornaImpegno, reqAgg);
				if (resAgg.isFallimento()) {
					throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Errori da gestire"));
					
				}
				res.setImpegno(resAgg.getImpegno());
			}else{
				AggiornaAccertamento reqAccAgg = req.getAggiornaAccertamento();
				//SIAC-7689
				if(reqAccAgg.getAccertamento()!= null){
					reqAccAgg.getAccertamento().setFlagSkipInsertMovDoppiaGestione(false);
				}
				
				//SIAC-7349 - SR167 - Start - MR - 01/06/2020: creo un array list di Integer, dove vado a settare gli UID delle modifiche annullate
				//per cambiare lo stato alle modifiche che vengono passate in request all'Aggiorna.
				//Essendo che le request per Annulla/Aggiorna vengono costruite lato FRONT-END, non avremo il cambio di stato automatico
				//Se il servizio va a buon fine
				for(Integer uidModificaEntrataAnnullata : listaUidModivicheEntrataAnnullate){
					for(ModificaMovimentoGestioneEntrata mmge : reqAccAgg.getAccertamento().getListaModificheMovimentoGestioneEntrata()){
						if(uidModificaEntrataAnnullata == mmge.getUid()){
							mmge.setStatoOperativoModificaMovimentoGestione(StatoOperativoModificaMovimentoGestione.ANNULLATO);
						}
					}
				}
				resAggAcc = executeExternalServiceSuccess(aggiornaAccertamento, reqAccAgg);
				if (resAggAcc.isFallimento()) {
					throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Errori da gestire"));
					
				}
				res.setAccertamento(resAggAcc.getAccertamento());
				
			}
			//SIAC-8647
			checkAggiornamentoConcorrente(datiOperazione);
		}

		res.setEsito(Esito.SUCCESSO);

	}

	private void impostaDatiPerControlloAggiornamentoConcorrenteImporto(DatiOperazioneDto datiOperazione) {
		final String methodName="impostaDatiPerControlloAggiornamentoConcorrenteImporto";
		Integer idEnte = datiOperazione.getSiacTEnteProprietario().getUid();
		Integer idMovgest = req.getIdMovimento() != 0? req.getIdMovimento() : 
			movgestDad.getUidMovgestBychiaveLogica(idEnte, req.getAnnoMovimento(), req.getNumeroMovimento(), req.getTipoMovimento(), req.getAnnoBilancio()); 
		if(StringUtils.isBlank(req.getNumeroSub())) {
			//testatamovimento
			movgestTsId = movgestDad.getUidMovgestTsTestataByMovgestid(datiOperazione, idMovgest);
			log.info(methodName, "Trovato idMovgestTs: " + movgestTsId + " (testata) a partire dal movgestId " + idMovgest);
		}else {
			SubImpegno subimpegno = movgestDad.getSubimpegno(req.getIdMovimento(), datiOperazione, req.getNumeroSub());
			movgestTsId = subimpegno.getUid();
			log.info(methodName, "Trovato idMovgestTs: " + movgestTsId + " (sub) a partire dal movgestId " + idMovgest);
		}
		importoAttualeMovgest = movgestDad.getImportoAttualeMovgest(movgestTsId, idEnte);
		log.info(methodName, "Importo attuale all'interno della transazione: " + importoAttualeMovgest);
	}
	
	private void checkAggiornamentoConcorrente(DatiOperazioneDto datiOperazione) {
		final String methodName="impostaDatiPerControlloAggiornamentoConcorrenteImporto";
		Integer idEnte = datiOperazione.getSiacTEnteProprietario().getUid();
		BigDecimal importoAttualeMovgestOutsideTransaction = movgestDad.getImportoAttualeMovgestOutsideTransaction(movgestTsId, idEnte);
		log.debug(methodName, "Importo attuale al di fuori della transazione: " + importoAttualeMovgest);
		if(importoAttualeMovgest.compareTo(importoAttualeMovgestOutsideTransaction) != 0) {
			throw new BusinessException(ErroreFin.IMPORTO_MODIFICATO_CONCORRENZA.getErrore(req.getTipoMovimento()));
		}
	}
}
