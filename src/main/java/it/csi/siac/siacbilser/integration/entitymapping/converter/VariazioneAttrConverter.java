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

import it.csi.siac.siacbilser.integration.entity.SiacRVariazioneAttr;
import it.csi.siac.siacbilser.integration.entity.SiacTVariazione;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTAttrEnum;
import it.csi.siac.siacbilser.model.VariazioneDiBilancio;

/**
 * The Class VariazioneAttrConverter.
 */
@Component
public class VariazioneAttrConverter extends BaseAttrConverter<VariazioneDiBilancio, SiacTVariazione, SiacRVariazioneAttr> {
	
	/**
	 * Instantiates a new variazione importi attr converter.
	 */
	public VariazioneAttrConverter() {
		super(VariazioneDiBilancio.class, SiacTVariazione.class);
	}

	@Override
	public VariazioneDiBilancio convertFrom(SiacTVariazione src, VariazioneDiBilancio dest) {
		final String methodName = "convertFrom";
		BeanWrapper bw = createBeanWrapper(dest, true);
		
		StringBuilder logMsgSet = new StringBuilder("Set:  "); 
		StringBuilder logMsgSkipped = new StringBuilder("Saltati attrCode:  ");
		for (SiacRVariazioneAttr rAttr : src.getSiacRVariazioneAttrs()) {
			readAttribute(rAttr, bw, logMsgSet, logMsgSkipped);
		}
		
		logMsgSet.delete(logMsgSet.length() - 2, logMsgSet.length() - 1);
		logMsgSkipped.delete(logMsgSkipped.length() - 2, logMsgSkipped.length() - 1);
		log.info(methodName, logMsgSet.toString());
		log.debug(methodName, logMsgSkipped.toString());
		
		log.debug(methodName, "fine");
		
		return dest;
	}

	@Override
	public SiacTVariazione convertTo(VariazioneDiBilancio src, SiacTVariazione dest) {
		Map<String, Object> attrs = SiacTAttrEnum.getFieldAttrNameValueMapByType(src);
		
		List<SiacRVariazioneAttr> rAttrs = new ArrayList<SiacRVariazioneAttr>();
		
		for (Entry<String, Object> entry : attrs.entrySet()) {
			SiacRVariazioneAttr rAttr = new SiacRVariazioneAttr();
			writeAttribute(entry, rAttr, dest.getSiacTEnteProprietario().getUid(), dest.getLoginOperazione(), dest, rAttrs);
		}
		
		dest.setSiacRVariazioneAttrs(rAttrs);
		
		return dest;
	}

}
