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
 * The persistent class for the siac_d_gestione_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_gestione_tipo")
public class SiacDGestioneTipo extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The gestione tipo id. */
	@Id
	@SequenceGenerator(name="SIAC_D_GESTIONE_TIPO_GESTIONETIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_GESTIONE_TIPO_GESTIONE_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_GESTIONE_TIPO_GESTIONETIPOID_GENERATOR")
	@Column(name="gestione_tipo_id")
	private Integer gestioneTipoId;

	/** The gestione tipo code. */
	@Column(name="gestione_tipo_code")
	private String gestioneTipoCode;

	/** The gestione tipo desc. */
	@Column(name="gestione_tipo_desc")
	private String gestioneTipoDesc;

	//bi-directional many-to-one association to SiacDGestioneLivello
	/** The siac d gestione livellos. */
	@OneToMany(mappedBy="siacDGestioneTipo")
	private List<SiacDGestioneLivello> siacDGestioneLivellos;

	/**
	 * Instantiates a new siac d gestione tipo.
	 */
	public SiacDGestioneTipo() {
	}

	/**
	 * Gets the gestione tipo id.
	 *
	 * @return the gestione tipo id
	 */
	public Integer getGestioneTipoId() {
		return this.gestioneTipoId;
	}

	/**
	 * Sets the gestione tipo id.
	 *
	 * @param gestioneTipoId the new gestione tipo id
	 */
	public void setGestioneTipoId(Integer gestioneTipoId) {
		this.gestioneTipoId = gestioneTipoId;
	}

	/**
	 * Gets the gestione tipo code.
	 *
	 * @return the gestione tipo code
	 */
	public String getGestioneTipoCode() {
		return this.gestioneTipoCode;
	}

	/**
	 * Sets the gestione tipo code.
	 *
	 * @param gestioneTipoCode the new gestione tipo code
	 */
	public void setGestioneTipoCode(String gestioneTipoCode) {
		this.gestioneTipoCode = gestioneTipoCode;
	}

	/**
	 * Gets the gestione tipo desc.
	 *
	 * @return the gestione tipo desc
	 */
	public String getGestioneTipoDesc() {
		return this.gestioneTipoDesc;
	}

	/**
	 * Sets the gestione tipo desc.
	 *
	 * @param gestioneTipoDesc the new gestione tipo desc
	 */
	public void setGestioneTipoDesc(String gestioneTipoDesc) {
		this.gestioneTipoDesc = gestioneTipoDesc;
	}

	/**
	 * Gets the siac d gestione livellos.
	 *
	 * @return the siac d gestione livellos
	 */
	public List<SiacDGestioneLivello> getSiacDGestioneLivellos() {
		return this.siacDGestioneLivellos;
	}

	/**
	 * Sets the siac d gestione livellos.
	 *
	 * @param siacDGestioneLivellos the new siac d gestione livellos
	 */
	public void setSiacDGestioneLivellos(List<SiacDGestioneLivello> siacDGestioneLivellos) {
		this.siacDGestioneLivellos = siacDGestioneLivellos;
	}

	/**
	 * Adds the siac d gestione livello.
	 *
	 * @param siacDGestioneLivello the siac d gestione livello
	 * @return the siac d gestione livello
	 */
	public SiacDGestioneLivello addSiacDGestioneLivello(SiacDGestioneLivello siacDGestioneLivello) {
		getSiacDGestioneLivellos().add(siacDGestioneLivello);
		siacDGestioneLivello.setSiacDGestioneTipo(this);

		return siacDGestioneLivello;
	}

	/**
	 * Removes the siac d gestione livello.
	 *
	 * @param siacDGestioneLivello the siac d gestione livello
	 * @return the siac d gestione livello
	 */
	public SiacDGestioneLivello removeSiacDGestioneLivello(SiacDGestioneLivello siacDGestioneLivello) {
		getSiacDGestioneLivellos().remove(siacDGestioneLivello);
		siacDGestioneLivello.setSiacDGestioneTipo(null);

		return siacDGestioneLivello;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return gestioneTipoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.gestioneTipoId = uid;
	}

}