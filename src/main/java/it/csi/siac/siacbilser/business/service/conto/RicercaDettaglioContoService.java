/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.conto;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.integration.dad.ContoDad;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioConto;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioContoResponse;
import it.csi.siac.siacgenser.model.Conto;

/**
 * The Class RicercaDettaglioContoService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDettaglioContoService extends CheckedAccountBaseService<RicercaDettaglioConto, RicercaDettaglioContoResponse> {

	//DAD
	@Autowired
	protected ContoDad contoDad;
	@Autowired
	protected BilancioDad bilancioDad;
	
	private Conto conto;

	private Bilancio bilancio;
	
	
	/*
	 *
	 * Per individuare l'istanza di un conto valida in un Bilancio applicare le regole già definite nelle linee guida di sviluppo sulle date validità e data cancellazione dell'entità e delle sue relazioni, avendo come periodo di riferimento 1/1 – 31/12 dell'anno di bilancio su cui si sta lavorando.
	 *
	 * Ovvero l'istanza del Piano dei Conti e del Conto e sue relazioni sono valide se sono vere le seguenti condizioni. 
	 * •	Data cancellazione =  nulla
	 * •	Data inizio validità <= anno bilancio
	 * •	Data fine validità  = nulla o  >= anno bilancio 

	 */


	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getConto(), "Conto");
		this.conto = req.getConto();
		
		checkEntita(req.getBilancio(), "Bilancio");
		this.bilancio = req.getBilancio();
		
	}
	
	@Override
	protected void init() {
		super.init();
		contoDad.setEnte(ente);
		contoDad.setLoginOperazione(loginOperazione);

		bilancioDad.setEnteEntity(ente);
	}
	
	@Override
	@Transactional(readOnly=true)
	public RicercaDettaglioContoResponse executeService(RicercaDettaglioConto serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		caricaBilancio();
		
		Date inizioAnno = Utility.primoGiornoDellAnno(bilancio.getAnno());
		
		Conto contoTrovato = contoDad.findContoById(this.conto, inizioAnno, req.getContoModelDetails());
		
		if(contoTrovato==null){
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Conto"));
			res.setEsito(Esito.FALLIMENTO);
			return;
		}
		
		
		Boolean contiFiglioSenzaFigli = contoDad.isContiFiglioSenzaFigli(contoTrovato);
		res.setContiFiglioSenzaFigli(contiFiglioSenzaFigli);
		
		res.setConto(contoTrovato);
	}
	
	
	protected void caricaBilancio() {
		Bilancio bil = bilancioDad.getBilancioByUid(bilancio.getUid());
		if(bil == null){
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("Bilancio", "uid: "+ bilancio.getUid()));
		}
		this.bilancio = bil;
	}

}
