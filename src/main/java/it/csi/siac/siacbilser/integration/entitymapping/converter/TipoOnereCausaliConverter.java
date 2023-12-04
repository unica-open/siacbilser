/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacDCausaleRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDCausale;
import it.csi.siac.siacbilser.integration.entity.SiacDCausaleFamTipo;
import it.csi.siac.siacbilser.integration.entity.SiacDModello;
import it.csi.siac.siacbilser.integration.entity.SiacDOnere;
import it.csi.siac.siacbilser.integration.entity.SiacRCausaleTipo;
import it.csi.siac.siacbilser.integration.entity.SiacROnereCausale;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCausaleFamTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDModelloEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siacfin2ser.model.Causale;
import it.csi.siac.siacfin2ser.model.Causale770;
import it.csi.siac.siacfin2ser.model.CausaleEntrata;
import it.csi.siac.siacfin2ser.model.CausaleSpesa;
import it.csi.siac.siacfin2ser.model.ModelloCausale;
import it.csi.siac.siacfin2ser.model.TipoFamigliaCausale;
import it.csi.siac.siacfin2ser.model.TipoOnere;


/**
 * The Class OnereAttrConverter.
 */
@Component
public class TipoOnereCausaliConverter extends ExtendedDozerConverter<TipoOnere, SiacDOnere > {
	
	@Autowired
	private SiacDCausaleRepository siacDCausaleRepository;
	
	/**
	 * Instantiates a new onere attr converter.
	 */
	public TipoOnereCausaliConverter() {
		super(TipoOnere.class, SiacDOnere.class);
	}

	public TipoOnere convertFrom(SiacDOnere src, TipoOnere dest) {
		final String methodName = "convertFrom";
		
		List<SiacROnereCausale> siacROnereCausales = src.getSiacROnereCausales();
		
		if(siacROnereCausales == null || siacROnereCausales.isEmpty()){
			log.debug(methodName, "tipo onere non ha causali");
			return dest;
		}
		List<Causale> listaCausali = new ArrayList<Causale>();
		
		for(SiacROnereCausale siacROnereCausale : siacROnereCausales){
			
			if((src.getDateToExtract() == null && siacROnereCausale.getDataCancellazione()!=null )
					|| (src.getDateToExtract() != null && !src.getDateToExtract().equals(siacROnereCausale.getDataInizioValidita()))){
				continue;
			}	
			
			
			SiacDCausale siacDCausale = siacROnereCausale.getSiacDCausale();
			siacDCausale.setDateToExtract(src.getDateToExtract());
				
			SiacDModello siacDModello = siacDCausale.getSiacDModello();
			if(siacDModello == null) {
				throw new IllegalStateException("Impossibile ottenere il modello per Causale.uid:"+siacDCausale.getUid());
			}
			ModelloCausale modello = SiacDModelloEnum.byCodice(siacDModello.getModelCode()).getModello();
			
					
			TipoFamigliaCausale tipoFamigliaCausale = null;
			for(SiacRCausaleTipo siacRCausaleTipo: siacDCausale.getSiacRCausaleTipos()) {
				if((src.getDateToExtract() == null && siacRCausaleTipo.getDataCancellazione()!=null )
						|| (src.getDateToExtract() != null && !src.getDateToExtract().equals(siacRCausaleTipo.getDataInizioValidita()))){
					continue;
				}
				
				SiacDCausaleFamTipo siacDCausaleFamTipo = siacRCausaleTipo.getSiacDCausaleTipo().getSiacDCausaleFamTipo();
				tipoFamigliaCausale = SiacDCausaleFamTipoEnum.byCodice(siacDCausaleFamTipo.getCausFamTipoCode()).getTipoFamigliaCausale();
				break;
			}
			
			if(tipoFamigliaCausale==null && ModelloCausale.ONERI.equals(modello)){
				
				String msg = "Impossibile ottenere TipoFamigliaCausale per Causale.uid:"+siacDCausale.getUid()  + "."
						+ (src.getDateToExtract()!=null?" dateToExtract: "+src.getDateToExtract():"")
						+ (modello!=null?" ModelloCausale: "+modello:"");
				
				log.warn(methodName, msg);
				throw new IllegalStateException(msg);
			}
			
			log.debug(methodName, "trovata causale: " + siacDCausale.getUid() + "con modello: " +
					modello + "e tipo famiglia: " + tipoFamigliaCausale);
			
			if(ModelloCausale.CAUSALE_770.equals(modello)){
				Causale770 causale770 = new Causale770();
				map(siacDCausale, causale770, BilMapId.SiacDCausale_Causale770);
				listaCausali.add(causale770);
			}else if(ModelloCausale.ONERI.equals(modello) && TipoFamigliaCausale.SPESA.equals(tipoFamigliaCausale)){
				CausaleSpesa causaleSpesa = new CausaleSpesa();
				map(siacDCausale, causaleSpesa, BilMapId.SiacDCausale_CausaleSpesa);
				listaCausali.add(causaleSpesa);
			}else if(ModelloCausale.ONERI.equals(modello) && TipoFamigliaCausale.ENTRATA.equals(tipoFamigliaCausale)){
				CausaleEntrata causaleEntrata = new CausaleEntrata();
				map(siacDCausale, causaleEntrata, BilMapId.SiacDCausale_CausaleEntrata);
				listaCausali.add(causaleEntrata);
			} else {
				log.warn(methodName, "ModelloCausale non supportato: "+ modello + " tipoFamigliaCausale:"+tipoFamigliaCausale);
			}
		}
		
		dest.setCausali(listaCausali);
		return dest;
	}

