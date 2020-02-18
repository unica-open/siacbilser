/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.util;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacfinser.TimingUtils;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoAttrFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoAttrModFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoClasseFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoClasseModFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoOnereFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoOnereModFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoRelazFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggettoRelazModFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggrelModpagFin;
import it.csi.siac.siacfinser.integration.entity.SiacRSoggrelModpagModFin;
import it.csi.siac.siacfinser.integration.entity.SiacTIndirizzoSoggettoFin;
import it.csi.siac.siacfinser.integration.entity.SiacTIndirizzoSoggettoModFin;
import it.csi.siac.siacfinser.integration.entity.SiacTModpagFin;
import it.csi.siac.siacfinser.integration.entity.SiacTModpagModFin;
import it.csi.siac.siacfinser.integration.entity.SiacTPersonaFisicaFin;
import it.csi.siac.siacfinser.integration.entity.SiacTPersonaFisicaModFin;
import it.csi.siac.siacfinser.integration.entity.SiacTPersonaGiuridicaFin;
import it.csi.siac.siacfinser.integration.entity.SiacTPersonaGiuridicaModFin;
import it.csi.siac.siacfinser.integration.entity.SiacTRecapitoSoggettoFin;
import it.csi.siac.siacfinser.integration.entity.SiacTRecapitoSoggettoModFin;
import it.csi.siac.siacfinser.integration.entity.SiacTSoggettoFin;
import it.csi.siac.siacfinser.integration.entity.SiacTSoggettoModFin;

public class EntityToEntityConverter {
	
	public static SiacTSoggettoModFin siacTSoggettoToSiacTSoggettoMod(SiacTSoggettoFin siacTSoggetto){
		SiacTSoggettoModFin siacTSoggettoMod = new SiacTSoggettoModFin();
		
		siacTSoggettoMod.setSiacTSoggetto(siacTSoggetto);
		siacTSoggettoMod.setSiacDAmbito(siacTSoggetto.getSiacDAmbito());
		siacTSoggettoMod.setCodiceFiscale(siacTSoggetto.getCodiceFiscale());
		siacTSoggettoMod.setCodiceFiscaleEstero(siacTSoggetto.getCodiceFiscaleEstero());
		siacTSoggettoMod.setPartitaIva(siacTSoggetto.getPartitaIva());
		
		if(siacTSoggetto.getSiacRSoggettoAttrs() != null && siacTSoggetto.getSiacRSoggettoAttrs().size() > 0){
			List<SiacRSoggettoAttrModFin> lista = new ArrayList<SiacRSoggettoAttrModFin>();; 
			for(SiacRSoggettoAttrFin iterato : siacTSoggetto.getSiacRSoggettoAttrs()){
				SiacRSoggettoAttrModFin siacRSoggettoAttrMod = new SiacRSoggettoAttrModFin();
				siacRSoggettoAttrMod = siacRSoggettoAttrToSiacRSoggettoAttrMod(iterato);
				lista.add(siacRSoggettoAttrMod);
			}
			siacTSoggettoMod.setSiacRSoggettoAttrMods(lista);
		}
		
		if(siacTSoggetto.getSiacRSoggettoClasses() != null && siacTSoggetto.getSiacRSoggettoClasses().size() > 0){
			List<SiacRSoggettoClasseModFin> lista = new ArrayList<SiacRSoggettoClasseModFin>();; 
			for(SiacRSoggettoClasseFin iterato : siacTSoggetto.getSiacRSoggettoClasses()){
				SiacRSoggettoClasseModFin siacRSoggettoClasseMod = new SiacRSoggettoClasseModFin();
				siacRSoggettoClasseMod = siacRSoggettoClasseToSiacRSoggettoClasseMod(iterato);
				lista.add(siacRSoggettoClasseMod);
			}
			siacTSoggettoMod.setSiacRSoggettoClasseMods(lista);
		}
		
		if(siacTSoggetto.getSiacRSoggettoOneres() != null && siacTSoggetto.getSiacRSoggettoOneres().size() > 0){
			List<SiacRSoggettoOnereModFin> lista = new ArrayList<SiacRSoggettoOnereModFin>();; 
			for(SiacRSoggettoOnereFin iterato : siacTSoggetto.getSiacRSoggettoOneres()){
				SiacRSoggettoOnereModFin siacRSoggettoOnereMod = new SiacRSoggettoOnereModFin();
				siacRSoggettoOnereMod = siacRSoggettoOnereToSiacRSoggettoOnereMod(iterato);
				lista.add(siacRSoggettoOnereMod);
			}
			siacTSoggettoMod.setSiacRSoggettoOnereMods(lista);
		}
		
		if(siacTSoggetto.getSiacTIndirizzoSoggettos() != null && siacTSoggetto.getSiacTIndirizzoSoggettos().size() > 0){
			List<SiacTIndirizzoSoggettoModFin> lista = new ArrayList<SiacTIndirizzoSoggettoModFin>();; 
			for(SiacTIndirizzoSoggettoFin iterato : siacTSoggetto.getSiacTIndirizzoSoggettos()){
				SiacTIndirizzoSoggettoModFin siacTIndirizzoSoggettoMod = new SiacTIndirizzoSoggettoModFin();
				siacTIndirizzoSoggettoMod = siacTIndirizzoSoggettoToSiacTIndirizzoSoggettoMod(iterato);
				lista.add(siacTIndirizzoSoggettoMod);
			}
			siacTSoggettoMod.setSiacTIndirizzoSoggettoMods(lista);
		}
		
		if(siacTSoggetto.getSiacTModpags() != null && siacTSoggetto.getSiacTModpags().size() > 0){
			List<SiacTModpagModFin> lista = new ArrayList<SiacTModpagModFin>();; 
			for(SiacTModpagFin iterato : siacTSoggetto.getSiacTModpags()){
				SiacTModpagModFin siacTModpagMod = new SiacTModpagModFin();
				siacTModpagMod = siacTModpagToSiacTModpagMod(iterato);
				lista.add(siacTModpagMod);
			}
			siacTSoggettoMod.setSiacTModpagMods(lista);
		}
		
		if(siacTSoggetto.getSiacTRecapitoSoggettos() != null && siacTSoggetto.getSiacTRecapitoSoggettos().size() > 0){
			List<SiacTRecapitoSoggettoModFin> lista = new ArrayList<SiacTRecapitoSoggettoModFin>();; 
			for(SiacTRecapitoSoggettoFin iterato : siacTSoggetto.getSiacTRecapitoSoggettos()){
				SiacTRecapitoSoggettoModFin siacTRecapitoSoggettoMod = new SiacTRecapitoSoggettoModFin();
				siacTRecapitoSoggettoMod = siacTRecapitoSoggettoToSiacTRecapitoSoggettoMod(iterato);
				lista.add(siacTRecapitoSoggettoMod);
			}
			siacTSoggettoMod.setSiacTRecapitoSoggettoMods(lista);
		}

		
		siacTSoggettoMod.setSiacTEnteProprietario(siacTSoggetto.getSiacTEnteProprietario());
		siacTSoggettoMod.setSoggettoCode(siacTSoggetto.getSoggettoCode());
		siacTSoggettoMod.setSoggettoDesc(siacTSoggetto.getSoggettoDesc());

		
		siacTSoggettoMod.setDataFineValiditaDurc(siacTSoggetto.getDataFineValiditaDurc());
		siacTSoggettoMod.setTipoFonteDurc(siacTSoggetto.getTipoFonteDurc());
		siacTSoggettoMod.setFonteDurc(siacTSoggetto.getFonteDurc());
		
		siacTSoggettoMod.setNoteDurc(siacTSoggetto.getNoteDurc());

		siacTSoggettoMod.setCanalePA(siacTSoggetto.getCanalePA());
		siacTSoggettoMod.setEmailPec(siacTSoggetto.getEmailPec());
		siacTSoggettoMod.setCodDestinatario(siacTSoggetto.getCodDestinatario());
		
		return siacTSoggettoMod;
	}
	
