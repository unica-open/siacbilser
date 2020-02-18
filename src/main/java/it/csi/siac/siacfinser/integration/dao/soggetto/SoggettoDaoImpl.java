/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.soggetto;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.integration.entity.SiacTBase;
import it.csi.siac.siacfinser.CommonUtils;
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.StringUtils;
import it.csi.siac.siacfinser.TimingUtils;
import it.csi.siac.siacfinser.integration.dao.common.AbstractDao;
import it.csi.siac.siacfinser.integration.dao.common.dto.RicercaSoggettoParamDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.SoggettoTipoDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.SoggettoTipoModDto;
import it.csi.siac.siacfinser.integration.entity.SiacDSoggettoStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRComuneProvinciaFin;
import it.csi.siac.siacfinser.integration.entity.SiacRComuneRegioneFin;
import it.csi.siac.siacfinser.integration.entity.SiacRModpagOrdineFin;
import it.csi.siac.siacfinser.integration.entity.SiacRModpagStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsSogModFin;
import it.csi.siac.siacfinser.integration.entity.SiacRProvinciaRegioneFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoRelazFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoRelazStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggrelModpagFin;
import it.csi.siac.siacfinser.integration.entity.SiacTComuneFin;
import it.csi.siac.siacfinser.integration.entity.SiacTIndirizzoSoggettoFin;
import it.csi.siac.siacfinser.integration.entity.SiacTLiquidazioneFin;
import it.csi.siac.siacfinser.integration.entity.SiacTModpagFin;
import it.csi.siac.siacfinser.integration.entity.SiacTModpagModFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsFin;
import it.csi.siac.siacfinser.integration.entity.SiacTNazioneFin;
import it.csi.siac.siacfinser.integration.entity.SiacTPersonaFisicaFin;
import it.csi.siac.siacfinser.integration.entity.SiacTPersonaFisicaModFin;
import it.csi.siac.siacfinser.integration.entity.SiacTPersonaGiuridicaFin;
import it.csi.siac.siacfinser.integration.entity.SiacTPersonaGiuridicaModFin;
import it.csi.siac.siacfinser.integration.entity.SiacTProvinciaFin;
import it.csi.siac.siacfinser.integration.entity.SiacTRegioneFin;
import it.csi.siac.siacfinser.integration.entity.SiacTSoggettoFin;
import it.csi.siac.siacfinser.integration.util.DataValiditaUtils;
import it.csi.siac.siacfinser.integration.util.DatiOperazioneUtils;
import it.csi.siac.siacfinser.model.soggetto.Soggetto.StatoOperativoAnagrafica;

@Component
@Transactional
public class SoggettoDaoImpl extends AbstractDao<SiacTSoggettoFin, Integer> implements SoggettoDao{
	
	@Autowired
	SiacTSoggettoFinRepository siacTSoggettoRepository;

	@Autowired
	SiacTSoggettoModRepository siacTSoggettoModRepository;

	@Autowired
	SiacTPersonaFisicaRepository siacTPersonaFisicaRepository;

	@Autowired
	SiacTPersonaGiuridicaRepository siacTPersonaGiuridicaRepository;

	@Autowired
	SiacTPersonaGiuridicaModRepository siacTPersonaGiuridicaModRepository;

	@Autowired
	SiacTPersonaFisicaModRepository siacTPersonaFisicaModRepository;

	@Autowired
	SiacDSoggettoStatoRepository siacDSoggettoStatoRepository;

	@Override
	public SiacTSoggettoFin create(SiacTSoggettoFin soggetto){
		super.save(soggetto);
		//Termino restituendo l'oggetto di ritorno: 
        return soggetto;
	}

