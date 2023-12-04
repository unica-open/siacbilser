/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.BeanWrapper;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRBilElemAttr;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTAttrEnum;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;

/**
 * The Class AttributiBilancioAttrConverter.
 */
@Component
public class CapitoloEntrataGestioneAttrConverter extends BaseAttrConverter<CapitoloEntrataGestione, SiacTBilElem, SiacRBilElemAttr> {
	
	/**
	 * Instantiates a new attributi bilancio attr converter.
	 */
	public CapitoloEntrataGestioneAttrConverter() {
		super(CapitoloEntrataGestione.class, SiacTBilElem.class);
	}

	@Override
	public CapitoloEntrataGestione convertFrom(SiacTBilElem src, CapitoloEntrataGestione dest) {
		final String methodName = "convertFrom";
		BeanWrapper bw = createBeanWrapper(dest, false);
		
		log.debug(methodName, "numero attributi: " + src.getSiacRBilElemAttrs().size());
		
		StringBuilder logMsgSet = new StringBuilder("Set:  "); 
		StringBuilder logMsgSkipped = new StringBuilder("Saltati attrCode:  ");
		for (SiacRBilElemAttr rBilAttr : src.getSiacRBilElemAttrs()) {
			readAttribute(rBilAttr, bw, logMsgSet, logMsgSkipped);
		}
		logMsgSet.delete(logMsgSet.length() - 2, logMsgSet.length() - 1);
		logMsgSkipped.delete(logMsgSkipped.length() - 2, logMsgSkipped.length() - 1);
		log.info(methodName, logMsgSet.toString());
		log.debug(methodName, logMsgSkipped.toString());
		
		log.debug(methodName, "fine");
		
		return dest;
	}
	
	
	@Override
	public SiacTBilElem convertTo(CapitoloEntrataGestione src, SiacTBilElem dest) {
		Map<String, Object> attrs = SiacTAttrEnum.getFieldAttrNameValueMapByType(src);
		List<SiacRBilElemAttr> siacRBilElemAttrs = new ArrayList<SiacRBilElemAttr>();
		
		for (Entry<String, Object> entry : attrs.entrySet()) {
			SiacRBilElemAttr siacRBilAttr = new SiacRBilElemAttr();
			writeAttribute(entry, siacRBilAttr, dest.getSiacTEnteProprietario().getUid(), dest.getLoginOperazione(), dest, siacRBilElemAttrs);
		}
		
		dest.setSiacRBilElemAttrs(siacRBilElemAttrs);
		
		return dest;
	}
	
}
