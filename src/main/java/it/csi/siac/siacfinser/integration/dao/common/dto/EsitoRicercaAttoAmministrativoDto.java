/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.util.List;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siaccorser.model.Errore;

public class EsitoRicercaAttoAmministrativoDto {
	
	private static final long serialVersionUID = 1L;

	private List<Errore> listaErrori;
	private AttoAmministrativo attoAmministrativo;
	
	public List<Errore> getListaErrori() {
		return listaErrori;
	}
	
	public void setListaErrori(List<Errore> listaErrori) {
		this.listaErrori = listaErrori;
	}
	
	public AttoAmministrativo getAttoAmministrativo() {
		return attoAmministrativo;
	}
	
	public void setAttoAmministrativo(AttoAmministrativo attoAmministrativo) {
		this.attoAmministrativo = attoAmministrativo;
	}
}
