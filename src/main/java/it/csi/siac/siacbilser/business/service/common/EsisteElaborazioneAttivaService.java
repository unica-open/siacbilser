/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.utility.ElaborazioniAttiveKeyHandler;
import it.csi.siac.siacbilser.frontend.webservice.msg.ElaborazioneWrapper;
import it.csi.siac.siacbilser.frontend.webservice.msg.EsisteElaborazioneAttiva;
import it.csi.siac.siacbilser.frontend.webservice.msg.EsisteElaborazioneAttivaResponse;
import it.csi.siac.siacbilser.integration.dad.ElaborazioniDad;
import it.csi.siac.siacbilser.model.ElabKeys;
import it.csi.siac.siacbilser.model.ElabKeysMapper;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * @author AR
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EsisteElaborazioneAttivaService
		extends CheckedAccountBaseService<EsisteElaborazioneAttiva, EsisteElaborazioneAttivaResponse> {

	@Autowired
	private ElaborazioniDad elaborazioniDad;

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getElaborazioni(), "elaborazioni");

		for (int i = 0; i < req.getElaborazioni().length; i++) {
			ElaborazioneWrapper ew = req.getElaborazioni()[i];
			checkCondition(ew.getUid() != null && ew.getUid() > 0, 
					ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid, index: " + i));
		}
	}

	@Override
	protected void init() {
		super.init();
		elaborazioniDad.setEnte(ente);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public EsisteElaborazioneAttivaResponse executeService(EsisteElaborazioneAttiva serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {

		for (ElaborazioneWrapper ew : req.getElaborazioni()) {
			
			ElaborazioniAttiveKeyHandler eakh = new ElaborazioniAttiveKeyHandler(
					ew.getUid(),
					ew.getClasseChiamante(), 
					ew.getClasseEntitaInElaborazione(), 
					ew.getAmbito() != null ? ew.getAmbito().getSuffix() : null);
			
			ElabKeysMapper ekm = ElabKeysMapper.valueOf(ew.getElaborazione());
			
			for (ElabKeys ek : ekm.getElabKeysList()) {
				
				String elabService = eakh.creaElabServiceFromPattern(ek);
				String elabKey = eakh.creaElabKeyFromPattern(ek);
				
				if (elaborazioniDad.esisteElaborazioneAttiva(elabService, elabKey)) {
					res.setEsisteElaborazioneAttiva(true);
					return;
				}
			}
		}
		
		res.setEsisteElaborazioneAttiva(false);
	}

}
