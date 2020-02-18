/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.soggetto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaComuni;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaComuniResponse;
import it.csi.siac.siacfinser.integration.dad.CommonDad;
import it.csi.siac.siacfinser.model.soggetto.ComuneNascita;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CercaComuniService extends AbstractBaseService<ListaComuni, ListaComuniResponse>
{

	@Autowired
	CommonDad commonDad;

	@Override
	protected void init(){
		commonDad.setEnte(req.getRichiedente().getAccount().getEnte());
	}
	
	
	@Override
	@Transactional(readOnly=true)
	public ListaComuniResponse executeService(ListaComuni serviceRequest) {
		return super.executeService(serviceRequest);
	}	

	@Override
	public void execute()
	{
		String methodName = "CercaComuniService-execute()";
		// 1. Chiamo il metodo che effettua il caricamento della lista dei
		// comuni:
		List<ComuneNascita> listaComuni = commonDad.findListaComuni(req.getDescrizioneComune(), req.getIdStato(),
				 req.getCodiceCatastale(), req.getRichiedente().getAccount().getEnte().getUid());
		// 2.Imposto il risultato nella response da ritornare in output al
		// servizio:
		res.setListaComuni(listaComuni);
		log.debug(methodName, "LISTA COMUNI--> " + res.getListaComuni());
	}
}
