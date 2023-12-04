/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilser.integration.dao.SiacTLiquidazioneRepository;
import it.csi.siac.siacbilser.integration.entity.SiacRAttoAmmClass;
import it.csi.siac.siacbilser.integration.entity.SiacRLiquidazioneAttoAmm;
import it.csi.siac.siacbilser.integration.entity.SiacTAttoAmm;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.SiacTLiquidazione;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDClassTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siaccommonser.util.log.LogSrvUtil;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;

@Component
public class LiquidazioneAttoAmmConverter extends ExtendedDozerConverter<Liquidazione, SiacTLiquidazione> {
	
	private LogSrvUtil log = new LogSrvUtil(this.getClass());
	
	@Autowired
	private SiacTLiquidazioneRepository siacTLiquidazioneRepository;

	
	public LiquidazioneAttoAmmConverter() {
		super(Liquidazione.class, SiacTLiquidazione.class);
	}

	@Override
	public Liquidazione convertFrom(SiacTLiquidazione src, Liquidazione dest) {
		String methodName = "convertFrom";
		SiacTLiquidazione siacTLiquidazione = siacTLiquidazioneRepository.findOne(src.getUid());
		
		if(siacTLiquidazione.getSiacRLiquidazioneAttoAmms()==null){
			log.debug(methodName , "siacTLiquidazione.getSiacRLiquidazioneStatos() == null");
			return dest;
		}
		
		for (SiacRLiquidazioneAttoAmm r : siacTLiquidazione.getSiacRLiquidazioneAttoAmms()) {
			log.debug(methodName , "SiacRLiquidazioneStato con dataCancellazione: " + r.getDataCancellazione() + " e dataFineValidita: " + r.getDataFineValidita());
			if (r.getDataFineValidita() == null && r.getDataCancellazione() == null) {
				SiacTAttoAmm siacTAttoAmm = r.getSiacTAttoAmm();
				AttoAmministrativo atto = new AttoAmministrativo();
				// Popolo l'atto amministrativo
				atto.setUid(siacTAttoAmm.getUid());	
				atto.setAnno(StringUtils.isNotBlank(siacTAttoAmm.getAttoammAnno()) ? Integer.parseInt(siacTAttoAmm.getAttoammAnno()) : 0);
				atto.setNumero(siacTAttoAmm.getAttoammNumero());
				atto.setNote(siacTAttoAmm.getAttoammNote());
				atto.setOggetto(siacTAttoAmm.getAttoammOggetto());
				atto.setDataCreazione(siacTAttoAmm.getDataCreazione());
				
				// Reperisco il tipo dell'atto amministrativo
				TipoAtto tipoAtto = new TipoAtto();
				tipoAtto.setUid(siacTAttoAmm.getSiacDAttoAmmTipo().getUid());
				tipoAtto.setCodice(siacTAttoAmm.getSiacDAttoAmmTipo().getAttoammTipoCode());
				tipoAtto.setDescrizione(siacTAttoAmm.getSiacDAttoAmmTipo().getAttoammTipoDesc());
				atto.setTipoAtto(tipoAtto);
				
				StrutturaAmministrativoContabile strutturaAmmContabile = ricercaStrutturaAmministrativoContabile(siacTAttoAmm);
				atto.setStrutturaAmmContabile(strutturaAmmContabile);
				
				dest.setAttoAmministrativoLiquidazione(atto);
				break;
			}
		}
		return dest;
	}

	@Override
	public SiacTLiquidazione convertTo(Liquidazione src, SiacTLiquidazione dest) {
		
		return dest;
	}

	
	/**
	 * Ricerca struttura amministrativo contabile.
	 *
	 * @param siacTAttoAmm the siac t atto amm
	 * @return the struttura amministrativo contabile
	 */
	private StrutturaAmministrativoContabile ricercaStrutturaAmministrativoContabile(SiacTAttoAmm siacTAttoAmm) {
		
		for(SiacRAttoAmmClass r : siacTAttoAmm.getSiacRAttoAmmClasses()){
			if(r.getDataCancellazione() == null) {
				SiacTClass siacTClass = r.getSiacTClass();
				SiacDClassTipoEnum tipo = SiacDClassTipoEnum.byCodice(siacTClass.getSiacDClassTipo().getClassifTipoCode());				
				if(tipo.getCodificaClass().equals(StrutturaAmministrativoContabile.class)){
					StrutturaAmministrativoContabile sac = new StrutturaAmministrativoContabile();
					map(r.getSiacTClass(),sac,BilMapId.SiacTClass_ClassificatoreGerarchico);
					return sac;
				}
			}
		}
		
		return null;
	}


	

}
