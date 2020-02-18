/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the siac_r_variazione_attr database table.
 */
@Entity
@Table(name="siac_r_variazione_attr")
public class SiacRVariazioneAttr extends SiacTEnteBase implements SiacRAttrBase<SiacTVariazione> {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The variazione attr id. */
	@Id
	@SequenceGenerator(name="SIAC_R_VARIAZIONE_ATTR_VARIAZIONEATTRID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_VARIAZIONE_ATTR_VARIAZIONE_ATTR_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_VARIAZIONE_ATTR_VARIAZIONEATTRID_GENERATOR")
	@Column(name="variazione_attr_id")
	private Integer variazioneAttrId;

	/** The boolean_. */
	@Column(name="boolean")
	private String boolean_;
	
	/** The numerico. */
	private BigDecimal numerico;

	/** The percentuale. */
	private BigDecimal percentuale;

	/** The tabella id. */
	@Column(name="tabella_id")
	private Integer tabellaId;

	/** The testo. */
	private String testo;

	//bi-directional many-to-one association to SiacTAttr
	/** The siac t attr. */
	@ManyToOne
	@JoinColumn(name="attr_id")
	private SiacTAttr siacTAttr;

	//bi-directional many-to-one association to SiacTVariazione
	/** The siac t variazione. */
	@ManyToOne
	@JoinColumn(name="variazione_id")
	private SiacTVariazione siacTVariazione;

	/**
	 * Instantiates a new siac r variazione attr.
	 */
	public SiacRVariazioneAttr() {
	}

	/**
	 * Gets the variazione attr id.
	 *
	 * @return the variazione attr id
	 */
	public Integer getVariazioneAttrId() {
		return this.variazioneAttrId;
	}

	/**
	 * Sets the variazione attr id.
	 *
	 * @param variazioneAttrId the new variazione attr id
	 */
	public void setVariazioneAttrId(Integer variazioneAttrId) {
		this.variazioneAttrId = variazioneAttrId;
	}

	@Override
	public String getBoolean_() {
		return this.boolean_;
	}

	@Override
	public void setBoolean_(String boolean_) {
		this.boolean_ = boolean_;
	}

	@Override
	public BigDecimal getNumerico() {
		return this.numerico;
	}

	@Override
	public void setNumerico(BigDecimal numerico) {
		this.numerico = numerico;
	}

	@Override
	public BigDecimal getPercentuale() {
		return this.percentuale;
	}

	@Override
	public void setPercentuale(BigDecimal percentuale) {
		this.percentuale = percentuale;
	}

	@Override
	public Integer getTabellaId() {
		return this.tabellaId;
	}

	@Override
	public void setTabellaId(Integer tabellaId) {
		this.tabellaId = tabellaId;
	}

	@Override
	public String getTesto() {
		return this.testo;
	}

	@Override
	public void setTesto(String testo) {
		this.testo = testo;
	}

	@Override
	public SiacTAttr getSiacTAttr() {
		return this.siacTAttr;
	}

	@Override
	public void setSiacTAttr(SiacTAttr siacTAttr) {
		this.siacTAttr = siacTAttr;
	}

	/**
	 * Gets the siac t variazione.
	 *
	 * @return the siac t variazione
	 */
	public SiacTVariazione getSiacTVariazione() {
		return this.siacTVariazione;
	}

	/**
	 * Sets the siac t variazione.
	 *
	 * @param siacTVariazione the new siac t variazione
	 */
	public void setSiacTVariazione(SiacTVariazione siacTVariazione) {
		this.siacTVariazione = siacTVariazione;
	}

	@Override
	public Integer getUid() {
		return variazioneAttrId;
	}

	@Override
	public void setUid(Integer uid) {
		this.variazioneAttrId = uid;
		
	}

	@Override
	public void setEntity(SiacTVariazione entity) {
		setSiacTVariazione(entity);
	}

}