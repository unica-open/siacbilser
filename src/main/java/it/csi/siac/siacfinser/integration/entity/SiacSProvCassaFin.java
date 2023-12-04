/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_t_prov_cassa database table.
 * 
 */
@Entity
@Table(name="siac_s_prov_cassa")
public class SiacSProvCassaFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_S_PROV_CASSA_ID_GENERATOR", allocationSize=1, sequenceName="siac_s_prov_cassa_provc_st_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_S_PROV_CASSA_ID_GENERATOR")
	@Column(name="provc_st_id")
	private Integer provcStId;
	
	//bi-directional many-to-one association to SiacTAttoAmmFin
	@ManyToOne
	@JoinColumn(name="provc_id")
	private SiacTProvCassaFin siacTProvCassaFin;

	//bi-directional many-to-one association to SiacTClassFin
	@ManyToOne
	@JoinColumn(name="sac_id")
	private SiacTClassFin siacTClassSAC;

	@Column(name="sac_code")
	private String sacCode;

	@Column(name="sac_desc")
	private String sacDesc;

	@Column(name="sac_tipo_code")
	private String sacTipoCode;

	@Column(name="sac_tipo_desc")
	private String sacTipoDesc;
	
	@Column(name="provc_data_invio_servizio")
	private Date provcDataInvioServizio;
	
	public SiacSProvCassaFin() {
	}


	@Override
	public Integer getUid() {
		return this.provcStId;
	}

	@Override
	public void setUid(Integer uid) {
		this.provcStId = uid;
	}


	public Integer getProvcStId() {
		return provcStId;
	}


	public void setProvcStId(Integer provcStId) {
		this.provcStId = provcStId;
	}



	public SiacTProvCassaFin getSiacTProvCassaFin() {
		return siacTProvCassaFin;
	}


	public void setSiacTProvCassaFin(SiacTProvCassaFin siacTProvCassaFin) {
		this.siacTProvCassaFin = siacTProvCassaFin;
	}


	public String getSacCode() {
		return sacCode;
	}

	public SiacTClassFin getSiacTClassSAC() {
		return siacTClassSAC;
	}


	public void setSiacTClassSAC(SiacTClassFin siacTClassSAC) {
		this.siacTClassSAC = siacTClassSAC;
	}


	public void setSacCode(String sacCode) {
		this.sacCode = sacCode;
	}


	public String getSacDesc() {
		return sacDesc;
	}


	public void setSacDesc(String sacDesc) {
		this.sacDesc = sacDesc;
	}


	public String getSacTipoCode() {
		return sacTipoCode;
	}


	public void setSacTipoCode(String sacTipoCode) {
		this.sacTipoCode = sacTipoCode;
	}


	public String getSacTipoDesc() {
		return sacTipoDesc;
	}


	public void setSacTipoDesc(String sacTipoDesc) {
		this.sacTipoDesc = sacTipoDesc;
	}


	public Date getProvcDataInvioServizio() {
		return provcDataInvioServizio;
	}


	public void setProvcDataInvioServizio(Date provcDataInvioServizio) {
		this.provcDataInvioServizio = provcDataInvioServizio;
	}
	
	
	
}