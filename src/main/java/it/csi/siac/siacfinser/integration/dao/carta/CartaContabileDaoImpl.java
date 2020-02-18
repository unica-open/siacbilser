/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.carta;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacfinser.StringUtils;
import it.csi.siac.siacfinser.TimingUtils;
import it.csi.siac.siacfinser.integration.dao.common.AbstractDao;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.RicercaCartaContabileParamDto;
import it.csi.siac.siacfinser.integration.entity.SiacTCartacontFin;
import it.csi.siac.siacfinser.integration.util.DataValiditaUtils;
import it.csi.siac.siacfinser.model.ric.RicercaCartaContabileK;

@Component
@Transactional
public class CartaContabileDaoImpl extends AbstractDao<SiacTCartacontFin, Integer> implements CartaContabileDao {


	@Autowired
	SiacTCartaContRepository siacTCartaContRepository;
	
	/**
	 * E' il metodo di "engine" di ricerca delle carte contabili.
	 * Utilizzato sia per avere un'anteprima del numero di risultati attesi (rispetto al filtro indicato)
	 * sia per recuperare tutti i dati (rispetto al filtro indicato)
	 */
	@Override
	public Query creaQueryRicercaCarteContabili(RicercaCartaContabileParamDto params, DatiOperazioneDto datiOperazione, boolean soloCount){
		
		Map<String,Object> param = new HashMap<String, Object>();
		Date nowDate = TimingUtils.getNowDate();
		param.put(DataValiditaUtils.NOW_DATE_PARAM_JPQL, nowDate);
		
		Query query = null;
		// Parametri di input ricevuti dal servizio :

		// ente
		Integer idEnte = datiOperazione.getSiacTEnteProprietario().getUid();

		// anno esercizio
		Integer annoEsercizio = params.getAnnoEsercizio();
		
		// carta contabile
		Integer numeroCartaContabileDa = params.getNumeroCartaContabileDa();
		Integer numeroCartaContabileA = params.getNumeroCartaContabileA();
		Date dataScadenzaDa = params.getDataScadenzaDa();
		Date dataScadenzaA = params.getDataScadenzaA();
		String statoOperativo = params.getStatoOperativo();
		String oggetto = params.getOggetto();

		// capitolo
		Integer annoCapitolo = params.getAnnoCapitolo();
		BigDecimal numeroCapitolo = params.getNumeroCapitolo();
		BigDecimal numeroArticolo = params.getNumeroArticolo();
		Integer numeroUEB = params.getNumeroUEB();

		// impegno
		Integer annoImpegno = params.getAnnoImpegno();
		BigDecimal numeroImpegno = params.getNumeroImpegno();
		BigDecimal numeroSubImpegno = params.getNumeroSubImpegno();

		// provvedimento
		Integer annoProvvedimento = params.getAnnoProvvedimento();
		Integer numeroProvvedimento = params.getNumeroProvvedimento();
		String tipoProvvedimento = params.getTipoProvvedimento();
		String strutturaAmministrativaProvvedimento = params.getStrutturaAmministrativaProvvedimento();

		// soggetto
		String codiceCreditore = params.getCodiceCreditore();

		// sub-documento
		Integer subDocumentoSpesaId = params.getSubDocumentoSpesaId();
		
		String str = "";
		if(soloCount==true){
			str = "SELECT count(DISTINCT cartaCont) FROM SiacTCartacontFin cartaCont";
		} else {
			str = "SELECT DISTINCT cartaCont FROM SiacTCartacontFin cartaCont";
		}

		StringBuilder jpql = new StringBuilder(str);

		// join con altre tabelle
		// siac_t_cartacont_det
		jpql.append(" left join cartaCont.siacTCartacontDets cartaContDet ");
	
		// siac_t_cartacont + siac_r_cartacont_stato + siac_d_cartacont_stato
		jpql.append(" left join cartaCont.siacRCartacontStatos rCartaContStatos left join rCartaContStatos.siacDCartacontStato dCartaContStato ");
		
		// siac_t_cartacont + siac_t_atto_amm
		jpql.append(" left join cartaCont.siacTAttoAmm tAttoAmm left join tAttoAmm.siacDAttoAmmTipo dAttoAmmTipo ");
		jpql.append(" left join tAttoAmm.siacRAttoAmmClasses rAttoClass  left join rAttoClass.siacTClass tClassAtto ");
		
		// siac_t_cartacont_det + siac_r_cartacont_det_soggetto + siac_t_soggetto
		jpql.append(" left join cartaContDet.siacRCartacontDetSoggettos rDetSoggetto left join rDetSoggetto.siacTSoggetto tSoggetto ");			
		
		// siac_t_cartacont_det + siac_r_cartacont_det_movgest_ts + siac_t_movgest_ts
		jpql.append(" left join cartaContDet.siacRCartacontDetMovgestTs rDetMovgestTs left join rDetMovgestTs.siacTMovgestT tMovgestTs ");

		// siac_t_cartacont_det + siac_r_cartacont_det_subdoc + siac_t_subdoc
		jpql.append(" left join cartaContDet.siacRCartacontDetSubdocs rDetSubDocs left join rDetSubDocs.siacTSubdoc tSubDoc ");
		
		// siac_t_movgest_ts + siac_t_movgest + siac_r_movgest_bil_elem + siac_t_bil_elem
		jpql.append(" left join tMovgestTs.siacTMovgest.siacRMovgestBilElems rBilElem left join rBilElem.siacTBilElem tBilElem ");
		
		// clausola di where
		jpql.append(" WHERE cartaCont.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		param.put("enteProprietarioId", idEnte);

		// clausola di validita' per la tabella siac_t_cartacont_det : inizio
		//jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("cartaContDet"));
		// clausola di validita' per la tabella siac_t_cartacont_det : fine
		
		// clausole di validita' per le tabelle di relazione : inizio
		jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rAttoClass"));
		jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rCartaContStatos"));
		jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rDetSoggetto"));
		jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rDetMovgestTs"));
		jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rBilElem"));
		
		if(subDocumentoSpesaId!=null)
			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rDetSubDocs"));
		// clausole di validita' per le tabelle di relazione : fine
		
		// annoEsercizio
	    jpql.append(" AND cartaCont.siacTBil.siacTPeriodo.anno = :annoEsercizio ");
		param.put("annoEsercizio", String.valueOf(annoEsercizio));
		
		// numero carta contabile : inizio
		if(numeroCartaContabileDa!=null){
			jpql.append(" AND cartaCont.cartacNumero >= :numeroCartaContabileDa ");
			param.put("numeroCartaContabileDa", numeroCartaContabileDa);				
		}
		
		if(numeroCartaContabileA!=null){
			jpql.append(" AND cartaCont.cartacNumero <= :numeroCartaContabileA ");
			param.put("numeroCartaContabileA", numeroCartaContabileA);				
		}
		// numero carta contabile : fine
		
		// data scadenza carta contabile : inizio
		if(dataScadenzaDa!=null){
			jpql.append(" AND cartaCont.cartacDataScadenza >= :dataScadenzaDa ");
			param.put("dataScadenzaDa", dataScadenzaDa);				
		}
		
		if(dataScadenzaA!=null){
			jpql.append(" AND cartaCont.cartacDataScadenza <= :dataScadenzaA ");
			param.put("dataScadenzaA", dataScadenzaA);				
		}
		// data scadenza carta contabile : fine			
		
		// oggetto
		if(!StringUtils.isEmpty(oggetto)){
			jpql.append(" AND UPPER(cartaCont.cartacOggetto) LIKE UPPER(:oggettoLike)");
			String oggettoLike = null;
			if(oggetto.contains("%")){
				oggettoLike = oggetto;
			} else {
				oggettoLike = '%' + oggetto + '%';
			}
			param.put("oggettoLike", oggettoLike);
		}
		
		// stato operativo
		if(statoOperativo!=null && !StringUtils.isEmpty(statoOperativo)){
			jpql.append(" AND dCartaContStato.cartacStatoCode = :statoOperativo");
			param.put("statoOperativo", statoOperativo);
		}
		
		// provvedimento : inizio
		// anno provvedimento
		if(annoProvvedimento!=null){
			jpql.append(" AND tAttoAmm.attoammAnno = :annoProvvedimento ");
			param.put("annoProvvedimento", String.valueOf(annoProvvedimento));
		}

		// numero provvedimento
		if(numeroProvvedimento!=null){
			jpql.append(" AND tAttoAmm.attoammNumero = :numeroProvvedimento ");
			param.put("numeroProvvedimento", numeroProvvedimento);
		}

		// tipo provvedimento
		if(tipoProvvedimento!=null && !StringUtils.isEmpty(tipoProvvedimento)){
			jpql.append(" AND dAttoAmmTipo.attoammTipoCode = :tipoProvvedimento");
			param.put("tipoProvvedimento", tipoProvvedimento);
		}

		// struttura amministrativa del provvedimento
		if(strutturaAmministrativaProvvedimento!=null && !StringUtils.isEmpty(strutturaAmministrativaProvvedimento)){
			jpql.append(" AND  tClassAtto.classifCode = :codeStruttura ");
			param.put("codeStruttura", strutturaAmministrativaProvvedimento);
		}
		// provvedimento : fine
		
		// codice creditore
		if(codiceCreditore!=null && !StringUtils.isEmpty(codiceCreditore)){
			jpql.append(" AND  tSoggetto.soggettoCode = :codiceCreditore ");
			param.put("codiceCreditore", codiceCreditore);
		}
		
		// impegno : inizio
		// anno impegno
		if(annoImpegno!=null){
			jpql.append(" AND tMovgestTs.siacTMovgest.movgestAnno = :annoImpegno ");
			param.put("annoImpegno", annoImpegno);
		}
		
		// numero impegno
		if(numeroImpegno!=null){
			jpql.append(" AND tMovgestTs.siacTMovgest.movgestNumero = :numeroImpegno ");
			param.put("numeroImpegno", numeroImpegno);
		}
		
		// numero sub-impegno
		if(numeroSubImpegno!=null){
			jpql.append(" AND tMovgestTs.movgestTsCode = :numeroSubImpegno ");
			param.put("numeroSubImpegno", String.valueOf(numeroSubImpegno));
		}
		// impegno : fine
		
		// 23/09/2014 : Modifico il filtro per capitolo
		// Filtro solo se e' presente la tripletta completa annoCapitolo + numeroCapitolo + numeroArticolo
		// Il parametro numeroUEB e' facoltativo, e filtro solo se e' presente la tripletta
		// l'analisi non lo prevedeva, ci e' stato comunicato telefonicamente
		if(annoCapitolo != null && numeroCapitolo != null && numeroArticolo != null){
			jpql.append(" AND tBilElem.siacTBil.siacTPeriodo.anno = :annoCapitolo ");
			param.put("annoCapitolo", String.valueOf(annoCapitolo));
			
			jpql.append(" AND tBilElem.elemCode = :numeroCapitolo ");
			param.put("numeroCapitolo", String.valueOf(numeroCapitolo));
			
			jpql.append(" AND tBilElem.elemCode2 = :numeroArticolo ");
			param.put("numeroArticolo", String.valueOf(numeroArticolo));
			
			// numero ueb
			if(numeroUEB != null){
				jpql.append(" AND tBilElem.elemCode3 = :numeroUEB ");
				param.put("numeroUEB", String.valueOf(numeroUEB));
			}
		}
		// capitolo : fine

		// sub-documento : inizio
		if(subDocumentoSpesaId!=null){
			jpql.append(" AND tSubDoc.subdocId = :subDocumentoSpesaId ");
			param.put("subDocumentoSpesaId", subDocumentoSpesaId);
		}
		// sub-documento : fine

		if(soloCount==false)
			jpql.append(" ORDER BY cartaCont.cartacNumero DESC ");
			
		query = createQuery(jpql.toString(), param);			


		//Termino restituendo l'oggetto di ritorno: 
        //Termino restituendo l'oggetto di ritorno: 
        return query;
	}

	/**
	 * Wrapper di creaQueryRicercaCarteContabili per avere un'anteprima del numero atteso di risultati
	 */
	@Override
	public Long contaCarteContabili(RicercaCartaContabileParamDto params, DatiOperazioneDto datiOperazione){
		Long conteggioCarteContabili = new Long(0);
		Query query = creaQueryRicercaCarteContabili(params, datiOperazione, true);
		conteggioCarteContabili = (Long)query.getSingleResult();
		//Termino restituendo l'oggetto di ritorno: 
        //Termino restituendo l'oggetto di ritorno: 
        return conteggioCarteContabili;
	}
	
	/**
	 * Wrapper di creaQueryRicercaCarteContabili per ottenere tutti i dati completi per la ricerca indicata
	 */
	@Override
	public List<SiacTCartacontFin> ricercaCarteContabili(RicercaCartaContabileParamDto params, DatiOperazioneDto datiOperazione, int numeroPagina, int numeroRisultatiPerPagina){
		List<SiacTCartacontFin> lista = new ArrayList<SiacTCartacontFin>();
		
		Query query = creaQueryRicercaCarteContabili(params, datiOperazione, false);

		if(numeroPagina == 0 || numeroRisultatiPerPagina == 0){ 
			lista = query.getResultList();
		} else {
			int start = (numeroPagina - 1) * numeroRisultatiPerPagina;
			int end = start + numeroRisultatiPerPagina;
			
			if (end > query.getResultList().size())
				end = query.getResultList().size();
			
			lista = query.getResultList().subList(start, end);
		}
		//Termino restituendo l'oggetto di ritorno: 
        return lista;
	}
     
	
	/**
	 * Carica un SiacTCartacontFin per chiave
	 */
	public SiacTCartacontFin ricercaCartaContabile(RicercaCartaContabileK pk, Integer idEnte, DatiOperazioneDto datiOperazione) {
	    
	    SiacTCartacontFin siacTCartacont = null;
	    siacTCartacont = siacTCartaContRepository.findCartaContByAnnoCartaNumeroCarta(idEnte, pk.getCartaContabile().getNumero(), 
	    																				  ((Integer)pk.getBilancio().getAnno()).toString(), 
	    																				  datiOperazione.getTs());
	    //Termino restituendo l'oggetto di ritorno: 
        return siacTCartacont;
	}
	
	/**
	 * Data una lista di id di sub documenti di spesa restiutuisce, tra essi, solo
	 * quelli che NON hanno collegamenti con carta
	 * @param subdocIdList
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Integer> soloSubDocNonCollegatiACarte(List<Integer> subdocIdList){
		List<Integer> filtrati = null;
		if(subdocIdList!=null && subdocIdList.size()>0){
			
			
			StringBuilder jpql = new StringBuilder();
			Map<String, Object> param = new HashMap<String, Object>();
			
			jpql.append(" SELECT sd.subdocId ");
			jpql.append(" FROM SiacTSubdoc sd ");
			
			//COSTRUZIONE DELLA CLAUSOLA IN DEGLI ID:
			jpql.append(" WHERE sd.subdocId IN ( ");
			for(int i=0;i<subdocIdList.size();i++){
				if(i>0){
					jpql.append(" , ");
				}
				String nameParametroIesimo = "subdocIdParam" + i;
				jpql.append(" :"+nameParametroIesimo + " ");
				param.put(nameParametroIesimo, subdocIdList.get(i));
			}
			jpql.append(" ) ");
			//
			
			//COSTRUZIONE DELLA CLAUSOLA NOT IN DEGLI ID CON CARTE:
			jpql.append(" AND sd.subdocId NOT IN ( ");
			
			jpql.append(" SELECT rc.siacTSubdoc.subdocId ");
			jpql.append(" FROM SiacRCartacontDetSubdoc rc ");
			
			jpql.append(" WHERE rc.siacTSubdoc.subdocId IN ( ");
			for(int i=0;i<subdocIdList.size();i++){
				if(i>0){
					jpql.append(" , ");
				}
				String nameParametroIesimo = "subdocIdParamRelCarta" + i;
				jpql.append(" :"+nameParametroIesimo + " ");
				param.put(nameParametroIesimo, subdocIdList.get(i));
			}
			jpql.append(" ) ");
			
			jpql.append(" AND rc.dataCancellazione IS NULL ");
			jpql.append(" AND (rc.dataFineValidita IS NULL OR rc.dataFineValidita > CURRENT_TIMESTAMP) ");
			
			jpql.append(" AND rc.siacTCartacontDet.dataCancellazione IS NULL ");
			jpql.append(" AND (rc.siacTCartacontDet.dataFineValidita IS NULL OR rc.siacTCartacontDet.dataFineValidita > CURRENT_TIMESTAMP) ");
			
			
			jpql.append(" ) ");
			///
			
			Query query = createQuery(jpql.toString(), param);
			
			filtrati = (List<Integer>)query.getResultList();
		}
		return filtrati;
	}
}