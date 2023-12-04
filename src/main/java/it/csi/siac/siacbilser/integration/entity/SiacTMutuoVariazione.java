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
@Table(name="siac_t_mutuo_variazione")
public class SiacTMutuoVariazione extends SiacTEnteBaseExt {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_MUTUO_VARIAZIONE_MUTUOVARIAZIONEID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_MUTUO_VARIAZIONE_MUTUO_VARIAZIONE_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_MUTUO_VARIAZIONE_MUTUOVARIAZIONEID_GENERATOR")
	@Column(name="mutuo_variazione_id")
	private Integer mutuoVariazioneId;

	@ManyToOne
	@JoinColumn(name="mutuo_id")
	private SiacTMutuo siacTMutuo;
	
	@ManyToOne
	@JoinColumn(name="mutuo_variazione_tipo_id")
	private SiacDMutuoVariazioneTipo siacDMutuoVariazioneTipo;
	
	@Column(name="mutuo_variazione_anno")
	private Integer mutuoVariazioneAnno;
	
	@Column(name="mutuo_variazione_num_rata")
	private Integer mutuoVariazioneNumRata;
	
	@Column(name="mutuo_variazione_anno_fine_piano_ammortamento")
	private Integer mutuoVariazioneAnnoFinePianoAmmortamento;
	
	@Column(name="mutuo_variazione_num_rata_finale")
	private Integer mutuoVariazioneNumRataFinale;
	
	@Column(name="mutuo_variazione_importo_rata")
	private BigDecimal mutuoVariazioneImportoRata;
	
	@Column(name="mutuo_variazione_tasso_euribor")
	private BigDecimal mutuoVariazioneTassoEuribor;
	
	@Override
	public Integer getUid() {
		return getMutuoVariazioneId();
	}

	@Override
	public void setUid(Integer uid) {
		setMutuoVariazioneId(uid);
	}

	public Integer getMutuoVariazioneId() {
		return mutuoVariazioneId;
	}

	public void setMutuoVariazioneId(Integer mutuoVariazioneId) {
		this.mutuoVariazioneId = mutuoVariazioneId;
	}

	public SiacTMutuo getSiacTMutuo() {
		return siacTMutuo;
	}

	public void setSiacTMutuo(SiacTMutuo siacTMutuo) {
		this.siacTMutuo = siacTMutuo;
	}
	
	public SiacDMutuoVariazioneTipo getSiacDMutuoVariazioneTipo() {
		return siacDMutuoVariazioneTipo;
	}
	
	public void setSiacDMutuoVariazioneTipo(SiacDMutuoVariazioneTipo siacDMutuoVariazioneTipo) {
		this.siacDMutuoVariazioneTipo = siacDMutuoVariazioneTipo;
	}

	public Integer getMutuoVariazioneAnno() {
		return mutuoVariazioneAnno;
	}

	public void setMutuoVariazioneAnno(Integer mutuoVariazioneAnno) {
		this.mutuoVariazioneAnno = mutuoVariazioneAnno;
	}

	public Integer getMutuoVariazioneNumRata() {
		return mutuoVariazioneNumRata;
	}

	public void setMutuoVariazioneNumRata(Integer mutuoVariazioneNumRata) {
		this.mutuoVariazioneNumRata = mutuoVariazioneNumRata;
	}

	public Integer getMutuoVariazioneAnnoFinePianoAmmortamento() {
		return mutuoVariazioneAnnoFinePianoAmmortamento;
	}

	public void setMutuoVariazioneAnnoFinePianoAmmortamento(Integer mutuoVariazioneAnnoFinePianoAmmortamento) {
		this.mutuoVariazioneAnnoFinePianoAmmortamento = mutuoVariazioneAnnoFinePianoAmmortamento;
	}

	public Integer getMutuoVariazioneNumRataFinale() {
		return mutuoVariazioneNumRataFinale;
	}

	public void setMutuoVariazioneNumRataFinale(Integer mutuoVariazioneNumRataFinale) {
		this.mutuoVariazioneNumRataFinale = mutuoVariazioneNumRataFinale;
	}

	public BigDecimal getMutuoVariazioneImportoRata() {
		return mutuoVariazioneImportoRata;
	}

	public void setMutuoVariazioneImportoRata(BigDecimal mutuoVariazioneImportoRata) {
		this.mutuoVariazioneImportoRata = mutuoVariazioneImportoRata;
	}

	public BigDecimal getMutuoVariazioneTassoEuribor() {
		return mutuoVariazioneTassoEuribor;
	}

	public void setMutuoVariazioneTassoEuribor(BigDecimal mutuoVariazioneTassoEuribor) {
		this.mutuoVariazioneTassoEuribor = mutuoVariazioneTassoEuribor;
	}


	
}