/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacfinser.CommonUtils;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsFin;
import it.csi.siac.siacfinser.model.movgest.VincoloImpegno;

public class ModificaVincoliImpegnoInfoDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<SiacRMovgestTsFin> vincoliDaAnnullare = new ArrayList<SiacRMovgestTsFin>();
	private List<VincoloImpegno> vincoliDaInserire = new ArrayList<VincoloImpegno>();
	private List<VincoloImpegno> vincoliDaAggiornare = new ArrayList<VincoloImpegno>();
	private ArrayList<VincoloImpegno> vincoliInvariati = new ArrayList<VincoloImpegno>();
	
	private ArrayList<MovGestInfoDto> elencoInfoAccertamentiCoinvolti = new ArrayList<MovGestInfoDto>();
	
	private List<SiacRMovgestTsFin> vincoliOld;
	
	public List<SiacRMovgestTsFin> getVincoliDaAnnullare() {
		return vincoliDaAnnullare;
	}
	public void setVincoliDaAnnullare(List<SiacRMovgestTsFin> vincoliDaAnnullare) {
		this.vincoliDaAnnullare = vincoliDaAnnullare;
	}
	public List<VincoloImpegno> getVincoliDaInserire() {
		return vincoliDaInserire;
	}
	public void setVincoliDaInserire(List<VincoloImpegno> vincoliDaInserire) {
		this.vincoliDaInserire = vincoliDaInserire;
	}
	public List<VincoloImpegno> getVincoliDaAggiornare() {
		return vincoliDaAggiornare;
	}
	public void setVincoliDaAggiornare(List<VincoloImpegno> vincoliDaAggiornare) {
		this.vincoliDaAggiornare = vincoliDaAggiornare;
	}
	public List<SiacRMovgestTsFin> getVincoliOld() {
		return vincoliOld;
	}
	public void setVincoliOld(List<SiacRMovgestTsFin> vincoliOld) {
		this.vincoliOld = vincoliOld;
	}
	public List<VincoloImpegno> getVincoliDaInserireEAggiornare() {
		return CommonUtils.toList(vincoliDaInserire,vincoliDaAggiornare);
	}
	public ArrayList<MovGestInfoDto> getElencoInfoAccertamentiCoinvolti() {
		return elencoInfoAccertamentiCoinvolti;
	}
	public void setElencoInfoAccertamentiCoinvolti(
			ArrayList<MovGestInfoDto> elencoInfoAccertamentiCoinvolti) {
		this.elencoInfoAccertamentiCoinvolti = elencoInfoAccertamentiCoinvolti;
	}
	public MovGestInfoDto findInfoAccertamento(int idMovGest){
		MovGestInfoDto trovato = null;
		if(elencoInfoAccertamentiCoinvolti!=null && elencoInfoAccertamentiCoinvolti.size()>0){
			for(MovGestInfoDto it: elencoInfoAccertamentiCoinvolti){
				if(it.getMovGestId()==idMovGest){
					trovato = it;
					break;
				}
			}
		}
		return trovato;
	}
	public ArrayList<VincoloImpegno> getVincoliInvariati() {
		return vincoliInvariati;
	}
	public void setVincoliInvariati(ArrayList<VincoloImpegno> vincoliInvariati) {
		this.vincoliInvariati = vincoliInvariati;
	}
}
