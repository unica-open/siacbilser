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
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_t_movgest_ts_det_mod database table.
 * 
 */
@Entity
@Table(name="siac_t_movgest_ts_det_mod")
@NamedQuery(name="SiacTMovgestTsDetMod.findAll", query="SELECT s FROM SiacTMovgestTsDetMod s")
public class SiacTMovgestTsDetMod extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The movgest ts det mod id. */
	@Id
	@SequenceGenerator(name="SIAC_T_MOVGEST_TS_DET_MOD_MOVGESTTSDETMODID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_MOVGEST_TS_DET_MOD_MOVGEST_TS_DET_MOD_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_MOVGEST_TS_DET_MOD_MOVGESTTSDETMODID_GENERATOR")
	@Column(name="movgest_ts_det_mod_id")
	private Integer movgestTsDetModId;

	/** The movgest ts det importo. */
	@Column(name="movgest_ts_det_importo")
	private BigDecimal movgestTsDetImporto;

	//bi-directional many-to-one association to SiacDMovgestTsDetTipo
	/** The siac d movgest ts det tipo. */
	@ManyToOne
	@JoinColumn(name="movgest_ts_det_tipo_id")
	private SiacDMovgestTsDetTipo siacDMovgestTsDetTipo;

	//bi-directional many-to-one association to SiacRModificaStato
	/** The siac r modifica stato. */
	@ManyToOne
	@JoinColumn(name="mod_stato_r_id")
	private SiacRModificaStato siacRModificaStato;

	//bi-directional many-to-one association to SiacTMovgestT
	/** The siac t movgest t. */
	@ManyToOne
	@JoinColumn(name="movgest_ts_id")
	private SiacTMovgestT siacTMovgestT;

	//bi-directional many-to-one association to SiacTMovgestTsDet
	/** The siac t movgest ts det. */
	@ManyToOne
	@JoinColumn(name="movgest_ts_det_id")
	private SiacTMovgestTsDet siacTMovgestTsDet;

	/**
	 * Instantiates a new siac t movgest ts det mod.
	 */
	public SiacTMovgestTsDetMod() {
	}

	/**
	 * Gets the movgest ts det mod id.
	 *
	 * @return the movgest ts det mod id
	 */
	public Integer getMovgestTsDetModId() {
		return this.movgestTsDetModId;
	}

	/**
	 * Sets the movgest ts det mod id.
	 *
	 * @param movgestTsDetModId the new movgest ts det mod id
	 */
	public void setMovgestTsDetModId(Integer movgestTsDetModId) {
		this.movgestTsDetModId = movgestTsDetModId;
	}

	/**
	 * Gets the movgest ts det importo.
	 *
	 * @return the movgest ts det importo
	 */
	public BigDecimal getMovgestTsDetImporto() {
		return this.movgestTsDetImporto;
	}

	/**
	 * Sets the movgest ts det importo.
	 *
	 * @param movgestTsDetImporto the new movgest ts det importo
	 */
	public void setMovgestTsDetImporto(BigDecimal movgestTsDetImporto) {
		this.movgestTsDetImporto = movgestTsDetImporto;
	}

	/**
	 * Gets the siac d movgest ts det tipo.
	 *
	 * @return the siac d movgest ts det tipo
	 */
	public SiacDMovgestTsDetTipo getSiacDMovgestTsDetTipo() {
		return this.siacDMovgestTsDetTipo;
	}

	/**
	 * Sets the siac d movgest ts det tipo.
	 *
	 * @param siacDMovgestTsDetTipo the new siac d movgest ts det tipo
	 */
	public void setSiacDMovgestTsDetTipo(SiacDMovgestTsDetTipo siacDMovgestTsDetTipo) {
		this.siacDMovgestTsDetTipo = siacDMovgestTsDetTipo;
	}

	/**
	 * Gets the siac r modifica stato.
	 *
	 * @return the siac r modifica stato
	 */
	public SiacRModificaStato getSiacRModificaStato() {
		return this.siacRModificaStato;
	}

	/**
	 * Sets the siac r modifica stato.
	 *
	 * @param siacRModificaStato the new siac r modifica stato
	 */
	public void setSiacRModificaStato(SiacRModificaStato siacRModificaStato) {
		this.siacRModificaStato = siacRModificaStato;
	}

	/**
	 * Gets the siac t movgest t.
	 *
	 * @return the siac t movgest t
	 */
	public SiacTMovgestT getSiacTMovgestT() {
		return this.siacTMovgestT;
	}

	/**
	 * Sets the siac t movgest t.
	 *
	 * @param siacTMovgestT the new siac t movgest t
	 */
	public void setSiacTMovgestT(SiacTMovgestT siacTMovgestT) {
		this.siacTMovgestT = siacTMovgestT;
	}

	/**
	 * Gets the siac t movgest ts det.
	 *
	 * @return the siac t movgest ts det
	 */
	public SiacTMovgestTsDet getSiacTMovgestTsDet() {
		return this.siacTMovgestTsDet;
	}

	/**
	 * Sets the siac t movgest ts det.
	 *
	 * @param siacTMovgestTsDet the new siac t movgest ts det
	 */
	public void setSiacTMovgestTsDet(SiacTMovgestTsDet siacTMovgestTsDet) {
		this.siacTMovgestTsDet = siacTMovgestTsDet;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return movgestTsDetModId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.movgestTsDetModId = uid;
	}

}