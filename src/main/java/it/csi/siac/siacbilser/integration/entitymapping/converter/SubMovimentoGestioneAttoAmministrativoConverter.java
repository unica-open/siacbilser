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
import it.csi.siac.siacbilser.integration.entity.SiacTMovgestT;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfinser.model.MovimentoGestione;

/**
 * The Class SubMovimentoGestioneAttoAmministrativoConverter.
 */
@Component
public class SubMovimentoGestioneAttoAmministrativoConverter extends ExtendedDozerConverter<MovimentoGestione, SiacTMovgestT> {
	@Autowired
	private SiacTMovgestTRepository siacTMovgestTRepository;
	
	/**
	 * Instantiates a new sub movimento gestione - atto amministrativo converter.
	 */
	public SubMovimentoGestioneAttoAmministrativoConverter() {
		super(MovimentoGestione.class, SiacTMovgestT.class);
	}
	@Override
	public MovimentoGestione convertFrom(SiacTMovgestT src, MovimentoGestione dest) {
		
		
		
		List<SiacTAttoAmm> siacTAttoAmms = siacTMovgestTRepository.findSiacTAttoAmmByMovgestTsId(src.getUid());
		if(siacTAttoAmms != null && !siacTAttoAmms.isEmpty()) {
			AttoAmministrativo attoAmministrativo = map(siacTAttoAmms.get(0), AttoAmministrativo.class, BilMapId.SiacTAttoAmm_AttoAmministrativo);
			dest.setAttoAmministrativo(attoAmministrativo);
		}
		
		return dest;
	}

		@Override
		public SiacTMovgestT convertTo(MovimentoGestione src, SiacTMovgestT dest) {
			return dest;
		}

}
