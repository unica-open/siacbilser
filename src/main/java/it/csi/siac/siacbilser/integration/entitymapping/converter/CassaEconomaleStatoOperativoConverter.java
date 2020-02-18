/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.Date;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTCassaEcon;
import it.csi.siac.siaccecser.model.CassaEconomale;
import it.csi.siac.siaccecser.model.StatoOperativoCassaEconomale;

/**
 * The Class DocumentoEntrataSubdocumentoIvaConverter.
 */
@Component
public class CassaEconomaleStatoOperativoConverter extends ExtendedDozerConverter<CassaEconomale, SiacTCassaEcon > {
	

	/**
	 * Instantiates a new subdocumento Entrata subdocumento iva converter.
	 */
	public CassaEconomaleStatoOperativoConverter() {
		super(CassaEconomale.class, SiacTCassaEcon.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public CassaEconomale convertFrom(SiacTCassaEcon src, CassaEconomale dest) {
		
		Date dataFineValidita = src.getDataFineValidita();
		Date now = new Date();
		
		if(src.getDataCancellazione() == null && (dataFineValidita == null || dataFineValidita.after(now)) ){
			dest.setStatoOperativoCassaEconomale(StatoOperativoCassaEconomale.VALIDA);
		}else{
			dest.setStatoOperativoCassaEconomale(StatoOperativoCassaEconomale.ANNULLATA);
		}
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
