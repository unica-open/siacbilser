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
 * The persistent class for the siac_d_forma_giuridica_cat database table.
 * 
 */
@Entity
@Table(name="siac_d_forma_giuridica_cat")
public class SiacDFormaGiuridicaCatFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="forma_giuridica_cat_id")
	private Integer formaGiuridicaCatId;

	@Column(name="forma_giuridica_cat_code")
	private String formaGiuridicaCatCode;

	@Column(name="forma_giuridica_cat_desc")
	private String formaGiuridicaCatDesc;

	//bi-directional many-to-one association to SiacTFormaGiuridicaFin
	@OneToMany(mappedBy="siacDFormaGiuridicaCat")
	private List<SiacTFormaGiuridicaFin> siacTFormaGiuridicas;

	public SiacDFormaGiuridicaCatFin() {
	}

	public Integer getFormaGiuridicaCatId() {
		return this.formaGiuridicaCatId;
	}

	public void setFormaGiuridicaCatId(Integer formaGiuridicaCatId) {
		this.formaGiuridicaCatId = formaGiuridicaCatId;
	}

	public String getFormaGiuridicaCatCode() {
		return this.formaGiuridicaCatCode;
	}

	public void setFormaGiuridicaCatCode(String formaGiuridicaCatCode) {
		this.formaGiuridicaCatCode = formaGiuridicaCatCode;
	}

	public String getFormaGiuridicaCatDesc() {
		return this.formaGiuridicaCatDesc;
	}

	public void setFormaGiuridicaCatDesc(String formaGiuridicaCatDesc) {
		this.formaGiuridicaCatDesc = formaGiuridicaCatDesc;
	}

	public List<SiacTFormaGiuridicaFin> getSiacTFormaGiuridicas() {
		return this.siacTFormaGiuridicas;
	}

	public void setSiacTFormaGiuridicas(List<SiacTFormaGiuridicaFin> siacTFormaGiuridicas) {
		this.siacTFormaGiuridicas = siacTFormaGiuridicas;
	}

	public SiacTFormaGiuridicaFin addSiacTFormaGiuridica(SiacTFormaGiuridicaFin siacTFormaGiuridica) {
		getSiacTFormaGiuridicas().add(siacTFormaGiuridica);
		siacTFormaGiuridica.setSiacDFormaGiuridicaCat(this);

		return siacTFormaGiuridica;
	}

	public SiacTFormaGiuridicaFin removeSiacTFormaGiuridica(SiacTFormaGiuridicaFin siacTFormaGiuridica) {
		getSiacTFormaGiuridicas().remove(siacTFormaGiuridica);
		siacTFormaGiuridica.setSiacDFormaGiuridicaCat(null);

		return siacTFormaGiuridica;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.formaGiuridicaCatId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.formaGiuridicaCatId = uid;
	}
}