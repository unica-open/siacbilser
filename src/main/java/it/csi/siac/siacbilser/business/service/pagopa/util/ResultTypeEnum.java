/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.pagopa.util;

import it.csi.epay.epaywso.types.ResultType;

public enum ResultTypeEnum {
	DEFAULT_RT000_OK("000", "Operazione completata correttamente."),
	TRASMETTI_FLUSSI_RT001_OK("001", "Flusso trasmesso correttamente."),
	
	DEFAULT_RT100_ERRORI_APPLICATIVI("100", "Impossibile completare l'operazione: errori di natura applicativa."),
	
	DEFAULT_RT200_ERRORI_DI_SISTEMA("200", "Impossibile completare l'operazione: errori di natura sistemistica."),
	
	DEFAULT_RT099_NON_IMPLEMENTATO("099", "Operazione non implementata"),
	;
	
	private String codiceErrore;
	private String messaggioErrrore;

	ResultTypeEnum(String codiceErrore, String messaggioErrrore){
		this.codiceErrore = codiceErrore;
		this.messaggioErrrore = messaggioErrrore;
	}
	
	public ResultType getResultType() {
		ResultType rt = new ResultType();
		rt.setCodice(getCodiceErrore());
		rt.setMessaggio(getMessaggioErrrore());
		return rt;
	}
	
	public ResultType getResultType(String messaggioErroreAggiuntivo) {
		ResultType rt = new ResultType();
		rt.setCodice(getCodiceErrore());
		rt.setMessaggio(String.format("%s %s", getCodiceErrore(), messaggioErroreAggiuntivo));
		return rt;
	}
	
	
	public String getCodiceErrore() {
		return codiceErrore;
	}

	public String getMessaggioErrrore() {
		return messaggioErrrore;
	}
}