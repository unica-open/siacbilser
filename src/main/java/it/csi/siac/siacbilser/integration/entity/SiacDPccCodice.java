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
 * The persistent class for the siac_d_pcc_codice database table.
 * 
 */
@Entity
@Table(name="siac_d_pcc_codice")
@NamedQuery(name="SiacDPccCodice.findAll", query="SELECT s FROM SiacDPccCodice s")
public class SiacDPccCodice extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_PCC_CODICE_PCCCODID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_PCC_CODICE_PCCCOD_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_PCC_CODICE_PCCCODID_GENERATOR")
	@Column(name="pcccod_id")
	private Integer pcccodId;

	@Column(name="pcccod_code")
	private String pcccodCode;

	@Column(name="pcccod_desc")
	private String pcccodDesc;

	//bi-directional many-to-one association to SiacRPccCodiceClass
	@OneToMany(mappedBy="siacDPccCodice")
	private List<SiacRPccCodiceClass> siacRPccCodiceClasses;

	//bi-directional many-to-one association to SiacTDoc
	@OneToMany(mappedBy="siacDPccCodice")
	private List<SiacTDoc> siacTDocs;
	
	//bi-directional many-to-one association to SiacRPccCodiceClass
	@OneToMany(mappedBy="siacDPccCodice")
	private List<SiacRPccUfficioCodice> siacRPccUfficioCodices;

	public SiacDPccCodice() {
	}

	public Integer getPcccodId() {
		return this.pcccodId;
	}

	public void setPcccodId(Integer pcccodId) {
		this.pcccodId = pcccodId;
	}

	public String getPcccodCode() {
		return this.pcccodCode;
	}

	public void setPcccodCode(String pcccodCode) {
		this.pcccodCode = pcccodCode;
	}

	public String getPcccodDesc() {
		return this.pcccodDesc;
	}

	public void setPcccodDesc(String pcccodDesc) {
		this.pcccodDesc = pcccodDesc;
	}

	public List<SiacRPccCodiceClass> getSiacRPccCodiceClasses() {
		return this.siacRPccCodiceClasses;
	}

	public void setSiacRPccCodiceClasses(List<SiacRPccCodiceClass> siacRPccCodiceClasses) {
		this.siacRPccCodiceClasses = siacRPccCodiceClasses;
	}

	public SiacRPccCodiceClass addSiacRPccCodiceClass(SiacRPccCodiceClass siacRPccCodiceClass) {
		getSiacRPccCodiceClasses().add(siacRPccCodiceClass);
		siacRPccCodiceClass.setSiacDPccCodice(this);

		return siacRPccCodiceClass;
	}

	public SiacRPccCodiceClass removeSiacRPccCodiceClass(SiacRPccCodiceClass siacRPccCodiceClass) {
		getSiacRPccCodiceClasses().remove(siacRPccCodiceClass);
		siacRPccCodiceClass.setSiacDPccCodice(null);

		return siacRPccCodiceClass;
	}

	public List<SiacTDoc> getSiacTDocs() {
		return this.siacTDocs;
	}

	public void setSiacTDocs(List<SiacTDoc> siacTDocs) {
		this.siacTDocs = siacTDocs;
	}

	public SiacTDoc addSiacTDoc(SiacTDoc siacTDoc) {
		getSiacTDocs().add(siacTDoc);
		siacTDoc.setSiacDPccCodice(this);

		return siacTDoc;
	}

	public SiacTDoc removeSiacTDoc(SiacTDoc siacTDoc) {
		getSiacTDocs().remove(siacTDoc);
		siacTDoc.setSiacDPccCodice(null);

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
		siacRPccUfficioCodice.setSiacDPccCodice(this);

		return siacRPccUfficioCodice;
	}

	public SiacRPccUfficioCodice removeSiacRPccCodiceClass(SiacRPccUfficioCodice siacRPccUfficioCodice) {
		getSiacRPccUfficioCodices().remove(siacRPccUfficioCodice);
		siacRPccUfficioCodice.setSiacDPccCodice(null);

		return siacRPccUfficioCodice;
	}
	
	
	@Override
	public Integer getUid() {
		return getPcccodId();
	}

	@Override
	public void setUid(Integer uid) {
		setPcccodId(uid);
	}

}