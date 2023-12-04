/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.Progetto;
import it.csi.siac.siacbilser.model.TipoProgetto;
import it.csi.siac.siaccommonser.util.log.LogSrvUtil;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siacfinser.CommonUtil;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.StringUtilsFin;
import it.csi.siac.siacfinser.TimingUtils;
import it.csi.siac.siacfinser.integration.dad.datacontainer.DisponibilitaMovimentoGestioneContainer;
import it.csi.siac.siacfinser.integration.dao.common.dto.OttimizzazioneModificheMovimentoGestioneDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.OttimizzazioneMovGestDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.OttimizzazioneSoggettoDto;
import it.csi.siac.siacfinser.integration.entity.SiacDAttoAmmStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDModificaTipoFin;
import it.csi.siac.siacfinser.integration.entity.SiacDSiopeAssenzaMotivazioneFin;
import it.csi.siac.siacfinser.integration.entity.SiacDSiopeTipoDebitoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRAttoAmmClassFin;
import it.csi.siac.siacfinser.integration.entity.SiacRAttoAmmStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRClassBaseFin;
import it.csi.siac.siacfinser.integration.entity.SiacRComuneProvinciaFin;
import it.csi.siac.siacfinser.integration.entity.SiacRComuneRegioneFin;
import it.csi.siac.siacfinser.integration.entity.SiacRFormaGiuridicaFin;
import it.csi.siac.siacfinser.integration.entity.SiacRFormaGiuridicaModFin;
import it.csi.siac.siacfinser.integration.entity.SiacRIndirizzoSoggettoTipoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRIndirizzoSoggettoTipoModFin;
import it.csi.siac.siacfinser.integration.entity.SiacRModificaStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRModpagOrdineFin;
import it.csi.siac.siacfinser.integration.entity.SiacRModpagStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestBilElemFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestClassFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsAttoAmmFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsAttrFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsProgrammaFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsSogFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsSogModFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsSogclasseFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsSogclasseModFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoAttrFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoAttrModFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoClasseFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoClasseModFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoOnereFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoOnereModFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoRelazFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoRelazStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoTipoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoTipoModFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggrelModpagFin;
import it.csi.siac.siacfinser.integration.entity.SiacTAttoAmmFin;
import it.csi.siac.siacfinser.integration.entity.SiacTAttrFin;
import it.csi.siac.siacfinser.integration.entity.SiacTAvanzovincoloFin;
import it.csi.siac.siacfinser.integration.entity.SiacTBilElemFin;
import it.csi.siac.siacfinser.integration.entity.SiacTClassFin;
import it.csi.siac.siacfinser.integration.entity.SiacTComuneFin;
import it.csi.siac.siacfinser.integration.entity.SiacTIndirizzoSoggettoFin;
import it.csi.siac.siacfinser.integration.entity.SiacTIndirizzoSoggettoModFin;
import it.csi.siac.siacfinser.integration.entity.SiacTModificaFin;
import it.csi.siac.siacfinser.integration.entity.SiacTModpagFin;
import it.csi.siac.siacfinser.integration.entity.SiacTModpagModFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsDetFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsDetModFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsFin;
import it.csi.siac.siacfinser.integration.entity.SiacTPersonaFisicaFin;
import it.csi.siac.siacfinser.integration.entity.SiacTPersonaFisicaModFin;
import it.csi.siac.siacfinser.integration.entity.SiacTPersonaGiuridicaFin;
import it.csi.siac.siacfinser.integration.entity.SiacTPersonaGiuridicaModFin;
import it.csi.siac.siacfinser.integration.entity.SiacTRecapitoSoggettoFin;
import it.csi.siac.siacfinser.integration.entity.SiacTRecapitoSoggettoModFin;
import it.csi.siac.siacfinser.integration.entity.SiacTSoggettoFin;
import it.csi.siac.siacfinser.integration.entity.SiacTSoggettoModFin;
import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;
import it.csi.siac.siacfinser.integration.util.dto.RSoggettoClasseConverterDto;
import it.csi.siac.siacfinser.integration.util.dto.RSoggettoOnereConverterDto;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.AccertamentoAbstract;
import it.csi.siac.siacfinser.model.AttoAmministrativoStoricizzato;
import it.csi.siac.siacfinser.model.Avanzovincolo;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.ImpegnoAbstract;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.MovimentoGestione.AttributoMovimentoGestione;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.TipoAvanzovincolo;
import it.csi.siac.siacfinser.model.TipoFonteDurc;
import it.csi.siac.siacfinser.model.codifiche.ClasseSoggetto;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.codifiche.NaturaGiuridicaSoggetto;
import it.csi.siac.siacfinser.model.codifiche.TipoSoggetto;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestione.StatoOperativoModificaMovimentoGestione;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneEntrata;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesa;
import it.csi.siac.siacfinser.model.siopeplus.SiopeAssenzaMotivazione;
import it.csi.siac.siacfinser.model.siopeplus.SiopeTipoDebito;
import it.csi.siac.siacfinser.model.soggetto.ClassificazioneSoggetto;
import it.csi.siac.siacfinser.model.soggetto.ComuneNascita;
import it.csi.siac.siacfinser.model.soggetto.Contatto;
import it.csi.siac.siacfinser.model.soggetto.IndirizzoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.Onere;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto.Sesso;
import it.csi.siac.siacfinser.model.soggetto.Soggetto.StatoOperativoAnagrafica;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggettoCessioni;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto.StatoOperativoSedeSecondaria;


public class EntityToModelConverter {
	
	protected static transient LogSrvUtil log = new LogSrvUtil(EntityToModelConverter.class);
	
	public static Soggetto soggettoEntityToSoggettoModel(SiacTSoggettoFin dto, Soggetto soggetto) {
		List<SiacTSoggettoFin> dtos = new ArrayList<SiacTSoggettoFin>();
		dtos.add(dto);
		List<Soggetto> soggettos = new ArrayList<Soggetto>();
		soggettos.add(soggetto);
		soggettos = soggettoEntityToSoggettoModel(dtos, soggettos, false);
		return soggettos.get(0);
	}
		
	public static Soggetto soggettoEntityModToSoggettoModel(SiacTSoggettoModFin dto, Soggetto soggetto) {
		List<SiacTSoggettoModFin> dtos = new ArrayList<SiacTSoggettoModFin>();
		dtos.add(dto);
		List<Soggetto> soggettos = new ArrayList<Soggetto>();
		soggettos.add(soggetto);
		soggettos = soggettoEntityModToSoggettoModel(dtos, soggettos);
		return soggettos.get(0);
	}
	
	public static List<Soggetto> soggettoEntityModToSoggettoModel(List<SiacTSoggettoModFin> dtos, List<Soggetto> soggettos) {
		final String methodName = "soggettoEntityModToSoggettoModel";
		for(Soggetto soggetto : soggettos){
			int idSoggetto = soggetto.getUid();
			for(SiacTSoggettoModFin siacTSoggettoMod : dtos){
				int idSiacTSoggettoMod = siacTSoggettoMod.getSogModId();
				if(idSoggetto == idSiacTSoggettoMod){
					
					
					// DURC
					setDatiDurc(soggetto, siacTSoggettoMod);
					
					
					// soggetto CF ESTERO
					if(!StringUtilsFin.isEmpty(siacTSoggettoMod.getCodiceFiscaleEstero())){
						soggetto.setResidenteEstero(true);
					}
					
					//2. CONTATTI
					List<Contatto> contatti = siacTRecapitoSoggettoModToContattoList(siacTSoggettoMod.getSiacTRecapitoSoggettoMods(), true);
					soggetto.setContatti(contatti);
					//END CONTATTI
					
					//3. ONERI
					RSoggettoOnereConverterDto soggettoOnereConverterDto = siacRSoggettoOnereModToOnereList(siacTSoggettoMod.getSiacRSoggettoOnereMods(),true);
					List<Onere> listaOneri = soggettoOnereConverterDto.getLista();
					soggetto.setTipoOnereId(soggettoOnereConverterDto.getElencoCodes());
					soggetto.setElencoOneri(listaOneri);
					//END ONERI
					
					//4. CLASSIFICAZIONI:
					RSoggettoClasseConverterDto soggettoClasseConverterDto = siacRSoggettoClasseModToClassificazioneSoggettoList(siacTSoggettoMod.getSiacRSoggettoClasseMods(),true);
					List<ClassificazioneSoggetto> listaClassSoggetto = soggettoClasseConverterDto.getLista();
					soggetto.setTipoClassificazioneSoggettoId(soggettoClasseConverterDto.getElencoCodes());
					soggetto.setElencoClass(listaClassSoggetto);
					//END ALTRI POSSIBILI CAMPI	
					
					//5. TipoSoggetto
					List<TipoSoggetto> listaSoggettoTipo = siacRSoggettoTipoModToSoggettoTipoList(siacTSoggettoMod.getSiacRSoggettoTipoMods(),true);
					if(listaSoggettoTipo!=null && listaSoggettoTipo.size()>0){
						soggetto.setTipoSoggetto(listaSoggettoTipo.get(0));
					}
					//END 
					
					//6. Natura giuridica
					List<NaturaGiuridicaSoggetto> listaNaturaGiuridica = siacRFormaGiuridicaModToNaturaGiuridicaSoggettoList(siacTSoggettoMod.getSiacRFormaGiuridicaMods(),true);
					if(listaNaturaGiuridica!=null && listaNaturaGiuridica.size()>0){
						soggetto.setNaturaGiuridicaSoggetto(listaNaturaGiuridica.get(0));
					}
					//END 
					
					// 8. PERSONA FISICA
					List<SiacTPersonaFisicaModFin> siacTPersonaFisicaMods = siacTSoggettoMod.getSiacTPersonaFisicaMods();
					if(null!=siacTPersonaFisicaMods && siacTPersonaFisicaMods.size() > 0){
						for(SiacTPersonaFisicaModFin siacTPersonaFisicaMod : siacTPersonaFisicaMods){
							if(null!=siacTPersonaFisicaMod && siacTPersonaFisicaMod.getDataFineValidita() == null){
								soggetto.setDataNascita(siacTPersonaFisicaMod.getNascitaData());
								soggetto.setCognome(siacTPersonaFisicaMod.getCognome());
								soggetto.setNome(siacTPersonaFisicaMod.getNome());
								
								if(CostantiFin.SESSO_M.equalsIgnoreCase(siacTPersonaFisicaMod.getSesso())){
									soggetto.setSesso(Sesso.MASCHIO);
									soggetto.setSessoStringa(Sesso.MASCHIO.toString());
								}else{
									soggetto.setSesso(Sesso.FEMMINA);
									soggetto.setSessoStringa(Sesso.FEMMINA.toString());
								}

								if(null!=siacTPersonaFisicaMod.getSiacTComune()){
									SiacTComuneFin siactcomune = siacTPersonaFisicaMod.getSiacTComune();
									ComuneNascita comuneNascita = siacTComuneToComuneNascita(siactcomune);
									soggetto.setComuneNascita(comuneNascita);
								}
							}
						}
					}
					// END PERSONA FISICA	
					
					// 13. PERSONA GIURIDICA
					List<SiacTPersonaGiuridicaModFin> siacTPersonaGiuridicaMods = siacTSoggettoMod.getSiacTPersonaGiuridicaMods();
					if(null!=siacTPersonaGiuridicaMods && siacTPersonaGiuridicaMods.size() > 0){
						for(SiacTPersonaGiuridicaModFin siacTPersonaGiuridicaMod : siacTPersonaGiuridicaMods){
							if(null!=siacTPersonaGiuridicaMod && siacTPersonaGiuridicaMod.getDataFineValidita() == null){
								//log.debug(methodName,  "siacTPersonaGiuridica : valida");
							}
						}
					}
					// END PERSONAGIURIDICA	

					List<SiacTIndirizzoSoggettoModFin> listaIndirizzi = siacTSoggettoMod.getSiacTIndirizzoSoggettoMods();
					ArrayList<SiacTIndirizzoSoggettoModFin> listaValidi = new ArrayList<SiacTIndirizzoSoggettoModFin>();
					for(SiacTIndirizzoSoggettoModFin itInd: listaIndirizzi){
						if(itInd!=null && itInd.getDataFineValidita()==null){
							listaValidi.add(itInd);
						}
					}
					siacTSoggettoMod.setSiacTIndirizzoSoggettoMods(listaValidi);
					
					//8. ALTRI POSSIBILI CAMPI
					//END ALTRI POSSIBILI CAMPI	
					
					break;
				}
			}
		}
		return soggettos;
	}
	
	private static void setDatiDurc(Soggetto soggetto, SiacTSoggettoModFin siacTSoggettoMod) {
		
		soggetto.setDataFineValiditaDurc(siacTSoggettoMod.getDataFineValiditaDurc());
		soggetto.setTipoFonteDurc(siacTSoggettoMod.getTipoFonteDurc());
		
		if (TipoFonteDurc.AUTOMATICA.getCodice().equals(siacTSoggettoMod.getTipoFonteDurc())) {
			soggetto.setDescrizioneFonteDurc(siacTSoggettoMod.getFonteDurcAutomatica());
		} 
		
		SiacTClassFin SiacTClass = siacTSoggettoMod.getFonteDurc();
		
		if (TipoFonteDurc.MANUALE.getCodice().equals(siacTSoggettoMod.getTipoFonteDurc()) && SiacTClass != null) {
				soggetto.setFonteDurcClassifId(SiacTClass.getClassifId());
				soggetto.setDescrizioneFonteDurc(String.format("%s - %s", SiacTClass.getClassifCode(), SiacTClass.getClassifDesc()));
		} 
		
		soggetto.setNoteDurc(siacTSoggettoMod.getNoteDurc());
		
		soggetto.setLoginInserimentoDurc(siacTSoggettoMod.getLoginInserimentoDurc());
		soggetto.setLoginModificaDurc(siacTSoggettoMod.getLoginModificaDurc());
	}

	public static List<Soggetto> soggettoEntityToSoggettoModel(List<SiacTSoggettoFin> dtos, List<Soggetto> soggettos, boolean fromRicerca) {
		String methodName = "soggettoEntityToSoggettoModel";		
		for(Soggetto soggettoIt : soggettos){
			int idIterato = soggettoIt.getUid();
			for(SiacTSoggettoFin siacTSoggettoIt : dtos){
				int idConfronto = siacTSoggettoIt.getSoggettoId();
				if(idIterato==idConfronto){
					
					// CODICE SOGGETTO VERSIONE NUMBER
					soggettoIt.setCodiceSoggettoNumber(new BigInteger(siacTSoggettoIt.getSoggettoCode()));
					
					
					// DURC
					setDatiDurc(soggettoIt, siacTSoggettoIt);
					
					
					//1. SOGGETTO STATO
					List<SiacRSoggettoStatoFin> listaRSogStato =  siacTSoggettoIt.getSiacRSoggettoStatos();
					if(listaRSogStato!=null && listaRSogStato.size()>0) {
						SiacRSoggettoStatoFin trovatoValido = null;
						for(SiacRSoggettoStatoFin rSoggStato : listaRSogStato){
							if(rSoggStato!=null && rSoggStato.getDataFineValidita()==null){
								trovatoValido = rSoggStato;
								break;
							}
							if(trovatoValido!=null){
								String code = trovatoValido.getSiacDSoggettoStato().getSoggettoStatoCode();
								StatoOperativoAnagrafica statoOpAnag = CostantiFin.statoOperativoAnagraficaStringToEnum(code);
								soggettoIt.setStatoOperativo(statoOpAnag);
								soggettoIt.setIdStatoOperativoAnagrafica(trovatoValido.getSiacDSoggettoStato().getSoggettoStatoId());
								soggettoIt.setNotaStatoOperativo(trovatoValido.getNotaOperazione());
								soggettoIt.setDataStato(trovatoValido.getDataInizioValidita());
							}
						}
						if(trovatoValido!=null){
							String code = trovatoValido.getSiacDSoggettoStato().getSoggettoStatoCode();
							StatoOperativoAnagrafica statoOpAnag = CostantiFin.statoOperativoAnagraficaStringToEnum(code);
							soggettoIt.setStatoOperativo(statoOpAnag);
							soggettoIt.setIdStatoOperativoAnagrafica(trovatoValido.getSiacDSoggettoStato().getSoggettoStatoId());
							soggettoIt.setNotaStatoOperativo(trovatoValido.getNotaOperazione());
							soggettoIt.setDataStato(trovatoValido.getDataInizioValidita());
						}
						
						
						if (StatoOperativoAnagrafica.VALIDO.equals(soggettoIt.getStatoOperativo())) {
							soggettoIt.setNotaStatoSospesoPrecedente(getUltimaNotaSospesoNotNull(listaRSogStato));
							soggettoIt.setNotaStatoBloccoPrecedente(getUltimaNotaBloccatoNotNull(listaRSogStato));
						}
					}
					
					// se arrivo da ricerca soggetti devo settare lo stato 
					// in modifica "al volo" Jira-138
					if(fromRicerca){
						if(null!=siacTSoggettoIt.getSiacTSoggettoMods() &&
							siacTSoggettoIt.getSiacTSoggettoMods().size()>0){
							for(SiacTSoggettoModFin siacTSoggettoMod : siacTSoggettoIt.getSiacTSoggettoMods()){								
								if(siacTSoggettoMod.getDataFineValidita()==null){									
									soggettoIt.setStatoOperativo(StatoOperativoAnagrafica.IN_MODIFICA);
									soggettoIt.setLoginControlloPerModifica(siacTSoggettoMod.getLoginOperazione());
//									it.setIdStatoOperativoAnagrafica(trovatoValido.getSiacDSoggettoStato().getSoggettoStatoId());
//									it.setDataStato(trovatoValido.getSiacDSoggettoStato().getDataCreazione());									
								}								
							}
						}	
					}					
					// END SOGGETTO STATO
					
					//2. CONTATTI
					List<Contatto> contatti = siacTRecapitoSoggettoToContattoList(siacTSoggettoIt.getSiacTRecapitoSoggettos(), true);
					soggettoIt.setContatti(contatti);
					//END CONTATTI
					
					//3. ONERI
					RSoggettoOnereConverterDto soggettoOnereConverterDto = siacRSoggettoOnereToOnereList(siacTSoggettoIt.getSiacRSoggettoOneres(),true);
					List<Onere> listaOneri = soggettoOnereConverterDto.getLista();
					soggettoIt.setTipoOnereId(soggettoOnereConverterDto.getElencoCodes());
					soggettoIt.setElencoOneri(listaOneri);
					//END ONERI
					
					//4. CLASSIFICAZIONI:
					RSoggettoClasseConverterDto soggettoClasseConverterDto = siacRSoggettoClasseToClassificazioneSoggettoList(siacTSoggettoIt.getSiacRSoggettoClasses(),true);
					List<ClassificazioneSoggetto> listaClassSoggetto = soggettoClasseConverterDto.getLista();
					soggettoIt.setTipoClassificazioneSoggettoId(soggettoClasseConverterDto.getElencoCodes());
					soggettoIt.setElencoClass(listaClassSoggetto);
					//END CLASSIFICAZIONI	
					
					//5. TipoSoggetto
					List<TipoSoggetto> listaSoggettoTipo = siacRSoggettoTipoToSoggettoTipoList(siacTSoggettoIt.getSiacRSoggettoTipos(),true);
					if(listaSoggettoTipo!=null && listaSoggettoTipo.size()>0){
						soggettoIt.setTipoSoggetto(listaSoggettoTipo.get(0));
					}
					// END TIPO SOGGETTO
					
					//6. Natura giuridica
					List<NaturaGiuridicaSoggetto> listaNaturaGiuridica = siacRFormaGiuridicaToNaturaGiuridicaSoggettoList(siacTSoggettoIt.getSiacRFormaGiuridicas(),true);
					if(listaNaturaGiuridica!=null && listaNaturaGiuridica.size()>0){
						soggettoIt.setNaturaGiuridicaSoggetto(listaNaturaGiuridica.get(0));
					}
					//END NATURA GIURIDICA
					
					// 7. INDIRIZZI
					List<SiacTIndirizzoSoggettoFin> listaIndirizzi = siacTSoggettoIt.getSiacTIndirizzoSoggettos();

					ArrayList<SiacTIndirizzoSoggettoFin> listaValidi = new ArrayList<SiacTIndirizzoSoggettoFin>();
					for(SiacTIndirizzoSoggettoFin itInd: listaIndirizzi){
						if(itInd!=null && itInd.getDataFineValidita()==null){
							listaValidi.add(itInd);
						}
					}
					siacTSoggettoIt.setSiacTIndirizzoSoggettos(listaValidi);
					// END INDIRIZZI
					
					// 8. ATTRIBUTI
//					List<SiacRSoggettoAttrFin> listaNote = itsiac.getSiacRSoggettoAttrs();
//					if(null != listaNote && listaNote.size() > 0){
//						
//						it.setNote(listaNote.get(0).getTesto());
//					}
//					
					handleSoggettoAttrs(siacTSoggettoIt.getSiacRSoggettoAttrs(), soggettoIt);
					
					// 9. LEGAMI DA - SOGGETTI SUCCESSIVI
					List<SiacRSoggettoRelazFin> listaRelazioniDa = siacTSoggettoIt.getSiacRSoggettoRelazs1();
					if(null != listaRelazioniDa && listaRelazioniDa.size() > 0){
						
						List<Integer> soggettosIdDa = new ArrayList<Integer>();
						List<Integer> legamisIdDa = new ArrayList<Integer>();
						
						for(SiacRSoggettoRelazFin itRelazDa : listaRelazioniDa){
							if(itRelazDa != null && 
							   itRelazDa.getDataFineValidita() == null && itRelazDa.getDataCancellazione() == null
							   && isLegameSoggetto(itRelazDa.getSiacDRelazTipo().getRelazTipoCode())){
								
								soggettosIdDa.add(itRelazDa.getSiacTSoggetto2().getUid());
								legamisIdDa.add(itRelazDa.getSoggettoRelazId());
							}
						}
						
						if(null != soggettosIdDa && soggettosIdDa.size() > 0){
							soggettoIt.setIdsSoggettiSuccessivi(soggettosIdDa);
						}
						
						if(null != legamisIdDa && legamisIdDa.size() > 0){
							soggettoIt.setIdLegamiSoggettiSuccessivi(legamisIdDa);
						}

					}
					// END LEGAMI DA SOGGETTI SUCCESSIVI
					
					// 10. LEGAMI A - SOGGETTI PRECEDENTI
					List<SiacRSoggettoRelazFin> listaRelazioniA = siacTSoggettoIt.getSiacRSoggettoRelazs2();
					if(null != listaRelazioniA && listaRelazioniA.size() > 0){
						
						List<Integer> soggettosIdA = new ArrayList<Integer>();
						List<Integer> legamisIdA = new ArrayList<Integer>();
						
						for(SiacRSoggettoRelazFin itRelazA : listaRelazioniA){
							if(itRelazA != null && 
							   itRelazA.getDataFineValidita() == null && itRelazA.getDataCancellazione() == null
							   && isLegameSoggetto(itRelazA.getSiacDRelazTipo().getRelazTipoCode())){

								soggettosIdA.add(itRelazA.getSiacTSoggetto1().getUid());
								legamisIdA.add(itRelazA.getSoggettoRelazId());
							}
						}
						
						if(null != soggettosIdA && soggettosIdA.size() > 0){
							soggettoIt.setIdsSoggettiPrecedenti(soggettosIdA);
						}
						
						if(null != legamisIdA && legamisIdA.size() > 0){
							soggettoIt.setIdLegamiSoggettiPrecedenti(legamisIdA);
						}

					}
					// END LEGAMI DA SOGGETTI PRECEDENTI
					
					// 11. PERSONA FISICA
					List<SiacTPersonaFisicaFin> siacTPersonaFisicas = siacTSoggettoIt.getSiacTPersonaFisica();
					if(null!=siacTPersonaFisicas && siacTPersonaFisicas.size() > 0){
						for(SiacTPersonaFisicaFin siacTPersonaFisica : siacTPersonaFisicas){
							if(null!=siacTPersonaFisica && siacTPersonaFisica.getDataFineValidita() == null){
								soggettoIt.setDataNascita(siacTPersonaFisica.getNascitaData());
								soggettoIt.setCognome(siacTPersonaFisica.getCognome());
								soggettoIt.setNome(siacTPersonaFisica.getNome());
								
								if(CostantiFin.SESSO_M.equalsIgnoreCase(siacTPersonaFisica.getSesso())){
									soggettoIt.setSesso(Sesso.MASCHIO);
									soggettoIt.setSessoStringa(Sesso.MASCHIO.toString());
								}else{
									soggettoIt.setSesso(Sesso.FEMMINA);
									soggettoIt.setSessoStringa(Sesso.FEMMINA.toString());
								}

								if(null!=siacTPersonaFisica.getSiacTComune()){
									SiacTComuneFin siactcomune = siacTPersonaFisica.getSiacTComune();
									ComuneNascita comuneNascita = siacTComuneToComuneNascita(siactcomune);
									soggettoIt.setComuneNascita(comuneNascita);
								}
							}
						}
					}
					// END PERSONA FISICA	
					
					// 13. PERSONA GIURIDICA
					List<SiacTPersonaGiuridicaFin> siacTPersonaGiuridicas = siacTSoggettoIt.getSiacTPersonaGiuridica();
					if(null!=siacTPersonaGiuridicas && siacTPersonaGiuridicas.size() > 0){
						for(SiacTPersonaGiuridicaFin siacTPersonaGiuridica : siacTPersonaGiuridicas){
							if(null!=siacTPersonaGiuridica && siacTPersonaGiuridica.getDataFineValidita() == null){
								log.debug("",methodName + "siacTPersonaGiuridica : valida");
							}
						}
					}
					// END PERSONAGIURIDICA	
					
					// 14. ALTRI POSSIBILI CAMPI
					// END ALTRI POSSIBILI CAMPI	
					
					break;
				}
			}
		}
		return soggettos;
	}

	private static void setDatiDurc(Soggetto soggetto, SiacTSoggettoFin siacTSoggetto) {
		
		soggetto.setDataFineValiditaDurc(siacTSoggetto.getDataFineValiditaDurc());
		soggetto.setTipoFonteDurc(siacTSoggetto.getTipoFonteDurc());
		
		if (TipoFonteDurc.AUTOMATICA.getCodice().equals(siacTSoggetto.getTipoFonteDurc())) {
			soggetto.setDescrizioneFonteDurc(siacTSoggetto.getFonteDurcAutomatica());
		} 
		
		SiacTClassFin SiacTClass = siacTSoggetto.getFonteDurc();
		
		if (TipoFonteDurc.MANUALE.getCodice().equals(siacTSoggetto.getTipoFonteDurc()) && SiacTClass != null) {
				soggetto.setFonteDurcClassifId(SiacTClass.getClassifId());
				soggetto.setDescrizioneFonteDurc(String.format("%s - %s", SiacTClass.getClassifCode(), SiacTClass.getClassifDesc()));
		} 
		
		soggetto.setNoteDurc(siacTSoggetto.getNoteDurc());
		
		soggetto.setLoginInserimentoDurc(siacTSoggetto.getLoginInserimentoDurc());
		soggetto.setLoginModificaDurc(siacTSoggetto.getLoginModificaDurc());
	}

	

	private static String getUltimaNotaSospesoNotNull(List<SiacRSoggettoStatoFin> listaRSogStato) {
		
		Integer uid = 0;
		String nota = null;
		
		for (SiacRSoggettoStatoFin rss : listaRSogStato) {
			if (StatoOperativoAnagrafica.SOSPESO.equals(
					CostantiFin.statoOperativoAnagraficaStringToEnum(rss.getSiacDSoggettoStato().getSoggettoStatoCode()))
					&& rss.getUid() > uid && org.apache.commons.lang.StringUtils.isNotBlank(rss.getNotaOperazione())) {
				nota = rss.getNotaOperazione();
				uid = rss.getUid();
			}
		}
		
		return nota;
	}
	
	//SIAC-7114
	private static String getUltimaNotaBloccatoNotNull(List<SiacRSoggettoStatoFin> listaRSogStato) {
		
		Integer uid = 0;
		String nota = null;
		
		for (SiacRSoggettoStatoFin rss : listaRSogStato) {
			if (StatoOperativoAnagrafica.BLOCCATO.equals(
					CostantiFin.statoOperativoAnagraficaStringToEnum(rss.getSiacDSoggettoStato().getSoggettoStatoCode()))
					&& rss.getUid() > uid && org.apache.commons.lang.StringUtils.isNotBlank(rss.getNotaOperazione())) {
				nota = rss.getNotaOperazione();
				uid = rss.getUid();
			}
		}

		return nota;
	}

	private static boolean isLegameSoggetto(String tipoRelazCode) {
		return !("SEDE_SECONDARIA".equalsIgnoreCase(tipoRelazCode) || 
				"CSI".equalsIgnoreCase(tipoRelazCode) || 
				"CSC".equalsIgnoreCase(tipoRelazCode));
	}
	
	private static void handleSoggettoAttrs(List<SiacRSoggettoAttrFin> siacRSoggettoAttrs,
			Soggetto it)
	{
		for (SiacRSoggettoAttrFin rsa : siacRSoggettoAttrs)
			if (rsa.isEntitaValida()) {
				SiacTAttrFin attr = rsa.getSiacTAttr();
				if (attr != null)
					if (CostantiFin.T_ATTR_CODE_MATRICOLA.equals(attr.getAttrCode()))
						it.setMatricola(rsa.getTesto());
					else if (CostantiFin.T_ATTR_CODE_NOTE_SOGGETTO.equals(attr.getAttrCode()))
						it.setNote(rsa.getTesto());
			}
	}

