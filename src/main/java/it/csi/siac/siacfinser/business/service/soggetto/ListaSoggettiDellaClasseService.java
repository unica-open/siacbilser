/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.soggetto;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaSoggettiDellaClasse;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaSoggettiDellaClasseResponse;
import it.csi.siac.siacfinser.integration.dad.SoggettoFinDad;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ListaSoggettiDellaClasseService extends AbstractBaseService<ListaSoggettiDellaClasse, ListaSoggettiDellaClasseResponse>
{

	@Autowired
	SoggettoFinDad soggettoDad;

	@Override
	@Transactional
	public ListaSoggettiDellaClasseResponse executeService(ListaSoggettiDellaClasse serviceRequest)
	{
		return super.executeService(serviceRequest);
	}

	@Override
	public void execute(){
		
		Ente ente = req.getRichiedente().getAccount().getEnte();

		String codiceAmbito = req.getCodificaAmbito();
		
		DatiOperazioneDto datiOperazione = commonDad.inizializzaDatiOperazione(ente, req.getRichiedente(), Operazione.RICERCA, codiceAmbito, null);
		
		List<CodificaFin> listaSoggetti = soggettoDad.listaSoggettiDellaClasse(datiOperazione, req.getCodiceClasse());
		
		res.setListaSoggetti(listaSoggetti);
		
	}

	@Override
	protected void checkServiceParam() throws ServiceParamError{
		checkNotNull(req.getRichiedente(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("richiedente"));
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		if(StringUtils.isEmpty(req.getCodiceClasse())){
			checkNotNull(null, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("codice classe"));
		}
		if(StringUtils.isEmpty(req.getCodificaAmbito())){
			checkNotNull(null, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("codifica ambito"));
		}
	}
}
