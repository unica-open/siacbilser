/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.gruppoattivitaiva;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.ProrataEChiusuraGruppoIvaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceProrataEChiusuraGruppoIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceProrataEChiusuraGruppoIvaResponse;
import it.csi.siac.siacfin2ser.model.ProRataEChiusuraGruppoIva;

// TODO: Auto-generated Javadoc
/**
 * The Class InserisceProrataEChiusuraGruppoIvaService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceProrataEChiusuraGruppoIvaService extends CheckedAccountBaseService<InserisceProrataEChiusuraGruppoIva, InserisceProrataEChiusuraGruppoIvaResponse> {

	/** The prorata e chiusura gruppo iva dad. */
	@Autowired
	private ProrataEChiusuraGruppoIvaDad prorataEChiusuraGruppoIvaDad;
	
	/** The prorata. */
	private ProRataEChiusuraGruppoIva prorata;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		prorata = req.getProRataEChiusuraGruppoIva();
		
		checkNotNull(req.getRichiedente(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("richiedente"));
		
		checkNotNull(prorata, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("prorata gruppo Iva"));
		checkNotNull(prorata.getAnnoEsercizio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno esercizio prorata gruppo Iva"));
		checkNotNull(prorata.getPercentualeProRata(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("percentuale prorata gruppo Iva"));
		
		checkNotNull(prorata.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		checkCondition(prorata.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente"));
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		prorataEChiusuraGruppoIvaDad.setLoginOperazione(loginOperazione);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional
	@Override
	public InserisceProrataEChiusuraGruppoIvaResponse executeService(InserisceProrataEChiusuraGruppoIva serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		prorataEChiusuraGruppoIvaDad.inserisciProrataEChiusuraGruppoIva(prorata);
		res.setProRataEChiusuraGruppoIva(prorata);
	}

	
}
