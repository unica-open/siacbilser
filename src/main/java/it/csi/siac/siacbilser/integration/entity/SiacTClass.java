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
 * The persistent class for the siac_t_class database table.
 * 
 */
@Entity
@Table(name="siac_t_class")
public class SiacTClass extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The classif id. */
	@Id
	@SequenceGenerator(name="SIAC_T_CLASS_CLASSIFID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_CLASS_CLASSIF_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_CLASS_CLASSIFID_GENERATOR")
	@Column(name="classif_id")
	private Integer classifId;

	/** The classif code. */
	@Column(name="classif_code")
	private String classifCode;

	/** The classif desc. */
	@Column(name="classif_desc")
	private String classifDesc;

	

	//bi-directional many-to-one association to SiacRAccountRuoloOp
	/** The siac r account ruolo ops. */
	@OneToMany(mappedBy="siacTClass")
	private List<SiacRAccountRuoloOp> siacRAccountRuoloOps;

	//bi-directional many-to-one association to SiacRAttoAmmClass
	@OneToMany(mappedBy="siacTClass")
	private List<SiacRAttoAmmClass> siacRAttoAmmClasses;

	//bi-directional many-to-one association to SiacRBilElemClass
	/** The siac r bil elem classes. */
	@OneToMany(mappedBy="siacTClass")
	private List<SiacRBilElemClass> siacRBilElemClasses;

	//bi-directional many-to-one association to SiacRBilElemClassVar
	/** The siac r bil elem class vars. */
	@OneToMany(mappedBy="siacTClass")
	private List<SiacRBilElemClassVar> siacRBilElemClassVars;

	//bi-directional many-to-one association to SiacRCausaleClass
	@OneToMany(mappedBy="siacTClass")
	private List<SiacRCausaleClass> siacRCausaleClasses;

	//bi-directional many-to-one association to SiacRClass
	/** The siac r classes1. */
	@OneToMany(mappedBy="siacTClassA")
	private List<SiacRClass> siacRClasses1;

	//bi-directional many-to-one association to SiacRClass
	/** The siac r classes2. */
	@OneToMany(mappedBy="siacTClassB")
	private List<SiacRClass> siacRClasses2;

	//bi-directional many-to-one association to SiacRClassAttr
	/** The siac r class attrs. */
	@OneToMany(mappedBy="siacTClass")
	private List<SiacRClassAttr> siacRClassAttrs;

	//bi-directional many-to-one association to SiacRClassFamTree
	/** The siac r class fam trees figlio. */
	@OneToMany(mappedBy="siacTClassFiglio")
	private List<SiacRClassFamTree> siacRClassFamTreesFiglio;

	//bi-directional many-to-one association to SiacRClassFamTree
	/** The siac r class fam trees padre. */
	@OneToMany(mappedBy="siacTClassPadre")//, fetch=FetchType.EAGER)
