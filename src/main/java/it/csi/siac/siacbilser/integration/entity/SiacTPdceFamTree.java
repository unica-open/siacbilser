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
 * The persistent class for the siac_t_pdce_fam_tree database table.
 * 
 */
@Entity
@Table(name="siac_t_pdce_fam_tree")
@NamedQuery(name="SiacTPdceFamTree.findAll", query="SELECT s FROM SiacTPdceFamTree s")
public class SiacTPdceFamTree extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_PDCE_FAM_TREE_PDCEFAMTREEID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_PDCE_FAM_TREE_PDCE_FAM_TREE_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_PDCE_FAM_TREE_PDCEFAMTREEID_GENERATOR")
	@Column(name="pdce_fam_tree_id")
	private Integer pdceFamTreeId;
	
	@Column(name="pdce_fam_code")
	private String pdceFamCode;

	@Column(name="pdce_fam_desc")
	private String pdceFamDesc;
	
	//bi-directional many-to-one association to SiacTPdceConto
	@OneToMany(mappedBy="siacTPdceFamTree")
	private List<SiacTPdceConto> siacTPdceContos;
	
	//bi-directional many-to-one association to SiacDPdceFam
	@ManyToOne
	@JoinColumn(name="pdce_fam_id")
	private SiacDPdceFam siacDPdceFam;
	
	//bi-directional many-to-one association to SiacDAmbito
	@ManyToOne
	@JoinColumn(name="ambito_id")
	private SiacDAmbito siacDAmbito;

	public SiacTPdceFamTree() {
	}

	public Integer getPdceFamTreeId() {
		return this.pdceFamTreeId;
	}

	public void setPdceFamTreeId(Integer pdceFamTreeId) {
		this.pdceFamTreeId = pdceFamTreeId;
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

	public List<SiacTPdceConto> getSiacTPdceContos() {
		return this.siacTPdceContos;
	}

	public void setSiacTPdceContos(List<SiacTPdceConto> siacTPdceContos) {
		this.siacTPdceContos = siacTPdceContos;
	}

	public SiacTPdceConto addSiacTPdceConto(SiacTPdceConto siacTPdceConto) {
		getSiacTPdceContos().add(siacTPdceConto);
		siacTPdceConto.setSiacTPdceFamTree(this);

		return siacTPdceConto;
	}

	public SiacTPdceConto removeSiacTPdceConto(SiacTPdceConto siacTPdceConto) {
		getSiacTPdceContos().remove(siacTPdceConto);
		siacTPdceConto.setSiacTPdceFamTree(null);

		return siacTPdceConto;
	}

	public SiacDPdceFam getSiacDPdceFam() {
		return this.siacDPdceFam;
	}

	public void setSiacDPdceFam(SiacDPdceFam siacDPdceFam) {
		this.siacDPdceFam = siacDPdceFam;
	}
	
	public SiacDAmbito getSiacDAmbito() {
		return siacDAmbito;
	}

	public void setSiacDAmbito(SiacDAmbito siacDAmbito) {
		this.siacDAmbito = siacDAmbito;
	}

	@Override
	public Integer getUid() {
		return this.pdceFamTreeId;
	}

	@Override
	public void setUid(Integer uid) {
		this.pdceFamTreeId = uid;
	}

}