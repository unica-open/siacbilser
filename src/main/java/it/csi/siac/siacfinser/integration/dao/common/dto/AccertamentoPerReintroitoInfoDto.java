/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.codifiche.ClasseSoggetto;
import it.csi.siac.siacfinser.model.ric.MovimentoKey;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

public class AccertamentoPerReintroitoInfoDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private MovimentoKey key;
	private Accertamento accertamento;
	private SubAccertamento subAccertamento;
	
	private EsitoRicercaMovimentoPkDto esitoRicercaMovPkDto;
	
	private MovGestInfoDto movGestInfoAccertamento;
	private MovGestInfoDto movGestInfoSubAccertamento;
	
	
	public ClasseSoggetto getClasseSoggettoAccOSub(){
		if(subAccertamento!=null){
			//IL SUB NON PUO' AVERE CLASSE SOGGETTO, MI RICONDUCO A QUELLA DEL SOGG SE PRESENTE:
			if(subAccertamento.getSoggetto()==null && accertamento.getClasseSoggetto()!=null
					&& accertamento.getClasseSoggetto().getCodice()!=null && accertamento.getSoggetto()==null){
				return accertamento.getClasseSoggetto();
			} else {
				return null;
			}
		} else {
			return accertamento.getClasseSoggetto();
		}
	}
	
	/**
	 * Se ci si riferisce ad un sub ritorna il Soggetto di subAccertamento,
	 * altrimenti quello di accertamento
	 * @return
	 */
	public Soggetto getSoggettoAccOSub(){
		if(subAccertamento!=null){
			return subAccertamento.getSoggetto();
		} else {
			return accertamento.getSoggetto();
		}
	}
	
	/**
	 * Se ci si riferisce ad un sub ritorna il disp a incassare di subAccertamento,
	 * altrimenti quello di accertamento
	 * @return
	 */
	public BigDecimal getDisponibileIncassareAccOSub(){
		if(subAccertamento!=null){
			return subAccertamento.getDisponibilitaIncassare();
		} else {
			return accertamento.getDisponibilitaIncassare();
		}
	}
	
	public Accertamento getAccertamento() {
		return accertamento;
	}
	public void setAccertamento(Accertamento accertamento) {
		this.accertamento = accertamento;
	}
	public SubAccertamento getSubAccertamento() {
		return subAccertamento;
	}
	public void setSubAccertamento(SubAccertamento subAccertamento) {
		this.subAccertamento = subAccertamento;
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

	public MovGestInfoDto getMovGestInfoAccertamento() {
		return movGestInfoAccertamento;
	}

	public void setMovGestInfoAccertamento(MovGestInfoDto movGestInfoAccertamento) {
		this.movGestInfoAccertamento = movGestInfoAccertamento;
	}

	public MovGestInfoDto getMovGestInfoSubAccertamento() {
		return movGestInfoSubAccertamento;
	}

	public void setMovGestInfoSubAccertamento(MovGestInfoDto movGestInfoSubAccertamento) {
		this.movGestInfoSubAccertamento = movGestInfoSubAccertamento;
	}
	
}
