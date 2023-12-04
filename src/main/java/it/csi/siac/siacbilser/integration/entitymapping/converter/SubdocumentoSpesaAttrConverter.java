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

import it.csi.siac.siacbilser.integration.entity.SiacRSubdocAttr;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTAttrEnum;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;

/**
 * The Class SubdocumentoSpesaAttrConverter.
 */
@Component
public class SubdocumentoSpesaAttrConverter extends BaseAttrConverter<SubdocumentoSpesa, SiacTSubdoc, SiacRSubdocAttr> {
	/**
	 * Instantiates a new subdocumento spesa attr converter.
	 */
	public SubdocumentoSpesaAttrConverter() {
		super(SubdocumentoSpesa.class, SiacTSubdoc.class);
	}

	@Override
	public SubdocumentoSpesa convertFrom(SiacTSubdoc src, SubdocumentoSpesa dest) {
		final String methodName = "convertFrom";
		BeanWrapper bw = createBeanWrapper(dest, true);
		
		log.debug(methodName, "numero attributi: " + src.getSiacRSubdocAttrs().size());
		
		StringBuilder logMsgSet = new StringBuilder("Set:  "); 
		StringBuilder logMsgSkipped = new StringBuilder("Saltati attrCode:  ");
		for (SiacRSubdocAttr rAttr : src.getSiacRSubdocAttrs()) {
			readAttribute(rAttr, bw, logMsgSet, logMsgSkipped, "datiCertificazioneCrediti");
		}
		
		logMsgSet.delete(logMsgSet.length() - 2, logMsgSet.length() - 1);
		logMsgSkipped.delete(logMsgSkipped.length() - 2, logMsgSkipped.length() - 1);
		log.info(methodName, logMsgSet.toString());
		log.debug(methodName, logMsgSkipped.toString());
		
		log.debug(methodName, "fine");
		
		return dest;
	}
	
	@Override
	public SiacTSubdoc convertTo(SubdocumentoSpesa src, SiacTSubdoc dest) {
		Map<String, Object> attrs = SiacTAttrEnum.getFieldAttrNameValueMapByType(src);
		
		if(src.getDatiCertificazioneCrediti()!=null){
			Map<String, Object> attrs2 = SiacTAttrEnum.getFieldAttrNameValueMapByType(src.getDatiCertificazioneCrediti());
			attrs.putAll(attrs2);
		}
		
		List<SiacRSubdocAttr> siacRBilAttrs = new ArrayList<SiacRSubdocAttr>();
		
		for (Entry<String, Object> entry : attrs.entrySet()) {
			SiacRSubdocAttr siacRBilAttr = new SiacRSubdocAttr();
			writeAttribute(entry, siacRBilAttr, dest.getSiacTEnteProprietario().getUid(), dest.getLoginOperazione(), dest, siacRBilAttrs);
		}
		
		dest.setSiacRSubdocAttrs(siacRBilAttrs);
		
		return dest;
	}
		
	
	
	
}
