/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter.cespiti;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTCespitiElabAmmortamentiDett;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDOperazioneEpEnum;
import it.csi.siac.siacbilser.integration.entitymapping.converter.ExtendedDozerConverter;
import it.csi.siac.siaccespser.model.DettaglioAnteprimaAmmortamentoAnnuoCespite;
import it.csi.siac.siacgenser.model.OperazioneSegnoConto;

/**
 * The Class DocumentoEntrataSubdocumentoIvaConverter.
 */
@Component
public class DettaglioAnteprimaAmmortamentoAnnuoCespiteSegnoConverter extends ExtendedDozerConverter<DettaglioAnteprimaAmmortamentoAnnuoCespite, SiacTCespitiElabAmmortamentiDett> {
	

	/**
	 * Instantiates a new subdocumento Entrata subdocumento iva converter.
	 */
	public DettaglioAnteprimaAmmortamentoAnnuoCespiteSegnoConverter() {
		super(DettaglioAnteprimaAmmortamentoAnnuoCespite.class, SiacTCespitiElabAmmortamentiDett.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public DettaglioAnteprimaAmmortamentoAnnuoCespite convertFrom(SiacTCespitiElabAmmortamentiDett src, DettaglioAnteprimaAmmortamentoAnnuoCespite dest) {
		
		if(src.getElabDetSegno() == null){
			return dest;
		}
		
		SiacDOperazioneEpEnum siacDOperazioneEpEnum = SiacDOperazioneEpEnum.byDescrizione(StringUtils.trim(src.getElabDetSegno()));
		OperazioneSegnoConto operazioneSegnoConto = (OperazioneSegnoConto) siacDOperazioneEpEnum.getOperazione();
		dest.setSegno(operazioneSegnoConto);
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTCespitiElabAmmortamentiDett convertTo(DettaglioAnteprimaAmmortamentoAnnuoCespite src, SiacTCespitiElabAmmortamentiDett dest) {
		if(src.getSegno() == null){
			return dest;
		}
		
		SiacDOperazioneEpEnum siacDOperazioneEpEnum = SiacDOperazioneEpEnum.byOperazione(src.getSegno());
		String segno = siacDOperazioneEpEnum.getDescrizione();
		dest.setElabDetSegno(segno ); //FIXME!!!! sta salvando una descrizione di un enum!!!! ricontrollare!
		return dest;
	}



	

}
