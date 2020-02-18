/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;


import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

// TODO: Auto-generated Javadoc
/**
 * Entita comune per le tabelle che hanno ente_proprietario_id
 * e che tracciano il login di creazione, cancellazione e modifica. 
 * 
 * @author Domenico Lisi
 * 
 */
@MappedSuperclass
public abstract class SiacTEnteBaseExt extends SiacTBaseExt {

	/** Per la serializzazione */
	private static final long serialVersionUID = -6659025387890826304L;
	
	//bi-directional many-to-one association to SiacTEnteProprietario
	/** The siac t ente proprietario. */
	@ManyToOne
	@JoinColumn(name="ente_proprietario_id")
	private SiacTEnteProprietario siacTEnteProprietario;

	/**
	 * Gets the siac t ente proprietario.
	 *
	 * @return the siac t ente proprietario
	 */
	public SiacTEnteProprietario getSiacTEnteProprietario() {
		return siacTEnteProprietario;
	}

	/**
	 * Sets the siac t ente proprietario.
	 *
	 * @param siacTEnteProprietario the new siac t ente proprietario
	 */
	public void setSiacTEnteProprietario(SiacTEnteProprietario siacTEnteProprietario) {
		this.siacTEnteProprietario = siacTEnteProprietario;
	}
	
	
	
}
