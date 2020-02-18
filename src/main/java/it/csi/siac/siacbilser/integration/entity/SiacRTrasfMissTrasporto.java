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
 * The persistent class for the siac_r_trasf_miss_trasporto database table.
 * 
 */
@Entity
@Table(name="siac_r_trasf_miss_trasporto")
@NamedQuery(name="SiacRTrasfMissTrasporto.findAll", query="SELECT s FROM SiacRTrasfMissTrasporto s")
public class SiacRTrasfMissTrasporto extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_R_TRASF_MISS_TRASPORTO_TMTMID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_TRASF_MISS_TRASPORTO_TMTM_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_TRASF_MISS_TRASPORTO_TMTMID_GENERATOR")
	@Column(name="tmtm_id")
	private Integer tmtmId;

	//bi-directional many-to-one association to SiacDTrasportoMezzo
	@ManyToOne
	@JoinColumn(name="mtra_id")
	private SiacDTrasportoMezzo siacDTrasportoMezzo;

	//bi-directional many-to-one association to SiacTTrasfMiss
	@ManyToOne
	@JoinColumn(name="tramis_id")
	private SiacTTrasfMiss siacTTrasfMiss;

	public SiacRTrasfMissTrasporto() {
	}

	public Integer getTmtmId() {
		return this.tmtmId;
	}

	public void setTmtmId(Integer tmtmId) {
		this.tmtmId = tmtmId;
	}

	public SiacDTrasportoMezzo getSiacDTrasportoMezzo() {
		return this.siacDTrasportoMezzo;
	}

	public void setSiacDTrasportoMezzo(SiacDTrasportoMezzo siacDTrasportoMezzo) {
		this.siacDTrasportoMezzo = siacDTrasportoMezzo;
	}

	public SiacTTrasfMiss getSiacTTrasfMiss() {
		return this.siacTTrasfMiss;
	}

	public void setSiacTTrasfMiss(SiacTTrasfMiss siacTTrasfMiss) {
		this.siacTTrasfMiss = siacTTrasfMiss;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return this.tmtmId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.tmtmId = uid;
		
	}

}