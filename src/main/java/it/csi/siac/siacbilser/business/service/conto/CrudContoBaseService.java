/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.conto;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.integration.dad.ContoDad;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.model.Conto;


/**
 * Base per inserisci/aggiorna Conto.
 * 
 * @author Domenico
 *
 * @param <REQ> the generic type
 * @param <RES> the generic type
 */
public abstract class CrudContoBaseService<REQ extends ServiceRequest,RES extends ServiceResponse> extends CheckedAccountBaseService<REQ,RES> {

	//DAD
	@Autowired
	protected ContoDad contoDad;
	@Autowired
	protected BilancioDad bilancioDad;
	
	protected Conto conto;
	protected Bilancio bilancio;

	

	@Override
	protected void init() {
		super.init();
		contoDad.setEnte(ente);
		contoDad.setLoginOperazione(loginOperazione);
		
		bilancioDad.setEnteEntity(ente);
	}
	
	
	

	protected void caricaBilancio() {
		Bilancio bil = bilancioDad.getBilancioByUid(bilancio.getUid());
		if(bil == null){
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("Bilancio", "uid: "+ bilancio.getUid()));
		}
		this.bilancio = bil;
	}

	protected void caricaContoPadre() {
		Date inizioAnno = Utility.primoGiornoDellAnno(bilancio.getAnno());
		
		Conto contoPadre = contoDad.findContoById(conto.getContoPadre(), inizioAnno);
		if(contoPadre==null){
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("Conto padre", "uid: "+ conto.getContoPadre().getUid()));
		}
		
		conto.setContoPadre(contoPadre);
	}
	
	protected void checkCodiceConto() {
		
		//Verifica se il codice conto e' coerente con il padre
		boolean isCodiceContoCoerenteColPadre = conto.getCodice().startsWith(conto.getContoPadre().getCodice());
		if(!isCodiceContoCoerenteColPadre) {
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Codice conto non coerente, deve iniziare con \""+ conto.getContoPadre().getCodice()+ "\""));
		}
		
		
		
		
		
		
	}

}
