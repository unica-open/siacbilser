/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilser.integration.dao.SiacTAttoAmmRepository;
import it.csi.siac.siacbilser.integration.entity.SiacRAttoAmmClass;
import it.csi.siac.siacbilser.integration.entity.SiacRAttoAmmStato;
import it.csi.siac.siacbilser.integration.entity.SiacRSubdocAttoAmm;
import it.csi.siac.siacbilser.integration.entity.SiacTAttoAllegato;
import it.csi.siac.siacbilser.integration.entity.SiacTAttoAmm;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDClassTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;

/**
 * The Class SubdocumentoSpesaAttoAmmConverter.
 */
@Component
public class SubdocumentoSpesaAttoAmmConverter extends ExtendedDozerConverter<SubdocumentoSpesa, SiacTSubdoc> {
	
	@Autowired
	private SiacTAttoAmmRepository siacTAttoAmmRepository;
	
	/**
	 * Instantiates a new subdocumento spesa atto amm converter.
	 */
	public SubdocumentoSpesaAttoAmmConverter() {
		super(SubdocumentoSpesa.class, SiacTSubdoc.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SubdocumentoSpesa convertFrom(SiacTSubdoc src, SubdocumentoSpesa dest) {
		final String methodName = "convertFrom";
		if(src.getSiacRSubdocAttoAmms()!=null){
			for(SiacRSubdocAttoAmm siacRSubdocClass : src.getSiacRSubdocAttoAmms()){
				if(siacRSubdocClass.getDataCancellazione()!=null) {
					continue;
				}
				
				SiacTAttoAmm siacTAttoAmm = siacTAttoAmmRepository.findOne(siacRSubdocClass.getSiacTAttoAmm().getUid());
				
				log.debug(methodName, "siacRSubdocClass.siacTAttoAmm.uid: " + siacTAttoAmm.getUid());
				
				AttoAmministrativo	atto = dest.getAttoAmministrativo();
				if(atto== null){
					atto = new AttoAmministrativo();
				} 
				
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
				
				// Stato
				if(siacTAttoAmm.getSiacRAttoAmmStatos() != null) {
					for(SiacRAttoAmmStato sraas : siacTAttoAmm.getSiacRAttoAmmStatos()) {
						if(sraas.getDataCancellazione() == null && sraas.getSiacDAttoAmmStato() != null) {
							atto.setStatoOperativo(sraas.getSiacDAttoAmmStato().getAttoammStatoCode());
							break;
						}
					}
				}
				
				
				if(siacTAttoAmm.getSiacTAttoAllegatos() != null){
					AllegatoAtto allegatoAtto = new AllegatoAtto();
					for(SiacTAttoAllegato allegato : siacTAttoAmm.getSiacTAttoAllegatos()){
						if(allegato.getDataCancellazione() == null){
							allegatoAtto.setUid(allegato.getUid());
							break;
						}
					}
					atto.setAllegatoAtto(allegatoAtto);
				}	
				
				StrutturaAmministrativoContabile strutturaAmmContabile = ricercaStrutturaAmministrativoContabile(siacTAttoAmm);
				atto.setStrutturaAmmContabile(strutturaAmmContabile);
				
				dest.setAttoAmministrativo(atto);
			}	
		}
		
		return dest;
	}
	

	/**
	 * Ricerca struttura amministrativo contabile.
	 *
	 * @param siacTAttoAmm the siac t atto amm
	 * @return the struttura amministrativo contabile
	 */
	private StrutturaAmministrativoContabile ricercaStrutturaAmministrativoContabile(SiacTAttoAmm siacTAttoAmm) {
		if(siacTAttoAmm.getSiacRAttoAmmClasses()!=null) {
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
		}
		
		return null;
	}
	
	
	

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTSubdoc convertTo(SubdocumentoSpesa src, SiacTSubdoc dest) {	
		
		if(dest == null) {
			dest = new SiacTSubdoc();
		}
		
		AttoAmministrativo atto = src.getAttoAmministrativo();
		
		// L'atto è facoltativo quindi potrebbe essere passato null.
		if(atto==null || atto.getUid()==0){
			return dest;
		}		
		
		SiacRSubdocAttoAmm siacRSubdocAttoAmm = new SiacRSubdocAttoAmm();
		siacRSubdocAttoAmm.setSiacTSubdoc(dest);
		
		SiacTAttoAmm siacTAttoAmm = new SiacTAttoAmm();
		siacTAttoAmm.setAttoammId(atto.getUid());
		
		siacRSubdocAttoAmm.setSiacTAttoAmm(siacTAttoAmm);
		siacRSubdocAttoAmm.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		siacRSubdocAttoAmm.setLoginOperazione(dest.getLoginOperazione());
		
		List<SiacRSubdocAttoAmm> siacRProgrammaAttoAmms = new ArrayList<SiacRSubdocAttoAmm>();
		siacRProgrammaAttoAmms.add(siacRSubdocAttoAmm);
		dest.setSiacRSubdocAttoAmms(siacRProgrammaAttoAmms);
		
		return dest;
	}



}
