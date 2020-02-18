/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.model.siopeplus.SiopeTipoDebito;

/**
 * The Class SubdocumentoSpesaSiopeTipoDebitoConverter.
 */
@Component
public class SubdocumentoSpesaSiopeTipoDebitoConverter extends ExtendedDozerConverter<SubdocumentoSpesa, SiacTSubdoc> {

	/**
	 * Instantiates a new subdocumento spesa siope tipo debito converter.
	 */
	public SubdocumentoSpesaSiopeTipoDebitoConverter() {
		super(SubdocumentoSpesa.class, SiacTSubdoc.class);
	}

	@Override
	public SubdocumentoSpesa convertFrom(SiacTSubdoc src, SubdocumentoSpesa dest) {
		if(src.getSiacDSiopeTipoDebito() == null || dest == null) {
			return dest;
		}
		
		SiopeTipoDebito siopeTipoDebito = map(src.getSiacDSiopeTipoDebito(), SiopeTipoDebito.class, BilMapId.SiacDSiopeTipoDebito_SiopeTipoDebito);
		dest.setSiopeTipoDebito(siopeTipoDebito);
		
		return dest;
	}

	@Override
	public SiacTSubdoc convertTo(SubdocumentoSpesa src, SiacTSubdoc dest) {
		// SIAC-5471: il tipo debito SIOPE non deve essere copiato. Mantengo il codice in caso di necessita'
//		if(src.getSiopeTipoDebito() == null || src.getSiopeTipoDebito().getUid() == 0 || dest == null){
//			return dest;
//		}
//		SiacDSiopeTipoDebito siacDSiopeTipoDebito = new SiacDSiopeTipoDebito();
//		siacDSiopeTipoDebito.setUid(src.getSiopeTipoDebito().getUid());
//		dest.setSiacDSiopeTipoDebito(siacDSiopeTipoDebito);
		return dest;
	}

}
