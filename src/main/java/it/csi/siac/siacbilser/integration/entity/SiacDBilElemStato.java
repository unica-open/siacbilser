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
 * The persistent class for the siac_d_bil_elem_stato database table.
 * 
 */
@Entity
@Table(name="siac_d_bil_elem_stato")
@NamedQuery(name="SiacDBilElemStato.findAll", query="SELECT s FROM SiacDBilElemStato s")
public class SiacDBilElemStato extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The elem stato id. */
	@Id
	@SequenceGenerator(name="SIAC_D_BIL_ELEM_STATO_ELEMSTATOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_BIL_ELEM_STATO_ELEM_STATO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_BIL_ELEM_STATO_ELEMSTATOID_GENERATOR")
	@Column(name="elem_stato_id")
	private Integer elemStatoId;

	/** The elem stato code. */
	@Column(name="elem_stato_code")
	private String elemStatoCode;

	/** The elem stato desc. */
	@Column(name="elem_stato_desc")
	private String elemStatoDesc;

	//bi-directional many-to-one association to SiacRBilElemStato
	/** The siac r bil elem statos. */
	@OneToMany(mappedBy="siacDBilElemStato")
	private List<SiacRBilElemStato> siacRBilElemStatos;

	/**
	 * Instantiates a new siac d bil elem stato.
	 */
	public SiacDBilElemStato() {
	}

	/**
	 * Gets the elem stato id.
	 *
	 * @return the elem stato id
	 */
	public Integer getElemStatoId() {
		return this.elemStatoId;
	}

	/**
	 * Sets the elem stato id.
	 *
	 * @param elemStatoId the new elem stato id
	 */
	public void setElemStatoId(Integer elemStatoId) {
		this.elemStatoId = elemStatoId;
	}

	/**
	 * Gets the elem stato code.
	 *
	 * @return the elem stato code
	 */
	public String getElemStatoCode() {
		return this.elemStatoCode;
	}

	/**
	 * Sets the elem stato code.
	 *
	 * @param elemStatoCode the new elem stato code
	 */
	public void setElemStatoCode(String elemStatoCode) {
		this.elemStatoCode = elemStatoCode;
	}

	/**
	 * Gets the elem stato desc.
	 *
	 * @return the elem stato desc
	 */
	public String getElemStatoDesc() {
		return this.elemStatoDesc;
	}

	/**
	 * Sets the elem stato desc.
	 *
	 * @param elemStatoDesc the new elem stato desc
	 */
	public void setElemStatoDesc(String elemStatoDesc) {
		this.elemStatoDesc = elemStatoDesc;
	}

	/**
	 * Gets the siac r bil elem statos.
	 *
	 * @return the siac r bil elem statos
	 */
	public List<SiacRBilElemStato> getSiacRBilElemStatos() {
		return this.siacRBilElemStatos;
	}

	/**
	 * Sets the siac r bil elem statos.
	 *
	 * @param siacRBilElemStatos the new siac r bil elem statos
	 */
	public void setSiacRBilElemStatos(List<SiacRBilElemStato> siacRBilElemStatos) {
		this.siacRBilElemStatos = siacRBilElemStatos;
	}

	/**
	 * Adds the siac r bil elem stato.
	 *
	 * @param siacRBilElemStato the siac r bil elem stato
	 * @return the siac r bil elem stato
	 */
	public SiacRBilElemStato addSiacRBilElemStato(SiacRBilElemStato siacRBilElemStato) {
		getSiacRBilElemStatos().add(siacRBilElemStato);
		siacRBilElemStato.setSiacDBilElemStato(this);

		return siacRBilElemStato;
	}

	/**
	 * Removes the siac r bil elem stato.
	 *
	 * @param siacRBilElemStato the siac r bil elem stato
	 * @return the siac r bil elem stato
	 */
	public SiacRBilElemStato removeSiacRBilElemStato(SiacRBilElemStato siacRBilElemStato) {
		getSiacRBilElemStatos().remove(siacRBilElemStato);
		siacRBilElemStato.setSiacDBilElemStato(null);

		return siacRBilElemStato;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return elemStatoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.elemStatoId = uid;
	}

}