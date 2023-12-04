/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.utility.threadlocal;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import it.csi.siac.siaccommon.model.ModelDetailEnum;

/**
 * Thread-local for handling ModelDetails
 * @author Marchino Alessandro
 *
 */
public class ModelDetailThreadLocal extends ThreadLocal<Collection<ModelDetailEnum>> {
	
	@Override
	protected Collection<ModelDetailEnum> initialValue() {
		return new HashSet<ModelDetailEnum>();
	}
	
	/**
	 * Gets the thread-registered model details by class
	 * @param clazz the class for the model detail
	 * @return the model details coherent with the required class
	 */
	@SuppressWarnings("unchecked")
	public <MD extends ModelDetailEnum> MD[] byModelDetailClass(Class<MD> clazz) {
		Collection<MD> res = new HashSet<MD>();
		Collection<ModelDetailEnum> currentData = this.get();
		
		for(ModelDetailEnum md : currentData) {
			if(clazz.isInstance(md)) {
				res.add((MD)md);
			}
		}
		MD[] arr = (MD[]) Array.newInstance(clazz, res.size());
		return res.toArray(arr);
	}
	
	/**
	 * Clears the thread-registered model details
	 */
	public void clear() {
		this.get().clear();
	}
	
	/**
	 * Gets the thread-registered model details by class
	 * @param clazz the class for the model detail
	 * @return the model details coherent with the required class
	 */
	public void clearByModelDetailClass(Class<? extends ModelDetailEnum> clazz) {
		Collection<ModelDetailEnum> currentData = this.get();
		
		for(Iterator<ModelDetailEnum> it = currentData.iterator(); it.hasNext();) {
			ModelDetailEnum md = it.next();
			if(clazz.isInstance(md)) {
				it.remove();
			}
		}
	}
	
	public void addModelDetails(ModelDetailEnum... modelDetails) {
		if(modelDetails != null && modelDetails.length > 0) {
			Collection<ModelDetailEnum> currentData = this.get();
			for(ModelDetailEnum md : modelDetails) {
				currentData.add(md);
			}
		}
	}
	
}