	/**
	 * E' il metodo di "engine" di ricerca dei soggetti.
	 * Utilizzato sia per avere un'anteprima del numero di risultati attesi (rispetto al filtro indicato)
	 * sia per recuperare tutti i dati (rispetto al filtro indicato)
	 */
	@Override 
	public Query creaQueryRicercaSoggetti(Integer enteUid, RicercaSoggettoParamDto prs,  String codiceAmbito, boolean soloCount){
		Map<String,Object> param = new HashMap<String, Object>();
		Date nowDate = TimingUtils.getNowDate();
		param.put(DataValiditaUtils.NOW_DATE_PARAM_JPQL, nowDate);
		Query query = null;
		// PARAMETRI DI INPUT:
		String codiceSoggetto = prs.getCodiceSoggetto();
		String partitaIva = prs.getPartitaIva();
		String codiceFiscale = prs.getCodiceFiscale();
		String denominazione = prs.getDenominazione();
		String classe= prs.getClasse();
		String titoloNaturaGiuridica= prs.getTitoloNaturaGiuridica();
		String formaGiuridica= prs.getFormaGiuridica();
		String statoSoggetto= prs.getStatoSoggetto();
		String matricola = prs.getMatricola();
		//SIAC-6565-CR1215
		String emailPec = prs.getEmailPec();
		String codDestinatario = prs.getCodDestinatario();

		StringBuilder jpql = new StringBuilder("Select sogg FROM SiacTSoggettoFin sogg left join fetch sogg.siacDAmbito amb ");

		// Condizione dinamiche dopo il WHERE			
		if(!StringUtils.isEmpty(codiceSoggetto)){
			jpql.append(" WHERE sogg.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
			
			
			param.put("enteProprietarioId", enteUid);

			jpql.append(" AND sogg.soggettoCode = :codiceSoggetto ");
			param.put("codiceSoggetto", codiceSoggetto); 
		} else {
			if(!StringUtils.isEmpty(classe)){
				jpql.append(" , SiacRSoggettoClasseFin rsogclas ");
			}

			if(!StringUtils.isEmpty(titoloNaturaGiuridica) || !StringUtils.isEmpty(formaGiuridica)){
				jpql.append(" , SiacRFormaGiuridicaFin rformagiu ");
			}

			if(!StringUtils.isEmpty(statoSoggetto)){
				jpql.append(" , SiacRSoggettoStatoFin rsoggStato ");
			}

			jpql.append(" WHERE sogg.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
			
			param.put("enteProprietarioId", enteUid);
			
			
			if(!StringUtils.isEmpty(partitaIva)){
				jpql.append(" AND sogg.partitaIva = :partitaIva ");
				param.put("partitaIva", partitaIva);
			}

			if(!StringUtils.isEmpty(codiceFiscale)){
				jpql.append(" AND UPPER(sogg.codiceFiscale) = UPPER(:codiceFiscale) ");
				param.put("codiceFiscale", codiceFiscale);
			}

			if(!StringUtils.isEmpty(prs.getCodiceFiscaleEstero())){
				jpql.append(" AND UPPER(sogg.codiceFiscaleEstero) = UPPER(:codiceFiscaleEstero) ");
				param.put("codiceFiscaleEstero", prs.getCodiceFiscaleEstero());
			}

			if(!StringUtils.isEmpty(denominazione)){
				jpql.append(" AND UPPER(sogg.soggettoDesc) LIKE UPPER(:denominazione) ");
				String denomLike = null;
				if(denominazione.contains("%")){
					denomLike = denominazione;
				} else {
					denomLike = '%' + denominazione + '%';
				}
				param.put("denominazione", denomLike);
			}
			//SIAC-6565-CR1215
			if(!StringUtils.isEmpty(emailPec)){
				jpql.append(" AND UPPER(sogg.emailPec) LIKE UPPER(:emailPec) ");
				String denomLike = null;
				if(emailPec.contains("%")){
					denomLike = emailPec;
				} else {
					denomLike = '%' + emailPec + '%';
				}
				param.put("emailPec", denomLike);
			}
			if(!StringUtils.isEmpty(codDestinatario)){
				jpql.append(" AND UPPER(sogg.codDestinatario) LIKE UPPER(:codDestinatario) ");
				String denomLike = null;
				if(codDestinatario.contains("%")){
					denomLike = codDestinatario;
				} else {
					denomLike = '%' + codDestinatario + '%';
				}
				param.put("codDestinatario", denomLike);
			}

			if(!StringUtils.isEmpty(classe)){
				jpql.append(" AND rsogclas.siacDSoggettoClasse.soggettoClasseId = :classe ");				
				param.put("classe",  Integer.valueOf(classe));
				//jpql.append(" and rsogclas.dataFineValidita is null ");
				jpql.append(" AND  ").append(DataValiditaUtils.validitaForQuery("rsogclas"));
				jpql.append(" and rsogclas.siacTSoggetto.soggettoId = sogg.soggettoId ");
			}

			if(!StringUtils.isEmpty(titoloNaturaGiuridica) || !StringUtils.isEmpty(formaGiuridica)){
				jpql.append(" and rformagiu.siacTSoggetto.soggettoId = sogg.soggettoId ");
				jpql.append(" AND  ").append(DataValiditaUtils.validitaForQuery("rformagiu"));
				if(!StringUtils.isEmpty(titoloNaturaGiuridica)){
					jpql.append(" and rformagiu.siacTFormaGiuridica.formaGiuridicaDesc = :descFormaGiuridica ");
					param.put("descFormaGiuridica", titoloNaturaGiuridica);
				}

				if(!StringUtils.isEmpty(formaGiuridica)){
					jpql.append(" and rformagiu.siacTFormaGiuridica.formaGiuridicaId = :tipoFormaGiuridica ");
					param.put("tipoFormaGiuridica", Integer.parseInt(formaGiuridica));
				}
			}

			if(!StringUtils.isEmpty(statoSoggetto)){
				SiacDSoggettoStatoFin siacDSoggettoStato = siacDSoggettoStatoRepository.findOne(Integer.parseInt(statoSoggetto));
				List<SiacDSoggettoStatoFin> listaStati = siacDSoggettoStatoRepository.findValidoByEnteAndByCode(enteUid, new Timestamp(System.currentTimeMillis()), siacDSoggettoStato.getSoggettoStatoCode());

				SiacDSoggettoStatoFin siacSoggettoStatoSelezionato = listaStati.get(0);
				if(siacSoggettoStatoSelezionato.getSoggettoStatoCode().equals(StatoOperativoAnagrafica.IN_MODIFICA.toString())){
					// Stato "virtuale" IN_MODIFICA
					List<SiacDSoggettoStatoFin>  listaStatoValido = siacDSoggettoStatoRepository.findValidoByEnteAndByCode(enteUid,
							                                                                                            new Timestamp(System.currentTimeMillis()), 
							                                                                                            StatoOperativoAnagrafica.VALIDO.toString());

					SiacDSoggettoStatoFin statoValido =  listaStatoValido.get(0);

					jpql.append(" and rsoggStato.siacDSoggettoStato.soggettoStatoId = :stato ");
					param.put("stato", statoValido.getUid());

					jpql.append(" AND EXISTS ( SELECT 1 FROM SiacTSoggettoModFin modp WHERE modp.siacTSoggetto=sogg ");
					jpql.append(" AND  ").append(DataValiditaUtils.validitaForQuery("modp")).append(" ) ");
				}else{
					// Tutti gli altri stati
					jpql.append(" and rsoggStato.siacDSoggettoStato.soggettoStatoId = :stato ");
					param.put("stato", Integer.parseInt(statoSoggetto));

				}
				
				jpql.append(" and sogg.siacTEnteProprietario.enteProprietarioId = rsoggStato.siacTEnteProprietario.enteProprietarioId ");
				jpql.append(" AND  ").append(DataValiditaUtils.validitaForQuery("rsoggStato"));
				jpql.append(" and rsoggStato.siacTSoggetto.soggettoId = sogg.soggettoId ");
			}
			
			
			if (org.apache.commons.lang.StringUtils.isNotEmpty(matricola))
			{
				param.put("matricola", matricola);

				jpql.append(" and EXISTS " +
						" (SELECT 1 FROM SiacRSoggettoAttrFin sattr WHERE sattr.siacTSoggetto=sogg AND " +
						"  sattr.testo=:matricola AND sattr.siacTEnteProprietario.enteProprietarioId=:enteProprietarioId AND EXISTS " +
						" (SELECT 1 FROM SiacTAttr attr WHERE attr.attrCode='Matricola' AND attr.siacTEnteProprietario.enteProprietarioId=:enteProprietarioId  ) )");
			}
		}

		jpql.append(" AND sogg.siacDAmbito.ambitoCode=:codiceAmbito ");
		param.put("codiceAmbito", codiceAmbito);

		
		// Mi permette di tirare fuori solo soggetti e non sedi
		// jira-525 : per distinguere un soggetto da una sede e' sbagliato testare la valorizzazione dei campi codice_fiscale, partita_iva, codice_fiscale_estero.
		// E' invece necessario controllare che non esista una relazione di tipo SEDE_SECONDARIA (campo soggetto_id_a della tabella siac_r_soggetto_relaz)
		// per il soggetto in questione 
		
		jpql.append(" AND NOT EXISTS (SELECT 1 FROM SiacRSoggettoRelazFin srsr WHERE srsr.siacTSoggetto2.soggettoId=sogg.soggettoId AND ");
		jpql.append(DataValiditaUtils.validitaForQuery("srsr"));
		
		
		jpql.append(" AND srsr.siacTEnteProprietario.enteProprietarioId = sogg.siacTEnteProprietario.enteProprietarioId ");
		jpql.append(" AND srsr.siacDRelazTipo.siacTEnteProprietario.enteProprietarioId = sogg.siacTEnteProprietario.enteProprietarioId ");
		
		jpql.append(" AND srsr.siacDRelazTipo.relazTipoCode = :tipoRelazioneSedeSecondaria ");
		jpql.append(" AND  ").append(DataValiditaUtils.validitaForQuery("srsr.siacDRelazTipo")).append(" ) ");;
		
		param.put("tipoRelazioneSedeSecondaria", Constanti.SEDE_SECONDARIA);
		
		jpql.append("order by to_number(sogg.soggettoCode, '999999999999')");

		query = createQuery(jpql.toString(), param);			


		//Termino restituendo l'oggetto di ritorno: 
        return query;
	}
	
	/**
	 * Wrapper di "creaQueryRicercaSoggetti"
	 */
	public List<SiacTSoggettoFin> ricercaSoggetti(Integer enteUid, RicercaSoggettoParamDto prs, String codiceAmbito) throws RuntimeException{
		List<SiacTSoggettoFin> lista = new ArrayList<SiacTSoggettoFin>();
		Query query = creaQueryRicercaSoggetti(enteUid, prs, codiceAmbito, false);
		if(query.getResultList().size() > Constanti.MAX_RIGHE_ESTRAIBILI){
			lista = query.setMaxResults(Constanti.MAX_RIGHE_ESTRAIBILI + 1).getResultList();
		}else{
			lista = query.getResultList();
		}
		//Termino restituendo l'oggetto di ritorno: 
        return lista;
	}
	
	/**
	 * Wrapper di "creaQueryRicercaSoggetti"
	 */
	public List<SiacTSoggettoFin> ricercaSoggettiOttimizzato(Integer enteUid, RicercaSoggettoParamDto prs, String codiceAmbito) throws RuntimeException{
		List<SiacTSoggettoFin> lista = new ArrayList<SiacTSoggettoFin>();
		Query query = creaQueryRicercaSoggetti(enteUid, prs, codiceAmbito, false);
		lista = query.getResultList();
		//Termino restituendo l'oggetto di ritorno: 
        return lista;
	}

	/**
	 * Si parte dal presupposto che un soggetto puo' essere o una persona fisica oppure una persona giuridica:
	 * Nel caso sia una perona fisica avra' un legame con un record valido di tipo siactTpersonaFisica.
	 * Nel caso sia una perona giuridica avra' un legame con un record valido di tipo siactTpersonaGiuridica.
	 */
	public SoggettoTipoDto getPersonaFisicaOppureGiuridica(Integer idSiacTSoggetto){
		SoggettoTipoDto soggettoTipoDto = new SoggettoTipoDto();
		Timestamp now= getNow();
		List<SiacTPersonaFisicaFin> listaPersonaFisica = siacTPersonaFisicaRepository.findValidaByIdSoggetto(idSiacTSoggetto,now);
		if(listaPersonaFisica==null || listaPersonaFisica.size()==0){
			boolean isPersonaFisica = false;
			List<SiacTPersonaGiuridicaFin> listaPersonaGiuridica = siacTPersonaGiuridicaRepository.findValidaByIdSoggetto(idSiacTSoggetto,now);
			if(null!=listaPersonaGiuridica && listaPersonaGiuridica.size() > 0){
				for(SiacTPersonaGiuridicaFin siacTPersonaGiuridica : listaPersonaGiuridica){
					if(null!=siacTPersonaGiuridica && siacTPersonaGiuridica.getDataFineValidita() == null){
						soggettoTipoDto.setPersonaFisica(isPersonaFisica);
						soggettoTipoDto.setSiactTpersonaGiuridica(siacTPersonaGiuridica);
					}
				}					
			}
		} else {
			boolean isPersonaFisica = true;				
			if(null!=listaPersonaFisica && listaPersonaFisica.size() > 0){
				for(SiacTPersonaFisicaFin siacTPersonaFisica : listaPersonaFisica){
					if(null!=siacTPersonaFisica && siacTPersonaFisica.getDataFineValidita() == null){
						soggettoTipoDto.setPersonaFisica(isPersonaFisica);
						soggettoTipoDto.setSiactTpersonaFisica(siacTPersonaFisica);
					}
				}					
			}
		}
		//Termino restituendo l'oggetto di ritorno: 
        return soggettoTipoDto;
	}

	/**
	 * Si parte dal presupposto che un soggetto puo' essere o una persona fisica oppure una persona giuridica:
	 * Nel caso sia una perona fisica avra' un legame con un record valido di tipo siactTpersonaFisica.
	 * Nel caso sia una perona giuridica avra' un legame con un record valido di tipo siactTpersonaGiuridica.
	 */
	public SoggettoTipoModDto getPersonaFisicaOppureGiuridicaMod(Integer idSogMod){
		SoggettoTipoModDto soggettoTipoDto = new SoggettoTipoModDto();
		Timestamp now =  getNow();
		List<SiacTPersonaFisicaModFin> listaPersonaFisica = siacTPersonaFisicaModRepository.findValidoByIdSoggMod(idSogMod,now);
		if(listaPersonaFisica==null || listaPersonaFisica.size()==0){
			boolean isPersonaFisica = false;
			List<SiacTPersonaGiuridicaModFin> listaPersonaGiuridica = siacTPersonaGiuridicaModRepository.findValidoByIdSoggMod(idSogMod,now);
			if(null!=listaPersonaGiuridica && listaPersonaGiuridica.size() > 0){
				for(SiacTPersonaGiuridicaModFin siacTPersonaGiuridicaMod : listaPersonaGiuridica){
					if(null!=siacTPersonaGiuridicaMod && siacTPersonaGiuridicaMod.getDataFineValidita() == null){
						// SiacTPersonaGiuridicaFin personaGiuridica = listaPersonaGiuridica.get(0);
						soggettoTipoDto.setPersonaFisica(isPersonaFisica);
						soggettoTipoDto.setSiactTpersonaGiuridicaMod(siacTPersonaGiuridicaMod);
					}
				}	
			}
		} else {
			boolean isPersonaFisica = true;
			if(null!=listaPersonaFisica && listaPersonaFisica.size() > 0){
				for(SiacTPersonaFisicaModFin siacTPersonaFisicaMod : listaPersonaFisica){
					if(null!=siacTPersonaFisicaMod && siacTPersonaFisicaMod.getDataFineValidita() == null){
						soggettoTipoDto.setPersonaFisica(isPersonaFisica);
						soggettoTipoDto.setSiactTpersonaFisicaMod(siacTPersonaFisicaMod);
					}
				}					
			}
		}
		//Termino restituendo l'oggetto di ritorno: 
        return soggettoTipoDto;
	}
	
	public List<SiacTSoggettoFin> ricercaBySiacRMovgestTsSogModFin(List<SiacRMovgestTsSogModFin> listaInput, Boolean validi) {
		List<SiacTSoggettoFin> listaRitorno = new ArrayList<SiacTSoggettoFin>();
		if(listaInput!=null && listaInput.size()>0){
			List<List<SiacRMovgestTsSogModFin>> esploso = StringUtils.esplodiInListe(listaInput, DIMENSIONE_MASSIMA_QUERY_IN);
			if(esploso!=null && esploso.size()>0){
				for(List<SiacRMovgestTsSogModFin> listaIt : esploso){
					List<SiacTSoggettoFin> risultatoParziale = ricercaBySiacRMovgestTsSogModFinCORE(listaIt,validi);
					if(risultatoParziale!=null && risultatoParziale.size()>0){
						listaRitorno.addAll(risultatoParziale);
					}
				}
				//per sicurezza che non ci siano doppioni:
				listaRitorno = CommonUtils.ritornaSoloDistintiByUid(listaRitorno);
			}
		}
        return listaRitorno;
	}
	
	/**
	 * E' un metodo di caricamento dati "massivo" utile per ottimizzare servizi di ricerca onerosi.
	 * Dato in input un elenco di SiacRMovgestTsSogModFin vengono restituiti TUTTI I DISTINTI SiacTSoggettoFin in relazione
	 * con i SiacRMovgestTsSogModFin indicati.
	 * 
	 * validi = true     --> ritorna solo validi
	 * validi = false    --> ritorna solo non validi
	 * validi = null     --> ritorna tutti
	 */
	private List<SiacTSoggettoFin> ricercaBySiacRMovgestTsSogModFinCORE(List<SiacRMovgestTsSogModFin> listaSiacRMovgestTsSogModFin, Boolean validi) {
		Map<String,Object> param = new HashMap<String, Object>();
		List<SiacTSoggettoFin> listaRitorno = new ArrayList<SiacTSoggettoFin>();
		
		if(listaSiacRMovgestTsSogModFin!=null && listaSiacRMovgestTsSogModFin.size()>0){
			
			StringBuilder jpql = new StringBuilder("SELECT DISTINCT sf FROM SiacRMovgestTsSogModFin rs, SiacTSoggettoFin sf WHERE ");
			
			jpql.append(" rs.movgestTsSogModId IN ( ");
			int i =0;
			for(SiacRMovgestTsSogModFin it: listaSiacRMovgestTsSogModFin){
				if(i>0){
					jpql.append(" , ");
				}
				String idParamName = "id" + i;
				jpql.append(" :"+idParamName+" ");
				param.put(idParamName, it.getMovgestTsSogModId());
				i++;
			}
			jpql.append(" ) ");
			
			jpql.append(" AND ( sf.soggettoId = rs.siacTSoggetto1.soggettoId OR sf.soggettoId = rs.siacTSoggetto2.soggettoId ) ");
			
			if(validi!=null && validi){
				jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("sf"));
				param.put(DataValiditaUtils.NOW_DATE_PARAM_JPQL, getNowDate());
			}
			
			//LANCIO DELLA QUERY:
			Query query =  createQuery(jpql.toString(), param);
			listaRitorno = query.getResultList();
			
			//FILTRO VALIDI O MENO:
			if(validi!=null && !validi){
				//solo per non validi, i validi vengono richiesti on-query
				listaRitorno = DatiOperazioneUtils.filtraByValidita(listaRitorno, getNow(),validi);
			}
			//
			
		}
		//Termino restituendo l'oggetto di ritorno: 
        return listaRitorno;
	}
	
	public List<SiacTSoggettoFin> ricercaBySiacTMovgestPkMassive(List<SiacTMovgestTsFin> listaInput) {
		List<SiacTSoggettoFin> listaRitorno = new ArrayList<SiacTSoggettoFin>();
		if(listaInput!=null && listaInput.size()>0){
			List<List<SiacTMovgestTsFin>> esploso = StringUtils.esplodiInListe(listaInput, DIMENSIONE_MASSIMA_QUERY_IN);
			if(esploso!=null && esploso.size()>0){
				for(List<SiacTMovgestTsFin> listaIt : esploso){
					List<SiacTSoggettoFin> risultatoParziale = ricercaBySiacTMovgestPkMassiveCORE(listaIt);
					if(risultatoParziale!=null && risultatoParziale.size()>0){
						listaRitorno.addAll(risultatoParziale);
					}
				}
				//per sicurezza che non ci siano doppioni:
				listaRitorno = CommonUtils.ritornaSoloDistintiByUid(listaRitorno);
			}
		}
        return listaRitorno;
	}
	
	
	/**
	 * E' un metodo di caricamento dati "massivo" utile per ottimizzare servizi di ricerca onerosi.
	 * Dato in input un elenco di SiacTMovgestTsFin vengono restituiti TUTTI I DISTINTI SiacTSoggettoFin in relazione
	 * con i SiacTMovgestTsFin indicati.
	 */
	private List<SiacTSoggettoFin> ricercaBySiacTMovgestPkMassiveCORE(List<SiacTMovgestTsFin> listaSiacTMovgestTs) {
		Map<String,Object> param = new HashMap<String, Object>();
		List<SiacTSoggettoFin> listaRitorno = new ArrayList<SiacTSoggettoFin>();
		
		if(listaSiacTMovgestTs!=null && listaSiacTMovgestTs.size()>0){
			
			StringBuilder jpql = new StringBuilder("SELECT DISTINCT rs.siacTSoggetto FROM SiacRMovgestTsSogFin rs WHERE ");
			
			jpql.append(" rs.siacTMovgestT.movgestTsId IN ( ");
			int i =0;
			for(SiacTMovgestTsFin it: listaSiacTMovgestTs){
				if(i>0){
					jpql.append(" , ");
				}
				String idParamName = "id" + i;
				jpql.append(" :"+idParamName+" ");
				param.put(idParamName, it.getMovgestTsId());
				i++;
			}
			jpql.append(" ) ");
			
			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rs.siacTSoggetto"));
			param.put(DataValiditaUtils.NOW_DATE_PARAM_JPQL, getNowDate());

			
			//LANCIO DELLA QUERY:
			Query query =  createQuery(jpql.toString(), param);
			listaRitorno = query.getResultList();
		}
		//Termino restituendo l'oggetto di ritorno: 
        return listaRitorno;
	}
	
	
	public List<SiacTSoggettoFin> ricercaBySiacTLiquidazionePkMassive(List<SiacTLiquidazioneFin> listaInput) {
		List<SiacTSoggettoFin> listaRitorno = new ArrayList<SiacTSoggettoFin>();
		if(listaInput!=null && listaInput.size()>0){
			List<List<SiacTLiquidazioneFin>> esploso = StringUtils.esplodiInListe(listaInput, DIMENSIONE_MASSIMA_QUERY_IN);
			if(esploso!=null && esploso.size()>0){
				for(List<SiacTLiquidazioneFin> listaIt : esploso){
					List<SiacTSoggettoFin> risultatoParziale = ricercaBySiacTLiquidazionePkMassiveCORE(listaIt);
					if(risultatoParziale!=null && risultatoParziale.size()>0){
						listaRitorno.addAll(risultatoParziale);
					}
				}
				//per sicurezza che non ci siano doppioni:
				listaRitorno = CommonUtils.ritornaSoloDistintiByUid(listaRitorno);
			}
		}
        return listaRitorno;
	}
	
	/**
	 * E' un metodo di caricamento dati "massivo" utile per ottimizzare servizi di ricerca onerosi.
	 * Dato in input un elenco di SiacTLiquidazioneFin vengono restituiti TUTTI I DISTINTI SiacTSoggettoFin in relazione
	 * con i SiacTLiquidazioneFin indicati.
	 */
	private List<SiacTSoggettoFin> ricercaBySiacTLiquidazionePkMassiveCORE(List<SiacTLiquidazioneFin> listaSiacTLiquidazione) {
		Map<String,Object> param = new HashMap<String, Object>();
		List<SiacTSoggettoFin> listaRitorno = new ArrayList<SiacTSoggettoFin>();
		
		if(listaSiacTLiquidazione!=null && listaSiacTLiquidazione.size()>0){
			
			StringBuilder jpql = new StringBuilder("SELECT DISTINCT rs.siacTSoggetto FROM SiacRLiquidazioneSoggettoFin rs WHERE ");
			
			jpql.append(" rs.siacTLiquidazione.liqId IN ( ");
			int i =0;
			for(SiacTLiquidazioneFin it: listaSiacTLiquidazione){
				if(i>0){
					jpql.append(" , ");
				}
				String idParamName = "id" + i;
				jpql.append(" :"+idParamName+" ");
				param.put(idParamName, it.getLiqId());
				i++;
			}
			jpql.append(" ) ");
			
			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rs.siacTSoggetto"));
			param.put(DataValiditaUtils.NOW_DATE_PARAM_JPQL, getNowDate());
			
			//LANCIO DELLA QUERY:
			Query query =  createQuery(jpql.toString(), param);
			listaRitorno = query.getResultList();
		}
		//Termino restituendo l'oggetto di ritorno: 
        return listaRitorno;
	}
	
	public List<SiacRSoggettoRelazFin> ricercaSiacRSoggettoRelazMassive(List<SiacTSoggettoFin> listaInput) {
		List<SiacRSoggettoRelazFin> listaRitorno = new ArrayList<SiacRSoggettoRelazFin>();
		if(listaInput!=null && listaInput.size()>0){
			List<List<SiacTSoggettoFin>> esploso = StringUtils.esplodiInListe(listaInput, DIMENSIONE_MASSIMA_QUERY_IN);
			if(esploso!=null && esploso.size()>0){
				for(List<SiacTSoggettoFin> listaIt : esploso){
					List<SiacRSoggettoRelazFin> risultatoParziale = ricercaSiacRSoggettoRelazMassiveCORE(listaIt);
					if(risultatoParziale!=null && risultatoParziale.size()>0){
						listaRitorno.addAll(risultatoParziale);
					}
				}
				//per sicurezza che non ci siano doppioni:
				listaRitorno = CommonUtils.ritornaSoloDistintiByUid(listaRitorno);
			}
		}
        return listaRitorno;
	}
	
	/**
	 * E' un metodo di caricamento dati "massivo" utile per ottimizzare servizi di ricerca onerosi.
	 * Dato in input un elenco di SiacTSoggettoFin vengono restituiti TUTTI I DISTINTI SiacRSoggettoRelazFin in relazione
	 * con i SiacTSoggettoFin indicati.
	 */
	private List<SiacRSoggettoRelazFin> ricercaSiacRSoggettoRelazMassiveCORE(List<SiacTSoggettoFin> listaSiacTSoggetto) {
		Map<String,Object> param = new HashMap<String, Object>();
		List<SiacRSoggettoRelazFin> listaRitorno = new ArrayList<SiacRSoggettoRelazFin>();
		
		if(listaSiacTSoggetto!=null && listaSiacTSoggetto.size()>0){
			
			StringBuilder jpql = new StringBuilder("SELECT DISTINCT rs FROM SiacRSoggettoRelazFin rs WHERE ");
			
			jpql.append(" ( ");
			int i =0;
			for(SiacTSoggettoFin it: listaSiacTSoggetto){
				if(i>0){
					jpql.append(" OR ");
				}
				String idParamName = "id" + i;
				jpql.append(" ( ");
				
					jpql.append(" rs.siacTSoggetto1.soggettoId = :"+idParamName+" ");
					jpql.append(" OR rs.siacTSoggetto2.soggettoId = :"+idParamName+" ");
				
				jpql.append(" ) ");
				param.put(idParamName, it.getSoggettoId());
				i++;
			}
			jpql.append(" ) ");
			
			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rs"));
			param.put(DataValiditaUtils.NOW_DATE_PARAM_JPQL, getNowDate());
			
			//LANCIO DELLA QUERY:
			Query query =  createQuery(jpql.toString(), param);
			listaRitorno = query.getResultList();
		}
		//Termino restituendo l'oggetto di ritorno: 
        return listaRitorno;
	}
	
	public List<SiacTSoggettoFin> ricercaSiacTSoggettoFinBySiacRSoggettoRelazFin(List<SiacRSoggettoRelazFin> listaInput) {
		List<SiacTSoggettoFin> listaRitorno = new ArrayList<SiacTSoggettoFin>();
		if(listaInput!=null && listaInput.size()>0){
			List<List<SiacRSoggettoRelazFin>> esploso = StringUtils.esplodiInListe(listaInput, DIMENSIONE_MASSIMA_QUERY_IN);
			if(esploso!=null && esploso.size()>0){
				for(List<SiacRSoggettoRelazFin> listaIt : esploso){
					List<SiacTSoggettoFin> risultatoParziale = ricercaSiacTSoggettoFinBySiacRSoggettoRelazFinCORE(listaIt);
					if(risultatoParziale!=null && risultatoParziale.size()>0){
						listaRitorno.addAll(risultatoParziale);
					}
				}
				//per sicurezza che non ci siano doppioni:
				listaRitorno = CommonUtils.ritornaSoloDistintiByUid(listaRitorno);
			}
		}
        return listaRitorno;
	}
	
	private List<SiacTSoggettoFin> ricercaSiacTSoggettoFinBySiacRSoggettoRelazFinCORE(List<SiacRSoggettoRelazFin> listaSiacRSoggettoRelazFin) {
		Map<String,Object> param = new HashMap<String, Object>();
		List<SiacTSoggettoFin> listaRitorno = new ArrayList<SiacTSoggettoFin>();
		
		if(listaSiacRSoggettoRelazFin!=null && listaSiacRSoggettoRelazFin.size()>0){
			
			StringBuilder jpql = new StringBuilder("SELECT DISTINCT sogg FROM SiacTSoggettoFin sogg WHERE ");
			
			jpql.append(" ( ");
			int i =0;
			for(SiacRSoggettoRelazFin it: listaSiacRSoggettoRelazFin){
				
				
				if(it.getSiacTSoggetto1()!=null){
					
					if(i>0){
						jpql.append(" OR ");
					}
					String idParamName = "id" + i;
					jpql.append(" ( ");
					
						jpql.append(" sogg.soggettoId = :"+idParamName+" ");
					
					jpql.append(" ) ");
					param.put(idParamName, it.getSiacTSoggetto1().getSoggettoId());
					i++;
					
				}
				
				if(it.getSiacTSoggetto2()!=null){
					
					if(i>0){
						jpql.append(" OR ");
					}
					String idParamName = "id" + i;
					jpql.append(" ( ");
					
						jpql.append(" sogg.soggettoId = :"+idParamName+" ");
					
					jpql.append(" ) ");
					param.put(idParamName, it.getSiacTSoggetto2().getSoggettoId());
					i++;
					
				}
				
				
			}
			jpql.append(" ) ");
			
			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("sogg"));
			param.put(DataValiditaUtils.NOW_DATE_PARAM_JPQL, getNowDate());
			
			//LANCIO DELLA QUERY:
			Query query =  createQuery(jpql.toString(), param);
			listaRitorno = query.getResultList();
		}
		//Termino restituendo l'oggetto di ritorno: 
        return listaRitorno;
	}
	
	
	public List<SiacRSoggrelModpagFin> ricercaSiacRSoggrelModpagFinMassive(List<SiacRSoggettoRelazFin> listaInput) {
		List<SiacRSoggrelModpagFin> listaRitorno = new ArrayList<SiacRSoggrelModpagFin>();
		if(listaInput!=null && listaInput.size()>0){
			List<List<SiacRSoggettoRelazFin>> esploso = StringUtils.esplodiInListe(listaInput, DIMENSIONE_MASSIMA_QUERY_IN);
			if(esploso!=null && esploso.size()>0){
				for(List<SiacRSoggettoRelazFin> listaIt : esploso){
					List<SiacRSoggrelModpagFin> risultatoParziale = ricercaSiacRSoggrelModpagFinMassiveCORE(listaIt);
					if(risultatoParziale!=null && risultatoParziale.size()>0){
						listaRitorno.addAll(risultatoParziale);
					}
				}
				//per sicurezza che non ci siano doppioni:
				listaRitorno = CommonUtils.ritornaSoloDistintiByUid(listaRitorno);
			}
		}
        return listaRitorno;
	}
	
