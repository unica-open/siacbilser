/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity.base;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class SiacLoginMultiplo extends SiacTEnteBase {
	private static final long serialVersionUID = 1L;

	@Basic
	@Column(name="login_cancellazione")
	private String loginCancellazione;

	@Basic
	@Column(name="login_creazione")
	private String loginCreazione;

	@Basic
	@Column(name="login_modifica")
	private String loginModifica;

	public String getLoginCancellazione() {
		return loginCancellazione;
	}

	public void setLoginCancellazione(String loginCancellazione) {
		this.loginCancellazione = loginCancellazione;
	}

	public String getLoginCreazione() {
		return loginCreazione;
	}

	public void setLoginCreazione(String loginCreazione) {
		this.loginCreazione = loginCreazione;
	}

	public String getLoginModifica() {
		return loginModifica;
	}

	public void setLoginModifica(String loginModifica) {
		this.loginModifica = loginModifica;
	}

}
