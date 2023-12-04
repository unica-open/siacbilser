/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.util.List;

import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;

public class SoggettoInRicercaLiquidazioneDto implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2573703973624140188L;
	
	private Soggetto soggetto;
	private List<ModalitaPagamentoSoggetto> listaModPag;
	private List<SedeSecondariaSoggetto> listaSediSecondarie;
	public Soggetto getSoggetto() {
		return soggetto;
	}
	public void setSoggetto(Soggetto soggetto) {
		this.soggetto = soggetto;
	}
	public List<ModalitaPagamentoSoggetto> getListaModPag() {
		return listaModPag;
	}
	public void setListaModPag(List<ModalitaPagamentoSoggetto> listaModPag) {
		this.listaModPag = listaModPag;
	}
	public List<SedeSecondariaSoggetto> getListaSediSecondarie() {
		return listaSediSecondarie;
	}
	public void setListaSediSecondarie(
			List<SedeSecondariaSoggetto> listaSediSecondarie) {
		this.listaSediSecondarie = listaSediSecondarie;
	}
	
}
