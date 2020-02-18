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
 * The persistent class for the siac_t_forma_giuridica database table.
 * 
 */
@Entity
@Table(name="siac_t_forma_giuridica")
public class SiacTFormaGiuridicaFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="forma_giuridica_id")
	private Integer formaGiuridicaId;

	@Column(name="forma_giuridica_desc")
	private String formaGiuridicaDesc;

	@Column(name="forma_giuridica_istat_codice")
	private String formaGiuridicaIstatCodice;

	//bi-directional many-to-one association to SiacRFormaGiuridicaFin
	@OneToMany(mappedBy="siacTFormaGiuridica")
	private List<SiacRFormaGiuridicaFin> siacRFormaGiuridicas;

	//bi-directional many-to-one association to SiacDFormaGiuridicaCatFin
	@ManyToOne
	@JoinColumn(name="forma_giuridica_cat_id")
	private SiacDFormaGiuridicaCatFin siacDFormaGiuridicaCat;

	//bi-directional many-to-one association to SiacDFormaGiuridicaTipoFin
	@ManyToOne
	@JoinColumn(name="forma_giuridica_tipo_id")
	private SiacDFormaGiuridicaTipoFin siacDFormaGiuridicaTipo;

	public SiacTFormaGiuridicaFin() {
	}

	public Integer getFormaGiuridicaId() {
		return this.formaGiuridicaId;
	}

	public void setFormaGiuridicaId(Integer formaGiuridicaId) {
		this.formaGiuridicaId = formaGiuridicaId;
	}

	public String getFormaGiuridicaDesc() {
		return this.formaGiuridicaDesc;
	}

	public void setFormaGiuridicaDesc(String formaGiuridicaDesc) {
		this.formaGiuridicaDesc = formaGiuridicaDesc;
	}

	public String getFormaGiuridicaIstatCodice() {
		return this.formaGiuridicaIstatCodice;
	}

	public void setFormaGiuridicaIstatCodice(String formaGiuridicaIstatCodice) {
		this.formaGiuridicaIstatCodice = formaGiuridicaIstatCodice;
	}

	public List<SiacRFormaGiuridicaFin> getSiacRFormaGiuridicas() {
		return this.siacRFormaGiuridicas;
	}

	public void setSiacRFormaGiuridicas(List<SiacRFormaGiuridicaFin> siacRFormaGiuridicas) {
		this.siacRFormaGiuridicas = siacRFormaGiuridicas;
	}

	public SiacRFormaGiuridicaFin addSiacRFormaGiuridica(SiacRFormaGiuridicaFin siacRFormaGiuridica) {
		getSiacRFormaGiuridicas().add(siacRFormaGiuridica);
		siacRFormaGiuridica.setSiacTFormaGiuridica(this);

		return siacRFormaGiuridica;
	}

	public SiacRFormaGiuridicaFin removeSiacRFormaGiuridica(SiacRFormaGiuridicaFin siacRFormaGiuridica) {
		getSiacRFormaGiuridicas().remove(siacRFormaGiuridica);
		siacRFormaGiuridica.setSiacTFormaGiuridica(null);

		return siacRFormaGiuridica;
	}

	public SiacDFormaGiuridicaCatFin getSiacDFormaGiuridicaCat() {
		return this.siacDFormaGiuridicaCat;
	}

	public void setSiacDFormaGiuridicaCat(SiacDFormaGiuridicaCatFin siacDFormaGiuridicaCat) {
		this.siacDFormaGiuridicaCat = siacDFormaGiuridicaCat;
	}

	public SiacDFormaGiuridicaTipoFin getSiacDFormaGiuridicaTipo() {
		return this.siacDFormaGiuridicaTipo;
	}

	public void setSiacDFormaGiuridicaTipo(SiacDFormaGiuridicaTipoFin siacDFormaGiuridicaTipo) {
		this.siacDFormaGiuridicaTipo = siacDFormaGiuridicaTipo;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.formaGiuridicaId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.formaGiuridicaId = uid;
	}
}