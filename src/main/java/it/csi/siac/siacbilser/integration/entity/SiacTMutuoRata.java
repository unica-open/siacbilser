/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siaccommon.util.date.DateUtil;

@Entity
@Table(name="siac_t_mutuo_rata")
public class SiacTMutuoRata extends SiacTEnteBaseExt {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_MUTUO_RATA_MUTUORATAID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_MUTUO_RATA_MUTUO_RATA_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_MUTUO_RATA_MUTUORATAID_GENERATOR")
	@Column(name="mutuo_rata_id")
	private Integer mutuoRataId;

	@ManyToOne
	@JoinColumn(name="mutuo_id")
	private SiacTMutuo siacTMutuo;
	
	@Column(name="mutuo_rata_anno")
	private Integer mutuoRataAnno;
	
	@Column(name="mutuo_rata_num_rata_piano")
	private Integer mutuoRataNumRataPiano;
	
	@Column(name="mutuo_rata_num_rata_anno")
	private Integer mutuoRataNumRataAnno;
	
	@Column(name="mutuo_rata_data_scadenza")
	private Date mutuoRataDataScadenza;
	
	@Column(name="mutuo_rata_importo")
	private BigDecimal mutuoRataImporto;
	
	@Column(name="mutuo_rata_importo_quota_interessi")
	private BigDecimal mutuoRataImportoQuotaInteressi;
	
	@Column(name="mutuo_rata_importo_quota_capitale")
	private BigDecimal mutuoRataImportoQuotaCapitale;
	
	@Column(name="mutuo_rata_importo_quota_oneri")
	private BigDecimal mutuoRataImportoQuotaOneri;
	
	@Column(name="mutuo_rata_debito_iniziale")
	private BigDecimal mutuoRataDebitoIniziale;
	
	@Column(name="mutuo_rata_debito_residuo")
	private BigDecimal mutuoRataDebitoResiduo;
	
	public SiacTMutuoRata() {
		super();
	}
	
	public SiacTMutuoRata(SiacTMutuoRata siacTMutuoRata) {
		super(siacTMutuoRata);
		this.mutuoRataId = siacTMutuoRata.mutuoRataId;
		this.siacTMutuo = siacTMutuoRata.siacTMutuo;
		this.mutuoRataAnno = siacTMutuoRata.mutuoRataAnno;
		this.mutuoRataNumRataPiano = siacTMutuoRata.mutuoRataNumRataPiano;
		this.mutuoRataNumRataAnno = siacTMutuoRata.mutuoRataNumRataAnno;
		this.mutuoRataDataScadenza = siacTMutuoRata.mutuoRataDataScadenza;
		this.mutuoRataImporto = siacTMutuoRata.mutuoRataImporto;
		this.mutuoRataImportoQuotaInteressi = siacTMutuoRata.mutuoRataImportoQuotaInteressi;
		this.mutuoRataImportoQuotaCapitale = siacTMutuoRata.mutuoRataImportoQuotaCapitale;
		this.mutuoRataImportoQuotaOneri = siacTMutuoRata.mutuoRataImportoQuotaOneri;
		this.mutuoRataDebitoIniziale = siacTMutuoRata.mutuoRataDebitoIniziale;
		this.mutuoRataDebitoResiduo = siacTMutuoRata.mutuoRataDebitoResiduo;
	}

	@Override
	public Integer getUid() {
		return getMutuoRataId();
	}

	@Override
	public void setUid(Integer uid) {
		setMutuoRataId(uid);
	}

	public Integer getMutuoRataId() {
		return mutuoRataId;
	}

	public void setMutuoRataId(Integer mutuoRataId) {
		this.mutuoRataId = mutuoRataId;
	}

	public SiacTMutuo getSiacTMutuo() {
		return siacTMutuo;
	}

	public void setSiacTMutuo(SiacTMutuo siacTMutuo) {
		this.siacTMutuo = siacTMutuo;
	}
	
	public Integer getMutuoRataAnno() {
		return mutuoRataAnno;
	}

	public void setMutuoRataAnno(Integer mutuoRataAnno) {
		this.mutuoRataAnno = mutuoRataAnno;
	}

	public Integer getMutuoRataNumRataPiano() {
		return mutuoRataNumRataPiano;
	}

	public void setMutuoRataNumRataPiano(Integer mutuoRataNumRataPiano) {
		this.mutuoRataNumRataPiano = mutuoRataNumRataPiano;
	}

	public Integer getMutuoRataNumRataAnno() {
		return mutuoRataNumRataAnno;
	}

	public void setMutuoRataNumRataAnno(Integer mutuoRataNUmRataAnno) {
		this.mutuoRataNumRataAnno = mutuoRataNUmRataAnno;
	}

	public Date getMutuoRataDataScadenza() {
		return mutuoRataDataScadenza;
	}

	public void setMutuoRataDataScadenza(Date mutuoRataDataScadenza) {
		this.mutuoRataDataScadenza = mutuoRataDataScadenza;
	}

	public BigDecimal getMutuoRataImporto() {
		return mutuoRataImporto;
	}

	public void setMutuoRataImporto(BigDecimal mutuoRataImporto) {
		this.mutuoRataImporto = mutuoRataImporto;
	}

	public BigDecimal getMutuoRataImportoQuotaInteressi() {
		return mutuoRataImportoQuotaInteressi;
	}

	public void setMutuoRataImportoQuotaInteressi(BigDecimal mutuoRataImportoQuotaInteressi) {
		this.mutuoRataImportoQuotaInteressi = mutuoRataImportoQuotaInteressi;
	}

	public BigDecimal getMutuoRataImportoQuotaCapitale() {
		return mutuoRataImportoQuotaCapitale;
	}

	public void setMutuoRataImportoQuotaCapitale(BigDecimal mutuoRataImportoQuotaCapitale) {
		this.mutuoRataImportoQuotaCapitale = mutuoRataImportoQuotaCapitale;
	}

	public BigDecimal getMutuoRataImportoQuotaOneri() {
		return mutuoRataImportoQuotaOneri;
	}

	public void setMutuoRataImportoQuotaOneri(BigDecimal mutuoRataImportoQuotaOneri) {
		this.mutuoRataImportoQuotaOneri = mutuoRataImportoQuotaOneri;
	}
	
	public BigDecimal getMutuoRataDebitoIniziale() {
		return mutuoRataDebitoIniziale;
	}

	public void setMutuoRataDebitoIniziale(BigDecimal mutuoRataDebitoIniziale) {
		this.mutuoRataDebitoIniziale = mutuoRataDebitoIniziale;
	}

	public BigDecimal getMutuoRataDebitoResiduo() {
		return mutuoRataDebitoResiduo;
	}

	public void setMutuoRataDebitoResiduo(BigDecimal mutuoRataDebitoResiduo) {
		this.mutuoRataDebitoResiduo = mutuoRataDebitoResiduo;
	}

	public Boolean isScaduta() {
		return DateUtil.dayBeforeToday(getMutuoRataDataScadenza());
	}


}