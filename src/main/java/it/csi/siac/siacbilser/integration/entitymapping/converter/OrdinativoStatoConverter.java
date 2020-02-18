/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.dozer.DozerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacTOrdinativoBilRepository;
import it.csi.siac.siacbilser.integration.entity.SiacROrdinativoStato;
import it.csi.siac.siacbilser.integration.entity.SiacTOrdinativo;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDOrdinativoStatoEnum;
import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo.StatoOperativoOrdinativo;

@Component
public class OrdinativoStatoConverter extends DozerConverter<Ordinativo, SiacTOrdinativo > {
	
	private LogUtil log = new LogUtil(this.getClass());
	
	@Autowired
	private SiacTOrdinativoBilRepository siacTOrdinativoBilRepository;

	public OrdinativoStatoConverter() {
		super(Ordinativo.class, SiacTOrdinativo.class);
	}

	@Override
	public Ordinativo convertFrom(SiacTOrdinativo src, Ordinativo dest) {
		String methodName = "convertFrom";
		
		SiacTOrdinativo siacTOrdinativo =  siacTOrdinativoBilRepository.findOne(src.getUid());
		if(siacTOrdinativo.getSiacROrdinativoStatos() == null){
			log.debug(methodName , "siacTOrdinativo.getSiacROrdinativoStatos() == null");
			return dest;
		}
				
		for (SiacROrdinativoStato r : siacTOrdinativo.getSiacROrdinativoStatos()) {
			log.debug(methodName , "SiacROrdinativoStato con dataCancellazione: " + r.getDataCancellazione() + " e dataFineValidita: " + r.getDataFineValidita());
			if (r.getDataFineValidita() == null && r.getDataCancellazione() == null) {
				StatoOperativoOrdinativo statoOperativo = SiacDOrdinativoStatoEnum.byCodice(r.getSiacDOrdinativoStato().getOrdinativoStatoCode()).getStatoOperativo();
				log.debug(methodName , "trovato statoOperativo: " + statoOperativo.name());
				dest.setStatoOperativoOrdinativo(statoOperativo);
				dest.setCodStatoOperativoOrdinativo(r.getSiacDOrdinativoStato().getOrdinativoStatoCode());
				break;
			}
		}
		return dest;
	}

	@Override
	public SiacTOrdinativo convertTo(Ordinativo src, SiacTOrdinativo dest) {
		
		return dest;
	}



	

}
