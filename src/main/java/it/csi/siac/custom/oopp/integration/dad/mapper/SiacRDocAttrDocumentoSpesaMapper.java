/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.custom.oopp.integration.dad.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRDocAttr;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTAttrEnum;
import it.csi.siac.siacbilser.integration.entity.helper.SiacTAttrHelper;
import it.csi.siac.siaccommon.util.mapper.BaseMapper;
import it.csi.siac.siacintegser.model.custom.oopp.DocumentoSpesa;

@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
 class SiacRDocAttrDocumentoSpesaMapper extends BaseMapper<SiacRDocAttr, DocumentoSpesa> {

	
	@Autowired private SiacTAttrHelper siacTAttrHelper;
	
	@Override
	public void map(SiacRDocAttr o1, DocumentoSpesa o2) {
		if (o1 == null || o2 == null) {
			return;
		}
		
		if (siacTAttrHelper.checkAttrCode(o1.getSiacTAttr(), SiacTAttrEnum.Arrotondamento)) {
			o2.setArrotondamento(o1.getNumerico());
			return;
		}

		if (siacTAttrHelper.checkAttrCode(o1.getSiacTAttr(), SiacTAttrEnum.NumeroRepertorio)) {
			o2.setNumeroRepertorio(o1.getTesto());
			return;
		}

		if (siacTAttrHelper.checkAttrCode(o1.getSiacTAttr(), SiacTAttrEnum.RegistroRepertorio)) {
			o2.setRegistroRepertorio(o1.getTesto());
			return;
		}
	}

}
