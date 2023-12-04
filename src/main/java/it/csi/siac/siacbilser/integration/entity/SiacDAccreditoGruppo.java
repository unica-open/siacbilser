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
 * The persistent class for the siac_d_accredito_gruppo database table.
 * 
 */
@Entity
@Table(name="siac_d_accredito_gruppo")
@NamedQuery(name="SiacDAccreditoGruppo.findAll", query="SELECT s FROM SiacDAccreditoGruppo s")
public class SiacDAccreditoGruppo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The accredito gruppo id. */
	@Id
	@SequenceGenerator(name="SIAC_D_ACCREDITO_GRUPPO_ACCREDITOGRUPPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_ACCREDITO_GRUPPO_ACCREDITO_GRUPPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_ACCREDITO_GRUPPO_ACCREDITOGRUPPOID_GENERATOR")
	@Column(name="accredito_gruppo_id")
	private Integer accreditoGruppoId;

	/** The accredito gruppo code. */
	@Column(name="accredito_gruppo_code")
	private String accreditoGruppoCode;

	/** The accredito gruppo desc. */
	@Column(name="accredito_gruppo_desc")
	private String accreditoGruppoDesc;
	

	//bi-directional many-to-one association to SiacDAccreditoTipo
	/** The siac d accredito tipos. */
	@OneToMany(mappedBy="siacDAccreditoGruppo")
	private List<SiacDAccreditoTipo> siacDAccreditoTipos;

	/**
	 * Instantiates a new siac d accredito gruppo.
	 */
	public SiacDAccreditoGruppo() {
	}

	/**
	 * Gets the accredito gruppo id.
	 *
	 * @return the accredito gruppo id
	 */
	public Integer getAccreditoGruppoId() {
		return this.accreditoGruppoId;
	}

	/**
	 * Sets the accredito gruppo id.
	 *
	 * @param accreditoGruppoId the new accredito gruppo id
	 */
	public void setAccreditoGruppoId(Integer accreditoGruppoId) {
		this.accreditoGruppoId = accreditoGruppoId;
	}

	/**
	 * Gets the accredito gruppo code.
	 *
	 * @return the accredito gruppo code
	 */
	public String getAccreditoGruppoCode() {
		return this.accreditoGruppoCode;
	}

	/**
	 * Sets the accredito gruppo code.
	 *
	 * @param accreditoGruppoCode the new accredito gruppo code
	 */
	public void setAccreditoGruppoCode(String accreditoGruppoCode) {
		this.accreditoGruppoCode = accreditoGruppoCode;
	}

	/**
	 * Gets the accredito gruppo desc.
	 *
	 * @return the accredito gruppo desc
	 */
	public String getAccreditoGruppoDesc() {
		return this.accreditoGruppoDesc;
	}

	/**
	 * Sets the accredito gruppo desc.
	 *
	 * @param accreditoGruppoDesc the new accredito gruppo desc
	 */
	public void setAccreditoGruppoDesc(String accreditoGruppoDesc) {
		this.accreditoGruppoDesc = accreditoGruppoDesc;
	}



	/**
	 * Gets the siac d accredito tipos.
	 *
	 * @return the siac d accredito tipos
	 */
	public List<SiacDAccreditoTipo> getSiacDAccreditoTipos() {
		return this.siacDAccreditoTipos;
	}

	/**
	 * Sets the siac d accredito tipos.
	 *
	 * @param siacDAccreditoTipos the new siac d accredito tipos
	 */
	public void setSiacDAccreditoTipos(List<SiacDAccreditoTipo> siacDAccreditoTipos) {
		this.siacDAccreditoTipos = siacDAccreditoTipos;
	}

	/**
	 * Adds the siac d accredito tipo.
	 *
	 * @param siacDAccreditoTipo the siac d accredito tipo
	 * @return the siac d accredito tipo
	 */
	public SiacDAccreditoTipo addSiacDAccreditoTipo(SiacDAccreditoTipo siacDAccreditoTipo) {
		getSiacDAccreditoTipos().add(siacDAccreditoTipo);
		siacDAccreditoTipo.setSiacDAccreditoGruppo(this);

		return siacDAccreditoTipo;
	}

	/**
	 * Removes the siac d accredito tipo.
	 *
	 * @param siacDAccreditoTipo the siac d accredito tipo
	 * @return the siac d accredito tipo
	 */
	public SiacDAccreditoTipo removeSiacDAccreditoTipo(SiacDAccreditoTipo siacDAccreditoTipo) {
		getSiacDAccreditoTipos().remove(siacDAccreditoTipo);
		siacDAccreditoTipo.setSiacDAccreditoGruppo(null);

		return siacDAccreditoTipo;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return accreditoGruppoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.accreditoGruppoId = uid;
		
	}

}