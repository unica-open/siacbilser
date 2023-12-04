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
import it.csi.siac.siacbilser.integration.entity.SiacDCronopStato;
import it.csi.siac.siacbilser.integration.entity.SiacRCronopStato;
import it.csi.siac.siacbilser.integration.entity.SiacTCronop;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCronopStatoEnum;
import it.csi.siac.siacbilser.model.StatoOperativoCronoprogramma;
import it.csi.siac.siaccommonser.util.log.LogSrvUtil;

// TODO: Auto-generated Javadoc
/**
 * The Class CronoprogrammaStatoConverter.
 */
@Component
public class CronoprogrammaStatoConverter extends DozerConverter<StatoOperativoCronoprogramma, SiacTCronop > {
	
	/** The log. */
	private LogSrvUtil log = new LogSrvUtil(this.getClass());
	
	/** The eef. */
	@Autowired
	private EnumEntityFactory eef;

	/**
	 * Instantiates a new cronoprogramma stato converter.
	 */
	public CronoprogrammaStatoConverter() {
		super(StatoOperativoCronoprogramma.class, SiacTCronop.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public StatoOperativoCronoprogramma convertFrom(SiacTCronop src, StatoOperativoCronoprogramma dest) {
		for (SiacRCronopStato siacRCronopStato : src.getSiacRCronopStatos()) {
			if(siacRCronopStato.getDataCancellazione()==null){
				return SiacDCronopStatoEnum.byCodice(siacRCronopStato.getSiacDCronopStato().getCronopStatoCode()).getStatoOperativo();
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTCronop convertTo(StatoOperativoCronoprogramma src, SiacTCronop dest) {
		final String methodName = "convertTo";
		
		if(dest== null) {
			return dest;
		}
		
		List<SiacRCronopStato> siacRCronopStatos = new ArrayList<SiacRCronopStato>();
		SiacRCronopStato siacRCronopStato = new SiacRCronopStato();
	
		SiacDCronopStatoEnum variazioneStato =  SiacDCronopStatoEnum.byStatoOperativo(src);
		SiacDCronopStato siacDCronopStato = eef.getEntity(variazioneStato, dest.getSiacTEnteProprietario().getUid(), SiacDCronopStato.class); 
				
		log .debug(methodName, "setting siacDCronopStato to: "+siacDCronopStato.getCronopStatoCode()+ " ["+siacDCronopStato.getUid()+"]");
		siacRCronopStato.setSiacDCronopStato(siacDCronopStato);
		siacRCronopStato.setSiacTCronop(dest);		
		
		siacRCronopStato.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		siacRCronopStato.setLoginOperazione(dest.getLoginOperazione());
		
		
		siacRCronopStatos.add(siacRCronopStato);
		dest.setSiacRCronopStatos(siacRCronopStatos);
		
		return dest;
	}



	

}
