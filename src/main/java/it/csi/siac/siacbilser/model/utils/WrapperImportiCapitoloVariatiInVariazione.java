/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/


package it.csi.siac.siacbilser.model.utils;

import java.io.Serializable;
import java.math.BigDecimal;

import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemDetTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemTipoEnum;
import it.csi.siac.siacbilser.model.TipoCapitolo;

public class WrapperImportiCapitoloVariatiInVariazione implements Serializable {

	/**Per la serializzazione */
	private static final long serialVersionUID = 5792804168842503835L;
	
	private String anno;
	private String elemTipoCode;
	private SiacDBilElemTipoEnum siacDBilElemTipoEnum;
	private SiacDBilElemDetTipoEnum siacDBilElemDetTipoEnum;
//	private String elemDetTipoCode;
	private BigDecimal importoVariato;
	
	public WrapperImportiCapitoloVariatiInVariazione(String elemTipoCode,String anno, String elemDetTipoCode, BigDecimal importoVariato) {
		this.anno = anno;
		this.siacDBilElemDetTipoEnum = SiacDBilElemDetTipoEnum.byCodice(elemDetTipoCode);
		this.siacDBilElemTipoEnum = SiacDBilElemTipoEnum.byCodice(elemTipoCode);
		this.elemTipoCode = elemTipoCode;
		this.importoVariato = importoVariato;
	}
	
	public String getCodiceTipoImporto() {
		return this.siacDBilElemTipoEnum.getCodice();
	}
	public TipoCapitolo getTipoCapitolo() {
		return this.siacDBilElemTipoEnum.getTipoCapitolo();
	}

	public SiacDBilElemDetTipoEnum getSiacDBilElemDetTipoEnum() {
		return siacDBilElemDetTipoEnum;
	}

	public Integer getAnnoInteger() {
		return Integer.valueOf(this.anno);
	}
	
	public String getAnno() {
		return this.anno;
	}

	public String getElemDetTipoCode() {
		return siacDBilElemDetTipoEnum != null? siacDBilElemDetTipoEnum.getCodice() : null;
	}

	public BigDecimal getImportoVariato() {
		return importoVariato;
	}
	
	public boolean isSpesaPrevisione() {
		return TipoCapitolo.CAPITOLO_USCITA_PREVISIONE.equals(getTipoCapitolo());
	}
	
	

}
