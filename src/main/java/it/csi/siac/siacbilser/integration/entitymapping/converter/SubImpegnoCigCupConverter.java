/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.Iterator;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRMovgestTsAttr;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgestT;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTAttrEnum;
import it.csi.siac.siacfinser.model.SubImpegno;

/**
 * The Class SubImpegnoCigConverter.
 */
@Component
public class SubImpegnoCigCupConverter extends ExtendedDozerConverter<SubImpegno, SiacTMovgestT> {

	/**
	 * Instantiates a new sub impegno - cig converter.
	 */
	public SubImpegnoCigCupConverter() {
		super(SubImpegno.class, SiacTMovgestT.class);
	}
	@Override
	public SubImpegno convertFrom(SiacTMovgestT src, SubImpegno dest) {
		if(src == null || src.getSiacRMovgestTsAttrs() == null || dest == null){
			return dest;
		}
		populateCigCup(src.getSiacRMovgestTsAttrs(), dest);
		return dest;
	}
	
	private void populateCigCup(Iterable<SiacRMovgestTsAttr> siacTMovgestTsAttrs, SubImpegno dest) {
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
	
	@Override
	public SiacTMovgestT convertTo(SubImpegno src, SiacTMovgestT dest) {
		return dest;
	}

}
