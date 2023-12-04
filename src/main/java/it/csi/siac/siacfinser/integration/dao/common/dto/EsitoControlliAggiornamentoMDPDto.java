/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.util.List;

import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;

public class EsitoControlliAggiornamentoMDPDto {

	private static final long serialVersionUID = 1L;

	private List<Errore> listaErrori;
	private ModalitaPagamentoSoggetto modalitaPagamentoDaAnnullare;
	
	public List<Errore> getListaErrori() {
		return listaErrori;
	}
	
	public void setListaErrori(List<Errore> listaErrori) {
		this.listaErrori = listaErrori;
	}

	public ModalitaPagamentoSoggetto getModalitaPagamentoDaAnnullare() {
		return modalitaPagamentoDaAnnullare;
	}

	public void setModalitaPagamentoDaAnnullare(
			ModalitaPagamentoSoggetto modalitaPagamentoDaAnnullare) {
		this.modalitaPagamentoDaAnnullare = modalitaPagamentoDaAnnullare;
	}

}
