/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.ProvvisorioDiCassaBilDao;
import it.csi.siac.siacbilser.integration.entity.SiacTProvCassa;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;

/**
 * The Class SubdocumentoSpesaProvvisorioDiCassaConverter
 */
@Component
public class ProvvisorioImportoDaRegolarizzareConverter extends ExtendedDozerConverter<ProvvisorioDiCassa, SiacTProvCassa> {

	@Autowired
	private ProvvisorioDiCassaBilDao provvisorioDiCassaBilDao;
	
	/**
	 * Instantiates a new subdocumento spesa subdocumento iva converter.
	 */
	public ProvvisorioImportoDaRegolarizzareConverter() {
		super(ProvvisorioDiCassa.class, SiacTProvCassa.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public ProvvisorioDiCassa convertFrom(SiacTProvCassa src, ProvvisorioDiCassa dest) {
		
//		BigDecimal importo = BigDecimal.ZERO;
//		if(src.getSiacROrdinativoProvCassas() == null){
//			dest.setImportoDaRegolarizzare(src.getProvcImporto());
//			return dest;
//		}
//		for(SiacROrdinativoProvCassa r : src.getSiacROrdinativoProvCassas()){
//			if(r.getDataCancellazione() == null && r.getDataFineValidita() == null){
//				importo = importo.add(r.getOrdProvcImporto());
//			}
//		}
//		dest.setImportoDaRegolarizzare(src.getProvcImporto().subtract(importo));
		dest.setImportoDaRegolarizzare(provvisorioDiCassaBilDao.calcolaImportoDaRegolarizzare(src.getUid()));
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTProvCassa convertTo(ProvvisorioDiCassa src, SiacTProvCassa dest) {
		return dest;
	}



	

}
