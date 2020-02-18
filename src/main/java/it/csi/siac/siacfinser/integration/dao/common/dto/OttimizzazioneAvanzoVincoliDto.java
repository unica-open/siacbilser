/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacfinser.CommonUtils;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacTAvanzovincoloFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsDetFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsFin;

public class OttimizzazioneAvanzoVincoliDto implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2573703973624140188L;
	
	private List<SiacTAvanzovincoloFin> distintiSiacTAvanzovincoloFinCoinvolti;
	private List<SiacRMovgestTsFin> distintiSiacRMovgestTsFinCoinvolti;
	private List<SiacTMovgestTsFin> distintiSiacTMovgestTsFinCoinvolti;
	private List<SiacTMovgestTsDetFin> distintiSiacTMovgestTsDetCoinvolti;
	private List<SiacRMovgestTsStatoFin> distintiSiacRMovgestTsStatoCoinvolti ;
	
	/**
	 * tipoImporto va valorizzato con Constanti.MOVGEST_TS_DET_TIPO_INIZIALE / Constanti.MOVGEST_TS_DET_TIPO_ATTUALE
	 * @param movgestTsId
	 * @param tipoImporto
	 * @return
	 */
	public BigDecimal estraiImporto(Integer movgestTsId, String tipoImporto){
		List<SiacTMovgestTsDetFin> listaSiacTMovgestTsDet = filtraSiacTMovgestTsDetByMovgestTs(movgestTsId);
		BigDecimal importo = BigDecimal.ZERO;
		for(SiacTMovgestTsDetFin siacTMovgestTsDet : listaSiacTMovgestTsDet){
			if(null!=siacTMovgestTsDet && siacTMovgestTsDet.getDataFineValidita()==null){
				if(tipoImporto.equalsIgnoreCase(siacTMovgestTsDet.getSiacDMovgestTsDetTipo().getMovgestTsDetTipoCode())){
					importo = siacTMovgestTsDet.getMovgestTsDetImporto();
					break;
				}
			}		
		}
		return importo;
	}
	
	public String estraiStatoCode(Integer movgestTsId){
		List<SiacRMovgestTsStatoFin> lista = filtraSiacRMovgestTsStatoByMovgestTs(movgestTsId);
		String statoCode = "";
		for(SiacRMovgestTsStatoFin statoIt : lista){
			if(null!=statoIt && statoIt.getDataFineValidita()==null && statoIt.getSiacDMovgestStato()!=null){
				statoCode = statoIt.getSiacDMovgestStato().getMovgestStatoCode();
					break;
			}		
		}
		return statoCode;
	}
	
	public List<SiacRMovgestTsStatoFin> filtraSiacRMovgestTsStatoByMovgestTs(Integer movgestTsId){
		List<SiacRMovgestTsStatoFin> filtrati = new ArrayList<SiacRMovgestTsStatoFin>();
		if(movgestTsId!=null && distintiSiacRMovgestTsStatoCoinvolti!=null && distintiSiacRMovgestTsStatoCoinvolti.size()>0){
			for(SiacRMovgestTsStatoFin it : distintiSiacRMovgestTsStatoCoinvolti){
				if(it.getSiacTMovgestT().getMovgestTsId().intValue()==movgestTsId.intValue()){
					filtrati.add(it);
				}
			}
		}
		return filtrati;
	}
	
	
	
	public List<SiacTMovgestTsDetFin> filtraSiacTMovgestTsDetByMovgestTs(Integer movgestTsId){
		List<SiacTMovgestTsDetFin> filtrati = new ArrayList<SiacTMovgestTsDetFin>();
		if(movgestTsId!=null && distintiSiacTMovgestTsDetCoinvolti!=null && distintiSiacTMovgestTsDetCoinvolti.size()>0){
			for(SiacTMovgestTsDetFin it : distintiSiacTMovgestTsDetCoinvolti){
				if(it.getSiacTMovgestT().getMovgestTsId().intValue()==movgestTsId.intValue()){
					filtrati.add(it);
				}
			}
		}
		return filtrati;
	}
	
	public List<SiacRMovgestTsFin> filtraSiacRMovgestTsFinByAvanzovincolo(Integer avavId){
		List<SiacRMovgestTsFin> filtrati = new ArrayList<SiacRMovgestTsFin>();
		if(avavId!=null && distintiSiacRMovgestTsFinCoinvolti!=null && distintiSiacRMovgestTsFinCoinvolti.size()>0){
			for(SiacRMovgestTsFin it : distintiSiacRMovgestTsFinCoinvolti){
				if(it.getSiacTAvanzovincoloFin()!=null && it.getSiacTAvanzovincoloFin().getAvavId()==avavId.intValue()){
					filtrati.add(it);
				}
			}
		}
		return filtrati;
	}
	
	public List<SiacTMovgestTsFin> estraiDistintiSiacTMovgestTsFinBySiacRMovgestTsFinCoinvolti(){
		List<SiacTMovgestTsFin> coinvolti = new ArrayList<SiacTMovgestTsFin>();
		if(distintiSiacRMovgestTsFinCoinvolti!=null && distintiSiacRMovgestTsFinCoinvolti.size()>0){
			for(SiacRMovgestTsFin it : distintiSiacRMovgestTsFinCoinvolti){
				if(it!=null && it.getSiacTMovgestTsA()!=null){
					coinvolti.add(it.getSiacTMovgestTsA());
				}
				if(it!=null && it.getSiacTMovgestTsB()!=null){
					coinvolti.add(it.getSiacTMovgestTsB());
				}
			}
		}
		coinvolti = CommonUtils.ritornaSoloDistintiByUid(coinvolti);
		return coinvolti;
	}
	
	public List<SiacTAvanzovincoloFin> getDistintiSiacTAvanzovincoloFinCoinvolti() {
		return distintiSiacTAvanzovincoloFinCoinvolti;
	}
	public void setDistintiSiacTAvanzovincoloFinCoinvolti(
			List<SiacTAvanzovincoloFin> distintiSiacTAvanzovincoloFinCoinvolti) {
		this.distintiSiacTAvanzovincoloFinCoinvolti = distintiSiacTAvanzovincoloFinCoinvolti;
	}
	public List<SiacRMovgestTsFin> getDistintiSiacRMovgestTsFinCoinvolti() {
		return distintiSiacRMovgestTsFinCoinvolti;
	}
	public void setDistintiSiacRMovgestTsFinCoinvolti(
			List<SiacRMovgestTsFin> distintiSiacRMovgestTsFinCoinvolti) {
		this.distintiSiacRMovgestTsFinCoinvolti = distintiSiacRMovgestTsFinCoinvolti;
	}
	public List<SiacTMovgestTsFin> getDistintiSiacTMovgestTsFinCoinvolti() {
		return distintiSiacTMovgestTsFinCoinvolti;
	}
	public void setDistintiSiacTMovgestTsFinCoinvolti(
			List<SiacTMovgestTsFin> distintiSiacTMovgestTsFinCoinvolti) {
		this.distintiSiacTMovgestTsFinCoinvolti = distintiSiacTMovgestTsFinCoinvolti;
	}
	public List<SiacTMovgestTsDetFin> getDistintiSiacTMovgestTsDetCoinvolti() {
		return distintiSiacTMovgestTsDetCoinvolti;
	}
	public void setDistintiSiacTMovgestTsDetCoinvolti(
			List<SiacTMovgestTsDetFin> distintiSiacTMovgestTsDetCoinvolti) {
		this.distintiSiacTMovgestTsDetCoinvolti = distintiSiacTMovgestTsDetCoinvolti;
	}

	public List<SiacRMovgestTsStatoFin> getDistintiSiacRMovgestTsStatoCoinvolti() {
		return distintiSiacRMovgestTsStatoCoinvolti;
	}

	public void setDistintiSiacRMovgestTsStatoCoinvolti(
			List<SiacRMovgestTsStatoFin> distintiSiacRMovgestTsStatoCoinvolti) {
		this.distintiSiacRMovgestTsStatoCoinvolti = distintiSiacRMovgestTsStatoCoinvolti;
	}
	
}
