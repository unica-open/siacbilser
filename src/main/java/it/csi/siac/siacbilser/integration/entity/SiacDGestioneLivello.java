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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_d_gestione_livello database table.
 * 
 */
@Entity
@Table(name="siac_d_gestione_livello")
public class SiacDGestioneLivello extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The gestione livello id. */
	@Id
	@SequenceGenerator(name="SIAC_D_GESTIONE_LIVELLO_GESTIONELIVELLOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_GESTIONE_LIVELLO_GESTIONE_LIVELLO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_GESTIONE_LIVELLO_GESTIONELIVELLOID_GENERATOR")
	@Column(name="gestione_livello_id")
	private Integer gestioneLivelloId;

	/** The gestione livello code. */
	@Column(name="gestione_livello_code")
	private String gestioneLivelloCode;

	/** The gestione livello desc. */
	@Column(name="gestione_livello_desc")
	private String gestioneLivelloDesc;

	//bi-directional many-to-one association to SiacDGestioneTipo
	/** The siac d gestione tipo. */
	@ManyToOne
	@JoinColumn(name="gestione_tipo_id")
	private SiacDGestioneTipo siacDGestioneTipo;

	//bi-directional many-to-one association to SiacRGestioneEnte
	/** The siac r gestione entes. */
	@OneToMany(mappedBy="siacDGestioneLivello")
	private List<SiacRGestioneEnte> siacRGestioneEntes;

	/**
	 * Instantiates a new siac d gestione livello.
	 */
	public SiacDGestioneLivello() {
	}

	/**
	 * Gets the gestione livello id.
	 *
	 * @return the gestione livello id
	 */
	public Integer getGestioneLivelloId() {
		return this.gestioneLivelloId;
	}

	/**
	 * Sets the gestione livello id.
	 *
	 * @param gestioneLivelloId the new gestione livello id
	 */
	public void setGestioneLivelloId(Integer gestioneLivelloId) {
		this.gestioneLivelloId = gestioneLivelloId;
	}

	/**
	 * Gets the gestione livello code.
	 *
	 * @return the gestione livello code
	 */
	public String getGestioneLivelloCode() {
		return this.gestioneLivelloCode;
	}

	/**
	 * Sets the gestione livello code.
	 *
	 * @param gestioneLivelloCode the new gestione livello code
	 */
	public void setGestioneLivelloCode(String gestioneLivelloCode) {
		this.gestioneLivelloCode = gestioneLivelloCode;
	}

	/**
	 * Gets the gestione livello desc.
	 *
	 * @return the gestione livello desc
	 */
	public String getGestioneLivelloDesc() {
		return this.gestioneLivelloDesc;
	}

	/**
	 * Sets the gestione livello desc.
	 *
	 * @param gestioneLivelloDesc the new gestione livello desc
	 */
	public void setGestioneLivelloDesc(String gestioneLivelloDesc) {
		this.gestioneLivelloDesc = gestioneLivelloDesc;
	}

	/**
	 * Gets the siac d gestione tipo.
	 *
	 * @return the siac d gestione tipo
	 */
	public SiacDGestioneTipo getSiacDGestioneTipo() {
		return this.siacDGestioneTipo;
	}

	/**
	 * Sets the siac d gestione tipo.
	 *
	 * @param siacDGestioneTipo the new siac d gestione tipo
	 */
	public void setSiacDGestioneTipo(SiacDGestioneTipo siacDGestioneTipo) {
		this.siacDGestioneTipo = siacDGestioneTipo;
	}

	/**
	 * Gets the siac r gestione entes.
	 *
	 * @return the siac r gestione entes
	 */
	public List<SiacRGestioneEnte> getSiacRGestioneEntes() {
		return this.siacRGestioneEntes;
	}

	/**
	 * Sets the siac r gestione entes.
	 *
	 * @param siacRGestioneEntes the new siac r gestione entes
	 */
	public void setSiacRGestioneEntes(List<SiacRGestioneEnte> siacRGestioneEntes) {
		this.siacRGestioneEntes = siacRGestioneEntes;
	}

	/**
	 * Adds the siac r gestione ente.
	 *
	 * @param siacRGestioneEnte the siac r gestione ente
	 * @return the siac r gestione ente
	 */
	public SiacRGestioneEnte addSiacRGestioneEnte(SiacRGestioneEnte siacRGestioneEnte) {
		getSiacRGestioneEntes().add(siacRGestioneEnte);
		siacRGestioneEnte.setSiacDGestioneLivello(this);

		return siacRGestioneEnte;
	}

	/**
	 * Removes the siac r gestione ente.
	 *
	 * @param siacRGestioneEnte the siac r gestione ente
	 * @return the siac r gestione ente
	 */
	public SiacRGestioneEnte removeSiacRGestioneEnte(SiacRGestioneEnte siacRGestioneEnte) {
		getSiacRGestioneEntes().remove(siacRGestioneEnte);
		siacRGestioneEnte.setSiacDGestioneLivello(null);

		return siacRGestioneEnte;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return gestioneLivelloId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.gestioneLivelloId = uid;		
	}

}