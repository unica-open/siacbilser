/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity.base;


import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import it.csi.siac.siaccommonser.integration.entity.SiacTBase;
import it.csi.siac.siacfinser.integration.entity.SiacTEnteProprietarioFin;

/**
 * Entita comune per i dto di corserverimpl
 * 
 * @author
 * 
 */
@MappedSuperclass
public abstract class SiacTEnteBase extends SiacTBase {

	/** Per la serializzazione */
	private static final long serialVersionUID = 6267504631967785510L;
	//bi-directional many-to-one association to SiacTEnteProprietarioFin
	@ManyToOne
	@JoinColumn(name="ente_proprietario_id")
	private SiacTEnteProprietarioFin siacTEnteProprietario;

	public SiacTEnteProprietarioFin getSiacTEnteProprietario() {
		return siacTEnteProprietario;
	}

	public void setSiacTEnteProprietario(SiacTEnteProprietarioFin siacTEnteProprietario) {
		this.siacTEnteProprietario = siacTEnteProprietario;
	}
	
	
	
	
}