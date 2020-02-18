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
 * The persistent class for the siac_t_ordinativo_ts_det database table.
 * 
 */
@Entity
@Table(name="siac_t_ordinativo_ts_det")
@NamedQuery(name="SiacTOrdinativoTsDet.findAll", query="SELECT s FROM SiacTOrdinativoTsDet s")
public class SiacTOrdinativoTsDet extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The ord ts det id. */
	@Id
	@SequenceGenerator(name="SIAC_T_ORDINATIVO_TS_DET_ORDTSDETID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_ORDINATIVO_TS_DET_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_ORDINATIVO_TS_DET_ORDTSDETID_GENERATOR")
	@Column(name="ord_ts_det_id")
	private Integer ordTsDetId;

	/** The ord ts det importo. */
	@Column(name="ord_ts_det_importo")
	private BigDecimal ordTsDetImporto;

	//bi-directional many-to-one association to SiacDOrdinativoTsDetTipo
	/** The siac d ordinativo ts det tipo. */
	@ManyToOne
	@JoinColumn(name="ord_ts_det_tipo_id")
	private SiacDOrdinativoTsDetTipo siacDOrdinativoTsDetTipo;

	//bi-directional many-to-one association to SiacTOrdinativoTS
	/** The siac t ordinativo t. */
	@ManyToOne
	@JoinColumn(name="ord_ts_id")
	private SiacTOrdinativoT siacTOrdinativoT;

	/**
	 * Instantiates a new siac t ordinativo ts det.
	 */
	public SiacTOrdinativoTsDet() {
	}

	/**
	 * Gets the ord ts det id.
	 *
	 * @return the ord ts det id
	 */
	public Integer getOrdTsDetId() {
		return this.ordTsDetId;
	}

	/**
	 * Sets the ord ts det id.
	 *
	 * @param ordTsDetId the new ord ts det id
	 */
	public void setOrdTsDetId(Integer ordTsDetId) {
		this.ordTsDetId = ordTsDetId;
	}

	/**
	 * Gets the ord ts det importo.
	 *
	 * @return the ord ts det importo
	 */
	public BigDecimal getOrdTsDetImporto() {
		return this.ordTsDetImporto;
	}

	/**
	 * Sets the ord ts det importo.
	 *
	 * @param ordTsDetImporto the new ord ts det importo
	 */
	public void setOrdTsDetImporto(BigDecimal ordTsDetImporto) {
		this.ordTsDetImporto = ordTsDetImporto;
	}

//	public List<SiacRLiquidazioneOrd> getSiacRLiquidazioneOrds() {
//		return this.siacRLiquidazioneOrds;
//	}
//
//	public void setSiacRLiquidazioneOrds(List<SiacRLiquidazioneOrd> siacRLiquidazioneOrds) {
//		this.siacRLiquidazioneOrds = siacRLiquidazioneOrds;
//	}

//	public SiacRLiquidazioneOrd addSiacRLiquidazioneOrd(SiacRLiquidazioneOrd siacRLiquidazioneOrd) {
//		getSiacRLiquidazioneOrds().add(siacRLiquidazioneOrd);
//		siacRLiquidazioneOrd.setSiacTOrdinativoTsDet(this);
//
//		return siacRLiquidazioneOrd;
//	}
//
//	public SiacRLiquidazioneOrd removeSiacRLiquidazioneOrd(SiacRLiquidazioneOrd siacRLiquidazioneOrd) {
//		getSiacRLiquidazioneOrds().remove(siacRLiquidazioneOrd);
//		siacRLiquidazioneOrd.setSiacTOrdinativoTsDet(null);
//
//		return siacRLiquidazioneOrd;
//	}

	/**
 * Gets the siac d ordinativo ts det tipo.
 *
 * @return the siac d ordinativo ts det tipo
 */
public SiacDOrdinativoTsDetTipo getSiacDOrdinativoTsDetTipo() {
		return this.siacDOrdinativoTsDetTipo;
	}

	/**
	 * Sets the siac d ordinativo ts det tipo.
	 *
	 * @param siacDOrdinativoTsDetTipo the new siac d ordinativo ts det tipo
	 */
	public void setSiacDOrdinativoTsDetTipo(SiacDOrdinativoTsDetTipo siacDOrdinativoTsDetTipo) {
		this.siacDOrdinativoTsDetTipo = siacDOrdinativoTsDetTipo;
	}

	/**
	 * Gets the siac t ordinativo t.
	 *
	 * @return the siac t ordinativo t
	 */
	public SiacTOrdinativoT getSiacTOrdinativoT() {
		return this.siacTOrdinativoT;
	}

	/**
	 * Sets the siac t ordinativo t.
	 *
	 * @param siacTOrdinativoT the new siac t ordinativo t
	 */
	public void setSiacTOrdinativoT(SiacTOrdinativoT siacTOrdinativoT) {
		this.siacTOrdinativoT = siacTOrdinativoT;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return ordTsDetId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		ordTsDetId = uid;
	}

}