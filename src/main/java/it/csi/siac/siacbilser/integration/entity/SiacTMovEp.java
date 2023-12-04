/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.util.List;

import javax.persistence.CascadeType;
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
 * The persistent class for the siac_t_mov_ep database table.
 * 
 */
@Entity
@Table(name="siac_t_mov_ep")
@NamedQuery(name="SiacTMovEp.findAll", query="SELECT s FROM SiacTMovEp s")
public class SiacTMovEp extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_MOV_EP_MOVEPID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_MOV_EP_MOVEP_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_MOV_EP_MOVEPID_GENERATOR")
	@Column(name="movep_id")
	private Integer movepId;

	@Column(name="movep_code")
	private Integer movepCode;

	@Column(name="movep_desc")
	private String movepDesc;

	//bi-directional many-to-one association to SiacTCausaleEp
	@ManyToOne
	@JoinColumn(name="causale_ep_id")
	private SiacTCausaleEp siacTCausaleEp;

	//bi-directional many-to-one association to SiacTPrimaNota
	@ManyToOne
	@JoinColumn(name="regep_id")
	private SiacTPrimaNota siacTPrimaNota;

	//bi-directional many-to-one association to SiacTRegMovfin
	@ManyToOne
	@JoinColumn(name="regmovfin_id")
	private SiacTRegMovfin siacTRegMovfin;

	//bi-directional many-to-one association to SiacTMovEpDet
	@OneToMany(mappedBy="siacTMovEp", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacTMovEpDet> siacTMovEpDets;
	
	//bi-directional many-to-one association to SiacDAmbito
	@ManyToOne
	@JoinColumn(name="ambito_id")
	private SiacDAmbito siacDAmbito;

	public SiacTMovEp() {
	}

	public Integer getMovepId() {
		return this.movepId;
	}

	public void setMovepId(Integer movepId) {
		this.movepId = movepId;
	}

	public Integer getMovepCode() {
		return this.movepCode;
	}

	public void setMovepCode(Integer movepCode) {
		this.movepCode = movepCode;
	}

	public String getMovepDesc() {
		return this.movepDesc;
	}

	public void setMovepDesc(String movepDesc) {
		this.movepDesc = movepDesc;
	}

	public SiacTCausaleEp getSiacTCausaleEp() {
		return this.siacTCausaleEp;
	}

	public void setSiacTCausaleEp(SiacTCausaleEp siacTCausaleEp) {
		this.siacTCausaleEp = siacTCausaleEp;
	}

	public SiacTPrimaNota getSiacTPrimaNota() {
		return this.siacTPrimaNota;
	}

	public void setSiacTPrimaNota(SiacTPrimaNota siacTPrimaNota) {
		this.siacTPrimaNota = siacTPrimaNota;
	}

	public SiacTRegMovfin getSiacTRegMovfin() {
		return this.siacTRegMovfin;
	}

	public void setSiacTRegMovfin(SiacTRegMovfin siacTRegMovfin) {
		this.siacTRegMovfin = siacTRegMovfin;
	}

	public List<SiacTMovEpDet> getSiacTMovEpDets() {
		return this.siacTMovEpDets;
	}

	public void setSiacTMovEpDets(List<SiacTMovEpDet> siacTMovEpDets) {
		this.siacTMovEpDets = siacTMovEpDets;
	}

	public SiacTMovEpDet addSiacTMovEpDet(SiacTMovEpDet siacTMovEpDet) {
		getSiacTMovEpDets().add(siacTMovEpDet);
		siacTMovEpDet.setSiacTMovEp(this);

		return siacTMovEpDet;
	}

	public SiacTMovEpDet removeSiacTMovEpDet(SiacTMovEpDet siacTMovEpDet) {
		getSiacTMovEpDets().remove(siacTMovEpDet);
		siacTMovEpDet.setSiacTMovEp(null);

		return siacTMovEpDet;
	}
	
	public SiacDAmbito getSiacDAmbito() {
		return siacDAmbito;
	}

	public void setSiacDAmbito(SiacDAmbito siacDAmbito) {
		this.siacDAmbito = siacDAmbito;
	}


	@Override
	public Integer getUid() {
		return movepId;
	}

	@Override
	public void setUid(Integer uid) {
		this.movepId = uid;
	}

}