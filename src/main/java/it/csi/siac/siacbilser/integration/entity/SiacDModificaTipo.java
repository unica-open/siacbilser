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
 * The persistent class for the siac_d_modifica_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_modifica_tipo")
@NamedQuery(name="SiacDModificaTipo.findAll", query="SELECT s FROM SiacDModificaTipo s")
public class SiacDModificaTipo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The mod tipo id. */
	@Id
	@SequenceGenerator(name="SIAC_D_MODIFICA_TIPO_MODTIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_MODIFICA_TIPO_MOD_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_MODIFICA_TIPO_MODTIPOID_GENERATOR")
	@Column(name="mod_tipo_id")
	private Integer modTipoId;

	

	/** The mod tipo code. */
	@Column(name="mod_tipo_code")
	private String modTipoCode;

	/** The mod tipo desc. */
	@Column(name="mod_tipo_desc")
	private String modTipoDesc;


	//bi-directional many-to-one association to SiacTModifica
	/** The siac t modificas. */
	@OneToMany(mappedBy="siacDModificaTipo")
	private List<SiacTModifica> siacTModificas;

	/**
	 * Instantiates a new siac d modifica tipo.
	 */
	public SiacDModificaTipo() {
	}

	/**
	 * Gets the mod tipo id.
	 *
	 * @return the mod tipo id
	 */
	public Integer getModTipoId() {
		return this.modTipoId;
	}

	/**
	 * Sets the mod tipo id.
	 *
	 * @param modTipoId the new mod tipo id
	 */
	public void setModTipoId(Integer modTipoId) {
		this.modTipoId = modTipoId;
	}

	

	/**
	 * Gets the mod tipo code.
	 *
	 * @return the mod tipo code
	 */
	public String getModTipoCode() {
		return this.modTipoCode;
	}

	/**
	 * Sets the mod tipo code.
	 *
	 * @param modTipoCode the new mod tipo code
	 */
	public void setModTipoCode(String modTipoCode) {
		this.modTipoCode = modTipoCode;
	}

	/**
	 * Gets the mod tipo desc.
	 *
	 * @return the mod tipo desc
	 */
	public String getModTipoDesc() {
		return this.modTipoDesc;
	}

	/**
	 * Sets the mod tipo desc.
	 *
	 * @param modTipoDesc the new mod tipo desc
	 */
	public void setModTipoDesc(String modTipoDesc) {
		this.modTipoDesc = modTipoDesc;
	}


	/**
	 * Gets the siac t modificas.
	 *
	 * @return the siac t modificas
	 */
	public List<SiacTModifica> getSiacTModificas() {
		return this.siacTModificas;
	}

	/**
	 * Sets the siac t modificas.
	 *
	 * @param siacTModificas the new siac t modificas
	 */
	public void setSiacTModificas(List<SiacTModifica> siacTModificas) {
		this.siacTModificas = siacTModificas;
	}

	/**
	 * Adds the siac t modifica.
	 *
	 * @param siacTModifica the siac t modifica
	 * @return the siac t modifica
	 */
	public SiacTModifica addSiacTModifica(SiacTModifica siacTModifica) {
		getSiacTModificas().add(siacTModifica);
		siacTModifica.setSiacDModificaTipo(this);

		return siacTModifica;
	}

	/**
	 * Removes the siac t modifica.
	 *
	 * @param siacTModifica the siac t modifica
	 * @return the siac t modifica
	 */
	public SiacTModifica removeSiacTModifica(SiacTModifica siacTModifica) {
		getSiacTModificas().remove(siacTModifica);
		siacTModifica.setSiacDModificaTipo(null);

		return siacTModifica;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return modTipoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.modTipoId = uid;
	}

}