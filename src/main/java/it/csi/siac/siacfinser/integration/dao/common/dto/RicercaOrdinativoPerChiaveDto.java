/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;

import it.csi.siac.siacattser.model.ric.RicercaAtti;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloEGest;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloUGest;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoIncasso;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoPagamento;

public class RicercaOrdinativoPerChiaveDto implements Serializable {

	private static final long serialVersionUID = 7291264343102277160L;
	
	private OrdinativoPagamento ordinativoPagamento;
	
	private OrdinativoIncasso ordinativoIncasso;
	
	private RicercaDettaglioCapitoloUGest ricercaDettaglioCapitoloUGest;
	
	private RicercaDettaglioCapitoloEGest ricercaDettaglioCapitoloEGest;
	
	private RicercaAtti ricercaAtti;

	public OrdinativoPagamento getOrdinativoPagamento() {
		return ordinativoPagamento;
	}

	public void setOrdinativoPagamento(OrdinativoPagamento ordinativoPagamento) {
		this.ordinativoPagamento = ordinativoPagamento;
	}

	public OrdinativoIncasso getOrdinativoIncasso() {
		return ordinativoIncasso;
	}

	public void setOrdinativoIncasso(OrdinativoIncasso ordinativoIncasso) {
		this.ordinativoIncasso = ordinativoIncasso;
	}

	public RicercaDettaglioCapitoloUGest getRicercaDettaglioCapitoloUGest() {
		return ricercaDettaglioCapitoloUGest;
	}

	public void setRicercaDettaglioCapitoloUGest(
			RicercaDettaglioCapitoloUGest ricercaDettaglioCapitoloUGest) {
		this.ricercaDettaglioCapitoloUGest = ricercaDettaglioCapitoloUGest;
	}

	public RicercaDettaglioCapitoloEGest getRicercaDettaglioCapitoloEGest() {
		return ricercaDettaglioCapitoloEGest;
	}

	public void setRicercaDettaglioCapitoloEGest(
			RicercaDettaglioCapitoloEGest ricercaDettaglioCapitoloEGest) {
		this.ricercaDettaglioCapitoloEGest = ricercaDettaglioCapitoloEGest;
	}

	public RicercaAtti getRicercaAtti() {
		return ricercaAtti;
	}

	public void setRicercaAtti(RicercaAtti ricercaAtti) {
		this.ricercaAtti = ricercaAtti;
	}

}
