/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_d_gestione_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_gestione_tipo")
public class SiacDGestioneTipoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="gestione_tipo_id")
	private Integer gestioneTipoId;

	@Column(name="gestione_tipo_code")
	private String gestioneTipoCode;

	@Column(name="gestione_tipo_desc")
	private String gestioneTipoDesc;

	//bi-directional many-to-one association to SiacDGestioneLivelloFin
	@OneToMany(mappedBy="siacDGestioneTipo")
	private List<SiacDGestioneLivelloFin> siacDGestioneLivellos;

	public SiacDGestioneTipoFin() {
	}

	public Integer getGestioneTipoId() {
		return this.gestioneTipoId;
	}

	public void setGestioneTipoId(Integer gestioneTipoId) {
		this.gestioneTipoId = gestioneTipoId;
	}

	public String getGestioneTipoCode() {
		return this.gestioneTipoCode;
	}

	public void setGestioneTipoCode(String gestioneTipoCode) {
		this.gestioneTipoCode = gestioneTipoCode;
	}

	public String getGestioneTipoDesc() {
		return this.gestioneTipoDesc;
	}

	public void setGestioneTipoDesc(String gestioneTipoDesc) {
		this.gestioneTipoDesc = gestioneTipoDesc;
	}

	public List<SiacDGestioneLivelloFin> getSiacDGestioneLivellos() {
		return this.siacDGestioneLivellos;
	}

	public void setSiacDGestioneLivellos(List<SiacDGestioneLivelloFin> siacDGestioneLivellos) {
		this.siacDGestioneLivellos = siacDGestioneLivellos;
	}

	public SiacDGestioneLivelloFin addSiacDGestioneLivello(SiacDGestioneLivelloFin siacDGestioneLivello) {
		getSiacDGestioneLivellos().add(siacDGestioneLivello);
		siacDGestioneLivello.setSiacDGestioneTipo(this);

		return siacDGestioneLivello;
	}

	public SiacDGestioneLivelloFin removeSiacDGestioneLivello(SiacDGestioneLivelloFin siacDGestioneLivello) {
		getSiacDGestioneLivellos().remove(siacDGestioneLivello);
		siacDGestioneLivello.setSiacDGestioneTipo(null);

		return siacDGestioneLivello;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.gestioneTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.gestioneTipoId = uid;
	}
}