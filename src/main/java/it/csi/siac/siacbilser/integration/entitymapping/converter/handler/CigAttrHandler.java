/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter.handler;

public class CigAttrHandler extends BaseSiacTAttrHandler{
	private static final long serialVersionUID = -4651049125851958153L;

	@Override
	protected Class<?> getAttrType(){
		return String.class;
	}

	@Override
	protected String innerHandleAttrValue(Object attrValue){
		return ((String)attrValue).toUpperCase();
	}
}
