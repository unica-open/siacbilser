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
@Table(name="siac_d_pn_prov_accettazione_stato")
@NamedQuery(name="SiacDPnProvAccettazioneStato.findAll", query="SELECT s FROM SiacDPnProvAccettazioneStato s")
public class SiacDPnProvAccettazioneStato extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="siac_d_pn_prov_accettazione_stato_pn_sta_acc_prov_idGENERATOR", allocationSize=1, sequenceName="siac_d_pn_prov_accettazione_stato_pn_sta_acc_prov_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="siac_d_pn_prov_accettazione_stato_pn_sta_acc_prov_idGENERATOR")

	@Column(name="pn_sta_acc_prov_id")
	private Integer pnStaAccProvId;

	@Column(name="pn_sta_acc_prov_code")
	private String pnStaAccProvCode;

	@Column(name="pn_sta_acc_prov_desc")
	private String pnStaAccProvDesc;

	//bi-directional many-to-one association to SiacRPnDefAccettazioneStato
	@OneToMany(mappedBy="siacDPnProvAccettazioneStato", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRPnProvAccettazioneStato> siacRPnProvAccettazioneStato;
	
	/**
	 * @return the pnStaAccProvId
	 */
	public Integer getPnStaAccProvId() {
		return pnStaAccProvId;
	}

	/**
	 * @param pnStaAccProvId the pnStaAccProvId to set
	 */
	public void setPnStaAccProvId(Integer pnStaAccProvId) {
		this.pnStaAccProvId = pnStaAccProvId;
	}

	/**
	 * @return the pnStaAccProvCode
	 */
	public String getPnStaAccProvCode() {
		return pnStaAccProvCode;
	}

	/**
	 * @param pnStaAccProvCode the pnStaAccProvCode to set
	 */
	public void setPnStaAccProvCode(String pnStaAccProvCode) {
		this.pnStaAccProvCode = pnStaAccProvCode;
	}

	/**
	 * @return the pnStaAccProvDesc
	 */
	public String getPnStaAccProvDesc() {
		return pnStaAccProvDesc;
	}

	/**
	 * @param pnStaAccProvDesc the pnStaAccProvDesc to set
	 */
	public void setPnStaAccProvDesc(String pnStaAccProvDesc) {
		this.pnStaAccProvDesc = pnStaAccProvDesc;
	}

	/**
	 * @return the siacRPnProvAccettazioneStato
	 */
	public List<SiacRPnProvAccettazioneStato> getSiacRPnProvAccettazioneStato() {
		return siacRPnProvAccettazioneStato;
	}

	/**
	 * @param siacRPnProvAccettazioneStato the siacRPnProvAccettazioneStato to set
	 */
	public void setSiacRPnProvAccettazioneStato(List<SiacRPnProvAccettazioneStato> siacRPnProvAccettazioneStato) {
		this.siacRPnProvAccettazioneStato = siacRPnProvAccettazioneStato;
	}

	@Override
	public Integer getUid() {
		return this.pnStaAccProvId;
	}

	@Override
	public void setUid(Integer uid) {
		this.pnStaAccProvId = uid;
	}

}