	/**
	 * E' un metodo di caricamento dati "massivo" utile per ottimizzare servizi di ricerca onerosi.
	 * Dato in input un elenco di SiacRSoggettoRelazFin vengono restituiti TUTTI I DISTINTI SiacRSoggettoRelazModFin in relazione
	 * con i listaSiacTSiacRSoggettoRelazFin indicati.
	 */
	private List<SiacRSoggrelModpagFin> ricercaSiacRSoggrelModpagFinMassiveCORE(List<SiacRSoggettoRelazFin> listaSiacTSiacRSoggettoRelazFin) {
		Map<String,Object> param = new HashMap<String, Object>();
		List<SiacRSoggrelModpagFin> listaRitorno = new ArrayList<SiacRSoggrelModpagFin>();
		
		if(listaSiacTSiacRSoggettoRelazFin!=null && listaSiacTSiacRSoggettoRelazFin.size()>0){
			
			StringBuilder jpql = new StringBuilder("SELECT DISTINCT rs FROM SiacRSoggrelModpagFin rs WHERE ");
			
			jpql.append(" ( ");
			int i =0;
			for(SiacRSoggettoRelazFin it: listaSiacTSiacRSoggettoRelazFin){
				if(i>0){
					jpql.append(" OR ");
				}
				String idParamName = "id" + i;
				jpql.append(" ( ");
				
					jpql.append(" rs.siacRSoggettoRelaz.soggettoRelazId = :"+idParamName+" ");
				
				jpql.append(" ) ");
				param.put(idParamName, it.getSoggettoRelazId());
				i++;
			}
			jpql.append(" ) ");
			
			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rs"));
			param.put(DataValiditaUtils.NOW_DATE_PARAM_JPQL, getNowDate());

			
			//LANCIO DELLA QUERY:
			Query query =  createQuery(jpql.toString(), param);
			listaRitorno = query.getResultList();
		}
		//Termino restituendo l'oggetto di ritorno: 
        return listaRitorno;
	}
	
