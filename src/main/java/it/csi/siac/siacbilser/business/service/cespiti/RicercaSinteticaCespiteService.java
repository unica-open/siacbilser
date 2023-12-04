/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cespiti;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dad.CespiteDad;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaCespiteResponse;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siaccespser.model.CespiteModelDetail;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;


/**
 * 
 * @author Antonino
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSinteticaCespiteService extends CheckedAccountBaseService<RicercaSinteticaCespite, RicercaSinteticaCespiteResponse> {

	//DAD
	@Autowired
	private CespiteDad cespiteDad;

	private Cespite cespite;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		cespite = req.getCespite();
		checkNotNull(req.getParametriPaginazione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("parametri paginazione"));
		
		checkCondition(req.getNumeroInventarioDa() == null || req.getNumeroInventarioA() == null
				|| req.getNumeroInventarioDa().compareTo(req.getNumeroInventarioA()) <= 0,
				ErroreCore.VALORE_NON_CONSENTITO.getErrore("numero inventario da/a", "il numero da non puo' essere superiore al numero a"));
	}
	
	@Override
	protected void init() {
		super.init();
		cespiteDad.setEnte(ente);
		cespiteDad.setLoginOperazione(loginOperazione);
	}
	
	@Transactional
	@Override
	public RicercaSinteticaCespiteResponse executeService(RicercaSinteticaCespite serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		Utility.MDTL.addModelDetails(req.getModelDetails());
		Utility.AAACTL.set(req.getAnteprimaAmmortamentoAnnuoCespite());
		Utility.MDETTL.set(req.getMovimentoDettaglio());
		Date dataInizioValiditaFiltro = Utility.ultimoGiornoDellAnno(req.getAnnoBilancio());
		ListaPaginata<Cespite> listaCespite = cespiteDad.ricercaSinteticaCespite(cespite, 
				req.getTipoBeneCespite(), 
				req.getClassificazioneGiuridicaCespite(),
				req.getDismissioneCespite(),
				req.getCategoriaCespiti(),
				req.getDettaglioAnteprimaAmmortamentoAnnuoCespite(),
				req.getNumeroInventarioDa(), 
				req.getNumeroInventarioA(), 
				req.getEscludiCespitiCollegatiADismissione(),
				req.getConPianoAmmortamentoCompleto(),
				req.getMassimoAnnoAmmortato(),
				req.getMovimentoDettaglio(),
				Utility.MDTL.byModelDetailClass(CespiteModelDetail.class), 
				dataInizioValiditaFiltro, 
				req.getParametriPaginazione());
		res.setListaCespite(listaCespite);
	}
	
}