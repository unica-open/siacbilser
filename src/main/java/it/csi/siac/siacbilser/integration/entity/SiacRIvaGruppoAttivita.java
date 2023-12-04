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
 * The persistent class for the siac_r_iva_gruppo_attivita database table.
 * 
 */
@Entity
@Table(name="siac_r_iva_gruppo_attivita")
@NamedQuery(name="SiacRIvaGruppoAttivita.findAll", query="SELECT s FROM SiacRIvaGruppoAttivita s")
public class SiacRIvaGruppoAttivita extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The ivagratt id. */
	@Id
	@SequenceGenerator(name="SIAC_R_IVA_GRUPPO_ATTIVITA_IVAGRATTID_GENERATOR", sequenceName="SIAC_R_IVA_GRUPPO_ATTIVITA_IVAGRATT_ID_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_IVA_GRUPPO_ATTIVITA_IVAGRATTID_GENERATOR")
	@Column(name="ivagratt_id")
	private Integer ivagrattId;

	//bi-directional many-to-one association to SiacTIvaAttivita
	/** The siac t iva attivita. */
	@ManyToOne
	@JoinColumn(name="ivaatt_id")
	private SiacTIvaAttivita siacTIvaAttivita;

	//bi-directional many-to-one association to SiacTIvaGruppo
	/** The siac t iva gruppo. */
	@ManyToOne
	@JoinColumn(name="ivagru_id")
	private SiacTIvaGruppo siacTIvaGruppo;

	/**
	 * Instantiates a new siac r iva gruppo attivita.
	 */
	public SiacRIvaGruppoAttivita() {
	}

	/**
	 * Gets the ivagratt id.
	 *
	 * @return the ivagratt id
	 */
	public Integer getIvagrattId() {
		return this.ivagrattId;
	}

	/**
	 * Sets the ivagratt id.
	 *
	 * @param ivagrattId the new ivagratt id
	 */
	public void setIvagrattId(Integer ivagrattId) {
		this.ivagrattId = ivagrattId;
	}

	/**
	 * Gets the siac t iva attivita.
	 *
	 * @return the siac t iva attivita
	 */
	public SiacTIvaAttivita getSiacTIvaAttivita() {
		return this.siacTIvaAttivita;
	}

	/**
	 * Sets the siac t iva attivita.
	 *
	 * @param siacTIvaAttivita the new siac t iva attivita
	 */
	public void setSiacTIvaAttivita(SiacTIvaAttivita siacTIvaAttivita) {
		this.siacTIvaAttivita = siacTIvaAttivita;
	}

	/**
	 * Gets the siac t iva gruppo.
	 *
	 * @return the siac t iva gruppo
	 */
	public SiacTIvaGruppo getSiacTIvaGruppo() {
		return this.siacTIvaGruppo;
	}

	/**
	 * Sets the siac t iva gruppo.
	 *
	 * @param siacTIvaGruppo the new siac t iva gruppo
	 */
	public void setSiacTIvaGruppo(SiacTIvaGruppo siacTIvaGruppo) {
		this.siacTIvaGruppo = siacTIvaGruppo;
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return ivagrattId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.ivagrattId = uid;
	}

}