	public List<SiacRSoggettoRelazStatoFin> ricercaSiacRSoggettoRelazStatoFinMassive(List<SiacRSoggettoRelazFin> listaInput) {
		List<SiacRSoggettoRelazStatoFin> listaRitorno = new ArrayList<SiacRSoggettoRelazStatoFin>();
		if(listaInput!=null && listaInput.size()>0){
			List<List<SiacRSoggettoRelazFin>> esploso = StringUtils.esplodiInListe(listaInput, DIMENSIONE_MASSIMA_QUERY_IN);
			if(esploso!=null && esploso.size()>0){
				for(List<SiacRSoggettoRelazFin> listaIt : esploso){
					List<SiacRSoggettoRelazStatoFin> risultatoParziale = ricercaSiacRSoggettoRelazStatoFinMassiveCORE(listaIt);
					if(risultatoParziale!=null && risultatoParziale.size()>0){
						listaRitorno.addAll(risultatoParziale);
					}
				}
				//per sicurezza che non ci siano doppioni:
				listaRitorno = CommonUtils.ritornaSoloDistintiByUid(listaRitorno);
			}
		}
        return listaRitorno;
	}
	
	private List<SiacRSoggettoRelazStatoFin> ricercaSiacRSoggettoRelazStatoFinMassiveCORE(List<SiacRSoggettoRelazFin> listaSiacTSiacRSoggettoRelazFin) {
		Map<String,Object> param = new HashMap<String, Object>();
		List<SiacRSoggettoRelazStatoFin> listaRitorno = new ArrayList<SiacRSoggettoRelazStatoFin>();
		
		if(listaSiacTSiacRSoggettoRelazFin!=null && listaSiacTSiacRSoggettoRelazFin.size()>0){
			
			StringBuilder jpql = new StringBuilder("SELECT DISTINCT rs FROM SiacRSoggettoRelazStatoFin rs WHERE ");
			
			jpql.append(" ( ");
			int i =0;
			for(SiacRSoggettoRelazFin it: listaSiacTSiacRSoggettoRelazFin){
				if(i>0){
					jpql.append(" OR ");
				}
				String idParamName = "id" + i;
				jpql.append(" ( ");
				
					jpql.append(" rs.siacRSoggettoRelaz.soggettoRelazId = :"+idParamName+" ");
				
				jpql.append(" ) ");
				param.put(idParamName, it.getSoggettoRelazId());
				i++;
			}
			jpql.append(" ) ");
			
			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rs"));
			param.put(DataValiditaUtils.NOW_DATE_PARAM_JPQL, getNowDate());
			
			//LANCIO DELLA QUERY:
			Query query =  createQuery(jpql.toString(), param);
			listaRitorno = query.getResultList();
		}
		//Termino restituendo l'oggetto di ritorno: 
        return listaRitorno;
	}
	
	
	public List<SiacRModpagOrdineFin> ricercaSiacRModpagOrdineFinBySiacTModpagFin(List<SiacTModpagFin> listaInput) {
		List<SiacRModpagOrdineFin> listaRitorno = new ArrayList<SiacRModpagOrdineFin>();
		if(listaInput!=null && listaInput.size()>0){
			List<List<SiacTModpagFin>> esploso = StringUtils.esplodiInListe(listaInput, DIMENSIONE_MASSIMA_QUERY_IN);
			if(esploso!=null && esploso.size()>0){
				for(List<SiacTModpagFin> listaIt : esploso){
					List<SiacRModpagOrdineFin> risultatoParziale = ricercaSiacRModpagOrdineFinBySiacTModpagFinCORE(listaIt);
					if(risultatoParziale!=null && risultatoParziale.size()>0){
						listaRitorno.addAll(risultatoParziale);
					}
				}
				//per sicurezza che non ci siano doppioni:
				listaRitorno = CommonUtils.ritornaSoloDistintiByUid(listaRitorno);
			}
		}
        return listaRitorno;
	}
	
