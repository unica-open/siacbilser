/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.dozer.DozerConverter;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacDDocTipo;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocFamTipoEnum;
import it.csi.siac.siacfin2ser.model.TipoDocumento;

/**
 * The Class TipoDocumentoFamTipoConverter.
 */
@Component
public class TipoDocumentoFamTipoConverter extends DozerConverter<TipoDocumento, SiacDDocTipo > {

	/**
	 *  Costruttore vuoto.
	 */
	public TipoDocumentoFamTipoConverter() {
		super(TipoDocumento.class, SiacDDocTipo.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public TipoDocumento convertFrom(SiacDDocTipo src, TipoDocumento dest) {
		// Caso base: se non ho legata l'entity, restituisco il null sull'enum
		if(src.getSiacDDocFamTipo()==null) {
			return dest;
		}
			
		if(src.getSiacDDocFamTipo().getDataCancellazione()==null){
			dest.setTipoFamigliaDocumento( SiacDDocFamTipoEnum.byCodice(src.getSiacDDocFamTipo().getDocFamTipoCode()).getTipoFamigliaDocumento());
		}
		
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacDDocTipo convertTo( TipoDocumento src, SiacDDocTipo dest) {
		
		/*SiacDDocFamTipoEnum siacDDocFamTipoEnum = SiacDDocFamTipoEnum.byTipoFamigliaDocumentoEvenNull(src.getTipoFamigliaDocumento());
		
		if(siacDDocFamTipoEnum == null) {
			return dest;
		}
		
		SiacDDocFamTipo siacDDocFamTipo = eef.getEntity(siacDDocFamTipoEnum, dest.getSiacTEnteProprietario().getEnteProprietarioId(),
				SiacDDocFamTipo.class);
		
		dest.setSiacDDocFamTipo(siacDDocFamTipo);*/
		return dest;
		
	}
	

}
