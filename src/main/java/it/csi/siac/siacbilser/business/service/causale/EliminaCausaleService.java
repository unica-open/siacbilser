/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.causale;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.integration.dad.CausaleEPDad;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.frontend.webservice.msg.EliminaCausale;
import it.csi.siac.siacgenser.frontend.webservice.msg.EliminaCausaleResponse;
import it.csi.siac.siacgenser.model.CausaleEP;

/**
 * Elimina una CausaleEP
 * 
 * @author Domenico
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EliminaCausaleService extends CheckedAccountBaseService<EliminaCausale, EliminaCausaleResponse> {

	@Autowired
	private CausaleEPDad causaleEPDad;
	@Autowired
	private BilancioDad bilancioDad;
	
	private CausaleEP causaleEP;
	private Bilancio bilancio;
	
	/*
	 * Il sistema deve verificare le condizioni seguenti.
	 * 
	 * DA NON SVILUPPARE IN V1
	 * E' eliminabile una CausaleEP non collegata ad una PrimaNota che non abbia data cancellazione valorizzata. 
	 * In questi casi, se richiesto visualizzare un messaggio per l'operatore,  
	 * inviare il messaggio  < COR_ERR_0044 Operazione non consentita (messaggio:  'La causale è collegata a registrazioni.')>.
	 * 
	 * Se il controllo da esito positivo il sistema procede aggiornando la data di cancellazione dell'istanza e delle sue relazioni con le entità correlate.
	 * Il sistema notifica l'esito dell'operazione con eventuali segnalazioni di errore.
	 *
	 */
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getCausaleEP(), "causaleEP");
		this.causaleEP = req.getCausaleEP();
		this.causaleEP.setEnte(ente);
		
		checkEntita(req.getBilancio(), "bilancio", false);
		this.bilancio = req.getBilancio();
	}
	
	
	@Override
	@Transactional
	public EliminaCausaleResponse executeService(EliminaCausale serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	
	@Override
	protected void init() {
		super.init();
		causaleEPDad.setEnte(ente);
		causaleEPDad.setLoginOperazione(loginOperazione);
		
		bilancioDad.setEnteEntity(ente);
	}
	
	@Override
	protected void execute() {
		caricaBilancio();
		
		Date inizioAnno = Utility.primoGiornoDellAnno(bilancio.getAnno());
		causaleEP.setDataInizioValidita(inizioAnno);
		
		checkNonCollegataAdUnaPrimaNota();
		
		causaleEPDad.eliminaCausaleEP(causaleEP);
		
		res.setCausaleEP(causaleEP);
	}


	/**
	 * DA NON SVILUPPARE IN V1 E' eliminabile una CausaleEP non collegata ad una
	 * PrimaNota che non abbia data cancellazione valorizzata. 
	 * In questi casi, se richiesto visualizzare un messaggio per l'operatore, inviare il
	 * messaggio < COR_ERR_0044 Operazione non consentita (messaggio: 'La causale è collegata a registrazioni.')>.
	 */
	private void checkNonCollegataAdUnaPrimaNota() {
		// TODO checkNonCollegataAdUnaPrimaNota DA NON SVILUPPARE IN V1

	}
	
	protected void caricaBilancio() {
		Bilancio bil = bilancioDad.getBilancioByUid(bilancio.getUid());
		if(bil == null){
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("Bilancio", "uid: "+ bilancio.getUid()));
		}
		this.bilancio = bil;
	}

}
