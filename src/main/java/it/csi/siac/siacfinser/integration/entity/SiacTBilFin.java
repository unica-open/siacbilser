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
 * The persistent class for the siac_t_bil database table.
 * 
 */
@Entity
@Table(name="siac_t_bil")
public class SiacTBilFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_BIL_BIL_ID_GENERATOR", allocationSize=1, sequenceName="siac_t_bil_bil_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_BIL_BIL_ID_GENERATOR")
	@Column(name="bil_id")
	private Integer bilId;

	@Column(name="bil_code")
	private String bilCode;

	@Column(name="bil_desc")
	private String bilDesc;

	//bi-directional many-to-one association to SiacRBilFaseOperativaFin
	@OneToMany(mappedBy="siacTBil")
	private List<SiacRBilFaseOperativaFin> siacRBilFaseOperativas;

	//bi-directional many-to-one association to SiacRBilStatoOpFin
	@OneToMany(mappedBy="siacTBil")
	private List<SiacRBilStatoOpFin> siacRBilStatoOps;

	//bi-directional many-to-one association to SiacDBilTipoFin
	@ManyToOne
	@JoinColumn(name="bil_tipo_id")
	private SiacDBilTipoFin siacDBilTipo;

	//bi-directional many-to-one association to SiacTPeriodoFin
	@ManyToOne
	@JoinColumn(name="periodo_id")
	private SiacTPeriodoFin siacTPeriodo;

	//bi-directional many-to-one association to SiacTBilElemFin
	@OneToMany(mappedBy="siacTBil")
	private List<SiacTBilElemFin> siacTBilElems;

	//bi-directional many-to-one association to SiacTMovgestFin
	@OneToMany(mappedBy="siacTBil")
	private List<SiacTMovgestFin> siacTMovgests;

	//bi-directional many-to-one association to SiacTVariazioneFin
	@OneToMany(mappedBy="siacTBil")
	private List<SiacTVariazioneFin> siacTVariaziones;

	//bi-directional many-to-one association to SiacTVariazioneNumFin
	@OneToMany(mappedBy="siacTBil")
	private List<SiacTVariazioneNumFin> siacTVariazioneNums;

	public SiacTBilFin() {
	}

	public Integer getBilId() {
		return this.bilId;
	}

	public void setBilId(Integer bilId) {
		this.bilId = bilId;
	}

	public String getBilCode() {
		return this.bilCode;
	}

	public void setBilCode(String bilCode) {
		this.bilCode = bilCode;
	}

	public String getBilDesc() {
		return this.bilDesc;
	}

	public void setBilDesc(String bilDesc) {
		this.bilDesc = bilDesc;
	}

	public List<SiacRBilFaseOperativaFin> getSiacRBilFaseOperativas() {
		return this.siacRBilFaseOperativas;
	}

	public void setSiacRBilFaseOperativas(List<SiacRBilFaseOperativaFin> siacRBilFaseOperativas) {
		this.siacRBilFaseOperativas = siacRBilFaseOperativas;
	}

	public SiacRBilFaseOperativaFin addSiacRBilFaseOperativa(SiacRBilFaseOperativaFin siacRBilFaseOperativa) {
		getSiacRBilFaseOperativas().add(siacRBilFaseOperativa);
		siacRBilFaseOperativa.setSiacTBil(this);

		return siacRBilFaseOperativa;
	}

	public SiacRBilFaseOperativaFin removeSiacRBilFaseOperativa(SiacRBilFaseOperativaFin siacRBilFaseOperativa) {
		getSiacRBilFaseOperativas().remove(siacRBilFaseOperativa);
		siacRBilFaseOperativa.setSiacTBil(null);

		return siacRBilFaseOperativa;
	}

	public List<SiacRBilStatoOpFin> getSiacRBilStatoOps() {
		return this.siacRBilStatoOps;
	}

	public void setSiacRBilStatoOps(List<SiacRBilStatoOpFin> siacRBilStatoOps) {
		this.siacRBilStatoOps = siacRBilStatoOps;
	}

	public SiacRBilStatoOpFin addSiacRBilStatoOp(SiacRBilStatoOpFin siacRBilStatoOp) {
		getSiacRBilStatoOps().add(siacRBilStatoOp);
		siacRBilStatoOp.setSiacTBil(this);

		return siacRBilStatoOp;
	}

	public SiacRBilStatoOpFin removeSiacRBilStatoOp(SiacRBilStatoOpFin siacRBilStatoOp) {
		getSiacRBilStatoOps().remove(siacRBilStatoOp);
		siacRBilStatoOp.setSiacTBil(null);

		return siacRBilStatoOp;
	}

	public SiacDBilTipoFin getSiacDBilTipo() {
		return this.siacDBilTipo;
	}

	public void setSiacDBilTipo(SiacDBilTipoFin siacDBilTipo) {
		this.siacDBilTipo = siacDBilTipo;
	}

	public SiacTPeriodoFin getSiacTPeriodo() {
		return this.siacTPeriodo;
	}

	public void setSiacTPeriodo(SiacTPeriodoFin siacTPeriodo) {
		this.siacTPeriodo = siacTPeriodo;
	}

	public List<SiacTBilElemFin> getSiacTBilElems() {
		return this.siacTBilElems;
	}

	public void setSiacTBilElems(List<SiacTBilElemFin> siacTBilElems) {
		this.siacTBilElems = siacTBilElems;
	}

	public SiacTBilElemFin addSiacTBilElem(SiacTBilElemFin siacTBilElem) {
		getSiacTBilElems().add(siacTBilElem);
		siacTBilElem.setSiacTBil(this);

		return siacTBilElem;
	}

	public SiacTBilElemFin removeSiacTBilElem(SiacTBilElemFin siacTBilElem) {
		getSiacTBilElems().remove(siacTBilElem);
		siacTBilElem.setSiacTBil(null);

		return siacTBilElem;
	}

	public List<SiacTMovgestFin> getSiacTMovgests() {
		return this.siacTMovgests;
	}

	public void setSiacTMovgests(List<SiacTMovgestFin> siacTMovgests) {
		this.siacTMovgests = siacTMovgests;
	}

	public SiacTMovgestFin addSiacTMovgest(SiacTMovgestFin siacTMovgest) {
		getSiacTMovgests().add(siacTMovgest);
		siacTMovgest.setSiacTBil(this);

		return siacTMovgest;
	}

	public SiacTMovgestFin removeSiacTMovgest(SiacTMovgestFin siacTMovgest) {
		getSiacTMovgests().remove(siacTMovgest);
		siacTMovgest.setSiacTBil(null);

		return siacTMovgest;
	}

	public List<SiacTVariazioneFin> getSiacTVariaziones() {
		return this.siacTVariaziones;
	}

	public void setSiacTVariaziones(List<SiacTVariazioneFin> siacTVariaziones) {
		this.siacTVariaziones = siacTVariaziones;
	}

	public SiacTVariazioneFin addSiacTVariazione(SiacTVariazioneFin siacTVariazione) {
		getSiacTVariaziones().add(siacTVariazione);
		siacTVariazione.setSiacTBil(this);

		return siacTVariazione;
	}

	public SiacTVariazioneFin removeSiacTVariazione(SiacTVariazioneFin siacTVariazione) {
		getSiacTVariaziones().remove(siacTVariazione);
		siacTVariazione.setSiacTBil(null);

		return siacTVariazione;
	}

	public List<SiacTVariazioneNumFin> getSiacTVariazioneNums() {
		return this.siacTVariazioneNums;
	}

	public void setSiacTVariazioneNums(List<SiacTVariazioneNumFin> siacTVariazioneNums) {
		this.siacTVariazioneNums = siacTVariazioneNums;
	}

	public SiacTVariazioneNumFin addSiacTVariazioneNum(SiacTVariazioneNumFin siacTVariazioneNum) {
		getSiacTVariazioneNums().add(siacTVariazioneNum);
		siacTVariazioneNum.setSiacTBil(this);

		return siacTVariazioneNum;
	}

	public SiacTVariazioneNumFin removeSiacTVariazioneNum(SiacTVariazioneNumFin siacTVariazioneNum) {
		getSiacTVariazioneNums().remove(siacTVariazioneNum);
		siacTVariazioneNum.setSiacTBil(null);

		return siacTVariazioneNum;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.bilId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.bilId = uid;
	}

}