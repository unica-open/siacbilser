/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity.base;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import it.csi.siac.siacfinser.integration.entity.SiacRModificaStatoFin;

@MappedSuperclass
public abstract class SiacConModificaStato extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	
	//bi-directional many-to-one association to SiacRModificaStatoFin
	@ManyToOne
	@JoinColumn(name="mod_stato_r_id")
	private SiacRModificaStatoFin siacRModificaStato;
	
	public SiacRModificaStatoFin getSiacRModificaStato() {
		return this.siacRModificaStato;
	}

	public void setSiacRModificaStato(SiacRModificaStatoFin siacRModificaStato) {
		this.siacRModificaStato = siacRModificaStato;
	}
	
	
}
