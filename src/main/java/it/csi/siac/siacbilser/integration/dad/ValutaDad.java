/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.SiacDValutaRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDValuta;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siaccommonser.integration.dad.base.BaseDadImpl;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siacfin2ser.model.Valuta;

/**
 * Data access delegate di una cassa economale
 *
 * 
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class ValutaDad extends BaseDadImpl {
	
	@Autowired
	private SiacDValutaRepository siacDValutaRepository;
	
	private Ente ente;
	
	/**
	 * Sets the ente.
	 *
	 * @param ente the ente to set
	 */
	public void setEnte(Ente ente) {
		this.ente = ente;
	}
	
	public List<Valuta> findByCodice(String codice) {
		List<SiacDValuta> siacDValutas = siacDValutaRepository.findByEnteProprietarioIdAndValutaCode(ente.getUid(), codice);
		return convertiLista(siacDValutas, Valuta.class, BilMapId.SiacDValuta_Valuta);
	}
}
