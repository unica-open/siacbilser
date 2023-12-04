/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccorser.util;

import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.dozer.CustomConverter;
import org.dozer.spring.DozerBeanMapperFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

/**
 * Estende DozerBeanMapperFactoryBean per creare dinamicamente i CustomConverter beans
 * il cui nome fa match con la property customConverterMask.
 * 
 * @author Domenico Lisi
 */
public class ExtendedDozerBeanMapperFactoryBean extends DozerBeanMapperFactoryBean {
	
	@Autowired
	private ApplicationContext appCtx;
	
	private String customConverterMask;
	
	
	public ExtendedDozerBeanMapperFactoryBean() {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	/**
	 * @return the cosutomConverterMask
	 */
	public String getCustomConverterMask() {
		return customConverterMask;
	}

	/**
	 * @param customConverterMask the cosutomConverterMask to set
	 */
	public void setCustomConverterMask(String customConverterMask) {
		this.customConverterMask = customConverterMask;
		
		Map<String, CustomConverter> customConvertersMap = findMatchingCustomConverterOnCurrentContext(customConverterMask);
		
		setCustomConvertersWithId(customConvertersMap);
	}

	/**
	 * Trova i CustomConverter il cui nome bean fa match con la cosutomConverterMask.
	 * 
	 * @param customConverterMask
	 * @return
	 */
	private Map<String, CustomConverter> findMatchingCustomConverterOnCurrentContext(String customConverterMask) {
		Map<String, CustomConverter> customConvertersMap = appCtx.getBeansOfType(CustomConverter.class);
		
		Set<String> toDelCustomConvertersBean = new HashSet<String>();
		for (Entry<String, CustomConverter> entry : customConvertersMap.entrySet()) {
			String customConverterBeanName = entry.getKey();
			CustomConverter customConverter = entry.getValue();
			
			if(!customConverter.getClass().getName().matches(customConverterMask)) {
				toDelCustomConvertersBean.add(customConverterBeanName);
			}
		}
		
		for (String customConverterBeanName : toDelCustomConvertersBean) {
			customConvertersMap.remove(customConverterBeanName);
		}
		
		System.out.println("ExtendedDozerBeanMapperFactoryBean "
				+ "\ncustomConverterMask: "+customConverterMask
				+ "\nCreated customConverters: " + customConvertersMap.keySet()
				+ "\nRemoved customConverters: " + toDelCustomConvertersBean);
		
		return customConvertersMap;
	}

}
