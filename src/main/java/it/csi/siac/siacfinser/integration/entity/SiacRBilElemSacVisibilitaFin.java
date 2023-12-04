/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_r_bil_elem_sac_visibilita database table.
 * 
 */
@Entity
@Table(name="siac_r_bil_elem_sac_visibilita")
public class SiacRBilElemSacVisibilitaFin extends SiacTEnteBase {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_BIL_ELEM_SAC_VISIBILITA_ELEM_CLASSIF_ID_SAC_GENERATOR", allocationSize=1, sequenceName="siac_r_vincolo_bil_elem_vincolo_elem_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_BIL_ELEM_SAC_VISIBILITA_ELEM_CLASSIF_ID_SAC_GENERATOR")
	@Column(name="elem_classif_id_sac")
	private Integer elemClassifIdSac;

	@ManyToOne
	@JoinColumn(name="elem_id")
	private SiacTBilElemFin siacTBilElem;

	@ManyToOne
	@JoinColumn(name="classif_id")
	private SiacTClassFin siacTClass;
	
	
	public Integer getElemClassifIdSac() {
		return elemClassifIdSac;
	}

	public void setElemClassifIdSac(Integer elemClassifIdSac) {
		this.elemClassifIdSac = elemClassifIdSac;
	}

	public SiacTBilElemFin getSiacTBilElem() {
		return siacTBilElem;
	}

	public void setSiacTBilElem(SiacTBilElemFin siacTBilElem) {
		this.siacTBilElem = siacTBilElem;
	}

	public SiacTClassFin getSiacTClass() {
		return siacTClass;
	}

	public void setSiacTClass(SiacTClassFin siacTClass) {
		this.siacTClass = siacTClass;
	}

	@Override
	public Integer getUid() {
		return this.elemClassifIdSac;
	}

	@Override
	public void setUid(Integer uid) {
		this.elemClassifIdSac = uid;
	}
}