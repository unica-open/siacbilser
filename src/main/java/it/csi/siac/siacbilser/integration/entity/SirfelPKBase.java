/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * Classe di base per le Embeddeable Entity che rappresentano la Primary Key.
 * Modella il fatto che tutte le Pirmary Key delle tabelle Sirfel hanno almeno l'Ente Proprietario.
 * 
 * @author Domenico
 */
@MappedSuperclass
public class SirfelPKBase implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8363917390929050418L;
	
	@Column(name="ente_proprietario_id")
	protected Integer enteProprietarioId;

	public Integer getEnteProprietarioId() {
		return this.enteProprietarioId;
	}
	public void setEnteProprietarioId(Integer enteProprietarioId) {
		this.enteProprietarioId = enteProprietarioId;
	}
	
//	
//	/** The siac t ente proprietario. */
//	@ManyToOne
//	@JoinColumn(name="ente_proprietario_id")
//	private SiacTEnteProprietario siacTEnteProprietario;
//
//	/**
//	 * Gets the siac t ente proprietario.
//	 *
//	 * @return the siac t ente proprietario
//	 */
//	public SiacTEnteProprietario getSiacTEnteProprietario() {
//		return siacTEnteProprietario;
//	}
//
//	/**
//	 * Sets the siac t ente proprietario.
//	 *
//	 * @param siacTEnteProprietario the new siac t ente proprietario
//	 */
//	public void setSiacTEnteProprietario(SiacTEnteProprietario siacTEnteProprietario) {
//		this.siacTEnteProprietario = siacTEnteProprietario;
//		if(siacTEnteProprietario!=null){
//			enteProprietarioId = siacTEnteProprietario.getUid();
//		} else {
//			enteProprietarioId = null;
//		}
//	}
//	
}
