/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.util.List;

import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsFin;

public class MovGestMainModelEntityInfoDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1226305153541672471L;
	
	private SiacTMovgestTsFin siacTMovgestTs;
	
	private List<MovGestModelEntityInfoDto> listaSub=null;

	public SiacTMovgestTsFin getSiacTMovgestTs() {
		return siacTMovgestTs;
	}

	public void setSiacTMovgestTs(SiacTMovgestTsFin siacTMovgestTs) {
		this.siacTMovgestTs = siacTMovgestTs;
	}

	public List<MovGestModelEntityInfoDto> getListaSub() {
		return listaSub;
	}

	public void setListaSub(List<MovGestModelEntityInfoDto> listaSub) {
		this.listaSub = listaSub;
	}
	
	
}
