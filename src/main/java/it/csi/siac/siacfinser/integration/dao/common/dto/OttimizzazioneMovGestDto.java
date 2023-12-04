/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTAttrEnum;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siacfinser.CommonUtil;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.StringUtilsFin;
import it.csi.siac.siacfinser.integration.entity.SiacRLiquidazioneMovgestFin;
import it.csi.siac.siacfinser.integration.entity.SiacRLiquidazioneOrdFin;
import it.csi.siac.siacfinser.integration.entity.SiacRLiquidazioneStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestBilElemFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestClassFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsAttoAmmFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsAttrFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsProgrammaFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsSogFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsSogclasseFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacROrdinativoStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacROrdinativoTsMovgestTFin;
import it.csi.siac.siacfinser.integration.entity.SiacRProgrammaAttrFin;
import it.csi.siac.siacfinser.integration.entity.SiacRVincoloAttrFin;
import it.csi.siac.siacfinser.integration.entity.SiacRVincoloBilElemFin;
import it.csi.siac.siacfinser.integration.entity.SiacTBilElemFin;
import it.csi.siac.siacfinser.integration.entity.SiacTLiquidazioneFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsDetFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsFin;
import it.csi.siac.siacfinser.integration.entity.SiacTOrdinativoFin;
import it.csi.siac.siacfinser.integration.entity.SiacTOrdinativoTFin;
import it.csi.siac.siacfinser.integration.entity.SiacTOrdinativoTsDetFin;
import it.csi.siac.siacfinser.integration.entity.SiacTProgrammaFin;
import it.csi.siac.siacfinser.integration.entity.SiacTSoggettoFin;
import it.csi.siac.siacfinser.integration.entity.SiacTVincoloFin;
import it.csi.siac.siacfinser.model.MovimentoGestione.AttributoMovimentoGestione;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

