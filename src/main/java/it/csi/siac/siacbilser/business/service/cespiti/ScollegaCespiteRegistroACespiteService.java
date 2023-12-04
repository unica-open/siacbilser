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
import it.csi.siac.siacbilser.integration.dad.CespiteDad;
import it.csi.siac.siaccespser.frontend.webservice.msg.ScollegaCespiteRegistroACespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.ScollegaCespiteRegistroACespiteResponse;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siaccespser.model.CespiteModelDetail;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * 
 * @author Antonino
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ScollegaCespiteRegistroACespiteService extends CheckedAccountBaseService<ScollegaCespiteRegistroACespite, ScollegaCespiteRegistroACespiteResponse> {

	private Cespite cespite;
	//DAD
	@Autowired
	CespiteDad cespiteDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		checkEntita(req.getCespite(), "cespite");
		checkEntita(req.getMovimentoDettaglio(), "movimento dettaglio");
	}
	
	@Override
	protected void init() {
		super.init();
		cespiteDad.setEnte(ente);
		cespiteDad.setLoginOperazione(loginOperazione);

	}
	
	@Transactional
	@Override
	public ScollegaCespiteRegistroACespiteResponse executeService(ScollegaCespiteRegistroACespite serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		caricaCespite();
		checkCespite();
		
		cespiteDad.scollegaCespitiDaPrimaNota(this.cespite, req.getMovimentoDettaglio());
		
		eliminaCespiteSeContestuale();
	}

	private void eliminaCespiteSeContestuale() {
		final String methodName="eliminaCespiteSeContestuale";
		if(Boolean.TRUE.equals(this.cespite.getInserimentoContestualeRegistroA())) {
			log.debug(methodName, "Il cespite scollegato risulta essere stato inserito tramite la prima nota: lo elimino.");
			cespiteDad.eliminaCespite(this.cespite);
		}
	}

	private void checkCespite() {
		if(this.cespite == null || this.cespite.getUid() == 0) {
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("scollegamento", "cespite"));
		}
	}
	
	/**
	 * Carica cespite.
	 */
	private void caricaCespite() {
		this.cespite = cespiteDad.findCespiteById(req.getCespite(), new CespiteModelDetail[] { CespiteModelDetail.IsInserimentoContestualeRegistroA});
	}

}