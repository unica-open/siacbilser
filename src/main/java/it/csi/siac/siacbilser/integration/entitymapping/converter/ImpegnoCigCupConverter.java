/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.Iterator;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRMovgestTsAttr;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgest;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgestT;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTAttrEnum;
import it.csi.siac.siacfinser.model.Impegno;

/**
 * The Class ImpegnoCigConverter.
 */
@Component
public class ImpegnoCigCupConverter extends ExtendedDozerConverter<Impegno, SiacTMovgest> {

	/**
	 * Instantiates a new impegno - cig converter.
	 */
	public ImpegnoCigCupConverter() {
		super(Impegno.class, SiacTMovgest.class);
	}
	@Override
	public Impegno convertFrom(SiacTMovgest src, Impegno dest) {
		SiacTMovgestT siacTMovgestT = getSiacTMovgestT(src);
		if(siacTMovgestT == null || siacTMovgestT.getSiacRMovgestTsAttrs() == null || dest == null){
			return dest;
		}
		populateCigCup(siacTMovgestT.getSiacRMovgestTsAttrs(), dest);
		return dest;
	}
	
	private void populateCigCup(Iterable<SiacRMovgestTsAttr> siacTMovgestTsAttrs, Impegno dest) {
		boolean cigPopulated = false;
		boolean cupPopulated = false;
		for(Iterator<SiacRMovgestTsAttr> it = siacTMovgestTsAttrs.iterator(); it.hasNext() && !cigPopulated && !cupPopulated;) {
			SiacRMovgestTsAttr rmta = it.next();
			if(rmta.getDataCancellazione() == null && rmta.getSiacTAttr() != null && SiacTAttrEnum.Cig.getCodice().equals(rmta.getSiacTAttr().getAttrCode())) {
				dest.setCig(rmta.getTesto());
				cigPopulated = true;
			} else if(rmta.getDataCancellazione() == null && rmta.getSiacTAttr() != null && SiacTAttrEnum.Cup.getCodice().equals(rmta.getSiacTAttr().getAttrCode())) {
				dest.setCup(rmta.getTesto());
				cupPopulated = true;
			}
		}
	}

	/**
	 * Trova il ts corrispondente alla testata
	 * @param src la testata
	 * @return il ts
	 */
	private SiacTMovgestT getSiacTMovgestT(SiacTMovgest src) {
		if(src == null || src.getSiacTMovgestTs() == null) {
			return null;
		}
		for(SiacTMovgestT siacTMovgestT : src.getSiacTMovgestTs()){
			if(siacTMovgestT.getDataCancellazione() == null && "T".equals(siacTMovgestT.getSiacDMovgestTsTipo().getMovgestTsTipoCode())) {
				return siacTMovgestT;
			}
		}
		return null;
	}
	
	@Override
	public SiacTMovgest convertTo(Impegno src, SiacTMovgest dest) {
		return dest;
	}

}
