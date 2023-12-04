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
 * The persistent class for the siac_d_forma_giuridica_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_forma_giuridica_tipo")
public class SiacDFormaGiuridicaTipoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="forma_giuridica_tipo_id")
	private Integer formaGiuridicaTipoId;

	@Column(name="forma_giuridica_tipo_code")
	private String formaGiuridicaTipoCode;

	@Column(name="forma_giuridica_tipo_desc")
	private String formaGiuridicaTipoDesc;

	//bi-directional many-to-one association to SiacTFormaGiuridicaFin
	@OneToMany(mappedBy="siacDFormaGiuridicaTipo")
	private List<SiacTFormaGiuridicaFin> siacTFormaGiuridicas;

	public SiacDFormaGiuridicaTipoFin() {
	}

	public Integer getFormaGiuridicaTipoId() {
		return this.formaGiuridicaTipoId;
	}

	public void setFormaGiuridicaTipoId(Integer formaGiuridicaTipoId) {
		this.formaGiuridicaTipoId = formaGiuridicaTipoId;
	}

	public String getFormaGiuridicaTipoCode() {
		return this.formaGiuridicaTipoCode;
	}

	public void setFormaGiuridicaTipoCode(String formaGiuridicaTipoCode) {
		this.formaGiuridicaTipoCode = formaGiuridicaTipoCode;
	}

	public String getFormaGiuridicaTipoDesc() {
		return this.formaGiuridicaTipoDesc;
	}

	public void setFormaGiuridicaTipoDesc(String formaGiuridicaTipoDesc) {
		this.formaGiuridicaTipoDesc = formaGiuridicaTipoDesc;
	}

	public List<SiacTFormaGiuridicaFin> getSiacTFormaGiuridicas() {
		return this.siacTFormaGiuridicas;
	}

	public void setSiacTFormaGiuridicas(List<SiacTFormaGiuridicaFin> siacTFormaGiuridicas) {
		this.siacTFormaGiuridicas = siacTFormaGiuridicas;
	}

	public SiacTFormaGiuridicaFin addSiacTFormaGiuridica(SiacTFormaGiuridicaFin siacTFormaGiuridica) {
		getSiacTFormaGiuridicas().add(siacTFormaGiuridica);
		siacTFormaGiuridica.setSiacDFormaGiuridicaTipo(this);

		return siacTFormaGiuridica;
	}

	public SiacTFormaGiuridicaFin removeSiacTFormaGiuridica(SiacTFormaGiuridicaFin siacTFormaGiuridica) {
		getSiacTFormaGiuridicas().remove(siacTFormaGiuridica);
		siacTFormaGiuridica.setSiacDFormaGiuridicaTipo(null);

		return siacTFormaGiuridica;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.formaGiuridicaTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.formaGiuridicaTipoId = uid;
	}
}