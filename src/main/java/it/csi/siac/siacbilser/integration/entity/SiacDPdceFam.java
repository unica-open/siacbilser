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
 * The persistent class for the siac_d_pdce_fam database table.
 * 
 */
@Entity
@Table(name="siac_d_pdce_fam")
@NamedQuery(name="SiacDPdceFam.findAll", query="SELECT s FROM SiacDPdceFam s")
public class SiacDPdceFam extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_PDCE_FAM_PDCEFAMID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_PDCE_FAM_PDCE_FAM_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_PDCE_FAM_PDCEFAMID_GENERATOR")
	@Column(name="pdce_fam_id")
	private Integer pdceFamId;

	@Column(name="pdce_fam_code")
	private String pdceFamCode;

	@Column(name="pdce_fam_desc")
	private String pdceFamDesc;

	@Column(name="pdce_fam_segno")
	private String pdceFamSegno;
	
	@Column(name="pdce_livello_legge")
	private Integer pdceLivelloLegge;

	//bi-directional many-to-one association to SiacTPdceFamTree
	@OneToMany(mappedBy="siacDPdceFam")
	private List<SiacTPdceFamTree> siacTPdceFamTrees;
	
	//bi-directional many-to-one association to SiacDAmbito
	@ManyToOne
	@JoinColumn(name="ambito_id")
	private SiacDAmbito siacDAmbito;

	public SiacDPdceFam() {
	}

	public Integer getPdceFamId() {
		return this.pdceFamId;
	}

	public void setPdceFamId(Integer pdceFamId) {
		this.pdceFamId = pdceFamId;
	}

	public String getPdceFamCode() {
		return this.pdceFamCode;
	}

	public void setPdceFamCode(String pdceFamCode) {
		this.pdceFamCode = pdceFamCode;
	}

	public String getPdceFamDesc() {
		return this.pdceFamDesc;
	}

	public void setPdceFamDesc(String pdceFamDesc) {
		this.pdceFamDesc = pdceFamDesc;
	}

	public String getPdceFamSegno() {
		return this.pdceFamSegno;
	}

	public void setPdceFamSegno(String pdceFamSegno) {
		this.pdceFamSegno = pdceFamSegno;
	}

	public Integer getPdceLivelloLegge() {
		return pdceLivelloLegge;
	}

	public void setPdceLivelloLegge(Integer pdceLivelloLegge) {
		this.pdceLivelloLegge = pdceLivelloLegge;
	}

	public List<SiacTPdceFamTree> getSiacTPdceFamTrees() {
		return this.siacTPdceFamTrees;
	}

	public void setSiacTPdceFamTrees(List<SiacTPdceFamTree> siacTPdceFamTrees) {
		this.siacTPdceFamTrees = siacTPdceFamTrees;
	}

	public SiacTPdceFamTree addSiacTPdceFamTree(SiacTPdceFamTree siacTPdceFamTree) {
		getSiacTPdceFamTrees().add(siacTPdceFamTree);
		siacTPdceFamTree.setSiacDPdceFam(this);

		return siacTPdceFamTree;
	}

	public SiacTPdceFamTree removeSiacTPdceFamTree(SiacTPdceFamTree siacTPdceFamTree) {
		getSiacTPdceFamTrees().remove(siacTPdceFamTree);
		siacTPdceFamTree.setSiacDPdceFam(null);

		return siacTPdceFamTree;
	}
	
	public SiacDAmbito getSiacDAmbito() {
		return siacDAmbito;
	}

	public void setSiacDAmbito(SiacDAmbito siacDAmbito) {
		this.siacDAmbito = siacDAmbito;
	}

	@Override
	public Integer getUid() {
		return pdceFamId;
	}

	@Override
	public void setUid(Integer uid) {
		this.pdceFamId = uid;
	}

}