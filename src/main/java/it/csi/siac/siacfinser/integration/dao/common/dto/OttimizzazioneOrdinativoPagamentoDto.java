/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.csi.siac.siacbilser.integration.entity.SiacRSubdocLiquidazione;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfinser.CommonUtils;
import it.csi.siac.siacfinser.integration.entity.SiacRDocStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRLiquidazioneAttoAmmFin;
import it.csi.siac.siacfinser.integration.entity.SiacRLiquidazioneAttrFin;
import it.csi.siac.siacfinser.integration.entity.SiacRLiquidazioneClassFin;
import it.csi.siac.siacfinser.integration.entity.SiacRLiquidazioneMovgestFin;
import it.csi.siac.siacfinser.integration.entity.SiacRLiquidazioneOrdFin;
import it.csi.siac.siacfinser.integration.entity.SiacRLiquidazioneSoggettoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRLiquidazioneStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMutuoVoceLiquidazioneFin;
import it.csi.siac.siacfinser.integration.entity.SiacROrdinativoModpagFin;
import it.csi.siac.siacfinser.integration.entity.SiacROrdinativoSoggettoFin;
import it.csi.siac.siacfinser.integration.entity.SiacROrdinativoStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSubdocLiquidazioneFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSubdocOrdinativoTFin;
import it.csi.siac.siacfinser.integration.entity.SiacTDocFin;
import it.csi.siac.siacfinser.integration.entity.SiacTLiquidazioneFin;
import it.csi.siac.siacfinser.integration.entity.SiacTModpagFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsFin;
import it.csi.siac.siacfinser.integration.entity.SiacTOrdinativoFin;
import it.csi.siac.siacfinser.integration.entity.SiacTOrdinativoTFin;
import it.csi.siac.siacfinser.integration.entity.SiacTOrdinativoTsDetFin;
import it.csi.siac.siacfinser.integration.entity.SiacTSoggettoFin;
import it.csi.siac.siacfinser.integration.entity.SiacTSubdocFin;

