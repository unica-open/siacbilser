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
import it.csi.siac.siacfinser.StringUtils;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestBilElemFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsAttoAmmFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMutuoAttoAmmFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMutuoSoggettoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMutuoStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMutuoVoceLiquidazioneFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMutuoVoceMovgestFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoClasseFin;
import it.csi.siac.siacfinser.integration.entity.SiacTLiquidazioneFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsDetFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsDetModFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMutuoFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMutuoVoceFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMutuoVoceVarFin;
import it.csi.siac.siacfinser.integration.entity.SiacTSoggettoFin;

public class OttimizzazioneMutuoDto implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2573703973624140188L;
	
	
	private List<SiacTMovgestTsFin> distintiSiacTMovgestTsFinCoinvolti;
	
	private List<SiacRMutuoStatoFin> distintiSiacRMutuoStatoFinCoinvolti;
	
	private List<SiacTMovgestFin> distintiSiacTMovgestFinCoinvolti;
	
	private List<SiacTMutuoVoceFin> distintiSiacTMutuoVoceFinCoinvolti;
	
	private List<SiacTMutuoFin> distintiSiacTMutuoFinCoinvolti;
	
	private List<SiacRMutuoVoceMovgestFin> distintiSiacRMutuoVoceMovgestFinCoinvolti;
	
	private List<SiacRMovgestBilElemFin> distintiSiacRMovgestBilElemCoinvolti;
	
	private List<SiacRMovgestTsAttoAmmFin> distintiSiacRMovgestTsAttoAmmCoinvolti;
	
	private List<SiacTMovgestTsDetFin> distintiSiacTMovgestTsDetCoinvolti;
	
	private List<CodificaImportoDto> listaDisponibiliLiquidare;
	
	private List<SiacTMovgestTsDetModFin> distintiSiacTMovgestTsDetModFinCoinvolti;
	
	private List<SiacTMutuoVoceVarFin> distintiSiacTMutuoVoceVarFinCoinvolti;
	
	private List<SiacRMutuoAttoAmmFin> distintiSiacRMutuoAttoAmmFinCoinvolti;
	
	private List<SiacRMutuoSoggettoFin> distintiSiacRMutuoSoggettoFinCoinvolti;
	
	private List<SiacRSoggettoClasseFin> distintiSiacRSoggettoClasseFinCoinvolti;
	
	private List<SiacRMutuoVoceLiquidazioneFin> distintiSiacRMutuoVoceLiquidazioneFinCoinvoltiInStatoValido;
	
	//UTILITIES:
	
	public List<SiacRMutuoVoceLiquidazioneFin> filtraSiacRMutuoVoceLiquidazioneFinBySiacTMutuoVoceFin(SiacTMutuoVoceFin siacTMutuoVoceFin){
		List<SiacRMutuoVoceLiquidazioneFin> filtrati = new ArrayList<SiacRMutuoVoceLiquidazioneFin>();
		if(siacTMutuoVoceFin!=null){
			Integer mutVoceId = siacTMutuoVoceFin.getMutVoceId();
			if(mutVoceId!=null && distintiSiacRMutuoVoceLiquidazioneFinCoinvoltiInStatoValido!=null && distintiSiacRMutuoVoceLiquidazioneFinCoinvoltiInStatoValido.size()>0){
				for(SiacRMutuoVoceLiquidazioneFin it : distintiSiacRMutuoVoceLiquidazioneFinCoinvoltiInStatoValido){
					if(it.getSiacTMutuoVoce().getMutVoceId().intValue()==mutVoceId.intValue()){
						filtrati.add(it);
					}
				}
			}
			filtrati = CommonUtils.soloValidiSiacTBase(filtrati, null);
		}
		return filtrati;
	}
	
	
	public List<SiacTLiquidazioneFin> filtraSiacTLiquidazioneFinBySiacTMutuoVoceFinInStatoValido(SiacTMutuoVoceFin siacTMutuoVoceFin){
		List<SiacTLiquidazioneFin> filtrati = new ArrayList<SiacTLiquidazioneFin>();
		List<SiacRMutuoVoceLiquidazioneFin> filtratiSiacRMutuoVoceLiquidazioneFinInStatoValido = filtraSiacRMutuoVoceLiquidazioneFinBySiacTMutuoVoceFin(siacTMutuoVoceFin);
		if(filtratiSiacRMutuoVoceLiquidazioneFinInStatoValido!=null && filtratiSiacRMutuoVoceLiquidazioneFinInStatoValido.size()>0){
			for(SiacRMutuoVoceLiquidazioneFin it : filtratiSiacRMutuoVoceLiquidazioneFinInStatoValido){
				filtrati.add(it.getSiacTLiquidazione());
			}
			filtrati = CommonUtils.soloValidiSiacTBase(filtrati, null);
		}
		return filtrati;
	}
	
	public BigDecimal calcolaSommaLiquidazioniByVoceMutuo(SiacTMutuoVoceFin siacTMutuoVoceFin){
		BigDecimal disp = BigDecimal.ZERO;
		List<SiacTLiquidazioneFin> liqValide =  filtraSiacTLiquidazioneFinBySiacTMutuoVoceFinInStatoValido(siacTMutuoVoceFin);
		if(liqValide!=null && liqValide.size()>0){
			for(SiacTLiquidazioneFin liqIt : liqValide){
				disp = disp.add(liqIt.getLiqImporto());
			}
		}
		return disp;
	}
	
	public List<SiacRSoggettoClasseFin> filtraSiacRSoggettoClasseFinBySiacTSoggettoFin(SiacTSoggettoFin siacTSoggettoFin){
		List<SiacRSoggettoClasseFin> filtrati = new ArrayList<SiacRSoggettoClasseFin>();
		if(siacTSoggettoFin!=null){
			Integer soggId = siacTSoggettoFin.getSoggettoId();
			if(soggId!=null && distintiSiacRSoggettoClasseFinCoinvolti!=null && distintiSiacRSoggettoClasseFinCoinvolti.size()>0){
				for(SiacRSoggettoClasseFin it : distintiSiacRSoggettoClasseFinCoinvolti){
					if(it.getSiacTSoggetto().getSoggettoId().intValue()==soggId.intValue()){
						filtrati.add(it);
					}
				}
			}
		}
		return filtrati;
	}
	
	public List<SiacRMutuoSoggettoFin> filtraSiacRMutuoSoggettoFinBySiacTMutuoVoceFin(SiacTMutuoFin siacTMutuoFin){
		List<SiacRMutuoSoggettoFin> filtrati = new ArrayList<SiacRMutuoSoggettoFin>();
		if(siacTMutuoFin!=null){
			Integer mutId = siacTMutuoFin.getMutId();
			if(mutId!=null && distintiSiacRMutuoSoggettoFinCoinvolti!=null && distintiSiacRMutuoSoggettoFinCoinvolti.size()>0){
				for(SiacRMutuoSoggettoFin it : distintiSiacRMutuoSoggettoFinCoinvolti){
					if(it.getSiacTMutuo().getMutId().intValue()==mutId.intValue()){
						filtrati.add(it);
					}
				}
			}
		}
		return filtrati;
	}
	
	public List<SiacRMutuoAttoAmmFin> filtraSiacRMutuoAttoAmmFinBySiacTMutuoVoceFin(SiacTMutuoFin siacTMutuoFin){
		List<SiacRMutuoAttoAmmFin> filtrati = new ArrayList<SiacRMutuoAttoAmmFin>();
		if(siacTMutuoFin!=null){
			Integer mutId = siacTMutuoFin.getMutId();
			if(mutId!=null && distintiSiacRMutuoAttoAmmFinCoinvolti!=null && distintiSiacRMutuoAttoAmmFinCoinvolti.size()>0){
				for(SiacRMutuoAttoAmmFin it : distintiSiacRMutuoAttoAmmFinCoinvolti){
					if(it.getSiacTMutuo().getMutId().intValue()==mutId.intValue()){
						filtrati.add(it);
					}
				}
			}
		}
		return filtrati;
	}
	
	public List<SiacRMutuoStatoFin> filtraSiacRMutuoStatoFinBySiacTMutuoVoceFin(SiacTMutuoFin siacTMutuoFin){
		List<SiacRMutuoStatoFin> filtrati = new ArrayList<SiacRMutuoStatoFin>();
		if(siacTMutuoFin!=null){
			Integer mutId = siacTMutuoFin.getMutId();
			if(mutId!=null && distintiSiacRMutuoStatoFinCoinvolti!=null && distintiSiacRMutuoStatoFinCoinvolti.size()>0){
				for(SiacRMutuoStatoFin it : distintiSiacRMutuoStatoFinCoinvolti){
					if(it.getSiacTMutuo().getMutId().intValue()==mutId.intValue()){
						filtrati.add(it);
					}
				}
			}
		}
		return filtrati;
	}
	
	public SiacTMutuoFin estraiSiacTMutuoByNumeroMutuo(String numeroMutuo){
		SiacTMutuoFin trovato = null;
		if(distintiSiacTMutuoFinCoinvolti!=null && distintiSiacTMutuoFinCoinvolti.size()>0){
			for(SiacTMutuoFin it : distintiSiacTMutuoFinCoinvolti){
				if(it!=null && it.getMutCode().equals(numeroMutuo)){
					trovato = it;
					break;
				}
			}
		}
		return trovato;
	}
	
	public SiacRMutuoVoceMovgestFin getSiacRMutuoVoceMovgestFinValidoBySiacTMutuoVoceFin(SiacTMutuoVoceFin siacTMutuoVoceFin){
		List<SiacRMutuoVoceMovgestFin> lista = filtraSiacRMovgestTsFinBySiacTMutuoVoceFin(siacTMutuoVoceFin);
		return CommonUtils.getValidoSiacTBase(lista, null);
	}
	
	
	public List<SiacRMutuoVoceMovgestFin> filtraSiacRMovgestTsFinBySiacTMutuoVoceFin(SiacTMutuoVoceFin siacTMutuoVoceFin){
		List<SiacRMutuoVoceMovgestFin> filtrati = new ArrayList<SiacRMutuoVoceMovgestFin>();
		if(siacTMutuoVoceFin!=null){
			Integer mutVoceId = siacTMutuoVoceFin.getMutVoceId();
			if(mutVoceId!=null && distintiSiacRMutuoVoceMovgestFinCoinvolti!=null && distintiSiacRMutuoVoceMovgestFinCoinvolti.size()>0){
				for(SiacRMutuoVoceMovgestFin it : distintiSiacRMutuoVoceMovgestFinCoinvolti){
					if(it.getSiacTMutuoVoce().getMutVoceId().intValue()==mutVoceId.intValue()){
						filtrati.add(it);
					}
				}
			}
		}
		return filtrati;
	}
	
	public List<SiacTMutuoVoceVarFin> filtraSiacTMutuoVoceVarFinFinBySiacTMutuoVoceFin(SiacTMutuoVoceFin siacTMutuoVoceFin){
		List<SiacTMutuoVoceVarFin> filtrati = new ArrayList<SiacTMutuoVoceVarFin>();
		if(siacTMutuoVoceFin!=null){
			Integer mutVoceId = siacTMutuoVoceFin.getMutVoceId();
			if(mutVoceId!=null && distintiSiacTMutuoVoceVarFinCoinvolti!=null && distintiSiacTMutuoVoceVarFinCoinvolti.size()>0){
				for(SiacTMutuoVoceVarFin it : distintiSiacTMutuoVoceVarFinCoinvolti){
					if(it.getSiacTMutuoVoce().getMutVoceId().intValue()==mutVoceId.intValue()){
						filtrati.add(it);
					}
				}
			}
		}
		return filtrati;
	}
	
	public List<SiacRMutuoVoceMovgestFin> filtraSiacRMovgestTsFinBySiacTMovgestTsFin(SiacTMovgestTsFin siacTMovgestTsFin){
		List<SiacRMutuoVoceMovgestFin> filtrati = new ArrayList<SiacRMutuoVoceMovgestFin>();
		if(siacTMovgestTsFin!=null){
			Integer movgestTsId = siacTMovgestTsFin.getMovgestTsId();
			if(movgestTsId!=null && distintiSiacRMutuoVoceMovgestFinCoinvolti!=null && distintiSiacRMutuoVoceMovgestFinCoinvolti.size()>0){
				for(SiacRMutuoVoceMovgestFin it : distintiSiacRMutuoVoceMovgestFinCoinvolti){
					if(it.getSiacTMovgestTs().getMovgestTsId().intValue()==movgestTsId.intValue()){
						filtrati.add(it);
					}
				}
			}
		}
		return filtrati;
	}
	
	public List<SiacTMutuoVoceFin> getSiacTMutuoVoceValideFinBySiacTMovgestTsFin(SiacTMovgestTsFin siacTMovgestTsFin){
		List<SiacTMutuoVoceFin> listaValide = null;
		if(siacTMovgestTsFin!=null){
			List<SiacRMutuoVoceMovgestFin> siacRMutuoVoceMovgestFins = filtraSiacRMovgestTsFinBySiacTMovgestTsFin(siacTMovgestTsFin);
			siacRMutuoVoceMovgestFins = CommonUtils.soloValidiSiacTBase(siacRMutuoVoceMovgestFins, null);
			if(!StringUtils.isEmpty(siacRMutuoVoceMovgestFins)){
				listaValide = new ArrayList<SiacTMutuoVoceFin>();
				for(SiacRMutuoVoceMovgestFin it : siacRMutuoVoceMovgestFins){
					if(it!=null && it.getSiacTMutuoVoce()!=null){
						listaValide.add(it.getSiacTMutuoVoce());
					}
				}
				listaValide = CommonUtils.soloValidiSiacTBase(listaValide, null);
			}
		}
		return listaValide;
	}
	
	public BigDecimal findSommaVociMutuoValideBySiacTMovgestTsFin(SiacTMovgestTsFin siacTMovgestTsFin){
		BigDecimal totVoci = BigDecimal.ZERO;
		if(siacTMovgestTsFin!=null){
			List<SiacTMutuoVoceFin> listaVoci = getSiacTMutuoVoceValideFinBySiacTMovgestTsFin(siacTMovgestTsFin);
			if(!StringUtils.isEmpty(listaVoci)){
				for(SiacTMutuoVoceFin it : listaVoci){
					if(it!=null){
						totVoci = totVoci.add(it.getMutVoceImportoAttuale());
					}
				}
			}
		}
		return totVoci;
	}

	public List<SiacRMovgestBilElemFin> filtraSiacRMovgestBilElemFinBySiacTMovgestFin(SiacTMovgestFin siacTMovgestFin){
		List<SiacRMovgestBilElemFin> filtrati = new ArrayList<SiacRMovgestBilElemFin>();
		if(siacTMovgestFin!=null){
			Integer movGestId = siacTMovgestFin.getMovgestId();
			if(movGestId!=null && distintiSiacRMovgestBilElemCoinvolti!=null && distintiSiacRMovgestBilElemCoinvolti.size()>0){
				for(SiacRMovgestBilElemFin it : distintiSiacRMovgestBilElemCoinvolti){
					if(it.getSiacTMovgest().getMovgestId().intValue()==movGestId.intValue()){
						filtrati.add(it);
					}
				}
			}
		}
		return filtrati;
	}
	
	public SiacRMovgestBilElemFin getSiacRMovgestBilElemFinValido(SiacTMovgestFin siacTMovgestFin){
		List<SiacRMovgestBilElemFin> filtrati = filtraSiacRMovgestBilElemFinBySiacTMovgestFin(siacTMovgestFin);
		return  CommonUtils.getValidoSiacTBase(filtrati, null);
	}
	
	public SiacRMovgestTsAttoAmmFin getSiacRMovgestTsAttoAmmFinValido(SiacTMovgestTsFin siacTMovgestTsFin){
		List<SiacRMovgestTsAttoAmmFin> filtrati = filtraSiacRMovgestTsAttoAmmFinBySiacTMovgestFin(siacTMovgestTsFin);
		return  CommonUtils.getValidoSiacTBase(filtrati, null);
	}
	
	public List<SiacRMovgestTsAttoAmmFin> filtraSiacRMovgestTsAttoAmmFinBySiacTMovgestFin(SiacTMovgestTsFin siacTMovgestTsFin){
		List<SiacRMovgestTsAttoAmmFin> filtrati = new ArrayList<SiacRMovgestTsAttoAmmFin>();
		if(siacTMovgestTsFin!=null){
			Integer movGestTsId = siacTMovgestTsFin.getMovgestTsId();
			if(movGestTsId!=null && distintiSiacRMovgestTsAttoAmmCoinvolti!=null && distintiSiacRMovgestTsAttoAmmCoinvolti.size()>0){
				for(SiacRMovgestTsAttoAmmFin it : distintiSiacRMovgestTsAttoAmmCoinvolti){
					if(it.getSiacTMovgestT().getMovgestTsId().intValue()==movGestTsId.intValue()){
						filtrati.add(it);
					}
				}
			}
		}
		return filtrati;
	}
	
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
	
	public BigDecimal estraiDisponibileLiquidare(Integer movgestTsId){
		BigDecimal disp = BigDecimal.ZERO;
		if(movgestTsId!=null && listaDisponibiliLiquidare!=null && listaDisponibiliLiquidare.size()>0){
			for(CodificaImportoDto it : listaDisponibiliLiquidare){
				if(it!=null && it.getImporto()!=null && it.getIdOggetto().intValue()==movgestTsId.intValue()){
					return it.getImporto();
				}
			}
		}
		return disp;
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
		filtrati = CommonUtils.soloValidiSiacTBase(filtrati, null);
		return filtrati;
	}
	
	/**
	 * Non si basa su variabili interne al dto ma filtra l'input
	 * @param siacRMutuoVoceMovgestFin
	 * @return
	 */
	public List<SiacTMutuoFin> estraiSiacTMutuoFinBySiacRMutuoVoceMovgestFin(List<SiacRMutuoVoceMovgestFin> siacRMutuoVoceMovgestFin){
		List<SiacTMutuoFin> lista = new ArrayList<SiacTMutuoFin>();
		if(!StringUtils.isEmpty(siacRMutuoVoceMovgestFin)){
			for(SiacRMutuoVoceMovgestFin it : siacRMutuoVoceMovgestFin){
				lista.add(it.getSiacTMutuoVoce().getSiacTMutuo());
			}
		}
		return lista;
	}
	
	/**
	 * 
	 * Non si basa su variabili interne al dto ma filtra l'input
	 * 
	 * @param siacRMutuoSoggettoFin
	 * @return
	 */
	public List<SiacTSoggettoFin> estraiSiacTSoggettoFinBySiacRMutuoSoggettoFin(List<SiacRMutuoSoggettoFin> siacRMutuoSoggettoFin){
		List<SiacTSoggettoFin> lista = new ArrayList<SiacTSoggettoFin>();
		if(!StringUtils.isEmpty(siacRMutuoSoggettoFin)){
			for(SiacRMutuoSoggettoFin it : siacRMutuoSoggettoFin){
				lista.add(it.getSiacTSoggetto());
			}
				
		}
		return lista;
	}
	
	/**
	 * 
	 * Non si basa su variabili interne al dto ma filtra l'input
	 * 
	 * @param siacRMutuoVoceMovgestFin
	 * @return
	 */
	public List<SiacTMovgestFin> estraiSiacTMovgestFinBySiacRMutuoVoceMovgestFin(List<SiacRMutuoVoceMovgestFin> siacRMutuoVoceMovgestFin){
		List<SiacTMovgestFin> lista = new ArrayList<SiacTMovgestFin>();
		if(!StringUtils.isEmpty(siacRMutuoVoceMovgestFin)){
			for(SiacRMutuoVoceMovgestFin it : siacRMutuoVoceMovgestFin){
				lista.add(it.getSiacTMovgestTs().getSiacTMovgest());
			}
		}
		return lista;
	}
	
	/**
	 * 
	 * Non si basa su variabili interne al dto ma filtra l'input
	 * 
	 * @param siacRMutuoVoceMovgestFin
	 * @return
	 */
	public List<SiacTMovgestTsFin> estraiSiacTMovgestTsFinBySiacRMutuoVoceMovgestFin(List<SiacRMutuoVoceMovgestFin> siacRMutuoVoceMovgestFin){
		List<SiacTMovgestTsFin> lista = new ArrayList<SiacTMovgestTsFin>();
		if(!StringUtils.isEmpty(siacRMutuoVoceMovgestFin)){
			for(SiacRMutuoVoceMovgestFin it : siacRMutuoVoceMovgestFin){
				lista.add(it.getSiacTMovgestTs());
			}
		}
		return lista;
	}

	/**
	 * Non si basa su variabili interne al dto ma filtra l'input
	 * 
	 * @param siacRMutuoVoceMovgestFin
	 * @return
	 */
	public List<SiacTMutuoVoceFin> estraiSiacTMutuoVoceFinBySiacRMutuoVoceMovgestFin(List<SiacRMutuoVoceMovgestFin> siacRMutuoVoceMovgestFin){
		List<SiacTMutuoVoceFin> lista = new ArrayList<SiacTMutuoVoceFin>();
		if(!StringUtils.isEmpty(siacRMutuoVoceMovgestFin)){
			for(SiacRMutuoVoceMovgestFin it : siacRMutuoVoceMovgestFin){
				lista.add(it.getSiacTMutuoVoce());
			}
		}
		return lista;
	}
	
	//GETTER E SETTER:
	
	public List<SiacTMutuoVoceFin> getDistintiSiacTMutuoVoceFinCoinvolti() {
		return distintiSiacTMutuoVoceFinCoinvolti;
	}


	public void setDistintiSiacTMutuoVoceFinCoinvolti(
			List<SiacTMutuoVoceFin> distintiSiacTMutuoVoceFinCoinvolti) {
		this.distintiSiacTMutuoVoceFinCoinvolti = distintiSiacTMutuoVoceFinCoinvolti;
	}


	public List<SiacRMutuoVoceMovgestFin> getDistintiSiacRMutuoVoceMovgestFinCoinvolti() {
		return distintiSiacRMutuoVoceMovgestFinCoinvolti;
	}


	public void setDistintiSiacRMutuoVoceMovgestFinCoinvolti(
			List<SiacRMutuoVoceMovgestFin> distintiSiacRMutuoVoceMovgestFinCoinvolti) {
		this.distintiSiacRMutuoVoceMovgestFinCoinvolti = distintiSiacRMutuoVoceMovgestFinCoinvolti;
	}


	public List<SiacRMovgestBilElemFin> getDistintiSiacRMovgestBilElemCoinvolti() {
		return distintiSiacRMovgestBilElemCoinvolti;
	}


	public void setDistintiSiacRMovgestBilElemCoinvolti(
			List<SiacRMovgestBilElemFin> distintiSiacRMovgestBilElemCoinvolti) {
		this.distintiSiacRMovgestBilElemCoinvolti = distintiSiacRMovgestBilElemCoinvolti;
	}


	public List<SiacRMovgestTsAttoAmmFin> getDistintiSiacRMovgestTsAttoAmmCoinvolti() {
		return distintiSiacRMovgestTsAttoAmmCoinvolti;
	}


	public void setDistintiSiacRMovgestTsAttoAmmCoinvolti(
			List<SiacRMovgestTsAttoAmmFin> distintiSiacRMovgestTsAttoAmmCoinvolti) {
		this.distintiSiacRMovgestTsAttoAmmCoinvolti = distintiSiacRMovgestTsAttoAmmCoinvolti;
	}


	public List<SiacTMovgestTsDetFin> getDistintiSiacTMovgestTsDetCoinvolti() {
		return distintiSiacTMovgestTsDetCoinvolti;
	}


	public void setDistintiSiacTMovgestTsDetCoinvolti(
			List<SiacTMovgestTsDetFin> distintiSiacTMovgestTsDetCoinvolti) {
		this.distintiSiacTMovgestTsDetCoinvolti = distintiSiacTMovgestTsDetCoinvolti;
	}


	public List<CodificaImportoDto> getListaDisponibiliLiquidare() {
		return listaDisponibiliLiquidare;
	}

	public void setListaDisponibiliLiquidare(
			List<CodificaImportoDto> listaDisponibiliLiquidare) {
		this.listaDisponibiliLiquidare = listaDisponibiliLiquidare;
	}


	public List<SiacTMovgestTsDetModFin> getDistintiSiacTMovgestTsDetModFinCoinvolti() {
		return distintiSiacTMovgestTsDetModFinCoinvolti;
	}


	public void setDistintiSiacTMovgestTsDetModFinCoinvolti(
			List<SiacTMovgestTsDetModFin> distintiSiacTMovgestTsDetModFinCoinvolti) {
		this.distintiSiacTMovgestTsDetModFinCoinvolti = distintiSiacTMovgestTsDetModFinCoinvolti;
	}


	public List<SiacTMutuoVoceVarFin> getDistintiSiacTMutuoVoceVarFinCoinvolti() {
		return distintiSiacTMutuoVoceVarFinCoinvolti;
	}


	public void setDistintiSiacTMutuoVoceVarFinCoinvolti(
			List<SiacTMutuoVoceVarFin> distintiSiacTMutuoVoceVarFinCoinvolti) {
		this.distintiSiacTMutuoVoceVarFinCoinvolti = distintiSiacTMutuoVoceVarFinCoinvolti;
	}

	public List<SiacTMovgestTsFin> getDistintiSiacTMovgestTsFinCoinvolti() {
		return distintiSiacTMovgestTsFinCoinvolti;
	}

	public void setDistintiSiacTMovgestTsFinCoinvolti(
			List<SiacTMovgestTsFin> distintiSiacTMovgestTsFinCoinvolti) {
		this.distintiSiacTMovgestTsFinCoinvolti = distintiSiacTMovgestTsFinCoinvolti;
	}

	public List<SiacTMovgestFin> getDistintiSiacTMovgestFinCoinvolti() {
		return distintiSiacTMovgestFinCoinvolti;
	}

	public void setDistintiSiacTMovgestFinCoinvolti(
			List<SiacTMovgestFin> distintiSiacTMovgestFinCoinvolti) {
		this.distintiSiacTMovgestFinCoinvolti = distintiSiacTMovgestFinCoinvolti;
	}

	public List<SiacTMutuoFin> getDistintiSiacTMutuoFinCoinvolti() {
		return distintiSiacTMutuoFinCoinvolti;
	}

	public void setDistintiSiacTMutuoFinCoinvolti(
			List<SiacTMutuoFin> distintiSiacTMutuoFinCoinvolti) {
		this.distintiSiacTMutuoFinCoinvolti = distintiSiacTMutuoFinCoinvolti;
	}

	public List<SiacRMutuoStatoFin> getDistintiSiacRMutuoStatoFinCoinvolti() {
		return distintiSiacRMutuoStatoFinCoinvolti;
	}

	public void setDistintiSiacRMutuoStatoFinCoinvolti(
			List<SiacRMutuoStatoFin> distintiSiacRMutuoStatoFinCoinvolti) {
		this.distintiSiacRMutuoStatoFinCoinvolti = distintiSiacRMutuoStatoFinCoinvolti;
	}

	public List<SiacRMutuoAttoAmmFin> getDistintiSiacRMutuoAttoAmmFinCoinvolti() {
		return distintiSiacRMutuoAttoAmmFinCoinvolti;
	}

	public void setDistintiSiacRMutuoAttoAmmFinCoinvolti(
			List<SiacRMutuoAttoAmmFin> distintiSiacRMutuoAttoAmmFinCoinvolti) {
		this.distintiSiacRMutuoAttoAmmFinCoinvolti = distintiSiacRMutuoAttoAmmFinCoinvolti;
	}

	public List<SiacRMutuoSoggettoFin> getDistintiSiacRMutuoSoggettoFinCoinvolti() {
		return distintiSiacRMutuoSoggettoFinCoinvolti;
	}

	public void setDistintiSiacRMutuoSoggettoFinCoinvolti(
			List<SiacRMutuoSoggettoFin> distintiSiacRMutuoSoggettoFinCoinvolti) {
		this.distintiSiacRMutuoSoggettoFinCoinvolti = distintiSiacRMutuoSoggettoFinCoinvolti;
	}

	public List<SiacRSoggettoClasseFin> getDistintiSiacRSoggettoClasseFinCoinvolti() {
		return distintiSiacRSoggettoClasseFinCoinvolti;
	}

	public void setDistintiSiacRSoggettoClasseFinCoinvolti(
			List<SiacRSoggettoClasseFin> distintiSiacRSoggettoClasseFinCoinvolti) {
		this.distintiSiacRSoggettoClasseFinCoinvolti = distintiSiacRSoggettoClasseFinCoinvolti;
	}


	public List<SiacRMutuoVoceLiquidazioneFin> getDistintiSiacRMutuoVoceLiquidazioneFinCoinvoltiInStatoValido() {
		return distintiSiacRMutuoVoceLiquidazioneFinCoinvoltiInStatoValido;
	}


	public void setDistintiSiacRMutuoVoceLiquidazioneFinCoinvoltiInStatoValido(
			List<SiacRMutuoVoceLiquidazioneFin> distintiSiacRMutuoVoceLiquidazioneFinCoinvoltiInStatoValido) {
		this.distintiSiacRMutuoVoceLiquidazioneFinCoinvoltiInStatoValido = distintiSiacRMutuoVoceLiquidazioneFinCoinvoltiInStatoValido;
	}
	
}
