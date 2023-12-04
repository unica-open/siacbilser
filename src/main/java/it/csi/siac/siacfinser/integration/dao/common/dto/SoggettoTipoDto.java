/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;

import it.csi.siac.siacfinser.integration.entity.SiacTPersonaFisicaFin;
import it.csi.siac.siacfinser.integration.entity.SiacTPersonaGiuridicaFin;

/**
 * Data Transfer Object per uso interno nelle classi SoggettoFinDad e SoggettoDao
 * @author claudio.picco
 *
 */
public class SoggettoTipoDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4758948018168138345L;
	//ti dice se si tratta di una persona fisica oppure giuridica:
	private boolean personaFisica;
	/////////////////////////////////////////////////////////////
	
	//valorizzati in alternativa a seconda se si tratta di una persona fisica o giuridica:
	private SiacTPersonaFisicaFin siactTpersonaFisica = null;
	private SiacTPersonaGiuridicaFin siactTpersonaGiuridica = null;
	/////////////////////////////////////////////////////////////////////////////////////
	
	public boolean isPersonaFisica() {
		return personaFisica;
	}
	public void setPersonaFisica(boolean personaFisica) {
		this.personaFisica = personaFisica;
	}
	public SiacTPersonaFisicaFin getSiactTpersonaFisica() {
		return siactTpersonaFisica;
	}
	public void setSiactTpersonaFisica(SiacTPersonaFisicaFin siactTpersonaFisica) {
		this.siactTpersonaFisica = siactTpersonaFisica;
	}
	public SiacTPersonaGiuridicaFin getSiactTpersonaGiuridica() {
		return siactTpersonaGiuridica;
	}
	public void setSiactTpersonaGiuridica(
			SiacTPersonaGiuridicaFin siactTpersonaGiuridica) {
		this.siactTpersonaGiuridica = siactTpersonaGiuridica;
	}

}
