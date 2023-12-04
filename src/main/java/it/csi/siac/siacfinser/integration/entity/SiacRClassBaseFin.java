/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


@MappedSuperclass
public abstract class SiacRClassBaseFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;


	//bi-directional many-to-one association to SiacTClassFin
	@ManyToOne
	@JoinColumn(name="classif_id")
	private SiacTClassFin siacTClass;


	public SiacTClassFin getSiacTClass() {
		return siacTClass;
	}


	public void setSiacTClass(SiacTClassFin siacTClass) {
		this.siacTClass = siacTClass;
	}

}