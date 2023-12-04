/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
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

import it.csi.siac.siacfinser.integration.entity.base.SiacLoginMultiplo;


/**
 * The persistent class for the siac_t_ordinativo database table.
 * 
 */
@Entity
@Table(name="siac_t_ordinativo")
public class SiacTOrdinativoFin extends SiacLoginMultiplo {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_ORDINATIVO_ID_GENERATOR", allocationSize=1, sequenceName="siac_t_ordinativo_ord_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_ORDINATIVO_ID_GENERATOR")
	@Column(name="ord_id")
	private Integer ordId;

	@Column(name="ord_anno")
	private Integer ordAnno;

	@Column(name="ord_beneficiariomult")
	private Boolean ordBeneficiariomult;

	@Column(name="ord_cast_cassa")
	private BigDecimal ordCastCassa;

	@Column(name="ord_cast_competenza")
	private BigDecimal ordCastCompetenza;

	@Column(name="ord_cast_emessi")
	private BigDecimal ordCastEmessi;

	@Column(name="ord_desc")
	private String ordDesc;

	@Column(name="ord_emissione_data")
	private Timestamp ordEmissioneData;
	
	@Column(name="ord_trasm_oil_data")
	private Timestamp ordTrasmisisoneData;

	@Column(name="ord_numero")
	private BigDecimal ordNumero;

	@Column(name="ord_riduzione_data")
	private Timestamp ordRiduzioneData;

	@Column(name="ord_spostamento_data")
	private Timestamp ordSpostamentoData;

	@Column(name="ord_variazione_data")
	private Timestamp ordVariazioneData;
	
	@Column(name = "ord_da_trasmettere")
	private Boolean ordDaTrasmettere;

	//ottobre 2017 SIOPE PLUS:
	
	//bi-directional many-to-one association to SiacDSiopeAssenzaMotivazioneFin
	@ManyToOne
	@JoinColumn(name="siope_assenza_motivazione_id")
	private SiacDSiopeAssenzaMotivazioneFin siacDSiopeAssenzaMotivazione;
	
	//bi-directional many-to-one association to SiacDSiopeTipoDebitoFin
	@ManyToOne
	@JoinColumn(name="siope_tipo_debito_id")
	private SiacDSiopeTipoDebitoFin siacDSiopeTipoDebitoFin;
	
	//bi-directional many-to-one association to SiacDCausaleFin
	@ManyToOne
	@JoinColumn(name="caus_id")
	private SiacDCausaleFin siacDCausale;

	//bi-directional many-to-one association to SiacROrdinativoFin
	@OneToMany(mappedBy="siacTOrdinativo1")
	private List<SiacROrdinativoFin> siacROrdinativos1 = new ArrayList<SiacROrdinativoFin>();

	//bi-directional many-to-one association to SiacROrdinativoFin
	@OneToMany(mappedBy="siacTOrdinativo2")
	private List<SiacROrdinativoFin> siacROrdinativos2;

	//bi-directional many-to-one association to SiacROrdinativoAttoAmmFin
	@OneToMany(mappedBy="siacTOrdinativo")
	private List<SiacROrdinativoAttoAmmFin> siacROrdinativoAttoAmms;

	//bi-directional many-to-one association to SiacROrdinativoAttrFin
	@OneToMany(mappedBy="siacTOrdinativo")
	private List<SiacROrdinativoAttrFin> siacROrdinativoAttrs;

	//bi-directional many-to-one association to SiacROrdinativoBilElemFin
	@OneToMany(mappedBy="siacTOrdinativo")
	private List<SiacROrdinativoBilElemFin> siacROrdinativoBilElems;

	//bi-directional many-to-one association to SiacROrdinativoClassFin
	@OneToMany(mappedBy="siacTOrdinativo")
	private List<SiacROrdinativoClassFin> siacROrdinativoClasses;

	//bi-directional many-to-one association to SiacROrdinativoModpagFin
	@OneToMany(mappedBy="siacTOrdinativo")
	private List<SiacROrdinativoModpagFin> siacROrdinativoModpags;

