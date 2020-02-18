/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacDPdceRelTipo;
import it.csi.siac.siacbilser.integration.entity.SiacRPdceConto;
import it.csi.siac.siacbilser.integration.entity.SiacTPdceConto;
import it.csi.siac.siacbilser.integration.entitymapping.GenMapId;
import it.csi.siac.siacgenser.model.Conto;
import it.csi.siac.siacgenser.model.TipoLegame;

/**
 * The Class ContoContoCollegatoConverter.
 */
@Component
public class ContoContoCollegatoConverter extends ExtendedDozerConverter<Conto, SiacTPdceConto> {
	

	/**
	 * Instantiates a new conto conto collegato converter.
	 */
	public ContoContoCollegatoConverter() {
		super(Conto.class, SiacTPdceConto.class);
	}

	@Override
	public Conto convertFrom(SiacTPdceConto src, Conto dest) {
		
		if(src.getSiacRPdceContos1()!=null){
			for(SiacRPdceConto siacRPdceConto : src.getSiacRPdceContos1()){
				if(siacRPdceConto.getDataCancellazione()!=null
						|| !siacRPdceConto.isDataValiditaCompresa(dest.getDataInizioValiditaFiltro())){
					continue;
				}
				
				if(siacRPdceConto.getSiacTPdceConto2()==null){  //su DB SiacTPdceConto2 e' nullable!
					continue;
				}
				
				SiacTPdceConto siacTPdceConto2 = siacRPdceConto.getSiacTPdceConto2();
				
				Conto contoCollegato = new Conto();
				contoCollegato.setDataInizioValiditaFiltro(dest.getDataInizioValiditaFiltro());
				map(siacTPdceConto2, contoCollegato, GenMapId.SiacTPdceConto_Conto_Base); //occhio questo converter a sua volta non deve mappare un altro conto collegato!!
				dest.setContoCollegato(contoCollegato);
				
				
				SiacDPdceRelTipo siacDPdceRelTipo = siacRPdceConto.getSiacDPdceRelTipo();
				if(siacDPdceRelTipo != null) { //siacDPdceRelTipo e' nullable su DB.
					TipoLegame tipoLegame = new TipoLegame();
					tipoLegame.setUid(siacDPdceRelTipo.getUid());
					tipoLegame.setCodice(siacDPdceRelTipo.getPdcerelCode());
					tipoLegame.setDescrizione(siacDPdceRelTipo.getPdcerelDesc());
					
					dest.setTipoLegame(tipoLegame);
				}
				
							
			}	
		}
		
		return dest;
	}
	

	@Override
	public SiacTPdceConto convertTo(Conto src, SiacTPdceConto dest) {
		
		if(src.getContoCollegato()==null || src.getContoCollegato().getUid()==0){
			return dest;
		}
		
		
		SiacRPdceConto siacRPdceConto = new SiacRPdceConto();
		
		SiacTPdceConto siacTPdceConto2 = new SiacTPdceConto();
		siacTPdceConto2.setUid(src.getContoCollegato().getUid());
		siacRPdceConto.setSiacTPdceConto2(siacTPdceConto2);
		
		if(src.getTipoLegame()!=null && src.getTipoLegame().getUid()!=0) {
			SiacDPdceRelTipo siacDPdceRelTipo = new SiacDPdceRelTipo();
			siacDPdceRelTipo.setUid(src.getTipoLegame().getUid());
			siacRPdceConto.setSiacDPdceRelTipo(siacDPdceRelTipo);
		}
		
		siacRPdceConto.setLoginOperazione(dest.getLoginOperazione());
		siacRPdceConto.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		
		
		dest.setSiacRPdceContos1(new ArrayList<SiacRPdceConto>());
		dest.addSiacRPdceContos1(siacRPdceConto);
		
		
		return dest;
	}



}
