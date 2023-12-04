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
 * The persistent class for the siac_d_vincolo_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_vincolo_tipo")
@NamedQuery(name="SiacDVincoloTipo.findAll", query="SELECT s FROM SiacDVincoloTipo s")
public class SiacDVincoloTipo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The vincolo tipo id. */
	@Id
	@SequenceGenerator(name="SIAC_D_VINCOLO_TIPO_VINCOLOTIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_VINCOLO_TIPO_VINCOLO_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_VINCOLO_TIPO_VINCOLOTIPOID_GENERATOR")
	@Column(name="vincolo_tipo_id")
	private Integer vincoloTipoId;

	/** The vincolo tipo code. */
	@Column(name="vincolo_tipo_code")
	private String vincoloTipoCode;

	/** The vincolo tipo desc. */
	@Column(name="vincolo_tipo_desc")
	private String vincoloTipoDesc;

	//bi-directional many-to-one association to SiacTVincolo
	/** The siac t vincolos. */
	@OneToMany(mappedBy="siacDVincoloTipo")
	private List<SiacTVincolo> siacTVincolos;

	/**
	 * Instantiates a new siac d vincolo tipo.
	 */
	public SiacDVincoloTipo() {
	}

	/**
	 * Gets the vincolo tipo id.
	 *
	 * @return the vincolo tipo id
	 */
	public Integer getVincoloTipoId() {
		return this.vincoloTipoId;
	}

	/**
	 * Sets the vincolo tipo id.
	 *
	 * @param vincoloTipoId the new vincolo tipo id
	 */
	public void setVincoloTipoId(Integer vincoloTipoId) {
		this.vincoloTipoId = vincoloTipoId;
	}

	/**
	 * Gets the vincolo tipo code.
	 *
	 * @return the vincolo tipo code
	 */
	public String getVincoloTipoCode() {
		return this.vincoloTipoCode;
	}

	/**
	 * Sets the vincolo tipo code.
	 *
	 * @param vincoloTipoCode the new vincolo tipo code
	 */
	public void setVincoloTipoCode(String vincoloTipoCode) {
		this.vincoloTipoCode = vincoloTipoCode;
	}

	/**
	 * Gets the vincolo tipo desc.
	 *
	 * @return the vincolo tipo desc
	 */
	public String getVincoloTipoDesc() {
		return this.vincoloTipoDesc;
	}

	/**
	 * Sets the vincolo tipo desc.
	 *
	 * @param vincoloTipoDesc the new vincolo tipo desc
	 */
	public void setVincoloTipoDesc(String vincoloTipoDesc) {
		this.vincoloTipoDesc = vincoloTipoDesc;
	}

	/**
	 * Gets the siac t vincolos.
	 *
	 * @return the siac t vincolos
	 */
	public List<SiacTVincolo> getSiacTVincolos() {
		return this.siacTVincolos;
	}

	/**
	 * Sets the siac t vincolos.
	 *
	 * @param siacTVincolos the new siac t vincolos
	 */
	public void setSiacTVincolos(List<SiacTVincolo> siacTVincolos) {
		this.siacTVincolos = siacTVincolos;
	}

	/**
	 * Adds the siac t vincolo.
	 *
	 * @param siacTVincolo the siac t vincolo
	 * @return the siac t vincolo
	 */
	public SiacTVincolo addSiacTVincolo(SiacTVincolo siacTVincolo) {
		getSiacTVincolos().add(siacTVincolo);
		siacTVincolo.setSiacDVincoloTipo(this);

		return siacTVincolo;
	}

	/**
	 * Removes the siac t vincolo.
	 *
	 * @param siacTVincolo the siac t vincolo
	 * @return the siac t vincolo
	 */
	public SiacTVincolo removeSiacTVincolo(SiacTVincolo siacTVincolo) {
		getSiacTVincolos().remove(siacTVincolo);
		siacTVincolo.setSiacDVincoloTipo(null);

		return siacTVincolo;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return vincoloTipoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.vincoloTipoId = uid;
		
	}

}