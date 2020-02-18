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


/**
 * The persistent class for the siac_t_operazione_asincrona_det database table.
 * 
 */
@Entity
@Table(name="siac_t_operazione_asincrona_det")
@NamedQuery(name="SiacTOperazioneAsincronaDet.findAll", query="SELECT s FROM SiacTOperazioneAsincronaDet s")
public class SiacTOperazioneAsincronaDet extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_OPERAZIONE_ASINCRONA_DET_OPASDETID_GENERATOR", allocationSize = 1, sequenceName="SIAC_T_OPERAZIONE_ASINCRONA_DET_OPAS_DET_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_OPERAZIONE_ASINCRONA_DET_OPASDETID_GENERATOR")
	@Column(name="opas_det_id")
	private Integer opasDetId;

	@Column(name="opas_det_code")
	private String opasDetCode;

	@Column(name="opas_det_desc")
	private String opasDetDesc;

	@Column(name="opas_det_esito")
	private String opasDetEsito;

	@Column(name="opas_det_msg")
	private String opasDetMsg;

	@Column(name="opas_srv_resp")
	private String opasSrvResp;

	//bi-directional many-to-one association to SiacTOperazioneAsincrona
	@ManyToOne
	@JoinColumn(name="opas_id")
	private SiacTOperazioneAsincrona siacTOperazioneAsincrona;

	public Integer getOpasDetId() {
		return this.opasDetId;
	}

	public void setOpasDetId(Integer opasDetId) {
		this.opasDetId = opasDetId;
	}

	public String getOpasDetCode() {
		return this.opasDetCode;
	}

	public void setOpasDetCode(String opasDetCode) {
		this.opasDetCode = opasDetCode;
	}

	public String getOpasDetDesc() {
		return this.opasDetDesc;
	}

	public void setOpasDetDesc(String opasDetDesc) {
		this.opasDetDesc = opasDetDesc;
	}

	public String getOpasDetEsito() {
		return this.opasDetEsito;
	}

	public void setOpasDetEsito(String opasDetEsito) {
		this.opasDetEsito = opasDetEsito;
	}

	public String getOpasDetMsg() {
		return this.opasDetMsg;
	}

	public void setOpasDetMsg(String opasDetMsg) {
		this.opasDetMsg = opasDetMsg;
	}

	public String getOpasSrvResp() {
		return opasSrvResp;
	}

	public void setOpasSrvResp(String opasSrvResp) {
		this.opasSrvResp = opasSrvResp;
	}

	public SiacTOperazioneAsincrona getSiacTOperazioneAsincrona() {
		return this.siacTOperazioneAsincrona;
	}

	public void setSiacTOperazioneAsincrona(SiacTOperazioneAsincrona siacTOperazioneAsincrona) {
		this.siacTOperazioneAsincrona = siacTOperazioneAsincrona;
	}

	@Override
	public Integer getUid() {
		return opasDetId;
	}

	@Override
	public void setUid(Integer uid) {
		opasDetId = uid;
	}

}