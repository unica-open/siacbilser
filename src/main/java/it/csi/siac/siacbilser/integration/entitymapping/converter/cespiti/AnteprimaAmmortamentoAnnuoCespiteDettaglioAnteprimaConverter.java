/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter.cespiti;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTCespitiElabAmmortamentiDett;
import it.csi.siac.siacbilser.integration.entitymapping.CespMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.ExtendedDozerConverter;
import it.csi.siac.siaccespser.model.AnteprimaAmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.model.DettaglioAnteprimaAmmortamentoAnnuoCespite;

/**
 * The Class DocumentoEntrataSubdocumentoIvaConverter.
 */
@Component
public class AnteprimaAmmortamentoAnnuoCespiteDettaglioAnteprimaConverter extends ExtendedDozerConverter<DettaglioAnteprimaAmmortamentoAnnuoCespite, SiacTCespitiElabAmmortamentiDett> {
	

	/**
	 * Instantiates a new subdocumento Entrata subdocumento iva converter.
	 */
	public AnteprimaAmmortamentoAnnuoCespiteDettaglioAnteprimaConverter() {
		super(DettaglioAnteprimaAmmortamentoAnnuoCespite.class, SiacTCespitiElabAmmortamentiDett.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public DettaglioAnteprimaAmmortamentoAnnuoCespite convertFrom(SiacTCespitiElabAmmortamentiDett src, DettaglioAnteprimaAmmortamentoAnnuoCespite dest) {
		mapNotNull(src.getSiacTCespitiElabAmmortamenti(), AnteprimaAmmortamentoAnnuoCespite.class, CespMapId.SiacTCespitiElabAmmortamenti_AnteprimaAmmortamentoAnnuoCespite_ModelDetail);
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTCespitiElabAmmortamentiDett convertTo(DettaglioAnteprimaAmmortamentoAnnuoCespite src, SiacTCespitiElabAmmortamentiDett dest) {
		return dest;
	}



	

}
