/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.soggetto;



import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siaccommonser.business.service.base.BaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeGestioneSoggetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeGestioneSoggettoResponse;
import it.csi.siac.siacfinser.integration.dad.CommonDad;
import it.csi.siac.siacfinser.integration.dad.SoggettoFinDad;
import it.csi.siac.siacfinser.model.codifiche.CodificaExtFin;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;



@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ListeGestioneSoggettoService extends BaseService<ListeGestioneSoggetto, ListeGestioneSoggettoResponse> {

	
	@Autowired
	SoggettoFinDad soggettoDad;
	
	@Autowired
	CommonDad commonDad;
	
	@Override
	protected void init() {
	}
	
	
//	@Override
//	@Transactional(readOnly=true)
//	public ListeGestioneSoggettoResponse executeService(ListeGestioneSoggetto serviceRequest) {
//		return super.executeService(serviceRequest);
//	}	
	
	@Override
	public void execute() {
		final String methodName="ListeGestioneSoggettoService-execute()";
		//1. Carico la lista delle nazioni:
		ArrayList<CodificaFin> nazioni = commonDad.findNazioni(req.getRichiedente().getAccount().getEnte());
		//2. Carico la lista delle nature giuridiche:
		ArrayList<CodificaExtFin> listaNaturaGiuridica = commonDad.findListaNaturaGiuridica(req.getRichiedente().getAccount().getEnte());
		//3. Carico la lista delle nature giuridiche soggetto:
		ArrayList<CodificaFin> listaNaturaGiuridicaSoggetto = commonDad.findListaNaturaGiuridicaSoggetto(req.getRichiedente().getAccount().getEnte());
		//4. Carico la lista dei soggetti classe:
		ArrayList<CodificaFin> soggettiClasse = commonDad.findSoggettiClasse(req.getRichiedente().getAccount().getEnte());
		//2. Costruisco la response di ritorno:
		res.setListaGiuridicaSoggetto(listaNaturaGiuridicaSoggetto);
		res.setListaNaturaGiuridica(listaNaturaGiuridica);
		res.setListaNazioni(nazioni);
		res.setListaClasseSoggetto(soggettiClasse);
	}
}