	//bi-directional many-to-one association to SiacROrdinativoProvCassaFin
	@OneToMany(mappedBy="siacTOrdinativo")
	private List<SiacROrdinativoProvCassaFin> siacROrdinativoProvCassas;

	//bi-directional many-to-one association to SiacROrdinativoSoggettoFin
	@OneToMany(mappedBy="siacTOrdinativo")
	private List<SiacROrdinativoSoggettoFin> siacROrdinativoSoggettos;

	//bi-directional many-to-one association to SiacROrdinativoStatoFin
	@OneToMany(mappedBy="siacTOrdinativo")
	private List<SiacROrdinativoStatoFin> siacROrdinativoStatos;

	//bi-directional many-to-one association to SiacDCodicebolloFin
	@ManyToOne
	@JoinColumn(name="codbollo_id")
	private SiacDCodicebolloFin siacDCodicebollo;

	//bi-directional many-to-one association to SiacDCommissioneTipoFin
	@ManyToOne
	@JoinColumn(name="comm_tipo_id")
	private SiacDCommissioneTipoFin siacDCommissioneTipo;

	//bi-directional many-to-one association to SiacDContotesoreriaFin
	@ManyToOne
	@JoinColumn(name="contotes_id")
	private SiacDContotesoreriaFin siacDContotesoreria;

	//bi-directional many-to-one association to SiacDDistintaFin
	@ManyToOne
	@JoinColumn(name="dist_id")
	private SiacDDistintaFin siacDDistinta;

	//bi-directional many-to-one association to SiacDNoteTesoriereFin
	@ManyToOne
	@JoinColumn(name="notetes_id")
	private SiacDNoteTesoriereFin siacDNoteTesoriere;

	//bi-directional many-to-one association to SiacDOrdinativoTipoFin
	@ManyToOne
	@JoinColumn(name="ord_tipo_id")
	private SiacDOrdinativoTipoFin siacDOrdinativoTipo;

	//bi-directional many-to-one association to SiacTBilFin
	@ManyToOne
	@JoinColumn(name="bil_id")
	private SiacTBilFin siacTBil;

	//bi-directional many-to-one association to SiacTOrdinativoTFin
	@OneToMany(mappedBy="siacTOrdinativo")
	private List<SiacTOrdinativoTFin> siacTOrdinativoTs;
	
	
	//bi-directional many-to-one association to SiacROrdinativoFirma
	@OneToMany(mappedBy="siacTOrdinativo")
	private List<SiacROrdinativoFirma> siacROrdinativoFirmas;

	//bi-directional many-to-one association to SiacROrdinativoQuietanza
	@OneToMany(mappedBy="siacTOrdinativo")
	private List<SiacROrdinativoQuietanzaFin> siacROrdinativoQuietanzas;


	public SiacTOrdinativoFin() {
	}

	public SiacTOrdinativoFin(Integer uid) {
		this();
		setUid(uid);
	}

	public Integer getOrdId() {
		return this.ordId;
	}

	public void setOrdId(Integer ordId) {
		this.ordId = ordId;
	}

	public Integer getOrdAnno() {
		return this.ordAnno;
	}

	public void setOrdAnno(Integer ordAnno) {
		this.ordAnno = ordAnno;
	}

	public Boolean getOrdBeneficiariomult() {
		return this.ordBeneficiariomult;
	}

	public void setOrdBeneficiariomult(Boolean ordBeneficiariomult) {
		this.ordBeneficiariomult = ordBeneficiariomult;
	}

	public BigDecimal getOrdCastCassa() {
		return this.ordCastCassa;
	}

	public void setOrdCastCassa(BigDecimal ordCastCassa) {
		this.ordCastCassa = ordCastCassa;
	}

	public BigDecimal getOrdCastCompetenza() {
		return this.ordCastCompetenza;
	}

	public void setOrdCastCompetenza(BigDecimal ordCastCompetenza) {
		this.ordCastCompetenza = ordCastCompetenza;
	}

	public BigDecimal getOrdCastEmessi() {
		return this.ordCastEmessi;
	}

	public void setOrdCastEmessi(BigDecimal ordCastEmessi) {
		this.ordCastEmessi = ordCastEmessi;
	}

