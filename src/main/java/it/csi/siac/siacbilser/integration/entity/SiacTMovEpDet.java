/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.math.BigDecimal;
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
 * The persistent class for the siac_t_mov_ep_det database table.
 * 
 */
@Entity
@Table(name="siac_t_mov_ep_det")
@NamedQuery(name="SiacTMovEpDet.findAll", query="SELECT s FROM SiacTMovEpDet s")
public class SiacTMovEpDet extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_MOV_EP_DET_MOVEPDETID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_MOV_EP_DET_MOVEP_DET_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_MOV_EP_DET_MOVEPDETID_GENERATOR")
	@Column(name="movep_det_id")
	private Integer movepDetId;

	@Column(name="movep_det_code")
	private Integer movepDetCode;

	@Column(name="movep_det_desc")
	private String movepDetDesc;

	@Column(name="movep_det_importo")
	private BigDecimal movepDetImporto;

	@Column(name="movep_det_segno")
	private String movepDetSegno;

	//bi-directional many-to-one association to SiacTMovEp
	@ManyToOne
	@JoinColumn(name="movep_id")
	private SiacTMovEp siacTMovEp;

	//bi-directional many-to-one association to SiacTPdceConto
	@ManyToOne
	@JoinColumn(name="pdce_conto_id")
	private SiacTPdceConto siacTPdceConto;
	
	//bi-directional many-to-one association to SiacDAmbito
	@ManyToOne
	@JoinColumn(name="ambito_id")
	private SiacDAmbito siacDAmbito;
	
	//bi-directional many-to-one association to SiacRMovEpDetClass
	@OneToMany(mappedBy="siacTMovEpDet", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRMovEpDetClass> siacRMovEpDetClasses;
	
	@OneToMany(mappedBy="siacTMovEpDet", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRCespitiMovEpDet> siacRCespitiMovEpDets;

	public SiacTMovEpDet() {
	}

	public Integer getMovepDetId() {
		return this.movepDetId;
	}

	public void setMovepDetId(Integer movepDetId) {
		this.movepDetId = movepDetId;
	}

	public Integer getMovepDetCode() {
		return this.movepDetCode;
	}

	public void setMovepDetCode(Integer movepDetCode) {
		this.movepDetCode = movepDetCode;
	}

	public String getMovepDetDesc() {
		return this.movepDetDesc;
	}

	public void setMovepDetDesc(String movepDetDesc) {
		this.movepDetDesc = movepDetDesc;
	}

	public BigDecimal getMovepDetImporto() {
		return this.movepDetImporto;
	}

	public void setMovepDetImporto(BigDecimal movepDetImporto) {
		this.movepDetImporto = movepDetImporto;
	}

	public String getMovepDetSegno() {
		return this.movepDetSegno;
	}

	public void setMovepDetSegno(String movepDetSegno) {
		this.movepDetSegno = movepDetSegno;
	}

	public SiacTMovEp getSiacTMovEp() {
		return this.siacTMovEp;
	}

	public void setSiacTMovEp(SiacTMovEp siacTMovEp) {
		this.siacTMovEp = siacTMovEp;
	}

	public SiacTPdceConto getSiacTPdceConto() {
		return this.siacTPdceConto;
	}

	public void setSiacTPdceConto(SiacTPdceConto siacTPdceConto) {
		this.siacTPdceConto = siacTPdceConto;
	}
	
	public SiacDAmbito getSiacDAmbito() {
		return siacDAmbito;
	}

	public void setSiacDAmbito(SiacDAmbito siacDAmbito) {
		this.siacDAmbito = siacDAmbito;
	}
	
	public List<SiacRMovEpDetClass> getSiacRMovEpDetClasses() {
		return this.siacRMovEpDetClasses;
	}

	public void setSiacRMovEpDetClasses(List<SiacRMovEpDetClass> siacRMovEpDetClasses) {
		this.siacRMovEpDetClasses = siacRMovEpDetClasses;
	}

	public SiacRMovEpDetClass addSiacRMovEpDetClass(SiacRMovEpDetClass siacRMovEpDetClass) {
		getSiacRMovEpDetClasses().add(siacRMovEpDetClass);
		siacRMovEpDetClass.setSiacTMovEpDet(this);

		return siacRMovEpDetClass;
	}

	public SiacRMovEpDetClass removeSiacRMovEpDetClass(SiacRMovEpDetClass siacRMovEpDetClass) {
		getSiacRMovEpDetClasses().remove(siacRMovEpDetClass);
		siacRMovEpDetClass.setSiacTMovEpDet(null);

		return siacRMovEpDetClass;
	}
	
	public List<SiacRCespitiMovEpDet> getSiacRCespitiMovEpDets() {
		return this.siacRCespitiMovEpDets;
	}

	public void setSiacRCespitiMovEpDets(List<SiacRCespitiMovEpDet> siacRCespitiMovEpDets) {
		this.siacRCespitiMovEpDets = siacRCespitiMovEpDets;
	}

	public SiacRCespitiMovEpDet addSiacRCespitiMovEpDet(SiacRCespitiMovEpDet siacRCespitiMovEpDets) {
		getSiacRCespitiMovEpDets().add(siacRCespitiMovEpDets);
		siacRCespitiMovEpDets.setSiacTMovEpDet(this);
		return siacRCespitiMovEpDets;
	}

	public SiacRCespitiMovEpDet removeSiacRCespitiMovEpDet(SiacRCespitiMovEpDet siacRCespitiMovEpDet) {
		getSiacRCespitiMovEpDets().remove(siacRCespitiMovEpDet);
		siacRCespitiMovEpDet.setSiacTMovEpDet(null);

		return siacRCespitiMovEpDet;
	}

	@Override
	public Integer getUid() {
		return movepDetId;
	}

	@Override
	public void setUid(Integer uid) {
		this.movepDetId = uid;
	}

}