/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter.cespiti;

import java.util.Date;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.entity.SiacDCespitiBeneTipo;
import it.csi.siac.siacbilser.integration.entity.SiacTCespiti;
import it.csi.siac.siacbilser.integration.entitymapping.CespMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.ExtendedDozerConverter;
import it.csi.siac.siacbilser.integration.entitymapping.converter.base.Converters;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siaccespser.model.TipoBeneCespite;
import it.csi.siac.siaccespser.model.TipoBeneCespiteModelDetail;

/**
 * The Class CespitiTipoBeneCespiteConverter.
 *
 * @author Anto
 */
@Component
public class CespiteTipoBeneCespiteModelDetailConverter extends ExtendedDozerConverter<Cespite, SiacTCespiti > {
	
	public CespiteTipoBeneCespiteModelDetailConverter() {
		super(Cespite.class, SiacTCespiti.class);
	}

	@Override
	public Cespite convertFrom(SiacTCespiti src, Cespite dest) {
		String methodName = "convertFrom";
		
		if(src.getSiacDCespitiBeneTipo()== null){
			log.warn(methodName, "Cespite [uid: " + src.getUid() + "] privo del Tipo Bene! Controllare su DB. Entita associata:" + src.getClass().getSimpleName());
			return dest;
		}
		
		// Per evitare problemi in scrittura
		TipoBeneCespite tipoBeneCespite = new TipoBeneCespite();
		Date dataInizioValiditaFiltro = Utility.ultimoGiornoDellAnno(Utility.BTL.get().getAnno());
		tipoBeneCespite.setDataInizioValiditaFiltro(dataInizioValiditaFiltro);
	
		mapNotNull(src.getSiacDCespitiBeneTipo(),
				tipoBeneCespite,
				CespMapId.SiacDCespitiBeneTipo_TipoBeneCespite_ModelDetail,
				Converters.byModelDetails(Utility.MDTL.byModelDetailClass(TipoBeneCespiteModelDetail.class)));
		
		dest.setTipoBeneCespite(tipoBeneCespite);
		return dest;
		
	}

	@Override
	public SiacTCespiti convertTo(Cespite src, SiacTCespiti dest) {
		if(src.getTipoBeneCespite() == null || src.getTipoBeneCespite().getUid() == 0) {
			return dest;
		}
		SiacDCespitiBeneTipo siacDCespitiBeneTipo = new SiacDCespitiBeneTipo();
		siacDCespitiBeneTipo.setUid(src.getTipoBeneCespite().getUid());
		dest.setSiacDCespitiBeneTipo(siacDCespitiBeneTipo);
		return dest;
	}


}
