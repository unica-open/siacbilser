/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.cassaeconomale.rendiconto.model;

import javax.xml.bind.annotation.XmlType;

import it.csi.siac.siacbilser.business.service.stampa.base.ReportInternalSvcDictionary;
import it.csi.siac.siaccecser.model.OperazioneCassa;
@XmlType(namespace = ReportInternalSvcDictionary.NAMESPACE)
public class StampaRendicontoCassaOperazioneCassa extends OperazioneCassa {

	/** per serializzazione **/
	private static final long serialVersionUID = 6703923727167973751L;
	
	private String descrizione;

	/**
	 * @return the descrizione
	 */
	public final String getDescrizione() {
		return descrizione;
	}

	/**
	 * @param descrizione the descrizione to set
	 */
	public final void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
}
