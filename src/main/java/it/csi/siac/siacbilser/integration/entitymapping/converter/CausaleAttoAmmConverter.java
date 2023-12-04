/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.integration.entity.SiacDCausale;
import it.csi.siac.siacbilser.integration.entity.SiacRAttoAmmStato;
import it.csi.siac.siacbilser.integration.entity.SiacRCausaleAttoAmm;
import it.csi.siac.siacbilser.integration.entity.SiacTAttoAmm;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfin2ser.model.Causale;

/**
 * The Class CausaleAttoAmmConverter.
 */
@Component
public class CausaleAttoAmmConverter extends ExtendedDozerConverter<Causale, SiacDCausale > {
	
	/**
	 * Instantiates a new causale atto amm converter.
	 */
	public CausaleAttoAmmConverter() {
		super(Causale.class, SiacDCausale.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public Causale convertFrom(SiacDCausale src, Causale dest) {
		
		if(src.getSiacRCausaleAttoAmms()!=null) {
			for(SiacRCausaleAttoAmm siacRCausaleAttoAmm : src.getSiacRCausaleAttoAmms()){
				if((src.getDateToExtract() == null && siacRCausaleAttoAmm.getDataCancellazione()!=null )
						|| (src.getDateToExtract() != null && !src.getDateToExtract().equals(siacRCausaleAttoAmm.getDataInizioValidita()))){
					continue;
				}
				
				SiacTAttoAmm siacTAttoAmm = siacRCausaleAttoAmm.getSiacTAttoAmm();
				
				if(siacTAttoAmm!=null) {
				
					AttoAmministrativo attoAmm = new AttoAmministrativo();
					
					
					map(siacTAttoAmm, attoAmm, BilMapId.SiacTAttoAmm_AttoAmministrativo);
					
//					// Popolo l'atto amministrativo
//					attoAmm.setUid(siacTAttoAmm.getUid());	
//					attoAmm.setAnno(StringUtils.isNotBlank(siacTAttoAmm.getAttoammAnno()) ? Integer.parseInt(siacTAttoAmm.getAttoammAnno()) : 0);
//					attoAmm.setNumero(siacTAttoAmm.getAttoammNumero());
//					attoAmm.setNote(siacTAttoAmm.getAttoammNote());
//					attoAmm.setOggetto(siacTAttoAmm.getAttoammOggetto());
//					attoAmm.setDataCreazione(siacTAttoAmm.getDataCreazione());
//					
//					// Reperisco il tipo dell'atto amministrativo
//					TipoAtto tipoAtto = new TipoAtto();
//					tipoAtto.setUid(siacTAttoAmm.getSiacDAttoAmmTipo().getUid());
//					tipoAtto.setCodice(siacTAttoAmm.getSiacDAttoAmmTipo().getAttoammTipoCode());
//					tipoAtto.setDescrizione(siacTAttoAmm.getSiacDAttoAmmTipo().getAttoammTipoDesc());
//					attoAmm.setTipoAtto(tipoAtto);
					
										
					if(siacTAttoAmm.getSiacRAttoAmmStatos()!=null){						
						for(SiacRAttoAmmStato rac : siacTAttoAmm.getSiacRAttoAmmStatos()){
							if(rac.getDataCancellazione()!=null){
								continue;
							}
							
							String codiceStato = rac.getSiacDAttoAmmStato().getAttoammStatoCode();
							attoAmm.setStatoOperativo(codiceStato);
							
						}
					
					}
					
					dest.setAttoAmministrativo(attoAmm);
				
				}
							
			}	
		}
		
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacDCausale convertTo(Causale src, SiacDCausale dest) {
		
		dest.setSiacRCausaleAttoAmms(new ArrayList<SiacRCausaleAttoAmm>());
		
		if(src.getAttoAmministrativo()==null || src.getAttoAmministrativo().getUid() == 0) { //facoltativo
			return dest;
		}
		
		SiacRCausaleAttoAmm siacRCausaleAttoAmm = new SiacRCausaleAttoAmm();
		siacRCausaleAttoAmm.setSiacDCausale(dest);
		SiacTAttoAmm siacTAttoAmm = new SiacTAttoAmm();
		siacTAttoAmm.setUid(src.getAttoAmministrativo().getUid());	
		siacRCausaleAttoAmm.setSiacTAttoAmm(siacTAttoAmm);
		
		siacRCausaleAttoAmm.setLoginOperazione(dest.getLoginOperazione());
		siacRCausaleAttoAmm.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		
		dest.addSiacRCausaleAttoAmm(siacRCausaleAttoAmm);
		
				
		return dest;
	}



	

}
