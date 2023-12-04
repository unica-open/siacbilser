/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.consultazioneentita;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacconsultazioneentitaser.frontend.webservice.msg.OttieniNavigazioneTipoEntitaConsultabile;
import it.csi.siac.siacconsultazioneentitaser.frontend.webservice.msg.OttieniNavigazioneTipoEntitaConsultabileResponse;
import it.csi.siac.siacconsultazioneentitaser.model.TipoEntitaConsultabile;

/**
 * Ottiene la navigazione a partere da un {@link TipoEntitaConsultabile}.
 * Attualmente l'implementazione e' statica e basata su {@link NavigazioneEntitaConsultabili}.
 * In una prossima release questa configurazione puo' essere reperita da DB.
 * 
 * @author Domenico
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class OttieniNavigazioneTipoEntitaConsultabileService extends CheckedAccountBaseService<OttieniNavigazioneTipoEntitaConsultabile, OttieniNavigazioneTipoEntitaConsultabileResponse> {

	
	@Transactional(readOnly=true)
	public OttieniNavigazioneTipoEntitaConsultabileResponse executeService(OttieniNavigazioneTipoEntitaConsultabile serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	
	@Override
	protected void execute() {
		
		NavigazioneEntitaConsultabili nec = NavigazioneEntitaConsultabili.byTipoEntitaConsultabile(req.getTipoEntitaConsultabile());
		res.setEntitaConsultabili(nec.getTipoEntitaConsultabiliList());
		res.setParent(nec.isParent());
		
	}

}
