/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.conto;

import java.util.Date;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceConto;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceContoResponse;
import it.csi.siac.siacgenser.model.Conto;
import it.csi.siac.siacgenser.model.errore.ErroreGEN;


/**
 * Servizio di Inserimento di un Conto figlio
 * 
 * @author Domenico
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceContoService extends CrudContoBaseService<InserisceConto, InserisceContoResponse> {


	/*
	 * Per il salvataggio impostare gli attributi del conto non gestiti dalla  maschera:
	 * - data inizio validità del conto al 01/01/ dell'anno di bilancio su cui si sta lavorando 
	 * - attivo = TRUE 
	 * - livello = livello del padre a +1 
	 * - ordine = valorizzare con il codice del Conto 
	 * - impostare le date di logging con la data dell'operazione di inserimento (data corrente)
	 * 
	 * E procedere salvando:
	 * - i dati del conto (entità Conto ed entità correlate). 
	 * - le relazioni con codifiche e classificatori generici se compilati, 
	 *  impostando la data inizio validità della relazione al 01/01/ dell'anno di bilancio su cui si sta lavorando
	 * 
	 * Il sistema notifica l'esito dell'operazione con eventuali segnalazioni di
	 * errore.
	 */

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		this.conto = req.getConto();
		checkNotNull(conto, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("conto"));
		conto.setEnte(ente);
		
		checkEntita(conto.getContoPadre(), "conto padre conto");
		
		checkNotBlank(conto.getCodice(), "codice conto");
		checkNotBlank(conto.getDescrizione(), "descrizione conto");
		
		checkEntita(conto.getTipoConto(), "tipo conto");
		
		checkCondition( (conto.getTipoConto().isTipoCespiti() && conto.getCategoriaCespiti() != null) || (!conto.getTipoConto().isTipoCespiti() && conto.getCategoriaCespiti() == null), 
				ErroreGEN.OPERAZIONE_NON_CONSENTITA_0011.getErrore());
		
		this.bilancio = req.getBilancio();
		checkEntita(bilancio, "bilancio");
	}
	
	@Override
	@Transactional
	public InserisceContoResponse executeService(InserisceConto serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		
		caricaBilancio();
		caricaContoPadre();
		
		checkCodiceConto();
		
		Date inizioAnno = Utility.primoGiornoDellAnno(bilancio.getAnno());
		conto.setDataInizioValidita(inizioAnno);
		
		conto.setLivello(conto.getContoPadre().getLivello() + 1);
		
		conto.setOrdine(conto.getCodice());
		
		conto.setAttivo(Boolean.TRUE);
		
		conto.setAmbito(conto.getContoPadre().getAmbito());
		
		contoDad.inserisciConto(conto);
		res.setConto(conto);
	}
	
	


	@Override
	protected void checkCodiceConto() {
		if(conto.getContoPadre().getLivello() != 0){
			super.checkCodiceConto();
		}
		
		//Verifica se il codice conto non e' già stato utilizato
		Conto contoTrovato = contoDad.ricercaContoByCodice(this.conto.getCodice(), bilancio.getAnno(), this.conto.getContoPadre().getAmbito());
		if(contoTrovato!=null) {
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Codice non univoco: "+ this.conto.getCodice()));
		}
	}


}
