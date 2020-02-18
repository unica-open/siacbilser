/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_d_gestione_livello database table.
 * 
 */
@Entity
@Table(name="siac_d_gestione_livello")
public class SiacDGestioneLivelloFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="gestione_livello_id")
	private Integer gestioneLivelloId;

	@Column(name="gestione_livello_code")
	private String gestioneLivelloCode;

	@Column(name="gestione_livello_desc")
	private String gestioneLivelloDesc;

	//bi-directional many-to-one association to SiacDGestioneTipoFin
	@ManyToOne
	@JoinColumn(name="gestione_tipo_id")
	private SiacDGestioneTipoFin siacDGestioneTipo;

	//bi-directional many-to-one association to SiacRGestioneEnteFin
	@OneToMany(mappedBy="siacDGestioneLivello")
	private List<SiacRGestioneEnteFin> siacRGestioneEntes;

	public SiacDGestioneLivelloFin() {
	}

	public Integer getGestioneLivelloId() {
		return this.gestioneLivelloId;
	}

	public void setGestioneLivelloId(Integer gestioneLivelloId) {
		this.gestioneLivelloId = gestioneLivelloId;
	}

	public String getGestioneLivelloCode() {
		return this.gestioneLivelloCode;
	}

	public void setGestioneLivelloCode(String gestioneLivelloCode) {
		this.gestioneLivelloCode = gestioneLivelloCode;
	}

	public String getGestioneLivelloDesc() {
		return this.gestioneLivelloDesc;
	}

	public void setGestioneLivelloDesc(String gestioneLivelloDesc) {
		this.gestioneLivelloDesc = gestioneLivelloDesc;
	}

	public SiacDGestioneTipoFin getSiacDGestioneTipo() {
		return this.siacDGestioneTipo;
	}

	public void setSiacDGestioneTipo(SiacDGestioneTipoFin siacDGestioneTipo) {
		this.siacDGestioneTipo = siacDGestioneTipo;
	}

	public List<SiacRGestioneEnteFin> getSiacRGestioneEntes() {
		return this.siacRGestioneEntes;
	}

	public void setSiacRGestioneEntes(List<SiacRGestioneEnteFin> siacRGestioneEntes) {
		this.siacRGestioneEntes = siacRGestioneEntes;
	}

	public SiacRGestioneEnteFin addSiacRGestioneEnte(SiacRGestioneEnteFin siacRGestioneEnte) {
		getSiacRGestioneEntes().add(siacRGestioneEnte);
		siacRGestioneEnte.setSiacDGestioneLivello(this);

		return siacRGestioneEnte;
	}

	public SiacRGestioneEnteFin removeSiacRGestioneEnte(SiacRGestioneEnteFin siacRGestioneEnte) {
		getSiacRGestioneEntes().remove(siacRGestioneEnte);
		siacRGestioneEnte.setSiacDGestioneLivello(null);

		return siacRGestioneEnte;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.gestioneLivelloId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.gestioneLivelloId = uid;
	}
}