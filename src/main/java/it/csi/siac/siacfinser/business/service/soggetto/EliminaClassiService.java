/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.soggetto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaClasse;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaClasseResponse;
import it.csi.siac.siacfinser.integration.dad.SoggettoFinDad;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EliminaClassiService extends AbstractBaseService<AnnullaClasse, AnnullaClasseResponse>
{
	@Autowired
	private ApplicationContext appCtx;

	@Autowired
	private SoggettoFinDad	soggettoFinDad;
	
	
	@Override
	@Transactional
	public AnnullaClasseResponse executeService(AnnullaClasse serviceRequest) {
		return super.executeService(serviceRequest);
	}	
	
	@Override
	protected void execute()
	{
		List<Errore> errori = new ArrayList<Errore>(); 
		soggettoFinDad.annullaClasseSoggetto(req.getClasseSoggetto(), req.getEnte(), req.getRichiedente(), errori);
		
		this.res.addErrori(errori);
		this.res.setEsito(errori.size() > 0 ? Esito.FALLIMENTO : Esito.SUCCESSO);
	}
}
