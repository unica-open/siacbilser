/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.base;

public interface ElaboraFileAsyncServiceInfo {
	
	String getAsyncServiceName();

	Class<? extends ElaboraFileAsyncBaseService<?,?>> getAsyncServiceClass();
	
}
