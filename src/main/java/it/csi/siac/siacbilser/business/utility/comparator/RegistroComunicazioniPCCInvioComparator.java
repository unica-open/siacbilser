/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.utility.comparator;

import java.io.Serializable;
import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;

import it.csi.siac.siacfin2ser.model.RegistroComunicazioniPCC;
import it.csi.siac.siacfin2ser.model.TipoOperazionePCC;

/**
 * Comparatore per l'invio delle comunicazioni a PCC.
 * Posiziona in testa le comunicazioni CCS, e a seguire le altre nell'ordine di inserimento
 * @author Marchino Alessandro
 *
 */
public final class RegistroComunicazioniPCCInvioComparator implements Comparator<RegistroComunicazioniPCC>, Serializable {

	/** Per la serializzazione */
	private static final long serialVersionUID = 983081664923407765L;
	
	public static final RegistroComunicazioniPCCInvioComparator INSTANCE = new RegistroComunicazioniPCCInvioComparator();
	
	private RegistroComunicazioniPCCInvioComparator() {
		// Prevent multiple instantiation, but not enforce it
	}
	
	@Override
	public int compare(RegistroComunicazioniPCC o1, RegistroComunicazioniPCC o2) {
		if(o1 == null && o2 == null) {
			return 0;
		}
		if(o1 == null) {
			return -1;
		}
		if(o2 == null) {
			return 1;
		}
		TipoOperazionePCC.Value value1 = TipoOperazionePCC.Value.byTipoOperazionePCC(o1.getTipoOperazionePCC());
		TipoOperazionePCC.Value value2 = TipoOperazionePCC.Value.byTipoOperazionePCC(o2.getTipoOperazionePCC());
		if(value1 == null && value2 == null) {
			return 0;
		}
		if(value1 == null) {
			return -1;
		}
		if(value2 == null) {
			return 1;
		}
		return new CompareToBuilder()
				.append(value1.getSortOrder(), value2.getSortOrder())
				.append(o2.getDataCreazione(), o1.getDataCreazione())
				.toComparison();
	}
	
}
