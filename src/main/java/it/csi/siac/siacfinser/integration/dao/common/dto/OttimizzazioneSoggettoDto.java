/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.common.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacfinser.CommonUtils;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.integration.entity.SiacRComuneProvinciaFin;
import it.csi.siac.siacfinser.integration.entity.SiacRComuneRegioneFin;
import it.csi.siac.siacfinser.integration.entity.SiacRFormaGiuridicaFin;
import it.csi.siac.siacfinser.integration.entity.SiacRLiquidazioneSoggettoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRModpagOrdineFin;
import it.csi.siac.siacfinser.integration.entity.SiacRModpagStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsSogFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsSogclasseFin;
import it.csi.siac.siacfinser.integration.entity.SiacRProvinciaRegioneFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoAttrFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoClasseFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoOnereFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoRelazFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoRelazStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoTipoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggrelModpagFin;
import it.csi.siac.siacfinser.integration.entity.SiacTComuneFin;
import it.csi.siac.siacfinser.integration.entity.SiacTFormaGiuridicaFin;
import it.csi.siac.siacfinser.integration.entity.SiacTIndirizzoSoggettoFin;
import it.csi.siac.siacfinser.integration.entity.SiacTModpagFin;
import it.csi.siac.siacfinser.integration.entity.SiacTModpagModFin;
import it.csi.siac.siacfinser.integration.entity.SiacTNazioneFin;
import it.csi.siac.siacfinser.integration.entity.SiacTPersonaFisicaFin;
import it.csi.siac.siacfinser.integration.entity.SiacTPersonaGiuridicaFin;
import it.csi.siac.siacfinser.integration.entity.SiacTProvinciaFin;
import it.csi.siac.siacfinser.integration.entity.SiacTRecapitoSoggettoFin;
import it.csi.siac.siacfinser.integration.entity.SiacTRegioneFin;
import it.csi.siac.siacfinser.integration.entity.SiacTSoggettoFin;
import it.csi.siac.siacfinser.integration.entity.SiacTSoggettoModFin;
import it.csi.siac.siacfinser.integration.util.DatiOperazioneUtils;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

