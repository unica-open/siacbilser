/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cassaeconomale;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.integration.dad.TipoOperazioneDiCassaDad;
import it.csi.siac.siaccecser.frontend.webservice.msg.InserisceTipoOperazioneDiCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.InserisceTipoOperazioneDiCassaResponse;
import it.csi.siac.siaccecser.model.TipoOperazioneCassa;
import it.csi.siac.siaccecser.model.errore.ErroreCEC;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceTipoOperazioneDiCassaService extends CheckedAccountBaseService<InserisceTipoOperazioneDiCassa, InserisceTipoOperazioneDiCassaResponse> {
		
	@Autowired 
	private TipoOperazioneDiCassaDad tipoOperazioneDiCassaDad;
	@Autowired 
	private BilancioDad bilancioDad;
	
	private TipoOperazioneCassa tipoOperazioneCassa;
	private Bilancio bilancio;
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		tipoOperazioneCassa = req.getTipoOperazioneCassa();
		bilancio = req.getBilancio();
		
		checkEntita(bilancio, "bilancio", false);
		checkNotNull(tipoOperazioneCassa, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo operazione di cassa"));
		
		checkCondition(StringUtils.isNotBlank(tipoOperazioneCassa.getCodice()), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("codice tipo operazione di cassa"), false);
		checkCondition(StringUtils.isNotBlank(tipoOperazioneCassa.getDescrizione()), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("descrizione tipo operazione di cassa"), false);
		// Lotto M
		checkEntita(tipoOperazioneCassa.getCassaEconomale(), "cassa economale tipo operazione di cassa", false);
		checkEntita(tipoOperazioneCassa.getEnte(), "ente tipo operazione di cassa", false);
		checkNotNull(tipoOperazioneCassa.getTipologiaOperazioneCassa(), "tipologia operazione tipo operazione di cassa", false);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		tipoOperazioneDiCassaDad.setEnte(ente);
		tipoOperazioneDiCassaDad.setLoginOperazione(loginOperazione);
	}
	
	

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public InserisceTipoOperazioneDiCassaResponse executeService(InserisceTipoOperazioneDiCassa serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		checkCodiceGiaEsistente();
		tipoOperazioneCassa.setDataFineValidita(null);
		tipoOperazioneDiCassaDad.inserisciTipoOperazioneCassa(tipoOperazioneCassa);
		res.setTipoOperazioneCassa(tipoOperazioneCassa);
	}

	private void checkCodiceGiaEsistente() {
		List<TipoOperazioneCassa> tipiOperazione = tipoOperazioneDiCassaDad.findTipoOperazioneByCodiceAndAnnoAndCassaEconomaleAndTipologia(tipoOperazioneCassa, bilancio.getAnno());
		if(tipiOperazione!= null && !tipiOperazione.isEmpty()){
			throw new BusinessException(ErroreCEC.CEC_ERR_0009.getErrore());
		}
	}
	
	
}
