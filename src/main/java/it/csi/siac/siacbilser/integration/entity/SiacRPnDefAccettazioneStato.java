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
@Table(name="siac_r_pn_def_accettazione_stato")
@NamedQuery(name="SiacRPnDefAccettazioneStato.findAll", query="SELECT s FROM SiacRPnDefAccettazioneStato s")
public class SiacRPnDefAccettazioneStato extends SiacTEnteBase{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;


	@Id
	@SequenceGenerator(name="siac_r_pn_def_accettazione_stato_pn_r_sta_acc_def_idGENERATOR", allocationSize=1, sequenceName="siac_r_pn_def_accettazione_stato_pn_r_sta_acc_def_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="siac_r_pn_def_accettazione_stato_pn_r_sta_acc_def_idGENERATOR")
	@Column(name="pn_r_sta_acc_def_id")
	private Integer pnRStaAccDefId;


	//bi-directional many-to-one association to SiacTAttr
	/** The siac t attr. */
	@ManyToOne
	@JoinColumn(name="pnota_id")
	private SiacTPrimaNota siacTPrimaNota;

	//bi-directional many-to-one association to SiacTSubdoc
	/** The siac t subdoc. */
	@ManyToOne
	@JoinColumn(name="pn_sta_acc_def_id")
	private SiacDPnDefAccettazioneStato siacDPnDefAccettazioneStato;

	/**
	 * Instantiates a new siac r subdoc attr.
	 */
	public SiacRPnDefAccettazioneStato() {
	}


	/**
	 * @return the pnRStaAccDefId
	 */
	public Integer getPnRStaAccDefId() {
		return pnRStaAccDefId;
	}


	/**
	 * @param pnRStaAccDefId the pnRStaAccDefId to set
	 */
	public void setPnRStaAccDefId(Integer pnRStaAccDefId) {
		this.pnRStaAccDefId = pnRStaAccDefId;
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
	public SiacDPnDefAccettazioneStato getSiacDPnDefAccettazioneStato() {
		return siacDPnDefAccettazioneStato;
	}




	/**
	 * @param siacDPnDefAccettazioneStato the siacDPnDefAccettazioneStato to set
	 */
	public void setSiacDPnDefAccettazioneStato(SiacDPnDefAccettazioneStato siacDPnDefAccettazioneStato) {
		this.siacDPnDefAccettazioneStato = siacDPnDefAccettazioneStato;
	}




	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return pnRStaAccDefId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.pnRStaAccDefId = uid;
	}


}