/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;

import it.csi.siac.siacfinser.model.ordinativo.OrdinativoIncasso;
import it.csi.siac.siacfinser.model.ric.OrdinativoKey;

public class RitenutaSpiltPerReintroitoInfoDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ImpegnoPerReintroitoInfoDto impegno;
	private AccertamentoPerReintroitoInfoDto accertamento;
	
	private OrdinativoIncasso ordinativoIncasso;
	private OrdinativoKey ordinativoIncassoKey;

	public ImpegnoPerReintroitoInfoDto getImpegno() {
		return impegno;
	}

	public void setImpegno(ImpegnoPerReintroitoInfoDto impegno) {
		this.impegno = impegno;
	}

	public AccertamentoPerReintroitoInfoDto getAccertamento() {
		return accertamento;
	}

	public void setAccertamento(AccertamentoPerReintroitoInfoDto accertamento) {
		this.accertamento = accertamento;
	}

	public OrdinativoIncasso getOrdinativoIncasso() {
		return ordinativoIncasso;
	}

	public void setOrdinativoIncasso(OrdinativoIncasso ordinativoIncasso) {
		this.ordinativoIncasso = ordinativoIncasso;
	}

	public OrdinativoKey getOrdinativoIncassoKey() {
		return ordinativoIncassoKey;
	}

	public void setOrdinativoIncassoKey(OrdinativoKey ordinativoIncassoKey) {
		this.ordinativoIncassoKey = ordinativoIncassoKey;
	}
	
}
