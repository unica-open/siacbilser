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
import it.csi.siac.siacbilser.integration.entity.SiacDCassaEconOperazStato;
import it.csi.siac.siacbilser.integration.entity.SiacRCassaEconOperazStato;
import it.csi.siac.siacbilser.integration.entity.SiacTCassaEconOperaz;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCassaEconOperazStatoEnum;
import it.csi.siac.siaccecser.model.OperazioneCassa;
import it.csi.siac.siaccecser.model.StatoOperativoOperazioneCassa;

/**
 * The Class DocumentoEntrataSubdocumentoIvaConverter.
 */
@Component
public class OperazioneCassaStatoOperativoConverter extends ExtendedDozerConverter<OperazioneCassa, SiacTCassaEconOperaz > {
	

	@Autowired
	private EnumEntityFactory eef;
	
	/**
	 * Instantiates a new subdocumento Entrata subdocumento iva converter.
	 */
	public OperazioneCassaStatoOperativoConverter() {
		super(OperazioneCassa.class, SiacTCassaEconOperaz.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public OperazioneCassa convertFrom(SiacTCassaEconOperaz src, OperazioneCassa dest) {
		
		if(src.getSiacRCassaEconOperazStatos() == null){
			return dest;
		}
		for(SiacRCassaEconOperazStato siacRCassaEconOperazStato : src.getSiacRCassaEconOperazStatos()){
			if(siacRCassaEconOperazStato.getDataCancellazione() == null){
				SiacDCassaEconOperazStato siacDCassaEconOperazStato = siacRCassaEconOperazStato.getSiacDCassaEconOperazStato();
				SiacDCassaEconOperazStatoEnum siacDCassaEconOperazStatoEnum = SiacDCassaEconOperazStatoEnum.byCodice(siacDCassaEconOperazStato.getCassaeconopStatoCode());
				StatoOperativoOperazioneCassa statoOperativoOperazioneCassa = siacDCassaEconOperazStatoEnum.getStatoOperativo();
				dest.setStatoOperativoOperazioneCassa(statoOperativoOperazioneCassa);
				break;
			}
		}
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTCassaEconOperaz convertTo(OperazioneCassa src, SiacTCassaEconOperaz dest) {
		
		if(src.getStatoOperativoOperazioneCassa() == null){
			return dest;
		}
		
		SiacDCassaEconOperazStatoEnum siacDCassaEconOperazStatoEnum = SiacDCassaEconOperazStatoEnum.byCodice(src.getStatoOperativoOperazioneCassa().getCodice());
		SiacDCassaEconOperazStato siacDCassaEconOperazStato = eef.getEntity(siacDCassaEconOperazStatoEnum, dest.getSiacTEnteProprietario().getUid(), SiacDCassaEconOperazStato.class);
		
		List<SiacRCassaEconOperazStato> siacRCassaEconOperazStatos = new ArrayList<SiacRCassaEconOperazStato>();
		SiacRCassaEconOperazStato siacRCassaEconOperazStato = new SiacRCassaEconOperazStato();
		siacRCassaEconOperazStato.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		siacRCassaEconOperazStato.setSiacDCassaEconOperazStato(siacDCassaEconOperazStato);
		siacRCassaEconOperazStato.setSiacTCassaEconOperaz(dest);
		siacRCassaEconOperazStato.setLoginOperazione(dest.getLoginOperazione());
		siacRCassaEconOperazStatos.add(siacRCassaEconOperazStato );
		dest.setSiacRCassaEconOperazStatos(siacRCassaEconOperazStatos);
		return dest;
	}



	

}
