/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacDCausaleTipoRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDCausale;
import it.csi.siac.siacbilser.integration.entity.SiacDCausaleTipo;
import it.csi.siac.siacbilser.integration.entity.SiacRCausaleTipo;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfin2ser.model.Causale;
import it.csi.siac.siacfin2ser.model.TipoCausale;

/**
 * The Class CausaleTipoConverter.
 */
@Component
public class CausaleTipoConverter extends ExtendedDozerConverter<Causale, SiacDCausale > {
	
	@Autowired
	private SiacDCausaleTipoRepository siacDCausaleTipoRepository;
	
	/**
	 * Instantiates a new causale tipo converter.
	 */
	public CausaleTipoConverter() {
		super(Causale.class, SiacDCausale.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public Causale convertFrom(SiacDCausale src, Causale dest) {
		if(src.getSiacRCausaleTipos()!=null){
			for (SiacRCausaleTipo siacRCausaleTipo : src.getSiacRCausaleTipos()) {
				if((src.getDateToExtract() == null && siacRCausaleTipo.getDataCancellazione()!=null)
						|| (src.getDateToExtract() != null && !src.getDateToExtract().equals(siacRCausaleTipo.getDataInizioValidita()))){
					continue;
				}
					
				SiacDCausaleTipo siacDCausaleTipo = siacDCausaleTipoRepository.findOne(siacRCausaleTipo.getSiacDCausaleTipo().getUid());
				TipoCausale tipoCausale = new TipoCausale();
				
				map(siacDCausaleTipo, tipoCausale, BilMapId.SiacDCausaleTipo_TipoCausale);
				dest.setTipoCausale(tipoCausale);
				return dest;
					
			}
		}
			
		
		return dest;
		
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacDCausale convertTo(Causale src, SiacDCausale dest) {
		
		if(dest== null || src.getTipoCausale() == null || src.getTipoCausale().getUid() == 0) {
			return dest;
		}
		
		List<SiacRCausaleTipo> siacRCausaleTipos = new ArrayList<SiacRCausaleTipo>();
		SiacRCausaleTipo siacRCausaleTipo = new SiacRCausaleTipo();	
	
		SiacDCausaleTipo siacDCausaleTipo = new SiacDCausaleTipo();		
		siacDCausaleTipo.setUid(src.getTipoCausale().getUid());
				
		siacRCausaleTipo.setSiacDCausaleTipo(siacDCausaleTipo);
		siacRCausaleTipo.setSiacDCausale(dest);		
		
		siacRCausaleTipo.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		siacRCausaleTipo.setLoginOperazione(dest.getLoginOperazione());
		
		
		siacRCausaleTipos.add(siacRCausaleTipo);
		dest.setSiacRCausaleTipos(siacRCausaleTipos);
		
		return dest;
	}



	

}
