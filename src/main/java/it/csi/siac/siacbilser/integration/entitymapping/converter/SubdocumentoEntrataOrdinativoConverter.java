/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacTOrdinativoTBilRepository;
import it.csi.siac.siacbilser.integration.entity.SiacRSubdocOrdinativoT;
import it.csi.siac.siacbilser.integration.entity.SiacTOrdinativoT;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo.StatoOperativoOrdinativo;

/**
 * The Class SubdocumentoEntrataOrdinativoConverter.
 */
@Component
public class SubdocumentoEntrataOrdinativoConverter extends ExtendedDozerConverter<SubdocumentoEntrata, SiacTSubdoc > {

	@Autowired
	private SiacTOrdinativoTBilRepository siacTOrdinativoTBilRepository;
	
	/**
	 * Instantiates a new subdocumento spesa subdocumento iva converter.
	 */
	public SubdocumentoEntrataOrdinativoConverter() {
		super(SubdocumentoEntrata.class, SiacTSubdoc.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SubdocumentoEntrata convertFrom(SiacTSubdoc src, SubdocumentoEntrata dest) {
		final String methodName = "convertFrom";
		
		if(src.getSiacRSubdocOrdinativoTs()!=null){
			for (SiacRSubdocOrdinativoT  siacRSubdocOrdinativoT : src.getSiacRSubdocOrdinativoTs()) {
				if(siacRSubdocOrdinativoT.getDataCancellazione() == null){
					
					SiacTOrdinativoT siacTOrdinativoT = siacTOrdinativoTBilRepository.findOne(siacRSubdocOrdinativoT.getSiacTOrdinativoT().getUid());
					
					Ordinativo ordinativo = mapNotNull(siacTOrdinativoT.getSiacTOrdinativo(), Ordinativo.class,
							BilMapId.SiacTOrdinativo_Ordinativo);
					
					if(!StatoOperativoOrdinativo.ANNULLATO.equals(ordinativo.getStatoOperativoOrdinativo())){
						dest.setOrdinativo(ordinativo);
					} else {
						log.debug(methodName, "ordinativo saltato perche' in stato "+StatoOperativoOrdinativo.ANNULLATO);
					}
					
				}
			}
		}
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTSubdoc convertTo(SubdocumentoEntrata src, SiacTSubdoc dest) {
		
//		if(src.getOrdinativo() == null || src.getOrdinativo().getUid() == 0){
//			return dest;
//		}
//		
//		SiacRSubdocOrdinativoT siacRSubdocOrdinativoT = new SiacRSubdocOrdinativoT();
//		
//		SiacTOrdinativoT siacTOrdinativoT = new SiacTOrdinativoT();
//		siacTOrdinativoT.setUid(src.getOrdinativo().getUid());
//		siacRSubdocOrdinativoT.setSiacTOrdinativoT(siacTOrdinativoT);
//		
//		siacRSubdocOrdinativoT.setSiacTSubdoc(dest);
//		siacRSubdocOrdinativoT.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
//		siacRSubdocOrdinativoT.setLoginOperazione(dest.getLoginOperazione());
//		
//		dest.setSiacRSubdocOrdinativoTs(new ArrayList<SiacRSubdocOrdinativoT>());
//		dest.addSiacRSubdocOrdinativoT(siacRSubdocOrdinativoT);
		
		return dest;
	
	}



	

}
