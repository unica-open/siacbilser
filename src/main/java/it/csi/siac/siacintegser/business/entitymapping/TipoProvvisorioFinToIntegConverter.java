/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.entitymapping;

import org.dozer.DozerConverter;
import org.springframework.stereotype.Component;

import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;
import it.csi.siac.siacintegser.model.enumeration.TipoProvvisorioDiCassa;

@Component
public class TipoProvvisorioFinToIntegConverter extends DozerConverter<it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa, it.csi.siac.siacintegser.model.integ.ProvvisorioDiCassa> {

	
	
	public TipoProvvisorioFinToIntegConverter() {
		super(it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa.class, it.csi.siac.siacintegser.model.integ.ProvvisorioDiCassa.class);
	}

	@Override
	public ProvvisorioDiCassa convertFrom(
			it.csi.siac.siacintegser.model.integ.ProvvisorioDiCassa arg0,
			ProvvisorioDiCassa arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public it.csi.siac.siacintegser.model.integ.ProvvisorioDiCassa convertTo(ProvvisorioDiCassa provvisorioFin,
			it.csi.siac.siacintegser.model.integ.ProvvisorioDiCassa provvisorioInteg) {
	
		
		if(provvisorioFin!=null){
			
			if(provvisorioInteg==null){
				provvisorioInteg = new it.csi.siac.siacintegser.model.integ.ProvvisorioDiCassa();
			}
			
			//TIPO PROVVISORIO:
			provvisorioInteg.setTipoProvvisorioDiCassa(tipoProvvisorioFinToTipoProvvisorioInteg(provvisorioFin.getTipoProvvisorioDiCassa()));
		}
		
		
		return provvisorioInteg;
	}
	
	public static TipoProvvisorioDiCassa tipoProvvisorioFinToTipoProvvisorioInteg(it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa.TipoProvvisorioDiCassa tipoFin){
		TipoProvvisorioDiCassa tipoInteg = null;
		if(tipoFin!=null){
			if(tipoFin.equals(it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa.TipoProvvisorioDiCassa.E)){
				tipoInteg = TipoProvvisorioDiCassa.ENTRATA;
			} else if(tipoFin.equals(it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa.TipoProvvisorioDiCassa.S)){
				tipoInteg = TipoProvvisorioDiCassa.SPESA;
			}
		} else {
			tipoInteg = null;
		}
		return tipoInteg;
	}
	
}
