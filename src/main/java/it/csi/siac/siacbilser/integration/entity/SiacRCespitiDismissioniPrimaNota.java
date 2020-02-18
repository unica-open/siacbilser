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
@Table(name="siac_r_cespiti_dismissioni_prima_nota")
@NamedQuery(name="SiacRCespitiDismissioniPrimaNota.findAll", query="SELECT s FROM SiacRCespitiDismissioniPrimaNota s")
public class SiacRCespitiDismissioniPrimaNota extends SiacTEnteBase{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;


	@Id
	@SequenceGenerator(name="siac_r_cespiti_dismissioni_prima_nota_ces_dismissioni_pn_idGENERATOR", allocationSize=1, sequenceName="siac_r_cespiti_dismissioni_prima_nota_ces_dismissioni_pn_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="siac_r_cespiti_dismissioni_prima_nota_ces_dismissioni_pn_idGENERATOR")
	
	@Column(name="ces_dismissioni_pn_id")
	private Integer cesDismissioniPnId;


	//bi-directional many-to-one association to SiacTAttr
	/** The siac t attr. */
	@ManyToOne
	@JoinColumn(name="pnota_id")
	private SiacTPrimaNota siacTPrimaNota;

	//bi-directional many-to-one association to SiacTSubdoc
	/** The siac t subdoc. */
	@ManyToOne
	@JoinColumn(name="ces_dismissioni_id")
	private SiacTCespitiDismissioni siacTCespitiDismissioni;
	
	@ManyToOne
	@JoinColumn(name="ces_amm_dett_id")
	private SiacTCespitiAmmortamentoDett siacTCespitiAmmortamentoDett;

	/**
	 * Instantiates a new siac r subdoc attr.
	 */
	public SiacRCespitiDismissioniPrimaNota() {
	}

	

	
	/**
	 * @return the cesdismissioniPnId
	 */
	public Integer getCesDismissioniPnId() {
		return cesDismissioniPnId;
	}




	/**
	 * @param cesdismissioniPnId the cesdismissioniPnId to set
	 */
	public void setCesdismissioniPnId(Integer cesDismissioniPnId) {
		this.cesDismissioniPnId = cesDismissioniPnId;
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
	 * @return the siacTCespitidismissioni
	 */
	public SiacTCespitiDismissioni getSiacTCespitiDismissioni() {
		return siacTCespitiDismissioni;
	}




	/**
	 * @param siacTCespitidismissioni the siacTCespitidismissioni to set
	 */
	public void setSiacTCespitiDismissioni(SiacTCespitiDismissioni siacTCespitiDismissioni) {
		this.siacTCespitiDismissioni = siacTCespitiDismissioni;
	}


	/**
	 * @return the siacTCespitiAmmortamentoDett
	 */
	public SiacTCespitiAmmortamentoDett getSiacTCespitiAmmortamentoDett() {
		return siacTCespitiAmmortamentoDett;
	}




	/**
	 * @param siacTCespitiAmmortamentoDett the siacTCespitiAmmortamentoDett to set
	 */
	public void setSiacTCespitiAmmortamentoDett(SiacTCespitiAmmortamentoDett siacTCespitiAmmortamentoDett) {
		this.siacTCespitiAmmortamentoDett = siacTCespitiAmmortamentoDett;
	}




	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return cesDismissioniPnId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.cesDismissioniPnId = uid;
	}


}