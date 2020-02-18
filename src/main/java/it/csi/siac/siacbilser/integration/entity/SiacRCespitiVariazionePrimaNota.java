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
@Table(name="siac_r_cespiti_variazione_prima_nota")
@NamedQuery(name="SiacRCespitiVariazionePrimaNota.findAll", query="SELECT s FROM SiacRCespitiVariazionePrimaNota s")
public class SiacRCespitiVariazionePrimaNota extends SiacTEnteBase{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;


	@Id
	@SequenceGenerator(name="siac_r_cespiti_variazione_prima_nota_ces_var_pn_idGENERATOR", allocationSize=1, sequenceName="siac_r_cespiti_variazione_prima_nota_ces_var_pn_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="siac_r_cespiti_variazione_prima_nota_ces_var_pn_idGENERATOR")
	@Column(name="ces_var_pn_id")
	private Integer cesVariazionePnId;


	//bi-directional many-to-one association to SiacTAttr
	/** The siac t attr. */
	@ManyToOne
	@JoinColumn(name="pnota_id")
	private SiacTPrimaNota siacTPrimaNota;

	//bi-directional many-to-one association to SiacTSubdoc
	/** The siac t subdoc. */
	@ManyToOne
	@JoinColumn(name="ces_var_id")
	private SiacTCespitiVariazione siacTCespitiVariazione;

	/**
	 * Instantiates a new siac r subdoc attr.
	 */
	public SiacRCespitiVariazionePrimaNota() {
	}

	

	
	/**
	 * @return the cesDismissioniPnId
	 */
	public Integer getCesVariazionePnId() {
		return cesVariazionePnId;
	}




	/**
	 * @param cesDismissioniPnId the cesDismissioniPnId to set
	 */
	public void setCesVariazionePnId(Integer cesVariazionePnId) {
		this.cesVariazionePnId = cesVariazionePnId;
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
	 * @return the siacTCespitiDVariazione
	 */
	public SiacTCespitiVariazione getSiacTCespitiVariazione() {
		return siacTCespitiVariazione;
	}




	/**
	 * @param siacTCespitiVariazione the siacTCespitiVariazione to set
	 */
	public void setSiacTCespitiVariazione(SiacTCespitiVariazione siacTCespitiVariazione) {
		this.siacTCespitiVariazione = siacTCespitiVariazione;
	}




	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return cesVariazionePnId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.cesVariazionePnId = uid;
	}


}