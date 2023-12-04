/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_t_bil_elem database table.
 * 
 */
@Entity
@Table(name="siac_t_bil_elem")
public class SiacTBilElemFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_BIL_ELEM_ELEM_ID_GENERATOR", allocationSize=1, sequenceName="siac_t_bil_elem_elem_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_BIL_ELEM_ELEM_ID_GENERATOR")
	@Column(name="elem_id")
	private Integer elemId;

	@Column(name="elem_code")
	private String elemCode;

	@Column(name="elem_code2")
	private String elemCode2;

	@Column(name="elem_code3")
	private String elemCode3;

	@Column(name="elem_desc")
	private String elemDesc;

	@Column(name="elem_desc2")
	private String elemDesc2;

	private Integer livello;

	private String ordine;

	//bi-directional many-to-one association to SiacRBilElemAttoLeggeFin
	@OneToMany(mappedBy="siacTBilElem")
	private List<SiacRBilElemAttoLeggeFin> siacRBilElemAttoLegges;

	//bi-directional many-to-one association to SiacRBilElemAttrFin
	@OneToMany(mappedBy="siacTBilElem")
	private List<SiacRBilElemAttrFin> siacRBilElemAttrs;

	//bi-directional many-to-one association to SiacRBilElemClassFin
	@OneToMany(mappedBy="siacTBilElem")
	private List<SiacRBilElemClassFin> siacRBilElemClasses;

	//bi-directional many-to-one association to SiacRBilElemClassVarFin
	@OneToMany(mappedBy="siacTBilElem")
	private List<SiacRBilElemClassVarFin> siacRBilElemClassVars;

	//bi-directional many-to-one association to SiacRBilElemRelTempoFin
	@OneToMany(mappedBy="siacTBilElem1")
	private List<SiacRBilElemRelTempoFin> siacRBilElemRelTempos1;

	// bi-directional many-to-one association to SiacRBilElemRelTempoFin
	// @OneToMany(mappedBy="siacTBilElem2")
	// private List<SiacRBilElemRelTempoFin> siacRBilElemRelTempos2;

	//bi-directional many-to-one association to SiacRBilElemRelTempoFin
	@OneToMany(mappedBy="siacTBilElem3")
	private List<SiacRBilElemRelTempoFin> siacRBilElemRelTempos3;

	//bi-directional many-to-one association to SiacRBilElemStatoFin
	@OneToMany(mappedBy="siacTBilElem")
	private List<SiacRBilElemStatoFin> siacRBilElemStatos;

	//bi-directional many-to-one association to SiacRMovgestBilElemFin
	@OneToMany(mappedBy="siacTBilElem")
	private List<SiacRMovgestBilElemFin> siacRMovgestBilElems;

	//bi-directional many-to-one association to SiacDBilElemTipoFin
	@ManyToOne
	@JoinColumn(name="elem_tipo_id")
	private SiacDBilElemTipoFin siacDBilElemTipo;

	//bi-directional many-to-one association to SiacTBilFin
	@ManyToOne
	@JoinColumn(name="bil_id")
	private SiacTBilFin siacTBil;

	//bi-directional many-to-one association to SiacTBilElemFin
	@ManyToOne
	@JoinColumn(name="elem_id_padre")
	private SiacTBilElemFin siacTBilElem;

	//bi-directional many-to-one association to SiacTBilElemFin
	@OneToMany(mappedBy="siacTBilElem")
	private List<SiacTBilElemFin> siacTBilElems;

	//bi-directional many-to-one association to SiacTBilElemDetFin
	@OneToMany(mappedBy="siacTBilElem")
	private List<SiacTBilElemDetFin> siacTBilElemDets;

	//bi-directional many-to-one association to SiacTBilElemDetVarFin
	@OneToMany(mappedBy="siacTBilElem")
	private List<SiacTBilElemDetVarFin> siacTBilElemDetVars;

	//bi-directional many-to-one association to SiacTBilElemVarFin
	@OneToMany(mappedBy="siacTBilElem")
	private List<SiacTBilElemVarFin> siacTBilElemVars;

	public SiacTBilElemFin() {
	}

	public Integer getElemId() {
		return this.elemId;
	}

	public void setElemId(Integer elemId) {
		this.elemId = elemId;
	}

	public String getElemCode() {
		return this.elemCode;
	}

	public void setElemCode(String elemCode) {
		this.elemCode = elemCode;
	}

	public String getElemCode2() {
		return this.elemCode2;
	}

	public void setElemCode2(String elemCode2) {
		this.elemCode2 = elemCode2;
	}

	public String getElemCode3() {
		return this.elemCode3;
	}

	public void setElemCode3(String elemCode3) {
		this.elemCode3 = elemCode3;
	}

	public String getElemDesc() {
		return this.elemDesc;
	}

	public void setElemDesc(String elemDesc) {
		this.elemDesc = elemDesc;
	}

	public String getElemDesc2() {
		return this.elemDesc2;
	}

	public void setElemDesc2(String elemDesc2) {
		this.elemDesc2 = elemDesc2;
	}

	public Integer getLivello() {
		return this.livello;
	}

	public void setLivello(Integer livello) {
		this.livello = livello;
	}

	public String getOrdine() {
		return this.ordine;
	}

	public void setOrdine(String ordine) {
		this.ordine = ordine;
	}

	public List<SiacRBilElemAttoLeggeFin> getSiacRBilElemAttoLegges() {
		return this.siacRBilElemAttoLegges;
	}

	public void setSiacRBilElemAttoLegges(List<SiacRBilElemAttoLeggeFin> siacRBilElemAttoLegges) {
		this.siacRBilElemAttoLegges = siacRBilElemAttoLegges;
	}

	public SiacRBilElemAttoLeggeFin addSiacRBilElemAttoLegge(SiacRBilElemAttoLeggeFin siacRBilElemAttoLegge) {
		getSiacRBilElemAttoLegges().add(siacRBilElemAttoLegge);
		siacRBilElemAttoLegge.setSiacTBilElem(this);

		return siacRBilElemAttoLegge;
	}

	public SiacRBilElemAttoLeggeFin removeSiacRBilElemAttoLegge(SiacRBilElemAttoLeggeFin siacRBilElemAttoLegge) {
		getSiacRBilElemAttoLegges().remove(siacRBilElemAttoLegge);
		siacRBilElemAttoLegge.setSiacTBilElem(null);

		return siacRBilElemAttoLegge;
	}

	public List<SiacRBilElemAttrFin> getSiacRBilElemAttrs() {
		return this.siacRBilElemAttrs;
	}

	public void setSiacRBilElemAttrs(List<SiacRBilElemAttrFin> siacRBilElemAttrs) {
		this.siacRBilElemAttrs = siacRBilElemAttrs;
	}

	public SiacRBilElemAttrFin addSiacRBilElemAttr(SiacRBilElemAttrFin siacRBilElemAttr) {
		getSiacRBilElemAttrs().add(siacRBilElemAttr);
		siacRBilElemAttr.setSiacTBilElem(this);

		return siacRBilElemAttr;
	}

	public SiacRBilElemAttrFin removeSiacRBilElemAttr(SiacRBilElemAttrFin siacRBilElemAttr) {
		getSiacRBilElemAttrs().remove(siacRBilElemAttr);
		siacRBilElemAttr.setSiacTBilElem(null);

		return siacRBilElemAttr;
	}

	public List<SiacRBilElemClassFin> getSiacRBilElemClasses() {
		return this.siacRBilElemClasses;
	}

	public void setSiacRBilElemClasses(List<SiacRBilElemClassFin> siacRBilElemClasses) {
		this.siacRBilElemClasses = siacRBilElemClasses;
	}

	public SiacRBilElemClassFin addSiacRBilElemClass(SiacRBilElemClassFin siacRBilElemClass) {
		getSiacRBilElemClasses().add(siacRBilElemClass);
		siacRBilElemClass.setSiacTBilElem(this);

		return siacRBilElemClass;
	}

	public SiacRBilElemClassFin removeSiacRBilElemClass(SiacRBilElemClassFin siacRBilElemClass) {
		getSiacRBilElemClasses().remove(siacRBilElemClass);
		siacRBilElemClass.setSiacTBilElem(null);

		return siacRBilElemClass;
	}

	public List<SiacRBilElemClassVarFin> getSiacRBilElemClassVars() {
		return this.siacRBilElemClassVars;
	}

	public void setSiacRBilElemClassVars(List<SiacRBilElemClassVarFin> siacRBilElemClassVars) {
		this.siacRBilElemClassVars = siacRBilElemClassVars;
	}

	public SiacRBilElemClassVarFin addSiacRBilElemClassVar(SiacRBilElemClassVarFin siacRBilElemClassVar) {
		getSiacRBilElemClassVars().add(siacRBilElemClassVar);
		siacRBilElemClassVar.setSiacTBilElem(this);

		return siacRBilElemClassVar;
	}

	public SiacRBilElemClassVarFin removeSiacRBilElemClassVar(SiacRBilElemClassVarFin siacRBilElemClassVar) {
		getSiacRBilElemClassVars().remove(siacRBilElemClassVar);
		siacRBilElemClassVar.setSiacTBilElem(null);

		return siacRBilElemClassVar;
	}

	public List<SiacRBilElemRelTempoFin> getSiacRBilElemRelTempos1() {
		return this.siacRBilElemRelTempos1;
	}

	public void setSiacRBilElemRelTempos1(List<SiacRBilElemRelTempoFin> siacRBilElemRelTempos1) {
		this.siacRBilElemRelTempos1 = siacRBilElemRelTempos1;
	}

	public SiacRBilElemRelTempoFin addSiacRBilElemRelTempos1(SiacRBilElemRelTempoFin siacRBilElemRelTempos1) {
		getSiacRBilElemRelTempos1().add(siacRBilElemRelTempos1);
		siacRBilElemRelTempos1.setSiacTBilElem1(this);

		return siacRBilElemRelTempos1;
	}

	public SiacRBilElemRelTempoFin removeSiacRBilElemRelTempos1(SiacRBilElemRelTempoFin siacRBilElemRelTempos1) {
		getSiacRBilElemRelTempos1().remove(siacRBilElemRelTempos1);
		siacRBilElemRelTempos1.setSiacTBilElem1(null);

		return siacRBilElemRelTempos1;
	}

