/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_t_bil_elem_var database table.
 * 
 */
@Entity
@Table(name="siac_t_bil_elem_var")
public class SiacTBilElemVar extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The elem var id. */
	@Id
	@SequenceGenerator(name="SIAC_T_BIL_ELEM_VAR_ELEMVARID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_BIL_ELEM_VAR_ELEM_VAR_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_BIL_ELEM_VAR_ELEMVARID_GENERATOR")
	@Column(name="elem_var_id")
	private Integer elemVarId;

	/** The bil id. */
	@Column(name="bil_id")
	private Integer bilId;

	/** The elem code. */
	@Column(name="elem_code")
	private String elemCode;
	
	/** The elem code. */
	@Column(name="elem_code_prec")
	private String elemCodePrec;

	/** The elem code2. */
	@Column(name="elem_code2")
	private String elemCode2;
	
	/** The elem code2. */
	@Column(name="elem_code2_prec")
	private String elemCode2Prec;

	/** The elem code3. */
	@Column(name="elem_code3")
	private String elemCode3;
	
	/** The elem code3. */
	@Column(name="elem_code3_prec")
	private String elemCode3Prec;

	/** The elem desc. */
	@Column(name="elem_desc")
	private String elemDesc;
	
	/** The elem desc. */
	@Column(name="elem_desc_prec")
	private String elemDescPrec;

	/** The elem desc2. */
	@Column(name="elem_desc2")
	private String elemDesc2;
	
	/** The elem desc2. */
	@Column(name="elem_desc2_prec")
	private String elemDesc2Prec;
	
//	siacTBilElemVar.setElemCodePrec(siacTBilElem.getElemCode());
//	siacTBilElemVar.setElemCode2Prec(siacTBilElem.getElemCode2());
//	siacTBilElemVar.setElemCode3Prec(siacTBilElem.getElemCode3());
//	siacTBilElemVar.setElemDescPrec(siacTBilElem.getElemDesc());
//	siacTBilElemVar.setElemDesc2Prec(siacTBilElem.getElemDesc2());
	
	
	

	/** The elem id padre. */
	@Column(name="elem_id_padre")
	private Integer elemIdPadre;

	/** The elem tipo id. */
	@Column(name="elem_tipo_id")
	private Integer elemTipoId;

	/** The livello. */
	private Integer livello;

	/** The ordine. */
	private String ordine;

	/** The periodo id. */
	@Column(name="periodo_id")
	private Integer periodoId;

	//bi-directional many-to-one association to SiacRVariazioneStato
	/** The siac r variazione stato. */
	@ManyToOne
	@JoinColumn(name="variazione_stato_id")
	private SiacRVariazioneStato siacRVariazioneStato;

	//bi-directional many-to-one association to SiacTBilElem
	/** The siac t bil elem. */
	@ManyToOne
	@JoinColumn(name="elem_id")
	private SiacTBilElem siacTBilElem;

	/**
	 * Instantiates a new siac t bil elem var.
	 */
	public SiacTBilElemVar() {
	}

	/**
	 * Gets the elem var id.
	 *
	 * @return the elem var id
	 */
	public Integer getElemVarId() {
		return this.elemVarId;
	}

	/**
	 * Sets the elem var id.
	 *
	 * @param elemVarId the new elem var id
	 */
	public void setElemVarId(Integer elemVarId) {
		this.elemVarId = elemVarId;
	}

	/**
	 * Gets the bil id.
	 *
	 * @return the bil id
	 */
	public Integer getBilId() {
		return this.bilId;
	}

	/**
	 * Sets the bil id.
	 *
	 * @param bilId the new bil id
	 */
	public void setBilId(Integer bilId) {
		this.bilId = bilId;
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
	 * Gets the elem tipo id.
	 *
	 * @return the elem tipo id
	 */
	public Integer getElemTipoId() {
		return this.elemTipoId;
	}

	/**
	 * Sets the elem tipo id.
	 *
	 * @param elemTipoId the new elem tipo id
	 */
	public void setElemTipoId(Integer elemTipoId) {
		this.elemTipoId = elemTipoId;
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
	 * Gets the periodo id.
	 *
	 * @return the periodo id
	 */
	public Integer getPeriodoId() {
		return this.periodoId;
	}

	/**
	 * Sets the periodo id.
	 *
	 * @param periodoId the new periodo id
	 */
	public void setPeriodoId(Integer periodoId) {
		this.periodoId = periodoId;
	}

	/**
	 * Gets the siac r variazione stato.
	 *
	 * @return the siac r variazione stato
	 */
	public SiacRVariazioneStato getSiacRVariazioneStato() {
		return this.siacRVariazioneStato;
	}

	/**
	 * Sets the siac r variazione stato.
	 *
	 * @param siacRVariazioneStato the new siac r variazione stato
	 */
	public void setSiacRVariazioneStato(SiacRVariazioneStato siacRVariazioneStato) {
		this.siacRVariazioneStato = siacRVariazioneStato;
	}

	/**
	 * Gets the siac t bil elem.
	 *
	 * @return the siac t bil elem
	 */
	public SiacTBilElem getSiacTBilElem() {
		return this.siacTBilElem;
	}

	/**
	 * Sets the siac t bil elem.
	 *
	 * @param siacTBilElem the new siac t bil elem
	 */
	public void setSiacTBilElem(SiacTBilElem siacTBilElem) {
		this.siacTBilElem = siacTBilElem;
	}
	
	/**
	 * @return the elemCodePrec
	 */
	public String getElemCodePrec() {
		return elemCodePrec;
	}

	/**
	 * @param elemCodePrec the elemCodePrec to set
	 */
	public void setElemCodePrec(String elemCodePrec) {
		this.elemCodePrec = elemCodePrec;
	}

	/**
	 * @return the elemCode2Prec
	 */
	public String getElemCode2Prec() {
		return elemCode2Prec;
	}

	/**
	 * @param elemCode2Prec the elemCode2Prec to set
	 */
	public void setElemCode2Prec(String elemCode2Prec) {
		this.elemCode2Prec = elemCode2Prec;
	}

	/**
	 * @return the elemCode3Prec
	 */
	public String getElemCode3Prec() {
		return elemCode3Prec;
	}

	/**
	 * @param elemCode3Prec the elemCode3Prec to set
	 */
	public void setElemCode3Prec(String elemCode3Prec) {
		this.elemCode3Prec = elemCode3Prec;
	}

	/**
	 * @return the elemDescPrec
	 */
	public String getElemDescPrec() {
		return elemDescPrec;
	}

	/**
	 * @param elemDescPrec the elemDescPrec to set
	 */
	public void setElemDescPrec(String elemDescPrec) {
		this.elemDescPrec = elemDescPrec;
	}

	/**
	 * @return the elemDesc2Prec
	 */
	public String getElemDesc2Prec() {
		return elemDesc2Prec;
	}

	/**
	 * @param elemDesc2Prec the elemDesc2Prec to set
	 */
	public void setElemDesc2Prec(String elemDesc2Prec) {
		this.elemDesc2Prec = elemDesc2Prec;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return elemVarId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.elemVarId = uid;
		
	}

}