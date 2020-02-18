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
 * The persistent class for the siac_d_modifica_stato database table.
 * 
 */
@Entity
@Table(name="siac_d_modifica_stato")
@NamedQuery(name="SiacDModificaStato.findAll", query="SELECT s FROM SiacDModificaStato s")
public class SiacDModificaStato extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The mod stato id. */
	@Id
	@SequenceGenerator(name="SIAC_D_MODIFICA_STATO_MODSTATOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_MODIFICA_STATO_MOD_STATO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_MODIFICA_STATO_MODSTATOID_GENERATOR")
	@Column(name="mod_stato_id")
	private Integer modStatoId;

	
	/** The mod stato code. */
	@Column(name="mod_stato_code")
	private String modStatoCode;

	/** The mod stato desc. */
	@Column(name="mod_stato_desc")
	private String modStatoDesc;

	

	//bi-directional many-to-one association to SiacRModificaStato
	/** The siac r modifica statos. */
	@OneToMany(mappedBy="siacDModificaStato")
	private List<SiacRModificaStato> siacRModificaStatos;

	/**
	 * Instantiates a new siac d modifica stato.
	 */
	public SiacDModificaStato() {
	}

	/**
	 * Gets the mod stato id.
	 *
	 * @return the mod stato id
	 */
	public Integer getModStatoId() {
		return this.modStatoId;
	}

	/**
	 * Sets the mod stato id.
	 *
	 * @param modStatoId the new mod stato id
	 */
	public void setModStatoId(Integer modStatoId) {
		this.modStatoId = modStatoId;
	}

	

	/**
	 * Gets the mod stato code.
	 *
	 * @return the mod stato code
	 */
	public String getModStatoCode() {
		return this.modStatoCode;
	}

	/**
	 * Sets the mod stato code.
	 *
	 * @param modStatoCode the new mod stato code
	 */
	public void setModStatoCode(String modStatoCode) {
		this.modStatoCode = modStatoCode;
	}

	/**
	 * Gets the mod stato desc.
	 *
	 * @return the mod stato desc
	 */
	public String getModStatoDesc() {
		return this.modStatoDesc;
	}

	/**
	 * Sets the mod stato desc.
	 *
	 * @param modStatoDesc the new mod stato desc
	 */
	public void setModStatoDesc(String modStatoDesc) {
		this.modStatoDesc = modStatoDesc;
	}


	/**
	 * Gets the siac r modifica statos.
	 *
	 * @return the siac r modifica statos
	 */
	public List<SiacRModificaStato> getSiacRModificaStatos() {
		return this.siacRModificaStatos;
	}

	/**
	 * Sets the siac r modifica statos.
	 *
	 * @param siacRModificaStatos the new siac r modifica statos
	 */
	public void setSiacRModificaStatos(List<SiacRModificaStato> siacRModificaStatos) {
		this.siacRModificaStatos = siacRModificaStatos;
	}

	/**
	 * Adds the siac r modifica stato.
	 *
	 * @param siacRModificaStato the siac r modifica stato
	 * @return the siac r modifica stato
	 */
	public SiacRModificaStato addSiacRModificaStato(SiacRModificaStato siacRModificaStato) {
		getSiacRModificaStatos().add(siacRModificaStato);
		siacRModificaStato.setSiacDModificaStato(this);

		return siacRModificaStato;
	}

	/**
	 * Removes the siac r modifica stato.
	 *
	 * @param siacRModificaStato the siac r modifica stato
	 * @return the siac r modifica stato
	 */
	public SiacRModificaStato removeSiacRModificaStato(SiacRModificaStato siacRModificaStato) {
		getSiacRModificaStatos().remove(siacRModificaStato);
		siacRModificaStato.setSiacDModificaStato(null);

		return siacRModificaStato;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return modStatoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.modStatoId = uid;
	}

}