/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_r_gestione_ente database table.
 * 
 */
@Entity
@Table(name="siac_r_gestione_ente")
public class SiacRGestioneEnte extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The gestione ente id. */
	@Id
	@SequenceGenerator(name="SIAC_R_GESTIONE_ENTE_GESTIONEENTEID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_GESTIONE_ENTE_GESTIONE_ENTE_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_GESTIONE_ENTE_GESTIONEENTEID_GENERATOR")
	@Column(name="gestione_ente_id")
	private Integer gestioneEnteId;
	

	//bi-directional many-to-one association to SiacDGestioneLivello
	/** The siac d gestione livello. */
	@ManyToOne
	@JoinColumn(name="gestione_livello_id")
	private SiacDGestioneLivello siacDGestioneLivello;

	/**
	 * Instantiates a new siac r gestione ente.
	 */
	public SiacRGestioneEnte() {
	}

	/**
	 * Gets the gestione ente id.
	 *
	 * @return the gestione ente id
	 */
	public Integer getGestioneEnteId() {
		return this.gestioneEnteId;
	}

	/**
	 * Sets the gestione ente id.
	 *
	 * @param gestioneEnteId the new gestione ente id
	 */
	public void setGestioneEnteId(Integer gestioneEnteId) {
		this.gestioneEnteId = gestioneEnteId;
	}

	/**
	 * Gets the siac d gestione livello.
	 *
	 * @return the siac d gestione livello
	 */
	public SiacDGestioneLivello getSiacDGestioneLivello() {
		return this.siacDGestioneLivello;
	}

	/**
	 * Sets the siac d gestione livello.
	 *
	 * @param siacDGestioneLivello the new siac d gestione livello
	 */
	public void setSiacDGestioneLivello(SiacDGestioneLivello siacDGestioneLivello) {
		this.siacDGestioneLivello = siacDGestioneLivello;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return gestioneEnteId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.gestioneEnteId = uid;
		
	}

	

}