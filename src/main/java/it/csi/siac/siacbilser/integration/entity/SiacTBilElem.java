/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
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
 * The persistent class for the siac_t_bil_elem database table.
 * 
 */
@Entity
@Table(name="siac_t_bil_elem")
public class SiacTBilElem extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The elem id. */
	@Id
	@SequenceGenerator(name="SIAC_T_BIL_ELEM_ELEMID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_BIL_ELEM_ELEM_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_BIL_ELEM_ELEMID_GENERATOR")
	@Column(name="elem_id")
	private Integer elemId;

	/** The elem code. */
	@Column(name="elem_code")
	private String elemCode;

	/** The elem code2. */
	@Column(name="elem_code2")
	private String elemCode2;

	/** The elem code3. */
	@Column(name="elem_code3")
	private String elemCode3;

	/** The elem desc. */
	@Column(name="elem_desc")
	private String elemDesc;

	/** The elem desc2. */
	@Column(name="elem_desc2")
	private String elemDesc2;

	/** The elem id padre. */
	@Column(name="elem_id_padre")
	private Integer elemIdPadre;

	/** The livello. */
	private Integer livello;	

	/** The ordine. */
	private String ordine;



	//bi-directional many-to-one association to SiacRBilElemAttoLegge
	/** The siac r bil elem atto legges. */
	@OneToMany(mappedBy="siacTBilElem")
	private List<SiacRBilElemAttoLegge> siacRBilElemAttoLegges;

	//bi-directional many-to-one association to SiacRBilElemAttr
	/** The siac r bil elem attrs. */
	@OneToMany(mappedBy="siacTBilElem", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRBilElemAttr> siacRBilElemAttrs;

	//bi-directional many-to-one association to SiacRBilElemCategoria
	@OneToMany(mappedBy="siacTBilElem", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRBilElemCategoria> siacRBilElemCategorias;

	//bi-directional many-to-one association to SiacRBilElemClass
	/** The siac r bil elem classes. */
	@OneToMany(mappedBy="siacTBilElem", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRBilElemClass> siacRBilElemClasses;

	//bi-directional many-to-one association to SiacRBilElemClassVar
	/** The siac r bil elem class vars. */
	@OneToMany(mappedBy="siacTBilElem")
	private List<SiacRBilElemClassVar> siacRBilElemClassVars;

	//bi-directional many-to-one association to SiacRBilElemIvaAttivita
	/** The siac r bil elem iva attivitas. */
	@OneToMany(mappedBy="siacTBilElem")
	private List<SiacRBilElemIvaAttivita> siacRBilElemIvaAttivitas;

	//bi-directional many-to-one association to SiacRBilElemRelTempo
	/** The siac r bil elem rel tempos. */
	@OneToMany(mappedBy="siacTBilElem", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRBilElemRelTempo> siacRBilElemRelTempos;

	//bi-directional many-to-one association to SiacRBilElemRelTempo
	/** The siac r bil elem rel tempo olds. */
	@OneToMany(mappedBy="siacTBilElemOld")
	private List<SiacRBilElemRelTempo> siacRBilElemRelTempoOlds;

	/*
	 //TODO ENTITY
	 //bi-directional many-to-one association to SiacRBilElemRelTempo
	@OneToMany(mappedBy="siacTBilElem3")
	private List<SiacRBilElemRelTempo> siacRBilElemRelTempos3;*/

	//bi-directional many-to-one association to SiacRBilElemStato
	/** The siac r bil elem statos. */
	@OneToMany(mappedBy="siacTBilElem", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRBilElemStato> siacRBilElemStatos;

	//bi-directional many-to-one association to SiacRCausaleBilElem
	@OneToMany(mappedBy="siacTBilElem")
	private List<SiacRCausaleBilElem> siacRCausaleBilElems;

	//bi-directional many-to-one association to SiacRCronopElemBilElem
	@OneToMany(mappedBy="siacTBilElem")
	private List<SiacRCronopElemBilElem> siacRCronopElemBilElems;

	//bi-directional many-to-one association to SiacRFondoEconBilElem
	@OneToMany(mappedBy="siacTBilElem")
	private List<SiacRFondoEconBilElem> siacRFondoEconBilElems;

	//bi-directional many-to-one association to SiacRMovgestBilElem
	@OneToMany(mappedBy="siacTBilElem")
	private List<SiacRMovgestBilElem> siacRMovgestBilElems;

	//bi-directional many-to-one association to SiacROrdinativoBilElem
	@OneToMany(mappedBy="siacTBilElem")
	private List<SiacROrdinativoBilElem> siacROrdinativoBilElems;

	//bi-directional many-to-one association to SiacRPredocBilElem
	@OneToMany(mappedBy="siacTBilElem")
	private List<SiacRPredocBilElem> siacRPredocBilElems;

	//bi-directional many-to-one association to SiacRVincoloBilElem
	@OneToMany(mappedBy="siacTBilElem")
	private List<SiacRVincoloBilElem> siacRVincoloBilElems;
	
	//bi-directional many-to-one association to SiacRBilElemStipendioCodice
	@OneToMany(mappedBy="siacTBilElem")
	private List<SiacRBilElemStipendioCodice> siacRBilElemStipendioCodices;

	//bi-directional many-to-one association to SiacDBilElemTipo
	/** The siac d bil elem tipo. */
	@ManyToOne
	@JoinColumn(name="elem_tipo_id")
	private SiacDBilElemTipo siacDBilElemTipo;

	//bi-directional many-to-one association to SiacTBil
	/** The siac t bil. */
	@ManyToOne
	@JoinColumn(name="bil_id")
	private SiacTBil siacTBil;

	//bi-directional many-to-one association to SiacTBilElemDet
	/** The siac t bil elem dets. */
	@OneToMany(mappedBy="siacTBilElem", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacTBilElemDet> siacTBilElemDets;

	//bi-directional many-to-one association to SiacTBilElemDetVar
	/** The siac t bil elem det vars. */
	@OneToMany(mappedBy="siacTBilElem")
	private List<SiacTBilElemDetVar> siacTBilElemDetVars;

	//bi-directional many-to-one association to SiacTBilElemVar
	/** The siac t bil elem vars. */
	@OneToMany(mappedBy="siacTBilElem")
	private List<SiacTBilElemVar> siacTBilElemVars;

	//bi-directional many-to-one association to SiacRConciliazioneBeneficiario
	@OneToMany(mappedBy="siacTBilElem")
	private List<SiacRConciliazioneBeneficiario> siacRConciliazioneBeneficiarios;

	//bi-directional many-to-one association to SiacRConciliazioneCapitolo
	@OneToMany(mappedBy="siacTBilElem")
	private List<SiacRConciliazioneCapitolo> siacRConciliazioneCapitolos;

	// bi-directional many-to-one association to SiacRBilElemAccFondiDubbiaEsig
	@OneToMany(mappedBy="siacTBilElem")
	private List<SiacRBilElemAccFondiDubbiaEsig> siacRBilElemAccFondiDubbiaEsigs;
	
	/**
	 * Instantiates a new siac t bil elem.
	 */
	public SiacTBilElem() {
	}

	/**
	 * Gets the elem id.
	 *
	 * @return the elem id
	 */
	public Integer getElemId() {
		return this.elemId;
	}

	/**
	 * Sets the elem id.
	 *
	 * @param elemId the new elem id
	 */
	public void setElemId(Integer elemId) {
		this.elemId = elemId;
	}

	/**
	 * Gets the elem code.
	 *
	 * @return the elem code
	 */
	public String getElemCode() {
		return this.elemCode;
	}

	/**
	 * Sets the elem code.
	 *
	 * @param elemCode the new elem code
	 */
	public void setElemCode(String elemCode) {
		this.elemCode = elemCode;
	}

	/**
	 * Gets the elem code2.
	 *
	 * @return the elem code2
	 */
	public String getElemCode2() {
		return this.elemCode2;
	}

	/**
	 * Sets the elem code2.
	 *
	 * @param elemCode2 the new elem code2
	 */
	public void setElemCode2(String elemCode2) {
		this.elemCode2 = elemCode2;
	}

	/**
	 * Gets the elem code3.
	 *
	 * @return the elem code3
	 */
	public String getElemCode3() {
		return this.elemCode3;
	}

	/**
	 * Sets the elem code3.
	 *
	 * @param elemCode3 the new elem code3
	 */
	public void setElemCode3(String elemCode3) {
		this.elemCode3 = elemCode3;
	}

	/**
	 * Gets the elem desc.
	 *
	 * @return the elem desc
	 */
	public String getElemDesc() {
		return this.elemDesc;
	}

	/**
	 * Sets the elem desc.
	 *
	 * @param elemDesc the new elem desc
	 */
	public void setElemDesc(String elemDesc) {
		this.elemDesc = elemDesc;
	}

	/**
	 * Gets the elem desc2.
	 *
	 * @return the elem desc2
	 */
	public String getElemDesc2() {
		return this.elemDesc2;
	}

	/**
	 * Sets the elem desc2.
	 *
	 * @param elemDesc2 the new elem desc2
	 */
	public void setElemDesc2(String elemDesc2) {
		this.elemDesc2 = elemDesc2;
	}

	/**
	 * Gets the elem id padre.
	 *
	 * @return the elem id padre
	 */
	public Integer getElemIdPadre() {
		return this.elemIdPadre;
	}

	/**
	 * Sets the elem id padre.
	 *
	 * @param elemIdPadre the new elem id padre
	 */
	public void setElemIdPadre(Integer elemIdPadre) {
		this.elemIdPadre = elemIdPadre;
	}

	/**
	 * Gets the livello.
	 *
	 * @return the livello
	 */
	public Integer getLivello() {
		return this.livello;
	}

	/**
	 * Sets the livello.
	 *
	 * @param livello the new livello
	 */
	public void setLivello(Integer livello) {
		this.livello = livello;
	}


	/**
	 * Gets the ordine.
	 *
	 * @return the ordine
	 */
	public String getOrdine() {
		return this.ordine;
	}

	/**
	 * Sets the ordine.
	 *
	 * @param ordine the new ordine
	 */
	public void setOrdine(String ordine) {
		this.ordine = ordine;
	}



	/**
	 * Gets the siac r bil elem atto legges.
	 *
	 * @return the siac r bil elem atto legges
	 */
	public List<SiacRBilElemAttoLegge> getSiacRBilElemAttoLegges() {
		return this.siacRBilElemAttoLegges;
	}

	/**
	 * Sets the siac r bil elem atto legges.
	 *
	 * @param siacRBilElemAttoLegges the new siac r bil elem atto legges
	 */
	public void setSiacRBilElemAttoLegges(List<SiacRBilElemAttoLegge> siacRBilElemAttoLegges) {
		this.siacRBilElemAttoLegges = siacRBilElemAttoLegges;
	}

	/**
	 * Adds the siac r bil elem atto legge.
	 *
	 * @param siacRBilElemAttoLegge the siac r bil elem atto legge
	 * @return the siac r bil elem atto legge
	 */
	public SiacRBilElemAttoLegge addSiacRBilElemAttoLegge(SiacRBilElemAttoLegge siacRBilElemAttoLegge) {
		getSiacRBilElemAttoLegges().add(siacRBilElemAttoLegge);
		siacRBilElemAttoLegge.setSiacTBilElem(this);

		return siacRBilElemAttoLegge;
	}

	/**
	 * Removes the siac r bil elem atto legge.
	 *
	 * @param siacRBilElemAttoLegge the siac r bil elem atto legge
	 * @return the siac r bil elem atto legge
	 */
	public SiacRBilElemAttoLegge removeSiacRBilElemAttoLegge(SiacRBilElemAttoLegge siacRBilElemAttoLegge) {
		getSiacRBilElemAttoLegges().remove(siacRBilElemAttoLegge);
		siacRBilElemAttoLegge.setSiacTBilElem(null);

		return siacRBilElemAttoLegge;
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
		siacRBilElemAttr.setSiacTBilElem(this);

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
		siacRBilElemAttr.setSiacTBilElem(null);

		return siacRBilElemAttr;
	}

	public List<SiacRBilElemCategoria> getSiacRBilElemCategorias() {
		return this.siacRBilElemCategorias;
	}

	public void setSiacRBilElemCategorias(List<SiacRBilElemCategoria> siacRBilElemCategorias) {
		this.siacRBilElemCategorias = siacRBilElemCategorias;
	}

	public SiacRBilElemCategoria addSiacRBilElemCategoria(SiacRBilElemCategoria siacRBilElemCategoria) {
		getSiacRBilElemCategorias().add(siacRBilElemCategoria);
		siacRBilElemCategoria.setSiacTBilElem(this);

		return siacRBilElemCategoria;
	}

	public SiacRBilElemCategoria removeSiacRBilElemCategoria(SiacRBilElemCategoria siacRBilElemCategoria) {
		getSiacRBilElemCategorias().remove(siacRBilElemCategoria);
		siacRBilElemCategoria.setSiacTBilElem(null);

		return siacRBilElemCategoria;
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
		siacRBilElemClass.setSiacTBilElem(this);

		return siacRBilElemClass;
	}
	
	
	/**
	 * Adds the siac t class.
	 *
	 * @param siacTClass the siac t class
	 */
	public void addSiacTClass(SiacTClass siacTClass) {
		if (siacRBilElemClasses == null){
			siacRBilElemClasses = new ArrayList<SiacRBilElemClass>();
		}

		SiacRBilElemClass siacRBilElemClass = new SiacRBilElemClass();
		siacRBilElemClass.setSiacTClass(siacTClass);
		siacRBilElemClass.setSiacTBilElem(this);
		siacRBilElemClass.setSiacTEnteProprietario(this.getSiacTEnteProprietario());
		siacRBilElemClass.setLoginOperazione(this.getLoginOperazione());

		siacRBilElemClasses.add(siacRBilElemClass);
	}
	
	

	/**
	 * Removes the siac r bil elem class.
	 *
	 * @param siacRBilElemClass the siac r bil elem class
	 * @return the siac r bil elem class
	 */
	public SiacRBilElemClass removeSiacRBilElemClass(SiacRBilElemClass siacRBilElemClass) {
		getSiacRBilElemClasses().remove(siacRBilElemClass);
		siacRBilElemClass.setSiacTBilElem(null);

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
		siacRBilElemClassVar.setSiacTBilElem(this);

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
		siacRBilElemClassVar.setSiacTBilElem(null);

		return siacRBilElemClassVar;
	}

	/**
	 * Gets the siac r bil elem iva attivitas.
	 *
	 * @return the siac r bil elem iva attivitas
	 */
	public List<SiacRBilElemIvaAttivita> getSiacRBilElemIvaAttivitas() {
		return this.siacRBilElemIvaAttivitas;
	}

	/**
	 * Sets the siac r bil elem iva attivitas.
	 *
	 * @param siacRBilElemIvaAttivitas the new siac r bil elem iva attivitas
	 */
	public void setSiacRBilElemIvaAttivitas(List<SiacRBilElemIvaAttivita> siacRBilElemIvaAttivitas) {
		this.siacRBilElemIvaAttivitas = siacRBilElemIvaAttivitas;
	}

	/**
	 * Adds the siac r bil elem iva attivita.
	 *
	 * @param siacRBilElemIvaAttivita the siac r bil elem iva attivita
	 * @return the siac r bil elem iva attivita
	 */
	public SiacRBilElemIvaAttivita addSiacRBilElemIvaAttivita(SiacRBilElemIvaAttivita siacRBilElemIvaAttivita) {
		getSiacRBilElemIvaAttivitas().add(siacRBilElemIvaAttivita);
		siacRBilElemIvaAttivita.setSiacTBilElem(this);

		return siacRBilElemIvaAttivita;
	}

	/**
	 * Removes the siac r bil elem iva attivita.
	 *
	 * @param siacRBilElemIvaAttivita the siac r bil elem iva attivita
	 * @return the siac r bil elem iva attivita
	 */
	public SiacRBilElemIvaAttivita removeSiacRBilElemIvaAttivita(SiacRBilElemIvaAttivita siacRBilElemIvaAttivita) {
		getSiacRBilElemIvaAttivitas().remove(siacRBilElemIvaAttivita);
		siacRBilElemIvaAttivita.setSiacTBilElem(null);

		return siacRBilElemIvaAttivita;
	}
	
	/**
	 * Gets the siac r bil elem rel tempos.
	 *
	 * @return the siac r bil elem rel tempos
	 */
	public List<SiacRBilElemRelTempo> getSiacRBilElemRelTempos() {
		return this.siacRBilElemRelTempos;
	}

	/**
	 * Sets the siac r bil elem rel tempos.
	 *
	 * @param siacRBilElemRelTempos1 the new siac r bil elem rel tempos
	 */
	public void setSiacRBilElemRelTempos(List<SiacRBilElemRelTempo> siacRBilElemRelTempos1) {
		this.siacRBilElemRelTempos = siacRBilElemRelTempos1;
	}

	/**
	 * Adds the siac r bil elem rel tempos1.
	 *
	 * @param siacRBilElemRelTempos1 the siac r bil elem rel tempos1
	 * @return the siac r bil elem rel tempo
	 */
	public SiacRBilElemRelTempo addSiacRBilElemRelTempos1(SiacRBilElemRelTempo siacRBilElemRelTempos1) {
		getSiacRBilElemRelTempos().add(siacRBilElemRelTempos1);
		siacRBilElemRelTempos1.setSiacTBilElem(this);

		return siacRBilElemRelTempos1;
	}

	/**
	 * Removes the siac r bil elem rel tempos1.
	 *
	 * @param siacRBilElemRelTempos1 the siac r bil elem rel tempos1
	 * @return the siac r bil elem rel tempo
	 */
	public SiacRBilElemRelTempo removeSiacRBilElemRelTempos1(SiacRBilElemRelTempo siacRBilElemRelTempos1) {
		getSiacRBilElemRelTempos().remove(siacRBilElemRelTempos1);
		siacRBilElemRelTempos1.setSiacTBilElem(null);

		return siacRBilElemRelTempos1;
	}

	/**
	 * Gets the siac r bil elem rel tempo olds.
	 *
	 * @return the siac r bil elem rel tempo olds
	 */
	public List<SiacRBilElemRelTempo> getSiacRBilElemRelTempoOlds() {
		return this.siacRBilElemRelTempoOlds;
	}

	/**
	 * Sets the siac r bil elem rel tempo olds.
	 *
	 * @param siacRBilElemRelTempoOlds the new siac r bil elem rel tempo olds
	 */
	public void setSiacRBilElemRelTempoOlds(List<SiacRBilElemRelTempo> siacRBilElemRelTempoOlds) {
		this.siacRBilElemRelTempoOlds = siacRBilElemRelTempoOlds;
	}

	/**
	 * Adds the siac r bil elem rel tempos2.
	 *
	 * @param siacRBilElemRelTempos2 the siac r bil elem rel tempos2
	 * @return the siac r bil elem rel tempo
	 */
	public SiacRBilElemRelTempo addSiacRBilElemRelTempos2(SiacRBilElemRelTempo siacRBilElemRelTempos2) {
		getSiacRBilElemRelTempoOlds().add(siacRBilElemRelTempos2);
		siacRBilElemRelTempos2.setSiacTBilElemOld(this);

		return siacRBilElemRelTempos2;
	}

	/**
	 * Removes the siac r bil elem rel tempos2.
	 *
	 * @param siacRBilElemRelTempos2 the siac r bil elem rel tempos2
	 * @return the siac r bil elem rel tempo
	 */
	public SiacRBilElemRelTempo removeSiacRBilElemRelTempos2(SiacRBilElemRelTempo siacRBilElemRelTempos2) {
		getSiacRBilElemRelTempoOlds().remove(siacRBilElemRelTempos2);
		siacRBilElemRelTempos2.setSiacTBilElemOld(null);

		return siacRBilElemRelTempos2;
	}

	/**
	 * Gets the siac r bil elem statos.
	 *
	 * @return the siac r bil elem statos
	 */
	public List<SiacRBilElemStato> getSiacRBilElemStatos() {
		return this.siacRBilElemStatos;
	}

	/**
	 * Sets the siac r bil elem statos.
	 *
	 * @param siacRBilElemStatos the new siac r bil elem statos
	 */
	public void setSiacRBilElemStatos(List<SiacRBilElemStato> siacRBilElemStatos) {
		this.siacRBilElemStatos = siacRBilElemStatos;
	}

	/**
	 * Adds the siac r bil elem stato.
	 *
	 * @param siacRBilElemStato the siac r bil elem stato
	 * @return the siac r bil elem stato
	 */
	public SiacRBilElemStato addSiacRBilElemStato(SiacRBilElemStato siacRBilElemStato) {
		getSiacRBilElemStatos().add(siacRBilElemStato);
		siacRBilElemStato.setSiacTBilElem(this);

		return siacRBilElemStato;
	}

	/**
	 * Removes the siac r bil elem stato.
	 *
	 * @param siacRBilElemStato the siac r bil elem stato
	 * @return the siac r bil elem stato
	 */
	public SiacRBilElemStato removeSiacRBilElemStato(SiacRBilElemStato siacRBilElemStato) {
		getSiacRBilElemStatos().remove(siacRBilElemStato);
		siacRBilElemStato.setSiacTBilElem(null);

		return siacRBilElemStato;
	}

	public List<SiacRCausaleBilElem> getSiacRCausaleBilElems() {
		return this.siacRCausaleBilElems;
	}

	public void setSiacRCausaleBilElems(List<SiacRCausaleBilElem> siacRCausaleBilElems) {
		this.siacRCausaleBilElems = siacRCausaleBilElems;
	}

	public SiacRCausaleBilElem addSiacRCausaleBilElem(SiacRCausaleBilElem siacRCausaleBilElem) {
		getSiacRCausaleBilElems().add(siacRCausaleBilElem);
		siacRCausaleBilElem.setSiacTBilElem(this);

		return siacRCausaleBilElem;
	}

	public SiacRCausaleBilElem removeSiacRCausaleBilElem(SiacRCausaleBilElem siacRCausaleBilElem) {
		getSiacRCausaleBilElems().remove(siacRCausaleBilElem);
		siacRCausaleBilElem.setSiacTBilElem(null);

		return siacRCausaleBilElem;
	}

	public List<SiacRCronopElemBilElem> getSiacRCronopElemBilElems() {
		return this.siacRCronopElemBilElems;
	}

	public void setSiacRCronopElemBilElems(List<SiacRCronopElemBilElem> siacRCronopElemBilElems) {
		this.siacRCronopElemBilElems = siacRCronopElemBilElems;
	}

	public SiacRCronopElemBilElem addSiacRCronopElemBilElem(SiacRCronopElemBilElem siacRCronopElemBilElem) {
		getSiacRCronopElemBilElems().add(siacRCronopElemBilElem);
		siacRCronopElemBilElem.setSiacTBilElem(this);

		return siacRCronopElemBilElem;
	}

	public SiacRCronopElemBilElem removeSiacRCronopElemBilElem(SiacRCronopElemBilElem siacRCronopElemBilElem) {
		getSiacRCronopElemBilElems().remove(siacRCronopElemBilElem);
		siacRCronopElemBilElem.setSiacTBilElem(null);

		return siacRCronopElemBilElem;
	}

	public List<SiacRFondoEconBilElem> getSiacRFondoEconBilElems() {
		return this.siacRFondoEconBilElems;
	}

	public void setSiacRFondoEconBilElems(List<SiacRFondoEconBilElem> siacRFondoEconBilElems) {
		this.siacRFondoEconBilElems = siacRFondoEconBilElems;
	}

	public SiacRFondoEconBilElem addSiacRFondoEconBilElem(SiacRFondoEconBilElem siacRFondoEconBilElem) {
		getSiacRFondoEconBilElems().add(siacRFondoEconBilElem);
		siacRFondoEconBilElem.setSiacTBilElem(this);

		return siacRFondoEconBilElem;
	}

	public SiacRFondoEconBilElem removeSiacRFondoEconBilElem(SiacRFondoEconBilElem siacRFondoEconBilElem) {
		getSiacRFondoEconBilElems().remove(siacRFondoEconBilElem);
		siacRFondoEconBilElem.setSiacTBilElem(null);

		return siacRFondoEconBilElem;
	}

	public List<SiacRMovgestBilElem> getSiacRMovgestBilElems() {
		return this.siacRMovgestBilElems;
	}

	public void setSiacRMovgestBilElems(List<SiacRMovgestBilElem> siacRMovgestBilElems) {
		this.siacRMovgestBilElems = siacRMovgestBilElems;
	}

	public SiacRMovgestBilElem addSiacRMovgestBilElem(SiacRMovgestBilElem siacRMovgestBilElem) {
		getSiacRMovgestBilElems().add(siacRMovgestBilElem);
		siacRMovgestBilElem.setSiacTBilElem(this);

		return siacRMovgestBilElem;
	}

	public SiacRMovgestBilElem removeSiacRMovgestBilElem(SiacRMovgestBilElem siacRMovgestBilElem) {
		getSiacRMovgestBilElems().remove(siacRMovgestBilElem);
		siacRMovgestBilElem.setSiacTBilElem(null);

		return siacRMovgestBilElem;
	}

	public List<SiacROrdinativoBilElem> getSiacROrdinativoBilElems() {
		return this.siacROrdinativoBilElems;
	}

	public void setSiacROrdinativoBilElems(List<SiacROrdinativoBilElem> siacROrdinativoBilElems) {
		this.siacROrdinativoBilElems = siacROrdinativoBilElems;
	}

	public SiacROrdinativoBilElem addSiacROrdinativoBilElem(SiacROrdinativoBilElem siacROrdinativoBilElem) {
		getSiacROrdinativoBilElems().add(siacROrdinativoBilElem);
		siacROrdinativoBilElem.setSiacTBilElem(this);

		return siacROrdinativoBilElem;
	}

	public SiacROrdinativoBilElem removeSiacROrdinativoBilElem(SiacROrdinativoBilElem siacROrdinativoBilElem) {
		getSiacROrdinativoBilElems().remove(siacROrdinativoBilElem);
		siacROrdinativoBilElem.setSiacTBilElem(null);

		return siacROrdinativoBilElem;
	}

	public List<SiacRPredocBilElem> getSiacRPredocBilElems() {
		return this.siacRPredocBilElems;
	}

	public void setSiacRPredocBilElems(List<SiacRPredocBilElem> siacRPredocBilElems) {
		this.siacRPredocBilElems = siacRPredocBilElems;
	}

	public SiacRPredocBilElem addSiacRPredocBilElem(SiacRPredocBilElem siacRPredocBilElem) {
		getSiacRPredocBilElems().add(siacRPredocBilElem);
		siacRPredocBilElem.setSiacTBilElem(this);

		return siacRPredocBilElem;
	}

	public SiacRPredocBilElem removeSiacRPredocBilElem(SiacRPredocBilElem siacRPredocBilElem) {
		getSiacRPredocBilElems().remove(siacRPredocBilElem);
		siacRPredocBilElem.setSiacTBilElem(null);

		return siacRPredocBilElem;
	}

	public List<SiacRVincoloBilElem> getSiacRVincoloBilElems() {
		return this.siacRVincoloBilElems;
	}

	public void setSiacRVincoloBilElems(List<SiacRVincoloBilElem> siacRVincoloBilElems) {
		this.siacRVincoloBilElems = siacRVincoloBilElems;
	}

	public SiacRVincoloBilElem addSiacRVincoloBilElem(SiacRVincoloBilElem siacRVincoloBilElem) {
		getSiacRVincoloBilElems().add(siacRVincoloBilElem);
		siacRVincoloBilElem.setSiacTBilElem(this);

		return siacRVincoloBilElem;
	}

	public SiacRVincoloBilElem removeSiacRVincoloBilElem(SiacRVincoloBilElem siacRVincoloBilElem) {
		getSiacRVincoloBilElems().remove(siacRVincoloBilElem);
		siacRVincoloBilElem.setSiacTBilElem(null);

		return siacRVincoloBilElem;
	}

	
	public List<SiacRBilElemStipendioCodice> getSiacRBilElemStipendioCodices() {
		return siacRBilElemStipendioCodices;
	}

	
	public void setSiacRBilElemStipendioCodices(
			List<SiacRBilElemStipendioCodice> siacRBilElemStipendioCodices) {
		this.siacRBilElemStipendioCodices = siacRBilElemStipendioCodices;
	}
	
	public SiacRBilElemStipendioCodice addSiacRBilElemStipendioCodice(SiacRBilElemStipendioCodice siacRBilElemStipendioCodice) {
		getSiacRBilElemStipendioCodices().add(siacRBilElemStipendioCodice);
		siacRBilElemStipendioCodice.setSiacTBilElem(this);

		return siacRBilElemStipendioCodice;
	}

	public SiacRBilElemStipendioCodice removeSiacRBilElemStipendioCodice(SiacRBilElemStipendioCodice siacRBilElemStipendioCodice) {
		getSiacRBilElemStipendioCodices().remove(siacRBilElemStipendioCodice);
		siacRBilElemStipendioCodice.setSiacTBilElem(null);

		return siacRBilElemStipendioCodice;
	}

	/**
	 * Gets the siac d bil elem tipo.
	 *
	 * @return the siac d bil elem tipo
	 */
	public SiacDBilElemTipo getSiacDBilElemTipo() {
		return this.siacDBilElemTipo;
	}

	/**
	 * Sets the siac d bil elem tipo.
	 *
	 * @param siacDBilElemTipo the new siac d bil elem tipo
	 */
	public void setSiacDBilElemTipo(SiacDBilElemTipo siacDBilElemTipo) {
		this.siacDBilElemTipo = siacDBilElemTipo;
	}

	/**
	 * Gets the siac t bil.
	 *
	 * @return the siac t bil
	 */
	public SiacTBil getSiacTBil() {
		return this.siacTBil;
	}

	/**
	 * Sets the siac t bil.
	 *
	 * @param siacTBil the new siac t bil
	 */
	public void setSiacTBil(SiacTBil siacTBil) {
		this.siacTBil = siacTBil;
	}



	/**
	 * Gets the siac t bil elem dets.
	 *
	 * @return the siac t bil elem dets
	 */
	public List<SiacTBilElemDet> getSiacTBilElemDets() {
		return this.siacTBilElemDets;
	}

	/**
	 * Sets the siac t bil elem dets.
	 *
	 * @param siacTBilElemDets the new siac t bil elem dets
	 */
	public void setSiacTBilElemDets(List<SiacTBilElemDet> siacTBilElemDets) {
		this.siacTBilElemDets = siacTBilElemDets;
	}

	/**
	 * Adds the siac t bil elem det.
	 *
	 * @param siacTBilElemDet the siac t bil elem det
	 * @return the siac t bil elem det
	 */
	public SiacTBilElemDet addSiacTBilElemDet(SiacTBilElemDet siacTBilElemDet) {
		getSiacTBilElemDets().add(siacTBilElemDet);
		siacTBilElemDet.setSiacTBilElem(this);

		return siacTBilElemDet;
	}

	/**
	 * Removes the siac t bil elem det.
	 *
	 * @param siacTBilElemDet the siac t bil elem det
	 * @return the siac t bil elem det
	 */
	public SiacTBilElemDet removeSiacTBilElemDet(SiacTBilElemDet siacTBilElemDet) {
		getSiacTBilElemDets().remove(siacTBilElemDet);
		siacTBilElemDet.setSiacTBilElem(null);

		return siacTBilElemDet;
	}

	/**
	 * Gets the siac t bil elem det vars.
	 *
	 * @return the siac t bil elem det vars
	 */
	public List<SiacTBilElemDetVar> getSiacTBilElemDetVars() {
		return this.siacTBilElemDetVars;
	}

	/**
	 * Sets the siac t bil elem det vars.
	 *
	 * @param siacTBilElemDetVars the new siac t bil elem det vars
	 */
	public void setSiacTBilElemDetVars(List<SiacTBilElemDetVar> siacTBilElemDetVars) {
		this.siacTBilElemDetVars = siacTBilElemDetVars;
	}

	/**
	 * Adds the siac t bil elem det var.
	 *
	 * @param siacTBilElemDetVar the siac t bil elem det var
	 * @return the siac t bil elem det var
	 */
	public SiacTBilElemDetVar addSiacTBilElemDetVar(SiacTBilElemDetVar siacTBilElemDetVar) {
		getSiacTBilElemDetVars().add(siacTBilElemDetVar);
		siacTBilElemDetVar.setSiacTBilElem(this);

		return siacTBilElemDetVar;
	}

	/**
	 * Removes the siac t bil elem det var.
	 *
	 * @param siacTBilElemDetVar the siac t bil elem det var
	 * @return the siac t bil elem det var
	 */
	public SiacTBilElemDetVar removeSiacTBilElemDetVar(SiacTBilElemDetVar siacTBilElemDetVar) {
		getSiacTBilElemDetVars().remove(siacTBilElemDetVar);
		siacTBilElemDetVar.setSiacTBilElem(null);

		return siacTBilElemDetVar;
	}

	/**
	 * Gets the siac t bil elem vars.
	 *
	 * @return the siac t bil elem vars
	 */
	public List<SiacTBilElemVar> getSiacTBilElemVars() {
		return this.siacTBilElemVars;
	}

	/**
	 * Sets the siac t bil elem vars.
	 *
	 * @param siacTBilElemVars the new siac t bil elem vars
	 */
	public void setSiacTBilElemVars(List<SiacTBilElemVar> siacTBilElemVars) {
		this.siacTBilElemVars = siacTBilElemVars;
	}

	/**
	 * Adds the siac t bil elem var.
	 *
	 * @param siacTBilElemVar the siac t bil elem var
	 * @return the siac t bil elem var
	 */
	public SiacTBilElemVar addSiacTBilElemVar(SiacTBilElemVar siacTBilElemVar) {
		getSiacTBilElemVars().add(siacTBilElemVar);
		siacTBilElemVar.setSiacTBilElem(this);

		return siacTBilElemVar;
	}

	/**
	 * Removes the siac t bil elem var.
	 *
	 * @param siacTBilElemVar the siac t bil elem var
	 * @return the siac t bil elem var
	 */
	public SiacTBilElemVar removeSiacTBilElemVar(SiacTBilElemVar siacTBilElemVar) {
		getSiacTBilElemVars().remove(siacTBilElemVar);
		siacTBilElemVar.setSiacTBilElem(null);

		return siacTBilElemVar;
	}

	public List<SiacRConciliazioneBeneficiario> getSiacRConciliazioneBeneficiarios() {
		return this.siacRConciliazioneBeneficiarios;
	}

	public void setSiacRConciliazioneBeneficiarios(List<SiacRConciliazioneBeneficiario> siacRConciliazioneBeneficiarios) {
		this.siacRConciliazioneBeneficiarios = siacRConciliazioneBeneficiarios;
	}

	public SiacRConciliazioneBeneficiario addSiacRConciliazioneBeneficiario(SiacRConciliazioneBeneficiario siacRConciliazioneBeneficiario) {
		getSiacRConciliazioneBeneficiarios().add(siacRConciliazioneBeneficiario);
		siacRConciliazioneBeneficiario.setSiacTBilElem(this);

		return siacRConciliazioneBeneficiario;
	}

	public SiacRConciliazioneBeneficiario removeSiacRConciliazioneBeneficiario(SiacRConciliazioneBeneficiario siacRConciliazioneBeneficiario) {
		getSiacRConciliazioneBeneficiarios().remove(siacRConciliazioneBeneficiario);
		siacRConciliazioneBeneficiario.setSiacTBilElem(null);

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
		siacRConciliazioneCapitolo.setSiacTBilElem(this);

		return siacRConciliazioneCapitolo;
	}

	public SiacRConciliazioneCapitolo removeSiacRConciliazioneCapitolo(SiacRConciliazioneCapitolo siacRConciliazioneCapitolo) {
		getSiacRConciliazioneCapitolos().remove(siacRConciliazioneCapitolo);
		siacRConciliazioneCapitolo.setSiacTBilElem(null);

		return siacRConciliazioneCapitolo;
	}
	
	public List<SiacRBilElemAccFondiDubbiaEsig> getSiacRBilElemAccFondiDubbiaEsigs() {
		return this.siacRBilElemAccFondiDubbiaEsigs;
	}

	public void setSiacRBilElemAccFondiDubbiaEsigs(List<SiacRBilElemAccFondiDubbiaEsig> siacRBilElemAccFondiDubbiaEsigs) {
		this.siacRBilElemAccFondiDubbiaEsigs = siacRBilElemAccFondiDubbiaEsigs;
	}

	public SiacRBilElemAccFondiDubbiaEsig addSiacRBilElemAccFondiDubbiaEsig(SiacRBilElemAccFondiDubbiaEsig siacRBilElemAccFondiDubbiaEsig) {
		getSiacRBilElemAccFondiDubbiaEsigs().add(siacRBilElemAccFondiDubbiaEsig);
		siacRBilElemAccFondiDubbiaEsig.setSiacTBilElem(this);

		return siacRBilElemAccFondiDubbiaEsig;
	}

	public SiacRBilElemAccFondiDubbiaEsig removeSiacRBilElemAccFondiDubbiaEsig(SiacRBilElemAccFondiDubbiaEsig siacRBilElemAccFondiDubbiaEsig) {
		getSiacRBilElemAccFondiDubbiaEsigs().remove(siacRBilElemAccFondiDubbiaEsig);
		siacRBilElemAccFondiDubbiaEsig.setSiacTBilElem(null);

		return siacRBilElemAccFondiDubbiaEsig;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return elemId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.elemId = uid;
		
	}

}