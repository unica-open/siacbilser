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
 * The persistent class for the siac_d_variazione_stato database table.
 * 
 */
@Entity
@Table(name="siac_d_variazione_stato")
@NamedQuery(name="SiacDVariazioneStato.findAll", query="SELECT s FROM SiacDVariazioneStato s")
public class SiacDVariazioneStato extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The variazione stato tipo id. */
	@Id
	@SequenceGenerator(name="SIAC_D_VARIAZIONE_STATO_VARIAZIONESTATOTIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_VARIAZIONE_STATO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_VARIAZIONE_STATO_VARIAZIONESTATOTIPOID_GENERATOR")
	@Column(name="variazione_stato_tipo_id")
	private Integer variazioneStatoTipoId;


	/** The variazione stato tipo code. */
	@Column(name="variazione_stato_tipo_code")
	private String variazioneStatoTipoCode;

	/** The variazione stato tipo desc. */
	@Column(name="variazione_stato_tipo_desc")
	private String variazioneStatoTipoDesc;



	//bi-directional many-to-one association to SiacRVariazioneStato
	/** The siac r variazione statos. */
	@OneToMany(mappedBy="siacDVariazioneStato")
	private List<SiacRVariazioneStato> siacRVariazioneStatos;

	/**
	 * Instantiates a new siac d variazione stato.
	 */
	public SiacDVariazioneStato() {
	}

	/**
	 * Gets the variazione stato tipo id.
	 *
	 * @return the variazione stato tipo id
	 */
	public Integer getVariazioneStatoTipoId() {
		return this.variazioneStatoTipoId;
	}

	/**
	 * Sets the variazione stato tipo id.
	 *
	 * @param variazioneStatoTipoId the new variazione stato tipo id
	 */
	public void setVariazioneStatoTipoId(Integer variazioneStatoTipoId) {
		this.variazioneStatoTipoId = variazioneStatoTipoId;
	}

	/**
	 * Gets the variazione stato tipo code.
	 *
	 * @return the variazione stato tipo code
	 */
	public String getVariazioneStatoTipoCode() {
		return this.variazioneStatoTipoCode;
	}

	/**
	 * Sets the variazione stato tipo code.
	 *
	 * @param variazioneStatoTipoCode the new variazione stato tipo code
	 */
	public void setVariazioneStatoTipoCode(String variazioneStatoTipoCode) {
		this.variazioneStatoTipoCode = variazioneStatoTipoCode;
	}

	/**
	 * Gets the variazione stato tipo desc.
	 *
	 * @return the variazione stato tipo desc
	 */
	public String getVariazioneStatoTipoDesc() {
		return this.variazioneStatoTipoDesc;
	}

	/**
	 * Sets the variazione stato tipo desc.
	 *
	 * @param variazioneStatoTipoDesc the new variazione stato tipo desc
	 */
	public void setVariazioneStatoTipoDesc(String variazioneStatoTipoDesc) {
		this.variazioneStatoTipoDesc = variazioneStatoTipoDesc;
	}


	/**
	 * Gets the siac r variazione statos.
	 *
	 * @return the siac r variazione statos
	 */
	public List<SiacRVariazioneStato> getSiacRVariazioneStatos() {
		return this.siacRVariazioneStatos;
	}

	/**
	 * Sets the siac r variazione statos.
	 *
	 * @param siacRVariazioneStatos the new siac r variazione statos
	 */
	public void setSiacRVariazioneStatos(List<SiacRVariazioneStato> siacRVariazioneStatos) {
		this.siacRVariazioneStatos = siacRVariazioneStatos;
	}

	/**
	 * Adds the siac r variazione stato.
	 *
	 * @param siacRVariazioneStato the siac r variazione stato
	 * @return the siac r variazione stato
	 */
	public SiacRVariazioneStato addSiacRVariazioneStato(SiacRVariazioneStato siacRVariazioneStato) {
		getSiacRVariazioneStatos().add(siacRVariazioneStato);
		siacRVariazioneStato.setSiacDVariazioneStato(this);

		return siacRVariazioneStato;
	}

	/**
	 * Removes the siac r variazione stato.
	 *
	 * @param siacRVariazioneStato the siac r variazione stato
	 * @return the siac r variazione stato
	 */
	public SiacRVariazioneStato removeSiacRVariazioneStato(SiacRVariazioneStato siacRVariazioneStato) {
		getSiacRVariazioneStatos().remove(siacRVariazioneStato);
		siacRVariazioneStato.setSiacDVariazioneStato(null);

		return siacRVariazioneStato;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return variazioneStatoTipoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.variazioneStatoTipoId = uid;
		
	}

}