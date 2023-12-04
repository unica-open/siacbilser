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
import it.csi.siac.siacbilser.integration.entity.SiacDProgrammaStato;
import it.csi.siac.siacbilser.integration.entity.SiacRProgrammaStato;
import it.csi.siac.siacbilser.integration.entity.SiacTProgramma;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDProgrammaStatoEnum;
import it.csi.siac.siacbilser.model.StatoOperativoProgetto;
import it.csi.siac.siaccommonser.util.log.LogSrvUtil;

// TODO: Auto-generated Javadoc
/**
 * Converter per lo StatoOperativoProgetto tra Progetto e SiacTProgramma.
 *  
 * @author Marchino Alessandro
 * @version 1.0.0 - 05/02/2014
 *
 */
@Component
public class ProgettoStatoConverter extends DozerConverter<StatoOperativoProgetto, SiacTProgramma> {
	
	/** The log. */
	private LogSrvUtil log = new LogSrvUtil(this.getClass());
	
	/** The eef. */
	@Autowired
	private EnumEntityFactory eef;

	/**
	 *  Costruttore vuoto di default.
	 */
	public ProgettoStatoConverter() {
		super(StatoOperativoProgetto.class, SiacTProgramma.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public StatoOperativoProgetto convertFrom(SiacTProgramma src, StatoOperativoProgetto dest) {
		for(SiacRProgrammaStato siacRProgrammaStato : src.getSiacRProgrammaStatos()) {
			if(siacRProgrammaStato.getDataCancellazione() == null) {
				return SiacDProgrammaStatoEnum.byCodice(siacRProgrammaStato.getSiacDProgrammaStato().getProgrammaStatoCode()).getStatoOperativoProgetto();
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTProgramma convertTo(StatoOperativoProgetto src, SiacTProgramma dest) {
		final String methodName = "convertTo";
		
		if(dest == null) {
			return dest;
		}
		
		List<SiacRProgrammaStato> siacRProgrammaStatos = new ArrayList<SiacRProgrammaStato>();
		SiacRProgrammaStato siacRProgrammaStato = new SiacRProgrammaStato();
		
		SiacDProgrammaStatoEnum programmaStato = SiacDProgrammaStatoEnum.byStatoOperativo(src);
		SiacDProgrammaStato siacDProgrammaStato = eef.getEntity(programmaStato, dest.getSiacTEnteProprietario().getUid(), SiacDProgrammaStato.class);
		
		log.debug(methodName, "Setting siacDProgrammaStato to: " + siacDProgrammaStato.getProgrammaStatoCode() + " [" + siacDProgrammaStato.getUid() + "]");
		
		siacRProgrammaStato.setSiacDProgrammaStato(siacDProgrammaStato);
		siacRProgrammaStato.setSiacTProgramma(dest);
		siacRProgrammaStato.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		siacRProgrammaStato.setLoginOperazione(dest.getLoginOperazione());
		
		siacRProgrammaStatos.add(siacRProgrammaStato);
		dest.setSiacRProgrammaStatos(siacRProgrammaStatos);
		
		return dest;
	}

}
