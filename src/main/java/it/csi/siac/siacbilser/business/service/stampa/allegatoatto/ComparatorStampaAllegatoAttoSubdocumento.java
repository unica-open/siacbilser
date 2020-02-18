/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.allegatoatto;

import java.io.Serializable;
import java.util.Comparator;

import org.apache.commons.lang.builder.CompareToBuilder;

import it.csi.siac.siacbilser.business.service.stampa.allegatoatto.model.StampaAllegatoAttoSubdocumento;

/**
 * Comparator per i dati del subdocumento nella stampa allegato atto
 * @author Marchino Alessandro
 *
 */
public final class ComparatorStampaAllegatoAttoSubdocumento implements Comparator<StampaAllegatoAttoSubdocumento>, Serializable {

	/** Per la serializzazione */
	private static final long serialVersionUID = 2492186358512420687L;
	/** Unica istanza da utilizzare */
	public static final ComparatorStampaAllegatoAttoSubdocumento INSTANCE = new ComparatorStampaAllegatoAttoSubdocumento();
	
	private ComparatorStampaAllegatoAttoSubdocumento() {
	}
	
	@Override
	public int compare(StampaAllegatoAttoSubdocumento o1, StampaAllegatoAttoSubdocumento o2) {
		if(o1.getSubdocumentoSpesa() == null && o2.getSubdocumentoSpesa() == null) {
			return 0;
		}
		if(o1.getSubdocumentoSpesa() == null) {
			return 1;
		}
		if(o2.getSubdocumentoSpesa() == null) {
			return -1;
		}
		return new CompareToBuilder()
				.append(o1.getSubdocumentoSpesa().getDocumento().getTipoDocumento().getCodice(), o2.getSubdocumentoSpesa().getDocumento().getTipoDocumento().getCodice())
				.append(o1.getSubdocumentoSpesa().getDocumento().getAnno(), o2.getSubdocumentoSpesa().getDocumento().getAnno())
				.append(o1.getSubdocumentoSpesa().getDocumento().getNumero(), o2.getSubdocumentoSpesa().getDocumento().getNumero())
				.toComparison();
	}

}
