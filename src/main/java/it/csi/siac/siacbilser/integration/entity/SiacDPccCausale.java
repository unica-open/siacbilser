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
 * The persistent class for the siac_d_pcc_causale database table.
 * 
 */
@Entity
@Table(name="siac_d_pcc_causale")
@NamedQuery(name="SiacDPccCausale.findAll", query="SELECT s FROM SiacDPccCausale s")
public class SiacDPccCausale extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_PCC_CAUSALE_PCCCAUID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_PCC_CAUSALE_PCCCAU_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_PCC_CAUSALE_PCCCAUID_GENERATOR")
	@Column(name="pcccau_id")
	private Integer pcccauId;

	@Column(name="pcccau_code")
	private String pcccauCode;

	@Column(name="pcccau_desc")
	private String pcccauDesc;

	//bi-directional many-to-one association to SiacRPccDebitoStatoCausale
	@OneToMany(mappedBy="siacDPccCausale")
	private List<SiacRPccDebitoStatoCausale> siacRPccDebitoStatoCausales;

	//bi-directional many-to-one association to SiacTRegistroPcc
	@OneToMany(mappedBy="siacDPccCausale")
	private List<SiacTRegistroPcc> siacTRegistroPccs;

	public SiacDPccCausale() {
	}

	public Integer getPcccauId() {
		return this.pcccauId;
	}

	public void setPcccauId(Integer pcccauId) {
		this.pcccauId = pcccauId;
	}

	public String getPcccauCode() {
		return this.pcccauCode;
	}

	public void setPcccauCode(String pcccauCode) {
		this.pcccauCode = pcccauCode;
	}

	public String getPcccauDesc() {
		return this.pcccauDesc;
	}

	public void setPcccauDesc(String pcccauDesc) {
		this.pcccauDesc = pcccauDesc;
	}

	public List<SiacRPccDebitoStatoCausale> getSiacRPccDebitoStatoCausales() {
		return this.siacRPccDebitoStatoCausales;
	}

	public void setSiacRPccDebitoStatoCausales(List<SiacRPccDebitoStatoCausale> siacRPccDebitoStatoCausales) {
		this.siacRPccDebitoStatoCausales = siacRPccDebitoStatoCausales;
	}

	public SiacRPccDebitoStatoCausale addSiacRPccDebitoStatoCausale(SiacRPccDebitoStatoCausale siacRPccDebitoStatoCausale) {
		getSiacRPccDebitoStatoCausales().add(siacRPccDebitoStatoCausale);
		siacRPccDebitoStatoCausale.setSiacDPccCausale(this);

		return siacRPccDebitoStatoCausale;
	}

	public SiacRPccDebitoStatoCausale removeSiacRPccDebitoStatoCausale(SiacRPccDebitoStatoCausale siacRPccDebitoStatoCausale) {
		getSiacRPccDebitoStatoCausales().remove(siacRPccDebitoStatoCausale);
		siacRPccDebitoStatoCausale.setSiacDPccCausale(null);

		return siacRPccDebitoStatoCausale;
	}

	public List<SiacTRegistroPcc> getSiacTRegistroPccs() {
		return this.siacTRegistroPccs;
	}

	public void setSiacTRegistroPccs(List<SiacTRegistroPcc> siacTRegistroPccs) {
		this.siacTRegistroPccs = siacTRegistroPccs;
	}

	public SiacTRegistroPcc addSiacTRegistroPcc(SiacTRegistroPcc siacTRegistroPcc) {
		getSiacTRegistroPccs().add(siacTRegistroPcc);
		siacTRegistroPcc.setSiacDPccCausale(this);

		return siacTRegistroPcc;
	}

	public SiacTRegistroPcc removeSiacTRegistroPcc(SiacTRegistroPcc siacTRegistroPcc) {
		getSiacTRegistroPccs().remove(siacTRegistroPcc);
		siacTRegistroPcc.setSiacDPccCausale(null);

		return siacTRegistroPcc;
	}

	@Override
	public Integer getUid() {
		return pcccauId;
	}

	@Override
	public void setUid(Integer uid) {
		this.pcccauId = uid;
	}

}