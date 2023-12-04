/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.base;

public interface ElaboraFileServiceInfo {

	String getServiceName();
	
	Class<? extends ElaboraFileBaseService> getServiceClass();
}
