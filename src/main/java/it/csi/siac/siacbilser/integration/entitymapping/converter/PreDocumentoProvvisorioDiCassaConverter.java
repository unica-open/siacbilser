/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilser.integration.entity.SiacRPredocProvCassa;
import it.csi.siac.siacbilser.integration.entity.SiacTPredoc;
import it.csi.siac.siacbilser.integration.entity.SiacTProvCassa;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfin2ser.model.PreDocumento;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;

/**
 * The Class PreDocumentoSoggConverter.
 */
public class PreDocumentoProvvisorioDiCassaConverter extends ExtendedDozerConverter<PreDocumento<?, ?>, SiacTPredoc> {

	/**
	 * Instantiates a new pre documento sogg converter.
	 */
	@SuppressWarnings("unchecked")
	public PreDocumentoProvvisorioDiCassaConverter() {
		super((Class<PreDocumento<?, ?>>)(Class<?>)PreDocumento.class, SiacTPredoc.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	public PreDocumento<?, ?> convertFrom(SiacTPredoc src, PreDocumento<?, ?> dest) {

		if (src.getSiacRPredocProvCassas() != null) {
			for (SiacRPredocProvCassa siacRPredocProvCassa : src.getSiacRPredocProvCassas()) {
				if (siacRPredocProvCassa.getDataCancellazione() == null) {
					ProvvisorioDiCassa provvisorio = map(siacRPredocProvCassa.getSiacTProvCassa(), ProvvisorioDiCassa.class, BilMapId.SiacTProvCassa_ProvvisorioDiCassa);
					dest.setProvvisorioDiCassa(provvisorio);
				}
			}
		}
		return dest;
	}
	

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTPredoc convertTo(PreDocumento<?, ?> src, SiacTPredoc dest) {
		List<SiacRPredocProvCassa> siacRPredocProvCassas = new ArrayList<SiacRPredocProvCassa>();
		
		if(src.getProvvisorioDiCassa() != null && src.getProvvisorioDiCassa().getUid() != 0){
			SiacTProvCassa siacTProvCassa = new SiacTProvCassa();
			siacTProvCassa.setUid(src.getProvvisorioDiCassa().getUid());
			SiacRPredocProvCassa siacRPredocProvCassa = new SiacRPredocProvCassa();
			siacRPredocProvCassa.setSiacTProvCassa(siacTProvCassa);
			siacRPredocProvCassa.setSiacTPredoc(dest);
			siacRPredocProvCassa.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
			siacRPredocProvCassa.setLoginOperazione(dest.getLoginOperazione());
			siacRPredocProvCassas.add(siacRPredocProvCassa);
		}
		dest.setSiacRPredocProvCassas(siacRPredocProvCassas);
		return dest;
	}

}
