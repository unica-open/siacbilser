/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.classificazionecespiti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.CespiteDad;
import it.csi.siac.siaccespser.frontend.webservice.msg.VerificaAnnullabilitaTipoBeneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.VerificaAnnullabilitaTipoBeneCespiteResponse;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Messaggio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * 
 * @author Marchino Alessandro
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class VerificaAnnullabilitaTipoBeneCespiteService extends CheckedAccountBaseService<VerificaAnnullabilitaTipoBeneCespite, VerificaAnnullabilitaTipoBeneCespiteResponse> {

	//DAD
	@Autowired
	private CespiteDad cespiteDad;

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getTipoBeneCespite(), "tipo bene cespite");
	}
	
	@Override
	@Transactional(readOnly = true)
	public VerificaAnnullabilitaTipoBeneCespiteResponse executeService(VerificaAnnullabilitaTipoBeneCespite serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		checkCespitiCollegati();
	}
	
	/**
	 * Controlla che non siano collegati dei cespiti
	 */
	private void checkCespitiCollegati() {
		Long numeroCespiti = cespiteDad.countCespitiByTipoBene(req.getTipoBeneCespite());
		if(numeroCespiti != null && numeroCespiti.longValue() > 0) {
			Errore errore = ErroreCore.AGGIORNAMENTO_CON_CONFERMA_WARN.getErrore("Il tipo bene", "collegato a cespiti", "l'annullamento");
			res.getListaMessaggio().add(new Messaggio(errore.getCodice(), errore.getDescrizione()));
		}
	}
	
}
