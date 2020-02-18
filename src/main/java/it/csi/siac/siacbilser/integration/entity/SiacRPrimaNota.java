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
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the siac_r_prima_nota_stato database table.
 * 
 */
@Entity
@Table(name="siac_r_prima_nota")
@NamedQuery(name="SiacRPrimaNota.findAll", query="SELECT s FROM SiacRPrimaNota s")
public class SiacRPrimaNota extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_PRIMA_NOTA_PNOTARID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_PRIMA_NOTA_PNOTA_R_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_PRIMA_NOTA_PNOTARID_GENERATOR")
	@Column(name="pnota_r_id")
	private Integer pnotaRId;

	//bi-directional many-to-one association to SiacTPrimaNota
	@ManyToOne
	@JoinColumn(name="pnota_id_da")
	private SiacTPrimaNota siacTPrimaNotaPadre;
	
	//bi-directional many-to-one association to SiacTPrimaNota
	@ManyToOne
	@JoinColumn(name="pnota_id_a")
	private SiacTPrimaNota siacTPrimaNotaFiglio;
	
	//bi-directional many-to-one association to SiacTPrimaNota
	@ManyToOne
	@JoinColumn(name="pnota_rel_tipo_id")
	private SiacDPrimaNotaRelTipo siacDPrimaNotaRelTipo;
	
	@Column(name="note")
	private String note;

	public SiacRPrimaNota() {
	}

	/**
	 * @return the pnotaRId
	 */
	public Integer getPnotaRId() {
		return pnotaRId;
	}

	/**
	 * @param pnotaRId the pnotaRId to set
	 */
	public void setPnotaRId(Integer pnotaRId) {
		this.pnotaRId = pnotaRId;
	}

	/**
	 * @return the siacTPrimaNotaPadre
	 */
	public SiacTPrimaNota getSiacTPrimaNotaPadre() {
		return siacTPrimaNotaPadre;
	}

	/**
	 * @param siacTPrimaNotaPadre the siacTPrimaNotaPadre to set
	 */
	public void setSiacTPrimaNotaPadre(SiacTPrimaNota siacTPrimaNotaPadre) {
		this.siacTPrimaNotaPadre = siacTPrimaNotaPadre;
	}

	/**
	 * @return the siacTPrimaNotaFiglio
	 */
	public SiacTPrimaNota getSiacTPrimaNotaFiglio() {
		return siacTPrimaNotaFiglio;
	}

	/**
	 * @param siacTPrimaNotaFiglio the siacTPrimaNotaFiglio to set
	 */
	public void setSiacTPrimaNotaFiglio(SiacTPrimaNota siacTPrimaNotaFiglio) {
		this.siacTPrimaNotaFiglio = siacTPrimaNotaFiglio;
	}

	/**
	 * @return the siacDPrimaNotaRelTipo
	 */
	public SiacDPrimaNotaRelTipo getSiacDPrimaNotaRelTipo() {
		return siacDPrimaNotaRelTipo;
	}

	/**
	 * @param siacDPrimaNotaRelTipo the siacDPrimaNotaRelTipo to set
	 */
	public void setSiacDPrimaNotaRelTipo(SiacDPrimaNotaRelTipo siacDPrimaNotaRelTipo) {
		this.siacDPrimaNotaRelTipo = siacDPrimaNotaRelTipo;
	}
	
	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @param note the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return this.pnotaRId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.pnotaRId = uid;
		
	}

}