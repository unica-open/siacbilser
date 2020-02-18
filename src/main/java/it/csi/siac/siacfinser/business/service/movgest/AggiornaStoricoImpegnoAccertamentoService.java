/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.movgest;

import org.apache.commons.lang.SerializationUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaStoricoImpegnoAccertamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaStoricoImpegnoAccertamentoResponse;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.StoricoImpegnoAccertamento;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaStoricoImpegnoAccertamentoService extends BaseInserisceAggiornaStoricoImpegnoAccertamentoService<AggiornaStoricoImpegnoAccertamento, AggiornaStoricoImpegnoAccertamentoResponse> {
	
	private StoricoImpegnoAccertamento storicoImpegnoAccertamentoOld;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		checkDatiBase();
		checkEntita(req.getStoricoImpegnoAccertamento(), "storico");
	}

	
	@Override
	protected void init() {
		bilancio = req.getBilancio();
	}
	
	
	
	@Override
	@Transactional
	public AggiornaStoricoImpegnoAccertamentoResponse executeService(AggiornaStoricoImpegnoAccertamento serviceRequest) {
		return super.executeService(serviceRequest);
	}	
	
	@Override
	public void execute() {
		final String methodName = "AggiornaStoricoImpegnoAccertamentoService : execute()";
		log.debug(methodName, "- Begin");
		
		StoricoImpegnoAccertamento storicoImpegniAccertamenti = req.getStoricoImpegnoAccertamento();
		//2. Si inizializza l'oggetto DatiOperazioneDto, dto di comodo generico che verra' passato tra i metodi:
		DatiOperazioneDto datiOperazioneDto = commonDad.inizializzaDatiOperazione(req.getEnte(), req.getRichiedente(), Operazione.MODIFICA, req.getBilancio().getAnno());
		
		checkEsistenzaStorico(storicoImpegniAccertamenti);
		
		caricaDatiImpegnoSubimpegno(storicoImpegniAccertamenti);

		checkImpegnoAccertamento(storicoImpegniAccertamenti.getImpegno(), storicoImpegniAccertamenti.getAccertamento());

		//lo devo fare qui, prima gestisco il record precedente perchè poi non posso piu' trovare il record di doppia gestione
		effettuaOperazioniDoppiaGestione(storicoImpegniAccertamenti, datiOperazioneDto);
		
		//inserisco
		storicoImpegnoAccertamentoDad.aggiornaStorico(storicoImpegniAccertamenti, datiOperazioneDto);
		
		res.setEsito(Esito.SUCCESSO);
	}

	
	/**
	 * Check esistenza storico.
	 *
	 * @param storicoImpegniAccertamenti the storico impegni accertamenti
	 */
	private void checkEsistenzaStorico(StoricoImpegnoAccertamento storicoImpegniAccertamenti) {
		storicoImpegnoAccertamentoOld = storicoImpegnoAccertamentoDad.ricercaDettaglio(storicoImpegniAccertamenti);
		if(storicoImpegnoAccertamentoOld == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("storico per impegno " + creaStringaMovgest(storicoImpegniAccertamenti.getImpegno()) + " e accertamento: " + creaStringaMovgest(storicoImpegniAccertamenti.getAccertamento())));
		}
	}
	
	private String creaStringaMovgest(MovimentoGestione movgest) {
		if(movgest == null){
			return "";
		}
		return new StringBuilder()
				.append(movgest.getAnnoMovimento())
				.append("/")
				.append(movgest.getNumero() != null? movgest.getNumero() : "N.D.")
				.toString();
	}

	
	@Override
	protected void salvaRecordInDoppiaGestione(StoricoImpegnoAccertamento storicoImpegniAccertamenti,String annoBilancioPiuUno,	DatiOperazioneDto datiOperazioneDto) {
		final String methodName = "salvaRecordInDoppiaGestione";
		int uidRecordAnnoBilancioPiuUno = getUidRecordInDoppiaGestioneAnnoBilancioPiuUno(annoBilancioPiuUno);
		storicoImpegniAccertamenti.setUid(uidRecordAnnoBilancioPiuUno);
		log.debug(methodName, "uidRecordAnnoBilancioPiuUno: " + uidRecordAnnoBilancioPiuUno);
		if(uidRecordAnnoBilancioPiuUno == 0) {
			DatiOperazioneDto datiOperazioneDtoInserimento = (DatiOperazioneDto) SerializationUtils.clone(datiOperazioneDto);
			datiOperazioneDtoInserimento.setOperazione(Operazione.INSERIMENTO);
			storicoImpegnoAccertamentoDad.inserisciStorico(storicoImpegniAccertamenti, datiOperazioneDtoInserimento);
			return;
		}
		storicoImpegnoAccertamentoDad.aggiornaStorico(storicoImpegniAccertamenti, datiOperazioneDto);
	}


	
	
	private int getUidRecordInDoppiaGestioneAnnoBilancioPiuUno(String annoBilancioPiuUno) {
		
		StoricoImpegnoAccertamento ricercaDettaglio = storicoImpegnoAccertamentoDad.ricercaRecordCorrispondenteInAnnoBilancio(req.getStoricoImpegnoAccertamento(), annoBilancioPiuUno);
		return ricercaDettaglio != null? ricercaDettaglio.getUid() : 0;
	}

}