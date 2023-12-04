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
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.VerificaIban;
import it.csi.siac.siacfinser.frontend.webservice.msg.VerificaIbanResponse;
import it.csi.siac.siacfinser.integration.dad.SoggettoFinDad;
import it.csi.siac.siacfinser.integration.entity.SiacTSepaFin;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class VerificaIbanService extends AbstractBaseService<VerificaIban, VerificaIbanResponse>
{

	@Autowired
	SoggettoFinDad soggettoDad;

//	@Override
//	@Transactional
//	public VerificaIbanResponse executeService(VerificaIban serviceRequest)	{		
//		return super.executeService(serviceRequest);
//	}

	@Override
	public void execute()
	{
		SiacTSepaFin infoSepa = soggettoDad.getInfoSepa(req.getIban().substring(0, 2), req.getEnte().getUid());

		if (infoSepa != null)
		{
			res.setSepa(true);

			Integer lunghezzaIbanValida = infoSepa.getLunghezzaIban();

			if (req.getIban().length() != lunghezzaIbanValida)
			{
				res.addErrore(ErroreCore.VALORE_NON_CONSENTITO.getErrore("IBAN", String.format(
						"(la lunghezza deve essere di %s caratteri)", String.valueOf(lunghezzaIbanValida))));

				res.setEsito(Esito.FALLIMENTO);
			}
		}
		else
			res.setSepa(false);
	}

	@Override
	protected void checkServiceParam() throws ServiceParamError
	{

		checkNotNull(req.getRichiedente(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("richiedente"));
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		checkNotNull(req.getIban(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("iban"));

	}
}
