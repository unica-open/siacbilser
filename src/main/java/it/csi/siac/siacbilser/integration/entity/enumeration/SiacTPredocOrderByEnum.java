/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacfin2ser.frontend.webservice.msg.OrdinamentoPreDocumentoEntrata;

public enum SiacTPredocOrderByEnum implements OrderByEnum{

	PREDOC_NUMERO(OrdinamentoPreDocumentoEntrata.NUMERO_PREDOCUMENTO, "CAST({0}.predocNumero AS int)"),
	PREDOC_PERIODO_COMPETENZA(OrdinamentoPreDocumentoEntrata.PERIODO_COMPETENZA, "{0}.predocPeriodoCompetenza"),
	PREDOC_DATA_COMPETENZA_NOMINATIVO(OrdinamentoPreDocumentoEntrata.DATA_COMPETENZA_NOMINATIVO, "{0}.predocDataCompetenza, {1}.predocanNome, {1}.predocanCognome, {1}.predocanRagioneSociale"),
	;
	private final OrdinamentoPreDocumentoEntrata ordinamentoPreDocumentoEntrata;
	private final String orderByClauseTemplate;
	
	/**
	 * Costruttore
	 * @param ordinamentoPreDocumentoEntrata l'ordinamento richiesto
	 * @param orderByClauseTemplate il template della clausola di order by
	 */
	private SiacTPredocOrderByEnum(OrdinamentoPreDocumentoEntrata ordinamentoPreDocumentoEntrata, String orderByClauseTemplate) {
		this.ordinamentoPreDocumentoEntrata = ordinamentoPreDocumentoEntrata;
		this.orderByClauseTemplate = orderByClauseTemplate;
	}

	@Override
	public String getOrderByClause(String... alias) {
		return MessageFormat.format(orderByClauseTemplate, (Object[]) alias);
	}
	
	public static SiacTPredocOrderByEnum byOrdinamentoPreDocumentoEntrata(OrdinamentoPreDocumentoEntrata ordinamentoPreDocumentoEntrata) {
		for(SiacTPredocOrderByEnum stpobe : values()) {
			if(stpobe.ordinamentoPreDocumentoEntrata.equals(ordinamentoPreDocumentoEntrata)) {
				return stpobe;
			}
		}
		return null;
	}
	
	public static List<SiacTPredocOrderByEnum> byOrdinamentoPreDocumentoEntrata(List<OrdinamentoPreDocumentoEntrata> ordinamentoPreDocumentoEntrata) {
		List<SiacTPredocOrderByEnum> res = new ArrayList<SiacTPredocOrderByEnum>();
		for(OrdinamentoPreDocumentoEntrata opde : ordinamentoPreDocumentoEntrata) {
			SiacTPredocOrderByEnum stpobe = byOrdinamentoPreDocumentoEntrata(opde);
			if(stpobe != null) {
				res.add(stpobe);
			}
		}
		return res;
	}
	
}
