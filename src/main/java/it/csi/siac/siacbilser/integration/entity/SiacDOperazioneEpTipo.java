/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.util.List;

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
 * The persistent class for the siac_d_operazione_ep_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_operazione_ep_tipo")
@NamedQuery(name="SiacDOperazioneEpTipo.findAll", query="SELECT s FROM SiacDOperazioneEpTipo s")
public class SiacDOperazioneEpTipo extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_OPERAZIONE_EP_TIPO_OPEREPTIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_OPERAZIONE_EP_TIPO_OPER_EP_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_OPERAZIONE_EP_TIPO_OPEREPTIPOID_GENERATOR")
	@Column(name="oper_ep_tipo_id")
	private Integer operEpTipoId;


	@Column(name="oper_ep_tipo_code")
	private String operEpTipoCode;

	@Column(name="oper_ep_tipo_desc")
	private String operEpTipoDesc;


	//bi-directional many-to-one association to SiacDOperazioneEp
	@OneToMany(mappedBy="siacDOperazioneEpTipo")
	private List<SiacDOperazioneEp> siacDOperazioneEps;

	public SiacDOperazioneEpTipo() {
	}

	public Integer getOperEpTipoId() {
		return this.operEpTipoId;
	}

	public void setOperEpTipoId(Integer operEpTipoId) {
		this.operEpTipoId = operEpTipoId;
	}

	public String getOperEpTipoCode() {
		return this.operEpTipoCode;
	}

	public void setOperEpTipoCode(String operEpTipoCode) {
		this.operEpTipoCode = operEpTipoCode;
	}

	public String getOperEpTipoDesc() {
		return this.operEpTipoDesc;
	}

	public void setOperEpTipoDesc(String operEpTipoDesc) {
		this.operEpTipoDesc = operEpTipoDesc;
	}


	public List<SiacDOperazioneEp> getSiacDOperazioneEps() {
		return this.siacDOperazioneEps;
	}

	public void setSiacDOperazioneEps(List<SiacDOperazioneEp> siacDOperazioneEps) {
		this.siacDOperazioneEps = siacDOperazioneEps;
	}

	public SiacDOperazioneEp addSiacDOperazioneEp(SiacDOperazioneEp siacDOperazioneEp) {
		getSiacDOperazioneEps().add(siacDOperazioneEp);
		siacDOperazioneEp.setSiacDOperazioneEpTipo(this);

		return siacDOperazioneEp;
	}

	public SiacDOperazioneEp removeSiacDOperazioneEp(SiacDOperazioneEp siacDOperazioneEp) {
		getSiacDOperazioneEps().remove(siacDOperazioneEp);
		siacDOperazioneEp.setSiacDOperazioneEpTipo(null);

		return siacDOperazioneEp;
	}

	@Override
	public Integer getUid() {
		return operEpTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		this.operEpTipoId = uid;
	}

}