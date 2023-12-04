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
 * The persistent class for the siac_d_pcc_operazione_tipo database table.
 * 
 */
@Entity
@Table(name="siac_d_pcc_operazione_tipo")
@NamedQuery(name="SiacDPccOperazioneTipo.findAll", query="SELECT s FROM SiacDPccOperazioneTipo s")
public class SiacDPccOperazioneTipo extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SIAC_D_PCC_OPERAZIONE_TIPO_PCCOPTIPOID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_PCC_OPERAZIONE_TIPO_PCCOP_TIPO_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_PCC_OPERAZIONE_TIPO_PCCOPTIPOID_GENERATOR")
	@Column(name="pccop_tipo_id")
	private Integer pccopTipoId;

	@Column(name="pccop_tipo_code")
	private String pccopTipoCode;

	@Column(name="pccop_tipo_desc")
	private String pccopTipoDesc;
	
	//bi-directional many-to-one association to SiacTRegistroPcc
	@OneToMany(mappedBy="siacDPccOperazioneTipo")
	private List<SiacTRegistroPcc> siacTRegistroPccs;

	public SiacDPccOperazioneTipo() {
	}

	public Integer getPccopTipoId() {
		return this.pccopTipoId;
	}

	public void setPccopTipoId(Integer pccopTipoId) {
		this.pccopTipoId = pccopTipoId;
	}

	public String getPccopTipoCode() {
		return this.pccopTipoCode;
	}

	public void setPccopTipoCode(String pccopTipoCode) {
		this.pccopTipoCode = pccopTipoCode;
	}

	public String getPccopTipoDesc() {
		return this.pccopTipoDesc;
	}

	public void setPccopTipoDesc(String pccopTipoDesc) {
		this.pccopTipoDesc = pccopTipoDesc;
	}

	public List<SiacTRegistroPcc> getSiacTRegistroPccs() {
		return this.siacTRegistroPccs;
	}

	public void setSiacTRegistroPccs(List<SiacTRegistroPcc> siacTRegistroPccs) {
		this.siacTRegistroPccs = siacTRegistroPccs;
	}

	public SiacTRegistroPcc addSiacTRegistroPcc(SiacTRegistroPcc siacTRegistroPcc) {
		getSiacTRegistroPccs().add(siacTRegistroPcc);
		siacTRegistroPcc.setSiacDPccOperazioneTipo(this);

		return siacTRegistroPcc;
	}

	public SiacTRegistroPcc removeSiacTRegistroPcc(SiacTRegistroPcc siacTRegistroPcc) {
		getSiacTRegistroPccs().remove(siacTRegistroPcc);
		siacTRegistroPcc.setSiacDPccOperazioneTipo(null);

		return siacTRegistroPcc;
	}

	@Override
	public Integer getUid() {
		return pccopTipoId;
	}

	@Override
	public void setUid(Integer uid) {
		this.pccopTipoId = uid;
	}

}