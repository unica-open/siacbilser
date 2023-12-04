/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.helper;

import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRClassFamTree;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDClassTipoEnum;
import it.csi.siac.siacbilser.integration.entity.helper.base.SiacTEnteBaseHelper;
import it.csi.siac.siaccommon.util.collections.CollectionUtil;
import it.csi.siac.siaccommon.util.collections.Filter;
import it.csi.siac.siaccommonser.util.entity.EntityUtil;

@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SiacTClassHelper extends SiacTEnteBaseHelper {

	
	public boolean equalsSiacDClassTipo(SiacTClass siacTClass, SiacDClassTipoEnum siacDClassTipo) {
		return siacDClassTipo.equals(SiacDClassTipoEnum.byCodice(siacTClass.getSiacDClassTipo().getClassifTipoCode()));
	}
	
	public SiacTClass getSiacTClassGerarchia(List<SiacTClass> siacTClassList, SiacDClassTipoEnum siacDClassTipo, SiacDClassTipoEnum siacDClassTipoPadre) {
		try {
			return getSiacTClassGerarchia(CollectionUtil.getOneOnly(filterBySiacDClassTipo(siacTClassList, siacDClassTipo)), siacDClassTipoPadre);
		}
		catch (NullPointerException npe) {
			return null;
		}
	}
	
	public SiacTClass getSiacTClassGerarchia(SiacTClass siacTClass, SiacDClassTipoEnum siacDClassTipoPadre) {
		try {
			return CollectionUtil.getOneOnly(CollectionUtil.filter(siacTClass.getSiacRClassFamTreesFiglio(), new Filter<SiacRClassFamTree>() {

						@Override
						public boolean isAcceptable(SiacRClassFamTree source) {
							return EntityUtil.isValid(source)
									&& equalsSiacDClassTipo(source.getSiacTClassPadre(), siacDClassTipoPadre);
						}
					})).getSiacTClassPadre();
		}
		catch (NullPointerException npe) {
			return null;
		}
	}
	
	public List<SiacTClass> filterBySiacDClassTipo(List<SiacTClass> siacTClassList, SiacDClassTipoEnum siacDClassTipo) {
		return CollectionUtil.filter(siacTClassList, new Filter<SiacTClass>() {

			@Override
			public boolean isAcceptable(SiacTClass source) {
				return EntityUtil.isValid(source) && equalsSiacDClassTipo(source, siacDClassTipo);
			}
		});
	}
}
