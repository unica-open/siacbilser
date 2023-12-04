/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_t_iva_gruppo database table.
 * 
 */
@Entity
@Table(name="siac_t_iva_gruppo")
public class SiacTIvaGruppoFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ivagru_id")
	private Integer ivagruId;

	@Column(name="ivagru_code")
	private String ivagruCode;

	@Column(name="ivagru_desc")
	private String ivagruDesc;

	@Column(name="ivagru_ivaprecedente")
	private BigDecimal ivagruIvaprecedente;

//	//bi-directional many-to-one association to SiacRIvaGruppoAttivita
//	@OneToMany(mappedBy="siacTIvaGruppo")
//	private List<SiacRIvaGruppoAttivita> siacRIvaGruppoAttivitas;
//
//	//bi-directional many-to-one association to SiacRIvaGruppoChiusura
//	@OneToMany(mappedBy="siacTIvaGruppo")
//	private List<SiacRIvaGruppoChiusura> siacRIvaGruppoChiusuras;
//
//	//bi-directional many-to-one association to SiacRIvaGruppoProrata
//	@OneToMany(mappedBy="siacTIvaGruppo")
//	private List<SiacRIvaGruppoProrata> siacRIvaGruppoProratas;

	//bi-directional many-to-one association to SiacRIvaRegistroGruppoFin
	@OneToMany(mappedBy="siacTIvaGruppo")
	private List<SiacRIvaRegistroGruppoFin> siacRIvaRegistroGruppos;

//	//bi-directional many-to-one association to SiacDIvaGruppoTipo
//	@ManyToOne
//	@JoinColumn(name="ivagru_tipo_id")
//	private SiacDIvaGruppoTipo siacDIvaGruppoTipo;

	public SiacTIvaGruppoFin() {
	}

	public Integer getIvagruId() {
		return this.ivagruId;
	}

	public void setIvagruId(Integer ivagruId) {
		this.ivagruId = ivagruId;
	}

	public String getIvagruCode() {
		return this.ivagruCode;
	}

	public void setIvagruCode(String ivagruCode) {
		this.ivagruCode = ivagruCode;
	}

	public String getIvagruDesc() {
		return this.ivagruDesc;
	}

	public void setIvagruDesc(String ivagruDesc) {
		this.ivagruDesc = ivagruDesc;
	}

	public BigDecimal getIvagruIvaprecedente() {
		return this.ivagruIvaprecedente;
	}

	public void setIvagruIvaprecedente(BigDecimal ivagruIvaprecedente) {
		this.ivagruIvaprecedente = ivagruIvaprecedente;
	}

//	public List<SiacRIvaGruppoAttivita> getSiacRIvaGruppoAttivitas() {
//		return this.siacRIvaGruppoAttivitas;
//	}
//
//	public void setSiacRIvaGruppoAttivitas(List<SiacRIvaGruppoAttivita> siacRIvaGruppoAttivitas) {
//		this.siacRIvaGruppoAttivitas = siacRIvaGruppoAttivitas;
//	}
//
//	public SiacRIvaGruppoAttivita addSiacRIvaGruppoAttivita(SiacRIvaGruppoAttivita siacRIvaGruppoAttivita) {
//		getSiacRIvaGruppoAttivitas().add(siacRIvaGruppoAttivita);
//		siacRIvaGruppoAttivita.setSiacTIvaGruppo(this);
//
//		return siacRIvaGruppoAttivita;
//	}

