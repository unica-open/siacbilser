/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.util.List;

import it.csi.siac.siacfinser.CommonUtil;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsFin;

public class AttributoTClassInfoMassiveDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private OggettoDellAttributoTClass tipoOggetto;
	
	
	//OGGETTO PRINCIPALE
	private List<SiacTMovgestTsFin> siacTMovgestTs;
	//

	public OggettoDellAttributoTClass getTipoOggetto() {
		return tipoOggetto;
	}

	public void setTipoOggetto(OggettoDellAttributoTClass tipoOggetto) {
		this.tipoOggetto = tipoOggetto;
	}

	public List<SiacTMovgestTsFin> getSiacTMovgestTs() {
		return siacTMovgestTs;
	}

	public void setSiacTMovgestTs(List<SiacTMovgestTsFin> siacTMovgestTs) {
		this.siacTMovgestTs = siacTMovgestTs;
	}

	public List<Integer> getIdOggetti(){
		 List<Integer> id = null;
		if(OggettoDellAttributoTClass.T_MOVGEST_TS.equals(tipoOggetto)){
			id = CommonUtil.getIdListSiacTBase(siacTMovgestTs);
		} else if(OggettoDellAttributoTClass.T_LIQUIDAZIONE.equals(tipoOggetto)){
			//non implementato per il momento
		} else if(OggettoDellAttributoTClass.T_ORDINATIVO.equals(tipoOggetto)){
			//non implementato per il momento
		} else if(OggettoDellAttributoTClass.T_SOGGETTO.equals(tipoOggetto)){
			//non implementato per il momento
		} else if(OggettoDellAttributoTClass.T_SOGGETTO_MOD.equals(tipoOggetto)){
			//non implementato per il momento
		} else if(OggettoDellAttributoTClass.T_CARTACONT.equals(tipoOggetto)){
			//non implementato per il momento
		} else if(OggettoDellAttributoTClass.T_CARTACONT_ESTERA.equals(tipoOggetto)){
			//non implementato per il momento
		} else if(OggettoDellAttributoTClass.T_CARTACONT_DET.equals(tipoOggetto)){
			//non implementato per il momento
		}
		return id;
	}
	
}
