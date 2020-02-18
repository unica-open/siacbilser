/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.List;

import org.dozer.DozerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacTElencoDocRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTElencoDoc;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;

@Component
public class ElencoDocContieneQuoteACoperturaConverter extends DozerConverter<ElencoDocumentiAllegato, SiacTElencoDoc> {
	
	private LogUtil log = new LogUtil(this.getClass());
	
	
	@Autowired
	private SiacTElencoDocRepository siacTElencoDocRepository;

	
	public ElencoDocContieneQuoteACoperturaConverter() {
		super(ElencoDocumentiAllegato.class, SiacTElencoDoc.class);
	}

	@Override
	public ElencoDocumentiAllegato convertFrom(SiacTElencoDoc src, ElencoDocumentiAllegato dest) {
		final String methodName = "convertFrom";
		
		List<SiacTSubdoc> siacTSubdocs = siacTElencoDocRepository.findQuoteCollegateAProvvisorioDiCassa(src.getUid());
		
		dest.setContieneQuoteACopertura(siacTSubdocs!=null && !siacTSubdocs.isEmpty());
		
		log.debug(methodName, "ContieneQuoteACopertura: " + dest.getContieneQuoteACopertura());
		
		return dest;
	}

	@Override
	public SiacTElencoDoc convertTo(ElencoDocumentiAllegato src, SiacTElencoDoc dest) {
		
		return dest;
	}
	

}
