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

@Entity
@Table(name="siac_s_mutuo_storico")
public class SiacSMutuoStorico extends SiacTEnteBaseExt {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_S_MUTUO_STORICO_MUTUO_STORICO_ID_GENERATOR", allocationSize=1, sequenceName="SIAC_S_MUTUO_STORICO_MUTUO_STORICO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_S_MUTUO_STORICO_MUTUO_STORICO_ID_GENERATOR")
	@Column(name="mutuo_storico_id")
	private Integer mutuoStoricoId;

	@ManyToOne
	@JoinColumn(name="mutuo_id")
	private SiacTMutuo siacTMutuo;
	
	@Column(name="mutuo_numero")
	private Integer mutuoNumero;

	@Column(name="mutuo_oggetto")
	private String mutuoOggetto;
	
	//bi-directional many-to-one association to SiacDMutuoStato
	/** The siac d mutuo stato. */
	@ManyToOne
	@JoinColumn(name="mutuo_stato_id")
	private SiacDMutuoStato siacDMutuoStato;
	
	@ManyToOne
	@JoinColumn(name="mutuo_tipo_tasso_id")
	private SiacDMutuoTipoTasso siacDMutuoTipoTasso;
	
	@Column(name = "mutuo_data_atto")
	private Date mutuoDataAtto;
	
	@ManyToOne
	@JoinColumn(name="mutuo_soggetto_id")
	private SiacTSoggetto siacTSoggetto;	
	
	@Column(name="mutuo_somma_iniziale")
	private BigDecimal mutuoSommaIniziale;
	
	@Column(name="mutuo_somma_effettiva")
	private BigDecimal mutuoSommaEffettiva;

	@Column(name="mutuo_tasso")
	private BigDecimal mutuoTasso;
	
	@Column(name="mutuo_tasso_euribor")
	private BigDecimal mutuoTassoEuribor;
	
	@Column(name="mutuo_tasso_spread")
	private BigDecimal mutuoTassoSpread;

	@Column(name="mutuo_durata_anni")
	private Integer mutuoDurataAnni;
	
	@Column(name="mutuo_anno_inizio")
	private Integer mutuoAnnoInizio;
	
	@Column(name="mutuo_anno_fine")
	private Integer mutuoAnnoFine;
	
	@ManyToOne
	@JoinColumn(name="mutuo_periodo_rimborso_id")
	private SiacDMutuoPeriodoRimborso siacDMutuoPeriodoRimborso;
	
	@Column(name="mutuo_data_scadenza_prima_rata")
	private Date mutuoDataScadenzaPrimaRata;
	
	@Column(name="mutuo_annualita")
	private BigDecimal mutuoAnnualita;
	
	@Column(name="mutuo_preammortamento")
	private BigDecimal mutuoPreAmmortamento;
	
	@Column(name="mutuo_data_inizio_piano_ammortamento")
	private Date mutuoDataInizioPianoAmmortamento;
	
	@Column(name="mutuo_data_scadenza_ultima_rata")
	private Date mutuoDataScadenzaUltimaRata;
	


	public SiacSMutuoStorico(SiacTMutuo mutuo) {
		
		SiacTMutuo siacTMutuo = new SiacTMutuo();
		siacTMutuo.setUid(mutuo.getUid());
		this.siacTMutuo = siacTMutuo;
		this.mutuoNumero = mutuo.getMutuoNumero();
		this.mutuoOggetto = mutuo.getMutuoOggetto();
		this.siacDMutuoStato = mutuo.getSiacDMutuoStato();
		this.siacDMutuoTipoTasso = mutuo.getSiacDMutuoTipoTasso();
		this.mutuoDataAtto = mutuo.getMutuoDataAtto();
		this.siacTSoggetto = mutuo.getSiacTSoggetto();
		this.mutuoSommaIniziale = mutuo.getMutuoSommaIniziale();
		this.mutuoSommaEffettiva = mutuo.getMutuoSommaEffettiva();
		this.mutuoTasso = mutuo.getMutuoTasso();
		this.mutuoTassoEuribor = mutuo.getMutuoTassoEuribor();
		this.mutuoTassoSpread = mutuo.getMutuoTassoSpread();
		this.mutuoDurataAnni = mutuo.getMutuoDurataAnni();
		this.mutuoAnnoInizio = mutuo.getMutuoAnnoInizio();
		this.mutuoAnnoFine = mutuo.getMutuoAnnoFine();
		this.siacDMutuoPeriodoRimborso = mutuo.getSiacDMutuoPeriodoRimborso();
		this.mutuoDataScadenzaPrimaRata = mutuo.getMutuoDataScadenzaPrimaRata();
		this.mutuoAnnualita = mutuo.getMutuoAnnualita();
		this.mutuoPreAmmortamento = mutuo.getMutuoPreAmmortamento();
		this.mutuoDataInizioPianoAmmortamento = mutuo.getMutuoDataInizioPianoAmmortamento();
		this.mutuoDataScadenzaUltimaRata = mutuo.getMutuoDataScadenzaUltimaRata();
	}

	@Override
	public Integer getUid() {
		return getMutuoStoricoId();
	}

	@Override
	public void setUid(Integer uid) {
		setMutuoStoricoId(uid);
	}
	
	public Integer getMutuoStoricoId() {
		return mutuoStoricoId;
	}

	public void setMutuoStoricoId(Integer mutuoStoricoId) {
		this.mutuoStoricoId = mutuoStoricoId;
	}

	public SiacTMutuo getSiacTMutuo() {
		return siacTMutuo;
	}

