/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.helper;

import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacDBilElemCategoria;
import it.csi.siac.siacbilser.integration.entity.SiacRBilElemClass;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.helper.base.SiacTEnteBaseHelper;
import it.csi.siac.siaccommon.util.collections.Function;
import it.csi.siac.siaccommonser.util.entity.EntityUtil;

@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SiacTBilElemHelper extends SiacTEnteBaseHelper {

	public List<SiacTClass> getSiacTClassList(SiacTBilElem obj) {
		try {
			return EntityUtil.getAllValidMappedBy(obj.getSiacRBilElemClasses(), new Function<SiacRBilElemClass, SiacTClass>(){
				@Override
				public SiacTClass map(SiacRBilElemClass s) {
					return s.getSiacTClass();
				}}
			);
		}
		catch (NullPointerException npe) {
			return null;
		}
	}
	
	public SiacDBilElemCategoria getSiacDBilElemCategoria(SiacTBilElem obj) {
		try {
			return EntityUtil.findFirstValid(obj.getSiacRBilElemCategorias()).getSiacDBilElemCategoria();
		}
		catch (NullPointerException npe) {
			return null;
		}
	}
}
