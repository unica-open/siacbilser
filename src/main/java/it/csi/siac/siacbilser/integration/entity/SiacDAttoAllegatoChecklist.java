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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name="siac_d_atto_allegato_checklist")
public class SiacDAttoAllegatoChecklist extends SiacTEnteBase {
	
	private static final long serialVersionUID = 3963608016172223805L;

	@Id
	@SequenceGenerator(name="SIAC_D_ATTO_ALLEGATO_CHECKLISTID_GENERATOR", allocationSize=1, sequenceName="SIAC_D_ATTO_ALLEGATO_CHECKLIST_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SIAC_D_ATTO_ALLEGATO_CHECKLISTID_GENERATOR")
	@Column(name="atto_allegato_checklist_id")
	private Integer attoAllegatoChecklistId;

	@Column(name="atto_allegato_checklist_code")
	private String attoAllegatoChecklistCode;

	@Column(name="atto_allegato_checklist_desc")
	private String attoAllegatoChecklistDesc;


	@Override
	public Integer getUid() {
		return attoAllegatoChecklistId;
	}

	@Override
	public void setUid(Integer uid) {
		this.attoAllegatoChecklistId = uid;
		
	}

	public Integer getAttoAllegatoChecklistId() {
		return attoAllegatoChecklistId;
	}

	public void setAttoAllegatoChecklistId(Integer attoAllegatoChecklistId) {
		this.attoAllegatoChecklistId = attoAllegatoChecklistId;
	}

	public String getAttoAllegatoChecklistCode() {
		return attoAllegatoChecklistCode;
	}

	public void setAttoAllegatoChecklistCode(String attoAllegatoChecklistCode) {
		this.attoAllegatoChecklistCode = attoAllegatoChecklistCode;
	}

	public String getAttoAllegatoChecklistDesc() {
		return attoAllegatoChecklistDesc;
	}

	public void setAttoAllegatoChecklistDesc(String attoAllegatoChecklistDesc) {
		this.attoAllegatoChecklistDesc = attoAllegatoChecklistDesc;
	}

}