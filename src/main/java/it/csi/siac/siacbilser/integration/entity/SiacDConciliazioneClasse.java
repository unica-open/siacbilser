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
 * The persistent class for the siac_d_conciliazione_classe database table.
 * 
 */
@Entity
@Table(name="siac_d_conciliazione_classe")
@NamedQuery(name="SiacDConciliazioneClasse.findAll", query="SELECT s FROM SiacDConciliazioneClasse s")
public class SiacDConciliazioneClasse extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_CONCILIAZIONE_CLASSE_CONCCLAID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_CONCILIAZIONE_CLASSE_CONCCLA_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_CONCILIAZIONE_CLASSE_CONCCLAID_GENERATOR")
	@Column(name="conccla_id")
	private Integer concclaId;

	@Column(name="conccla_code")
	private String concclaCode;

	@Column(name="conccla_desc")
	private String concclaDesc;

	//bi-directional many-to-one association to SiacRConciliazioneClasseCausaleEp
	@OneToMany(mappedBy="siacDConciliazioneClasse")
	private List<SiacRConciliazioneClasseCausaleEp> siacRConciliazioneClasseCausaleEps;

	//bi-directional many-to-one association to SiacRConciliazioneTitolo
	@OneToMany(mappedBy="siacDConciliazioneClasse")
	private List<SiacRConciliazioneTitolo> siacRConciliazioneTitolos;

	public SiacDConciliazioneClasse() {
	}

	public Integer getConcclaId() {
		return this.concclaId;
	}

	public void setConcclaId(Integer concclaId) {
		this.concclaId = concclaId;
	}

	public String getConcclaCode() {
		return this.concclaCode;
	}

	public void setConcclaCode(String concclaCode) {
		this.concclaCode = concclaCode;
	}

	public String getConcclaDesc() {
		return this.concclaDesc;
	}

	public void setConcclaDesc(String concclaDesc) {
		this.concclaDesc = concclaDesc;
	}

	public List<SiacRConciliazioneClasseCausaleEp> getSiacRConciliazioneClasseCausaleEps() {
		return this.siacRConciliazioneClasseCausaleEps;
	}

	public void setSiacRConciliazioneClasseCausaleEps(List<SiacRConciliazioneClasseCausaleEp> siacRConciliazioneClasseCausaleEps) {
		this.siacRConciliazioneClasseCausaleEps = siacRConciliazioneClasseCausaleEps;
	}

	public SiacRConciliazioneClasseCausaleEp addSiacRConciliazioneClasseCausaleEp(SiacRConciliazioneClasseCausaleEp siacRConciliazioneClasseCausaleEp) {
		getSiacRConciliazioneClasseCausaleEps().add(siacRConciliazioneClasseCausaleEp);
		siacRConciliazioneClasseCausaleEp.setSiacDConciliazioneClasse(this);

		return siacRConciliazioneClasseCausaleEp;
	}

	public SiacRConciliazioneClasseCausaleEp removeSiacRConciliazioneClasseCausaleEp(SiacRConciliazioneClasseCausaleEp siacRConciliazioneClasseCausaleEp) {
		getSiacRConciliazioneClasseCausaleEps().remove(siacRConciliazioneClasseCausaleEp);
		siacRConciliazioneClasseCausaleEp.setSiacDConciliazioneClasse(null);

		return siacRConciliazioneClasseCausaleEp;
	}

	public List<SiacRConciliazioneTitolo> getSiacRConciliazioneTitolos() {
		return this.siacRConciliazioneTitolos;
	}

	public void setSiacRConciliazioneTitolos(List<SiacRConciliazioneTitolo> siacRConciliazioneTitolos) {
		this.siacRConciliazioneTitolos = siacRConciliazioneTitolos;
	}

	public SiacRConciliazioneTitolo addSiacRConciliazioneTitolo(SiacRConciliazioneTitolo siacRConciliazioneTitolo) {
		getSiacRConciliazioneTitolos().add(siacRConciliazioneTitolo);
		siacRConciliazioneTitolo.setSiacDConciliazioneClasse(this);

		return siacRConciliazioneTitolo;
	}

	public SiacRConciliazioneTitolo removeSiacRConciliazioneTitolo(SiacRConciliazioneTitolo siacRConciliazioneTitolo) {
		getSiacRConciliazioneTitolos().remove(siacRConciliazioneTitolo);
		siacRConciliazioneTitolo.setSiacDConciliazioneClasse(null);

		return siacRConciliazioneTitolo;
	}

	@Override
	public Integer getUid() {
		return concclaId;
	}

	@Override
	public void setUid(Integer uid) {
		this.concclaId = uid;
	}

}