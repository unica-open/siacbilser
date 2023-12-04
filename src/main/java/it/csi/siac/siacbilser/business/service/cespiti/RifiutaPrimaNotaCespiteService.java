/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cespiti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.Inventario;
import it.csi.siac.siacbilser.integration.dad.PrimaNotaInvDad;
import it.csi.siac.siaccespser.frontend.webservice.msg.RifiutaPrimaNotaCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RifiutaPrimaNotaCespiteResponse;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.model.MovimentoDettaglioModelDetail;
import it.csi.siac.siacgenser.model.MovimentoEPModelDetail;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.PrimaNotaModelDetail;
import it.csi.siac.siacgenser.model.StatoAccettazionePrimaNotaProvvisoria;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;

/**
 * Inserisce e completa gli Elenchi di Documenti Spesa, Entrata, IVA Spesa, Iva
 * Entrata, comprensivi di AllegatoAtto.
 * 
 * @author Antonino
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RifiutaPrimaNotaCespiteService extends CheckedAccountBaseService<RifiutaPrimaNotaCespite, RifiutaPrimaNotaCespiteResponse> {

	private PrimaNota primaNotaINV;

	@Autowired
	@Inventario
	private PrimaNotaInvDad primaNotaInvDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		 checkEntita(req.getPrimaNota(), "prima nota");
	}

	@Override
	protected void init() {
		super.init();
		primaNotaInvDad.setEnte(ente);
		primaNotaInvDad.setLoginOperazione(loginOperazione);
	}

	@Transactional
	@Override
	public RifiutaPrimaNotaCespiteResponse executeService(RifiutaPrimaNotaCespite serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		checkPrimaNota();
		aggiornaStatoPrimaNota();
	}
	
	private void aggiornaStatoPrimaNota() {
		primaNotaInvDad.aggiornaStatoOperativiPrimaNota(this.primaNotaINV, StatoOperativoPrimaNota.ANNULLATO, StatoAccettazionePrimaNotaProvvisoria.RIFIUTATO);
	}

	/**
	 * Carica prima nota ambito di ambito INV che deve essere validata.
	 */
	private void checkPrimaNota() {
		final String methodName ="checkPrimaNota";
		
		Utility.MDTL.addModelDetails(MovimentoEPModelDetail.MovimentoDettaglioModelDetail ,MovimentoDettaglioModelDetail.ContoModelDetail, MovimentoDettaglioModelDetail.Segno);
		log.debug(methodName, "carico da db la prima nota con uid: " + req.getPrimaNota().getUid());
		this.primaNotaINV = primaNotaInvDad.findPrimaNotaByUid(req.getPrimaNota().getUid(), new PrimaNotaModelDetail[] {PrimaNotaModelDetail.MovimentiEpModelDetail});
		if(this.primaNotaINV == null ) {
			throw new BusinessException(ErroreCore.ANNULLAMENTO_NON_POSSIBILE.getErrore("Prima nota con uid: " + req.getPrimaNota().getUid() + " non presente."));
		}
	}

}
