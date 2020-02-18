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
 * The persistent class for the siac_d_gruppo_azioni database table.
 * 
 */
@Entity
@Table(name="siac_d_gruppo_azioni")
public class SiacDGruppoAzioni extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The gruppo azioni id. */
	@Id
	@SequenceGenerator(name="SIAC_D_GRUPPO_AZIONI_GRUPPOAZIONIID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_GRUPPO_AZIONI_GRUPPO_AZIONI_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_GRUPPO_AZIONI_GRUPPOAZIONIID_GENERATOR")
	@Column(name="gruppo_azioni_id")
	private Integer gruppoAzioniId;

	/** The gruppo azioni code. */
	@Column(name="gruppo_azioni_code")
	private String gruppoAzioniCode;

	/** The gruppo azioni desc. */
	@Column(name="gruppo_azioni_desc")
	private String gruppoAzioniDesc;

	/** The titolo. */
	private String titolo;

	//bi-directional many-to-one association to SiacTAzione
	/** The siac t aziones. */
	@OneToMany(mappedBy="siacDGruppoAzioni")
	private List<SiacTAzione> siacTAziones;

	/**
	 * Instantiates a new siac d gruppo azioni.
	 */
	public SiacDGruppoAzioni() {
	}

	/**
	 * Gets the gruppo azioni id.
	 *
	 * @return the gruppo azioni id
	 */
	public Integer getGruppoAzioniId() {
		return this.gruppoAzioniId;
	}

	/**
	 * Sets the gruppo azioni id.
	 *
	 * @param gruppoAzioniId the new gruppo azioni id
	 */
	public void setGruppoAzioniId(Integer gruppoAzioniId) {
		this.gruppoAzioniId = gruppoAzioniId;
	}

	/**
	 * Gets the gruppo azioni code.
	 *
	 * @return the gruppo azioni code
	 */
	public String getGruppoAzioniCode() {
		return this.gruppoAzioniCode;
	}

	/**
	 * Sets the gruppo azioni code.
	 *
	 * @param gruppoAzioniCode the new gruppo azioni code
	 */
	public void setGruppoAzioniCode(String gruppoAzioniCode) {
		this.gruppoAzioniCode = gruppoAzioniCode;
	}

	/**
	 * Gets the gruppo azioni desc.
	 *
	 * @return the gruppo azioni desc
	 */
	public String getGruppoAzioniDesc() {
		return this.gruppoAzioniDesc;
	}

	/**
	 * Sets the gruppo azioni desc.
	 *
	 * @param gruppoAzioniDesc the new gruppo azioni desc
	 */
	public void setGruppoAzioniDesc(String gruppoAzioniDesc) {
		this.gruppoAzioniDesc = gruppoAzioniDesc;
	}

	/**
	 * Gets the titolo.
	 *
	 * @return the titolo
	 */
	public String getTitolo() {
		return this.titolo;
	}

	/**
	 * Sets the titolo.
	 *
	 * @param titolo the new titolo
	 */
	public void setTitolo(String titolo) {
		this.titolo = titolo;
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
		siacTAzione.setSiacDGruppoAzioni(this);

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
		siacTAzione.setSiacDGruppoAzioni(null);

		return siacTAzione;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.gruppoAzioniId = uid;
	}

}