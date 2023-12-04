/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.util.List;

import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siacfinser.model.carta.CartaContabile;

public class EsitoGestioneCartaContabileDto {

	private static final long serialVersionUID = 1L;

	private List<Errore> listaErrori;
	private CartaContabile cartaContabile;
	
	public List<Errore> getListaErrori() {
		return listaErrori;
	}
	
	public void setListaErrori(List<Errore> listaErrori) {
		this.listaErrori = listaErrori;
	}
	
	public CartaContabile getCartaContabile() {
		return cartaContabile;
	}
	
	public void setCartaContabile(CartaContabile cartaContabile) {
		this.cartaContabile = cartaContabile;
	}
}
