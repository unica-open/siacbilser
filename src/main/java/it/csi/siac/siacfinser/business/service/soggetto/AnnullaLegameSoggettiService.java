/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.soggetto;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaLegameSoggetti;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaLegameSoggettiResponse;
import it.csi.siac.siacfinser.integration.dad.SoggettoFinDad;
import it.csi.siac.siacfinser.model.codifiche.TipoRelazioneSoggetto;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AnnullaLegameSoggettiService extends AbstractBaseService<AnnullaLegameSoggetti, AnnullaLegameSoggettiResponse> {
	
	@Autowired
	SoggettoFinDad soggettoDad;
	
	@Override
	protected void init() {
		log.debug("","AnnullaLegameSoggettiService - init()");
	}	
	
	/*
	 * VARIAZIONE LINEE GUIDA 3.2.2.2
	 */
	@Override
	@Transactional
	public AnnullaLegameSoggettiResponse executeService(
			AnnullaLegameSoggetti serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	public void execute() {
		final String methodName = "AnnullaLegameSoggettiService - execute()";
		
		String codiceAmbito = req.getCodificaAmbito();
		
		if (org.apache.commons.lang.StringUtils.isEmpty(codiceAmbito))
			codiceAmbito = CostantiFin.AMBITO_FIN;

		
		
		//1. Come prima cosa occorre verificare che il legame indicato in input non sia gia' esistente:
		boolean verificaEsistenzaLegameTraSoggetti = soggettoDad.verificaEsistenzaLegameTraSoggetti(req.getSoggettoCorrente(), 
				req.getSoggettoPrecedente(), req.getTipoLegame(), codiceAmbito, req.getEnte());
		if (verificaEsistenzaLegameTraSoggetti == false) {
			//..nel caso il legame gia' esiste non si puo' procedere con l'operazione e il servizio termina segnalando l'errore
			//Costruzione response esito negativo:
			res.setErrori(Arrays.asList(ErroreFin.LEGAME_NON_VALIDO.getErrore()));
			res.setEsito(Esito.FALLIMENTO);
		} else {
			//..nel caso il legame non esiste si puo' procedere con l'operazione, incocando il metodo annullaLegameSoggetti:
			res.setEsito(Esito.SUCCESSO);
			TipoRelazioneSoggetto tipoRelazioneSoggetto = req.getTipoLegame();
			Soggetto soggettoCorrente = req.getSoggettoCorrente();
			Soggetto soggettoPrecedente = req.getSoggettoPrecedente();
			Richiedente richiedente = req.getRichiedente();
			Ente ente = req.getEnte();
			//imposto l'esito positivo per la response:
			res.setSoggettoCorrente(soggettoDad.annullaLegameSoggetti(richiedente, codiceAmbito, ente, tipoRelazioneSoggetto, soggettoCorrente, soggettoPrecedente));
		}
	}

	@Override
	protected void checkServiceParam() throws ServiceParamError {		

		Soggetto soggettoCorrente = req.getSoggettoCorrente();
		Soggetto soggettoPrecedente = req.getSoggettoPrecedente();
		TipoRelazioneSoggetto tipoLegame = req.getTipoLegame();

		Ente ente = req.getEnte();
		
		if(soggettoCorrente == null){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Soggetto Corrente"));			
		} else if(soggettoPrecedente == null){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Soggetto Precedente"));			
		} else if(tipoLegame == null){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Tipo Legame"));			
		} else if(ente == null){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Ente"));
		} else if(soggettoCorrente.getCodiceSoggetto() == null || "".equalsIgnoreCase(soggettoCorrente.getCodiceSoggetto())){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("SoggettoCorrente.codiceSoggetto"));
		} else if(soggettoPrecedente.getCodiceSoggetto() == null || "".equalsIgnoreCase(soggettoPrecedente.getCodiceSoggetto())){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("SoggettoPrecedente.codiceSoggetto"));
		}			
	}
}
