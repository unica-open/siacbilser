/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.carta.PreDocumentoCarta;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;



public final class ModelUtils{
	
	public static ArrayList<ModalitaPagamentoSoggetto> filtraValidi(List<ModalitaPagamentoSoggetto> lista){
		ArrayList<ModalitaPagamentoSoggetto> listMod= new ArrayList<ModalitaPagamentoSoggetto>();
		
		//step 1: filtro in base alla data di fine validita
		Timestamp now=  new Timestamp(System.currentTimeMillis());
		ArrayList<ModalitaPagamentoSoggetto> listaDataValida = (ArrayList<ModalitaPagamentoSoggetto>) CommonUtil.soloValidi(lista, now);
		
		//step 2: ulteriore filtro in base allo stato:
		if(listaDataValida!= null && listaDataValida.size()>0){
			for(ModalitaPagamentoSoggetto modPag:listaDataValida){
				if(modPag!=null && CostantiFin.STATO_VALIDO.equalsIgnoreCase(modPag.getDescrizioneStatoModalitaPagamento())){
					modPag.setDescrizioneStatoModalitaPagamento(modPag.getDescrizioneStatoModalitaPagamento().toUpperCase());
					listMod.add(modPag);
				}
			}
		}
		return listMod;
	}
	
	public static boolean isValida(ModalitaPagamentoSoggetto mps){
		if(mps==null){
			return false;
		}
		ArrayList<ModalitaPagamentoSoggetto> lista = new ArrayList<ModalitaPagamentoSoggetto>();
		lista.add(mps);
		ArrayList<ModalitaPagamentoSoggetto> validi = filtraValidi(lista);
		boolean valida = false;
		if(validi!=null && validi.size()>0){
			valida = true;
		}
		return valida;
	}
	
	public static SubImpegno getSubImpByNumero(List<SubImpegno> listaSub,BigDecimal numSub){
		SubImpegno subTrovato = null;
		if(numSub!=null && listaSub!=null && listaSub.size()>0){
			for(SubImpegno it : listaSub){
				if(it!=null){
					BigDecimal numSubIt = it.getNumeroBigDecimal();
					if(numSubIt!=null && numSub.compareTo(numSubIt)==0){
						subTrovato = it;
						break;
					}
				}
			}
		}
		return subTrovato;
	}
	
	public static SubAccertamento getSubAccByNumero(List<SubAccertamento> listaSub,BigDecimal numSub){
		SubAccertamento subTrovato = null;
		if(numSub!=null && listaSub!=null && listaSub.size()>0){
			for(SubAccertamento it : listaSub){
				if(it!=null){
					BigDecimal numSubIt = it.getNumeroBigDecimal();
					if(numSubIt!=null && numSub.compareTo(numSubIt)==0){
						subTrovato = it;
						break;
					}
				}
			}
		}
		return subTrovato;
	}
	
	/**
	 * Cerca l'elemento privo di numero, tipicamente si usa
	 * per individuare l'elemento che e' stato indicato in inserimento in mezzo
	 * agli altri gia' presenti
	 * @param listaSub
	 * @return
	 */
	public static SubImpegno trovaSubImpSenzaNumero(List<SubImpegno> listaSub){
		SubImpegno subTrovato = null;
		if(listaSub!=null && listaSub.size()>0){
			for(SubImpegno it : listaSub){
				if(it!=null){
					BigDecimal numSubIt = it.getNumeroBigDecimal();
					if(numSubIt==null || numSubIt.compareTo(BigDecimal.ZERO)==0){
						subTrovato = it;
						break;
					}
				}
			}
		}
		return subTrovato;
	}
	
	/**
	 * Cerca l'elemento privo di numero, tipicamente si usa
	 * per individuare l'elemento che e' stato indicato in inserimento in mezzo
	 * agli altri gia' presenti
	 * @param listaSub
	 * @return
	 */
	public static SubAccertamento trovaSubAccSenzaNumero(List<SubAccertamento> listaSub){
		SubAccertamento subTrovato = null;
		if(listaSub!=null && listaSub.size()>0){
			for(SubAccertamento it : listaSub){
				if(it!=null){
					BigDecimal numSubIt = it.getNumeroBigDecimal();
					if(numSubIt==null || numSubIt.compareTo(BigDecimal.ZERO)==0){
						subTrovato = it;
						break;
					}
				}
			}
		}
		return subTrovato;
	}
	
	public static boolean presenteSubImpInLista(List<SubImpegno> listaSub,BigDecimal numSub){
		SubImpegno subTrovato = getSubImpByNumero(listaSub, numSub);
		if(subTrovato!=null){
			return true;
		}
		return false;
	}
	
	public static boolean presenteSubAccInLista(List<SubAccertamento> listaSub,BigDecimal numSub){
		SubAccertamento subTrovato = getSubAccByNumero(listaSub, numSub);
		if(subTrovato!=null){
			return true;
		}
		return false;
	}
	
	public static PreDocumentoCarta getPreDocCartaByNumero(List<PreDocumentoCarta> listaRighe,Integer num){
		PreDocumentoCarta trovato = null;
		if(num!=null && listaRighe!=null && listaRighe.size()>0){
			for(PreDocumentoCarta it : listaRighe){
				if(it!=null){
					Integer numIt = it.getNumero();
					if(numIt!=null && num.compareTo(numIt)==0){
						trovato = it;
						break;
					}
				}
			}
		}
		return trovato;
	}
	
	public static SubdocumentoSpesa getSubdocumentoSpesaByNumero(List<SubdocumentoSpesa> lista,Integer num){
		SubdocumentoSpesa trovato = null;
		if(num!=null && lista!=null && lista.size()>0){
			for(SubdocumentoSpesa it : lista){
				if(it!=null){
					Integer numIt = it.getNumero();
					if(numIt!=null && num.compareTo(numIt)==0){
						trovato = it;
						break;
					}
				}
			}
		}
		return trovato;
	}

}
