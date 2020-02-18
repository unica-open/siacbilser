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
 * The persistent class for the siac_d_modello database table.
 * 
 */
@Entity
@Table(name="siac_d_modello")
public class SiacDModelloFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="model_id")
	private Integer modelId;

	@Column(name="model_code")
	private String modelCode;

	@Column(name="model_desc")
	private String modelDesc;

	//bi-directional many-to-one association to SiacDCausaleFin
	@OneToMany(mappedBy="siacDModello")
	private List<SiacDCausaleFin> siacDCausales;

	public SiacDModelloFin() {
	}

	public Integer getModelId() {
		return this.modelId;
	}

	public void setModelId(Integer modelId) {
		this.modelId = modelId;
	}

	public String getModelCode() {
		return this.modelCode;
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}

	public String getModelDesc() {
		return this.modelDesc;
	}

	public void setModelDesc(String modelDesc) {
		this.modelDesc = modelDesc;
	}

	public List<SiacDCausaleFin> getSiacDCausales() {
		return this.siacDCausales;
	}

	public void setSiacDCausales(List<SiacDCausaleFin> siacDCausales) {
		this.siacDCausales = siacDCausales;
	}

	public SiacDCausaleFin addSiacDCausale(SiacDCausaleFin siacDCausale) {
		getSiacDCausales().add(siacDCausale);
		siacDCausale.setSiacDModello(this);

		return siacDCausale;
	}

	public SiacDCausaleFin removeSiacDCausale(SiacDCausaleFin siacDCausale) {
		getSiacDCausales().remove(siacDCausale);
		siacDCausale.setSiacDModello(null);

		return siacDCausale;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.modelId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.modelId = uid;
	}
}