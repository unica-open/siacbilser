/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.registrocomunicazionipcc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.RegistroComunicazioniPCCDad;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaRegistroComunicazioniPCC;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaRegistroComunicazioniPCCResponse;
import it.csi.siac.siacfin2ser.model.RegistroComunicazioniPCC;

/**
 * The Class AggiornaRegistroComunicazioniPCCService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaRegistroComunicazioniPCCService extends CheckedAccountBaseService<AggiornaRegistroComunicazioniPCC, AggiornaRegistroComunicazioniPCCResponse> {
	
//	private final LogSrvUtil log = new LogSrvUtil(getClass());
	
	@Autowired
	private RegistroComunicazioniPCCDad registroComunicazioniPCCDad;
	
	private RegistroComunicazioniPCC registroComunicazioniPCC;
	private RegistroComunicazioniPCC registroComunicazioniPCCOld;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getRegistroComunicazioniPCC(), "registro comunicazioni pcc");
		
		registroComunicazioniPCC = req.getRegistroComunicazioniPCC();
		checkEntita(registroComunicazioniPCC.getEnte(), "ente registro comunicazioni pcc");
		checkEntita(registroComunicazioniPCC.getDocumentoSpesa(), "documento registro comunicazioni pcc");
		checkEntita(registroComunicazioniPCC.getSubdocumentoSpesa(), "subdocumento registro comunicazioni pcc");
		checkEntita(registroComunicazioniPCC.getTipoOperazionePCC(), "tipo operazione registro comunicazioni pcc");
	}
	
	@Override
	@Transactional
	public AggiornaRegistroComunicazioniPCCResponse executeService(AggiornaRegistroComunicazioniPCC serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void init() {
		registroComunicazioniPCCDad.setEnte(ente);
		registroComunicazioniPCCDad.setLoginOperazione(loginOperazione);
	}

	@Override
	protected void execute() {
		checkRegistroComunicazioniPCCExistance();
		// TODO: implementare tali controlli?
//		checkSubdocumentoNotChanged();
//		checkDocumentoNotChanged();
		
		registroComunicazioniPCCDad.aggiornaRegistroComunicazioniPCC(registroComunicazioniPCC);
		res.setRegistroComunicazioniPCC(registroComunicazioniPCC);
	}

	private void checkRegistroComunicazioniPCCExistance() {
		registroComunicazioniPCCOld = registroComunicazioniPCCDad.findByUid(registroComunicazioniPCC.getUid());
		if(registroComunicazioniPCCOld == null) {
			// Non ho trovato il registro sul database: non posso aggiornare
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Registro comunicazioni PCC", "uid " + registroComunicazioniPCC.getUid()));
		}
	}

//	/**
//	 * Controlla che il subdocumento della registrazione PCC non sia stato variato
//	 */
//	private void checkSubdocumentoNotChanged() {
//		final String methodName = "checkSubdocumentoNotChanged";
//		log.debug(methodName, "Old subdocumento uid " + (registroComunicazioniPCCOld.getSubdocumentoSpesa() != null ? registroComunicazioniPCCOld.getSubdocumentoSpesa().getUid() : "null") 
//			+ " --- New subdocumento uid " + registroComunicazioniPCC.getSubdocumentoSpesa().getUid());
//		if(registroComunicazioniPCCOld.getSubdocumentoSpesa() == null || registroComunicazioniPCCOld.getSubdocumentoSpesa().getUid() != registroComunicazioniPCC.getSubdocumentoSpesa().getUid()) {
//			throw new BusinessException(ErroreCore.VALORE_NON_CONSENTITO.getErrore("subdocumento", "non e' possibile cambiare il subdocumento collegato al registro"));
//		}
//	}
//	
//	/**
//	 * Controlla che il subdocumento della registrazione PCC non sia stato variato
//	 */
//	private void checkDocumentoNotChanged() {
//		final String methodName = "checkDocumentoNotChanged";
//		log.debug(methodName, "Old Documento uid " + (registroComunicazioniPCCOld.getDocumentoSpesa() != null ? registroComunicazioniPCCOld.getDocumentoSpesa().getUid() : "null") 
//			+ " --- New documento uid " + registroComunicazioniPCC.getDocumentoSpesa().getUid());
//		if(registroComunicazioniPCCOld.getDocumentoSpesa() == null || registroComunicazioniPCCOld.getDocumentoSpesa().getUid() != registroComunicazioniPCC.getDocumentoSpesa().getUid()) {
//			throw new BusinessException(ErroreCore.VALORE_NON_CONSENTITO.getErrore("documento", "non e' possibile cambiare il documento collegato al registro"));
//		}
//	}

}
