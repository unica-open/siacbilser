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
 * The persistent class for the siac_r_causale_ep_pdce_conto_oper database table.
 * 
 */
@Entity
@Table(name="siac_r_causale_ep_pdce_conto_oper")
@NamedQuery(name="SiacRCausaleEpPdceContoOper.findAll", query="SELECT s FROM SiacRCausaleEpPdceContoOper s")
public class SiacRCausaleEpPdceContoOper extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_CAUSALE_EP_PDCE_CONTO_OPER_CAUSALEEPPDCECONTOOPID_GENERATOR", allocationSize=1, sequenceName="siac_r_causale_ep_pdce_conto_op_causale_ep_pdce_conto_op_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_CAUSALE_EP_PDCE_CONTO_OPER_CAUSALEEPPDCECONTOOPID_GENERATOR")
	@Column(name="causale_ep_pdce_conto_op_id")
	private Integer causaleEpPdceContoOpId;

	//bi-directional many-to-one association to SiacDOperazioneEp
	@ManyToOne
	@JoinColumn(name="oper_ep_id")
	private SiacDOperazioneEp siacDOperazioneEp;

	//bi-directional many-to-one association to SiacRCausaleEpPdceConto
	@ManyToOne
	@JoinColumn(name="causale_ep_pdce_conto_id")
	private SiacRCausaleEpPdceConto siacRCausaleEpPdceConto;

	public SiacRCausaleEpPdceContoOper() {
	}

	public Integer getCausaleEpPdceContoOpId() {
		return this.causaleEpPdceContoOpId;
	}

	public void setCausaleEpPdceContoOpId(Integer causaleEpPdceContoOpId) {
		this.causaleEpPdceContoOpId = causaleEpPdceContoOpId;
	}

	public SiacDOperazioneEp getSiacDOperazioneEp() {
		return this.siacDOperazioneEp;
	}

	public void setSiacDOperazioneEp(SiacDOperazioneEp siacDOperazioneEp) {
		this.siacDOperazioneEp = siacDOperazioneEp;
	}

	public SiacRCausaleEpPdceConto getSiacRCausaleEpPdceConto() {
		return this.siacRCausaleEpPdceConto;
	}

	public void setSiacRCausaleEpPdceConto(SiacRCausaleEpPdceConto siacRCausaleEpPdceConto) {
		this.siacRCausaleEpPdceConto = siacRCausaleEpPdceConto;
	}

	@Override
	public Integer getUid() {
		return causaleEpPdceContoOpId;
	}

	@Override
	public void setUid(Integer uid) {
		this.causaleEpPdceContoOpId = uid;
	}

}