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

import it.csi.siac.siacbilser.integration.entity.SiacRBilAttr;
import it.csi.siac.siacbilser.integration.entity.SiacTBil;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTAttrEnum;
import it.csi.siac.siacbilser.model.AttributiBilancio;

/**
 * The Class AttributiBilancioAttrConverter.
 */
@Component
public class AttributiBilancioAttrConverter extends BaseAttrConverter<AttributiBilancio, SiacTBil, SiacRBilAttr> {
	
	/**
	 * Instantiates a new attributi bilancio attr converter.
	 */
	public AttributiBilancioAttrConverter() {
		super(AttributiBilancio.class, SiacTBil.class);
	}

	@Override
	public AttributiBilancio convertFrom(SiacTBil src, AttributiBilancio dest) {
		final String methodName = "convertFrom";
		BeanWrapper bw = createBeanWrapper(dest, false);
		
		log.debug(methodName, "numero attributi: " + src.getSiacRBilAttrs().size());
		
		StringBuilder logMsgSet = new StringBuilder("Set:  "); 
		StringBuilder logMsgSkipped = new StringBuilder("Saltati attrCode:  ");
		for (SiacRBilAttr rBilAttr : src.getSiacRBilAttrs()) {
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
	public SiacTBil convertTo(AttributiBilancio src, SiacTBil dest) {
		Map<String, Object> attrs = SiacTAttrEnum.getFieldAttrNameValueMapByType(src);
		List<SiacRBilAttr> siacRBilAttrs = new ArrayList<SiacRBilAttr>();
		
		for (Entry<String, Object> entry : attrs.entrySet()) {
			SiacRBilAttr siacRBilAttr = new SiacRBilAttr();
			writeAttribute(entry, siacRBilAttr, dest.getSiacTEnteProprietario().getUid(), dest.getLoginOperazione(), dest, siacRBilAttrs);
		}
		
		dest.setSiacRBilAttrs(siacRBilAttrs);
		
		return dest;
	}
	
}