	public static List<Soggetto> soggettoEntityToSoggettoModelOPT(List<SiacTSoggettoFin> dtos, List<Soggetto> soggettos, boolean fromRicerca,OttimizzazioneSoggettoDto ottimizzazioneDto) {
		String methodName = "soggettoEntityToSoggettoModelOPT";		
		
		for(Soggetto it : soggettos){
			int idIterato = it.getUid();
			for(SiacTSoggettoFin itsiac : dtos){
				int idConfronto = itsiac.getSoggettoId();
				if(idIterato==idConfronto){
					
					// CODICE SOGGETTO VERSIONE NUMBER
					it.setCodiceSoggettoNumber(new BigInteger(itsiac.getSoggettoCode()));
					
					//1. SOGGETTO STATO
					List<SiacRSoggettoStatoFin> listaRSogStato = ottimizzazioneDto.filtraSiacRSoggettoStatoBySoggettoId(idIterato);
					//List<SiacRSoggettoStatoFin> listaRSogStato =  itsiac.getSiacRSoggettoStatos();
					if(listaRSogStato!=null && listaRSogStato.size()>0){
						SiacRSoggettoStatoFin trovatoValido = null;
						for(SiacRSoggettoStatoFin rSoggStato : listaRSogStato){
							if(rSoggStato!=null && rSoggStato.getDataFineValidita()==null){
								trovatoValido = rSoggStato;
								break;
							}
							if(trovatoValido!=null){
								String code = trovatoValido.getSiacDSoggettoStato().getSoggettoStatoCode();
								StatoOperativoAnagrafica statoOpAnag = CostantiFin.statoOperativoAnagraficaStringToEnum(code);
								it.setStatoOperativo(statoOpAnag);
								it.setIdStatoOperativoAnagrafica(trovatoValido.getSiacDSoggettoStato().getSoggettoStatoId());
								it.setDataStato(trovatoValido.getDataInizioValidita());
							}
						}
						if(trovatoValido!=null){
							String code = trovatoValido.getSiacDSoggettoStato().getSoggettoStatoCode();
							StatoOperativoAnagrafica statoOpAnag = CostantiFin.statoOperativoAnagraficaStringToEnum(code);
							it.setStatoOperativo(statoOpAnag);
							it.setIdStatoOperativoAnagrafica(trovatoValido.getSiacDSoggettoStato().getSoggettoStatoId());
							it.setDataStato(trovatoValido.getDataInizioValidita());
						}
					}
					
					// se arrivo da ricerca soggetti devo settare lo stato 
					// in modifica "al volo" Jira-138
					if(fromRicerca){
						if(null!=itsiac.getSiacTSoggettoMods() &&
							itsiac.getSiacTSoggettoMods().size()>0){
							for(SiacTSoggettoModFin siacTSoggettoMod : itsiac.getSiacTSoggettoMods()){								
								if(siacTSoggettoMod.getDataFineValidita()==null){									
									it.setStatoOperativo(StatoOperativoAnagrafica.IN_MODIFICA);
									it.setLoginControlloPerModifica(siacTSoggettoMod.getLoginOperazione());
//									it.setIdStatoOperativoAnagrafica(trovatoValido.getSiacDSoggettoStato().getSoggettoStatoId());
//									it.setDataStato(trovatoValido.getSiacDSoggettoStato().getDataCreazione());									
								}								
							}
						}	
					}					
					// END SOGGETTO STATO
					
					//2. CONTATTI
					List<SiacTRecapitoSoggettoFin> listaSiacTRecapitoSoggetto = ottimizzazioneDto.filtraSiacTRecapitoSoggettoBySoggettoId(idIterato);
					List<Contatto> contatti = siacTRecapitoSoggettoToContattoList(listaSiacTRecapitoSoggetto, true);
					it.setContatti(contatti);
					//END CONTATTI
					
					//3. ONERI
					List<SiacRSoggettoOnereFin> listaSiacRSoggettoOnere = ottimizzazioneDto.filtraSiacRSoggettoOnereBySoggettoId(idIterato);
					RSoggettoOnereConverterDto soggettoOnereConverterDto = siacRSoggettoOnereToOnereList(listaSiacRSoggettoOnere,true);
					List<Onere> listaOneri = soggettoOnereConverterDto.getLista();
					it.setTipoOnereId(soggettoOnereConverterDto.getElencoCodes());
					it.setElencoOneri(listaOneri);
					//END ONERI
					
					//4. CLASSIFICAZIONI:
					List<SiacRSoggettoClasseFin> siacRSoggettoClasse = ottimizzazioneDto.filtraSiacRSoggettoClasseBySoggettoId(idIterato);
					RSoggettoClasseConverterDto soggettoClasseConverterDto = siacRSoggettoClasseToClassificazioneSoggettoList(siacRSoggettoClasse,true);
					List<ClassificazioneSoggetto> listaClassSoggetto = soggettoClasseConverterDto.getLista();
					it.setTipoClassificazioneSoggettoId(soggettoClasseConverterDto.getElencoCodes());
					it.setElencoClass(listaClassSoggetto);
					//END CLASSIFICAZIONI	
					
					//5. TipoSoggetto
					List<SiacRSoggettoTipoFin> siacRSoggettoTipo = ottimizzazioneDto.filtraSiacRSoggettoTipoBySoggettoId(idIterato);
					List<TipoSoggetto> listaSoggettoTipo = siacRSoggettoTipoToSoggettoTipoList(siacRSoggettoTipo,true);
					if(listaSoggettoTipo!=null && listaSoggettoTipo.size()>0){
						it.setTipoSoggetto(listaSoggettoTipo.get(0));
					}
					// END TIPO SOGGETTO
					
					//6. Natura giuridica
					//List<NaturaGiuridicaSoggetto> listaNaturaGiuridica = siacRFormaGiuridicaToNaturaGiuridicaSoggettoList(itsiac.getSiacRFormaGiuridicas(),true);
					List<NaturaGiuridicaSoggetto> listaNaturaGiuridica = 
							siacRFormaGiuridicaToNaturaGiuridicaSoggettoListOPT(ottimizzazioneDto.filtraSiacRFormaGiuridicaBySoggettoId(idIterato),true,ottimizzazioneDto);
					if(listaNaturaGiuridica!=null && listaNaturaGiuridica.size()>0){
						it.setNaturaGiuridicaSoggetto(listaNaturaGiuridica.get(0));
					}
					//END NATURA GIURIDICA
					
					// 7. INDIRIZZI
					List<SiacTIndirizzoSoggettoFin> listaIndirizzi = ottimizzazioneDto.filtraSiacTIndirizzoSoggettoBySoggettoId(idIterato);
					ArrayList<SiacTIndirizzoSoggettoFin> listaValidi = new ArrayList<SiacTIndirizzoSoggettoFin>();
					for(SiacTIndirizzoSoggettoFin itInd: listaIndirizzi){
						if(itInd!=null && itInd.getDataFineValidita()==null){
							listaValidi.add(itInd);
						}
					}
					itsiac.setSiacTIndirizzoSoggettos(listaValidi);
					// END INDIRIZZI
					
					// 8. NOTE
					//List<SiacRSoggettoAttrFin> listaNote = itsiac.getSiacRSoggettoAttrs();
					List<SiacRSoggettoAttrFin> listaNote = ottimizzazioneDto.filtraSiacRSoggettoAttrBySoggettoId(idIterato);
					if(null != listaNote && listaNote.size() > 0){
						it.setNote(listaNote.get(0).getTesto());
					}
					// END NOTE
					
					// 9. LEGAMI DA - SOGGETTI SUCCESSIVI
					List<SiacRSoggettoRelazFin> listaRelazioniDa = ottimizzazioneDto.filtraSiacRSoggettoRelaz1BySoggettoId(idIterato);
					if(null != listaRelazioniDa && listaRelazioniDa.size() > 0){
						
						List<Integer> soggettosIdDa = new ArrayList<Integer>();
						List<Integer> legamisIdDa = new ArrayList<Integer>();
						
						for(SiacRSoggettoRelazFin itRelazDa : listaRelazioniDa){
							if(itRelazDa != null && itRelazDa.getDataFineValidita() == null){
//								&& TipoRelazioneSoggetto.CATENA.name().equalsIgnoreCase(itRelazDa.getSiacDRelazTipo().getRelazTipoCode())){
								
								soggettosIdDa.add(itRelazDa.getSiacTSoggetto2().getUid());
								legamisIdDa.add(itRelazDa.getSoggettoRelazId());
							}
						}
						
						if(null != soggettosIdDa && soggettosIdDa.size() > 0){
							it.setIdsSoggettiSuccessivi(soggettosIdDa);
						}
						
						if(null != legamisIdDa && legamisIdDa.size() > 0){
							it.setIdLegamiSoggettiSuccessivi(legamisIdDa);
						}

					}
					// END LEGAMI DA SOGGETTI SUCCESSIVI
					
					// 10. LEGAMI A - SOGGETTI PRECEDENTI
					List<SiacRSoggettoRelazFin> listaRelazioniA = ottimizzazioneDto.filtraSiacRSoggettoRelaz2BySoggettoId(idIterato);
					if(null != listaRelazioniA && listaRelazioniA.size() > 0){
						
						List<Integer> soggettosIdA = new ArrayList<Integer>();
						List<Integer> legamisIdA = new ArrayList<Integer>();
						
						for(SiacRSoggettoRelazFin itRelazA : listaRelazioniA){
							if(itRelazA != null && itRelazA.getDataFineValidita() == null){
//								 && TipoRelazioneSoggetto.CATENA.name().equalsIgnoreCase(itRelazA.getSiacDRelazTipo().getRelazTipoCode())){

								soggettosIdA.add(itRelazA.getSiacTSoggetto1().getUid());
								legamisIdA.add(itRelazA.getSoggettoRelazId());
							}
						}
						
						if(null != soggettosIdA && soggettosIdA.size() > 0){
							it.setIdsSoggettiPrecedenti(soggettosIdA);
						}
						
						if(null != legamisIdA && legamisIdA.size() > 0){
							it.setIdLegamiSoggettiPrecedenti(legamisIdA);
						}

					}
					// END LEGAMI DA SOGGETTI PRECEDENTI
					
					// 11. PERSONA FISICA
					List<SiacTPersonaFisicaFin> siacTPersonaFisicas = ottimizzazioneDto.filtraSiacTPersonaFisicaBySoggettoId(idIterato);
					if(null!=siacTPersonaFisicas && siacTPersonaFisicas.size() > 0){
						for(SiacTPersonaFisicaFin siacTPersonaFisica : siacTPersonaFisicas){
							if(null!=siacTPersonaFisica && siacTPersonaFisica.getDataFineValidita() == null){
								it.setDataNascita(siacTPersonaFisica.getNascitaData());
								it.setCognome(siacTPersonaFisica.getCognome());
								it.setNome(siacTPersonaFisica.getNome());
								
								if(CostantiFin.SESSO_M.equalsIgnoreCase(siacTPersonaFisica.getSesso())){
									it.setSesso(Sesso.MASCHIO);
									it.setSessoStringa(Sesso.MASCHIO.toString());
								}else{
									it.setSesso(Sesso.FEMMINA);
									it.setSessoStringa(Sesso.FEMMINA.toString());
								}

								if(null!=siacTPersonaFisica.getSiacTComune()){
									SiacTComuneFin siactcomune = siacTPersonaFisica.getSiacTComune();
									ComuneNascita comuneNascita = siacTComuneToComuneNascita(siactcomune);
									it.setComuneNascita(comuneNascita);
								}
							}
						}
					}
					// END PERSONA FISICA	
					
					// 13. PERSONA GIURIDICA
					List<SiacTPersonaGiuridicaFin> siacTPersonaGiuridicas = ottimizzazioneDto.filtraSiacTPersonaGiuridicaBySoggettoId(idIterato);
					if(null!=siacTPersonaGiuridicas && siacTPersonaGiuridicas.size() > 0){
						for(SiacTPersonaGiuridicaFin siacTPersonaGiuridica : siacTPersonaGiuridicas){
							if(null!=siacTPersonaGiuridica && siacTPersonaGiuridica.getDataFineValidita() == null){
								log.debug("",methodName + "siacTPersonaGiuridica : valida");
							}
						}
					}
					// END PERSONAGIURIDICA	
					
					// 14. ALTRI POSSIBILI CAMPI
					// END ALTRI POSSIBILI CAMPI	
					
					break;
				}
			}
		}
		return soggettos;
	}
	
	/**
	 * torna l'unico con data fine validita nulla
	 * @param list
	 * @return
	 */
	public final static <T extends SiacTEnteBase> T findValido(List<T> list){
		if(list!=null && list.size()>0){
			for(T iterato : list){
				if(iterato!=null && iterato.getDataFineValidita()==null){
					return iterato;
				}
			}
		}
		return null;
	}
	
	private static ComuneNascita siacTComuneToComuneNascita(SiacTComuneFin siactcomune){
		ComuneNascita comuneNascita = new ComuneNascita();
		if(siactcomune!=null){
			comuneNascita.setDescrizione(siactcomune.getComuneDesc());
			comuneNascita.setCodiceBelfiore(siactcomune.getComuneDesc());
			comuneNascita.setCodiceIstat(siactcomune.getComuneIstatCode());
			comuneNascita.setUid(siactcomune.getUid());
			SiacRComuneProvinciaFin provincia = findValido(siactcomune.getSiacRComuneProvincias());
			if(provincia!=null && provincia.getSiacTProvincia()!=null){
				comuneNascita.setProvinciaCode(provincia.getSiacTProvincia().getProvinciaIstatCode());
				comuneNascita.setProvinciaDesc(provincia.getSiacTProvincia().getProvinciaDesc());
			}
			SiacRComuneRegioneFin regione = findValido( siactcomune.getSiacRComuneRegiones());
			if(regione!=null && regione.getSiacTRegione()!=null){
				comuneNascita.setRegioneCode(regione.getSiacTRegione().getRegioneIstatCodice());
				comuneNascita.setRegioneDesc(regione.getSiacTRegione().getRegioneDenominazione());
			}
			if(siactcomune.getSiacTNazione()!=null){
				comuneNascita.setNazioneCode(siactcomune.getSiacTNazione().getNazioneCode());
				comuneNascita.setNazioneDesc(siactcomune.getSiacTNazione().getNazioneDesc());
			}
		}
		return comuneNascita;
	}
	
	private static List<NaturaGiuridicaSoggetto> siacRFormaGiuridicaToNaturaGiuridicaSoggettoList(List<SiacRFormaGiuridicaFin> listaFrom, boolean soloValidi) {
		List<NaturaGiuridicaSoggetto> tipoSoggs = new ArrayList<NaturaGiuridicaSoggetto>();
		if(listaFrom!=null && listaFrom.size()>0){
			List<SiacRFormaGiuridicaFin> listaDaConvertire = new ArrayList<SiacRFormaGiuridicaFin>();
			for(SiacRFormaGiuridicaFin oggettoFrom : listaFrom){
				if(oggettoFrom!=null){
					if(soloValidi){
						if(oggettoFrom!=null && oggettoFrom.getDataFineValidita()==null){
							listaDaConvertire.add(oggettoFrom);
						}
					} else {
						listaDaConvertire.add(oggettoFrom);
					}
				}
			}
			if(listaDaConvertire!=null && listaDaConvertire.size()>0){
				for(SiacRFormaGiuridicaFin trovatoValido : listaDaConvertire){
					NaturaGiuridicaSoggetto toAdd = siacRFormaGiuridicaToNaturaGiuridicaSoggetto(trovatoValido);
					tipoSoggs.add(toAdd);
				}
			}
		}
		return tipoSoggs;
	}
	
	/**
	 * Presuppone che in input siano gia validi
	 * @param listaFrom
	 * @param soloValidi
	 * @param ottimizzazioneDto
	 * @return
	 */
	private static List<NaturaGiuridicaSoggetto> siacRFormaGiuridicaToNaturaGiuridicaSoggettoListOPT(List<SiacRFormaGiuridicaFin> listaFrom, boolean soloValidi,
			OttimizzazioneSoggettoDto ottimizzazioneDto) {
		List<NaturaGiuridicaSoggetto> tipoSoggs = new ArrayList<NaturaGiuridicaSoggetto>();
		if(listaFrom!=null && listaFrom.size()>0){
			for(SiacRFormaGiuridicaFin trovatoValido : listaFrom){
				NaturaGiuridicaSoggetto toAdd = siacRFormaGiuridicaToNaturaGiuridicaSoggetto(trovatoValido);
				tipoSoggs.add(toAdd);
			}
		}
		return tipoSoggs;
	}

	private static List<NaturaGiuridicaSoggetto> siacRFormaGiuridicaModToNaturaGiuridicaSoggettoList(List<SiacRFormaGiuridicaModFin> listaFrom, boolean soloValidi) {
		List<NaturaGiuridicaSoggetto> tipoSoggs = new ArrayList<NaturaGiuridicaSoggetto>();
		if(listaFrom!=null && listaFrom.size()>0){
			List<SiacRFormaGiuridicaModFin> listaDaConvertire = new ArrayList<SiacRFormaGiuridicaModFin>();
			for(SiacRFormaGiuridicaModFin oggettoFrom : listaFrom){
				if(oggettoFrom!=null){
					if(soloValidi){
						if(oggettoFrom!=null && oggettoFrom.getDataFineValidita()==null){
							listaDaConvertire.add(oggettoFrom);
						}
					} else {
						listaDaConvertire.add(oggettoFrom);
					}
				}
			}
			if(listaDaConvertire!=null && listaDaConvertire.size()>0){
				for(SiacRFormaGiuridicaModFin trovatoValido : listaDaConvertire){
					NaturaGiuridicaSoggetto toAdd = siacRFormaGiuridicaModToNaturaGiuridicaSoggetto(trovatoValido);
					tipoSoggs.add(toAdd);
				}
			}
		}
		return tipoSoggs;
	}
	
	private static NaturaGiuridicaSoggetto siacRFormaGiuridicaToNaturaGiuridicaSoggetto(SiacRFormaGiuridicaFin oggFrom) {
		NaturaGiuridicaSoggetto naturaGiuridicaSoggetto = new NaturaGiuridicaSoggetto();
		if(oggFrom.getSiacTFormaGiuridica()!=null){
			naturaGiuridicaSoggetto.setUid(oggFrom.getSiacTFormaGiuridica().getUid());
			naturaGiuridicaSoggetto.setSoggettoTipoCode(oggFrom.getSiacTFormaGiuridica().getFormaGiuridicaIstatCodice());
			naturaGiuridicaSoggetto.setSoggettoTipoDesc(oggFrom.getSiacTFormaGiuridica().getFormaGiuridicaDesc());
		}
		return naturaGiuridicaSoggetto;
	}

	private static NaturaGiuridicaSoggetto siacRFormaGiuridicaModToNaturaGiuridicaSoggetto(SiacRFormaGiuridicaModFin oggFrom) {
		NaturaGiuridicaSoggetto naturaGiuridicaSoggetto = new NaturaGiuridicaSoggetto();
		if(oggFrom.getSiacTFormaGiuridica()!=null){
			naturaGiuridicaSoggetto.setUid(oggFrom.getSiacTFormaGiuridica().getUid());
			naturaGiuridicaSoggetto.setSoggettoTipoCode(oggFrom.getSiacTFormaGiuridica().getFormaGiuridicaIstatCodice());
			naturaGiuridicaSoggetto.setSoggettoTipoDesc(oggFrom.getSiacTFormaGiuridica().getFormaGiuridicaDesc());
		}
		return naturaGiuridicaSoggetto;
	}

	private static List<TipoSoggetto> siacRSoggettoTipoToSoggettoTipoList(List<SiacRSoggettoTipoFin> siacRSoggettoTipos, boolean soloValidi) {
		List<TipoSoggetto> tipoSoggs = new ArrayList<TipoSoggetto>();
		if(siacRSoggettoTipos!=null && siacRSoggettoTipos.size()>0){
			List<SiacRSoggettoTipoFin> listaDaConvertire = new ArrayList<SiacRSoggettoTipoFin>();
			for(SiacRSoggettoTipoFin rSoggClasse : siacRSoggettoTipos){
				if(rSoggClasse!=null){
					if(soloValidi){
						if(rSoggClasse!=null && rSoggClasse.getDataFineValidita()==null){
							listaDaConvertire.add(rSoggClasse);
						}
					} else {
						listaDaConvertire.add(rSoggClasse);
					}
				}
			}
			if(listaDaConvertire!=null && listaDaConvertire.size()>0){
				for(SiacRSoggettoTipoFin trovatoValido : listaDaConvertire){
					TipoSoggetto tipoSogg = siacRSoggettoTipoToSoggettoTipo(trovatoValido);
					tipoSoggs.add(tipoSogg);
				}
			}
		}
		return tipoSoggs;
	}

	private static List<TipoSoggetto> siacRSoggettoTipoModToSoggettoTipoList(List<SiacRSoggettoTipoModFin> siacRSoggettoTipoMods, boolean soloValidi) {
		List<TipoSoggetto> tipoSoggs = new ArrayList<TipoSoggetto>();
		if(siacRSoggettoTipoMods!=null && siacRSoggettoTipoMods.size()>0){
			List<SiacRSoggettoTipoModFin> listaDaConvertire = new ArrayList<SiacRSoggettoTipoModFin>();
			for(SiacRSoggettoTipoModFin rSoggClasse : siacRSoggettoTipoMods){
				if(rSoggClasse!=null){
					if(soloValidi){
						if(rSoggClasse!=null && rSoggClasse.getDataFineValidita()==null){
							listaDaConvertire.add(rSoggClasse);
						}
					} else {
						listaDaConvertire.add(rSoggClasse);
					}
				}
			}
			if(listaDaConvertire!=null && listaDaConvertire.size()>0){
				for(SiacRSoggettoTipoModFin trovatoValido : listaDaConvertire){
					TipoSoggetto tipoSogg = siacRSoggettoTipoModToSoggettoTipo(trovatoValido);
					tipoSoggs.add(tipoSogg);
				}
			}
		}
		return tipoSoggs;
	}

	private static TipoSoggetto siacRSoggettoTipoToSoggettoTipo(SiacRSoggettoTipoFin trovatoValido) {
		TipoSoggetto tipoSoggetto = new TipoSoggetto();
		tipoSoggetto.setUid(trovatoValido.getUid());
		if(trovatoValido.getSiacDSoggettoTipo()!=null){
			tipoSoggetto.setSoggettoTipoCode(trovatoValido.getSiacDSoggettoTipo().getSoggettoTipoCode());
			tipoSoggetto.setSoggettoTipoDesc(trovatoValido.getSiacDSoggettoTipo().getSoggettoTipoDesc());
			tipoSoggetto.setSoggettoTipoId(trovatoValido.getSiacDSoggettoTipo().getSoggettoTipoId());
		}
		return tipoSoggetto;
	}

	private static TipoSoggetto siacRSoggettoTipoModToSoggettoTipo(SiacRSoggettoTipoModFin trovatoValido) {
		TipoSoggetto tipoSoggetto = new TipoSoggetto();
		tipoSoggetto.setUid(trovatoValido.getUid());
		if(trovatoValido.getSiacDSoggettoTipo()!=null){
			tipoSoggetto.setSoggettoTipoCode(trovatoValido.getSiacDSoggettoTipo().getSoggettoTipoCode());
			tipoSoggetto.setSoggettoTipoDesc(trovatoValido.getSiacDSoggettoTipo().getSoggettoTipoDesc());
			tipoSoggetto.setSoggettoTipoId(trovatoValido.getSiacDSoggettoTipo().getSoggettoTipoId());
		}
		return tipoSoggetto;
	}

	private static RSoggettoClasseConverterDto siacRSoggettoClasseToClassificazioneSoggettoList(List<SiacRSoggettoClasseFin> listaRSoggClasse, boolean soloValidi) {
		RSoggettoClasseConverterDto risultato = new RSoggettoClasseConverterDto();
		List<ClassificazioneSoggetto> classSoggs = new ArrayList<ClassificazioneSoggetto>();
		if(listaRSoggClasse!=null && listaRSoggClasse.size()>0){
			List<SiacRSoggettoClasseFin> listaDaConvertire = new ArrayList<SiacRSoggettoClasseFin>();
			for(SiacRSoggettoClasseFin rSoggClasse : listaRSoggClasse){
				if(rSoggClasse!=null){
					if(soloValidi){
						if(rSoggClasse!=null && rSoggClasse.getDataFineValidita()==null){
							listaDaConvertire.add(rSoggClasse);
						}
					} else {
						listaDaConvertire.add(rSoggClasse);
					}
				}
			}
			if(listaDaConvertire!=null && listaDaConvertire.size()>0){
				for(SiacRSoggettoClasseFin trovatoValido : listaDaConvertire){
					ClassificazioneSoggetto onere = siacRSoggettoClasseToClassificazioneSoggetto(trovatoValido);
					classSoggs.add(onere);
				}
			}
		}
		if(classSoggs!=null && classSoggs.size()>0){
			String[] elencoCodes = new String[classSoggs.size()];
			for(int i=0;i<classSoggs.size();i++){
				elencoCodes[i] = classSoggs.get(i).getSoggettoClasseCode();
			}
			risultato.setElencoCodes(elencoCodes);
		}
		risultato.setLista(classSoggs);
		return risultato;
	}
	
	
	private static RSoggettoClasseConverterDto siacRSoggettoClasseModToClassificazioneSoggettoList(List<SiacRSoggettoClasseModFin> listaRSoggClasse, boolean soloValidi) {
		RSoggettoClasseConverterDto risultato = new RSoggettoClasseConverterDto();
		List<ClassificazioneSoggetto> classSoggs = new ArrayList<ClassificazioneSoggetto>();
		if(listaRSoggClasse!=null && listaRSoggClasse.size()>0){
			List<SiacRSoggettoClasseModFin> listaDaConvertire = new ArrayList<SiacRSoggettoClasseModFin>();
			for(SiacRSoggettoClasseModFin rSoggClasse : listaRSoggClasse){
				if(rSoggClasse!=null){
					if(soloValidi){
						if(rSoggClasse!=null && rSoggClasse.getDataFineValidita()==null){
							listaDaConvertire.add(rSoggClasse);
						}
					} else {
						listaDaConvertire.add(rSoggClasse);
					}
				}
			}
			if(listaDaConvertire!=null && listaDaConvertire.size()>0){
				for(SiacRSoggettoClasseModFin trovatoValido : listaDaConvertire){
					ClassificazioneSoggetto onere = siacRSoggettoClasseModToClassificazioneSoggetto(trovatoValido);
					classSoggs.add(onere);
				}
			}
		}
		if(classSoggs!=null && classSoggs.size()>0){
			String[] elencoCodes = new String[classSoggs.size()];
			for(int i=0;i<classSoggs.size();i++){
				elencoCodes[i] = classSoggs.get(i).getSoggettoClasseCode();
			}
			risultato.setElencoCodes(elencoCodes);
		}
		risultato.setLista(classSoggs);
		return risultato;
	}

	private static ClassificazioneSoggetto siacRSoggettoClasseToClassificazioneSoggetto(SiacRSoggettoClasseFin rsoggClasse) {
		ClassificazioneSoggetto classSogg = new ClassificazioneSoggetto();
		classSogg.setUid(rsoggClasse.getUid());
		if(rsoggClasse.getSiacDSoggettoClasse()!=null){
			classSogg.setIdSoggClasse(rsoggClasse.getSiacDSoggettoClasse().getUid());
			classSogg.setSoggettoClasseCode(rsoggClasse.getSiacDSoggettoClasse().getSoggettoClasseCode());
			classSogg.setSoggettoClasseDesc(rsoggClasse.getSiacDSoggettoClasse().getSoggettoClasseDesc());
			if(rsoggClasse.getSiacDSoggettoClasse().getSiacDSoggettoClasseTipo()!=null){
				classSogg.setIdTipoSoggClasse(rsoggClasse.getSiacDSoggettoClasse().getSiacDSoggettoClasseTipo().getUid());
				classSogg.setSoggettoTipoClasseCode(rsoggClasse.getSiacDSoggettoClasse().getSiacDSoggettoClasseTipo().getSoggettoClasseTipoCode());
				classSogg.setSoggettoTipoClasseDesc(rsoggClasse.getSiacDSoggettoClasse().getSiacDSoggettoClasseTipo().getSoggettoClasseTipoDesc());
			}
		}
		return classSogg;
	}
	
	
	private static ClassificazioneSoggetto siacRSoggettoClasseModToClassificazioneSoggetto(SiacRSoggettoClasseModFin rsoggClasse) {
		ClassificazioneSoggetto classSogg = new ClassificazioneSoggetto();
		classSogg.setUid(rsoggClasse.getUid());
		if(rsoggClasse.getSiacDSoggettoClasse()!=null){
			classSogg.setIdSoggClasse(rsoggClasse.getSiacDSoggettoClasse().getUid());
			classSogg.setSoggettoClasseCode(rsoggClasse.getSiacDSoggettoClasse().getSoggettoClasseCode());
			classSogg.setSoggettoClasseDesc(rsoggClasse.getSiacDSoggettoClasse().getSoggettoClasseDesc());
			if(rsoggClasse.getSiacDSoggettoClasse().getSiacDSoggettoClasseTipo()!=null){
				classSogg.setIdTipoSoggClasse(rsoggClasse.getSiacDSoggettoClasse().getSiacDSoggettoClasseTipo().getUid());
				classSogg.setSoggettoTipoClasseCode(rsoggClasse.getSiacDSoggettoClasse().getSiacDSoggettoClasseTipo().getSoggettoClasseTipoCode());
				classSogg.setSoggettoTipoClasseDesc(rsoggClasse.getSiacDSoggettoClasse().getSiacDSoggettoClasseTipo().getSoggettoClasseTipoDesc());
			}
		}
		return classSogg;
	}

	private static RSoggettoOnereConverterDto siacRSoggettoOnereToOnereList(List<SiacRSoggettoOnereFin> listaRSoggOnere, boolean soloValidi) {
		RSoggettoOnereConverterDto risultato = new RSoggettoOnereConverterDto();
		List<Onere> oneri = new ArrayList<Onere>();
		if(listaRSoggOnere!=null && listaRSoggOnere.size()>0){
			List<SiacRSoggettoOnereFin> listaDaConvertire = new ArrayList<SiacRSoggettoOnereFin>();
			for(SiacRSoggettoOnereFin rSoggOnere : listaRSoggOnere){
				if(rSoggOnere!=null){
					if(soloValidi){
						if(rSoggOnere!=null && rSoggOnere.getDataFineValidita()==null){
							listaDaConvertire.add(rSoggOnere);
						}
					} else {
						listaDaConvertire.add(rSoggOnere);
					}
				}
			}
			if(listaDaConvertire!=null && listaDaConvertire.size()>0){
				for(SiacRSoggettoOnereFin trovatoValido : listaDaConvertire){
					Onere onere = siacRSoggettoOnereToOnere(trovatoValido);
					oneri.add(onere);
				}
			}
		}
		if(oneri!=null && oneri.size()>0){
			String[] elencoCodes = new String[oneri.size()];
			for(int i=0;i<oneri.size();i++){
				elencoCodes[i] = oneri.get(i).getOnereCod();
			}
			risultato.setElencoCodes(elencoCodes);
		}
		risultato.setLista(oneri);
		return risultato;
	}
	
	
	private static RSoggettoOnereConverterDto siacRSoggettoOnereModToOnereList(List<SiacRSoggettoOnereModFin> listaRSoggOnere, boolean soloValidi) {
		RSoggettoOnereConverterDto risultato = new RSoggettoOnereConverterDto();
		List<Onere> oneri = new ArrayList<Onere>();
		if(listaRSoggOnere!=null && listaRSoggOnere.size()>0){
			List<SiacRSoggettoOnereModFin> listaDaConvertire = new ArrayList<SiacRSoggettoOnereModFin>();
			for(SiacRSoggettoOnereModFin rSoggOnere : listaRSoggOnere){
				if(rSoggOnere!=null){
					if(soloValidi){
						if(rSoggOnere!=null && rSoggOnere.getDataFineValidita()==null){
							listaDaConvertire.add(rSoggOnere);
						}
					} else {
						listaDaConvertire.add(rSoggOnere);
					}
				}
			}
			if(listaDaConvertire!=null && listaDaConvertire.size()>0){
				for(SiacRSoggettoOnereModFin trovatoValido : listaDaConvertire){
					Onere onere = siacRSoggettoOnereModToOnere(trovatoValido);
					oneri.add(onere);
				}
			}
		}
		if(oneri!=null && oneri.size()>0){
			String[] elencoCodes = new String[oneri.size()];
			for(int i=0;i<oneri.size();i++){
				elencoCodes[i] = oneri.get(i).getOnereCod();
			}
			risultato.setElencoCodes(elencoCodes);
		}
		risultato.setLista(oneri);
		return risultato;
	}

	private static Onere siacRSoggettoOnereToOnere(SiacRSoggettoOnereFin trovatoValido) {
		Onere onere = new Onere();
		if(trovatoValido.getSiacDOnere()!=null){
			onere.setOnereCod(trovatoValido.getSiacDOnere().getOnereCode());
			onere.setOnereDesc(trovatoValido.getSiacDOnere().getOnereDesc());
			onere.setIdDOnere(trovatoValido.getSiacDOnere().getUid());
		}
		onere.setUid(trovatoValido.getUid());
		return onere;
	}
	
	private static Onere siacRSoggettoOnereModToOnere(SiacRSoggettoOnereModFin trovatoValido) {
		Onere onere = new Onere();
		if(trovatoValido.getSiacDOnere()!=null){
			onere.setOnereCod(trovatoValido.getSiacDOnere().getOnereCode());
			onere.setOnereDesc(trovatoValido.getSiacDOnere().getOnereDesc());
			onere.setIdDOnere(trovatoValido.getSiacDOnere().getUid());
		}
		onere.setUid(trovatoValido.getUid());
		return onere;
	}

	/**
	 * converte da List<SiacTRecapitoSoggettoFin> a List<Contatto>
	 * @param listaTRecapito
	 * @param soloValidi
	 * @return
	 */
	public static List<Contatto> siacTRecapitoSoggettoToContattoList(List<SiacTRecapitoSoggettoFin> listaTRecapito,boolean soloValidi){
		List<Contatto> contatti = new ArrayList<Contatto>();
		if(listaTRecapito!=null && listaTRecapito.size()>0){
			List<SiacTRecapitoSoggettoFin> listaDaConvertire = new ArrayList<SiacTRecapitoSoggettoFin>();
			for(SiacTRecapitoSoggettoFin tRecapitoSogg : listaTRecapito){
				if(tRecapitoSogg!=null){
					if(soloValidi){
						if(tRecapitoSogg!=null && tRecapitoSogg.getDataFineValidita()==null){
							listaDaConvertire.add(tRecapitoSogg);
						}
					} else {
						listaDaConvertire.add(tRecapitoSogg);
					}
				}
			}
			if(listaDaConvertire!=null && listaDaConvertire.size()>0){
				for(SiacTRecapitoSoggettoFin trovatoValido : listaDaConvertire){
					Contatto contatto = siacTRecapitoSoggettoToContatto(trovatoValido);
					contatti.add(contatto);
				}
			}
		}
		return contatti;
	}
	
	public static List<Contatto> siacTRecapitoSoggettoModToContattoList(List<SiacTRecapitoSoggettoModFin> listaTRecapito,boolean soloValidi){
		List<Contatto> contatti = new ArrayList<Contatto>();
		if(listaTRecapito!=null && listaTRecapito.size()>0){
			List<SiacTRecapitoSoggettoModFin> listaDaConvertire = new ArrayList<SiacTRecapitoSoggettoModFin>();
			for(SiacTRecapitoSoggettoModFin tRecapitoSogg : listaTRecapito){
				if(tRecapitoSogg!=null){
					if(soloValidi){
						if(tRecapitoSogg!=null && tRecapitoSogg.getDataFineValidita()==null){
							listaDaConvertire.add(tRecapitoSogg);
						}
					} else {
						listaDaConvertire.add(tRecapitoSogg);
					}
				}
			}
			if(listaDaConvertire!=null && listaDaConvertire.size()>0){
				for(SiacTRecapitoSoggettoModFin trovatoValido : listaDaConvertire){
					Contatto contatto = siacTRecapitoSoggettoModToContatto(trovatoValido);
					contatti.add(contatto);
				}
			}
		}
		return contatti;
	}
	
