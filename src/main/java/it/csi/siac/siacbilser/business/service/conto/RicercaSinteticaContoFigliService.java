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
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaContoFigli;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaContoFigliResponse;
import it.csi.siac.siacgenser.model.Conto;

/**
 * Service RicercaSinteticaContoFigliService.
 * 
 * @author Domenico
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSinteticaContoFigliService extends CheckedAccountBaseService<RicercaSinteticaContoFigli, RicercaSinteticaContoFigliResponse> {

	//DAD
	@Autowired
	private ContoDad contoDad;
	@Autowired
	private BilancioDad bilancioDad;
	
	private Bilancio bilancio;
	private Date inizioAnno;
	


	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getConto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("conto"));
		req.getConto().setEnte(ente);
		checkNotNull(req.getConto().getAmbito(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ambito conto"));
		
		checkEntita(req.getBilancio(), "Bilancio");
		this.bilancio = req.getBilancio();
		
		checkParametriPaginazione(req.getParametriPaginazione());
		
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
	public RicercaSinteticaContoFigliResponse executeService(RicercaSinteticaContoFigli serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		caricaBilancio();
		inizioAnno = Utility.primoGiornoDellAnno(bilancio.getAnno());
		
		Conto contoPadre = ricercaSinteticaContoPadre();
		
		contoPadre.setDataInizioValiditaFiltro(inizioAnno);
		ListaPaginata<Conto> contiFiglio = contoDad.ricercaSinteticaContoFigli(contoPadre, req.getParametriPaginazione());
		
		res.setContoPadre(contoPadre);
		res.setContiFiglio(contiFiglio);
	}
	

	private Conto ricercaSinteticaContoPadre() {
		req.getConto().setDataInizioValiditaFiltro(inizioAnno);
		
		// SIAC-5225: Imposto i parametri di paginazione minimi per la ricerca. Seleziono solo il primo elemento
		// Utilizzo dei nuovi parametri di paginazione in quanto il padre deve essere sempre lo stesso, indipendentemente dalla pagina dei figli
		ParametriPaginazione pp = new ParametriPaginazione(0, 1);
		ListaPaginata<Conto> contiPadre = contoDad.ricercaSinteticaConto(req.getConto(), pp);
		
		if(contiPadre==null || contiPadre.isEmpty()){
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Conto",""), Esito.FALLIMENTO);
		}
		
		if(contiPadre.getTotaleElementi()>1){
			throw new BusinessException("E' stato trovato piu' di un Conto", Esito.FALLIMENTO);
		}
		
		Conto conto = contiPadre.get(0);
		return conto;
	}
	
	
	protected void caricaBilancio() {
		Bilancio bil = bilancioDad.getBilancioByUid(bilancio.getUid());
		if(bil == null){
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("Bilancio", "uid: "+ bilancio.getUid()));
		}
		this.bilancio = bil;
	}

}
