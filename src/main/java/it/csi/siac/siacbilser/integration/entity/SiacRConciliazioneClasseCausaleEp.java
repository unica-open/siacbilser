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
 * The persistent class for the siac_r_conciliazione_classe_causale_ep database table.
 * 
 */
@Entity
@Table(name="siac_r_conciliazione_classe_causale_ep")
@NamedQuery(name="SiacRConciliazioneClasseCausaleEp.findAll", query="SELECT s FROM SiacRConciliazioneClasseCausaleEp s")
public class SiacRConciliazioneClasseCausaleEp extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_CONCILIAZIONE_CLASSE_CAUSALE_EP_CONCCLACAUSID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_CONCILIAZIONE_CLASSE_CAUSALE_EP_CONCCLACAUS_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_CONCILIAZIONE_CLASSE_CAUSALE_EP_CONCCLACAUSID_GENERATOR")
	@Column(name="concclacaus_id")
	private Integer concclacausId;

	//bi-directional many-to-one association to SiacDConciliazioneClasse
	@ManyToOne
	@JoinColumn(name="conccla_id")
	private SiacDConciliazioneClasse siacDConciliazioneClasse;

//	//bi-directional many-to-one association to SiacTCausaleEp
//	@ManyToOne
//	@JoinColumn(name="causale_ep_id")
//	private SiacTCausaleEp siacTCausaleEp; //legame da deprecare! SIAC-4596
	
	//bi-directional many-to-one association to SiacTCausaleEp
	@ManyToOne
	@JoinColumn(name="causale_ep_pdce_conto_id")
	private SiacRCausaleEpPdceConto siacRCausaleEpPdceConto; //new SIAC-4596
	

	public SiacRConciliazioneClasseCausaleEp() {
	}

	public Integer getConcclacausId() {
		return this.concclacausId;
	}

	public void setConcclacausId(Integer concclacausId) {
		this.concclacausId = concclacausId;
	}

	public SiacDConciliazioneClasse getSiacDConciliazioneClasse() {
		return this.siacDConciliazioneClasse;
	}

	public void setSiacDConciliazioneClasse(SiacDConciliazioneClasse siacDConciliazioneClasse) {
		this.siacDConciliazioneClasse = siacDConciliazioneClasse;
	}

//	public SiacTCausaleEp getSiacTCausaleEp() {
//		return this.siacTCausaleEp;
//	}
//
//	public void setSiacTCausaleEp(SiacTCausaleEp siacTCausaleEp) {
//		this.siacTCausaleEp = siacTCausaleEp;
//	}
	
	public SiacRCausaleEpPdceConto getSiacRCausaleEpPdceConto() {
		return siacRCausaleEpPdceConto;
	}

	public void setSiacRCausaleEpPdceConto(SiacRCausaleEpPdceConto siacRCausaleEpPdceConto) {
		this.siacRCausaleEpPdceConto = siacRCausaleEpPdceConto;
	}

	@Override
	public Integer getUid() {
		return this.concclacausId;
	}

	@Override
	public void setUid(Integer uid) {
		this.concclacausId = uid;
	}

}