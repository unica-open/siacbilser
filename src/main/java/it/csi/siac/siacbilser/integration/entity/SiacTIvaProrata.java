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
 * The persistent class for the siac_t_iva_prorata database table.
 * 
 */
@Entity
@Table(name="siac_t_iva_prorata")
@NamedQuery(name="SiacTIvaProrata.findAll", query="SELECT s FROM SiacTIvaProrata s")
public class SiacTIvaProrata extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The ivapro id. */
	@Id
	@SequenceGenerator(name="SIAC_T_IVA_PRORATA_IVAPROID_GENERATOR", sequenceName="SIAC_T_IVA_PRORATA_IVAPRO_ID_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_T_IVA_PRORATA_IVAPROID_GENERATOR")
	@Column(name="ivapro_id")
	private Integer ivaproId;

	/** The ivapro perc. */
	@Column(name="ivapro_perc")
	private BigDecimal ivaproPerc;

	//bi-directional many-to-one association to SiacRIvaGruppoProrata
	/** The siac r iva gruppo proratas. */
	@OneToMany(mappedBy="siacTIvaProrata", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
	private List<SiacRIvaGruppoProrata> siacRIvaGruppoProratas;

	/**
	 * Instantiates a new siac t iva prorata.
	 */
	public SiacTIvaProrata() {
	}

	/**
	 * Gets the ivapro id.
	 *
	 * @return the ivapro id
	 */
	public Integer getIvaproId() {
		return this.ivaproId;
	}

	/**
	 * Sets the ivapro id.
	 *
	 * @param ivaproId the new ivapro id
	 */
	public void setIvaproId(Integer ivaproId) {
		this.ivaproId = ivaproId;
	}

	/**
	 * Gets the ivapro perc.
	 *
	 * @return the ivapro perc
	 */
	public BigDecimal getIvaproPerc() {
		return this.ivaproPerc;
	}

	/**
	 * Sets the ivapro perc.
	 *
	 * @param ivaproPerc the new ivapro perc
	 */
	public void setIvaproPerc(BigDecimal ivaproPerc) {
		this.ivaproPerc = ivaproPerc;
	}
	
	/**
	 * Gets the siac r iva gruppo proratas.
	 *
	 * @return the siac r iva gruppo proratas
	 */
	public List<SiacRIvaGruppoProrata> getSiacRIvaGruppoProratas() {
		return this.siacRIvaGruppoProratas;
	}

	/**
	 * Sets the siac r iva gruppo proratas.
	 *
	 * @param siacRIvaGruppoProratas the new siac r iva gruppo proratas
	 */
	public void setSiacRIvaGruppoProratas(List<SiacRIvaGruppoProrata> siacRIvaGruppoProratas) {
		this.siacRIvaGruppoProratas = siacRIvaGruppoProratas;
	}

	/**
	 * Adds the siac r iva gruppo prorata.
	 *
	 * @param siacRIvaGruppoProrata the siac r iva gruppo prorata
	 * @return the siac r iva gruppo prorata
	 */
	public SiacRIvaGruppoProrata addSiacRIvaGruppoProrata(SiacRIvaGruppoProrata siacRIvaGruppoProrata) {
		getSiacRIvaGruppoProratas().add(siacRIvaGruppoProrata);
		siacRIvaGruppoProrata.setSiacTIvaProrata(this);

		return siacRIvaGruppoProrata;
	}

	/**
	 * Removes the siac r iva gruppo prorata.
	 *
	 * @param siacRIvaGruppoProrata the siac r iva gruppo prorata
	 * @return the siac r iva gruppo prorata
	 */
	public SiacRIvaGruppoProrata removeSiacRIvaGruppoProrata(SiacRIvaGruppoProrata siacRIvaGruppoProrata) {
		getSiacRIvaGruppoProratas().remove(siacRIvaGruppoProrata);
		siacRIvaGruppoProrata.setSiacTIvaProrata(null);

		return siacRIvaGruppoProrata;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return ivaproId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.ivaproId = uid;
	}

}