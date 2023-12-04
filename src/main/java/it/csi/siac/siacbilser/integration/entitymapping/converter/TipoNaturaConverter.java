/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.dozer.DozerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SirfelDNaturaRepository;
import it.csi.siac.siacbilser.integration.entity.SirfelDNatura;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siacfin2ser.model.TipoNatura;

// TODO: Auto-generated Javadoc
/**
 * Converter per il tipo di operazione Iva.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 27/05/2014
 *
 */
@Component
public class TipoNaturaConverter extends DozerConverter<TipoNatura, SirfelDNatura> {
	
	/** The eef. */
	@Autowired
	private SirfelDNaturaRepository sirfelDNaturaRepository;

	/**
	 *  Costruttore vuoto.
	 */
	public TipoNaturaConverter() {
		super(TipoNatura.class, SirfelDNatura.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public TipoNatura convertFrom(SirfelDNatura src, TipoNatura dest) {
				
		if(src!= null){
			if(dest== null){
				dest = new TipoNatura();
			}
			
			dest.setCodice(src.getCodice());
			dest.setDescrizione(src.getDescrizione());
			Ente ente = new Ente();
			if(src.getEnteProprietarioId()!= null){
				ente.setUid(src.getEnteProprietarioId().intValue());
			}
			dest.setEnte(ente);
		}
		
		
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SirfelDNatura convertTo(TipoNatura src, SirfelDNatura dest) {
		
		if(src!= null && src.getCodice() != null && src.getEnte()!= null){
			dest = sirfelDNaturaRepository.findByEnteECodice(src.getEnte().getUid(), src.getCodice());
		}
		
		return dest;
		
	}
	
}
