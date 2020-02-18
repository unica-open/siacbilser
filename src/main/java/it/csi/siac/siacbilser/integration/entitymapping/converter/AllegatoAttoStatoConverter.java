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
import it.csi.siac.siacbilser.integration.entity.SiacDAttoAllegatoStato;
import it.csi.siac.siacbilser.integration.entity.SiacRAttoAllegatoStato;
import it.csi.siac.siacbilser.integration.entity.SiacTAttoAllegato;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDAttoAllegatoStatoEnum;
import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siacfin2ser.model.StatoOperativoAllegatoAtto;

// TODO: Auto-generated Javadoc
/**
 * Converter per lo StatoOperativoAllegatoAtto
 *  
 *
 */
@Component
public class AllegatoAttoStatoConverter extends DozerConverter<StatoOperativoAllegatoAtto, SiacTAttoAllegato> {
	
	/** The log. */
	private LogUtil log = new LogUtil(this.getClass());
	
	/** The eef. */
	@Autowired
	private EnumEntityFactory eef;

	/**
	 *  Costruttore vuoto di default.
	 */
	public AllegatoAttoStatoConverter() {
		super(StatoOperativoAllegatoAtto.class, SiacTAttoAllegato.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public StatoOperativoAllegatoAtto convertFrom(SiacTAttoAllegato src, StatoOperativoAllegatoAtto dest) {
		for(SiacRAttoAllegatoStato siacRAttoAllegatoStato : src.getSiacRAttoAllegatoStatos()) {
			if(siacRAttoAllegatoStato.getDataCancellazione() == null) {
				return SiacDAttoAllegatoStatoEnum.byCodice(siacRAttoAllegatoStato.getSiacDAttoAllegatoStato().getAttoalStatoCode()).getStatoOperativoAllegatoAtto();
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTAttoAllegato convertTo(StatoOperativoAllegatoAtto src, SiacTAttoAllegato dest) {
		final String methodName = "convertTo";
		
		if(dest == null) {
			return dest;
		}
		
		List<SiacRAttoAllegatoStato> siacRAttoAllegatoStatos = new ArrayList<SiacRAttoAllegatoStato>();
		SiacRAttoAllegatoStato siacRAttoAllegatoStato = new SiacRAttoAllegatoStato();
		
		SiacDAttoAllegatoStatoEnum stato = SiacDAttoAllegatoStatoEnum.byStatoOperativo(src);
		SiacDAttoAllegatoStato siacDAttoAllegatoStato = eef.getEntity(stato, dest.getSiacTEnteProprietario().getUid(), SiacDAttoAllegatoStato.class);
		
		log.debug(methodName, "Setting siacDAttoAllegato to: " + siacDAttoAllegatoStato.getAttoalStatoCode() + " [" + siacDAttoAllegatoStato.getUid() + "]");
		
		siacRAttoAllegatoStato.setSiacDAttoAllegatoStato(siacDAttoAllegatoStato);
		siacRAttoAllegatoStato.setSiacTAttoAllegato(dest);
		siacRAttoAllegatoStato.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		siacRAttoAllegatoStato.setLoginOperazione(dest.getLoginOperazione());
		
		siacRAttoAllegatoStatos.add(siacRAttoAllegatoStato);
		dest.setSiacRAttoAllegatoStatos(siacRAttoAllegatoStatos);
		
		return dest;
	}

}
