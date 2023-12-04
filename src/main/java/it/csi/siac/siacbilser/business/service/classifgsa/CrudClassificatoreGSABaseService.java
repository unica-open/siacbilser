/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.classifgsa;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.integration.dad.ClassificatoreGSADad;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.model.ClassificatoreGSA;


/**
 * Base per inserisci/aggiorna Conto.
 * 
 * @author Domenico
 *
 * @param <REQ> the generic type
 * @param <RES> the generic type
 */
public abstract class CrudClassificatoreGSABaseService<REQ extends ServiceRequest,RES extends ServiceResponse> extends CheckedAccountBaseService<REQ,RES> {


	@Autowired	protected ClassificatoreGSADad classificatoreGsaDad;
	@Autowired	protected BilancioDad bilancioDad;
	
	protected ClassificatoreGSA classificatoreGSA;
	protected Bilancio bilancio;

	

	@Override
	protected void init() {
		super.init();
		classificatoreGsaDad.setEnte(ente);
		classificatoreGsaDad.setLoginOperazione(loginOperazione);
		
		bilancioDad.setEnteEntity(ente);
	}
	
	
	

	protected void caricaBilancio() {
		Bilancio bil = bilancioDad.getBilancioByUid(bilancio.getUid());
		if(bil == null){
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("Bilancio", "uid: "+ bilancio.getUid()));
		}
		this.bilancio = bil;
	}
	
	protected void popolaDatiDefaultClassificatoreGSA() {
		Date inizioAnno = Utility.primoGiornoDellAnno(bilancio.getAnno());
		classificatoreGSA.setDataInizioValidita(inizioAnno);
		int livello = classificatoreGSA.getClassificatoreGSAPadre()!= null && classificatoreGSA.getClassificatoreGSAPadre().getUid()!= 0 ? 1 : 0;
		classificatoreGSA.setLivello(livello);
		classificatoreGSA.setEnte(ente);
	}
	
	/**
	 * @return
	 */
	protected ClassificatoreGSA caricaClassificatoreGSAByCodice() {
		return classificatoreGsaDad.findClassificatoreGSAValidoByCodice(classificatoreGSA);
	}
	
	/**
	 * Check classificatore padre.
	 */
	protected void checkClassificatorePadre() {
		ClassificatoreGSA classificatoreGSAPadre = classificatoreGSA.getClassificatoreGSAPadre();
		//TODO: valutare se inserire un controllo sul livello
		if( classificatoreGSAPadre == null || classificatoreGSAPadre.getUid() == 0){
			//nonsto inserendo un classificatore figlio di qualche altro classificatore: non devo e non posso controllare un padre che non esiste!
			return;
		}
		ClassificatoreGSA cgsaPadre = classificatoreGsaDad.findClassificatoreGSAById(classificatoreGSAPadre);
		if(cgsaPadre == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_COMPLETA.getErrore("Classificatore gsa di livello 2 con codice " + classificatoreGSA.getCodice(), " non è legato ad un classificatore di livello superiore. "));
		}
		classificatoreGSA.setClassificatoreGSAPadre(cgsaPadre);
	}
	
	/**
	 * Check classificatore esistente.
	 *
	 * @return the classificatore GSA
	 */
	protected ClassificatoreGSA checkClassificatoreGSAEsistente() {
		ClassificatoreGSA cgsaOld = classificatoreGsaDad.findClassificatoreGSAById(classificatoreGSA);
		if(cgsaOld == null) {
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore(" classificatore gsa", "il classificatore non e' presente in archivio. "));
		}
		return cgsaOld;
	}

	
}
