/**
 * SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
 * SPDX-License-Identifier: EUPL-1.2
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

@Entity
@Table(name="siac_r_prima_nota_class")
@NamedQuery(name="SiacRPrimaNotaClass.findAll", query="SELECT s FROM SiacRPrimaNotaClass s")
public class SiacRPrimaNotaClass extends SiacTEnteBase {

	/** per la serializzazione */
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="SIAC_R_PRIMA_NOTA_CLASS_PNOTA_CLASSIF_ID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_PRIMA_NOTA_CLASS_PNOTA_CLASSIF_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_PRIMA_NOTA_CLASS_PNOTA_CLASSIF_ID_GENERATOR")
	@Column(name="pnota_classif_id")
	private Integer pnotaClassifId;

	@ManyToOne
	@JoinColumn(name="pnota_id")
	private SiacTPrimaNota siacTPrimaNota;
	
	@ManyToOne
	@JoinColumn(name="classif_id")
	private SiacTClass siacTClass;
	
	public SiacRPrimaNotaClass() {}

	/**
	 * @return the pnotaClassifId
	 */
	public Integer getPnotaClassifId() {
		return pnotaClassifId;
	}

	/**
	 * @param pnotaClassifId the pnotaClassifId to set
	 */
	public void setPnotaClassifId(Integer pnotaClassifId) {
		this.pnotaClassifId = pnotaClassifId;
	}

	/**
	 * @return the siacTPrimaNota
	 */
	public SiacTPrimaNota getSiacTPrimaNota() {
		return siacTPrimaNota;
	}

	/**
	 * @param siacTPrimaNota the siacTPrimaNota to set
	 */
	public void setSiacTPrimaNota(SiacTPrimaNota siacTPrimaNota) {
		this.siacTPrimaNota = siacTPrimaNota;
	}

	/**
	 * @return the siacTClass
	 */
	public SiacTClass getSiacTClass() {
		return siacTClass;
	}

	/**
	 * @param siacTClass the siacTClass to set
	 */
	public void setSiacTClass(SiacTClass siacTClass) {
		this.siacTClass = siacTClass;
	}

	@Override
	public Integer getUid() {
		return this.pnotaClassifId;
	}

	@Override
	public void setUid(Integer uid) {
		this.pnotaClassifId = uid;
	}

}
