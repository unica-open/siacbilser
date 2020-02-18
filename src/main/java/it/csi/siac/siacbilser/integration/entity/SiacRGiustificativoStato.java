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
 * The persistent class for the siac_r_giustificativo_stato database table.
 * 
 */
@Entity
@Table(name="siac_r_giustificativo_stato")
@NamedQuery(name="SiacRGiustificativoStato.findAll", query="SELECT s FROM SiacRGiustificativoStato s")
public class SiacRGiustificativoStato extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_GIUSTIFICATIVO_STATO_GSTRSTATOID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_GIUSTIFICATIVO_STATO_GST_R_STATO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_GIUSTIFICATIVO_STATO_GSTRSTATOID_GENERATOR")
	@Column(name="gst_r_stato_id")
	private Integer gstRStatoId;

	//bi-directional many-to-one association to SiacDGiustificativoStato
	@ManyToOne
	@JoinColumn(name="giust_stato_id")
	private SiacDGiustificativoStato siacDGiustificativoStato;

	//bi-directional many-to-one association to SiacTGiustificativoDet
	@ManyToOne
	@JoinColumn(name="gst_id")
	private SiacTGiustificativoDet siacTGiustificativoDet;

	public SiacRGiustificativoStato() {
	}

	public Integer getGstRStatoId() {
		return this.gstRStatoId;
	}

	public void setGstRStatoId(Integer gstRStatoId) {
		this.gstRStatoId = gstRStatoId;
	}

	public SiacDGiustificativoStato getSiacDGiustificativoStato() {
		return this.siacDGiustificativoStato;
	}

	public void setSiacDGiustificativoStato(SiacDGiustificativoStato siacDGiustificativoStato) {
		this.siacDGiustificativoStato = siacDGiustificativoStato;
	}

	public SiacTGiustificativoDet getSiacTGiustificativoDet() {
		return this.siacTGiustificativoDet;
	}

	public void setSiacTGiustificativoDet(SiacTGiustificativoDet siacTGiustificativoDet) {
		this.siacTGiustificativoDet = siacTGiustificativoDet;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return this.gstRStatoId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.gstRStatoId = uid;
		
	}

}