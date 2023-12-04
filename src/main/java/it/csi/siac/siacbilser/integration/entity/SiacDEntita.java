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
 * The persistent class for the siac_d_entita database table.
 * 
 */
@Entity
@Table(name="siac_d_entita")
@NamedQuery(name="SiacDEntita.findAll", query="SELECT s FROM SiacDEntita s")
public class SiacDEntita extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The entita id. */
	@Id
	@SequenceGenerator(name="SIAC_D_ENTITA_ENTITAID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_ENTITA_ENTITA_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_ENTITA_ENTITAID_GENERATOR")
	@Column(name="entita_id")
	private Integer entitaId;

	/** The entita code. */
	@Column(name="entita_code")
	private String entitaCode;

	/** The entita desc. */
	@Column(name="entita_desc")
	private String entitaDesc;

	//bi-directional many-to-one association to SiacRAttrEntita
	/** The siac r attr entitas. */
	@OneToMany(mappedBy="siacDEntita")
	private List<SiacRAttrEntita> siacRAttrEntitas;

	/**
	 * Instantiates a new siac d entita.
	 */
	public SiacDEntita() {
	}

	/**
	 * Gets the entita id.
	 *
	 * @return the entita id
	 */
	public Integer getEntitaId() {
		return this.entitaId;
	}

	/**
	 * Sets the entita id.
	 *
	 * @param entitaId the new entita id
	 */
	public void setEntitaId(Integer entitaId) {
		this.entitaId = entitaId;
	}

	/**
	 * Gets the entita code.
	 *
	 * @return the entita code
	 */
	public String getEntitaCode() {
		return this.entitaCode;
	}

	/**
	 * Sets the entita code.
	 *
	 * @param entitaCode the new entita code
	 */
	public void setEntitaCode(String entitaCode) {
		this.entitaCode = entitaCode;
	}

	/**
	 * Gets the entita desc.
	 *
	 * @return the entita desc
	 */
	public String getEntitaDesc() {
		return this.entitaDesc;
	}

	/**
	 * Sets the entita desc.
	 *
	 * @param entitaDesc the new entita desc
	 */
	public void setEntitaDesc(String entitaDesc) {
		this.entitaDesc = entitaDesc;
	}

	/**
	 * Gets the siac r attr entitas.
	 *
	 * @return the siac r attr entitas
	 */
	public List<SiacRAttrEntita> getSiacRAttrEntitas() {
		return this.siacRAttrEntitas;
	}

	/**
	 * Sets the siac r attr entitas.
	 *
	 * @param siacRAttrEntitas the new siac r attr entitas
	 */
	public void setSiacRAttrEntitas(List<SiacRAttrEntita> siacRAttrEntitas) {
		this.siacRAttrEntitas = siacRAttrEntitas;
	}

	/**
	 * Adds the siac r attr entita.
	 *
	 * @param siacRAttrEntita the siac r attr entita
	 * @return the siac r attr entita
	 */
	public SiacRAttrEntita addSiacRAttrEntita(SiacRAttrEntita siacRAttrEntita) {
		getSiacRAttrEntitas().add(siacRAttrEntita);
		siacRAttrEntita.setSiacDEntita(this);

		return siacRAttrEntita;
	}

	/**
	 * Removes the siac r attr entita.
	 *
	 * @param siacRAttrEntita the siac r attr entita
	 * @return the siac r attr entita
	 */
	public SiacRAttrEntita removeSiacRAttrEntita(SiacRAttrEntita siacRAttrEntita) {
		getSiacRAttrEntitas().remove(siacRAttrEntita);
		siacRAttrEntita.setSiacDEntita(null);

		return siacRAttrEntita;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return entitaId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.entitaId = uid;
		
	}

}