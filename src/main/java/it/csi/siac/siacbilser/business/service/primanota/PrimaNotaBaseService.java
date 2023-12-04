/**
 * SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
 * SPDX-License-Identifier: EUPL-1.2
 */
package it.csi.siac.siacbilser.business.service.primanota;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.ClassificatoriDad;
import it.csi.siac.siacbilser.integration.dad.PrimaNotaDad;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.frontend.webservice.msg.BasePrimaNotaRequest;
import it.csi.siac.siacgenser.frontend.webservice.msg.BasePrimaNotaResponse;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.TipoCausale;
import it.csi.siac.siacintegser.model.enumeration.TipoClassificatore;

public abstract class PrimaNotaBaseService<REQ extends BasePrimaNotaRequest, RES extends BasePrimaNotaResponse> extends CheckedAccountBaseService<REQ, RES> {
	
	protected PrimaNota primaNota;
	
	@Autowired
	protected PrimaNotaDad primaNotaDad;
	@Autowired
	private ClassificatoriDad classificatoriDad;
	
	protected boolean isPrimaNotaIntegrata() {
		return TipoCausale.Integrata.equals(primaNota.getTipoCausale());
	}
	
	//SIAC-8323
	protected boolean isPrimaNotaGSA() {
		return primaNota.getAmbito() != null && Ambito.AMBITO_GSA.equals(primaNota.getAmbito());
	}
	
	/**
	 * SIAC-8134
	 * Popola la struttura amministrativa contabile competente
	 */
	protected void popolaStrutturaCompetente() { 
		String methodName = "popolaStrutturaCompetente";
		
		if(isPrimaNotaIntegrata() || primaNota.getStrutturaCompetente() == null || primaNota.getStrutturaCompetente().getUid() == 0) {
			return;
		}
		
		StrutturaAmministrativoContabile strutturaCompetente = trovaStrutturaCompetente(primaNota.getStrutturaCompetente());
		
		if(strutturaCompetente == null) {
			log.debug(methodName, "ERRORE nel reperimento della SAC con UID: [" + primaNota.getStrutturaCompetente().getUid() + "]");
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA_SINGOLO_MSG.getErrore("La Struttura competentente"));
		}
		
		// mi assicuro di lavorare solo con i settori (CDC), i Centri Di Responsabilita' (CDR) non sono ammessi
		if(strutturaCompetente.getTipoClassificatore() != null && TipoClassificatore.CDR.getCodice().equals(strutturaCompetente.getTipoClassificatore().getCodice())) {
			log.debug(methodName, "ERRORE la SAC con UID: [" + primaNota.getStrutturaCompetente().getUid() + "] non e' un non e' un CDC");
			throw new BusinessException(ErroreCore.FORMATO_NON_VALIDO.getErrore("Struttura competente", ", selezionare un centro di controllo anziche' un centro di responsabilita'"));
		}
		
		primaNota.setStrutturaCompetente(strutturaCompetente);
			
	}
	
	protected StrutturaAmministrativoContabile trovaStrutturaCompetente(StrutturaAmministrativoContabile strutturaCompetente) {
		return classificatoriDad.ricercaClassificatoreById(strutturaCompetente.getUid());
	}
	
}