	/**
	 * converte da SiacTRecapitoSoggettoFin a Contatto
	 * @param siacTRecapitoSoggetto
	 * @return
	 */
	public static Contatto siacTRecapitoSoggettoToContatto(SiacTRecapitoSoggettoFin siacTRecapitoSoggetto){
		Contatto contatto = new Contatto();
		if(siacTRecapitoSoggetto.getSiacDRecapitoModo()!=null){
			contatto.setIdTipoContatto(siacTRecapitoSoggetto.getSiacDRecapitoModo().getUid());
			contatto.setContattoCodModo(siacTRecapitoSoggetto.getSiacDRecapitoModo().getRecapitoModoCode());
			contatto.setDescrizioneModo(siacTRecapitoSoggetto.getSiacDRecapitoModo().getRecapitoModoDesc());
		}
		contatto.setDescrizione(siacTRecapitoSoggetto.getRecapitoDesc());
		contatto.setContattoCod(siacTRecapitoSoggetto.getRecapitoCode());
		Integer uuid = siacTRecapitoSoggetto.getUid();
		if(uuid!=null){
			contatto.setUid(uuid);
		}
		contatto.setAvviso(siacTRecapitoSoggetto.getAvviso());
		return contatto;
	}
	
	
	public static Contatto siacTRecapitoSoggettoModToContatto(SiacTRecapitoSoggettoModFin siacTRecapitoSoggetto){
		Contatto contatto = new Contatto();
		if(siacTRecapitoSoggetto.getSiacDRecapitoModo()!=null){
			contatto.setIdTipoContatto(siacTRecapitoSoggetto.getSiacDRecapitoModo().getUid());
			contatto.setContattoCodModo(siacTRecapitoSoggetto.getSiacDRecapitoModo().getRecapitoModoCode());
			contatto.setDescrizioneModo(siacTRecapitoSoggetto.getSiacDRecapitoModo().getRecapitoModoDesc());
		}
		contatto.setDescrizione(siacTRecapitoSoggetto.getRecapitoDesc());
		contatto.setContattoCod(siacTRecapitoSoggetto.getRecapitoCode());
		contatto.setUid(siacTRecapitoSoggetto.getUid());
		contatto.setAvviso(siacTRecapitoSoggetto.getAvviso());
		return contatto;
	}
	
	public static List<IndirizzoSoggetto> indirizzoSoggettoEntityToIndirizzoSoggettoModel(List<SiacTIndirizzoSoggettoFin> dtos, List<IndirizzoSoggetto> indirizzos) {
		for(IndirizzoSoggetto it : indirizzos){
			int idIterato = it.getIndirizzoId();
			for(SiacTIndirizzoSoggettoFin itsiac : dtos){
				int idConfronto = itsiac.getIndirizzoId();
				if(idIterato==idConfronto){
					
					//1. PRINCIPALE
					it.setPrincipale(Boolean.toString(itsiac.getPrincipale()!=null && itsiac.getPrincipale().equalsIgnoreCase(CostantiFin.TRUE)));
					// END PRINCIPALE
					
					//2. AVVISO
					it.setAvviso(Boolean.toString(itsiac.getAvviso()!=null && itsiac.getAvviso().equalsIgnoreCase(CostantiFin.TRUE))); 
					// END AVVISO
					
					//3. TIPO INDIRIZZO
					List<SiacRIndirizzoSoggettoTipoFin> listaRIndirizzoSoggettoTipo =  itsiac.getSiacRIndirizzoSoggettoTipos();
					if(listaRIndirizzoSoggettoTipo!=null && listaRIndirizzoSoggettoTipo.size()>0){
						SiacRIndirizzoSoggettoTipoFin trovatoValido = null;
						for(SiacRIndirizzoSoggettoTipoFin rIndirizzoSoggettoTipo : listaRIndirizzoSoggettoTipo){
							if(rIndirizzoSoggettoTipo!=null && rIndirizzoSoggettoTipo.getDataFineValidita()==null){
								trovatoValido = rIndirizzoSoggettoTipo;
								break;
							}
						}
						if(trovatoValido!=null){
							String tipoCode = trovatoValido.getSiacDIndirizzoTipo().getIndirizzoTipoCode();
							String tipoDesc = trovatoValido.getSiacDIndirizzoTipo().getIndirizzoTipoDesc();
							Integer tipoId = trovatoValido.getSiacDIndirizzoTipo().getIndirizzoTipoId();
							
                            it.setIdTipoIndirizzo(tipoCode);
                            it.setIdTipoIndirizzoDesc(tipoDesc);
						}
					}
					//END TIPO INDIRIZZO		
					
					//COMUNE:
					impostaCodiceComune(it, itsiac.getSiacTComune());
					
					
					break;
				}
			}
		}
		return indirizzos;
	}
	
	
	private static void impostaCodiceComune(IndirizzoSoggetto it, SiacTComuneFin siacTComune){
		//codice comune
		if(siacTComune!=null){
			

			
			it.setCodiceIstatComune(siacTComune.getComuneIstatCode());
			
		}
	}
	
	
	public static List<IndirizzoSoggetto> indirizzoSoggettoModEntityToIndirizzoSoggettoModel(List<SiacTIndirizzoSoggettoModFin> dtos, List<IndirizzoSoggetto> indirizzos) {
		for(IndirizzoSoggetto it : indirizzos){
			int idIterato = it.getIndirizzoId();
			for(SiacTIndirizzoSoggettoModFin itsiac : dtos){
				int idConfronto = itsiac.getIndirizzoModId();
				if(idIterato==idConfronto){
					
					//1. PRINCIPALE
					it.setPrincipale(Boolean.toString(itsiac.getPrincipale()!=null && itsiac.getPrincipale().equalsIgnoreCase(CostantiFin.TRUE)));
					// END PRINCIPALE
					
					//2. AVVISO
					it.setAvviso(Boolean.toString(itsiac.getAvviso()!=null && itsiac.getAvviso().equalsIgnoreCase(CostantiFin.TRUE))); 
					// END AVVISO
					
					//3. TIPO INDIRIZZO
					List<SiacRIndirizzoSoggettoTipoModFin> listaRIndirizzoSoggettoTipoMod =  itsiac.getSiacRIndirizzoSoggettoTipoMods();
					if(listaRIndirizzoSoggettoTipoMod!=null && listaRIndirizzoSoggettoTipoMod.size()>0){
						SiacRIndirizzoSoggettoTipoModFin trovatoValido = null;
						for(SiacRIndirizzoSoggettoTipoModFin rIndirizzoSoggettoTipoMod : listaRIndirizzoSoggettoTipoMod){
							if(rIndirizzoSoggettoTipoMod!=null && rIndirizzoSoggettoTipoMod.getDataFineValidita()==null){
								trovatoValido = rIndirizzoSoggettoTipoMod;
								break;
							}
						}
						if(trovatoValido!=null){
							String tipoCode = trovatoValido.getSiacDIndirizzoTipo().getIndirizzoTipoCode();
							String tipoDesc = trovatoValido.getSiacDIndirizzoTipo().getIndirizzoTipoDesc();
							Integer tipoId = trovatoValido.getSiacDIndirizzoTipo().getIndirizzoTipoId();

                            it.setIdTipoIndirizzo(tipoCode);
                            it.setIdTipoIndirizzoDesc(tipoDesc);
						}
					}
					//END TIPO INDIRIZZO
					
					//COMUNE:
					impostaCodiceComune(it, itsiac.getSiacTComune());
					
					break;
				}
			}
		}
		return indirizzos;
	}
	
	/**
	 * Wrapper di retrocompatibilita'
	 * @param dtos
	 * @param soggettoModPags
	 * @param associatoA
	 * @return
	 */
	public static List<ModalitaPagamentoSoggetto> modalitaPagamentoEntityToModalitaPagamentoModel(List<SiacTModpagFin> dtos, List<ModalitaPagamentoSoggetto> soggettoModPags, String associatoA) {
		return modalitaPagamentoEntityToModalitaPagamentoModel(dtos, soggettoModPags, associatoA, null);
	}
	
	public static List<ModalitaPagamentoSoggetto> modalitaPagamentoEntityToModalitaPagamentoModel(List<SiacTModpagFin> dtos, List<ModalitaPagamentoSoggetto> soggettoModPags, String associatoA,OttimizzazioneSoggettoDto ottimizzazioneSoggettoDto) {
		for(ModalitaPagamentoSoggetto it : soggettoModPags){
			int idIterato = it.getUid();
			for(SiacTModpagFin itsiac : dtos){
				int idConfronto = itsiac.getModpagId();
				if(idIterato==idConfronto){
					
					//STATO MODALITA' PAGAMENTO
					
					List<SiacRModpagStatoFin> listaRModPagStato = null;
					if(ottimizzazioneSoggettoDto!=null){
						//RAMO OTTIMIZZATO
						listaRModPagStato = ottimizzazioneSoggettoDto.filtraSiacRModpagStatoFinBySiacTModpagFin(itsiac);
					} else {
						//RAMO CLASSICO
						listaRModPagStato =  itsiac.getSiacRModpagStatos();
					}
					
					if(listaRModPagStato!=null && listaRModPagStato.size()>0){
						SiacRModpagStatoFin trovatoValido = null;
						for(SiacRModpagStatoFin rModPagStato : listaRModPagStato){
						
							//da verificare la data fine validita a null
							if(rModPagStato!=null && rModPagStato.getDataFineValidita()==null){
								trovatoValido = rModPagStato;
								break;
							}
						}
						if(trovatoValido!=null){
							
							String statoCode = trovatoValido.getSiacDModpagStato().getModpagStatoCode();
							String statoDesc = trovatoValido.getSiacDModpagStato().getModpagStatoDesc();
                            Integer statoId = trovatoValido.getSiacDModpagStato().getModpagStatoId();

                            it.setIdStatoModalitaPagamento(statoId);
                            it.setCodiceStatoModalitaPagamento(statoCode);
                            it.setDescrizioneStatoModalitaPagamento(statoDesc);
                            
                            // conto corrente intestazione
                            it.setIntestazioneConto(trovatoValido.getSiacTModpag().getContocorrenteIntestazione());
                            
                            if(trovatoValido.getSiacTModpag().getQuietanzanteNascitaData()!=null){
                            	// data nascita quietanzante
                            	it.setDataNascitaQuietanzante(TimingUtils.convertiDataIn_GgMmYyyy(trovatoValido.getSiacTModpag().getQuietanzanteNascitaData()));
                            }
                            
                            if(trovatoValido.getSiacTModpag().getQuietanzianteNascitaLuogo()!=null){
                            	ComuneNascita cn = new ComuneNascita();
                            	it.setComuneNascita(cn);
                            	it.getComuneNascita().setDescrizione(trovatoValido.getSiacTModpag().getQuietanzianteNascitaLuogo());
                            	it.getComuneNascita().setNazioneCode(trovatoValido.getSiacTModpag().getQuietanzianteNascitaStato());
                            }
                            
                            
						}
					}
					// END SOGGETTO STATO
					
					//2. ALTRI POSSIBILI CAMPI
					it.setLoginCreazione(itsiac.getLoginCreazione());
					it.setLoginModifica(itsiac.getLoginModifica());
					
					// codice progressivo MDP
					
					// fix - jira SIAC-1974
					List<SiacRModpagOrdineFin> rModpagOrdineS = null;
					SiacRModpagOrdineFin rModpagOrdine = null;
					if(ottimizzazioneSoggettoDto!=null){
						//RAMO OTTIMIZZATO
						rModpagOrdineS = ottimizzazioneSoggettoDto.filtraSiacRModpagOrdineFinBySiacTModpagFin(itsiac);
					} else {
						//RAMO CLASSICO
						rModpagOrdineS = itsiac.getSiacRModpagOrdines();
					}
					
					if(rModpagOrdineS!=null && rModpagOrdineS.size()>0){
						rModpagOrdine = rModpagOrdineS.get(0);
						it.setCodiceModalitaPagamento(String.valueOf(rModpagOrdine.getOrdine()));
					}
							
					//END ALTRI POSSIBILI CAMPI					
					it.setAssociatoA(associatoA);
					break;
				}
			}
		}
		return soggettoModPags;
	}
	
	public static List<ModalitaPagamentoSoggetto> modalitaPagamentoEntityToModalitaPagamentoCessioniModel(List<SiacTModpagFin> dtos, List<ModalitaPagamentoSoggetto> soggettoModPags, String associatoA) {


		for(ModalitaPagamentoSoggetto it : soggettoModPags){
			int idIterato = it.getUid();		
			for(SiacTModpagFin itsiac : dtos){
				int idConfronto = itsiac.getModpagId();
				if(idIterato==idConfronto){
						
					//1. STATO MODALITA' PAGAMENTO CESSIONI
					//Associazione mdp destinatario della cessione
					List<SiacRSoggrelModpagFin> listaRSoggrelModpag = itsiac.getSiacRSoggrelModpags();
					if(listaRSoggrelModpag!=null && listaRSoggrelModpag.size()>0){
						for(SiacRSoggrelModpagFin rSoggrModpag : listaRSoggrelModpag){

							//Soggetti della relazione cessione
							SiacRSoggettoRelazFin rSoggettoRelaz =  rSoggrModpag.getSiacRSoggettoRelaz();
							if(rSoggettoRelaz != null){

								//Stati della cessione
								List<SiacRSoggettoRelazStatoFin> listaRSoggettoRelazStato =  rSoggettoRelaz.getSiacRSoggettoRelazStatos();
								if(listaRSoggettoRelazStato!=null && listaRSoggettoRelazStato.size()>0){
									SiacRSoggettoRelazStatoFin trovatoValidoCessione = null;
									for(SiacRSoggettoRelazStatoFin rSoggettoRelazStato : listaRSoggettoRelazStato){

										//????da verificare la data fine validita a null
										if(rSoggettoRelazStato!=null && rSoggettoRelazStato.getDataFineValidita()==null){
											trovatoValidoCessione = rSoggettoRelazStato;
					
//											break;									
										}

										if(trovatoValidoCessione!=null){
																				
											//campi r_soggetto_relaz
											Integer tipoRelazCessId = rSoggettoRelaz.getSiacDRelazTipo().getRelazTipoId();
											Integer soggIdDa = rSoggettoRelaz.getSiacTSoggetto1().getSoggettoId();
											Integer soggIdA = rSoggettoRelaz.getSiacTSoggetto2().getSoggettoId();
											Date dataInizValid = rSoggettoRelaz.getDataInizioValidita();
											Date dataFineValid = rSoggettoRelaz.getDataFineValidita();
											Date dataCreaz = rSoggettoRelaz.getDataCreazione();
											Date dataMod = rSoggettoRelaz.getDataModifica();
											Date dataCanc = rSoggettoRelaz.getDataCancellazione();
											String loginOp = rSoggettoRelaz.getLoginOperazione();
											
											//campi r_soggrel_modpag
											Integer soggRelazId = rSoggettoRelaz.getSoggettoRelazId();
											Integer modpagId = rSoggrModpag.getSoggrelmpagId();
											//Da varificare String note =  rSoggrModpag.????;
											String logCanc = rSoggrModpag.getLoginCancellazione();
											String logCreaz = rSoggrModpag.getLoginCreazione();
											String logMod = rSoggrModpag.getLoginModifica();
											
											//campi r_soggetto_relaz_stato
											Integer idStatoModCessione = trovatoValidoCessione.getSiacDRelazStato().getRelazStatoId();
											String codStatoModCessione = trovatoValidoCessione.getSiacDRelazStato().getRelazStatoCode();
											String descrStatoModCessione = trovatoValidoCessione.getSiacDRelazStato().getRelazStatoDesc();
											
											ModalitaPagamentoSoggettoCessioni mdpCess = new ModalitaPagamentoSoggettoCessioni();
											
											if(tipoRelazCessId != null){
												mdpCess.setTipoRelazCessioneId(tipoRelazCessId);
											}
											if(soggIdA != null){
												mdpCess.setCessioneSoggettoA(soggIdA);
											}
											if(soggIdDa != null){
												mdpCess.setCessioneSoggettoDa(soggIdDa);
											}
											if(dataInizValid != null){
												mdpCess.setDataInizioValidita(dataInizValid);
											}
											if(dataFineValid != null){
												mdpCess.setDataFineValidita(dataFineValid);
											}
											if(dataCreaz != null){
												mdpCess.setDataCreazione(dataCreaz);
											}
											if(dataMod!=null){
												mdpCess.setDataModifica(dataMod);
											}
											if(dataCanc != null){
												mdpCess.setDataCancellazione(dataCanc);
											}
											if(loginOp!=null){
												mdpCess.setLoginOperazione(loginOp);
											}
											if(soggRelazId!=null){
												mdpCess.setSoggettoRelazId(soggRelazId);
											}
											if(modpagId!=null){
												mdpCess.setModpagId(modpagId);
											}
											if(logCanc!=null){
												mdpCess.setLoginCancellazione(logCanc);
											}
											if(logCreaz!=null){
												mdpCess.setLoginCreazione(logCreaz);
											}
											if(logMod!=null){
												mdpCess.setLoginModifica(logMod);
											}
											if(idStatoModCessione!=null){
												mdpCess.setIdStatoModalitaCessione(idStatoModCessione);
											}
											if(descrStatoModCessione!=null){
												mdpCess.setDescrizioneStatoModalitaCessione(descrStatoModCessione);
											}
											if(codStatoModCessione!=null){
												mdpCess.setCodiceStatoModalitaCessione(codStatoModCessione);
											}
											
											it.setModalitaPagamentoSoggettoCessione(mdpCess);

																						
											log.debug("","!!!!!!!!!!!!!!!! id stato cessione    --> " + idStatoModCessione);
											log.debug("","!!!!!!!!!!!!!!!! id tipo  cessione    --> " + tipoRelazCessId);
											log.debug("","!!!!!!!!!!!!!!!! id SoggIdDA          --> " + soggIdDa);
											log.debug("","!!!!!!!!!!!!!!!! id SoggIdA           --> " + soggIdA);
										}
									}
								}									
							}
						}
					} 
					
					it.setLoginCreazione(itsiac.getLoginCreazione());
					it.setLoginModifica(itsiac.getLoginModifica());
		
					it.setAssociatoA(associatoA);
					break;
				}
			}
		}
		log.debug("","step 4");
		return soggettoModPags;
	}	
	
	public static ModalitaPagamentoSoggetto modalitaPagamentoSingleEntityToModalitaPagamentoModel(SiacTModpagFin mdpDef, ModalitaPagamentoSoggetto modPag, String associatoA) {
			int idDef = modPag.getUid();
			int idConfronto = mdpDef.getModpagId();				
			
			if(idDef==idConfronto){
				//1. STATO MODALITA' PAGAMENTO 
				List<SiacRModpagStatoFin> listaRModPagStato =  mdpDef.getSiacRModpagStatos();
				if(listaRModPagStato!=null && listaRModPagStato.size()>0){
					SiacRModpagStatoFin trovatoValido = null;
					for(SiacRModpagStatoFin rModPagStato : listaRModPagStato){
						if(rModPagStato!=null && rModPagStato.getDataFineValidita()==null){
							trovatoValido = rModPagStato;
							break;
						}
					}
					if(trovatoValido!=null){
							
						String statoCode = trovatoValido.getSiacDModpagStato().getModpagStatoCode();
						String statoDesc = trovatoValido.getSiacDModpagStato().getModpagStatoDesc();
                        Integer statoId = trovatoValido.getSiacDModpagStato().getModpagStatoId();

                        /*log.debug("","modalitaPagamentoEntityToModalitaPagamentoModel : statoId    --> " + statoId);
                        log.debug("","modalitaPagamentoEntityToModalitaPagamentoModel : statoCode  --> " + statoCode);
                        log.debug("","modalitaPagamentoEntityToModalitaPagamentoModel : statoDesc  --> " + statoDesc);*/
                        
                        modPag.setIdStatoModalitaPagamento(statoId);
                        modPag.setCodiceStatoModalitaPagamento(statoCode);
                        modPag.setDescrizioneStatoModalitaPagamento(statoDesc);                            
					}
				}
				// END SOGGETTO STATO
					
				//2. ALTRI POSSIBILI CAMPI
					
				modPag.setLoginCreazione(mdpDef.getLoginCreazione());
				modPag.setLoginModifica(mdpDef.getLoginModifica());
				
				
				//END ALTRI POSSIBILI CAMPI					
				modPag.setAssociatoA(associatoA);
				
				// carico il codice MDP se presente
				if(null!=mdpDef.getSiacRModpagOrdines() && !mdpDef.getSiacRModpagOrdines().isEmpty()){
					for (SiacRModpagOrdineFin siacRModpagOrdine : mdpDef.getSiacRModpagOrdines()) {
						if(siacRModpagOrdine.getDataCancellazione()==null){
							modPag.setCodiceModalitaPagamento(String.valueOf(siacRModpagOrdine.getOrdine()));
						}
					}
				}
			}
			
		return modPag;
	}

	public static ModalitaPagamentoSoggetto modalitaPagamentoModSingleEntityToModalitaPagamentoModel(SiacTModpagModFin mdpMod, ModalitaPagamentoSoggetto modPagMod, String associatoA) {
		int idDef = modPagMod.getUid();
		int idConfronto = mdpMod.getSiacTModpag().getModpagId();
		
		if(idDef==idConfronto){
//			//ESSENDO IN MODIFICA NON MI SERVE TRATTARE LO STATO 
//			List<SiacRModpagStatoFin> listaRModPagStato =  mdpDef.getSiacRModpagStatos();
//			if(listaRModPagStato!=null && listaRModPagStato.size()>0){
//				SiacRModpagStatoFin trovatoValido = null;
//				for(SiacRModpagStatoFin rModPagStato : listaRModPagStato){
//					if(rModPagStato!=null && rModPagStato.getDataFineValidita()==null){
//						trovatoValido = rModPagStato;
//						break;
//					}
//				}
//				if(trovatoValido!=null){
//						
//					String statoCode = trovatoValido.getSiacDModpagStato().getModpagStatoCode();
//					String statoDesc = trovatoValido.getSiacDModpagStato().getModpagStatoDesc();
//                    Integer statoId = trovatoValido.getSiacDModpagStato().getModpagStatoId();
//
//                    log.debug("","modalitaPagamentoEntityToModalitaPagamentoModel : statoId    --> " + statoId);
//                    log.debug("","modalitaPagamentoEntityToModalitaPagamentoModel : statoCode  --> " + statoCode);
//                    log.debug("","modalitaPagamentoEntityToModalitaPagamentoModel : statoDesc  --> " + statoDesc);
//                    
//                    modPag.setIdStatoModalitaPagamento(statoId);
//                    modPag.setCodiceStatoModalitaPagamento(statoCode);
//                    modPag.setDescrizioneStatoModalitaPagamento(statoDesc);                            
//				}
//			}
			// END SOGGETTO STATO
			
			//2. ALTRI POSSIBILI CAMPI

			//END ALTRI POSSIBILI CAMPI					
			modPagMod.setAssociatoA(associatoA);
		}
		
		
		// nuovi campi
		// intestazione conto
		if(mdpMod.getContocorrenteIntestazione()!=null){
			modPagMod.setIntestazioneConto(mdpMod.getContocorrenteIntestazione());
		}
		
		// luogo e stato di nascita
		if(mdpMod.getQuietanzianteNascitaLuogo()!=null){
			
			ComuneNascita cn = new ComuneNascita();
			cn.setDescrizione(mdpMod.getQuietanzianteNascitaLuogo());
			cn.setNazioneCode(mdpMod.getQuietanzianteNascitaStato());
			
			modPagMod.setComuneNascita(cn);
		}
		
		// data di nascita quietanzante
		if(mdpMod.getQuietanzanteNascitaData()!=null){
			modPagMod.setDataNascitaQuietanzante(TimingUtils.convertiDataIn_GgMmYyyy(mdpMod.getQuietanzanteNascitaData()));
		}
		
		// fine - nuovi campi
		
	return modPagMod;
}


	public static SedeSecondariaSoggetto soggettoEntityToSedeSecondariaSoggettoModel(SiacTSoggettoFin dto, 
			SedeSecondariaSoggetto sedeSecondariaSoggetto,OttimizzazioneSoggettoDto ottimizzazioneSoggetto) {
		List<SiacTSoggettoFin> dtos = new ArrayList<SiacTSoggettoFin>();
		dtos.add(dto);
		List<SedeSecondariaSoggetto> sediSecondarie = new ArrayList<SedeSecondariaSoggetto>();
		sediSecondarie.add(sedeSecondariaSoggetto);
		Map<Integer, Boolean> mappaPerModifica = new HashMap<Integer, Boolean>();
		mappaPerModifica.put(sedeSecondariaSoggetto.getUid(), false);
		sediSecondarie = soggettoEntityToSedeSecondariaSoggettoModel(dtos, sediSecondarie, mappaPerModifica, false,ottimizzazioneSoggetto);
		return sediSecondarie.get(0);
	}
	
	public static List<SedeSecondariaSoggetto> soggettoEntityToSedeSecondariaSoggettoModel(List<SiacTSoggettoFin> listaSoggetti, List<SedeSecondariaSoggetto> listaSedi,
			Map<Integer, Boolean> mappaSediModificate, boolean isIncludeModif,OttimizzazioneSoggettoDto ottimizzazioneSoggetto) {
		for(SedeSecondariaSoggetto sedeSecondariaSoggetto : listaSedi) {
			int idIterato = sedeSecondariaSoggetto.getUid();
			for(SiacTSoggettoFin siacTSoggettoFin : listaSoggetti) {
				int idConfronto = siacTSoggettoFin.getSoggettoId();
				if(idIterato == idConfronto){
					// 1. INDIRIZZO
					List<SiacTIndirizzoSoggettoFin> listaIndirizzi = null;
					
					if(ottimizzazioneSoggetto!=null){
						//RAMO OTTIMIZZATO
						listaIndirizzi = ottimizzazioneSoggetto.filtraSiacTIndirizzoSoggettoBySoggettoId(idIterato);
					} else {
						//RAMO CLASSICO
						listaIndirizzi =  siacTSoggettoFin.getSiacTIndirizzoSoggettos();
					}
					
					
					if(listaIndirizzi!=null && listaIndirizzi.size()>0) {
						SiacTIndirizzoSoggettoFin trovatoValido = null;
						for(SiacTIndirizzoSoggettoFin currentIndirizzo : listaIndirizzi){
							if((currentIndirizzo != null) && (currentIndirizzo.getDataFineValidita() == null) && (currentIndirizzo.getDataCancellazione() == null)){
								trovatoValido = currentIndirizzo;
								break;
							}
						}
						if(trovatoValido!=null){
							IndirizzoSoggetto supportIndirizzo = new IndirizzoSoggetto();
							supportIndirizzo.setIndirizzoId(trovatoValido.getUid());
							supportIndirizzo.setDenominazione(trovatoValido.getToponimo());
							
							//COMUNE:
							impostaCodiceComune(supportIndirizzo, trovatoValido.getSiacTComune());
							
							supportIndirizzo.setComune(trovatoValido.getSiacTComune().getComuneDesc());
							supportIndirizzo.setCap(trovatoValido.getZipCode());
							supportIndirizzo.setSedime(trovatoValido.getSiacDViaTipo() != null ? trovatoValido.getSiacDViaTipo().getViaTipoDesc() : "");
							supportIndirizzo.setNumeroCivico(trovatoValido.getNumeroCivico());
							supportIndirizzo.setCodiceNazione(trovatoValido.getSiacTComune().getSiacTNazione().getNazioneCode());
							supportIndirizzo.setNazione(trovatoValido.getSiacTComune().getSiacTNazione().getNazioneDesc());
							supportIndirizzo.setAvviso(trovatoValido.getAvviso());
							
							//COMUNE E PROVINCIA:
							SiacTComuneFin comune = trovatoValido.getSiacTComune();
							if(comune!=null){
								
								SiacRComuneProvinciaFin siacRComuneProv = null;
								if(ottimizzazioneSoggetto!=null){
									//RAMO OTTIMIZZATO
									siacRComuneProv = ottimizzazioneSoggetto.getSiacRComuneProvinciaValidoBySiacTComuneFin(comune);
								} else {
									//RAMO CLASSICO
									if(comune!=null){
										List<SiacRComuneProvinciaFin> rComunProv = comune.getSiacRComuneProvincias();
										if(rComunProv!=null && rComunProv.size()>0){
											siacRComuneProv = rComunProv.get(0);
										}
									}
								}
								if(siacRComuneProv!=null && siacRComuneProv.getSiacTProvincia()!=null){
									supportIndirizzo.setProvincia(siacRComuneProv.getSiacTProvincia().getSiglaAutomobilistica());
								}
							}
							//
								
							
							sedeSecondariaSoggetto.setIndirizzoSoggettoPrincipale(supportIndirizzo);
						}
					}
					// END INDIRIZZO
					
					// 2. STATO SEDE SECONDARIA 
					List<SiacRSoggettoStatoFin> listaRSogStato = null;
					if(ottimizzazioneSoggetto!=null){
						//RAMO OTTIMIZZATO
						listaRSogStato = ottimizzazioneSoggetto.filtraSiacRSoggettoStatoBySoggettoId(idIterato);
					} else {
						//RAMO CLASSICO
						listaRSogStato =  siacTSoggettoFin.getSiacRSoggettoStatos();
					}
					
					if(listaRSogStato!=null && listaRSogStato.size()>0){
						SiacRSoggettoStatoFin trovatoValido = null;
						for(SiacRSoggettoStatoFin rSoggStato : listaRSogStato){
							if(rSoggStato!=null && rSoggStato.getDataFineValidita()==null){
								trovatoValido = rSoggStato;
								break;
							}
						}
						if(trovatoValido!=null){
							String code = trovatoValido.getSiacDSoggettoStato().getSoggettoStatoCode();
							StatoOperativoSedeSecondaria statoOpSedeSecondaria = CostantiFin.statoOperativoSediSecondarieStringToEnum(code);
							sedeSecondariaSoggetto.setStatoOperativoSedeSecondaria(statoOpSedeSecondaria);
							sedeSecondariaSoggetto.setIdStatoOperativoSedeSecondaria(trovatoValido.getSiacDSoggettoStato().getSoggettoStatoId());
							sedeSecondariaSoggetto.setDescrizioneStatoOperativoSedeSecondaria(trovatoValido.getSiacDSoggettoStato().getSoggettoStatoDesc());
						}
					}
					
					//
					// TEST PER GESTIONE DINAMICA STATO SEDE SECONDARIA
					//
					// Modifica per jira-686
					//
					// - Se isIncludeModif == false : non sovrascrivo lo stato, perch� provengo da chiamata esterna 
					// 
					// - Se isIncludeModif == true  : sovrascrivo lo stato, perch� provengo da chiamata interna
                    //					              oppure il chiamante esterno ha bisogno di gestire anche lo stato IN_MODIFICA
					//
					if(isIncludeModif == true){
						if(null!=siacTSoggettoFin.getSiacTSoggettoMods() && siacTSoggettoFin.getSiacTSoggettoMods().size()>0){
							for(SiacTSoggettoModFin siacTSoggettoMod : siacTSoggettoFin.getSiacTSoggettoMods()){
								if(siacTSoggettoMod.getDataFineValidita()==null){
									sedeSecondariaSoggetto.setStatoOperativoSedeSecondaria(StatoOperativoSedeSecondaria.IN_MODIFICA);
									sedeSecondariaSoggetto.setDescrizioneStatoOperativoSedeSecondaria(StatoOperativoSedeSecondaria.IN_MODIFICA.name());
									sedeSecondariaSoggetto.setUtenteModifica(siacTSoggettoFin.getLoginOperazione());
								}
							}
						}else if(mappaSediModificate != null && mappaSediModificate.get(idConfronto) ){
							sedeSecondariaSoggetto.setStatoOperativoSedeSecondaria(StatoOperativoSedeSecondaria.IN_MODIFICA);
							sedeSecondariaSoggetto.setDescrizioneStatoOperativoSedeSecondaria(StatoOperativoSedeSecondaria.IN_MODIFICA.name());
							sedeSecondariaSoggetto.setUtenteModifica(siacTSoggettoFin.getLoginOperazione());
						}
					}
					// END TEST PER GESTIONE DINAMICA STATO SEDE SECONDARIA
					// END STATO SEDE SECONDARIA				
					
					// 4. Contatti-recapiti
					
					List<SiacTRecapitoSoggettoFin> listaTRSog = null;
					if(ottimizzazioneSoggetto!=null){
						//RAMO OTTIMIZZATO
						listaTRSog= ottimizzazioneSoggetto.filtraSiacTRecapitoSoggettoBySoggettoId(idIterato);
					} else {
						//RAMO CLASSICO
						listaTRSog = siacTSoggettoFin.getSiacTRecapitoSoggettos();
					}
					
					if (listaTRSog != null && listaTRSog.size() > 0) {
						sedeSecondariaSoggetto.setContatti(siacTRecapitoSoggettoToContattoList(listaTRSog, true));
					}
					
					//END
					break;
				}
			}
		}
		return listaSedi;
	}

