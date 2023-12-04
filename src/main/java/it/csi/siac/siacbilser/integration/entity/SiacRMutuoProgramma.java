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
@Table(name="siac_r_mutuo_programma")
public class SiacRMutuoProgramma extends SiacTEnteBaseExt {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_MUTUO_PROGRAMMA_MUTUOPROGRAMMAID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_MUTUO_PROGRAMMA_MUTUO_PROGRAMMA_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_MUTUO_PROGRAMMA_MUTUOPROGRAMMAID_GENERATOR")
	@Column(name="mutuo_programma_id")
	private Integer mutuoProgrammaId;

	@ManyToOne
	@JoinColumn(name="mutuo_id")
	private SiacTMutuo siacTMutuo;
	
	@ManyToOne
	@JoinColumn(name="programma_id")
	private SiacTProgramma siacTProgramma;
	
	@Column(name="mutuo_programma_importo_iniziale")
	private BigDecimal mutuoProgrammaImportoIniziale;
	
	@Column(name="mutuo_programma_importo_finale")
	private BigDecimal mutuoProgrammaImportoFinale;

	@Override
	public Integer getUid() {
		return getMutuoProgrammaId();
	}

	@Override
	public void setUid(Integer uid) {
		setMutuoProgrammaId(uid);
	}

	public Integer getMutuoProgrammaId() {
		return mutuoProgrammaId;
	}

	public void setMutuoProgrammaId(Integer mutuoProgrammaId) {
		this.mutuoProgrammaId = mutuoProgrammaId;
	}

	public SiacTMutuo getSiacTMutuo() {
		return siacTMutuo;
	}

	public void setSiacTMutuo(SiacTMutuo siacTMutuo) {
		this.siacTMutuo = siacTMutuo;
	}

	public SiacTProgramma getSiacTProgramma() {
		return siacTProgramma;
	}

	public void setSiacTProgramma(SiacTProgramma siacTProgramma) {
		this.siacTProgramma = siacTProgramma;
	}

	public BigDecimal getMutuoProgrammaImportoIniziale() {
		return mutuoProgrammaImportoIniziale;
	}

	public void setMutuoProgrammaImportoIniziale(BigDecimal mutuoProgrammaImportoIniziale) {
		this.mutuoProgrammaImportoIniziale = mutuoProgrammaImportoIniziale;
	}

	public BigDecimal getMutuoProgrammaImportoFinale() {
		return mutuoProgrammaImportoFinale;
	}

	public void setMutuoProgrammaImportoFinale(BigDecimal mutuoProgrammaImportoFinale) {
		this.mutuoProgrammaImportoFinale = mutuoProgrammaImportoFinale;
	}

}