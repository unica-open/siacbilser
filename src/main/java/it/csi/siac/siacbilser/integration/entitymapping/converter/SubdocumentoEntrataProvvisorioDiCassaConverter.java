/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRSubdocProvCassa;
import it.csi.siac.siacbilser.integration.entity.SiacTProvCassa;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;

@Component
public class SubdocumentoEntrataProvvisorioDiCassaConverter extends ExtendedDozerConverter<SubdocumentoEntrata, SiacTSubdoc > {

	/**
	 * Instantiates a new subdocumento spesa subdocumento iva converter.
	 */
	public SubdocumentoEntrataProvvisorioDiCassaConverter() {
		super(SubdocumentoEntrata.class, SiacTSubdoc.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SubdocumentoEntrata convertFrom(SiacTSubdoc src, SubdocumentoEntrata dest) {
		
		if(src.getSiacRSubdocProvCassas()!=null){
			for (SiacRSubdocProvCassa  siacRSubdocProvCassa :src.getSiacRSubdocProvCassas()) {
				if(siacRSubdocProvCassa.getDataCancellazione() == null){
					ProvvisorioDiCassa provvisorio = map(siacRSubdocProvCassa.getSiacTProvCassa(), ProvvisorioDiCassa.class,
							BilMapId.SiacTProvCassa_ProvvisorioDiCassa);
					dest.setProvvisorioCassa(provvisorio);
					break;
				}
			}
			dest.setFlagACopertura(dest.getProvvisorioCassa() != null && dest.getProvvisorioCassa().getUid() != 0);
		}
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTSubdoc convertTo(SubdocumentoEntrata src, SiacTSubdoc dest) {
		List<SiacRSubdocProvCassa> siacRSubdocProvCassas = new ArrayList<SiacRSubdocProvCassa>();
		
		if(src.getProvvisorioCassa() != null){
			SiacTProvCassa siacTProvCassa = new SiacTProvCassa();
			siacTProvCassa.setUid(src.getProvvisorioCassa().getUid());
			SiacRSubdocProvCassa siacRSubdocProvCassa = new SiacRSubdocProvCassa();
			siacRSubdocProvCassa.setSiacTProvCassa(siacTProvCassa);
			siacRSubdocProvCassa.setSiacTSubdoc(dest);
			siacRSubdocProvCassa.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
			siacRSubdocProvCassa.setLoginOperazione(dest.getLoginOperazione());
			siacRSubdocProvCassas.add(siacRSubdocProvCassa);
		}
		dest.setSiacRSubdocProvCassas(siacRSubdocProvCassas);
		return dest;
	}



	

}
