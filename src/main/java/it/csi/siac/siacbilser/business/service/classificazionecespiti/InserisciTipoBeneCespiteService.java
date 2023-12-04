/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.classificazionecespiti;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciTipoBeneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciTipoBeneCespiteResponse;
import it.csi.siac.siaccespser.model.TipoBeneCespite;
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
public class InserisciTipoBeneCespiteService extends BaseInserisciAggiornaTipoBeneCespiteService<InserisciTipoBeneCespite, InserisciTipoBeneCespiteResponse> {

	@Override
	protected void checkServiceParam() throws ServiceParamError {		

		checkNotNull(req.getTipoBeneCespite(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("categoria cespiti"));		
		tipoBeneCespite = req.getTipoBeneCespite();
		checkNotBlank(tipoBeneCespite.getCodice(),"codice");
		checkNotBlank(tipoBeneCespite.getDescrizione(),"descrizione");
		checkEntita(tipoBeneCespite.getCategoriaCespiti(), "categoria");
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional
	@Override
	public InserisciTipoBeneCespiteResponse executeService(InserisciTipoBeneCespite serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		
		caricaPrimoGiornoDellAnno();
		
		checkCodiceTipoBene();
		checkContiTipoBene();
		caricaEventiSePresenti();
		checkEventi();
		caricaCausali();
		checkCategoria();
		
		impostaValoriDiDefault();		
		TipoBeneCespite tipoBeneCespiteInserito = tipoBeneCespiteDad.inserisciTipoBeneCespite(tipoBeneCespite);
		
		res.setTipoBeneCespite(tipoBeneCespiteInserito);
	}


	private void checkCodiceTipoBene() {
		TipoBeneCespite tipoBeneByCodice = tipoBeneCespiteDad.findByCodice(tipoBeneCespite.getCodice());
		if(tipoBeneByCodice != null) {
			throw new BusinessException(ErroreCore.ENTITA_PRESENTE.getErrore("inserimento tipo bene", "tipo bene con codice " + tipoBeneCespite.getCodice()));
		}
	}
	
}