	public static SiacTSoggettoFin siacTSoggettoModToSiacTSoggetto(SiacTSoggettoModFin siacTSoggettoMod, String loginOperazione){		
		SiacTSoggettoFin siacTSoggetto=new SiacTSoggettoFin();
		
		siacTSoggetto.setUid(siacTSoggettoMod.getSiacTSoggetto().getUid());
		siacTSoggetto.setSiacDAmbito(siacTSoggettoMod.getSiacDAmbito());
		siacTSoggetto.setCodiceFiscale(siacTSoggettoMod.getCodiceFiscale());
		siacTSoggetto.setCodiceFiscaleEstero(siacTSoggettoMod.getCodiceFiscaleEstero());
		siacTSoggetto.setDataCancellazione(siacTSoggettoMod.getDataCancellazione());
		siacTSoggetto.setDataCreazione(siacTSoggettoMod.getDataCreazione());
		siacTSoggetto.setDataFineValidita(siacTSoggettoMod.getDataFineValidita());
		siacTSoggetto.setDataInizioValidita(siacTSoggettoMod.getDataInizioValidita());
		siacTSoggetto.setDataModifica(siacTSoggettoMod.getDataModifica());
		siacTSoggetto.setLoginOperazione(siacTSoggettoMod.getLoginOperazione());
		siacTSoggetto.setPartitaIva(siacTSoggettoMod.getPartitaIva());
		
		if(siacTSoggettoMod.getSiacRSoggettoAttrMods() != null && siacTSoggettoMod.getSiacRSoggettoAttrMods().size() > 0){
			List<SiacRSoggettoAttrFin> lista = new ArrayList<SiacRSoggettoAttrFin>();; 
			for(SiacRSoggettoAttrModFin iterato : siacTSoggettoMod.getSiacRSoggettoAttrMods()){
				SiacRSoggettoAttrFin siacRSoggettoAttr = new SiacRSoggettoAttrFin();
				siacRSoggettoAttr = siacRSoggettoAttrModToSiacRSoggettoAttr(iterato);
				if(null!=loginOperazione){
					siacRSoggettoAttr.setLoginOperazione(loginOperazione);			
				}
				lista.add(siacRSoggettoAttr);
			}
			siacTSoggetto.setSiacRSoggettoAttrs(lista);
		}
				
		if(siacTSoggettoMod.getSiacRSoggettoClasseMods() != null && siacTSoggettoMod.getSiacRSoggettoClasseMods().size() > 0){
			List<SiacRSoggettoClasseFin> lista = new ArrayList<SiacRSoggettoClasseFin>();; 
			for(SiacRSoggettoClasseModFin iterato : siacTSoggettoMod.getSiacRSoggettoClasseMods()){
				SiacRSoggettoClasseFin siacRSoggettoClasse = new SiacRSoggettoClasseFin();
				siacRSoggettoClasse = siacRSoggettoClasseModToSiacRSoggettoClasse(iterato);
				lista.add(siacRSoggettoClasse);
			}
			siacTSoggetto.setSiacRSoggettoClasses(lista);
		}
		
		if(siacTSoggettoMod.getSiacRSoggettoOnereMods() != null && siacTSoggettoMod.getSiacRSoggettoOnereMods().size() > 0){
			List<SiacRSoggettoOnereFin> lista = new ArrayList<SiacRSoggettoOnereFin>();; 
			for(SiacRSoggettoOnereModFin iterato : siacTSoggettoMod.getSiacRSoggettoOnereMods()){
				SiacRSoggettoOnereFin siacRSoggettoOnere = new SiacRSoggettoOnereFin();
				siacRSoggettoOnere = siacRSoggettoOnereModToSiacRSoggettoOnere(iterato);
				lista.add(siacRSoggettoOnere);
			}
			siacTSoggetto.setSiacRSoggettoOneres(lista);
		}		
		
		if(siacTSoggettoMod.getSiacTIndirizzoSoggettoMods() != null && siacTSoggettoMod.getSiacTIndirizzoSoggettoMods().size() > 0){
			List<SiacTIndirizzoSoggettoFin> lista = new ArrayList<SiacTIndirizzoSoggettoFin>();
			for(SiacTIndirizzoSoggettoModFin iterato : siacTSoggettoMod.getSiacTIndirizzoSoggettoMods()){
				SiacTIndirizzoSoggettoFin siacTIndirizzoSoggetto = new SiacTIndirizzoSoggettoFin();
				siacTIndirizzoSoggetto = siacTIndirizzoSoggettoModToSiacTIndirizzoSoggetto(iterato);
				lista.add(siacTIndirizzoSoggetto);
			}
			siacTSoggetto.setSiacTIndirizzoSoggettos(lista);
		}
		
		if(siacTSoggettoMod.getSiacTModpagMods() != null && siacTSoggettoMod.getSiacTModpagMods().size() > 0){
			List<SiacTModpagFin> lista = new ArrayList<SiacTModpagFin>(); 
			for(SiacTModpagModFin iterato : siacTSoggettoMod.getSiacTModpagMods()){
				SiacTModpagFin siacTModpag = new SiacTModpagFin();
				siacTModpag = siacTModpagToSiacTModpag(iterato);
				lista.add(siacTModpag);
			}
			siacTSoggetto.setSiacTModpags(lista);
		}
		
		if(siacTSoggettoMod.getSiacTRecapitoSoggettoMods() != null && siacTSoggettoMod.getSiacTRecapitoSoggettoMods().size() > 0){
			List<SiacTRecapitoSoggettoFin> lista = new ArrayList<SiacTRecapitoSoggettoFin>();; 
			for(SiacTRecapitoSoggettoModFin iterato : siacTSoggettoMod.getSiacTRecapitoSoggettoMods()){
				SiacTRecapitoSoggettoFin siacTRecapitoSoggetto = new SiacTRecapitoSoggettoFin();
				siacTRecapitoSoggetto = siacTRecapitoSoggettoModToSiacTRecapitoSoggetto(iterato);
				lista.add(siacTRecapitoSoggetto);
			}
			siacTSoggetto.setSiacTRecapitoSoggettos(lista);
		}
		
		
		siacTSoggetto.setDataFineValiditaDurc(siacTSoggettoMod.getDataFineValiditaDurc());
		siacTSoggetto.setTipoFonteDurc(siacTSoggettoMod.getTipoFonteDurc());
		siacTSoggetto.setFonteDurc(siacTSoggettoMod.getFonteDurc());
		siacTSoggetto.setNoteDurc(siacTSoggettoMod.getNoteDurc());
		
		siacTSoggetto.setSiacTEnteProprietario(siacTSoggettoMod.getSiacTEnteProprietario());
		siacTSoggetto.setSoggettoCode(siacTSoggettoMod.getSoggettoCode());
		siacTSoggetto.setSoggettoDesc(siacTSoggettoMod.getSoggettoDesc());
		
		siacTSoggetto.setCanalePA(siacTSoggettoMod.getCanalePA());
		siacTSoggetto.setEmailPec(siacTSoggettoMod.getEmailPec());
		siacTSoggetto.setCodDestinatario(siacTSoggettoMod.getCodDestinatario());
		
		return siacTSoggetto;
	}
	
