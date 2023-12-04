/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.List;

import org.dozer.DozerConverter;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRBilFaseOperativa;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDFaseOperativaEnum;
import it.csi.siac.siaccorser.model.FaseBilancio;

/**
 * Converter per la fase del Bilancio.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 09/01/2014
 *
 */
@Component
public class BilancioFaseConverter extends DozerConverter<FaseBilancio, List<SiacRBilFaseOperativa>> {

	/**
	 *  Costruttore vuoto.
	 */
	@SuppressWarnings("unchecked")
	public BilancioFaseConverter() {
		super(FaseBilancio.class, (Class<List<SiacRBilFaseOperativa>>)(Class<?>)List.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public FaseBilancio convertFrom(List<SiacRBilFaseOperativa> src, FaseBilancio dest) {
		if(src!=null){
			for(SiacRBilFaseOperativa siacRBilFaseOperativa : src) {
				if(siacRBilFaseOperativa.getDataCancellazione() == null) {
					return SiacDFaseOperativaEnum.byCodice(siacRBilFaseOperativa.getSiacDFaseOperativa().getFaseOperativaCode()).getFaseBilancio();
				}
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public List<SiacRBilFaseOperativa> convertTo(FaseBilancio src, List<SiacRBilFaseOperativa> dest) {
		/*if(dest == null) {
			dest = new ArrayList<SiacRBilFaseOperativa>();
		}
		
		SiacRBilFaseOperativa siacRBilFaseOperativa = new SiacRBilFaseOperativa();
		
		SiacDFaseOperativaEnum siacDFaseOperativaEnum = SiacDFaseOperativaEnum.byFaseBilancio();
		SiacDFaseOperativa siacDFaseOperativa = eef.getEntity(siacDFaseOperativaEnum, dest.get(0).getSiacTEnteProprietario().getUid(), SiacDFaseOperativa.class);
		
		siacRBilFaseOperativa.setSiacDFaseOperativa(siacDFaseOperativa);
		
		dest.add(siacRBilFaseOperativa);*/
		return dest;
	}
	
}
