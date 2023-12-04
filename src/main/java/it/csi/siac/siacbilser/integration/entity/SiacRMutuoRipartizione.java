/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="siac_r_mutuo_ripartizione")
public class SiacRMutuoRipartizione extends SiacTEnteBaseExt {
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="SIAC_R_MUTUO_RIPARTIZIONE_MUTUORIPARTIZIONEID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_MUTUO_RIPARTIZIONE_MUTUO_RIPARTIZIONE_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_MUTUO_RIPARTIZIONE_MUTUORIPARTIZIONEID_GENERATOR")
	@Column(name="mutuo_ripartizione_id")
	private Integer mutuoRipartizioneId;

	@ManyToOne
	@JoinColumn(name="mutuo_id")
	private SiacTMutuo siacTMutuo;
	
	@ManyToOne
	@JoinColumn(name="elem_id")
	private SiacTBilElem siacTBilElem;
	
	@ManyToOne
	@JoinColumn(name="mutuo_ripartizione_tipo_id")
	private SiacDMutuoRipartizioneTipo siacDMutuoRipartizioneTipo;
	
	@Column(name="mutuo_ripartizione_importo")
	private BigDecimal mutuoRipartizioneImporto;
	
	@Column(name="mutuo_ripartizione_perc")
	private BigDecimal mutuoRipartizionePerc;

	@Override
	public Integer getUid() {
		return getMutuoRipartizioneId();
	}

	@Override
	public void setUid(Integer uid) {
		setMutuoRipartizioneId(uid);
	}

	public Integer getMutuoRipartizioneId() {
		return mutuoRipartizioneId;
	}

	public void setMutuoRipartizioneId(Integer mutuoRipartizioneId) {
		this.mutuoRipartizioneId = mutuoRipartizioneId;
	}

	public SiacTMutuo getSiacTMutuo() {
		return siacTMutuo;
	}

	public void setSiacTMutuo(SiacTMutuo siacTMutuo) {
		this.siacTMutuo = siacTMutuo;
	}

	public SiacTBilElem getSiacTBilElem() {
		return siacTBilElem;
	}

	public void setSiacTBilElem(SiacTBilElem siacTBilElem) {
		this.siacTBilElem = siacTBilElem;
	}
	
	public SiacDMutuoRipartizioneTipo getSiacDMutuoRipartizioneTipo() {
		return siacDMutuoRipartizioneTipo;
	}

	public void setSiacDMutuoRipartizioneTipo(SiacDMutuoRipartizioneTipo siacDMutuoRipartizioneTipo) {
		this.siacDMutuoRipartizioneTipo = siacDMutuoRipartizioneTipo;
	}

	public BigDecimal getMutuoRipartizioneImporto() {
		return mutuoRipartizioneImporto;
	}

	public void setMutuoRipartizioneImporto(BigDecimal mutuoRipartizioneImporto) {
		this.mutuoRipartizioneImporto = mutuoRipartizioneImporto;
	}

	public BigDecimal getMutuoRipartizionePerc() {
		return mutuoRipartizionePerc;
	}

	public void setMutuoRipartizionePerc(BigDecimal mutuoRipartizionePerc) {
		this.mutuoRipartizionePerc = mutuoRipartizionePerc;
	}

}