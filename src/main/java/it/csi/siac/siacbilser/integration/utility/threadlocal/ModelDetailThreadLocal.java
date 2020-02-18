/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.utility.threadlocal;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import it.csi.siac.siacbilser.model.ModelDetail;

/**
 * Thread-local for handling ModelDetails
 * @author Marchino Alessandro
 *
 */
public class ModelDetailThreadLocal extends ThreadLocal<Collection<ModelDetail>> {
	
	@Override
	protected Collection<ModelDetail> initialValue() {
		return new HashSet<ModelDetail>();
	}
	
	/**
	 * Gets the thread-registered model details by class
	 * @param clazz the class for the model detail
	 * @return the model details coherent with the required class
	 */
	@SuppressWarnings("unchecked")
	public <MD extends ModelDetail> MD[] byModelDetailClass(Class<MD> clazz) {
		Collection<MD> res = new HashSet<MD>();
		Collection<ModelDetail> currentData = this.get();
		
		for(ModelDetail md : currentData) {
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
	public void clearByModelDetailClass(Class<? extends ModelDetail> clazz) {
		Collection<ModelDetail> currentData = this.get();
		
		for(Iterator<ModelDetail> it = currentData.iterator(); it.hasNext();) {
			ModelDetail md = it.next();
			if(clazz.isInstance(md)) {
				it.remove();
			}
		}
	}
	
	public void addModelDetails(ModelDetail... modelDetails) {
		if(modelDetails != null && modelDetails.length > 0) {
			Collection<ModelDetail> currentData = this.get();
			for(ModelDetail md : modelDetails) {
				currentData.add(md);
			}
		}
	}
	
}