	public String getOrdDesc() {
		return this.ordDesc;
	}

	public void setOrdDesc(String ordDesc) {
		this.ordDesc = ordDesc;
	}

	public Timestamp getOrdEmissioneData() {
		return this.ordEmissioneData;
	}

	public void setOrdEmissioneData(Timestamp ordEmissioneData) {
		this.ordEmissioneData = ordEmissioneData;
	}

	public BigDecimal getOrdNumero() {
		return this.ordNumero;
	}

	public void setOrdNumero(BigDecimal ordNumero) {
		this.ordNumero = ordNumero;
	}

	public Timestamp getOrdRiduzioneData() {
		return this.ordRiduzioneData;
	}

	public void setOrdRiduzioneData(Timestamp ordRiduzioneData) {
		this.ordRiduzioneData = ordRiduzioneData;
	}

	public Timestamp getOrdSpostamentoData() {
		return this.ordSpostamentoData;
	}

	public void setOrdSpostamentoData(Timestamp ordSpostamentoData) {
		this.ordSpostamentoData = ordSpostamentoData;
	}

	public Timestamp getOrdVariazioneData() {
		return this.ordVariazioneData;
	}

	public void setOrdVariazioneData(Timestamp ordVariazioneData) {
		this.ordVariazioneData = ordVariazioneData;
	}

	public List<SiacROrdinativoFin> getSiacROrdinativos1() {
		return this.siacROrdinativos1;
	}

	public void setSiacROrdinativos1(List<SiacROrdinativoFin> siacROrdinativos1) {
		this.siacROrdinativos1 = siacROrdinativos1;
	}

	public SiacROrdinativoFin addSiacROrdinativos1(SiacROrdinativoFin siacROrdinativos1) {
		getSiacROrdinativos1().add(siacROrdinativos1);
		siacROrdinativos1.setSiacTOrdinativo1(this);

		return siacROrdinativos1;
	}

	public SiacROrdinativoFin removeSiacROrdinativos1(SiacROrdinativoFin siacROrdinativos1) {
		getSiacROrdinativos1().remove(siacROrdinativos1);
		siacROrdinativos1.setSiacTOrdinativo1(null);

		return siacROrdinativos1;
	}

	public List<SiacROrdinativoFin> getSiacROrdinativos2() {
		return this.siacROrdinativos2;
	}

	public void setSiacROrdinativos2(List<SiacROrdinativoFin> siacROrdinativos2) {
		this.siacROrdinativos2 = siacROrdinativos2;
	}

	public SiacROrdinativoFin addSiacROrdinativos2(SiacROrdinativoFin siacROrdinativos2) {
		getSiacROrdinativos2().add(siacROrdinativos2);
		siacROrdinativos2.setSiacTOrdinativo2(this);

		return siacROrdinativos2;
	}

	public SiacROrdinativoFin removeSiacROrdinativos2(SiacROrdinativoFin siacROrdinativos2) {
		getSiacROrdinativos2().remove(siacROrdinativos2);
		siacROrdinativos2.setSiacTOrdinativo2(null);

		return siacROrdinativos2;
	}

	public List<SiacROrdinativoAttoAmmFin> getSiacROrdinativoAttoAmms() {
		return this.siacROrdinativoAttoAmms;
	}

	public void setSiacROrdinativoAttoAmms(List<SiacROrdinativoAttoAmmFin> siacROrdinativoAttoAmms) {
		this.siacROrdinativoAttoAmms = siacROrdinativoAttoAmms;
	}

	public SiacROrdinativoAttoAmmFin addSiacROrdinativoAttoAmm(SiacROrdinativoAttoAmmFin siacROrdinativoAttoAmm) {
		getSiacROrdinativoAttoAmms().add(siacROrdinativoAttoAmm);
		siacROrdinativoAttoAmm.setSiacTOrdinativo(this);

		return siacROrdinativoAttoAmm;
	}

