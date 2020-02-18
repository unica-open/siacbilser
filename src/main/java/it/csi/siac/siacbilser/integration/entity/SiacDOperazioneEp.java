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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the siac_d_operazione_ep database table.
 * 
 */
@Entity
@Table(name="siac_d_operazione_ep")
@NamedQuery(name="SiacDOperazioneEp.findAll", query="SELECT s FROM SiacDOperazioneEp s")
public class SiacDOperazioneEp extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_OPERAZIONE_EP_OPEREPID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_OPERAZIONE_EP_OPER_EP_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_OPERAZIONE_EP_OPEREPID_GENERATOR")
	@Column(name="oper_ep_id")
	private Integer operEpId;

	@Column(name="oper_ep_code")
	private String operEpCode;

	@Column(name="oper_ep_desc")
	private String operEpDesc;

	//bi-directional many-to-one association to SiacDOperazioneEpTipo
	@ManyToOne
	@JoinColumn(name="oper_ep_tipo_id")
	private SiacDOperazioneEpTipo siacDOperazioneEpTipo;

	//bi-directional many-to-one association to SiacRCausaleEpPdceContoOper
	@OneToMany(mappedBy="siacDOperazioneEp")
	private List<SiacRCausaleEpPdceContoOper> siacRCausaleEpPdceContoOpers;

	public SiacDOperazioneEp() {
	}

	public Integer getOperEpId() {
		return this.operEpId;
	}

	public void setOperEpId(Integer operEpId) {
		this.operEpId = operEpId;
	}

	public String getOperEpCode() {
		return this.operEpCode;
	}

	public void setOperEpCode(String operEpCode) {
		this.operEpCode = operEpCode;
	}

	public String getOperEpDesc() {
		return this.operEpDesc;
	}

	public void setOperEpDesc(String operEpDesc) {
		this.operEpDesc = operEpDesc;
	}

	public SiacDOperazioneEpTipo getSiacDOperazioneEpTipo() {
		return this.siacDOperazioneEpTipo;
	}

	public void setSiacDOperazioneEpTipo(SiacDOperazioneEpTipo siacDOperazioneEpTipo) {
		this.siacDOperazioneEpTipo = siacDOperazioneEpTipo;
	}

	public List<SiacRCausaleEpPdceContoOper> getSiacRCausaleEpPdceContoOpers() {
		return this.siacRCausaleEpPdceContoOpers;
	}

	public void setSiacRCausaleEpPdceContoOpers(List<SiacRCausaleEpPdceContoOper> siacRCausaleEpPdceContoOpers) {
		this.siacRCausaleEpPdceContoOpers = siacRCausaleEpPdceContoOpers;
	}

	public SiacRCausaleEpPdceContoOper addSiacRCausaleEpPdceContoOper(SiacRCausaleEpPdceContoOper siacRCausaleEpPdceContoOper) {
		getSiacRCausaleEpPdceContoOpers().add(siacRCausaleEpPdceContoOper);
		siacRCausaleEpPdceContoOper.setSiacDOperazioneEp(this);

		return siacRCausaleEpPdceContoOper;
	}

	public SiacRCausaleEpPdceContoOper removeSiacRCausaleEpPdceContoOper(SiacRCausaleEpPdceContoOper siacRCausaleEpPdceContoOper) {
		getSiacRCausaleEpPdceContoOpers().remove(siacRCausaleEpPdceContoOper);
		siacRCausaleEpPdceContoOper.setSiacDOperazioneEp(null);

		return siacRCausaleEpPdceContoOper;
	}

	@Override
	public Integer getUid() {
		return operEpId;
	}

	@Override
	public void setUid(Integer uid) {
		this.operEpId = uid;
	}

}