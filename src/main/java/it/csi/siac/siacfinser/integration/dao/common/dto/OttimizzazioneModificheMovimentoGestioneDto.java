/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacfinser.CommonUtil;
import it.csi.siac.siacfinser.integration.entity.SiacRModificaStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsSogModFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsSogclasseModFin;
import it.csi.siac.siacfinser.integration.entity.SiacTModificaFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsDetModFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsFin;
import it.csi.siac.siacfinser.integration.entity.SiacTSoggettoFin;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

public class OttimizzazioneModificheMovimentoGestioneDto implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2573703973624140188L;
	
	private List<SiacRMovgestTsSogModFin> distintiSiacRMovgestTsSogModFinCoinvolti;
	
	private List<SiacRMovgestTsSogclasseModFin> distintiSiacRMovgestTsSogclasseModFinCoinvolti;
	
	private List<SiacTSoggettoFin> distintiSiacTSoggettiCoinvolti;
	
	private OttimizzazioneSoggettoDto ottimizzazioneSoggettoDtoPerModifiche;
	
	private List<Soggetto> distintiSoggettiCoinvolti;
	
	private List<SiacRModificaStatoFin> distintiSiacRModificaStatoCoinvolti;
	
	private List<SiacTModificaFin> distintiSiacTModificaCoinvolti;
	
	private List<SiacTMovgestTsDetModFin> distintiSiacTMovgestTsDetModFinCoinvolti;
	//SIAC-6865
	private List<Integer> uidsModificheImportoPreEsistentiValide;
	
	
	public List<SiacRMovgestTsSogclasseModFin> filtraSiacRMovgestTsSogclasseModFinBySiacRModificaStato(SiacRModificaStatoFin siacRModificaStato){
		List<SiacRMovgestTsSogclasseModFin> filtrati = new ArrayList<SiacRMovgestTsSogclasseModFin>();
		if(siacRModificaStato!=null && distintiSiacRMovgestTsSogclasseModFinCoinvolti!=null && distintiSiacRMovgestTsSogclasseModFinCoinvolti.size()>0){
			Integer idMovgest = siacRModificaStato.getModStatoRId();
			if(idMovgest!=null){
				for(SiacRMovgestTsSogclasseModFin it : distintiSiacRMovgestTsSogclasseModFinCoinvolti){
					if(it.getSiacTMovgestT()!=null && it.getSiacTMovgestT().getMovgestTsId()!=null && 
							it.getSiacRModificaStato().getModStatoRId().intValue()==idMovgest.intValue()){
						filtrati.add(it);
					}
				}
			}
		}
		filtrati = CommonUtil.soloValidiSiacTBase(filtrati, null);
		return filtrati;
	}
	
	public List<SiacRMovgestTsSogModFin> filtraSiacRMovgestTsSogModFinBySiacRModificaStato(SiacRModificaStatoFin siacRModificaStato){
		List<SiacRMovgestTsSogModFin> filtrati = new ArrayList<SiacRMovgestTsSogModFin>();
		if(siacRModificaStato!=null && distintiSiacRMovgestTsSogModFinCoinvolti!=null && distintiSiacRMovgestTsSogModFinCoinvolti.size()>0){
			Integer idMovgest = siacRModificaStato.getModStatoRId();
			if(idMovgest!=null){
				for(SiacRMovgestTsSogModFin it : distintiSiacRMovgestTsSogModFinCoinvolti){
					if(it.getSiacTMovgestT()!=null && it.getSiacTMovgestT().getMovgestTsId()!=null && 
							it.getSiacRModificaStato().getModStatoRId().intValue()==idMovgest.intValue()){
						filtrati.add(it);
					}
				}
			}
		}
		filtrati = CommonUtil.soloValidiSiacTBase(filtrati, null);
		return filtrati;
	}
	
	public List<SiacTMovgestTsDetModFin> filtraSiacTMovgestTsDetModFinBySiacRModificaStato(SiacRModificaStatoFin siacRModificaStato){
		List<SiacTMovgestTsDetModFin> filtrati = new ArrayList<SiacTMovgestTsDetModFin>();
		if(siacRModificaStato!=null && distintiSiacTMovgestTsDetModFinCoinvolti!=null && distintiSiacTMovgestTsDetModFinCoinvolti.size()>0){
			Integer idMovgest = siacRModificaStato.getModStatoRId();
			if(idMovgest!=null){
				for(SiacTMovgestTsDetModFin it : distintiSiacTMovgestTsDetModFinCoinvolti){
					if(it.getSiacTMovgestT()!=null && it.getSiacTMovgestT().getMovgestTsId()!=null && 
							it.getSiacRModificaStato().getModStatoRId().intValue()==idMovgest.intValue()){
						filtrati.add(it);
					}
				}
			}
		}
		filtrati = CommonUtil.soloValidiSiacTBase(filtrati, null);
		return filtrati;
	}
	
	public SiacTMovgestTsDetModFin getSiacTMovgestTsDetModFinValidoBySiacTModifica(SiacTModificaFin siacTModificaFin){
		SiacTMovgestTsDetModFin valido = null;
		List<SiacTMovgestTsDetModFin> filtrati = filtraSiacTMovgestTsDetModFinBySiacTModifica(siacTModificaFin);
		if(filtrati!=null && filtrati.size()>0){
			valido = filtrati.get(0);
		}
		return valido;
	}
	
	public List<SiacTMovgestTsDetModFin> filtraSiacTMovgestTsDetModFinBySiacTModifica(SiacTModificaFin siacTModificaFin){
		List<SiacTMovgestTsDetModFin> filtrati = null;
		List<SiacRModificaStatoFin> rmods = filtraSiacRModificaStatoFinBySiacTModifica(siacTModificaFin);
		rmods =  CommonUtil.soloValidiSiacTBase(rmods, null);
		if(rmods!=null && rmods.size()>0){
			SiacRModificaStatoFin valido = rmods.get(0);
			filtrati = filtraSiacTMovgestTsDetModFinBySiacRModificaStato(valido);
			filtrati = CommonUtil.soloValidiSiacTBase(filtrati, null);
		}
		return filtrati;
	}
	
	public List<SiacTMovgestTsDetModFin> filtraSiacTMovgestTsDetModFinBySiacTMovgestTsFin(SiacTMovgestTsFin siacTMovgestTs){
		List<SiacTMovgestTsDetModFin> filtrati = new ArrayList<SiacTMovgestTsDetModFin>();
		if(siacTMovgestTs!=null && distintiSiacTMovgestTsDetModFinCoinvolti!=null && distintiSiacTMovgestTsDetModFinCoinvolti.size()>0){
			Integer movgestTsId = siacTMovgestTs.getMovgestTsId();
			if(movgestTsId!=null){
				for(SiacTMovgestTsDetModFin it : distintiSiacTMovgestTsDetModFinCoinvolti){
					if(it.getSiacTMovgestT()!=null && it.getSiacTMovgestT().getMovgestTsId()!=null && 
							it.getSiacTMovgestT().getMovgestTsId().intValue()==movgestTsId.intValue()){
						filtrati.add(it);
					}
				}
			}
		}
		filtrati = CommonUtil.soloValidiSiacTBase(filtrati, null);
		return filtrati;
	}
	
	public List<SiacRModificaStatoFin> filtraSiacRModificaStatoFinBySiacTModifica(SiacTModificaFin siacTModificaFin){
		List<SiacRModificaStatoFin> filtrati = new ArrayList<SiacRModificaStatoFin>();
		if(siacTModificaFin!=null && distintiSiacRModificaStatoCoinvolti!=null && distintiSiacRModificaStatoCoinvolti.size()>0){
			Integer idModifica = siacTModificaFin.getModId();
			if(idModifica!=null){
				for(SiacRModificaStatoFin it : distintiSiacRModificaStatoCoinvolti){
					if(it.getSiacTModifica()!=null && it.getSiacTModifica().getModId()!=null && 
							it.getSiacTModifica().getModId().intValue()==idModifica.intValue()){
						filtrati.add(it);
					}
				}
			}
		}
		filtrati = CommonUtil.soloValidiSiacTBase(filtrati, null);
		return filtrati;
	}
	
	public List<SiacRMovgestTsSogModFin> filtraSiacRMovgestTsSogModFinBySiacTMovgestTsFin(SiacTMovgestTsFin siacTMovgestTs){
		List<SiacRMovgestTsSogModFin> filtrati = new ArrayList<SiacRMovgestTsSogModFin>();
		if(siacTMovgestTs!=null && distintiSiacRMovgestTsSogModFinCoinvolti!=null && distintiSiacRMovgestTsSogModFinCoinvolti.size()>0){
			Integer movgestTsId = siacTMovgestTs.getMovgestTsId();
			if(movgestTsId!=null){
				for(SiacRMovgestTsSogModFin it : distintiSiacRMovgestTsSogModFinCoinvolti){
					if(it.getSiacTMovgestT()!=null && it.getSiacTMovgestT().getMovgestTsId()!=null && 
							it.getSiacTMovgestT().getMovgestTsId().intValue()==movgestTsId.intValue()){
						filtrati.add(it);
					}
				}
			}
		}
		filtrati = CommonUtil.soloValidiSiacTBase(filtrati, null);
		return filtrati;
	}
	
	public List<SiacRMovgestTsSogclasseModFin> filtraSiacRMovgestTsSogclasseModFinBySiacTMovgestTsFin(SiacTMovgestTsFin siacTMovgestTs){
		List<SiacRMovgestTsSogclasseModFin> filtrati = new ArrayList<SiacRMovgestTsSogclasseModFin>();
		if(siacTMovgestTs!=null && distintiSiacRMovgestTsSogclasseModFinCoinvolti!=null && distintiSiacRMovgestTsSogclasseModFinCoinvolti.size()>0){
			Integer movgestTsId = siacTMovgestTs.getMovgestTsId();
			if(movgestTsId!=null){
				for(SiacRMovgestTsSogclasseModFin it : distintiSiacRMovgestTsSogclasseModFinCoinvolti){
					if(it.getSiacTMovgestT()!=null && it.getSiacTMovgestT().getMovgestTsId()!=null && 
							it.getSiacTMovgestT().getMovgestTsId().intValue()==movgestTsId.intValue()){
						filtrati.add(it);
					}
				}
			}
		}
		filtrati = CommonUtil.soloValidiSiacTBase(filtrati, null);
		return filtrati;
	}
	

	public List<SiacRMovgestTsSogModFin> getDistintiSiacRMovgestTsSogModFinCoinvolti() {
		return distintiSiacRMovgestTsSogModFinCoinvolti;
	}


	public void setDistintiSiacRMovgestTsSogModFinCoinvolti(
			List<SiacRMovgestTsSogModFin> distintiSiacRMovgestTsSogModFinCoinvolti) {
		this.distintiSiacRMovgestTsSogModFinCoinvolti = distintiSiacRMovgestTsSogModFinCoinvolti;
	}


	public List<SiacTSoggettoFin> getDistintiSiacTSoggettiCoinvolti() {
		return distintiSiacTSoggettiCoinvolti;
	}


	public void setDistintiSiacTSoggettiCoinvolti(
			List<SiacTSoggettoFin> distintiSiacTSoggettiCoinvolti) {
		this.distintiSiacTSoggettiCoinvolti = distintiSiacTSoggettiCoinvolti;
	}


	public OttimizzazioneSoggettoDto getOttimizzazioneSoggettoDtoPerModifiche() {
		return ottimizzazioneSoggettoDtoPerModifiche;
	}


	public void setOttimizzazioneSoggettoDtoPerModifiche(
			OttimizzazioneSoggettoDto ottimizzazioneSoggettoDtoPerModifiche) {
		this.ottimizzazioneSoggettoDtoPerModifiche = ottimizzazioneSoggettoDtoPerModifiche;
	}


	public List<Soggetto> getDistintiSoggettiCoinvolti() {
		return distintiSoggettiCoinvolti;
	}


	public void setDistintiSoggettiCoinvolti(
			List<Soggetto> distintiSoggettiCoinvolti) {
		this.distintiSoggettiCoinvolti = distintiSoggettiCoinvolti;
	}


	public List<SiacRModificaStatoFin> getDistintiSiacRModificaStatoCoinvolti() {
		return distintiSiacRModificaStatoCoinvolti;
	}


	public void setDistintiSiacRModificaStatoCoinvolti(
			List<SiacRModificaStatoFin> distintiSiacRModificaStatoCoinvolti) {
		this.distintiSiacRModificaStatoCoinvolti = distintiSiacRModificaStatoCoinvolti;
	}


	public List<SiacTModificaFin> getDistintiSiacTModificaCoinvolti() {
		return distintiSiacTModificaCoinvolti;
	}


	public void setDistintiSiacTModificaCoinvolti(
			List<SiacTModificaFin> distintiSiacTModificaCoinvolti) {
		this.distintiSiacTModificaCoinvolti = distintiSiacTModificaCoinvolti;
	}

	public List<SiacRMovgestTsSogclasseModFin> getDistintiSiacRMovgestTsSogclasseModFinCoinvolti() {
		return distintiSiacRMovgestTsSogclasseModFinCoinvolti;
	}

	public void setDistintiSiacRMovgestTsSogclasseModFinCoinvolti(
			List<SiacRMovgestTsSogclasseModFin> distintiSiacRMovgestTsSogclasseModFinCoinvolti) {
		this.distintiSiacRMovgestTsSogclasseModFinCoinvolti = distintiSiacRMovgestTsSogclasseModFinCoinvolti;
	}

	public List<SiacTMovgestTsDetModFin> getDistintiSiacTMovgestTsDetModFinCoinvolti() {
		return distintiSiacTMovgestTsDetModFinCoinvolti;
	}

	public void setDistintiSiacTMovgestTsDetModFinCoinvolti(
			List<SiacTMovgestTsDetModFin> distintiSiacTMovgestTsDetModFinCoinvolti) {
		this.distintiSiacTMovgestTsDetModFinCoinvolti = distintiSiacTMovgestTsDetModFinCoinvolti;
	}

	/**
	 * @return the uidsModificheImportoPreEsistentiValide
	 */
	public List<Integer> getUidsModificheImportoPreEsistentiValide() {
		return uidsModificheImportoPreEsistentiValide;
	}

	/**
	 * @param uidsModificheImportoPreEsistentiValide the uidsModificheImportoPreEsistentiValide to set
	 */
	public void setUidsModificheImportoPreEsistentiValide(List<Integer> uidsModificheImportoPreEsistentiValide) {
		this.uidsModificheImportoPreEsistentiValide = uidsModificheImportoPreEsistentiValide;
	}	
}
