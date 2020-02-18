/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacTCassaEconRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDCassaEconOperazTipo;
import it.csi.siac.siacbilser.integration.entity.SiacRCassaEconOperazTipoCassa;
import it.csi.siac.siacbilser.integration.entity.SiacTCassaEcon;
import it.csi.siac.siacbilser.integration.entitymapping.CecMapId;
import it.csi.siac.siaccecser.model.CassaEconomale;
import it.csi.siac.siaccecser.model.TipoOperazioneCassa;

/**
 * Converter per la cassa economale del tipo di operazione.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 14/09/2015
 */
@Component
public class TipoOperazioneCassaCassaEconomaleConverter extends ExtendedDozerConverter<TipoOperazioneCassa, SiacDCassaEconOperazTipo> {

	@Autowired
	private SiacTCassaEconRepository siacTCassaEconRepository;
	
	/**
	 *  Costruttore vuoto.
	 */
	public TipoOperazioneCassaCassaEconomaleConverter() {
		super(TipoOperazioneCassa.class, SiacDCassaEconOperazTipo.class);
	}

	@Override
	public TipoOperazioneCassa convertFrom(SiacDCassaEconOperazTipo src, TipoOperazioneCassa dest) {
		if(src == null || src.getSiacRCassaEconOperazTipoCassas() == null) {
			return dest;
		}
		
		for(SiacRCassaEconOperazTipoCassa siacRCassaEconOperazTipoCassa : src.getSiacRCassaEconOperazTipoCassas()) {
			if(siacRCassaEconOperazTipoCassa.getDataCancellazione() == null) {
				SiacTCassaEcon siacTCassaEcon = siacTCassaEconRepository.findOne(siacRCassaEconOperazTipoCassa.getSiacTCassaEcon().getUid());
				
				CassaEconomale cassaEconomale = mapNotNull(siacTCassaEcon, CassaEconomale.class, CecMapId.SiacTCassaEcon_CassaEconomale_Minimal);
				dest.setCassaEconomale(cassaEconomale);
				break;
			}
		}
		
		return dest;
	}

	@Override
	public SiacDCassaEconOperazTipo convertTo(TipoOperazioneCassa src, SiacDCassaEconOperazTipo dest) {
		if(src == null || src.getCassaEconomale() == null || src.getCassaEconomale().getUid() == 0) {
			return dest;
		}
		
		List<SiacRCassaEconOperazTipoCassa> siacRCassaEconOperazTipoCassas = new ArrayList<SiacRCassaEconOperazTipoCassa>();
		
		SiacTCassaEcon siacTCassaEcon = new SiacTCassaEcon();
		siacTCassaEcon.setUid(src.getCassaEconomale().getUid());
		
		SiacRCassaEconOperazTipoCassa siacRCassaEconOperazTipoCassa = new SiacRCassaEconOperazTipoCassa();
		siacRCassaEconOperazTipoCassa.setSiacDCassaEconOperazTipo(dest);
		siacRCassaEconOperazTipoCassa.setSiacTCassaEcon(siacTCassaEcon);
		
		siacRCassaEconOperazTipoCassa.setLoginOperazione(dest.getLoginOperazione());
		siacRCassaEconOperazTipoCassa.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		
		siacRCassaEconOperazTipoCassas.add(siacRCassaEconOperazTipoCassa);
		dest.setSiacRCassaEconOperazTipoCassas(siacRCassaEconOperazTipoCassas);
		
		return dest;
	}
	
}
