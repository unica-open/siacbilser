/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.attributibilancio.report.excel;

import java.math.BigDecimal;
import java.util.Date;

import javax.xml.bind.annotation.XmlType;

import it.csi.siac.siacbilser.model.BILDataDictionary;

@XmlType(namespace = BILDataDictionary.NAMESPACE)
public class IntestazioneAccantonamentoFondiDubbiaEsigibilitaExcelRow extends BaseAccantonamentoFondiDubbiaEsigibilitaExcelRow {

	
	private String faseAttributiBilancio;
	private String statoAttributiBilancio;
//	private String utente;
	private Date dataOraElaborazione;
	private String anniEsercizio;
	private String riscossioneVirtuosa;
	private String quinquennioRiferimento;
	private BigDecimal accantonamentoGradualeEntiLocali;
	


	public String getFaseAttributiBilancio() {
		return this.faseAttributiBilancio;
	}

	public void setFaseAttributiBilancio(String faseAttributiBilancio) {
		this.faseAttributiBilancio = faseAttributiBilancio;
	}

	public String getStatoAttributiBilancio() {
		return this.statoAttributiBilancio;
	}

	public void setStatoAttributiBilancio(String statoAttributiBilancio) {
		this.statoAttributiBilancio = statoAttributiBilancio;
	}

	public Date getDataOraElaborazione() {
		return dataOraElaborazione;
	}

	public void setDataOraElaborazione(Date dataOraElaborazione) {
		this.dataOraElaborazione = dataOraElaborazione;
	}

	public String getAnniEsercizio() {
		return anniEsercizio;
	}

	public void setAnniEsercizio(String anniEsercizio) {
		this.anniEsercizio = anniEsercizio;
	}

	public String getRiscossioneVirtuosa() {
		return riscossioneVirtuosa;
	}

	public void setRiscossioneVirtuosa(String riscossioneVirtuosa) {
		this.riscossioneVirtuosa = riscossioneVirtuosa;
	}

	public String getQuinquennioRiferimento() {
		return quinquennioRiferimento;
	}

	public void setQuinquennioRiferimento(String quinquennioRiferimento) {
		this.quinquennioRiferimento = quinquennioRiferimento;
	}

	public BigDecimal getAccantonamentoGradualeEntiLocali() {
		return accantonamentoGradualeEntiLocali;
	}

	public void setAccantonamentoGradualeEntiLocali(BigDecimal accantonamentoGradualeEntiLocali) {
		this.accantonamentoGradualeEntiLocali = accantonamentoGradualeEntiLocali;
	}

}
