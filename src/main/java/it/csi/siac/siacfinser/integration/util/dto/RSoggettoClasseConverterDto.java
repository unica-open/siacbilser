/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.util.dto;

import java.util.List;

import it.csi.siac.siacfinser.model.soggetto.ClassificazioneSoggetto;


public class RSoggettoClasseConverterDto {
	
	private List<ClassificazioneSoggetto> lista;
	private String[] elencoCodes;
	
	public List<ClassificazioneSoggetto> getLista() {
		return lista;
	}
	public void setLista(List<ClassificazioneSoggetto> lista) {
		this.lista = lista;
	}
	public String[] getElencoCodes() {
		return elencoCodes;
	}
	public void setElencoCodes(String[] elencoCodes) {
		this.elencoCodes = elencoCodes;
	}

}
