/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.attiamministrativi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlType;

import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Messaggio;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siacintegser.business.service.attiamministrativi.model.AttoAmministrativoElab;
import it.csi.siac.siacintegser.frontend.webservice.INTEGSvcDictionary;

@XmlType(namespace = INTEGSvcDictionary.NAMESPACE)
public class ElaboraAttoAmministrativoResponse extends ServiceResponse {
	
	private AttoAmministrativoElab attoAmministrativo;
	private Ente ente;
	
	private List<Messaggio> messaggi = new ArrayList<Messaggio>();
	
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
	/**
	 * @return the ente
	 */
	public Ente getEnte() {
		return ente;
	}
	/**
	 * @param ente the ente to set
	 */
	public void setEnte(Ente ente) {
		this.ente = ente;
	}
	
	public List<Messaggio> getMessaggi() {
		return messaggi;
	}
	public void setMessaggi(List<Messaggio> messaggi) {
		this.messaggi = messaggi != null ? messaggi : new ArrayList<Messaggio>();
	}
	
	public void addMessaggio(Messaggio messaggio) {
		getMessaggi().add(messaggio);
	}
	
	public void addMessaggi(Collection<Messaggio> messaggi) {
		getMessaggi().addAll(messaggi);
	}
	
	
	
	
	
}
