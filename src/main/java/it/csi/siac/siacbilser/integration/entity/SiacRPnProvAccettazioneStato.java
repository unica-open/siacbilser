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
@Table(name="siac_r_pn_prov_accettazione_stato")
@NamedQuery(name="SiacRPnProvAccettazioneStato.findAll", query="SELECT s FROM SiacRPnProvAccettazioneStato s")
public class SiacRPnProvAccettazioneStato extends SiacTEnteBase{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;


	@Id
	@SequenceGenerator(name="siac_r_pn_prov_accettazione_stato_pn_r_sta_acc_prov_idGENERATOR", allocationSize=1, sequenceName="siac_r_pn_prov_accettazione_stato_pn_r_sta_acc_prov_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="siac_r_pn_prov_accettazione_stato_pn_r_sta_acc_prov_idGENERATOR")
	@Column(name="pn_r_sta_acc_prov_id")
	private Integer pnRStaAccProvId;


	//bi-directional many-to-one association to SiacTAttr
	/** The siac t attr. */
	@ManyToOne
	@JoinColumn(name="pnota_id")
	private SiacTPrimaNota siacTPrimaNota;

	//bi-directional many-to-one association to SiacTSubdoc
	/** The siac t subdoc. */
	@ManyToOne
	@JoinColumn(name="pn_sta_acc_prov_id")
	private SiacDPnProvAccettazioneStato siacDPnProvAccettazioneStato;

	/**
	 * Instantiates a new siac r subdoc attr.
	 */
	public SiacRPnProvAccettazioneStato() {
	}


	/**
	 * @return the pnRStaAccDefId
	 */
	public Integer getPnRStaAccProvId() {
		return pnRStaAccProvId;
	}


	/**
	 * @param pnRStaAccDefId the pnRStaAccDefId to set
	 */
	public void setPnRStaAccProvId(Integer pnRStaAccProvId) {
		this.pnRStaAccProvId = pnRStaAccProvId;
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
	 * @return the siacDPnDefAccettazioneStato
	 */
	public SiacDPnProvAccettazioneStato getSiacDPnProvAccettazioneStato() {
		return siacDPnProvAccettazioneStato;
	}




	/**
	 * @param siacDPnDefAccettazioneStato the siacDPnDefAccettazioneStato to set
	 */
	public void setSiacDPnProvAccettazioneStato(SiacDPnProvAccettazioneStato siacDPnProvAccettazioneStato) {
		this.siacDPnProvAccettazioneStato = siacDPnProvAccettazioneStato;
	}




	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return pnRStaAccProvId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.pnRStaAccProvId = uid;
	}


}