	public static SedeSecondariaSoggetto soggettoEntityModToSedeSecondariaSoggettoModel(SiacTSoggettoModFin dto, SedeSecondariaSoggetto sedeSecondariaSoggetto) {
		
		// 1. INDIRIZZO
		List<SiacTIndirizzoSoggettoModFin> listaIndirizzi =  dto.getSiacTIndirizzoSoggettoMods();
		if(listaIndirizzi!=null && listaIndirizzi.size()>0) {
			SiacTIndirizzoSoggettoModFin trovatoValido = listaIndirizzi.get(0);
			if(trovatoValido!=null){
				IndirizzoSoggetto supportIndirizzo = new IndirizzoSoggetto();
				supportIndirizzo.setIndirizzoId(trovatoValido.getUid());
				supportIndirizzo.setDenominazione(trovatoValido.getToponimo());
				
				//COMUNE:
				impostaCodiceComune(supportIndirizzo, trovatoValido.getSiacTComune());
				
				supportIndirizzo.setComune(trovatoValido.getSiacTComune().getComuneDesc());
				supportIndirizzo.setCap(trovatoValido.getZipCode());
				supportIndirizzo.setSedime(trovatoValido.getSiacDViaTipo() != null ? trovatoValido.getSiacDViaTipo().getViaTipoDesc() : "");
				supportIndirizzo.setNumeroCivico(trovatoValido.getNumeroCivico());
				supportIndirizzo.setCodiceNazione(trovatoValido.getSiacTComune().getSiacTNazione().getNazioneCode());
				sedeSecondariaSoggetto.setIndirizzoSoggettoPrincipale(supportIndirizzo);
			}
		}
		// END INDIRIZZO
		
		// 4. Contatti-recapiti
		List<SiacTRecapitoSoggettoModFin> listaTRSog = dto.getSiacTRecapitoSoggettoMods();
		if (listaTRSog != null && listaTRSog.size() > 0) {
			sedeSecondariaSoggetto.setContatti(siacTRecapitoSoggettoModToContattoList(listaTRSog, true));
		}
		//END
		
		return sedeSecondariaSoggetto;
	}
	
//	public static String estraiNotaValida(SiacTSoggettoFin soggEntity) {
//		String valida = null;
//		List<SiacRSoggettoAttrFin> notes = soggEntity.getSiacRSoggettoAttrs();
//		if(notes!=null && notes.size()>0){
//			for(SiacRSoggettoAttrFin it : notes){
//				if(it!=null && it.getDataFineValidita()==null){
//					valida = it.getTesto();
//					break;
//				}
//			}
//		}
//		return valida;
//	}
	
	// estrae genericamente gli attributi di soggetto passando l'attributo
	public static String estraiAttrSoggetto(SiacTSoggettoFin soggEntity, String attributo) {
		String valida = null;
		List<SiacRSoggettoAttrFin> notes = soggEntity.getSiacRSoggettoAttrs();
		if(notes!=null && notes.size()>0){
			for(SiacRSoggettoAttrFin it : notes){
				// se e' valido ed e' del tipo cercato (es. nota, matricola)
				if(it!=null && it.getDataFineValidita()==null && it.getSiacTAttr().getAttrCode().equals(attributo)){
					valida = it.getTesto();
					break;
				}
			}
		}
		return valida;
	}
	
	// estrae genericamente gli attributi di soggetto passando l'attributo per le tavole MOD
	public static String estraiAttrSoggettoMod(SiacTSoggettoModFin soggEntity, String attributo) {
		String valida = null;
		List<SiacRSoggettoAttrModFin> notes = soggEntity.getSiacRSoggettoAttrMods();
		if(notes!=null && notes.size()>0){
			for(SiacRSoggettoAttrModFin it : notes){
				if(it!=null && it.getDataFineValidita()==null && it.getSiacTAttr().getAttrCode().equals(attributo)){
					valida = it.getTesto();
					break;
				}
			}
		}
		return valida;
	}
	
	public static String estraiNotaValidaMod(SiacTSoggettoModFin soggEntity) {
		String valida = null;
		List<SiacRSoggettoAttrModFin> notes = soggEntity.getSiacRSoggettoAttrMods();
		if(notes!=null && notes.size()>0){
			for(SiacRSoggettoAttrModFin it : notes){
				if(it!=null && it.getDataFineValidita()==null){
					valida = it.getTesto();
					break;
				}
			}
		}
		return valida;
	}

	/* METODI PER MOVIMENTI_GESTIONALI */
	public static Impegno siacTMovgestEntityToImpegnoModel(SiacTMovgestFin dto, Impegno movimentoGestione, OttimizzazioneMovGestDto ottimizzazioneDto) {
		List<SiacTMovgestFin> dtos = new ArrayList<SiacTMovgestFin>();
		dtos.add(dto);
		List<MovimentoGestione> movimentos = new ArrayList<MovimentoGestione>();
		movimentos.add(movimentoGestione);
		movimentos = siacTMovgestEntityToImpegnoModel(dtos, movimentos,ottimizzazioneDto);
		return (Impegno) movimentos.get(0);
	}
	
	/* METODI PER MOVIMENTI_GESTIONALI */
	public static Impegno siacTMovgestEntityToImpegnoModel(SiacTMovgestFin dto, Impegno movimentoGestione, OttimizzazioneMovGestDto ottimizzazioneDto, boolean ottimizzatoCompletamenteDaChiamante) {
		List<SiacTMovgestFin> dtos = new ArrayList<SiacTMovgestFin>();
		dtos.add(dto);
		List<MovimentoGestione> movimentos = new ArrayList<MovimentoGestione>();
		movimentos.add(movimentoGestione);
		movimentos = siacTMovgestEntityToImpegnoModel(dtos, movimentos,ottimizzazioneDto, ottimizzatoCompletamenteDaChiamante);
		return (Impegno) movimentos.get(0);
	}
	
	/**
	 * wrapper di retrocompatibilita'
	 * @param dto
	 * @param movimentoGestione
	 * @return
	 */
	public static Accertamento siacTMovgestEntityToAccertamentoModel(SiacTMovgestFin dto, Accertamento movimentoGestione) {
		return siacTMovgestEntityToAccertamentoModel(dto, movimentoGestione,null);
	}

	public static Accertamento siacTMovgestEntityToAccertamentoModel(SiacTMovgestFin dto, Accertamento movimentoGestione,OttimizzazioneMovGestDto ottimizzazioneDto) {
		List<SiacTMovgestFin> dtos = new ArrayList<SiacTMovgestFin>();
		dtos.add(dto);
		List<MovimentoGestione> movimentos = new ArrayList<MovimentoGestione>();
		movimentos.add(movimentoGestione);
		movimentos = siacTMovgestEntityToImpegnoModel(dtos, movimentos,ottimizzazioneDto);
		return (Accertamento) movimentos.get(0);
	}
	
	public static Accertamento siacTMovgestEntityToAccertamentoModel(SiacTMovgestFin dto, Accertamento movimentoGestione,OttimizzazioneMovGestDto ottimizzazioneDto, boolean ottimizzatoCompletamenteDaChiamante) {
		List<SiacTMovgestFin> dtos = new ArrayList<SiacTMovgestFin>();
		dtos.add(dto);
		List<MovimentoGestione> movimentos = new ArrayList<MovimentoGestione>();
		movimentos.add(movimentoGestione);
		movimentos = siacTMovgestEntityToImpegnoModel(dtos, movimentos,ottimizzazioneDto,ottimizzatoCompletamenteDaChiamante);
		return (Accertamento) movimentos.get(0);
	}
	
	
	public static Accertamento siacTMovgestEntityToAccertamentoModelOPT(SiacTMovgestFin dto, Accertamento movimentoGestione,
			OttimizzazioneMovGestDto ottimizzazioneDto) {
		List<SiacTMovgestFin> dtos = new ArrayList<SiacTMovgestFin>();
		dtos.add(dto);
		List<MovimentoGestione> movimentos = new ArrayList<MovimentoGestione>();
		movimentos.add(movimentoGestione);
		movimentos = siacTMovgestEntityToImpegnoModelOPT(dtos, movimentos,ottimizzazioneDto);
		return (Accertamento) movimentos.get(0);
	}
	
	public static List<Impegno> siacTMovgestEntityToImpegnoModelPerRicerca(List<SiacTMovgestFin> dtos, List<Impegno> movimentos) {
		String methodName = "siacTMovgestEntityToImpegnoModelPerRicerca";
		
		for(Impegno it : movimentos){
			int idIterato = it.getUid();
			for(SiacTMovgestFin itsiac : dtos){
				int idConfronto = itsiac.getMovgestId();
				if(idIterato==idConfronto){					
					// 1. ANNO_MOVIMENTO 
					// it.setAnnoMovimento(Integer.parseInt(itsiac.getMovgestAnno()));
					it.setAnnoMovimento(itsiac.getMovgestAnno());
					// END ANNO_MOVIMENTO
					
					//NUMERO
					it.setNumeroBigDecimal(itsiac.getMovgestNumero());
					
					// 2. ATTRIBUTI ESTRATTI DALLE TABELLE siac_t_movgest_ts + siac_t_movgest_ts_det
					List<SiacTMovgestTsFin> listaSiacTMovgestTs = itsiac.getSiacTMovgestTs();
					if(null!=listaSiacTMovgestTs && listaSiacTMovgestTs.size()>0){	
						for(SiacTMovgestTsFin siacTMovgestTs : listaSiacTMovgestTs){
							if(null!=siacTMovgestTs &&
							   siacTMovgestTs.getDataFineValidita()==null &&
							   CostantiFin.MOVGEST_TS_TIPO_TESTATA.equalsIgnoreCase(siacTMovgestTs.getSiacDMovgestTsTipo().getMovgestTsTipoCode())){
		
								//2.0.1 UID SOGGETTO ASSOCIATO
								if(siacTMovgestTs.getSiacRMovgestTsSogs() != null && !siacTMovgestTs.getSiacRMovgestTsSogs().isEmpty()){	
									
									for(SiacRMovgestTsSogFin rMovGestTsSog : siacTMovgestTs.getSiacRMovgestTsSogs()){
										if(rMovGestTsSog.getSiacTSoggetto().getDataCancellazione()==null){
											it.setSoggettoCode(rMovGestTsSog.getSiacTSoggetto().getSoggettoCode());
											break;
										}
									}
								}
		
								// classe soggetto se presente
								if(siacTMovgestTs.getSiacRMovgestTsSogclasses()!=null && !siacTMovgestTs.getSiacRMovgestTsSogclasses().isEmpty()){
									// sempre unico record
									if(siacTMovgestTs.getSiacRMovgestTsSogclasses().get(0).getDataCancellazione()==null){
										if(null!=siacTMovgestTs.getSiacRMovgestTsSogclasses().get(0).getSiacDSoggettoClasse()){
											ClasseSoggetto classeSogg = new ClasseSoggetto();
											classeSogg.setCodice(siacTMovgestTs.getSiacRMovgestTsSogclasses().get(0).getSiacDSoggettoClasse().getSoggettoClasseCode());
											classeSogg.setDescrizione(siacTMovgestTs.getSiacRMovgestTsSogclasses().get(0).getSiacDSoggettoClasse().getSoggettoClasseDesc());
											it.setClasseSoggetto(classeSogg);
										}
									}
								}
								
								// 2.1 UTENTE_CREAZIONE + DATA_EMISSIONE
								it.setUtenteCreazione(siacTMovgestTs.getLoginCreazione());
								it.setDataEmissione(siacTMovgestTs.getDataCreazione());
								// END UTENTE_CREAZIONE + DATA_EMISSIONE
								
								// 2.2 UTENTE_MODIFICA + DATA_MODIFICA
								it.setUtenteModifica(siacTMovgestTs.getLoginModifica());
								it.setDataModifica(siacTMovgestTs.getDataModifica());
								// END UTENTE_MODIFICA + DATA_MODIFICA

								// 2.3 UTENTE_CANCELLAZIONE + DATA_CANCELLAZIONE
								it.setUtenteCancellazione(siacTMovgestTs.getLoginCancellazione());
								it.setDataAnnullamento(siacTMovgestTs.getDataCancellazione());
								// END UTENTE_CANCELLAZIONE + DATA_CANCELLAZIONE								

								// 2.4 DATA_SCADENZA
								it.setDataScadenza(siacTMovgestTs.getMovgestTsScadenzaData());
								// END DATA_SCADENZA

								// 2.5 IMPORTO_INIZIALE e IMPORTO_ATTUALE
								it = setImporti(it, siacTMovgestTs);
								// END IMPORTO_INIZIALE e IMPORTO_ATTUALE
		
								// 2.6 DESCRIZONE
								it.setDescrizione(siacTMovgestTs.getMovgestTsDesc());
								// END DESCRIZONE
								
								// 2.7. STATO_OPERATIVO_MOVIMENTO_GESTIONE_SPESA	
								setStatoOperativoMovimentoGestione(siacTMovgestTs, it, null, false);
								// END STATO_OPERATIVO_MOVIMENTO_GESTIONE_SPESA
								
								//Setto id Provvedimento
								if(siacTMovgestTs.getSiacRMovgestTsAttoAmms() != null && siacTMovgestTs.getSiacRMovgestTsAttoAmms().size() > 0){
									for(SiacRMovgestTsAttoAmmFin amm : siacTMovgestTs.getSiacRMovgestTsAttoAmms()){
										if(amm.getSiacTAttoAmm() != null){
											AttoAmministrativo prov = new AttoAmministrativo();
											prov.setUid(amm.getSiacTAttoAmm().getAttoammId());
											it.setAttoAmministrativo(prov);
										}
									}
								}
								
								
								List<SiacRMovgestClassFin> listaSiacRMovgestClass = siacTMovgestTs.getSiacRMovgestClasses();
								List<SiacRMovgestTsAttrFin> listaSiacRMovgestTsAttr = siacTMovgestTs.getSiacRMovgestTsAttrs();
								//DATI TRANSAZIONE ELEMENTARE:
								it = (Impegno) TransazioneElementareEntityToModelConverter.
										convertiDatiTransazioneElementare(it, listaSiacRMovgestClass,listaSiacRMovgestTsAttr);
								//END DATI TRANSAZIONE ELEMENTARE
								
								//7. SIOPE PLUS
								settaCampiSiopePlus(siacTMovgestTs, it);
								//  END SIOPE PLUS
								
							}
						}
					}
					// END ATTRIBUTI ESTRATTI DALLE TABELLE siac_t_movgest_ts + siac_t_movgest_ts_det
					
					//Setto id capitolo
					if(itsiac.getSiacRMovgestBilElems() != null && itsiac.getSiacRMovgestBilElems().size() > 0){
						for(SiacRMovgestBilElemFin elemR : itsiac.getSiacRMovgestBilElems()){
							if(elemR.getSiacTBilElem() != null){
								CapitoloUscitaGestione cap = new CapitoloUscitaGestione();
								cap.setUid(elemR.getSiacTBilElem().getElemId());
								it.setCapitoloUscitaGestione(cap);
							}
						}
					}
					
					
					// 3. ALTRI POSSIBILI CAMPI
					// END ALTRI POSSIBILI CAMPI	

					break;
				}
			}
		}
		return movimentos;
	}
	
	/**
	 * Mappa l'entuty in input
	 * @param dtos
	 * @param movimentos
	 * @return
	 */
	public static List<Accertamento> siacTMovgestEntityToAccertamentoModelPerRicerca(List<SiacTMovgestFin> dtos, List<Accertamento> movimentos) {
		for(Accertamento it : movimentos){
			int idIterato = it.getUid();
			for(SiacTMovgestFin itsiac : dtos){
				int idConfronto = itsiac.getMovgestId();
				if(idIterato==idConfronto){					
					// 1. ANNO_MOVIMENTO 
					// it.setAnnoMovimento(Integer.parseInt(itsiac.getMovgestAnno()));
					it.setAnnoMovimento(itsiac.getMovgestAnno());
					// END ANNO_MOVIMENTO
					
					//NUMERO
					it.setNumeroBigDecimal(itsiac.getMovgestNumero());
					
					// 2. ATTRIBUTI ESTRATTI DALLE TABELLE siac_t_movgest_ts + siac_t_movgest_ts_det
					List<SiacTMovgestTsFin> listaSiacTMovgestTs = itsiac.getSiacTMovgestTs();
					if(null!=listaSiacTMovgestTs && listaSiacTMovgestTs.size()>0){	
						for(SiacTMovgestTsFin siacTMovgestTs : listaSiacTMovgestTs){
							if(null!=siacTMovgestTs &&
							   siacTMovgestTs.getDataFineValidita()==null &&
							   CostantiFin.MOVGEST_TS_TIPO_TESTATA.equalsIgnoreCase(siacTMovgestTs.getSiacDMovgestTsTipo().getMovgestTsTipoCode())){
		
								//2.0.1 UID SOGGETTO ASSOCIATO
								if(siacTMovgestTs.getSiacRMovgestTsSogs() != null && !siacTMovgestTs.getSiacRMovgestTsSogs().isEmpty()){	log.debug(""," - entro nel metodo aggiorna!!?? FFF");
									for(SiacRMovgestTsSogFin rMovGestTsSog : siacTMovgestTs.getSiacRMovgestTsSogs()){
										it.setSoggettoCode(rMovGestTsSog.getSiacTSoggetto().getSoggettoCode());
										break;
									}
								}
								
								// classe soggetto se presente
								if(siacTMovgestTs.getSiacRMovgestTsSogclasses()!=null && !siacTMovgestTs.getSiacRMovgestTsSogclasses().isEmpty()){
									// sempre unico record
									if(null!=siacTMovgestTs.getSiacRMovgestTsSogclasses().get(0).getSiacDSoggettoClasse()){
										ClasseSoggetto classeSogg = new ClasseSoggetto();
										classeSogg.setCodice(siacTMovgestTs.getSiacRMovgestTsSogclasses().get(0).getSiacDSoggettoClasse().getSoggettoClasseCode());
										classeSogg.setDescrizione(siacTMovgestTs.getSiacRMovgestTsSogclasses().get(0).getSiacDSoggettoClasse().getSoggettoClasseDesc());
										it.setClasseSoggetto(classeSogg);
									}
								}
		
								// 2.1 UTENTE_CREAZIONE + DATA_EMISSIONE
								it.setUtenteCreazione(siacTMovgestTs.getLoginCreazione());
								it.setDataEmissione(siacTMovgestTs.getDataCreazione());
								// END UTENTE_CREAZIONE + DATA_EMISSIONE
								
								// 2.2 UTENTE_MODIFICA + DATA_MODIFICA
								it.setUtenteModifica(siacTMovgestTs.getLoginModifica());
								it.setDataModifica(siacTMovgestTs.getDataModifica());
								// END UTENTE_MODIFICA + DATA_MODIFICA

								// 2.3 UTENTE_CANCELLAZIONE + DATA_CANCELLAZIONE
								it.setUtenteCancellazione(siacTMovgestTs.getLoginCancellazione());
								it.setDataAnnullamento(siacTMovgestTs.getDataCancellazione());
								// END UTENTE_CANCELLAZIONE + DATA_CANCELLAZIONE								

								// 2.4 DATA_SCADENZA
								it.setDataScadenza(siacTMovgestTs.getMovgestTsScadenzaData());
								// END DATA_SCADENZA

								// 2.5 IMPORTO_INIZIALE e IMPORTO_ATTUALE
								it = setImporti(it, siacTMovgestTs);
								// END IMPORTO_INIZIALE e IMPORTO_ATTUALE
		
								// 2.6 DESCRIZONE
								it.setDescrizione(siacTMovgestTs.getMovgestTsDesc());
								// END DESCRIZONE
								
								// 2.7. STATO_OPERATIVO_MOVIMENTO_GESTIONE_SPESA	
								setStatoOperativoMovimentoGestione(siacTMovgestTs, it, null, false);
								// END STATO_OPERATIVO_MOVIMENTO_GESTIONE_SPESA
								
								
								//Gestione Provvedimento
								if(siacTMovgestTs.getSiacRMovgestTsAttoAmms() != null && siacTMovgestTs.getSiacRMovgestTsAttoAmms().size() > 0){
									for(SiacRMovgestTsAttoAmmFin ammR : siacTMovgestTs.getSiacRMovgestTsAttoAmms()){
										if(ammR.getSiacTAttoAmm() != null){
											AttoAmministrativo atto = new AttoAmministrativo();
											atto.setUid(ammR.getSiacTAttoAmm().getUid());
											it.setAttoAmministrativo(atto);
										}
									}
								}
								
								List<SiacRMovgestClassFin> listaSiacRMovgestClass = siacTMovgestTs.getSiacRMovgestClasses();
								List<SiacRMovgestTsAttrFin> listaSiacRMovgestTsAttr = siacTMovgestTs.getSiacRMovgestTsAttrs();
								//DATI TRANSAZIONE ELEMENTARE:
								it = (Accertamento) TransazioneElementareEntityToModelConverter.
										convertiDatiTransazioneElementare(it, listaSiacRMovgestClass,listaSiacRMovgestTsAttr);
								//END DATI TRANSAZIONE ELEMENTARE
								
								//7. SIOPE PLUS
								settaCampiSiopePlus(siacTMovgestTs, it);
								//  END SIOPE PLUS

							}
						}
					}
					// END ATTRIBUTI ESTRATTI DALLE TABELLE siac_t_movgest_ts + siac_t_movgest_ts_det
					
					// 3. ALTRI POSSIBILI CAMPI
					// disponibilita ad utilizzare
					if(itsiac.getSiacTMovgestTs()!=null && !itsiac.getSiacTMovgestTs().isEmpty()){
						DisponibilitaMovimentoGestioneContainer disponibilitaUtilizzare = calcolaDisponibilitaAUtilizzare(itsiac.getSiacTMovgestTs().get(0));
						it.setDisponibilitaUtilizzare(disponibilitaUtilizzare.getDisponibilita());
						// SIAC-6695
						it.setMotivazioneDisponibilitaUtilizzare(disponibilitaUtilizzare.getMotivazione());
					}
					// END ALTRI POSSIBILI CAMPI	

					//Gestione capitolo entrata
					if(itsiac.getSiacRMovgestBilElems() != null && itsiac.getSiacRMovgestBilElems().size() > 0){
						for(SiacRMovgestBilElemFin bilE : itsiac.getSiacRMovgestBilElems()){
							if(bilE.getSiacTBilElem() != null){
								CapitoloEntrataGestione capitolo = new CapitoloEntrataGestione();
								capitolo.setUid(bilE.getSiacTBilElem().getUid());
								it.setCapitoloEntrataGestione(capitolo);
							}
						}
					}
					
					
					break;
				}
			}
		}
		return movimentos;
	}
	
	
	private static DisponibilitaMovimentoGestioneContainer calcolaDisponibilitaAUtilizzare(SiacTMovgestTsFin siacTMovgestTs){		
		//
		BigDecimal risultato = BigDecimal.ZERO;
		BigDecimal utilizzabile = BigDecimal.ZERO;
		BigDecimal totQuoteVincoli = BigDecimal.ZERO;
		
		if(siacTMovgestTs.getSiacTMovgestTsDets()!=null && !siacTMovgestTs.getSiacTMovgestTsDets().isEmpty()){
			
			for (SiacTMovgestTsDetFin tsDet : siacTMovgestTs.getSiacTMovgestTsDets()) {
				if(tsDet.getDataCancellazione()==null &&
					tsDet.getDataFineValidita()==null &&	
					tsDet.getSiacDMovgestTsDetTipo().getMovgestTsDetTipoCode().equals(CostantiFin.MOVGEST_TS_DET_TIPO_UTILIZZABILE)){
					
					// calcolo utilizzabile
					utilizzabile = utilizzabile.add(tsDet.getMovgestTsDetImporto());
				}
			}
		}
		
		// ora sulle relazioni vincoli
		if(siacTMovgestTs.getSiacRMovgestTsA()!=null && !siacTMovgestTs.getSiacRMovgestTsA().isEmpty()){
			
			for (SiacRMovgestTsFin siacRMovgestTs : siacTMovgestTs.getSiacRMovgestTsA()) {
				if(siacRMovgestTs.getDataCancellazione()==null && siacRMovgestTs.getDataFineValidita()==null)
				totQuoteVincoli = totQuoteVincoli.add(siacRMovgestTs.getMovgestTsImporto());
				
			}
			
		}
		
		risultato = utilizzabile.subtract(totQuoteVincoli);
		
		return new DisponibilitaMovimentoGestioneContainer(risultato, "Disponibilita' calcolata come differenza tra utilizzabile (" + utilizzabile
				+ ") e totale delle quote vincolate (" + totQuoteVincoli + ")");
	}	
	
	
	

	
	
	
	public static Soggetto setSoggettoMovimentoGestione(
			SiacTMovgestTsFin siacTMovgestTs) {
		
		Soggetto soggetto = new Soggetto();
		
		if(siacTMovgestTs.getSiacRMovgestTsSogs() != null && !siacTMovgestTs.getSiacRMovgestTsSogs().isEmpty()){
			for(SiacRMovgestTsSogFin rMovGestTsSog : siacTMovgestTs.getSiacRMovgestTsSogs()){
				if(rMovGestTsSog.getDataFineValidita()==null){			
					
					soggetto.setUid(rMovGestTsSog.getSiacTSoggetto().getUid());
					soggetto.setCodiceSoggetto(rMovGestTsSog.getSiacTSoggetto().getSoggettoCode());
					soggetto.setCodiceFiscale(rMovGestTsSog.getSiacTSoggetto().getCodiceFiscale());
					soggetto.setDenominazione(rMovGestTsSog.getSiacTSoggetto().getSoggettoDesc());
					break;
				}
			}
		}
		return soggetto;
	}

	public static List<SiacRMovgestTsAttrFin> setAttributiMovimentoGestione(
			MovimentoGestione it, SiacTMovgestTsFin siacTMovgestTs) {
		List<SiacRMovgestTsAttrFin> listaSiacRMovgestTsAttr = siacTMovgestTs.getSiacRMovgestTsAttrs();
		for(SiacRMovgestTsAttrFin siacRMovgestTsAttr : listaSiacRMovgestTsAttr){
			if(null!=siacRMovgestTsAttr && siacRMovgestTsAttr.getDataFineValidita()==null){
				String codiceAttributo = siacRMovgestTsAttr.getSiacTAttr().getAttrCode();
				AttributoMovimentoGestione attributoMovimentoGestione = CostantiFin.attributoMovimentoGestioneStringToEnum(codiceAttributo);
				switch (attributoMovimentoGestione) {
					case annoCapitoloOrigine:
						if(siacRMovgestTsAttr.getTesto() != null){
							if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
								it.setAnnoCapitoloOrigine(Integer.parseInt(siacRMovgestTsAttr.getTesto()));
							}
						}
						break;
						
					case annoOriginePlur:
						if(siacRMovgestTsAttr.getTesto() != null){
							if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
								it.setAnnoOriginePlur(Integer.parseInt(siacRMovgestTsAttr.getTesto()));
							}
						
						}										
						break;
						
					case annoRiaccertato:
						if(siacRMovgestTsAttr.getTesto() != null && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
							if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
								it.setAnnoRiaccertato(Integer.parseInt(siacRMovgestTsAttr.getTesto()));
							}
							
						}
						break;
						
					case numeroArticoloOrigine:
						if(siacRMovgestTsAttr.getTesto() != null){
							if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
								it.setNumeroArticoloOrigine(Integer.parseInt(siacRMovgestTsAttr.getTesto()));	
							}
							
						}									
						break;
						
