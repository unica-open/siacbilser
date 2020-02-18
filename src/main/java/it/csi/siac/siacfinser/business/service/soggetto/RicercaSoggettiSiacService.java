/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.soggetto;

import java.util.Arrays;
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
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggetti;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettiResponse;
import it.csi.siac.siacfinser.integration.dad.SoggettoFinDad;
import it.csi.siac.siacfinser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSoggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSoggettiSiacService extends 
		AbstractBaseService<RicercaSoggetti, RicercaSoggettiResponse>
{

	@Autowired
	private SoggettoFinDad soggettoDad;

	@Override
	protected void init()
	{
		final String methodName = "RicercaSoggettiSiacService : init()";
		log.debug(methodName, "Begin");
	}

//	@Transactional(readOnly = true)
//	public RicercaSoggettiResponse executeService(RicercaSoggetti serviceRequest)
//	{
//		return super.executeService(serviceRequest);
//	}
	
	@Override
	public void execute()
	{
		final String methodName = "RicercaSoggettiSiacService - execute()";
		// 1. Vengono letti i valori ricevuti in input dalla request
		ParametroRicercaSoggetto paramRicSogg = req.getParametroRicercaSoggetto();
		Ente ente = req.getRichiedente().getAccount().getEnte();
		Integer idEnte = ente.getUid();

		String codiceAmbito = req.getCodiceAmbito();

		if (codiceAmbito == null)
			codiceAmbito = Constanti.AMBITO_FIN;

		// 2. Si invoca il metodo ricercaSoggetti che ci restituisce il numero
		// di risultati attesi dalla query composta
		// secondo i parametri di ricerca
		List<Soggetto> listaRisultati = soggettoDad.ricercaSoggetti(paramRicSogg, idEnte,
				codiceAmbito);

		// ...solo se il numero di risultati attesi e minore del numero massimo
		// accettabile si procede con il caricamento di tutti i dati:
		if (listaRisultati.size() <= Constanti.MAX_RIGHE_ESTRAIBILI)
		{
			// 3. si invoca il metodo che carica tutti i dati rispetto alla
			// query composta dall'input ricevuto:
			List<Soggetto> paginata = getPaginata(listaRisultati, req.getNumPagina(),
					req.getNumRisultatiPerPagina());
			// 4. Viene costruita la response per esito OK
			res.setEsito(Esito.SUCCESSO);
			res.setSoggetti(paginata);
			res.setNumRisultati(listaRisultati.size());
			res.setNumPagina(req.getNumPagina());
		}
		else
		{
			// Viene costruita la response per esito KO
			res.setErrori(Arrays.asList(ErroreFin.RICERCA_TROPPO_ESTESA.getErrore()));
			res.setEsito(Esito.FALLIMENTO);
			res.setSoggetti(null);
		}
	}

	@Override
	protected void checkServiceParam() throws ServiceParamError
	{
		final String methodName = "RicercaSoggettiSiacService : checkServiceParam()";
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
						|| StringUtils.isNotBlank(paramRicSogg.getStatoSoggetto())
						//SIAC-6565-CR1215
						
						|| StringUtils.isNotBlank(paramRicSogg.getCodiceSoggettoPrecedente())
						|| StringUtils.isNotBlank(paramRicSogg.getEmailPec()) 
						|| StringUtils.isNotBlank(paramRicSogg.getCodDestinatario())
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
