/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;




/**
 * The persistent class for the siac_r_gestione_ente database table.
 * 
 */
@Entity
@Table(name="siac_r_gestione_ente")
public class SiacRGestioneEnteFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="gestione_ente_id")
	private Integer gestioneEnteId;

	//bi-directional many-to-one association to SiacDGestioneLivelloFin
	@ManyToOne
	@JoinColumn(name="gestione_livello_id")
	private SiacDGestioneLivelloFin siacDGestioneLivello;

	public SiacRGestioneEnteFin() {
	}

	public Integer getGestioneEnteId() {
		return this.gestioneEnteId;
	}

	public void setGestioneEnteId(Integer gestioneEnteId) {
		this.gestioneEnteId = gestioneEnteId;
	}

	public SiacDGestioneLivelloFin getSiacDGestioneLivello() {
		return this.siacDGestioneLivello;
	}

	public void setSiacDGestioneLivello(SiacDGestioneLivelloFin siacDGestioneLivello) {
		this.siacDGestioneLivello = siacDGestioneLivello;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.gestioneEnteId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.gestioneEnteId = uid;
	}
}