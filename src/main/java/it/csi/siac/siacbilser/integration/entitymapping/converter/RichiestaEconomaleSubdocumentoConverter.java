/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacTSubdocRepository;
import it.csi.siac.siacbilser.integration.entity.SiacRRichiestaEconSubdoc;
import it.csi.siac.siacbilser.integration.entity.SiacTRichiestaEcon;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocFamTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siaccecser.model.RichiestaEconomale;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;

/**
 * The Class RichiestaEconomaleSubdocumentoConverter.
 */
@Component
public class RichiestaEconomaleSubdocumentoConverter extends ExtendedDozerConverter<RichiestaEconomale, SiacTRichiestaEcon> {
	
	@Autowired
	private SiacTSubdocRepository siacTSubdocRepository;
	
	/**
	 * Instantiates a new richiesta economale subdocumento converter.
	 */
	public RichiestaEconomaleSubdocumentoConverter() {
		super(RichiestaEconomale.class, SiacTRichiestaEcon.class);
	}

	@Override
	public RichiestaEconomale convertFrom(SiacTRichiestaEcon src, RichiestaEconomale dest) {
		if(src == null || src.getSiacRRichiestaEconSubdocs() == null || src.getSiacRRichiestaEconSubdocs().isEmpty()){
			return dest;
		}
		
		List<SubdocumentoSpesa> subdocumenti = new ArrayList<SubdocumentoSpesa>();
		
		for(SiacRRichiestaEconSubdoc siacRRichiestaEconSubdoc : src.getSiacRRichiestaEconSubdocs()){
			if(siacRRichiestaEconSubdoc.getDataCancellazione() == null
					&& siacRRichiestaEconSubdoc.getSiacTSubdoc() != null
					&& siacRRichiestaEconSubdoc.getSiacTSubdoc().getSiacTDoc() != null
					&& siacRRichiestaEconSubdoc.getSiacTSubdoc().getSiacTDoc().getSiacDDocTipo() != null
					&& siacRRichiestaEconSubdoc.getSiacTSubdoc().getSiacTDoc().getSiacDDocTipo().getSiacDDocFamTipo() != null
					&& (SiacDDocFamTipoEnum.Spesa.getCodice().equals(siacRRichiestaEconSubdoc.getSiacTSubdoc().getSiacTDoc().getSiacDDocTipo().getSiacDDocFamTipo().getDocFamTipoCode())
							|| SiacDDocFamTipoEnum.IvaSpesa.getCodice().equals(siacRRichiestaEconSubdoc.getSiacTSubdoc().getSiacTDoc().getSiacDDocTipo().getSiacDDocFamTipo().getDocFamTipoCode()))){
				
				// Ho un subdocumento di spesa. Lo mappo e lo reinvio
				SiacTSubdoc siacTSubdoc = siacTSubdocRepository.findOne(siacRRichiestaEconSubdoc.getSiacTSubdoc().getUid());
				
				SubdocumentoSpesa subdocumentoSpesa = map(siacTSubdoc, SubdocumentoSpesa.class, BilMapId.SiacTSubdoc_SubdocumentoSpesa_Base);
				subdocumenti.add(subdocumentoSpesa);
			}
		}
		dest.setSubdocumenti(subdocumenti);
		return dest;
	}
	
	@Override
	public SiacTRichiestaEcon convertTo(RichiestaEconomale src, SiacTRichiestaEcon dest) {
		
		if(src == null || src.getSubdocumenti() == null || src.getSubdocumenti().isEmpty()){
			return dest;
		}
		
		List<SiacRRichiestaEconSubdoc> siacRRichiestaEconSubdocs = new ArrayList<SiacRRichiestaEconSubdoc>();
		
		for(SubdocumentoSpesa ss : src.getSubdocumenti()) {
			SiacTSubdoc siacTSubdoc = new SiacTSubdoc();
			siacTSubdoc.setUid(ss.getUid());
			
			SiacRRichiestaEconSubdoc siacRRichiestaEconSubdoc = new SiacRRichiestaEconSubdoc();
			siacRRichiestaEconSubdoc.setSiacTRichiestaEcon(dest);
			siacRRichiestaEconSubdoc.setSiacTSubdoc(siacTSubdoc);
			
			siacRRichiestaEconSubdoc.setLoginOperazione(dest.getLoginOperazione());
			siacRRichiestaEconSubdoc.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
			
			siacRRichiestaEconSubdocs.add(siacRRichiestaEconSubdoc);
		}
		
		dest.setSiacRRichiestaEconSubdocs(siacRRichiestaEconSubdocs);
		
		return dest;
	}



	

}
