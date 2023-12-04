/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.util.List;

import it.csi.siac.siacfinser.integration.entity.SiacRAttrBaseFin;
import it.csi.siac.siacfinser.integration.entity.SiacRClassBaseFin;

public class EsitoSalvataggioTransazioneElmentare implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<SiacRClassBaseFin> tClassSaved;
	
	private SiacRAttrBaseFin siacRAttrBaseCupSaved;

	public SiacRAttrBaseFin getSiacRAttrBaseCupSaved() {
		return siacRAttrBaseCupSaved;
	}

	public void setSiacRAttrBaseCupSaved(SiacRAttrBaseFin siacRAttrBaseCupSaved) {
		this.siacRAttrBaseCupSaved = siacRAttrBaseCupSaved;
	}

	public List<SiacRClassBaseFin> gettClassSaved() {
		return tClassSaved;
	}

	public void settClassSaved(List<SiacRClassBaseFin> tClassSaved) {
		this.tClassSaved = tClassSaved;
	}

	
	
}