//	public SiacRIvaGruppoAttivita removeSiacRIvaGruppoAttivita(SiacRIvaGruppoAttivita siacRIvaGruppoAttivita) {
//		getSiacRIvaGruppoAttivitas().remove(siacRIvaGruppoAttivita);
//		siacRIvaGruppoAttivita.setSiacTIvaGruppo(null);
//
//		return siacRIvaGruppoAttivita;
//	}
//
//	public List<SiacRIvaGruppoChiusura> getSiacRIvaGruppoChiusuras() {
//		return this.siacRIvaGruppoChiusuras;
//	}
//
//	public void setSiacRIvaGruppoChiusuras(List<SiacRIvaGruppoChiusura> siacRIvaGruppoChiusuras) {
//		this.siacRIvaGruppoChiusuras = siacRIvaGruppoChiusuras;
//	}
//
//	public SiacRIvaGruppoChiusura addSiacRIvaGruppoChiusura(SiacRIvaGruppoChiusura siacRIvaGruppoChiusura) {
//		getSiacRIvaGruppoChiusuras().add(siacRIvaGruppoChiusura);
//		siacRIvaGruppoChiusura.setSiacTIvaGruppo(this);
//
//		return siacRIvaGruppoChiusura;
//	}

//	public SiacRIvaGruppoChiusura removeSiacRIvaGruppoChiusura(SiacRIvaGruppoChiusura siacRIvaGruppoChiusura) {
//		getSiacRIvaGruppoChiusuras().remove(siacRIvaGruppoChiusura);
//		siacRIvaGruppoChiusura.setSiacTIvaGruppo(null);
//
//		return siacRIvaGruppoChiusura;
//	}
//
//	public List<SiacRIvaGruppoProrata> getSiacRIvaGruppoProratas() {
//		return this.siacRIvaGruppoProratas;
//	}
//
//	public void setSiacRIvaGruppoProratas(List<SiacRIvaGruppoProrata> siacRIvaGruppoProratas) {
//		this.siacRIvaGruppoProratas = siacRIvaGruppoProratas;
//	}
//
//	public SiacRIvaGruppoProrata addSiacRIvaGruppoProrata(SiacRIvaGruppoProrata siacRIvaGruppoProrata) {
//		getSiacRIvaGruppoProratas().add(siacRIvaGruppoProrata);
//		siacRIvaGruppoProrata.setSiacTIvaGruppo(this);
//
//		return siacRIvaGruppoProrata;
//	}
//
//	public SiacRIvaGruppoProrata removeSiacRIvaGruppoProrata(SiacRIvaGruppoProrata siacRIvaGruppoProrata) {
//		getSiacRIvaGruppoProratas().remove(siacRIvaGruppoProrata);
//		siacRIvaGruppoProrata.setSiacTIvaGruppo(null);
//
//		return siacRIvaGruppoProrata;
//	}

	public List<SiacRIvaRegistroGruppoFin> getSiacRIvaRegistroGruppos() {
		return this.siacRIvaRegistroGruppos;
	}

	public void setSiacRIvaRegistroGruppos(List<SiacRIvaRegistroGruppoFin> siacRIvaRegistroGruppos) {
		this.siacRIvaRegistroGruppos = siacRIvaRegistroGruppos;
	}

	public SiacRIvaRegistroGruppoFin addSiacRIvaRegistroGruppo(SiacRIvaRegistroGruppoFin siacRIvaRegistroGruppo) {
		getSiacRIvaRegistroGruppos().add(siacRIvaRegistroGruppo);
		siacRIvaRegistroGruppo.setSiacTIvaGruppo(this);

		return siacRIvaRegistroGruppo;
	}

	public SiacRIvaRegistroGruppoFin removeSiacRIvaRegistroGruppo(SiacRIvaRegistroGruppoFin siacRIvaRegistroGruppo) {
		getSiacRIvaRegistroGruppos().remove(siacRIvaRegistroGruppo);
		siacRIvaRegistroGruppo.setSiacTIvaGruppo(null);

		return siacRIvaRegistroGruppo;
	}

//	public SiacDIvaGruppoTipo getSiacDIvaGruppoTipo() {
//		return this.siacDIvaGruppoTipo;
//	}
//
//	public void setSiacDIvaGruppoTipo(SiacDIvaGruppoTipo siacDIvaGruppoTipo) {
//		this.siacDIvaGruppoTipo = siacDIvaGruppoTipo;
//	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.ivagruId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.ivagruId = uid;
	}

}