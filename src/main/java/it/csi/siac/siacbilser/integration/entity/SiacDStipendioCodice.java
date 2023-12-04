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


@Entity
@Table(name="siac_d_stipendio_codice")
@NamedQuery(name="SiacDStipendioCodice.findAll", query="SELECT s FROM SiacDStipendioCodice s")
public class SiacDStipendioCodice extends SiacTEnteBase{
	
	
	private static final long serialVersionUID = -2815628725739805566L;

	@Id
	@SequenceGenerator(name="SIAC_D_STIPENDIO_CODICE_STIPCODE_GENERATOR", allocationSize=1, sequenceName="SIAC_D_STIPENDIO_CODICE_STIPCODE_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_STIPENDIO_CODICE_STIPCODE_GENERATOR")
	@Column(name="stipcode_id")
	private Integer stipcodeId;

	@Column(name="stipcode_code")
	private String stipcodeCode;
	
	@Column(name="stipcode_desc")
	private String stipcodeDesc;
	
	//bi-directional many-to-one association to SiacRBilElemStipendioCodice
	/** The siac r doc classes. */
	@OneToMany(mappedBy="siacDStipendioCodice")
	private List<SiacRBilElemStipendioCodice> siacRBilElemStipendioCodices;

	/**
	 * @return the stipcodeId
	 */
	public Integer getStipcodeId() {
		return stipcodeId;
	}

	/**
	 * @param stipcodeId the stipcodeId to set
	 */
	public void setStipcodeId(Integer stipcodeId) {
		this.stipcodeId = stipcodeId;
	}

	/**
	 * @return the stipcodeCode
	 */
	public String getStipcodeCode() {
		return stipcodeCode;
	}

	/**
	 * @param stipcodeCode the stipcodeCode to set
	 */
	public void setStipcodeCode(String stipcodeCode) {
		this.stipcodeCode = stipcodeCode;
	}

	/**
	 * @return the stipcodeDesc
	 */
	public String getStipcodeDesc() {
		return stipcodeDesc;
	}

	/**
	 * @param stipcodeDesc the stipcodeDesc to set
	 */
	public void setStipcodeDesc(String stipcodeDesc) {
		this.stipcodeDesc = stipcodeDesc;
	}

	/**
	 * @return the siacRBilElemStipendioCodices
	 */
	public List<SiacRBilElemStipendioCodice> getSiacRBilElemStipendioCodices() {
		return siacRBilElemStipendioCodices;
	}

	/**
	 * @param siacRBilElemStipendioCodices the siacRBilElemStipendioCodices to set
	 */
	public void setSiacRBilElemStipendioCodices(
			List<SiacRBilElemStipendioCodice> siacRBilElemStipendioCodices) {
		this.siacRBilElemStipendioCodices = siacRBilElemStipendioCodices;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#getUid()
	 */
	@Override
	public Integer getUid() {
		return stipcodeId;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.entity.SiacTBase#setUid(java.lang.Integer)
	 */
	@Override
	public void setUid(Integer uid) {
		this.stipcodeId = uid;
		
	}
	
	

}
