/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.custom.oopp.integration.dad.mapper;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siaccommon.util.mapper.BaseMapper;
import it.csi.siac.siacintegser.model.custom.oopp.DocumentoSpesa;

@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SiacTSubdocDocumentoSpesaMapper extends BaseMapper<SiacTSubdoc, DocumentoSpesa> {

	@Override
	public void map(SiacTSubdoc o1, DocumentoSpesa o2) {
		if (o1 == null || o2 == null) {
			return;
		}

		
	}
}