	@Override
	public SiacDOnere convertTo(TipoOnere src, SiacDOnere dest) {	
		
		if(src.getCausali() == null){
			return dest;
		}
		dest.setSiacROnereCausales(new ArrayList<SiacROnereCausale>());
		
		for(Causale causale : src.getCausali()){
			
//			SiacDCausale siacDCausale = new SiacDCausale();
//			siacDCausale.setUid(causale.getUid());
			
			SiacDCausale siacDCausale = new SiacDCausale();
			siacDCausale.setLoginOperazione(dest.getLoginOperazione());
			Ente ente = new Ente();
			ente.setUid(dest.getSiacTEnteProprietario().getUid());
			causale.setEnte(ente);
			
			if(causale instanceof CausaleSpesa){
				map((CausaleSpesa)causale, siacDCausale, BilMapId.SiacDCausale_CausaleSpesa);
				siacDCausale.setOnCascade(true);
				siacDCausale.setLoginOperazione(dest.getLoginOperazione());
			} else if(causale instanceof CausaleEntrata){
				map((CausaleEntrata)causale, siacDCausale, BilMapId.SiacDCausale_CausaleEntrata);
				siacDCausale.setOnCascade(true);
				siacDCausale.setLoginOperazione(dest.getLoginOperazione());
			} else if(causale instanceof Causale770){
				siacDCausale = siacDCausaleRepository.findOne(causale.getUid());
				if(siacDCausale==null){
					throw new IllegalArgumentException("Impossibile trovare causale 770 con uid: "+causale.getUid());
				}
				//map((Causale770)causale, siacDCausale, BilMapId.SiacDCausale_Causale770);
				
			} else {
				throw new IllegalArgumentException("Causale di tipo non supportato! ["+causale.getClass().getName()+"] ");
			}
			
			
			SiacROnereCausale siacROnereCausale = new SiacROnereCausale();
			siacROnereCausale.setSiacDCausale(siacDCausale);
			siacROnereCausale.setSiacDOnere(dest);
			siacROnereCausale.setLoginOperazione(dest.getLoginOperazione());
			siacROnereCausale.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
			dest.addSiacROnereCausale(siacROnereCausale);
		}
		return dest;	
		
	}
	

}
