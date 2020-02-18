/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the siac_d_trasporto_mezzo database table.
 * 
 */
@Entity
@Table(name="siac_d_trasporto_mezzo")
@NamedQuery(name="SiacDTrasportoMezzo.findAll", query="SELECT s FROM SiacDTrasportoMezzo s")
public class SiacDTrasportoMezzo extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_TRASPORTO_MEZZO_MTRAID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_TRASPORTO_MEZZO_MTRA_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_TRASPORTO_MEZZO_MTRAID_GENERATOR")
	@Column(name="mtra_id")
	private Integer mtraId;

	@Column(name="mtra_code")
	private String mtraCode;

	@Column(name="mtra_desc")
	private String mtraDesc;

	//bi-directional many-to-one association to SiacRTrasfMissTrasporto
	@OneToMany(mappedBy="siacDTrasportoMezzo")
	private List<SiacRTrasfMissTrasporto> siacRTrasfMissTrasportos;

	public SiacDTrasportoMezzo() {
	}

	public Integer getMtraId() {
		return this.mtraId;
	}

	public void setMtraId(Integer mtraId) {
		this.mtraId = mtraId;
	}

	public String getMtraCode() {
		return this.mtraCode;
	}

	public void setMtraCode(String mtraCode) {
		this.mtraCode = mtraCode;
	}

	public String getMtraDesc() {
		return this.mtraDesc;
	}

	public void setMtraDesc(String mtraDesc) {
		this.mtraDesc = mtraDesc;
	}

	public List<SiacRTrasfMissTrasporto> getSiacRTrasfMissTrasportos() {
		return this.siacRTrasfMissTrasportos;
	}

	public void setSiacRTrasfMissTrasportos(List<SiacRTrasfMissTrasporto> siacRTrasfMissTrasportos) {
		this.siacRTrasfMissTrasportos = siacRTrasfMissTrasportos;
	}

	public SiacRTrasfMissTrasporto addSiacRTrasfMissTrasporto(SiacRTrasfMissTrasporto siacRTrasfMissTrasporto) {
		getSiacRTrasfMissTrasportos().add(siacRTrasfMissTrasporto);
		siacRTrasfMissTrasporto.setSiacDTrasportoMezzo(this);

		return siacRTrasfMissTrasporto;
	}

	public SiacRTrasfMissTrasporto removeSiacRTrasfMissTrasporto(SiacRTrasfMissTrasporto siacRTrasfMissTrasporto) {
		getSiacRTrasfMissTrasportos().remove(siacRTrasfMissTrasporto);
		siacRTrasfMissTrasporto.setSiacDTrasportoMezzo(null);

		return siacRTrasfMissTrasporto;
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return this.mtraId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.mtraId = uid;
		
	}

}