public class OttimizzazioneMovGestDto implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2573703973624140188L;
	
	//Quando il chiamante ha gia sotto mano il movimento per il quale intende invocare il ricercaMovimentoPk:
	private SiacTMovgestFin movimentoDaChiamante;
	//
	
	
	private OttimizzazioneModificheMovimentoGestioneDto ottimizzazioneModDto;
	
	private List<SiacTMovgestTsFin> distintiSiacTMovgestTsFinCoinvolti;
	private List<SiacTMovgestFin> distintiSiacTMovgestFinCoinvolti;

	private List<SiacTSoggettoFin> distintiSiacTSoggettiCoinvolti;
	
	private List<SiacRMovgestTsSogFin> distintiSiacRSoggettiCoinvolti;
	
	private List<Soggetto> distintiSoggettiCoinvolti;
	
	private List<SiacRMovgestTsAttrFin> distintiSiacRMovgestTsAttrCoinvolti;
	private List<SiacRMovgestClassFin> distintiSiacRMovgestClassCoinvolti ;
	private List<SiacRMovgestTsStatoFin> distintiSiacRMovgestTsStatoCoinvolti ;
	
	private List<SiacTMovgestTsDetFin> distintiSiacTMovgestTsDetCoinvolti;
	
	private List<SiacRMovgestTsSogclasseFin> distintiSiacRMovgestTsSogclasseCoinvolti;
	
	private List<SiacRMovgestTsProgrammaFin> distintiSiacRMovgestTsProgrammaCoinvolti;
	private List<SiacRProgrammaAttrFin> distintiSiacRProgrammaAttrFinCoinvolti;
	
	
	private List<SiacRMovgestTsAttoAmmFin> distintiSiacRMovgestTsAttoAmmCoinvolti;
	
	private List<SiacRMovgestBilElemFin> distintiSiacRMovgestBilElemCoinvolti;
	private List<SiacRVincoloBilElemFin> distintSiacRVincoloBilElemFinCoinvolti;
	private List<SiacRVincoloAttrFin> distintiSiacRVincoloAttrFinCoinvolti;
	
	private List<SiacROrdinativoTsMovgestTFin> distintiSiacROrdinativoTsMovgestTCoinvolti;
	
	private List<SiacRLiquidazioneMovgestFin> distintiSiacRLiquidazioneMovgestFinCoinvolti;
	
	private List<SiacTLiquidazioneFin> distintiSiacTLiquidazioneFinCoinvolti;
	private List<SiacRLiquidazioneStatoFin> distintiSiacRLiquidazioneStatoFinCoinvolti;
	private List<SiacRLiquidazioneOrdFin> distintiSiacRLiquidazioneOrdFinCoinvolti;
	private List<SiacTOrdinativoTFin> distintiSiacTOrdinativoTFinCoinvolti;
	private List<SiacTOrdinativoFin> distintiSiacTOrdinativoFinCoinvolti;
	private List<SiacTOrdinativoTsDetFin> distintiSiacTOrdinativoTsDetFinCoinvolti;
	private List<SiacROrdinativoStatoFin> distintiSiacROrdinativoStatoFinCoinvolti;
	
	private List<SiacRMovgestTsFin> distintiSiacRMovgestTsFinCoinvolti;
	
	private List<CodificaImportoDto> listaDisponibiliIncassare;
	private List<CodificaImportoDto> listaDisponibiliLiquidareDaFunction;
	
	private List<CodificaImportoDto> listaDisponibiliPagareDaFunction;
	
	
	private OttimizzazioneSoggettoDto ottimizzazioneSoggetti;
	
	//UTILITIES:
	
	public SiacTSoggettoFin getSoggettoByMovGestTsId(Integer movgestTsId){
		SiacTSoggettoFin soggetto = null;
		SiacRMovgestTsSogFin trovato = null;
		if(movgestTsId != null && distintiSiacRSoggettiCoinvolti != null && distintiSiacRSoggettiCoinvolti.size() > 0){
			for(SiacRMovgestTsSogFin it : distintiSiacRSoggettiCoinvolti){
				//SIAC-7619
				if(it.getSiacTMovgestT().getMovgestTsId().intValue() == movgestTsId.intValue() && it.isEntitaValida()){
					trovato = it;
					break;
				}
			}
		}
		if(trovato != null){
			soggetto = trovato.getSiacTSoggetto();
		}
		return soggetto;
	}
	
	public List<SiacRMovgestTsSogFin> filtraSiacRMovgestTsSogFinByMovGestTsId(Integer movgestTsId){
		List<SiacRMovgestTsSogFin> filtrati = new ArrayList<SiacRMovgestTsSogFin>();
		if(movgestTsId != null && distintiSiacRSoggettiCoinvolti != null && distintiSiacRSoggettiCoinvolti.size() > 0){
			for(SiacRMovgestTsSogFin it : distintiSiacRSoggettiCoinvolti){
				//SIAC-7619
				if(it.getSiacTMovgestT().getMovgestTsId().intValue() == movgestTsId.intValue()&& it.isEntitaValida()){
					filtrati.add(it);
				}
			}
		}
		return filtrati;
	}
	
	public List<SiacRMovgestTsSogclasseFin> filtraSiacRMovgestTsSogclasseFinByMovGestTsId(Integer movgestTsId){
		List<SiacRMovgestTsSogclasseFin> filtrati = new ArrayList<SiacRMovgestTsSogclasseFin>();
		if(movgestTsId != null && distintiSiacRMovgestTsSogclasseCoinvolti != null && distintiSiacRMovgestTsSogclasseCoinvolti.size()>0){
			for(SiacRMovgestTsSogclasseFin it : distintiSiacRMovgestTsSogclasseCoinvolti){
				//SIAC-7619
				if(it.getSiacTMovgestT().getMovgestTsId().intValue() == movgestTsId.intValue() && it.isEntitaValida()){
					filtrati.add(it);
				}
			}
		}
		return filtrati;
	}
	
	public List<SiacRMovgestBilElemFin> filtraSiacRMovgestBilElemFinBySiacTMovgest(SiacTMovgestFin siacTMovgestFin){
		List<SiacRMovgestBilElemFin> filtrati = new ArrayList<SiacRMovgestBilElemFin>();
		if(siacTMovgestFin != null){
			Integer id = siacTMovgestFin.getMovgestId();
			if(id!=null && distintiSiacRMovgestBilElemCoinvolti!=null && distintiSiacRMovgestBilElemCoinvolti.size()>0){
				for(SiacRMovgestBilElemFin it : distintiSiacRMovgestBilElemCoinvolti){
					if(it.getSiacTMovgest().getMovgestId().intValue()==id.intValue()){
						filtrati.add(it);
					}
				}
			}
		}
		return filtrati;
	}
	
	
	public List<SiacRProgrammaAttrFin> filtraSiacRProgrammaAttrFinBySiacTProgrammaFin(SiacTProgrammaFin siacTProgrammaFin){
		List<SiacRProgrammaAttrFin> filtrati = new ArrayList<SiacRProgrammaAttrFin>();
		if(siacTProgrammaFin != null){
			Integer id = siacTProgrammaFin.getProgrammaId();
			if(id != null && distintiSiacRProgrammaAttrFinCoinvolti != null && distintiSiacRProgrammaAttrFinCoinvolti.size() > 0){
				for(SiacRProgrammaAttrFin it : distintiSiacRProgrammaAttrFinCoinvolti){
					if(it.getSiacTProgramma().getProgrammaId().intValue() == id.intValue() && it.isEntitaValida()){
						filtrati.add(it);
					}
				}
			}
		}
		return filtrati;
	}
	
	public List<SiacRMovgestTsFin> filtraSiacRMovgestTsFinByMovgestTsA(Integer movgestTsId){
		List<SiacRMovgestTsFin> filtrati = new ArrayList<SiacRMovgestTsFin>();
		if(movgestTsId!=null && distintiSiacRMovgestTsFinCoinvolti!=null && distintiSiacRMovgestTsFinCoinvolti.size()>0){
			for(SiacRMovgestTsFin it : distintiSiacRMovgestTsFinCoinvolti){
				if(it.getSiacTMovgestTsA()!=null && it.getSiacTMovgestTsA().getMovgestTsId().intValue()==movgestTsId.intValue()){
					filtrati.add(it);
				}
			}
		}
		return filtrati;
	}
	
	public List<SiacRMovgestTsFin> filtraSiacRMovgestTsFinByMovgestTsB(Integer movgestTsId){
		List<SiacRMovgestTsFin> filtrati = new ArrayList<SiacRMovgestTsFin>();
		if(movgestTsId!=null && distintiSiacRMovgestTsFinCoinvolti!=null && distintiSiacRMovgestTsFinCoinvolti.size()>0){
			for(SiacRMovgestTsFin it : distintiSiacRMovgestTsFinCoinvolti){
				if(it.getSiacTMovgestTsB()!=null && it.getSiacTMovgestTsB().getMovgestTsId().intValue()==movgestTsId.intValue()){
					filtrati.add(it);
				}
			}
		}
		return filtrati;
	}
	
	public List<SiacRMovgestTsAttrFin> filtraSiacRMovgestTsAttrByMovgestTs(Integer movgestTsId){
		List<SiacRMovgestTsAttrFin> filtrati = new ArrayList<SiacRMovgestTsAttrFin>();
		if(movgestTsId!=null && distintiSiacRMovgestTsAttrCoinvolti!=null && distintiSiacRMovgestTsAttrCoinvolti.size()>0){
			for(SiacRMovgestTsAttrFin it : distintiSiacRMovgestTsAttrCoinvolti){
				if(it.getSiacTMovgestT().getMovgestTsId().intValue()==movgestTsId.intValue()){
					filtrati.add(it);
				}
			}
		}
		return filtrati;
	}
	
	public List<SiacRMovgestClassFin> filtraSiacRMovgestClassByMovgestTs(Integer movgestTsId){
		List<SiacRMovgestClassFin> filtrati = new ArrayList<SiacRMovgestClassFin>();
		if(movgestTsId!=null && distintiSiacRMovgestClassCoinvolti!=null && distintiSiacRMovgestClassCoinvolti.size()>0){
			for(SiacRMovgestClassFin it : distintiSiacRMovgestClassCoinvolti){
				if(it.getSiacTMovgestT().getMovgestTsId().intValue()==movgestTsId.intValue()){
					filtrati.add(it);
				}
			}
		}
		return filtrati;
	}
	
	public SiacRMovgestTsStatoFin getStatoValido(Integer movgestTsId){
		List<SiacRMovgestTsStatoFin> l = filtraSiacRMovgestTsStatoByMovgestTs(movgestTsId);
		List<SiacRMovgestTsStatoFin> sv = CommonUtil.soloValidiSiacTBase(l, null);
		return CommonUtil.getFirst(sv);
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
	
	public SiacTBilElemFin getSiacTBilElemFinValido(Integer movgestId){
		List<SiacRMovgestBilElemFin> l = filtraSiacRMovgestBilElemByMovgestTs(movgestId);
		List<SiacRMovgestBilElemFin> sv = CommonUtil.soloValidiSiacTBase(l, null);
		if(sv!=null && sv.size()>0){
			return CommonUtil.getFirst(sv).getSiacTBilElem();
		}
		return null;
	}
	
	public List<SiacRMovgestBilElemFin> filtraSiacRMovgestBilElemByMovgestTs(Integer movgestId){
		List<SiacRMovgestBilElemFin> filtrati = new ArrayList<SiacRMovgestBilElemFin>();
		if(movgestId!=null && distintiSiacRMovgestBilElemCoinvolti!=null && distintiSiacRMovgestBilElemCoinvolti.size()>0){
			for(SiacRMovgestBilElemFin it : distintiSiacRMovgestBilElemCoinvolti){
				if(it.getSiacTMovgest().getMovgestId().intValue()==movgestId.intValue()){
					filtrati.add(it);
				}
			}
		}
		return filtrati;
	}
	
	public boolean presenteFlagTrasferimentiVincolati(Integer elemId){
		boolean presenteFlagTrasferimentiVincolati = false;
		List<SiacRVincoloAttrFin> rVincoli = getRVincoloAttrValidiByCode(elemId, SiacTAttrEnum.FlagTrasferimentiVincolati.getCodice());
		if(rVincoli!=null && rVincoli.size()>0){
			for(SiacRVincoloAttrFin it: rVincoli){
				if(it.getBoolean_()!=null && it.getBoolean_().equals(CostantiFin.TRUE)){
					presenteFlagTrasferimentiVincolati = true;
					break;
				}
			}
		}
		return presenteFlagTrasferimentiVincolati;
	}
	
	public List<SiacRVincoloAttrFin> getRVincoloAttrValidiByCode(Integer elemId, String code){
		List<SiacRVincoloAttrFin> validi = null;
		if(elemId!=null && code!=null){
			List<SiacRVincoloBilElemFin> rVincoloBilElems = filtraSiacRVincoloBilElemFinByBilElem(elemId);
			List<SiacRVincoloBilElemFin> rVincoloBilElemsValidi = CommonUtil.soloValidiSiacTBase(rVincoloBilElems, null);
			if(rVincoloBilElemsValidi!=null && rVincoloBilElemsValidi.size()>0){
				List<SiacTVincoloFin> listaSiacTVincolo = estraiSiacTVincoloFinBySiacRVincoloBilElemFinCoinvolti(rVincoloBilElemsValidi);
				List<SiacTVincoloFin> listaSiacTVincoloValidi = CommonUtil.soloValidiSiacTBase(listaSiacTVincolo, null);
				List<SiacRVincoloAttrFin> siacRVincoloAttrFinByCode = filtraSiacRVincoloAttrFinByCodeAndVincoli(code,listaSiacTVincoloValidi);
				validi = CommonUtil.soloValidiSiacTBase(siacRVincoloAttrFinByCode, null);
			}
		}
		return validi;
	}
	
	
	public List<SiacRVincoloAttrFin> filtraSiacRVincoloAttrFinByCodeAndVincoli(String code,List<SiacTVincoloFin> vincoli){
		List<SiacRVincoloAttrFin> coinvolti = new ArrayList<SiacRVincoloAttrFin>();
	
		if(code!=null && vincoli!=null && vincoli.size()>0
				&& distintiSiacRVincoloAttrFinCoinvolti!=null && distintiSiacRVincoloAttrFinCoinvolti.size()>0){
			
			for(SiacRVincoloAttrFin it : distintiSiacRVincoloAttrFinCoinvolti){
				if(it!=null && it.getSiacTAttr()!=null && code.equals(it.getSiacTAttr().getAttrCode())){
					//ok il code trova riscontro, deve pero' anche appartenere ad uno dei vincoli indicati:
					for(SiacTVincoloFin vincoloIt : vincoli){
						if(vincoloIt!=null && vincoloIt.getVincoloId()!=null 
								&& vincoloIt.getVincoloId().intValue() == it.getSiacTVincolo().getUid()){
							coinvolti.add(it);
						}
					}
				}
			}
		}
		coinvolti = CommonUtil.ritornaSoloDistintiByUid(coinvolti);
		return coinvolti;
	}
	
	public List<SiacRVincoloBilElemFin> filtraSiacRVincoloBilElemFinByBilElem(Integer elemId){
		List<SiacRVincoloBilElemFin> filtrati = new ArrayList<SiacRVincoloBilElemFin>();
		if(elemId!=null && distintSiacRVincoloBilElemFinCoinvolti!=null && distintSiacRVincoloBilElemFinCoinvolti.size()>0){
			for(SiacRVincoloBilElemFin it : distintSiacRVincoloBilElemFinCoinvolti){
				if(it.getSiacTBilElem().getElemId().intValue()==elemId.intValue()){
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
	
	public List<SiacRMovgestTsProgrammaFin> filtraSiacRMovgestTsProgrammaByMovgestTs(Integer movgestTsId){
		List<SiacRMovgestTsProgrammaFin> filtrati = new ArrayList<SiacRMovgestTsProgrammaFin>();
		if(movgestTsId!=null && distintiSiacRMovgestTsProgrammaCoinvolti!=null && distintiSiacRMovgestTsProgrammaCoinvolti.size()>0){
			for(SiacRMovgestTsProgrammaFin it : distintiSiacRMovgestTsProgrammaCoinvolti){
				if(it.getSiacTMovgestT().getMovgestTsId().intValue()==movgestTsId.intValue()){
					filtrati.add(it);
				}
			}
		}
		return filtrati;
	}
	
	public List<SiacRMovgestTsAttoAmmFin> filtraSiacRMovgestTsAttoAmmByMovgestTs(Integer movgestTsId){
		List<SiacRMovgestTsAttoAmmFin> filtrati = new ArrayList<SiacRMovgestTsAttoAmmFin>();
		if(movgestTsId!=null && distintiSiacRMovgestTsAttoAmmCoinvolti!=null && distintiSiacRMovgestTsAttoAmmCoinvolti.size()>0){
			for(SiacRMovgestTsAttoAmmFin it : distintiSiacRMovgestTsAttoAmmCoinvolti){
				if(it.getSiacTMovgestT().getMovgestTsId().intValue()==movgestTsId.intValue()){
					filtrati.add(it);
				}
			}
		}
		return filtrati;
	}
	
	public List<SiacRMovgestBilElemFin> filtraSiacRMovgestBilElemByMovgest(Integer movgestId){
		List<SiacRMovgestBilElemFin> filtrati = new ArrayList<SiacRMovgestBilElemFin>();
		if(movgestId!=null && distintiSiacRMovgestBilElemCoinvolti!=null && distintiSiacRMovgestBilElemCoinvolti.size()>0){
			for(SiacRMovgestBilElemFin it : distintiSiacRMovgestBilElemCoinvolti){
				if(it.getSiacTMovgest().getMovgestId().intValue()==movgestId.intValue()){
					filtrati.add(it);
				}
			}
		}
		return filtrati;
	}
	
	public List<SiacRLiquidazioneMovgestFin> filtraSiacRLiquidazioneMovgestFinByMovgestTs(Integer movgestTsId){
		List<SiacRLiquidazioneMovgestFin> filtrati = new ArrayList<SiacRLiquidazioneMovgestFin>();
		if(movgestTsId!=null && distintiSiacRLiquidazioneMovgestFinCoinvolti!=null && distintiSiacRLiquidazioneMovgestFinCoinvolti.size()>0){
			for(SiacRLiquidazioneMovgestFin it : distintiSiacRLiquidazioneMovgestFinCoinvolti){
				if(it.getSiacTMovgestTs().getMovgestTsId().intValue()==movgestTsId.intValue()){
					filtrati.add(it);
				}
			}
		}
		return filtrati;
	}
	
	public List<SiacRLiquidazioneStatoFin> filtraSiacRLiquidazioneStatoFinBySiacTLiquidazioneFin(SiacTLiquidazioneFin siacTLiquidazioneFin){
		List<SiacRLiquidazioneStatoFin> filtrati = new ArrayList<SiacRLiquidazioneStatoFin>();
		if(siacTLiquidazioneFin!=null){
			Integer liqId = siacTLiquidazioneFin.getLiqId();
			if(liqId!=null && distintiSiacRLiquidazioneStatoFinCoinvolti!=null && distintiSiacRLiquidazioneStatoFinCoinvolti.size()>0){
				for(SiacRLiquidazioneStatoFin it : distintiSiacRLiquidazioneStatoFinCoinvolti){
					if(it.getSiacTLiquidazione().getLiqId().intValue()==liqId.intValue()){
						filtrati.add(it);
					}
				}
			}
		}
		return filtrati;
	}
	
	public List<SiacRLiquidazioneOrdFin> filtraSiacRLiquidazioneOrdFinBySiacTLiquidazioneFin(SiacTLiquidazioneFin siacTLiquidazioneFin){
		List<SiacRLiquidazioneOrdFin> filtrati = new ArrayList<SiacRLiquidazioneOrdFin>();
		if(siacTLiquidazioneFin!=null){
			Integer liqId = siacTLiquidazioneFin.getLiqId();
			if(liqId!=null && distintiSiacRLiquidazioneOrdFinCoinvolti!=null && distintiSiacRLiquidazioneOrdFinCoinvolti.size()>0){
				for(SiacRLiquidazioneOrdFin it : distintiSiacRLiquidazioneOrdFinCoinvolti){
					if(it.getSiacTLiquidazione().getLiqId().intValue()==liqId.intValue()){
						filtrati.add(it);
					}
				}
			}
		}
		return filtrati;
	}
	
	public List<SiacROrdinativoStatoFin> filtraSiacROrdinativoStatoFinBySiacTOrdinativoFin(SiacTOrdinativoFin siacTOrdinativo){
		List<SiacROrdinativoStatoFin> filtrati = new ArrayList<SiacROrdinativoStatoFin>();
		if(siacTOrdinativo!=null){
			Integer ordId = siacTOrdinativo.getOrdId();
			if(ordId!=null && distintiSiacROrdinativoStatoFinCoinvolti!=null && distintiSiacROrdinativoStatoFinCoinvolti.size()>0){
				for(SiacROrdinativoStatoFin it : distintiSiacROrdinativoStatoFinCoinvolti){
					if(it.getSiacTOrdinativo().getOrdId().intValue()==ordId.intValue()){
						filtrati.add(it);
					}
				}
			}
		}
		return filtrati;
	}
	
	public List<SiacTOrdinativoTsDetFin> filtraSiacTOrdinativoTsDetFinBySiacTOrdinativoTFin(SiacTOrdinativoTFin siacTOrdinativoTFin){
		List<SiacTOrdinativoTsDetFin> filtrati = new ArrayList<SiacTOrdinativoTsDetFin>();
		if(siacTOrdinativoTFin!=null){
			Integer ordTsId = siacTOrdinativoTFin.getOrdTsId();
			if(ordTsId!=null && distintiSiacTOrdinativoTsDetFinCoinvolti!=null && distintiSiacTOrdinativoTsDetFinCoinvolti.size()>0){
				for(SiacTOrdinativoTsDetFin it : distintiSiacTOrdinativoTsDetFinCoinvolti){
					if(it.getSiacTOrdinativoT().getOrdTsId().intValue()==ordTsId.intValue()){
						filtrati.add(it);
					}
				}
			}
		}
		return filtrati;
	}
	 
	
	public List<SiacRMovgestTsSogclasseFin> filtraSiacRMovgestTsSogclasseByMovgestTs(Integer movgestTsId){
		List<SiacRMovgestTsSogclasseFin> filtrati = new ArrayList<SiacRMovgestTsSogclasseFin>();
		if(movgestTsId!=null && distintiSiacRMovgestTsSogclasseCoinvolti!=null && distintiSiacRMovgestTsSogclasseCoinvolti.size()>0){
			for(SiacRMovgestTsSogclasseFin it : distintiSiacRMovgestTsSogclasseCoinvolti){
				if(it.getSiacTMovgestT().getMovgestTsId().intValue()==movgestTsId.intValue()){
					filtrati.add(it);
				}
			}
		}
		return filtrati;
	}
	
	public List<SiacROrdinativoTsMovgestTFin> filtraSiacROrdinativoTsMovgestTByMovgestTs(Integer movgestTsId){
		List<SiacROrdinativoTsMovgestTFin> filtrati = new ArrayList<SiacROrdinativoTsMovgestTFin>();
		if(movgestTsId!=null && distintiSiacROrdinativoTsMovgestTCoinvolti!=null && distintiSiacROrdinativoTsMovgestTCoinvolti.size()>0){
			for(SiacROrdinativoTsMovgestTFin it : distintiSiacROrdinativoTsMovgestTCoinvolti){
				if(it.getSiacTMovgestTs().getMovgestTsId().intValue()==movgestTsId.intValue()){
					filtrati.add(it);
				}
			}
		}
		return filtrati;
	}
	
	public List<SiacTMovgestTsFin> filtraSiacTMovgestTsFinByIdPadre(Integer idPadre){
		List<SiacTMovgestTsFin> filtrati = new ArrayList<SiacTMovgestTsFin>();
		if(idPadre!=null && distintiSiacTMovgestTsFinCoinvolti!=null && distintiSiacTMovgestTsFinCoinvolti.size()>0){
			for(SiacTMovgestTsFin it : distintiSiacTMovgestTsFinCoinvolti){
				if(it.getMovgestTsIdPadre()!=null && it.getMovgestTsIdPadre().intValue()==idPadre.intValue()){
					filtrati.add(it);
				}
			}
		}
		return filtrati;
	}
	
	public SiacTMovgestTsFin getSiacTMovgestTsFinById(Integer movgestTsId){
		return CommonUtil.getByIdSiacTBase(distintiSiacTMovgestTsFinCoinvolti, movgestTsId);
	}
	
	public SiacTMovgestTsFin getTestataByMovgestId(Integer movgestId){
		SiacTMovgestTsFin trovato = null;
		if(movgestId!=null && distintiSiacTMovgestTsFinCoinvolti!=null && distintiSiacTMovgestTsFinCoinvolti.size()>0){
			for(SiacTMovgestTsFin it : distintiSiacTMovgestTsFinCoinvolti){
				if(it.getMovgestTsIdPadre()==null
						&& it.getSiacTMovgest()!=null && it.getSiacTMovgest().getMovgestId()!=null
						&& it.getSiacTMovgest().getMovgestId().intValue()==movgestId.intValue()){
					trovato=it;
					break;
				}
			}
		}
		return trovato;
	}
	
	public List<SiacTMovgestTsFin> getListaSubByMovgestId(Integer movgestId){
		List<SiacTMovgestTsFin> trovati = null;
		if(movgestId!=null && distintiSiacTMovgestTsFinCoinvolti!=null && distintiSiacTMovgestTsFinCoinvolti.size()>0){
			trovati = new ArrayList<SiacTMovgestTsFin>();
			for(SiacTMovgestTsFin it : distintiSiacTMovgestTsFinCoinvolti){
				if(it.getMovgestTsIdPadre()!=null
						&& it.getSiacTMovgest()!=null && it.getSiacTMovgest().getMovgestId()!=null
						&& it.getSiacTMovgest().getMovgestId().intValue()==movgestId.intValue()){
					trovati.add(it);
				}
			}
		}
		trovati = CommonUtil.ritornaSoloDistintiByUid(trovati);
		return trovati;
	}
	
	public List<SiacTMovgestTsFin> getTestataPiuListaSub(SiacTMovgestFin siacTMovgest){
		
		List<SiacTMovgestTsFin> listaSiacTMovgestTs = null;
		
		if(siacTMovgest!=null && siacTMovgest.getMovgestId()!=null){
			
			listaSiacTMovgestTs = new ArrayList<SiacTMovgestTsFin>();
			
			Integer movgestId = siacTMovgest.getMovgestId();
			SiacTMovgestTsFin testata = getTestataByMovgestId(movgestId);
			List<SiacTMovgestTsFin> subs = getListaSubByMovgestId(movgestId);
			
			listaSiacTMovgestTs.add(testata);
			listaSiacTMovgestTs.addAll(subs);
			
			listaSiacTMovgestTs = CommonUtil.ritornaSoloDistintiByUid(listaSiacTMovgestTs);
			
		}
		
	  //Termino restituendo l'oggetto di ritorno: 
	  return listaSiacTMovgestTs;
	 }

	
	
	/**
	 * Per richiedere quelli in uno stato preciso (es. voglio solo i validi oppure voglio solo i provvisori)
	 * @param statoRichiesto
	 * @return
	 */
	public List<SiacTMovgestTsFin> filtraSiacTMovgestTsFinByStato(String statoRichiesto,List<SiacTMovgestTsFin> listaDaFiltrare){
		List<SiacTMovgestTsFin> listaSubInStatoRichiesto = new ArrayList<SiacTMovgestTsFin>();
		if(listaDaFiltrare!=null && listaDaFiltrare.size()>0){
			for(SiacTMovgestTsFin subIterato : listaDaFiltrare){
				if(subIterato!=null){
					String statoCode = estraiStatoCode(subIterato.getMovgestTsId());
					if(statoCode.equals(statoRichiesto)){
						SiacTMovgestTsFin daAggiungere = subIterato;
						listaSubInStatoRichiesto.add(daAggiungere);
					}
				}
			}
		}
		return listaSubInStatoRichiesto;
	}
	
	/**
	 * Per richiedere tutti tranne quelli nello stato indicato (caso tipico: li voglio tutti tranne gli annullati) 
	 * @param statoDaEscludere
	 * @return
	 */
	public List<SiacTMovgestTsFin> filtraSiacTMovgestTsFinTranneLoStatoIndicato(String statoDaEscludere, List<SiacTMovgestTsFin> listaDaFiltrare){
		List<SiacTMovgestTsFin> listaSubInStatoDiversoDaRichiesto = new ArrayList<SiacTMovgestTsFin>();
		if(listaDaFiltrare!=null && listaDaFiltrare.size()>0){
			for(SiacTMovgestTsFin subIterato : listaDaFiltrare){
				if(subIterato!=null){
					String statoCode = estraiStatoCode(subIterato.getMovgestTsId());
					if(!statoCode.equals(statoDaEscludere)){
						SiacTMovgestTsFin daAggiungere = subIterato;
						listaSubInStatoDiversoDaRichiesto.add(daAggiungere);
					}
				}
			}
		}
		return listaSubInStatoDiversoDaRichiesto;
	}
	
	public ClassificatoreGenerico estraiAttributoTClass(Integer movgestTsId, String classTipoCode){
		ClassificatoreGenerico tipo = null;
		List<SiacRMovgestClassFin> lista = filtraSiacRMovgestClassByMovgestTs(movgestTsId);
		for(SiacRMovgestClassFin siacRMovgestClass : lista){
			if(null!=siacRMovgestClass && siacRMovgestClass.getDataFineValidita()==null){
				if(siacRMovgestClass.getSiacTClass().getSiacDClassTipo().getClassifTipoCode().equalsIgnoreCase(classTipoCode)){
					ClassificatoreGenerico trovato = new ClassificatoreGenerico();
					trovato.setUid(siacRMovgestClass.getSiacTClass().getClassifId());
					trovato.setCodice(siacRMovgestClass.getSiacTClass().getClassifCode());
					trovato.setDescrizione(siacRMovgestClass.getSiacTClass().getClassifDesc());
					tipo = trovato;
					break;
				}
			}									
		}
		return tipo;
	}
	
	public ClassificatoreGenerico estraiAttributoTClass(Integer movgestTsId,  List<String> classTipoCodeMultiplo){
		ClassificatoreGenerico tipo = null;
		List<SiacRMovgestClassFin> lista = filtraSiacRMovgestClassByMovgestTs(movgestTsId);
		for(SiacRMovgestClassFin siacRMovgestClass : lista){
			if(null!=siacRMovgestClass && siacRMovgestClass.getDataFineValidita()==null){
				if(StringUtilsFin.contenutoIn(siacRMovgestClass.getSiacTClass().getSiacDClassTipo().getClassifTipoCode(), classTipoCodeMultiplo)){	
					ClassificatoreGenerico trovato = new ClassificatoreGenerico();
					trovato.setUid(siacRMovgestClass.getSiacTClass().getClassifId());
					trovato.setCodice(siacRMovgestClass.getSiacTClass().getClassifCode());
					trovato.setDescrizione(siacRMovgestClass.getSiacTClass().getClassifDesc());
					tipo = trovato;
					break;
				}
			}									
		}
		return tipo;
	}
	
	/**
	 * tipoImporto va valorizzato con CostantiFin.MOVGEST_TS_DET_TIPO_INIZIALE / CostantiFin.MOVGEST_TS_DET_TIPO_ATTUALE
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
	
	public Date estraiStatoDataInizioValidata(Integer movgestTsId){
		List<SiacRMovgestTsStatoFin> lista = filtraSiacRMovgestTsStatoByMovgestTs(movgestTsId);
		Date data = null;
		for(SiacRMovgestTsStatoFin statoIt : lista){
			if(null!=statoIt && statoIt.getDataFineValidita()==null && statoIt.getSiacDMovgestStato()!=null){
				data = statoIt.getDataInizioValidita();
				break;
			}		
		}
		return data;
	}
	
	public String estraiStatoDescr(Integer movgestTsId){
		List<SiacRMovgestTsStatoFin> lista = filtraSiacRMovgestTsStatoByMovgestTs(movgestTsId);
		String statoCode = "";
		for(SiacRMovgestTsStatoFin statoIt : lista){
			if(null!=statoIt && statoIt.getDataFineValidita()==null && statoIt.getSiacDMovgestStato()!=null){
				statoCode = statoIt.getSiacDMovgestStato().getMovgestStatoDesc();
					break;
			}		
		}
		return statoCode;
	}
	
	public BigDecimal estraiDisponibileIncassare(Integer movgestTsId){
		BigDecimal disp = BigDecimal.ZERO;
		if(movgestTsId!=null && listaDisponibiliIncassare!=null && listaDisponibiliIncassare.size()>0){
			for(CodificaImportoDto it : listaDisponibiliIncassare){
				if(it!=null && it.getImporto()!=null && it.getIdOggetto().intValue()==movgestTsId.intValue()){
					return it.getImporto();
				}
			}
		}
		return disp;
	}
	
	public BigDecimal estraiDisponibileLiquidareDaFunction(Integer movgestTsId){
		BigDecimal disp = BigDecimal.ZERO;
		if(movgestTsId!=null && listaDisponibiliLiquidareDaFunction!=null && listaDisponibiliLiquidareDaFunction.size()>0){
			for(CodificaImportoDto it : listaDisponibiliLiquidareDaFunction){
				if(it!=null && it.getImporto()!=null && it.getIdOggetto().intValue()==movgestTsId.intValue()){
					return it.getImporto();
				}
			}
		}
		return disp;
	}
	
	public BigDecimal estraiDisponibilePagareDaFunction(Integer movgestTsId){
		BigDecimal disp = BigDecimal.ZERO;
		if(movgestTsId!=null && listaDisponibiliPagareDaFunction!=null && listaDisponibiliPagareDaFunction.size()>0){
			for(CodificaImportoDto it : listaDisponibiliPagareDaFunction){
				if(it!=null && it.getImporto()!=null && it.getIdOggetto().intValue()==movgestTsId.intValue()){
					return it.getImporto();
				}
			}
		}
		return disp;
	}
	
	public String getRMovgestTsAttr(SiacTMovgestTsFin itSubs,AttributoMovimentoGestione richiesto){
		List<SiacRMovgestTsAttrFin> listaSiacRMovgestTsAttr =  filtraSiacRMovgestTsAttrByMovgestTs(itSubs.getMovgestTsId());
		String testo = null;
		for(SiacRMovgestTsAttrFin siacRMovgestTsAttr : listaSiacRMovgestTsAttr){
			if(null!=siacRMovgestTsAttr && siacRMovgestTsAttr.getDataFineValidita()==null){
				String codiceAttributo = siacRMovgestTsAttr.getSiacTAttr().getAttrCode();
				AttributoMovimentoGestione attributoMovimentoGestione = CostantiFin.attributoMovimentoGestioneStringToEnum(codiceAttributo);
				
				boolean trovato = false;
				if(richiesto.equals(attributoMovimentoGestione)){
					if(siacRMovgestTsAttr.getTesto() != null){
						if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
							testo = siacRMovgestTsAttr.getTesto();
							trovato = true;
						}
					}
				}
				if(trovato){
					break;
				}
				
			}
		}
		return testo;
	}
	
	public boolean getRMovgestTsAttrBool(SiacTMovgestTsFin itSubs,AttributoMovimentoGestione richiesto){
		List<SiacRMovgestTsAttrFin> listaSiacRMovgestTsAttr =  filtraSiacRMovgestTsAttrByMovgestTs(itSubs.getMovgestTsId());
		boolean valoreBooleano = false;
		for(SiacRMovgestTsAttrFin siacRMovgestTsAttr : listaSiacRMovgestTsAttr){
			if(null!=siacRMovgestTsAttr && siacRMovgestTsAttr.getDataFineValidita()==null){
				String codiceAttributo = siacRMovgestTsAttr.getSiacTAttr().getAttrCode();
				AttributoMovimentoGestione attributoMovimentoGestione = CostantiFin.attributoMovimentoGestioneStringToEnum(codiceAttributo);
				
				boolean trovato = false;
				if(richiesto.equals(attributoMovimentoGestione)){
					if(siacRMovgestTsAttr.getBoolean_() != null){
						if(CostantiFin.TRUE.equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_())){
							valoreBooleano = true;
						}else {
							valoreBooleano = false;
						}
						trovato = true;
					}
				}
				
				if(trovato){
					break;
				}
				
			}
		}
		return valoreBooleano;
	}
	
	public List<SiacTMovgestFin> estraiSiacTMovgestFinByDistintiSiacTMovgestTsFinCoinvolti(){
		List<SiacTMovgestFin> coinvolti = new ArrayList<SiacTMovgestFin>();
		if(distintiSiacTMovgestTsFinCoinvolti!=null && distintiSiacTMovgestTsFinCoinvolti.size()>0){
			for(SiacTMovgestTsFin it : distintiSiacTMovgestTsFinCoinvolti){
				if(it!=null && it.getSiacTMovgest()!=null){
					coinvolti.add(it.getSiacTMovgest());
				}
			}
		}
		coinvolti = CommonUtil.ritornaSoloDistintiByUid(coinvolti);
		return coinvolti;
	}
	
	public List<SiacTMovgestFin> estraiSiacTMovgestFinBySiacRMovgestTsFinCoinvolti(){
		List<SiacTMovgestFin> coinvolti = new ArrayList<SiacTMovgestFin>();
		List<SiacTMovgestTsFin> distintiSiacTMovgestTsFin = estraiDistintiSiacTMovgestTsFinBySiacRMovgestTsFinCoinvolti();
		if(distintiSiacTMovgestTsFin!=null && distintiSiacTMovgestTsFin.size()>0){
			for(SiacTMovgestTsFin it : distintiSiacTMovgestTsFin){
				if(it!=null && it.getSiacTMovgest()!=null){
					coinvolti.add(it.getSiacTMovgest());
				}
			}
		}
		coinvolti = CommonUtil.ritornaSoloDistintiByUid(coinvolti);
		return coinvolti;
	}
	
	/**
	 * Ritorna solo gli impegni --> sono gli elementi B
	 * @return
	 */
	public List<SiacTMovgestFin> estraiDistintiSiacTMovgestFinImpegniBySiacRMovgestTsFinCoinvolti(){
		List<SiacTMovgestFin> coinvolti = new ArrayList<SiacTMovgestFin>();
		List<SiacTMovgestTsFin> coinvoltiTs = estraiDistintiSiacTMovgestTsFinImpegniBySiacRMovgestTsFinCoinvolti();
		if(coinvoltiTs!=null && coinvoltiTs.size()>0){
			for(SiacTMovgestTsFin it : coinvoltiTs){
				if(it!=null && it.getSiacTMovgest()!=null){
					coinvolti.add(it.getSiacTMovgest());
				}
			}
		}
		coinvolti = CommonUtil.ritornaSoloDistintiByUid(coinvolti);
		return coinvolti;
	}
	
	/**
	 * Ritorna solo gli impegni --> sono gli elementi B
	 * @return
	 */
	public List<SiacTMovgestTsFin> estraiDistintiSiacTMovgestTsFinImpegniBySiacRMovgestTsFinCoinvolti(){
		List<SiacTMovgestTsFin> coinvolti = new ArrayList<SiacTMovgestTsFin>();
		if(distintiSiacRMovgestTsFinCoinvolti!=null && distintiSiacRMovgestTsFinCoinvolti.size()>0){
			for(SiacRMovgestTsFin it : distintiSiacRMovgestTsFinCoinvolti){
				if(it!=null && it.getSiacTMovgestTsB()!=null){
					coinvolti.add(it.getSiacTMovgestTsB());
				}
			}
		}
		coinvolti = CommonUtil.ritornaSoloDistintiByUid(coinvolti);
		return coinvolti;
	}
	
	/**
	 * Ritorna solo gli accertamenti --> sono gli elementi A
	 * @return
	 */
	public List<SiacTMovgestFin> estraiDistintiSiacTMovgestFinAccertamentiBySiacRMovgestTsFinCoinvolti(){
		List<SiacTMovgestFin> coinvolti = new ArrayList<SiacTMovgestFin>();
		List<SiacTMovgestTsFin> coinvoltiTs = estraiDistintiSiacTMovgestTsFinAccertamentiBySiacRMovgestTsFinCoinvolti();
		if(coinvoltiTs!=null && coinvoltiTs.size()>0){
			for(SiacTMovgestTsFin it : coinvoltiTs){
				if(it!=null && it.getSiacTMovgest()!=null){
					coinvolti.add(it.getSiacTMovgest());
				}
			}
		}
		coinvolti = CommonUtil.ritornaSoloDistintiByUid(coinvolti);
		return coinvolti;
	}
	
	/**
	 * Ritorna solo gli accertamenti --> sono gli elementi A
	 * @return
	 */
	public List<SiacTMovgestTsFin> estraiDistintiSiacTMovgestTsFinAccertamentiBySiacRMovgestTsFinCoinvolti(){
		List<SiacTMovgestTsFin> coinvolti = new ArrayList<SiacTMovgestTsFin>();
		if(distintiSiacRMovgestTsFinCoinvolti!=null && distintiSiacRMovgestTsFinCoinvolti.size()>0){
			for(SiacRMovgestTsFin it : distintiSiacRMovgestTsFinCoinvolti){
				if(it!=null && it.getSiacTMovgestTsA()!=null){
					coinvolti.add(it.getSiacTMovgestTsA());
				}
			}
		}
		coinvolti = CommonUtil.ritornaSoloDistintiByUid(coinvolti);
		return coinvolti;
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
		coinvolti = CommonUtil.ritornaSoloDistintiByUid(coinvolti);
		return coinvolti;
	}
	
	
	public List<SiacTBilElemFin> estraiSiacTBilElemFinBySiacRMovgestBilElemFinCoinvolti(){
		List<SiacTBilElemFin> coinvolti = new ArrayList<SiacTBilElemFin>();
		if(distintiSiacRMovgestBilElemCoinvolti!=null && distintiSiacRMovgestBilElemCoinvolti.size()>0){
			for(SiacRMovgestBilElemFin it : distintiSiacRMovgestBilElemCoinvolti){
				if(it!=null && it.getSiacTBilElem()!=null){
					coinvolti.add(it.getSiacTBilElem());
				}
			}
		}
		coinvolti = CommonUtil.ritornaSoloDistintiByUid(coinvolti);
		return coinvolti;
	}
	
	public List<SiacTVincoloFin> estraiSiacTVincoloFinBySiacRVincoloBilElemFinCoinvolti(){
		List<SiacTVincoloFin> coinvolti = new ArrayList<SiacTVincoloFin>();
		if(distintSiacRVincoloBilElemFinCoinvolti!=null && distintSiacRVincoloBilElemFinCoinvolti.size()>0){
			for(SiacRVincoloBilElemFin it : distintSiacRVincoloBilElemFinCoinvolti){
				if(it!=null && it.getSiacTVincolo()!=null){
					coinvolti.add(it.getSiacTVincolo());
				}
			}
		}
		coinvolti = CommonUtil.ritornaSoloDistintiByUid(coinvolti);
		return coinvolti;
	}
	
	public List<SiacTVincoloFin> estraiSiacTVincoloFinBySiacRVincoloBilElemFinCoinvolti(List<SiacRVincoloBilElemFin> lista){
		List<SiacTVincoloFin> coinvolti = new ArrayList<SiacTVincoloFin>();
		if(lista!=null && lista.size()>0){
			for(SiacRVincoloBilElemFin it : lista){
				if(it!=null && it.getSiacTVincolo()!=null){
					coinvolti.add(it.getSiacTVincolo());
				}
			}
		}
		coinvolti = CommonUtil.ritornaSoloDistintiByUid(coinvolti);
		return coinvolti;
	}
	
	//GETTER AND SETTER:
	
	public List<SiacTSoggettoFin> getDistintiSiacTSoggettiCoinvolti() {
		return distintiSiacTSoggettiCoinvolti;
	}
	public void setDistintiSiacTSoggettiCoinvolti(
			List<SiacTSoggettoFin> distintiSiacTSoggettiCoinvolti) {
		this.distintiSiacTSoggettiCoinvolti = distintiSiacTSoggettiCoinvolti;
	}
	public List<SiacRMovgestTsSogFin> getDistintiSiacRSoggettiCoinvolti() {
		return distintiSiacRSoggettiCoinvolti;
	}
	public void setDistintiSiacRSoggettiCoinvolti(
			List<SiacRMovgestTsSogFin> distintiSiacRSoggettiCoinvolti) {
		this.distintiSiacRSoggettiCoinvolti = distintiSiacRSoggettiCoinvolti;
	}
	public List<Soggetto> getDistintiSoggettiCoinvolti() {
		return distintiSoggettiCoinvolti;
	}
	public void setDistintiSoggettiCoinvolti(
			List<Soggetto> distintiSoggettiCoinvolti) {
		this.distintiSoggettiCoinvolti = distintiSoggettiCoinvolti;
	}
	public List<SiacRMovgestTsAttrFin> getDistintiSiacRMovgestTsAttrCoinvolti() {
		return distintiSiacRMovgestTsAttrCoinvolti;
	}
	public void setDistintiSiacRMovgestTsAttrCoinvolti(
			List<SiacRMovgestTsAttrFin> distintiSiacRMovgestTsAttrCoinvolti) {
		this.distintiSiacRMovgestTsAttrCoinvolti = distintiSiacRMovgestTsAttrCoinvolti;
	}
	public List<SiacRMovgestClassFin> getDistintiSiacRMovgestClassCoinvolti() {
		return distintiSiacRMovgestClassCoinvolti;
	}
	public void setDistintiSiacRMovgestClassCoinvolti(
			List<SiacRMovgestClassFin> distintiSiacRMovgestClassCoinvolti) {
		this.distintiSiacRMovgestClassCoinvolti = distintiSiacRMovgestClassCoinvolti;
	}

	public List<SiacTMovgestTsDetFin> getDistintiSiacTMovgestTsDetCoinvolti() {
		return distintiSiacTMovgestTsDetCoinvolti;
	}

	public void setDistintiSiacTMovgestTsDetCoinvolti(
			List<SiacTMovgestTsDetFin> distintiSiacTMovgestTsDetCoinvolti) {
		this.distintiSiacTMovgestTsDetCoinvolti = distintiSiacTMovgestTsDetCoinvolti;
	}

	public List<SiacRMovgestTsSogclasseFin> getDistintiSiacRMovgestTsSogclasseCoinvolti() {
		return distintiSiacRMovgestTsSogclasseCoinvolti;
	}

	public void setDistintiSiacRMovgestTsSogclasseCoinvolti(
			List<SiacRMovgestTsSogclasseFin> distintiSiacRMovgestTsSogclasseCoinvolti) {
		this.distintiSiacRMovgestTsSogclasseCoinvolti = distintiSiacRMovgestTsSogclasseCoinvolti;
	}

	public List<SiacRMovgestTsStatoFin> getDistintiSiacRMovgestTsStatoCoinvolti() {
		return distintiSiacRMovgestTsStatoCoinvolti;
	}

	public void setDistintiSiacRMovgestTsStatoCoinvolti(
			List<SiacRMovgestTsStatoFin> distintiSiacRMovgestTsStatoCoinvolti) {
		this.distintiSiacRMovgestTsStatoCoinvolti = distintiSiacRMovgestTsStatoCoinvolti;
	}

	public List<SiacRMovgestTsProgrammaFin> getDistintiSiacRMovgestTsProgrammaCoinvolti() {
		return distintiSiacRMovgestTsProgrammaCoinvolti;
	}

	public void setDistintiSiacRMovgestTsProgrammaCoinvolti(
			List<SiacRMovgestTsProgrammaFin> distintiSiacRMovgestTsProgrammaCoinvolti) {
		this.distintiSiacRMovgestTsProgrammaCoinvolti = distintiSiacRMovgestTsProgrammaCoinvolti;
	}

	public List<SiacRMovgestTsAttoAmmFin> getDistintiSiacRMovgestTsAttoAmmCoinvolti() {
		return distintiSiacRMovgestTsAttoAmmCoinvolti;
	}

	public void setDistintiSiacRMovgestTsAttoAmmCoinvolti(
			List<SiacRMovgestTsAttoAmmFin> distintiSiacRMovgestTsAttoAmmCoinvolti) {
		this.distintiSiacRMovgestTsAttoAmmCoinvolti = distintiSiacRMovgestTsAttoAmmCoinvolti;
	}

	public List<SiacRMovgestBilElemFin> getDistintiSiacRMovgestBilElemCoinvolti() {
		return distintiSiacRMovgestBilElemCoinvolti;
	}

	public void setDistintiSiacRMovgestBilElemCoinvolti(
			List<SiacRMovgestBilElemFin> distintiSiacRMovgestBilElemCoinvolti) {
		this.distintiSiacRMovgestBilElemCoinvolti = distintiSiacRMovgestBilElemCoinvolti;
	}

	public List<SiacROrdinativoTsMovgestTFin> getDistintiSiacROrdinativoTsMovgestTCoinvolti() {
		return distintiSiacROrdinativoTsMovgestTCoinvolti;
	}

	public void setDistintiSiacROrdinativoTsMovgestTCoinvolti(
			List<SiacROrdinativoTsMovgestTFin> distintiSiacROrdinativoTsMovgestTCoinvolti) {
		this.distintiSiacROrdinativoTsMovgestTCoinvolti = distintiSiacROrdinativoTsMovgestTCoinvolti;
	}

	public List<CodificaImportoDto> getListaDisponibiliIncassare() {
		return listaDisponibiliIncassare;
	}

	public void setListaDisponibiliIncassare(
			List<CodificaImportoDto> listaDisponibiliIncassare) {
		this.listaDisponibiliIncassare = listaDisponibiliIncassare;
	}


	public OttimizzazioneModificheMovimentoGestioneDto getOttimizzazioneModDto() {
		return ottimizzazioneModDto;
	}

	public void setOttimizzazioneModDto(
			OttimizzazioneModificheMovimentoGestioneDto ottimizzazioneModDto) {
		this.ottimizzazioneModDto = ottimizzazioneModDto;
	}

	public List<SiacRLiquidazioneMovgestFin> getDistintiSiacRLiquidazioneMovgestFinCoinvolti() {
		return distintiSiacRLiquidazioneMovgestFinCoinvolti;
	}

	public void setDistintiSiacRLiquidazioneMovgestFinCoinvolti(
			List<SiacRLiquidazioneMovgestFin> distintiSiacRLiquidazioneMovgestFinCoinvolti) {
		this.distintiSiacRLiquidazioneMovgestFinCoinvolti = distintiSiacRLiquidazioneMovgestFinCoinvolti;
	}

	public List<SiacTLiquidazioneFin> getDistintiSiacTLiquidazioneFinCoinvolti() {
		return distintiSiacTLiquidazioneFinCoinvolti;
	}

	public void setDistintiSiacTLiquidazioneFinCoinvolti(
			List<SiacTLiquidazioneFin> distintiSiacTLiquidazioneFinCoinvolti) {
		this.distintiSiacTLiquidazioneFinCoinvolti = distintiSiacTLiquidazioneFinCoinvolti;
	}

	public List<SiacRLiquidazioneStatoFin> getDistintiSiacRLiquidazioneStatoFinCoinvolti() {
		return distintiSiacRLiquidazioneStatoFinCoinvolti;
	}

	public void setDistintiSiacRLiquidazioneStatoFinCoinvolti(
			List<SiacRLiquidazioneStatoFin> distintiSiacRLiquidazioneStatoFinCoinvolti) {
		this.distintiSiacRLiquidazioneStatoFinCoinvolti = distintiSiacRLiquidazioneStatoFinCoinvolti;
	}

	public List<SiacRLiquidazioneOrdFin> getDistintiSiacRLiquidazioneOrdFinCoinvolti() {
		return distintiSiacRLiquidazioneOrdFinCoinvolti;
	}

	public void setDistintiSiacRLiquidazioneOrdFinCoinvolti(
			List<SiacRLiquidazioneOrdFin> distintiSiacRLiquidazioneOrdFinCoinvolti) {
		this.distintiSiacRLiquidazioneOrdFinCoinvolti = distintiSiacRLiquidazioneOrdFinCoinvolti;
	}

	public List<SiacTOrdinativoTFin> getDistintiSiacTOrdinativoTFinCoinvolti() {
		return distintiSiacTOrdinativoTFinCoinvolti;
	}

	public void setDistintiSiacTOrdinativoTFinCoinvolti(
			List<SiacTOrdinativoTFin> distintiSiacTOrdinativoTFinCoinvolti) {
		this.distintiSiacTOrdinativoTFinCoinvolti = distintiSiacTOrdinativoTFinCoinvolti;
	}

	public List<SiacTOrdinativoFin> getDistintiSiacTOrdinativoFinCoinvolti() {
		return distintiSiacTOrdinativoFinCoinvolti;
	}

	public void setDistintiSiacTOrdinativoFinCoinvolti(
			List<SiacTOrdinativoFin> distintiSiacTOrdinativoFinCoinvolti) {
		this.distintiSiacTOrdinativoFinCoinvolti = distintiSiacTOrdinativoFinCoinvolti;
	}

	public List<SiacTOrdinativoTsDetFin> getDistintiSiacTOrdinativoTsDetFinCoinvolti() {
		return distintiSiacTOrdinativoTsDetFinCoinvolti;
	}

	public void setDistintiSiacTOrdinativoTsDetFinCoinvolti(
			List<SiacTOrdinativoTsDetFin> distintiSiacTOrdinativoTsDetFinCoinvolti) {
		this.distintiSiacTOrdinativoTsDetFinCoinvolti = distintiSiacTOrdinativoTsDetFinCoinvolti;
	}

	public List<SiacROrdinativoStatoFin> getDistintiSiacROrdinativoStatoFinCoinvolti() {
		return distintiSiacROrdinativoStatoFinCoinvolti;
	}

	public void setDistintiSiacROrdinativoStatoFinCoinvolti(
			List<SiacROrdinativoStatoFin> distintiSiacROrdinativoStatoFinCoinvolti) {
		this.distintiSiacROrdinativoStatoFinCoinvolti = distintiSiacROrdinativoStatoFinCoinvolti;
	}

	public List<CodificaImportoDto> getListaDisponibiliLiquidareDaFunction() {
		return listaDisponibiliLiquidareDaFunction;
	}

	public void setListaDisponibiliLiquidareDaFunction(
			List<CodificaImportoDto> listaDisponibiliLiquidare) {
		this.listaDisponibiliLiquidareDaFunction = listaDisponibiliLiquidare;
	}

	public List<SiacRMovgestTsFin> getDistintiSiacRMovgestTsFinCoinvolti() {
		return distintiSiacRMovgestTsFinCoinvolti;
	}


	/**
	 * @return the distintiSiacTMovgestTsFinCoinvolti
	 */
	public List<SiacTMovgestTsFin> getDistintiSiacTMovgestTsFinCoinvolti() {
		return distintiSiacTMovgestTsFinCoinvolti;
	}


	/**
	 * @param distintiSiacTMovgestTsFinCoinvolti the distintiSiacTMovgestTsFinCoinvolti to set
	 */
	public void setDistintiSiacTMovgestTsFinCoinvolti(
			List<SiacTMovgestTsFin> distintiSiacTMovgestTsFinCoinvolti) {
		this.distintiSiacTMovgestTsFinCoinvolti = distintiSiacTMovgestTsFinCoinvolti;
	}


	/**
	 * @param distintiSiacRMovgestTsFinCoinvolti the distintiSiacRMovgestTsFinCoinvolti to set
	 */
	public void setDistintiSiacRMovgestTsFinCoinvolti(
			List<SiacRMovgestTsFin> distintiSiacRMovgestTsFinCoinvolti) {
		this.distintiSiacRMovgestTsFinCoinvolti = distintiSiacRMovgestTsFinCoinvolti;
	}



	public List<SiacTMovgestFin> getDistintiSiacTMovgestFinCoinvolti() {
		return distintiSiacTMovgestFinCoinvolti;
	}


	public void setDistintiSiacTMovgestFinCoinvolti(
			List<SiacTMovgestFin> distintiSiacTMovgestFinCoinvolti) {
		this.distintiSiacTMovgestFinCoinvolti = distintiSiacTMovgestFinCoinvolti;
	}


	public OttimizzazioneSoggettoDto getOttimizzazioneSoggetti() {
		return ottimizzazioneSoggetti;
	}


	public void setOttimizzazioneSoggetti(
			OttimizzazioneSoggettoDto ottimizzazioneSoggetti) {
		this.ottimizzazioneSoggetti = ottimizzazioneSoggetti;
	}

	public SiacTMovgestFin getMovimentoDaChiamante() {
		return movimentoDaChiamante;
	}

	public void setMovimentoDaChiamante(SiacTMovgestFin movimentoDaChiamante) {
		this.movimentoDaChiamante = movimentoDaChiamante;
	}

	public List<SiacRProgrammaAttrFin> getDistintiSiacRProgrammaAttrFinCoinvolti() {
		return distintiSiacRProgrammaAttrFinCoinvolti;
	}

	public void setDistintiSiacRProgrammaAttrFinCoinvolti(
			List<SiacRProgrammaAttrFin> distintiSiacRProgrammaAttrFinCoinvolti) {
		this.distintiSiacRProgrammaAttrFinCoinvolti = distintiSiacRProgrammaAttrFinCoinvolti;
	}

	public List<SiacRVincoloBilElemFin> getDistintSiacRVincoloBilElemFinCoinvolti() {
		return distintSiacRVincoloBilElemFinCoinvolti;
	}

	public void setDistintSiacRVincoloBilElemFinCoinvolti(
			List<SiacRVincoloBilElemFin> distintSiacRVincoloBilElemFinCoinvolti) {
		this.distintSiacRVincoloBilElemFinCoinvolti = distintSiacRVincoloBilElemFinCoinvolti;
	}

	public List<SiacRVincoloAttrFin> getDistintiSiacRVincoloAttrFinCoinvolti() {
		return distintiSiacRVincoloAttrFinCoinvolti;
	}

	public void setDistintiSiacRVincoloAttrFinCoinvolti(
			List<SiacRVincoloAttrFin> distintiSiacRVincoloAttrFinCoinvolti) {
		this.distintiSiacRVincoloAttrFinCoinvolti = distintiSiacRVincoloAttrFinCoinvolti;
	}

	public List<CodificaImportoDto> getListaDisponibiliPagareDaFunction() {
		return listaDisponibiliPagareDaFunction;
	}

	public void setListaDisponibiliPagareDaFunction(
			List<CodificaImportoDto> listaDisponibiliPagareDaFunction) {
		this.listaDisponibiliPagareDaFunction = listaDisponibiliPagareDaFunction;
	}
	
}
