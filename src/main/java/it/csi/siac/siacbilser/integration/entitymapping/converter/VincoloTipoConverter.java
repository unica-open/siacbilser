/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.dozer.DozerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacDVincoloTipo;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDVincoloTipoEnum;
import it.csi.siac.siacbilser.model.TipoVincoloCapitoli;
import it.csi.siac.siaccommonser.util.log.LogSrvUtil;

// TODO: Auto-generated Javadoc
/**
 * Converte da TipoVincoloCapitoli a SiacDVincoloTipo e viceversa.
 * 
 * @author Domenico
 *
 */
@Component
public class VincoloTipoConverter extends DozerConverter<TipoVincoloCapitoli, SiacDVincoloTipo> {
	
	/** The log. */
	private LogSrvUtil log = new LogSrvUtil(this.getClass());
	
	/** The eef. */
	@Autowired
	private EnumEntityFactory eef;

	/**
	 * Instantiates a new vincolo tipo converter.
	 */
	public VincoloTipoConverter() {
		super(TipoVincoloCapitoli.class, SiacDVincoloTipo.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public TipoVincoloCapitoli convertFrom(SiacDVincoloTipo src, TipoVincoloCapitoli dest) {
		final String methodName = "convertFrom";
		try{
			return SiacDVincoloTipoEnum.byCodice(src.getVincoloTipoCode()).getTipoVincoloCapitoli();
		} catch (RuntimeException re){			
			log.debug(methodName, "Cannot map "+src+" to TipoVincoloCapitoli. Returning null. " + re.getMessage());
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacDVincoloTipo convertTo(TipoVincoloCapitoli src, SiacDVincoloTipo dest) {
		final String methodName = "convertTo";
		try{
			//Occhio prevede che prima di questo converter sia già stato settato dest.getSiacTEnteProprietario().getUid()!!!
			return eef.getEntity(SiacDVincoloTipoEnum.byTipoVincoloCapitoli(src), dest.getSiacTEnteProprietario().getUid(), SiacDVincoloTipo.class);
		} catch (RuntimeException re){			
			log.debug(methodName, "Cannot map "+src+" to SiacDVincoloTipo. Returning null. " + re.getMessage());
			return null;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//
//	public VariazioniTipoConverter() {
//		super(StatoOperativoVariazioneBilancio.class, SiacTVariazione.class);
//	}
//
//	@Override
//	public StatoOperativoVariazioneBilancio convertFrom(SiacTVariazione src, StatoOperativoVariazioneBilancio dest) {
//		for (SiacRVariazioneStato siacRVariazioneStato : src.getSiacRVariazioneStatos()) {
//			if(siacRVariazioneStato.getDataCancellazione()==null){
//				return new VariazioneStatoConverter().convertFrom(siacRVariazioneStato.getSiacDVariazioneStato(),null);
//			}
//			
//		}
//		return null;
//	}
//
//	@Override
//	public SiacTVariazione convertTo(StatoOperativoVariazioneBilancio src, SiacTVariazione dest) {
//		final String methodName = "convertTo";
//		
//		if(dest== null) {
//			dest = new SiacTVariazione().getsiac;
//		}
//		
//		List<SiacRVariazioneStato> siacRVariazioneStatos = new ArrayList<SiacRVariazioneStato>();
//		SiacRVariazioneStato siacRVariazioneStato = new SiacRVariazioneStato();
//				
//		SiacDVariazioneStato siacDVariazioneStato = new VariazioneStatoConverter().convertTo(src,null);
//		log .debug(methodName, "setting siacDVariazioneStato to: "+siacDVariazioneStato.getVariazioneStatoTipoCode()+ " ["+siacDVariazioneStato.getUid()+"]");
//		siacRVariazioneStato.setSiacDVariazioneStato(siacDVariazioneStato);
//		siacRVariazioneStato.setSiacTVariazione(dest);		
//		
//		siacRVariazioneStato.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
//		siacRVariazioneStato.setLoginOperazione(dest.getLoginOperazione());
//		
//		
//		siacRVariazioneStatos.add(siacRVariazioneStato);
//		dest.setSiacRVariazioneStatos(siacRVariazioneStatos);
//		
//		return dest;
//	}



	

}
