/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.util.List;

import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsSogModFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsSogclasseModFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsDetModFin;

public class ImpegnoInAnnullamentoInfoDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1226305153541672471L;
	
	private List<ImportoImpegnoInAnnullamentoInfoDto> elencoModifiche;
	private List<SiacTMovgestTsDetModFin> siacTMovgestTsDetModDaAnnullare;
	private List<SiacRMovgestTsSogclasseModFin> siacRMovgestTsSogclasseModListDaAnnullare;
	private List<SiacRMovgestTsSogModFin> siacRMovgestTsSogModListDaAnnullare;
	
	private MovGestModelEntityInfoDto movGestModelEntityInfoDto;
	
	private boolean annullaInDoppiaGestione;
	
	public List<ImportoImpegnoInAnnullamentoInfoDto> getElencoModifiche() {
		return elencoModifiche;
	}
	public void setElencoModifiche(
			List<ImportoImpegnoInAnnullamentoInfoDto> elencoModifiche) {
		this.elencoModifiche = elencoModifiche;
	}
	public List<SiacTMovgestTsDetModFin> getSiacTMovgestTsDetModDaAnnullare() {
		return siacTMovgestTsDetModDaAnnullare;
	}
	public void setSiacTMovgestTsDetModDaAnnullare(
			List<SiacTMovgestTsDetModFin> siacTMovgestTsDetModDaAnnullare) {
		this.siacTMovgestTsDetModDaAnnullare = siacTMovgestTsDetModDaAnnullare;
	}
	public List<SiacRMovgestTsSogclasseModFin> getSiacRMovgestTsSogclasseModListDaAnnullare() {
		return siacRMovgestTsSogclasseModListDaAnnullare;
	}
	public void setSiacRMovgestTsSogclasseModListDaAnnullare(
			List<SiacRMovgestTsSogclasseModFin> siacRMovgestTsSogclasseModListDaAnnullare) {
		this.siacRMovgestTsSogclasseModListDaAnnullare = siacRMovgestTsSogclasseModListDaAnnullare;
	}
	public List<SiacRMovgestTsSogModFin> getSiacRMovgestTsSogModListDaAnnullare() {
		return siacRMovgestTsSogModListDaAnnullare;
	}
	public void setSiacRMovgestTsSogModListDaAnnullare(
			List<SiacRMovgestTsSogModFin> siacRMovgestTsSogModListDaAnnullare) {
		this.siacRMovgestTsSogModListDaAnnullare = siacRMovgestTsSogModListDaAnnullare;
	}
	public boolean isAnnullaInDoppiaGestione() {
		return annullaInDoppiaGestione;
	}
	public void setAnnullaInDoppiaGestione(boolean annullaInDoppiaGestione) {
		this.annullaInDoppiaGestione = annullaInDoppiaGestione;
	}
	public MovGestModelEntityInfoDto getMovGestModelEntityInfoDto() {
		return movGestModelEntityInfoDto;
	}
	public void setMovGestModelEntityInfoDto(
			MovGestModelEntityInfoDto movGestModelEntityInfoDto) {
		this.movGestModelEntityInfoDto = movGestModelEntityInfoDto;
	}
	
}
