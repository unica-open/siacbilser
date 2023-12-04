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
import it.csi.siac.siacbilser.integration.dad.TipoGiustificativoDad;
import it.csi.siac.siaccecser.frontend.webservice.msg.AnnullaTipoGiustificativo;
import it.csi.siac.siaccecser.frontend.webservice.msg.AnnullaTipoGiustificativoResponse;
import it.csi.siac.siaccecser.model.StatoOperativoTipoGiustificativo;
import it.csi.siac.siaccecser.model.TipoGiustificativo;
import it.csi.siac.siaccecser.model.errore.ErroreCEC;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;

// TODO: Auto-generated Javadoc
/**
 * Inserimento dell'anagrafica del Documento di Entrata .
 *
 * @author Domenico
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AnnullaTipoGiustificativoService extends CheckedAccountBaseService<AnnullaTipoGiustificativo, AnnullaTipoGiustificativoResponse> {
		
	@Autowired
	private TipoGiustificativoDad tipoGiustificativoDad;
	private TipoGiustificativo tipoGiustificativo;
	
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		checkEntita(req.getBilancio(), "bilancio", false);
		checkEntita(req.getTipoGiustificativo(), "tipo giustificativo");
		tipoGiustificativo = req.getTipoGiustificativo();
		
	}
	
	@Override
	protected void init() {
		tipoGiustificativoDad.setEnte(ente);
		tipoGiustificativoDad.setLoginOperazione(loginOperazione);
	}
	
	@Override
	@Transactional
	public AnnullaTipoGiustificativoResponse executeService(AnnullaTipoGiustificativo serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		
		checkTipoGiustificativoGiaAnnullato();
		checkNonAssociatoARichiestaValida();
		tipoGiustificativoDad.annullaTipoGiustificativo(tipoGiustificativo);
		tipoGiustificativo.setStatoOperativoTipoGiustificativo(StatoOperativoTipoGiustificativo.ANNULLATO);
		res.setTipoGiustificativo(tipoGiustificativo);
	}

	private void checkTipoGiustificativoGiaAnnullato() {
		TipoGiustificativo tipo = tipoGiustificativoDad.ricercaDettaglioTipoGiustificativo(tipoGiustificativo.getUid());
		if(StatoOperativoTipoGiustificativo.ANNULLATO.equals(tipo.getStatoOperativoTipoGiustificativo())){
			throw new BusinessException(ErroreCEC.CEC_ERR_0002.getErrore());
		}
	}

	/**
	 * &Eacute; annullabile se l'elemento non &eacute; associato a nessuna richiesta valida per la cassa in elaborazione e per l'anno di bilancio.
	 * In questo caso visualizzare il messaggio <code>&lt;CEC_ERR_027, Tipo che non si pu&ograve; annullare perch&eacute; associato ad una richiesta&gt;</code>.
	 */
	private void checkNonAssociatoARichiestaValida() {
		final String methodName = "checkNonAssociatoARichiestaValida";
		Long numeroRichiesteValide = tipoGiustificativoDad.countNumeroRichiesteValidePerTipoGiustificativoBilancio(tipoGiustificativo, req.getBilancio());
		log.debug(methodName, "Numero di richieste valide associate al tipo giustificativo " + tipoGiustificativo.getUid() + ": " + numeroRichiesteValide);
		
		if(numeroRichiesteValide.longValue() > 0L) {
			throw new BusinessException(ErroreCEC.CEC_ERR_0027.getErrore());
		}
	}
	
}
