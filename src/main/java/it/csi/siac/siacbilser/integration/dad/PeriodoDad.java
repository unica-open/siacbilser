/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.SiacTPeriodoRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTPeriodo;
import it.csi.siac.siaccommonser.integration.dad.base.BaseDadImpl;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siacfin2ser.model.Periodo;

/**
 * Data access delegate di un Periodo.
 *
 * @author Domenico
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class PeriodoDad extends BaseDadImpl {

	@Autowired
	private SiacTPeriodoRepository siacTPeriodoRepository;
	
	private Ente ente;

	/**
	 * Ottiene l'uid di un Periodo a partire dal suo anno e dal suo tipo
	 * @param periodo
	 * @param anno
	 * @return
	 */
	public Integer findPeriodoUid(Periodo periodo, Integer anno){
		SiacTPeriodo siacTPeriodo = siacTPeriodoRepository.findByAnnoAndPeriodoTipoAndEnteProprietario(""+anno,periodo.getCodice(), ente.getUid());
		if(siacTPeriodo!=null){
			return siacTPeriodo.getUid();
		}
		return null;
	}
	

	/**
	 * Sets the ente.
	 *
	 * @param ente the new ente
	 */
	public void setEnte(Ente ente) {
		this.ente = ente;
	}

}
