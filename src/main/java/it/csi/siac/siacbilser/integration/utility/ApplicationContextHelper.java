/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.utility;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;

public final class ApplicationContextHelper {
	
	public static <T> T getBean(ApplicationContext appCtx, Class<T> clazz) {
		ConfigurableListableBeanFactory beanFactory = (ConfigurableListableBeanFactory)appCtx.getAutowireCapableBeanFactory();
		String[] beanNames = beanFactory.getBeanNamesForType(clazz);
		for(String beanName : beanNames) {
			BeanDefinition bd = beanFactory.getBeanDefinition(beanName);
			if(bd.isPrimary()) {
				return beanFactory.getBean(beanName, clazz);
			}
		}
		throw new IllegalArgumentException("No primary bean found for class " + clazz.getName());
	}

}
