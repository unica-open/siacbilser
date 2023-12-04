/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.classificazionecespiti;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaTipoBeneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaTipoBeneCespiteResponse;
import it.csi.siac.siaccespser.model.TipoBeneCespite;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * 
 * @author Antonino
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaTipoBeneCespiteService extends BaseInserisciAggiornaTipoBeneCespiteService<AggiornaTipoBeneCespite, AggiornaTipoBeneCespiteResponse> {

	
	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		checkNotNull(req.getTipoBeneCespite(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("categoria cespiti"));		
		tipoBeneCespite = req.getTipoBeneCespite();
		checkNotBlank(tipoBeneCespite.getCodice(),"codice");
		checkNotBlank(tipoBeneCespite.getDescrizione(),"descrizione");
		checkEntita(tipoBeneCespite.getCategoriaCespiti(), "categoria");
	}
	
	@Override
	protected void execute() {
		
		caricaPrimoGiornoDellAnno();
		
		checkContiTipoBene();
		caricaEventiSePresenti();
		checkEventi();
		checkCategoria();
		caricaCausali();
		
		impostaValoriDiDefault();
		TipoBeneCespite tipoBeneCespiteAggiornato = tipoBeneCespiteDad.aggiornaTipoBeneCespite(tipoBeneCespite);
		
		res.setTipoBeneCespite(tipoBeneCespiteAggiornato);
	}
			
}
