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
 * The persistent class for the siac_r_bil_elem_iva_attivita database table.
 * 
 */
@Entity
@Table(name="siac_r_bil_elem_iva_attivita")
@NamedQuery(name="SiacRBilElemIvaAttivita.findAll", query="SELECT s FROM SiacRBilElemIvaAttivita s")
public class SiacRBilElemIvaAttivita extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The bil elem ivaatt id. */
	@Id
	@SequenceGenerator(name="SIAC_R_BIL_ELEM_IVA_ATTIVITA_BILELEMIVAATTID_GENERATOR", sequenceName="SIAC_R_BIL_ELEM_IVA_ATTIVITA_BIL_ELEM_IVAATT_ID_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_BIL_ELEM_IVA_ATTIVITA_BILELEMIVAATTID_GENERATOR")
	@Column(name="bil_elem_ivaatt_id")
	private Integer bilElemIvaattId;

	//bi-directional many-to-one association to SiacTBilElem
	/** The siac t bil elem. */
	@ManyToOne
	@JoinColumn(name="elem_id")
	private SiacTBilElem siacTBilElem;

	//bi-directional many-to-one association to SiacTIvaAttivita
	/** The siac t iva attivita. */
	@ManyToOne
	@JoinColumn(name="ivaatt_id")
	private SiacTIvaAttivita siacTIvaAttivita;

	/**
	 * Instantiates a new siac r bil elem iva attivita.
	 */
	public SiacRBilElemIvaAttivita() {
	}

	/**
	 * Gets the bil elem ivaatt id.
	 *
	 * @return the bil elem ivaatt id
	 */
	public Integer getBilElemIvaattId() {
		return this.bilElemIvaattId;
	}

	/**
	 * Sets the bil elem ivaatt id.
	 *
	 * @param bilElemIvaattId the new bil elem ivaatt id
	 */
	public void setBilElemIvaattId(Integer bilElemIvaattId) {
		this.bilElemIvaattId = bilElemIvaattId;
	}

	/**
	 * Gets the siac t bil elem.
	 *
	 * @return the siac t bil elem
	 */
	public SiacTBilElem getSiacTBilElem() {
		return this.siacTBilElem;
	}

	/**
	 * Sets the siac t bil elem.
	 *
	 * @param siacTBilElem the new siac t bil elem
	 */
	public void setSiacTBilElem(SiacTBilElem siacTBilElem) {
		this.siacTBilElem = siacTBilElem;
	}

	/**
	 * Gets the siac t iva attivita.
	 *
	 * @return the siac t iva attivita
	 */
	public SiacTIvaAttivita getSiacTIvaAttivita() {
		return this.siacTIvaAttivita;
	}

	/**
	 * Sets the siac t iva attivita.
	 *
	 * @param siacTIvaAttivita the new siac t iva attivita
	 */
	public void setSiacTIvaAttivita(SiacTIvaAttivita siacTIvaAttivita) {
		this.siacTIvaAttivita = siacTIvaAttivita;
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return bilElemIvaattId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.bilElemIvaattId = uid;
	}

}