/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.classifgsa;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.frontend.webservice.msg.AnnullaClassificatoreGSA;
import it.csi.siac.siacgenser.frontend.webservice.msg.AnnullaClassificatoreGSAResponse;


/**
 * Per l'annullamento Il sistema verifica 
 * <ul>
 *  <li> se l’eliminazione riguarda un Classificatore di LIV 1 tutti i suoi classificatori di Liv2 devono essere annullati . In caso contrario l’eliminazione non è possibile e il sistema segnala </li>
 *  <li> se il classificatore è già annullato il sistema segnala </li>
 * </ul>
 * Se controlli superati al classificatore viene impostato lo stato Annullato
 * 
 * @author Elisa Chiari
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AnnullaClassificatoreGSAService extends CrudClassificatoreGSABaseService<AnnullaClassificatoreGSA, AnnullaClassificatoreGSAResponse> {
	

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		this.classificatoreGSA = req.getClassificatoreGSA();
		this.bilancio = req.getBilancio();
		checkEntita(req.getClassificatoreGSA(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("classificatore gsa").getTesto());
		
	}
	
	@Override
	@Transactional
	public AnnullaClassificatoreGSAResponse executeService(AnnullaClassificatoreGSA serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		
		checkClassificatoreGSAEsistente();
		
		checkStatoOperativoClassificatoriFigli();
		
		aggiornaStatoClassificatoreGSA();
		
	}

	private void aggiornaStatoClassificatoreGSA() {
		classificatoreGsaDad.annullaStatoClassificatoreGSA(classificatoreGSA);		
	}

	/**
	 * Se l’eliminazione riguarda un Classificatore di LIV 1 tutti i suoi classificatori di Liv2 devono essere annullati .
	 * In caso contrario l’eliminazione non &rgrave; possibile e il sistema segnala 
	 */
	private void checkStatoOperativoClassificatoriFigli() {
		Boolean classificatoreGSASenzaFigliValidi = classificatoreGsaDad.isClassificatoreGSASenzaFigliValidi(classificatoreGSA);
		if(!Boolean.TRUE.equals(classificatoreGSASenzaFigliValidi)) {
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("esistono dei classificatori di livello 2 non annullati"));
		}
	}

}
