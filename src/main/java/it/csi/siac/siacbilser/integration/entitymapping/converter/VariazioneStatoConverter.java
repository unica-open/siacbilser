/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.dozer.DozerConverter;

import it.csi.siac.siacbilser.integration.entity.SiacDVariazioneStato;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDVariazioneStatoEnum;
import it.csi.siac.siacbilser.model.StatoOperativoVariazioneBilancio;

// TODO: Auto-generated Javadoc
/**
 * @deprecated Non più usato. Da eliminare. Utilizzare Variazion[i]StatoConverter!
 *
 */
@Deprecated
public class VariazioneStatoConverter extends DozerConverter<StatoOperativoVariazioneBilancio, SiacDVariazioneStato>  {
	
//	@Autowired
//	private EnumEntityFactory eef;

	/**
 * Instantiates a new variazione stato converter.
 */
public VariazioneStatoConverter() {
		super(StatoOperativoVariazioneBilancio.class, SiacDVariazioneStato.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public StatoOperativoVariazioneBilancio convertFrom(SiacDVariazioneStato src, StatoOperativoVariazioneBilancio dest) {
		return SiacDVariazioneStatoEnum.byCodice(src.getVariazioneStatoTipoCode()).getStatoOperativoVariazioneDiBilancio();
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacDVariazioneStato convertTo(StatoOperativoVariazioneBilancio src, SiacDVariazioneStato dest) {
//		SiacDVariazioneStatoEnum e =  SiacDVariazioneStatoEnum.byStatoOperativoVariazioneDiBilancio(src);
//		return eef.getEntity(e, 1, SiacDVariazioneStato.class); //TODO Occhio! ente 1 schiantato! Per ora questo converter NON viene usato
		
		//return SiacDVariazioneStatoEnum.byStatoOperativoVariazioneDiBilancio(src).getEntity();
		
		return null;
		
	}


	

}