	public static SiacTIndirizzoSoggettoModFin siacTIndirizzoSoggettoToSiacTIndirizzoSoggettoMod(SiacTIndirizzoSoggettoFin siacTIndirizzoSoggetto){
		SiacTIndirizzoSoggettoModFin siacTIndirizzoSoggettoMod = new SiacTIndirizzoSoggettoModFin();

		siacTIndirizzoSoggettoMod.setAvviso(siacTIndirizzoSoggetto.getAvviso());
		siacTIndirizzoSoggettoMod.setFrazione(siacTIndirizzoSoggetto.getFrazione());
		siacTIndirizzoSoggettoMod.setInterno(siacTIndirizzoSoggetto.getInterno());
		siacTIndirizzoSoggettoMod.setNumeroCivico(siacTIndirizzoSoggetto.getNumeroCivico());
		siacTIndirizzoSoggettoMod.setPrincipale(siacTIndirizzoSoggetto.getPrincipale());
		siacTIndirizzoSoggettoMod.setSiacDViaTipo(siacTIndirizzoSoggetto.getSiacDViaTipo());
		siacTIndirizzoSoggettoMod.setSiacTComune(siacTIndirizzoSoggetto.getSiacTComune());
		siacTIndirizzoSoggettoMod.setSiacTEnteProprietario(siacTIndirizzoSoggetto.getSiacTEnteProprietario());
		siacTIndirizzoSoggettoMod.setSiacTSoggetto(siacTIndirizzoSoggetto.getSiacTSoggetto());
		siacTIndirizzoSoggettoMod.setToponimo(siacTIndirizzoSoggetto.getToponimo());
		siacTIndirizzoSoggettoMod.setZipCode(siacTIndirizzoSoggetto.getZipCode());

		return siacTIndirizzoSoggettoMod;
	}
	
