/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.provvedimento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.frontend.webservice.msg.RicercaSinteticaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaSinteticaProvvedimentoResponse;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.AttoAmministrativoDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSinteticaProvvedimentoService extends CheckedAccountBaseService<RicercaSinteticaProvvedimento, RicercaSinteticaProvvedimentoResponse>{
	/** The provvedimento dad. */
	@Autowired
	private AttoAmministrativoDad attoAmministrativoDad;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		attoAmministrativoDad.setLoginOperazione(loginOperazione);
		attoAmministrativoDad.setEnte(req.getEnte());
	}
	
	 
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getRicercaAtti(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ricerca atti"),true);
		
		if (req.getRicercaAtti().getUid() == null) {
		
			checkNotNull(req.getRicercaAtti().getAnnoAtto(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno atto"),true);
			checkCondition(req.getRicercaAtti().getAnnoAtto() != null && (req.getRicercaAtti().getTipoAtto() != null ||  req.getRicercaAtti().getNumeroAtto() != null), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("quando valorizzato anno atto bisogna valorizzare o il tipo o il numero atto"));
		}
		checkParametriPaginazione(req.getParametriPaginazione());
	}
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional(readOnly=true)
	public RicercaSinteticaProvvedimentoResponse executeService(RicercaSinteticaProvvedimento serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		
		ListaPaginata<AttoAmministrativo> listaAtti = attoAmministrativoDad.ricercaSintetica(req.getRicercaAtti(), req.getParametriPaginazione(), req.getAttoAmministrativoModelDetail());
		
		res.setAttiAmministrativi(listaAtti);
		
		res.setEsito(Esito.SUCCESSO); //di default è già SUCCESSO!
	}

	/**
	 * Gets the provvedimento dad.
	 *
	 * @return the provvedimento dad
	 */
	public AttoAmministrativoDad getAttoAmministrativoDad() {
		return attoAmministrativoDad;
	}

	/**
	 * Sets the provvedimento dad.
	 *
	 * @param attoAmministrativoDad the new provvedimento dad
	 */
	public void setAttoAmministrativoDad(AttoAmministrativoDad attoAmministrativoDad) {
		this.attoAmministrativoDad = attoAmministrativoDad;
	}
}
