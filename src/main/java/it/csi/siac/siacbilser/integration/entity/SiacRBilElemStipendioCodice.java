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

@Entity
@Table(name = "siac_r_bil_elem_stipendio_codice")
@NamedQuery(name="SiacRBilElemStipendioCodice.findAll", query="SELECT s FROM SiacRBilElemStipendioCodice s")
public class SiacRBilElemStipendioCodice extends SiacTEnteBase{
	private static final long serialVersionUID = 1L;

	
	@Id
	@SequenceGenerator(name="SIAC_R_BIL_ELEM_STIPENDIO_CODICE_ELEMELEMID_GENERATOR", allocationSize=1, sequenceName="SIAC_R_BIL_ELEM_STIPENDIO_CODICE_ELEM_ELEM_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_R_BIL_ELEM_STIPENDIO_CODICE_ELEMELEMID_GENERATOR")
	@Column(name="elem_elem_id")
	private Integer elemElemId;

	//bi-directional many-to-one association to SiacDClassTipoFin
	@ManyToOne
	@JoinColumn(name="elem_id")
	private SiacTBilElem siacTBilElem;

	//bi-directional many-to-one association to SiacTAttrFin
	@ManyToOne
	@JoinColumn(name="stipcode_id")
	private SiacDStipendioCodice siacDStipendioCodice;
	

	/**
	 * @return the elemElemId
	 */
	public Integer getElemElemId() {
		return elemElemId;
	}

	/**
	 * @param elemElemId the elemElemId to set
	 */
	public void setElemElemId(Integer elemElemId) {
		this.elemElemId = elemElemId;
	}

	/**
	 * @return the siacTBilElem
	 */
	public SiacTBilElem getSiacTBilElem() {
		return siacTBilElem;
	}

	/**
	 * @param siacTBilElem the siacTBilElem to set
	 */
	public void setSiacTBilElem(SiacTBilElem siacTBilElem) {
		this.siacTBilElem = siacTBilElem;
	}

	/**
	 * @return the siacDStipendioCodice
	 */
	public SiacDStipendioCodice getSiacDStipendioCodice() {
		return siacDStipendioCodice;
	}

	/**
	 * @param siacDStipendioCodice the siacDStipendioCodice to set
	 */
	public void setSiacDStipendioCodice(SiacDStipendioCodice siacDStipendioCodice) {
		this.siacDStipendioCodice = siacDStipendioCodice;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return elemElemId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.elemElemId = uid;
		
	}

}
