/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.util.List;

import it.csi.siac.siacfinser.model.ric.MovimentoKey;

/**
 * Classe di comodo per raggruppare per lo stesso movimento le righe ritenute reintroito
 * @author claudio.picco
 *
 */
public class RitenuteReintroitoConStessoMovimentoDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<RitenutaSpiltPerReintroitoInfoDto> listaRitenute;
	
	private MovimentoKey movimentoKey;

	public List<RitenutaSpiltPerReintroitoInfoDto> getListaRitenute() {
		return listaRitenute;
	}

	public void setListaRitenute(
			List<RitenutaSpiltPerReintroitoInfoDto> listaRitenute) {
		this.listaRitenute = listaRitenute;
	}

	public MovimentoKey getMovimentoKey() {
		return movimentoKey;
	}

	public void setMovimentoKey(MovimentoKey movimentoKey) {
		this.movimentoKey = movimentoKey;
	}
	
}