	public SiacROrdinativoAttoAmmFin removeSiacROrdinativoAttoAmm(SiacROrdinativoAttoAmmFin siacROrdinativoAttoAmm) {
		getSiacROrdinativoAttoAmms().remove(siacROrdinativoAttoAmm);
		siacROrdinativoAttoAmm.setSiacTOrdinativo(null);

		return siacROrdinativoAttoAmm;
	}

	public List<SiacROrdinativoAttrFin> getSiacROrdinativoAttrs() {
		return this.siacROrdinativoAttrs;
	}

	public void setSiacROrdinativoAttrs(List<SiacROrdinativoAttrFin> siacROrdinativoAttrs) {
		this.siacROrdinativoAttrs = siacROrdinativoAttrs;
	}

	public SiacROrdinativoAttrFin addSiacROrdinativoAttr(SiacROrdinativoAttrFin siacROrdinativoAttr) {
		getSiacROrdinativoAttrs().add(siacROrdinativoAttr);
		siacROrdinativoAttr.setSiacTOrdinativo(this);

		return siacROrdinativoAttr;
	}

	public SiacROrdinativoAttrFin removeSiacROrdinativoAttr(SiacROrdinativoAttrFin siacROrdinativoAttr) {
		getSiacROrdinativoAttrs().remove(siacROrdinativoAttr);
		siacROrdinativoAttr.setSiacTOrdinativo(null);

		return siacROrdinativoAttr;
	}

	public List<SiacROrdinativoBilElemFin> getSiacROrdinativoBilElems() {
		return this.siacROrdinativoBilElems;
	}

	public void setSiacROrdinativoBilElems(List<SiacROrdinativoBilElemFin> siacROrdinativoBilElems) {
		this.siacROrdinativoBilElems = siacROrdinativoBilElems;
	}

	public SiacROrdinativoBilElemFin addSiacROrdinativoBilElem(SiacROrdinativoBilElemFin siacROrdinativoBilElem) {
		getSiacROrdinativoBilElems().add(siacROrdinativoBilElem);
		siacROrdinativoBilElem.setSiacTOrdinativo(this);

		return siacROrdinativoBilElem;
	}

	public SiacROrdinativoBilElemFin removeSiacROrdinativoBilElem(SiacROrdinativoBilElemFin siacROrdinativoBilElem) {
		getSiacROrdinativoBilElems().remove(siacROrdinativoBilElem);
		siacROrdinativoBilElem.setSiacTOrdinativo(null);

		return siacROrdinativoBilElem;
	}

	public List<SiacROrdinativoClassFin> getSiacROrdinativoClasses() {
		return this.siacROrdinativoClasses;
	}

	public void setSiacROrdinativoClasses(List<SiacROrdinativoClassFin> siacROrdinativoClasses) {
		this.siacROrdinativoClasses = siacROrdinativoClasses;
	}

	public SiacROrdinativoClassFin addSiacROrdinativoClass(SiacROrdinativoClassFin siacROrdinativoClass) {
		getSiacROrdinativoClasses().add(siacROrdinativoClass);
		siacROrdinativoClass.setSiacTOrdinativo(this);

		return siacROrdinativoClass;
	}

	public SiacROrdinativoClassFin removeSiacROrdinativoClass(SiacROrdinativoClassFin siacROrdinativoClass) {
		getSiacROrdinativoClasses().remove(siacROrdinativoClass);
		siacROrdinativoClass.setSiacTOrdinativo(null);

		return siacROrdinativoClass;
	}

	public List<SiacROrdinativoModpagFin> getSiacROrdinativoModpags() {
		return this.siacROrdinativoModpags;
	}

	public void setSiacROrdinativoModpags(List<SiacROrdinativoModpagFin> siacROrdinativoModpags) {
		this.siacROrdinativoModpags = siacROrdinativoModpags;
	}

	public SiacROrdinativoModpagFin addSiacROrdinativoModpag(SiacROrdinativoModpagFin siacROrdinativoModpag) {
		getSiacROrdinativoModpags().add(siacROrdinativoModpag);
		siacROrdinativoModpag.setSiacTOrdinativo(this);

		return siacROrdinativoModpag;
	}

