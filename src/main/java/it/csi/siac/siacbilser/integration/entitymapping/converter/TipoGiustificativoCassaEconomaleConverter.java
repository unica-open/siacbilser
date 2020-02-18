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
import it.csi.siac.siacbilser.integration.entity.SiacDGiustificativo;
import it.csi.siac.siacbilser.integration.entity.SiacRCassaEconGiustificativo;
import it.csi.siac.siacbilser.integration.entity.SiacTCassaEcon;
import it.csi.siac.siacbilser.integration.entitymapping.CecMapId;
import it.csi.siac.siaccecser.model.CassaEconomale;
import it.csi.siac.siaccecser.model.TipoGiustificativo;

/**
 * Converter per la cassa economale del tipo di operazione.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 14/09/2015
 */
@Component
public class TipoGiustificativoCassaEconomaleConverter extends ExtendedDozerConverter<TipoGiustificativo, SiacDGiustificativo> {

	@Autowired
	private SiacTCassaEconRepository siacTCassaEconRepository;
	
	/**
	 *  Costruttore vuoto.
	 */
	public TipoGiustificativoCassaEconomaleConverter() {
		super(TipoGiustificativo.class, SiacDGiustificativo.class);
	}

	@Override
	public TipoGiustificativo convertFrom(SiacDGiustificativo src, TipoGiustificativo dest) {
		if(src == null || src.getSiacRCassaEconGiustificativos() == null) {
			return dest;
		}
		
		for(SiacRCassaEconGiustificativo siacRCassaEconGiustificativo : src.getSiacRCassaEconGiustificativos()) {
			if(siacRCassaEconGiustificativo.getDataCancellazione() == null) {
				SiacTCassaEcon siacTCassaEcon = siacTCassaEconRepository.findOne(siacRCassaEconGiustificativo.getSiacTCassaEcon().getUid());
				
				CassaEconomale cassaEconomale = mapNotNull(siacTCassaEcon, CassaEconomale.class, CecMapId.SiacTCassaEcon_CassaEconomale_Minimal);
				dest.setCassaEconomale(cassaEconomale);
				break;
			}
		}
		
		return dest;
	}

	@Override
	public SiacDGiustificativo convertTo(TipoGiustificativo src, SiacDGiustificativo dest) {
		if(src == null || src.getCassaEconomale() == null || src.getCassaEconomale().getUid() == 0) {
			return dest;
		}
		
		List<SiacRCassaEconGiustificativo> siacRCassaEconGiustificativos = new ArrayList<SiacRCassaEconGiustificativo>();
		
		SiacTCassaEcon siacTCassaEcon = new SiacTCassaEcon();
		siacTCassaEcon.setUid(src.getCassaEconomale().getUid());
		
		SiacRCassaEconGiustificativo siacRCassaEconGiustificativo = new SiacRCassaEconGiustificativo();
		siacRCassaEconGiustificativo.setSiacDGiustificativo(dest);
		siacRCassaEconGiustificativo.setSiacTCassaEcon(siacTCassaEcon);
		
		siacRCassaEconGiustificativo.setLoginOperazione(dest.getLoginOperazione());
		siacRCassaEconGiustificativo.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		
		siacRCassaEconGiustificativos.add(siacRCassaEconGiustificativo);
		dest.setSiacRCassaEconGiustificativos(siacRCassaEconGiustificativos);
		
		return dest;
	}
	
}
