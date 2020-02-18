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


// TODO: Auto-generated Javadoc
/**
 * The persistent class for the siac_r_iva_gruppo_chiusura database table.
 * 
 */
@Entity
@Table(name="siac_r_iva_gruppo_chiusura")
@NamedQuery(name="SiacRIvaGruppoChiusura.findAll", query="SELECT s FROM SiacRIvaGruppoChiusura s")
public class SiacRIvaGruppoChiusura extends SiacTEnteBase {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The ivagruchi id. */
	@Id
	@SequenceGenerator(name="SIAC_R_IVA_GRUPPO_CHIUSURA_IVAGRUCHIID_GENERATOR", sequenceName="SIAC_R_IVA_GRUPPO_CHIUSURA_IVAGRUCHI_ID_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_IVA_GRUPPO_CHIUSURA_IVAGRUCHIID_GENERATOR")
	@Column(name="ivagruchi_id")
	private Integer ivagruchiId;

	/** The ivagruchitipo anno. */
	@Column(name="ivagruchitipo_anno")
	private Integer ivagruchitipoAnno;

	//bi-directional many-to-one association to SiacDIvaChiusuraTipo
	/** The siac d iva chiusura tipo. */
	@ManyToOne
	@JoinColumn(name="ivachi_tipo_id")
	private SiacDIvaChiusuraTipo siacDIvaChiusuraTipo;

	//bi-directional many-to-one association to SiacTIvaGruppo
	/** The siac t iva gruppo. */
	@ManyToOne
	@JoinColumn(name="ivagru_id")
	private SiacTIvaGruppo siacTIvaGruppo;

	/**
	 * Instantiates a new siac r iva gruppo chiusura.
	 */
	public SiacRIvaGruppoChiusura() {
	}

	/**
	 * Gets the ivagruchi id.
	 *
	 * @return the ivagruchi id
	 */
	public Integer getIvagruchiId() {
		return this.ivagruchiId;
	}

	/**
	 * Sets the ivagruchi id.
	 *
	 * @param ivagruchiId the new ivagruchi id
	 */
	public void setIvagruchiId(Integer ivagruchiId) {
		this.ivagruchiId = ivagruchiId;
	}

	/**
	 * Gets the ivagruchitipo anno.
	 *
	 * @return the ivagruchitipo anno
	 */
	public Integer getIvagruchitipoAnno() {
		return this.ivagruchitipoAnno;
	}

	/**
	 * Sets the ivagruchitipo anno.
	 *
	 * @param ivagruchitipoAnno the new ivagruchitipo anno
	 */
	public void setIvagruchitipoAnno(Integer ivagruchitipoAnno) {
		this.ivagruchitipoAnno = ivagruchitipoAnno;
	}

	/**
	 * Gets the siac d iva chiusura tipo.
	 *
	 * @return the siac d iva chiusura tipo
	 */
	public SiacDIvaChiusuraTipo getSiacDIvaChiusuraTipo() {
		return this.siacDIvaChiusuraTipo;
	}

	/**
	 * Sets the siac d iva chiusura tipo.
	 *
	 * @param siacDIvaChiusuraTipo the new siac d iva chiusura tipo
	 */
	public void setSiacDIvaChiusuraTipo(SiacDIvaChiusuraTipo siacDIvaChiusuraTipo) {
		this.siacDIvaChiusuraTipo = siacDIvaChiusuraTipo;
	}

	/**
	 * Gets the siac t iva gruppo.
	 *
	 * @return the siac t iva gruppo
	 */
	public SiacTIvaGruppo getSiacTIvaGruppo() {
		return this.siacTIvaGruppo;
	}

	/**
	 * Sets the siac t iva gruppo.
	 *
	 * @param siacTIvaGruppo the new siac t iva gruppo
	 */
	public void setSiacTIvaGruppo(SiacTIvaGruppo siacTIvaGruppo) {
		this.siacTIvaGruppo = siacTIvaGruppo;
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return ivagruchiId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.ivagruchiId = uid;
	}

}