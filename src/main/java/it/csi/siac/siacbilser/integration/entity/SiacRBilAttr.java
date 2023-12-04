/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the siac_r_bil_attr database table.
 * 
 */
@Entity
@Table(name="siac_r_bil_attr")
@NamedQuery(name="SiacRBilAttr.findAll", query="SELECT s FROM SiacRBilAttr s")
public class SiacRBilAttr extends SiacTEnteBase implements SiacRAttrBase<SiacTBil> {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The bil attr id. */
	@Id
	@SequenceGenerator(name="SIAC_R_BIL_ATTR_BILATTRID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_BIL_ATTR_BIL_ATTR_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_BIL_ATTR_BILATTRID_GENERATOR")
	@Column(name="bil_attr_id")
	private Integer bilAttrId;

	/** The boolean_. */
	@Column(name="boolean")
	private String boolean_;

	/** The numerico. */
	private BigDecimal numerico;

	/** The percentuale. */
	private BigDecimal percentuale;

	/** The tabella id. */
	@Column(name="tabella_id")
	private Integer tabellaId;

	/** The testo. */
	private String testo;

	//bi-directional many-to-one association to SiacTAttr
	/** The siac t attr. */
	@ManyToOne
	@JoinColumn(name="attr_id")
	private SiacTAttr siacTAttr;

	//bi-directional many-to-one association to SiacTBil
	/** The siac t bil. */
	@ManyToOne
	@JoinColumn(name="bil_id")
	private SiacTBil siacTBil;

	/**
	 * Instantiates a new siac r bil attr.
	 */
	public SiacRBilAttr() {
	}

	/**
	 * Gets the bil attr id.
	 * 
	 * @return the bilAttrId
	 */
	public Integer getBilAttrId() {
		return bilAttrId;
	}



	/**
	 * Sets the bil attr id.
	 * 
	 * @param bilAttrId the bilAttrId to set
	 */
	public void setBilAttrId(Integer bilAttrId) {
		this.bilAttrId = bilAttrId;
	}

	@Override
	public String getBoolean_() {
		return this.boolean_;
	}

	@Override
	public void setBoolean_(String boolean_) {
		this.boolean_ = boolean_;
	}


	@Override
	public BigDecimal getNumerico() {
		return this.numerico;
	}

	@Override
	public void setNumerico(BigDecimal numerico) {
		this.numerico = numerico;
	}

	@Override
	public BigDecimal getPercentuale() {
		return this.percentuale;
	}

	@Override
	public void setPercentuale(BigDecimal percentuale) {
		this.percentuale = percentuale;
	}

	@Override
	public Integer getTabellaId() {
		return this.tabellaId;
	}

	@Override
	public void setTabellaId(Integer tabellaId) {
		this.tabellaId = tabellaId;
	}

	@Override
	public String getTesto() {
		return this.testo;
	}

	@Override
	public void setTesto(String testo) {
		this.testo = testo;
	}

	@Override
	public SiacTAttr getSiacTAttr() {
		return this.siacTAttr;
	}

	@Override
	public void setSiacTAttr(SiacTAttr siacTAttr) {
		this.siacTAttr = siacTAttr;
	}

	/**
	 * Gets the siac t bil.
	 *
	 * @return the siac t bil
	 */
	public SiacTBil getSiacTBil() {
		return this.siacTBil;
	}

	/**
	 * Sets the siac t bil.
	 *
	 * @param siacTBil the new siac t bil
	 */
	public void setSiacTBil(SiacTBil siacTBil) {
		this.siacTBil = siacTBil;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return bilAttrId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.bilAttrId = uid;
		
	}

	@Override
	public void setEntity(SiacTBil entity) {
		setSiacTBil(entity);
	}

}