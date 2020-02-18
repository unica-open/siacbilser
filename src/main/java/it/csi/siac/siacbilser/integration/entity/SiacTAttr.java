/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

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


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_t_attr database table.
 * 
 */
@Entity
@Table(name="siac_t_attr")
public class SiacTAttr extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The attr id. */
	@Id
	@SequenceGenerator(name="SIAC_T_ATTR_ATTRID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_ATTR_ATTR_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_ATTR_ATTRID_GENERATOR")
	@Column(name="attr_id")
	private Integer attrId;

	/** The attr code. */
	@Column(name="attr_code")
	private String attrCode;

	/** The attr desc. */
	@Column(name="attr_desc")
	private String attrDesc;

	/** The tabella nome. */
	@Column(name="tabella_nome")
	private String tabellaNome;

	//bi-directional many-to-one association to SiacRAttrBilElemTipo
	/** The siac r attr bil elem tipos. */
	@OneToMany(mappedBy="siacTAttr")
	private List<SiacRAttrBilElemTipo> siacRAttrBilElemTipos;

	//bi-directional many-to-one association to SiacRAttrClassTipo
	/** The siac r attr class tipos. */
	@OneToMany(mappedBy="siacTAttr")
	private List<SiacRAttrClassTipo> siacRAttrClassTipos;

	//bi-directional many-to-one association to SiacRAttrEntita
	/** The siac r attr entitas. */
	@OneToMany(mappedBy="siacTAttr")
	private List<SiacRAttrEntita> siacRAttrEntitas;

	//bi-directional many-to-one association to SiacRBilElemAttr
	/** The siac r bil elem attrs. */
	@OneToMany(mappedBy="siacTAttr")
	private List<SiacRBilElemAttr> siacRBilElemAttrs;

	//bi-directional many-to-one association to SiacRBilElemTipoAttrIdElemCode
	@OneToMany(mappedBy="siacTAttr")
	private List<SiacRBilElemTipoAttrIdElemCode> siacRBilElemTipoAttrIdElemCodes;

	//bi-directional many-to-one association to SiacRCartacontAttr
	@OneToMany(mappedBy="siacTAttr")
	private List<SiacRCartacontAttr> siacRCartacontAttrs;

	//bi-directional many-to-one association to SiacRCartacontDetAttr
	@OneToMany(mappedBy="siacTAttr")
	private List<SiacRCartacontDetAttr> siacRCartacontDetAttrs;

	//bi-directional many-to-one association to SiacRCartacontEsteraAttr
	@OneToMany(mappedBy="siacTAttr")
	private List<SiacRCartacontEsteraAttr> siacRCartacontEsteraAttrs;

	//bi-directional many-to-one association to SiacRClassAttr
	/** The siac r class attrs. */
	@OneToMany(mappedBy="siacTAttr")
	private List<SiacRClassAttr> siacRClassAttrs;

	//bi-directional many-to-one association to SiacRCronopAttr
	@OneToMany(mappedBy="siacTAttr")
	private List<SiacRCronopAttr> siacRCronopAttrs;

	//bi-directional many-to-one association to SiacRDocAttr
	@OneToMany(mappedBy="siacTAttr")
	private List<SiacRDocAttr> siacRDocAttrs;

	//bi-directional many-to-one association to SiacRDocTipoAttr
	@OneToMany(mappedBy="siacTAttr")
	private List<SiacRDocTipoAttr> siacRDocTipoAttrs;

	//bi-directional many-to-one association to SiacRIvaAttAttr
	@OneToMany(mappedBy="siacTAttr")
	private List<SiacRIvaAttAttr> siacRIvaAttAttrs;

	//bi-directional many-to-one association to SiacRLiquidazioneAttr
	@OneToMany(mappedBy="siacTAttr")
	private List<SiacRLiquidazioneAttr> siacRLiquidazioneAttrs;

	//bi-directional many-to-one association to SiacRMovgestTsAttr
	/** The siac r movgest ts attrs. */
	@OneToMany(mappedBy="siacTAttr")
	private List<SiacRMovgestTsAttr> siacRMovgestTsAttrs;

	//bi-directional many-to-one association to SiacROnereAttr
	@OneToMany(mappedBy="siacTAttr")
	private List<SiacROnereAttr> siacROnereAttrs;

	//bi-directional many-to-one association to SiacROrdinativoAttr
	@OneToMany(mappedBy="siacTAttr")
	private List<SiacROrdinativoAttr> siacROrdinativoAttrs;

	//bi-directional many-to-one association to SiacRProgrammaAttr
	@OneToMany(mappedBy="siacTAttr")
	private List<SiacRProgrammaAttr> siacRProgrammaAttrs;

	//bi-directional many-to-one association to SiacRSoggettoAttr
	@OneToMany(mappedBy="siacTAttr")
	private List<SiacRSoggettoAttr> siacRSoggettoAttrs;

	//bi-directional many-to-one association to SiacRSoggettoAttrMod
	@OneToMany(mappedBy="siacTAttr")
	private List<SiacRSoggettoAttrMod> siacRSoggettoAttrMods;

	//bi-directional many-to-one association to SiacRSubdocAttr
	@OneToMany(mappedBy="siacTAttr")
	private List<SiacRSubdocAttr> siacRSubdocAttrs;

	//bi-directional many-to-one association to SiacRSubdocIvaAttr
	@OneToMany(mappedBy="siacTAttr")
	private List<SiacRSubdocIvaAttr> siacRSubdocIvaAttrs;

	//bi-directional many-to-one association to SiacRSubdocTipoAttr
	@OneToMany(mappedBy="siacTAttr")
	private List<SiacRSubdocTipoAttr> siacRSubdocTipoAttrs;

	//bi-directional many-to-one association to SiacRVariazioneAttr
	/** The siac r variazione attrs. */
	@OneToMany(mappedBy="siacTAttr")
	private List<SiacRVariazioneAttr> siacRVariazioneAttrs;

	//bi-directional many-to-one association to SiacRVincoloAttr
	/** The siac r vincolo attrs. */
	@OneToMany(mappedBy="siacTAttr")
	private List<SiacRVincoloAttr> siacRVincoloAttrs;

	//bi-directional many-to-one association to SiacDAttrTipo
	/** The siac d attr tipo. */
	@ManyToOne
	@JoinColumn(name="attr_tipo_id")
	private SiacDAttrTipo siacDAttrTipo;

	/**
	 * Instantiates a new siac t attr.
	 */
	public SiacTAttr() {
	}

	/**
	 * Gets the attr id.
	 *
	 * @return the attr id
	 */
	public Integer getAttrId() {
		return this.attrId;
	}

	/**
	 * Sets the attr id.
	 *
	 * @param attrId the new attr id
	 */
	public void setAttrId(Integer attrId) {
		this.attrId = attrId;
	}

	/**
	 * Gets the attr code.
	 *
	 * @return the attr code
	 */
	public String getAttrCode() {
		return this.attrCode;
	}

	/**
	 * Sets the attr code.
	 *
	 * @param attrCode the new attr code
	 */
	public void setAttrCode(String attrCode) {
		this.attrCode = attrCode;
	}

	/**
	 * Gets the attr desc.
	 *
	 * @return the attr desc
	 */
	public String getAttrDesc() {
		return this.attrDesc;
	}

	/**
	 * Sets the attr desc.
	 *
	 * @param attrDesc the new attr desc
	 */
	public void setAttrDesc(String attrDesc) {
		this.attrDesc = attrDesc;
	}

	/**
	 * Gets the tabella nome.
	 *
	 * @return the tabella nome
	 */
	public String getTabellaNome() {
		return this.tabellaNome;
	}

	/**
	 * Sets the tabella nome.
	 *
	 * @param tabellaNome the new tabella nome
	 */
	public void setTabellaNome(String tabellaNome) {
		this.tabellaNome = tabellaNome;
	}

	/**
	 * Gets the siac r attr bil elem tipos.
	 *
	 * @return the siac r attr bil elem tipos
	 */
	public List<SiacRAttrBilElemTipo> getSiacRAttrBilElemTipos() {
		return this.siacRAttrBilElemTipos;
	}

	/**
	 * Sets the siac r attr bil elem tipos.
	 *
	 * @param siacRAttrBilElemTipos the new siac r attr bil elem tipos
	 */
	public void setSiacRAttrBilElemTipos(List<SiacRAttrBilElemTipo> siacRAttrBilElemTipos) {
		this.siacRAttrBilElemTipos = siacRAttrBilElemTipos;
	}

	/**
	 * Adds the siac r attr bil elem tipo.
	 *
	 * @param siacRAttrBilElemTipo the siac r attr bil elem tipo
	 * @return the siac r attr bil elem tipo
	 */
	public SiacRAttrBilElemTipo addSiacRAttrBilElemTipo(SiacRAttrBilElemTipo siacRAttrBilElemTipo) {
		getSiacRAttrBilElemTipos().add(siacRAttrBilElemTipo);
		siacRAttrBilElemTipo.setSiacTAttr(this);

		return siacRAttrBilElemTipo;
	}

	/**
	 * Removes the siac r attr bil elem tipo.
	 *
	 * @param siacRAttrBilElemTipo the siac r attr bil elem tipo
	 * @return the siac r attr bil elem tipo
	 */
	public SiacRAttrBilElemTipo removeSiacRAttrBilElemTipo(SiacRAttrBilElemTipo siacRAttrBilElemTipo) {
		getSiacRAttrBilElemTipos().remove(siacRAttrBilElemTipo);
		siacRAttrBilElemTipo.setSiacTAttr(null);

		return siacRAttrBilElemTipo;
	}

	/**
	 * Gets the siac r attr class tipos.
	 *
	 * @return the siac r attr class tipos
	 */
	public List<SiacRAttrClassTipo> getSiacRAttrClassTipos() {
		return this.siacRAttrClassTipos;
	}

	/**
	 * Sets the siac r attr class tipos.
	 *
	 * @param siacRAttrClassTipos the new siac r attr class tipos
	 */
	public void setSiacRAttrClassTipos(List<SiacRAttrClassTipo> siacRAttrClassTipos) {
		this.siacRAttrClassTipos = siacRAttrClassTipos;
	}

	/**
	 * Adds the siac r attr class tipo.
	 *
	 * @param siacRAttrClassTipo the siac r attr class tipo
	 * @return the siac r attr class tipo
	 */
	public SiacRAttrClassTipo addSiacRAttrClassTipo(SiacRAttrClassTipo siacRAttrClassTipo) {
		getSiacRAttrClassTipos().add(siacRAttrClassTipo);
		siacRAttrClassTipo.setSiacTAttr(this);

		return siacRAttrClassTipo;
	}

	/**
	 * Removes the siac r attr class tipo.
	 *
	 * @param siacRAttrClassTipo the siac r attr class tipo
	 * @return the siac r attr class tipo
	 */
	public SiacRAttrClassTipo removeSiacRAttrClassTipo(SiacRAttrClassTipo siacRAttrClassTipo) {
		getSiacRAttrClassTipos().remove(siacRAttrClassTipo);
		siacRAttrClassTipo.setSiacTAttr(null);

		return siacRAttrClassTipo;
	}

	/**
	 * Gets the siac r attr entitas.
	 *
	 * @return the siac r attr entitas
	 */
	public List<SiacRAttrEntita> getSiacRAttrEntitas() {
		return this.siacRAttrEntitas;
	}

	/**
	 * Sets the siac r attr entitas.
	 *
	 * @param siacRAttrEntitas the new siac r attr entitas
	 */
	public void setSiacRAttrEntitas(List<SiacRAttrEntita> siacRAttrEntitas) {
		this.siacRAttrEntitas = siacRAttrEntitas;
	}

	/**
	 * Adds the siac r attr entita.
	 *
	 * @param siacRAttrEntita the siac r attr entita
	 * @return the siac r attr entita
	 */
	public SiacRAttrEntita addSiacRAttrEntita(SiacRAttrEntita siacRAttrEntita) {
		getSiacRAttrEntitas().add(siacRAttrEntita);
		siacRAttrEntita.setSiacTAttr(this);

		return siacRAttrEntita;
	}

	/**
	 * Removes the siac r attr entita.
	 *
	 * @param siacRAttrEntita the siac r attr entita
	 * @return the siac r attr entita
	 */
	public SiacRAttrEntita removeSiacRAttrEntita(SiacRAttrEntita siacRAttrEntita) {
		getSiacRAttrEntitas().remove(siacRAttrEntita);
		siacRAttrEntita.setSiacTAttr(null);

		return siacRAttrEntita;
	}

	/**
	 * Gets the siac r bil elem attrs.
	 *
	 * @return the siac r bil elem attrs
	 */
	public List<SiacRBilElemAttr> getSiacRBilElemAttrs() {
		return this.siacRBilElemAttrs;
	}

	/**
	 * Sets the siac r bil elem attrs.
	 *
	 * @param siacRBilElemAttrs the new siac r bil elem attrs
	 */
	public void setSiacRBilElemAttrs(List<SiacRBilElemAttr> siacRBilElemAttrs) {
		this.siacRBilElemAttrs = siacRBilElemAttrs;
	}

	/**
	 * Adds the siac r bil elem attr.
	 *
	 * @param siacRBilElemAttr the siac r bil elem attr
	 * @return the siac r bil elem attr
	 */
	public SiacRBilElemAttr addSiacRBilElemAttr(SiacRBilElemAttr siacRBilElemAttr) {
		getSiacRBilElemAttrs().add(siacRBilElemAttr);
		siacRBilElemAttr.setSiacTAttr(this);

		return siacRBilElemAttr;
	}

	/**
	 * Removes the siac r bil elem attr.
	 *
	 * @param siacRBilElemAttr the siac r bil elem attr
	 * @return the siac r bil elem attr
	 */
	public SiacRBilElemAttr removeSiacRBilElemAttr(SiacRBilElemAttr siacRBilElemAttr) {
		getSiacRBilElemAttrs().remove(siacRBilElemAttr);
		siacRBilElemAttr.setSiacTAttr(null);

		return siacRBilElemAttr;
	}

	public List<SiacRBilElemTipoAttrIdElemCode> getSiacRBilElemTipoAttrIdElemCodes() {
		return this.siacRBilElemTipoAttrIdElemCodes;
	}

	public void setSiacRBilElemTipoAttrIdElemCodes(List<SiacRBilElemTipoAttrIdElemCode> siacRBilElemTipoAttrIdElemCodes) {
		this.siacRBilElemTipoAttrIdElemCodes = siacRBilElemTipoAttrIdElemCodes;
	}

	public SiacRBilElemTipoAttrIdElemCode addSiacRBilElemTipoAttrIdElemCode(SiacRBilElemTipoAttrIdElemCode siacRBilElemTipoAttrIdElemCode) {
		getSiacRBilElemTipoAttrIdElemCodes().add(siacRBilElemTipoAttrIdElemCode);
		siacRBilElemTipoAttrIdElemCode.setSiacTAttr(this);

		return siacRBilElemTipoAttrIdElemCode;
	}

	public SiacRBilElemTipoAttrIdElemCode removeSiacRBilElemTipoAttrIdElemCode(SiacRBilElemTipoAttrIdElemCode siacRBilElemTipoAttrIdElemCode) {
		getSiacRBilElemTipoAttrIdElemCodes().remove(siacRBilElemTipoAttrIdElemCode);
		siacRBilElemTipoAttrIdElemCode.setSiacTAttr(null);

		return siacRBilElemTipoAttrIdElemCode;
	}

	public List<SiacRCartacontAttr> getSiacRCartacontAttrs() {
		return this.siacRCartacontAttrs;
	}

	public void setSiacRCartacontAttrs(List<SiacRCartacontAttr> siacRCartacontAttrs) {
		this.siacRCartacontAttrs = siacRCartacontAttrs;
	}

	public SiacRCartacontAttr addSiacRCartacontAttr(SiacRCartacontAttr siacRCartacontAttr) {
		getSiacRCartacontAttrs().add(siacRCartacontAttr);
		siacRCartacontAttr.setSiacTAttr(this);

		return siacRCartacontAttr;
	}

	public SiacRCartacontAttr removeSiacRCartacontAttr(SiacRCartacontAttr siacRCartacontAttr) {
		getSiacRCartacontAttrs().remove(siacRCartacontAttr);
		siacRCartacontAttr.setSiacTAttr(null);

		return siacRCartacontAttr;
	}

	public List<SiacRCartacontDetAttr> getSiacRCartacontDetAttrs() {
		return this.siacRCartacontDetAttrs;
	}

	public void setSiacRCartacontDetAttrs(List<SiacRCartacontDetAttr> siacRCartacontDetAttrs) {
		this.siacRCartacontDetAttrs = siacRCartacontDetAttrs;
	}

	public SiacRCartacontDetAttr addSiacRCartacontDetAttr(SiacRCartacontDetAttr siacRCartacontDetAttr) {
		getSiacRCartacontDetAttrs().add(siacRCartacontDetAttr);
		siacRCartacontDetAttr.setSiacTAttr(this);

		return siacRCartacontDetAttr;
	}

	public SiacRCartacontDetAttr removeSiacRCartacontDetAttr(SiacRCartacontDetAttr siacRCartacontDetAttr) {
		getSiacRCartacontDetAttrs().remove(siacRCartacontDetAttr);
		siacRCartacontDetAttr.setSiacTAttr(null);

		return siacRCartacontDetAttr;
	}

	public List<SiacRCartacontEsteraAttr> getSiacRCartacontEsteraAttrs() {
		return this.siacRCartacontEsteraAttrs;
	}

	public void setSiacRCartacontEsteraAttrs(List<SiacRCartacontEsteraAttr> siacRCartacontEsteraAttrs) {
		this.siacRCartacontEsteraAttrs = siacRCartacontEsteraAttrs;
	}

	public SiacRCartacontEsteraAttr addSiacRCartacontEsteraAttr(SiacRCartacontEsteraAttr siacRCartacontEsteraAttr) {
		getSiacRCartacontEsteraAttrs().add(siacRCartacontEsteraAttr);
		siacRCartacontEsteraAttr.setSiacTAttr(this);

		return siacRCartacontEsteraAttr;
	}

	public SiacRCartacontEsteraAttr removeSiacRCartacontEsteraAttr(SiacRCartacontEsteraAttr siacRCartacontEsteraAttr) {
		getSiacRCartacontEsteraAttrs().remove(siacRCartacontEsteraAttr);
		siacRCartacontEsteraAttr.setSiacTAttr(null);

		return siacRCartacontEsteraAttr;
	}

	/**
	 * Gets the siac r class attrs.
	 *
	 * @return the siac r class attrs
	 */
	public List<SiacRClassAttr> getSiacRClassAttrs() {
		return this.siacRClassAttrs;
	}

	/**
	 * Sets the siac r class attrs.
	 *
	 * @param siacRClassAttrs the new siac r class attrs
	 */
	public void setSiacRClassAttrs(List<SiacRClassAttr> siacRClassAttrs) {
		this.siacRClassAttrs = siacRClassAttrs;
	}

	/**
	 * Adds the siac r class attr.
	 *
	 * @param siacRClassAttr the siac r class attr
	 * @return the siac r class attr
	 */
	public SiacRClassAttr addSiacRClassAttr(SiacRClassAttr siacRClassAttr) {
		getSiacRClassAttrs().add(siacRClassAttr);
		siacRClassAttr.setSiacTAttr(this);

		return siacRClassAttr;
	}

	/**
	 * Removes the siac r class attr.
	 *
	 * @param siacRClassAttr the siac r class attr
	 * @return the siac r class attr
	 */
	public SiacRClassAttr removeSiacRClassAttr(SiacRClassAttr siacRClassAttr) {
		getSiacRClassAttrs().remove(siacRClassAttr);
		siacRClassAttr.setSiacTAttr(null);

		return siacRClassAttr;
	}

	public List<SiacRCronopAttr> getSiacRCronopAttrs() {
		return this.siacRCronopAttrs;
	}

	public void setSiacRCronopAttrs(List<SiacRCronopAttr> siacRCronopAttrs) {
		this.siacRCronopAttrs = siacRCronopAttrs;
	}

	public SiacRCronopAttr addSiacRCronopAttr(SiacRCronopAttr siacRCronopAttr) {
		getSiacRCronopAttrs().add(siacRCronopAttr);
		siacRCronopAttr.setSiacTAttr(this);

		return siacRCronopAttr;
	}

	public SiacRCronopAttr removeSiacRCronopAttr(SiacRCronopAttr siacRCronopAttr) {
		getSiacRCronopAttrs().remove(siacRCronopAttr);
		siacRCronopAttr.setSiacTAttr(null);

		return siacRCronopAttr;
	}

	public List<SiacRDocAttr> getSiacRDocAttrs() {
		return this.siacRDocAttrs;
	}

	public void setSiacRDocAttrs(List<SiacRDocAttr> siacRDocAttrs) {
		this.siacRDocAttrs = siacRDocAttrs;
	}

	public SiacRDocAttr addSiacRDocAttr(SiacRDocAttr siacRDocAttr) {
		getSiacRDocAttrs().add(siacRDocAttr);
		siacRDocAttr.setSiacTAttr(this);

		return siacRDocAttr;
	}

	public SiacRDocAttr removeSiacRDocAttr(SiacRDocAttr siacRDocAttr) {
		getSiacRDocAttrs().remove(siacRDocAttr);
		siacRDocAttr.setSiacTAttr(null);

		return siacRDocAttr;
	}

	public List<SiacRDocTipoAttr> getSiacRDocTipoAttrs() {
		return this.siacRDocTipoAttrs;
	}

	public void setSiacRDocTipoAttrs(List<SiacRDocTipoAttr> siacRDocTipoAttrs) {
		this.siacRDocTipoAttrs = siacRDocTipoAttrs;
	}

	public SiacRDocTipoAttr addSiacRDocTipoAttr(SiacRDocTipoAttr siacRDocTipoAttr) {
		getSiacRDocTipoAttrs().add(siacRDocTipoAttr);
		siacRDocTipoAttr.setSiacTAttr(this);

		return siacRDocTipoAttr;
	}

	public SiacRDocTipoAttr removeSiacRDocTipoAttr(SiacRDocTipoAttr siacRDocTipoAttr) {
		getSiacRDocTipoAttrs().remove(siacRDocTipoAttr);
		siacRDocTipoAttr.setSiacTAttr(null);

		return siacRDocTipoAttr;
	}

	public List<SiacRIvaAttAttr> getSiacRIvaAttAttrs() {
		return this.siacRIvaAttAttrs;
	}

	public void setSiacRIvaAttAttrs(List<SiacRIvaAttAttr> siacRIvaAttAttrs) {
		this.siacRIvaAttAttrs = siacRIvaAttAttrs;
	}

	public SiacRIvaAttAttr addSiacRIvaAttAttr(SiacRIvaAttAttr siacRIvaAttAttr) {
		getSiacRIvaAttAttrs().add(siacRIvaAttAttr);
		siacRIvaAttAttr.setSiacTAttr(this);

		return siacRIvaAttAttr;
	}

	public SiacRIvaAttAttr removeSiacRIvaAttAttr(SiacRIvaAttAttr siacRIvaAttAttr) {
		getSiacRIvaAttAttrs().remove(siacRIvaAttAttr);
		siacRIvaAttAttr.setSiacTAttr(null);

		return siacRIvaAttAttr;
	}

	public List<SiacRLiquidazioneAttr> getSiacRLiquidazioneAttrs() {
		return this.siacRLiquidazioneAttrs;
	}

	public void setSiacRLiquidazioneAttrs(List<SiacRLiquidazioneAttr> siacRLiquidazioneAttrs) {
		this.siacRLiquidazioneAttrs = siacRLiquidazioneAttrs;
	}

	public SiacRLiquidazioneAttr addSiacRLiquidazioneAttr(SiacRLiquidazioneAttr siacRLiquidazioneAttr) {
		getSiacRLiquidazioneAttrs().add(siacRLiquidazioneAttr);
		siacRLiquidazioneAttr.setSiacTAttr(this);

		return siacRLiquidazioneAttr;
	}

	public SiacRLiquidazioneAttr removeSiacRLiquidazioneAttr(SiacRLiquidazioneAttr siacRLiquidazioneAttr) {
		getSiacRLiquidazioneAttrs().remove(siacRLiquidazioneAttr);
		siacRLiquidazioneAttr.setSiacTAttr(null);

		return siacRLiquidazioneAttr;
	}

	/**
	 * Gets the siac r movgest ts attrs.
	 *
	 * @return the siac r movgest ts attrs
	 */
	public List<SiacRMovgestTsAttr> getSiacRMovgestTsAttrs() {
		return this.siacRMovgestTsAttrs;
	}

	/**
	 * Sets the siac r movgest ts attrs.
	 *
	 * @param siacRMovgestTsAttrs the new siac r movgest ts attrs
	 */
	public void setSiacRMovgestTsAttrs(List<SiacRMovgestTsAttr> siacRMovgestTsAttrs) {
		this.siacRMovgestTsAttrs = siacRMovgestTsAttrs;
	}

	/**
	 * Adds the siac r movgest ts attr.
	 *
	 * @param siacRMovgestTsAttr the siac r movgest ts attr
	 * @return the siac r movgest ts attr
	 */
	public SiacRMovgestTsAttr addSiacRMovgestTsAttr(SiacRMovgestTsAttr siacRMovgestTsAttr) {
		getSiacRMovgestTsAttrs().add(siacRMovgestTsAttr);
		siacRMovgestTsAttr.setSiacTAttr(this);

		return siacRMovgestTsAttr;
	}

	/**
	 * Removes the siac r movgest ts attr.
	 *
	 * @param siacRMovgestTsAttr the siac r movgest ts attr
	 * @return the siac r movgest ts attr
	 */
	public SiacRMovgestTsAttr removeSiacRMovgestTsAttr(SiacRMovgestTsAttr siacRMovgestTsAttr) {
		getSiacRMovgestTsAttrs().remove(siacRMovgestTsAttr);
		siacRMovgestTsAttr.setSiacTAttr(null);

		return siacRMovgestTsAttr;
	}

	public List<SiacROnereAttr> getSiacROnereAttrs() {
		return this.siacROnereAttrs;
	}

	public void setSiacROnereAttrs(List<SiacROnereAttr> siacROnereAttrs) {
		this.siacROnereAttrs = siacROnereAttrs;
	}

	public SiacROnereAttr addSiacROnereAttr(SiacROnereAttr siacROnereAttr) {
		getSiacROnereAttrs().add(siacROnereAttr);
		siacROnereAttr.setSiacTAttr(this);

		return siacROnereAttr;
	}

	public SiacROnereAttr removeSiacROnereAttr(SiacROnereAttr siacROnereAttr) {
		getSiacROnereAttrs().remove(siacROnereAttr);
		siacROnereAttr.setSiacTAttr(null);

		return siacROnereAttr;
	}

	public List<SiacROrdinativoAttr> getSiacROrdinativoAttrs() {
		return this.siacROrdinativoAttrs;
	}

	public void setSiacROrdinativoAttrs(List<SiacROrdinativoAttr> siacROrdinativoAttrs) {
		this.siacROrdinativoAttrs = siacROrdinativoAttrs;
	}

	public SiacROrdinativoAttr addSiacROrdinativoAttr(SiacROrdinativoAttr siacROrdinativoAttr) {
		getSiacROrdinativoAttrs().add(siacROrdinativoAttr);
		siacROrdinativoAttr.setSiacTAttr(this);

		return siacROrdinativoAttr;
	}

	public SiacROrdinativoAttr removeSiacROrdinativoAttr(SiacROrdinativoAttr siacROrdinativoAttr) {
		getSiacROrdinativoAttrs().remove(siacROrdinativoAttr);
		siacROrdinativoAttr.setSiacTAttr(null);

		return siacROrdinativoAttr;
	}

	public List<SiacRProgrammaAttr> getSiacRProgrammaAttrs() {
		return this.siacRProgrammaAttrs;
	}

	public void setSiacRProgrammaAttrs(List<SiacRProgrammaAttr> siacRProgrammaAttrs) {
		this.siacRProgrammaAttrs = siacRProgrammaAttrs;
	}

	public SiacRProgrammaAttr addSiacRProgrammaAttr(SiacRProgrammaAttr siacRProgrammaAttr) {
		getSiacRProgrammaAttrs().add(siacRProgrammaAttr);
		siacRProgrammaAttr.setSiacTAttr(this);

		return siacRProgrammaAttr;
	}

	public SiacRProgrammaAttr removeSiacRProgrammaAttr(SiacRProgrammaAttr siacRProgrammaAttr) {
		getSiacRProgrammaAttrs().remove(siacRProgrammaAttr);
		siacRProgrammaAttr.setSiacTAttr(null);

		return siacRProgrammaAttr;
	}

	public List<SiacRSoggettoAttr> getSiacRSoggettoAttrs() {
		return this.siacRSoggettoAttrs;
	}

	public void setSiacRSoggettoAttrs(List<SiacRSoggettoAttr> siacRSoggettoAttrs) {
		this.siacRSoggettoAttrs = siacRSoggettoAttrs;
	}

	public SiacRSoggettoAttr addSiacRSoggettoAttr(SiacRSoggettoAttr siacRSoggettoAttr) {
		getSiacRSoggettoAttrs().add(siacRSoggettoAttr);
		siacRSoggettoAttr.setSiacTAttr(this);

		return siacRSoggettoAttr;
	}

	public SiacRSoggettoAttr removeSiacRSoggettoAttr(SiacRSoggettoAttr siacRSoggettoAttr) {
		getSiacRSoggettoAttrs().remove(siacRSoggettoAttr);
		siacRSoggettoAttr.setSiacTAttr(null);

		return siacRSoggettoAttr;
	}

	public List<SiacRSoggettoAttrMod> getSiacRSoggettoAttrMods() {
		return this.siacRSoggettoAttrMods;
	}

	public void setSiacRSoggettoAttrMods(List<SiacRSoggettoAttrMod> siacRSoggettoAttrMods) {
		this.siacRSoggettoAttrMods = siacRSoggettoAttrMods;
	}

	public SiacRSoggettoAttrMod addSiacRSoggettoAttrMod(SiacRSoggettoAttrMod siacRSoggettoAttrMod) {
		getSiacRSoggettoAttrMods().add(siacRSoggettoAttrMod);
		siacRSoggettoAttrMod.setSiacTAttr(this);

		return siacRSoggettoAttrMod;
	}

	public SiacRSoggettoAttrMod removeSiacRSoggettoAttrMod(SiacRSoggettoAttrMod siacRSoggettoAttrMod) {
		getSiacRSoggettoAttrMods().remove(siacRSoggettoAttrMod);
		siacRSoggettoAttrMod.setSiacTAttr(null);

		return siacRSoggettoAttrMod;
	}

	public List<SiacRSubdocAttr> getSiacRSubdocAttrs() {
		return this.siacRSubdocAttrs;
	}

	public void setSiacRSubdocAttrs(List<SiacRSubdocAttr> siacRSubdocAttrs) {
		this.siacRSubdocAttrs = siacRSubdocAttrs;
	}

	public SiacRSubdocAttr addSiacRSubdocAttr(SiacRSubdocAttr siacRSubdocAttr) {
		getSiacRSubdocAttrs().add(siacRSubdocAttr);
		siacRSubdocAttr.setSiacTAttr(this);

		return siacRSubdocAttr;
	}

	public SiacRSubdocAttr removeSiacRSubdocAttr(SiacRSubdocAttr siacRSubdocAttr) {
		getSiacRSubdocAttrs().remove(siacRSubdocAttr);
		siacRSubdocAttr.setSiacTAttr(null);

		return siacRSubdocAttr;
	}
	
	/**
	 * Gets the siac r subdoc iva attrs.
	 *
	 * @return the siac r subdoc iva attrs
	 */
	public List<SiacRSubdocIvaAttr> getSiacRSubdocIvaAttrs() {
		return this.siacRSubdocIvaAttrs;
	}

	/**
	 * Sets the siac r subdoc iva attrs.
	 *
	 * @param siacRSubdocIvaAttrs the new siac r subdoc iva attrs
	 */
	public void setSiacRSubdocIvaAttrs(List<SiacRSubdocIvaAttr> siacRSubdocIvaAttrs) {
		this.siacRSubdocIvaAttrs = siacRSubdocIvaAttrs;
	}

	/**
	 * Adds the siac r subdoc iva attr.
	 *
	 * @param siacRSubdocIvaAttr the siac r subdoc iva attr
	 * @return the siac r subdoc iva attr
	 */
	public SiacRSubdocIvaAttr addSiacRSubdocIvaAttr(SiacRSubdocIvaAttr siacRSubdocIvaAttr) {
		getSiacRSubdocIvaAttrs().add(siacRSubdocIvaAttr);
		siacRSubdocIvaAttr.setSiacTAttr(this);

		return siacRSubdocIvaAttr;
	}

	/**
	 * Removes the siac r subdoc iva attr.
	 *
	 * @param siacRSubdocIvaAttr the siac r subdoc iva attr
	 * @return the siac r subdoc iva attr
	 */
	public SiacRSubdocIvaAttr removeSiacRSubdocIvaAttr(SiacRSubdocIvaAttr siacRSubdocIvaAttr) {
		getSiacRSubdocIvaAttrs().remove(siacRSubdocIvaAttr);
		siacRSubdocIvaAttr.setSiacTAttr(null);

		return siacRSubdocIvaAttr;
	}

	public List<SiacRSubdocTipoAttr> getSiacRSubdocTipoAttrs() {
		return this.siacRSubdocTipoAttrs;
	}

	public void setSiacRSubdocTipoAttrs(List<SiacRSubdocTipoAttr> siacRSubdocTipoAttrs) {
		this.siacRSubdocTipoAttrs = siacRSubdocTipoAttrs;
	}

	public SiacRSubdocTipoAttr addSiacRSubdocTipoAttr(SiacRSubdocTipoAttr siacRSubdocTipoAttr) {
		getSiacRSubdocTipoAttrs().add(siacRSubdocTipoAttr);
		siacRSubdocTipoAttr.setSiacTAttr(this);

		return siacRSubdocTipoAttr;
	}

	public SiacRSubdocTipoAttr removeSiacRSubdocTipoAttr(SiacRSubdocTipoAttr siacRSubdocTipoAttr) {
		getSiacRSubdocTipoAttrs().remove(siacRSubdocTipoAttr);
		siacRSubdocTipoAttr.setSiacTAttr(null);

		return siacRSubdocTipoAttr;
	}

	public List<SiacRVariazioneAttr> getSiacRVariazioneAttrs() {
		return this.siacRVariazioneAttrs;
	}

	public void setSiacRVariazioneAttrs(List<SiacRVariazioneAttr> siacRVariazioneAttrs) {
		this.siacRVariazioneAttrs = siacRVariazioneAttrs;
	}

	public SiacRVariazioneAttr addSiacRVariazioneAttr(SiacRVariazioneAttr siacRVariazioneAttr) {
		getSiacRVariazioneAttrs().add(siacRVariazioneAttr);
		siacRVariazioneAttr.setSiacTAttr(this);

		return siacRVariazioneAttr;
	}

	public SiacRVariazioneAttr removeSiacRVariazioneAttr(SiacRVariazioneAttr siacRVariazioneAttr) {
		getSiacRVariazioneAttrs().remove(siacRVariazioneAttr);
		siacRVariazioneAttr.setSiacTAttr(null);

		return siacRVariazioneAttr;
	}

	public List<SiacRVincoloAttr> getSiacRVincoloAttrs() {
		return this.siacRVincoloAttrs;
	}

	public void setSiacRVincoloAttrs(List<SiacRVincoloAttr> siacRVincoloAttrs) {
		this.siacRVincoloAttrs = siacRVincoloAttrs;
	}

	public SiacRVincoloAttr addSiacRVincoloAttr(SiacRVincoloAttr siacRVincoloAttr) {
		getSiacRVincoloAttrs().add(siacRVincoloAttr);
		siacRVincoloAttr.setSiacTAttr(this);

		return siacRVincoloAttr;
	}

	public SiacRVincoloAttr removeSiacRVincoloAttr(SiacRVincoloAttr siacRVincoloAttr) {
		getSiacRVincoloAttrs().remove(siacRVincoloAttr);
		siacRVincoloAttr.setSiacTAttr(null);

		return siacRVincoloAttr;
	}

	public SiacDAttrTipo getSiacDAttrTipo() {
		return this.siacDAttrTipo;
	}

	public void setSiacDAttrTipo(SiacDAttrTipo siacDAttrTipo) {
		this.siacDAttrTipo = siacDAttrTipo;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return attrId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.attrId = uid;
	}

}