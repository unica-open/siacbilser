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
 * The persistent class for the siac_d_azione_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_azione_tipo")
@NamedQuery(name="SiacDAzioneTipo.findAll", query="SELECT s FROM SiacDAzioneTipo s")
public class SiacDAzioneTipo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The azione tipo id. */
	@Id
	@SequenceGenerator(name="SIAC_D_AZIONE_TIPO_AZIONETIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_AZIONE_TIPO_AZIONE_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_AZIONE_TIPO_AZIONETIPOID_GENERATOR")
	@Column(name="azione_tipo_id")
	private Integer azioneTipoId;

	/** The azione tipo code. */
	@Column(name="azione_tipo_code")
	private String azioneTipoCode;

	/** The azione tipo desc. */
	@Column(name="azione_tipo_desc")
	private String azioneTipoDesc;

	
	

	
	

	
	

	
	

	
	

	
	

	//bi-directional many-to-one association to SiacTAzione
	/** The siac t aziones. */
	@OneToMany(mappedBy="siacDAzioneTipo")
	private List<SiacTAzione> siacTAziones;

	/**
	 * Instantiates a new siac d azione tipo.
	 */
	public SiacDAzioneTipo() {
	}

	/**
	 * Gets the azione tipo id.
	 *
	 * @return the azione tipo id
	 */
	public Integer getAzioneTipoId() {
		return this.azioneTipoId;
	}

	/**
	 * Sets the azione tipo id.
	 *
	 * @param azioneTipoId the new azione tipo id
	 */
	public void setAzioneTipoId(Integer azioneTipoId) {
		this.azioneTipoId = azioneTipoId;
	}

	/**
	 * Gets the azione tipo code.
	 *
	 * @return the azione tipo code
	 */
	public String getAzioneTipoCode() {
		return this.azioneTipoCode;
	}

	/**
	 * Sets the azione tipo code.
	 *
	 * @param azioneTipoCode the new azione tipo code
	 */
	public void setAzioneTipoCode(String azioneTipoCode) {
		this.azioneTipoCode = azioneTipoCode;
	}

	/**
	 * Gets the azione tipo desc.
	 *
	 * @return the azione tipo desc
	 */
	public String getAzioneTipoDesc() {
		return this.azioneTipoDesc;
	}

	/**
	 * Sets the azione tipo desc.
	 *
	 * @param azioneTipoDesc the new azione tipo desc
	 */
	public void setAzioneTipoDesc(String azioneTipoDesc) {
		this.azioneTipoDesc = azioneTipoDesc;
	}

	/**
	 * Gets the siac t aziones.
	 *
	 * @return the siac t aziones
	 */
	public List<SiacTAzione> getSiacTAziones() {
		return this.siacTAziones;
	}

	/**
	 * Sets the siac t aziones.
	 *
	 * @param siacTAziones the new siac t aziones
	 */
	public void setSiacTAziones(List<SiacTAzione> siacTAziones) {
		this.siacTAziones = siacTAziones;
	}

	/**
	 * Adds the siac t azione.
	 *
	 * @param siacTAzione the siac t azione
	 * @return the siac t azione
	 */
	public SiacTAzione addSiacTAzione(SiacTAzione siacTAzione) {
		getSiacTAziones().add(siacTAzione);
		siacTAzione.setSiacDAzioneTipo(this);

		return siacTAzione;
	}

	/**
	 * Removes the siac t azione.
	 *
	 * @param siacTAzione the siac t azione
	 * @return the siac t azione
	 */
	public SiacTAzione removeSiacTAzione(SiacTAzione siacTAzione) {
		getSiacTAziones().remove(siacTAzione);
		siacTAzione.setSiacDAzioneTipo(null);

		return siacTAzione;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return azioneTipoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.azioneTipoId = uid;
	}

}