/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacDPrimaNotaStato;
import it.csi.siac.siacbilser.integration.entity.SiacRPrimaNotaStato;
import it.csi.siac.siacbilser.integration.entity.SiacTPrimaNota;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDPrimaNotaStatoEnum;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;

/**
 * The Class DocumentoEntrataSubdocumentoIvaConverter.
 */
@Component
public class PrimaNotaStatoOperativoConverter extends ExtendedDozerConverter<PrimaNota, SiacTPrimaNota> {
	

	@Autowired
	private EnumEntityFactory eef;
	
	/**
	 * Instantiates a new subdocumento Entrata subdocumento iva converter.
	 */
	public PrimaNotaStatoOperativoConverter() {
		super(PrimaNota.class, SiacTPrimaNota.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public PrimaNota convertFrom(SiacTPrimaNota src, PrimaNota dest) {
		
		if(src.getSiacRPrimaNotaStatos() == null){
			return dest;
		}
		for(SiacRPrimaNotaStato siacRPrimaNotaStato: src.getSiacRPrimaNotaStatos()){
			if(siacRPrimaNotaStato.getDataCancellazione() == null){
				SiacDPrimaNotaStatoEnum siacDPrimaNotaStatoEnum = SiacDPrimaNotaStatoEnum.byCodice(siacRPrimaNotaStato.getSiacDPrimaNotaStato().getPnotaStatoCode());
				StatoOperativoPrimaNota statoOperativoPrimaNota = siacDPrimaNotaStatoEnum.getStatoOperativo();
				dest.setStatoOperativoPrimaNota(statoOperativoPrimaNota);
				break;
			}
		}
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTPrimaNota convertTo(PrimaNota src, SiacTPrimaNota dest) {
		
		if(src.getStatoOperativoPrimaNota() == null){
			return dest;
		}
		
		SiacDPrimaNotaStatoEnum siacDPrimaNotaStatoEnum = SiacDPrimaNotaStatoEnum.byStatoOperativo(src.getStatoOperativoPrimaNota());
		SiacDPrimaNotaStato siacDPrimaNotaStato = eef.getEntity(siacDPrimaNotaStatoEnum, dest.getSiacTEnteProprietario().getUid(), SiacDPrimaNotaStato.class);
		
		List<SiacRPrimaNotaStato> siacRPrimaNotaStatos = new ArrayList<SiacRPrimaNotaStato>();
		SiacRPrimaNotaStato siacRPrimaNotaStato = new SiacRPrimaNotaStato();
		
		siacRPrimaNotaStato.setSiacDPrimaNotaStato(siacDPrimaNotaStato);
		siacRPrimaNotaStato.setSiacTPrimaNota(dest);
		siacRPrimaNotaStato.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		siacRPrimaNotaStato.setLoginOperazione(dest.getLoginOperazione());
		
		siacRPrimaNotaStatos.add(siacRPrimaNotaStato);
		dest.setSiacRPrimaNotaStatos(siacRPrimaNotaStatos);
		return dest;
	}



	

}
