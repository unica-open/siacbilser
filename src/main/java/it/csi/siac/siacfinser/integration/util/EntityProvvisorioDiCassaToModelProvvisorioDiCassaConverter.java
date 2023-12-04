/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.util;

import java.math.BigDecimal;
import java.util.List;

import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.TipoClassificatore;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.entity.SiacDClassTipoFin;
import it.csi.siac.siacfinser.integration.entity.SiacROrdinativoProvCassaFin;
import it.csi.siac.siacfinser.integration.entity.SiacRProvCassaClassFin;
import it.csi.siac.siacfinser.integration.entity.SiacTClassFin;
import it.csi.siac.siacfinser.integration.entity.SiacTProvCassaFin;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa.TipoProvvisorioDiCassa;

public class EntityProvvisorioDiCassaToModelProvvisorioDiCassaConverter extends EntityToModelConverter {
	
	public static List<ProvvisorioDiCassa> siacTProvCassaEntityToProvvisorioDiCassaModel(List<SiacTProvCassaFin> dtos, List<ProvvisorioDiCassa> listaProvvisori, DatiOperazioneDto datiOperazioneDto){
		String methodName = "siacTProvCassaEntityToProvvisorioDiCassaModel";
		for(ProvvisorioDiCassa pc : listaProvvisori){
			int idIterato = pc.getUid();
			for(SiacTProvCassaFin siacTProvCassa : dtos){
				int idConfronto = siacTProvCassa.getUid();
				if(idIterato==idConfronto){
					
					// 1. Tipo Provvisorio di Cassa
					if(CostantiFin.D_PROV_CASSA_TIPO_SPESA.equalsIgnoreCase(siacTProvCassa.getSiacDProvCassaTipo().getProvcTipoCode())){
						pc.setTipoProvvisorioDiCassa(TipoProvvisorioDiCassa.S);
						pc.setDescTipoProvvisorioDiCassa(TipoProvvisorioDiCassa.S.toString());
					} else {
						pc.setTipoProvvisorioDiCassa(TipoProvvisorioDiCassa.E);
						pc.setDescTipoProvvisorioDiCassa(TipoProvvisorioDiCassa.E.toString());
					}
					
					//2. Importo da emettere
					BigDecimal importoDaEmettere = siacTProvCassa.getProvcImporto();																						
					if(siacTProvCassa.getSiacROrdinativoProvCassas()!=null && siacTProvCassa.getSiacROrdinativoProvCassas().size()>0){
						List<SiacROrdinativoProvCassaFin> listSiacROrdinativoProvCassa = siacTProvCassa.getSiacROrdinativoProvCassas();						
						for(SiacROrdinativoProvCassaFin siacROrdinativoProvCassa : listSiacROrdinativoProvCassa){
							importoDaEmettere = importoDaEmettere.subtract(siacROrdinativoProvCassa.getOrdProvcImporto());
						}					
					}
					pc.setImportoDaEmettere(importoDaEmettere);
					
					//3. Importo da regolarizzare
					pc.setImportoDaRegolarizzare(importoDaEmettere);
					
					
					pc.setCodiceContoEvidenza(siacTProvCassa.getProvcCodiceContoEvidenza());
					pc.setDescrizioneContoEvidenza(siacTProvCassa.getProvcDescrizioneContoEvidenza());
					
					//STRUTTURA AMM:
					StrutturaAmministrativoContabile sac = popolaStrutturaAmm(siacTProvCassa,datiOperazioneDto);
					pc.setStrutturaAmministrativoContabile(sac);
					
					break;
				}
			}
		}
		
		return listaProvvisori;
	}
	
	public static ProvvisorioDiCassa siacTProvCassaEntityToProvvisorioDiCassaModelPerChiave(SiacTProvCassaFin dto, ProvvisorioDiCassa provvisorioDiCassa, DatiOperazioneDto datiOperazioneDto) {
		String methodName = "siacTProvCassaEntityToProvvisorioDiCassaModelPerChiave";

		ProvvisorioDiCassa provCassaTrovata = provvisorioDiCassa;
		int idIterato = provvisorioDiCassa.getUid();
		int idConfronto = dto.getProvcId();

		if(idIterato==idConfronto){					
			// 1. Tipo Provvisorio di Cassa
			if(CostantiFin.D_PROV_CASSA_TIPO_SPESA.equalsIgnoreCase(dto.getSiacDProvCassaTipo().getProvcTipoCode())){
				provvisorioDiCassa.setTipoProvvisorioDiCassa(TipoProvvisorioDiCassa.S);
				provvisorioDiCassa.setDescTipoProvvisorioDiCassa(TipoProvvisorioDiCassa.S.toString());
			} else {
				provvisorioDiCassa.setTipoProvvisorioDiCassa(TipoProvvisorioDiCassa.E);
				provvisorioDiCassa.setDescTipoProvvisorioDiCassa(TipoProvvisorioDiCassa.E.toString());
			}	

			provCassaTrovata.setCodiceContoEvidenza(dto.getProvcCodiceContoEvidenza());
			provCassaTrovata.setDescrizioneContoEvidenza(dto.getProvcDescrizioneContoEvidenza());
			
			//STRUTTURA AMM:
			StrutturaAmministrativoContabile sac = popolaStrutturaAmm(dto, datiOperazioneDto);
			provCassaTrovata.setStrutturaAmministrativoContabile(sac);

			// 2. Estrazione ordinativi
			// 3. Estrazione predoc_prov_cassa
			// 4. Estrazione subdoc_prov_cassa

		}	
		
		return provCassaTrovata;
	}	
	
	private static StrutturaAmministrativoContabile popolaStrutturaAmm(SiacTProvCassaFin dto, DatiOperazioneDto datiOperazioneDto){
		StrutturaAmministrativoContabile sac = null;
		//Struttura amministrativa:
		SiacRProvCassaClassFin legameValido = null;
		if(dto.getSiacRProvCassaClasses()!=null){
			legameValido = DatiOperazioneUtil.getValido(dto.getSiacRProvCassaClasses(), datiOperazioneDto.getTs());
		}
		if(legameValido!=null){
			sac = new StrutturaAmministrativoContabile();
			SiacTClassFin siaTClass = legameValido.getSiacTClass();
			if(siaTClass!=null){
				sac.setUid(siaTClass.getUid());
				sac.setCodice(siaTClass.getClassifCode());
				sac.setDescrizione(siaTClass.getClassifDesc());
				
				SiacDClassTipoFin dClassTipo = siaTClass.getSiacDClassTipo();
				
				if(dClassTipo!=null){
					TipoClassificatore tipoClassificatore = new TipoClassificatore();
					tipoClassificatore.setUid(dClassTipo.getUid());
					tipoClassificatore.setCodice(dClassTipo.getClassifTipoCode());
					tipoClassificatore.setDescrizione(dClassTipo.getClassifTipoDesc());
					
					sac.setTipoClassificatore(tipoClassificatore);
				}
			}
		} 
		return sac;
	}
	
	
}
