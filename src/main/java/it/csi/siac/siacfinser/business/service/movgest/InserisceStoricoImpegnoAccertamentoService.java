/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.movgest;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceStoricoImpegnoAccertamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceStoricoImpegnoAccertamentoResponse;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.model.StoricoImpegnoAccertamento;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceStoricoImpegnoAccertamentoService extends BaseInserisceAggiornaStoricoImpegnoAccertamentoService<InserisceStoricoImpegnoAccertamento, InserisceStoricoImpegnoAccertamentoResponse> {
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		checkDatiBase();		
	}

	
	@Override
	protected void init() {
		bilancio = req.getBilancio();
	}
	
	
	
	@Override
	@Transactional
	public InserisceStoricoImpegnoAccertamentoResponse executeService(InserisceStoricoImpegnoAccertamento serviceRequest) {
		return super.executeService(serviceRequest);
	}	
	
	@Override
	public void execute() {
		final String methodName = "InserisceImpegniService : execute()";
		log.debug(methodName, "- Begin");
		
		StoricoImpegnoAccertamento storicoImpegniAccertamenti = req.getStoricoImpegnoAccertamento();

		//2. Si inizializza l'oggetto DatiOperazioneDto, dto di comodo generico che verra' passato tra i metodi:
		DatiOperazioneDto datiOperazioneDto = commonDad.inizializzaDatiOperazione(req.getEnte(), req.getRichiedente(), Operazione.INSERIMENTO, req.getBilancio().getAnno());
		
		caricaDatiImpegnoSubimpegno(storicoImpegniAccertamenti);

		checkImpegnoAccertamento(storicoImpegniAccertamenti.getImpegno(), storicoImpegniAccertamenti.getAccertamento());
		
		//inserisco
		storicoImpegnoAccertamentoDad.inserisciStorico(storicoImpegniAccertamenti, datiOperazioneDto);
		
		//TODOIn Predisposizione Consuntivo deve avere ripercussioni anche sull’anno successivo.
		effettuaOperazioniDoppiaGestione(storicoImpegniAccertamenti, datiOperazioneDto);
		
		res.setEsito(Esito.SUCCESSO);
	}
	
	/**
	 * @param storicoImpegniAccertamenti
	 * @param datiOperazioneDto
	 */
	@Override
	protected void salvaRecordInDoppiaGestione(StoricoImpegnoAccertamento storicoImpegniAccertamenti, String annoBilancioPiuUno,	DatiOperazioneDto datiOperazioneDto) {
		storicoImpegnoAccertamentoDad.inserisciStorico(storicoImpegniAccertamenti, datiOperazioneDto);
	}

}