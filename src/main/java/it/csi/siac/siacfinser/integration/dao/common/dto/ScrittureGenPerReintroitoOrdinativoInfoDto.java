/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.util.List;

public class ScrittureGenPerReintroitoOrdinativoInfoDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private LiquidazioneScritturaGenInfoDto liquidazionePrincipale;
	private OrdinativoPagamentoScritturaGenInfoDto ordPagPrincipaleSpostato;
	private OrdinativoPagamentoScritturaGenInfoDto nuovoOrdPagPrincipaleInserito;
	
	private List<ScritturaGenRitenuteStessoImpegnoInfoDto> ritenuteRaggruppatePerImpegno;
	
	private List<OrdinativoIncassoScritturaGenInfoDto> ordinativiIncassoSpostati;

	public LiquidazioneScritturaGenInfoDto getLiquidazionePrincipale() {
		return liquidazionePrincipale;
	}

	public void setLiquidazionePrincipale(LiquidazioneScritturaGenInfoDto liquidazionePrincipale) {
		this.liquidazionePrincipale = liquidazionePrincipale;
	}

	public OrdinativoPagamentoScritturaGenInfoDto getOrdPagPrincipaleSpostato() {
		return ordPagPrincipaleSpostato;
	}

	public void setOrdPagPrincipaleSpostato(OrdinativoPagamentoScritturaGenInfoDto ordPagPrincipaleSpostato) {
		this.ordPagPrincipaleSpostato = ordPagPrincipaleSpostato;
	}

	public OrdinativoPagamentoScritturaGenInfoDto getNuovoOrdPagPrincipaleInserito() {
		return nuovoOrdPagPrincipaleInserito;
	}

	public void setNuovoOrdPagPrincipaleInserito(OrdinativoPagamentoScritturaGenInfoDto nuovoOrdPagPrincipaleInserito) {
		this.nuovoOrdPagPrincipaleInserito = nuovoOrdPagPrincipaleInserito;
	}

	public List<ScritturaGenRitenuteStessoImpegnoInfoDto> getRitenuteRaggruppatePerImpegno() {
		return ritenuteRaggruppatePerImpegno;
	}

	public void setRitenuteRaggruppatePerImpegno(
			List<ScritturaGenRitenuteStessoImpegnoInfoDto> ritenuteRaggruppatePerImpegno) {
		this.ritenuteRaggruppatePerImpegno = ritenuteRaggruppatePerImpegno;
	}

	public List<OrdinativoIncassoScritturaGenInfoDto> getOrdinativiIncassoSpostati() {
		return ordinativiIncassoSpostati;
	}

	public void setOrdinativiIncassoSpostati(List<OrdinativoIncassoScritturaGenInfoDto> ordinativiIncassoSpostati) {
		this.ordinativiIncassoSpostati = ordinativiIncassoSpostati;
	}
	
	
}
