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
 * The persistent class for the siac_d_mutuo_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_mutuo_tipo")
@NamedQuery(name="SiacDMutuoTipo.findAll", query="SELECT s FROM SiacDMutuoTipo s")
public class SiacDMutuoTipo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The mut tipo id. */
	@Id
	@SequenceGenerator(name="SIAC_D_MUTUO_TIPO_MUTTIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_MUTUO_TIPO_MUT_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_MUTUO_TIPO_MUTTIPOID_GENERATOR")
	@Column(name="mut_tipo_id")
	private Integer mutTipoId;

	/** The mut tipo code. */
	@Column(name="mut_tipo_code")
	private String mutTipoCode;

	/** The mut tipo desc. */
	@Column(name="mut_tipo_desc")
	private String mutTipoDesc;

	//bi-directional many-to-one association to SiacTMutuo
	/** The siac t mutuos. */
	@OneToMany(mappedBy="siacDMutuoTipo")
	private List<SiacTMutuo> siacTMutuos;

	/**
	 * Instantiates a new siac d mutuo tipo.
	 */
	public SiacDMutuoTipo() {
	}

	/**
	 * Gets the mut tipo id.
	 *
	 * @return the mut tipo id
	 */
	public Integer getMutTipoId() {
		return this.mutTipoId;
	}

	/**
	 * Sets the mut tipo id.
	 *
	 * @param mutTipoId the new mut tipo id
	 */
	public void setMutTipoId(Integer mutTipoId) {
		this.mutTipoId = mutTipoId;
	}

	/**
	 * Gets the mut tipo code.
	 *
	 * @return the mut tipo code
	 */
	public String getMutTipoCode() {
		return this.mutTipoCode;
	}

	/**
	 * Sets the mut tipo code.
	 *
	 * @param mutTipoCode the new mut tipo code
	 */
	public void setMutTipoCode(String mutTipoCode) {
		this.mutTipoCode = mutTipoCode;
	}

	/**
	 * Gets the mut tipo desc.
	 *
	 * @return the mut tipo desc
	 */
	public String getMutTipoDesc() {
		return this.mutTipoDesc;
	}

	/**
	 * Sets the mut tipo desc.
	 *
	 * @param mutTipoDesc the new mut tipo desc
	 */
	public void setMutTipoDesc(String mutTipoDesc) {
		this.mutTipoDesc = mutTipoDesc;
	}

	/**
	 * Gets the siac t mutuos.
	 *
	 * @return the siac t mutuos
	 */
	public List<SiacTMutuo> getSiacTMutuos() {
		return this.siacTMutuos;
	}

	/**
	 * Sets the siac t mutuos.
	 *
	 * @param siacTMutuos the new siac t mutuos
	 */
	public void setSiacTMutuos(List<SiacTMutuo> siacTMutuos) {
		this.siacTMutuos = siacTMutuos;
	}

	/**
	 * Adds the siac t mutuo.
	 *
	 * @param siacTMutuo the siac t mutuo
	 * @return the siac t mutuo
	 */
	public SiacTMutuo addSiacTMutuo(SiacTMutuo siacTMutuo) {
		getSiacTMutuos().add(siacTMutuo);
		siacTMutuo.setSiacDMutuoTipo(this);

		return siacTMutuo;
	}

	/**
	 * Removes the siac t mutuo.
	 *
	 * @param siacTMutuo the siac t mutuo
	 * @return the siac t mutuo
	 */
	public SiacTMutuo removeSiacTMutuo(SiacTMutuo siacTMutuo) {
		getSiacTMutuos().remove(siacTMutuo);
		siacTMutuo.setSiacDMutuoTipo(null);

		return siacTMutuo;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return mutTipoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.mutTipoId = uid;
	}

}