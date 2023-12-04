/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.custom.stilo.business.service.provvedimento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.custom.stilo.integration.dad.AttoAmministrativoStiloDad;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.StatoOperativoAtti;
import it.csi.siac.siacbilser.business.service.provvedimento.AggiornaProvvedimentoService;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaProvvedimentoStiloService extends AggiornaProvvedimentoService {
	
	@Autowired
	private AttoAmministrativoStiloDad attoAmministrativoStiloDad;

	
	@Override
	protected void init() {
		
		super.init();
		
		attoAmministrativoStiloDad.setLoginOperazione(loginOperazione);
		attoAmministrativoStiloDad.setEnte(req.getEnte());
	}
	
	@Override
	protected Boolean isAnnullabile() {
		return attoAmministrativoStiloDad.isAnnullabile(req.getAttoAmministrativo());
	}

	
	
	@Override
	protected void effettuaOperazioniCollegateCambioStatoProvvedimento(AttoAmministrativo attoAmministrativoAggiornato) {
		
		if (StatoOperativoAtti.ANNULLATO.equals(req.getAttoAmministrativo().getStatoOperativoAtti())) {
			attoAmministrativoStiloDad.annullaMovimentiGestioneCollegatiAllAttoAmm(req.getAttoAmministrativo());
		}
		
		super.effettuaOperazioniCollegateCambioStatoProvvedimento(attoAmministrativoAggiornato);
	}
}