//	@Fetch(value=FetchMode.SELECT ) 
//	@BatchSize (size = 20) 
	private List<SiacRClassFamTree> siacRClassFamTreesPadre;

	//bi-directional many-to-one association to SiacRCronopElemClass
	@OneToMany(mappedBy="siacTClass")
	private List<SiacRCronopElemClass> siacRCronopElemClasses;

	//bi-directional many-to-one association to SiacRDocClass
	@OneToMany(mappedBy="siacTClass")
	private List<SiacRDocClass> siacRDocClasses;

	//bi-directional many-to-one association to SiacRGruppoRuoloOp
	/** The siac r gruppo ruolo ops. */
	@OneToMany(mappedBy="siacTClass")
	private List<SiacRGruppoRuoloOp> siacRGruppoRuoloOps;

	//bi-directional many-to-one association to SiacRLiquidazioneClass
	@OneToMany(mappedBy="siacTClass")
	private List<SiacRLiquidazioneClass> siacRLiquidazioneClasses;

	//bi-directional many-to-one association to SiacRMovgestClass
	@OneToMany(mappedBy="siacTClass")
	private List<SiacRMovgestClass> siacRMovgestClasses;

	//bi-directional many-to-one association to SiacROrdinativoClass
	@OneToMany(mappedBy="siacTClass")
	private List<SiacROrdinativoClass> siacROrdinativoClasses;

	//bi-directional many-to-one association to SiacRPredocClass
	@OneToMany(mappedBy="siacTClass")
	private List<SiacRPredocClass> siacRPredocClasses;

	//bi-directional many-to-one association to SiacRProgrammaClass
	@OneToMany(mappedBy="siacTClass")
	private List<SiacRProgrammaClass> siacRProgrammaClasses;

	//bi-directional many-to-one association to SiacRSubdocClass
	@OneToMany(mappedBy="siacTClass")
	private List<SiacRSubdocClass> siacRSubdocClasses;
	
	// bi-directional many-to-one association to SiacRAccountClass
	@OneToMany(mappedBy="siacTClass")
	private List<SiacRAccountClass> siacRAccountClasses;

	//bi-directional many-to-one association to SiacDClassTipo
	/** The siac d class tipo. */
	@ManyToOne
	@JoinColumn(name="classif_tipo_id")
	private SiacDClassTipo siacDClassTipo;

	//bi-directional many-to-one association to SiacRConciliazioneTitolo
	@OneToMany(mappedBy="siacTClass")
	private List<SiacRConciliazioneTitolo> siacRConciliazioneTitolos;

	//bi-directional many-to-one association to SiacRMovEpDetClass
	@OneToMany(mappedBy="siacTClass")
	private List<SiacRMovEpDetClass> siacRMovEpDetClasses;
	/**
	 * Instantiates a new siac t class.
	 */
	public SiacTClass() {
	}

	/**
	 * Gets the classif id.
	 *
	 * @return the classif id
	 */
	public Integer getClassifId() {
		return this.classifId;
	}

	/**
	 * Sets the classif id.
	 *
	 * @param classifId the new classif id
	 */
	public void setClassifId(Integer classifId) {
		this.classifId = classifId;
	}

	/**
	 * Gets the classif code.
	 *
	 * @return the classif code
	 */
	public String getClassifCode() {
		return this.classifCode;
	}

	/**
	 * Sets the classif code.
	 *
	 * @param classifCode the new classif code
	 */
	public void setClassifCode(String classifCode) {
		this.classifCode = classifCode;
	}

	/**
	 * Gets the classif desc.
	 *
	 * @return the classif desc
	 */
	public String getClassifDesc() {
		return this.classifDesc;
	}

	/**
	 * Sets the classif desc.
	 *
	 * @param classifDesc the new classif desc
	 */
	public void setClassifDesc(String classifDesc) {
		this.classifDesc = classifDesc;
	}

	

	/**
	 * Gets the siac r account ruolo ops.
	 *
	 * @return the siac r account ruolo ops
	 */
	public List<SiacRAccountRuoloOp> getSiacRAccountRuoloOps() {
		return this.siacRAccountRuoloOps;
	}

	/**
	 * Sets the siac r account ruolo ops.
	 *
	 * @param siacRAccountRuoloOps the new siac r account ruolo ops
	 */
	public void setSiacRAccountRuoloOps(List<SiacRAccountRuoloOp> siacRAccountRuoloOps) {
		this.siacRAccountRuoloOps = siacRAccountRuoloOps;
	}

	/**
	 * Adds the siac r account ruolo op.
	 *
	 * @param siacRAccountRuoloOp the siac r account ruolo op
	 * @return the siac r account ruolo op
	 */
	public SiacRAccountRuoloOp addSiacRAccountRuoloOp(SiacRAccountRuoloOp siacRAccountRuoloOp) {
		getSiacRAccountRuoloOps().add(siacRAccountRuoloOp);
		siacRAccountRuoloOp.setSiacTClass(this);

		return siacRAccountRuoloOp;
	}

	/**
	 * Removes the siac r account ruolo op.
	 *
	 * @param siacRAccountRuoloOp the siac r account ruolo op
	 * @return the siac r account ruolo op
	 */
	public SiacRAccountRuoloOp removeSiacRAccountRuoloOp(SiacRAccountRuoloOp siacRAccountRuoloOp) {
		getSiacRAccountRuoloOps().remove(siacRAccountRuoloOp);
		siacRAccountRuoloOp.setSiacTClass(null);

		return siacRAccountRuoloOp;
	}

	public List<SiacRAttoAmmClass> getSiacRAttoAmmClasses() {
		return this.siacRAttoAmmClasses;
	}

	public void setSiacRAttoAmmClasses(List<SiacRAttoAmmClass> siacRAttoAmmClasses) {
		this.siacRAttoAmmClasses = siacRAttoAmmClasses;
	}

	public SiacRAttoAmmClass addSiacRAttoAmmClass(SiacRAttoAmmClass siacRAttoAmmClass) {
		getSiacRAttoAmmClasses().add(siacRAttoAmmClass);
		siacRAttoAmmClass.setSiacTClass(this);

		return siacRAttoAmmClass;
	}

	public SiacRAttoAmmClass removeSiacRAttoAmmClass(SiacRAttoAmmClass siacRAttoAmmClass) {
		getSiacRAttoAmmClasses().remove(siacRAttoAmmClass);
		siacRAttoAmmClass.setSiacTClass(null);

		return siacRAttoAmmClass;
	}

	/**
	 * Gets the siac r bil elem classes.
	 *
	 * @return the siac r bil elem classes
	 */
	public List<SiacRBilElemClass> getSiacRBilElemClasses() {
		return this.siacRBilElemClasses;
	}

	/**
	 * Sets the siac r bil elem classes.
	 *
	 * @param siacRBilElemClasses the new siac r bil elem classes
	 */
	public void setSiacRBilElemClasses(List<SiacRBilElemClass> siacRBilElemClasses) {
		this.siacRBilElemClasses = siacRBilElemClasses;
	}

	/**
	 * Adds the siac r bil elem class.
	 *
	 * @param siacRBilElemClass the siac r bil elem class
	 * @return the siac r bil elem class
	 */
	public SiacRBilElemClass addSiacRBilElemClass(SiacRBilElemClass siacRBilElemClass) {
		getSiacRBilElemClasses().add(siacRBilElemClass);
		siacRBilElemClass.setSiacTClass(this);

		return siacRBilElemClass;
	}

	/**
	 * Removes the siac r bil elem class.
	 *
	 * @param siacRBilElemClass the siac r bil elem class
	 * @return the siac r bil elem class
	 */
	public SiacRBilElemClass removeSiacRBilElemClass(SiacRBilElemClass siacRBilElemClass) {
		getSiacRBilElemClasses().remove(siacRBilElemClass);
		siacRBilElemClass.setSiacTClass(null);

		return siacRBilElemClass;
	}

	/**
	 * Gets the siac r bil elem class vars.
	 *
	 * @return the siac r bil elem class vars
	 */
	public List<SiacRBilElemClassVar> getSiacRBilElemClassVars() {
		return this.siacRBilElemClassVars;
	}

	/**
	 * Sets the siac r bil elem class vars.
	 *
	 * @param siacRBilElemClassVars the new siac r bil elem class vars
	 */
	public void setSiacRBilElemClassVars(List<SiacRBilElemClassVar> siacRBilElemClassVars) {
		this.siacRBilElemClassVars = siacRBilElemClassVars;
	}

	/**
	 * Adds the siac r bil elem class var.
	 *
	 * @param siacRBilElemClassVar the siac r bil elem class var
	 * @return the siac r bil elem class var
	 */
	public SiacRBilElemClassVar addSiacRBilElemClassVar(SiacRBilElemClassVar siacRBilElemClassVar) {
		getSiacRBilElemClassVars().add(siacRBilElemClassVar);
		siacRBilElemClassVar.setSiacTClass(this);

		return siacRBilElemClassVar;
	}

	/**
	 * Removes the siac r bil elem class var.
	 *
	 * @param siacRBilElemClassVar the siac r bil elem class var
	 * @return the siac r bil elem class var
	 */
	public SiacRBilElemClassVar removeSiacRBilElemClassVar(SiacRBilElemClassVar siacRBilElemClassVar) {
		getSiacRBilElemClassVars().remove(siacRBilElemClassVar);
		siacRBilElemClassVar.setSiacTClass(null);

		return siacRBilElemClassVar;
	}

	public List<SiacRCausaleClass> getSiacRCausaleClasses() {
		return this.siacRCausaleClasses;
	}

	public void setSiacRCausaleClasses(List<SiacRCausaleClass> siacRCausaleClasses) {
		this.siacRCausaleClasses = siacRCausaleClasses;
	}

	public SiacRCausaleClass addSiacRCausaleClass(SiacRCausaleClass siacRCausaleClass) {
		getSiacRCausaleClasses().add(siacRCausaleClass);
		siacRCausaleClass.setSiacTClass(this);

		return siacRCausaleClass;
	}

	public SiacRCausaleClass removeSiacRCausaleClass(SiacRCausaleClass siacRCausaleClass) {
		getSiacRCausaleClasses().remove(siacRCausaleClass);
		siacRCausaleClass.setSiacTClass(null);

		return siacRCausaleClass;
	}

	/**
	 * Gets the siac r classes1.
	 *
	 * @return the siac r classes1
	 */
	public List<SiacRClass> getSiacRClasses1() {
		return this.siacRClasses1;
	}

	/**
	 * Sets the siac r classes1.
	 *
	 * @param siacRClasses1 the new siac r classes1
	 */
	public void setSiacRClasses1(List<SiacRClass> siacRClasses1) {
		this.siacRClasses1 = siacRClasses1;
	}

	/**
	 * Adds the siac r classes1.
	 *
	 * @param siacRClasses1 the siac r classes1
	 * @return the siac r class
	 */
	public SiacRClass addSiacRClasses1(SiacRClass siacRClasses1) {
		getSiacRClasses1().add(siacRClasses1);
		siacRClasses1.setSiacTClassA(this);

		return siacRClasses1;
	}

	/**
	 * Removes the siac r classes1.
	 *
	 * @param siacRClasses1 the siac r classes1
	 * @return the siac r class
	 */
	public SiacRClass removeSiacRClasses1(SiacRClass siacRClasses1) {
		getSiacRClasses1().remove(siacRClasses1);
		siacRClasses1.setSiacTClassA(null);

		return siacRClasses1;
	}

	/**
	 * Gets the siac r classes2.
	 *
	 * @return the siac r classes2
	 */
	public List<SiacRClass> getSiacRClasses2() {
		return this.siacRClasses2;
	}

	/**
	 * Sets the siac r classes2.
	 *
	 * @param siacRClasses2 the new siac r classes2
	 */
	public void setSiacRClasses2(List<SiacRClass> siacRClasses2) {
		this.siacRClasses2 = siacRClasses2;
	}

	/**
	 * Adds the siac r classes2.
	 *
	 * @param siacRClasses2 the siac r classes2
	 * @return the siac r class
	 */
	public SiacRClass addSiacRClasses2(SiacRClass siacRClasses2) {
		getSiacRClasses2().add(siacRClasses2);
		siacRClasses2.setSiacTClassB(this);

		return siacRClasses2;
	}

	/**
	 * Removes the siac r classes2.
	 *
	 * @param siacRClasses2 the siac r classes2
	 * @return the siac r class
	 */
	public SiacRClass removeSiacRClasses2(SiacRClass siacRClasses2) {
		getSiacRClasses2().remove(siacRClasses2);
		siacRClasses2.setSiacTClassB(null);

		return siacRClasses2;
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
		siacRClassAttr.setSiacTClass(this);

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
		siacRClassAttr.setSiacTClass(null);

		return siacRClassAttr;
	}

	/**
	 * Gets the siac r class fam trees figlio.
	 *
	 * @return the siac r class fam trees figlio
	 */
	public List<SiacRClassFamTree> getSiacRClassFamTreesFiglio() {
		return this.siacRClassFamTreesFiglio;
	}

	/**
	 * Sets the siac r class fam trees figlio.
	 *
	 * @param siacRClassFamTrees1 the new siac r class fam trees figlio
	 */
	public void setSiacRClassFamTreesFiglio(List<SiacRClassFamTree> siacRClassFamTrees1) {
		this.siacRClassFamTreesFiglio = siacRClassFamTrees1;
	}

	/**
	 * Adds the siac r class fam trees1.
	 *
	 * @param siacRClassFamTrees1 the siac r class fam trees1
	 * @return the siac r class fam tree
	 */
	public SiacRClassFamTree addSiacRClassFamTrees1(SiacRClassFamTree siacRClassFamTrees1) {
		getSiacRClassFamTreesFiglio().add(siacRClassFamTrees1);
		siacRClassFamTrees1.setSiacTClassFiglio(this);

		return siacRClassFamTrees1;
	}

	/**
	 * Removes the siac r class fam trees1.
	 *
	 * @param siacRClassFamTrees1 the siac r class fam trees1
	 * @return the siac r class fam tree
	 */
	public SiacRClassFamTree removeSiacRClassFamTrees1(SiacRClassFamTree siacRClassFamTrees1) {
		getSiacRClassFamTreesFiglio().remove(siacRClassFamTrees1);
		siacRClassFamTrees1.setSiacTClassFiglio(null);

		return siacRClassFamTrees1;
	}

	/**
	 * Gets the siac r class fam trees padre.
	 *
	 * @return the siac r class fam trees padre
	 */
	public List<SiacRClassFamTree> getSiacRClassFamTreesPadre() {
		return this.siacRClassFamTreesPadre;
	}

	/**
	 * Sets the siac r class fam trees padre.
	 *
	 * @param siacRClassFamTrees2 the new siac r class fam trees padre
	 */
	public void setSiacRClassFamTreesPadre(List<SiacRClassFamTree> siacRClassFamTrees2) {
		this.siacRClassFamTreesPadre = siacRClassFamTrees2;
	}

	/**
	 * Adds the siac r class fam trees2.
	 *
	 * @param siacRClassFamTrees2 the siac r class fam trees2
	 * @return the siac r class fam tree
	 */
	public SiacRClassFamTree addSiacRClassFamTrees2(SiacRClassFamTree siacRClassFamTrees2) {
		getSiacRClassFamTreesPadre().add(siacRClassFamTrees2);
		siacRClassFamTrees2.setSiacTClassPadre(this);

		return siacRClassFamTrees2;
	}

	/**
	 * Removes the siac r class fam trees2.
	 *
	 * @param siacRClassFamTrees2 the siac r class fam trees2
	 * @return the siac r class fam tree
	 */
	public SiacRClassFamTree removeSiacRClassFamTrees2(SiacRClassFamTree siacRClassFamTrees2) {
		getSiacRClassFamTreesPadre().remove(siacRClassFamTrees2);
		siacRClassFamTrees2.setSiacTClassPadre(null);

		return siacRClassFamTrees2;
	}

	public List<SiacRCronopElemClass> getSiacRCronopElemClasses() {
		return this.siacRCronopElemClasses;
	}

	public void setSiacRCronopElemClasses(List<SiacRCronopElemClass> siacRCronopElemClasses) {
		this.siacRCronopElemClasses = siacRCronopElemClasses;
	}

	public SiacRCronopElemClass addSiacRCronopElemClass(SiacRCronopElemClass siacRCronopElemClass) {
		getSiacRCronopElemClasses().add(siacRCronopElemClass);
		siacRCronopElemClass.setSiacTClass(this);

		return siacRCronopElemClass;
	}

	public SiacRCronopElemClass removeSiacRCronopElemClass(SiacRCronopElemClass siacRCronopElemClass) {
		getSiacRCronopElemClasses().remove(siacRCronopElemClass);
		siacRCronopElemClass.setSiacTClass(null);

		return siacRCronopElemClass;
	}

	public List<SiacRDocClass> getSiacRDocClasses() {
		return this.siacRDocClasses;
	}

	public void setSiacRDocClasses(List<SiacRDocClass> siacRDocClasses) {
		this.siacRDocClasses = siacRDocClasses;
	}

	public SiacRDocClass addSiacRDocClass(SiacRDocClass siacRDocClass) {
		getSiacRDocClasses().add(siacRDocClass);
		siacRDocClass.setSiacTClass(this);

		return siacRDocClass;
	}

	public SiacRDocClass removeSiacRDocClass(SiacRDocClass siacRDocClass) {
		getSiacRDocClasses().remove(siacRDocClass);
		siacRDocClass.setSiacTClass(null);

		return siacRDocClass;
	}

	/**
	 * Gets the siac r gruppo ruolo ops.
	 *
	 * @return the siac r gruppo ruolo ops
	 */
	public List<SiacRGruppoRuoloOp> getSiacRGruppoRuoloOps() {
		return this.siacRGruppoRuoloOps;
	}

	/**
	 * Sets the siac r gruppo ruolo ops.
	 *
	 * @param siacRGruppoRuoloOps the new siac r gruppo ruolo ops
	 */
	public void setSiacRGruppoRuoloOps(List<SiacRGruppoRuoloOp> siacRGruppoRuoloOps) {
		this.siacRGruppoRuoloOps = siacRGruppoRuoloOps;
	}

	/**
	 * Adds the siac r gruppo ruolo op.
	 *
	 * @param siacRGruppoRuoloOp the siac r gruppo ruolo op
	 * @return the siac r gruppo ruolo op
	 */
	public SiacRGruppoRuoloOp addSiacRGruppoRuoloOp(SiacRGruppoRuoloOp siacRGruppoRuoloOp) {
		getSiacRGruppoRuoloOps().add(siacRGruppoRuoloOp);
		siacRGruppoRuoloOp.setSiacTClass(this);

		return siacRGruppoRuoloOp;
	}

	/**
	 * Removes the siac r gruppo ruolo op.
	 *
	 * @param siacRGruppoRuoloOp the siac r gruppo ruolo op
	 * @return the siac r gruppo ruolo op
	 */
	public SiacRGruppoRuoloOp removeSiacRGruppoRuoloOp(SiacRGruppoRuoloOp siacRGruppoRuoloOp) {
		getSiacRGruppoRuoloOps().remove(siacRGruppoRuoloOp);
		siacRGruppoRuoloOp.setSiacTClass(null);

		return siacRGruppoRuoloOp;
	}

	public List<SiacRLiquidazioneClass> getSiacRLiquidazioneClasses() {
		return this.siacRLiquidazioneClasses;
	}

	public void setSiacRLiquidazioneClasses(List<SiacRLiquidazioneClass> siacRLiquidazioneClasses) {
		this.siacRLiquidazioneClasses = siacRLiquidazioneClasses;
	}

	public SiacRLiquidazioneClass addSiacRLiquidazioneClass(SiacRLiquidazioneClass siacRLiquidazioneClass) {
		getSiacRLiquidazioneClasses().add(siacRLiquidazioneClass);
		siacRLiquidazioneClass.setSiacTClass(this);

		return siacRLiquidazioneClass;
	}

	public SiacRLiquidazioneClass removeSiacRLiquidazioneClass(SiacRLiquidazioneClass siacRLiquidazioneClass) {
		getSiacRLiquidazioneClasses().remove(siacRLiquidazioneClass);
		siacRLiquidazioneClass.setSiacTClass(null);

		return siacRLiquidazioneClass;
	}

	public List<SiacRMovgestClass> getSiacRMovgestClasses() {
		return this.siacRMovgestClasses;
	}

	public void setSiacRMovgestClasses(List<SiacRMovgestClass> siacRMovgestClasses) {
		this.siacRMovgestClasses = siacRMovgestClasses;
	}

	public SiacRMovgestClass addSiacRMovgestClass(SiacRMovgestClass siacRMovgestClass) {
		getSiacRMovgestClasses().add(siacRMovgestClass);
		siacRMovgestClass.setSiacTClass(this);

		return siacRMovgestClass;
	}

	public SiacRMovgestClass removeSiacRMovgestClass(SiacRMovgestClass siacRMovgestClass) {
		getSiacRMovgestClasses().remove(siacRMovgestClass);
		siacRMovgestClass.setSiacTClass(null);

		return siacRMovgestClass;
	}

	public List<SiacROrdinativoClass> getSiacROrdinativoClasses() {
		return this.siacROrdinativoClasses;
	}

	public void setSiacROrdinativoClasses(List<SiacROrdinativoClass> siacROrdinativoClasses) {
		this.siacROrdinativoClasses = siacROrdinativoClasses;
	}

	public SiacROrdinativoClass addSiacROrdinativoClass(SiacROrdinativoClass siacROrdinativoClass) {
		getSiacROrdinativoClasses().add(siacROrdinativoClass);
		siacROrdinativoClass.setSiacTClass(this);

		return siacROrdinativoClass;
	}

	public SiacROrdinativoClass removeSiacROrdinativoClass(SiacROrdinativoClass siacROrdinativoClass) {
		getSiacROrdinativoClasses().remove(siacROrdinativoClass);
		siacROrdinativoClass.setSiacTClass(null);

		return siacROrdinativoClass;
	}

	public List<SiacRPredocClass> getSiacRPredocClasses() {
		return this.siacRPredocClasses;
	}

	public void setSiacRPredocClasses(List<SiacRPredocClass> siacRPredocClasses) {
		this.siacRPredocClasses = siacRPredocClasses;
	}

	public SiacRPredocClass addSiacRPredocClass(SiacRPredocClass siacRPredocClass) {
		getSiacRPredocClasses().add(siacRPredocClass);
		siacRPredocClass.setSiacTClass(this);

		return siacRPredocClass;
	}

	public SiacRPredocClass removeSiacRPredocClass(SiacRPredocClass siacRPredocClass) {
		getSiacRPredocClasses().remove(siacRPredocClass);
		siacRPredocClass.setSiacTClass(null);

		return siacRPredocClass;
	}

	public List<SiacRProgrammaClass> getSiacRProgrammaClasses() {
		return this.siacRProgrammaClasses;
	}

	public void setSiacRProgrammaClasses(List<SiacRProgrammaClass> siacRProgrammaClasses) {
		this.siacRProgrammaClasses = siacRProgrammaClasses;
	}

	public SiacRProgrammaClass addSiacRProgrammaClass(SiacRProgrammaClass siacRProgrammaClass) {
		getSiacRProgrammaClasses().add(siacRProgrammaClass);
		siacRProgrammaClass.setSiacTClass(this);

		return siacRProgrammaClass;
	}

	public SiacRProgrammaClass removeSiacRProgrammaClass(SiacRProgrammaClass siacRProgrammaClass) {
		getSiacRProgrammaClasses().remove(siacRProgrammaClass);
		siacRProgrammaClass.setSiacTClass(null);

		return siacRProgrammaClass;
	}

	public List<SiacRSubdocClass> getSiacRSubdocClasses() {
		return this.siacRSubdocClasses;
	}

	public void setSiacRSubdocClasses(List<SiacRSubdocClass> siacRSubdocClasses) {
		this.siacRSubdocClasses = siacRSubdocClasses;
	}

	public SiacRSubdocClass addSiacRSubdocClass(SiacRSubdocClass siacRSubdocClass) {
		getSiacRSubdocClasses().add(siacRSubdocClass);
		siacRSubdocClass.setSiacTClass(this);

		return siacRSubdocClass;
	}

	public SiacRSubdocClass removeSiacRSubdocClass(SiacRSubdocClass siacRSubdocClass) {
		getSiacRSubdocClasses().remove(siacRSubdocClass);
		siacRSubdocClass.setSiacTClass(null);

		return siacRSubdocClass;
	}
	
	public List<SiacRAccountClass> getSiacRAccountClasses() {
		return this.siacRAccountClasses;
	}

	public void setSiacRAccountClasses(List<SiacRAccountClass> siacRAccountClasses) {
		this.siacRAccountClasses = siacRAccountClasses;
	}

	public SiacRAccountClass addSiacRAccountClass(SiacRAccountClass siacRAccountClass) {
		getSiacRAccountClasses().add(siacRAccountClass);
		siacRAccountClass.setSiacTClass(this);

		return siacRAccountClass;
	}

	public SiacRAccountClass removeSiacRAccountClass(SiacRAccountClass siacRAccountClass) {
		getSiacRAccountClasses().remove(siacRAccountClass);
		siacRAccountClass.setSiacTClass(null);

		return siacRAccountClass;
	}

	/**
	 * Gets the siac d class tipo.
	 *
	 * @return the siac d class tipo
	 */
	public SiacDClassTipo getSiacDClassTipo() {
		return this.siacDClassTipo;
	}

	/**
	 * Sets the siac d class tipo.
	 *
	 * @param siacDClassTipo the new siac d class tipo
	 */
	public void setSiacDClassTipo(SiacDClassTipo siacDClassTipo) {
		this.siacDClassTipo = siacDClassTipo;
	}

	public List<SiacRConciliazioneTitolo> getSiacRConciliazioneTitolos() {
		return this.siacRConciliazioneTitolos;
	}

	public void setSiacRConciliazioneTitolos(List<SiacRConciliazioneTitolo> siacRConciliazioneTitolos) {
		this.siacRConciliazioneTitolos = siacRConciliazioneTitolos;
	}

	public SiacRConciliazioneTitolo addSiacRConciliazioneTitolo(SiacRConciliazioneTitolo siacRConciliazioneTitolo) {
		getSiacRConciliazioneTitolos().add(siacRConciliazioneTitolo);
		siacRConciliazioneTitolo.setSiacTClass(this);

		return siacRConciliazioneTitolo;
	}

	public SiacRConciliazioneTitolo removeSiacRConciliazioneTitolo(SiacRConciliazioneTitolo siacRConciliazioneTitolo) {
		getSiacRConciliazioneTitolos().remove(siacRConciliazioneTitolo);
		siacRConciliazioneTitolo.setSiacTClass(null);

		return siacRConciliazioneTitolo;
	}
	
	public List<SiacRMovEpDetClass> getSiacRMovEpDetClasses() {
		return this.siacRMovEpDetClasses;
	}

	public void setSiacRMovEpDetClasses(List<SiacRMovEpDetClass> siacRMovEpDetClasses) {
		this.siacRMovEpDetClasses = siacRMovEpDetClasses;
	}

	public SiacRMovEpDetClass addSiacRMovEpDetClass(SiacRMovEpDetClass siacRMovEpDetClass) {
		getSiacRMovEpDetClasses().add(siacRMovEpDetClass);
		siacRMovEpDetClass.setSiacTClass(this);

		return siacRMovEpDetClass;
	}

	public SiacRMovEpDetClass removeSiacRMovEpDetClass(SiacRMovEpDetClass siacRMovEpDetClass) {
		getSiacRMovEpDetClasses().remove(siacRMovEpDetClass);
		siacRMovEpDetClass.setSiacTClass(null);

		return siacRMovEpDetClass;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return classifId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.classifId = uid;
		
	}
		

}