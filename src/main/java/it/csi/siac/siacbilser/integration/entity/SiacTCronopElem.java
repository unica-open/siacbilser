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


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_t_cronop_elem database table.
 * 
 */
@Entity
@Table(name="siac_t_cronop_elem")
@NamedQuery(name="SiacTCronopElem.findAll", query="SELECT s FROM SiacTCronopElem s")
public class SiacTCronopElem extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The cronop elem id. */
	@Id
	@SequenceGenerator(name="SIAC_T_CRONOP_ELEM_CRONOPELEMID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_CRONOP_ELEM_CRONOP_ELEM_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_CRONOP_ELEM_CRONOPELEMID_GENERATOR")
	@Column(name="cronop_elem_id")
	private Integer cronopElemId;

	/** The cronop elem code. */
	@Column(name="cronop_elem_code")
	private String cronopElemCode;

	/** The cronop elem code2. */
	@Column(name="cronop_elem_code2")
	private String cronopElemCode2;

	/** The cronop elem code3. */
	@Column(name="cronop_elem_code3")
	private String cronopElemCode3;

	/** The cronop elem desc. */
	@Column(name="cronop_elem_desc")
	private String cronopElemDesc;

	/** The cronop elem desc2. */
	@Column(name="cronop_elem_desc2")
	private String cronopElemDesc2;

	
	//SIAC-4103 aggiunto campo "avanzo Di Amministrazione"
	/** The cronop elem desc2. */
	@Column(name="cronop_elem_is_ava_amm")
	private Boolean cronopElemIsAvaAmm;
	
	/** The livello. */
	private Integer livello;

	
	/** The ordine. */
	private String ordine;

	
	//bi-directional many-to-one association to SiacRCronopElemBilElem
	/** The siac r cronop elem bil elems. */
	@OneToMany(mappedBy="siacTCronopElem", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRCronopElemBilElem> siacRCronopElemBilElems;

	//bi-directional many-to-one association to SiacRCronopElemClass
	/** The siac r cronop elem classes. */
	@OneToMany(mappedBy="siacTCronopElem", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRCronopElemClass> siacRCronopElemClasses;

	//bi-directional many-to-one association to SiacDBilElemTipo
	/** The siac d bil elem tipo. */
	@ManyToOne
	@JoinColumn(name="elem_tipo_id")
	private SiacDBilElemTipo siacDBilElemTipo;

	//bi-directional many-to-one association to SiacTCronop
	/** The siac t cronop. */
	@ManyToOne
	@JoinColumn(name="cronop_id")
	private SiacTCronop siacTCronop;

	//bi-directional many-to-one association to SiacTCronopElem
	/** The siac t cronop elem. */
	@ManyToOne
	@JoinColumn(name="cronop_elem_id_padre")
	private SiacTCronopElem siacTCronopElem;

	//bi-directional many-to-one association to SiacTCronopElem
	/** The siac t cronop elems. */
	@OneToMany(mappedBy="siacTCronopElem")
	private List<SiacTCronopElem> siacTCronopElems;

	//bi-directional many-to-one association to SiacTCronopElemDet
	/** The siac t cronop elem dets. */
	@OneToMany(mappedBy="siacTCronopElem", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacTCronopElemDet> siacTCronopElemDets;

	/**
	 * Instantiates a new siac t cronop elem.
	 */
	public SiacTCronopElem() {
	}

	/**
	 * Gets the cronop elem id.
	 *
	 * @return the cronop elem id
	 */
	public Integer getCronopElemId() {
		return this.cronopElemId;
	}

	/**
	 * Sets the cronop elem id.
	 *
	 * @param cronopElemId the new cronop elem id
	 */
	public void setCronopElemId(Integer cronopElemId) {
		this.cronopElemId = cronopElemId;
	}

	/**
	 * Gets the cronop elem code.
	 *
	 * @return the cronop elem code
	 */
	public String getCronopElemCode() {
		return this.cronopElemCode;
	}

	/**
	 * Sets the cronop elem code.
	 *
	 * @param cronopElemCode the new cronop elem code
	 */
	public void setCronopElemCode(String cronopElemCode) {
		this.cronopElemCode = cronopElemCode;
	}

	/**
	 * Gets the cronop elem code2.
	 *
	 * @return the cronop elem code2
	 */
	public String getCronopElemCode2() {
		return this.cronopElemCode2;
	}

	/**
	 * Sets the cronop elem code2.
	 *
	 * @param cronopElemCode2 the new cronop elem code2
	 */
	public void setCronopElemCode2(String cronopElemCode2) {
		this.cronopElemCode2 = cronopElemCode2;
	}

	/**
	 * Gets the cronop elem code3.
	 *
	 * @return the cronop elem code3
	 */
	public String getCronopElemCode3() {
		return this.cronopElemCode3;
	}

	/**
	 * Sets the cronop elem code3.
	 *
	 * @param cronopElemCode3 the new cronop elem code3
	 */
	public void setCronopElemCode3(String cronopElemCode3) {
		this.cronopElemCode3 = cronopElemCode3;
	}

	/**
	 * Gets the cronop elem desc.
	 *
	 * @return the cronop elem desc
	 */
	public String getCronopElemDesc() {
		return this.cronopElemDesc;
	}

	/**
	 * Sets the cronop elem desc.
	 *
	 * @param cronopElemDesc the new cronop elem desc
	 */
	public void setCronopElemDesc(String cronopElemDesc) {
		this.cronopElemDesc = cronopElemDesc;
	}

	/**
	 * Gets the cronop elem desc2.
	 *
	 * @return the cronop elem desc2
	 */
	public String getCronopElemDesc2() {
		return this.cronopElemDesc2;
	}

	/**
	 * Sets the cronop elem desc2.
	 *
	 * @param cronopElemDesc2 the new cronop elem desc2
	 */
	public void setCronopElemDesc2(String cronopElemDesc2) {
		this.cronopElemDesc2 = cronopElemDesc2;
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
	 * Gets the siac r cronop elem bil elems.
	 *
	 * @return the siac r cronop elem bil elems
	 */
	public List<SiacRCronopElemBilElem> getSiacRCronopElemBilElems() {
		return this.siacRCronopElemBilElems;
	}

	/**
	 * Sets the siac r cronop elem bil elems.
	 *
	 * @param siacRCronopElemBilElems the new siac r cronop elem bil elems
	 */
	public void setSiacRCronopElemBilElems(List<SiacRCronopElemBilElem> siacRCronopElemBilElems) {
		this.siacRCronopElemBilElems = siacRCronopElemBilElems;
	}

	/**
	 * Adds the siac r cronop elem bil elem.
	 *
	 * @param siacRCronopElemBilElem the siac r cronop elem bil elem
	 * @return the siac r cronop elem bil elem
	 */
	public SiacRCronopElemBilElem addSiacRCronopElemBilElem(SiacRCronopElemBilElem siacRCronopElemBilElem) {
		getSiacRCronopElemBilElems().add(siacRCronopElemBilElem);
		siacRCronopElemBilElem.setSiacTCronopElem(this);

		return siacRCronopElemBilElem;
	}

	/**
	 * Removes the siac r cronop elem bil elem.
	 *
	 * @param siacRCronopElemBilElem the siac r cronop elem bil elem
	 * @return the siac r cronop elem bil elem
	 */
	public SiacRCronopElemBilElem removeSiacRCronopElemBilElem(SiacRCronopElemBilElem siacRCronopElemBilElem) {
		getSiacRCronopElemBilElems().remove(siacRCronopElemBilElem);
		siacRCronopElemBilElem.setSiacTCronopElem(null);

		return siacRCronopElemBilElem;
	}

	/**
	 * Gets the siac r cronop elem classes.
	 *
	 * @return the siac r cronop elem classes
	 */
	public List<SiacRCronopElemClass> getSiacRCronopElemClasses() {
		return this.siacRCronopElemClasses;
	}

	/**
	 * Sets the siac r cronop elem classes.
	 *
	 * @param siacRCronopElemClasses the new siac r cronop elem classes
	 */
	public void setSiacRCronopElemClasses(List<SiacRCronopElemClass> siacRCronopElemClasses) {
		this.siacRCronopElemClasses = siacRCronopElemClasses;
	}

	/**
	 * Adds the siac r cronop elem class.
	 *
	 * @param siacRCronopElemClass the siac r cronop elem class
	 * @return the siac r cronop elem class
	 */
	public SiacRCronopElemClass addSiacRCronopElemClass(SiacRCronopElemClass siacRCronopElemClass) {
		getSiacRCronopElemClasses().add(siacRCronopElemClass);
		siacRCronopElemClass.setSiacTCronopElem(this);

		return siacRCronopElemClass;
	}

	/**
	 * Removes the siac r cronop elem class.
	 *
	 * @param siacRCronopElemClass the siac r cronop elem class
	 * @return the siac r cronop elem class
	 */
	public SiacRCronopElemClass removeSiacRCronopElemClass(SiacRCronopElemClass siacRCronopElemClass) {
		getSiacRCronopElemClasses().remove(siacRCronopElemClass);
		siacRCronopElemClass.setSiacTCronopElem(null);

		return siacRCronopElemClass;
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
	 * Gets the siac t cronop.
	 *
	 * @return the siac t cronop
	 */
	public SiacTCronop getSiacTCronop() {
		return this.siacTCronop;
	}

	/**
	 * Sets the siac t cronop.
	 *
	 * @param siacTCronop the new siac t cronop
	 */
	public void setSiacTCronop(SiacTCronop siacTCronop) {
		this.siacTCronop = siacTCronop;
	}

	/**
	 * Gets the siac t cronop elem.
	 *
	 * @return the siac t cronop elem
	 */
	public SiacTCronopElem getSiacTCronopElem() {
		return this.siacTCronopElem;
	}

	/**
	 * Sets the siac t cronop elem.
	 *
	 * @param siacTCronopElem the new siac t cronop elem
	 */
	public void setSiacTCronopElem(SiacTCronopElem siacTCronopElem) {
		this.siacTCronopElem = siacTCronopElem;
	}

	/**
	 * Gets the siac t cronop elems.
	 *
	 * @return the siac t cronop elems
	 */
	public List<SiacTCronopElem> getSiacTCronopElems() {
		return this.siacTCronopElems;
	}

	/**
	 * Sets the siac t cronop elems.
	 *
	 * @param siacTCronopElems the new siac t cronop elems
	 */
	public void setSiacTCronopElems(List<SiacTCronopElem> siacTCronopElems) {
		this.siacTCronopElems = siacTCronopElems;
	}

	/**
	 * Adds the siac t cronop elem.
	 *
	 * @param siacTCronopElem the siac t cronop elem
	 * @return the siac t cronop elem
	 */
	public SiacTCronopElem addSiacTCronopElem(SiacTCronopElem siacTCronopElem) {
		getSiacTCronopElems().add(siacTCronopElem);
		siacTCronopElem.setSiacTCronopElem(this);

		return siacTCronopElem;
	}

	/**
	 * Removes the siac t cronop elem.
	 *
	 * @param siacTCronopElem the siac t cronop elem
	 * @return the siac t cronop elem
	 */
	public SiacTCronopElem removeSiacTCronopElem(SiacTCronopElem siacTCronopElem) {
		getSiacTCronopElems().remove(siacTCronopElem);
		siacTCronopElem.setSiacTCronopElem(null);

		return siacTCronopElem;
	}

	/**
	 * Gets the siac t cronop elem dets.
	 *
	 * @return the siac t cronop elem dets
	 */
	public List<SiacTCronopElemDet> getSiacTCronopElemDets() {
		return this.siacTCronopElemDets;
	}

	/**
	 * Sets the siac t cronop elem dets.
	 *
	 * @param siacTCronopElemDets the new siac t cronop elem dets
	 */
	public void setSiacTCronopElemDets(List<SiacTCronopElemDet> siacTCronopElemDets) {
		this.siacTCronopElemDets = siacTCronopElemDets;
	}

	/**
	 * Adds the siac t cronop elem det.
	 *
	 * @param siacTCronopElemDet the siac t cronop elem det
	 * @return the siac t cronop elem det
	 */
	public SiacTCronopElemDet addSiacTCronopElemDet(SiacTCronopElemDet siacTCronopElemDet) {
		getSiacTCronopElemDets().add(siacTCronopElemDet);
		siacTCronopElemDet.setSiacTCronopElem(this);

		return siacTCronopElemDet;
	}

	/**
	 * Removes the siac t cronop elem det.
	 *
	 * @param siacTCronopElemDet the siac t cronop elem det
	 * @return the siac t cronop elem det
	 */
	public SiacTCronopElemDet removeSiacTCronopElemDet(SiacTCronopElemDet siacTCronopElemDet) {
		getSiacTCronopElemDets().remove(siacTCronopElemDet);
		siacTCronopElemDet.setSiacTCronopElem(null);

		return siacTCronopElemDet;
	}

	/**
	 * @return the cronopElemIsAvaAmm
	 */
	public Boolean getCronopElemIsAvaAmm() {
		return cronopElemIsAvaAmm;
	}

	/**
	 * @param cronopElemIsAvaAmm the cronopElemIsAvaAmm to set
	 */
	public void setCronopElemIsAvaAmm(Boolean cronopElemIsAvaAmm) {
		this.cronopElemIsAvaAmm = cronopElemIsAvaAmm;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return cronopElemId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.cronopElemId = uid;
		
	}


}