//	public List<SiacRBilElemRelTempoFin> getSiacRBilElemRelTempos2() {
//		return this.siacRBilElemRelTempos2;
//	}
//
//	public void setSiacRBilElemRelTempos2(List<SiacRBilElemRelTempoFin> siacRBilElemRelTempos2) {
//		this.siacRBilElemRelTempos2 = siacRBilElemRelTempos2;
//	}
//
//	public SiacRBilElemRelTempoFin addSiacRBilElemRelTempos2(SiacRBilElemRelTempoFin siacRBilElemRelTempos2) {
//		getSiacRBilElemRelTempos2().add(siacRBilElemRelTempos2);
//		siacRBilElemRelTempos2.setSiacTBilElem2(this);
//
//		return siacRBilElemRelTempos2;
//	}
//
//	public SiacRBilElemRelTempoFin removeSiacRBilElemRelTempos2(SiacRBilElemRelTempoFin siacRBilElemRelTempos2) {
//		getSiacRBilElemRelTempos2().remove(siacRBilElemRelTempos2);
//		siacRBilElemRelTempos2.setSiacTBilElem2(null);
//
//		return siacRBilElemRelTempos2;
//	}

	public List<SiacRBilElemRelTempoFin> getSiacRBilElemRelTempos3() {
		return this.siacRBilElemRelTempos3;
	}

	public void setSiacRBilElemRelTempos3(List<SiacRBilElemRelTempoFin> siacRBilElemRelTempos3) {
		this.siacRBilElemRelTempos3 = siacRBilElemRelTempos3;
	}

	public SiacRBilElemRelTempoFin addSiacRBilElemRelTempos3(SiacRBilElemRelTempoFin siacRBilElemRelTempos3) {
		getSiacRBilElemRelTempos3().add(siacRBilElemRelTempos3);
		siacRBilElemRelTempos3.setSiacTBilElem3(this);

		return siacRBilElemRelTempos3;
	}

	public SiacRBilElemRelTempoFin removeSiacRBilElemRelTempos3(SiacRBilElemRelTempoFin siacRBilElemRelTempos3) {
		getSiacRBilElemRelTempos3().remove(siacRBilElemRelTempos3);
		siacRBilElemRelTempos3.setSiacTBilElem3(null);

		return siacRBilElemRelTempos3;
	}

	public List<SiacRBilElemStatoFin> getSiacRBilElemStatos() {
		return this.siacRBilElemStatos;
	}

	public void setSiacRBilElemStatos(List<SiacRBilElemStatoFin> siacRBilElemStatos) {
		this.siacRBilElemStatos = siacRBilElemStatos;
	}

	public SiacRBilElemStatoFin addSiacRBilElemStato(SiacRBilElemStatoFin siacRBilElemStato) {
		getSiacRBilElemStatos().add(siacRBilElemStato);
		siacRBilElemStato.setSiacTBilElem(this);

		return siacRBilElemStato;
	}

	public SiacRBilElemStatoFin removeSiacRBilElemStato(SiacRBilElemStatoFin siacRBilElemStato) {
		getSiacRBilElemStatos().remove(siacRBilElemStato);
		siacRBilElemStato.setSiacTBilElem(null);

		return siacRBilElemStato;
	}

	public List<SiacRMovgestBilElemFin> getSiacRMovgestBilElems() {
		return this.siacRMovgestBilElems;
	}

	public void setSiacRMovgestBilElems(List<SiacRMovgestBilElemFin> siacRMovgestBilElems) {
		this.siacRMovgestBilElems = siacRMovgestBilElems;
	}

	public SiacRMovgestBilElemFin addSiacRMovgestBilElem(SiacRMovgestBilElemFin siacRMovgestBilElem) {
		getSiacRMovgestBilElems().add(siacRMovgestBilElem);
		siacRMovgestBilElem.setSiacTBilElem(this);

		return siacRMovgestBilElem;
	}

	public SiacRMovgestBilElemFin removeSiacRMovgestBilElem(SiacRMovgestBilElemFin siacRMovgestBilElem) {
		getSiacRMovgestBilElems().remove(siacRMovgestBilElem);
		siacRMovgestBilElem.setSiacTBilElem(null);

		return siacRMovgestBilElem;
	}

	public SiacDBilElemTipoFin getSiacDBilElemTipo() {
		return this.siacDBilElemTipo;
	}

	public void setSiacDBilElemTipo(SiacDBilElemTipoFin siacDBilElemTipo) {
		this.siacDBilElemTipo = siacDBilElemTipo;
	}

	public SiacTBilFin getSiacTBil() {
		return this.siacTBil;
	}

	public void setSiacTBil(SiacTBilFin siacTBil) {
		this.siacTBil = siacTBil;
	}

	public SiacTBilElemFin getSiacTBilElem() {
		return this.siacTBilElem;
	}

	public void setSiacTBilElem(SiacTBilElemFin siacTBilElem) {
		this.siacTBilElem = siacTBilElem;
	}

	public List<SiacTBilElemFin> getSiacTBilElems() {
		return this.siacTBilElems;
	}

	public void setSiacTBilElems(List<SiacTBilElemFin> siacTBilElems) {
		this.siacTBilElems = siacTBilElems;
	}

	public SiacTBilElemFin addSiacTBilElem(SiacTBilElemFin siacTBilElem) {
		getSiacTBilElems().add(siacTBilElem);
		siacTBilElem.setSiacTBilElem(this);

		return siacTBilElem;
	}

	public SiacTBilElemFin removeSiacTBilElem(SiacTBilElemFin siacTBilElem) {
		getSiacTBilElems().remove(siacTBilElem);
		siacTBilElem.setSiacTBilElem(null);

		return siacTBilElem;
	}

	public List<SiacTBilElemDetFin> getSiacTBilElemDets() {
		return this.siacTBilElemDets;
	}

	public void setSiacTBilElemDets(List<SiacTBilElemDetFin> siacTBilElemDets) {
		this.siacTBilElemDets = siacTBilElemDets;
	}

	public SiacTBilElemDetFin addSiacTBilElemDet(SiacTBilElemDetFin siacTBilElemDet) {
		getSiacTBilElemDets().add(siacTBilElemDet);
		siacTBilElemDet.setSiacTBilElem(this);

		return siacTBilElemDet;
	}

	public SiacTBilElemDetFin removeSiacTBilElemDet(SiacTBilElemDetFin siacTBilElemDet) {
		getSiacTBilElemDets().remove(siacTBilElemDet);
		siacTBilElemDet.setSiacTBilElem(null);

		return siacTBilElemDet;
	}

	public List<SiacTBilElemDetVarFin> getSiacTBilElemDetVars() {
		return this.siacTBilElemDetVars;
	}

	public void setSiacTBilElemDetVars(List<SiacTBilElemDetVarFin> siacTBilElemDetVars) {
		this.siacTBilElemDetVars = siacTBilElemDetVars;
	}

	public SiacTBilElemDetVarFin addSiacTBilElemDetVar(SiacTBilElemDetVarFin siacTBilElemDetVar) {
		getSiacTBilElemDetVars().add(siacTBilElemDetVar);
		siacTBilElemDetVar.setSiacTBilElem(this);

		return siacTBilElemDetVar;
	}

	public SiacTBilElemDetVarFin removeSiacTBilElemDetVar(SiacTBilElemDetVarFin siacTBilElemDetVar) {
		getSiacTBilElemDetVars().remove(siacTBilElemDetVar);
		siacTBilElemDetVar.setSiacTBilElem(null);

		return siacTBilElemDetVar;
	}

	public List<SiacTBilElemVarFin> getSiacTBilElemVars() {
		return this.siacTBilElemVars;
	}

	public void setSiacTBilElemVars(List<SiacTBilElemVarFin> siacTBilElemVars) {
		this.siacTBilElemVars = siacTBilElemVars;
	}

	public SiacTBilElemVarFin addSiacTBilElemVar(SiacTBilElemVarFin siacTBilElemVar) {
		getSiacTBilElemVars().add(siacTBilElemVar);
		siacTBilElemVar.setSiacTBilElem(this);

		return siacTBilElemVar;
	}

	public SiacTBilElemVarFin removeSiacTBilElemVar(SiacTBilElemVarFin siacTBilElemVar) {
		getSiacTBilElemVars().remove(siacTBilElemVar);
		siacTBilElemVar.setSiacTBilElem(null);

		return siacTBilElemVar;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.elemId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.elemId = uid;
	}
}