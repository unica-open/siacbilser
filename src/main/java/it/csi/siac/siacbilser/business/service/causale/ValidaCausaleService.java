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
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.frontend.webservice.msg.ValidaCausale;
import it.csi.siac.siacgenser.frontend.webservice.msg.ValidaCausaleResponse;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.CausaleEPModelDetail;
import it.csi.siac.siacgenser.model.StatoOperativoCausaleEP;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ValidaCausaleService extends CheckedAccountBaseService<ValidaCausale, ValidaCausaleResponse> {

	@Autowired
	private CausaleEPDad causaleEPDad;
	@Autowired
	private BilancioDad bilancioDad;
	
	private CausaleEP causaleEP;
	private Bilancio bilancio;
	
	/*
	 * 
	 * Il sistema deve verificare che il passaggio di stato sia coerente, vedi diagramma a stati descritto nel dizionario dati [5].   
	 * In caso contrario invia il messaggio di errore  <COR_ERR_0028 Operazione incompatibile con stato dell'entità  
	 * (entità:  riportare il CausaleEP  codice – descrizione,   stato:  riportare StatoOperativoCausaleEP.descrizione)>.
	 * 
	 * Se il controllo da esito positivo il sistema procede aggiornando lo stato della causale. 
	 * Il sistema notifica l’esito dell’operazione con eventuali segnalazioni di errore.
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
	
	@Transactional
	@Override
	public ValidaCausaleResponse executeService(ValidaCausale serviceRequest) {
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
		caricaStatoOperativoCausaleEP();
		
		checkStatoOperativo();
		
		Date inizioAnno = Utility.primoGiornoDellAnno(bilancio.getAnno());
		causaleEP.setDataInizioValidita(inizioAnno);
		
		
		causaleEPDad.aggiornaStatoOperativoCausaleEP(causaleEP, StatoOperativoCausaleEP.VALIDO);

		res.setCausaleEP(causaleEP);
	}

	private void checkStatoOperativo() {
		if(causaleEP.getStatoOperativoCausaleEP() != StatoOperativoCausaleEP.PROVVISORIO) {
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Stato attuale ("+ causaleEP.getStatoOperativoCausaleEP()+") non congruente."), Esito.FALLIMENTO);
		}
	}

	private void caricaStatoOperativoCausaleEP() {
		String methodName = "caricaStatoOperativoCausaleEP";
		Date inizioAnno = Utility.primoGiornoDellAnno(bilancio.getAnno());
		this.causaleEP.setDataInizioValiditaFiltro(inizioAnno);
		
		CausaleEP causaleEPTrovata = causaleEPDad.findCausaleEPById(this.causaleEP, CausaleEPModelDetail.Stato);
		log.debug(methodName, "statoAttuale causaleEP:" + causaleEPTrovata.getStatoOperativoCausaleEP());
		this.causaleEP.setStatoOperativoCausaleEP(causaleEPTrovata.getStatoOperativoCausaleEP());
	}
	
	protected void caricaBilancio() {
		Bilancio bil = bilancioDad.getBilancioByUid(bilancio.getUid());
		if(bil == null){
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("Bilancio", "uid: "+ bilancio.getUid()));
		}
		this.bilancio = bil;
	}

}
