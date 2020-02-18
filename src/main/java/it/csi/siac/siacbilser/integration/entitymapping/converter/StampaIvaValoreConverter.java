/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTIvaStampa;
import it.csi.siac.siacbilser.integration.entity.SiacTIvaStampaValore;
import it.csi.siac.siacfin2ser.model.StampaIva;

// TODO: Auto-generated Javadoc
/**
 * The Class GruppoAttivitaIvaRegistroIvaConverter.
 */
@Component
public class StampaIvaValoreConverter extends ExtendedDozerConverter<StampaIva,SiacTIvaStampa > {
	
	
	/**
	 * Instantiates a new gruppo attivita iva registro iva converter.
	 */
	public StampaIvaValoreConverter() {
		super(StampaIva.class, SiacTIvaStampa.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public StampaIva convertFrom(SiacTIvaStampa src, StampaIva dest) {
		
		BooleanToStringConverter btsc = new BooleanToStringConverter();
		
		if(src.getSiacTIvaStampaValores()!=null){
			for(SiacTIvaStampaValore siacTStampaValore: src.getSiacTIvaStampaValores()){
				if(siacTStampaValore.getDataCancellazione()==null){				
					
					dest.setFlagStampaDefinitivo(btsc.convertFrom(siacTStampaValore.getFlagstampadef()));
					dest.setFlagStampaProvvisorio(btsc.convertFrom(siacTStampaValore.getFlagstampaprovv()));
					dest.setFlagIncassati(btsc.convertFrom(siacTStampaValore.getFlagincassati()));
					dest.setFlagPagati(btsc.convertFrom(siacTStampaValore.getFlagpagati()));
					dest.setUltimaPaginaStampaDefinitiva(siacTStampaValore.getUltimapaginastampadef());
					dest.setUltimaPaginaStampaProvvisoria(siacTStampaValore.getUltimapaginastampaprovv());
					dest.setUltimaDataProtocolloDefinitiva(siacTStampaValore.getUltimadataprotocollodef());
					dest.setUltimaDataProtocolloProvvisoria(siacTStampaValore.getUltimadataprotocolloprovv());
					dest.setUltimoNumProtocolloDefinitivo(siacTStampaValore.getUltimonumprotocollodef() != null
							? Integer.valueOf(siacTStampaValore.getUltimonumprotocollodef())
							: Integer.valueOf(0));
					dest.setUltimoNumProtocolloProvvisorio(siacTStampaValore.getUltimonumprotocolloprovv() != null
							? Integer.valueOf(siacTStampaValore.getUltimonumprotocolloprovv())
							: Integer.valueOf(0));
					
				}
			}
		}
		
        return dest;

	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTIvaStampa convertTo(StampaIva src, SiacTIvaStampa dest) {
		
		BooleanToStringConverter btsc = new BooleanToStringConverter();
		
		List<SiacTIvaStampaValore> siacTIvaStampaValores = new ArrayList<SiacTIvaStampaValore>();
		SiacTIvaStampaValore siacTIvaStampaValore = new SiacTIvaStampaValore();
		
		siacTIvaStampaValore.setFlagincassati(btsc.convertTo(src.getFlagIncassati()));
		siacTIvaStampaValore.setFlagpagati(btsc.convertTo(src.getFlagPagati()));
		siacTIvaStampaValore.setFlagstampadef(btsc.convertTo(src.getFlagStampaDefinitivo()));
		siacTIvaStampaValore.setFlagstampaprovv(btsc.convertTo(src.getFlagStampaProvvisorio()));
		siacTIvaStampaValore.setUltimapaginastampadef(src.getUltimaPaginaStampaDefinitiva());
		siacTIvaStampaValore.setUltimapaginastampaprovv(src.getUltimaPaginaStampaProvvisoria());
		siacTIvaStampaValore.setUltimadataprotocollodef(src.getUltimaDataProtocolloDefinitiva());
		siacTIvaStampaValore.setUltimadataprotocolloprovv(src.getUltimaDataProtocolloProvvisoria());
		siacTIvaStampaValore.setUltimonumprotocollodef(src.getUltimoNumProtocolloDefinitivo()!=null?src.getUltimoNumProtocolloDefinitivo().toString():null);
		siacTIvaStampaValore.setUltimonumprotocolloprovv(src.getUltimoNumProtocolloProvvisorio()!=null?src.getUltimoNumProtocolloProvvisorio().toString():null);
		siacTIvaStampaValore.setSiacTIvaStampa(dest);
		siacTIvaStampaValore.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		siacTIvaStampaValore.setLoginOperazione(dest.getLoginOperazione());

		
		siacTIvaStampaValores.add(siacTIvaStampaValore);
		dest.setSiacTIvaStampaValores(siacTIvaStampaValores);
		return dest;
	}



	

}
