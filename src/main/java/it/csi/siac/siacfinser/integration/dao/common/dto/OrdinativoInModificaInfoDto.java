/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;

import it.csi.siac.siacfinser.integration.entity.SiacTOrdinativoFin;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo;

public class OrdinativoInModificaInfoDto extends AbstractOrdinativoInEditingInfoDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1226305153541672471L;
	
	private SiacTOrdinativoFin siacTOrdinativo;
	private Ordinativo ordinativo;
	private SubOrdinativoInModificaInfoDto infoModificheSubOrdinativi;
	private RegolarizzazioniDiCassaInModificaInfoDto infoModificheRegolarizzazioniDiCassa;
	
	private RicercaOrdinativoPerChiaveDto datiOrdinativo;

	public Ordinativo getOrdinativo() {
		return ordinativo;
	}
	public void setOrdinativo(Ordinativo ordinativo) {
		this.ordinativo = ordinativo;
	}
	public SiacTOrdinativoFin getSiacTOrdinativo() {
		return siacTOrdinativo;
	}
	public void setSiacTOrdinativo(SiacTOrdinativoFin siacTOrdinativo) {
		this.siacTOrdinativo = siacTOrdinativo;
	}
	public SubOrdinativoInModificaInfoDto getInfoModificheSubOrdinativi() {
		return infoModificheSubOrdinativi;
	}
	public void setInfoModificheSubOrdinativi(
			SubOrdinativoInModificaInfoDto infoModificheSubOrdinativi) {
		this.infoModificheSubOrdinativi = infoModificheSubOrdinativi;
	}
	public RegolarizzazioniDiCassaInModificaInfoDto getInfoModificheRegolarizzazioniDiCassa() {
		return infoModificheRegolarizzazioniDiCassa;
	}
	public void setInfoModificheRegolarizzazioniDiCassa(
			RegolarizzazioniDiCassaInModificaInfoDto infoModificheRegolarizzazioniDiCassa) {
		this.infoModificheRegolarizzazioniDiCassa = infoModificheRegolarizzazioniDiCassa;
	}
	public RicercaOrdinativoPerChiaveDto getDatiOrdinativo() {
		return datiOrdinativo;
	}
	public void setDatiOrdinativo(RicercaOrdinativoPerChiaveDto datiOrdinativo) {
		this.datiOrdinativo = datiOrdinativo;
	}
	
}