	private List<SiacRModpagOrdineFin> ricercaSiacRModpagOrdineFinBySiacTModpagFinCORE(List<SiacTModpagFin> listaSiacTModpagFin){
		Map<String,Object> param = new HashMap<String, Object>();
		List<SiacRModpagOrdineFin> listaRitorno = new ArrayList<SiacRModpagOrdineFin>();
		
		if(listaSiacTModpagFin!=null && listaSiacTModpagFin.size()>0){
			
			StringBuilder jpql = new StringBuilder("SELECT DISTINCT rs FROM SiacRModpagOrdineFin rs WHERE ");
			
			jpql.append(" rs.siacTModpag.modpagId IN ( ");
			int i =0;
			for(SiacTModpagFin it: listaSiacTModpagFin){
				if(i>0){
					jpql.append(" , ");
				}
				String idParamName = "id" + i;
				jpql.append(" :"+idParamName+" ");
				param.put(idParamName, it.getModpagId());
				i++;
			}
			jpql.append(" ) ");
			
			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rs"));
			param.put(DataValiditaUtils.NOW_DATE_PARAM_JPQL, getNowDate());
			
			//LANCIO DELLA QUERY:
			Query query =  createQuery(jpql.toString(), param);
			listaRitorno = query.getResultList();
		}
		//Termino restituendo l'oggetto di ritorno: 
        return listaRitorno;
	}
	
	public List<SiacRModpagStatoFin> ricercaSiacRModpagStatoBySiacTModpagFin(List<SiacTModpagFin> listaInput) {
		List<SiacRModpagStatoFin> listaRitorno = new ArrayList<SiacRModpagStatoFin>();
		if(listaInput!=null && listaInput.size()>0){
			List<List<SiacTModpagFin>> esploso = StringUtils.esplodiInListe(listaInput, DIMENSIONE_MASSIMA_QUERY_IN);
			if(esploso!=null && esploso.size()>0){
				for(List<SiacTModpagFin> listaIt : esploso){
					List<SiacRModpagStatoFin> risultatoParziale = ricercaSiacRModpagStatoBySiacTModpagFinCORE(listaIt);
					if(risultatoParziale!=null && risultatoParziale.size()>0){
						listaRitorno.addAll(risultatoParziale);
					}
				}
				//per sicurezza che non ci siano doppioni:
				listaRitorno = CommonUtils.ritornaSoloDistintiByUid(listaRitorno);
			}
		}
        return listaRitorno;
	}
	
