/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;


import java.io.Serializable;

import javax.persistence.MappedSuperclass;

/**
 * Entita comune per le tabelle di Sirfel* (Portale Fatture).
 *
 * @author Domenico Lisi
 */
@MappedSuperclass
public abstract class SirfelTBase<PK extends SirfelPKBase> implements Serializable {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 9192492572671769478L;

	public abstract PK getId();
	
	public abstract void setId(PK id);
	
	public Integer getEnteProprietarioId() {
		return getId() != null ? getId().getEnteProprietarioId() : null;
	}
}
