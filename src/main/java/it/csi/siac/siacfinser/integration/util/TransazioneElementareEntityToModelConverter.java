/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.util;

import java.util.List;

import it.csi.siac.siaccommonser.util.log.LogSrvUtil;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.StringUtilsFin;
import it.csi.siac.siacfinser.integration.entity.SiacRAttrBaseFin;
import it.csi.siac.siacfinser.integration.entity.SiacRClassBaseFin;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.MovimentoGestione.AttributoMovimentoGestione;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.TransazioneElementare;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoIncasso;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoPagamento;


public class TransazioneElementareEntityToModelConverter {
	
	protected transient LogSrvUtil log = new LogSrvUtil(this.getClass());
	
	public static boolean isEntrata(TransazioneElementare transazioneElementare){
		boolean entrata = false;
		if(transazioneElementare instanceof Accertamento || transazioneElementare instanceof SubAccertamento ||
				transazioneElementare instanceof OrdinativoIncasso)
		{
			entrata = true;
		}
		return entrata;
	}
	
	public static TipoOggettoConClassificatori getTipoClassificatori(TransazioneElementare transazioneElementare){
		
		TipoOggettoConClassificatori tipoOggettoConClassificatori = null;
		
		if(transazioneElementare instanceof Accertamento ){
			tipoOggettoConClassificatori = TipoOggettoConClassificatori.ACCERTAMENTO;
		}
		
		if(transazioneElementare instanceof Impegno ){
			tipoOggettoConClassificatori = TipoOggettoConClassificatori.IMPEGNO;
		}
		
		if(transazioneElementare instanceof OrdinativoPagamento ){
			tipoOggettoConClassificatori = TipoOggettoConClassificatori.ORDINATIVO_PAGAMENTO;
		}
		
		if(transazioneElementare instanceof Liquidazione ){
			tipoOggettoConClassificatori = TipoOggettoConClassificatori.LIQUIDAZIONE;
		}
		
		// TODO INCASso
		if(transazioneElementare instanceof OrdinativoIncasso ){
			tipoOggettoConClassificatori = TipoOggettoConClassificatori.ORDINATIVO_INCASSO;
		}
		
		return tipoOggettoConClassificatori;
		
	}
	
