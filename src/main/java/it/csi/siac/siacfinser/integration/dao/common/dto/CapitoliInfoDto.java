/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.util.HashMap;

import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;

public class CapitoliInfoDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9022917634596827927L;
	/**
	 * 
	 */
	private HashMap<Integer, CapitoloUscitaGestione> capitoliDaServizioUscita;
	private HashMap<Integer, CapitoloEntrataGestione> capitoliDaServizioEntrata;
	
	public HashMap<Integer, CapitoloUscitaGestione> getCapitoliDaServizioUscita() {
		return capitoliDaServizioUscita;
	}
	public void setCapitoliDaServizioUscita(
			HashMap<Integer, CapitoloUscitaGestione> capitoliDaServizioUscita) {
		this.capitoliDaServizioUscita = capitoliDaServizioUscita;
	}
	public HashMap<Integer, CapitoloEntrataGestione> getCapitoliDaServizioEntrata() {
		return capitoliDaServizioEntrata;
	}
	public void setCapitoliDaServizioEntrata(
			HashMap<Integer, CapitoloEntrataGestione> capitoliDaServizioEntrata) {
		this.capitoliDaServizioEntrata = capitoliDaServizioEntrata;
	}
	
	public CapitoloUscitaGestione getCapitoloUscitaById(Integer idCapitolo){
		CapitoloUscitaGestione capitolo = null;
		if(capitoliDaServizioUscita!=null){
			capitolo = capitoliDaServizioUscita.get(idCapitolo);
		}
		return capitolo;
	}
	
}
