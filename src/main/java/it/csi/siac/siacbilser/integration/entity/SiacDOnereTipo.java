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
 * The persistent class for the siac_d_onere_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_onere_tipo")
@NamedQuery(name="SiacDOnereTipo.findAll", query="SELECT s FROM SiacDOnereTipo s")
public class SiacDOnereTipo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The onere tipo id. */
	@Id
	@SequenceGenerator(name="SIAC_D_ONERE_TIPO_ONERETIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_ONERE_TIPO_ONERE_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_ONERE_TIPO_ONERETIPOID_GENERATOR")
	@Column(name="onere_tipo_id")
	private Integer onereTipoId;

	/** The onere tipo code. */
	@Column(name="onere_tipo_code")
	private String onereTipoCode;

	/** The onere tipo desc. */
	@Column(name="onere_tipo_desc")
	private String onereTipoDesc;

	//bi-directional many-to-one association to SiacDOnere
	/** The siac d oneres. */
	@OneToMany(mappedBy="siacDOnereTipo")
	private List<SiacDOnere> siacDOneres;

	/**
	 * Instantiates a new siac d onere tipo.
	 */
	public SiacDOnereTipo() {
	}

	/**
	 * Gets the onere tipo id.
	 *
	 * @return the onere tipo id
	 */
	public Integer getOnereTipoId() {
		return this.onereTipoId;
	}

	/**
	 * Sets the onere tipo id.
	 *
	 * @param onereTipoId the new onere tipo id
	 */
	public void setOnereTipoId(Integer onereTipoId) {
		this.onereTipoId = onereTipoId;
	}

	/**
	 * Gets the onere tipo code.
	 *
	 * @return the onere tipo code
	 */
	public String getOnereTipoCode() {
		return this.onereTipoCode;
	}

	/**
	 * Sets the onere tipo code.
	 *
	 * @param onereTipoCode the new onere tipo code
	 */
	public void setOnereTipoCode(String onereTipoCode) {
		this.onereTipoCode = onereTipoCode;
	}

	/**
	 * Gets the onere tipo desc.
	 *
	 * @return the onere tipo desc
	 */
	public String getOnereTipoDesc() {
		return this.onereTipoDesc;
	}

	/**
	 * Sets the onere tipo desc.
	 *
	 * @param onereTipoDesc the new onere tipo desc
	 */
	public void setOnereTipoDesc(String onereTipoDesc) {
		this.onereTipoDesc = onereTipoDesc;
	}

	/**
	 * Gets the siac d oneres.
	 *
	 * @return the siac d oneres
	 */
	public List<SiacDOnere> getSiacDOneres() {
		return this.siacDOneres;
	}

	/**
	 * Sets the siac d oneres.
	 *
	 * @param siacDOneres the new siac d oneres
	 */
	public void setSiacDOneres(List<SiacDOnere> siacDOneres) {
		this.siacDOneres = siacDOneres;
	}

	/**
	 * Adds the siac d onere.
	 *
	 * @param siacDOnere the siac d onere
	 * @return the siac d onere
	 */
	public SiacDOnere addSiacDOnere(SiacDOnere siacDOnere) {
		getSiacDOneres().add(siacDOnere);
		siacDOnere.setSiacDOnereTipo(this);

		return siacDOnere;
	}

	/**
	 * Removes the siac d onere.
	 *
	 * @param siacDOnere the siac d onere
	 * @return the siac d onere
	 */
	public SiacDOnere removeSiacDOnere(SiacDOnere siacDOnere) {
		getSiacDOneres().remove(siacDOnere);
		siacDOnere.setSiacDOnereTipo(null);

		return siacDOnere;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return onereTipoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.onereTipoId = uid;
		
	}

}