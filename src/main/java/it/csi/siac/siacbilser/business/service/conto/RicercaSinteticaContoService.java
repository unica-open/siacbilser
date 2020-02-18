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
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaConto;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaContoResponse;
import it.csi.siac.siacgenser.model.Conto;

/**
 * Service RicercaSinteticaContoService.
 * 
 * @author Domenico
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSinteticaContoService extends CheckedAccountBaseService<RicercaSinteticaConto, RicercaSinteticaContoResponse> {

	//DAD
	@Autowired
	protected ContoDad contoDad;
	@Autowired
	private BilancioDad bilancioDad;
	
	private Bilancio bilancio;


	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getConto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("conto"));
		checkNotNull(req.getConto().getAmbito(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ambito conto"));
		req.getConto().setEnte(ente);
		
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
	public RicercaSinteticaContoResponse executeService(RicercaSinteticaConto serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		caricaBilancio();
		Date inizioAnno = Utility.primoGiornoDellAnno(bilancio.getAnno());
		req.getConto().setDataInizioValiditaFiltro(inizioAnno);
		
		ListaPaginata<Conto> conti = contoDad.ricercaSinteticaConto(req.getConto(), req.getParametriPaginazione());
		
		res.setConti(conti);
		res.setCardinalitaComplessiva(conti.size());
	}
	
	protected void caricaBilancio() {
		Bilancio bil = bilancioDad.getBilancioByUid(bilancio.getUid());
		if(bil == null){
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("Bilancio", "uid: "+ bilancio.getUid()));
		}
		this.bilancio = bil;
	}

}
