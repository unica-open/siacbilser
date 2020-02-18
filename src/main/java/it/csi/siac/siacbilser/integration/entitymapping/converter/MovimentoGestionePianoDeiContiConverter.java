/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacTMovgestTRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgest;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siacfinser.model.MovimentoGestione;

/**
 * The Class MovimentoGestionePianoDeiContiConverter.
 */
@Component
public class MovimentoGestionePianoDeiContiConverter extends ExtendedDozerConverter<MovimentoGestione, SiacTMovgest> {

	@Autowired
	private SiacTMovgestTRepository siacTMovgestTRepository;
	
	/**
	 * Instantiates a new movimento gestione (testata / sub) - piano dei conti converter.
	 */
	public MovimentoGestionePianoDeiContiConverter() {
		super(MovimentoGestione.class, SiacTMovgest.class);
	}
	@Override
	public MovimentoGestione convertFrom(SiacTMovgest src, MovimentoGestione dest) {
		if(src.getSiacTMovgestTs() == null) {
			return dest;
		}
		aggiungiInformazioniPianoDeiConti(src, dest);
		return dest;
	}
	
	/**
	 * Aggiunge le informazioni del piano dei conti nel movimento di gestione
	 * @param src le testate/sub
	 * @param dest il movimento di gestione
	 */
	private void aggiungiInformazioniPianoDeiConti(SiacTMovgest src, MovimentoGestione dest) {
		List<String> listaCodici = Arrays.asList(TipologiaClassificatore.PDC.name(), TipologiaClassificatore.PDC_I.name(), TipologiaClassificatore.PDC_II.name(),
				TipologiaClassificatore.PDC_III.name(), TipologiaClassificatore.PDC_IV.name(), TipologiaClassificatore.PDC_V.name());
		List<SiacTClass> siacTClasses = siacTMovgestTRepository.findSiacTClassByMovGestIdECodiciTipo(src.getUid(), listaCodici);
		if(siacTClasses == null || siacTClasses.isEmpty()) {
			log.debug("aggiungiInformazioniPianoDeiConti", "Non sono stati trovati classificatori per il movimento con uid: " + src.getUid());
			return;
		}
		// Prendo il primo
		SiacTClass siacTClass = siacTClasses.get(0);
		dest.setIdPdc(siacTClass.getUid());
		dest.setCodPdc(siacTClass.getClassifCode());
		dest.setDescPdc(siacTClass.getClassifDesc());
	}
	
	@Override
	public SiacTMovgest convertTo(MovimentoGestione src, SiacTMovgest dest) {
		return dest;
	}

}
