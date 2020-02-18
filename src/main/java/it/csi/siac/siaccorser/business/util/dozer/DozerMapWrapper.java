/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccorser.business.util.dozer;

import java.util.HashMap;
import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * The Class DozerMapWrapper.
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public class DozerMapWrapper<K, V>  {
	
	/** The map. */
	private Map<K,V> map = new HashMap<K, V>();

	/**
	 * Sets the map.
	 *
	 * @param map the map
	 */
	public void setMap(Map<K,V> map) {
		this.map = map;
	}

	/**
	 * Gets the map.
	 *
	 * @return the map
	 */
	public Map<K,V> getMap() {
		return map;
	}
	
}
