/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.helper;

import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRSubdocAttr;
import it.csi.siac.siacbilser.integration.entity.SiacRSubdocOrdinativoT;
import it.csi.siac.siacbilser.integration.entity.SiacTOrdinativo;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siacbilser.integration.entity.helper.base.SiacTEnteBaseHelper;
import it.csi.siac.siaccommon.util.collections.Function;
import it.csi.siac.siaccommonser.util.entity.EntityUtil;

@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SiacTSubdocHelper extends SiacTEnteBaseHelper {

	public List<SiacTOrdinativo> getSiacTOrdinativoList(SiacTSubdoc siacTSubdoc) {
		try {
			return EntityUtil.getAllValidMappedBy(siacTSubdoc.getSiacRSubdocOrdinativoTs(), new Function<SiacRSubdocOrdinativoT, SiacTOrdinativo>(){

				@Override
				public SiacTOrdinativo map(SiacRSubdocOrdinativoT source) {
					return source.getSiacTOrdinativoT().getSiacTOrdinativo();
				}}
			);
		}
		catch (NullPointerException npe) {
			return null;
		}
	}

	public List<SiacRSubdocAttr> getSiacRSubdocAttrList(SiacTSubdoc siacTSubdoc) {
		try {
			return EntityUtil.getAllValid(siacTSubdoc.getSiacRSubdocAttrs());
		}
		catch (NullPointerException npe) {
			return null;
		}
	}
}
