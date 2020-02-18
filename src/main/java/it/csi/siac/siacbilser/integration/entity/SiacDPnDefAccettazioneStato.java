/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the siac_d_cespiti_categoria database table.
 * 
 */
@Entity
@Table(name="siac_d_pn_def_accettazione_stato")
@NamedQuery(name="SiacDPnDefAccettazioneStato.findAll", query="SELECT s FROM SiacDPnDefAccettazioneStato s")
public class SiacDPnDefAccettazioneStato extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="siac_d_pn_def_accettazione_stato_pn_sta_acc_def_idGENERATOR", allocationSize=1, sequenceName="siac_d_pn_def_accettazione_stato_pn_sta_acc_def_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="siac_d_pn_def_accettazione_stato_pn_sta_acc_def_idGENERATOR")

	@Column(name="pn_sta_acc_def_id")
	private Integer pnStaAccDefId;

	@Column(name="pn_sta_acc_def_code")
	private String pnStaAccDefCode;

	@Column(name="pn_sta_acc_def_desc")
	private String pnStaAccDefDesc;

	//bi-directional many-to-one association to SiacRPnDefAccettazioneStato
	@OneToMany(mappedBy="siacDPnDefAccettazioneStato", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRPnDefAccettazioneStato> siacRPnDefAccettazioneStato;

	/**
	 * @return the pnStaAccDefId
	 */
	public Integer getPnStaAccDefId() {
		return pnStaAccDefId;
	}

	/**
	 * @param pnStaAccDefId the pnStaAccDefId to set
	 */
	public void setPnStaAccDefId(Integer pnStaAccDefId) {
		this.pnStaAccDefId = pnStaAccDefId;
	}

	/**
	 * @return the pnStaAccDefCode
	 */
	public String getPnStaAccDefCode() {
		return pnStaAccDefCode;
	}

	/**
	 * @param pnStaAccDefCode the pnStaAccDefCode to set
	 */
	public void setPnStaAccDefCode(String pnStaAccDefCode) {
		this.pnStaAccDefCode = pnStaAccDefCode;
	}

	/**
	 * @return the pnStaAccDefDesc
	 */
	public String getPnStaAccDefDesc() {
		return pnStaAccDefDesc;
	}

	/**
	 * @param pnStaAccDefDesc the pnStaAccDefDesc to set
	 */
	public void setPnStaAccDefDesc(String pnStaAccDefDesc) {
		this.pnStaAccDefDesc = pnStaAccDefDesc;
	}

	

	
	
	/**
	 * @return the siacRPnDefAccettazioneStato
	 */
	public List<SiacRPnDefAccettazioneStato> getSiacRPnDefAccettazioneStato() {
		return siacRPnDefAccettazioneStato;
	}

	/**
	 * @param siacRPnDefAccettazioneStato the siacRPnDefAccettazioneStato to set
	 */
	public void setSiacRPnDefAccettazioneStato(List<SiacRPnDefAccettazioneStato> siacRPnDefAccettazioneStato) {
		this.siacRPnDefAccettazioneStato = siacRPnDefAccettazioneStato;
	}

	@Override
	public Integer getUid() {
		return this.pnStaAccDefId;
	}

	@Override
	public void setUid(Integer uid) {
		this.pnStaAccDefId = uid;
	}

}