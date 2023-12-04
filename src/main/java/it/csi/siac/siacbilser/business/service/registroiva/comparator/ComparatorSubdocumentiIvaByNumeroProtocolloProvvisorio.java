/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.registroiva.comparator;

import java.io.Serializable;
import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;

import it.csi.siac.siacfin2ser.model.SubdocumentoIva;

/**
 * The Class ComparatorSubdocumentiIvaByNumeroProtocolloProvvisorio.
 */
public class ComparatorSubdocumentiIvaByNumeroProtocolloProvvisorio implements Comparator<SubdocumentoIva<?, ?, ?>>, Serializable {

	//per la serializzazione
	private static final long serialVersionUID = 1586552193562992576L;
	
	//istanza del comparatore: perche' mai usata?
	public static final ComparatorSubdocumentiIvaByNumeroProtocolloProvvisorio INSTANCE = new ComparatorSubdocumentiIvaByNumeroProtocolloProvvisorio();

	@Override
	public int compare(SubdocumentoIva<?, ?, ?> o1, SubdocumentoIva<?, ?, ?> o2) {
		
		//CASO 1: ENTRAMBI SONO NULL
		if(o1 == null && o2 == null) {
			//i due oggetti sono uguali: ritorno 0
			return 0;
		}
		
		//CASO 2: ALMENTO UNO DEI DUE E' NULL
		if(o1 == null) {
			//o2 e' maggiore di o1 se questo e' null
			return 1;
		}
		if(o2 == null) {
			//o2 e' minore di o1 se e' null
			return -1;
		}
		
		//CASO 3: NESSUNO DEI DUE OGGETTI E' NULL
		return new CompareToBuilder()
			//ordino in base al numero di protocollo provvisorio
			.append(o1.getNumeroProtocolloProvvisorio(), o2.getNumeroProtocolloProvvisorio())
			.toComparison();
	}
	
}
