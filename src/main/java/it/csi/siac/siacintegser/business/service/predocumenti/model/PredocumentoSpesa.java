/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.predocumenti.model;

import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;

public class PredocumentoSpesa extends Predocumento
{
	private ModalitaPagamentoSoggetto modalitaPagamentoSoggetto;

	public ModalitaPagamentoSoggetto getModalitaPagamentoSoggetto()
	{
		return modalitaPagamentoSoggetto;
	}

	public void setModalitaPagamentoSoggetto(ModalitaPagamentoSoggetto modalitaPagamentoSoggetto)
	{
		this.modalitaPagamentoSoggetto = modalitaPagamentoSoggetto;
	}
}
