/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;

import it.csi.siac.siacfinser.integration.entity.SiacTRecapitoSoggettoModFin;
import it.csi.siac.siacfinser.model.soggetto.Contatto;

public class ContattoModModificatoDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8791487027221368641L;
	private SiacTRecapitoSoggettoModFin daModificareSulDb;
	private Contatto contattoFromFrontEnd;
	
	public ContattoModModificatoDto(SiacTRecapitoSoggettoModFin daModificareSulDb,Contatto contattoFromFrontEnd){
		this.contattoFromFrontEnd = contattoFromFrontEnd;
		this.daModificareSulDb = daModificareSulDb;
	}
	
	
	public SiacTRecapitoSoggettoModFin getDaModificareSulDb() {
		return daModificareSulDb;
	}
	public void setDaModificareSulDb(SiacTRecapitoSoggettoModFin daModificareSulDb) {
		this.daModificareSulDb = daModificareSulDb;
	}
	public Contatto getContattoFromFrontEnd() {
		return contattoFromFrontEnd;
	}
	public void setContattoFromFrontEnd(Contatto contattoFromFrontEnd) {
		this.contattoFromFrontEnd = contattoFromFrontEnd;
	}
	
	
	
}
