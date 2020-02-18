/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacfinser.CommonUtils;
import it.csi.siac.siacfinser.integration.entity.SiacTModpagFin;
import it.csi.siac.siacfinser.integration.entity.SiacTModpagModFin;
import it.csi.siac.siacfinser.integration.entity.SiacTNazioneFin;

public class OttimizzazioneModalitaPagamentoDto implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2573703973624140188L;
	
	
	private List<SiacTModpagFin> distintiSiacTSiacTModpagFinCoinvolti;
	
	private List<SiacTModpagModFin> distintiSiacTModpagModFinCoinvolti;
	
	private List<SiacTNazioneFin> distintiSiacTNazioneFinCoinvolti;
	
	//UTILITIES:
	
	public SiacTModpagFin findSiacTModpagFinByID(Integer modpagId){
		return CommonUtils.getByIdSiacTBase(distintiSiacTSiacTModpagFinCoinvolti, modpagId);
	}
	
	
	public SiacTModpagModFin findSiacTModpagModFinByModPagId(Integer idModPag){
		List<SiacTModpagModFin> filtrati = new ArrayList<SiacTModpagModFin>();
		if(idModPag!=null && distintiSiacTModpagModFinCoinvolti!=null && distintiSiacTModpagModFinCoinvolti.size()>0){
			for(SiacTModpagModFin it : distintiSiacTModpagModFinCoinvolti){
				if(it!=null && it.getSiacTModpag()!=null && it.getSiacTModpag().getModpagId()!=null && it.getSiacTModpag().getModpagId().intValue()==idModPag.intValue()){
					filtrati.add(it);
				}
			}
		}
		filtrati = CommonUtils.soloValidiSiacTBase(filtrati, null);
		if(filtrati!=null && filtrati.size()>0){
			//IN TEORIA SEMPRE UNO ED UNO SOLO
			return filtrati.get(0);
		} else {
			return null;
		}
	}
	
	public SiacTNazioneFin findSiacTNazioneFinCode(String nazioneCode){
		List<SiacTNazioneFin> filtrati = new ArrayList<SiacTNazioneFin>();
		if(nazioneCode!=null && distintiSiacTNazioneFinCoinvolti!=null && distintiSiacTNazioneFinCoinvolti.size()>0){
			for(SiacTNazioneFin it : distintiSiacTNazioneFinCoinvolti){
				if(it!=null && it.getNazioneCode()!=null && it.getNazioneCode().equalsIgnoreCase(nazioneCode)){
					filtrati.add(it);
				}
			}
		}
		return CommonUtils.getValidoSiacTBase(filtrati, null);
	}

	public List<SiacTModpagModFin> getDistintiSiacTModpagModFinCoinvolti() {
		return distintiSiacTModpagModFinCoinvolti;
	}

	public void setDistintiSiacTModpagModFinCoinvolti(
			List<SiacTModpagModFin> distintiSiacTModpagModFinCoinvolti) {
		this.distintiSiacTModpagModFinCoinvolti = distintiSiacTModpagModFinCoinvolti;
	}

	public List<SiacTModpagFin> getDistintiSiacTSiacTModpagFinCoinvolti() {
		return distintiSiacTSiacTModpagFinCoinvolti;
	}

	public void setDistintiSiacTSiacTModpagFinCoinvolti(
			List<SiacTModpagFin> distintiSiacTSiacTModpagFinCoinvolti) {
		this.distintiSiacTSiacTModpagFinCoinvolti = distintiSiacTSiacTModpagFinCoinvolti;
	}


	public List<SiacTNazioneFin> getDistintiSiacTNazioneFinCoinvolti() {
		return distintiSiacTNazioneFinCoinvolti;
	}


	public void setDistintiSiacTNazioneFinCoinvolti(
			List<SiacTNazioneFin> distintiSiacTNazioneFinCoinvolti) {
		this.distintiSiacTNazioneFinCoinvolti = distintiSiacTNazioneFinCoinvolti;
	}
	
	
}
