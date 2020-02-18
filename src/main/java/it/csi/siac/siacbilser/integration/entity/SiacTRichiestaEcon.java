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
 * The persistent class for the siac_t_richiesta_econ database table.
 * 
 */
@Entity
@Table(name="siac_t_richiesta_econ")
@NamedQuery(name="SiacTRichiestaEcon.findAll", query="SELECT s FROM SiacTRichiestaEcon s")
public class SiacTRichiestaEcon extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_RICHIESTA_ECON_RICECONID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_RICHIESTA_ECON_RICECON_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_RICHIESTA_ECON_RICECONID_GENERATOR")
	@Column(name="ricecon_id")
	private Integer riceconId;
	
	@Column(name="ricecon_codice_fiscale")
	private String riceconCodiceFiscale;

	@Column(name="ricecon_cognome")
	private String riceconCognome;

	@Column(name="ricecon_da_proc_est")
	private Boolean riceconDaProcEst;

	@Column(name="ricecon_delegato_incasso")
	private String riceconDelegatoIncasso;

	@Column(name="ricecon_desc")
	private String riceconDesc;

	@Column(name="ricecon_importo")
	private BigDecimal riceconImporto;

	@Column(name="ricecon_matricola")
	private String riceconMatricola;

	@Column(name="ricecon_nome")
	private String riceconNome;

	@Column(name="ricecon_note")
	private String riceconNote;

	@Column(name="ricecon_numero")
	private Integer riceconNumero;

	@Column(name="ricecon_unita_organizzativa")
	private String riceconUnitaOrganizzativa;

	@Column(name="ricecon_codice_beneficiario")
	private String riceconCodiceBeneficiario;

	@Column(name="missione_esterna_id")
	private String missioneEsternaId;
	
	@Column(name="ricecon_rit_su_fattura")
	private Boolean riceconRitSuFattura;

	//bi-directional many-to-one association to SiacRRichiestaEconClass
	@OneToMany(mappedBy="siacTRichiestaEcon", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRRichiestaEconClass> siacRRichiestaEconClasses;

	//bi-directional many-to-one association to SiacRRichiestaEconMovgest
	@OneToMany(mappedBy="siacTRichiestaEcon", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRRichiestaEconMovgest> siacRRichiestaEconMovgests;

	//bi-directional many-to-one association to SiacRRichiestaEconSog
	@OneToMany(mappedBy="siacTRichiestaEcon", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRRichiestaEconSog> siacRRichiestaEconSogs;

	//bi-directional many-to-one association to SiacRRichiestaEconStato
	@OneToMany(mappedBy="siacTRichiestaEcon", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRRichiestaEconStato> siacRRichiestaEconStatos;

	//bi-directional many-to-one association to SiacTGiustificativo
	@OneToMany(mappedBy="siacTRichiestaEcon", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacTGiustificativo> siacTGiustificativos;

	//bi-directional many-to-one association to SiacTGiustificativoDet
	@OneToMany(mappedBy="siacTRichiestaEcon", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacTGiustificativoDet> siacTGiustificativoDets;

	//bi-directional many-to-one association to SiacTMovimento
	@OneToMany(mappedBy="siacTRichiestaEcon", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacTMovimento> siacTMovimentos;

	//bi-directional many-to-one association to SiacDRichiestaEconTipo
	@ManyToOne
	@JoinColumn(name="ricecon_tipo_id")
	private SiacDRichiestaEconTipo siacDRichiestaEconTipo;

	//bi-directional many-to-one association to SiacTBil
	@ManyToOne
	@JoinColumn(name="bil_id")
	private SiacTBil siacTBil;

	//bi-directional many-to-one association to SiacTCassaEcon
	@ManyToOne
	@JoinColumn(name="cassaecon_id")
	private SiacTCassaEcon siacTCassaEcon;

	//bi-directional many-to-one association to SiacTRichiestaEconSospesa
	@OneToMany(mappedBy="siacTRichiestaEcon", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacTRichiestaEconSospesa> siacTRichiestaEconSospesas;

	//bi-directional many-to-one association to SiacTTrasfMiss
	@OneToMany(mappedBy="siacTRichiestaEcon", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacTTrasfMiss> siacTTrasfMisses;

	//bi-directional many-to-one association to SiacRRichiestaEconSubdoc
	@OneToMany(mappedBy="siacTRichiestaEcon", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRRichiestaEconSubdoc> siacRRichiestaEconSubdocs;

	public SiacTRichiestaEcon() {
	}

	public Integer getRiceconId() {
		return this.riceconId;
	}

	public void setRiceconId(Integer riceconId) {
		this.riceconId = riceconId;
	}

	public String getRiceconCodiceFiscale() {
		return this.riceconCodiceFiscale;
	}

	public void setRiceconCodiceFiscale(String riceconCodiceFiscale) {
		this.riceconCodiceFiscale = riceconCodiceFiscale;
	}

	public String getRiceconCognome() {
		return this.riceconCognome;
	}

	public void setRiceconCognome(String riceconCognome) {
		this.riceconCognome = riceconCognome;
	}

	public Boolean getRiceconDaProcEst() {
		return this.riceconDaProcEst;
	}

	public void setRiceconDaProcEst(Boolean riceconDaProcEst) {
		this.riceconDaProcEst = riceconDaProcEst;
	}

	public String getRiceconDelegatoIncasso() {
		return this.riceconDelegatoIncasso;
	}

	public void setRiceconDelegatoIncasso(String riceconDelegatoIncasso) {
		this.riceconDelegatoIncasso = riceconDelegatoIncasso;
	}

	public String getRiceconDesc() {
		return this.riceconDesc;
	}

	public void setRiceconDesc(String riceconDesc) {
		this.riceconDesc = riceconDesc;
	}

	public BigDecimal getRiceconImporto() {
		return this.riceconImporto;
	}

	public void setRiceconImporto(BigDecimal riceconImporto) {
		this.riceconImporto = riceconImporto;
	}

	public String getRiceconMatricola() {
		return this.riceconMatricola;
	}

	public void setRiceconMatricola(String riceconMatricola) {
		this.riceconMatricola = riceconMatricola;
	}

	public String getRiceconNome() {
		return this.riceconNome;
	}

	public void setRiceconNome(String riceconNome) {
		this.riceconNome = riceconNome;
	}

	public String getRiceconNote() {
		return this.riceconNote;
	}

	public void setRiceconNote(String riceconNote) {
		this.riceconNote = riceconNote;
	}

	public Integer getRiceconNumero() {
		return this.riceconNumero;
	}

	public void setRiceconNumero(Integer riceconNumero) {
		this.riceconNumero = riceconNumero;
	}

	public String getRiceconUnitaOrganizzativa() {
		return this.riceconUnitaOrganizzativa;
	}

	public void setRiceconUnitaOrganizzativa(String riceconUnitaOrganizzativa) {
		this.riceconUnitaOrganizzativa = riceconUnitaOrganizzativa;
	}

	public String getRiceconCodiceBeneficiario() {
		return riceconCodiceBeneficiario;
	}

	public void setRiceconCodiceBeneficiario(String riceconCodiceBeneficiario) {
		this.riceconCodiceBeneficiario = riceconCodiceBeneficiario;
	}

	public String getMissioneEsternaId() {
		return missioneEsternaId;
	}

	public void setMissioneEsternaId(String missioneEsternaId) {
		this.missioneEsternaId = missioneEsternaId;
	}
	
	public Boolean getRiceconRitSuFattura() {
		return riceconRitSuFattura;
	}

	public void setRiceconRitSuFattura(Boolean riceconRitSuFattura) {
		this.riceconRitSuFattura = riceconRitSuFattura;
	}

	public List<SiacRRichiestaEconClass> getSiacRRichiestaEconClasses() {
		return this.siacRRichiestaEconClasses;
	}

	public void setSiacRRichiestaEconClasses(List<SiacRRichiestaEconClass> siacRRichiestaEconClasses) {
		this.siacRRichiestaEconClasses = siacRRichiestaEconClasses;
	}

	public SiacRRichiestaEconClass addSiacRRichiestaEconClass(SiacRRichiestaEconClass siacRRichiestaEconClass) {
		getSiacRRichiestaEconClasses().add(siacRRichiestaEconClass);
		siacRRichiestaEconClass.setSiacTRichiestaEcon(this);

		return siacRRichiestaEconClass;
	}

	public SiacRRichiestaEconClass removeSiacRRichiestaEconClass(SiacRRichiestaEconClass siacRRichiestaEconClass) {
		getSiacRRichiestaEconClasses().remove(siacRRichiestaEconClass);
		siacRRichiestaEconClass.setSiacTRichiestaEcon(null);

		return siacRRichiestaEconClass;
	}

	public List<SiacRRichiestaEconMovgest> getSiacRRichiestaEconMovgests() {
		return this.siacRRichiestaEconMovgests;
	}

	public void setSiacRRichiestaEconMovgests(List<SiacRRichiestaEconMovgest> siacRRichiestaEconMovgests) {
		this.siacRRichiestaEconMovgests = siacRRichiestaEconMovgests;
	}

	public SiacRRichiestaEconMovgest addSiacRRichiestaEconMovgest(SiacRRichiestaEconMovgest siacRRichiestaEconMovgest) {
		getSiacRRichiestaEconMovgests().add(siacRRichiestaEconMovgest);
		siacRRichiestaEconMovgest.setSiacTRichiestaEcon(this);

		return siacRRichiestaEconMovgest;
	}

	public SiacRRichiestaEconMovgest removeSiacRRichiestaEconMovgest(SiacRRichiestaEconMovgest siacRRichiestaEconMovgest) {
		getSiacRRichiestaEconMovgests().remove(siacRRichiestaEconMovgest);
		siacRRichiestaEconMovgest.setSiacTRichiestaEcon(null);

		return siacRRichiestaEconMovgest;
	}

	public List<SiacRRichiestaEconSog> getSiacRRichiestaEconSogs() {
		return this.siacRRichiestaEconSogs;
	}

	public void setSiacRRichiestaEconSogs(List<SiacRRichiestaEconSog> siacRRichiestaEconSogs) {
		this.siacRRichiestaEconSogs = siacRRichiestaEconSogs;
	}

	public SiacRRichiestaEconSog addSiacRRichiestaEconSog(SiacRRichiestaEconSog siacRRichiestaEconSog) {
		getSiacRRichiestaEconSogs().add(siacRRichiestaEconSog);
		siacRRichiestaEconSog.setSiacTRichiestaEcon(this);

		return siacRRichiestaEconSog;
	}

	public SiacRRichiestaEconSog removeSiacRRichiestaEconSog(SiacRRichiestaEconSog siacRRichiestaEconSog) {
		getSiacRRichiestaEconSogs().remove(siacRRichiestaEconSog);
		siacRRichiestaEconSog.setSiacTRichiestaEcon(null);

		return siacRRichiestaEconSog;
	}

	public List<SiacRRichiestaEconStato> getSiacRRichiestaEconStatos() {
		return this.siacRRichiestaEconStatos;
	}

	public void setSiacRRichiestaEconStatos(List<SiacRRichiestaEconStato> siacRRichiestaEconStatos) {
		this.siacRRichiestaEconStatos = siacRRichiestaEconStatos;
	}

	public SiacRRichiestaEconStato addSiacRRichiestaEconStato(SiacRRichiestaEconStato siacRRichiestaEconStato) {
		getSiacRRichiestaEconStatos().add(siacRRichiestaEconStato);
		siacRRichiestaEconStato.setSiacTRichiestaEcon(this);

		return siacRRichiestaEconStato;
	}

	public SiacRRichiestaEconStato removeSiacRRichiestaEconStato(SiacRRichiestaEconStato siacRRichiestaEconStato) {
		getSiacRRichiestaEconStatos().remove(siacRRichiestaEconStato);
		siacRRichiestaEconStato.setSiacTRichiestaEcon(null);

		return siacRRichiestaEconStato;
	}

	public List<SiacTGiustificativo> getSiacTGiustificativos() {
		return this.siacTGiustificativos;
	}

	public void setSiacTGiustificativos(List<SiacTGiustificativo> siacTGiustificativos) {
		this.siacTGiustificativos = siacTGiustificativos;
	}

	public SiacTGiustificativo addSiacTGiustificativo(SiacTGiustificativo siacTGiustificativo) {
		getSiacTGiustificativos().add(siacTGiustificativo);
		siacTGiustificativo.setSiacTRichiestaEcon(this);

		return siacTGiustificativo;
	}

	public SiacTGiustificativo removeSiacTGiustificativo(SiacTGiustificativo siacTGiustificativo) {
		getSiacTGiustificativos().remove(siacTGiustificativo);
		siacTGiustificativo.setSiacTRichiestaEcon(null);

		return siacTGiustificativo;
	}

	public List<SiacTGiustificativoDet> getSiacTGiustificativoDets() {
		return this.siacTGiustificativoDets;
	}

	public void setSiacTGiustificativoDets(List<SiacTGiustificativoDet> siacTGiustificativoDets) {
		this.siacTGiustificativoDets = siacTGiustificativoDets;
	}

	public SiacTGiustificativoDet addSiacTGiustificativoDet(SiacTGiustificativoDet siacTGiustificativoDet) {
		getSiacTGiustificativoDets().add(siacTGiustificativoDet);
		siacTGiustificativoDet.setSiacTRichiestaEcon(this);

		return siacTGiustificativoDet;
	}

	public SiacTGiustificativoDet removeSiacTGiustificativoDet(SiacTGiustificativoDet siacTGiustificativoDet) {
		getSiacTGiustificativoDets().remove(siacTGiustificativoDet);
		siacTGiustificativoDet.setSiacTRichiestaEcon(null);

		return siacTGiustificativoDet;
	}

	public List<SiacTMovimento> getSiacTMovimentos() {
		return this.siacTMovimentos;
	}

	public void setSiacTMovimentos(List<SiacTMovimento> siacTMovimentos) {
		this.siacTMovimentos = siacTMovimentos;
	}

	public SiacTMovimento addSiacTMovimento(SiacTMovimento siacTMovimento) {
		getSiacTMovimentos().add(siacTMovimento);
		siacTMovimento.setSiacTRichiestaEcon(this);

		return siacTMovimento;
	}

	public SiacTMovimento removeSiacTMovimento(SiacTMovimento siacTMovimento) {
		getSiacTMovimentos().remove(siacTMovimento);
		siacTMovimento.setSiacTRichiestaEcon(null);

		return siacTMovimento;
	}

	public SiacDRichiestaEconTipo getSiacDRichiestaEconTipo() {
		return this.siacDRichiestaEconTipo;
	}

	public void setSiacDRichiestaEconTipo(SiacDRichiestaEconTipo siacDRichiestaEconTipo) {
		this.siacDRichiestaEconTipo = siacDRichiestaEconTipo;
	}

	public SiacTBil getSiacTBil() {
		return this.siacTBil;
	}

	public void setSiacTBil(SiacTBil siacTBil) {
		this.siacTBil = siacTBil;
	}

	public SiacTCassaEcon getSiacTCassaEcon() {
		return this.siacTCassaEcon;
	}

	public void setSiacTCassaEcon(SiacTCassaEcon siacTCassaEcon) {
		this.siacTCassaEcon = siacTCassaEcon;
	}

	public List<SiacTRichiestaEconSospesa> getSiacTRichiestaEconSospesas() {
		return this.siacTRichiestaEconSospesas;
	}

	public void setSiacTRichiestaEconSospesas(List<SiacTRichiestaEconSospesa> siacTRichiestaEconSospesas) {
		this.siacTRichiestaEconSospesas = siacTRichiestaEconSospesas;
	}

	public SiacTRichiestaEconSospesa addSiacTRichiestaEconSospesa(SiacTRichiestaEconSospesa siacTRichiestaEconSospesa) {
		getSiacTRichiestaEconSospesas().add(siacTRichiestaEconSospesa);
		siacTRichiestaEconSospesa.setSiacTRichiestaEcon(this);

		return siacTRichiestaEconSospesa;
	}

	public SiacTRichiestaEconSospesa removeSiacTRichiestaEconSospesa(SiacTRichiestaEconSospesa siacTRichiestaEconSospesa) {
		getSiacTRichiestaEconSospesas().remove(siacTRichiestaEconSospesa);
		siacTRichiestaEconSospesa.setSiacTRichiestaEcon(null);

		return siacTRichiestaEconSospesa;
	}

	public List<SiacTTrasfMiss> getSiacTTrasfMisses() {
		return this.siacTTrasfMisses;
	}

	public void setSiacTTrasfMisses(List<SiacTTrasfMiss> siacTTrasfMisses) {
		this.siacTTrasfMisses = siacTTrasfMisses;
	}

	public SiacTTrasfMiss addSiacTTrasfMiss(SiacTTrasfMiss siacTTrasfMiss) {
		getSiacTTrasfMisses().add(siacTTrasfMiss);
		siacTTrasfMiss.setSiacTRichiestaEcon(this);

		return siacTTrasfMiss;
	}

	public SiacTTrasfMiss removeSiacTTrasfMiss(SiacTTrasfMiss siacTTrasfMiss) {
		getSiacTTrasfMisses().remove(siacTTrasfMiss);
		siacTTrasfMiss.setSiacTRichiestaEcon(null);

		return siacTTrasfMiss;
	}

	public List<SiacRRichiestaEconSubdoc> getSiacRRichiestaEconSubdocs() {
		return this.siacRRichiestaEconSubdocs;
	}

	public void setSiacRRichiestaEconSubdocs(List<SiacRRichiestaEconSubdoc> siacRRichiestaEconSubdocs) {
		this.siacRRichiestaEconSubdocs = siacRRichiestaEconSubdocs;
	}

	public SiacRRichiestaEconSubdoc addSiacRRichiestaEconSubdoc(SiacRRichiestaEconSubdoc siacRRichiestaEconSubdoc) {
		getSiacRRichiestaEconSubdocs().add(siacRRichiestaEconSubdoc);
		siacRRichiestaEconSubdoc.setSiacTRichiestaEcon(this);

		return siacRRichiestaEconSubdoc;
	}

	public SiacRRichiestaEconSubdoc removeSiacRRichiestaEconSubdoc(SiacRRichiestaEconSubdoc siacRRichiestaEconSubdoc) {
		getSiacRRichiestaEconSubdocs().remove(siacRRichiestaEconSubdoc);
		siacRRichiestaEconSubdoc.setSiacTRichiestaEcon(null);

		return siacRRichiestaEconSubdoc;
	}

	@Override
	public Integer getUid() {
		return riceconId;
	}

	@Override
	public void setUid(Integer uid) {
		this.riceconId = uid;
	}

}