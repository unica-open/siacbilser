/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cespiti;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.CespiteDad;
import it.csi.siac.siacbilser.integration.dad.DismissioneCespiteDad;
import it.csi.siac.siaccespser.frontend.webservice.msg.EliminaDismissioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.EliminaDismissioneCespiteResponse;
import it.csi.siac.siaccespser.model.DismissioneCespite;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.PrimaNotaModelDetail;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;

/**
 * 
 * @author Antonino
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EliminaDismissioneCespiteService extends CheckedAccountBaseService<EliminaDismissioneCespite, EliminaDismissioneCespiteResponse> {

	//DAD
	@Autowired
	private DismissioneCespiteDad dismissioneCespiteDad;
	
	@Autowired
	private CespiteDad cespiteDad;

	private DismissioneCespite dismissioneCespite;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		checkEntita(req.getDismissioneCespite(), "cespite");		
		dismissioneCespite = req.getDismissioneCespite();
	}
	
	@Override
	protected void init() {
		super.init();
		dismissioneCespiteDad.setEnte(ente);
		dismissioneCespiteDad.setLoginOperazione(loginOperazione);
		cespiteDad.setEnte(ente);
		cespiteDad.setLoginOperazione(loginOperazione);	
	}
	
	@Transactional
	@Override
	public EliminaDismissioneCespiteResponse executeService(EliminaDismissioneCespite serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		checkPrimeNoteDefinitiveCollegate();
		annullaPrimeNoteProvvisorieCollegate();
		
		cespiteDad.scollegaTuttiICespitiByDismissione(dismissioneCespite);
		
		dismissioneCespiteDad.eliminaDismissione(dismissioneCespite);
		
		res.setDismissioneCespite(dismissioneCespite);
	}

	/**
	 * 
	 */
	private void annullaPrimeNoteProvvisorieCollegate() {
		List<PrimaNota> primeNoteProvvisorieCollegate = dismissioneCespiteDad.caricaPrimeNoteCollegateADismissione(dismissioneCespite,StatoOperativoPrimaNota.PROVVISORIO,new PrimaNotaModelDetail[] {});
		if (primeNoteProvvisorieCollegate != null) {
			dismissioneCespiteDad.annullaPrimeNoteCollegateADismissione(primeNoteProvvisorieCollegate);		
		}
	}

	/**
	 * 
	 */
	private void checkPrimeNoteDefinitiveCollegate() {
		List<PrimaNota> primeNoteDefinitiveCollegate = dismissioneCespiteDad.caricaPrimeNoteCollegateADismissione(dismissioneCespite,StatoOperativoPrimaNota.DEFINITIVO,new PrimaNotaModelDetail[] {});
		if(primeNoteDefinitiveCollegate != null && !primeNoteDefinitiveCollegate.isEmpty()) {
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("la dismissione presenta delle prime note in stato definitivo."));
		}
	}

		



}