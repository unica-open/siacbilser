/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.attiamministrativi;

import javax.xml.bind.annotation.XmlType;

import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siacintegser.business.service.attiamministrativi.model.AttoAmministrativoElab;
import it.csi.siac.siacintegser.frontend.webservice.INTEGSvcDictionary;

@XmlType(namespace = INTEGSvcDictionary.NAMESPACE)
public class ElaboraAttoAmministrativo extends ServiceRequest {
	
	private AttoAmministrativoElab attoAmministrativo;

	/**
	 * @return the attoAmministrativo
	 */
	public AttoAmministrativoElab getAttoAmministrativo() {
		return attoAmministrativo;
	}

	/**
	 * @param attoAmministrativo the attoAmministrativo to set
	 */
	public void setAttoAmministrativo(AttoAmministrativoElab attoAmministrativo) {
		this.attoAmministrativo = attoAmministrativo;
	}
	
}