	public static SiacTIndirizzoSoggettoFin siacTIndirizzoSoggettoModToSiacTIndirizzoSoggetto(SiacTIndirizzoSoggettoModFin siacTIndirizzoSoggettoMod){
		SiacTIndirizzoSoggettoFin siacTIndirizzoSoggetto= new SiacTIndirizzoSoggettoFin();
		
		siacTIndirizzoSoggetto.setAvviso(siacTIndirizzoSoggettoMod.getAvviso());
		siacTIndirizzoSoggetto.setFrazione(siacTIndirizzoSoggettoMod.getFrazione());
		siacTIndirizzoSoggetto.setInterno(siacTIndirizzoSoggettoMod.getInterno());
		siacTIndirizzoSoggetto.setNumeroCivico(siacTIndirizzoSoggettoMod.getNumeroCivico());
		siacTIndirizzoSoggetto.setPrincipale(siacTIndirizzoSoggettoMod.getPrincipale());
		siacTIndirizzoSoggetto.setSiacDViaTipo(siacTIndirizzoSoggettoMod.getSiacDViaTipo());
		siacTIndirizzoSoggetto.setSiacTComune(siacTIndirizzoSoggettoMod.getSiacTComune());
		siacTIndirizzoSoggetto.setSiacTEnteProprietario(siacTIndirizzoSoggettoMod.getSiacTEnteProprietario());
		siacTIndirizzoSoggetto.setSiacTSoggetto(siacTIndirizzoSoggettoMod.getSiacTSoggetto());
		siacTIndirizzoSoggetto.setToponimo(siacTIndirizzoSoggettoMod.getToponimo());
		siacTIndirizzoSoggetto.setZipCode(siacTIndirizzoSoggettoMod.getZipCode());
		
		return siacTIndirizzoSoggetto;
	}

	
	public static SiacTModpagModFin siacTModpagToSiacTModpagMod(SiacTModpagFin siacTModpag){
		SiacTModpagModFin siacTModpagMod = new SiacTModpagModFin();

		siacTModpagMod.setBic(siacTModpag.getBic());
		siacTModpagMod.setContocorrente(siacTModpag.getContocorrente());
		siacTModpagMod.setDataCancellazione(siacTModpag.getDataCancellazione());
		siacTModpagMod.setDataCreazione(siacTModpag.getDataCreazione());
		siacTModpagMod.setDataFineValidita(siacTModpag.getDataFineValidita());
		siacTModpagMod.setDataInizioValidita(siacTModpag.getDataInizioValidita());
		siacTModpagMod.setDataModifica(siacTModpag.getDataModifica());
		siacTModpagMod.setIban(siacTModpag.getIban());
		siacTModpagMod.setLoginOperazione(siacTModpag.getLoginOperazione());
		siacTModpagMod.setNote(siacTModpag.getNote());
		siacTModpagMod.setPerStipendi(siacTModpag.getPerStipendi());
		siacTModpagMod.setQuietanziante(siacTModpag.getQuietanziante());
		siacTModpagMod.setQuietanzianteCodiceFiscale(siacTModpag.getQuietanzianteCodiceFiscale());
		siacTModpagMod.setSiacDAccreditoTipo(siacTModpag.getSiacDAccreditoTipo());
		siacTModpagMod.setSiacTEnteProprietario(siacTModpag.getSiacTEnteProprietario());
		siacTModpagMod.setSiacTSoggetto(siacTModpag.getSiacTSoggetto());
		
		return siacTModpagMod;
	}
	
	public static SiacTModpagFin siacTModpagToSiacTModpag(SiacTModpagModFin siacTModpagMod){
		TimingUtils tu= new TimingUtils();
		SiacTModpagFin siacTModpag= new SiacTModpagFin();
		
		siacTModpag.setBic(siacTModpagMod.getBic());
		siacTModpag.setContocorrente(siacTModpagMod.getContocorrente());
		siacTModpag.setDataCancellazione(siacTModpagMod.getDataCancellazione());
		siacTModpag.setDataCreazione(siacTModpagMod.getDataCreazione());
		siacTModpag.setDataFineValidita(siacTModpagMod.getDataFineValidita());
		siacTModpag.setDataInizioValidita(siacTModpagMod.getDataInizioValidita());
		siacTModpag.setDataModifica(siacTModpagMod.getDataModifica());		
		siacTModpag.setIban(siacTModpagMod.getIban());
		siacTModpag.setLoginOperazione(siacTModpagMod.getLoginOperazione());
		siacTModpag.setNote(siacTModpagMod.getNote());
		siacTModpag.setPerStipendi(siacTModpagMod.getPerStipendi());
		siacTModpag.setQuietanziante(siacTModpagMod.getQuietanziante());
		siacTModpag.setQuietanzianteCodiceFiscale(siacTModpagMod.getQuietanzianteCodiceFiscale());
		siacTModpag.setSiacDAccreditoTipo(siacTModpagMod.getSiacDAccreditoTipo());
		siacTModpag.setSiacTEnteProprietario(siacTModpagMod.getSiacTEnteProprietario());
		siacTModpag.setSiacTSoggetto(siacTModpagMod.getSiacTSoggetto());
		
		return siacTModpag;
	}
		

	public static SiacTPersonaFisicaModFin siacTPersonaFisicaToSiacTPersonaFisicaMod(SiacTPersonaFisicaFin siacTPersonaFisica, SiacTSoggettoModFin siacTSoggettoMod){
		SiacTPersonaFisicaModFin siacTPersonaFisicaMod = new SiacTPersonaFisicaModFin();

		siacTPersonaFisicaMod.setCognome(siacTPersonaFisica.getCognome());
		siacTPersonaFisicaMod.setNascitaData(siacTPersonaFisica.getNascitaData());
		siacTPersonaFisicaMod.setNome(siacTPersonaFisica.getNome());
		siacTPersonaFisicaMod.setSesso(siacTPersonaFisica.getSesso());
		siacTPersonaFisicaMod.setSiacTComune(siacTPersonaFisica.getSiacTComune());
		siacTPersonaFisicaMod.setSiacTEnteProprietario(siacTPersonaFisica.getSiacTEnteProprietario());
		siacTPersonaFisicaMod.setSiacTSoggetto(siacTPersonaFisica.getSiacTSoggetto());
		
		siacTPersonaFisicaMod.setSiacTSoggettoMod(siacTSoggettoMod);
		// siacTPersonaFisicaMod.setPerfModId(perfModId);
		
		return siacTPersonaFisicaMod;
	}

	public static SiacTPersonaFisicaFin siacTPersonaFisicaToSiacTPersonaFisica(SiacTPersonaFisicaFin siacTPersonaFisica){
		SiacTPersonaFisicaFin siacTPersonaFisicaNew = new SiacTPersonaFisicaFin();

		siacTPersonaFisicaNew.setCognome(siacTPersonaFisica.getCognome());
		siacTPersonaFisicaNew.setNascitaData(siacTPersonaFisica.getNascitaData());
		siacTPersonaFisicaNew.setNome(siacTPersonaFisica.getNome());
		siacTPersonaFisicaNew.setSesso(siacTPersonaFisica.getSesso());
		siacTPersonaFisicaNew.setSiacTComune(siacTPersonaFisica.getSiacTComune());
		siacTPersonaFisicaNew.setSiacTEnteProprietario(siacTPersonaFisica.getSiacTEnteProprietario());
		siacTPersonaFisicaNew.setSiacTSoggetto(siacTPersonaFisica.getSiacTSoggetto());
		
		return siacTPersonaFisicaNew;
	}
	
	

