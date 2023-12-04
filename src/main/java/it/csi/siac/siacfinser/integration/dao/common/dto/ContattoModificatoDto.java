/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;

import it.csi.siac.siacfinser.integration.entity.SiacTRecapitoSoggettoFin;
import it.csi.siac.siacfinser.model.soggetto.Contatto;

public class ContattoModificatoDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4885615362248060433L;
	private SiacTRecapitoSoggettoFin daModificareSulDb;
	private Contatto contattoFromFrontEnd;
	
	public ContattoModificatoDto(SiacTRecapitoSoggettoFin daModificareSulDb,Contatto contattoFromFrontEnd){
		this.contattoFromFrontEnd = contattoFromFrontEnd;
		this.daModificareSulDb = daModificareSulDb;
	}

	public SiacTRecapitoSoggettoFin getDaModificareSulDb() {
		return daModificareSulDb;
	}

	public void setDaModificareSulDb(SiacTRecapitoSoggettoFin daModificareSulDb) {
		this.daModificareSulDb = daModificareSulDb;
	}

	public Contatto getContattoFromFrontEnd() {
		return contattoFromFrontEnd;
	}

	public void setContattoFromFrontEnd(Contatto contattoFromFrontEnd) {
		this.contattoFromFrontEnd = contattoFromFrontEnd;
	}
	
}
