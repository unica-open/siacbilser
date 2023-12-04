/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsFin;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.movgest.VincoloImpegno;

public class EsitoCalcoloImportiVincoliDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private BigDecimal deltaVincolo;
	
	private SiacTMovgestTsFin siacTMovgestTsAcc;
	private SiacTMovgestTsFin siacTMovgestTsAccResiduo;
	
	private VincoloImpegno vincoloResiduo;
	
	private Accertamento accertamentoFresco;

	public BigDecimal getDeltaVincolo() {
		return deltaVincolo;
	}

	public void setDeltaVincolo(BigDecimal deltaVincolo) {
		this.deltaVincolo = deltaVincolo;
	}

	public SiacTMovgestTsFin getSiacTMovgestTsAcc() {
		return siacTMovgestTsAcc;
	}

	public void setSiacTMovgestTsAcc(SiacTMovgestTsFin siacTMovgestTsAcc) {
		this.siacTMovgestTsAcc = siacTMovgestTsAcc;
	}

	public SiacTMovgestTsFin getSiacTMovgestTsAccResiduo() {
		return siacTMovgestTsAccResiduo;
	}

	public void setSiacTMovgestTsAccResiduo(SiacTMovgestTsFin siacTMovgestTsAccResiduo) {
		this.siacTMovgestTsAccResiduo = siacTMovgestTsAccResiduo;
	}

	public VincoloImpegno getVincoloResiduo() {
		return vincoloResiduo;
	}

	public void setVincoloResiduo(VincoloImpegno vincoloResiduo) {
		this.vincoloResiduo = vincoloResiduo;
	}

	public Accertamento getAccertamentoFresco() {
		return accertamentoFresco;
	}

	public void setAccertamentoFresco(Accertamento accertamentoFresco) {
		this.accertamentoFresco = accertamentoFresco;
	}

}
