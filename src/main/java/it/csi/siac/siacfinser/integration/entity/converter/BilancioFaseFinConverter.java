/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity.converter;



import java.util.List;

import org.dozer.DozerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;
import it.csi.siac.siacfinser.integration.dao.EnumEntityFinFactory;
import it.csi.siac.siacfinser.integration.entity.SiacRBilFaseOperativaFin;
import it.csi.siac.siacfinser.integration.entity.enumeration.SiacDFaseOperativaFinEnum;

/**
 * Converter per la fase del Bilancio.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 09/01/2014
 *
 */
@Component
public class BilancioFaseFinConverter extends DozerConverter<FaseBilancio, List<SiacRBilFaseOperativaFin>> {
	
	@Autowired
	private EnumEntityFinFactory eef;

	/** Costruttore vuoto */
	@SuppressWarnings("unchecked")
	public BilancioFaseFinConverter() {
		super(FaseBilancio.class, (Class<List<SiacRBilFaseOperativaFin>>)(Class<?>)List.class);
	}

	@Override
	public FaseBilancio convertFrom(List<SiacRBilFaseOperativaFin> src, FaseBilancio dest) {
		for(SiacRBilFaseOperativaFin siacRBilFaseOperativa : src) {
			if(siacRBilFaseOperativa.getDataCancellazione() == null) {
				return SiacDFaseOperativaFinEnum.byCodice(siacRBilFaseOperativa.getSiacDFaseOperativa().getFaseOperativaCode()).getFaseBilancio();
			}
		}
		return null;
	}

	@Override
	public List<SiacRBilFaseOperativaFin> convertTo(FaseBilancio src, List<SiacRBilFaseOperativaFin> dest) {
//		if(dest == null) {
//			dest = new ArrayList<SiacRBilFaseOperativaFin>();
//		}
//		
//		SiacRBilFaseOperativaFin siacRBilFaseOperativa = new SiacRBilFaseOperativaFin();
//		
//		SiacDFaseOperativaFinEnum siacDFaseOperativaEnum = SiacDFaseOperativaFinEnum.byFaseBilancio();
//		SiacDFaseOperativaFin siacDFaseOperativa = eef.getEntity(siacDFaseOperativaEnum, dest.get(0).getSiacTEnteProprietario().getUid(), SiacDFaseOperativaFin.class);
//		
//		siacRBilFaseOperativa.setSiacDFaseOperativa(siacDFaseOperativa);
//		
//		dest.add(siacRBilFaseOperativa);
		return dest;
	}
	
}

