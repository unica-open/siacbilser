/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.util.dto;

import java.util.List;

import it.csi.siac.siacfinser.model.soggetto.Onere;


public class RSoggettoOnereConverterDto {
	
	private List<Onere> lista;
	private String[] elencoCodes;
	
	public List<Onere> getLista() {
		return lista;
	}
	public void setLista(List<Onere> lista) {
		this.lista = lista;
	}
	public String[] getElencoCodes() {
		return elencoCodes;
	}
	public void setElencoCodes(String[] elencoCodes) {
		this.elencoCodes = elencoCodes;
	}

}
