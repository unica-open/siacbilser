/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.cespiti.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import it.csi.siac.siacbilser.business.service.base.ServiceCallGroup;
import it.csi.siac.siacbilser.business.service.base.ServiceExecutor;
import it.csi.siac.siacbilser.business.service.base.responsehandler.NotNullResponseHandler;
import it.csi.siac.siacbilser.business.service.cespiti.InserisciCespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.RicercaCespitePerChiaveService;
import it.csi.siac.siacbilser.business.service.classificazionecespiti.RicercaSinteticaTipoBeneCespiteService;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaCespitePerChiave;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaCespitePerChiaveResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaTipoBeneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaTipoBeneCespiteResponse;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siaccespser.model.TipoBeneCespite;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

public class CespiteServiceCallGroup extends ServiceCallGroup 
{
	private List<Errore> errori = new ArrayList<Errore>();
	
	public CespiteServiceCallGroup(ServiceExecutor serviceExecutor, Richiedente richiedente) {
		super(serviceExecutor, richiedente);
	}
	
	public void inserisciCespite(Cespite cespite, boolean preserveValoreAttuale) {
		errori = new ArrayList<Errore>();
		
		TipoBeneCespite tipoBeneCespite = readTipoBeneCespite(cespite.getTipoBeneCespite());
		
		if (tipoBeneCespite == null) {
			return;
		}

//		if (! checkNumeroInventarioCespite(cespite)) {
//			return;
//		}
//		
		cespite.setTipoBeneCespite(tipoBeneCespite);
		
		Cespite cespiteInserito = saveCespite(cespite, preserveValoreAttuale);
		
		if (cespiteInserito == null) {
			return;
		}
	}
	
	private TipoBeneCespite readTipoBeneCespite(TipoBeneCespite tipoBeneCespite) {
		
		RicercaSinteticaTipoBeneCespite request = new RicercaSinteticaTipoBeneCespite();
		
		request.setTipoBeneCespite(tipoBeneCespite);
		request.setParametriPaginazione(ParametriPaginazione.TUTTI_GLI_ELEMENTI);
		request.setAnnoBilancio(Calendar.getInstance().get(Calendar.YEAR));
		request.setRichiedente(richiedente); 
		
		RicercaSinteticaTipoBeneCespiteResponse response = ricercaSinteticaTipoBeneCespite(request);
		
		if (response.isFallimento()) {
			addErrori(response);
			return null;
		}
		
		if (response.getListaTipoBeneCespite().isEmpty()) {
			errori.add(ErroreCore.ENTITA_NON_TROVATA.getErrore(
					"tipo bene cespite", 
					String.format("il codice %s", tipoBeneCespite.getCodice()))
			);
			
			return null;
		}
		
		return response.getListaTipoBeneCespite().get(0);
	}

	private RicercaSinteticaTipoBeneCespiteResponse ricercaSinteticaTipoBeneCespite(RicercaSinteticaTipoBeneCespite request) {
		return se.executeServiceSuccessTxRequiresNew(RicercaSinteticaTipoBeneCespiteService.class, request);
	}
	
	
	private boolean checkNumeroInventarioCespite(Cespite cespite) {
		
		RicercaCespitePerChiave request = new RicercaCespitePerChiave();
		
		request.setCespite(cespite);
		request.setRichiedente(richiedente);
		
		RicercaCespitePerChiaveResponse response = ricercaCespitePerChiave(request);
		
		if (response.verificatoErrore(ErroreCore.ENTITA_NON_TROVATA)) {
			return true;
		}
		
		if (response.isFallimento()) {
			addErrori(response);
			return false;
		}
		
		errori.add(ErroreCore.ENTITA_PRESENTE.getErrore(
				"inserimento cespite", 
				String.format("numero inventario %s", cespite.getNumeroInventario()))
		);
		
		return false;
	}
	
	private RicercaCespitePerChiaveResponse ricercaCespitePerChiave(RicercaCespitePerChiave request) {
		return se.executeServiceSuccessTxRequiresNew(RicercaCespitePerChiaveService.class, request, ErroreCore.ENTITA_NON_TROVATA.getCodice());
	}

	private Cespite saveCespite(Cespite cespite, boolean preserveValoreAttuale) {
		
		InserisciCespite request = new InserisciCespite();
		
		request.setCespite(cespite);
		request.setRichiedente(richiedente);
		request.setPreserveValoreAttuale(preserveValoreAttuale);
		
		InserisciCespiteResponse response = se.executeServiceTxRequiresNew(
												InserisciCespiteService.class, 
												request, 
												new NotNullResponseHandler<InserisciCespiteResponse>());
		
		if (response.isFallimento()) {
			addErrori(response);
		}
		
		return response.getCespite();
	}
	
	private void addErrori(ServiceResponse response) {
		errori.addAll(response.getErrori());
	}


	public List<Errore> getErrori() {
		return errori;
	}

}
