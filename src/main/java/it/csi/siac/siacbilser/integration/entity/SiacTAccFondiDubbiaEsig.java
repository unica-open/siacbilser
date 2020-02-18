/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_t_bil_elem database table.
 * 
 */
@Entity
@Table(name="siac_t_acc_fondi_dubbia_esig")
@NamedQuery(name="SiacTAccFondiDubbiaEsig.findAll", query="SELECT s FROM SiacTAccFondiDubbiaEsig s")
public class SiacTAccFondiDubbiaEsig extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The acc fde id. */
	@Id
	@SequenceGenerator(name="SIAC_T_ACC_FONDI_DUBBIA_ESIG_ACCFDEID_GENERATOR", allocationSize=1, sequenceName="SIAC_T_ACC_FONDI_DUBBIA_ESIG_ACC_FDE_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_ACC_FONDI_DUBBIA_ESIG_ACCFDEID_GENERATOR")
	@Column(name="acc_fde_id")
	private Integer accFdeId;
	
	/** The perc acc fondi. */
	@Column(name="perc_acc_fondi")
	private BigDecimal percAccFondi;

	/** The perc acc fondi. */
	@Column(name="perc_acc_fondi_1")
	private BigDecimal percAccFondi1;

	/** The perc acc fondi. */
	@Column(name="perc_acc_fondi_2")
	private BigDecimal percAccFondi2;

	/** The perc acc fondi. */
	@Column(name="perc_acc_fondi_3")
	private BigDecimal percAccFondi3;

	/** The perc acc fondi. */
	@Column(name="perc_acc_fondi_4")
	private BigDecimal percAccFondi4;

	/** The perc acc fondi. */
	@Column(name="perc_delta")
	private BigDecimal percDelta;

	//bi-directional many-to-one association to SiacRBilElemAccFondiDubbiaEsig
	@OneToMany(mappedBy="siacTAccFondiDubbiaEsig", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRBilElemAccFondiDubbiaEsig> siacRBilElemAccFondiDubbiaEsigs;
	
	/**
	 * Instantiates a new siac t bil elem.
	 */
	public SiacTAccFondiDubbiaEsig() {
	}

	/**
	 * @return the accFdeId
	 */
	public Integer getAccFdeId() {
		return accFdeId;
	}

	/**
	 * @param accFdeId the accFdeId to set
	 */
	public void setAccFdeId(Integer accFdeId) {
		this.accFdeId = accFdeId;
	}

	/**
	 * @return the percAccFondi
	 */
	public BigDecimal getPercAccFondi() {
		return percAccFondi;
	}

	/**
	 * @param percAccFondi the percAccFondi to set
	 */
	public void setPercAccFondi(BigDecimal percAccFondi) {
		this.percAccFondi = percAccFondi;
	}

	/**
	 * @return the percAccFondi1
	 */
	public BigDecimal getPercAccFondi1() {
		return percAccFondi1;
	}

	/**
	 * @param percAccFondi1 the percAccFondi1 to set
	 */
	public void setPercAccFondi1(BigDecimal percAccFondi1) {
		this.percAccFondi1 = percAccFondi1;
	}

	/**
	 * @return the percAccFondi2
	 */
	public BigDecimal getPercAccFondi2() {
		return percAccFondi2;
	}

	/**
	 * @param percAccFondi2 the percAccFondi2 to set
	 */
	public void setPercAccFondi2(BigDecimal percAccFondi2) {
		this.percAccFondi2 = percAccFondi2;
	}

	/**
	 * @return the percAccFondi3
	 */
	public BigDecimal getPercAccFondi3() {
		return percAccFondi3;
	}

	/**
	 * @param percAccFondi3 the percAccFondi3 to set
	 */
	public void setPercAccFondi3(BigDecimal percAccFondi3) {
		this.percAccFondi3 = percAccFondi3;
	}

	/**
	 * @return the percAccFondi4
	 */
	public BigDecimal getPercAccFondi4() {
		return percAccFondi4;
	}

	/**
	 * @param percAccFondi4 the percAccFondi4 to set
	 */
	public void setPercAccFondi4(BigDecimal percAccFondi4) {
		this.percAccFondi4 = percAccFondi4;
	}

	/**
	 * @return the percDelta
	 */
	public BigDecimal getPercDelta() {
		return percDelta;
	}

	/**
	 * @param percDelta the percDelta to set
	 */
	public void setPercDelta(BigDecimal percDelta) {
		this.percDelta = percDelta;
	}

	/**
	 * @return the siacRBilElemAccFondiDubbiaEsigs
	 */
	public List<SiacRBilElemAccFondiDubbiaEsig> getSiacRBilElemAccFondiDubbiaEsigs() {
		return this.siacRBilElemAccFondiDubbiaEsigs;
	}

	/**
	 * @param siacRBilElemAccFondiDubbiaEsigs the siacRBilElemAccFondiDubbiaEsigs to set
	 */
	public void setSiacRBilElemAccFondiDubbiaEsigs(List<SiacRBilElemAccFondiDubbiaEsig> siacRBilElemAccFondiDubbiaEsigs) {
		this.siacRBilElemAccFondiDubbiaEsigs = siacRBilElemAccFondiDubbiaEsigs;
	}

	/**
	 * @param siacRBilElemAccFondiDubbiaEsig the siacRBilElemAccFondiDubbiaEsig to add
	 * @return the siacRBilElemAccFondiDubbiaEsigs
	 */
	public SiacRBilElemAccFondiDubbiaEsig addSiacRBilElemAccFondiDubbiaEsig(SiacRBilElemAccFondiDubbiaEsig siacRBilElemAccFondiDubbiaEsig) {
		getSiacRBilElemAccFondiDubbiaEsigs().add(siacRBilElemAccFondiDubbiaEsig);
		siacRBilElemAccFondiDubbiaEsig.setSiacTAccFondiDubbiaEsig(this);

		return siacRBilElemAccFondiDubbiaEsig;
	}

	/**
	 * @param siacRBilElemAccFondiDubbiaEsig the siacRBilElemAccFondiDubbiaEsig to remove
	 * @return the siacRBilElemAccFondiDubbiaEsigs
	 */
	public SiacRBilElemAccFondiDubbiaEsig removeSiacRBilElemAccFondiDubbiaEsig(SiacRBilElemAccFondiDubbiaEsig siacRBilElemAccFondiDubbiaEsig) {
		getSiacRBilElemAccFondiDubbiaEsigs().remove(siacRBilElemAccFondiDubbiaEsig);
		siacRBilElemAccFondiDubbiaEsig.setSiacTAccFondiDubbiaEsig(null);

		return siacRBilElemAccFondiDubbiaEsig;
	}

	@Override
	public Integer getUid() {
		return accFdeId;
	}

	@Override
	public void setUid(Integer uid) {
		this.accFdeId = uid;
		
	}

}