public class OttimizzazioneSoggettoDto implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2573703973624140188L;
	
	/**
	 * Variabile che serve quando di passa da un metodo ad un altro e si puo' sfruttare il fatto di aver gia' caricato dei soggetti 
	 * interi in precedenza nell'esecuzione del codice:
	 */
	private List<Soggetto> soggettiGiaCaricati;
	//
	
	private List<SiacTSoggettoFin> distintiSiacTSoggettiCoinvolti;//somma di quelli in input piu' i loro relazionati
	
	private List<SiacRMovgestTsSogclasseFin> distintiSiacRMovgestTsSogclasseFinCoinvolti;
	
	private List<SiacTSoggettoFin> distintiSiacTSoggettiCoinvoltiDirettamente;//quelli relazionati a quelli in input ai metodi di ottimizzazione
	private List<SiacTSoggettoFin> distintiSiacTSoggettiCoinvoltiTramiteRelazioni;//quelli in input ai metodi di ottimizzazione
	
	
	private List<SiacRMovgestTsSogFin> distintiSiacRSoggettiCoinvolti;
	
	private List<SiacRSoggettoStatoFin> distintiSiacRSoggettoStatoCoinvolti;
	private List<SiacRFormaGiuridicaFin> distintiSiacRFormaGiuridicaCoinvolti;
	private List<SiacTFormaGiuridicaFin> distintiSiacTFormaGiuridicaCoinvolti;
	private List<SiacRSoggettoAttrFin> distintiSiacRSoggettoAttrCoinvolti;
	
	private List<SiacRSoggettoRelazFin> distintiSiacRSoggettoRelaz;
	
	
	private List<SiacRSoggettoClasseFin> distintiSiacRSoggettoClasses;
	private List<SiacRSoggettoTipoFin> distintiSiacRSoggettoTipo;
	
	private List<SiacTRecapitoSoggettoFin> distintiSiacTRecapitoSoggetto;
	private List<SiacRSoggettoOnereFin> distintiSiacRSoggettoOnere;
	private List<SiacTIndirizzoSoggettoFin> distintiSiacTIndirizzoSoggetto;
	
	private List<SiacTPersonaFisicaFin> distintiSiacTPersonaFisica;
	private List<SiacTPersonaGiuridicaFin> distintiSiacTPersonaGiuridica;
	
	private List<SiacTModpagFin> distintiSiacTModpagFinCoinvolti;
	
	private List<SiacRSoggrelModpagFin> distintiSiacRSoggrelModpagFinCoinvolti;
	private List<SiacRSoggettoRelazStatoFin> distintSiacRSoggettoRelazStatoFinCoinvolti;
	
	private List<SiacTModpagModFin> distintiSiacTModpagModFinCoinvolti;
	
	private List<SiacRModpagStatoFin> distintiSiacRModpagStatoFinCoinvolti;
	
	private List<SiacRModpagOrdineFin> distintiSiacRModpagOrdineFinCoinvolti;
	
	private List<SiacTSoggettoModFin> distintiSiacTSoggettoModFinCoinvolti;
	
	//verso LIQUIDAZIONI:
	private List<SiacRLiquidazioneSoggettoFin> distintiSiacRLiquidazioneSoggetto;
	///
	
	
	//Verso comune-prov-regione:
	private List<SiacTComuneFin> distintiSiacTComuneFinCoinvolti;
	private List<SiacRComuneProvinciaFin> distintiSiacRComuneProvinciaFinCoinvolti;
	private List<SiacRComuneRegioneFin> distintiSiacRComuneRegioneFinCoinvolti; 
	private List<SiacTProvinciaFin> distintiSiacTProvinciaFinCoinvolti;
	private List<SiacRProvinciaRegioneFin> distintiSiacRProvinciaRegioneFinCoinvolti;
	private List<SiacTRegioneFin> distintiSiacTRegioneFinCoinvolti;
	private List<SiacTNazioneFin> distintiSiacTNazioneFinCoinvolti;
	//
	
	
	//UTILITIES:
	
	public List<SiacTSoggettoFin> estraiSediSecondarie(Integer idSoggetto){
		List<SiacTSoggettoFin> listaSedi = new ArrayList<SiacTSoggettoFin>();
		if(idSoggetto!=null){
			List<SiacRSoggettoRelazFin> filtratiBySoggetto1 = filtraSiacRSoggettoRelaz1BySoggettoId(idSoggetto);
			List<SiacRSoggettoRelazFin> relazSediSecondarie = filtraByTipoRelazione(filtratiBySoggetto1, Constanti.SEDE_SECONDARIA);
			if(relazSediSecondarie!=null && relazSediSecondarie.size()>0){
				for(SiacRSoggettoRelazFin sedeIt : relazSediSecondarie){
					if(sedeIt!=null && sedeIt.getSiacTSoggetto2()!=null){
						listaSedi.add(sedeIt.getSiacTSoggetto2());
					}
				}
			}
		}
		return listaSedi;
	}
	
	public List<SiacRSoggettoRelazFin> filtraByTipoRelazione(List<SiacRSoggettoRelazFin> listaDaFiltrare, String codiceTipoRelazione){
		List<SiacRSoggettoRelazFin> listaFiltrata = new ArrayList<SiacRSoggettoRelazFin>();
		if(listaDaFiltrare!=null && listaDaFiltrare.size()>0 && codiceTipoRelazione!=null){
			for(SiacRSoggettoRelazFin relazIterata : listaDaFiltrare){
				if(relazIterata!=null && 
						relazIterata.getSiacDRelazTipo()!=null 
						&& codiceTipoRelazione.equalsIgnoreCase(relazIterata.getSiacDRelazTipo().getRelazTipoCode())){
					listaFiltrata.add(relazIterata);
				}
				
			}
		}
		return listaFiltrata;
	}
	
	public List<SiacRModpagStatoFin> filtraSiacRModpagStatoFinBySiacTModpagFin(SiacTModpagFin siacTModpagFin){
		List<SiacRModpagStatoFin> filtrati = new ArrayList<SiacRModpagStatoFin>();
		if(siacTModpagFin!=null && distintiSiacRModpagStatoFinCoinvolti!=null && distintiSiacRModpagStatoFinCoinvolti.size()>0){
			Integer idModPag = siacTModpagFin.getModpagId();
			if(idModPag!=null){
				for(SiacRModpagStatoFin it : distintiSiacRModpagStatoFinCoinvolti){
					if(it.getSiacTModpag().getModpagId().intValue()==idModPag.intValue()){
						filtrati.add(it);
					}
				}
			}
		}
		filtrati = CommonUtils.soloValidiSiacTBase(filtrati, null);
		return filtrati;
	}
	
	public List<SiacRModpagOrdineFin> filtraSiacRModpagOrdineFinBySiacTModpagFin(SiacTModpagFin siacTModpagFin){
		List<SiacRModpagOrdineFin> filtrati = new ArrayList<SiacRModpagOrdineFin>();
		if(siacTModpagFin!=null && distintiSiacRModpagOrdineFinCoinvolti!=null && distintiSiacRModpagOrdineFinCoinvolti.size()>0){
			Integer idModPag = siacTModpagFin.getModpagId();
			if(idModPag!=null){
				for(SiacRModpagOrdineFin it : distintiSiacRModpagOrdineFinCoinvolti){
					if(it!=null && it.getSiacTModpag()!=null && it.getSiacTModpag().getModpagId()!=null &&
							it.getSiacTModpag().getModpagId().intValue()==idModPag.intValue()){
						filtrati.add(it);
					}
				}
			}
		}
		filtrati = CommonUtils.soloValidiSiacTBase(filtrati, null);
		return filtrati;
	}
	
	public List<SiacRModpagOrdineFin> filtraSiacRModpagOrdineFinBySiacTSoggettoFin(SiacTSoggettoFin siacTSoggetto){
		List<SiacRModpagOrdineFin> filtrati = new ArrayList<SiacRModpagOrdineFin>();
		if(siacTSoggetto!=null && distintiSiacRModpagOrdineFinCoinvolti!=null && distintiSiacRModpagOrdineFinCoinvolti.size()>0){
			Integer soggId = siacTSoggetto.getSoggettoId();
			if(soggId!=null){
				for(SiacRModpagOrdineFin it : distintiSiacRModpagOrdineFinCoinvolti){
					if(it.getSiacTSoggetto().getSoggettoId().intValue()==soggId.intValue()){
						filtrati.add(it);
					}
				}
			}
		}
		filtrati = CommonUtils.soloValidiSiacTBase(filtrati, null);
		return filtrati;
	}
	
	public List<SiacTModpagFin> filtraSiacTModpagFinBySoggettoId(Integer idSoggetto){
		List<SiacTModpagFin> filtrati = new ArrayList<SiacTModpagFin>();
		if(idSoggetto!=null && distintiSiacTModpagFinCoinvolti!=null && distintiSiacTModpagFinCoinvolti.size()>0){
			for(SiacTModpagFin it : distintiSiacTModpagFinCoinvolti){
				if(it.getSiacTSoggetto().getSoggettoId().intValue()==idSoggetto.intValue()){
					filtrati.add(it);
				}
			}
		}
		filtrati = CommonUtils.soloValidiSiacTBase(filtrati, null);
		return filtrati;
	}
	
	public List<SiacRSoggettoStatoFin> filtraSiacRSoggettoStatoBySoggettoId(Integer idSoggetto){
		List<SiacRSoggettoStatoFin> filtrati = new ArrayList<SiacRSoggettoStatoFin>();
		if(idSoggetto!=null && distintiSiacRSoggettoStatoCoinvolti!=null && distintiSiacRSoggettoStatoCoinvolti.size()>0){
			for(SiacRSoggettoStatoFin it : distintiSiacRSoggettoStatoCoinvolti){
				if(it.getSiacTSoggetto().getSoggettoId().intValue()==idSoggetto.intValue()){
					filtrati.add(it);
				}
			}
		}
		return filtrati;
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
	
	public SiacRComuneRegioneFin getSiacRComuneRegioneValidoBySiacTComuneFin(SiacTComuneFin siacTComuneFin){
		List<SiacRComuneRegioneFin> filtrati = new ArrayList<SiacRComuneRegioneFin>();
		if(siacTComuneFin!=null){
			Integer idComune = siacTComuneFin.getComuneId();
			if(idComune!=null){
				if(idComune!=null && distintiSiacRComuneRegioneFinCoinvolti!=null && distintiSiacRComuneRegioneFinCoinvolti.size()>0){
					for(SiacRComuneRegioneFin it : distintiSiacRComuneRegioneFinCoinvolti){
						if(it.getSiacTComune().getComuneId().intValue()==idComune.intValue()){
							filtrati.add(it);
						}
					}
				}
			}
		}
		SiacRComuneRegioneFin valido = CommonUtils.getValidoSiacTBase(filtrati, null);
		return valido;
	}
	
	public SiacRComuneProvinciaFin getSiacRComuneProvinciaValidoBySiacTComuneFin(SiacTComuneFin siacTComuneFin){
		List<SiacRComuneProvinciaFin> filtrati = new ArrayList<SiacRComuneProvinciaFin>();
		if(siacTComuneFin!=null){
			Integer idComune = siacTComuneFin.getComuneId();
			if(idComune!=null){
				if(idComune!=null && distintiSiacRComuneProvinciaFinCoinvolti!=null && distintiSiacRComuneProvinciaFinCoinvolti.size()>0){
					for(SiacRComuneProvinciaFin it : distintiSiacRComuneProvinciaFinCoinvolti){
						if(it.getSiacTComune().getComuneId().intValue()==idComune.intValue()){
							filtrati.add(it);
						}
					}
				}
			}
		}
		SiacRComuneProvinciaFin valido = CommonUtils.getValidoSiacTBase(filtrati, null);
		return valido;
	}
	
	public List<SiacRFormaGiuridicaFin> filtraSiacRFormaGiuridicaBySoggettoId(Integer idSoggetto){
		List<SiacRFormaGiuridicaFin> filtrati = new ArrayList<SiacRFormaGiuridicaFin>();
		if(idSoggetto!=null && distintiSiacRFormaGiuridicaCoinvolti!=null && distintiSiacRFormaGiuridicaCoinvolti.size()>0){
			for(SiacRFormaGiuridicaFin it : distintiSiacRFormaGiuridicaCoinvolti){
				if(it.getSiacTSoggetto().getSoggettoId().intValue()==idSoggetto.intValue()){
					filtrati.add(it);
				}
			}
		}
		return filtrati;
	}
	
	public SiacTFormaGiuridicaFin filtraSiacTFormaGiuridicaBySiacRForma(Integer idForma){
		SiacTFormaGiuridicaFin trovato = null;
		if(idForma!=null && distintiSiacRFormaGiuridicaCoinvolti!=null && distintiSiacRFormaGiuridicaCoinvolti.size()>0){
			SiacRFormaGiuridicaFin siacRForma = DatiOperazioneUtils.getById(distintiSiacRFormaGiuridicaCoinvolti, idForma);
			return siacRForma.getSiacTFormaGiuridica();
		}
		return trovato;
	}
	
	public List<SiacRSoggettoAttrFin> filtraSiacRSoggettoAttrBySoggettoId(Integer idSoggetto){
		List<SiacRSoggettoAttrFin> filtrati = new ArrayList<SiacRSoggettoAttrFin>();
		if(idSoggetto!=null && distintiSiacRSoggettoAttrCoinvolti!=null && distintiSiacRSoggettoAttrCoinvolti.size()>0){
			for(SiacRSoggettoAttrFin it : distintiSiacRSoggettoAttrCoinvolti){
				if(it.getSiacTSoggetto().getSoggettoId().intValue()==idSoggetto.intValue()){
					filtrati.add(it);
				}
			}
		}
		return filtrati;
	}
	
	
	public List<SiacRSoggettoClasseFin> filtraSiacRSoggettoClasseBySoggettoId(Integer idSoggetto){
		List<SiacRSoggettoClasseFin> filtrati = new ArrayList<SiacRSoggettoClasseFin>();
		if(idSoggetto!=null && distintiSiacRSoggettoClasses!=null && distintiSiacRSoggettoClasses.size()>0){
			for(SiacRSoggettoClasseFin it : distintiSiacRSoggettoClasses){
				if(it.getSiacTSoggetto().getSoggettoId().intValue()==idSoggetto.intValue()){
					filtrati.add(it);
				}
			}
		}
		return filtrati;
	}
	
	public List<SiacRSoggettoTipoFin> filtraSiacRSoggettoTipoBySoggettoId(Integer idSoggetto){
		List<SiacRSoggettoTipoFin> filtrati = new ArrayList<SiacRSoggettoTipoFin>();
		if(idSoggetto!=null && distintiSiacRSoggettoTipo!=null && distintiSiacRSoggettoTipo.size()>0){
			for(SiacRSoggettoTipoFin it : distintiSiacRSoggettoTipo){
				if(it.getSiacTSoggetto().getSoggettoId().intValue()==idSoggetto.intValue()){
					filtrati.add(it);
				}
			}
		}
		return filtrati;
	}
	
	//////////////////
	
	
	public SiacRSoggettoRelazStatoFin getSiacRSoggettoRelazStatoFinBySiacRSoggettoRelazFin(SiacRSoggettoRelazFin siacRSoggettoRelaz){
		SiacRSoggettoRelazStatoFin trovato = null;
		List<SiacRSoggettoRelazStatoFin> listSiacRSoggettoRelazStatoFin = filtraSiacRSoggettoRelazStatoFinBySiacRSoggettoRelazFin(siacRSoggettoRelaz);
		if(listSiacRSoggettoRelazStatoFin!=null && listSiacRSoggettoRelazStatoFin.size()>0){
			trovato = listSiacRSoggettoRelazStatoFin.get(0);
		}
		return trovato;
	}
	
	
	public List<SiacRSoggettoRelazStatoFin> filtraSiacRSoggettoRelazStatoFinBySiacRSoggettoRelazFin(SiacRSoggettoRelazFin siacRSoggettoRelaz){
		List<SiacRSoggettoRelazStatoFin> filtrati = new ArrayList<SiacRSoggettoRelazStatoFin>();
		if(siacRSoggettoRelaz!=null && siacRSoggettoRelaz.getSoggettoRelazId()!=null &&
				distintSiacRSoggettoRelazStatoFinCoinvolti!=null && distintSiacRSoggettoRelazStatoFinCoinvolti.size()>0){
			Integer soggettoRelazId = siacRSoggettoRelaz.getSoggettoRelazId();
			for(SiacRSoggettoRelazStatoFin it: distintSiacRSoggettoRelazStatoFinCoinvolti){
				if(it!=null && it.getSiacRSoggettoRelaz()!=null &&
						it.getSiacRSoggettoRelaz().getSoggettoRelazId().intValue()==soggettoRelazId.intValue()){
					filtrati.add(it);
				}
			}
		}
		return filtrati;
	}
	
	
	public List<SiacRSoggrelModpagFin> filtraByIdModPag(List<SiacRSoggrelModpagFin> listaDaFiltrare,Integer idModPag){
		List<SiacRSoggrelModpagFin> filtrati = new ArrayList<SiacRSoggrelModpagFin>();
		if(idModPag !=null && listaDaFiltrare!=null && listaDaFiltrare.size()>0){
			for(SiacRSoggrelModpagFin it : listaDaFiltrare){
				if(it!=null && it.getSiacTModpag()!=null && it.getSiacTModpag().getModpagId()!=null
						&& it.getSiacTModpag().getModpagId().intValue()==idModPag.intValue()){
					filtrati.add(it);
				}
			}
		}
		return filtrati;
	}
	
	public List<SiacRSoggettoRelazFin> filtraSiacRSoggettoRelaz1BySoggettoIdAndModPag(Integer idSoggetto,SiacTModpagFin itsiac){
		List<SiacRSoggettoRelazFin> filtrati = new ArrayList<SiacRSoggettoRelazFin>();
		if(idSoggetto!=null && itsiac!=null && itsiac.getModpagId()!=null){
			List<SiacRSoggettoRelazFin> filtratiBySoggetto1 = filtraSiacRSoggettoRelaz1BySoggettoId(idSoggetto);
			if(filtratiBySoggetto1!=null && filtratiBySoggetto1.size()>0){
				
				Integer idModPag = itsiac.getModpagId();
				
				for(SiacRSoggettoRelazFin it : filtratiBySoggetto1){
					
					List<SiacRSoggrelModpagFin> siacRSoggrelModpagFinByRelaz = filtraSiacRSoggrelModpagFinBySiacRSoggettoRelazFin(it);
					List<SiacRSoggrelModpagFin> siacRSoggrelModpagFinByRelazFiltratiByModPag = filtraByIdModPag(siacRSoggrelModpagFinByRelaz, idModPag);
					
					if(siacRSoggrelModpagFinByRelazFiltratiByModPag!=null && siacRSoggrelModpagFinByRelazFiltratiByModPag.size()>0){
						filtrati.add(it);
					}
				}
			}
		}
		return filtrati;
	}
	
	
	public List<SiacRSoggettoRelazFin> filtraSiacRSoggettoRelaz1BySoggettoId(Integer idSoggetto){
		List<SiacRSoggettoRelazFin> filtrati = new ArrayList<SiacRSoggettoRelazFin>();
		if(idSoggetto!=null && distintiSiacRSoggettoRelaz!=null && distintiSiacRSoggettoRelaz.size()>0){
			for(SiacRSoggettoRelazFin it : distintiSiacRSoggettoRelaz){
				if(it.getSiacTSoggetto1().getSoggettoId().intValue()==idSoggetto.intValue()){
					filtrati.add(it);
				}
			}
		}
		return filtrati;
	}
	
	public List<SiacRSoggettoRelazFin> filtraSiacRSoggettoRelaz2BySoggettoId(Integer idSoggetto){
		List<SiacRSoggettoRelazFin> filtrati = new ArrayList<SiacRSoggettoRelazFin>();
		if(idSoggetto!=null && distintiSiacRSoggettoRelaz!=null && distintiSiacRSoggettoRelaz.size()>0){
			for(SiacRSoggettoRelazFin it : distintiSiacRSoggettoRelaz){
				if(it.getSiacTSoggetto2().getSoggettoId().intValue()==idSoggetto.intValue()){
					filtrati.add(it);
				}
			}
		}
		return filtrati;
	}
	
	public SiacTSoggettoFin findSiacTSoggettoFinByID(Integer soggettoID){
		return CommonUtils.getByIdSiacTBase(distintiSiacTSoggettiCoinvolti, soggettoID);
	}
	
	public SiacRSoggettoRelazFin findSiacRSoggettoRelazFinByID(Integer soggettoRelazId){
		return CommonUtils.getByIdSiacTBase(distintiSiacRSoggettoRelaz, soggettoRelazId);
//		SiacRSoggettoRelazFin trovato = null;
//		if(soggettoRelazId!=null && distintiSiacRSoggettoRelaz!=null && distintiSiacRSoggettoRelaz.size()>0){
//			for(SiacRSoggettoRelazFin it : distintiSiacRSoggettoRelaz){
//				if(it.getSoggettoRelazId().intValue()==soggettoRelazId.intValue()){
//					trovato = it;
//					break;
//				}
//			}
//		}
//		return trovato;
	}
	
	public SiacTNazioneFin findSiacTNazioneFinByID(Integer idNazione){
		return CommonUtils.getByIdSiacTBase(distintiSiacTNazioneFinCoinvolti, idNazione);
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
	
	/////////////
	
	
	public List<SiacTSoggettoModFin> filtraSiacTSoggettoModFinBySoggettoId(Integer idSoggetto){
		List<SiacTSoggettoModFin> filtrati = new ArrayList<SiacTSoggettoModFin>();
		if(idSoggetto!=null && distintiSiacTSoggettoModFinCoinvolti!=null && distintiSiacTSoggettoModFinCoinvolti.size()>0){
			for(SiacTSoggettoModFin it : distintiSiacTSoggettoModFinCoinvolti){
				if(it.getSiacTSoggetto().getSoggettoId().intValue()==idSoggetto.intValue()){
					filtrati.add(it);
				}
			}
		}
		return filtrati;
	}
	
	
	public List<SiacTRecapitoSoggettoFin> filtraSiacTRecapitoSoggettoBySoggettoId(Integer idSoggetto){
		List<SiacTRecapitoSoggettoFin> filtrati = new ArrayList<SiacTRecapitoSoggettoFin>();
		if(idSoggetto!=null && distintiSiacTRecapitoSoggetto!=null && distintiSiacTRecapitoSoggetto.size()>0){
			for(SiacTRecapitoSoggettoFin it : distintiSiacTRecapitoSoggetto){
				if(it.getSiacTSoggetto().getSoggettoId().intValue()==idSoggetto.intValue()){
					filtrati.add(it);
				}
			}
		}
		return filtrati;
	}
	
	public List<SiacRSoggettoOnereFin> filtraSiacRSoggettoOnereBySoggettoId(Integer idSoggetto){
		List<SiacRSoggettoOnereFin> filtrati = new ArrayList<SiacRSoggettoOnereFin>();
		if(idSoggetto!=null && distintiSiacRSoggettoOnere!=null && distintiSiacRSoggettoOnere.size()>0){
			for(SiacRSoggettoOnereFin it : distintiSiacRSoggettoOnere){
				if(it.getSiacTSoggetto().getSoggettoId().intValue()==idSoggetto.intValue()){
					filtrati.add(it);
				}
			}
		}
		return filtrati;
	}
	
	public List<SiacTIndirizzoSoggettoFin> filtraSiacTIndirizzoSoggettoBySoggettoId(Integer idSoggetto){
		List<SiacTIndirizzoSoggettoFin> filtrati = new ArrayList<SiacTIndirizzoSoggettoFin>();
		if(idSoggetto!=null && distintiSiacTIndirizzoSoggetto!=null && distintiSiacTIndirizzoSoggetto.size()>0){
			for(SiacTIndirizzoSoggettoFin it : distintiSiacTIndirizzoSoggetto){
				if(it.getSiacTSoggetto().getSoggettoId().intValue()==idSoggetto.intValue()){
					filtrati.add(it);
				}
			}
		}
		return filtrati;
	}
	
	public List<SiacTPersonaFisicaFin> filtraSiacTPersonaFisicaBySoggettoId(Integer idSoggetto){
		List<SiacTPersonaFisicaFin> filtrati = new ArrayList<SiacTPersonaFisicaFin>();
		if(idSoggetto!=null && distintiSiacTPersonaFisica!=null && distintiSiacTPersonaFisica.size()>0){
			for(SiacTPersonaFisicaFin it : distintiSiacTPersonaFisica){
				if(it.getSiacTSoggetto().getSoggettoId().intValue()==idSoggetto.intValue()){
					filtrati.add(it);
				}
			}
		}
		return filtrati;
	}
	
	
	public List<SiacTPersonaGiuridicaFin> filtraSiacTPersonaGiuridicaBySoggettoId(Integer idSoggetto){
		List<SiacTPersonaGiuridicaFin> filtrati = new ArrayList<SiacTPersonaGiuridicaFin>();
		if(idSoggetto!=null && distintiSiacTPersonaGiuridica!=null && distintiSiacTPersonaGiuridica.size()>0){
			for(SiacTPersonaGiuridicaFin it : distintiSiacTPersonaGiuridica){
				if(it.getSiacTSoggetto().getSoggettoId().intValue()==idSoggetto.intValue()){
					filtrati.add(it);
				}
			}
		}
		return filtrati;
	}
	
	public List<SiacRSoggettoRelazFin> fitraSiacRSoggettoRelazFinBySoggettoUno(SiacTSoggettoFin siacTSoggettoFin){
		List<SiacRSoggettoRelazFin> bySoggettoUno = new ArrayList<SiacRSoggettoRelazFin>();
		if(distintiSiacRSoggettoRelaz!=null && distintiSiacRSoggettoRelaz.size()>0 && siacTSoggettoFin!=null){
			Integer soggId = siacTSoggettoFin.getSoggettoId();
			if(soggId!=null){
				for(SiacRSoggettoRelazFin it : distintiSiacRSoggettoRelaz){
					if(it!=null && it.getSiacTSoggetto1()!=null &&
							it.getSiacTSoggetto1().getSoggettoId()!=null && it.getSiacTSoggetto1().getSoggettoId().intValue()==soggId.intValue()){
						bySoggettoUno.add(it);
					}
				}
			}
		}
		bySoggettoUno = CommonUtils.soloValidiSiacTBase(bySoggettoUno, null);
		return bySoggettoUno;
	}
	
	public List<SiacRSoggettoRelazFin> fitraSiacRSoggettoRelazFinBySoggettoUno(){
		List<SiacRSoggettoRelazFin> listaValidi = new ArrayList<SiacRSoggettoRelazFin>();
		if(distintiSiacRSoggettoRelaz!=null && distintiSiacRSoggettoRelaz.size()>0){
			for(SiacRSoggettoRelazFin it : distintiSiacRSoggettoRelaz){
				if(it!=null && it.getSiacTSoggetto1()!=null){
					listaValidi.add(it);
				}
			}
		}
		listaValidi = CommonUtils.soloValidiSiacTBase(listaValidi, null);
		return listaValidi;
	}
	
	
	public List<SiacRSoggrelModpagFin> filtraSiacRSoggrelModpagFinBySiacRSoggettoRelazFin(SiacRSoggettoRelazFin siacRSoggettoRelazFin){
		List<SiacRSoggrelModpagFin> siacRSoggrelModpagFinByRelaz = new ArrayList<SiacRSoggrelModpagFin>();
		if(siacRSoggettoRelazFin!=null && distintiSiacRSoggrelModpagFinCoinvolti!=null && distintiSiacRSoggrelModpagFinCoinvolti.size()>0){
			
			Integer soggettoRelazId = siacRSoggettoRelazFin.getSoggettoRelazId();
			
			if(soggettoRelazId!=null){
				
				for(SiacRSoggrelModpagFin it: distintiSiacRSoggrelModpagFinCoinvolti){
					if(it.getSiacRSoggettoRelaz().getSoggettoRelazId().intValue()==soggettoRelazId.intValue()){
						siacRSoggrelModpagFinByRelaz.add(it);
					}
				}
				
			}
			
		}
		siacRSoggrelModpagFinByRelaz = CommonUtils.soloValidiSiacTBase(siacRSoggrelModpagFinByRelaz, null);
		return siacRSoggrelModpagFinByRelaz;
	}
	
	public List<SiacTModpagFin> filtraSiacTModpagFinBySiacRSoggrelModpagFin(List<SiacRSoggrelModpagFin> listaSiacRSoggrelModpagFin){
		List<SiacTModpagFin> distintiSiacTModpagFinBySoggRelazModPag = new ArrayList<SiacTModpagFin>();
		if(listaSiacRSoggrelModpagFin!=null && listaSiacRSoggrelModpagFin.size()>0){
			for(SiacRSoggrelModpagFin it: listaSiacRSoggrelModpagFin){
				if(it!=null){
					distintiSiacTModpagFinBySoggRelazModPag.add(it.getSiacTModpag());
				}
			}
		}
		distintiSiacTModpagFinBySoggRelazModPag = CommonUtils.soloValidiSiacTBase(distintiSiacTModpagFinBySoggRelazModPag, null);
		return distintiSiacTModpagFinBySoggRelazModPag;
	}
	
	public List<SiacTModpagFin> getListaTModpagsCessioni(SiacTSoggettoFin siacTSoggettoFin){
		List<SiacTModpagFin> listaTModpagsCessioni = new ArrayList<SiacTModpagFin>();
		if(siacTSoggettoFin!=null && siacTSoggettoFin.getSoggettoId()!=null && distintiSiacTSoggettiCoinvolti!=null && distintiSiacTSoggettiCoinvolti.size()>0){
			List<SiacRSoggettoRelazFin> bySoggettoUno = fitraSiacRSoggettoRelazFinBySoggettoUno(siacTSoggettoFin);
			//metodo 2
			if(bySoggettoUno!=null && bySoggettoUno.size()>0){
				for(SiacRSoggettoRelazFin it : bySoggettoUno){
					List<SiacRSoggrelModpagFin> siacRSoggrelModpagFinByRelaz = filtraSiacRSoggrelModpagFinBySiacRSoggettoRelazFin(it);
					List<SiacTModpagFin> distintiSiacTModpagFinBySoggRelazModPag = filtraSiacTModpagFinBySiacRSoggrelModpagFin(siacRSoggrelModpagFinByRelaz);
					//Questi: distintiSiacTModpagFinBySoggRelazModPag dovrebbero essere listaTModpagsCessioni
					if(distintiSiacTModpagFinBySoggRelazModPag!=null && distintiSiacTModpagFinBySoggRelazModPag.size()>0){
						listaTModpagsCessioni.addAll(distintiSiacTModpagFinBySoggRelazModPag);
					}
					//
				}
			}
		}
		listaTModpagsCessioni = CommonUtils.soloValidiSiacTBase(listaTModpagsCessioni, null);
		return listaTModpagsCessioni;
	}
	
	public List<SiacTModpagFin> getListaTModpagsCessioniAll(){
		List<SiacTModpagFin> listaTModpagsCessioni = new ArrayList<SiacTModpagFin>();
		if(distintiSiacTSoggettiCoinvolti!=null && distintiSiacTSoggettiCoinvolti.size()>0){
			List<SiacRSoggettoRelazFin> bySoggettoUno = fitraSiacRSoggettoRelazFinBySoggettoUno();
			//metodo 2
			if(bySoggettoUno!=null && bySoggettoUno.size()>0){
				for(SiacRSoggettoRelazFin it : bySoggettoUno){
					List<SiacRSoggrelModpagFin> siacRSoggrelModpagFinByRelaz = filtraSiacRSoggrelModpagFinBySiacRSoggettoRelazFin(it);
					List<SiacTModpagFin> distintiSiacTModpagFinBySoggRelazModPag = filtraSiacTModpagFinBySiacRSoggrelModpagFin(siacRSoggrelModpagFinByRelaz);
					//Questi: distintiSiacTModpagFinBySoggRelazModPag dovrebbero essere listaTModpagsCessioni
					if(distintiSiacTModpagFinBySoggRelazModPag!=null && distintiSiacTModpagFinBySoggRelazModPag.size()>0){
						listaTModpagsCessioni.addAll(distintiSiacTModpagFinBySoggRelazModPag);
					}
					//
				}
			}
		}
		listaTModpagsCessioni = CommonUtils.soloValidiSiacTBase(listaTModpagsCessioni, null);
		return listaTModpagsCessioni;
	}
	
	
	

	public List<SiacRSoggettoStatoFin> getDistintiSiacRSoggettoStatoCoinvolti() {
		return distintiSiacRSoggettoStatoCoinvolti;
	}

	public void setDistintiSiacRSoggettoStatoCoinvolti(
			List<SiacRSoggettoStatoFin> distintiSiacRSoggettoStatoCoinvolti) {
		this.distintiSiacRSoggettoStatoCoinvolti = distintiSiacRSoggettoStatoCoinvolti;
	}


	public List<SiacRFormaGiuridicaFin> getDistintiSiacRFormaGiuridicaCoinvolti() {
		return distintiSiacRFormaGiuridicaCoinvolti;
	}


	public void setDistintiSiacRFormaGiuridicaCoinvolti(
			List<SiacRFormaGiuridicaFin> distintiSiacRFormaGiuridicaCoinvolti) {
		this.distintiSiacRFormaGiuridicaCoinvolti = distintiSiacRFormaGiuridicaCoinvolti;
	}

	public List<SiacTFormaGiuridicaFin> getDistintiSiacTFormaGiuridicaCoinvolti() {
		return distintiSiacTFormaGiuridicaCoinvolti;
	}

	public void setDistintiSiacTFormaGiuridicaCoinvolti(
			List<SiacTFormaGiuridicaFin> distintiSiacTFormaGiuridicaCoinvolti) {
		this.distintiSiacTFormaGiuridicaCoinvolti = distintiSiacTFormaGiuridicaCoinvolti;
	}

	public List<SiacRSoggettoAttrFin> getDistintiSiacRSoggettoAttrCoinvolti() {
		return distintiSiacRSoggettoAttrCoinvolti;
	}

	public void setDistintiSiacRSoggettoAttrCoinvolti(
			List<SiacRSoggettoAttrFin> distintiSiacRSoggettoAttrCoinvolti) {
		this.distintiSiacRSoggettoAttrCoinvolti = distintiSiacRSoggettoAttrCoinvolti;
	}

	public List<SiacRSoggettoRelazFin> getDistintiSiacRSoggettoRelaz() {
		return distintiSiacRSoggettoRelaz;
	}

	public void setDistintiSiacRSoggettoRelaz(
			List<SiacRSoggettoRelazFin> distintiSiacRSoggettoRelaz) {
		this.distintiSiacRSoggettoRelaz = distintiSiacRSoggettoRelaz;
	}

	public List<SiacRSoggettoClasseFin> getDistintiSiacRSoggettoClasses() {
		return distintiSiacRSoggettoClasses;
	}

	public void setDistintiSiacRSoggettoClasses(
			List<SiacRSoggettoClasseFin> distintiSiacRSoggettoClasses) {
		this.distintiSiacRSoggettoClasses = distintiSiacRSoggettoClasses;
	}

	public List<SiacRSoggettoTipoFin> getDistintiSiacRSoggettoTipo() {
		return distintiSiacRSoggettoTipo;
	}

	public void setDistintiSiacRSoggettoTipo(
			List<SiacRSoggettoTipoFin> distintiSiacRSoggettoTipo) {
		this.distintiSiacRSoggettoTipo = distintiSiacRSoggettoTipo;
	}

	public List<SiacTRecapitoSoggettoFin> getDistintiSiacTRecapitoSoggetto() {
		return distintiSiacTRecapitoSoggetto;
	}

	public void setDistintiSiacTRecapitoSoggetto(
			List<SiacTRecapitoSoggettoFin> distintiSiacTRecapitoSoggetto) {
		this.distintiSiacTRecapitoSoggetto = distintiSiacTRecapitoSoggetto;
	}

	public List<SiacRSoggettoOnereFin> getDistintiSiacRSoggettoOnere() {
		return distintiSiacRSoggettoOnere;
	}

	public void setDistintiSiacRSoggettoOnere(
			List<SiacRSoggettoOnereFin> distintiSiacRSoggettoOnere) {
		this.distintiSiacRSoggettoOnere = distintiSiacRSoggettoOnere;
	}

	public List<SiacTIndirizzoSoggettoFin> getDistintiSiacTIndirizzoSoggetto() {
		return distintiSiacTIndirizzoSoggetto;
	}

	public void setDistintiSiacTIndirizzoSoggetto(
			List<SiacTIndirizzoSoggettoFin> distintiSiacTIndirizzoSoggetto) {
		this.distintiSiacTIndirizzoSoggetto = distintiSiacTIndirizzoSoggetto;
	}

	public List<SiacTPersonaFisicaFin> getDistintiSiacTPersonaFisica() {
		return distintiSiacTPersonaFisica;
	}

	public void setDistintiSiacTPersonaFisica(
			List<SiacTPersonaFisicaFin> distintiSiacTPersonaFisica) {
		this.distintiSiacTPersonaFisica = distintiSiacTPersonaFisica;
	}

	public List<SiacTPersonaGiuridicaFin> getDistintiSiacTPersonaGiuridica() {
		return distintiSiacTPersonaGiuridica;
	}

	public void setDistintiSiacTPersonaGiuridica(
			List<SiacTPersonaGiuridicaFin> distintiSiacTPersonaGiuridica) {
		this.distintiSiacTPersonaGiuridica = distintiSiacTPersonaGiuridica;
	}

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

	public List<SiacRLiquidazioneSoggettoFin> getDistintiSiacRLiquidazioneSoggetto() {
		return distintiSiacRLiquidazioneSoggetto;
	}

	public void setDistintiSiacRLiquidazioneSoggetto(
			List<SiacRLiquidazioneSoggettoFin> distintiSiacRLiquidazioneSoggetto) {
		this.distintiSiacRLiquidazioneSoggetto = distintiSiacRLiquidazioneSoggetto;
	}

	public List<SiacTModpagFin> getDistintiSiacTModpagFinCoinvolti() {
		return distintiSiacTModpagFinCoinvolti;
	}

	public void setDistintiSiacTModpagFinCoinvolti(
			List<SiacTModpagFin> distintiSiacTModpagFinCoinvolti) {
		this.distintiSiacTModpagFinCoinvolti = distintiSiacTModpagFinCoinvolti;
	}

	public List<SiacRSoggrelModpagFin> getDistintiSiacRSoggrelModpagFinCoinvolti() {
		return distintiSiacRSoggrelModpagFinCoinvolti;
	}

	public void setDistintiSiacRSoggrelModpagFinCoinvolti(
			List<SiacRSoggrelModpagFin> distintiSiacRSoggrelModpagFinCoinvolti) {
		this.distintiSiacRSoggrelModpagFinCoinvolti = distintiSiacRSoggrelModpagFinCoinvolti;
	}

	public List<SiacTModpagModFin> getDistintiSiacTModpagModFinCoinvolti() {
		return distintiSiacTModpagModFinCoinvolti;
	}

	public void setDistintiSiacTModpagModFinCoinvolti(List<SiacTModpagModFin> distintiSiacTModpagModFinCoinvolti) {
		this.distintiSiacTModpagModFinCoinvolti = distintiSiacTModpagModFinCoinvolti;
	}

	public List<Soggetto> getSoggettiGiaCaricati() {
		return soggettiGiaCaricati;
	}

	public void setSoggettiGiaCaricati(List<Soggetto> soggettiGiaCaricati) {
		this.soggettiGiaCaricati = soggettiGiaCaricati;
	}

	public List<SiacRModpagStatoFin> getDistintiSiacRModpagStatoFinCoinvolti() {
		return distintiSiacRModpagStatoFinCoinvolti;
	}

	public void setDistintiSiacRModpagStatoFinCoinvolti(
			List<SiacRModpagStatoFin> distintiSiacRModpagStatoFinCoinvolti) {
		this.distintiSiacRModpagStatoFinCoinvolti = distintiSiacRModpagStatoFinCoinvolti;
	}

	public List<SiacRModpagOrdineFin> getDistintiSiacRModpagOrdineFinCoinvolti() {
		return distintiSiacRModpagOrdineFinCoinvolti;
	}

	public void setDistintiSiacRModpagOrdineFinCoinvolti(
			List<SiacRModpagOrdineFin> distintiSiacRModpagOrdineFinCoinvolti) {
		this.distintiSiacRModpagOrdineFinCoinvolti = distintiSiacRModpagOrdineFinCoinvolti;
	}

	public List<SiacRSoggettoRelazStatoFin> getDistintSiacRSoggettoRelazStatoFinCoinvolti() {
		return distintSiacRSoggettoRelazStatoFinCoinvolti;
	}

	public void setDistintSiacRSoggettoRelazStatoFinCoinvolti(
			List<SiacRSoggettoRelazStatoFin> distintSiacRSoggettoRelazStatoFinCoinvolti) {
		this.distintSiacRSoggettoRelazStatoFinCoinvolti = distintSiacRSoggettoRelazStatoFinCoinvolti;
	}

	public List<SiacTSoggettoModFin> getDistintiSiacTSoggettoModFinCoinvolti() {
		return distintiSiacTSoggettoModFinCoinvolti;
	}

	public void setDistintiSiacTSoggettoModFinCoinvolti(
			List<SiacTSoggettoModFin> distintiSiacTSoggettoModFinCoinvolti) {
		this.distintiSiacTSoggettoModFinCoinvolti = distintiSiacTSoggettoModFinCoinvolti;
	}

	public List<SiacTComuneFin> getDistintiSiacTComuneFinCoinvolti() {
		return distintiSiacTComuneFinCoinvolti;
	}

	public void setDistintiSiacTComuneFinCoinvolti(
			List<SiacTComuneFin> distintiSiacTComuneFinCoinvolti) {
		this.distintiSiacTComuneFinCoinvolti = distintiSiacTComuneFinCoinvolti;
	}

	public List<SiacRComuneProvinciaFin> getDistintiSiacRComuneProvinciaFinCoinvolti() {
		return distintiSiacRComuneProvinciaFinCoinvolti;
	}

	public void setDistintiSiacRComuneProvinciaFinCoinvolti(
			List<SiacRComuneProvinciaFin> distintiSiacRComuneProvinciaFinCoinvolti) {
		this.distintiSiacRComuneProvinciaFinCoinvolti = distintiSiacRComuneProvinciaFinCoinvolti;
	}

	public List<SiacTProvinciaFin> getDistintiSiacTProvinciaFinCoinvolti() {
		return distintiSiacTProvinciaFinCoinvolti;
	}

	public void setDistintiSiacTProvinciaFinCoinvolti(
			List<SiacTProvinciaFin> distintiSiacTProvinciaFinCoinvolti) {
		this.distintiSiacTProvinciaFinCoinvolti = distintiSiacTProvinciaFinCoinvolti;
	}

	public List<SiacRProvinciaRegioneFin> getDistintiSiacRProvinciaRegioneFinCoinvolti() {
		return distintiSiacRProvinciaRegioneFinCoinvolti;
	}

	public void setDistintiSiacRProvinciaRegioneFinCoinvolti(
			List<SiacRProvinciaRegioneFin> distintiSiacRProvinciaRegioneFinCoinvolti) {
		this.distintiSiacRProvinciaRegioneFinCoinvolti = distintiSiacRProvinciaRegioneFinCoinvolti;
	}

	public List<SiacTRegioneFin> getDistintiSiacTRegioneFinCoinvolti() {
		return distintiSiacTRegioneFinCoinvolti;
	}

	public void setDistintiSiacTRegioneFinCoinvolti(
			List<SiacTRegioneFin> distintiSiacTRegioneFinCoinvolti) {
		this.distintiSiacTRegioneFinCoinvolti = distintiSiacTRegioneFinCoinvolti;
	}

	public List<SiacTNazioneFin> getDistintiSiacTNazioneFinCoinvolti() {
		return distintiSiacTNazioneFinCoinvolti;
	}

	public void setDistintiSiacTNazioneFinCoinvolti(
			List<SiacTNazioneFin> distintiSiacTNazioneFinCoinvolti) {
		this.distintiSiacTNazioneFinCoinvolti = distintiSiacTNazioneFinCoinvolti;
	}

	public List<SiacRComuneRegioneFin> getDistintiSiacRComuneRegioneFinCoinvolti() {
		return distintiSiacRComuneRegioneFinCoinvolti;
	}

	public void setDistintiSiacRComuneRegioneFinCoinvolti(
			List<SiacRComuneRegioneFin> distintiSiacRComuneRegioneFinCoinvolti) {
		this.distintiSiacRComuneRegioneFinCoinvolti = distintiSiacRComuneRegioneFinCoinvolti;
	}

	public List<SiacTSoggettoFin> getDistintiSiacTSoggettiCoinvoltiDirettamente() {
		return distintiSiacTSoggettiCoinvoltiDirettamente;
	}

	public void setDistintiSiacTSoggettiCoinvoltiDirettamente(
			List<SiacTSoggettoFin> distintiSiacTSoggettiCoinvoltiDirettamente) {
		this.distintiSiacTSoggettiCoinvoltiDirettamente = distintiSiacTSoggettiCoinvoltiDirettamente;
	}

	public List<SiacTSoggettoFin> getDistintiSiacTSoggettiCoinvoltiTramiteRelazioni() {
		return distintiSiacTSoggettiCoinvoltiTramiteRelazioni;
	}

	public void setDistintiSiacTSoggettiCoinvoltiTramiteRelazioni(
			List<SiacTSoggettoFin> distintiSiacTSoggettiCoinvoltiTramiteRelazioni) {
		this.distintiSiacTSoggettiCoinvoltiTramiteRelazioni = distintiSiacTSoggettiCoinvoltiTramiteRelazioni;
	}

	public List<SiacRMovgestTsSogclasseFin> getDistintiSiacRMovgestTsSogclasseFinCoinvolti() {
		return distintiSiacRMovgestTsSogclasseFinCoinvolti;
	}

	public void setDistintiSiacRMovgestTsSogclasseFinCoinvolti(
			List<SiacRMovgestTsSogclasseFin> distintiSiacRMovgestTsSogclasseFinCoinvolti) {
		this.distintiSiacRMovgestTsSogclasseFinCoinvolti = distintiSiacRMovgestTsSogclasseFinCoinvolti;
	}
	
}
