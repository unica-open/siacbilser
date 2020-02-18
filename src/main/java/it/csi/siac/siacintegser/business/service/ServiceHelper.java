/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siacfinser.business.service.RicercaStrutturaAmministrativaService;
import it.csi.siac.siacfinser.business.service.RicercaTipoProvvedimentoService;
import it.csi.siac.siacfinser.business.service.soggetto.RicercaSoggettoPerChiaveService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiaveResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaStrutturaAmministrativa;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaStrutturaAmministrativaResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaTipoProvvedimento;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaTipoProvvedimentoResponse;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSoggettoK;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;


/**
 * Classe che esponde le operazioni di utility di lettura delle entita di modello del siac
 * @author 1513
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ServiceHelper {
	
	@Autowired
	protected ApplicationContext appCtx;

	
	public StrutturaAmministrativoContabile ricercaStrutturaByCodice(Ente ente, Richiedente richiedente, String codiceStruttura, String codiceTipoStruttura) {
		
		StrutturaAmministrativoContabile sac = null; 
		
		RicercaStrutturaAmministrativa reqSac = new RicercaStrutturaAmministrativa();
		reqSac.setEnte(ente);
		reqSac.setRichiedente(richiedente);
		reqSac.setCodice(codiceStruttura);
		reqSac.setTipoCodice(codiceTipoStruttura);
			
		RicercaStrutturaAmministrativaResponse resSac = appCtx.getBean(RicercaStrutturaAmministrativaService.class).executeService(
				reqSac);

		if(resSac!=null && resSac.getStrutturaAmministrativoContabile()!=null)
			sac =  resSac.getStrutturaAmministrativoContabile();
		
		
		return sac;
	}
	
	
	public StrutturaAmministrativoContabile ricercaStrutturaCdr(Ente ente, Richiedente richiedente, String codice, String codiceTipoStruttura) {
		
		StrutturaAmministrativoContabile sac = null; 
		
		RicercaStrutturaAmministrativa reqSac = new RicercaStrutturaAmministrativa();
		reqSac.setEnte(ente);
		reqSac.setRichiedente(richiedente);
		reqSac.setCodice(codice);
		reqSac.setTipoCodice(codiceTipoStruttura);
		reqSac.setRicercaCdr(true);	
		
		RicercaStrutturaAmministrativaResponse resSac = appCtx.getBean(RicercaStrutturaAmministrativaService.class).executeService(
				reqSac);

		if(resSac!=null && resSac.getStrutturaAmministrativoContabile()!=null)
			sac =  resSac.getStrutturaAmministrativoContabile();
				
		return sac;
	}
	
	
	public TipoAtto ricercaTipoProvvedimentoByCodice(Ente ente, Richiedente richiedente, String codiceTipoProvvedimento) {
		
		TipoAtto tipoAtto = null;
		RicercaTipoProvvedimento req = new RicercaTipoProvvedimento();
		req.setEnte(ente);
		req.setRichiedente(richiedente);
		req.setCodice(codiceTipoProvvedimento);
		RicercaTipoProvvedimentoResponse res = appCtx.getBean(RicercaTipoProvvedimentoService.class).executeService(
				req);
		
		if(res!=null && res.getTipoAtto()!=null) tipoAtto = res.getTipoAtto();
		
		return tipoAtto;
	}
	
	
	public Soggetto ricercaSoggetto(String codiceSoggetto, Richiedente richiedente) {

		Soggetto soggetto = null;
		
		RicercaSoggettoPerChiave reqSoggetto = new RicercaSoggettoPerChiave();
		ParametroRicercaSoggettoK param = new ParametroRicercaSoggettoK();
		param.setCodice(codiceSoggetto);
		reqSoggetto.setParametroSoggettoK(param);
		reqSoggetto.setRichiedente(richiedente);
		RicercaSoggettoPerChiaveResponse resSoggetto = appCtx.getBean(RicercaSoggettoPerChiaveService.class).executeService(
				reqSoggetto);

		if (resSoggetto != null && resSoggetto.getSoggetto() != null) {
			soggetto = resSoggetto.getSoggetto();
		}

		return soggetto;
	}
}
