/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.integration.dao.SiacTMovgestTRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTAttoAmm;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgest;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgestT;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDMovgestTsTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfinser.model.MovimentoGestione;

/**
 * The Class MovimentoGestioneAttoAmministrativoConverter.
 */
@Component
public class MovimentoGestioneAttoAmministrativoConverter extends ExtendedDozerConverter<MovimentoGestione, SiacTMovgest> {
	@Autowired
	private SiacTMovgestTRepository siacTMovgestTRepository;
	
	/**
	 * Instantiates a new movimento gestione - atto amministrativo converter.
	 */
	public MovimentoGestioneAttoAmministrativoConverter() {
		super(MovimentoGestione.class, SiacTMovgest.class);
	}
	@Override
	public MovimentoGestione convertFrom(SiacTMovgest src, MovimentoGestione dest) {
		
		//siacTMovgestTRepository.findSiacTMovgestTestataBySiacTMovgestId(src.getUid());
		SiacTMovgestT siacTMovgestT = ottieniTestata(src);
		
		List<SiacTAttoAmm> siacTAttoAmms = siacTMovgestTRepository.findSiacTAttoAmmByMovgestTsId(siacTMovgestT.getUid());
		if(siacTAttoAmms != null && !siacTAttoAmms.isEmpty()) {
			AttoAmministrativo attoAmministrativo = map(siacTAttoAmms.get(0), AttoAmministrativo.class, BilMapId.SiacTAttoAmm_AttoAmministrativo);
			dest.setAttoAmministrativo(attoAmministrativo);
		}
		
		return dest;
	}
	

	private SiacTMovgestT ottieniTestata(SiacTMovgest src) {
		final String methodName = "ottieniTestata";
		for(SiacTMovgestT tmt : src.getSiacTMovgestTs()) {
			if(tmt != null && tmt.getSiacDMovgestTsTipo() != null && SiacDMovgestTsTipoEnum.Testata.getCodice().equals(tmt.getSiacDMovgestTsTipo().getMovgestTsTipoCode())) {
				return tmt;
			}
		}
		log.warn(methodName, "Testata non trovata su SiacTMovgestTs");
		throw new IllegalStateException("Testata non trovata su SiacTMovgestTs per SiacTMovgest [uid: " + src.getUid() + "]");
	}
		
		
		@Override
		public SiacTMovgest convertTo(MovimentoGestione src, SiacTMovgest dest) {
			return dest;
		}

}
