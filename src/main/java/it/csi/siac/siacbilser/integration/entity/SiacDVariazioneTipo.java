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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_d_variazione_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_variazione_tipo")
public class SiacDVariazioneTipo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The variazione tipo id. */
	@Id
	@SequenceGenerator(name="SIAC_D_VARIAZIONE_TIPO_VARIAZIONETIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_VARIAZIONE_TIPO_VARIAZIONE_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_VARIAZIONE_TIPO_VARIAZIONETIPOID_GENERATOR")
	@Column(name="variazione_tipo_id")
	private Integer variazioneTipoId;

	/** The variazione tipo code. */
	@Column(name="variazione_tipo_code")
	private String variazioneTipoCode;

	/** The variazione tipo desc. */
	@Column(name="variazione_tipo_desc")
	private String variazioneTipoDesc;

	/*//bi-directional many-to-one association to SiacTEnteProprietario
	@ManyToOne
	@JoinColumn(name="ente_proprietario_id")
	private SiacTEnteProprietario siacTEnteProprietario;*/

	//bi-directional many-to-one association to SiacTVariazione
	/** The siac t variaziones. */
	@OneToMany(mappedBy="siacDVariazioneTipo")
	private List<SiacTVariazione> siacTVariaziones;

	/**
	 * Instantiates a new siac d variazione tipo.
	 */
	public SiacDVariazioneTipo() {
	}

	/**
	 * Gets the variazione tipo id.
	 *
	 * @return the variazione tipo id
	 */
	public Integer getVariazioneTipoId() {
		return this.variazioneTipoId;
	}

	/**
	 * Sets the variazione tipo id.
	 *
	 * @param variazioneTipoId the new variazione tipo id
	 */
	public void setVariazioneTipoId(Integer variazioneTipoId) {
		this.variazioneTipoId = variazioneTipoId;
	}

	/**
	 * Gets the variazione tipo code.
	 *
	 * @return the variazione tipo code
	 */
	public String getVariazioneTipoCode() {
		return this.variazioneTipoCode;
	}

	/**
	 * Sets the variazione tipo code.
	 *
	 * @param variazioneTipoCode the new variazione tipo code
	 */
	public void setVariazioneTipoCode(String variazioneTipoCode) {
		this.variazioneTipoCode = variazioneTipoCode;
	}

	/**
	 * Gets the variazione tipo desc.
	 *
	 * @return the variazione tipo desc
	 */
	public String getVariazioneTipoDesc() {
		return this.variazioneTipoDesc;
	}

	/**
	 * Sets the variazione tipo desc.
	 *
	 * @param variazioneTipoDesc the new variazione tipo desc
	 */
	public void setVariazioneTipoDesc(String variazioneTipoDesc) {
		this.variazioneTipoDesc = variazioneTipoDesc;
	}

	/**
	 * Gets the siac t variaziones.
	 *
	 * @return the siac t variaziones
	 */
	public List<SiacTVariazione> getSiacTVariaziones() {
		return this.siacTVariaziones;
	}

	/**
	 * Sets the siac t variaziones.
	 *
	 * @param siacTVariaziones the new siac t variaziones
	 */
	public void setSiacTVariaziones(List<SiacTVariazione> siacTVariaziones) {
		this.siacTVariaziones = siacTVariaziones;
	}

	/**
	 * Adds the siac t variazione.
	 *
	 * @param siacTVariazione the siac t variazione
	 * @return the siac t variazione
	 */
	public SiacTVariazione addSiacTVariazione(SiacTVariazione siacTVariazione) {
		getSiacTVariaziones().add(siacTVariazione);
		siacTVariazione.setSiacDVariazioneTipo(this);

		return siacTVariazione;
	}

	/**
	 * Removes the siac t variazione.
	 *
	 * @param siacTVariazione the siac t variazione
	 * @return the siac t variazione
	 */
	public SiacTVariazione removeSiacTVariazione(SiacTVariazione siacTVariazione) {
		getSiacTVariaziones().remove(siacTVariazione);
		siacTVariazione.setSiacDVariazioneTipo(null);

		return siacTVariazione;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return variazioneTipoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.variazioneTipoId = uid;
	}

}