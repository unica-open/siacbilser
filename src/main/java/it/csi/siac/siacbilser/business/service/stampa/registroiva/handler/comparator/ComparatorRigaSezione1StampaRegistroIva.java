/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.registroiva.handler.comparator;

import java.util.Comparator;

import org.apache.commons.lang.builder.CompareToBuilder;

import it.csi.siac.siacbilser.business.service.stampa.base.DatiIva;
import it.csi.siac.siacbilser.business.service.stampa.registroiva.model.StampaRegistoIvaDatoIva;

/**
 * Comparator per le righe della StampaRegistroIva.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 29/07/2014
 *
 */
public final class ComparatorRigaSezione1StampaRegistroIva implements Comparator<DatiIva> {

	public static final ComparatorRigaSezione1StampaRegistroIva DEFINITIVO = new ComparatorRigaSezione1StampaRegistroIva(true);
	public static final ComparatorRigaSezione1StampaRegistroIva PROVVISORIO = new ComparatorRigaSezione1StampaRegistroIva(false);
	
	private final boolean definitivo;
	
	private ComparatorRigaSezione1StampaRegistroIva(boolean definitivo) {
		this.definitivo = definitivo;
	}
	
	@Override
	public int compare(DatiIva o1, DatiIva o2) {
		if(!(o1 instanceof StampaRegistoIvaDatoIva) || !(o2 instanceof StampaRegistoIvaDatoIva)) {
			// Non posso compararli
			return 0;
		}
		
		StampaRegistoIvaDatoIva<?, ?, ?> r1 = (StampaRegistoIvaDatoIva<?, ?, ?>) o1;
		StampaRegistoIvaDatoIva<?, ?, ?> r2 = (StampaRegistoIvaDatoIva<?, ?, ?>) o2;
		
		int compareDataDocumento = new CompareToBuilder().append(r1.getDocumento().getDataEmissione(), r2.getDocumento().getDataEmissione())
				.toComparison();
		
		if(compareDataDocumento != 0) {
			return compareDataDocumento;
		}
		
		Integer numProtocollo1 = definitivo ? r1.getSubdocumentoIva().getNumeroProtocolloDefinitivo() : r1.getSubdocumentoIva().getNumeroProtocolloProvvisorio();
		Integer numProtocollo2 = definitivo ? r2.getSubdocumentoIva().getNumeroProtocolloDefinitivo() : r2.getSubdocumentoIva().getNumeroProtocolloProvvisorio();
		
		return new CompareToBuilder().append(numProtocollo1, numProtocollo2)
				.toComparison();
	}

}
