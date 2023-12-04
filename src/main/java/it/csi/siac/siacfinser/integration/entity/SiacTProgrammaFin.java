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
 * The persistent class for the siac_t_programma database table.
 * 
 */
@Entity
@Table(name="siac_t_programma")
public class SiacTProgrammaFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_PROGRAMMA_ID_GENERATOR", allocationSize=1, sequenceName="siac_t_programma_programma_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_PROGRAMMA_ID_GENERATOR")
	@Column(name="programma_id")
	private Integer programmaId;

	@Column(name="programma_code")
	private String programmaCode;

	@Column(name="programma_desc")
	private String programmaDesc;

	//bi-directional many-to-one association to SiacRMovgestTsProgrammaFin
	@OneToMany(mappedBy="siacTProgramma")
	private List<SiacRMovgestTsProgrammaFin> siacRMovgestTsProgrammas;

	//bi-directional many-to-one association to SiacRProgrammaStatoFin
	@OneToMany(mappedBy="siacTProgramma")
	private List<SiacRProgrammaStatoFin> siacRProgrammaStatos;
	
	//bi-directional many-to-one association to SiacRProgrammaAttrFin
	@OneToMany(mappedBy="siacTProgramma")
	private List<SiacRProgrammaAttrFin> siacRProgrammaAttrs;

	@ManyToOne
	@JoinColumn(name="bil_id")
	private SiacTBilFin siacTBil;
	
	@ManyToOne
	@JoinColumn(name="programma_tipo_id")
	private SiacDProgrammaTipoFin siacDProgrammaTipo;

	public SiacTProgrammaFin() {
	}

	public Integer getProgrammaId() {
		return this.programmaId;
	}

	public void setProgrammaId(Integer programmaId) {
		this.programmaId = programmaId;
	}

	public String getProgrammaCode() {
		return this.programmaCode;
	}

	public void setProgrammaCode(String programmaCode) {
		this.programmaCode = programmaCode;
	}

	public String getProgrammaDesc() {
		return this.programmaDesc;
	}

	public void setProgrammaDesc(String programmaDesc) {
		this.programmaDesc = programmaDesc;
	}

	public List<SiacRMovgestTsProgrammaFin> getSiacRMovgestTsProgrammas() {
		return this.siacRMovgestTsProgrammas;
	}

	public void setSiacRMovgestTsProgrammas(List<SiacRMovgestTsProgrammaFin> siacRMovgestTsProgrammas) {
		this.siacRMovgestTsProgrammas = siacRMovgestTsProgrammas;
	}

	public SiacRMovgestTsProgrammaFin addSiacRMovgestTsProgramma(SiacRMovgestTsProgrammaFin siacRMovgestTsProgramma) {
		getSiacRMovgestTsProgrammas().add(siacRMovgestTsProgramma);
		siacRMovgestTsProgramma.setSiacTProgramma(this);

		return siacRMovgestTsProgramma;
	}

	public SiacRMovgestTsProgrammaFin removeSiacRMovgestTsProgramma(SiacRMovgestTsProgrammaFin siacRMovgestTsProgramma) {
		getSiacRMovgestTsProgrammas().remove(siacRMovgestTsProgramma);
		siacRMovgestTsProgramma.setSiacTProgramma(null);

		return siacRMovgestTsProgramma;
	}

	public List<SiacRProgrammaStatoFin> getSiacRProgrammaStatos() {
		return this.siacRProgrammaStatos;
	}

	public void setSiacRProgrammaStatos(List<SiacRProgrammaStatoFin> siacRProgrammaStatos) {
		this.siacRProgrammaStatos = siacRProgrammaStatos;
	}

	public SiacRProgrammaStatoFin addSiacRProgrammaStato(SiacRProgrammaStatoFin siacRProgrammaStato) {
		getSiacRProgrammaStatos().add(siacRProgrammaStato);
		siacRProgrammaStato.setSiacTProgramma(this);

		return siacRProgrammaStato;
	}

	public SiacRProgrammaStatoFin removeSiacRProgrammaStato(SiacRProgrammaStatoFin siacRProgrammaStato) {
		getSiacRProgrammaStatos().remove(siacRProgrammaStato);
		siacRProgrammaStato.setSiacTProgramma(null);

		return siacRProgrammaStato;
	}
	
	public List<SiacRProgrammaAttrFin> getSiacRProgrammaAttrs() {
		return this.siacRProgrammaAttrs;
	}

	public void setSiacRProgrammaAttrs(List<SiacRProgrammaAttrFin> siacRProgrammaAttrs) {
		this.siacRProgrammaAttrs = siacRProgrammaAttrs;
	}

	public SiacRProgrammaAttrFin addSiacRProgrammaAttr(SiacRProgrammaAttrFin siacRProgrammaAttr) {
		getSiacRProgrammaAttrs().add(siacRProgrammaAttr);
		siacRProgrammaAttr.setSiacTProgramma(this);

		return siacRProgrammaAttr;
	}

	public SiacRProgrammaAttrFin removeSiacRProgrammaAttr(SiacRProgrammaAttrFin siacRProgrammaAttr) {
		getSiacRProgrammaAttrs().remove(siacRProgrammaAttr);
		siacRProgrammaAttr.setSiacTProgramma(null);

		return siacRProgrammaAttr;
	}

	@Override
	public Integer getUid() {
		//  Auto-generated method stub
		return this.programmaId;
	}

	@Override
	public void setUid(Integer uid) {
		//  Auto-generated method stub
		this.programmaId = uid;
	}

	public SiacTBilFin getSiacTBil() {
		return siacTBil;
	}

	public void setSiacTBil(SiacTBilFin siacTBil) {
		this.siacTBil = siacTBil;
	}

	public SiacDProgrammaTipoFin getSiacDProgrammaTipo() {
		return siacDProgrammaTipo;
	}

	public void setSiacDProgrammaTipo(SiacDProgrammaTipoFin siacDProgrammaTipo) {
		this.siacDProgrammaTipo = siacDProgrammaTipo;
	}

}