	private List<SiacRModpagStatoFin> ricercaSiacRModpagStatoBySiacTModpagFinCORE(List<SiacTModpagFin> listaSiacTModpagFin) {
		Map<String,Object> param = new HashMap<String, Object>();
		List<SiacRModpagStatoFin> listaRitorno = new ArrayList<SiacRModpagStatoFin>();
		
		if(listaSiacTModpagFin!=null && listaSiacTModpagFin.size()>0){
			
			StringBuilder jpql = new StringBuilder("SELECT DISTINCT rs FROM SiacRModpagStatoFin rs WHERE ");
			
			jpql.append(" rs.siacTModpag.modpagId IN ( ");
			int i =0;
			for(SiacTModpagFin it: listaSiacTModpagFin){
				if(i>0){
					jpql.append(" , ");
				}
				String idParamName = "id" + i;
				jpql.append(" :"+idParamName+" ");
				param.put(idParamName, it.getModpagId());
				i++;
			}
			jpql.append(" ) ");
			
			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rs"));
			param.put(DataValiditaUtils.NOW_DATE_PARAM_JPQL, getNowDate());
			
			//LANCIO DELLA QUERY:
			Query query =  createQuery(jpql.toString(), param);
			listaRitorno = query.getResultList();
		}
		//Termino restituendo l'oggetto di ritorno: 
        return listaRitorno;
	}
	
	public List<SiacTModpagModFin> ricercaSiacTModpagModFinMassive(List<SiacTModpagFin> listaInput) {
		List<SiacTModpagModFin> listaRitorno = new ArrayList<SiacTModpagModFin>();
		if(listaInput!=null && listaInput.size()>0){
			List<List<SiacTModpagFin>> esploso = StringUtils.esplodiInListe(listaInput, DIMENSIONE_MASSIMA_QUERY_IN);
			if(esploso!=null && esploso.size()>0){
				for(List<SiacTModpagFin> listaIt : esploso){
					List<SiacTModpagModFin> risultatoParziale = ricercaSiacTModpagModFinMassiveCORE(listaIt);
					if(risultatoParziale!=null && risultatoParziale.size()>0){
						listaRitorno.addAll(risultatoParziale);
					}
				}
				//per sicurezza che non ci siano doppioni:
				listaRitorno = CommonUtils.ritornaSoloDistintiByUid(listaRitorno);
			}
		}
        return listaRitorno;
	}
	
	/**
	 * E' un metodo di caricamento dati "massivo" utile per ottimizzare servizi di ricerca onerosi.
	 * Dato in input un elenco di SiacTModpagFin vengono restituiti TUTTI I DISTINTI SiacTModpagModFin in relazione
	 * con i listaSiacTModpagFin indicati.
	 */
	private List<SiacTModpagModFin> ricercaSiacTModpagModFinMassiveCORE(List<SiacTModpagFin> listaSiacTModpagFin) {
		Map<String,Object> param = new HashMap<String, Object>();
		List<SiacTModpagModFin> listaRitorno = new ArrayList<SiacTModpagModFin>();
		
		if(listaSiacTModpagFin!=null && listaSiacTModpagFin.size()>0){
			
			StringBuilder jpql = new StringBuilder("SELECT DISTINCT rs FROM SiacTModpagModFin rs WHERE ");
			
			jpql.append(" ( ");
			int i =0;
			for(SiacTModpagFin it: listaSiacTModpagFin){
				if(i>0){
					jpql.append(" OR ");
				}
				String idParamName = "id" + i;
				jpql.append(" ( ");
				
					jpql.append(" rs.siacTModpag.modpagId = :"+idParamName+" ");
				
				jpql.append(" ) ");
				param.put(idParamName, it.getModpagId());
				i++;
			}
			jpql.append(" ) ");
			
			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rs"));
			param.put(DataValiditaUtils.NOW_DATE_PARAM_JPQL, getNowDate());
			
			//LANCIO DELLA QUERY:
			Query query =  createQuery(jpql.toString(), param);
			listaRitorno = query.getResultList();
		}
		//Termino restituendo l'oggetto di ritorno: 
        return listaRitorno;
	}
	
	
	public <ST extends SiacTBase>  List<ST> ricercaBySoggettoMassive(List<SiacTSoggettoFin> listaInput, String nomeEntity) {
		List<ST> listaRitorno = new ArrayList<ST>();
		if(listaInput!=null && listaInput.size()>0){
			List<List<SiacTSoggettoFin>> esploso = StringUtils.esplodiInListe(listaInput, DIMENSIONE_MASSIMA_QUERY_IN);
			if(esploso!=null && esploso.size()>0){
				for(List<SiacTSoggettoFin> listaIt : esploso){
					List<ST> risultatoParziale = ricercaBySoggettoMassiveCORE(listaIt,nomeEntity);
					if(risultatoParziale!=null && risultatoParziale.size()>0){
						listaRitorno.addAll(risultatoParziale);
					}
				}
				//per sicurezza che non ci siano doppioni:
				listaRitorno = CommonUtils.ritornaSoloDistintiByUid(listaRitorno);
			}
		}
        return listaRitorno;
	}
	
	/**
	 * E' un metodo di caricamento dati "massivo" utile per ottimizzare servizi di ricerca onerosi.
	 * Dato in input un elenco di SiacTSoggettoFin e il nome della Entity da cerca vengono restituiti TUTTI I DISTINTI 
	 * oggetti del tipo indicato in nomeEntity in relazione con i SiacTSoggettoFin indicati.
	 * ESEMPIO: Se indico nomeEntity = "SiacRSoggettoStatoFin" verranno restituiti tutti i distinti record SiacRSoggettoStatoFin
	 * in relazione con i record di SiacTSoggettoFin indicati
	 */
	private <ST extends SiacTBase>  List<ST> ricercaBySoggettoMassiveCORE(List<SiacTSoggettoFin> listaSiacTSoggetto, String nomeEntity) {
		Map<String,Object> param = new HashMap<String, Object>();
		List<ST> listaRitorno = new ArrayList<ST>();
		
		if(listaSiacTSoggetto!=null && listaSiacTSoggetto.size()>0){
			
			StringBuilder jpql = new StringBuilder("SELECT DISTINCT rs FROM "+nomeEntity+" rs WHERE ");
			
			jpql.append(" rs.siacTSoggetto.soggettoId IN ( ");
			int i =0;
			for(SiacTSoggettoFin it: listaSiacTSoggetto){
				if(i>0){
					jpql.append(" , ");
				}
				String idParamName = "id" + i;
				jpql.append(" :"+idParamName+" ");
				param.put(idParamName, it.getSoggettoId());
				i++;
			}
			jpql.append(" ) ");
			
			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rs"));
			param.put(DataValiditaUtils.NOW_DATE_PARAM_JPQL, getNowDate());
			
			//LANCIO DELLA QUERY:
			Query query =  createQuery(jpql.toString(), param);
			listaRitorno = query.getResultList();
		}
		//Termino restituendo l'oggetto di ritorno: 
        return listaRitorno;
	}
	
	public List<SiacTComuneFin> ricercaSiacTComuneFinBySiacTIndirizzoSoggettoFin(List<SiacTIndirizzoSoggettoFin> listaInput) {
		List<SiacTComuneFin> listaRitorno = new ArrayList<SiacTComuneFin>();
		if(listaInput!=null && listaInput.size()>0){
			List<List<SiacTIndirizzoSoggettoFin>> esploso = StringUtils.esplodiInListe(listaInput, DIMENSIONE_MASSIMA_QUERY_IN);
			if(esploso!=null && esploso.size()>0){
				for(List<SiacTIndirizzoSoggettoFin> listaIt : esploso){
					List<SiacTComuneFin> risultatoParziale = ricercaSiacTComuneFinBySiacTIndirizzoSoggettoFinCORE(listaIt);
					if(risultatoParziale!=null && risultatoParziale.size()>0){
						listaRitorno.addAll(risultatoParziale);
					}
				}
				//per sicurezza che non ci siano doppioni:
				listaRitorno = CommonUtils.ritornaSoloDistintiByUid(listaRitorno);
			}
		}
        return listaRitorno;
	}
	
