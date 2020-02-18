/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cassaeconomale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.TipoOperazioneDiCassaDad;
import it.csi.siac.siaccecser.frontend.webservice.msg.AnnullaTipoOperazioneDiCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.AnnullaTipoOperazioneDiCassaResponse;
import it.csi.siac.siaccecser.model.StatoOperativoTipoOperazioneCassa;
import it.csi.siac.siaccecser.model.TipoOperazioneCassa;
import it.csi.siac.siaccecser.model.errore.ErroreCEC;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AnnullaTipoOperazioneDiCassaService extends CheckedAccountBaseService<AnnullaTipoOperazioneDiCassa, AnnullaTipoOperazioneDiCassaResponse> {
		
	@Autowired 
	private TipoOperazioneDiCassaDad tipoOperazioneDiCassaDad;
	
	private TipoOperazioneCassa tipoOperazioneCassa;
	
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		tipoOperazioneCassa = req.getTipoOperazioneCassa();
		
		checkEntita(req.getBilancio(), "bilancio", false);
		checkEntita(tipoOperazioneCassa, "tipo operazione di cassa");
	}
	
	@Override
	protected void init() {
		tipoOperazioneDiCassaDad.setEnte(ente);
		tipoOperazioneDiCassaDad.setLoginOperazione(loginOperazione);
	}
	
	

	@Override
	@Transactional
	public AnnullaTipoOperazioneDiCassaResponse executeService(AnnullaTipoOperazioneDiCassa serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		checkTipoOperazioneGiaAnnullato();
		checkNonAssociatoAOperazioneCassa();
		tipoOperazioneDiCassaDad.annullaTipoOperazioneDiCassa(tipoOperazioneCassa);
		tipoOperazioneCassa.setStatoOperativoTipoOperazioneCassa(StatoOperativoTipoOperazioneCassa.ANNULLATO);
		res.setTipoOperazioneCassa(tipoOperazioneCassa);
	}

	private void checkTipoOperazioneGiaAnnullato() {
		TipoOperazioneCassa tipo = tipoOperazioneDiCassaDad.ricercaDettaglioTipoOperazioneDiCassa(tipoOperazioneCassa.getUid());
		if(StatoOperativoTipoOperazioneCassa.ANNULLATO.equals(tipo.getStatoOperativoTipoOperazioneCassa())){
			throw new BusinessException(ErroreCEC.CEC_ERR_0003.getErrore());
		}
	}

	/**
	 * &Eacute; annullabile se l'elemento non &eacute; associato a nessuna operazione di cassa valida per la cassa in elaborazione e per l'anno di bilancio.
	 * In questo caso visualizzare il messaggio <code>&lt;CEC_ERR_026, Tipo che non si pu&ograve; annullare perch&eacute; associato ad un'operazione di cassa&gt;</code>.
	 */
	private void checkNonAssociatoAOperazioneCassa() {
		final String methodName = "checkNonAssociatoAOperazioneCassa";
		Long numeroOperazioniDiCassaValide = tipoOperazioneDiCassaDad.countNumeroOperazioniCassaValidePerTipoOperazioneBilancio(tipoOperazioneCassa, req.getBilancio());
		log.debug(methodName, "Numero di operazioni di cassa valide associate al tipo di operazione di cassa " + tipoOperazioneCassa.getUid() + ": " + numeroOperazioniDiCassaValide);
		
		if(numeroOperazioniDiCassaValide.longValue() > 0L) {
			throw new BusinessException(ErroreCEC.CEC_ERR_0026.getErrore());
		}
	}
	
	
}
