/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.quadroeconomico;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.integration.dad.QuadroEconomicoDad;
import it.csi.siac.siacbilser.model.QuadroEconomico;
import it.csi.siac.siacbilser.model.QuadroEconomicoModelDetail;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;


/**
 * Base per inserisci/aggiorna .
 * 
 * @author Domenico
 *
 * @param <REQ> the generic type
 * @param <RES> the generic type
 */
public abstract class CrudQuadroEconomicoBaseService<REQ extends ServiceRequest,RES extends ServiceResponse> extends CheckedAccountBaseService<REQ,RES> {


	@Autowired	protected QuadroEconomicoDad quadroEconomicoDad;
	@Autowired	protected BilancioDad bilancioDad;
	
	protected QuadroEconomico quadroEconomico;
	protected Bilancio bilancio;

	

	@Override
	protected void init() {
		super.init();
		quadroEconomicoDad.setEnte(ente);
		quadroEconomicoDad.setLoginOperazione(loginOperazione);
		
		bilancioDad.setEnteEntity(ente);
	}
	
	
	

	protected void caricaBilancio() {
		Bilancio bil = bilancioDad.getBilancioByUid(bilancio.getUid());
		if(bil == null){
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("Bilancio", "uid: "+ bilancio.getUid()));
		}
		this.bilancio = bil;
	}
	
	protected void popolaDatiDefaultQuadroEconomico() {
		Date inizioAnno = Utility.primoGiornoDellAnno(bilancio.getAnno());
		quadroEconomico.setDataInizioValidita(inizioAnno);
		int livello = quadroEconomico.getQuadroEconomicoPadre()!= null && quadroEconomico.getQuadroEconomicoPadre().getUid()!= 0 ? 1 : 0;
		quadroEconomico.setLivello(livello);
		quadroEconomico.setEnte(ente);
	}
	
	/**
	 * @return
	 */
	protected List<QuadroEconomico> caricaQuadroEconomicoByCodice() {
		return quadroEconomicoDad.findQuadroEconomicoValidoByCodice(quadroEconomico);
	}

	protected List<QuadroEconomico> caricaQuadroEconomicoByCodiceAndParte() {
		return quadroEconomicoDad.findQuadroEconomicoValidoByCodiceAndParte(quadroEconomico, QuadroEconomicoModelDetail.Parte);
	}
	/**
	 * Check QuadroEconomico padre.
	 */
	protected void checkQuadroEconomicoPadre() {
		QuadroEconomico quadroEconomicoPadre = quadroEconomico.getQuadroEconomicoPadre();
		if(quadroEconomicoPadre == null || quadroEconomicoPadre.getUid() == 0){
			//nonsto inserendo un classificatore figlio di qualche altro classificatore: non devo e non posso controllare un padre che non esiste!
			return;
		}
		QuadroEconomico cgsaPadre = quadroEconomicoDad.findQuadroEconomicoById(quadroEconomicoPadre,QuadroEconomicoModelDetail.Parte);
		if(cgsaPadre == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_COMPLETA.getErrore("Quadro Economico di livello 2 con codice " + quadroEconomico.getCodice(), " non è legato ad un quadro economico di livello superiore. "));
		}
		quadroEconomico.setQuadroEconomicoPadre(cgsaPadre);
	}
	
	/**
	 * Check QuadroEconomico esistente.
	 *
	 * @return the QuadroEconomico
	 */
	protected QuadroEconomico checkQuadroEconomicoEsistente() {
		QuadroEconomico cgsaOld = quadroEconomicoDad.findQuadroEconomicoById(quadroEconomico,QuadroEconomicoModelDetail.Parte);
		if(cgsaOld == null) {
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore(" quadro economico", "il quadro economico non e' presente in archivio. "));
		}
		return cgsaOld;
	}

	
}
