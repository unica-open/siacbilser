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

import it.csi.siac.siacbilser.integration.dao.SiacTElabThresholdRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTElabThreshold;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTElabThresholdEnum;
import it.csi.siac.siacbilser.model.ElabThresholdKey;
import it.csi.siac.siaccommonser.integration.dad.base.BaseDadImpl;

/**
 * The Class ThresholdDad.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class ThresholdDad extends BaseDadImpl  {
	
	/** The siac t bil repository. */
	@Autowired
	private SiacTElabThresholdRepository siacTElabThresholdRepository;
	
	/**
	 * Ottiene la soglia via la chiave
	 * @param key la chiave della soglia
	 * @return la soglia, se presente; null altrimenti
	 */
	public Long findThresholdByCode(ElabThresholdKey key) {
		final String methodName = "findThresholdByCode";
		if(key == null) {
			throw new IllegalArgumentException("La chiave della soglia di elaborazione non puo' essere null");
		}
		
		SiacTElabThresholdEnum siacTElabThresholdEnum = SiacTElabThresholdEnum.byElabThresholdKey(key);
		if(siacTElabThresholdEnum == null) {
			log.warn(methodName, "Chiave " + key + " non presente nella tabella di rimappatura delle soglie");
			return null;
		}
		
		List<SiacTElabThreshold> siacTElabThresholds = siacTElabThresholdRepository.findByElthresCode(siacTElabThresholdEnum.getElthresCode());
		if(siacTElabThresholds == null || siacTElabThresholds.isEmpty()) {
			log.info(methodName, "Nessuna soglia impostata per la chiave " + key + " (codice su base dati: " + siacTElabThresholdEnum.getElthresCode() + ")");
			return null;
		}
		log.debug(methodName, "Presente almeno una soglia su base dati: la restituisco");
		
		return siacTElabThresholds.get(0).getElthresValue();
	}
}
