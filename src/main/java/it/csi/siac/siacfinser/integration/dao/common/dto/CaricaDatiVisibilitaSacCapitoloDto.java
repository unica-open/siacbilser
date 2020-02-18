/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.util.List;

public class CaricaDatiVisibilitaSacCapitoloDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean visibiliAll;
	private List<Integer> idSacVisibili;

	public boolean isVisibiliAll() {
		return visibiliAll;
	}

	public void setVisibiliAll(boolean visibiliAll) {
		this.visibiliAll = visibiliAll;
	}

	public List<Integer> getIdSacVisibili() {
		return idSacVisibili;
	}

	public void setIdSacVisibili(List<Integer> idSacVisibili) {
		this.idSacVisibili = idSacVisibili;
	}
	
}
