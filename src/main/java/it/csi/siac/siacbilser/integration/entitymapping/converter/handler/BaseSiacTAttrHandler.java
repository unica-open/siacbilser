/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter.handler;

import java.io.Serializable;

public abstract class BaseSiacTAttrHandler implements Serializable, SiacTAttrHandler {
	private static final long serialVersionUID = -7068400678807343724L;
	
	@Override
	public Object handleAttrValue(Object attrValue){
		if (attrValue == null) 
			return null;
		
		Class<?> cls = getAttrType();
		
		if (! cls.equals(attrValue.getClass())) {
				throw new IllegalArgumentException("Tipo errato: " + attrValue.getClass().getName());
		}
			
		return innerHandleAttrValue(attrValue);
	}

	protected abstract Class<?> getAttrType();

	protected abstract Object innerHandleAttrValue(Object attrValue);
}
