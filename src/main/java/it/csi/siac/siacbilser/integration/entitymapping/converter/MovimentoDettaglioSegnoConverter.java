/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTMovEpDet;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDOperazioneEpEnum;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.OperazioneSegnoConto;

/**
 * The Class DocumentoEntrataSubdocumentoIvaConverter.
 */
@Component
public class MovimentoDettaglioSegnoConverter extends ExtendedDozerConverter<MovimentoDettaglio, SiacTMovEpDet> {
	

	/**
	 * Instantiates a new subdocumento Entrata subdocumento iva converter.
	 */
	public MovimentoDettaglioSegnoConverter() {
		super(MovimentoDettaglio.class, SiacTMovEpDet.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public MovimentoDettaglio convertFrom(SiacTMovEpDet src, MovimentoDettaglio dest) {
		
		if(src.getMovepDetSegno() == null){
			return dest;
		}
		
		SiacDOperazioneEpEnum siacDOperazioneEpEnum = SiacDOperazioneEpEnum.byDescrizione(StringUtils.trim(src.getMovepDetSegno()));
		OperazioneSegnoConto operazioneSegnoConto = (OperazioneSegnoConto) siacDOperazioneEpEnum.getOperazione();
		dest.setSegno(operazioneSegnoConto);
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTMovEpDet convertTo(MovimentoDettaglio src, SiacTMovEpDet dest) {
		if(src.getSegno() == null){
			return dest;
		}
		
		SiacDOperazioneEpEnum siacDOperazioneEpEnum = SiacDOperazioneEpEnum.byOperazione(src.getSegno());
		String segno = siacDOperazioneEpEnum.getDescrizione();
		dest.setMovepDetSegno(segno ); //FIXME!!!! sta salvando una descrizione di un enum!!!! ricontrollare!
		return dest;
	}



	

}
