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
import it.csi.siac.siacbilser.integration.entity.SiacTMovgestT;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siacfinser.model.MovimentoGestione;

/**
 * The Class MovimentoGestioneTestataSubPianoDeiContiConverter.
 */
@Component
public class MovimentoGestioneTestataSubPianoDeiContiConverter extends ExtendedDozerConverter<MovimentoGestione, SiacTMovgestT> {

	@Autowired
	private SiacTMovgestTRepository siacTMovgestTRepository;
	
	/**
	 * Instantiates a new movimento gestione (testata / sub) - piano dei conti converter.
	 */
	public MovimentoGestioneTestataSubPianoDeiContiConverter() {
		super(MovimentoGestione.class, SiacTMovgestT.class);
	}
	@Override
	public MovimentoGestione convertFrom(SiacTMovgestT src, MovimentoGestione dest) {
		List<String> listaCodici = Arrays.asList(TipologiaClassificatore.PDC.name(), TipologiaClassificatore.PDC_I.name(), TipologiaClassificatore.PDC_II.name(),
				TipologiaClassificatore.PDC_III.name(), TipologiaClassificatore.PDC_IV.name(), TipologiaClassificatore.PDC_V.name());
		List<SiacTClass> siacTClasses = siacTMovgestTRepository.findSiacTClassByMovgestTsIdECodiciTipo(src.getUid(), listaCodici);
		if(siacTClasses == null || siacTClasses.isEmpty()) {
			return dest;
		}
		SiacTClass siacTClass = siacTClasses.get(0);
		dest.setCodPdc(siacTClass.getClassifCode());
		dest.setDescPdc(siacTClass.getClassifDesc());
		
		return dest;
	}
	
	@Override
	public SiacTMovgestT convertTo(MovimentoGestione src, SiacTMovgestT dest) {
		return dest;
	}

}
