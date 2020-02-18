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
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dad.CategoriaCespitiDad;
import it.csi.siac.siaccespser.frontend.webservice.msg.VerificaAnnullabilitaCategoriaCespiti;
import it.csi.siac.siaccespser.frontend.webservice.msg.VerificaAnnullabilitaCategoriaCespitiResponse;
import it.csi.siac.siaccespser.model.CategoriaCespiti;
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
public class VerificaAnnullabilitaCategoriaCespitiService extends CheckedAccountBaseService<VerificaAnnullabilitaCategoriaCespiti, VerificaAnnullabilitaCategoriaCespitiResponse> {

	//DAD
	@Autowired
	private CategoriaCespitiDad categoriaCespitiDad;
	
	private CategoriaCespiti categoriaCespiti;

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getCategoriaCespiti(), "categoria cespiti");
		categoriaCespiti = req.getCategoriaCespiti();
	}
	
	@Override
	@Transactional(readOnly = true)
	public VerificaAnnullabilitaCategoriaCespitiResponse executeService(VerificaAnnullabilitaCategoriaCespiti serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		checkTipoBeneCollegati();
	}
	
	/**
	 * Controlla che non siano collegati dei tipi bene
	 */
	private void checkTipoBeneCollegati() {
		categoriaCespiti.setDataInizioValiditaFiltro(Utility.primoGiornoDellAnno(req.getAnnoBilancio()));
		Long numeroTipoBene = categoriaCespitiDad.contaTipoBeneByCategoria(req.getCategoriaCespiti());
		if(numeroTipoBene != null && numeroTipoBene.longValue() > 0) {
			Errore errore = ErroreCore.AGGIORNAMENTO_CON_CONFERMA_WARN.getErrore("La categoria cespiti", "collegata a tipi bene", "l'annullamento");
			res.getListaMessaggio().add(new Messaggio(errore.getCodice(), errore.getDescrizione()));
		}
	}
	
}
