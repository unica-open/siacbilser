/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.helper;

import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimentoResponse;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaPuntualeProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaPuntualeProvvedimentoResponse;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacattser.model.ric.RicercaAtti;
import it.csi.siac.siacbilser.business.service.provvedimento.RicercaProvvedimentoService;
import it.csi.siac.siacbilser.business.service.provvedimento.RicercaPuntualeProvvedimentoService;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siacfinser.business.service.RicercaTipoProvvedimentoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaTipoProvvedimento;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaTipoProvvedimentoResponse;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ProvvedimentoServiceHelper extends IntegServiceHelper {

	public List<AttoAmministrativo> findAttiAmministrativi(Ente ente, Richiedente richiedente, Integer anno, 
			Integer numero, TipoAtto tipoAtto, StrutturaAmministrativoContabile strutturaAmministrativoContabile) {
		
		RicercaProvvedimento req = new RicercaProvvedimento();
		
		req.setEnte(ente);
		req.setRichiedente(richiedente);
		req.setRicercaAtti(new RicercaAtti(anno, numero, tipoAtto, strutturaAmministrativoContabile));
		
		RicercaProvvedimentoResponse res = appCtx.getBean(RicercaProvvedimentoService.class).executeService(req);
		
		checkServiceResponse(res);
		
		return res.getListaAttiAmministrativi();
	}

	public AttoAmministrativo findAttoAmministrativo(Ente ente, Richiedente richiedente, Integer anno, 
			Integer numero, TipoAtto tipoAtto, StrutturaAmministrativoContabile strutturaAmministrativoContabile) {
		RicercaPuntualeProvvedimento req = new RicercaPuntualeProvvedimento();
		
		req.setEnte(ente);
		req.setRichiedente(richiedente);
		req.setRicercaAtti(new RicercaAtti(anno, numero, tipoAtto, strutturaAmministrativoContabile));
		
		RicercaPuntualeProvvedimentoResponse res = appCtx.getBean(RicercaPuntualeProvvedimentoService.class).executeService(req);
		
		checkServiceResponse(res);
		
		return res.getAttoAmministrativo();
	}

	public AttoAmministrativo findAttoAmministrativo(Ente ente, Richiedente richiedente, Integer uid) {
		RicercaPuntualeProvvedimento req = new RicercaPuntualeProvvedimento();
		
		req.setEnte(ente);
		req.setRichiedente(richiedente);
		req.setRicercaAtti(new RicercaAtti(uid));
		
		RicercaPuntualeProvvedimentoResponse res = appCtx.getBean(RicercaPuntualeProvvedimentoService.class).executeService(req);
		
		checkServiceResponse(res);
		
		return res.getAttoAmministrativo();
	}

	public TipoAtto findTipoAttoByCodice(Ente ente, Richiedente richiedente, String codice) {

		RicercaTipoProvvedimento req = new RicercaTipoProvvedimento();

		req.setEnte(ente);
		req.setRichiedente(richiedente);
		req.setCodice(codice);

		RicercaTipoProvvedimentoResponse res = appCtx.getBean(RicercaTipoProvvedimentoService.class)
				.executeService(req);

		checkServiceResponse(res);

		return res.getTipoAtto();
	}
}
