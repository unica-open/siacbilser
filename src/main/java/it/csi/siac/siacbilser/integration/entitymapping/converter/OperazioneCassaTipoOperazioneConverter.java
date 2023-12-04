/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacDCassaEconOperazTipo;
import it.csi.siac.siacbilser.integration.entity.SiacRCassaEconOperazTipo;
import it.csi.siac.siacbilser.integration.entity.SiacTCassaEconOperaz;
import it.csi.siac.siacbilser.integration.entitymapping.CecMapId;
import it.csi.siac.siaccecser.model.OperazioneCassa;
import it.csi.siac.siaccecser.model.TipoOperazioneCassa;

/**
 * The Class DocumentoEntrataSubdocumentoIvaConverter.
 */
@Component
public class OperazioneCassaTipoOperazioneConverter extends ExtendedDozerConverter<OperazioneCassa, SiacTCassaEconOperaz > {
	

	/**
	 * Instantiates a new subdocumento Entrata subdocumento iva converter.
	 */
	public OperazioneCassaTipoOperazioneConverter() {
		super(OperazioneCassa.class, SiacTCassaEconOperaz.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public OperazioneCassa convertFrom(SiacTCassaEconOperaz src, OperazioneCassa dest) {
		if(src.getSiacRCassaEconOperazTipos() == null){
			return dest;
		}
		for(SiacRCassaEconOperazTipo siacRCassaEconOperazTipo: src.getSiacRCassaEconOperazTipos()){
			if(siacRCassaEconOperazTipo.getDataCancellazione() == null){
				SiacDCassaEconOperazTipo siacDCassaEconOperazTipo = siacRCassaEconOperazTipo.getSiacDCassaEconOperazTipo();
				TipoOperazioneCassa tipoOperazioneCassa = new TipoOperazioneCassa();
				map(siacDCassaEconOperazTipo, tipoOperazioneCassa , CecMapId.SiacDCassaEconOperazTipo_TipoOperazioneCassa);
				dest.setTipoOperazioneCassa(tipoOperazioneCassa);
				break;
			}
		}
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTCassaEconOperaz convertTo(OperazioneCassa src, SiacTCassaEconOperaz dest) {
		if(src.getTipoOperazioneCassa() == null){
			return dest;
		}
		SiacDCassaEconOperazTipo siacDCassaEconOperazTipo = new SiacDCassaEconOperazTipo();
		map(src.getTipoOperazioneCassa(), siacDCassaEconOperazTipo, CecMapId.SiacDCassaEconOperazTipo_TipoOperazioneCassa); 
		
		List<SiacRCassaEconOperazTipo> siacRCassaEconOperazTipos = new ArrayList<SiacRCassaEconOperazTipo>();
		SiacRCassaEconOperazTipo siacRCassaEconOperazTipo = new SiacRCassaEconOperazTipo();
		siacRCassaEconOperazTipo.setSiacDCassaEconOperazTipo(siacDCassaEconOperazTipo);
		siacRCassaEconOperazTipo.setSiacTCassaEconOperaz(dest);
		siacRCassaEconOperazTipo.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		siacRCassaEconOperazTipo.setLoginOperazione(dest.getLoginOperazione());
		siacRCassaEconOperazTipos.add(siacRCassaEconOperazTipo);
		dest.setSiacRCassaEconOperazTipos(siacRCassaEconOperazTipos);
		return dest;
	}



	

}
