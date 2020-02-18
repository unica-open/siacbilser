/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.codifiche.ClasseSoggetto;
import it.csi.siac.siacfinser.model.ric.MovimentoKey;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

public class ImpegnoPerReintroitoInfoDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private MovimentoKey key;
	private Impegno impegno;
	private SubImpegno subImpegno;
	
	private MovGestInfoDto movGestInfoImpegno;
	private MovGestInfoDto movGestInfoSubImpegno;
	
	private EsitoRicercaMovimentoPkDto esitoRicercaMovPkDto;
	
	public String getDescrizioneImpOSub(){
		if(subImpegno!=null){
			return subImpegno.getDescrizione();
		} else {
			return impegno.getDescrizione();
		}
	}
	
	public ClasseSoggetto getClasseSoggettoImpOSub(){
		if(subImpegno!=null){
			//IL SUB NON PUO' AVERE CLASSE SOGGETTO, MI RICONDUCO A QUELLA DEL SOGG SE PRESENTE:
			if(subImpegno.getSoggetto()==null && impegno.getClasseSoggetto()!=null
					&& impegno.getClasseSoggetto().getCodice()!=null && impegno.getSoggetto()==null){
				return impegno.getClasseSoggetto();
			} else {
				return null;
			}
		} else {
			return impegno.getClasseSoggetto();
		}
	}
	
	/**
	 * Se ci si riferisce ad un sub ritorna il Soggetto di subImpegno,
	 * altrimenti quello di impegno
	 * @return
	 */
	public Soggetto getSoggettoImpOSub(){
		if(subImpegno!=null){
			return subImpegno.getSoggetto();
		} else {
			return impegno.getSoggetto();
		}
	}
	
	/**
	 * Se ci si riferisce ad un sub ritorna il disp a liquidare di subImpegno,
	 * altrimenti quello di impegno
	 * @return
	 */
	public BigDecimal getDisponibileLiquidareImpOSub(){
		if(subImpegno!=null){
			return subImpegno.getDisponibilitaLiquidare();
		} else {
			return impegno.getDisponibilitaLiquidare();
		}
	}
	
	/**
	 *  Se ci si riferisce ad un sub ritorna il sub, altrimenti l'imp
	 * @return
	 */
	public Impegno getImpOSub(){
		if(subImpegno!=null){
			return subImpegno;
		} else {
			return impegno;
		}
	}
	
	public Impegno getImpegno() {
		return impegno;
	}
	public void setImpegno(Impegno impegno) {
		this.impegno = impegno;
	}
	public SubImpegno getSubImpegno() {
		return subImpegno;
	}
	public void setSubImpegno(SubImpegno subImpegno) {
		this.subImpegno = subImpegno;
	}
	public MovimentoKey getKey() {
		return key;
	}
	public void setKey(MovimentoKey key) {
		this.key = key;
	}

	public EsitoRicercaMovimentoPkDto getEsitoRicercaMovPkDto() {
		return esitoRicercaMovPkDto;
	}

	public void setEsitoRicercaMovPkDto(EsitoRicercaMovimentoPkDto esitoRicercaMovPkDto) {
		this.esitoRicercaMovPkDto = esitoRicercaMovPkDto;
	}

	public MovGestInfoDto getMovGestInfoImpegno() {
		return movGestInfoImpegno;
	}

	public void setMovGestInfoImpegno(MovGestInfoDto movGestInfoImpegno) {
		this.movGestInfoImpegno = movGestInfoImpegno;
	}

	public MovGestInfoDto getMovGestInfoSubImpegno() {
		return movGestInfoSubImpegno;
	}

	public void setMovGestInfoSubImpegno(MovGestInfoDto movGestInfoSubImpegno) {
		this.movGestInfoSubImpegno = movGestInfoSubImpegno;
	}
	
}
