/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/


package it.csi.siac.siacbilser.model.utils;

import java.io.Serializable;
import java.math.BigDecimal;

import it.csi.siac.siacbilser.model.TipoComponenteImportiCapitolo;

public class WrapperComponenteImportiCapitoloVariatiInVariazione extends WrapperImportiCapitoloVariatiInVariazione implements Serializable {

	/**Per la serializzazione */
	private static final long serialVersionUID = -8112844016676460612L;
	private Integer elemDetCompTipoId;
	private String elemDetCompTipoDesc;
	private TipoComponenteImportiCapitolo tipoComponenteImportiCapitolo;
	
	public WrapperComponenteImportiCapitoloVariatiInVariazione(Integer elemDetCompTipoId, String elemTipoCode, String anno, String elemDetTipoCode, BigDecimal importoVariato,String elemDetCompTipoDesc) {
		super(elemTipoCode, anno, elemDetTipoCode, importoVariato);
		this.elemDetCompTipoId = elemDetCompTipoId;
		this.elemDetCompTipoDesc = elemDetCompTipoDesc;
		this.tipoComponenteImportiCapitolo = new TipoComponenteImportiCapitolo();
		this.tipoComponenteImportiCapitolo.setUid(elemDetCompTipoId);
		this.tipoComponenteImportiCapitolo.setDescrizione(elemDetCompTipoDesc);
	}

	public TipoComponenteImportiCapitolo getTipoComponenteImportiCapitolo() {
		return tipoComponenteImportiCapitolo; 
	}
	
	public Integer getElemDetCompTipoId() {
		return elemDetCompTipoId;
	}

	public String getElemDetCompTipoDesc() {
		return elemDetCompTipoDesc;
	}
	 
	

	
	
}
