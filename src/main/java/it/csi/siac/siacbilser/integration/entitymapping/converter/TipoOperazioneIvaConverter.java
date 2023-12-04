/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.dozer.DozerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacDIvaOperazioneTipo;
import it.csi.siac.siacbilser.integration.entity.SiacTIvaAliquota;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDIvaOperazioneTipoEnum;
import it.csi.siac.siacfin2ser.model.TipoOperazioneIva;

// TODO: Auto-generated Javadoc
/**
 * Converter per il tipo di operazione Iva.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 27/05/2014
 *
 */
@Component
public class TipoOperazioneIvaConverter extends DozerConverter<TipoOperazioneIva, SiacTIvaAliquota> {
	
	/** The eef. */
	@Autowired
	private EnumEntityFactory eef;

	/**
	 *  Costruttore vuoto.
	 */
	public TipoOperazioneIvaConverter() {
		super(TipoOperazioneIva.class, SiacTIvaAliquota.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public TipoOperazioneIva convertFrom(SiacTIvaAliquota src, TipoOperazioneIva dest) {
		// Caso base: se non ho legata l'entity, restituisco il null sull'enum
		if(src == null || src.getSiacDIvaOperazioneTipo() == null) {
			return dest;
		}
		// Restituisco l'enum di pari codice
		return TipoOperazioneIva.fromCodice(src.getSiacDIvaOperazioneTipo().getIvaopTipoCode());
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTIvaAliquota convertTo(TipoOperazioneIva src, SiacTIvaAliquota dest) {
		SiacDIvaOperazioneTipoEnum siacDIvaOperazioneTipoEnum = SiacDIvaOperazioneTipoEnum.byTipoOperazioneIvaEvenNull(src);
		if(siacDIvaOperazioneTipoEnum == null) {
			return dest;
		}
		
		SiacDIvaOperazioneTipo siacDIvaOperazioneTipo = eef.getEntity(siacDIvaOperazioneTipoEnum, dest.getSiacTEnteProprietario().getEnteProprietarioId(),
				SiacDIvaOperazioneTipo.class);
		dest.setSiacDIvaOperazioneTipo(siacDIvaOperazioneTipo);
		
		return dest;
		
	}
	
}
