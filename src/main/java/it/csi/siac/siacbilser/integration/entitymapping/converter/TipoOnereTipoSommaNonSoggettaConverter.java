/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacDOnere;
import it.csi.siac.siacbilser.integration.entity.SiacDSommaNonSoggettaTipo;
import it.csi.siac.siacbilser.integration.entity.SiacROnereSommaNonSoggettaTipo;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfin2ser.model.CodiceSommaNonSoggetta;
import it.csi.siac.siacfin2ser.model.TipoOnere;


/**
 * The Class TipoOnereTipoIvaSplitReverseConverter.
 */
@Component
public class TipoOnereTipoSommaNonSoggettaConverter extends ExtendedDozerConverter<TipoOnere, SiacDOnere > {
	
	/**
	 * Instantiates a new tipo onere tipo iva split reverse converter.
	 */
	public TipoOnereTipoSommaNonSoggettaConverter() {
		super(TipoOnere.class, SiacDOnere.class);
	}

	@Override
	public TipoOnere convertFrom(SiacDOnere src, TipoOnere dest) {
		if(src.getSiacROnereSommaNonSoggettaTipos() != null) {
			List<CodiceSommaNonSoggetta> codiciSommaNonSoggetta = new ArrayList<CodiceSommaNonSoggetta>();
			for(SiacROnereSommaNonSoggettaTipo r : src.getSiacROnereSommaNonSoggettaTipos()){
				if(r.getDataCancellazione() == null){
					CodiceSommaNonSoggetta codiceSommaNonSoggetta = map(r.getSiacDSommaNonSoggettaTipo(), CodiceSommaNonSoggetta.class, BilMapId.SiacDSommaNonSoggettaTipo_CodiceSommaNonSoggetta);
					codiciSommaNonSoggetta.add(codiceSommaNonSoggetta);
				}
			}
			dest.setCodiciSommaNonSoggetta(codiciSommaNonSoggetta);
		}
		return dest;
	}

	@Override
	public SiacDOnere convertTo(TipoOnere src, SiacDOnere dest) {
		if(src.getCodiciSommaNonSoggetta() != null){
			List<SiacROnereSommaNonSoggettaTipo> siacROnereSommaNonSoggettaTipos = new ArrayList<SiacROnereSommaNonSoggettaTipo>();
			for(CodiceSommaNonSoggetta codiceSomma : src.getCodiciSommaNonSoggetta()){
				SiacDSommaNonSoggettaTipo siacDSommaNonSoggettaTipo = map(codiceSomma, SiacDSommaNonSoggettaTipo.class, BilMapId.SiacDSommaNonSoggettaTipo_CodiceSommaNonSoggetta);
				
				SiacROnereSommaNonSoggettaTipo siacROnereSommaNonSoggettaTipo = new SiacROnereSommaNonSoggettaTipo();
				siacROnereSommaNonSoggettaTipo.setSiacDSommaNonSoggettaTipo(siacDSommaNonSoggettaTipo);
				siacROnereSommaNonSoggettaTipo.setSiacDOnere(dest);
				siacROnereSommaNonSoggettaTipo.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
				siacROnereSommaNonSoggettaTipo.setLoginOperazione(dest.getLoginOperazione());
				
				siacROnereSommaNonSoggettaTipos.add(siacROnereSommaNonSoggettaTipo);
			}
			dest.setSiacROnereSommaNonSoggettaTipos(siacROnereSommaNonSoggettaTipos);
		}
		return dest;
	}
	
}
