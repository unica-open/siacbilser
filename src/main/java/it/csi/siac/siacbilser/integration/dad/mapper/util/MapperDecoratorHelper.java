/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/


package it.csi.siac.siacbilser.integration.dad.mapper.util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entitymapping.mapper.ModelDetailMapperDecorator;
import it.csi.siac.siaccommon.model.ComposedModelDetail;
import it.csi.siac.siaccommon.model.ModelDetailEnum;
import it.csi.siac.siaccommon.util.mapper.BaseMapperDecorator;
import it.csi.siac.siaccommon.util.mapper.MapperDecorator;

@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class MapperDecoratorHelper {
	
	@Autowired protected ApplicationContext appCtx;

	public <A, B> MapperDecorator<A, B>[] getDecoratorsFromModelDetails(ModelDetailEnum[] modelDetails) {
		return getDecoratorsFromModelDetails(modelDetails, null);
	}
		
	@SuppressWarnings("unchecked")
	public <A, B> MapperDecorator<A, B>[] getDecoratorsFromModelDetails(ModelDetailEnum[] modelDetailEnums, ComposedModelDetail[] composedModelDetails) {
		if (modelDetailEnums == null && composedModelDetails == null) {
			return null;
		}
		
		int modelDetailEnumsLength = modelDetailEnums == null ? 0 : modelDetailEnums.length;
		int composedModelDetailsLength = composedModelDetails == null ? 0 : composedModelDetails.length;
		
		MapperDecorator<A, B>[] mapperDecoratorArray = new MapperDecorator[modelDetailEnumsLength + composedModelDetailsLength];
		
		for (int i = 0; i < modelDetailEnumsLength; i++) {
			mapperDecoratorArray[i] = (MapperDecorator<A, B>) appCtx.getBean(ModelDetailMapperDecorator.fromModelDetailEnum(modelDetailEnums[i]));
		}
		
		for (int i = 0; i < composedModelDetailsLength; i++) {
			mapperDecoratorArray[i + modelDetailEnumsLength] = (MapperDecorator<A, B>) appCtx.getBean(ModelDetailMapperDecorator.fromModelDetailEnum(composedModelDetails[i].getModelDetailEnum()));
			
			((BaseMapperDecorator<A, B>) mapperDecoratorArray[i + modelDetailEnumsLength]).setMapperDecorators(
					getDecoratorsFromModelDetails(composedModelDetails[i].getModelDetailComponents()));	
		}

		return mapperDecoratorArray;
	}
	
}
