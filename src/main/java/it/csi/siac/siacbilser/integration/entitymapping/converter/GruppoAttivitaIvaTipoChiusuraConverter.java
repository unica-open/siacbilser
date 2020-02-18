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
import it.csi.siac.siacbilser.integration.entity.SiacDIvaChiusuraTipo;
import it.csi.siac.siacbilser.integration.entity.SiacRIvaGruppoChiusura;
import it.csi.siac.siacbilser.integration.entity.SiacTIvaGruppo;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDIvaChiusuraTipoEnum;
import it.csi.siac.siacfin2ser.model.GruppoAttivitaIva;
import it.csi.siac.siacfin2ser.model.TipoChiusura;



/**
 * Converter per il tipo di chiusura Iva.
 */
@Component
public class GruppoAttivitaIvaTipoChiusuraConverter extends DozerConverter<GruppoAttivitaIva, SiacTIvaGruppo> {
	
	/** The eef. */
	@Autowired
	private EnumEntityFactory eef;

	/**
	 *  Costruttore vuoto.
	 */
	public GruppoAttivitaIvaTipoChiusuraConverter() {
		super(GruppoAttivitaIva.class, SiacTIvaGruppo.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public GruppoAttivitaIva convertFrom(SiacTIvaGruppo src, GruppoAttivitaIva dest) {
		// Caso base: se non ho legata l'entity, restituisco il null sull'enum
		if(src.getSiacRIvaGruppoChiusuras()==null) {
			return dest;
		}
			
		for(SiacRIvaGruppoChiusura siacRIvaGruppoChiusura:src.getSiacRIvaGruppoChiusuras()){
			if(siacRIvaGruppoChiusura.getDataCancellazione() == null && (
						dest.getAnnualizzazione() == null || dest.getAnnualizzazione().equals(siacRIvaGruppoChiusura.getIvagruchitipoAnno())
					)){
				TipoChiusura tc = TipoChiusura.fromCodice(siacRIvaGruppoChiusura.getSiacDIvaChiusuraTipo().getIvachiTipoCode());
				dest.setTipoChiusura(tc);
				break;
			}
		}
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTIvaGruppo convertTo( GruppoAttivitaIva src, SiacTIvaGruppo dest) {
		if(src == null || dest == null) {
			return dest;
		}
		
		SiacDIvaChiusuraTipoEnum siacDIvaChiusuraTipoEnum = SiacDIvaChiusuraTipoEnum.byTipoChiusuraEvenNull(src.getTipoChiusura());
		if(siacDIvaChiusuraTipoEnum == null) {
			return dest;
		}
		List<SiacRIvaGruppoChiusura> siacRIvaGruppoChiusuras= new ArrayList<SiacRIvaGruppoChiusura>();
		SiacRIvaGruppoChiusura siacRIvaGruppoChiusura= new SiacRIvaGruppoChiusura();
		
		SiacDIvaChiusuraTipo siacDIvaChiusuraTipo = eef.getEntity(siacDIvaChiusuraTipoEnum, dest.getSiacTEnteProprietario().getEnteProprietarioId(),
				SiacDIvaChiusuraTipo.class);
		
		siacRIvaGruppoChiusura.setSiacDIvaChiusuraTipo(siacDIvaChiusuraTipo);
		siacRIvaGruppoChiusura.setSiacTIvaGruppo(dest);
		siacRIvaGruppoChiusura.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		siacRIvaGruppoChiusura.setLoginOperazione(dest.getLoginOperazione());
		
		if(src.getAnnualizzazione() != null) {
			siacRIvaGruppoChiusura.setIvagruchitipoAnno(src.getAnnualizzazione());
		}
		
		siacRIvaGruppoChiusuras.add(siacRIvaGruppoChiusura);
		dest.setSiacRIvaGruppoChiusuras(siacRIvaGruppoChiusuras);
		
		return dest;
		
	}
	
}
