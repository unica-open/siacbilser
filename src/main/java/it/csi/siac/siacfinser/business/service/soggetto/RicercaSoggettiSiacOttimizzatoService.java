/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.soggetto;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettiOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettiOttimizzatoResponse;
import it.csi.siac.siacfinser.integration.dad.SoggettoFinDad;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoRicercaSoggettiDto;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSoggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSoggettiSiacOttimizzatoService extends 
		AbstractBaseService<RicercaSoggettiOttimizzato, RicercaSoggettiOttimizzatoResponse>
{

	@Autowired
	private SoggettoFinDad soggettoDad;

	@Override
	protected void init()
	{
		final String methodName = "RicercaSoggettiSiacOttimizzatoService : init()";
		log.debug(methodName, "Begin");
	}
	
	
//	@Transactional(readOnly = true)
//	public RicercaSoggettiOttimizzatoResponse executeService(RicercaSoggettiOttimizzato serviceRequest)
//	{
//		return super.executeService(serviceRequest);
//	}

	@Override
	public void execute()
	{
		final String methodName = "RicercaSoggettiSiacOttimizzatoService - execute()";
		// 1. Vengono letti i valori ricevuti in input dalla request
		ParametroRicercaSoggetto paramRicSogg = req.getParametroRicercaSoggetto();
		Ente ente = req.getRichiedente().getAccount().getEnte();
		Integer idEnte = ente.getUid();
		
		String codiceAmbito = req.getCodiceAmbito();
		
		DatiOperazioneDto datiOperazione = commonDad.inizializzaDatiOperazione(ente, req.getRichiedente(), Operazione.RICERCA, null);

		if (codiceAmbito == null){
			codiceAmbito = CostantiFin.AMBITO_FIN;
		}

		// 2. Si invoca il metodo ricercaSoggetti che ci restituisce il numero
		// di risultati attesi dalla query composta
		// secondo i parametri di ricerca
		
		EsitoRicercaSoggettiDto esitoRicerca = soggettoDad.ricercaSoggettiOttimizzato(paramRicSogg, idEnte, codiceAmbito, datiOperazione,  req.getNumPagina(), req.getNumRisultatiPerPagina());

		List<Soggetto> paginata = esitoRicerca.getPaginaRichiesta();
		
		// 4. Viene costruita la response per esito OK
		res.setEsito(Esito.SUCCESSO);
		res.setSoggetti(paginata);
		res.setNumRisultati(esitoRicerca.getNumeroTotaleSoggetti());
		res.setNumPagina(req.getNumPagina());
	}

	@Override
	protected void checkServiceParam() throws ServiceParamError
	{
		final String methodName = "RicercaSoggettiSiacOttimizzatoService : checkServiceParam()";
		log.debug(methodName, " Begin");

		ParametroRicercaSoggetto paramRicSogg = req.getParametroRicercaSoggetto();

		checkCondition(paramRicSogg != null,
				ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore("NESSUN PARAMETRO INIZIALIZZATO"));

		checkCondition(
				StringUtils.isNotBlank(paramRicSogg.getCodiceSoggetto())
						|| StringUtils.isNotBlank(paramRicSogg.getPartitaIva())
						|| StringUtils.isNotBlank(paramRicSogg.getCodiceFiscale())
						|| StringUtils.isNotBlank(paramRicSogg.getCodiceFiscaleEstero())
						|| StringUtils.isNotBlank(paramRicSogg.getDenominazione())
						|| StringUtils.isNotBlank(paramRicSogg.getClasse())
						|| StringUtils.isNotBlank(paramRicSogg.getTitoloNaturaGiuridica())
						|| StringUtils.isNotBlank(paramRicSogg.getFormaGiuridica())
						|| StringUtils.isNotBlank(paramRicSogg.getSesso())
						|| StringUtils.isNotBlank(paramRicSogg.getMatricola())
						|| StringUtils.isNotBlank(paramRicSogg.getComuneNascita())
						|| StringUtils.isNotBlank(paramRicSogg.getIdStatoSoggetto())
						|| StringUtils.isNotBlank(paramRicSogg.getCodiceStatoSoggetto())
						|| StringUtils.isNotBlank(paramRicSogg.getCodiceSoggettoPrecedente())
						|| StringUtils.isNotBlank(paramRicSogg.getCodiceSoggettoSuccessore()),
				ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore("NESSUN PARAMETRO INIZIALIZZATO"));

		checkCondition(
				(StringUtils.isBlank(paramRicSogg.getSesso()) && StringUtils.isBlank(paramRicSogg
						.getComuneNascita()))
						|| StringUtils.isNotBlank(paramRicSogg.getDenominazione()),
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO
						.getErrore("LA DENOMINAZIONE E' OBBLIGATORIA SE VIENE INDICATO IL SESSO O IL COMUNE DI NASCITA"));

		checkCondition(
				StringUtils.isBlank(paramRicSogg.getFormaGiuridica())
						|| StringUtils.isNotBlank(paramRicSogg.getDenominazione()),
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO
						.getErrore("LA DENOMINAZIONE E' OBBLIGATORIA SE HO INDICATO LA NATURA GIURIDICA"));

		checkCondition(
				StringUtils.isBlank(paramRicSogg.getDenominazione())
						|| paramRicSogg.getDenominazione().trim().replaceAll("%", "")
								.replaceAll(" ", "").length() >= 3,
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO
						.getErrore("LA DENOMINAZIONE DEVE ESSERE LUNGA ALMENO 3 CARATTERI (% ESCLUSO)"));

	}
}
