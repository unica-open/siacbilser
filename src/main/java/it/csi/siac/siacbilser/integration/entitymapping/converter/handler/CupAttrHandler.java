/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter.handler;

public class CupAttrHandler extends BaseSiacTAttrHandler{
	private static final long serialVersionUID = 3380983299407213640L;

	@Override
	protected Class<?> getAttrType(){
		return String.class;
	}

	@Override
	protected String innerHandleAttrValue(Object attrValue){
		return ((String)attrValue).toUpperCase();
	}
}