	public static SiacTPersonaFisicaModFin siacTPersonaFisicaModToSiacTPersonaFisicaMod(SiacTPersonaFisicaModFin siacTPersonaFisicaMod){
		SiacTPersonaFisicaModFin siacTPersonaFisicaModNew = new SiacTPersonaFisicaModFin();

		siacTPersonaFisicaModNew.setCognome(siacTPersonaFisicaMod.getCognome());
		siacTPersonaFisicaModNew.setNascitaData(siacTPersonaFisicaMod.getNascitaData());
		siacTPersonaFisicaModNew.setNome(siacTPersonaFisicaMod.getNome());
		siacTPersonaFisicaModNew.setSesso(siacTPersonaFisicaMod.getSesso());
		siacTPersonaFisicaModNew.setSiacTComune(siacTPersonaFisicaMod.getSiacTComune());
		siacTPersonaFisicaModNew.setSiacTEnteProprietario(siacTPersonaFisicaMod.getSiacTEnteProprietario());
		siacTPersonaFisicaModNew.setSiacTSoggetto(siacTPersonaFisicaMod.getSiacTSoggetto());
		
		return siacTPersonaFisicaModNew;
	}
	
	public static SiacTPersonaFisicaFin siacTPersonaFisicaModToSiacTPersonaFisica(SiacTPersonaFisicaModFin siacTPersonaFisicaMod){
		SiacTPersonaFisicaFin siacTPersonaFisica=new SiacTPersonaFisicaFin();
		
		siacTPersonaFisica.setCognome(siacTPersonaFisicaMod.getCognome());
		siacTPersonaFisica.setNascitaData(siacTPersonaFisicaMod.getNascitaData());
		siacTPersonaFisica.setNome(siacTPersonaFisicaMod.getNome());
		siacTPersonaFisica.setSesso(siacTPersonaFisicaMod.getSesso());
		siacTPersonaFisica.setSiacTComune(siacTPersonaFisicaMod.getSiacTComune());
		siacTPersonaFisica.setSiacTEnteProprietario(siacTPersonaFisicaMod.getSiacTEnteProprietario());
		siacTPersonaFisica.setSiacTSoggetto(siacTPersonaFisicaMod.getSiacTSoggetto());
		return siacTPersonaFisica;
	}

	public static SiacTPersonaGiuridicaModFin siacTPersonaGiuridicaToSiacTPersonaGiuridicaMod(SiacTPersonaGiuridicaFin siacTPersonaGiuridica, SiacTSoggettoModFin siacTSoggettoMod){
		SiacTPersonaGiuridicaModFin siacTPersonaGiuridicaMod = new SiacTPersonaGiuridicaModFin();

		siacTPersonaGiuridicaMod.setRagioneSociale(siacTPersonaGiuridica.getRagioneSociale());
		siacTPersonaGiuridicaMod.setSiacTEnteProprietario(siacTPersonaGiuridica.getSiacTEnteProprietario());
		siacTPersonaGiuridicaMod.setSiacTSoggetto(siacTPersonaGiuridica.getSiacTSoggetto());
		
		siacTPersonaGiuridicaMod.setSiacTSoggettoMod(siacTSoggettoMod);
		
		return siacTPersonaGiuridicaMod;
	}
	
	public static SiacTPersonaGiuridicaFin siacTPersonaGiuridicaModToSiacTPersonaGiuridica(SiacTPersonaGiuridicaModFin siacTPersonaGiuridicaMod){
		SiacTPersonaGiuridicaFin siacTPersonaGiuridica=new SiacTPersonaGiuridicaFin();
		
		siacTPersonaGiuridica.setRagioneSociale(siacTPersonaGiuridicaMod.getRagioneSociale());
		siacTPersonaGiuridica.setSiacTEnteProprietario(siacTPersonaGiuridicaMod.getSiacTEnteProprietario());
		siacTPersonaGiuridica.setSiacTSoggetto(siacTPersonaGiuridicaMod.getSiacTSoggetto());
		
		return siacTPersonaGiuridica;
	}
		

	public static SiacTPersonaGiuridicaFin siacTPersonaGiuridicaToSiacTPersonaGiuridica(SiacTPersonaGiuridicaFin siacTPersonaGiuridica){
		SiacTPersonaGiuridicaFin siacTPersonaGiuridicaNew = new SiacTPersonaGiuridicaFin();
		
		siacTPersonaGiuridicaNew.setRagioneSociale(siacTPersonaGiuridica.getRagioneSociale());
		siacTPersonaGiuridicaNew.setSiacTEnteProprietario(siacTPersonaGiuridica.getSiacTEnteProprietario());
		siacTPersonaGiuridicaNew.setSiacTSoggetto(siacTPersonaGiuridica.getSiacTSoggetto());
		
		return siacTPersonaGiuridicaNew;
	}

	
	public static SiacTPersonaGiuridicaModFin siacTPersonaGiuridicaModToSiacTPersonaGiuridicaMod(SiacTPersonaGiuridicaModFin siacTPersonaGiuridicaMod){
		SiacTPersonaGiuridicaModFin siacTPersonaGiuridicaModNew = new SiacTPersonaGiuridicaModFin();
		
		siacTPersonaGiuridicaModNew.setRagioneSociale(siacTPersonaGiuridicaMod.getRagioneSociale());
		siacTPersonaGiuridicaModNew.setSiacTEnteProprietario(siacTPersonaGiuridicaMod.getSiacTEnteProprietario());
		siacTPersonaGiuridicaModNew.setSiacTSoggetto(siacTPersonaGiuridicaMod.getSiacTSoggetto());
		
		return siacTPersonaGiuridicaModNew;
	}
	public static SiacTRecapitoSoggettoModFin siacTRecapitoSoggettoToSiacTRecapitoSoggettoMod(SiacTRecapitoSoggettoFin siacTRecapitoSoggetto){
		SiacTRecapitoSoggettoModFin siacTRecapitoSoggettoMod = new SiacTRecapitoSoggettoModFin();

		siacTRecapitoSoggettoMod.setAvviso(siacTRecapitoSoggetto.getAvviso());
		siacTRecapitoSoggettoMod.setDataCancellazione(siacTRecapitoSoggetto.getDataCancellazione());
		siacTRecapitoSoggettoMod.setDataCreazione(siacTRecapitoSoggetto.getDataCreazione());
		siacTRecapitoSoggettoMod.setDataFineValidita(siacTRecapitoSoggetto.getDataFineValidita());
		siacTRecapitoSoggettoMod.setDataInizioValidita(siacTRecapitoSoggetto.getDataInizioValidita());
		siacTRecapitoSoggettoMod.setDataModifica(siacTRecapitoSoggetto.getDataModifica());
		siacTRecapitoSoggettoMod.setLoginOperazione(siacTRecapitoSoggetto.getLoginOperazione());
		siacTRecapitoSoggettoMod.setRecapitoCode(siacTRecapitoSoggetto.getRecapitoCode());
		siacTRecapitoSoggettoMod.setRecapitoDesc(siacTRecapitoSoggetto.getRecapitoDesc());
		siacTRecapitoSoggettoMod.setSiacDRecapitoModo(siacTRecapitoSoggetto.getSiacDRecapitoModo());
		siacTRecapitoSoggettoMod.setSiacTEnteProprietario(siacTRecapitoSoggetto.getSiacTEnteProprietario());
		siacTRecapitoSoggettoMod.setSiacTSoggetto(siacTRecapitoSoggetto.getSiacTSoggetto());
		
		return siacTRecapitoSoggettoMod;
	}	
	
