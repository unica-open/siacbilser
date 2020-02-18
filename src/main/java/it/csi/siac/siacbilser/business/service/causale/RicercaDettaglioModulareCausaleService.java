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
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioModulareCausale;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioModulareCausaleResponse;
import it.csi.siac.siacgenser.model.CausaleEP;

/**
 * Ricerca di dettaglio modulare di una Causale
 * @author Marchino Alessandro
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDettaglioModulareCausaleService extends CheckedAccountBaseService<RicercaDettaglioModulareCausale, RicercaDettaglioModulareCausaleResponse> {

	//DADs
	@Autowired
	private CausaleEPDad causaleEPDad;
	@Autowired
	private BilancioDad bilancioDad;
	
	private CausaleEP causaleEP;
	private Bilancio bilancio;

	/*
	 * L'operazione effettua la ricerca di una causale per identificativo fisico e ne espone tutti i dati di dettaglio e relazioni attive.
	 * 
	 * Per individuare l'istanza di una CausaleEP valida in un Bilancio applicare le regole gia' definite nelle linee
	 * guida di sviluppo sulle date validita' e data cancellazione dell'entita' e delle sue relazioni,
	 * avendo come periodo di riferimento 1/1 – 31/12 dell'anno di bilancio su cui si sta lavorando.
	 * 
	 * Ovvero l'istanza della Causale e sue relazioni sono valide se sono vere le seguenti condizioni:
	 * - Data cancellazione = nulla
	 * - Data inizio validita' <= anno bilancio
	 * - Data fine validita' = nulla o >= anno bilancio
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
	protected void init() {
		super.init();
		causaleEPDad.setEnte(ente);
		causaleEPDad.setLoginOperazione(loginOperazione);
		
		bilancioDad.setEnteEntity(ente);
	}
	
	
	@Override
	@Transactional(readOnly=true)
	public RicercaDettaglioModulareCausaleResponse executeService(RicercaDettaglioModulareCausale serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	
	@Override
	protected void execute() {
		caricaBilancio();
		
		Date inizioAnno = Utility.primoGiornoDellAnno(bilancio.getAnno());
		causaleEP.setDataInizioValiditaFiltro(inizioAnno);
		
		CausaleEP causaleEPTrovata = causaleEPDad.findCausaleEPByIdModelDetail(causaleEP, req.getCausaleEPModelDetails());
		
		res.setCausaleEP(causaleEPTrovata);
		
	}
	
	
	protected void caricaBilancio() {
		Bilancio bil = bilancioDad.getBilancioByUid(bilancio.getUid());
		if(bil == null){
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("Bilancio", "uid: "+ bilancio.getUid()));
		}
		this.bilancio = bil;
	}

}
