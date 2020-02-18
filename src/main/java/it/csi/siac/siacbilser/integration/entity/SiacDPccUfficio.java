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
 * The persistent class for the siac_d_pcc_ufficio database table.
 * 
 */
@Entity
@Table(name="siac_d_pcc_ufficio")
@NamedQuery(name="SiacDPccUfficio.findAll", query="SELECT s FROM SiacDPccUfficio s")
public class SiacDPccUfficio extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_PCC_UFFICIO_PCCUFFID_GENERATOR", allocationSize = 1, sequenceName="SIAC_D_PCC_UFFICIO_PCCUFF_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_PCC_UFFICIO_PCCUFFID_GENERATOR")
	@Column(name="pccuff_id")
	private Integer pccuffId;

	@Column(name="pccuff_code")
	private String pccuffCode;

	@Column(name="pccuff_desc")
	private String pccuffDesc;

	//bi-directional many-to-one association to SiacRPccUfficioClass
	@OneToMany(mappedBy="siacDPccUfficio")
	private List<SiacRPccUfficioClass> siacRPccUfficioClasses;

	//bi-directional many-to-one association to SiacTDoc
	@OneToMany(mappedBy="siacDPccUfficio")
	private List<SiacTDoc> siacTDocs;
	
	//bi-directional many-to-one association to SiacRPccCodiceClass
	@OneToMany(mappedBy="siacDPccUfficio")
	private List<SiacRPccUfficioCodice> siacRPccUfficioCodices;

	public SiacDPccUfficio() {
	}

	public Integer getPccuffId() {
		return this.pccuffId;
	}

	public void setPccuffId(Integer pccuffId) {
		this.pccuffId = pccuffId;
	}

	public String getPccuffCode() {
		return this.pccuffCode;
	}

	public void setPccuffCode(String pccuffCode) {
		this.pccuffCode = pccuffCode;
	}

	public String getPccuffDesc() {
		return this.pccuffDesc;
	}

	public void setPccuffDesc(String pccuffDesc) {
		this.pccuffDesc = pccuffDesc;
	}

	public List<SiacRPccUfficioClass> getSiacRPccUfficioClasses() {
		return this.siacRPccUfficioClasses;
	}

	public void setSiacRPccUfficioClasses(List<SiacRPccUfficioClass> siacRPccUfficioClasses) {
		this.siacRPccUfficioClasses = siacRPccUfficioClasses;
	}

	public SiacRPccUfficioClass addSiacRPccUfficioClass(SiacRPccUfficioClass siacRPccUfficioClass) {
		getSiacRPccUfficioClasses().add(siacRPccUfficioClass);
		siacRPccUfficioClass.setSiacDPccUfficio(this);

		return siacRPccUfficioClass;
	}

	public SiacRPccUfficioClass removeSiacRPccUfficioClass(SiacRPccUfficioClass siacRPccUfficioClass) {
		getSiacRPccUfficioClasses().remove(siacRPccUfficioClass);
		siacRPccUfficioClass.setSiacDPccUfficio(null);

		return siacRPccUfficioClass;
	}

	public List<SiacTDoc> getSiacTDocs() {
		return this.siacTDocs;
	}

	public void setSiacTDocs(List<SiacTDoc> siacTDocs) {
		this.siacTDocs = siacTDocs;
	}

	public SiacTDoc addSiacTDoc(SiacTDoc siacTDoc) {
		getSiacTDocs().add(siacTDoc);
		siacTDoc.setSiacDPccUfficio(this);

		return siacTDoc;
	}

	public SiacTDoc removeSiacTDoc(SiacTDoc siacTDoc) {
		getSiacTDocs().remove(siacTDoc);
		siacTDoc.setSiacDPccUfficio(null);

		return siacTDoc;
	}
	
	public List<SiacRPccUfficioCodice> getSiacRPccUfficioCodices() {
		return this.siacRPccUfficioCodices;
	}

	public void setSiacRPccUfficioCodices(List<SiacRPccUfficioCodice> siacRPccUfficioCodices) {
		this.siacRPccUfficioCodices = siacRPccUfficioCodices;
	}

	public SiacRPccUfficioCodice addSiacRPccCodiceClass(SiacRPccUfficioCodice siacRPccUfficioCodice) {
		getSiacRPccUfficioCodices().add(siacRPccUfficioCodice);
		siacRPccUfficioCodice.setSiacDPccUfficio(this);

		return siacRPccUfficioCodice;
	}

	public SiacRPccUfficioCodice removeSiacRPccCodiceClass(SiacRPccUfficioCodice siacRPccUfficioCodice) {
		getSiacRPccUfficioCodices().remove(siacRPccUfficioCodice);
		siacRPccUfficioCodice.setSiacDPccCodice(null);

		return siacRPccUfficioCodice;
	}

	@Override
	public Integer getUid() {
		return getPccuffId();
	}

	@Override
	public void setUid(Integer uid) {
		setPccuffId(uid);
	}

}