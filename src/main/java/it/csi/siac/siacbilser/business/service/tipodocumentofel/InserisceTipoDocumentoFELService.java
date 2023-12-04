/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.tipodocumentofel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceTipoDocumentoFEL;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceTipoDocumentoFELResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoDocumentoFEL;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoDocumentoFELResponse;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceTipoDocumentoFELService extends BaseTipoDocumentoFELService<InserisceTipoDocumentoFEL, InserisceTipoDocumentoFELResponse>{

	
	/** The ricerca puntuale capitolo entrata previsione service. */
	@Autowired
	private RicercaPuntualeTipoDocumentoFELService ricercaPuntualeTipoDocumentoFELService = null;
	
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
	 
		checkNotNull(req.getTipoDocumentoFEL().getCodice(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("codice"));
		checkNotNull(req.getTipoDocumentoFEL().getDescrizione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("descrizione"));
	}
	
	@Override
	@Transactional
	public InserisceTipoDocumentoFELResponse executeService(InserisceTipoDocumentoFEL serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		
//		if (esisteTipoDocumentoFel()) {
//			//Se l'entità esiste viene ritornato codice di errore ENTITA_PRESENTE
//			
//			TipoDocFEL tipodoc = req.getTipoDocumentoFEL();			
//			
//			res.addErrore(ErroreCore.ENTITA_PRESENTE.getErrore("inserimento",String.format("Codice: %s", 
//					tipodoc.getCodice()				
//					)));
//			res.setEsito(Esito.FALLIMENTO);
//			return;
//			
//		}
		
		
		res.setTipoDocumentoFEL(tipoDocumentoFELDad.inserisceTipoDocumentoFEL(req.getTipoDocumentoFEL()));
	}
	
	
	
	/**
	 * Esiste capitolo entrata previsione.
	 *
	 * @return true se esiste
	 */
	private boolean esisteTipoDocumentoFel() {
		
		
		RicercaTipoDocumentoFEL criteriRic = new RicercaTipoDocumentoFEL();
		
		criteriRic.setTipoDocFEL(req.getTipoDocumentoFEL());
		 
		
		RicercaTipoDocumentoFELResponse resRic = executeExternalService(ricercaPuntualeTipoDocumentoFELService,criteriRic);	

		return resRic.getTipoDocumentiFEL() != null;
	}
	
}
