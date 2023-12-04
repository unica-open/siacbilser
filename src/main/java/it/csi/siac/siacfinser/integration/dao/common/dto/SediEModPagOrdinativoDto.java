/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;

import it.csi.siac.siacfinser.integration.entity.SiacTModpagFin;
import it.csi.siac.siacfinser.integration.entity.SiacTSoggettoFin;

public class SediEModPagOrdinativoDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4931108753290880292L;
	
	private SiacTSoggettoFin siacTSoggettoOrdine;
	private SiacTSoggettoFin siacTSedeSecondariaOrdine;
	private SiacTModpagFin siacTModpagOrdinativo;
	
	
	public SiacTSoggettoFin getSoggettoDaRelazionare(){
		if(siacTSedeSecondariaOrdine!=null){
			return siacTSedeSecondariaOrdine;
		} else {
			return siacTSoggettoOrdine;
		}
	}
	
	public SiacTSoggettoFin getSiacTSedeSecondariaOrdine() {
		return siacTSedeSecondariaOrdine;
	}

	public void setSiacTSedeSecondariaOrdine(SiacTSoggettoFin siacTSedeSecondariaOrdine) {
		this.siacTSedeSecondariaOrdine = siacTSedeSecondariaOrdine;
	}

	public SiacTSoggettoFin getSiacTSoggettoOrdine() {
		return siacTSoggettoOrdine;
	}
	
	public void setSiacTSoggettoOrdine(SiacTSoggettoFin siacTSoggettoOrdine) {
		this.siacTSoggettoOrdine = siacTSoggettoOrdine;
	}
	
	public SiacTModpagFin getSiacTModpagOrdinativo() {
		return siacTModpagOrdinativo;
	}
	
	public void setSiacTModpagOrdinativo(SiacTModpagFin siacTModpagOrdinativo) {
		this.siacTModpagOrdinativo = siacTModpagOrdinativo;
	}	
}
