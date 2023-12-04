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
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_r_predoc_prov_cassa database table.
 * 
 */
@Entity
@Table(name="siac_r_predoc_prov_cassa")
@NamedQuery(name="SiacRPredocProvCassa.findAll", query="SELECT s FROM SiacRPredocProvCassa s")
public class SiacRPredocProvCassa extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The predoc provc id. */
	@Id
	@SequenceGenerator(name="SIAC_R_PREDOC_PROV_CASSA_PREDOCPROVCID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_PREDOC_PROV_CASSA_PREDOC_PROVC_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_PREDOC_PROV_CASSA_PREDOCPROVCID_GENERATOR")
	@Column(name="predoc_provc_id")
	private Integer predocProvcId;

	//bi-directional many-to-one association to SiacTPredoc
	/** The siac t predoc. */
	@ManyToOne
	@JoinColumn(name="predoc_id")
	private SiacTPredoc siacTPredoc;

	//bi-directional many-to-one association to SiacTProvCassa
	/** The siac t prov cassa. */
	@ManyToOne
	@JoinColumn(name="provc_id")
	private SiacTProvCassa siacTProvCassa;

	/**
	 * Instantiates a new siac r predoc prov cassa.
	 */
	public SiacRPredocProvCassa() {
	}

	/**
	 * Gets the predoc provc id.
	 *
	 * @return the predoc provc id
	 */
	public Integer getPredocProvcId() {
		return this.predocProvcId;
	}

	/**
	 * Sets the predoc provc id.
	 *
	 * @param predocProvcId the new predoc provc id
	 */
	public void setPredocProvcId(Integer predocProvcId) {
		this.predocProvcId = predocProvcId;
	}

	/**
	 * Gets the siac t predoc.
	 *
	 * @return the siac t predoc
	 */
	public SiacTPredoc getSiacTPredoc() {
		return this.siacTPredoc;
	}

	/**
	 * Sets the siac t predoc.
	 *
	 * @param siacTPredoc the new siac t predoc
	 */
	public void setSiacTPredoc(SiacTPredoc siacTPredoc) {
		this.siacTPredoc = siacTPredoc;
	}

	/**
	 * Gets the siac t prov cassa.
	 *
	 * @return the siac t prov cassa
	 */
	public SiacTProvCassa getSiacTProvCassa() {
		return this.siacTProvCassa;
	}

	/**
	 * Sets the siac t prov cassa.
	 *
	 * @param siacTProvCassa the new siac t prov cassa
	 */
	public void setSiacTProvCassa(SiacTProvCassa siacTProvCassa) {
		this.siacTProvCassa = siacTProvCassa;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return predocProvcId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.predocProvcId = uid;
	}

}