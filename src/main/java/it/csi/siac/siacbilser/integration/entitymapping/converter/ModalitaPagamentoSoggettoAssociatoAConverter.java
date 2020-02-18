/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.List;

import org.dozer.DozerConverter;

import it.csi.siac.siacbilser.integration.entity.SiacRSoggettoRelaz;
import it.csi.siac.siacbilser.integration.entity.SiacTModpag;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;

/**
 * The Class ModalitaPagamentoSoggettoAssociatoAConverter.
 */
public class ModalitaPagamentoSoggettoAssociatoAConverter extends DozerConverter<ModalitaPagamentoSoggetto, SiacTModpag> {
	
	// Conforme con FIN?
	private static final String SOGGETTO = "Soggetto";
	private static final String REL_SEDE_SECONDARIA = "SEDE_SECONDARIA";
	
	/**
	 * Instantiates a new modalita pagamento soggetto associato a converter
	 */
	public ModalitaPagamentoSoggettoAssociatoAConverter() {
		super(ModalitaPagamentoSoggetto.class, SiacTModpag.class);
	}

	@Override
	public ModalitaPagamentoSoggetto convertFrom(SiacTModpag src, ModalitaPagamentoSoggetto dest) {
		if(src.getSiacTSoggetto() != null) {
			List<SiacRSoggettoRelaz> siacRSoggettoRelazs1 = src.getSiacTSoggetto().getSiacRSoggettoRelazs1();
			List<SiacRSoggettoRelaz> siacRSoggettoRelazs2 = src.getSiacTSoggetto().getSiacRSoggettoRelazs2();
			
			// Soggetto: !siacRSoggettoRelazs1.isEmpty()
			// Sede secondaria: !siacRSoggettoRelazs2.isEmpty()
			String associatoA = "";
			if(siacRSoggettoRelazs1 != null && !siacRSoggettoRelazs1.isEmpty()) {
				for(SiacRSoggettoRelaz srsr : siacRSoggettoRelazs1) {
					if(srsr.getDataCancellazione() == null && srsr.getSiacDRelazTipo() != null
							&& REL_SEDE_SECONDARIA.equals(srsr.getSiacDRelazTipo().getRelazTipoCode())) {
						// Soggetto
						associatoA = SOGGETTO;
					}
				}
			} else if(siacRSoggettoRelazs2 != null && !siacRSoggettoRelazs2.isEmpty()) {
				for(SiacRSoggettoRelaz srsr : siacRSoggettoRelazs2) {
					if(srsr.getDataCancellazione() == null && srsr.getSiacDRelazTipo() != null
							&& REL_SEDE_SECONDARIA.equals(srsr.getSiacDRelazTipo().getRelazTipoCode())) {
						// Sede secondaria
						associatoA = src.getSiacTSoggetto().getSoggettoDesc();
					}
				}
			}
			
			dest.setAssociatoA(associatoA);
		}
		return dest;
	}
	
	@Override
	public SiacTModpag convertTo(ModalitaPagamentoSoggetto src, SiacTModpag dest) {
		return dest;
	}



}