	public void setSiacTMutuo(SiacTMutuo siacTMutuo) {
		this.siacTMutuo = siacTMutuo;
	}

	public Integer getMutuoNumero() {
		return mutuoNumero;
	}

	public void setMutuoNumero(Integer mutuoNumero) {
		this.mutuoNumero = mutuoNumero;
	}

	public String getMutuoOggetto() {
		return mutuoOggetto;
	}

	public void setMutuoOggetto(String mutuoOggetto) {
		this.mutuoOggetto = mutuoOggetto;
	}

	public SiacDMutuoStato getSiacDMutuoStato() {
		return siacDMutuoStato;
	}

	public void setSiacDMutuoStato(SiacDMutuoStato siacDMutuoStato) {
		this.siacDMutuoStato = siacDMutuoStato;
	}

	public SiacDMutuoTipoTasso getSiacDMutuoTipoTasso() {
		return siacDMutuoTipoTasso;
	}

	public void setSiacDMutuoTipoTasso(SiacDMutuoTipoTasso siacDMutuoTipoTasso) {
		this.siacDMutuoTipoTasso = siacDMutuoTipoTasso;
	}

	public Date getMutuoDataAtto() {
		return mutuoDataAtto;
	}

	public void setMutuoDataAtto(Date mutuoDataAtto) {
		this.mutuoDataAtto = mutuoDataAtto;
	}

	public SiacTSoggetto getSiacTSoggetto() {
		return siacTSoggetto;
	}

	public void setSiacTSoggetto(SiacTSoggetto siacTSoggetto) {
		this.siacTSoggetto = siacTSoggetto;
	}

	public BigDecimal getMutuoSommaIniziale() {
		return mutuoSommaIniziale;
	}

	public void setMutuoSommaIniziale(BigDecimal mutuoSommaIniziale) {
		this.mutuoSommaIniziale = mutuoSommaIniziale;
	}

	public BigDecimal getMutuoSommaEffettiva() {
		return mutuoSommaEffettiva;
	}

	public void setMutuoSommaEffettiva(BigDecimal mutuoSommaEffettiva) {
		this.mutuoSommaEffettiva = mutuoSommaEffettiva;
	}

	public BigDecimal getMutuoTasso() {
		return mutuoTasso;
	}

	public void setMutuoTasso(BigDecimal mutuoTasso) {
		this.mutuoTasso = mutuoTasso;
	}

	public BigDecimal getMutuoTassoEuribor() {
		return mutuoTassoEuribor;
	}

	public void setMutuoTassoEuribor(BigDecimal mutuoTassoEuribor) {
		this.mutuoTassoEuribor = mutuoTassoEuribor;
	}

	public BigDecimal getMutuoTassoSpread() {
		return mutuoTassoSpread;
	}

	public void setMutuoTassoSpread(BigDecimal mutuoTassoSpread) {
		this.mutuoTassoSpread = mutuoTassoSpread;
	}

	public Integer getMutuoDurataAnni() {
		return mutuoDurataAnni;
	}

	public void setMutuoDurataAnni(Integer mutuoDurataAnni) {
		this.mutuoDurataAnni = mutuoDurataAnni;
	}

	public Integer getMutuoAnnoInizio() {
		return mutuoAnnoInizio;
	}

	public void setMutuoAnnoInizio(Integer mutuoAnnoInizio) {
		this.mutuoAnnoInizio = mutuoAnnoInizio;
	}

	public Integer getMutuoAnnoFine() {
		return mutuoAnnoFine;
	}

	public void setMutuoAnnoFine(Integer mutuoAnnoFine) {
		this.mutuoAnnoFine = mutuoAnnoFine;
	}

	public SiacDMutuoPeriodoRimborso getSiacDMutuoPeriodoRimborso() {
		return siacDMutuoPeriodoRimborso;
	}

	public void setSiacDMutuoPeriodoRimborso(SiacDMutuoPeriodoRimborso siacDMutuoPeriodoRimborso) {
		this.siacDMutuoPeriodoRimborso = siacDMutuoPeriodoRimborso;
	}

	public Date getMutuoDataScadenzaPrimaRata() {
		return mutuoDataScadenzaPrimaRata;
	}

	public void setMutuoDataScadenzaPrimaRata(Date mutuoDataScadenzaPrimaRata) {
		this.mutuoDataScadenzaPrimaRata = mutuoDataScadenzaPrimaRata;
	}

	public BigDecimal getMutuoAnnualita() {
		return mutuoAnnualita;
	}

	public void setMutuoAnnualita(BigDecimal mutuoAnnualita) {
		this.mutuoAnnualita = mutuoAnnualita;
	}

	public BigDecimal getMutuoPreAmmortamento() {
		return mutuoPreAmmortamento;
	}

	public void setMutuoPreAmmortamento(BigDecimal mutuoPreAmmortamento) {
		this.mutuoPreAmmortamento = mutuoPreAmmortamento;
	}

	public Date getMutuoDataInizioPianoAmmortamento() {
		return mutuoDataInizioPianoAmmortamento;
	}

	public void setMutuoDataInizioPianoAmmortamento(Date mutuoDataInizioPianoAmmortamento) {
		this.mutuoDataInizioPianoAmmortamento = mutuoDataInizioPianoAmmortamento;
	}

	public Date getMutuoDataScadenzaUltimaRata() {
		return mutuoDataScadenzaUltimaRata;
	}

	public void setMutuoDataScadenzaUltimaRata(Date mutuoDataScadenzaUltimaRata) {
		this.mutuoDataScadenzaUltimaRata = mutuoDataScadenzaUltimaRata;
	}
	

	
}