/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;

import it.csi.siac.siacfinser.StringUtils;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoPagamento;

public class SiopePlusDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String codeMotivazioneAssenzaCig;
	private String codeDebitoSiope;
	private String cig;
	
	
	public SiopePlusDto(Liquidazione liq){
		if(liq!=null){
			if(liq.getSiopeAssenzaMotivazione()!=null){
				this.codeMotivazioneAssenzaCig = liq.getSiopeAssenzaMotivazione().getCodice();
			}
			if(liq.getSiopeTipoDebito()!=null){
				this.codeDebitoSiope = liq.getSiopeTipoDebito().getCodice();
			}
			this.cig = liq.getCig();
		}
	}
	
	public SiopePlusDto(OrdinativoPagamento ordPag){
		if(ordPag!=null){
			if(ordPag.getSiopeAssenzaMotivazione()!=null){
				this.codeMotivazioneAssenzaCig = ordPag.getSiopeAssenzaMotivazione().getCodice();
			}
			if(ordPag.getSiopeTipoDebito()!=null){
				this.codeDebitoSiope = ordPag.getSiopeTipoDebito().getCodice();
			}
			this.cig = ordPag.getCig();
		}
	}
	
	public boolean sonoDiversi(SiopePlusDto daConfrontare){
		
		String cigConfronto = daConfrontare.getCig();
		String codiceAssenzaConfronto = daConfrontare.getCodeMotivazioneAssenzaCig();
		String codiceDebitoConfronto = daConfrontare.getCodeDebitoSiope();
		
		if(!StringUtils.sonoUguali(cigConfronto, this.cig)){
			return true;
		}
		
		if(!StringUtils.sonoUguali(codiceAssenzaConfronto, this.codeMotivazioneAssenzaCig)){
			return true;
		}
		
		if(!StringUtils.sonoUguali(codiceDebitoConfronto, this.codeDebitoSiope)){
			return true;
		}
		
		return false;
	}

	public String getCodeMotivazioneAssenzaCig() {
		return codeMotivazioneAssenzaCig;
	}

	public void setCodeMotivazioneAssenzaCig(String codeMotivazioneAssenzaCig) {
		this.codeMotivazioneAssenzaCig = codeMotivazioneAssenzaCig;
	}

	public String getCodeDebitoSiope() {
		return codeDebitoSiope;
	}

	public void setCodeDebitoSiope(String codeDebitoSiope) {
		this.codeDebitoSiope = codeDebitoSiope;
	}

	public String getCig() {
		return cig;
	}

	public void setCig(String cig) {
		this.cig = cig;
	}
	
}
