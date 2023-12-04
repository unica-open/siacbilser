/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.dozer.DozerConverter;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacDIvaRegistrazioneTipo;
import it.csi.siac.siacbilser.integration.entity.SiacRIvaRegTipoDocFamTipo;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocFamTipoEnum;
import it.csi.siac.siacfin2ser.model.TipoRegistrazioneIva;


/**
 * The Class TipoRegistrazioneIvaFlagsConverter.
 */
@Component
public class TipoRegistrazioneIvaFlagsConverter extends DozerConverter<TipoRegistrazioneIva, SiacDIvaRegistrazioneTipo> {
	
	/**
	 *  Costruttore vuoto.
	 */
	public TipoRegistrazioneIvaFlagsConverter() {
		super(TipoRegistrazioneIva.class, SiacDIvaRegistrazioneTipo.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public TipoRegistrazioneIva convertFrom(SiacDIvaRegistrazioneTipo src, TipoRegistrazioneIva dest) {
		if(src.getSiacRIvaRegTipoDocFamTipos()!=null){
			for (SiacRIvaRegTipoDocFamTipo r: src.getSiacRIvaRegTipoDocFamTipos()){
				if(r.getDataCancellazione()!=null){
					continue;
				}
				String famTipoCode = r.getSiacDDocFamTipo().getDocFamTipoCode();
				if(SiacDDocFamTipoEnum.Entrata.getCodice().equals(famTipoCode)){
					dest.setFlagTipoRegistrazioneIvaEntrata(Boolean.TRUE);
				} else if(SiacDDocFamTipoEnum.Spesa.getCodice().equals(famTipoCode)){
					dest.setFlagTipoRegistrazioneIvaSpesa(Boolean.TRUE);
				}
				
			}
		}
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacDIvaRegistrazioneTipo convertTo(TipoRegistrazioneIva src, SiacDIvaRegistrazioneTipo dest) {
		//Non effettuiamo inserimenti.
		return dest;
		
	}
	
}
