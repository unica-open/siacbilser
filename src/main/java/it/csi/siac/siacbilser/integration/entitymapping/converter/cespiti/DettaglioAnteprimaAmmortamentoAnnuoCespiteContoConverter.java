/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter.cespiti;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTCespitiElabAmmortamentiDett;
import it.csi.siac.siacbilser.integration.entity.SiacTPdceConto;
import it.csi.siac.siacbilser.integration.entitymapping.converter.ExtendedDozerConverter;
import it.csi.siac.siaccespser.model.DettaglioAnteprimaAmmortamentoAnnuoCespite;
import it.csi.siac.siacgenser.model.Conto;

/**
 * The Class DocumentoEntrataSubdocumentoIvaConverter.
 */
@Component
public class DettaglioAnteprimaAmmortamentoAnnuoCespiteContoConverter extends ExtendedDozerConverter<DettaglioAnteprimaAmmortamentoAnnuoCespite, SiacTCespitiElabAmmortamentiDett> {
	

	/**
	 * Instantiates a new subdocumento Entrata subdocumento iva converter.
	 */
	public DettaglioAnteprimaAmmortamentoAnnuoCespiteContoConverter() {
		super(DettaglioAnteprimaAmmortamentoAnnuoCespite.class, SiacTCespitiElabAmmortamentiDett.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public DettaglioAnteprimaAmmortamentoAnnuoCespite convertFrom(SiacTCespitiElabAmmortamentiDett src, DettaglioAnteprimaAmmortamentoAnnuoCespite dest) {
		
		SiacTPdceConto siacTPdceConto = src.getSiacTPdceConto();
		if( siacTPdceConto == null){
			return dest;
		}
		
		Conto conto = new Conto();
		conto.setUid(siacTPdceConto.getUid());
		conto.setCodice(siacTPdceConto.getPdceContoCode());
		conto.setDescrizione(siacTPdceConto.getPdceContoDesc());
		
		dest.setConto(conto);
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