	public static SiacTRecapitoSoggettoFin siacTRecapitoSoggettoModToSiacTRecapitoSoggetto(SiacTRecapitoSoggettoModFin siacTRecapitoSoggettoMod){
		SiacTRecapitoSoggettoFin siacTRecapitoSoggetto=new SiacTRecapitoSoggettoFin();
		
		siacTRecapitoSoggetto.setAvviso(siacTRecapitoSoggettoMod.getAvviso());
		siacTRecapitoSoggetto.setDataCancellazione(siacTRecapitoSoggettoMod.getDataCancellazione());
		siacTRecapitoSoggetto.setDataCreazione(siacTRecapitoSoggettoMod.getDataCreazione());
		siacTRecapitoSoggetto.setDataFineValidita(siacTRecapitoSoggettoMod.getDataFineValidita());
		siacTRecapitoSoggetto.setDataInizioValidita(siacTRecapitoSoggettoMod.getDataInizioValidita());
		siacTRecapitoSoggetto.setDataModifica(siacTRecapitoSoggettoMod.getDataModifica());
		siacTRecapitoSoggetto.setLoginOperazione(siacTRecapitoSoggettoMod.getLoginOperazione());
		siacTRecapitoSoggetto.setRecapitoCode(siacTRecapitoSoggettoMod.getRecapitoCode());
		siacTRecapitoSoggetto.setRecapitoDesc(siacTRecapitoSoggettoMod.getRecapitoDesc());
		siacTRecapitoSoggetto.setSiacDRecapitoModo(siacTRecapitoSoggettoMod.getSiacDRecapitoModo());
		siacTRecapitoSoggetto.setSiacTEnteProprietario(siacTRecapitoSoggettoMod.getSiacTEnteProprietario());
		siacTRecapitoSoggetto.setSiacTSoggetto(siacTRecapitoSoggettoMod.getSiacTSoggetto());
		
		return siacTRecapitoSoggetto;
	}

	public static SiacRSoggrelModpagModFin siacRSoggrelModpagToSiacRSoggrelModpagMod(SiacRSoggrelModpagFin siacRSoggrelModpag){
		SiacRSoggrelModpagModFin siacRSoggrelModpagMod = new SiacRSoggrelModpagModFin();

		siacRSoggrelModpagMod.setDataCancellazione(siacRSoggrelModpag.getDataCancellazione());
		siacRSoggrelModpagMod.setDataCreazione(siacRSoggrelModpag.getDataCreazione());
		siacRSoggrelModpagMod.setDataFineValidita(siacRSoggrelModpag.getDataFineValidita());
		siacRSoggrelModpagMod.setDataInizioValidita(siacRSoggrelModpag.getDataInizioValidita());
		siacRSoggrelModpagMod.setDataModifica(siacRSoggrelModpag.getDataModifica());
		siacRSoggrelModpagMod.setLoginOperazione(siacRSoggrelModpag.getLoginOperazione());
		siacRSoggrelModpagMod.setSiacTEnteProprietario(siacRSoggrelModpag.getSiacTEnteProprietario());
		siacRSoggrelModpagMod.setSiacTModpag(siacRSoggrelModpag.getSiacTModpag());
		
		return siacRSoggrelModpagMod;
	}
	
	public static SiacRSoggrelModpagFin  siacRSoggrelModpagModToSiacRSoggrelModpag(SiacRSoggrelModpagModFin siacRSoggrelModpagMod){
		SiacRSoggrelModpagFin siacRSoggrelModpag=new SiacRSoggrelModpagFin();
		
		siacRSoggrelModpag.setDataCancellazione(siacRSoggrelModpagMod.getDataCancellazione());
		siacRSoggrelModpag.setDataCreazione(siacRSoggrelModpagMod.getDataCreazione());
		siacRSoggrelModpag.setDataFineValidita(siacRSoggrelModpagMod.getDataFineValidita());
		siacRSoggrelModpag.setDataInizioValidita(siacRSoggrelModpagMod.getDataInizioValidita());
		siacRSoggrelModpag.setDataModifica(siacRSoggrelModpagMod.getDataModifica());
		siacRSoggrelModpag.setLoginOperazione(siacRSoggrelModpagMod.getLoginOperazione());
		siacRSoggrelModpag.setSiacTEnteProprietario(siacRSoggrelModpagMod.getSiacTEnteProprietario());
		siacRSoggrelModpag.setSiacTModpag(siacRSoggrelModpagMod.getSiacTModpag());
		
		return siacRSoggrelModpag;
	}
	
