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
 * The persistent class for the siac_r_attr_entita database table.
 * 
 */
@Entity
@Table(name="siac_r_attr_entita")
@NamedQuery(name="SiacRAttrEntita.findAll", query="SELECT s FROM SiacRAttrEntita s")
public class SiacRAttrEntita extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The attr entita id. */
	@Id
	@SequenceGenerator(name="SIAC_R_ATTR_ENTITA_ATTRENTITAID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_ATTR_ENTITA_ATTR_ENTITA_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_ATTR_ENTITA_ATTRENTITAID_GENERATOR")
	@Column(name="attr_entita_id")
	private Integer attrEntitaId;

	//bi-directional many-to-one association to SiacDEntita
	/** The siac d entita. */
	@ManyToOne
	@JoinColumn(name="entita_id")
	private SiacDEntita siacDEntita;

	//bi-directional many-to-one association to SiacTAttr
	/** The siac t attr. */
	@ManyToOne
	@JoinColumn(name="attr_id")
	private SiacTAttr siacTAttr;

	/**
	 * Instantiates a new siac r attr entita.
	 */
	public SiacRAttrEntita() {
	}

	/**
	 * Gets the attr entita id.
	 *
	 * @return the attr entita id
	 */
	public Integer getAttrEntitaId() {
		return this.attrEntitaId;
	}

	/**
	 * Sets the attr entita id.
	 *
	 * @param attrEntitaId the new attr entita id
	 */
	public void setAttrEntitaId(Integer attrEntitaId) {
		this.attrEntitaId = attrEntitaId;
	}

	/**
	 * Gets the siac d entita.
	 *
	 * @return the siac d entita
	 */
	public SiacDEntita getSiacDEntita() {
		return this.siacDEntita;
	}

	/**
	 * Sets the siac d entita.
	 *
	 * @param siacDEntita the new siac d entita
	 */
	public void setSiacDEntita(SiacDEntita siacDEntita) {
		this.siacDEntita = siacDEntita;
	}

	/**
	 * Gets the siac t attr.
	 *
	 * @return the siac t attr
	 */
	public SiacTAttr getSiacTAttr() {
		return this.siacTAttr;
	}

	/**
	 * Sets the siac t attr.
	 *
	 * @param siacTAttr the new siac t attr
	 */
	public void setSiacTAttr(SiacTAttr siacTAttr) {
		this.siacTAttr = siacTAttr;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return attrEntitaId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.attrEntitaId = uid;		
	}

}