	private List<SiacTComuneFin> ricercaSiacTComuneFinBySiacTIndirizzoSoggettoFinCORE(List<SiacTIndirizzoSoggettoFin> lista){
		Map<String,Object> param = new HashMap<String, Object>();
		List<SiacTComuneFin> listaRitorno = new ArrayList<SiacTComuneFin>();
		
		if(lista!=null && lista.size()>0){
			
			StringBuilder jpql = new StringBuilder("SELECT DISTINCT rs FROM SiacTComuneFin rs WHERE ");
			
			jpql.append(" rs.comuneId IN ( ");
			int i =0;
			for(SiacTIndirizzoSoggettoFin it: lista){
				if(it!=null && it.getSiacTComune()!=null && it.getSiacTComune().getComuneId()!=null){
					if(i>0){
						jpql.append(" , ");
					}
					String idParamName = "id" + i;
					jpql.append(" :"+idParamName+" ");
					param.put(idParamName, it.getSiacTComune().getComuneId());
					i++;
				}
			}
			jpql.append(" ) ");
			
			if(i>0){
				//ha senso lanciare la query
				
				//LANCIO DELLA QUERY:
				jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rs"));
				param.put(DataValiditaUtils.NOW_DATE_PARAM_JPQL, getNowDate());
				
				//LANCIO DELLA QUERY:
				Query query =  createQuery(jpql.toString(), param);
				listaRitorno = query.getResultList();
			}
			
		}
		//Termino restituendo l'oggetto di ritorno: 
        return listaRitorno;
	}
	
	public List<SiacTComuneFin> ricercaSiacTComuneFinBySiacTPersonaFisicaFin(List<SiacTPersonaFisicaFin> listaInput) {
		List<SiacTComuneFin> listaRitorno = new ArrayList<SiacTComuneFin>();
		if(listaInput!=null && listaInput.size()>0){
			List<List<SiacTPersonaFisicaFin>> esploso = StringUtils.esplodiInListe(listaInput, DIMENSIONE_MASSIMA_QUERY_IN);
			if(esploso!=null && esploso.size()>0){
				for(List<SiacTPersonaFisicaFin> listaIt : esploso){
					List<SiacTComuneFin> risultatoParziale = ricercaSiacTComuneFinBySiacTPersonaFisicaFinCORE(listaIt);
					if(risultatoParziale!=null && risultatoParziale.size()>0){
						listaRitorno.addAll(risultatoParziale);
					}
				}
				//per sicurezza che non ci siano doppioni:
				listaRitorno = CommonUtils.ritornaSoloDistintiByUid(listaRitorno);
			}
		}
        return listaRitorno;
	}
	
	private List<SiacTComuneFin> ricercaSiacTComuneFinBySiacTPersonaFisicaFinCORE(List<SiacTPersonaFisicaFin> lista){
		Map<String,Object> param = new HashMap<String, Object>();
		List<SiacTComuneFin> listaRitorno = new ArrayList<SiacTComuneFin>();
		
		if(lista!=null && lista.size()>0){
			
			StringBuilder jpql = new StringBuilder("SELECT DISTINCT rs FROM SiacTComuneFin rs WHERE ");
			
			jpql.append(" rs.comuneId IN ( ");
			int i =0;
			for(SiacTPersonaFisicaFin it: lista){
				if(it!=null && it.getSiacTComune()!=null && it.getSiacTComune().getComuneId()!=null){
					if(i>0){
						jpql.append(" , ");
					}
					String idParamName = "id" + i;
					jpql.append(" :"+idParamName+" ");
					param.put(idParamName, it.getSiacTComune().getComuneId());
					i++;
				}
			}
			jpql.append(" ) ");
			
			if(i>0){
				//ha senso lanciare la query
				
				//LANCIO DELLA QUERY:
				jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rs"));
				param.put(DataValiditaUtils.NOW_DATE_PARAM_JPQL, getNowDate());
				
				//LANCIO DELLA QUERY:
				Query query =  createQuery(jpql.toString(), param);
				listaRitorno = query.getResultList();
			}
			
		}
		//Termino restituendo l'oggetto di ritorno: 
        return listaRitorno;
	}
	
	public List<SiacRComuneProvinciaFin> ricercaSiacRComuneProvinciaFinBySiacTComuneFin(List<SiacTComuneFin> listaInput) {
		List<SiacRComuneProvinciaFin> listaRitorno = new ArrayList<SiacRComuneProvinciaFin>();
		if(listaInput!=null && listaInput.size()>0){
			List<List<SiacTComuneFin>> esploso = StringUtils.esplodiInListe(listaInput, DIMENSIONE_MASSIMA_QUERY_IN);
			if(esploso!=null && esploso.size()>0){
				for(List<SiacTComuneFin> listaIt : esploso){
					List<SiacRComuneProvinciaFin> risultatoParziale = ricercaSiacRComuneProvinciaFinBySiacTComuneFinCORE(listaIt);
					if(risultatoParziale!=null && risultatoParziale.size()>0){
						listaRitorno.addAll(risultatoParziale);
					}
				}
				//per sicurezza che non ci siano doppioni:
				listaRitorno = CommonUtils.ritornaSoloDistintiByUid(listaRitorno);
			}
		}
        return listaRitorno;
	}
	
	private List<SiacRComuneProvinciaFin> ricercaSiacRComuneProvinciaFinBySiacTComuneFinCORE(List<SiacTComuneFin> lista){
		Map<String,Object> param = new HashMap<String, Object>();
		List<SiacRComuneProvinciaFin> listaRitorno = new ArrayList<SiacRComuneProvinciaFin>();
		
		if(lista!=null && lista.size()>0){
			
			StringBuilder jpql = new StringBuilder("SELECT DISTINCT rs FROM SiacRComuneProvinciaFin rs WHERE ");
			
			jpql.append(" rs.siacTComune.comuneId IN ( ");
			int i =0;
			for(SiacTComuneFin it: lista){
				if(i>0){
					jpql.append(" , ");
				}
				String idParamName = "id" + i;
				jpql.append(" :"+idParamName+" ");
				param.put(idParamName, it.getComuneId());
				i++;
			}
			jpql.append(" ) ");
			
			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rs"));
			param.put(DataValiditaUtils.NOW_DATE_PARAM_JPQL, getNowDate());
			
			//LANCIO DELLA QUERY:
			Query query =  createQuery(jpql.toString(), param);
			listaRitorno = query.getResultList();
		}
		//Termino restituendo l'oggetto di ritorno: 
        return listaRitorno;
	}
	
	@Override
	public List<SiacTProvinciaFin> ricercaSiacTProvinciaFinBySiacRComuneProvinciaFin(List<SiacRComuneProvinciaFin> listaInput) {
		List<SiacTProvinciaFin> listaRitorno = new ArrayList<SiacTProvinciaFin>();
		if(listaInput!=null && listaInput.size()>0){
			List<List<SiacRComuneProvinciaFin>> esploso = StringUtils.esplodiInListe(listaInput, DIMENSIONE_MASSIMA_QUERY_IN);
			if(esploso!=null && esploso.size()>0){
				for(List<SiacRComuneProvinciaFin> listaIt : esploso){
					List<SiacTProvinciaFin> risultatoParziale = ricercaSiacTProvinciaFinBySiacRComuneProvinciaFinCORE(listaIt);
					if(risultatoParziale!=null && risultatoParziale.size()>0){
						listaRitorno.addAll(risultatoParziale);
					}
				}
				//per sicurezza che non ci siano doppioni:
				listaRitorno = CommonUtils.ritornaSoloDistintiByUid(listaRitorno);
			}
		}
        return listaRitorno;
	}
	
	private List<SiacTProvinciaFin> ricercaSiacTProvinciaFinBySiacRComuneProvinciaFinCORE(List<SiacRComuneProvinciaFin> lista) {
		Map<String,Object> param = new HashMap<String, Object>();
		List<SiacTProvinciaFin> listaRitorno = new ArrayList<SiacTProvinciaFin>();
		
		if(lista!=null && lista.size()>0){
			
			StringBuilder jpql = new StringBuilder("SELECT DISTINCT rs FROM SiacTProvinciaFin rs WHERE ");
			
			jpql.append(" rs.provinciaId IN ( ");
			int i =0;
			for(SiacRComuneProvinciaFin it: lista){
				if(i>0){
					jpql.append(" , ");
				}
				String idParamName = "id" + i;
				jpql.append(" :"+idParamName+" ");
				param.put(idParamName, it.getSiacTProvincia().getProvinciaId());
				i++;
			}
			jpql.append(" ) ");
			
			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rs"));
			param.put(DataValiditaUtils.NOW_DATE_PARAM_JPQL, getNowDate());
			
			//LANCIO DELLA QUERY:
			Query query =  createQuery(jpql.toString(), param);
			listaRitorno = query.getResultList();
		}
		//Termino restituendo l'oggetto di ritorno: 
        return listaRitorno;
	}
	
	@Override
	public List<SiacRProvinciaRegioneFin> ricercaSiacRProvinciaRegioneFinBySiacTProvinciaFin(List<SiacTProvinciaFin> listaInput) {
		List<SiacRProvinciaRegioneFin> listaRitorno = new ArrayList<SiacRProvinciaRegioneFin>();
		if(listaInput!=null && listaInput.size()>0){
			List<List<SiacTProvinciaFin>> esploso = StringUtils.esplodiInListe(listaInput, DIMENSIONE_MASSIMA_QUERY_IN);
			if(esploso!=null && esploso.size()>0){
				for(List<SiacTProvinciaFin> listaIt : esploso){
					List<SiacRProvinciaRegioneFin> risultatoParziale = ricercaSiacRProvinciaRegioneFinBySiacTProvinciaFinCORE(listaIt);
					if(risultatoParziale!=null && risultatoParziale.size()>0){
						listaRitorno.addAll(risultatoParziale);
					}
				}
				//per sicurezza che non ci siano doppioni:
				listaRitorno = CommonUtils.ritornaSoloDistintiByUid(listaRitorno);
			}
		}
        return listaRitorno;
	}

