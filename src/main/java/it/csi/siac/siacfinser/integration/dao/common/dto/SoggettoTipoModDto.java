/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;

import it.csi.siac.siacfinser.integration.entity.SiacTPersonaFisicaModFin;
import it.csi.siac.siacfinser.integration.entity.SiacTPersonaGiuridicaModFin;

/**
 * Data Transfer Object per uso interno nelle classi SoggettoFinDad e SoggettoDao
 * @author claudio.picco
 *
 */
public class SoggettoTipoModDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3207128091520109197L;

	//ti dice se si tratta di una persona fisica oppure giuridica:
	private boolean personaFisica;
	/////////////////////////////////////////////////////////////
	
	//valorizzati in alternativa a seconda se si tratta di una persona fisica o giuridica:
	private SiacTPersonaFisicaModFin siactTpersonaFisicaMod = null;
	private SiacTPersonaGiuridicaModFin  siactTpersonaGiuridicaMod = null;
	/////////////////////////////////////////////////////////////////////////////////////
	
	public boolean isPersonaFisica() {
		return personaFisica;
	}
	public void setPersonaFisica(boolean personaFisica) {
		this.personaFisica = personaFisica;
	}
	public SiacTPersonaFisicaModFin getSiactTpersonaFisicaMod() {
		return siactTpersonaFisicaMod;
	}
	public void setSiactTpersonaFisicaMod(
			SiacTPersonaFisicaModFin siactTpersonaFisicaMod) {
		this.siactTpersonaFisicaMod = siactTpersonaFisicaMod;
	}
	public SiacTPersonaGiuridicaModFin getSiactTpersonaGiuridicaMod() {
		return siactTpersonaGiuridicaMod;
	}
	public void setSiactTpersonaGiuridicaMod(
			SiacTPersonaGiuridicaModFin siactTpersonaGiuridicaMod) {
		this.siactTpersonaGiuridicaMod = siactTpersonaGiuridicaMod;
	}

}
