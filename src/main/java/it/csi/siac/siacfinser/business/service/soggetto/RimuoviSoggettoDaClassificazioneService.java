/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.soggetto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.frontend.webservice.msg.RimuoviSoggettoDaClassificazione;
import it.csi.siac.siacfinser.frontend.webservice.msg.RimuoviSoggettoDaClassificazioneResponse;
import it.csi.siac.siacfinser.integration.dad.SoggettoFinDad;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.util.Operazione;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RimuoviSoggettoDaClassificazioneService extends AbstractSoggettoService<RimuoviSoggettoDaClassificazione, RimuoviSoggettoDaClassificazioneResponse> {

	@Autowired
	SoggettoFinDad soggettoDad;

	/*
	 * VARIAZIONE LINEE GUIDA 3.2.2.2
	 */
	@Override
	@Transactional
	public RimuoviSoggettoDaClassificazioneResponse executeService(
			RimuoviSoggettoDaClassificazione serviceRequest) {
		// TODO Auto-generated method stub
		return super.executeService(serviceRequest);
	}

	@Override
	public void execute() {
		final String methodName = "RimuoviSoggettoDaClassificazioneService - execute()";

		//1. Lettura variabili di input
		Richiedente richiedente = req.getRichiedente();
		Ente ente = req.getEnte();
		Integer idEnte = ente.getUid();
		String codiceSoggetto = req.getCodiceSoggetto();
		String codiceClassificazione = req.getCodiceClassificazione();
		
		String codiceAmbito = req.getCodificaAmbito();

		if (org.apache.commons.lang.StringUtils.isEmpty(codiceAmbito))
			codiceAmbito = Constanti.AMBITO_FIN;
		
		//1. Inizializzo dati operazione:
		DatiOperazioneDto datiOperazione = commonDad.inizializzaDatiOperazione(ente, req.getRichiedente(), Operazione.MODIFICA, null);
		
		//2. Effettuo l'aggiornamento:
		soggettoDad.rimuoviSoggettoDaClassificazione(codiceSoggetto, codiceClassificazione, codiceAmbito, richiedente, idEnte,datiOperazione);
		
		res.setErrori(null);
		res.setEsito(Esito.SUCCESSO);
	}
	

	@Override
	protected void checkServiceParam() throws ServiceParamError {	

	}
}