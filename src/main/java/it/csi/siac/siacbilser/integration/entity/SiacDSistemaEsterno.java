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
 * The persistent class for the siac_d_sistema_esterno database table.
 * 
 */
@Entity
@Table(name="siac_d_sistema_esterno")
@NamedQuery(name="SiacDSistemaEsterno.findAll", query="SELECT s FROM SiacDSistemaEsterno s")
public class SiacDSistemaEsterno extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_SISTEMA_ESTERNO_EXTSYSID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_SISTEMA_ESTERNO_EXTSYS_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_SISTEMA_ESTERNO_EXTSYSID_GENERATOR")
	@Column(name="extsys_id")
	private Integer extsysId;

	@Column(name="extsys_code")
	private String extsysCode;

	@Column(name="extsys_desc")
	private String extsysDesc;

	//bi-directional many-to-one association to SiacRSistemaEsternoEnte
	@OneToMany(mappedBy="siacDSistemaEsterno")
	private List<SiacRSistemaEsternoEnte> siacRSistemaEsternoEntes;

	public SiacDSistemaEsterno() {
	}

	public Integer getExtsysId() {
		return this.extsysId;
	}

	public void setExtsysId(Integer extsysId) {
		this.extsysId = extsysId;
	}

	public String getExtsysCode() {
		return this.extsysCode;
	}

	public void setExtsysCode(String extsysCode) {
		this.extsysCode = extsysCode;
	}

	public String getExtsysDesc() {
		return this.extsysDesc;
	}

	public void setExtsysDesc(String extsysDesc) {
		this.extsysDesc = extsysDesc;
	}

	public List<SiacRSistemaEsternoEnte> getSiacRSistemaEsternoEntes() {
		return this.siacRSistemaEsternoEntes;
	}

	public void setSiacRSistemaEsternoEntes(List<SiacRSistemaEsternoEnte> siacRSistemaEsternoEntes) {
		this.siacRSistemaEsternoEntes = siacRSistemaEsternoEntes;
	}

	public SiacRSistemaEsternoEnte addSiacRSistemaEsternoEnte(SiacRSistemaEsternoEnte siacRSistemaEsternoEnte) {
		getSiacRSistemaEsternoEntes().add(siacRSistemaEsternoEnte);
		siacRSistemaEsternoEnte.setSiacDSistemaEsterno(this);

		return siacRSistemaEsternoEnte;
	}

	public SiacRSistemaEsternoEnte removeSiacRSistemaEsternoEnte(SiacRSistemaEsternoEnte siacRSistemaEsternoEnte) {
		getSiacRSistemaEsternoEntes().remove(siacRSistemaEsternoEnte);
		siacRSistemaEsternoEnte.setSiacDSistemaEsterno(null);

		return siacRSistemaEsternoEnte;
	}

	@Override
	public Integer getUid() {
		return this.extsysId;
	}

	@Override
	public void setUid(Integer uid) {
		this.extsysId = uid;
	}

}