	public SiacROrdinativoModpagFin removeSiacROrdinativoModpag(SiacROrdinativoModpagFin siacROrdinativoModpag) {
		getSiacROrdinativoModpags().remove(siacROrdinativoModpag);
		siacROrdinativoModpag.setSiacTOrdinativo(null);

		return siacROrdinativoModpag;
	}

	public List<SiacROrdinativoProvCassaFin> getSiacROrdinativoProvCassas() {
		return this.siacROrdinativoProvCassas;
	}

	public void setSiacROrdinativoProvCassas(List<SiacROrdinativoProvCassaFin> siacROrdinativoProvCassas) {
		this.siacROrdinativoProvCassas = siacROrdinativoProvCassas;
	}

	public SiacROrdinativoProvCassaFin addSiacROrdinativoProvCassa(SiacROrdinativoProvCassaFin siacROrdinativoProvCassa) {
		getSiacROrdinativoProvCassas().add(siacROrdinativoProvCassa);
		siacROrdinativoProvCassa.setSiacTOrdinativo(this);

		return siacROrdinativoProvCassa;
	}

	public SiacROrdinativoProvCassaFin removeSiacROrdinativoProvCassa(SiacROrdinativoProvCassaFin siacROrdinativoProvCassa) {
		getSiacROrdinativoProvCassas().remove(siacROrdinativoProvCassa);
		siacROrdinativoProvCassa.setSiacTOrdinativo(null);

		return siacROrdinativoProvCassa;
	}

	public List<SiacROrdinativoSoggettoFin> getSiacROrdinativoSoggettos() {
		return this.siacROrdinativoSoggettos;
	}

	public void setSiacROrdinativoSoggettos(List<SiacROrdinativoSoggettoFin> siacROrdinativoSoggettos) {
		this.siacROrdinativoSoggettos = siacROrdinativoSoggettos;
	}

	public SiacROrdinativoSoggettoFin addSiacROrdinativoSoggetto(SiacROrdinativoSoggettoFin siacROrdinativoSoggetto) {
		getSiacROrdinativoSoggettos().add(siacROrdinativoSoggetto);
		siacROrdinativoSoggetto.setSiacTOrdinativo(this);

		return siacROrdinativoSoggetto;
	}

	public SiacROrdinativoSoggettoFin removeSiacROrdinativoSoggetto(SiacROrdinativoSoggettoFin siacROrdinativoSoggetto) {
		getSiacROrdinativoSoggettos().remove(siacROrdinativoSoggetto);
		siacROrdinativoSoggetto.setSiacTOrdinativo(null);

		return siacROrdinativoSoggetto;
	}

	public List<SiacROrdinativoStatoFin> getSiacROrdinativoStatos() {
		return this.siacROrdinativoStatos;
	}

	public void setSiacROrdinativoStatos(List<SiacROrdinativoStatoFin> siacROrdinativoStatos) {
		this.siacROrdinativoStatos = siacROrdinativoStatos;
	}

	public SiacROrdinativoStatoFin addSiacROrdinativoStato(SiacROrdinativoStatoFin siacROrdinativoStato) {
		getSiacROrdinativoStatos().add(siacROrdinativoStato);
		siacROrdinativoStato.setSiacTOrdinativo(this);

		return siacROrdinativoStato;
	}

	public SiacROrdinativoStatoFin removeSiacROrdinativoStato(SiacROrdinativoStatoFin siacROrdinativoStato) {
		getSiacROrdinativoStatos().remove(siacROrdinativoStato);
		siacROrdinativoStato.setSiacTOrdinativo(null);

		return siacROrdinativoStato;
	}

	public SiacDCodicebolloFin getSiacDCodicebollo() {
		return this.siacDCodicebollo;
	}

	public void setSiacDCodicebollo(SiacDCodicebolloFin siacDCodicebollo) {
		this.siacDCodicebollo = siacDCodicebollo;
	}

	public SiacDCommissioneTipoFin getSiacDCommissioneTipo() {
		return this.siacDCommissioneTipo;
	}