public class OttimizzazioneOrdinativoPagamentoDto implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2573703973624140188L;
	
	private List<SiacRLiquidazioneSoggettoFin> distintiSiacRLiquidazioneSoggetto;
	
	private List<SiacRLiquidazioneOrdFin> distintiSiacRLiquidazioneOrdFinCoinvolti;
	
	//dalle liquidazioni dell'ordinativo in questione si risale agli altri ordinativi delle sue liquidazioni:
	private List<SiacRLiquidazioneOrdFin> distintiSiacRLiquidazioneOrdFinCoinvoltiAncheVersoAltriOrdinativi;
	//
	
	private List<SiacROrdinativoStatoFin> distintiSiacROrdinativoStatoFinCoinvolti;//tutti anche verso altri ordinativi delle liq
	private List<SiacTOrdinativoTsDetFin> distintiSiacTOrdinativoTsDetFinCoinvolti;//tutti anche verso altri ordinativi delle liq
	
	private List<SiacRSubdocOrdinativoTFin> distintiSiacRSubdocOrdinativoTFinCoinvolti;
	
	private List<SiacRLiquidazioneMovgestFin> distintiSiacRLiquidazioneMovgestFinCoinvolti; 
	
	private OttimizzazioneSoggettoDto ottimizzazioneSoggettoDto;
	
	private OttimizzazioneMovGestDto ottimizzazioneMovimentiCoinvolti;
	
	private List<SiacTOrdinativoTFin> listaSubOrdinativiCoinvolti;
	
	private List<SiacRLiquidazioneStatoFin> distintiSiacRLiquidazioneStatoFinCoinvolti;
	private List<SiacRLiquidazioneClassFin> distintiSiacRLiquidazioneClassFinCoinvolti;
	private List<SiacRLiquidazioneAttrFin> distintiSiacRLiquidazioneAttrFinCoinvolti;
	private List<SiacRLiquidazioneAttoAmmFin> distintiSiacRLiquidazioneAttoAmmFinCoinvolti;
	
	
	private List<SiacROrdinativoSoggettoFin> distintiSiacROrdinativoSoggettoFinCoinvolti;
	
	//SUBDOCLIQUIDAIZONE:
	private List<SiacRSubdocLiquidazioneFin> distintiSiacRSubdocLiquidazioneFinCoinvolti;//FIN
	private List<SiacRSubdocLiquidazione> distintiSiacRSubdocLiquidazioneCoinvolti;//BIL
	//
	
	private List<SiacRMutuoVoceLiquidazioneFin> distintiSiacRMutuoVoceLiquidazioneFinCoinvolti;
	
	
	private SiacTLiquidazioneFin siacTLiquidazioneConsiderata;
	
	
	private List<SiacRDocStatoFin> distintiSiacRDocStatoFinCoinvolti;
	
	private OttimizzazioneModalitaPagamentoDto modalitaPagamentoLiquidazioniCoinvolte;
	
	private OttimizzazioneModalitaPagamentoDto modalitaPagamentoOrdinativo;
	
	private List<SiacROrdinativoModpagFin> distintiSiacROrdinativoModpagFinCoinvolti;
	
	
	private HashMap<Integer, DocumentoSpesa> cacheDocumentiSpesaGiaCaricati;
	
	
	public void addDocumentoSpesaCaricato(Integer id, DocumentoSpesa docSpesa){
		if(cacheDocumentiSpesaGiaCaricati==null){
			cacheDocumentiSpesaGiaCaricati = new HashMap<Integer, DocumentoSpesa>();
		}
		cacheDocumentiSpesaGiaCaricati.put(id, docSpesa);
	}
	
	public DocumentoSpesa getDocumentoSpesaGiaCaricato(Integer id){
		if(cacheDocumentiSpesaGiaCaricati==null){
			cacheDocumentiSpesaGiaCaricati = new HashMap<Integer, DocumentoSpesa>();
		}
		if(cacheDocumentiSpesaGiaCaricati.containsKey(id)){
			return cacheDocumentiSpesaGiaCaricati.get(id);
		}
		return null;
	}
	
	
	//UTILITIES:
	
	public List<SiacRDocStatoFin> filtraSiacRDocStatoFinBySiacTDocFin(SiacTDocFin siacTDocFin){
		List<SiacRDocStatoFin> filtrati = new ArrayList<SiacRDocStatoFin>();
		if(siacTDocFin!=null && distintiSiacRDocStatoFinCoinvolti!=null && distintiSiacRDocStatoFinCoinvolti.size()>0){
			Integer id = siacTDocFin.getDocId();
			if(id!=null){
				for(SiacRDocStatoFin it : distintiSiacRDocStatoFinCoinvolti){
					if(it.getSiacTDoc() !=null && it.getSiacTDoc().getDocId()!=null && 
							it.getSiacTDoc().getDocId().intValue()==id.intValue()){
						filtrati.add(it);
					}
				}
			}
		}
		filtrati = CommonUtils.soloValidiSiacTBase(filtrati, null);
		return filtrati;
	}
	
	public List<SiacRSubdocOrdinativoTFin> filtraSiacRSubdocOrdinativoTFinBySiacTOrdinativoTFin(SiacTOrdinativoTFin siacTOrdinativoTFin){
		List<SiacRSubdocOrdinativoTFin> filtrati = new ArrayList<SiacRSubdocOrdinativoTFin>();
		if(siacTOrdinativoTFin!=null && distintiSiacRSubdocOrdinativoTFinCoinvolti!=null && distintiSiacRSubdocOrdinativoTFinCoinvolti.size()>0){
			Integer id = siacTOrdinativoTFin.getOrdTsId();
			if(id!=null){
				for(SiacRSubdocOrdinativoTFin it : distintiSiacRSubdocOrdinativoTFinCoinvolti){
					if(it.getSiacTOrdinativoT() !=null && it.getSiacTOrdinativoT().getOrdTsId()!=null && 
							it.getSiacTOrdinativoT().getOrdTsId().intValue()==id.intValue()){
						filtrati.add(it);
					}
				}
			}
		}
		filtrati = CommonUtils.soloValidiSiacTBase(filtrati, null);
		return filtrati;
	}
	
	public List<SiacRLiquidazioneOrdFin> filtraSiacRLiquidazioneOrdFinBySiacTOrdinativoTFin(SiacTOrdinativoTFin siacTOrdinativoTFin){
		List<SiacRLiquidazioneOrdFin> filtrati = new ArrayList<SiacRLiquidazioneOrdFin>();
		if(siacTOrdinativoTFin!=null && distintiSiacRLiquidazioneOrdFinCoinvolti!=null && distintiSiacRLiquidazioneOrdFinCoinvolti.size()>0){
			Integer id = siacTOrdinativoTFin.getOrdTsId();
			if(id!=null){
				for(SiacRLiquidazioneOrdFin it : distintiSiacRLiquidazioneOrdFinCoinvolti){
					if(it.getSiacTOrdinativoT() !=null && it.getSiacTOrdinativoT().getOrdTsId()!=null && 
							it.getSiacTOrdinativoT().getOrdTsId().intValue()==id.intValue()){
						filtrati.add(it);
					}
				}
			}
		}
		filtrati = CommonUtils.soloValidiSiacTBase(filtrati, null);
		return filtrati;
	}
	
	public List<SiacRLiquidazioneOrdFin> filtraSiacRLiquidazioneOrdFinBySiacTLiquidazioneFinAncheVersoAltriOrdinativi(Integer idLiquidazione){
		SiacTLiquidazioneFin siacTLiquidazioneFin = new SiacTLiquidazioneFin();
		siacTLiquidazioneFin.setLiqId(idLiquidazione);
		return filtraSiacRLiquidazioneOrdFinBySiacTLiquidazioneFinAncheVersoAltriOrdinativi(siacTLiquidazioneFin);
	}
	
	public List<SiacRLiquidazioneOrdFin> filtraSiacRLiquidazioneOrdFinBySiacTLiquidazioneFinAncheVersoAltriOrdinativi(SiacTLiquidazioneFin siacTLiquidazioneFin){
		List<SiacRLiquidazioneOrdFin> filtrati = new ArrayList<SiacRLiquidazioneOrdFin>();
		if(siacTLiquidazioneFin!=null && distintiSiacRLiquidazioneOrdFinCoinvoltiAncheVersoAltriOrdinativi!=null && distintiSiacRLiquidazioneOrdFinCoinvoltiAncheVersoAltriOrdinativi.size()>0){
			Integer id = siacTLiquidazioneFin.getLiqId();
			if(id!=null){
				for(SiacRLiquidazioneOrdFin it : distintiSiacRLiquidazioneOrdFinCoinvoltiAncheVersoAltriOrdinativi){
					if(it.getSiacTLiquidazione()!=null && it.getSiacTLiquidazione().getLiqId()!=null && 
							it.getSiacTLiquidazione().getLiqId().intValue()==id.intValue()){
						filtrati.add(it);
					}
				}
			}
		}
		filtrati = CommonUtils.soloValidiSiacTBase(filtrati, null);
		return filtrati;
	}
	
	public List<SiacTOrdinativoTsDetFin> filtraSiacTOrdinativoTsDetFinBySiacTOrdinativoTFin(SiacTOrdinativoTFin siacTOrdinativoTFin){
		List<SiacTOrdinativoTsDetFin> filtrati = new ArrayList<SiacTOrdinativoTsDetFin>();
		if(siacTOrdinativoTFin!=null && distintiSiacTOrdinativoTsDetFinCoinvolti!=null && distintiSiacTOrdinativoTsDetFinCoinvolti.size()>0){
			Integer id = siacTOrdinativoTFin.getOrdTsId();
			if(id!=null){
				for(SiacTOrdinativoTsDetFin it : distintiSiacTOrdinativoTsDetFinCoinvolti){
					if(it.getSiacTOrdinativoT()!=null && it.getSiacTOrdinativoT().getOrdTsId()!=null && 
							it.getSiacTOrdinativoT().getOrdTsId().intValue()==id.intValue()){
						filtrati.add(it);
					}
				}
			}
		}
		filtrati = CommonUtils.soloValidiSiacTBase(filtrati, null);
		return filtrati;
	}
	
	
	
	public List<SiacTOrdinativoTFin> estraiSiacTOrdinativoTFinCoinvoltiBySiacRLiquidazioneOrdFinCoinvoltiAncheVersoAltriOrdinativi(){
		List<SiacTOrdinativoTFin> coinvolti = new ArrayList<SiacTOrdinativoTFin>();
		if(distintiSiacRLiquidazioneOrdFinCoinvoltiAncheVersoAltriOrdinativi!=null && distintiSiacRLiquidazioneOrdFinCoinvoltiAncheVersoAltriOrdinativi.size()>0){
			for(SiacRLiquidazioneOrdFin it : distintiSiacRLiquidazioneOrdFinCoinvoltiAncheVersoAltriOrdinativi){
				if(it!=null){
					coinvolti.add(it.getSiacTOrdinativoT());
				}
			}
		}
		coinvolti = CommonUtils.ritornaSoloDistintiByUid(coinvolti);
		return coinvolti;
	}
	
	public List<SiacTOrdinativoFin> estraiSiacTOrdinativoFinCoinvoltiBySiacRLiquidazioneOrdFinCoinvoltiAncheVersoAltriOrdinativi(){
		List<SiacTOrdinativoFin> coinvolti = new ArrayList<SiacTOrdinativoFin>();
		List<SiacTOrdinativoTFin> listaT = estraiSiacTOrdinativoTFinCoinvoltiBySiacRLiquidazioneOrdFinCoinvoltiAncheVersoAltriOrdinativi();
		if(listaT!=null && listaT.size()>0){
			for(SiacTOrdinativoTFin it : listaT){
				if(it!=null){
					coinvolti.add(it.getSiacTOrdinativo());
				}
			}
		}
		coinvolti = CommonUtils.ritornaSoloDistintiByUid(coinvolti);
		return coinvolti;
	}
	
	
	
	public List<SiacRLiquidazioneSoggettoFin> filtraSiacRLiquidazioneSoggettoByLiqId(Integer idLiquidazione){
		List<SiacRLiquidazioneSoggettoFin> filtrati = new ArrayList<SiacRLiquidazioneSoggettoFin>();
		if(idLiquidazione!=null && distintiSiacRLiquidazioneSoggetto!=null && distintiSiacRLiquidazioneSoggetto.size()>0){
			for(SiacRLiquidazioneSoggettoFin it : distintiSiacRLiquidazioneSoggetto){
				if(it.getSiacTLiquidazione().getLiqId().intValue()==idLiquidazione.intValue()){
					filtrati.add(it);
				}
			}
		}
		return filtrati;
	}
	
	public List<SiacRLiquidazioneClassFin> filtraSiacRLiquidazioneClassFinByLiqId(Integer idLiquidazione){
		List<SiacRLiquidazioneClassFin> filtrati = new ArrayList<SiacRLiquidazioneClassFin>();
		if(idLiquidazione!=null && distintiSiacRLiquidazioneClassFinCoinvolti!=null && distintiSiacRLiquidazioneClassFinCoinvolti.size()>0){
			for(SiacRLiquidazioneClassFin it : distintiSiacRLiquidazioneClassFinCoinvolti){
				if(it.getSiacTLiquidazione().getLiqId().intValue()==idLiquidazione.intValue()){
					filtrati.add(it);
				}
			}
		}
		return filtrati;
	}
	
	public List<SiacRLiquidazioneStatoFin> filtraSiacRLiquidazioneStatoFinByLiqId(Integer idLiquidazione){
		List<SiacRLiquidazioneStatoFin> filtrati = new ArrayList<SiacRLiquidazioneStatoFin>();
		if(idLiquidazione!=null && distintiSiacRLiquidazioneStatoFinCoinvolti!=null && distintiSiacRLiquidazioneStatoFinCoinvolti.size()>0){
			for(SiacRLiquidazioneStatoFin it : distintiSiacRLiquidazioneStatoFinCoinvolti){
				if(it.getSiacTLiquidazione().getLiqId().intValue()==idLiquidazione.intValue()){
					filtrati.add(it);
				}
			}
		}
		return filtrati;
	}
	
	public List<SiacRLiquidazioneAttrFin> filtraSiacRLiquidazioneAttrFinByLiqId(Integer idLiquidazione){
		List<SiacRLiquidazioneAttrFin> filtrati = new ArrayList<SiacRLiquidazioneAttrFin>();
		if(idLiquidazione!=null && distintiSiacRLiquidazioneAttrFinCoinvolti!=null && distintiSiacRLiquidazioneAttrFinCoinvolti.size()>0){
			for(SiacRLiquidazioneAttrFin it : distintiSiacRLiquidazioneAttrFinCoinvolti){
				if(it.getSiacTLiquidazione().getLiqId().intValue()==idLiquidazione.intValue()){
					filtrati.add(it);
				}
			}
		}
		return filtrati;
	}
	
	public List<SiacRLiquidazioneAttoAmmFin> filtraSiacRLiquidazioneAttoAmmFinByLiqId(Integer idLiquidazione){
		List<SiacRLiquidazioneAttoAmmFin> filtrati = new ArrayList<SiacRLiquidazioneAttoAmmFin>();
		if(idLiquidazione!=null && distintiSiacRLiquidazioneAttoAmmFinCoinvolti!=null && distintiSiacRLiquidazioneAttoAmmFinCoinvolti.size()>0){
			for(SiacRLiquidazioneAttoAmmFin it : distintiSiacRLiquidazioneAttoAmmFinCoinvolti){
				if(it.getSiacTLiquidazione().getLiqId().intValue()==idLiquidazione.intValue()){
					filtrati.add(it);
				}
			}
		}
		return filtrati;
	}
	
	public List<SiacRSubdocLiquidazioneFin> filtraSiacRSubdocLiquidazioneFinByLiqId(Integer idLiquidazione){
		List<SiacRSubdocLiquidazioneFin> filtrati = new ArrayList<SiacRSubdocLiquidazioneFin>();
		if(idLiquidazione!=null && distintiSiacRSubdocLiquidazioneFinCoinvolti!=null && distintiSiacRSubdocLiquidazioneFinCoinvolti.size()>0){
			for(SiacRSubdocLiquidazioneFin it : distintiSiacRSubdocLiquidazioneFinCoinvolti){
				if(it.getSiacTLiquidazione().getLiqId().intValue()==idLiquidazione.intValue()){
					filtrati.add(it);
				}
			}
		}
		return filtrati;
	}
	
	public List<SiacRSubdocLiquidazione> filtraSiacRSubdocLiquidazioneByLiqId(Integer idLiquidazione){
		List<SiacRSubdocLiquidazione> filtrati = new ArrayList<SiacRSubdocLiquidazione>();
		if(idLiquidazione!=null && distintiSiacRSubdocLiquidazioneCoinvolti!=null && distintiSiacRSubdocLiquidazioneCoinvolti.size()>0){
			for(SiacRSubdocLiquidazione it : distintiSiacRSubdocLiquidazioneCoinvolti){
				if(it.getSiacTLiquidazione().getLiqId().intValue()==idLiquidazione.intValue()){
					filtrati.add(it);
				}
			}
		}
		return filtrati;
	}
	
	public SiacRSubdocLiquidazione getSiacRSubdocLiquidazioneById(Integer subdocLiqId){
		return CommonUtils.getByIdSiacTBaseBil(distintiSiacRSubdocLiquidazioneCoinvolti, subdocLiqId);
	}
	
	
	public List<SiacRMutuoVoceLiquidazioneFin> filtraSiacRMutuoVoceLiquidazioneFinByLiqId(Integer idLiquidazione){
		List<SiacRMutuoVoceLiquidazioneFin> filtrati = new ArrayList<SiacRMutuoVoceLiquidazioneFin>();
		if(idLiquidazione!=null && distintiSiacRMutuoVoceLiquidazioneFinCoinvolti!=null && distintiSiacRMutuoVoceLiquidazioneFinCoinvolti.size()>0){
			for(SiacRMutuoVoceLiquidazioneFin it : distintiSiacRMutuoVoceLiquidazioneFinCoinvolti){
				if(it.getSiacTLiquidazione().getLiqId().intValue()==idLiquidazione.intValue()){
					filtrati.add(it);
				}
			}
		}
		return filtrati;
	}
	
	public List<SiacRLiquidazioneMovgestFin> filtraSiacRLiquidazioneMovgestFinByLiqId(Integer idLiquidazione){
		List<SiacRLiquidazioneMovgestFin> filtrati = new ArrayList<SiacRLiquidazioneMovgestFin>();
		if(idLiquidazione!=null && distintiSiacRLiquidazioneMovgestFinCoinvolti!=null && distintiSiacRLiquidazioneMovgestFinCoinvolti.size()>0){
			for(SiacRLiquidazioneMovgestFin it : distintiSiacRLiquidazioneMovgestFinCoinvolti){
				if(it.getSiacTLiquidazione().getLiqId().intValue()==idLiquidazione.intValue()){
					filtrati.add(it);
				}
			}
		}
		return filtrati;
	}
	
	public List<SiacROrdinativoStatoFin> filtraSiacROrdinativoStatoFinBySiacTOrdinativoFin(SiacTOrdinativoFin siacTOrdinativo){
		List<SiacROrdinativoStatoFin> filtrati = new ArrayList<SiacROrdinativoStatoFin>();
		if(siacTOrdinativo!=null && distintiSiacROrdinativoStatoFinCoinvolti!=null && distintiSiacROrdinativoStatoFinCoinvolti.size()>0){
			Integer id = siacTOrdinativo.getOrdId();
			if(id!=null){
				for(SiacROrdinativoStatoFin it : distintiSiacROrdinativoStatoFinCoinvolti){
					if(it.getSiacTOrdinativo()!=null && it.getSiacTOrdinativo().getOrdId()!=null && 
							it.getSiacTOrdinativo().getOrdId().intValue()==id.intValue()){
						filtrati.add(it);
					}
				}
			}
		}
		filtrati = CommonUtils.soloValidiSiacTBase(filtrati, null);
		return filtrati;
	}
	
	public List<SiacTMovgestTsFin> estraiDistintiSiacTMovgestTsFinBySiacRLiquidazioneMovgestFinCoinvolti(){
		List<SiacTMovgestTsFin> coinvolti = new ArrayList<SiacTMovgestTsFin>();
		if(distintiSiacRLiquidazioneMovgestFinCoinvolti!=null && distintiSiacRLiquidazioneMovgestFinCoinvolti.size()>0){
			for(SiacRLiquidazioneMovgestFin it : distintiSiacRLiquidazioneMovgestFinCoinvolti){
				if(it!=null && it.getSiacTMovgestTs()!=null){
					coinvolti.add(it.getSiacTMovgestTs());
				}
			}
		}
		coinvolti = CommonUtils.ritornaSoloDistintiByUid(coinvolti);
		return coinvolti;
	}
	
	public List<SiacTSoggettoFin> estraiDistintiSiacTSoggettoFinBySiacROrdinativoSoggettoFinCoinvolti(){
		List<SiacTSoggettoFin> coinvolti = new ArrayList<SiacTSoggettoFin>();
		if(distintiSiacROrdinativoSoggettoFinCoinvolti!=null && distintiSiacROrdinativoSoggettoFinCoinvolti.size()>0){
			for(SiacROrdinativoSoggettoFin it : distintiSiacROrdinativoSoggettoFinCoinvolti){
				if(it!=null && it.getSiacTSoggetto()!=null){
					coinvolti.add(it.getSiacTSoggetto());
				}
			}
		}
		coinvolti = CommonUtils.ritornaSoloDistintiByUid(coinvolti);
		return coinvolti;
	}
	
	public List<SiacTMovgestFin> estraiSiacTMovgestFinBySiacRLiquidazioneMovgestFinCoinvolti(){
		List<SiacTMovgestFin> coinvolti = new ArrayList<SiacTMovgestFin>();
		List<SiacTMovgestTsFin> distintiSiacTMovgestTsFin = estraiDistintiSiacTMovgestTsFinBySiacRLiquidazioneMovgestFinCoinvolti();
		if(distintiSiacTMovgestTsFin!=null && distintiSiacTMovgestTsFin.size()>0){
			for(SiacTMovgestTsFin it : distintiSiacTMovgestTsFin){
				if(it!=null && it.getSiacTMovgest()!=null){
					coinvolti.add(it.getSiacTMovgest());
				}
			}
		}
		coinvolti = CommonUtils.ritornaSoloDistintiByUid(coinvolti);
		return coinvolti;
	}
	
	public List<SiacTLiquidazioneFin> estraiSiacTLiquidazioneFinCoinvoltiBySiacRLiquidazioneOrdFinCoinvolti(){
		List<SiacTLiquidazioneFin> coinvolti = new ArrayList<SiacTLiquidazioneFin>();
		if(distintiSiacRLiquidazioneOrdFinCoinvolti!=null && distintiSiacRLiquidazioneOrdFinCoinvolti.size()>0){
			for(SiacRLiquidazioneOrdFin it : distintiSiacRLiquidazioneOrdFinCoinvolti){
				if(it!=null){
					coinvolti.add(it.getSiacTLiquidazione());
				}
			}
		}
		coinvolti = CommonUtils.ritornaSoloDistintiByUid(coinvolti);
		return coinvolti;
	}
	
	public List<SiacTModpagFin> estraiSiacTModpagFinBySiacTLiquidazioneFinCoinvoltiDaOrdFinCoinvolti(){
		List<SiacTModpagFin> coinvolti = new ArrayList<SiacTModpagFin>();
		List<SiacTLiquidazioneFin> liquidazioni = estraiSiacTLiquidazioneFinCoinvoltiBySiacRLiquidazioneOrdFinCoinvolti();
		if(liquidazioni!=null && liquidazioni.size()>0){
			for(SiacTLiquidazioneFin it : liquidazioni){
				if(it!=null){
					coinvolti.add(it.getSiacTModpag());
				}
			}
		}
		coinvolti = CommonUtils.ritornaSoloDistintiByUid(coinvolti);
		return coinvolti;
	}
	
	public List<SiacTModpagFin> estraiSiacTModpagFinBySiacROrdinativoModpagFinCoinvolti(){
		List<SiacTModpagFin> coinvolti = new ArrayList<SiacTModpagFin>();
		if(distintiSiacROrdinativoModpagFinCoinvolti!=null && distintiSiacROrdinativoModpagFinCoinvolti.size()>0){
			for(SiacROrdinativoModpagFin it : distintiSiacROrdinativoModpagFinCoinvolti){
				if(it!=null){
					coinvolti.add(it.getSiacTModpag());
				}
			}
		}
		coinvolti = CommonUtils.ritornaSoloDistintiByUid(coinvolti);
		return coinvolti;
	}
	
	
	
	public List<SiacTSubdocFin> estraiSiacTSubdocFinCoinvoltiBySiacRSubdocOrdinativoTFinCoinvolti(){
		List<SiacTSubdocFin> coinvolti = new ArrayList<SiacTSubdocFin>();
		if(distintiSiacRSubdocOrdinativoTFinCoinvolti!=null && distintiSiacRSubdocOrdinativoTFinCoinvolti.size()>0){
			for(SiacRSubdocOrdinativoTFin it : distintiSiacRSubdocOrdinativoTFinCoinvolti){
				if(it!=null){
					coinvolti.add(it.getSiacTSubdoc());
				}
			}
		}
		coinvolti = CommonUtils.ritornaSoloDistintiByUid(coinvolti);
		return coinvolti;
	}
	
	public List<SiacTDocFin> estraiSiacTDocFinCoinvoltiBySiacRSubdocOrdinativoTFinCoinvolti(){
		List<SiacTDocFin> coinvolti = new ArrayList<SiacTDocFin>();
		List<SiacTSubdocFin> coinvoltiSiacTSubdocFin = estraiSiacTSubdocFinCoinvoltiBySiacRSubdocOrdinativoTFinCoinvolti();
		if(coinvoltiSiacTSubdocFin!=null && coinvoltiSiacTSubdocFin.size()>0){
			for(SiacTSubdocFin it : coinvoltiSiacTSubdocFin){
				if(it!=null){
					coinvolti.add(it.getSiacTDoc());
				}
			}
		}
		coinvolti = CommonUtils.ritornaSoloDistintiByUid(coinvolti);
		return coinvolti;
	}
	
	public List<SiacTSoggettoFin> estraiSiacTSoggettoFinCoinvoltiBySiacRLiquidazioneSoggettoFinCoinvolti(){
		List<SiacTSoggettoFin> coinvolti = new ArrayList<SiacTSoggettoFin>();
		if(distintiSiacRLiquidazioneSoggetto!=null && distintiSiacRLiquidazioneSoggetto.size()>0){
			for(SiacRLiquidazioneSoggettoFin it : distintiSiacRLiquidazioneSoggetto){
				if(it!=null){
					coinvolti.add(it.getSiacTSoggetto());
				}
			}
		}
		coinvolti = CommonUtils.ritornaSoloDistintiByUid(coinvolti);
		return coinvolti;
	}
	
	
	public BigDecimal estraiImportoSubOrdinativo(SiacTOrdinativoTFin sub, String tipoImporto){
		List<SiacTOrdinativoTsDetFin> listaDet = filtraSiacTOrdinativoTsDetFinBySiacTOrdinativoTFin(sub);
		BigDecimal importo = BigDecimal.ZERO;
		for(SiacTOrdinativoTsDetFin detIt : listaDet){
			if(CommonUtils.isValidoSiacTBase(detIt, null)){
				if(tipoImporto.equalsIgnoreCase(detIt.getSiacDOrdinativoTsDetTipo().getOrdTsDetTipoCode())){
					importo = detIt.getOrdTsDetImporto();
					break;
				}
			}		
		}
		return importo;
	}

	public List<SiacRLiquidazioneSoggettoFin> getDistintiSiacRLiquidazioneSoggetto() {
		return distintiSiacRLiquidazioneSoggetto;
	}

	public void setDistintiSiacRLiquidazioneSoggetto(
			List<SiacRLiquidazioneSoggettoFin> distintiSiacRLiquidazioneSoggetto) {
		this.distintiSiacRLiquidazioneSoggetto = distintiSiacRLiquidazioneSoggetto;
	}

	public List<SiacRLiquidazioneOrdFin> getDistintiSiacRLiquidazioneOrdFinCoinvolti() {
		return distintiSiacRLiquidazioneOrdFinCoinvolti;
	}

	public void setDistintiSiacRLiquidazioneOrdFinCoinvolti(
			List<SiacRLiquidazioneOrdFin> distintiSiacRLiquidazioneOrdFinCoinvolti) {
		this.distintiSiacRLiquidazioneOrdFinCoinvolti = distintiSiacRLiquidazioneOrdFinCoinvolti;
	}

	public List<SiacRLiquidazioneMovgestFin> getDistintiSiacRLiquidazioneMovgestFinCoinvolti() {
		return distintiSiacRLiquidazioneMovgestFinCoinvolti;
	}

	public void setDistintiSiacRLiquidazioneMovgestFinCoinvolti(
			List<SiacRLiquidazioneMovgestFin> distintiSiacRLiquidazioneMovgestFinCoinvolti) {
		this.distintiSiacRLiquidazioneMovgestFinCoinvolti = distintiSiacRLiquidazioneMovgestFinCoinvolti;
	}

	public OttimizzazioneSoggettoDto getOttimizzazioneSoggettoDto() {
		return ottimizzazioneSoggettoDto;
	}

	public void setOttimizzazioneSoggettoDto(
			OttimizzazioneSoggettoDto ottimizzazioneSoggettoDto) {
		this.ottimizzazioneSoggettoDto = ottimizzazioneSoggettoDto;
	}

	public List<SiacTOrdinativoTFin> getListaSubOrdinativiCoinvolti() {
		return listaSubOrdinativiCoinvolti;
	}

	public void setListaSubOrdinativiCoinvolti(
			List<SiacTOrdinativoTFin> listaSubOrdinativiCoinvolti) {
		this.listaSubOrdinativiCoinvolti = listaSubOrdinativiCoinvolti;
	}

	public SiacTLiquidazioneFin getSiacTLiquidazioneConsiderata() {
		return siacTLiquidazioneConsiderata;
	}

	public void setSiacTLiquidazioneConsiderata(
			SiacTLiquidazioneFin siacTLiquidazioneConsiderata) {
		this.siacTLiquidazioneConsiderata = siacTLiquidazioneConsiderata;
	}

	public List<SiacRLiquidazioneStatoFin> getDistintiSiacRLiquidazioneStatoFinCoinvolti() {
		return distintiSiacRLiquidazioneStatoFinCoinvolti;
	}

	public void setDistintiSiacRLiquidazioneStatoFinCoinvolti(
			List<SiacRLiquidazioneStatoFin> distintiSiacRLiquidazioneStatoFinCoinvolti) {
		this.distintiSiacRLiquidazioneStatoFinCoinvolti = distintiSiacRLiquidazioneStatoFinCoinvolti;
	}

	public List<SiacRLiquidazioneClassFin> getDistintiSiacRLiquidazioneClassFinCoinvolti() {
		return distintiSiacRLiquidazioneClassFinCoinvolti;
	}

	public void setDistintiSiacRLiquidazioneClassFinCoinvolti(
			List<SiacRLiquidazioneClassFin> distintiSiacRLiquidazioneClassFinCoinvolti) {
		this.distintiSiacRLiquidazioneClassFinCoinvolti = distintiSiacRLiquidazioneClassFinCoinvolti;
	}

	public List<SiacRLiquidazioneAttrFin> getDistintiSiacRLiquidazioneAttrFinCoinvolti() {
		return distintiSiacRLiquidazioneAttrFinCoinvolti;
	}

	public void setDistintiSiacRLiquidazioneAttrFinCoinvolti(
			List<SiacRLiquidazioneAttrFin> distintiSiacRLiquidazioneAttrFinCoinvolti) {
		this.distintiSiacRLiquidazioneAttrFinCoinvolti = distintiSiacRLiquidazioneAttrFinCoinvolti;
	}

	public List<SiacRLiquidazioneAttoAmmFin> getDistintiSiacRLiquidazioneAttoAmmFinCoinvolti() {
		return distintiSiacRLiquidazioneAttoAmmFinCoinvolti;
	}

	public void setDistintiSiacRLiquidazioneAttoAmmFinCoinvolti(
			List<SiacRLiquidazioneAttoAmmFin> distintiSiacRLiquidazioneAttoAmmFinCoinvolti) {
		this.distintiSiacRLiquidazioneAttoAmmFinCoinvolti = distintiSiacRLiquidazioneAttoAmmFinCoinvolti;
	}

	public List<SiacRSubdocLiquidazioneFin> getDistintiSiacRSubdocLiquidazioneFinCoinvolti() {
		return distintiSiacRSubdocLiquidazioneFinCoinvolti;
	}

	public void setDistintiSiacRSubdocLiquidazioneFinCoinvolti(
			List<SiacRSubdocLiquidazioneFin> distintiSiacRSubdocLiquidazioneFinCoinvolti) {
		this.distintiSiacRSubdocLiquidazioneFinCoinvolti = distintiSiacRSubdocLiquidazioneFinCoinvolti;
	}

	public List<SiacRMutuoVoceLiquidazioneFin> getDistintiSiacRMutuoVoceLiquidazioneFinCoinvolti() {
		return distintiSiacRMutuoVoceLiquidazioneFinCoinvolti;
	}

	public void setDistintiSiacRMutuoVoceLiquidazioneFinCoinvolti(
			List<SiacRMutuoVoceLiquidazioneFin> distintiSiacRMutuoVoceLiquidazioneFinCoinvolti) {
		this.distintiSiacRMutuoVoceLiquidazioneFinCoinvolti = distintiSiacRMutuoVoceLiquidazioneFinCoinvolti;
	}

	public List<SiacRSubdocLiquidazione> getDistintiSiacRSubdocLiquidazioneCoinvolti() {
		return distintiSiacRSubdocLiquidazioneCoinvolti;
	}

	public void setDistintiSiacRSubdocLiquidazioneCoinvolti(
			List<SiacRSubdocLiquidazione> distintiSiacRSubdocLiquidazioneCoinvolti) {
		this.distintiSiacRSubdocLiquidazioneCoinvolti = distintiSiacRSubdocLiquidazioneCoinvolti;
	}

	public List<SiacRSubdocOrdinativoTFin> getDistintiSiacRSubdocOrdinativoTFinCoinvolti() {
		return distintiSiacRSubdocOrdinativoTFinCoinvolti;
	}

	public void setDistintiSiacRSubdocOrdinativoTFinCoinvolti(
			List<SiacRSubdocOrdinativoTFin> distintiSiacRSubdocOrdinativoTFinCoinvolti) {
		this.distintiSiacRSubdocOrdinativoTFinCoinvolti = distintiSiacRSubdocOrdinativoTFinCoinvolti;
	}

	public List<SiacRDocStatoFin> getDistintiSiacRDocStatoFinCoinvolti() {
		return distintiSiacRDocStatoFinCoinvolti;
	}

	public void setDistintiSiacRDocStatoFinCoinvolti(
			List<SiacRDocStatoFin> distintiSiacRDocStatoFinCoinvolti) {
		this.distintiSiacRDocStatoFinCoinvolti = distintiSiacRDocStatoFinCoinvolti;
	}

	public OttimizzazioneModalitaPagamentoDto getModalitaPagamentoLiquidazioniCoinvolte() {
		return modalitaPagamentoLiquidazioniCoinvolte;
	}

	public void setModalitaPagamentoLiquidazioniCoinvolte(
			OttimizzazioneModalitaPagamentoDto modalitaPagamentoLiquidazioniCoinvolte) {
		this.modalitaPagamentoLiquidazioniCoinvolte = modalitaPagamentoLiquidazioniCoinvolte;
	}

	public List<SiacROrdinativoModpagFin> getDistintiSiacROrdinativoModpagFinCoinvolti() {
		return distintiSiacROrdinativoModpagFinCoinvolti;
	}

	public void setDistintiSiacROrdinativoModpagFinCoinvolti(
			List<SiacROrdinativoModpagFin> distintiSiacROrdinativoModpagFinCoinvolti) {
		this.distintiSiacROrdinativoModpagFinCoinvolti = distintiSiacROrdinativoModpagFinCoinvolti;
	}

	public OttimizzazioneModalitaPagamentoDto getModalitaPagamentoOrdinativo() {
		return modalitaPagamentoOrdinativo;
	}

	public void setModalitaPagamentoOrdinativo(
			OttimizzazioneModalitaPagamentoDto modalitaPagamentoOrdinativo) {
		this.modalitaPagamentoOrdinativo = modalitaPagamentoOrdinativo;
	}

	public List<SiacROrdinativoSoggettoFin> getDistintiSiacROrdinativoSoggettoFinCoinvolti() {
		return distintiSiacROrdinativoSoggettoFinCoinvolti;
	}

	public void setDistintiSiacROrdinativoSoggettoFinCoinvolti(
			List<SiacROrdinativoSoggettoFin> distintiSiacROrdinativoSoggettoFinCoinvolti) {
		this.distintiSiacROrdinativoSoggettoFinCoinvolti = distintiSiacROrdinativoSoggettoFinCoinvolti;
	}

	public OttimizzazioneMovGestDto getOttimizzazioneMovimentiCoinvolti() {
		return ottimizzazioneMovimentiCoinvolti;
	}

	public void setOttimizzazioneMovimentiCoinvolti(
			OttimizzazioneMovGestDto ottimizzazioneMovimentiCoinvolti) {
		this.ottimizzazioneMovimentiCoinvolti = ottimizzazioneMovimentiCoinvolti;
	}

	public List<SiacRLiquidazioneOrdFin> getDistintiSiacRLiquidazioneOrdFinCoinvoltiAncheVersoAltriOrdinativi() {
		return distintiSiacRLiquidazioneOrdFinCoinvoltiAncheVersoAltriOrdinativi;
	}

	public void setDistintiSiacRLiquidazioneOrdFinCoinvoltiAncheVersoAltriOrdinativi(
			List<SiacRLiquidazioneOrdFin> distintiSiacRLiquidazioneOrdFinCoinvoltiAncheVersoAltriOrdinativi) {
		this.distintiSiacRLiquidazioneOrdFinCoinvoltiAncheVersoAltriOrdinativi = distintiSiacRLiquidazioneOrdFinCoinvoltiAncheVersoAltriOrdinativi;
	}

	public List<SiacROrdinativoStatoFin> getDistintiSiacROrdinativoStatoFinCoinvolti() {
		return distintiSiacROrdinativoStatoFinCoinvolti;
	}

	public void setDistintiSiacROrdinativoStatoFinCoinvolti(
			List<SiacROrdinativoStatoFin> distintiSiacROrdinativoStatoFinCoinvolti) {
		this.distintiSiacROrdinativoStatoFinCoinvolti = distintiSiacROrdinativoStatoFinCoinvolti;
	}

	public List<SiacTOrdinativoTsDetFin> getDistintiSiacTOrdinativoTsDetFinCoinvolti() {
		return distintiSiacTOrdinativoTsDetFinCoinvolti;
	}

	public void setDistintiSiacTOrdinativoTsDetFinCoinvolti(
			List<SiacTOrdinativoTsDetFin> distintiSiacTOrdinativoTsDetFinCoinvolti) {
		this.distintiSiacTOrdinativoTsDetFinCoinvolti = distintiSiacTOrdinativoTsDetFinCoinvolti;
	}

	public HashMap<Integer, DocumentoSpesa> getCacheDocumentiSpesaGiaCaricati() {
		return cacheDocumentiSpesaGiaCaricati;
	}

	public void setCacheDocumentiSpesaGiaCaricati(
			HashMap<Integer, DocumentoSpesa> cacheDocumentiSpesaGiaCaricati) {
		this.cacheDocumentiSpesaGiaCaricati = cacheDocumentiSpesaGiaCaricati;
	}
	
}