					case flagDaRiaccertamento:
						if(null!=siacRMovgestTsAttr.getBoolean_()){
							// if("S".equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_()) || "s".equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_())){
							if(CostantiFin.TRUE.equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_())){
								it.setFlagDaRiaccertamento(true);
							}else {
								it.setFlagDaRiaccertamento(false);
							}
						}												
						break;
						
					//SIAC-6997
					case flagDaReanno:
						if(null!=siacRMovgestTsAttr.getBoolean_()){
							// if("S".equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_()) || "s".equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_())){
							if(CostantiFin.TRUE.equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_())){
								it.setFlagDaReanno(true);
							}else {
								it.setFlagDaReanno(false);
							}
						}												
						break;
						
					case flagSoggettoDurc:
						if(null!=siacRMovgestTsAttr.getBoolean_()){
							it.setFlagSoggettoDurc(CostantiFin.TRUE.equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_()));
						}												
						break;
						
					case FlagCollegamentoAccertamentoFattura:
						if(null!=siacRMovgestTsAttr.getBoolean_()){
							((AccertamentoAbstract) it).setFlagFattura(CostantiFin.TRUE.equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_()));
						}												
						break;
						
					case FlagCollegamentoAccertamentoCorrispettivo:
						if(null!=siacRMovgestTsAttr.getBoolean_()){
							((AccertamentoAbstract) it).setFlagCorrispettivo(CostantiFin.TRUE.equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_()));
						}												
						break;
						
					case FlagAttivaGsa:
						if(null!=siacRMovgestTsAttr.getBoolean_()){
							if(CostantiFin.TRUE.equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_())){
								it.setFlagAttivaGsa(true);
							}else {
								it.setFlagAttivaGsa(false);
							}
						}												
						break;
						
					case numeroCapitoloOrigine:
						if(siacRMovgestTsAttr.getTesto() != null){
							if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
								it.setNumeroCapitoloOrigine(Integer.parseInt(siacRMovgestTsAttr.getTesto()));	
							}
						}						
						break;
						
					case numeroOriginePlur:
						if(siacRMovgestTsAttr.getTesto() != null){
							if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
								it.setNumeroOriginePlur(new BigDecimal(Integer.parseInt(siacRMovgestTsAttr.getTesto())));
							}
							
						}
						break;
						
					case numeroRiaccertato:
						if(siacRMovgestTsAttr.getTesto() != null){
							if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
								it.setNumeroRiaccertato(new BigDecimal(Integer.parseInt(siacRMovgestTsAttr.getTesto())));
							}
						}
						break;
						
					case numeroUEBOrigine:
						if(siacRMovgestTsAttr.getTesto() != null){
							if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
								it.setNumeroUEBOrigine(Integer.parseInt(siacRMovgestTsAttr.getTesto()));
							}
						
						} 
						break;
					
					case annoFinanziamento:
						if(it instanceof Impegno){
							if(siacRMovgestTsAttr.getTesto() != null){
								if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
									((ImpegnoAbstract) it).setAnnoFinanziamento(Integer.parseInt(siacRMovgestTsAttr.getTesto()));
								}
							}
						}
						break;
						
					case cig:
						if(it instanceof Impegno){
							if(siacRMovgestTsAttr.getTesto() != null){
								if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
									((ImpegnoAbstract) it).setCig(siacRMovgestTsAttr.getTesto());
								}
							}
						}
						break;
						
					case numeroAccFinanziamento:
						if(it instanceof Impegno){
							if(siacRMovgestTsAttr.getTesto() != null){
								if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
									((ImpegnoAbstract) it).setNumeroAccFinanziamento(Integer.parseInt(siacRMovgestTsAttr.getTesto()));
								}
							}
						}
						break;
						
					case validato:
						if(null!=siacRMovgestTsAttr.getBoolean_()){
							// if("S".equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_()) || "s".equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_())){
							if(CostantiFin.TRUE.equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_())){
								it.setValidato(true);
							}else {
								it.setValidato(false);
							}
						}
						break;

					default:
						break;
				}
			}									
		}
		return listaSiacRMovgestTsAttr;
	}

	public static void setStatoOperativoMovimentoGestione(
			MovimentoGestione it, SiacTMovgestTsFin siacTMovgestTs) {
		List<SiacRMovgestTsStatoFin> listaSiacRMovgestTsStato = siacTMovgestTs.getSiacRMovgestTsStatos();
		
		if(null!=listaSiacRMovgestTsStato && listaSiacRMovgestTsStato.size() > 0){
			for(SiacRMovgestTsStatoFin siacRMovgestTsStato : listaSiacRMovgestTsStato){
				if(null!=siacRMovgestTsStato && siacRMovgestTsStato.getDataFineValidita()==null){
					if(it instanceof Impegno){
						((ImpegnoAbstract) it).setStatoOperativoMovimentoGestioneSpesa(siacRMovgestTsStato.getSiacDMovgestStato().getMovgestStatoCode());
						((ImpegnoAbstract) it).setDescrizioneStatoOperativoMovimentoGestioneSpesa(siacRMovgestTsStato.getSiacDMovgestStato().getMovgestStatoDesc());
						((ImpegnoAbstract) it).setDataStatoOperativoMovimentoGestioneSpesa(siacRMovgestTsStato.getDataInizioValidita());
					} else if(it instanceof Accertamento){
						((AccertamentoAbstract) it).setStatoOperativoMovimentoGestioneEntrata(siacRMovgestTsStato.getSiacDMovgestStato().getMovgestStatoCode());
						((AccertamentoAbstract) it).setDescrizioneStatoOperativoMovimentoGestioneEntrata(siacRMovgestTsStato.getSiacDMovgestStato().getMovgestStatoDesc());
						((AccertamentoAbstract) it).setDataStatoOperativoMovimentoGestioneEntrata(siacRMovgestTsStato.getDataInizioValidita());
					}
				}									
			}
		
		}
	}
	
	
	public static List<SiacRMovgestClassFin> setTipoImpegno(
			MovimentoGestione it, SiacTMovgestTsFin siacTMovgestTs) {
		List<SiacRMovgestClassFin> listaSiacRMovgestClass = siacTMovgestTs.getSiacRMovgestClasses();
		for(SiacRMovgestClassFin siacRMovgestClass : listaSiacRMovgestClass){
			if(null!=siacRMovgestClass && siacRMovgestClass.getDataFineValidita()==null){
				if(siacRMovgestClass.getSiacTClass().getSiacDClassTipo().getClassifTipoCode().equalsIgnoreCase(CostantiFin.D_CLASS_TIPO_TIPO_IMPEGNO)){
					// TIPO
					if(it instanceof Impegno){
						ClassificatoreGenerico tipoImpegno = new ClassificatoreGenerico();
						tipoImpegno.setUid(siacRMovgestClass.getSiacTClass().getClassifId());
						tipoImpegno.setCodice(siacRMovgestClass.getSiacTClass().getClassifCode());
						tipoImpegno.setDescrizione(siacRMovgestClass.getSiacTClass().getClassifDesc());
						((ImpegnoAbstract) it).setTipoImpegno(tipoImpegno);
					} 
				}
			}									
		}
		return listaSiacRMovgestClass;
	}
	
	/**
	 * wrapper per retro-compatibilita'
	 * @param dtos
	 * @param movimentos
	 * @return
	 */
	public static List<MovimentoGestione> siacTMovgestEntityToImpegnoModel(List<SiacTMovgestFin> dtos, List<MovimentoGestione> movimentos) {
		return siacTMovgestEntityToImpegnoModel(dtos, movimentos,null);
	}
	
	/**
	 * 
	 * Wrapper di retrocompatibilita'
	 * 
	 * @param dtos
	 * @param movimentos
	 * @param ottimizzazioneDto
	 * @return
	 */
	public static List<MovimentoGestione> siacTMovgestEntityToImpegnoModel(List<SiacTMovgestFin> dtos,
			List<MovimentoGestione> movimentos, OttimizzazioneMovGestDto ottimizzazioneDto) {
		boolean ottimizzatoCompletamenteDaChiamante = false;
		return siacTMovgestEntityToImpegnoModel(dtos, movimentos, ottimizzazioneDto, ottimizzatoCompletamenteDaChiamante);
	}
	
	/**
	 * Mappa il movimento gestione (con tutti i suoi attibuti)
	 * usato principalmente nelle ricerche per chiave
	 * @param dtos
	 * @param movimentos
	 * @return
	 */
	public static List<MovimentoGestione> siacTMovgestEntityToImpegnoModel(List<SiacTMovgestFin> dtos,
			List<MovimentoGestione> movimentos, OttimizzazioneMovGestDto ottimizzazioneDto, boolean ottimizzatoCompletamenteDaChiamante) {
		
		//per non ripetere sempre il controllo di null pointer sull'oggetto ottimizzazioneDto intero ma solo sui sui contenuti:
		if(ottimizzazioneDto == null){
			ottimizzazioneDto = new OttimizzazioneMovGestDto();
		}
		//
		
		for(MovimentoGestione movimentoGestione : movimentos){
			int idIterato = movimentoGestione.getUid();
			for(SiacTMovgestFin siacTMovgest : dtos){
				int idConfronto = siacTMovgest.getMovgestId();
				if(idIterato == idConfronto){					
					// 1. ANNO_MOVIMENTO 
					// it.setAnnoMovimento(Integer.parseInt(itsiac.getMovgestAnno()));
					movimentoGestione.setAnnoMovimento(siacTMovgest.getMovgestAnno());
					// END ANNO_MOVIMENTO
					
					// set Parere finanziario
					// verificare se serve qui o se lo mappa gia dozer nella chiamata precedente a questa operazione
					movimentoGestione.setParereFinanziario(siacTMovgest.getParereFinanziario());
					
					// 2. ATTRIBUTI ESTRATTI DALLE TABELLE siac_t_movgest_ts + siac_t_movgest_ts_det
					List<SiacTMovgestTsFin> listaSiacTMovgestTs = siacTMovgest.getSiacTMovgestTs();
					if(listaSiacTMovgestTs != null && listaSiacTMovgestTs.size()>0){	
						for(SiacTMovgestTsFin siacTMovgestTs : listaSiacTMovgestTs){
							if(siacTMovgestTs != null &&
							   siacTMovgestTs.getDataFineValidita() == null &&
							   CostantiFin.MOVGEST_TS_TIPO_TESTATA.equalsIgnoreCase(siacTMovgestTs.getSiacDMovgestTsTipo().getMovgestTsTipoCode())){
								
								Integer movgestTsId = siacTMovgestTs.getMovgestTsId();
								
								//2.0.1 UID SOGGETTO ASSOCIATO
								settaDatiMinimiSoggettoAssociato(siacTMovgestTs, movimentoGestione, ottimizzazioneDto, ottimizzatoCompletamenteDaChiamante);
		
								// classe soggetto se presente
								if(siacTMovgestTs.getSiacRMovgestTsSogclasses() != null && !siacTMovgestTs.getSiacRMovgestTsSogclasses().isEmpty()){
									// sempre unico record
									if(siacTMovgestTs.getSiacRMovgestTsSogclasses().get(0).getSiacDSoggettoClasse() != null 
											//SIAC-7619 controllo che la classe sia valida
											&& siacTMovgestTs.getSiacRMovgestTsSogclasses().get(0).isEntitaValida()
											&& siacTMovgestTs.getSiacRMovgestTsSogclasses().get(0).getSiacDSoggettoClasse().isEntitaValida()){
										ClasseSoggetto classeSogg = new ClasseSoggetto();
										classeSogg.setCodice(siacTMovgestTs.getSiacRMovgestTsSogclasses().get(0).getSiacDSoggettoClasse().getSoggettoClasseCode());
										classeSogg.setDescrizione(siacTMovgestTs.getSiacRMovgestTsSogclasses().get(0).getSiacDSoggettoClasse().getSoggettoClasseDesc());
										movimentoGestione.setClasseSoggetto(classeSogg);
									}
								}
								
								// 2.1 UTENTE_CREAZIONE + DATA_EMISSIONE
								movimentoGestione.setUtenteCreazione(siacTMovgestTs.getLoginCreazione());
								movimentoGestione.setDataEmissione(siacTMovgestTs.getDataCreazione());
								// support data emissione --> con questa modalita arriva correttamente al front end
								movimentoGestione.setDataEmissioneSupport(siacTMovgestTs.getDataCreazione());
								// END UTENTE_CREAZIONE + DATA_EMISSIONE
								
								// 2.2 UTENTE_MODIFICA + DATA_MODIFICA
								movimentoGestione.setUtenteModifica(siacTMovgestTs.getLoginModifica());
								movimentoGestione.setDataModifica(siacTMovgestTs.getDataModifica());
								// END UTENTE_MODIFICA + DATA_MODIFICA

								// 2.3 UTENTE_CANCELLAZIONE + DATA_CANCELLAZIONE
								movimentoGestione.setUtenteCancellazione(siacTMovgestTs.getLoginCancellazione());
								movimentoGestione.setDataAnnullamento(siacTMovgestTs.getDataCancellazione());
								// END UTENTE_CANCELLAZIONE + DATA_CANCELLAZIONE								

								// 2.4 DATA_SCADENZA
								movimentoGestione.setDataScadenza(siacTMovgestTs.getMovgestTsScadenzaData());
								// END DATA_SCADENZA

								
								
								// 2.5 IMPORTO_INIZIALE e IMPORTO_ATTUALE e UTILIZZABILE
								List<SiacTMovgestTsDetFin> listaSiacTMovgestTsDet = ottimizzazioneDto.filtraSiacTMovgestTsDetByMovgestTs(movgestTsId);
								if(listaSiacTMovgestTsDet != null && listaSiacTMovgestTsDet.size()>0){
									//caricamento ottimizzato
									setImporti(movimentoGestione, listaSiacTMovgestTsDet);
								} else {
									//caricamento classico
									setImporti(movimentoGestione, siacTMovgestTs);
								}
								// END IMPORTO_INIZIALE e IMPORTO_ATTUALE
								
		
								// 2.6 DESCRIZONE
								movimentoGestione.setDescrizione(siacTMovgestTs.getMovgestTsDesc());
								// END DESCRIZONE
								
								
								// 2.7. STATO_OPERATIVO_MOVIMENTO_GESTIONE_SPESA	
								setStatoOperativoMovimentoGestione(siacTMovgestTs, movimentoGestione, ottimizzazioneDto, ottimizzatoCompletamenteDaChiamante);
								// END STATO_OPERATIVO_MOVIMENTO_GESTIONE_SPESA
								
 
								// 2.8 ATTRIBUTI ESTRATTI DALLE TABELLE siac_r_movgest_ts_attr + siac_t_attr
								List<SiacRMovgestTsAttrFin> listaSiacRMovgestTsAttr  = ottimizzazioneDto.filtraSiacRMovgestTsAttrByMovgestTs(movgestTsId);
								if(StringUtilsFin.isEmpty(listaSiacRMovgestTsAttr) && !ottimizzatoCompletamenteDaChiamante){
									//CARICAMENTO CLASSICO
									listaSiacRMovgestTsAttr = siacTMovgestTs.getSiacRMovgestTsAttrs();
								}
								
								boolean frazionabileValorizzato = false;//gestione vecchi impegni creati prima dell'introduzione di questo flag
								
								for(SiacRMovgestTsAttrFin siacRMovgestTsAttr : listaSiacRMovgestTsAttr){
									if(null!=siacRMovgestTsAttr && siacRMovgestTsAttr.getDataFineValidita()==null){
										String codiceAttributo = siacRMovgestTsAttr.getSiacTAttr().getAttrCode();
										AttributoMovimentoGestione attributoMovimentoGestione = CostantiFin.attributoMovimentoGestioneStringToEnum(codiceAttributo);
										switch (attributoMovimentoGestione) {
										
											case annoScritturaEconomicoPatrimoniale:
												if(siacRMovgestTsAttr.getTesto() != null){
													if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
														movimentoGestione.setAnnoScritturaEconomicoPatrimoniale(Integer.parseInt(siacRMovgestTsAttr.getTesto()));
													}
												}
												break;
										
											case annoCapitoloOrigine:
												if(siacRMovgestTsAttr.getTesto() != null){
													if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
														movimentoGestione.setAnnoCapitoloOrigine(Integer.parseInt(siacRMovgestTsAttr.getTesto()));
													}
												}
												break;
												
											case annoOriginePlur:
												if(siacRMovgestTsAttr.getTesto() != null){
													if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
														movimentoGestione.setAnnoOriginePlur(Integer.parseInt(siacRMovgestTsAttr.getTesto()));
													}
												
												}										
												break;
												
											case annoRiaccertato:
												if(siacRMovgestTsAttr.getTesto() != null && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
													if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
														movimentoGestione.setAnnoRiaccertato(Integer.parseInt(siacRMovgestTsAttr.getTesto()));
													}
													
												}
												break;
												
											case numeroArticoloOrigine:
												if(siacRMovgestTsAttr.getTesto() != null){
													if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
														movimentoGestione.setNumeroArticoloOrigine(Integer.parseInt(siacRMovgestTsAttr.getTesto()));	
													}
													
												}									
												break;
												
											case flagDaRiaccertamento:
												if(null!=siacRMovgestTsAttr.getBoolean_()){
													// if("S".equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_()) || "s".equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_())){
													if(CostantiFin.TRUE.equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_())){
														movimentoGestione.setFlagDaRiaccertamento(true);
													}else {
														movimentoGestione.setFlagDaRiaccertamento(false);
													}
												}												
												break;
												
											//SIAC-6997
											case flagDaReanno:
												if(null!=siacRMovgestTsAttr.getBoolean_()){
													// if("S".equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_()) || "s".equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_())){
													if(CostantiFin.TRUE.equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_())){
														movimentoGestione.setFlagDaReanno(true);
													}else {
														movimentoGestione.setFlagDaReanno(false);
													}
												}												
												break;
												
											case flagSoggettoDurc:
												if(null!=siacRMovgestTsAttr.getBoolean_()){
													movimentoGestione.setFlagSoggettoDurc(CostantiFin.TRUE.equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_()));
												}												
												break;
												
											case FlagCollegamentoAccertamentoFattura:
												if(null!=siacRMovgestTsAttr.getBoolean_()){
													((AccertamentoAbstract) movimentoGestione).setFlagFattura(CostantiFin.TRUE.equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_()));
												}												
												break;
												
											case FlagCollegamentoAccertamentoCorrispettivo:
												if(null!=siacRMovgestTsAttr.getBoolean_()){
													((AccertamentoAbstract) movimentoGestione).setFlagCorrispettivo(CostantiFin.TRUE.equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_()));
												}												
												break;
												
											case FlagAttivaGsa:
												if(null!=siacRMovgestTsAttr.getBoolean_()){
													if(CostantiFin.TRUE.equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_())){
														movimentoGestione.setFlagAttivaGsa(true);
													}else {
														movimentoGestione.setFlagAttivaGsa(false);
													}
												}												
												break;
												
											case numeroCapitoloOrigine:
												if(siacRMovgestTsAttr.getTesto() != null){
													if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
														movimentoGestione.setNumeroCapitoloOrigine(Integer.parseInt(siacRMovgestTsAttr.getTesto()));	
													}
												}						
												break;
												
											case numeroOriginePlur:
												if(siacRMovgestTsAttr.getTesto() != null){
													if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
														movimentoGestione.setNumeroOriginePlur(new BigDecimal(Integer.parseInt(siacRMovgestTsAttr.getTesto())));
													}
													
												}
												break;
												
											case numeroRiaccertato:
												if(siacRMovgestTsAttr.getTesto() != null){
													if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
														movimentoGestione.setNumeroRiaccertato(new BigDecimal(Integer.parseInt(siacRMovgestTsAttr.getTesto())));
													}
												}
												break;
												
											case numeroUEBOrigine:
												if(siacRMovgestTsAttr.getTesto() != null){
													if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
														movimentoGestione.setNumeroUEBOrigine(Integer.parseInt(siacRMovgestTsAttr.getTesto()));
													}
												
												} 
												break;
											
											case annoFinanziamento:
												if(movimentoGestione instanceof Impegno){
													if(siacRMovgestTsAttr.getTesto() != null){
														if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
															((ImpegnoAbstract) movimentoGestione).setAnnoFinanziamento(Integer.parseInt(siacRMovgestTsAttr.getTesto()));
														}
													}
												}
												break;
												
											case cig:
												if(movimentoGestione instanceof Impegno){
													if(siacRMovgestTsAttr.getTesto() != null){
														if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
															((ImpegnoAbstract) movimentoGestione).setCig(siacRMovgestTsAttr.getTesto());
														}
													}
												}
												break;
												
											case numeroAccFinanziamento:
												if(movimentoGestione instanceof Impegno){
													if(siacRMovgestTsAttr.getTesto() != null){
														if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
															((ImpegnoAbstract) movimentoGestione).setNumeroAccFinanziamento(Integer.parseInt(siacRMovgestTsAttr.getTesto()));
														}
													}
												}
												break;
												
											case validato:
												if(null!=siacRMovgestTsAttr.getBoolean_()){
													// if("S".equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_()) || "s".equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_())){
													if(CostantiFin.TRUE.equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_())){
														movimentoGestione.setValidato(true);
													}else {
														movimentoGestione.setValidato(false);
													}
												}
												break;
												
											case flagPrenotazione:
												if(null!=siacRMovgestTsAttr.getBoolean_()){
													if(CostantiFin.TRUE.equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_())){
														((ImpegnoAbstract) movimentoGestione).setFlagPrenotazione(true);
													}else {
														((ImpegnoAbstract) movimentoGestione).setFlagPrenotazione(false);
													}
												}												
												break;	
												
											case flagPrenotazioneLiquidabile:
												if(null!=siacRMovgestTsAttr.getBoolean_()){
													if(CostantiFin.TRUE.equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_())){
														((ImpegnoAbstract) movimentoGestione).setFlagPrenotazioneLiquidabile(true);
													}else {
														((ImpegnoAbstract) movimentoGestione).setFlagPrenotazioneLiquidabile(false);
													}
												}												
												break;		
												
											case flagFrazionabile:
												frazionabileValorizzato = true;
												if(null!=siacRMovgestTsAttr.getBoolean_()){
													if(CostantiFin.TRUE.equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_())){
														((ImpegnoAbstract) movimentoGestione).setFlagFrazionabile(true);
													}else {
														((ImpegnoAbstract) movimentoGestione).setFlagFrazionabile(false);
													}
												}												
												break;	
												
											case flagCassaEconomale:
												if(null!=siacRMovgestTsAttr.getBoolean_()){
													if(CostantiFin.TRUE.equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_())){
														((ImpegnoAbstract) movimentoGestione).setFlagCassaEconomale(true);
													}else {
														((ImpegnoAbstract) movimentoGestione).setFlagCassaEconomale(false);
													}
												}												
												break;		
												
											case annoPrenotazioneOrigine:
												if(siacRMovgestTsAttr.getTesto() != null && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
													if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
														((Impegno)movimentoGestione).setAnnoPrenotazioneOrigine(siacRMovgestTsAttr.getTesto());
													}
													
												}											
												break;
											
											case flagSDF:
												if(null!=siacRMovgestTsAttr.getBoolean_()){
													if(CostantiFin.TRUE.equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_())){
														((ImpegnoAbstract) movimentoGestione).setFlagSDF(true);
													}else {
														((ImpegnoAbstract) movimentoGestione).setFlagSDF(false);
													}
												}												
												break;

											case codVerbaleAccertamento:
												if(siacRMovgestTsAttr.getTesto() != null && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto()) && !siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null")){
														((AccertamentoAbstract)movimentoGestione).setCodiceVerbale(siacRMovgestTsAttr.getTesto());
													
												}											
												break;

											default:
												break;
										}
									}									
								}
								// END ATTRIBUTI ESTRATTI DALLE TABELLE siac_r_movgest_ts_attr + siac_t_attr
								
								if(movimentoGestione instanceof ImpegnoAbstract){
									//gestione frazionabile per impegni vecchi:
									if(!frazionabileValorizzato){
										((ImpegnoAbstract) movimentoGestione).setFlagFrazionabile(true);//default per non settato
									}
									((ImpegnoAbstract) movimentoGestione).setFrazionabileValorizzato(frazionabileValorizzato);//se al front end serve sapere se e' settato o meno
									//
								}
								
								// 3 CLASSE_SOGGETTO
								settaDatiClasseSoggetto(siacTMovgestTs, movimentoGestione, ottimizzazioneDto, ottimizzatoCompletamenteDaChiamante);

								// 4 tipoImpegno
								List<SiacRMovgestClassFin> listaSiacRMovgestClass = ottimizzazioneDto.filtraSiacRMovgestClassByMovgestTs(movgestTsId);
								if(StringUtilsFin.isEmpty(listaSiacRMovgestClass) && !ottimizzatoCompletamenteDaChiamante){
									//CARICAMENTO CLASSICO
									listaSiacRMovgestClass = siacTMovgestTs.getSiacRMovgestClasses();
								}
								
								for(SiacRMovgestClassFin siacRMovgestClass : listaSiacRMovgestClass){
									if(null!=siacRMovgestClass && siacRMovgestClass.getDataFineValidita()==null){
										if(siacRMovgestClass.getSiacTClass().getSiacDClassTipo().getClassifTipoCode().equalsIgnoreCase(CostantiFin.D_CLASS_TIPO_TIPO_IMPEGNO)){
											// TIPO
											if(movimentoGestione instanceof Impegno){
												ClassificatoreGenerico tipoImpegno = new ClassificatoreGenerico();
												tipoImpegno.setUid(siacRMovgestClass.getSiacTClass().getClassifId());
												tipoImpegno.setCodice(siacRMovgestClass.getSiacTClass().getClassifCode());
												tipoImpegno.setDescrizione(siacRMovgestClass.getSiacTClass().getClassifDesc());
												((ImpegnoAbstract) movimentoGestione).setTipoImpegno(tipoImpegno);
											} 
										}
									}									
								}
								// END tipoImpegno
								
								//DATI TRANSAZIONE ELEMENTARE:
								movimentoGestione = (MovimentoGestione) TransazioneElementareEntityToModelConverter.
										convertiDatiTransazioneElementare(movimentoGestione, listaSiacRMovgestClass,listaSiacRMovgestTsAttr);
								//END DATI TRANSAZIONE ELEMENTARE
								
								// 5 PROGETTO
								List<SiacRMovgestTsProgrammaFin> listaSiacRMovgestTsProgramma = ottimizzazioneDto.filtraSiacRMovgestTsProgrammaByMovgestTs(movgestTsId);
								if(StringUtilsFin.isEmpty(listaSiacRMovgestTsProgramma) && !ottimizzatoCompletamenteDaChiamante){
									//CARICAMENTO CLASSICO
									//System.out.println("progetto! passo dal ramo classico, non cerco nei dati precaricati da picco ") ;
									listaSiacRMovgestTsProgramma = siacTMovgestTs.getSiacRMovgestTsProgrammas();
								}
								
								for(SiacRMovgestTsProgrammaFin siacRMovgestTsProgramma : listaSiacRMovgestTsProgramma){
									if(siacRMovgestTsProgramma != null && siacRMovgestTsProgramma.getDataFineValidita() == null && siacRMovgestTsProgramma.getDataCancellazione() == null){
										
										Progetto progetto = new Progetto();
										progetto.setUid(siacRMovgestTsProgramma.getSiacTProgramma().getProgrammaId());
										progetto.setCodice(siacRMovgestTsProgramma.getSiacTProgramma().getProgrammaCode());
										progetto.setDescrizione(siacRMovgestTsProgramma.getSiacTProgramma().getProgrammaDesc()); 
										
										progetto.setTipoProgetto(TipoProgetto.byCodice(siacRMovgestTsProgramma.getSiacTProgramma().getSiacDProgrammaTipo().getProgrammaTipoCode()));
										progetto.setBilancio(Bilancio.getInstanceForUid(siacRMovgestTsProgramma.getSiacTProgramma().getSiacTBil().getUid()));
										
										movimentoGestione.setProgetto(progetto);	
									}									
								}
								// END PROGETTO
								// 6. ATTO AMMINISTRATIVO
								settaDatiAttoAmministrativo(siacTMovgestTs, movimentoGestione, ottimizzazioneDto, ottimizzatoCompletamenteDaChiamante);
								// END ATTO AMMINISTRATIVO
								
								//7. SIOPE PLUS
								settaCampiSiopePlus(siacTMovgestTs, movimentoGestione);
								//  END SIOPE PLUS
								
							}
						}
					}
					// END ATTRIBUTI ESTRATTI DALLE TABELLE siac_t_movgest_ts + siac_t_movgest_ts_det
					
					// 3. CAPITOLO DI ENTRATA / USCITA
					movimentoGestione = setChiaveCapitolo(siacTMovgest, movimentoGestione);
					// END CAPITOLO DI ENTRATA / USCITA	

					// 4. ALTRI POSSIBILI CAMPI
					// END ALTRI POSSIBILI CAMPI	

					break;
				}
			}
		
		}
		return movimentos;
	}
	
	public static void settaCampiSiopePlus(SiacTMovgestTsFin siacTMovgestTs,MovimentoGestione it){
		
		//1 assenza cig:
		it.setSiopeAssenzaMotivazione(buildSiopeMotivazioneAssenzaCig(siacTMovgestTs.getSiacDSiopeAssenzaMotivazione()));
		
		//2 tipo debito fin:
		it.setSiopeTipoDebito(buildSiopeTipoDebito(siacTMovgestTs.getSiacDSiopeTipoDebitoFin()));
	}
	
	/**
	 * converte i dati della classe soggetto
	 * @param siacTMovgestTs
	 * @param it
	 * @param ottimizzazioneDto
	 * @param ottimizzatoCompletamenteDaChiamante
	 */
	public static void settaDatiClasseSoggetto(SiacTMovgestTsFin siacTMovgestTs,MovimentoGestione it,OttimizzazioneMovGestDto ottimizzazioneDto,boolean ottimizzatoCompletamenteDaChiamante){
		ClasseSoggetto classeSoggetto = new ClasseSoggetto();
		
		Integer movgestTsId = siacTMovgestTs.getMovgestTsId();
		
		List<SiacRMovgestTsSogclasseFin> listaSiacRMovgestTsSogclasse = null;
		
		//Dato che la classe puo' essere vuota occorre valutare anche ottimizzatoCompletamenteDaChiamante
		//e non basta controllare che SiacRMovgestTsSogclasseFin estratto da ottimizzazioneDto sia vuoto
		if(ottimizzazioneDto != null){
			listaSiacRMovgestTsSogclasse = ottimizzazioneDto.filtraSiacRMovgestTsSogclasseFinByMovGestTsId(movgestTsId);
		}
		
		if(StringUtilsFin.isEmpty(listaSiacRMovgestTsSogclasse) && !ottimizzatoCompletamenteDaChiamante){
			//CARICAMENTO CLASSICO
			listaSiacRMovgestTsSogclasse = siacTMovgestTs.getSiacRMovgestTsSogclasses();
		}
		
		for(SiacRMovgestTsSogclasseFin siacRMovgestTsSogclasse : listaSiacRMovgestTsSogclasse){
			//SIAC-7619 controllo anche la dataCanellazione come null
			if(siacRMovgestTsSogclasse != null && siacRMovgestTsSogclasse.getDataFineValidita() == null && siacRMovgestTsSogclasse.getDataCancellazione() == null){
				
				classeSoggetto.setCodice(siacRMovgestTsSogclasse.getSiacDSoggettoClasse().getSoggettoClasseCode());
				classeSoggetto.setDescrizione(siacRMovgestTsSogclasse.getSiacDSoggettoClasse().getSoggettoClasseDesc());
				it.setClasseSoggetto(classeSoggetto);
				break;
			}
		}
	}
	
	/**
	 * converte i dati minimi per risalire al soggetto associato
	 * @param siacTMovgestTs
	 * @param it
	 * @param ottimizzazioneDto
	 * @param ottimizzatoCompletamenteDaChiamante
	 */
	public static void settaDatiMinimiSoggettoAssociato(SiacTMovgestTsFin siacTMovgestTs,MovimentoGestione it,OttimizzazioneMovGestDto ottimizzazioneDto,boolean ottimizzatoCompletamenteDaChiamante){
		
		Integer movgestTsId = siacTMovgestTs.getMovgestTsId();
		//Dato che il soggeto puo' essere vuoto occorre valutare anche ottimizzatoCompletamenteDaChiamante
		//e non basta controllare che siacRMovgestTsSogs estratto da ottimizzazioneDto sia vuoto
		
		List<SiacRMovgestTsSogFin> siacRMovgestTsSogs = null;
		if(ottimizzazioneDto != null){
			siacRMovgestTsSogs = ottimizzazioneDto.filtraSiacRMovgestTsSogFinByMovGestTsId(movgestTsId);
		}
		
		if(StringUtilsFin.isEmpty(siacRMovgestTsSogs) && !ottimizzatoCompletamenteDaChiamante){
			//CARICAMENTO CLASSICO
			siacRMovgestTsSogs = siacTMovgestTs.getSiacRMovgestTsSogs();
		}
		
		if(siacRMovgestTsSogs != null && !siacRMovgestTsSogs.isEmpty()){
			for(SiacRMovgestTsSogFin rMovGestTsSog : siacRMovgestTsSogs){
				if(rMovGestTsSog.getDataFineValidita() == null && rMovGestTsSog.getDataCancellazione() == null){
					it.setSoggettoCode(rMovGestTsSog.getSiacTSoggetto().getSoggettoCode());
					Soggetto soggetto = new Soggetto();
					soggetto.setUid(rMovGestTsSog.getSiacTSoggetto().getUid());
					soggetto.setCodiceSoggetto(rMovGestTsSog.getSiacTSoggetto().getSoggettoCode());
					soggetto.setCodiceFiscale(rMovGestTsSog.getSiacTSoggetto().getCodiceFiscale());
					soggetto.setDenominazione(rMovGestTsSog.getSiacTSoggetto().getSoggettoDesc());
					
					it.setSoggetto(soggetto);
					
					break;
				}
			}
		}
	}
	
	/**
	 * 
	 * converte lo stato di un impegno accertamento
	 * 
	 * @param siacTMovgestTs
	 * @param it
	 * @param ottimizzazioneDto
	 * @param ottimizzatoCompletamenteDaChiamante
	 */
	public static void setStatoOperativoMovimentoGestione(SiacTMovgestTsFin siacTMovgestTs,MovimentoGestione it,OttimizzazioneMovGestDto ottimizzazioneDto,boolean ottimizzatoCompletamenteDaChiamante){
		
		Integer movgestTsId = siacTMovgestTs.getMovgestTsId();
		
		List<SiacRMovgestTsStatoFin> listaSiacRMovgestTsStato = null;
		if(ottimizzazioneDto!=null){
			listaSiacRMovgestTsStato = ottimizzazioneDto.filtraSiacRMovgestTsStatoByMovgestTs(movgestTsId);
		}
		if(StringUtilsFin.isEmpty(listaSiacRMovgestTsStato) && !ottimizzatoCompletamenteDaChiamante){
			//CARICAMENTO CLASSICO
			listaSiacRMovgestTsStato = siacTMovgestTs.getSiacRMovgestTsStatos();
		}
		
		//metodo core:
		setStatoOperativoMovimentoGestione(it, siacTMovgestTs);
		
	}
	
	public static void settaAttoAmministrativoBase(SiacTMovgestTsFin siacTMovgestTs,MovimentoGestione it,OttimizzazioneMovGestDto ottimizzazioneDto,boolean ottimizzatoCompletamenteDaChiamante){
		// MAPPO ATTO AMMINISTRATIVO
		Integer movgestTsId = siacTMovgestTs.getMovgestTsId();
		List<SiacRMovgestTsAttoAmmFin> listaSiacRMovgestTsAttoAmm = null;
		if(ottimizzazioneDto!=null){
			listaSiacRMovgestTsAttoAmm = ottimizzazioneDto.filtraSiacRMovgestTsAttoAmmByMovgestTs(movgestTsId);
		}
		
		if(StringUtilsFin.isEmpty(listaSiacRMovgestTsAttoAmm) && !ottimizzatoCompletamenteDaChiamante){
			//CARICAMENTO CLASSICO
			listaSiacRMovgestTsAttoAmm = siacTMovgestTs.getSiacRMovgestTsAttoAmms();
		}
		if(null!=listaSiacRMovgestTsAttoAmm && listaSiacRMovgestTsAttoAmm.size() > 0){
			//estraggo l'unico record valido:
			SiacRMovgestTsAttoAmmFin siacRMovgestTsAttoAmm = DatiOperazioneUtil.getValido(listaSiacRMovgestTsAttoAmm, null);
			if(null!= siacRMovgestTsAttoAmm){
				EntityToModelConverter.settaAttoAmministrativoBase(siacRMovgestTsAttoAmm.getSiacTAttoAmm(), it);
			}
		}
	}
	
	public static void settaDatiAttoAmministrativo(SiacTMovgestTsFin siacTMovgestTs,MovimentoGestione it,OttimizzazioneMovGestDto ottimizzazioneDto,boolean ottimizzatoCompletamenteDaChiamante){
		
		Integer movgestTsId = siacTMovgestTs.getMovgestTsId();
		
		List<SiacRMovgestTsAttoAmmFin> listaSiacRMovgestTsAttoAmm = null;
		if(ottimizzazioneDto!=null){
			listaSiacRMovgestTsAttoAmm = ottimizzazioneDto.filtraSiacRMovgestTsAttoAmmByMovgestTs(movgestTsId);
		}
		
		if(StringUtilsFin.isEmpty(listaSiacRMovgestTsAttoAmm) && !ottimizzatoCompletamenteDaChiamante){
			//CARICAMENTO CLASSICO
			listaSiacRMovgestTsAttoAmm = siacTMovgestTs.getSiacRMovgestTsAttoAmms();
		}
		
		//metodo core:
		it = settaDatiAttoAmministrativo(listaSiacRMovgestTsAttoAmm, it);
	}

	/**
	 * Mappa la classe sogggetto se presente sull'impegno/sub
	 * @param it
	 * @param siacTMovgestTs
	 */
	public static ClasseSoggetto setClasseSoggettoMovimentoGestione( SiacTMovgestTsFin siacTMovgestTs) {
		List<SiacRMovgestTsSogclasseFin> listaSiacRMovgestTsSogclasse = siacTMovgestTs.getSiacRMovgestTsSogclasses();
		
		ClasseSoggetto classeSoggetto = new ClasseSoggetto();
		
		for(SiacRMovgestTsSogclasseFin siacRMovgestTsSogclasse : listaSiacRMovgestTsSogclasse){
			if(null!=siacRMovgestTsSogclasse && siacRMovgestTsSogclasse.getDataFineValidita()==null){
				
				classeSoggetto.setCodice(siacRMovgestTsSogclasse.getSiacDSoggettoClasse().getSoggettoClasseCode());
				classeSoggetto.setDescrizione(siacRMovgestTsSogclasse.getSiacDSoggettoClasse().getSoggettoClasseDesc());
				break;
			}
		}
		
		return classeSoggetto;
	}
	
	
	
	/**
	 * Dato un record SiacTMovgestTsFin ne legge gli importi (attuale, iniziale, utilizzabile) e li salva opportunamente
	 * nel model MovimentoGestione indicato
	 * @param it
	 * @param siacTMovgestTs
	 * @return
	 */
	public static <MG extends MovimentoGestione> MG setImporti(MG it,SiacTMovgestTsFin siacTMovgestTs){
		if(siacTMovgestTs!=null){
			List<SiacTMovgestTsDetFin> listaSiacTMovgestTsDet = siacTMovgestTs.getSiacTMovgestTsDets();
			if(listaSiacTMovgestTsDet!=null && listaSiacTMovgestTsDet.size()>0){
				listaSiacTMovgestTsDet = DatiOperazioneUtil.soloValidi(listaSiacTMovgestTsDet, null);
				it = setImporti(it, listaSiacTMovgestTsDet);
			}
		}
		return it;
	}
	
	/**
	 * Dato una lista di record SiacTMovgestTsDetFin ne legge gli importi (attuale, iniziale, utilizzabile) e li salva opportunamente
	 * nel model MovimentoGestione indicato
	 * @param it
	 * @param listaSiacTMovgestTsDet
	 * @return
	 */
	public static <MG extends MovimentoGestione> MG setImporti(MG it,List<SiacTMovgestTsDetFin> listaSiacTMovgestTsDet){
		if(listaSiacTMovgestTsDet!=null && listaSiacTMovgestTsDet.size()>0){
			for(SiacTMovgestTsDetFin siacTMovgestTsDet : listaSiacTMovgestTsDet){
				it = setImporto(it, siacTMovgestTsDet);
			}
		}
		return it;
	}
	
	/**
	 * Dato un record SiacTMovgestTsDetFin analizza se si tratta di MOVGEST_TS_DET_TIPO_INIZIALE, MOVGEST_TS_DET_TIPO_ATTUALE o MOVGEST_TS_DET_TIPO_UTILIZZABILE
	 * e di conseguenza popola il relativo campo in MovimentoGestione
	 * @param it
	 * @param siacTMovgestTsDet
	 * @return
	 */
	private static <MG extends MovimentoGestione> MG setImporto(MG it, SiacTMovgestTsDetFin siacTMovgestTsDet){
		if(siacTMovgestTsDet!=null && DatiOperazioneUtil.isValido(siacTMovgestTsDet, null)){
			if(CostantiFin.MOVGEST_TS_DET_TIPO_INIZIALE.equalsIgnoreCase(siacTMovgestTsDet.getSiacDMovgestTsDetTipo().getMovgestTsDetTipoCode())){
				it.setImportoIniziale(siacTMovgestTsDet.getMovgestTsDetImporto());											
			} else if(CostantiFin.MOVGEST_TS_DET_TIPO_ATTUALE.equalsIgnoreCase(siacTMovgestTsDet.getSiacDMovgestTsDetTipo().getMovgestTsDetTipoCode())){
				it.setImportoAttuale(siacTMovgestTsDet.getMovgestTsDetImporto());											
			} else if(CostantiFin.MOVGEST_TS_DET_TIPO_UTILIZZABILE.equalsIgnoreCase(siacTMovgestTsDet.getSiacDMovgestTsDetTipo().getMovgestTsDetTipoCode())
					&& it instanceof Accertamento){
				((AccertamentoAbstract) it).setImportoUtilizzabile(siacTMovgestTsDet.getMovgestTsDetImporto());
			}
		}
		return it;
	}
	
	
	
	public static List<MovimentoGestione> siacTMovgestEntityToImpegnoModelOPT(List<SiacTMovgestFin> dtos,
			List<MovimentoGestione> movimentos, OttimizzazioneMovGestDto ottimizzazioneDto) {
		String methodName = "siacTMovgestEntityToImpegnoModelOPT";
		for(MovimentoGestione movimentoGestione : movimentos){
			int idIterato = movimentoGestione.getUid();
			for(SiacTMovgestFin siacTMovgest : dtos){
				int idConfronto = siacTMovgest.getMovgestId();
				if(idIterato==idConfronto){	
					// 1. ANNO_MOVIMENTO 
					// it.setAnnoMovimento(Integer.parseInt(itsiac.getMovgestAnno()));
					movimentoGestione.setAnnoMovimento(siacTMovgest.getMovgestAnno());
					// END ANNO_MOVIMENTO
					
					// 2. ATTRIBUTI ESTRATTI DALLE TABELLE siac_t_movgest_ts + siac_t_movgest_ts_det
					List<SiacTMovgestTsFin> listaSiacTMovgestTs = siacTMovgest.getSiacTMovgestTs();
					if(null!=listaSiacTMovgestTs && listaSiacTMovgestTs.size()>0){	
						for(SiacTMovgestTsFin siacTMovgestTs : listaSiacTMovgestTs){
							Integer movgestTsId = siacTMovgestTs.getMovgestTsId();
							if(null!=siacTMovgestTs &&
							   siacTMovgestTs.getDataFineValidita()==null &&
							   CostantiFin.MOVGEST_TS_TIPO_TESTATA.equalsIgnoreCase(siacTMovgestTs.getSiacDMovgestTsTipo().getMovgestTsTipoCode())){
								
								// 2.1 UTENTE_CREAZIONE + DATA_EMISSIONE
								movimentoGestione.setUtenteCreazione(siacTMovgestTs.getLoginCreazione());
								movimentoGestione.setDataEmissione(siacTMovgestTs.getDataCreazione());
								// support data emissione --> con questa modalita arriva correttamente al front end
								movimentoGestione.setDataEmissioneSupport(siacTMovgestTs.getDataCreazione());
								// END UTENTE_CREAZIONE + DATA_EMISSIONE
								
								// 2.2 UTENTE_MODIFICA + DATA_MODIFICA
								movimentoGestione.setUtenteModifica(siacTMovgestTs.getLoginModifica());
								movimentoGestione.setDataModifica(siacTMovgestTs.getDataModifica());
								// END UTENTE_MODIFICA + DATA_MODIFICA

								// 2.3 UTENTE_CANCELLAZIONE + DATA_CANCELLAZIONE
								movimentoGestione.setUtenteCancellazione(siacTMovgestTs.getLoginCancellazione());
								movimentoGestione.setDataAnnullamento(siacTMovgestTs.getDataCancellazione());
								// END UTENTE_CANCELLAZIONE + DATA_CANCELLAZIONE								

								// 2.4 DATA_SCADENZA
								movimentoGestione.setDataScadenza(siacTMovgestTs.getMovgestTsScadenzaData());
								// END DATA_SCADENZA
								

								// 2.5 IMPORTO_INIZIALE e IMPORTO_ATTUALE
								//List<SiacTMovgestTsDetFin> listaSiacTMovgestTsDet = siacTMovgestTs.getSiacTMovgestTsDets();
								List<SiacTMovgestTsDetFin> listaSiacTMovgestTsDet = ottimizzazioneDto.filtraSiacTMovgestTsDetByMovgestTs(siacTMovgestTs.getMovgestTsId());
								movimentoGestione = setImporti(movimentoGestione, listaSiacTMovgestTsDet);
								// END IMPORTO_INIZIALE e IMPORTO_ATTUALE
		
								// 2.6 DESCRIZONE
								movimentoGestione.setDescrizione(siacTMovgestTs.getMovgestTsDesc());
								// END DESCRIZONE
								
								// 2.7. STATO_OPERATIVO_MOVIMENTO_GESTIONE_SPESA								
								//List<SiacRMovgestTsStatoFin> listaSiacRMovgestTsStato = siacTMovgestTs.getSiacRMovgestTsStatos();
								setStatoOperativoMovimentoGestione(siacTMovgestTs, movimentoGestione, ottimizzazioneDto, true);
								// END STATO_OPERATIVO_MOVIMENTO_GESTIONE_SPESA
								
								// 2.8 ATTRIBUTI ESTRATTI DALLE TABELLE siac_r_movgest_ts_attr + siac_t_attr
								//List<SiacRMovgestTsAttrFin> listaSiacRMovgestTsAttr = siacTMovgestTs.getSiacRMovgestTsAttrs();
								 
								List<SiacRMovgestTsAttrFin> listaSiacRMovgestTsAttr = 
										ottimizzazioneDto.filtraSiacRMovgestTsAttrByMovgestTs(siacTMovgestTs.getMovgestTsId());
								
								for(SiacRMovgestTsAttrFin siacRMovgestTsAttr : listaSiacRMovgestTsAttr){
									if(null!=siacRMovgestTsAttr && siacRMovgestTsAttr.getDataFineValidita()==null){
										String codiceAttributo = siacRMovgestTsAttr.getSiacTAttr().getAttrCode();
										AttributoMovimentoGestione attributoMovimentoGestione = CostantiFin.attributoMovimentoGestioneStringToEnum(codiceAttributo);
										switch (attributoMovimentoGestione) {
											case annoCapitoloOrigine:
												if(siacRMovgestTsAttr.getTesto() != null){
													if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
														movimentoGestione.setAnnoCapitoloOrigine(Integer.parseInt(siacRMovgestTsAttr.getTesto()));
													}
												}
												break;
												
											case annoOriginePlur:
												if(siacRMovgestTsAttr.getTesto() != null){
													if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
														movimentoGestione.setAnnoOriginePlur(Integer.parseInt(siacRMovgestTsAttr.getTesto()));
													}
												
												}										
												break;
												
											case annoRiaccertato:
												if(siacRMovgestTsAttr.getTesto() != null && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
													if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
														movimentoGestione.setAnnoRiaccertato(Integer.parseInt(siacRMovgestTsAttr.getTesto()));
													}
													
												}
												break;
												
											case numeroArticoloOrigine:
												if(siacRMovgestTsAttr.getTesto() != null){
													if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
														movimentoGestione.setNumeroArticoloOrigine(Integer.parseInt(siacRMovgestTsAttr.getTesto()));	
													}
													
												}									
												break;
												
											case flagDaRiaccertamento:
												if(null!=siacRMovgestTsAttr.getBoolean_()){
													// if("S".equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_()) || "s".equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_())){
													if(CostantiFin.TRUE.equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_())){
														movimentoGestione.setFlagDaRiaccertamento(true);
													}else {
														movimentoGestione.setFlagDaRiaccertamento(false);
													}
												}												
												break;

											//SIAC-6997
											case flagDaReanno:
												if(null!=siacRMovgestTsAttr.getBoolean_()){
													// if("S".equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_()) || "s".equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_())){
													if(CostantiFin.TRUE.equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_())){
														movimentoGestione.setFlagDaReanno(true);
													}else {
														movimentoGestione.setFlagDaReanno(false);
													}
												}												
												break;
												
											case flagSoggettoDurc:
												if(null!=siacRMovgestTsAttr.getBoolean_()){
													movimentoGestione.setFlagSoggettoDurc(CostantiFin.TRUE.equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_()));
												}												
												break;
												
											case numeroCapitoloOrigine:
												if(siacRMovgestTsAttr.getTesto() != null){
													if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
														movimentoGestione.setNumeroCapitoloOrigine(Integer.parseInt(siacRMovgestTsAttr.getTesto()));	
													}
												}						
												break;
												
											case numeroOriginePlur:
												if(siacRMovgestTsAttr.getTesto() != null){
													if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
														movimentoGestione.setNumeroOriginePlur(new BigDecimal(Integer.parseInt(siacRMovgestTsAttr.getTesto())));
													}
													
												}
												break;
												
											case numeroRiaccertato:
												if(siacRMovgestTsAttr.getTesto() != null){
													if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
														movimentoGestione.setNumeroRiaccertato(new BigDecimal(Integer.parseInt(siacRMovgestTsAttr.getTesto())));
													}
												}
												break;
												
											case numeroUEBOrigine:
												if(siacRMovgestTsAttr.getTesto() != null){
													if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
														movimentoGestione.setNumeroUEBOrigine(Integer.parseInt(siacRMovgestTsAttr.getTesto()));
													}
												
												} 
												break;
											
											case annoFinanziamento:
												if(movimentoGestione instanceof Impegno){
													if(siacRMovgestTsAttr.getTesto() != null){
														if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
															((ImpegnoAbstract) movimentoGestione).setAnnoFinanziamento(Integer.parseInt(siacRMovgestTsAttr.getTesto()));
														}
													}
												}
												break;
												
											case cig:
												if(movimentoGestione instanceof Impegno){
													if(siacRMovgestTsAttr.getTesto() != null){
														if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
															((ImpegnoAbstract) movimentoGestione).setCig(siacRMovgestTsAttr.getTesto());
														}
													}
												}
												break;
												
											case numeroAccFinanziamento:
												if(movimentoGestione instanceof Impegno){
													if(siacRMovgestTsAttr.getTesto() != null){
														if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
															((ImpegnoAbstract) movimentoGestione).setNumeroAccFinanziamento(Integer.parseInt(siacRMovgestTsAttr.getTesto()));
														}
													}
												}
												break;
												
											case validato:
												if(null!=siacRMovgestTsAttr.getBoolean_()){
													// if("S".equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_()) || "s".equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_())){
													if(CostantiFin.TRUE.equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_())){
														movimentoGestione.setValidato(true);
													}else {
														movimentoGestione.setValidato(false);
													}
												}
												break;

											default:
												break;
										}
									}									
								}
								// END ATTRIBUTI ESTRATTI DALLE TABELLE siac_r_movgest_ts_attr + siac_t_attr
								
								// 3 CLASSE_SOGGETTO
								//List<SiacRMovgestTsSogclasseFin> listaSiacRMovgestTsSogclasse = siacTMovgestTs.getSiacRMovgestTsSogclasses();
								List<SiacRMovgestTsSogclasseFin> listaSiacRMovgestTsSogclasse =  ottimizzazioneDto.getDistintiSiacRMovgestTsSogclasseCoinvolti();
								for(SiacRMovgestTsSogclasseFin siacRMovgestTsSogclasse : listaSiacRMovgestTsSogclasse){
									if(null!=siacRMovgestTsSogclasse && siacRMovgestTsSogclasse.getDataFineValidita()==null){
										ClasseSoggetto classeSoggetto = new ClasseSoggetto();
										classeSoggetto.setCodice(siacRMovgestTsSogclasse.getSiacDSoggettoClasse().getSoggettoClasseCode());
										classeSoggetto.setDescrizione(siacRMovgestTsSogclasse.getSiacDSoggettoClasse().getSoggettoClasseDesc());
										movimentoGestione.setClasseSoggetto(classeSoggetto);
									}
								}
								// END CLASSE_SOGGETTO

								// 4 tipoImpegno
								
								//List<SiacRMovgestClassFin> listaSiacRMovgestClass = siacTMovgestTs.getSiacRMovgestClasses();
								List<SiacRMovgestClassFin> listaSiacRMovgestClass =
										ottimizzazioneDto.filtraSiacRMovgestClassByMovgestTs(siacTMovgestTs.getMovgestTsId());
								
								for(SiacRMovgestClassFin siacRMovgestClass : listaSiacRMovgestClass){
									if(null!=siacRMovgestClass && siacRMovgestClass.getDataFineValidita()==null){
										if(siacRMovgestClass.getSiacTClass().getSiacDClassTipo().getClassifTipoCode().equalsIgnoreCase(CostantiFin.D_CLASS_TIPO_TIPO_IMPEGNO)){
											// TIPO
											if(movimentoGestione instanceof Impegno){
												ClassificatoreGenerico tipoImpegno = new ClassificatoreGenerico();
												tipoImpegno.setUid(siacRMovgestClass.getSiacTClass().getClassifId());
												tipoImpegno.setCodice(siacRMovgestClass.getSiacTClass().getClassifCode());
												tipoImpegno.setDescrizione(siacRMovgestClass.getSiacTClass().getClassifDesc());
												((ImpegnoAbstract) movimentoGestione).setTipoImpegno(tipoImpegno);
											} 
										}
									}									
								}
								// END tipoImpegno
								
								//DATI TRANSAZIONE ELEMENTARE:
								movimentoGestione = (MovimentoGestione) TransazioneElementareEntityToModelConverter.
										convertiDatiTransazioneElementare(movimentoGestione, listaSiacRMovgestClass,listaSiacRMovgestTsAttr);
								//END DATI TRANSAZIONE ELEMENTARE
								
								// 5 PROGETTO
								//List<SiacRMovgestTsProgrammaFin> listaSiacRMovgestTsProgramma = siacTMovgestTs.getSiacRMovgestTsProgrammas();
								List<SiacRMovgestTsProgrammaFin> listaSiacRMovgestTsProgramma = ottimizzazioneDto.filtraSiacRMovgestTsProgrammaByMovgestTs(movgestTsId);
								for (SiacRMovgestTsProgrammaFin siacRMovgestTsProgramma : listaSiacRMovgestTsProgramma) {
									if (null != siacRMovgestTsProgramma
											&& siacRMovgestTsProgramma.getDataFineValidita() == null) {

										Progetto progetto = new Progetto();
										progetto.setUid(siacRMovgestTsProgramma.getSiacTProgramma().getProgrammaId());
										progetto.setCodice(siacRMovgestTsProgramma.getSiacTProgramma().getProgrammaCode());
										progetto.setDescrizione(siacRMovgestTsProgramma.getSiacTProgramma().getProgrammaDesc());
										movimentoGestione.setProgetto(progetto);
									}
								}
								// END PROGETTO
								
								// 6. ATTO AMMINISTRATIVO
								//List<SiacRMovgestTsAttoAmmFin> listaSiacRMovgestTsAttoAmm = siacTMovgestTs.getSiacRMovgestTsAttoAmms();
								List<SiacRMovgestTsAttoAmmFin> listaSiacRMovgestTsAttoAmm = ottimizzazioneDto.filtraSiacRMovgestTsAttoAmmByMovgestTs(movgestTsId);
								movimentoGestione = settaDatiAttoAmministrativo(listaSiacRMovgestTsAttoAmm, movimentoGestione);
								// END ATTO AMMINISTRATIVO
								
								//7. SIOPE PLUS
								settaCampiSiopePlus(siacTMovgestTs, movimentoGestione);
								//  END SIOPE PLUS
							}
						}
					}
					// END ATTRIBUTI ESTRATTI DALLE TABELLE siac_t_movgest_ts + siac_t_movgest_ts_det
					
					// 3. CAPITOLO DI ENTRATA / USCITA
					List<SiacRMovgestBilElemFin> siacRMovgestBilElemCoinvolti = ottimizzazioneDto.filtraSiacRMovgestBilElemByMovgest(siacTMovgest.getMovgestId());
					movimentoGestione = setChiaveCapitoloOPT(movimentoGestione,CommonUtil.getFirst(siacRMovgestBilElemCoinvolti));
					// END CAPITOLO DI ENTRATA / USCITA	

					// 4. ALTRI POSSIBILI CAMPI
					// END ALTRI POSSIBILI CAMPI	

					break;
				}
			}
		}
		return movimentos;
	}
	
	/**
	 * Individua il record valido nella lista e lo utilizza per popolare l'AttoAmministrativo
	 * @param listaSiacRMovgestTsAttoAmm
	 * @param it
	 * @return
	 */
	private static MovimentoGestione settaDatiAttoAmministrativo(List<SiacRMovgestTsAttoAmmFin> listaSiacRMovgestTsAttoAmm,MovimentoGestione it){
		if(null!=listaSiacRMovgestTsAttoAmm && listaSiacRMovgestTsAttoAmm.size() > 0){
			//estraggo l'unico record valido:
			SiacRMovgestTsAttoAmmFin siacRMovgestTsAttoAmm = DatiOperazioneUtil.getValido(listaSiacRMovgestTsAttoAmm, null);
			if(null!= siacRMovgestTsAttoAmm){
				it = settaDatiAttoAmministrativo(siacRMovgestTsAttoAmm.getSiacTAttoAmm(), it);
			}
		}
		return it;
	}
	
	/**
	 * Metodo piu interno per il popolamento dei dati dell'atto in un MovimentoGestione
	 * @param siacTAttoAmm
	 * @param it
	 * @return
	 */
	private static MovimentoGestione settaDatiAttoAmministrativo(SiacTAttoAmmFin siacTAttoAmm,MovimentoGestione it){
		if(siacTAttoAmm!=null){
			AttoAmministrativo atto = siacTAttoToAttoAmministrativo(siacTAttoAmm);
			if(atto!=null){
				
				//TIPO ATTO:
				TipoAtto tipoAtto = atto.getTipoAtto();
				it.setAttoAmmTipoAtto(tipoAtto);
				
				//EVENTUALE STRUTTURA AMMINISTRATIVA:
				if(atto.getStrutturaAmmContabile()!=null && atto.getStrutturaAmmContabile().getUid()>0){
					it.setIdStrutturaAmministrativa(new Integer(atto.getStrutturaAmmContabile().getUid()));
				}
				
				//set dei dati indipendenti dal tipo di istanza di MovimentoGestione:
				String annoAtto = Integer.toString(atto.getAnno());
				Integer numeroAtto = atto.getNumero();
				String tipoCode = tipoAtto.getCodice();
						
				it.setAttoAmmAnno(annoAtto);
				it.setAttoAmmNumero(numeroAtto);
				
				//set dei dati specifici:
				if(it instanceof Impegno){
					//Nothing to do here
				} else if(it instanceof Accertamento){
					//Nothing to do here
				} else if(it instanceof ModificaMovimentoGestioneSpesa){
					((ModificaMovimentoGestioneSpesa) it).setAttoAmministrativoAnno(annoAtto);
					((ModificaMovimentoGestioneSpesa) it).setAttoAmministrativoNumero(numeroAtto);
					((ModificaMovimentoGestioneSpesa) it).setAttoAmministrativoTipoCode(tipoCode);
				}  else if(it instanceof ModificaMovimentoGestioneEntrata){
					((ModificaMovimentoGestioneEntrata) it).setAttoAmministrativoAnno(annoAtto);
					((ModificaMovimentoGestioneEntrata) it).setAttoAmministrativoNumero(numeroAtto);
					((ModificaMovimentoGestioneEntrata) it).setAttoAmministrativoTipoCode(tipoCode);
				}
			}
			
			
			it.setAttoAmministrativo(atto);
		}
		return it;
	}
	
	
	
	/**
	 * Metodo piu interno per il popolamento dei dati dell'atto in un MovimentoGestione
	 * @param siacTAttoAmm
	 * @param it
	 * @return
	 */
	public static MovimentoGestione settaAttoAmministrativoBase(SiacTAttoAmmFin siacTAttoAmm, MovimentoGestione it){
		if(siacTAttoAmm!=null){
			AttoAmministrativo atto = siacTAttoToAttoAmministrativo(siacTAttoAmm);
			it.setAttoAmministrativo(atto);
		}
		return it;
	}
	
	
	/**
	 * Estrazione dati da siat_t_atto e popolamento (minimale) di un AttoAmministrativo
	 * @param siacTAttoAmm
	 * @return
	 */
	public static AttoAmministrativo siacTAttoToAttoAmministrativo(SiacTAttoAmmFin siacTAttoAmm){
		
		AttoAmministrativo atto = null;
		if(siacTAttoAmm!=null && siacTAttoAmm.getUid() != null && siacTAttoAmm.getUid() > 0){
			atto = new AttoAmministrativo();
			//TIPO ATTO:
			TipoAtto tipoAtto = new TipoAtto();
			tipoAtto.setDescrizione(siacTAttoAmm.getSiacDAttoAmmTipo().getAttoammTipoDesc());
			tipoAtto.setCodice(siacTAttoAmm.getSiacDAttoAmmTipo().getAttoammTipoCode());
			tipoAtto.setUid(siacTAttoAmm.getSiacDAttoAmmTipo().getUid());
			//DATI ATTO:
			atto.setUid(siacTAttoAmm.getUid());
			atto.setAnno(new Integer(siacTAttoAmm.getAttoammAnno()));
			atto.setNumero(siacTAttoAmm.getAttoammNumero());
			atto.setTipoAtto(tipoAtto);
			atto.setNote(siacTAttoAmm.getAttoammNote());
			atto.setOggetto(siacTAttoAmm.getAttoammOggetto());
			atto.setDataCreazione(siacTAttoAmm.getDataCreazione());
			//SIAC-6929
			atto.setBloccoRagioneria(siacTAttoAmm.getAttoammBlocco());
			atto.setProvenienza(siacTAttoAmm.getAttoammProvenienza());

			//EVENTUALE STRUTTURA AMMINISTRATIVA:
			StrutturaAmministrativoContabile sac = estraiStrutturaAmministrativa(siacTAttoAmm);
			if(sac!=null){
				atto.setStrutturaAmmContabile(sac);
			}
			
			// RM 28/07/2025 Aggiungo lo stato!
			for (SiacRAttoAmmStatoFin siacRAttoAmmStato : siacTAttoAmm.getSiacRAttoAmmStatos()) {
				if(siacRAttoAmmStato.getDataCancellazione()==null){
					SiacDAttoAmmStatoFin siacDAttoAmmStato = siacRAttoAmmStato.getSiacDAttoAmmStato();
					atto.setStatoOperativo(siacDAttoAmmStato.getAttoammStatoCode());
					break;
				}
			}
			
			if(siacTAttoAmm.getParereRegolaritaContabile()!=null){
				atto.setParereRegolaritaContabile(siacTAttoAmm.getParereRegolaritaContabile());
			} else {
				atto.setParereRegolaritaContabile(Boolean.FALSE);
			}
			
		}
		return atto;
	}
	
	
	/**
	 * Estrazione dati da siat_t_atto e popolamento (minimale) di un AttoAmministrativo
	 * @param siacTAttoAmm
	 * @return
	 */
	public static AttoAmministrativoStoricizzato convertiSiacTAttoAmmToAttoAmministrativo(SiacTAttoAmmFin siacTAttoAmm){
		
		AttoAmministrativoStoricizzato atto = new AttoAmministrativoStoricizzato();
		
		if(siacTAttoAmm!=null && siacTAttoAmm.getUid() != null && siacTAttoAmm.getUid() > 0){
			
			//TIPO ATTO:
			TipoAtto tipoAtto = new TipoAtto();
			tipoAtto.setDescrizione(siacTAttoAmm.getSiacDAttoAmmTipo().getAttoammTipoDesc());
			tipoAtto.setCodice(siacTAttoAmm.getSiacDAttoAmmTipo().getAttoammTipoCode());
			tipoAtto.setUid(siacTAttoAmm.getSiacDAttoAmmTipo().getUid());
			
			//DATI ATTO:
			atto.setUid(siacTAttoAmm.getUid());
			atto.setAnno(new Integer(siacTAttoAmm.getAttoammAnno()));
			atto.setNumero(siacTAttoAmm.getAttoammNumero());
			atto.setTipoAtto(tipoAtto);
			//atto.setNote(siacTAttoAmm.getAttoammNote());
			atto.setOggetto(siacTAttoAmm.getAttoammOggetto());
			
			//EVENTUALE STRUTTURA AMMINISTRATIVA:
			StrutturaAmministrativoContabile sac = estraiStrutturaAmministrativa(siacTAttoAmm);
			if(sac!=null){
				atto.setStrutturaAmmContabile(sac);
			}
			
			
		}
		
		
					
		return atto;
		
	}
	
	public static MovimentoGestione setChiaveCapitolo(SiacTMovgestFin itsiac,MovimentoGestione it){
		List<SiacRMovgestBilElemFin> listaSiacRMovgestBilElem = itsiac.getSiacRMovgestBilElems();
		if(null!=listaSiacRMovgestBilElem && listaSiacRMovgestBilElem.size() > 0){
			for(SiacRMovgestBilElemFin siacRMovgestBilElem : listaSiacRMovgestBilElem){
				if(null!= siacRMovgestBilElem && siacRMovgestBilElem.getDataFineValidita() == null){
					//SIAC-8065
					Integer chiave = getChiaveCapitolo(itsiac) != null ? getChiaveCapitolo(itsiac) : 0;
					if(CostantiFin.D_BIL_ELEM_TIPO_ELEM_TIPO_CODE_CAP_UG.equalsIgnoreCase(siacRMovgestBilElem.getSiacTBilElem().getSiacDBilElemTipo().getElemTipoCode())){
						((ImpegnoAbstract) it).setChiaveCapitoloUscitaGestione(chiave);
					} else if(CostantiFin.D_BIL_ELEM_TIPO_ELEM_TIPO_CODE_CAP_EG.equalsIgnoreCase(siacRMovgestBilElem.getSiacTBilElem().getSiacDBilElemTipo().getElemTipoCode())){
						((AccertamentoAbstract) it).setChiaveCapitoloEntrataGestione(chiave);
					}
				}
			}
		}
		return it;
	}
	
	
	public static MovimentoGestione setChiaveLogicaCapitolo(SiacTMovgestFin itsiac,MovimentoGestione it){
		List<SiacRMovgestBilElemFin> listaSiacRMovgestBilElem = itsiac.getSiacRMovgestBilElems();
		if(null!=listaSiacRMovgestBilElem && listaSiacRMovgestBilElem.size() > 0){
			for(SiacRMovgestBilElemFin siacRMovgestBilElem : listaSiacRMovgestBilElem){
				if(null!= siacRMovgestBilElem && siacRMovgestBilElem.getDataFineValidita() == null){
					
					SiacRMovgestBilElemFin relazioneValida = DatiOperazioneUtil.getValido(listaSiacRMovgestBilElem, null);
					Integer uidCapitolo = relazioneValida.getSiacTBilElem().getElemId();
					String annoCapitolo = relazioneValida.getSiacTBilElem().getSiacTBil().getSiacTPeriodo().getAnno();
					String numeroCapitolo = relazioneValida.getSiacTBilElem().getElemCode();
					String numeroArticolo = relazioneValida.getSiacTBilElem().getElemCode2();
					String numeroUeb = relazioneValida.getSiacTBilElem().getElemCode3();
					
					if(CostantiFin.D_BIL_ELEM_TIPO_ELEM_TIPO_CODE_CAP_UG.equalsIgnoreCase(siacRMovgestBilElem.getSiacTBilElem().getSiacDBilElemTipo().getElemTipoCode())){
						CapitoloUscitaGestione capug = new CapitoloUscitaGestione();
						capug.setUid(uidCapitolo);
						capug.setAnnoCapitolo(Integer.parseInt(annoCapitolo));
						capug.setNumeroCapitolo(Integer.parseInt(numeroCapitolo));
						capug.setNumeroArticolo(Integer.parseInt(numeroArticolo));
						capug.setNumeroUEB(Integer.parseInt(numeroUeb));
						
						((Impegno) it).setCapitoloUscitaGestione(capug);
						
					} else if(CostantiFin.D_BIL_ELEM_TIPO_ELEM_TIPO_CODE_CAP_EG.equalsIgnoreCase(siacRMovgestBilElem.getSiacTBilElem().getSiacDBilElemTipo().getElemTipoCode())){
						
						CapitoloEntrataGestione capeg = new CapitoloEntrataGestione();
						capeg.setUid(uidCapitolo);
						capeg.setAnnoCapitolo(Integer.parseInt(annoCapitolo));
						capeg.setNumeroCapitolo(Integer.parseInt(numeroCapitolo));
						capeg.setNumeroArticolo(Integer.parseInt(numeroArticolo));
						capeg.setNumeroUEB(Integer.parseInt(numeroUeb));
						
						((Accertamento) it).setCapitoloEntrataGestione(capeg);
						
					}
				}
			}
		}
		return it;
	}
	
	public static MovimentoGestione setChiaveCapitoloOPT(MovimentoGestione it,SiacRMovgestBilElemFin siacRMovgestBilElem){
		if(null!=siacRMovgestBilElem){
			SiacTBilElemFin siacTBilElem = siacRMovgestBilElem.getSiacTBilElem();
			Integer chiave = siacTBilElem.getElemId();
			if(CostantiFin.D_BIL_ELEM_TIPO_ELEM_TIPO_CODE_CAP_UG.equalsIgnoreCase(siacTBilElem.getSiacDBilElemTipo().getElemTipoCode())){
				((ImpegnoAbstract) it).setChiaveCapitoloUscitaGestione(chiave);
			} else if(CostantiFin.D_BIL_ELEM_TIPO_ELEM_TIPO_CODE_CAP_EG.equalsIgnoreCase(siacTBilElem.getSiacDBilElemTipo().getElemTipoCode())){
				((AccertamentoAbstract) it).setChiaveCapitoloEntrataGestione(chiave);
			}
		}
		return it;
	}
	
	public static Integer getChiaveCapitolo(SiacTMovgestFin itsiac){
		List<SiacRMovgestBilElemFin> listaSiacRMovgestBilElem = itsiac.getSiacRMovgestBilElems();
		Integer chiave = null;
		if(null!=listaSiacRMovgestBilElem && listaSiacRMovgestBilElem.size() > 0){
			SiacRMovgestBilElemFin relazioneValida = DatiOperazioneUtil.getValido(listaSiacRMovgestBilElem, null);
			//SIAC-8065
			chiave = relazioneValida != null && relazioneValida.getSiacTBilElem() != null && relazioneValida.getSiacTBilElem().getElemId() != null
					? relazioneValida.getSiacTBilElem().getElemId() : null;
		}
		return chiave;
	}
	
	public static SubImpegno siacTMovgestTsEntityToSubImpegnoModel(SiacTMovgestTsFin dto, SubImpegno subImpegno,SiacTMovgestFin siacTMovgest,boolean caricaDatiUlteriori,OttimizzazioneMovGestDto ottimizzazioneDto) {
		List<SiacTMovgestTsFin> dtos = new ArrayList<SiacTMovgestTsFin>();
		dtos.add(dto);
		List<SubImpegno> movimentos = new ArrayList<SubImpegno>();
		movimentos.add(subImpegno);
		movimentos = siacTMovgestTsEntityToSubImpegnoModel(dtos, movimentos,siacTMovgest, caricaDatiUlteriori,ottimizzazioneDto);
		return movimentos.get(0);
	}
	
	public static SubAccertamento siacTMovgestTsEntityToSubAccertamentoModel(SiacTMovgestTsFin dto, SubAccertamento subAccertamento, SiacTMovgestFin siacTMovgest,OttimizzazioneMovGestDto ottimizzazioneDto) {
		List<SiacTMovgestTsFin> dtos = new ArrayList<SiacTMovgestTsFin>();
		dtos.add(dto);
		List<SubAccertamento> movimentos = new ArrayList<SubAccertamento>();
		movimentos.add(subAccertamento);
		movimentos = siacTMovgestTsEntityToSubAccertamentoModel(dtos, movimentos, siacTMovgest,ottimizzazioneDto);
		return movimentos.get(0);
	}
	
	public static MovimentoGestione settaDateEUtenti(MovimentoGestione it,SiacTMovgestTsFin itsiac){
		// 2. UTENTE_CREAZIONE + DATA_EMISSIONE
		it.setUtenteCreazione(itsiac.getLoginCreazione());
		it.setDataEmissione(new Timestamp(itsiac.getDataCreazione().getTime()));
		it.setDataCreazione(itsiac.getDataCreazione());
		// support data emissione --> con questa modalita arriva correttamente al front end
		it.setDataEmissioneSupport(itsiac.getDataCreazione());
		
		// END UTENTE_CREAZIONE + DATA_EMISSIONE

		// 3. UTENTE_MODIFICA + DATA_MODIFICA
		it.setUtenteModifica(itsiac.getLoginModifica());
		it.setDataModifica(itsiac.getDataModifica());
		// END UTENTE_MODIFICA + DATA_MODIFICA

		// 4. UTENTE_CANCELLAZIONE + DATA_CANCELLAZIONE
		it.setUtenteModifica(itsiac.getLoginModifica());
		it.setDataAnnullamento(itsiac.getDataCancellazione());
		// END UTENTE_CANCELLAZIONE + DATA_CANCELLAZIONE

		// 5. DATA_SCADENZA
		it.setDataScadenza(itsiac.getMovgestTsScadenzaData());
		// END DATA_SCADENZA
		
		return it;
	}
	
	public static List<SubImpegno> siacTMovgestTsEntityToSubImpegnoModel(List<SiacTMovgestTsFin> dtos, List<SubImpegno> subImpegnos,SiacTMovgestFin siacTMovgest, boolean caricaDatiUlteriori,OttimizzazioneMovGestDto ottimizzazioneDto) {
		String methodName = "movimentoGestioneEntityToSubImpegnoModel";
		
		//per non ripetere sempre il controllo di null pointer sull'oggetto ottimizzazioneDto intero ma solo sui sui contenuti:
		if(ottimizzazioneDto==null){
			ottimizzazioneDto = new OttimizzazioneMovGestDto();
		}
		//
		
		for(SubImpegno it : subImpegnos){
			int idIterato = it.getUid();
			for(SiacTMovgestTsFin itsiac : dtos){
				int idConfronto = itsiac.getMovgestTsId();
				if(idIterato==idConfronto){					
					// 1. ANNO_MOVIMENTO 
					// it.setAnnoMovimento(Integer.parseInt(siacTMovgest.getMovgestAnno()));
					it.setAnnoMovimento(siacTMovgest.getMovgestAnno());
					// END ANNO_MOVIMENTO

					it.setAnnoImpegnoPadre(siacTMovgest.getMovgestAnno());
					it.setNumeroImpegnoPadre(siacTMovgest.getMovgestNumero());
					
					// 2. UTENTE_CREAZIONE + DATA_EMISSIONE
					it.setUtenteCreazione(itsiac.getLoginCreazione());
					it.setDataEmissione(new Timestamp(itsiac.getDataCreazione().getTime()));
					it.setDataCreazione(itsiac.getDataCreazione());
					// support data emissione --> con questa modalita arriva correttamente al front end
					it.setDataEmissioneSupport(itsiac.getDataCreazione());
					
					// END UTENTE_CREAZIONE + DATA_EMISSIONE

					// 3. UTENTE_MODIFICA + DATA_MODIFICA
					it.setUtenteModifica(itsiac.getLoginModifica());
					it.setDataModifica(itsiac.getDataModifica());
					// END UTENTE_MODIFICA + DATA_MODIFICA

					// 4. UTENTE_CANCELLAZIONE + DATA_CANCELLAZIONE
					it.setUtenteModifica(itsiac.getLoginModifica());
					it.setDataAnnullamento(itsiac.getDataCancellazione());
					// END UTENTE_CANCELLAZIONE + DATA_CANCELLAZIONE

					// 5. DATA_SCADENZA
					it.setDataScadenza(itsiac.getMovgestTsScadenzaData());
					// END DATA_SCADENZA

					// 6. STATO_OPERATIVO_MOVIMENTO_GESTIONE_SPESA
					setStatoOperativoMovimentoGestione(itsiac, it, ottimizzazioneDto, false);
					// END STATO_OPERATIVO_MOVIMENTO_GESTIONE_SPESA

					// 7. IMPORTO_INIZIALE e IMPORTO_ATTUALE
					List<SiacTMovgestTsDetFin> listaSiacTMovgestTsDet = ottimizzazioneDto.filtraSiacTMovgestTsDetByMovgestTs(itsiac.getMovgestTsId());
					if(listaSiacTMovgestTsDet!=null && listaSiacTMovgestTsDet.size()>0){
						//caricamento ottimizzato
						setImporti(it, listaSiacTMovgestTsDet);
					} else {
						//caricamento classico
						setImporti(it, itsiac);
					}
					// END IMPORTO_INIZIALE e IMPORTO_ATTUALE

					// 8. ATTRIBUTI ESTRATTI DALLE TABELLE siac_r_movgest_ts_attr + siac_t_attr
					List<SiacRMovgestTsAttrFin> listaSiacRMovgestTsAttr = itsiac.getSiacRMovgestTsAttrs();
					for(SiacRMovgestTsAttrFin siacRMovgestTsAttr : listaSiacRMovgestTsAttr){
						if(null!=siacRMovgestTsAttr && siacRMovgestTsAttr.getDataFineValidita()==null){
							String codiceAttributo = siacRMovgestTsAttr.getSiacTAttr().getAttrCode();
							AttributoMovimentoGestione attributoMovimentoGestione = CostantiFin.attributoMovimentoGestioneStringToEnum(codiceAttributo);
							switch (attributoMovimentoGestione) {
							
								case annoCapitoloOrigine:
									if(siacRMovgestTsAttr.getTesto() != null){
										if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
											it.setAnnoCapitoloOrigine(Integer.parseInt(siacRMovgestTsAttr.getTesto()));
										}
									}
									break;

								case annoOriginePlur:
									if(siacRMovgestTsAttr.getTesto() != null){
										if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
											it.setAnnoOriginePlur(Integer.parseInt(siacRMovgestTsAttr.getTesto()));
										}
									
									}										
									break;

								case annoRiaccertato:
									if(siacRMovgestTsAttr.getTesto() != null){
										if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
											it.setAnnoRiaccertato(Integer.parseInt(siacRMovgestTsAttr.getTesto()));
										}
										
									}
									break;

								case numeroArticoloOrigine:
									if(siacRMovgestTsAttr.getTesto() != null){
										if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
											it.setNumeroArticoloOrigine(Integer.parseInt(siacRMovgestTsAttr.getTesto()));	
										}
										
									}									
									break;

								case flagDaRiaccertamento:
									if(null!=siacRMovgestTsAttr.getBoolean_()){
										// if("S".equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_()) || "s".equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_())){
										if(CostantiFin.TRUE.equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_())){
											it.setFlagDaRiaccertamento(true);
										}else {
											it.setFlagDaRiaccertamento(false);
										}
									}												
									break;
									
								case flagDaReanno:
									if(null!=siacRMovgestTsAttr.getBoolean_()){
										// if("S".equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_()) || "s".equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_())){
										if(CostantiFin.TRUE.equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_())){
											it.setFlagDaReanno(true);
										}else {
											it.setFlagDaReanno(false);
										}
									}												
									break;	

								case flagSoggettoDurc:
									if(null!=siacRMovgestTsAttr.getBoolean_()){
										it.setFlagSoggettoDurc(CostantiFin.TRUE.equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_()));
									}												
									break;

								case numeroCapitoloOrigine:
									if(siacRMovgestTsAttr.getTesto() != null){
										if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
											it.setNumeroCapitoloOrigine(Integer.parseInt(siacRMovgestTsAttr.getTesto()));	
										}
									}						
									break;

								case numeroOriginePlur:
									if(siacRMovgestTsAttr.getTesto() != null){
										if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
											it.setNumeroOriginePlur(new BigDecimal(Integer.parseInt(siacRMovgestTsAttr.getTesto())));
										}
										
									}
									break;

								case numeroRiaccertato:
									if(siacRMovgestTsAttr.getTesto() != null){
										if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
											it.setNumeroRiaccertato(new BigDecimal(Integer.parseInt(siacRMovgestTsAttr.getTesto())));
										}
									}
									break;

								case numeroUEBOrigine:
									if(siacRMovgestTsAttr.getTesto() != null){
										if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
											it.setNumeroUEBOrigine(Integer.parseInt(siacRMovgestTsAttr.getTesto()));
										}
									
									} 
									break;

								case annoFinanziamento:
									if(siacRMovgestTsAttr.getTesto() != null){
										if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
											it.setAnnoFinanziamento(Integer.parseInt(siacRMovgestTsAttr.getTesto()));
										}
									}
									break;

								case cig:
									if(siacRMovgestTsAttr.getTesto() != null){
										if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null")){
											it.setCig(siacRMovgestTsAttr.getTesto());
										}
									}
									break;

								case numeroAccFinanziamento:
									if(siacRMovgestTsAttr.getTesto() != null){
										if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
											it.setNumeroAccFinanziamento(Integer.parseInt(siacRMovgestTsAttr.getTesto()));
										}
									}									
									break;

								case validato:
									if(null!=siacRMovgestTsAttr.getBoolean_()){
										// if("S".equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_()) || "s".equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_())){
										if(CostantiFin.TRUE.equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_())){
											it.setValidato(true);
										}else {
											it.setValidato(false);
										}
									}
									break;
	
								default:
									break;
							}
						}									
					}
					// END ATTRIBUTI ESTRATTI DALLE TABELLE siac_r_movgest_ts_attr + siac_t_attr
					
					// 9 CLASSE_SOGGETTO
					List<SiacRMovgestTsSogclasseFin> listaSiacRMovgestTsSogclasse = itsiac.getSiacRMovgestTsSogclasses();
					
					/*
					 * caricaDatiUlteriori = per aumentare le prestazioni
					 */
					
					if(caricaDatiUlteriori){
						for(SiacRMovgestTsSogclasseFin siacRMovgestTsSogclasse : listaSiacRMovgestTsSogclasse){
							if(null!=siacRMovgestTsSogclasse && siacRMovgestTsSogclasse.getDataFineValidita()==null){
								ClasseSoggetto classeSoggetto = new ClasseSoggetto();
								classeSoggetto.setCodice(siacRMovgestTsSogclasse.getSiacDSoggettoClasse().getSoggettoClasseCode());
								classeSoggetto.setDescrizione(siacRMovgestTsSogclasse.getSiacDSoggettoClasse().getSoggettoClasseDesc());
								it.setClasseSoggetto(classeSoggetto);
							}
						}
					}
					// END CLASSE_SOGGETTO
					
					
					//  SOGGETTO ASSOCIATO
					if(itsiac.getSiacRMovgestTsSogs() != null && !itsiac.getSiacRMovgestTsSogs().isEmpty()){
						for(SiacRMovgestTsSogFin rMovGestTsSog : itsiac.getSiacRMovgestTsSogs()){
							if(rMovGestTsSog.getDataFineValidita()==null){
								it.setSoggettoCode(rMovGestTsSog.getSiacTSoggetto().getSoggettoCode());
								break;
							}
						}
					}
					// END SOGGETTO ASSOCIATO

					// 10 PROGETTO
					List<SiacRMovgestTsProgrammaFin> listaSiacRMovgestTsProgramma = itsiac.getSiacRMovgestTsProgrammas();
					/*
					 * caricaDatiUlteriori = per aumentare le prestazioni
					 */
					if(caricaDatiUlteriori){
						for(SiacRMovgestTsProgrammaFin siacRMovgestTsProgramma : listaSiacRMovgestTsProgramma){
							if(null!=siacRMovgestTsProgramma && siacRMovgestTsProgramma.getDataFineValidita()==null){
								//setto il codice
								Progetto progetto = new Progetto();
								progetto.setUid(siacRMovgestTsProgramma.getSiacTProgramma().getProgrammaId());
								progetto.setCodice(siacRMovgestTsProgramma.getSiacTProgramma().getProgrammaCode());
								progetto.setDescrizione(siacRMovgestTsProgramma.getSiacTProgramma().getProgrammaDesc());
								it.setProgetto(progetto);
							}									
						}
					}
					// END PROGETTO

					// 11 TIPO IMPEGNO
					List<SiacRMovgestClassFin> listaSiacRMovgestClass = itsiac.getSiacRMovgestClasses();
					/*
					 * caricaDatiUlteriori = per aumentare le prestazioni
					 */
					if(caricaDatiUlteriori){
						for(SiacRMovgestClassFin siacRMovgestClass : listaSiacRMovgestClass){
							if(null!=siacRMovgestClass && siacRMovgestClass.getDataFineValidita()==null){
								if(siacRMovgestClass.getSiacTClass().getSiacDClassTipo().getClassifTipoCode().equalsIgnoreCase(CostantiFin.D_CLASS_TIPO_TIPO_IMPEGNO)){
									// TIPO
									ClassificatoreGenerico tipoImpegno = new ClassificatoreGenerico();
									tipoImpegno.setUid(siacRMovgestClass.getSiacTClass().getClassifId());
									tipoImpegno.setCodice(siacRMovgestClass.getSiacTClass().getClassifCode());
									tipoImpegno.setDescrizione(siacRMovgestClass.getSiacTClass().getClassifDesc());
									it.setTipoImpegno(tipoImpegno);
								}
							}									
						}
					}
					// END tipo impegno
					
					//
					//DATI TRANSAZIONE ELEMENTARE:
					it = (SubImpegno) TransazioneElementareEntityToModelConverter.
							convertiDatiTransazioneElementare(it, listaSiacRMovgestClass,listaSiacRMovgestTsAttr);
					//END DATI TRANSAZIONE ELEMENTARE
					//

					// 12 DESCRIZONE
					it.setDescrizione(itsiac.getMovgestTsDesc());
					// END DESCRIZONE

					// 13. ATTO AMMINISTRATIVO
					it = (SubImpegno) settaDatiAttoAmm(itsiac, it);
					// END ATTO AMMINISTRATIVO
					
					//14. SIOPE PLUS
					settaCampiSiopePlus(itsiac, it);
					//  END SIOPE PLUS

					// 14. ALTRI POSSIBILI CAMPI
					// END ALTRI POSSIBILI CAMPI	

					break;
				}
			}
		}
		return subImpegnos;
	}
	
	/**
	 * Setta i dati dell'atto amministrativo
	 * @param itsiac
	 * @param it
	 * @return
	 */
	public static MovimentoGestione settaDatiAttoAmm(SiacTMovgestTsFin itsiac,MovimentoGestione it){
		List<SiacRMovgestTsAttoAmmFin> listaSiacRMovgestTsAttoAmm = itsiac.getSiacRMovgestTsAttoAmms();
		it = settaDatiAttoAmministrativo(listaSiacRMovgestTsAttoAmm, it);
		return it;
	}
	
	/**
	 * Ritorna null se non c'e' struttura amministrativa (possibile)
	 * @param siacRMovgestTsAttoAmm
	 * @return
	 */
	public static Integer estraiIdStrutturaAmministrativa(SiacTAttoAmmFin siacTAttoAmm){
		//EVENTUALE STRUTTURA AMMINISTRATIVA:
		Integer uidStruttura = null;
		if(siacTAttoAmm!=null && siacTAttoAmm.getSiacRAttoAmmClasses()!=null){
			SiacRAttoAmmClassFin legameValido = DatiOperazioneUtil.getValido(siacTAttoAmm.getSiacRAttoAmmClasses(), null);
			if(legameValido!=null && legameValido.getSiacTClass()!=null){
				uidStruttura =  legameValido.getSiacTClass().getUid();
			}
		}
		return uidStruttura;
	}
	
	
	public static StrutturaAmministrativoContabile estraiStrutturaAmministrativa(SiacTAttoAmmFin siacTAttoAmm){
		//EVENTUALE STRUTTURA AMMINISTRATIVA:
		StrutturaAmministrativoContabile struttura = new StrutturaAmministrativoContabile();
		if(siacTAttoAmm!=null && siacTAttoAmm.getSiacRAttoAmmClasses()!=null){
			SiacRAttoAmmClassFin legameValido = DatiOperazioneUtil.getValido(siacTAttoAmm.getSiacRAttoAmmClasses(), null);
			if(legameValido!=null && legameValido.getSiacTClass()!=null){
				struttura.setUid(legameValido.getSiacTClass().getUid());
				struttura.setCodice(legameValido.getSiacTClass().getClassifCode());
				struttura.setDescrizione(legameValido.getSiacTClass().getClassifDesc());
			}
		}
		return struttura;
	}
	
	/**
	 * Wrapper del setta atto che pero' accetta il dto di ottimizzazione dove sono gia' presenti i record da cui attingere
	 * @param itsiac
	 * @param it
	 * @param ottimizzazioneDto
	 * @return
	 */
	public static MovimentoGestione settaDatiAttoAmmOPT(SiacTMovgestTsFin itsiac,MovimentoGestione it, OttimizzazioneMovGestDto ottimizzazioneDto){
		List<SiacRMovgestTsAttoAmmFin> listaSiacRMovgestTsAttoAmm = ottimizzazioneDto.filtraSiacRMovgestTsAttoAmmByMovgestTs(itsiac.getMovgestTsId());
		it = settaDatiAttoAmministrativo(listaSiacRMovgestTsAttoAmm, it);
		return it;
	}

	

	
	public static List<SubAccertamento> siacTMovgestTsEntityToSubAccertamentoModel(List<SiacTMovgestTsFin> dtos, List<SubAccertamento> subAccertamentos, SiacTMovgestFin siacTMovgest,OttimizzazioneMovGestDto ottimizzazioneDto) {
		
		//per non ripetere sempre il controllo di null pointer sull'oggetto ottimizzazioneDto intero ma solo sui sui contenuti:
		if(ottimizzazioneDto==null){
			ottimizzazioneDto = new OttimizzazioneMovGestDto();
		}
		//
		
		String methodName = "movimentoGestioneEntityToSubAccertamentoModel";
		for(SubAccertamento subAccertamento : subAccertamentos){
			int idSubAccertamento = subAccertamento.getUid();
			for(SiacTMovgestTsFin siacTMovgestTs : dtos){
				int idConfronto = siacTMovgestTs.getMovgestTsId();
				if(idSubAccertamento==idConfronto){
					
					subAccertamento.setAnnoAccertamentoPadre(siacTMovgest.getMovgestAnno());
					subAccertamento.setNumeroAccertamentoPadre(siacTMovgest.getMovgestNumero());
					
					
					// 1. ANNO_MOVIMENTO 
					// it.setAnnoMovimento(Integer.parseInt(siacTMovgest.getMovgestAnno()));
					subAccertamento.setAnnoMovimento(siacTMovgest.getMovgestAnno());
					// END ANNO_MOVIMENTO

					// 2. UTENTE_CREAZIONE + DATA_EMISSIONE
					subAccertamento.setUtenteCreazione(siacTMovgestTs.getLoginCreazione());
					subAccertamento.setDataEmissione(siacTMovgestTs.getDataCreazione());
					// support data emissione --> con questa modalita arriva correttamente al front end
					subAccertamento.setDataEmissioneSupport(siacTMovgestTs.getDataCreazione());
					// END UTENTE_CREAZIONE + DATA_EMISSIONE

					// 3. UTENTE_MODIFICA + DATA_MODIFICA
					subAccertamento.setUtenteModifica(siacTMovgestTs.getLoginModifica());
					subAccertamento.setDataModifica(siacTMovgestTs.getDataModifica());
					// END UTENTE_MODIFICA + DATA_MODIFICA

					// 4. UTENTE_CANCELLAZIONE + DATA_CANCELLAZIONE
					subAccertamento.setUtenteModifica(siacTMovgestTs.getLoginModifica());
					subAccertamento.setDataAnnullamento(siacTMovgestTs.getDataCancellazione());
					// END UTENTE_CANCELLAZIONE + DATA_CANCELLAZIONE

					// 5. DATA_SCADENZA
					subAccertamento.setDataScadenza(siacTMovgestTs.getMovgestTsScadenzaData());
					// END DATA_SCADENZA

					// 6. STATO_OPERATIVO_MOVIMENTO_GESTIONE_SPESA
					setStatoOperativoMovimentoGestione(siacTMovgestTs, subAccertamento, ottimizzazioneDto, false);
					// END STATO_OPERATIVO_MOVIMENTO_GESTIONE_SPESA

					// 7. IMPORTO_INIZIALE e IMPORTO_ATTUALE
					List<SiacTMovgestTsDetFin> listaSiacTMovgestTsDet = ottimizzazioneDto.filtraSiacTMovgestTsDetByMovgestTs(siacTMovgestTs.getMovgestTsId());
					if(listaSiacTMovgestTsDet!=null && listaSiacTMovgestTsDet.size()>0){
						//caricamento ottimizzato
						setImporti(subAccertamento, listaSiacTMovgestTsDet);
					} else {
						//caricamento classico
						setImporti(subAccertamento, siacTMovgestTs);
					}
					// END IMPORTO_INIZIALE e IMPORTO_ATTUALE

					// 8. ATTRIBUTI ESTRATTI DALLE TABELLE siac_r_movgest_ts_attr + siac_t_attr
					List<SiacRMovgestTsAttrFin> listaSiacRMovgestTsAttr = siacTMovgestTs.getSiacRMovgestTsAttrs();
					for(SiacRMovgestTsAttrFin siacRMovgestTsAttr : listaSiacRMovgestTsAttr){
						if(null!=siacRMovgestTsAttr && siacRMovgestTsAttr.getDataFineValidita()==null){
							String codiceAttributo = siacRMovgestTsAttr.getSiacTAttr().getAttrCode();
							AttributoMovimentoGestione attributoMovimentoGestione = CostantiFin.attributoMovimentoGestioneStringToEnum(codiceAttributo);
							switch (attributoMovimentoGestione) {
							
								case annoCapitoloOrigine:
									if(siacRMovgestTsAttr.getTesto() != null){
										if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
											subAccertamento.setAnnoCapitoloOrigine(Integer.parseInt(siacRMovgestTsAttr.getTesto()));
										}
									}
									break;

								case annoOriginePlur:
									if(siacRMovgestTsAttr.getTesto() != null){
										if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
											subAccertamento.setAnnoOriginePlur(Integer.parseInt(siacRMovgestTsAttr.getTesto()));
										}
									
									}										
									break;

								case annoRiaccertato:
									if(siacRMovgestTsAttr.getTesto() != null){
										if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
											subAccertamento.setAnnoRiaccertato(Integer.parseInt(siacRMovgestTsAttr.getTesto()));
										}
										
									}
									break;

								case numeroArticoloOrigine:
									if(siacRMovgestTsAttr.getTesto() != null){
										if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
											subAccertamento.setNumeroArticoloOrigine(Integer.parseInt(siacRMovgestTsAttr.getTesto()));	
										}
										
									}									
									break;

								case flagDaRiaccertamento:
									if(null!=siacRMovgestTsAttr.getBoolean_()){
										// if("S".equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_()) || "s".equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_())){
										if(CostantiFin.TRUE.equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_())){
											subAccertamento.setFlagDaRiaccertamento(true);
										}else {
											subAccertamento.setFlagDaRiaccertamento(false);
										}
									}												
									break;
									
									
								//SIAC-6997
								case flagDaReanno:
									if(null!=siacRMovgestTsAttr.getBoolean_()){
										// if("S".equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_()) || "s".equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_())){
										if(CostantiFin.TRUE.equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_())){
											subAccertamento.setFlagDaReanno(true);
										}else {
											subAccertamento.setFlagDaReanno(false);
										}
									}												
									break;
									
									
								case FlagAttivaGsa:
									if(null!=siacRMovgestTsAttr.getBoolean_()){
										if(CostantiFin.TRUE.equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_())){
											subAccertamento.setFlagAttivaGsa(true);
										}else {
											subAccertamento.setFlagAttivaGsa(false);
										}
									}												
									break;

								case numeroCapitoloOrigine:
									if(siacRMovgestTsAttr.getTesto() != null){
										if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
											subAccertamento.setNumeroCapitoloOrigine(Integer.parseInt(siacRMovgestTsAttr.getTesto()));	
										}
									}						
									break;

								case numeroOriginePlur:
									if(siacRMovgestTsAttr.getTesto() != null){
										if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
											subAccertamento.setNumeroOriginePlur(new BigDecimal(Integer.parseInt(siacRMovgestTsAttr.getTesto())));
										}
										
									}
									break;

								case numeroRiaccertato:
									if(siacRMovgestTsAttr.getTesto() != null){
										if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
											subAccertamento.setNumeroRiaccertato(new BigDecimal(Integer.parseInt(siacRMovgestTsAttr.getTesto())));
										}
									}
									break;

								case numeroUEBOrigine:
									if(siacRMovgestTsAttr.getTesto() != null){
										if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
											subAccertamento.setNumeroUEBOrigine(Integer.parseInt(siacRMovgestTsAttr.getTesto()));
										}
									
									} 
									break;

								case validato:
									if(null!=siacRMovgestTsAttr.getBoolean_()){
										// if("S".equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_()) || "s".equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_())){
										if(CostantiFin.TRUE.equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_())){
											subAccertamento.setValidato(true);
										}else {
											subAccertamento.setValidato(false);
										}
									}
									break;
	
								default:
									break;
							}
						}									
					}
					// END ATTRIBUTI ESTRATTI DALLE TABELLE siac_r_movgest_ts_attr + siac_t_attr

					// 9 CLASSE_SOGGETTO
					List<SiacRMovgestTsSogclasseFin> listaSiacRMovgestTsSogclasse = siacTMovgestTs.getSiacRMovgestTsSogclasses();
					if(listaSiacRMovgestTsSogclasse != null && !listaSiacRMovgestTsSogclasse.isEmpty()){
						for(SiacRMovgestTsSogclasseFin siacRMovgestTsSogclasse : listaSiacRMovgestTsSogclasse){
							if(null!=siacRMovgestTsSogclasse && siacRMovgestTsSogclasse.getDataFineValidita()==null){
								ClasseSoggetto classeSoggetto = new ClasseSoggetto();
								classeSoggetto.setCodice(siacRMovgestTsSogclasse.getSiacDSoggettoClasse().getSoggettoClasseCode());
								classeSoggetto.setDescrizione(siacRMovgestTsSogclasse.getSiacDSoggettoClasse().getSoggettoClasseDesc());
								subAccertamento.setClasseSoggetto(classeSoggetto);
							}
						}
					}
					// END CLASSE_SOGGETTO

					//  SOGGETTO ASSOCIATO
					if(siacTMovgestTs.getSiacRMovgestTsSogs() != null && !siacTMovgestTs.getSiacRMovgestTsSogs().isEmpty()){
						for(SiacRMovgestTsSogFin rMovGestTsSog : siacTMovgestTs.getSiacRMovgestTsSogs()){
							if(rMovGestTsSog.getDataFineValidita()==null){
								subAccertamento.setSoggettoCode(rMovGestTsSog.getSiacTSoggetto().getSoggettoCode());
								break;
							}
						}
					} 
					// END SOGGETTO ASSOCIATO
					
					// 10 PROGETTO
					List<SiacRMovgestTsProgrammaFin> listaSiacRMovgestTsProgramma = siacTMovgestTs.getSiacRMovgestTsProgrammas();
					if(listaSiacRMovgestTsProgramma != null && !listaSiacRMovgestTsProgramma.isEmpty()){
						for(SiacRMovgestTsProgrammaFin siacRMovgestTsProgramma : listaSiacRMovgestTsProgramma){
							if(null!=siacRMovgestTsProgramma && siacRMovgestTsProgramma.getDataFineValidita()==null){
								//setto il codice
								Progetto progetto = new Progetto();
								progetto.setUid(siacRMovgestTsProgramma.getSiacTProgramma().getProgrammaId());
								progetto.setCodice(siacRMovgestTsProgramma.getSiacTProgramma().getProgrammaCode());
								progetto.setDescrizione(siacRMovgestTsProgramma.getSiacTProgramma().getProgrammaDesc());
								subAccertamento.setProgetto(progetto);
							}									
						}
					}
					// END PROGETTO

					// 11 TIPO IMPEGNO
					List<SiacRMovgestClassFin> listaSiacRMovgestClass = siacTMovgestTs.getSiacRMovgestClasses();
					for(SiacRMovgestClassFin siacRMovgestClass : listaSiacRMovgestClass){
						if(null!=siacRMovgestClass && siacRMovgestClass.getDataFineValidita()==null){
							if(siacRMovgestClass.getSiacTClass().getSiacDClassTipo().getClassifTipoCode().equalsIgnoreCase(CostantiFin.D_CLASS_TIPO_TIPO_IMPEGNO)){
								// TIPO
								ClassificatoreGenerico tipoImpegno = new ClassificatoreGenerico();
								tipoImpegno.setUid(siacRMovgestClass.getSiacTClass().getClassifId());
								tipoImpegno.setCodice(siacRMovgestClass.getSiacTClass().getClassifCode());
								tipoImpegno.setDescrizione(siacRMovgestClass.getSiacTClass().getClassifDesc());
								subAccertamento.setTipoImpegno(tipoImpegno);
							}
						}									
					}
					// END TIPO IMPEGNO
					
					//DATI TRANSAZIONE ELEMENTARE:
					subAccertamento = (SubAccertamento) TransazioneElementareEntityToModelConverter.
							convertiDatiTransazioneElementare(subAccertamento, listaSiacRMovgestClass,listaSiacRMovgestTsAttr);
					//END DATI TRANSAZIONE ELEMENTARE

					// 12 DESCRIZONE
					subAccertamento.setDescrizione(siacTMovgestTs.getMovgestTsDesc());
					// END DESCRIZONE

					// 13. ATTO AMMINISTRATIVO
					subAccertamento = (SubAccertamento) settaDatiAttoAmm(siacTMovgestTs, subAccertamento);
					// END ATTO AMMINISTRATIVO

					// 14. ALTRI POSSIBILI CAMPI
					// END ALTRI POSSIBILI CAMPI	

					break;
				}
			}
		}
		return subAccertamentos;
	}
	
	public static List<SubAccertamento> siacTMovgestTsEntityToSubAccertamentoModelOPT(List<SiacTMovgestTsFin> dtos, List<SubAccertamento> subAccertamentos,
			SiacTMovgestFin siacTMovgest,OttimizzazioneMovGestDto ottimizzazioneDto) {
		String methodName = "siacTMovgestTsEntityToSubAccertamentoModelOPT";
		for(SubAccertamento it : subAccertamentos){
			int idIterato = it.getUid();
			for(SiacTMovgestTsFin itsiac : dtos){
				int idConfronto = itsiac.getMovgestTsId();
				Integer movgestTsId = itsiac.getMovgestTsId();
				if(idIterato==idConfronto){
					
					
					it.setAnnoAccertamentoPadre(siacTMovgest.getMovgestAnno());
					it.setNumeroAccertamentoPadre(siacTMovgest.getMovgestNumero());
					
					
					// 1. ANNO_MOVIMENTO 
					// it.setAnnoMovimento(Integer.parseInt(siacTMovgest.getMovgestAnno()));
					it.setAnnoMovimento(siacTMovgest.getMovgestAnno());
					// END ANNO_MOVIMENTO

					// 2. UTENTE_CREAZIONE + DATA_EMISSIONE
					it.setUtenteCreazione(itsiac.getLoginCreazione());
					it.setDataEmissione(itsiac.getDataCreazione());
					// support data emissione --> con questa modalita arriva correttamente al front end
					it.setDataEmissioneSupport(itsiac.getDataCreazione());
					// END UTENTE_CREAZIONE + DATA_EMISSIONE

					// 3. UTENTE_MODIFICA + DATA_MODIFICA
					it.setUtenteModifica(itsiac.getLoginModifica());
					it.setDataModifica(itsiac.getDataModifica());
					// END UTENTE_MODIFICA + DATA_MODIFICA

					// 4. UTENTE_CANCELLAZIONE + DATA_CANCELLAZIONE
					it.setUtenteModifica(itsiac.getLoginModifica());
					it.setDataAnnullamento(itsiac.getDataCancellazione());
					// END UTENTE_CANCELLAZIONE + DATA_CANCELLAZIONE

					// 5. DATA_SCADENZA
					it.setDataScadenza(itsiac.getMovgestTsScadenzaData());
					// END DATA_SCADENZA

					// 6. STATO_OPERATIVO_MOVIMENTO_GESTIONE_SPESA
					setStatoOperativoMovimentoGestione(itsiac, it, ottimizzazioneDto, true);
					// END STATO_OPERATIVO_MOVIMENTO_GESTIONE_SPESA

					// 7. IMPORTO_INIZIALE e IMPORTO_ATTUALE
					List<SiacTMovgestTsDetFin> listaSiacTMovgestTsDet = ottimizzazioneDto.filtraSiacTMovgestTsDetByMovgestTs(movgestTsId);
					it = setImporti(it, listaSiacTMovgestTsDet);
					// END IMPORTO_INIZIALE e IMPORTO_ATTUALE
					
					// 8. ATTRIBUTI ESTRATTI DALLE TABELLE siac_r_movgest_ts_attr + siac_t_attr
					List<SiacRMovgestTsAttrFin> listaSiacRMovgestTsAttr = ottimizzazioneDto.filtraSiacRMovgestTsAttrByMovgestTs(movgestTsId);
					for(SiacRMovgestTsAttrFin siacRMovgestTsAttr : listaSiacRMovgestTsAttr){
						if(null!=siacRMovgestTsAttr && siacRMovgestTsAttr.getDataFineValidita()==null){
							String codiceAttributo = siacRMovgestTsAttr.getSiacTAttr().getAttrCode();
							AttributoMovimentoGestione attributoMovimentoGestione = CostantiFin.attributoMovimentoGestioneStringToEnum(codiceAttributo);
							switch (attributoMovimentoGestione) {
							
								case annoCapitoloOrigine:
									if(siacRMovgestTsAttr.getTesto() != null){
										if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
											it.setAnnoCapitoloOrigine(Integer.parseInt(siacRMovgestTsAttr.getTesto()));
										}
									}
									break;

								case annoOriginePlur:
									if(siacRMovgestTsAttr.getTesto() != null){
										if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
											it.setAnnoOriginePlur(Integer.parseInt(siacRMovgestTsAttr.getTesto()));
										}
									
									}										
									break;

								case annoRiaccertato:
									if(siacRMovgestTsAttr.getTesto() != null){
										if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
											it.setAnnoRiaccertato(Integer.parseInt(siacRMovgestTsAttr.getTesto()));
										}
										
									}
									break;

								case numeroArticoloOrigine:
									if(siacRMovgestTsAttr.getTesto() != null){
										if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
											it.setNumeroArticoloOrigine(Integer.parseInt(siacRMovgestTsAttr.getTesto()));	
										}
										
									}									
									break;

								case flagDaRiaccertamento:
									if(null!=siacRMovgestTsAttr.getBoolean_()){
										// if("S".equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_()) || "s".equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_())){
										if(CostantiFin.TRUE.equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_())){
											it.setFlagDaRiaccertamento(true);
										}else {
											it.setFlagDaRiaccertamento(false);
										}
									}												
									break;
								
								//SIAC-6997	
								case flagDaReanno:
									if(null!=siacRMovgestTsAttr.getBoolean_()){
										if(CostantiFin.TRUE.equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_())){
											it.setFlagDaReanno(true);
										}else {
											it.setFlagDaReanno(false);
										}
									}												
									break;	

								case numeroCapitoloOrigine:
									if(siacRMovgestTsAttr.getTesto() != null){
										if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
											it.setNumeroCapitoloOrigine(Integer.parseInt(siacRMovgestTsAttr.getTesto()));	
										}
									}						
									break;

								case numeroOriginePlur:
									if(siacRMovgestTsAttr.getTesto() != null){
										if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
											it.setNumeroOriginePlur(new BigDecimal(Integer.parseInt(siacRMovgestTsAttr.getTesto())));
										}
										
									}
									break;

								case numeroRiaccertato:
									if(siacRMovgestTsAttr.getTesto() != null){
										if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
											it.setNumeroRiaccertato(new BigDecimal(Integer.parseInt(siacRMovgestTsAttr.getTesto())));
										}
									}
									break;

								case numeroUEBOrigine:
									if(siacRMovgestTsAttr.getTesto() != null){
										if(!siacRMovgestTsAttr.getTesto().equalsIgnoreCase("null") && !StringUtilsFin.isEmpty(siacRMovgestTsAttr.getTesto())){
											it.setNumeroUEBOrigine(Integer.parseInt(siacRMovgestTsAttr.getTesto()));
										}
									
									} 
									break;

								case validato:
									if(null!=siacRMovgestTsAttr.getBoolean_()){
										// if("S".equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_()) || "s".equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_())){
										if(CostantiFin.TRUE.equalsIgnoreCase(siacRMovgestTsAttr.getBoolean_())){
											it.setValidato(true);
										}else {
											it.setValidato(false);
										}
									}
									break;
	
								default:
									break;
							}
						}									
					}
					// END ATTRIBUTI ESTRATTI DALLE TABELLE siac_r_movgest_ts_attr + siac_t_attr
					
					// 9 CLASSE_SOGGETTO
					List<SiacRMovgestTsSogclasseFin> listaSiacRMovgestTsSogclasse = ottimizzazioneDto.filtraSiacRMovgestTsSogclasseByMovgestTs(movgestTsId);
					if(listaSiacRMovgestTsSogclasse != null && !listaSiacRMovgestTsSogclasse.isEmpty()){
						for(SiacRMovgestTsSogclasseFin siacRMovgestTsSogclasse : listaSiacRMovgestTsSogclasse){
							if(null!=siacRMovgestTsSogclasse && siacRMovgestTsSogclasse.getDataFineValidita()==null){
								ClasseSoggetto classeSoggetto = new ClasseSoggetto();
								classeSoggetto.setCodice(siacRMovgestTsSogclasse.getSiacDSoggettoClasse().getSoggettoClasseCode());
								classeSoggetto.setDescrizione(siacRMovgestTsSogclasse.getSiacDSoggettoClasse().getSoggettoClasseDesc());
								it.setClasseSoggetto(classeSoggetto);
							}
						}
					}
					// END CLASSE_SOGGETTO

					// 10 PROGETTO
					List<SiacRMovgestTsProgrammaFin> listaSiacRMovgestTsProgramma = ottimizzazioneDto.filtraSiacRMovgestTsProgrammaByMovgestTs(movgestTsId);
					if(listaSiacRMovgestTsProgramma != null && !listaSiacRMovgestTsProgramma.isEmpty()){
						for(SiacRMovgestTsProgrammaFin siacRMovgestTsProgramma : listaSiacRMovgestTsProgramma){
							if(null!=siacRMovgestTsProgramma && siacRMovgestTsProgramma.getDataFineValidita()==null){
								//setto il codice
								Progetto progetto = new Progetto();
								progetto.setUid(siacRMovgestTsProgramma.getSiacTProgramma().getProgrammaId());
								progetto.setCodice(siacRMovgestTsProgramma.getSiacTProgramma().getProgrammaCode());
								progetto.setDescrizione(siacRMovgestTsProgramma.getSiacTProgramma().getProgrammaDesc());
								it.setProgetto(progetto);
							}									
						}
					}
					// END PROGETTO
					
					// 11 TIPO IMPEGNO
					List<SiacRMovgestClassFin> listaSiacRMovgestClass = ottimizzazioneDto.filtraSiacRMovgestClassByMovgestTs(movgestTsId);
					for(SiacRMovgestClassFin siacRMovgestClass : listaSiacRMovgestClass){
						if(null!=siacRMovgestClass && siacRMovgestClass.getDataFineValidita()==null){
							if(siacRMovgestClass.getSiacTClass().getSiacDClassTipo().getClassifTipoCode().equalsIgnoreCase(CostantiFin.D_CLASS_TIPO_TIPO_IMPEGNO)){
								// TIPO
								ClassificatoreGenerico tipoImpegno = new ClassificatoreGenerico();
								tipoImpegno.setUid(siacRMovgestClass.getSiacTClass().getClassifId());
								tipoImpegno.setCodice(siacRMovgestClass.getSiacTClass().getClassifCode());
								tipoImpegno.setDescrizione(siacRMovgestClass.getSiacTClass().getClassifDesc());
								it.setTipoImpegno(tipoImpegno);
							}
						}									
					}
					// END TIPO IMPEGNO
					
					//DATI TRANSAZIONE ELEMENTARE:
					it = (SubAccertamento) TransazioneElementareEntityToModelConverter.
							convertiDatiTransazioneElementare(it, listaSiacRMovgestClass,listaSiacRMovgestTsAttr);
					//END DATI TRANSAZIONE ELEMENTARE

					
					// 12 DESCRIZONE
					it.setDescrizione(itsiac.getMovgestTsDesc());
					// END DESCRIZONE

					// 13. ATTO AMMINISTRATIVO
					it = (SubAccertamento) settaDatiAttoAmmOPT(itsiac, it,ottimizzazioneDto);
					// END ATTO AMMINISTRATIVO

					// 14. ALTRI POSSIBILI CAMPI
					// END ALTRI POSSIBILI CAMPI	
					break;
				}
			}
		}
		return subAccertamentos;
	}

	/**
	 * Wrapper di retro-compatibilita'
	 * @param dto
	 * @param modificaMovimentoGestioneSpesa
	 * @return
	 */
	public static ModificaMovimentoGestioneSpesa siacTModificaEntityToModificaMovimentoGestioneSpesaModel(SiacTModificaFin dto, ModificaMovimentoGestioneSpesa modificaMovimentoGestioneSpesa) {
		return siacTModificaEntityToModificaMovimentoGestioneSpesaModel(dto,modificaMovimentoGestioneSpesa,null);
	}

	public static ModificaMovimentoGestioneSpesa siacTModificaEntityToModificaMovimentoGestioneSpesaModel(SiacTModificaFin dto, ModificaMovimentoGestioneSpesa modificaMovimentoGestioneSpesa, OttimizzazioneModificheMovimentoGestioneDto ottimizzazioneModDto) {
		String methodName = "siacTModificaEntityToModificaMovimentoGestioneSpesaModel";
		List<SiacTModificaFin> dtos = new ArrayList<SiacTModificaFin>();
		dtos.add(dto);
		List<ModificaMovimentoGestioneSpesa> movimentos = new ArrayList<ModificaMovimentoGestioneSpesa>();
		movimentos.add(modificaMovimentoGestioneSpesa);
		movimentos = siacTModificaEntityToModificaMovimentoGestioneSpesaModel(dtos, movimentos, ottimizzazioneModDto);
		return movimentos.get(0);
	}
	
	public static List<ModificaMovimentoGestioneSpesa> siacTModificaEntityToModificaMovimentoGestioneSpesaModel(List<SiacTModificaFin> dtos, List<ModificaMovimentoGestioneSpesa> movimentos, OttimizzazioneModificheMovimentoGestioneDto ottimizzazioneModDto) {
		String methodName = "siacTModificaEntityToModificaMovimentoGestioneSpesaModel";
		for(ModificaMovimentoGestioneSpesa it : movimentos){
			int idIterato = it.getIdModificaMovimentoGestione();
			for(SiacTModificaFin itsiac : dtos){
				int idConfronto = itsiac.getModId();
				if(idIterato==idConfronto){
					// Data creazione
					it.setDataEmissione(itsiac.getDataCreazione());
					it.setDataEmissioneSupport(itsiac.getDataCreazione());
					
					//Dati atto amministrativo:
					it = (ModificaMovimentoGestioneSpesa) settaDatiAttoAmministrativo(itsiac.getSiacTAttoAmm(), it);
					
					
					// 4. STATO_MODIFICA
					List<SiacRModificaStatoFin> elencoSiacRModificaStato = null;
					
					if(ottimizzazioneModDto!=null){
						//Ramo ottimizzato
						elencoSiacRModificaStato = ottimizzazioneModDto.filtraSiacRModificaStatoFinBySiacTModifica(itsiac);
					} else {
						//Ramo classico
						elencoSiacRModificaStato = itsiac.getSiacRModificaStatos();
					}
						
					
					if(elencoSiacRModificaStato!=null && elencoSiacRModificaStato.size()>0){
						for(SiacRModificaStatoFin siacRModificaStato : elencoSiacRModificaStato){
							if(null!=siacRModificaStato && siacRModificaStato.getDataFineValidita() == null){
								StatoOperativoModificaMovimentoGestione statoOperativoModificaMovimentoGestione = CostantiFin.statoOperativoModificaMovimentoGestioneStringToEnum(siacRModificaStato.getSiacDModificaStato().getModStatoCode());
								it.setStatoOperativoModificaMovimentoGestione(statoOperativoModificaMovimentoGestione);
								//risoluzione anomalia descrizione stato al posto del codice
								it.setCodiceStatoOperativoModificaMovimentoGestione(siacRModificaStato.getSiacDModificaStato().getModStatoCode());

								//SIAC-8118 passo la data della modifica dello stato opertivo 
								it.setDataFromStatoOperativo(siacRModificaStato.getDataModifica());
								
								List<SiacTMovgestTsDetModFin> listaSiacTMovgestTsDetMod = null;
								if(ottimizzazioneModDto!=null){
									//Ramo ottimizzato
									listaSiacTMovgestTsDetMod = ottimizzazioneModDto.filtraSiacTMovgestTsDetModFinBySiacRModificaStato(siacRModificaStato);
								} else {
									//Ramo classico
									 listaSiacTMovgestTsDetMod = siacRModificaStato.getSiacTMovgestTsDetMods();
								}
								
								if(null!=listaSiacTMovgestTsDetMod && listaSiacTMovgestTsDetMod.size() > 0){
									for(SiacTMovgestTsDetModFin siacTMovgestTsDetMod : listaSiacTMovgestTsDetMod){
										if(null!=siacTMovgestTsDetMod && siacTMovgestTsDetMod.getDataFineValidita() == null){
//											it.setImportoNew(siacTMovgestTsDetMod.getMovgestTsDetImporto());
//											it.setImportoOld(siacTMovgestTsDetMod.getSiacTMovgestTsDet().getMovgestTsDetImporto());	
											it.setImportoNew(siacTMovgestTsDetMod.getSiacTMovgestTsDet().getMovgestTsDetImporto());
											it.setImportoOld(siacTMovgestTsDetMod.getMovgestTsDetImporto());
											it.setReimputazione(siacTMovgestTsDetMod.getMtdmReimputazioneFlag());
											it.setAnnoReimputazione(siacTMovgestTsDetMod.getMtdmReimputazioneAnno());
											
											//SIAC-6865
											it.setFlagAggiudicazione(siacTMovgestTsDetMod.getMtdmAggiudicazioneFlag());
											//SIAC-7838
											it.setFlagAggiudicazioneSenzaSoggetto(siacTMovgestTsDetMod.getMtdmAggiudicazioneSenzaSog());

											if(siacTMovgestTsDetMod.getSiacTSoggettoAggiudicazione() != null) {
												it.setSoggettoAggiudicazione(new Soggetto());
												it.getSoggettoAggiudicazione().setUid(siacTMovgestTsDetMod.getSiacTSoggettoAggiudicazione().getSoggettoId());
												it.getSoggettoAggiudicazione().setCodiceSoggetto(siacTMovgestTsDetMod.getSiacTSoggettoAggiudicazione().getSoggettoCode());
												it.getSoggettoAggiudicazione().setDenominazione(siacTMovgestTsDetMod.getSiacTSoggettoAggiudicazione().getSoggettoDesc());
											}
											if(siacTMovgestTsDetMod.getSiacDSoggettoClasseAggiudicazione() != null) {
												it.setClasseSoggettoAggiudicazione(new ClasseSoggetto());
												it.getClasseSoggettoAggiudicazione().setUid(siacTMovgestTsDetMod.getSiacDSoggettoClasseAggiudicazione().getUid());
												it.getClasseSoggettoAggiudicazione().setCodice(siacTMovgestTsDetMod.getSiacDSoggettoClasseAggiudicazione().getSoggettoClasseCode());
												it.getClasseSoggettoAggiudicazione().setDescrizione(siacTMovgestTsDetMod.getSiacDSoggettoClasseAggiudicazione().getSoggettoClasseDesc());
											}

										}
									}
								}
								

								List<SiacRMovgestTsSogModFin> listaSiacRMovgestTsSogMod = null;
								if(ottimizzazioneModDto!=null){
									//Ramo ottimizzato
									listaSiacRMovgestTsSogMod = ottimizzazioneModDto.filtraSiacRMovgestTsSogModFinBySiacRModificaStato(siacRModificaStato);
								} else {
									//Ramo classico
									listaSiacRMovgestTsSogMod = siacRModificaStato.getSiacRMovgestTsSogMods();
								}
								
								if(null!=listaSiacRMovgestTsSogMod && listaSiacRMovgestTsSogMod.size() > 0){
									for(SiacRMovgestTsSogModFin siacRMovgestTsSogMod : listaSiacRMovgestTsSogMod){
										if(siacRMovgestTsSogMod != null && siacRMovgestTsSogMod.getDataFineValidita() == null 
												&& siacRMovgestTsSogMod.getDataCancellazione() == null && siacRMovgestTsSogMod.getDataCreazione()!=null){										
											if(siacRMovgestTsSogMod.getSiacTSoggetto2() != null && !StringUtilsFin.isEmpty(siacRMovgestTsSogMod.getSiacTSoggetto2().getSoggettoCode())){
												it.setNewSoggettoCodeMovimentoGestione(siacRMovgestTsSogMod.getSiacTSoggetto2().getSoggettoCode());
											}
											it.setOldSoggettoCodeMovimentoGestione(siacRMovgestTsSogMod.getSiacTSoggetto1().getSoggettoCode());
										}
									}
								}
								
								
								List<SiacRMovgestTsSogclasseModFin> listaSiacRMovgestTsSogclasseMod = null;
								if(ottimizzazioneModDto != null){
									//Ramo ottimizzato
									listaSiacRMovgestTsSogclasseMod = ottimizzazioneModDto.filtraSiacRMovgestTsSogclasseModFinBySiacRModificaStato(siacRModificaStato);
								} else {
									//Ramo classico
									listaSiacRMovgestTsSogclasseMod = siacRModificaStato.getSiacRMovgestTsSogclasseMods();
								}
								
								if(listaSiacRMovgestTsSogclasseMod != null && listaSiacRMovgestTsSogclasseMod.size() > 0){
									for(SiacRMovgestTsSogclasseModFin siacRMovgestTsSogclasseMod : listaSiacRMovgestTsSogclasseMod){
										if(siacRMovgestTsSogclasseMod != null && siacRMovgestTsSogclasseMod.getDataFineValidita() == null 
												&& siacRMovgestTsSogclasseMod.getDataCancellazione() == null && siacRMovgestTsSogclasseMod.getSiacDSoggettoClasse1() != null){
											it.setIdClasseSoggettoOldMovimentoGestione(siacRMovgestTsSogclasseMod.getSiacDSoggettoClasse1().getSoggettoClasseId());
											if(siacRMovgestTsSogclasseMod.getSiacDSoggettoClasse2() != null && siacRMovgestTsSogclasseMod.getSiacDSoggettoClasse2().getSoggettoClasseId()!=0){
												it.setIdClasseSoggettoNewMovimentoGestione(siacRMovgestTsSogclasseMod.getSiacDSoggettoClasse2().getSoggettoClasseId());
											}	
										}
									}
								}
							}
						}
					}
					// END STATO_MODIFICA

					// 5. TIPO_MODIFICA
					SiacDModificaTipoFin siacDModificaTipo = itsiac.getSiacDModificaTipo();
					if(null!=siacDModificaTipo && siacDModificaTipo.getDataFineValidita() == null){
//						TipoModificaMovimentoGestione tipoModificaMovimentoGestione = CostantiFin.tipoModificaMovimentoGestioneStringToEnum(siacDModificaTipo.getModTipoCode());
//						it.setTipoModificaMovimentoGestione(tipoModificaMovimentoGestione);
						it.setTipoModificaMovimentoGestione(siacDModificaTipo.getModTipoCode());
						it.setTipoMovimentoDesc(siacDModificaTipo.getModTipoDesc());
					}
					// END TIPO_MODIFICA
					
					// 6. ALTRI POSSIBILI CAMPI
					it.setNumeroModificaMovimentoGestione(itsiac.getModNum());
					//SIAC-6997
					it.setElaboraRorReanno(itsiac.getElabRorReanno() != null ? itsiac.getElabRorReanno() : false);
					// END ALTRI POSSIBILI CAMPI	

					break;
				}
			}
		}
		return movimentos;
	}
	
	/**
	 * Wrapper di retro compatibilita'
	 * @param dto
	 * @param modificaMovimentoGestioneEntrata
	 * @return
	 */
	public static ModificaMovimentoGestioneEntrata siacTModificaEntityToModificaMovimentoGestioneEntrataModel(SiacTModificaFin dto, ModificaMovimentoGestioneEntrata modificaMovimentoGestioneEntrata) {
		return siacTModificaEntityToModificaMovimentoGestioneEntrataModel(dto, modificaMovimentoGestioneEntrata, null);
	}
	
	public static ModificaMovimentoGestioneEntrata siacTModificaEntityToModificaMovimentoGestioneEntrataModel(SiacTModificaFin dto, ModificaMovimentoGestioneEntrata modificaMovimentoGestioneEntrata,OttimizzazioneModificheMovimentoGestioneDto ottimizzazioneModDto) {
		String methodName = "siacTModificaEntityToModificaMovimentoGestioneEntrataModel";
		List<SiacTModificaFin> dtos = new ArrayList<SiacTModificaFin>();
		dtos.add(dto);
		List<ModificaMovimentoGestioneEntrata> movimentos = new ArrayList<ModificaMovimentoGestioneEntrata>();
		movimentos.add(modificaMovimentoGestioneEntrata);
		movimentos = siacTModificaEntityToModificaMovimentoGestioneEntrataModel(dtos, movimentos,ottimizzazioneModDto);
		return movimentos.get(0);
	}
	
	public static List<ModificaMovimentoGestioneEntrata> siacTModificaEntityToModificaMovimentoGestioneEntrataModel(List<SiacTModificaFin> dtos, List<ModificaMovimentoGestioneEntrata> movimentos,OttimizzazioneModificheMovimentoGestioneDto ottimizzazioneModDto) {
		String methodName = "siacTModificaEntityToModificaMovimentoGestioneEntrataModel";
		for(ModificaMovimentoGestioneEntrata it : movimentos){
			int idIterato = it.getIdModificaMovimentoGestione();
			for(SiacTModificaFin itsiac : dtos){
				int idConfronto = itsiac.getModId();
				if(idIterato==idConfronto){

					//Dati atto amministrativo:
					it = (ModificaMovimentoGestioneEntrata) settaDatiAttoAmministrativo(itsiac.getSiacTAttoAmm(), it);
					
					// Data creazione
					it.setDataEmissione(itsiac.getDataCreazione());
					it.setDataEmissioneSupport(itsiac.getDataCreazione());
					
					// 4. STATO_MODIFICA
					List<SiacRModificaStatoFin> elencoSiacRModificaStato = null;
					
					if(ottimizzazioneModDto!=null){
						//Ramo ottimizzato
						elencoSiacRModificaStato = ottimizzazioneModDto.filtraSiacRModificaStatoFinBySiacTModifica(itsiac);
					} else {
						//Ramo classico
						elencoSiacRModificaStato = itsiac.getSiacRModificaStatos();
					}
					
					for(SiacRModificaStatoFin siacRModificaStato : elencoSiacRModificaStato){
						if(null!=siacRModificaStato && siacRModificaStato.getDataFineValidita() == null){
							//risoluzione anomalia descrizione stato al posto del codice
							it.setCodiceStatoOperativoModificaMovimentoGestione(siacRModificaStato.getSiacDModificaStato().getModStatoCode());
							StatoOperativoModificaMovimentoGestione statoOperativoModificaMovimentoGestione = CostantiFin.statoOperativoModificaMovimentoGestioneStringToEnum(siacRModificaStato.getSiacDModificaStato().getModStatoCode());
							
							it.setStatoOperativoModificaMovimentoGestione(statoOperativoModificaMovimentoGestione);
							
							//SIAC-8118 passo la data della modifica dello stato opertivo 
							it.setDataFromStatoOperativo(siacRModificaStato.getDataModifica());
							
							List<SiacTMovgestTsDetModFin> listaSiacTMovgestTsDetMod = null;
							if(ottimizzazioneModDto!=null){
								//Ramo ottimizzato
								listaSiacTMovgestTsDetMod = ottimizzazioneModDto.filtraSiacTMovgestTsDetModFinBySiacRModificaStato(siacRModificaStato);
							} else {
								//Ramo classico
								 listaSiacTMovgestTsDetMod = siacRModificaStato.getSiacTMovgestTsDetMods();
							}
							
							if(null!=listaSiacTMovgestTsDetMod && listaSiacTMovgestTsDetMod.size() > 0){
								for(SiacTMovgestTsDetModFin siacTMovgestTsDetMod : listaSiacTMovgestTsDetMod){
									if(null!=siacTMovgestTsDetMod && siacTMovgestTsDetMod.getDataFineValidita() == null){
//										it.setImportoNew(siacTMovgestTsDetMod.getMovgestTsDetImporto());
//										it.setImportoOld(siacTMovgestTsDetMod.getSiacTMovgestTsDet().getMovgestTsDetImporto());	
										it.setImportoNew(siacTMovgestTsDetMod.getSiacTMovgestTsDet().getMovgestTsDetImporto());
										it.setImportoOld(siacTMovgestTsDetMod.getMovgestTsDetImporto());
										it.setReimputazione(siacTMovgestTsDetMod.getMtdmReimputazioneFlag());
										it.setAnnoReimputazione(siacTMovgestTsDetMod.getMtdmReimputazioneAnno());
									}
								}
							}
							
							List<SiacRMovgestTsSogModFin> listaSiacRMovgestTsSogMod = null;
							if(ottimizzazioneModDto!=null){
								//Ramo ottimizzato
								listaSiacRMovgestTsSogMod = ottimizzazioneModDto.filtraSiacRMovgestTsSogModFinBySiacRModificaStato(siacRModificaStato);
							} else {
								//Ramo classico
								listaSiacRMovgestTsSogMod = siacRModificaStato.getSiacRMovgestTsSogMods();
							}
							
							if(null!=listaSiacRMovgestTsSogMod && listaSiacRMovgestTsSogMod.size() > 0){
								for(SiacRMovgestTsSogModFin siacRMovgestTsSogMod : listaSiacRMovgestTsSogMod){
									if(siacRMovgestTsSogMod != null && siacRMovgestTsSogMod.getDataFineValidita() == null 
											&& siacRMovgestTsSogMod.getDataCancellazione() == null && siacRMovgestTsSogMod.getDataCreazione()!=null){
										if(siacRMovgestTsSogMod.getSiacTSoggetto2() != null && !StringUtilsFin.isEmpty(siacRMovgestTsSogMod.getSiacTSoggetto2().getSoggettoCode())){
											it.setNewSoggettoCodeMovimentoGestione(siacRMovgestTsSogMod.getSiacTSoggetto2().getSoggettoCode());
										}
										it.setOldSoggettoCodeMovimentoGestione(siacRMovgestTsSogMod.getSiacTSoggetto1().getSoggettoCode());
									}
								}
							}
							
							List<SiacRMovgestTsSogclasseModFin> listaSiacRMovgestTsSogclasseMod = null;
							if(ottimizzazioneModDto!=null){
								//Ramo ottimizzato
								listaSiacRMovgestTsSogclasseMod = ottimizzazioneModDto.filtraSiacRMovgestTsSogclasseModFinBySiacRModificaStato(siacRModificaStato);
							} else {
								//Ramo classico
								listaSiacRMovgestTsSogclasseMod = siacRModificaStato.getSiacRMovgestTsSogclasseMods();
							}
							
							if(listaSiacRMovgestTsSogclasseMod != null && listaSiacRMovgestTsSogclasseMod.size() > 0){
								for(SiacRMovgestTsSogclasseModFin siacRMovgestTsSogclasseMod : listaSiacRMovgestTsSogclasseMod){
									if(siacRMovgestTsSogclasseMod != null && siacRMovgestTsSogclasseMod.getDataFineValidita() == null 
											&& siacRMovgestTsSogclasseMod.getDataCancellazione() == null && siacRMovgestTsSogclasseMod.getDataCreazione()!=null){
										it.setIdClasseSoggettoOldMovimentoGestione(siacRMovgestTsSogclasseMod.getSiacDSoggettoClasse1().getSoggettoClasseId());
										if(siacRMovgestTsSogclasseMod.getSiacDSoggettoClasse2()!=null && siacRMovgestTsSogclasseMod.getSiacDSoggettoClasse2().getSoggettoClasseId()!=0){
											it.setIdClasseSoggettoNewMovimentoGestione(siacRMovgestTsSogclasseMod.getSiacDSoggettoClasse2().getSoggettoClasseId());
										}
									}
								}
							}
						}
					}
					// END STATO_MODIFICA

					// 5. TIPO_MODIFICA
					SiacDModificaTipoFin siacDModificaTipo = itsiac.getSiacDModificaTipo();
					if(null!=siacDModificaTipo && siacDModificaTipo.getDataFineValidita() == null){
						//TipoModificaMovimentoGestione tipoModificaMovimentoGestione = CostantiFin.tipoModificaMovimentoGestioneStringToEnum(siacDModificaTipo.getModTipoCode());						
						//it.setTipoModificaMovimentoGestione(tipoModificaMovimentoGestione);
						it.setTipoModificaMovimentoGestione(siacDModificaTipo.getModTipoCode());
						it.setTipoMovimentoDesc(siacDModificaTipo.getModTipoDesc());
					}
					// END TIPO_MODIFICA
					
					// 6. ALTRI POSSIBILI CAMPI
					it.setNumeroModificaMovimentoGestione(itsiac.getModNum());
					//SIAC-6997
					it.setElaboraRorReanno(itsiac.getElabRorReanno() != null ? itsiac.getElabRorReanno() : false);
					// END ALTRI POSSIBILI CAMPI	

					break;
				}
			}
		}
		return movimentos;
	}
	
	
	
	
	
	
	public static <T extends SiacRClassBaseFin>
		CodificaFin convertiCodificaFromSiacTClass(List<T> listaSiacRClass, String tipoCodifica, boolean isEntrata){
	
		CodificaFin codifica = new CodificaFin();
		
		if(listaSiacRClass!=null && listaSiacRClass.size()>0){
			
			for(T siacRClass : listaSiacRClass){
				
				if(null!=siacRClass && siacRClass.getDataFineValidita()==null){
					
					String codice = siacRClass.getSiacTClass().getClassifCode();
					String desc = siacRClass.getSiacTClass().getClassifDesc();
					Integer siacTClassUid = siacRClass.getSiacTClass().getUid();
					
					String tipoCode = siacRClass.getSiacTClass().getSiacDClassTipo().getClassifTipoCode();
					
					if(tipoCode.equalsIgnoreCase(tipoCodifica) || StringUtilsFin.contenutoIn(tipoCode, CostantiFin.getCodiciPianoDeiConti())){
						// PIANO DEI CONTI:
						codifica.setCodice(codice); 
						codifica.setDescrizione(desc);
						codifica.setUid(siacTClassUid);	
	
					} 
				}

			}
		}
		
		return codifica;
	}
	
	public static List<Avanzovincolo> siacTAvanzovincoloFinEntityToAvanzovincoloModel(List<SiacTAvanzovincoloFin> dtos, List<Avanzovincolo> listaAvanzi) {
		List<Avanzovincolo> lista = new ArrayList<Avanzovincolo>();
		if(dtos!=null && dtos.size()>0){
			for(SiacTAvanzovincoloFin it: dtos){
				if(it!=null && it.getUid()!=null){
					Integer idIterato = it.getUid();
					Avanzovincolo mappato = CommonUtil.getById(listaAvanzi, idIterato);
					if(mappato==null){
						mappato = new Avanzovincolo();
					}
					//TIPO:
					if(it.getSiacDAvanzovincoloTipo()!=null){
						TipoAvanzovincolo tipoAvanzovincolo = new TipoAvanzovincolo();
						tipoAvanzovincolo.setUid(it.getSiacDAvanzovincoloTipo().getUid());
						tipoAvanzovincolo.setCodice(it.getSiacDAvanzovincoloTipo().getAvavTipoCode());
						tipoAvanzovincolo.setDescrizione(it.getSiacDAvanzovincoloTipo().getAvavTipoDesc());
						mappato.setTipoAvanzovincolo(tipoAvanzovincolo);
					}
					lista.add(mappato);
				}
			}
		}
		return lista;
	}
	
	/**
	 * 
	 * costruiscce il model SiopeTipoDebito a partire dai dati nell'entity SiacDSiopeTipoDebitoFin
	 * 
	 * @param siacDSiopeTipoDebito
	 * @return
	 */
	public static SiopeTipoDebito buildSiopeTipoDebito(SiacDSiopeTipoDebitoFin siacDSiopeTipoDebito){
		SiopeTipoDebito siopeTipoDebito = null;
		if(siacDSiopeTipoDebito != null && siacDSiopeTipoDebito.getUid()!=null){
			//esiste un siope tipo debito
			siopeTipoDebito = new SiopeTipoDebito();
			siopeTipoDebito.setUid(siacDSiopeTipoDebito.getUid());
			siopeTipoDebito.setCodice(siacDSiopeTipoDebito.getSiopeTipoDebitoCode());
			siopeTipoDebito.setDescrizione(siacDSiopeTipoDebito.getSiopeTipoDebitoDesc());
		}
		return siopeTipoDebito;
	}
	
	/**
	 * costruiscce il model SiopeAssenzaMotivazione a partire dai dati nell'entity SiacDSiopeAssenzaMotivazioneFin
	 * @param siacDSiopeAssenzaMotivazione
	 * @return
	 */
	public static SiopeAssenzaMotivazione buildSiopeMotivazioneAssenzaCig(SiacDSiopeAssenzaMotivazioneFin siacDSiopeAssenzaMotivazione){
		SiopeAssenzaMotivazione siopeAssenzaMotivazione = null;
		if(siacDSiopeAssenzaMotivazione!=null && siacDSiopeAssenzaMotivazione.getUid()!=null){
			//esiste un siope assenza motivazione cig
			siopeAssenzaMotivazione = new SiopeAssenzaMotivazione();
			siopeAssenzaMotivazione.setUid(siacDSiopeAssenzaMotivazione.getUid());
			siopeAssenzaMotivazione.setCodice(siacDSiopeAssenzaMotivazione.getSiopeAssenzaMotivazioneCode());
			siopeAssenzaMotivazione.setDescrizione(siacDSiopeAssenzaMotivazione.getSiopeAssenzaMotivazioneDesc());
		}
		return siopeAssenzaMotivazione;
	}
	
}
