/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.util.List;

import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsFin;

public class EsitoAggiornamentoSubMovGestTs implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1226305153541672471L;
	
	private List<SiacTMovgestTsFin> inseriti;
	private List<SiacTMovgestTsFin> aggiornati;
	private List<SiacTMovgestTsFin> eliminati;
	private List<SiacTMovgestTsFin> invariati;
	
	public List<SiacTMovgestTsFin> getInseriti() {
		return inseriti;
	}
	public void setInseriti(List<SiacTMovgestTsFin> inseriti) {
		this.inseriti = inseriti;
	}
	public List<SiacTMovgestTsFin> getAggiornati() {
		return aggiornati;
	}
	public void setAggiornati(List<SiacTMovgestTsFin> aggiornati) {
		this.aggiornati = aggiornati;
	}
	public List<SiacTMovgestTsFin> getEliminati() {
		return eliminati;
	}
	public void setEliminati(List<SiacTMovgestTsFin> eliminati) {
		this.eliminati = eliminati;
	}
	public List<SiacTMovgestTsFin> getInvariati() {
		return invariati;
	}
	public void setInvariati(List<SiacTMovgestTsFin> invariati) {
		this.invariati = invariati;
	}
	
}
