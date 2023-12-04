/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTCassaEcon;
import it.csi.siac.siacbilser.integration.entity.SiacTCassaEconOperaz;
import it.csi.siac.siacbilser.integration.entitymapping.CecMapId;
import it.csi.siac.siaccecser.model.CassaEconomale;
import it.csi.siac.siaccecser.model.OperazioneCassa;

/**
 * The Class DocumentoEntrataSubdocumentoIvaConverter.
 */
@Component
public class CassaEconomaleOperazioniCassaConverter extends ExtendedDozerConverter<CassaEconomale, SiacTCassaEcon > {
	
	/**
	 * Instantiates a new subdocumento Entrata subdocumento iva converter.
	 */
	public CassaEconomaleOperazioniCassaConverter() {
		super(CassaEconomale.class, SiacTCassaEcon.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public CassaEconomale convertFrom(SiacTCassaEcon src, CassaEconomale dest) {
		final String methodName = "convertFrom";
		
		if(src.getSiacTCassaEconOperazs() == null || src.getSiacTCassaEconOperazs().isEmpty()){
			log.debug(methodName, "non ci sono operazioni associate");
			return dest;
		}
		
		List<OperazioneCassa> listaOperazioni = new ArrayList<OperazioneCassa>();
		
		for(SiacTCassaEconOperaz siacTCassaEconOperaz : src.getSiacTCassaEconOperazs()){
			if(siacTCassaEconOperaz.getDataCancellazione() == null){
				
				OperazioneCassa operazioneCassa = new OperazioneCassa();
				map(siacTCassaEconOperaz, operazioneCassa, CecMapId.SiacTCassaEconOperaz_OperazioneCassa_Base);
				listaOperazioni.add(operazioneCassa);
			}
		}
		
		dest.setOperazioniCassaEconomale(listaOperazioni);
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTCassaEcon convertTo(CassaEconomale src, SiacTCassaEcon dest) {
	
		return dest;
	}



	

}