	private List<SiacRProvinciaRegioneFin> ricercaSiacRProvinciaRegioneFinBySiacTProvinciaFinCORE(List<SiacTProvinciaFin> lista) {
		Map<String,Object> param = new HashMap<String, Object>();
		List<SiacRProvinciaRegioneFin> listaRitorno = new ArrayList<SiacRProvinciaRegioneFin>();
		
		if(lista!=null && lista.size()>0){
			
			StringBuilder jpql = new StringBuilder("SELECT DISTINCT rs FROM SiacRProvinciaRegioneFin rs WHERE ");
			
			jpql.append(" rs.siacTProvincia.provinciaId IN ( ");
			int i =0;
			for(SiacTProvinciaFin it: lista){
				if(i>0){
					jpql.append(" , ");
				}
				String idParamName = "id" + i;
				jpql.append(" :"+idParamName+" ");
				param.put(idParamName, it.getProvinciaId());
				i++;
			}
			jpql.append(" ) ");
			
			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rs"));
			param.put(DataValiditaUtils.NOW_DATE_PARAM_JPQL, getNowDate());
			
			//LANCIO DELLA QUERY:
			Query query =  createQuery(jpql.toString(), param);
			listaRitorno = query.getResultList();
		}
		//Termino restituendo l'oggetto di ritorno: 
        return listaRitorno;
	}
	
	@Override
	public List<SiacTRegioneFin> ricercaSiacTRegioneFinFinBySiacRProvinciaRegioneFin(List<SiacRProvinciaRegioneFin> listaInput) {
		List<SiacTRegioneFin> listaRitorno = new ArrayList<SiacTRegioneFin>();
		if(listaInput!=null && listaInput.size()>0){
			List<List<SiacRProvinciaRegioneFin>> esploso = StringUtils.esplodiInListe(listaInput, DIMENSIONE_MASSIMA_QUERY_IN);
			if(esploso!=null && esploso.size()>0){
				for(List<SiacRProvinciaRegioneFin> listaIt : esploso){
					List<SiacTRegioneFin> risultatoParziale = ricercaSiacTRegioneFinFinBySiacRProvinciaRegioneFinCORE(listaIt);
					if(risultatoParziale!=null && risultatoParziale.size()>0){
						listaRitorno.addAll(risultatoParziale);
					}
				}
				//per sicurezza che non ci siano doppioni:
				listaRitorno = CommonUtils.ritornaSoloDistintiByUid(listaRitorno);
			}
		}
        return listaRitorno;
	}
	
	private List<SiacTRegioneFin> ricercaSiacTRegioneFinFinBySiacRProvinciaRegioneFinCORE(List<SiacRProvinciaRegioneFin> lista) {
		Map<String,Object> param = new HashMap<String, Object>();
		List<SiacTRegioneFin> listaRitorno = new ArrayList<SiacTRegioneFin>();
		
		if(lista!=null && lista.size()>0){
			
			StringBuilder jpql = new StringBuilder("SELECT DISTINCT rs FROM SiacTRegioneFin rs WHERE ");
			
			jpql.append(" rs.regioneId IN ( ");
			int i =0;
			for(SiacRProvinciaRegioneFin it: lista){
				if(i>0){
					jpql.append(" , ");
				}
				String idParamName = "id" + i;
				jpql.append(" :"+idParamName+" ");
				param.put(idParamName, it.getSiacTRegione().getRegioneId());
				i++;
			}
			jpql.append(" ) ");
			
			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rs"));
			param.put(DataValiditaUtils.NOW_DATE_PARAM_JPQL, getNowDate());
			
			//LANCIO DELLA QUERY:
			Query query =  createQuery(jpql.toString(), param);
			listaRitorno = query.getResultList();
		}
		//Termino restituendo l'oggetto di ritorno: 
        return listaRitorno;
	}
	
	@Override
	public List<SiacTNazioneFin> ricercaSiacTNazioneFinBySiacTComuneFin(List<SiacTComuneFin> listaInput) {
		List<SiacTNazioneFin> listaRitorno = new ArrayList<SiacTNazioneFin>();
		if(listaInput!=null && listaInput.size()>0){
			List<List<SiacTComuneFin>> esploso = StringUtils.esplodiInListe(listaInput, DIMENSIONE_MASSIMA_QUERY_IN);
			if(esploso!=null && esploso.size()>0){
				for(List<SiacTComuneFin> listaIt : esploso){
					List<SiacTNazioneFin> risultatoParziale = ricercaSiacTNazioneFinBySiacTComuneFinCORE(listaIt);
					if(risultatoParziale!=null && risultatoParziale.size()>0){
						listaRitorno.addAll(risultatoParziale);
					}
				}
				//per sicurezza che non ci siano doppioni:
				listaRitorno = CommonUtils.ritornaSoloDistintiByUid(listaRitorno);
			}
		}
        return listaRitorno;
	}

	private List<SiacTNazioneFin> ricercaSiacTNazioneFinBySiacTComuneFinCORE(List<SiacTComuneFin> lista) {
		Map<String,Object> param = new HashMap<String, Object>();
		List<SiacTNazioneFin> listaRitorno = new ArrayList<SiacTNazioneFin>();
		
		if(lista!=null && lista.size()>0){
			
			StringBuilder jpql = new StringBuilder("SELECT DISTINCT rs FROM SiacTNazioneFin rs WHERE ");
			
			jpql.append(" rs.nazioneId IN ( ");
			int i =0;
			for(SiacTComuneFin it: lista){
				if(i>0){
					jpql.append(" , ");
				}
				String idParamName = "id" + i;
				jpql.append(" :"+idParamName+" ");
				param.put(idParamName, it.getSiacTNazione().getNazioneId());
				i++;
			}
			jpql.append(" ) ");
			
			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rs"));
			param.put(DataValiditaUtils.NOW_DATE_PARAM_JPQL, getNowDate());
			
			//LANCIO DELLA QUERY:
			Query query =  createQuery(jpql.toString(), param);
			listaRitorno = query.getResultList();
		}
		//Termino restituendo l'oggetto di ritorno: 
        return listaRitorno;
	}

	@Override
	public List<SiacRComuneRegioneFin> ricercaSiacRComuneRegioneFinBySiacTComuneFin(List<SiacTComuneFin> listaInput) {
		List<SiacRComuneRegioneFin> listaRitorno = new ArrayList<SiacRComuneRegioneFin>();
		if(listaInput!=null && listaInput.size()>0){
			List<List<SiacTComuneFin>> esploso = StringUtils.esplodiInListe(listaInput, DIMENSIONE_MASSIMA_QUERY_IN);
			if(esploso!=null && esploso.size()>0){
				for(List<SiacTComuneFin> listaIt : esploso){
					List<SiacRComuneRegioneFin> risultatoParziale = ricercaSiacRComuneRegioneFinBySiacTComuneFinCORE(listaIt);
					if(risultatoParziale!=null && risultatoParziale.size()>0){
						listaRitorno.addAll(risultatoParziale);
					}
				}
				//per sicurezza che non ci siano doppioni:
				listaRitorno = CommonUtils.ritornaSoloDistintiByUid(listaRitorno);
			}
		}
        return listaRitorno;
	}
	
	private List<SiacRComuneRegioneFin> ricercaSiacRComuneRegioneFinBySiacTComuneFinCORE(List<SiacTComuneFin> lista) {
		Map<String,Object> param = new HashMap<String, Object>();
		List<SiacRComuneRegioneFin> listaRitorno = new ArrayList<SiacRComuneRegioneFin>();
		
		if(lista!=null && lista.size()>0){
			
			StringBuilder jpql = new StringBuilder("SELECT DISTINCT rs FROM SiacRComuneRegioneFin rs WHERE ");
			
			jpql.append(" rs.siacTComune.comuneId IN ( ");
			int i =0;
			for(SiacTComuneFin it: lista){
				if(i>0){
					jpql.append(" , ");
				}
				String idParamName = "id" + i;
				jpql.append(" :"+idParamName+" ");
				param.put(idParamName, it.getComuneId());
				i++;
			}
			jpql.append(" ) ");
			
			jpql.append(" AND ").append(DataValiditaUtils.validitaForQuery("rs"));
			param.put(DataValiditaUtils.NOW_DATE_PARAM_JPQL, getNowDate());
			
			//LANCIO DELLA QUERY:
			Query query =  createQuery(jpql.toString(), param);
			listaRitorno = query.getResultList();
		}
		//Termino restituendo l'oggetto di ritorno: 
        return listaRitorno;
	}
	
}