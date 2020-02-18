/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

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
 * The persistent class for the siac_t_pdce_conto database table.
 * 
 */
@Entity
@Table(name="siac_t_pdce_conto")
@NamedQuery(name="SiacTPdceConto.findAll", query="SELECT s FROM SiacTPdceConto s")
public class SiacTPdceConto extends SiacTEnteBaseExt {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_T_PDCE_CONTO_PDCECONTOID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_PDCE_CONTO_PDCE_CONTO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_PDCE_CONTO_PDCECONTOID_GENERATOR")
	@Column(name="pdce_conto_id")
	private Integer pdceContoId;
	
	private Integer livello;

	private String ordine;

	@Column(name="pdce_conto_a_partita")
	private Boolean pdceContoAPartita;

	@Column(name="pdce_conto_code")
	private String pdceContoCode;
	
	@Column(name="pdce_conto_desc")
	private String pdceContoDesc;

	//bi-directional many-to-one association to SiacRCausaleEpPdceConto
	@OneToMany(mappedBy="siacTPdceConto")
	private List<SiacRCausaleEpPdceConto> siacRCausaleEpPdceContos;

	//bi-directional many-to-one association to SiacRPdceConto
	@OneToMany(mappedBy="siacTPdceConto1", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRPdceConto> siacRPdceContos1;

	//bi-directional many-to-one association to SiacRPdceConto
	@OneToMany(mappedBy="siacTPdceConto2")
	private List<SiacRPdceConto> siacRPdceContos2;

	//bi-directional many-to-one association to SiacRPdceContoAttr
	@OneToMany(mappedBy="siacTPdceConto", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRPdceContoAttr> siacRPdceContoAttrs;

	//bi-directional many-to-one association to SiacRPdceContoClass
	@OneToMany(mappedBy="siacTPdceConto", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRPdceContoClass> siacRPdceContoClasses;

	//bi-directional many-to-one association to SiacRPdceContoSoggetto
	@OneToMany(mappedBy="siacTPdceConto", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRPdceContoSoggetto> siacRPdceContoSoggettos;

	//bi-directional many-to-one association to SiacTMovEpDet
	@OneToMany(mappedBy="siacTPdceConto")
	private List<SiacTMovEpDet> siacTMovEpDets;

	//bi-directional many-to-one association to SiacDCespitiCategoria
	@ManyToOne
	@JoinColumn(name="cescat_id")
	private SiacDCespitiCategoria siacDCespitiCategoria;

	//bi-directional many-to-one association to SiacDPdceContoTipo
	@ManyToOne
	@JoinColumn(name="pdce_ct_tipo_id")
	private SiacDPdceContoTipo siacDPdceContoTipo;

	//bi-directional many-to-one association to SiacTPdceConto
	@ManyToOne
	@JoinColumn(name="pdce_conto_id_padre")
	private SiacTPdceConto siacTPdceConto;

	//bi-directional many-to-one association to SiacTPdceConto
	@OneToMany(mappedBy="siacTPdceConto")
	private List<SiacTPdceConto> siacTPdceContos;

	//bi-directional many-to-one association to SiacTPdceFamTree
	@ManyToOne
	@JoinColumn(name="pdce_fam_tree_id")
	private SiacTPdceFamTree siacTPdceFamTree;
	
	//bi-directional many-to-one association to SiacDAmbito
	@ManyToOne
	@JoinColumn(name="ambito_id")
	private SiacDAmbito siacDAmbito;
	
	//bi-directional many-to-one association to SiacTRegMovfin
	@OneToMany(mappedBy="siacTPdceConto")
	private List<SiacTRegMovfin> siacTRegMovfins;

	//bi-directional many-to-one association to SiacRConciliazioneBeneficiario
	@OneToMany(mappedBy="siacTPdceConto")
	private List<SiacRConciliazioneBeneficiario> siacRConciliazioneBeneficiarios;

	//bi-directional many-to-one association to SiacRConciliazioneCapitolo
	@OneToMany(mappedBy="siacTPdceConto")
	private List<SiacRConciliazioneCapitolo> siacRConciliazioneCapitolos;

	//bi-directional many-to-one association to SiacRConciliazioneTitolo
	@OneToMany(mappedBy="siacTPdceConto")
	private List<SiacRConciliazioneTitolo> siacRConciliazioneTitolos;

	public SiacTPdceConto() {
	}

	public Integer getPdceContoId() {
		return this.pdceContoId;
	}

	public void setPdceContoId(Integer pdceContoId) {
		this.pdceContoId = pdceContoId;
	}

	

	public Integer getLivello() {
		return this.livello;
	}

	public void setLivello(Integer livello) {
		this.livello = livello;
	}
	

	public String getOrdine() {
		return this.ordine;
	}

	public void setOrdine(String ordine) {
		this.ordine = ordine;
	}

	public Boolean getPdceContoAPartita() {
		return this.pdceContoAPartita;
	}

	public void setPdceContoAPartita(Boolean pdceContoAPartita) {
		this.pdceContoAPartita = pdceContoAPartita;
	}

	public String getPdceContoCode() {
		return this.pdceContoCode;
	}

	public void setPdceContoCode(String pdceContoCode) {
		this.pdceContoCode = pdceContoCode;
	}

	public String getPdceContoDesc() {
		return this.pdceContoDesc;
	}

	public void setPdceContoDesc(String pdceContoDesc) {
		this.pdceContoDesc = pdceContoDesc;
	}

	public List<SiacRCausaleEpPdceConto> getSiacRCausaleEpPdceContos() {
		return this.siacRCausaleEpPdceContos;
	}

	public void setSiacRCausaleEpPdceContos(List<SiacRCausaleEpPdceConto> siacRCausaleEpPdceContos) {
		this.siacRCausaleEpPdceContos = siacRCausaleEpPdceContos;
	}

	public SiacRCausaleEpPdceConto addSiacRCausaleEpPdceConto(SiacRCausaleEpPdceConto siacRCausaleEpPdceConto) {
		getSiacRCausaleEpPdceContos().add(siacRCausaleEpPdceConto);
		siacRCausaleEpPdceConto.setSiacTPdceConto(this);

		return siacRCausaleEpPdceConto;
	}

	public SiacRCausaleEpPdceConto removeSiacRCausaleEpPdceConto(SiacRCausaleEpPdceConto siacRCausaleEpPdceConto) {
		getSiacRCausaleEpPdceContos().remove(siacRCausaleEpPdceConto);
		siacRCausaleEpPdceConto.setSiacTPdceConto(null);

		return siacRCausaleEpPdceConto;
	}

	public List<SiacRPdceConto> getSiacRPdceContos1() {
		return this.siacRPdceContos1;
	}

	public void setSiacRPdceContos1(List<SiacRPdceConto> siacRPdceContos1) {
		this.siacRPdceContos1 = siacRPdceContos1;
	}

	public SiacRPdceConto addSiacRPdceContos1(SiacRPdceConto siacRPdceContos1) {
		getSiacRPdceContos1().add(siacRPdceContos1);
		siacRPdceContos1.setSiacTPdceConto1(this);

		return siacRPdceContos1;
	}

	public SiacRPdceConto removeSiacRPdceContos1(SiacRPdceConto siacRPdceContos1) {
		getSiacRPdceContos1().remove(siacRPdceContos1);
		siacRPdceContos1.setSiacTPdceConto1(null);

		return siacRPdceContos1;
	}

	public List<SiacRPdceConto> getSiacRPdceContos2() {
		return this.siacRPdceContos2;
	}

	public void setSiacRPdceContos2(List<SiacRPdceConto> siacRPdceContos2) {
		this.siacRPdceContos2 = siacRPdceContos2;
	}

	public SiacRPdceConto addSiacRPdceContos2(SiacRPdceConto siacRPdceContos2) {
		getSiacRPdceContos2().add(siacRPdceContos2);
		siacRPdceContos2.setSiacTPdceConto2(this);

		return siacRPdceContos2;
	}

	public SiacRPdceConto removeSiacRPdceContos2(SiacRPdceConto siacRPdceContos2) {
		getSiacRPdceContos2().remove(siacRPdceContos2);
		siacRPdceContos2.setSiacTPdceConto2(null);

		return siacRPdceContos2;
	}

	public List<SiacRPdceContoAttr> getSiacRPdceContoAttrs() {
		return this.siacRPdceContoAttrs;
	}

	public void setSiacRPdceContoAttrs(List<SiacRPdceContoAttr> siacRPdceContoAttrs) {
		this.siacRPdceContoAttrs = siacRPdceContoAttrs;
	}

	public SiacRPdceContoAttr addSiacRPdceContoAttr(SiacRPdceContoAttr siacRPdceContoAttr) {
		getSiacRPdceContoAttrs().add(siacRPdceContoAttr);
		siacRPdceContoAttr.setSiacTPdceConto(this);

		return siacRPdceContoAttr;
	}

	public SiacRPdceContoAttr removeSiacRPdceContoAttr(SiacRPdceContoAttr siacRPdceContoAttr) {
		getSiacRPdceContoAttrs().remove(siacRPdceContoAttr);
		siacRPdceContoAttr.setSiacTPdceConto(null);

		return siacRPdceContoAttr;
	}

	public List<SiacRPdceContoClass> getSiacRPdceContoClasses() {
		return this.siacRPdceContoClasses;
	}

	public void setSiacRPdceContoClasses(List<SiacRPdceContoClass> siacRPdceContoClasses) {
		this.siacRPdceContoClasses = siacRPdceContoClasses;
	}

	public SiacRPdceContoClass addSiacRPdceContoClass(SiacRPdceContoClass siacRPdceContoClass) {
		getSiacRPdceContoClasses().add(siacRPdceContoClass);
		siacRPdceContoClass.setSiacTPdceConto(this);

		return siacRPdceContoClass;
	}

	public SiacRPdceContoClass removeSiacRPdceContoClass(SiacRPdceContoClass siacRPdceContoClass) {
		getSiacRPdceContoClasses().remove(siacRPdceContoClass);
		siacRPdceContoClass.setSiacTPdceConto(null);

		return siacRPdceContoClass;
	}

	public List<SiacRPdceContoSoggetto> getSiacRPdceContoSoggettos() {
		return this.siacRPdceContoSoggettos;
	}

	public void setSiacRPdceContoSoggettos(List<SiacRPdceContoSoggetto> siacRPdceContoSoggettos) {
		this.siacRPdceContoSoggettos = siacRPdceContoSoggettos;
	}

	public SiacRPdceContoSoggetto addSiacRPdceContoSoggetto(SiacRPdceContoSoggetto siacRPdceContoSoggetto) {
		getSiacRPdceContoSoggettos().add(siacRPdceContoSoggetto);
		siacRPdceContoSoggetto.setSiacTPdceConto(this);

		return siacRPdceContoSoggetto;
	}

	public SiacRPdceContoSoggetto removeSiacRPdceContoSoggetto(SiacRPdceContoSoggetto siacRPdceContoSoggetto) {
		getSiacRPdceContoSoggettos().remove(siacRPdceContoSoggetto);
		siacRPdceContoSoggetto.setSiacTPdceConto(null);

		return siacRPdceContoSoggetto;
	}

	public List<SiacTMovEpDet> getSiacTMovEpDets() {
		return this.siacTMovEpDets;
	}

	public void setSiacTMovEpDets(List<SiacTMovEpDet> siacTMovEpDets) {
		this.siacTMovEpDets = siacTMovEpDets;
	}

	public SiacTMovEpDet addSiacTMovEpDet(SiacTMovEpDet siacTMovEpDet) {
		getSiacTMovEpDets().add(siacTMovEpDet);
		siacTMovEpDet.setSiacTPdceConto(this);

		return siacTMovEpDet;
	}

	public SiacTMovEpDet removeSiacTMovEpDet(SiacTMovEpDet siacTMovEpDet) {
		getSiacTMovEpDets().remove(siacTMovEpDet);
		siacTMovEpDet.setSiacTPdceConto(null);

		return siacTMovEpDet;
	}

	public SiacDCespitiCategoria getSiacDCespitiCategoria() {
		return this.siacDCespitiCategoria;
	}

	public void setSiacDCespitiCategoria(SiacDCespitiCategoria siacDCespitiCategoria) {
		this.siacDCespitiCategoria = siacDCespitiCategoria;
	}

	public SiacDPdceContoTipo getSiacDPdceContoTipo() {
		return this.siacDPdceContoTipo;
	}

	public void setSiacDPdceContoTipo(SiacDPdceContoTipo siacDPdceContoTipo) {
		this.siacDPdceContoTipo = siacDPdceContoTipo;
	}

	public SiacTPdceConto getSiacTPdceConto() {
		return this.siacTPdceConto;
	}

	public void setSiacTPdceConto(SiacTPdceConto siacTPdceConto) {
		this.siacTPdceConto = siacTPdceConto;
	}

	public List<SiacTPdceConto> getSiacTPdceContos() {
		return this.siacTPdceContos;
	}

	public void setSiacTPdceContos(List<SiacTPdceConto> siacTPdceContos) {
		this.siacTPdceContos = siacTPdceContos;
	}

	public SiacTPdceConto addSiacTPdceConto(SiacTPdceConto siacTPdceConto) {
		getSiacTPdceContos().add(siacTPdceConto);
		siacTPdceConto.setSiacTPdceConto(this);

		return siacTPdceConto;
	}

	public SiacTPdceConto removeSiacTPdceConto(SiacTPdceConto siacTPdceConto) {
		getSiacTPdceContos().remove(siacTPdceConto);
		siacTPdceConto.setSiacTPdceConto(null);

		return siacTPdceConto;
	}

	public SiacTPdceFamTree getSiacTPdceFamTree() {
		return this.siacTPdceFamTree;
	}

	public void setSiacTPdceFamTree(SiacTPdceFamTree siacTPdceFamTree) {
		this.siacTPdceFamTree = siacTPdceFamTree;
	}
	
	public SiacDAmbito getSiacDAmbito() {
		return siacDAmbito;
	}

	public void setSiacDAmbito(SiacDAmbito siacDAmbito) {
		this.siacDAmbito = siacDAmbito;
	}
	
	public List<SiacTRegMovfin> getSiacTRegMovfins() {
		return this.siacTRegMovfins;
	}

	public void setSiacTRegMovfins(List<SiacTRegMovfin> siacTRegMovfins) {
		this.siacTRegMovfins = siacTRegMovfins;
	}

	public SiacTRegMovfin addSiacTRegMovfin(SiacTRegMovfin siacTRegMovfin) {
		getSiacTRegMovfins().add(siacTRegMovfin);
		siacTRegMovfin.setSiacTPdceConto(this);

		return siacTRegMovfin;
	}

	public SiacTRegMovfin removeSiacTRegMovfin(SiacTRegMovfin siacTRegMovfin) {
		getSiacTRegMovfins().remove(siacTRegMovfin);
		siacTRegMovfin.setSiacTPdceConto(null);

		return siacTRegMovfin;
	}

	public List<SiacRConciliazioneBeneficiario> getSiacRConciliazioneBeneficiarios() {
		return this.siacRConciliazioneBeneficiarios;
	}

	public void setSiacRConciliazioneBeneficiarios(List<SiacRConciliazioneBeneficiario> siacRConciliazioneBeneficiarios) {
		this.siacRConciliazioneBeneficiarios = siacRConciliazioneBeneficiarios;
	}

	public SiacRConciliazioneBeneficiario addSiacRConciliazioneBeneficiario(SiacRConciliazioneBeneficiario siacRConciliazioneBeneficiario) {
		getSiacRConciliazioneBeneficiarios().add(siacRConciliazioneBeneficiario);
		siacRConciliazioneBeneficiario.setSiacTPdceConto(this);

		return siacRConciliazioneBeneficiario;
	}

	public SiacRConciliazioneBeneficiario removeSiacRConciliazioneBeneficiario(SiacRConciliazioneBeneficiario siacRConciliazioneBeneficiario) {
		getSiacRConciliazioneBeneficiarios().remove(siacRConciliazioneBeneficiario);
		siacRConciliazioneBeneficiario.setSiacTPdceConto(null);

		return siacRConciliazioneBeneficiario;
	}

	public List<SiacRConciliazioneCapitolo> getSiacRConciliazioneCapitolos() {
		return this.siacRConciliazioneCapitolos;
	}

	public void setSiacRConciliazioneCapitolos(List<SiacRConciliazioneCapitolo> siacRConciliazioneCapitolos) {
		this.siacRConciliazioneCapitolos = siacRConciliazioneCapitolos;
	}

	public SiacRConciliazioneCapitolo addSiacRConciliazioneCapitolo(SiacRConciliazioneCapitolo siacRConciliazioneCapitolo) {
		getSiacRConciliazioneCapitolos().add(siacRConciliazioneCapitolo);
		siacRConciliazioneCapitolo.setSiacTPdceConto(this);

		return siacRConciliazioneCapitolo;
	}

	public SiacRConciliazioneCapitolo removeSiacRConciliazioneCapitolo(SiacRConciliazioneCapitolo siacRConciliazioneCapitolo) {
		getSiacRConciliazioneCapitolos().remove(siacRConciliazioneCapitolo);
		siacRConciliazioneCapitolo.setSiacTPdceConto(null);

		return siacRConciliazioneCapitolo;
	}

	public List<SiacRConciliazioneTitolo> getSiacRConciliazioneTitolos() {
		return this.siacRConciliazioneTitolos;
	}

	public void setSiacRConciliazioneTitolos(List<SiacRConciliazioneTitolo> siacRConciliazioneTitolos) {
		this.siacRConciliazioneTitolos = siacRConciliazioneTitolos;
	}

	public SiacRConciliazioneTitolo addSiacRConciliazioneTitolo(SiacRConciliazioneTitolo siacRConciliazioneTitolo) {
		getSiacRConciliazioneTitolos().add(siacRConciliazioneTitolo);
		siacRConciliazioneTitolo.setSiacTPdceConto(this);

		return siacRConciliazioneTitolo;
	}

	public SiacRConciliazioneTitolo removeSiacRConciliazioneTitolo(SiacRConciliazioneTitolo siacRConciliazioneTitolo) {
		getSiacRConciliazioneTitolos().remove(siacRConciliazioneTitolo);
		siacRConciliazioneTitolo.setSiacTPdceConto(null);

		return siacRConciliazioneTitolo;
	}

	@Override
	public Integer getUid() {
		return pdceContoId;
	}

	@Override
	public void setUid(Integer uid) {
		this.pdceContoId = uid;
	}

}