/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.util.dto;

import it.csi.siac.siacfinser.integration.entity.SiacTOrdinativoFin;

public class SiacTOrdinativoCollegatoCustom {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6371411770121166810L;
	
	private SiacTOrdinativoFin siacTOrdinativo;
	
	private String relazioneOrdinativiCollegati;

	public SiacTOrdinativoFin getSiacTOrdinativo() {
		return siacTOrdinativo;
	}

	public void setSiacTOrdinativo(SiacTOrdinativoFin siacTOrdinativo) {
		this.siacTOrdinativo = siacTOrdinativo;
	}

	public String getRelazioneOrdinativiCollegati() {
		return relazioneOrdinativiCollegati;
	}

	public void setRelazioneOrdinativiCollegati(
			String relazioneOrdinativiCollegati) {
		this.relazioneOrdinativiCollegati = relazioneOrdinativiCollegati;
	}

}