	public static SiacRSoggettoAttrModFin siacRSoggettoAttrToSiacRSoggettoAttrMod(SiacRSoggettoAttrFin siacRSoggettoAttr){
		SiacRSoggettoAttrModFin siacRSoggettoAttrMod = new SiacRSoggettoAttrModFin();

		siacRSoggettoAttrMod.setBoolean_(siacRSoggettoAttr.getBoolean_());
		siacRSoggettoAttrMod.setDataCancellazione(siacRSoggettoAttr.getDataCancellazione());
		siacRSoggettoAttrMod.setDataCreazione(siacRSoggettoAttr.getDataCreazione());
		siacRSoggettoAttrMod.setDataFineValidita(siacRSoggettoAttr.getDataFineValidita());
		siacRSoggettoAttrMod.setDataInizioValidita(siacRSoggettoAttr.getDataInizioValidita());
		siacRSoggettoAttrMod.setDataModifica(siacRSoggettoAttr.getDataModifica());
		siacRSoggettoAttrMod.setLoginOperazione(siacRSoggettoAttr.getLoginOperazione());
		siacRSoggettoAttrMod.setNumerico(siacRSoggettoAttr.getNumerico());
		siacRSoggettoAttrMod.setPercentuale(siacRSoggettoAttr.getPercentuale());
		siacRSoggettoAttrMod.setSiacTAttr(siacRSoggettoAttr.getSiacTAttr());
		siacRSoggettoAttrMod.setSiacTEnteProprietario(siacRSoggettoAttr.getSiacTEnteProprietario());
		siacRSoggettoAttrMod.setSiacTSoggetto(siacRSoggettoAttr.getSiacTSoggetto());
		siacRSoggettoAttrMod.setTabellaId(siacRSoggettoAttr.getTabellaId());
		siacRSoggettoAttrMod.setTesto(siacRSoggettoAttr.getTesto());

		return siacRSoggettoAttrMod;
	}
	
	public static SiacRSoggettoAttrFin siacRSoggettoAttrModToSiacRSoggettoAttr(SiacRSoggettoAttrModFin siacRSoggettoAttrMod){
		SiacRSoggettoAttrFin siacRSoggettoAttr=new SiacRSoggettoAttrFin();
		
		siacRSoggettoAttr.setBoolean_(siacRSoggettoAttrMod.getBoolean_());
		siacRSoggettoAttr.setDataCancellazione(siacRSoggettoAttrMod.getDataCancellazione());
		siacRSoggettoAttr.setDataCreazione(siacRSoggettoAttrMod.getDataCreazione());
		siacRSoggettoAttr.setDataFineValidita(siacRSoggettoAttrMod.getDataFineValidita());
		siacRSoggettoAttr.setDataInizioValidita(siacRSoggettoAttrMod.getDataInizioValidita());
		siacRSoggettoAttr.setDataModifica(siacRSoggettoAttrMod.getDataModifica());
		siacRSoggettoAttr.setLoginOperazione(siacRSoggettoAttrMod.getLoginOperazione());
		siacRSoggettoAttr.setNumerico(siacRSoggettoAttrMod.getNumerico());
		siacRSoggettoAttr.setPercentuale(siacRSoggettoAttrMod.getPercentuale());
		siacRSoggettoAttr.setSiacTAttr(siacRSoggettoAttrMod.getSiacTAttr());
		siacRSoggettoAttr.setSiacTEnteProprietario(siacRSoggettoAttrMod.getSiacTEnteProprietario());
		siacRSoggettoAttr.setSiacTSoggetto(siacRSoggettoAttrMod.getSiacTSoggetto());
		siacRSoggettoAttr.setTabellaId(siacRSoggettoAttrMod.getTabellaId());
		siacRSoggettoAttr.setTesto(siacRSoggettoAttrMod.getTesto());	
		
		return siacRSoggettoAttr;
	}

	public static SiacRSoggettoClasseModFin siacRSoggettoClasseToSiacRSoggettoClasseMod(SiacRSoggettoClasseFin siacRSoggettoClasse){
		SiacRSoggettoClasseModFin siacRSoggettoClasseMod = new SiacRSoggettoClasseModFin();

		siacRSoggettoClasseMod.setDataCancellazione(siacRSoggettoClasse.getDataCancellazione());
		siacRSoggettoClasseMod.setDataCreazione(siacRSoggettoClasse.getDataCreazione());
		siacRSoggettoClasseMod.setDataFineValidita(siacRSoggettoClasse.getDataFineValidita());
		siacRSoggettoClasseMod.setDataInizioValidita(siacRSoggettoClasse.getDataInizioValidita());
		siacRSoggettoClasseMod.setDataModifica(siacRSoggettoClasse.getDataModifica());
		siacRSoggettoClasseMod.setLoginOperazione(siacRSoggettoClasse.getLoginOperazione());
		siacRSoggettoClasseMod.setSiacDSoggettoClasse(siacRSoggettoClasse.getSiacDSoggettoClasse());
		siacRSoggettoClasseMod.setSiacTEnteProprietario(siacRSoggettoClasse.getSiacTEnteProprietario());
		siacRSoggettoClasseMod.setSiacTSoggetto(siacRSoggettoClasse.getSiacTSoggetto());
		
		return siacRSoggettoClasseMod;
	}
	
	public static SiacRSoggettoClasseFin siacRSoggettoClasseModToSiacRSoggettoClasse(SiacRSoggettoClasseModFin siacRSoggettoClasseMod){
		SiacRSoggettoClasseFin siacRSoggettoClasse=new SiacRSoggettoClasseFin();
		
		siacRSoggettoClasse.setDataCancellazione(siacRSoggettoClasseMod.getDataCancellazione());
		siacRSoggettoClasse.setDataCreazione(siacRSoggettoClasseMod.getDataCreazione());
		siacRSoggettoClasse.setDataFineValidita(siacRSoggettoClasseMod.getDataFineValidita());
		siacRSoggettoClasse.setDataInizioValidita(siacRSoggettoClasseMod.getDataInizioValidita());
		siacRSoggettoClasse.setDataModifica(siacRSoggettoClasseMod.getDataModifica());
		siacRSoggettoClasse.setLoginOperazione(siacRSoggettoClasseMod.getLoginOperazione());
		siacRSoggettoClasse.setSiacDSoggettoClasse(siacRSoggettoClasseMod.getSiacDSoggettoClasse());
		siacRSoggettoClasse.setSiacTEnteProprietario(siacRSoggettoClasseMod.getSiacTEnteProprietario());
		siacRSoggettoClasse.setSiacTSoggetto(siacRSoggettoClasseMod.getSiacTSoggetto());
		
		return siacRSoggettoClasse;
	}
	
