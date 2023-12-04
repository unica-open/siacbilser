/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_d_modello database table.
 * 
 */
@Entity
@Table(name="siac_d_modello")
@NamedQuery(name="SiacDModello.findAll", query="SELECT s FROM SiacDModello s")
public class SiacDModello extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The model id. */
	@Id
	@SequenceGenerator(name="SIAC_D_MODELLO_MODELID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_MODELLO_MODEL_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_MODELLO_MODELID_GENERATOR")
	@Column(name="model_id")
	private Integer modelId;

	/** The model code. */
	@Column(name="model_code")
	private String modelCode;

	/** The model desc. */
	@Column(name="model_desc")
	private String modelDesc;



	//bi-directional many-to-one association to SiacDCausale
	/** The siac d causales. */
	@OneToMany(mappedBy="siacDModello")
	private List<SiacDCausale> siacDCausales;


	/**
	 * Instantiates a new siac d modello.
	 */
	public SiacDModello() {
	}

	/**
	 * Gets the model id.
	 *
	 * @return the model id
	 */
	public Integer getModelId() {
		return this.modelId;
	}

	/**
	 * Sets the model id.
	 *
	 * @param modelId the new model id
	 */
	public void setModelId(Integer modelId) {
		this.modelId = modelId;
	}

	/**
	 * Gets the model code.
	 *
	 * @return the model code
	 */
	public String getModelCode() {
		return this.modelCode;
	}

	/**
	 * Sets the model code.
	 *
	 * @param modelCode the new model code
	 */
	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}

	/**
	 * Gets the model desc.
	 *
	 * @return the model desc
	 */
	public String getModelDesc() {
		return this.modelDesc;
	}

	/**
	 * Sets the model desc.
	 *
	 * @param modelDesc the new model desc
	 */
	public void setModelDesc(String modelDesc) {
		this.modelDesc = modelDesc;
	}

	/**
	 * Gets the siac d causales.
	 *
	 * @return the siac d causales
	 */
	public List<SiacDCausale> getSiacDCausales() {
		return this.siacDCausales;
	}

	/**
	 * Sets the siac d causales.
	 *
	 * @param siacDCausales the new siac d causales
	 */
	public void setSiacDCausales(List<SiacDCausale> siacDCausales) {
		this.siacDCausales = siacDCausales;
	}

	/**
	 * Adds the siac d causale.
	 *
	 * @param siacDCausale the siac d causale
	 * @return the siac d causale
	 */
	public SiacDCausale addSiacDCausale(SiacDCausale siacDCausale) {
		getSiacDCausales().add(siacDCausale);
		siacDCausale.setSiacDModello(this);

		return siacDCausale;
	}

	/**
	 * Removes the siac d causale.
	 *
	 * @param siacDCausale the siac d causale
	 * @return the siac d causale
	 */
	public SiacDCausale removeSiacDCausale(SiacDCausale siacDCausale) {
		getSiacDCausales().remove(siacDCausale);
		siacDCausale.setSiacDModello(null);

		return siacDCausale;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return modelId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.modelId = uid;
	}


}