	public void setSiacDCommissioneTipo(SiacDCommissioneTipoFin siacDCommissioneTipo) {
		this.siacDCommissioneTipo = siacDCommissioneTipo;
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

	public SiacDNoteTesoriereFin getSiacDNoteTesoriere() {
		return this.siacDNoteTesoriere;
	}

	public void setSiacDNoteTesoriere(SiacDNoteTesoriereFin siacDNoteTesoriere) {
		this.siacDNoteTesoriere = siacDNoteTesoriere;
	}

	public SiacDOrdinativoTipoFin getSiacDOrdinativoTipo() {
		return this.siacDOrdinativoTipo;
	}

	public void setSiacDOrdinativoTipo(SiacDOrdinativoTipoFin siacDOrdinativoTipo) {
		this.siacDOrdinativoTipo = siacDOrdinativoTipo;
	}

	public SiacTBilFin getSiacTBil() {
		return this.siacTBil;
	}

	public void setSiacTBil(SiacTBilFin siacTBil) {
		this.siacTBil = siacTBil;
	}

	public List<SiacTOrdinativoTFin> getSiacTOrdinativoTs() {
		return this.siacTOrdinativoTs;
	}

	public void setSiacTOrdinativoTs(List<SiacTOrdinativoTFin> siacTOrdinativoTs) {
		this.siacTOrdinativoTs = siacTOrdinativoTs;
	}

	public SiacTOrdinativoTFin addSiacTOrdinativoT(SiacTOrdinativoTFin siacTOrdinativoT) {
		getSiacTOrdinativoTs().add(siacTOrdinativoT);
		siacTOrdinativoT.setSiacTOrdinativo(this);

		return siacTOrdinativoT;
	}

	public SiacTOrdinativoTFin removeSiacTOrdinativoT(SiacTOrdinativoTFin siacTOrdinativoT) {
		getSiacTOrdinativoTs().remove(siacTOrdinativoT);
		siacTOrdinativoT.setSiacTOrdinativo(null);

		return siacTOrdinativoT;
	}
	
	
	

	/**
	 * @return the ordTrasmisisoneData
	 */
	public Timestamp getOrdTrasmisisoneData() {
		return ordTrasmisisoneData;
	}

	/**
	 * @param ordTrasmisisoneData the ordTrasmisisoneData to set
	 */
	public void setOrdTrasmisisoneData(Timestamp ordTrasmisisoneData) {
		this.ordTrasmisisoneData = ordTrasmisisoneData;
	}

	/**
	 * @return the siacROrdinativoFirmas
	 */
	public List<SiacROrdinativoFirma> getSiacROrdinativoFirmas() {
		return siacROrdinativoFirmas;
	}

	/**
	 * @param siacROrdinativoFirmas the siacROrdinativoFirmas to set
	 */
	public void setSiacROrdinativoFirmas(
			List<SiacROrdinativoFirma> siacROrdinativoFirmas) {
		this.siacROrdinativoFirmas = siacROrdinativoFirmas;
	}

	/**
	 * @return the siacROrdinativoQuietanzas
	 */
	public List<SiacROrdinativoQuietanzaFin> getSiacROrdinativoQuietanzas() {
		return siacROrdinativoQuietanzas;
	}

	/**
	 * @param siacROrdinativoQuietanzas the siacROrdinativoQuietanzas to set
	 */
	public void setSiacROrdinativoQuietanzas(
			List<SiacROrdinativoQuietanzaFin> siacROrdinativoQuietanzas) {
		this.siacROrdinativoQuietanzas = siacROrdinativoQuietanzas;
	}

	@Override
	public Integer getUid() {
		// TODO Auto-generated method stub
		return this.ordId;
	}

	@Override
	public void setUid(Integer uid) {
		// TODO Auto-generated method stub
		this.ordId = uid;
	}

	public SiacDCausaleFin getSiacDCausale() {
		return siacDCausale;
	}

	public void setSiacDCausale(SiacDCausaleFin siacDCausale) {
		this.siacDCausale = siacDCausale;
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

	public Boolean getOrdDaTrasmettere() {
		return ordDaTrasmettere;
	}

	public void setOrdDaTrasmettere(Boolean ordDaTrasmettere) {
		this.ordDaTrasmettere = ordDaTrasmettere;
	}

}