	public static SiacRSoggettoOnereModFin siacRSoggettoOnereToSiacRSoggettoOnereMod(SiacRSoggettoOnereFin siacRSoggettoOnere){
		SiacRSoggettoOnereModFin siacRSoggettoOnereMod = new SiacRSoggettoOnereModFin();

		siacRSoggettoOnereMod.setDataCancellazione(siacRSoggettoOnere.getDataCancellazione());
		siacRSoggettoOnereMod.setDataCreazione(siacRSoggettoOnere.getDataCreazione());
		siacRSoggettoOnereMod.setDataFineValidita(siacRSoggettoOnere.getDataFineValidita());
		siacRSoggettoOnereMod.setDataInizioValidita(siacRSoggettoOnere.getDataInizioValidita());
		siacRSoggettoOnereMod.setDataModifica(siacRSoggettoOnere.getDataModifica());
		siacRSoggettoOnereMod.setLoginOperazione(siacRSoggettoOnere.getLoginOperazione());
		siacRSoggettoOnereMod.setSiacDOnere(siacRSoggettoOnere.getSiacDOnere());
		siacRSoggettoOnereMod.setSiacTEnteProprietario(siacRSoggettoOnere.getSiacTEnteProprietario());
		siacRSoggettoOnereMod.setSiacTSoggetto(siacRSoggettoOnere.getSiacTSoggetto());
		
		return siacRSoggettoOnereMod;
	}
	
	public static SiacRSoggettoOnereFin siacRSoggettoOnereModToSiacRSoggettoOnere(SiacRSoggettoOnereModFin siacRSoggettoOnereMod){
		SiacRSoggettoOnereFin siacRSoggettoOnere=new SiacRSoggettoOnereFin();
		
		siacRSoggettoOnere.setDataCancellazione(siacRSoggettoOnereMod.getDataCancellazione());
		siacRSoggettoOnere.setDataCreazione(siacRSoggettoOnereMod.getDataCreazione());
		siacRSoggettoOnere.setDataFineValidita(siacRSoggettoOnereMod.getDataFineValidita());
		siacRSoggettoOnere.setDataInizioValidita(siacRSoggettoOnereMod.getDataInizioValidita());
		siacRSoggettoOnere.setDataModifica(siacRSoggettoOnereMod.getDataModifica());
		siacRSoggettoOnere.setLoginOperazione(siacRSoggettoOnereMod.getLoginOperazione());
		siacRSoggettoOnere.setSiacDOnere(siacRSoggettoOnereMod.getSiacDOnere());
		siacRSoggettoOnere.setSiacTEnteProprietario(siacRSoggettoOnereMod.getSiacTEnteProprietario());
		siacRSoggettoOnere.setSiacTSoggetto(siacRSoggettoOnereMod.getSiacTSoggetto());
		return siacRSoggettoOnere;
	}

	public static SiacRSoggettoRelazModFin siacRSoggettoRelazToSiacRSoggettoRelazMod(SiacRSoggettoRelazFin siacRSoggettoRelaz){
		SiacRSoggettoRelazModFin siacRSoggettoRelazMod = new SiacRSoggettoRelazModFin();

		siacRSoggettoRelazMod.setDataCancellazione(siacRSoggettoRelaz.getDataCancellazione());
		siacRSoggettoRelazMod.setDataCreazione(siacRSoggettoRelaz.getDataCreazione());
		siacRSoggettoRelazMod.setDataFineValidita(siacRSoggettoRelaz.getDataFineValidita());
		siacRSoggettoRelazMod.setDataInizioValidita(siacRSoggettoRelaz.getDataInizioValidita());
		siacRSoggettoRelazMod.setDataModifica(siacRSoggettoRelaz.getDataModifica());
		siacRSoggettoRelazMod.setLoginOperazione(siacRSoggettoRelaz.getLoginOperazione());
		siacRSoggettoRelazMod.setSiacDRelazTipo(siacRSoggettoRelaz.getSiacDRelazTipo());
		siacRSoggettoRelazMod.setSiacDRuolo1(siacRSoggettoRelaz.getSiacDRuolo1());
		siacRSoggettoRelazMod.setSiacDRuolo2(siacRSoggettoRelaz.getSiacDRuolo2());
		siacRSoggettoRelazMod.setSiacTEnteProprietario(siacRSoggettoRelaz.getSiacTEnteProprietario());
		siacRSoggettoRelazMod.setSiacTSoggetto1(siacRSoggettoRelaz.getSiacTSoggetto1());
		siacRSoggettoRelazMod.setSiacTSoggetto2(siacRSoggettoRelaz.getSiacTSoggetto2());
		
		return siacRSoggettoRelazMod;
	}
	
	public static SiacRSoggettoRelazFin siacRSoggettoRelazModToSiacRSoggettoRelaz(SiacRSoggettoRelazModFin siacRSoggettoRelazMod){
		SiacRSoggettoRelazFin siacRSoggettoRelaz=new SiacRSoggettoRelazFin();
		
		siacRSoggettoRelaz.setDataCancellazione(siacRSoggettoRelazMod.getDataCancellazione());
		siacRSoggettoRelaz.setDataCreazione(siacRSoggettoRelazMod.getDataCreazione());
		siacRSoggettoRelaz.setDataFineValidita(siacRSoggettoRelazMod.getDataFineValidita());
		siacRSoggettoRelaz.setDataInizioValidita(siacRSoggettoRelazMod.getDataInizioValidita());
		siacRSoggettoRelaz.setDataModifica(siacRSoggettoRelazMod.getDataModifica());
		siacRSoggettoRelaz.setLoginOperazione(siacRSoggettoRelazMod.getLoginOperazione());
		siacRSoggettoRelaz.setSiacDRelazTipo(siacRSoggettoRelazMod.getSiacDRelazTipo());
		siacRSoggettoRelaz.setSiacDRuolo1(siacRSoggettoRelazMod.getSiacDRuolo1());
		siacRSoggettoRelaz.setSiacDRuolo2(siacRSoggettoRelazMod.getSiacDRuolo2());
		siacRSoggettoRelaz.setSiacTEnteProprietario(siacRSoggettoRelazMod.getSiacTEnteProprietario());
		siacRSoggettoRelaz.setSiacTSoggetto1(siacRSoggettoRelazMod.getSiacTSoggetto1());
		siacRSoggettoRelaz.setSiacTSoggetto2(siacRSoggettoRelazMod.getSiacTSoggetto2());
		
		return siacRSoggettoRelaz;
	}
}