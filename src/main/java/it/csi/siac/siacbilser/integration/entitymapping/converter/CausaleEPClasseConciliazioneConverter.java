/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTCausaleEp;
import it.csi.siac.siacgenser.model.CausaleEP;

/**
 * The Class CausaleEPContoConverter.
 *
 * @author Domenico
 */
@Component
@Deprecated
public class CausaleEPClasseConciliazioneConverter extends ExtendedDozerConverter<CausaleEP, SiacTCausaleEp > {
	
	public CausaleEPClasseConciliazioneConverter() {
		super(CausaleEP.class, SiacTCausaleEp.class);
	}

	@Override
	public CausaleEP convertFrom(SiacTCausaleEp src, CausaleEP dest) {
//		if(src.getSiacRConciliazioneClasseCausaleEps() == null) {
//			return dest;
//		}
//		
//		for(SiacRConciliazioneClasseCausaleEp r : src.getSiacRConciliazioneClasseCausaleEps()){
//			if(r.getDataCancellazione()!=null){
//				continue;
//			}
//			SiacDConciliazioneClasseEnum siacDConciliazioneClasseEnum = SiacDConciliazioneClasseEnum.byCodice(r.getSiacDConciliazioneClasse().getConcclaCode());
//			dest.setClasseDiConciliazione(siacDConciliazioneClasseEnum.getClasseDiConciliazione());
//			break;
//		}
		return dest;
		
	}


	@Override
	public SiacTCausaleEp convertTo(CausaleEP src, SiacTCausaleEp dest) {
//		if(src.getClasseDiConciliazione() == null){
//			return dest;
//		}
//		List<SiacRConciliazioneClasseCausaleEp> siacRConciliazioneClasseCausaleEps = new ArrayList<SiacRConciliazioneClasseCausaleEp>();
//		SiacRConciliazioneClasseCausaleEp siacRConciliazioneClasseCausaleEp = new SiacRConciliazioneClasseCausaleEp();
//		
//		siacRConciliazioneClasseCausaleEp.setSiacTCausaleEp(dest);
//		siacRConciliazioneClasseCausaleEp.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
//		SiacDConciliazioneClasse siacDConciliazioneClasse = eef.getEntity(SiacDConciliazioneClasseEnum.byClasseDiConciliazione(src.getClasseDiConciliazione()), dest.getSiacTEnteProprietario().getUid(), SiacDConciliazioneClasse.class);
//		siacRConciliazioneClasseCausaleEp.setSiacDConciliazioneClasse(siacDConciliazioneClasse );
//		siacRConciliazioneClasseCausaleEp.setLoginOperazione(dest.getLoginOperazione());
//		
//		siacRConciliazioneClasseCausaleEps.add(siacRConciliazioneClasseCausaleEp);
//		dest.setSiacRConciliazioneClasseCausaleEps(siacRConciliazioneClasseCausaleEps);
		return dest;
	}

	
}
