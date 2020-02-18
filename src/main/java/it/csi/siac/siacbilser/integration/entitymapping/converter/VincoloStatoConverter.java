/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacDVincoloStato;
import it.csi.siac.siacbilser.integration.entity.SiacRVincoloStato;
import it.csi.siac.siacbilser.integration.entity.SiacTVincolo;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDVincoloStatoEnum;
import it.csi.siac.siacbilser.model.StatoOperativo;
import it.csi.siac.siaccommon.util.log.LogUtil;

// TODO: Auto-generated Javadoc
/**
 * The Class VincoloStatoConverter.
 */
@Component
public class VincoloStatoConverter extends DozerConverter<StatoOperativo, SiacTVincolo > {
	
	/** The log. */
	private LogUtil log = new LogUtil(this.getClass());
	
	/** The eef. */
	@Autowired
	private EnumEntityFactory eef;

	/**
	 * Instantiates a new vincolo stato converter.
	 */
	public VincoloStatoConverter() {
		super(StatoOperativo.class, SiacTVincolo.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public StatoOperativo convertFrom(SiacTVincolo src, StatoOperativo dest) {
		for (SiacRVincoloStato siacRVincoloStato : src.getSiacRVincoloStatos()) {
			if(siacRVincoloStato.getDataCancellazione()==null){
				//return new VariazioneStatoConverter().convertFrom(siacRVariazioneStato.getSiacDVariazioneStato(),null);
				return SiacDVincoloStatoEnum.byCodice(siacRVincoloStato.getSiacDVincoloStato().getVincoloStatoCode()).getStatoOperativo();
			}			
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTVincolo convertTo(StatoOperativo src, SiacTVincolo dest) {
		final String methodName = "convertTo";
		
		if(dest== null) {
			return dest;
		}
		
		List<SiacRVincoloStato> siacRVincoloStatos = new ArrayList<SiacRVincoloStato>();
		SiacRVincoloStato siacRVincoloStato = new SiacRVincoloStato();
	
		//SiacDVariazioneStato siacDVariazioneStato = new VariazioneStatoConverter().convertTo(src,null);
		SiacDVincoloStatoEnum variazioneStato =  SiacDVincoloStatoEnum.byStatoOperativo(src);
		SiacDVincoloStato siacDVincoloStato = eef.getEntity(variazioneStato, dest.getSiacTEnteProprietario().getUid(), SiacDVincoloStato.class); 
		
		
		log .debug(methodName, "setting siacDVincoloStato to: "+siacDVincoloStato.getVincoloStatoCode()+ " ["+siacDVincoloStato.getUid()+"]");
		siacRVincoloStato.setSiacDVincoloStato(siacDVincoloStato);
		siacRVincoloStato.setSiacTVincolo(dest);		
		
		siacRVincoloStato.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		siacRVincoloStato.setLoginOperazione(dest.getLoginOperazione());
		
		
		siacRVincoloStatos.add(siacRVincoloStato);
		dest.setSiacRVincoloStatos(siacRVincoloStatos);//RVariazioneStatos(siacRVincoloStatos);
		
		return dest;
	}



	

}
