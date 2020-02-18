/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.math.BigDecimal;
import java.util.List;

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
 * The persistent class for the siac_t_movgest_ts_det database table.
 * 
 */
@Entity
@Table(name="siac_t_movgest_ts_det")
@NamedQuery(name="SiacTMovgestTsDet.findAll", query="SELECT s FROM SiacTMovgestTsDet s")
public class SiacTMovgestTsDet extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The movgest ts det id. */
	@Id
	@SequenceGenerator(name="SIAC_T_MOVGEST_TS_DET_MOVGESTTSDETID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_MOVGEST_TS_DET_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_MOVGEST_TS_DET_MOVGESTTSDETID_GENERATOR")
	@Column(name="movgest_ts_det_id")
	private Integer movgestTsDetId;

	/** The movgest ts det importo. */
	@Column(name="movgest_ts_det_importo")
	private BigDecimal movgestTsDetImporto;

	//bi-directional many-to-one association to SiacDMovgestTsDetTipo
	/** The siac d movgest ts det tipo. */
	@ManyToOne
	@JoinColumn(name="movgest_ts_det_tipo_id")
	private SiacDMovgestTsDetTipo siacDMovgestTsDetTipo;

	//bi-directional many-to-one association to SiacTMovgestT
	/** The siac t movgest t. */
	@ManyToOne
	@JoinColumn(name="movgest_ts_id")
	private SiacTMovgestT siacTMovgestT;
	
	//bi-directional many-to-one association to SiacTMovgestTsDetMod
	@OneToMany(mappedBy="siacTMovgestTsDet")
	private List<SiacTMovgestTsDetMod> siacTMovgestTsDetMods;

	/**
	 * Instantiates a new siac t movgest ts det.
	 */
	public SiacTMovgestTsDet() {
	}

	/**
	 * Gets the movgest ts det id.
	 *
	 * @return the movgest ts det id
	 */
	public Integer getMovgestTsDetId() {
		return this.movgestTsDetId;
	}

	/**
	 * Sets the movgest ts det id.
	 *
	 * @param movgestTsDetId the new movgest ts det id
	 */
	public void setMovgestTsDetId(Integer movgestTsDetId) {
		this.movgestTsDetId = movgestTsDetId;
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
	
	public List<SiacTMovgestTsDetMod> getSiacTMovgestTsDetMods() {
		return this.siacTMovgestTsDetMods;
	}

	public void setSiacTMovgestTsDetMods(List<SiacTMovgestTsDetMod> siacTMovgestTsDetMods) {
		this.siacTMovgestTsDetMods = siacTMovgestTsDetMods;
	}

	public SiacTMovgestTsDetMod addSiacTMovgestTsDetMod(SiacTMovgestTsDetMod siacTMovgestTsDetMod) {
		getSiacTMovgestTsDetMods().add(siacTMovgestTsDetMod);
		siacTMovgestTsDetMod.setSiacTMovgestTsDet(this);

		return siacTMovgestTsDetMod;
	}

	public SiacTMovgestTsDetMod removeSiacTMovgestTsDetMod(SiacTMovgestTsDetMod siacTMovgestTsDetMod) {
		getSiacTMovgestTsDetMods().remove(siacTMovgestTsDetMod);
		siacTMovgestTsDetMod.setSiacTMovgestTsDet(null);

		return siacTMovgestTsDetMod;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return movgestTsDetId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		movgestTsDetId = uid;
	}

}