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
 * The persistent class for the siac_d_pdce_rel_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_pdce_rel_tipo")
@NamedQuery(name="SiacDPdceRelTipo.findAll", query="SELECT s FROM SiacDPdceRelTipo s")
public class SiacDPdceRelTipo extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_PDCE_REL_TIPO_PDCERELID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_PDCE_REL_TIPO_PDCEREL_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_PDCE_REL_TIPO_PDCERELID_GENERATOR")
	@Column(name="pdcerel_id")
	private Integer pdcerelId;

	@Column(name="pdcerel_code")
	private String pdcerelCode;

	@Column(name="pdcerel_desc")
	private String pdcerelDesc;

	//bi-directional many-to-one association to SiacRPdceConto
	@OneToMany(mappedBy="siacDPdceRelTipo")
	private List<SiacRPdceConto> siacRPdceContos;

	public SiacDPdceRelTipo() {
	}

	public Integer getPdcerelId() {
		return this.pdcerelId;
	}

	public void setPdcerelId(Integer pdcerelId) {
		this.pdcerelId = pdcerelId;
	}

	public String getPdcerelCode() {
		return this.pdcerelCode;
	}

	public void setPdcerelCode(String pdcerelCode) {
		this.pdcerelCode = pdcerelCode;
	}

	public String getPdcerelDesc() {
		return this.pdcerelDesc;
	}

	public void setPdcerelDesc(String pdcerelDesc) {
		this.pdcerelDesc = pdcerelDesc;
	}

	public List<SiacRPdceConto> getSiacRPdceContos() {
		return this.siacRPdceContos;
	}

	public void setSiacRPdceContos(List<SiacRPdceConto> siacRPdceContos) {
		this.siacRPdceContos = siacRPdceContos;
	}

	public SiacRPdceConto addSiacRPdceConto(SiacRPdceConto siacRPdceConto) {
		getSiacRPdceContos().add(siacRPdceConto);
		siacRPdceConto.setSiacDPdceRelTipo(this);

		return siacRPdceConto;
	}

	public SiacRPdceConto removeSiacRPdceConto(SiacRPdceConto siacRPdceConto) {
		getSiacRPdceContos().remove(siacRPdceConto);
		siacRPdceConto.setSiacDPdceRelTipo(null);

		return siacRPdceConto;
	}

	@Override
	public Integer getUid() {
		return this.pdcerelId;
	}

	@Override
	public void setUid(Integer uid) {
		this.pdcerelId = uid;
	}

}