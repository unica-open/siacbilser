/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.classificazionecespiti;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dad.CategoriaCespitiDad;
import it.csi.siac.siacbilser.integration.dad.TipoBeneCespiteDad;
import it.csi.siac.siaccespser.frontend.webservice.msg.EliminaCategoriaCespiti;
import it.csi.siac.siaccespser.frontend.webservice.msg.EliminaCategoriaCespitiResponse;
import it.csi.siac.siaccespser.model.CategoriaCespiti;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * 
 * @author Antonino
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EliminaCategoriaCespitiService extends CheckedAccountBaseService<EliminaCategoriaCespiti, EliminaCategoriaCespitiResponse> {

	//DAD
	@Autowired
	private CategoriaCespitiDad categoriaCespitiDad;
	@Autowired
	private TipoBeneCespiteDad tipoBeneCespiteDad;

	private CategoriaCespiti categoriaCespiti;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		checkEntita(req.getCategoriaCespiti(), "categoria cespiti");		
		categoriaCespiti = req.getCategoriaCespiti();
	}
	
	@Override
	protected void init() {
		super.init();
		categoriaCespitiDad.setEnte(ente);
		categoriaCespitiDad.setLoginOperazione(loginOperazione);		
	}
	
	@Transactional
	@Override
	public EliminaCategoriaCespitiResponse executeService(EliminaCategoriaCespiti serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		checkCategoriaNonCollegataATipoBene();
		categoriaCespitiDad.eliminaCategoriaCespiti(categoriaCespiti);
		res.setCategoriaCespiti(categoriaCespiti);
	}

	/**
	 * Check categoria non collegata A tipo bene.
	 */
	private void checkCategoriaNonCollegataATipoBene() {
		Date primoGiornoDellAnno = Utility.primoGiornoDellAnno(req.getAnnoBilancio());
		categoriaCespiti.setDataInizioValiditaFiltro(primoGiornoDellAnno);
		Long countTipoBene = tipoBeneCespiteDad.countTipoBeneByCategoria(categoriaCespiti);
		if(countTipoBene != null && countTipoBene.longValue() != 0L) {
			//non esitono accantonamenti per il capitolo: non posso aggiornarlo, lancio un errore 
			throw new BusinessException(ErroreCore.ESISTONO_ENTITA_COLLEGATE.getErrore());
		}
	}
	
}