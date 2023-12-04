/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;

import org.springframework.data.domain.Page;

import it.csi.siac.siaccommonser.integration.entity.SiacTBase;

public class EsitoRicercaPageableDto <T extends SiacTBase> implements Serializable {

	private static final long serialVersionUID = 1L;

	private Page<T> entitiesList;
	private Page<Integer> soloIdsList;
	
	public Page<T> getEntitiesList() {
		return entitiesList;
	}
	public void setEntitiesList(Page<T> entitiesList) {
		this.entitiesList = entitiesList;
	}
	public Page<Integer> getSoloIdsList() {
		return soloIdsList;
	}
	public void setSoloIdsList(Page<Integer> soloIdsList) {
		this.soloIdsList = soloIdsList;
	}
	
}
