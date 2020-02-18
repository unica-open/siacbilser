/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.soggetto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiaveResponse;
import it.csi.siac.siacfinser.integration.dad.SoggettoFinDad;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSoggettoK;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSoggettoSiacPerChiaveService extends AbstractSoggettoService<RicercaSoggettoPerChiave, RicercaSoggettoPerChiaveResponse>{

	@Autowired
	private SoggettoFinDad soggettoDad;

	@Override
	protected void init(){
		final String methodName = "RicercaSoggettoSiacPerChiaveService : init()";
		log.debug(methodName, " In esecuzione");
		initModalitaPagamentoSoggettoHelper();
	}
	

	@Override
	public void execute(){
		
		String methodName = "RicercaSoggettoSiacPerChiaveService-execute()";
		log.debug(methodName, " Begin");
		ParametroRicercaSoggettoK parametroSoggettoK = req.getParametroSoggettoK();
		log.debug(methodName, "- req.getParametroSoggettoK() " + parametroSoggettoK);

	
		Ente ente = req.getRichiedente().getAccount().getEnte();
		Integer idEnte = ente.getUid();
		Soggetto sogg = null;
		Soggetto soggMod = null;

		String codiceAmbito = req.getCodificaAmbito();
		
		DatiOperazioneDto datiOperazioneDto = commonDad.inizializzaDatiOperazione(ente, req.getRichiedente(), Operazione.RICERCA, codiceAmbito, null);

		if (org.apache.commons.lang.StringUtils.isEmpty(codiceAmbito)){
			codiceAmbito = Constanti.AMBITO_FIN;
		}

		// 2. Richiamo il metodo che va a caricare il soggetto richiesto:
		sogg = soggettoDad.ricercaSoggetto(codiceAmbito, idEnte, 
				parametroSoggettoK, true);
		
		if (sogg != null) {
			// Richiamo il metodo completaInformazioni che "veste" il soggetto
			// con dati ulteriori:
			sogg = completaInformazioni(sogg, codiceAmbito, idEnte, sogg.getCodiceSoggetto(),
					parametroSoggettoK.isIncludeModif(), req.getRichiedente(),datiOperazioneDto);
			// Ricerco l'eventuale versione in modifica (vedi tabelle _mod):
			soggMod = soggettoDad.ricercaSoggettoInModifica(idEnte, sogg.getCodiceSoggetto());
		}

		// 3. Restituzione dell'output:
		res.setEsito(Esito.SUCCESSO);
		res.setSoggetto(sogg);
		res.setSoggettoInModifica(soggMod);
		
		if (sogg != null)
		{
			// Jira-1640
			res.setListaModalitaPagamentoSoggetto(sogg.getModalitaPagamentoList());
			res.setListaSecondariaSoggetto(sogg.getSediSecondarie());
		}
	}

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		final String methodName = "RicercaSoggettoSiacPerChiaveService : checkServiceParam()";
		log.debug(methodName, "In Esecuzione " + req.getParametroSoggettoK());
		
		checkCondition(null != req.getParametroSoggettoK(),
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("CHIAVE RICERCA SOGGETTO"));
		
		checkCondition(req.getParametroSoggettoK().getCodice() !=null ||  
				req.getParametroSoggettoK().getMatricola()!=null,
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Indicare una chiave di ricerca"));
	}
}
