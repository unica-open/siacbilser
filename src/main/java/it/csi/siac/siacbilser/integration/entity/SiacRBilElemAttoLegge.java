/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.util.Date;

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
 * The persistent class for the siac_r_bil_elem_atto_legge database table.
 * 
 */
@Entity
@Table(name="siac_r_bil_elem_atto_legge")
public class SiacRBilElemAttoLegge extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The attolegge bil elem id. */
	@Id
	@SequenceGenerator(name="SIAC_R_BIL_ELEM_ATTO_LEGGE_ATTOLEGGEBILELEMID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_BIL_ELEM_ATTO_LEGGE_ATTOLEGGE_BIL_ELEM_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_BIL_ELEM_ATTO_LEGGE_ATTOLEGGEBILELEMID_GENERATOR")
	@Column(name="attolegge_bil_elem_id")
	private Integer attoleggeBilElemId;

	/** The descrizione. */
	private String descrizione;

	/** The finanziamento fine. */
	@Column(name="finanziamento_fine")
	private Date finanziamentoFine;

	/** The finanziamento inizio. */
	@Column(name="finanziamento_inizio")
	private Date finanziamentoInizio;

	/** The gerarchia. */
	private String gerarchia;

	//bi-directional many-to-one association to SiacTAttoLegge
	/** The siac t atto legge. */
	@ManyToOne
	@JoinColumn(name="attolegge_id")
	private SiacTAttoLegge siacTAttoLegge;

	//bi-directional many-to-one association to SiacTBilElem
	/** The siac t bil elem. */
	@ManyToOne
	@JoinColumn(name="elem_id")
	private SiacTBilElem siacTBilElem;

	/**
	 * Instantiates a new siac r bil elem atto legge.
	 */
	public SiacRBilElemAttoLegge() {
	}

	/**
	 * Gets the attolegge bil elem id.
	 *
	 * @return the attolegge bil elem id
	 */
	public Integer getAttoleggeBilElemId() {
		return this.attoleggeBilElemId;
	}

	/**
	 * Sets the attolegge bil elem id.
	 *
	 * @param attoleggeBilElemId the new attolegge bil elem id
	 */
	public void setAttoleggeBilElemId(Integer attoleggeBilElemId) {
		this.attoleggeBilElemId = attoleggeBilElemId;
	}

	/**
	 * Gets the descrizione.
	 *
	 * @return the descrizione
	 */
	public String getDescrizione() {
		return this.descrizione;
	}

	/**
	 * Sets the descrizione.
	 *
	 * @param descrizione the new descrizione
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	/**
	 * Gets the finanziamento fine.
	 *
	 * @return the finanziamento fine
	 */
	public Date getFinanziamentoFine() {
		return this.finanziamentoFine;
	}

	/**
	 * Sets the finanziamento fine.
	 *
	 * @param finanziamentoFine the new finanziamento fine
	 */
	public void setFinanziamentoFine(Date finanziamentoFine) {
		this.finanziamentoFine = finanziamentoFine;
	}

	/**
	 * Gets the finanziamento inizio.
	 *
	 * @return the finanziamento inizio
	 */
	public Date getFinanziamentoInizio() {
		return this.finanziamentoInizio;
	}

	/**
	 * Sets the finanziamento inizio.
	 *
	 * @param finanziamentoInizio the new finanziamento inizio
	 */
	public void setFinanziamentoInizio(Date finanziamentoInizio) {
		this.finanziamentoInizio = finanziamentoInizio;
	}

	/**
	 * Gets the gerarchia.
	 *
	 * @return the gerarchia
	 */
	public String getGerarchia() {
		return this.gerarchia;
	}

	/**
	 * Sets the gerarchia.
	 *
	 * @param gerarchia the new gerarchia
	 */
	public void setGerarchia(String gerarchia) {
		this.gerarchia = gerarchia;
	}

	/**
	 * Gets the siac t atto legge.
	 *
	 * @return the siac t atto legge
	 */
	public SiacTAttoLegge getSiacTAttoLegge() {
		return this.siacTAttoLegge;
	}

	/**
	 * Sets the siac t atto legge.
	 *
	 * @param siacTAttoLegge the new siac t atto legge
	 */
	public void setSiacTAttoLegge(SiacTAttoLegge siacTAttoLegge) {
		this.siacTAttoLegge = siacTAttoLegge;
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

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return attoleggeBilElemId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.attoleggeBilElemId = uid;
	}

}