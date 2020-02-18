/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.conto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.integration.dad.ContoDad;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.frontend.webservice.msg.AnnullaConto;
import it.csi.siac.siacgenser.frontend.webservice.msg.AnnullaContoResponse;
import it.csi.siac.siacgenser.model.Conto;

/**
 * Permette di annullare un conto e tutti i suoi figli.
 * 
 * @author Domenico
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AnnullaContoService extends CheckedAccountBaseService<AnnullaConto, AnnullaContoResponse> {
	
	//DAD
	@Autowired
	protected ContoDad contoDad;
	@Autowired
	protected BilancioDad bilancioDad;
	
	private Conto conto;
	private Bilancio bilancio;
	
	/*
	 * Per il salvataggio dell'operazione il sistema deve verificare se il Conto passato in input è annullabile, controllando quanto segue.
     * 
     * NON è annullabile se:  è collegato ad una causale in stato diverso da 'ANNULLATA'  nell'anno di bilancio in corso. 
     * Se il conto non è foglia la verifica va fatta su tutti  i conti a lui collegati di livello inferiore di tipo foglia. 
     * In questi casi  inviare il messaggio  < COR_ERR_0044	Operazione non consentita (messaggio:  'Il conto o uno dei suoi figli è collegato a causali in corso di validità, 
     * occorre scollegare le causali prima procedere all'annullamento.')>
     * 
     * Se la verifica ha esito positivo si opera come descritto di seguito.
     * •	Se data inizio validità  = anno corrente :  si opera una cancellazione logica impostando data cancellazione = data corrente.
     * •	Se data inizio validità  < anno corrente :  si chiude la validità del conto al 31/12 dell'anno precedente.
     * 
     * L'operazione è applicata a tutti i figli del conto.
     * Il sistema notifica l'esito dell'operazione con eventuali segnalazioni di errore.
	 * 
	 */

	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		this.conto = req.getConto();
		checkEntita(conto, "Conto");
		

		this.bilancio = req.getBilancio();
		checkEntita(bilancio, "bilancio");
	}
	
	
	@Override
	@Transactional
	public AnnullaContoResponse executeService(AnnullaConto serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void init() {
		super.init();
		contoDad.setEnte(ente);
		contoDad.setLoginOperazione(loginOperazione);
		
		bilancioDad.setEnteEntity(ente);
		
	}
	
	
	@Override
	protected void execute() {
		
		caricaBilancio();
		
		checkContoConCausaliTutteAnnullate();
		
		contoDad.annullaConto(this.conto);
	}

	
	private void caricaBilancio() {
		this.bilancio = bilancioDad.getBilancioByUid(req.getBilancio().getUid());
		
		if(this.bilancio == null){
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("Bilancio", "uid: "+ req.getBilancio().getUid()));
		}
	}

	/**
	 *  NON è annullabile se: è collegato ad una causale in stato diverso da 'ANNULLATA' nell'anno di bilancio in corso.
	 */
	private void checkContoConCausaliTutteAnnullate() {
		
		Long count = contoDad.countCausaliNonAnnullateAssociateAlConto(this.conto);
		if(count>0) {
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Il conto o uno dei suoi figli e' collegato a causali in corso di validita', occorre scollegare le causali prima procedere all'annullamento"));
		}
		
	}

}
