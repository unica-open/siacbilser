/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.util.List;

import it.csi.siac.siacfinser.model.SubImpegno;

public class EsitoCompilaListaSubImpegniConTuttiGliIdsDto  implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<SubImpegno> elencoIdsSubNonPresentiDaFe;
	private List<SubImpegno> tuttiISubSoloGliIds;
	private List<SubImpegno> subImpegniDaFeConAggiuntiINonPresenti;
	
	private OttimizzazioneMovGestDto ottimizzazioneMovGest;
	
	public List<SubImpegno> getElencoIdsSubNonPresentiDaFe() {
		return elencoIdsSubNonPresentiDaFe;
	}
	public void setElencoIdsSubNonPresentiDaFe(
			List<SubImpegno> elencoIdsSubNonPresentiDaFe) {
		this.elencoIdsSubNonPresentiDaFe = elencoIdsSubNonPresentiDaFe;
	}
	public List<SubImpegno> getTuttiISubSoloGliIds() {
		return tuttiISubSoloGliIds;
	}
	public void setTuttiISubSoloGliIds(List<SubImpegno> tuttiISubSoloGliIds) {
		this.tuttiISubSoloGliIds = tuttiISubSoloGliIds;
	}
	public List<SubImpegno> getSubImpegniDaFeConAggiuntiINonPresenti() {
		return subImpegniDaFeConAggiuntiINonPresenti;
	}
	public void setSubImpegniDaFeConAggiuntiINonPresenti(
			List<SubImpegno> subImpegniDaFeConAggiuntiINonPresenti) {
		this.subImpegniDaFeConAggiuntiINonPresenti = subImpegniDaFeConAggiuntiINonPresenti;
	}
	public OttimizzazioneMovGestDto getOttimizzazioneMovGest() {
		return ottimizzazioneMovGest;
	}
	public void setOttimizzazioneMovGest(
			OttimizzazioneMovGestDto ottimizzazioneMovGest) {
		this.ottimizzazioneMovGest = ottimizzazioneMovGest;
	}
	
	
}
