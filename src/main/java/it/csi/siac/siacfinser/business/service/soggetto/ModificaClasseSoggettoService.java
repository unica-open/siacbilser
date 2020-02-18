/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.soggetto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.ModificaClasse;
import it.csi.siac.siacfinser.frontend.webservice.msg.ModificaClasseResponse;
import it.csi.siac.siacfinser.integration.dad.SoggettoFinDad;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ModificaClasseSoggettoService extends AbstractBaseService<ModificaClasse, ModificaClasseResponse>
{

	@Autowired
	SoggettoFinDad soggettoDad;

	@Override
	@Transactional
	public ModificaClasseResponse executeService(ModificaClasse serviceRequest)
	{
		return super.executeService(serviceRequest);
	}

	@Override
	public void execute()
	{
		
		// costruisce la risposta
		this.res = new ModificaClasseResponse();
		try {
			// FIXME verificare che l'ente indicato nella richiesta sia quello del richiedente ?
			List<Errore>	errori = new ArrayList<Errore>();
			soggettoDad.modificaClasseSoggetto(req.getClasseSoggetto(), req.getEnte(), req.getRichiedente(), errori);
			this.res.addErrori(errori);
			this.res.setEsito(errori.size() > 0 ? Esito.FALLIMENTO : Esito.SUCCESSO);
		} catch (Exception e) {
			this.res.addErroreDiSistema(e);
			this.res.setEsito(Esito.FALLIMENTO);
		}
	}

	@Override
	protected void checkServiceParam() throws ServiceParamError
	{

		checkNotNull(req.getRichiedente(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("richiedente"));
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
//		checkNotNull(req.getIban(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("iban"));

	}
}
