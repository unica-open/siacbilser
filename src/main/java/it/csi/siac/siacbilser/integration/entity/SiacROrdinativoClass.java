/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

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


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_r_ordinativo_class database table.
 * 
 */
@Entity
@Table(name="siac_r_ordinativo_class")
@NamedQuery(name="SiacROrdinativoClass.findAll", query="SELECT s FROM SiacROrdinativoClass s")
public class SiacROrdinativoClass extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The ord classif id. */
	@Id
	@SequenceGenerator(name="SIAC_R_ORDINATIVO_CLASS_ORDCLASSIFID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_ORDINATIVO_CLASS_ORD_CLASSIF_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_ORDINATIVO_CLASS_ORDCLASSIFID_GENERATOR")
	@Column(name="ord_classif_id")
	private Integer ordClassifId;

	//bi-directional many-to-one association to SiacTClass
	/** The siac t class. */
	@ManyToOne
	@JoinColumn(name="classif_id")
	private SiacTClass siacTClass;

	//bi-directional many-to-one association to SiacTOrdinativo
	/** The siac t ordinativo. */
	@ManyToOne
	@JoinColumn(name="ord_id")
	private SiacTOrdinativo siacTOrdinativo;

	/**
	 * Instantiates a new siac r ordinativo class.
	 */
	public SiacROrdinativoClass() {
	}

	/**
	 * Gets the ord classif id.
	 *
	 * @return the ord classif id
	 */
	public Integer getOrdClassifId() {
		return this.ordClassifId;
	}

	/**
	 * Sets the ord classif id.
	 *
	 * @param ordClassifId the new ord classif id
	 */
	public void setOrdClassifId(Integer ordClassifId) {
		this.ordClassifId = ordClassifId;
	}

	/**
	 * Gets the siac t class.
	 *
	 * @return the siac t class
	 */
	public SiacTClass getSiacTClass() {
		return this.siacTClass;
	}

	/**
	 * Sets the siac t class.
	 *
	 * @param siacTClass the new siac t class
	 */
	public void setSiacTClass(SiacTClass siacTClass) {
		this.siacTClass = siacTClass;
	}

	/**
	 * Gets the siac t ordinativo.
	 *
	 * @return the siac t ordinativo
	 */
	public SiacTOrdinativo getSiacTOrdinativo() {
		return this.siacTOrdinativo;
	}

	/**
	 * Sets the siac t ordinativo.
	 *
	 * @param siacTOrdinativo the new siac t ordinativo
	 */
	public void setSiacTOrdinativo(SiacTOrdinativo siacTOrdinativo) {
		this.siacTOrdinativo = siacTOrdinativo;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return ordClassifId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.ordClassifId = uid;
	}

}