	public static <T extends SiacRClassBaseFin, R extends SiacRAttrBaseFin>
		TransazioneElementare convertiDatiTransazioneElementare(TransazioneElementare te,List<T> listaSiacRClass,List<R> listaSiacRAttr ){
		
		boolean entrata =isEntrata(te);
		
		
		if(listaSiacRAttr!=null && listaSiacRAttr.size()>0){
			for(R siacRAttr : listaSiacRAttr){
				if(null!=siacRAttr && siacRAttr.getDataFineValidita()==null){
					String codiceAttributo = siacRAttr.getSiacTAttr().getAttrCode();
					AttributoMovimentoGestione attributoMovimentoGestione = CostantiFin.attributoMovimentoGestioneStringToEnum(codiceAttributo);
					switch (attributoMovimentoGestione) {
						case cup:
							if(siacRAttr.getTesto() != null){
								if(!siacRAttr.getTesto().equalsIgnoreCase("null")){
									te.setCup(siacRAttr.getTesto());
								}
							}
							break;
						default:
							break;
					}
				}									
			}
		}
		
		if(listaSiacRClass!=null && listaSiacRClass.size()>0){
			for(T siacRClass : listaSiacRClass){
				if(null!=siacRClass && siacRClass.getDataFineValidita()==null){
					
					String codice = siacRClass.getSiacTClass().getClassifCode();
					String desc = siacRClass.getSiacTClass().getClassifDesc();
					Integer siacTClassUid = siacRClass.getSiacTClass().getUid();
					String codiceTipoClass = siacRClass.getSiacTClass().getSiacDClassTipo().getClassifTipoCode();
					
					String tipoCode = siacRClass.getSiacTClass().getSiacDClassTipo().getClassifTipoCode();
					
					if(tipoCode.equalsIgnoreCase(CostantiFin.D_CLASS_TIPO_MISSIONE)){
						//MISSIONE:
						te.setCodMissione(codice);
					} else if(tipoCode.equalsIgnoreCase(CostantiFin.D_CLASS_TIPO_PROGRAMMA)){
						// PROGRAMMA:
						te.setCodProgramma(codice);
						te.setDescProgramma(desc);
					}else if(StringUtilsFin.contenutoIn(tipoCode, CostantiFin.getCodiciPianoDeiConti())){
						// PIANO DEI CONTI:
						te.setCodPdc(codice); 
						te.setDescPdc(desc);
						te.setIdPdc(siacTClassUid);	
						te.setCodicePdc(codiceTipoClass);	
					} 
					else if(tipoCode.equalsIgnoreCase(CostantiFin.D_CLASS_TIPO_GRUPPO_COFOG)){
						//COFOG:
						te.setCodCofog(codice);
						te.setDescCofog(desc);
					} else if(tipoCode.equalsIgnoreCase(CostantiFin.D_CLASS_TIPO_TRANSAZIONE_UE_SPESA) && !entrata){
						// TRANSAZIONE UE SPESA:
						te.setCodTransazioneEuropeaSpesa(codice);
						te.setDescTransazioneEuropeaSpesa(desc);
					} else if(tipoCode.equalsIgnoreCase(CostantiFin.D_CLASS_TIPO_TRANSAZIONE_UE_ENTRATA) && entrata){
						// TRANSAZIONE UE ENTRATA:
						te.setCodTransazioneEuropeaSpesa(codice);
						te.setDescTransazioneEuropeaSpesa(desc);
					} else if(tipoCode.equalsIgnoreCase(CostantiFin.D_CLASS_TIPO_CLASSE_RICORRENTE_SPESA) && !entrata){
						// RICORRENTE ENTRATA
						te.setCodRicorrenteSpesa(codice);
					}else if(tipoCode.equalsIgnoreCase(CostantiFin.D_CLASS_TIPO_CLASSE_RICORRENTE_ENTRATA) && entrata){
						// RICORRENTE SPE?SA
						te.setCodRicorrenteSpesa(codice);
					}else if(StringUtilsFin.contenutoIn(tipoCode, CostantiFin.getCodiciSiopeEntrata()) && entrata){
						// SIOPE
						te.setCodSiope(codice);
						te.setDescCodSiope(desc);
						te.setIdSiope(siacTClassUid);
					}else if(StringUtilsFin.contenutoIn(tipoCode, CostantiFin.getCodiciSiopeSpesa()) && !entrata){
						// SIOPE
						te.setCodSiope(codice);
						te.setDescCodSiope(desc);
						te.setIdSiope(siacTClassUid);
					} else if(tipoCode.equalsIgnoreCase(CostantiFin.D_CLASS_TIPO_CLASSE_PERIMETRO_SANITARIO_SPESA) && !entrata){
						// PERIMETRO SANITARIO SPESA
						te.setCodCapitoloSanitarioSpesa(codice);
					} else if(tipoCode.equalsIgnoreCase(CostantiFin.D_CLASS_TIPO_CLASSE_PERIMETRO_SANITARIO_ENTRATA) && entrata){
						// PERIMETRO SANITARIO ENTRATA
						te.setCodCapitoloSanitarioSpesa(codice);
					} else if(tipoCode.equalsIgnoreCase(CostantiFin.D_CLASS_TIPO_PROGRAMMA_POLITICHE_REGIONALI_UNITARIE)){
						// PROGRAMMA_POLITICHE_REGIONALI_UNITARIE:
						te.setCodPrgPolReg(codice);
					} else if(tipoCode.equalsIgnoreCase(CostantiFin.D_CLASS_TIPO_CLASSIFICATORE_11)){
						// CLASSIFICATORE FIN 11
						te.setCodClassGen11(codice);
					} else if(tipoCode.equalsIgnoreCase(CostantiFin.D_CLASS_TIPO_CLASSIFICATORE_12)){
						// CLASSIFICATORE FIN 12
						te.setCodClassGen12(codice);
					} else if(tipoCode.equalsIgnoreCase(CostantiFin.D_CLASS_TIPO_CLASSIFICATORE_13)){
						// CLASSIFICATORE FIN 13
						te.setCodClassGen13(codice);
					} else if(tipoCode.equalsIgnoreCase(CostantiFin.D_CLASS_TIPO_CLASSIFICATORE_14)){
						// CLASSIFICATORE FIN 14
						te.setCodClassGen14(codice);
					} else if(tipoCode.equalsIgnoreCase(CostantiFin.D_CLASS_TIPO_CLASSIFICATORE_15)){
						// CLASSIFICATORE FIN 15
						te.setCodClassGen15(codice);
						
					} else if(tipoCode.equalsIgnoreCase(CostantiFin.D_CLASS_TIPO_CLASSIFICATORE_16)){
						// CLASSIFICATORE FIN 16
						te.setCodClassGen16(codice);
					} else if(tipoCode.equalsIgnoreCase(CostantiFin.D_CLASS_TIPO_CLASSIFICATORE_17)){
						// CLASSIFICATORE FIN 17
						te.setCodClassGen17(codice);
					} else if(tipoCode.equalsIgnoreCase(CostantiFin.D_CLASS_TIPO_CLASSIFICATORE_18)){
						// CLASSIFICATORE FIN 18
						te.setCodClassGen18(codice);
					} else if(tipoCode.equalsIgnoreCase(CostantiFin.D_CLASS_TIPO_CLASSIFICATORE_19)){
						// CLASSIFICATORE FIN 19
						te.setCodClassGen19(codice);
					} else if(tipoCode.equalsIgnoreCase(CostantiFin.D_CLASS_TIPO_CLASSIFICATORE_20)){
						// CLASSIFICATORE FIN 20
						te.setCodClassGen20(codice);
						
					} else if(tipoCode.equalsIgnoreCase(CostantiFin.D_CLASS_TIPO_CLASSIFICATORE_21)){
						// CLASSIFICATORE FIN 11
						te.setCodClassGen11(codice);
					} else if(tipoCode.equalsIgnoreCase(CostantiFin.D_CLASS_TIPO_CLASSIFICATORE_22)){
						// CLASSIFICATORE FIN 12
						te.setCodClassGen12(codice);
					} else if(tipoCode.equalsIgnoreCase(CostantiFin.D_CLASS_TIPO_CLASSIFICATORE_23)){
						// CLASSIFICATORE FIN 13
						te.setCodClassGen13(codice);
					} else if(tipoCode.equalsIgnoreCase(CostantiFin.D_CLASS_TIPO_CLASSIFICATORE_24)){
						// CLASSIFICATORE FIN 14
						te.setCodClassGen14(codice);
					} else if(tipoCode.equalsIgnoreCase(CostantiFin.D_CLASS_TIPO_CLASSIFICATORE_25)){
						// CLASSIFICATORE FIN 15
						te.setCodClassGen15(codice);
					}			
					
					// ordinativo incasso
					 else if(tipoCode.equalsIgnoreCase(CostantiFin.D_CLASS_TIPO_CLASSIFICATORE_26)){
						// CLASSIFICATORE FIN 16
						te.setCodClassGen16(codice);
					} else if(tipoCode.equalsIgnoreCase(CostantiFin.D_CLASS_TIPO_CLASSIFICATORE_27)){
						// CLASSIFICATORE FIN 17
						te.setCodClassGen17(codice);
					} else if(tipoCode.equalsIgnoreCase(CostantiFin.D_CLASS_TIPO_CLASSIFICATORE_28)){
						// CLASSIFICATORE FIN 18
						te.setCodClassGen18(codice);
					} else if(tipoCode.equalsIgnoreCase(CostantiFin.D_CLASS_TIPO_CLASSIFICATORE_29)){
						// CLASSIFICATORE FIN 19
						te.setCodClassGen19(codice);
					} else if(tipoCode.equalsIgnoreCase(CostantiFin.D_CLASS_TIPO_CLASSIFICATORE_30)){
						// CLASSIFICATORE FIN 20
						te.setCodClassGen20(codice);
					}					
					
				}									
			}
		}
		
		return te;
	}
	
	public static TransazioneElementare copiaDatiTransazioneElementare(TransazioneElementare da, TransazioneElementare a){
		a.setCodCapitoloSanitarioSpesa(da.getCodCapitoloSanitarioSpesa());
		a.setCodCofog(da.getCodCofog());
		a.setCodContoEconomico(da.getCodContoEconomico());
		a.setCodicePdc(da.getCodicePdc());
		a.setCodMissione(da.getCodMissione());
		a.setCodPrgPolReg(da.getCodPrgPolReg());
		a.setCodProgramma(da.getCodProgramma());
		a.setCodRicorrenteSpesa(da.getCodRicorrenteSpesa());
		a.setCodSiope(da.getCodSiope());
		a.setCodTransazioneEuropeaSpesa(da.getCodTransazioneEuropeaSpesa());
		a.setCup(da.getCup());
		a.setIdContoEconomico(da.getIdContoEconomico());
		a.setIdPdc(da.getIdPdc());
		a.setIdSiope(da.getIdSiope());
		a.setCodPdc(da.getCodPdc());
		return a;
	}
	
}
