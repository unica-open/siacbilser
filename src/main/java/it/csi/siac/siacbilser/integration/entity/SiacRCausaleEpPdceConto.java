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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the siac_r_causale_ep_pdce_conto database table.
 * 
 */
@Entity
@Table(name="siac_r_causale_ep_pdce_conto")
@NamedQuery(name="SiacRCausaleEpPdceConto.findAll", query="SELECT s FROM SiacRCausaleEpPdceConto s")
public class SiacRCausaleEpPdceConto extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_CAUSALE_EP_PDCE_CONTO_CAUSALEEPPDCECONTOID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_CAUSALE_EP_PDCE_CONTO_CAUSALE_EP_PDCE_CONTO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_CAUSALE_EP_PDCE_CONTO_CAUSALEEPPDCECONTOID_GENERATOR")
	@Column(name="causale_ep_pdce_conto_id")
	private Integer causaleEpPdceContoId;

	//bi-directional many-to-one association to SiacTCausaleEp
	@ManyToOne
	@JoinColumn(name="causale_ep_id")
	private SiacTCausaleEp siacTCausaleEp;

	//bi-directional many-to-one association to SiacTPdceConto
	@ManyToOne
	@JoinColumn(name="pdce_conto_id")
	private SiacTPdceConto siacTPdceConto;

	//bi-directional many-to-one association to SiacRCausaleEpPdceContoOper
	@OneToMany(mappedBy="siacRCausaleEpPdceConto", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRCausaleEpPdceContoOper> siacRCausaleEpPdceContoOpers;
	
	//bi-directional many-to-one association to SiacRConciliazioneClasseCausaleEp
	@OneToMany(mappedBy="siacRCausaleEpPdceConto", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE}) //new SIC-4596
	private List<SiacRConciliazioneClasseCausaleEp> siacRConciliazioneClasseCausaleEps;
	

	public SiacRCausaleEpPdceConto() {
	}

	public Integer getCausaleEpPdceContoId() {
		return this.causaleEpPdceContoId;
	}

	public void setCausaleEpPdceContoId(Integer causaleEpPdceContoId) {
		this.causaleEpPdceContoId = causaleEpPdceContoId;
	}

	public SiacTCausaleEp getSiacTCausaleEp() {
		return this.siacTCausaleEp;
	}

	public void setSiacTCausaleEp(SiacTCausaleEp siacTCausaleEp) {
		this.siacTCausaleEp = siacTCausaleEp;
	}

	public SiacTPdceConto getSiacTPdceConto() {
		return this.siacTPdceConto;
	}

	public void setSiacTPdceConto(SiacTPdceConto siacTPdceConto) {
		this.siacTPdceConto = siacTPdceConto;
	}

	public List<SiacRCausaleEpPdceContoOper> getSiacRCausaleEpPdceContoOpers() {
		return this.siacRCausaleEpPdceContoOpers;
	}

	public void setSiacRCausaleEpPdceContoOpers(List<SiacRCausaleEpPdceContoOper> siacRCausaleEpPdceContoOpers) {
		this.siacRCausaleEpPdceContoOpers = siacRCausaleEpPdceContoOpers;
	}

	public SiacRCausaleEpPdceContoOper addSiacRCausaleEpPdceContoOper(SiacRCausaleEpPdceContoOper siacRCausaleEpPdceContoOper) {
		getSiacRCausaleEpPdceContoOpers().add(siacRCausaleEpPdceContoOper);
		siacRCausaleEpPdceContoOper.setSiacRCausaleEpPdceConto(this);

		return siacRCausaleEpPdceContoOper;
	}

	public SiacRCausaleEpPdceContoOper removeSiacRCausaleEpPdceContoOper(SiacRCausaleEpPdceContoOper siacRCausaleEpPdceContoOper) {
		getSiacRCausaleEpPdceContoOpers().remove(siacRCausaleEpPdceContoOper);
		siacRCausaleEpPdceContoOper.setSiacRCausaleEpPdceConto(null);

		return siacRCausaleEpPdceContoOper;
	}
	
	public List<SiacRConciliazioneClasseCausaleEp> getSiacRConciliazioneClasseCausaleEps() {
		return this.siacRConciliazioneClasseCausaleEps;
	}

	public void setSiacRConciliazioneClasseCausaleEps(List<SiacRConciliazioneClasseCausaleEp> siacRConciliazioneClasseCausaleEps) {
		this.siacRConciliazioneClasseCausaleEps = siacRConciliazioneClasseCausaleEps;
	}

	public SiacRConciliazioneClasseCausaleEp addSiacRConciliazioneClasseCausaleEp(SiacRConciliazioneClasseCausaleEp siacRConciliazioneClasseCausaleEp) {
		getSiacRConciliazioneClasseCausaleEps().add(siacRConciliazioneClasseCausaleEp);
		siacRConciliazioneClasseCausaleEp.setSiacRCausaleEpPdceConto(this);

		return siacRConciliazioneClasseCausaleEp;
	}

	public SiacRConciliazioneClasseCausaleEp removeSiacRConciliazioneClasseCausaleEp(SiacRConciliazioneClasseCausaleEp siacRConciliazioneClasseCausaleEp) {
		getSiacRConciliazioneClasseCausaleEps().remove(siacRConciliazioneClasseCausaleEp);
		siacRConciliazioneClasseCausaleEp.setSiacRCausaleEpPdceConto(null);

		return siacRConciliazioneClasseCausaleEp;
	}

	@Override
	public Integer getUid() {
		return causaleEpPdceContoId;
	}

	@Override
	public void setUid(Integer uid) {
		this.causaleEpPdceContoId = uid;
	}

}