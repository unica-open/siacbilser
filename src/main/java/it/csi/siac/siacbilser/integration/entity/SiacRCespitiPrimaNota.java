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
 * The persistent class for the siac_r_subdoc_attr database table.
 * 
 */
@Entity
@Table(name="siac_r_cespiti_prima_nota")
@NamedQuery(name="SiacRCespitiPrimaNota.findAll", query="SELECT s FROM SiacRCespitiPrimaNota s")
public class SiacRCespitiPrimaNota extends SiacTEnteBase{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;


	@Id
	@SequenceGenerator(name="siac_r_cespiti_prima_nota_ces_pn_idGENERATOR", allocationSize=1, sequenceName="siac_r_cespiti_prima_nota_ces_pn_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="siac_r_cespiti_prima_nota_ces_pn_idGENERATOR")
	@Column(name="ces_pn_id")
	private Integer cesPnId;


	//bi-directional many-to-one association to SiacTAttr
	/** The siac t attr. */
	@ManyToOne
	@JoinColumn(name="pnota_id")
	private SiacTPrimaNota siacTPrimaNota;

	//bi-directional many-to-one association to SiacTSubdoc
	/** The siac t subdoc. */
	@ManyToOne
	@JoinColumn(name="ces_id")
	private SiacTCespiti siacTCespiti;

	/**
	 * Instantiates a new siac r subdoc attr.
	 */
	public SiacRCespitiPrimaNota() {
	}

	/**
	 * @return the cesPnId
	 */
	public Integer getCesPnId() {
		return cesPnId;
	}

	/**
	 * @param cesPnId the cesPnId to set
	 */
	public void setCesPnId(Integer cesPnId) {
		this.cesPnId = cesPnId;
	}


	/**
	 * @return the siacTPrimaNota
	 */
	public SiacTPrimaNota getSiacTPrimaNota() {
		return siacTPrimaNota;
	}


	/**
	 * @param siacTPrimaNota the siacTPrimaNota to set
	 */
	public void setSiacTPrimaNota(SiacTPrimaNota siacTPrimaNota) {
		this.siacTPrimaNota = siacTPrimaNota;
	}



	/**
	 * @return the siacTCespiti
	 */
	public SiacTCespiti getSiacTCespiti() {
		return siacTCespiti;
	}


	/**
	 * @param siacTCespiti the siacTCespiti to set
	 */
	public void setSiacTCespiti(SiacTCespiti siacTCespiti) {
		this.siacTCespiti = siacTCespiti;
	}


	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return cesPnId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.cesPnId = uid;
	}


}