/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
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

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;


/**
 * The persistent class for the siac_t_liquidazione database table.
 * 
 */
@Entity
@Table(name="siac_t_liquidazione")
public class SiacTLiquidazioneFin extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_LIQUIDAZIONE_LIQ_ID_GENERATOR", allocationSize=1, sequenceName="siac_t_liquidazione_liq_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_LIQUIDAZIONE_LIQ_ID_GENERATOR")
	@Column(name="liq_id")
	private Integer liqId;

	//bi-directional many-to-one association to SiacTBilFin
	@ManyToOne
	@JoinColumn(name="bil_id")
	private SiacTBilFin siacTBil;

	@Column(name="liq_anno")
	private Integer liqAnno;

	@Column(name="liq_automatica")
	private String liqAutomatica;

	@Column(name="liq_convalida_manuale")
	private String liqConvalidaManuale;

	@Column(name="liq_desc")
	private String liqDesc;

	@Column(name="liq_emissione_data")
	private Timestamp liqEmissioneData;

	@Column(name="liq_importo")
	private BigDecimal liqImporto;

	@Column(name="liq_numero")
	private BigDecimal liqNumero;
	
	//ottobre 2017 SIOPE PLUS:
	
	//bi-directional many-to-one association to SiacDSiopeAssenzaMotivazioneFin
	@ManyToOne
	@JoinColumn(name="siope_assenza_motivazione_id")
	private SiacDSiopeAssenzaMotivazioneFin siacDSiopeAssenzaMotivazione;
	
	//bi-directional many-to-one association to SiacDSiopeTipoDebitoFin
	@ManyToOne
	@JoinColumn(name="siope_tipo_debito_id")
	private SiacDSiopeTipoDebitoFin siacDSiopeTipoDebitoFin;
	
	//bi-directional many-to-one association to SiacRMutuoVoceLiquidazioneFin
	@OneToMany(mappedBy="siacTLiquidazione")
	private List<SiacRMutuoVoceLiquidazioneFin> siacRMutuoVoceLiquidaziones;

	//bi-directional many-to-one association to SiacRLiquidazioneAttoAmmFin
	@OneToMany(mappedBy="siacTLiquidazione")
	private List<SiacRLiquidazioneAttoAmmFin> siacRLiquidazioneAttoAmms;

	//bi-directional many-to-one association to SiacRLiquidazioneAttrFin
	@OneToMany(mappedBy="siacTLiquidazione")
	private List<SiacRLiquidazioneAttrFin> siacRLiquidazioneAttrs;

	//bi-directional many-to-one association to SiacRLiquidazioneClassFin
	@OneToMany(mappedBy="siacTLiquidazione")
	private List<SiacRLiquidazioneClassFin> siacRLiquidazioneClasses;

	//bi-directional many-to-one association to SiacRLiquidazioneMovgestFin
	@OneToMany(mappedBy="siacTLiquidazione")
	private List<SiacRLiquidazioneMovgestFin> siacRLiquidazioneMovgests;

	//bi-directional many-to-one association to SiacRLiquidazioneSoggettoFin
	@OneToMany(mappedBy="siacTLiquidazione")
	private List<SiacRLiquidazioneSoggettoFin> siacRLiquidazioneSoggettos;

	//bi-directional many-to-one association to SiacRLiquidazioneStatoFin
	@OneToMany(mappedBy="siacTLiquidazione")
	private List<SiacRLiquidazioneStatoFin> siacRLiquidazioneStatos;

	//bi-directional many-to-one association to SiacDContotesoreriaFin
	@ManyToOne
	@JoinColumn(name="contotes_id")
	private SiacDContotesoreriaFin siacDContotesoreria;

	//bi-directional many-to-one association to SiacDDistintaFin
	@ManyToOne
	@JoinColumn(name="dist_id")
	private SiacDDistintaFin siacDDistinta;

	//bi-directional many-to-one association to SiacRLiquidazioneOrdFin
	@OneToMany(mappedBy="siacTLiquidazione")
	private List<SiacRLiquidazioneOrdFin> siacRLiquidazioneOrds;
	
	//bi-directional many-to-one association to SiacRLiquidazioneStatoFin
	@OneToMany(mappedBy="siacTLiquidazione")
	private List<SiacRSubdocLiquidazioneFin> siacRSubdocLiquidaziones;
		
	
	@ManyToOne
	@JoinColumn(name="modpag_id")
	@NotFound(action = NotFoundAction.IGNORE)
	private SiacTModpagFin siacTModpag;
	

	@Column(name="soggetto_relaz_id")
	private Integer cessioneId;
	
	
	/**
	 * @return the cessioneId
	 */
	public Integer getCessioneId() {
		return cessioneId;
	}

	/**
	 * @param cessioneId the cessioneId to set
	 */
	public void setCessioneId(Integer cessioneId) {
		this.cessioneId = cessioneId;
	}

	public SiacTLiquidazioneFin() {
	}

	public Integer getLiqId() {
		return this.liqId;
	}

	public void setLiqId(Integer liqId) {
		this.liqId = liqId;
	}

	public Integer getLiqAnno() {
		return this.liqAnno;
	}

	public void setLiqAnno(Integer liqAnno) {
		this.liqAnno = liqAnno;
	}

	public String getLiqAutomatica() {
		return this.liqAutomatica;
	}

	public void setLiqAutomatica(String liqAutomatica) {
		this.liqAutomatica = liqAutomatica;
	}

	public String getLiqConvalidaManuale() {
		return this.liqConvalidaManuale;
	}

	public void setLiqConvalidaManuale(String liqConvalidaManuale) {
		this.liqConvalidaManuale = liqConvalidaManuale;
	}

	public String getLiqDesc() {
		return this.liqDesc;
	}

	public void setLiqDesc(String liqDesc) {
		this.liqDesc = liqDesc;
	}

	public Timestamp getLiqEmissioneData() {
		return this.liqEmissioneData;
	}

	public void setLiqEmissioneData(Timestamp liqEmissioneData) {
		this.liqEmissioneData = liqEmissioneData;
	}

	public BigDecimal getLiqImporto() {
		return this.liqImporto;
	}

	public void setLiqImporto(BigDecimal liqImporto) {
		this.liqImporto = liqImporto;
	}

	public BigDecimal getLiqNumero() {
		return this.liqNumero;
	}

	public void setLiqNumero(BigDecimal liqNumero) {
		this.liqNumero = liqNumero;
	}

	public List<SiacRLiquidazioneAttoAmmFin> getSiacRLiquidazioneAttoAmms() {
		return this.siacRLiquidazioneAttoAmms;
	}

	public void setSiacRLiquidazioneAttoAmms(List<SiacRLiquidazioneAttoAmmFin> siacRLiquidazioneAttoAmms) {
		this.siacRLiquidazioneAttoAmms = siacRLiquidazioneAttoAmms;
	}

	public SiacRLiquidazioneAttoAmmFin addSiacRLiquidazioneAttoAmm(SiacRLiquidazioneAttoAmmFin siacRLiquidazioneAttoAmm) {
		getSiacRLiquidazioneAttoAmms().add(siacRLiquidazioneAttoAmm);
		siacRLiquidazioneAttoAmm.setSiacTLiquidazione(this);

		return siacRLiquidazioneAttoAmm;
	}

	public SiacRLiquidazioneAttoAmmFin removeSiacRLiquidazioneAttoAmm(SiacRLiquidazioneAttoAmmFin siacRLiquidazioneAttoAmm) {
		getSiacRLiquidazioneAttoAmms().remove(siacRLiquidazioneAttoAmm);
		siacRLiquidazioneAttoAmm.setSiacTLiquidazione(null);

		return siacRLiquidazioneAttoAmm;
	}

	public List<SiacRLiquidazioneAttrFin> getSiacRLiquidazioneAttrs() {
		return this.siacRLiquidazioneAttrs;
	}

	public void setSiacRLiquidazioneAttrs(List<SiacRLiquidazioneAttrFin> siacRLiquidazioneAttrs) {
		this.siacRLiquidazioneAttrs = siacRLiquidazioneAttrs;
	}

	public SiacRLiquidazioneAttrFin addSiacRLiquidazioneAttr(SiacRLiquidazioneAttrFin siacRLiquidazioneAttr) {
		getSiacRLiquidazioneAttrs().add(siacRLiquidazioneAttr);
		siacRLiquidazioneAttr.setSiacTLiquidazione(this);

		return siacRLiquidazioneAttr;
	}

	public SiacRLiquidazioneAttrFin removeSiacRLiquidazioneAttr(SiacRLiquidazioneAttrFin siacRLiquidazioneAttr) {
		getSiacRLiquidazioneAttrs().remove(siacRLiquidazioneAttr);
		siacRLiquidazioneAttr.setSiacTLiquidazione(null);

		return siacRLiquidazioneAttr;
	}

	public List<SiacRLiquidazioneClassFin> getSiacRLiquidazioneClasses() {
		return this.siacRLiquidazioneClasses;
	}

	public void setSiacRLiquidazioneClasses(List<SiacRLiquidazioneClassFin> siacRLiquidazioneClasses) {
		this.siacRLiquidazioneClasses = siacRLiquidazioneClasses;
	}

	public SiacRLiquidazioneClassFin addSiacRLiquidazioneClass(SiacRLiquidazioneClassFin siacRLiquidazioneClass) {
		getSiacRLiquidazioneClasses().add(siacRLiquidazioneClass);
		siacRLiquidazioneClass.setSiacTLiquidazione(this);

		return siacRLiquidazioneClass;
	}

	public SiacRLiquidazioneClassFin removeSiacRLiquidazioneClass(SiacRLiquidazioneClassFin siacRLiquidazioneClass) {
		getSiacRLiquidazioneClasses().remove(siacRLiquidazioneClass);
		siacRLiquidazioneClass.setSiacTLiquidazione(null);

		return siacRLiquidazioneClass;
	}

	public List<SiacRLiquidazioneMovgestFin> getSiacRLiquidazioneMovgests() {
		return this.siacRLiquidazioneMovgests;
	}

	public void setSiacRLiquidazioneMovgests(List<SiacRLiquidazioneMovgestFin> siacRLiquidazioneMovgests) {
		this.siacRLiquidazioneMovgests = siacRLiquidazioneMovgests;
	}

	public SiacRLiquidazioneMovgestFin addSiacRLiquidazioneMovgest(SiacRLiquidazioneMovgestFin siacRLiquidazioneMovgest) {
		getSiacRLiquidazioneMovgests().add(siacRLiquidazioneMovgest);
		siacRLiquidazioneMovgest.setSiacTLiquidazione(this);

		return siacRLiquidazioneMovgest;
	}

	public SiacRLiquidazioneMovgestFin removeSiacRLiquidazioneMovgest(SiacRLiquidazioneMovgestFin siacRLiquidazioneMovgest) {
		getSiacRLiquidazioneMovgests().remove(siacRLiquidazioneMovgest);
		siacRLiquidazioneMovgest.setSiacTLiquidazione(null);

		return siacRLiquidazioneMovgest;
	}

	public List<SiacRLiquidazioneSoggettoFin> getSiacRLiquidazioneSoggettos() {
		return this.siacRLiquidazioneSoggettos;
	}

	public void setSiacRLiquidazioneSoggettos(List<SiacRLiquidazioneSoggettoFin> siacRLiquidazioneSoggettos) {
		this.siacRLiquidazioneSoggettos = siacRLiquidazioneSoggettos;
	}

	public SiacRLiquidazioneSoggettoFin addSiacRLiquidazioneSoggetto(SiacRLiquidazioneSoggettoFin siacRLiquidazioneSoggetto) {
		getSiacRLiquidazioneSoggettos().add(siacRLiquidazioneSoggetto);
		siacRLiquidazioneSoggetto.setSiacTLiquidazione(this);

		return siacRLiquidazioneSoggetto;
	}

	public SiacRLiquidazioneSoggettoFin removeSiacRLiquidazioneSoggetto(SiacRLiquidazioneSoggettoFin siacRLiquidazioneSoggetto) {
		getSiacRLiquidazioneSoggettos().remove(siacRLiquidazioneSoggetto);
		siacRLiquidazioneSoggetto.setSiacTLiquidazione(null);

		return siacRLiquidazioneSoggetto;
	}

	public List<SiacRLiquidazioneStatoFin> getSiacRLiquidazioneStatos() {
		return this.siacRLiquidazioneStatos;
	}

	public void setSiacRLiquidazioneStatos(List<SiacRLiquidazioneStatoFin> siacRLiquidazioneStatos) {
		this.siacRLiquidazioneStatos = siacRLiquidazioneStatos;
	}

	public SiacRLiquidazioneStatoFin addSiacRLiquidazioneStato(SiacRLiquidazioneStatoFin siacRLiquidazioneStato) {
		getSiacRLiquidazioneStatos().add(siacRLiquidazioneStato);
		siacRLiquidazioneStato.setSiacTLiquidazione(this);

		return siacRLiquidazioneStato;
	}

	public SiacRLiquidazioneStatoFin removeSiacRLiquidazioneStato(SiacRLiquidazioneStatoFin siacRLiquidazioneStato) {
		getSiacRLiquidazioneStatos().remove(siacRLiquidazioneStato);
		siacRLiquidazioneStato.setSiacTLiquidazione(null);

		return siacRLiquidazioneStato;
	}

	public SiacDContotesoreriaFin getSiacDContotesoreria() {
		return this.siacDContotesoreria;
	}

	public void setSiacDContotesoreria(SiacDContotesoreriaFin siacDContotesoreria) {
		this.siacDContotesoreria = siacDContotesoreria;
	}

	public SiacDDistintaFin getSiacDDistinta() {
		return this.siacDDistinta;
	}

	public void setSiacDDistinta(SiacDDistintaFin siacDDistinta) {
		this.siacDDistinta = siacDDistinta;
	}

	public SiacTModpagFin getSiacTModpag() {
		return this.siacTModpag;
	}

	public void setSiacTModpag(SiacTModpagFin siacTModpag) {
		this.siacTModpag = siacTModpag;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return liqId;
	}

	@Override
	public void setUid(Integer uid) {
		this.liqId = uid;
		
	}
	
	public SiacTBilFin getSiacTBil() {
		return siacTBil;
	}

	public List<SiacRMutuoVoceLiquidazioneFin> getSiacRMutuoVoceLiquidaziones() {
		return siacRMutuoVoceLiquidaziones;
	}

	public void setSiacTBil(SiacTBilFin siacTBil) {
		this.siacTBil = siacTBil;
	}

	public void setSiacRMutuoVoceLiquidaziones(
			List<SiacRMutuoVoceLiquidazioneFin> siacRMutuoVoceLiquidaziones) {
		this.siacRMutuoVoceLiquidaziones = siacRMutuoVoceLiquidaziones;
	}

	public List<SiacRLiquidazioneOrdFin> getSiacRLiquidazioneOrds() {
		return this.siacRLiquidazioneOrds;
	}

	public void setSiacRLiquidazioneOrds(List<SiacRLiquidazioneOrdFin> siacRLiquidazioneOrds) {
		this.siacRLiquidazioneOrds = siacRLiquidazioneOrds;
	}

	public SiacRLiquidazioneOrdFin addSiacRLiquidazioneOrd(SiacRLiquidazioneOrdFin siacRLiquidazioneOrd) {
		getSiacRLiquidazioneOrds().add(siacRLiquidazioneOrd);
		siacRLiquidazioneOrd.setSiacTLiquidazione(this);

		return siacRLiquidazioneOrd;
	}

	public SiacRLiquidazioneOrdFin removeSiacRLiquidazioneOrd(SiacRLiquidazioneOrdFin siacRLiquidazioneOrd) {
		getSiacRLiquidazioneOrds().remove(siacRLiquidazioneOrd);
		siacRLiquidazioneOrd.setSiacTLiquidazione(null);

		return siacRLiquidazioneOrd;
	}

	public List<SiacRSubdocLiquidazioneFin> getSiacRSubdocLiquidaziones() {
		return siacRSubdocLiquidaziones;
	}

	public void setSiacRSubdocLiquidaziones(List<SiacRSubdocLiquidazioneFin> siacRSubdocLiquidaziones) {
		this.siacRSubdocLiquidaziones = siacRSubdocLiquidaziones;
	}

	public SiacDSiopeAssenzaMotivazioneFin getSiacDSiopeAssenzaMotivazione() {
		return siacDSiopeAssenzaMotivazione;
	}

	public void setSiacDSiopeAssenzaMotivazione(SiacDSiopeAssenzaMotivazioneFin siacDSiopeAssenzaMotivazione) {
		this.siacDSiopeAssenzaMotivazione = siacDSiopeAssenzaMotivazione;
	}

	public SiacDSiopeTipoDebitoFin getSiacDSiopeTipoDebitoFin() {
		return siacDSiopeTipoDebitoFin;
	}

	public void setSiacDSiopeTipoDebitoFin(SiacDSiopeTipoDebitoFin siacDSiopeTipoDebitoFin) {
		this.siacDSiopeTipoDebitoFin = siacDSiopeTipoDebitoFin;
	}
	
}