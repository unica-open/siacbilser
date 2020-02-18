/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;

import it.csi.siac.siacfinser.integration.entity.SiacTLiquidazioneFin;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;

public class EsistoDoppiaGestioneLiquidazioneDto  implements Serializable{
	
	

	private static final long serialVersionUID = 1L;
	
	private SiacTLiquidazioneFin siacTLiquidazioneRibaltata;
	private Liquidazione liquidazioneRibaltata;
	
	private boolean presenzaErrori;
	
	
	public boolean isPresenzaErrori() {
		return presenzaErrori;
	}
	public void setPresenzaErrori(boolean presenzaErrori) {
		this.presenzaErrori = presenzaErrori;
	}
	public SiacTLiquidazioneFin getSiacTLiquidazioneRibaltata() {
		return siacTLiquidazioneRibaltata;
	}
	public void setSiacTLiquidazioneRibaltata(
			SiacTLiquidazioneFin siacTLiquidazioneRibaltata) {
		this.siacTLiquidazioneRibaltata = siacTLiquidazioneRibaltata;
	}
	public Liquidazione getLiquidazioneRibaltata() {
		return liquidazioneRibaltata;
	}
	public void setLiquidazioneRibaltata(Liquidazione liquidazioneRibaltata) {
		this.liquidazioneRibaltata = liquidazioneRibaltata;
	}
	
}
