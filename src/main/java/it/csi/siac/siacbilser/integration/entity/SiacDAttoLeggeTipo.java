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
 * The persistent class for the siac_d_atto_legge_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_atto_legge_tipo")
@NamedQuery(name="SiacDAttoLeggeTipo.findAll", query="SELECT s FROM SiacDAttoLeggeTipo s")
public class SiacDAttoLeggeTipo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The attolegge tipo id. */
	@Id
	@SequenceGenerator(name="SIAC_D_ATTO_LEGGE_TIPO_ATTOLEGGETIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_ATTO_LEGGE_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_ATTO_LEGGE_TIPO_ATTOLEGGETIPOID_GENERATOR")
	@Column(name="attolegge_tipo_id")
	private Integer attoleggeTipoId;

	/** The attolegge tipo code. */
	@Column(name="attolegge_tipo_code")
	private String attoleggeTipoCode;

	/** The attolegge tipo desc. */
	@Column(name="attolegge_tipo_desc")
	private String attoleggeTipoDesc;

	//bi-directional many-to-one association to SiacTAttoLegge
	/** The siac t atto legges. */
	@OneToMany(mappedBy="siacDAttoLeggeTipo")
	private List<SiacTAttoLegge> siacTAttoLegges;

	/**
	 * Instantiates a new siac d atto legge tipo.
	 */
	public SiacDAttoLeggeTipo() {
	}

	/**
	 * Gets the attolegge tipo id.
	 *
	 * @return the attolegge tipo id
	 */
	public Integer getAttoleggeTipoId() {
		return this.attoleggeTipoId;
	}

	/**
	 * Sets the attolegge tipo id.
	 *
	 * @param attoleggeTipoId the new attolegge tipo id
	 */
	public void setAttoleggeTipoId(Integer attoleggeTipoId) {
		this.attoleggeTipoId = attoleggeTipoId;
	}

	/**
	 * Gets the attolegge tipo code.
	 *
	 * @return the attolegge tipo code
	 */
	public String getAttoleggeTipoCode() {
		return this.attoleggeTipoCode;
	}

	/**
	 * Sets the attolegge tipo code.
	 *
	 * @param attoleggeTipoCode the new attolegge tipo code
	 */
	public void setAttoleggeTipoCode(String attoleggeTipoCode) {
		this.attoleggeTipoCode = attoleggeTipoCode;
	}

	/**
	 * Gets the attolegge tipo desc.
	 *
	 * @return the attolegge tipo desc
	 */
	public String getAttoleggeTipoDesc() {
		return this.attoleggeTipoDesc;
	}

	/**
	 * Sets the attolegge tipo desc.
	 *
	 * @param attoleggeTipoDesc the new attolegge tipo desc
	 */
	public void setAttoleggeTipoDesc(String attoleggeTipoDesc) {
		this.attoleggeTipoDesc = attoleggeTipoDesc;
	}

	/**
	 * Gets the siac t atto legges.
	 *
	 * @return the siac t atto legges
	 */
	public List<SiacTAttoLegge> getSiacTAttoLegges() {
		return this.siacTAttoLegges;
	}

	/**
	 * Sets the siac t atto legges.
	 *
	 * @param siacTAttoLegges the new siac t atto legges
	 */
	public void setSiacTAttoLegges(List<SiacTAttoLegge> siacTAttoLegges) {
		this.siacTAttoLegges = siacTAttoLegges;
	}

	/**
	 * Adds the siac t atto legge.
	 *
	 * @param siacTAttoLegge the siac t atto legge
	 * @return the siac t atto legge
	 */
	public SiacTAttoLegge addSiacTAttoLegge(SiacTAttoLegge siacTAttoLegge) {
		getSiacTAttoLegges().add(siacTAttoLegge);
		siacTAttoLegge.setSiacDAttoLeggeTipo(this);

		return siacTAttoLegge;
	}

	/**
	 * Removes the siac t atto legge.
	 *
	 * @param siacTAttoLegge the siac t atto legge
	 * @return the siac t atto legge
	 */
	public SiacTAttoLegge removeSiacTAttoLegge(SiacTAttoLegge siacTAttoLegge) {
		getSiacTAttoLegges().remove(siacTAttoLegge);
		siacTAttoLegge.setSiacDAttoLeggeTipo(null);

		return siacTAttoLegge;
	}


	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return attoleggeTipoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.attoleggeTipoId = uid;
	}
}