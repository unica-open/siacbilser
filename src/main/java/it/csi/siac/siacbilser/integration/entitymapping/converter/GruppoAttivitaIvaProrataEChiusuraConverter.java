/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRIvaGruppoProrata;
import it.csi.siac.siacbilser.integration.entity.SiacTIvaGruppo;
import it.csi.siac.siacbilser.integration.entity.SiacTIvaProrata;
import it.csi.siac.siacfin2ser.model.GruppoAttivitaIva;
import it.csi.siac.siacfin2ser.model.ProRataEChiusuraGruppoIva;

/**
 * The Class GruppoAttivitaIvaProrataEChiusuraConverter.
 */
@Component
public class GruppoAttivitaIvaProrataEChiusuraConverter extends ExtendedDozerConverter<GruppoAttivitaIva,SiacTIvaGruppo > {

	/**
	 * Instantiates a new gruppo attivita iva prorata e chiusura converter.
	 */
	public GruppoAttivitaIvaProrataEChiusuraConverter() {
		super(GruppoAttivitaIva.class, SiacTIvaGruppo.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public GruppoAttivitaIva convertFrom(SiacTIvaGruppo src, GruppoAttivitaIva dest) {
		if (src.getSiacRIvaGruppoProratas() != null) {

			List<ProRataEChiusuraGruppoIva> listaProRataEChiusuraGruppoIva = new ArrayList<ProRataEChiusuraGruppoIva>();
			for (SiacRIvaGruppoProrata siacRIvaGruppoProrata : src.getSiacRIvaGruppoProratas()) {
				//se ho la data di cancellazione passo alla relazione successiva
				if(siacRIvaGruppoProrata.getDataCancellazione() != null || (dest.getAnnualizzazione() != null && !dest.getAnnualizzazione().equals(siacRIvaGruppoProrata.getIvagruproAnno()))){
					continue;
				}

				SiacTIvaProrata siacTIvaProrata = siacRIvaGruppoProrata.getSiacTIvaProrata();

				ProRataEChiusuraGruppoIva proRataEChiusuraGruppoIva = new ProRataEChiusuraGruppoIva();
				
				proRataEChiusuraGruppoIva.setUid(siacTIvaProrata.getUid());
				proRataEChiusuraGruppoIva.setAnnoEsercizio(siacRIvaGruppoProrata.getIvagruproAnno());
				proRataEChiusuraGruppoIva.setPercentualeProRata(siacTIvaProrata.getIvaproPerc());
				proRataEChiusuraGruppoIva.setIvaPrecedente(siacRIvaGruppoProrata.getIvagruIvaprecedente());
				
				listaProRataEChiusuraGruppoIva.add(proRataEChiusuraGruppoIva);
			}
			
			dest.setListaProRataEChiusuraGruppoIva(listaProRataEChiusuraGruppoIva);
		}
		
		return dest;

	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTIvaGruppo convertTo(GruppoAttivitaIva src, SiacTIvaGruppo dest) {
//		if(dest== null) {
//			return dest;
//		}
//		
//		List<SiacRIvaGruppoProrata> siacRIvaGruppoProratas = new ArrayList<SiacRIvaGruppoProrata>();
//		for(ProRataEChiusuraGruppoIva proRataEChiusuraGruppoIva: src.getListaProRataEChiusuraGruppoIva()){
//			
//			SiacRIvaGruppoProrata siacRIvaGruppoProrata = new SiacRIvaGruppoProrata();	
//		
//			SiacTIvaProrata siacTIvaProrata = new SiacTIvaProrata();	
//			siacTIvaProrata.setIvaproPerc(proRataEChiusuraGruppoIva.getPercentualeProRata());
//			siacTIvaProrata.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());			
//			siacTIvaProrata.setLoginOperazione(dest.getLoginOperazione());
//			
//			siacRIvaGruppoProrata.setSiacTIvaProrata(siacTIvaProrata);
//			siacRIvaGruppoProrata.setSiacTIvaGruppo(dest);
//			
//			siacRIvaGruppoProrata.setIvagruproAnno(proRataEChiusuraGruppoIva.getAnnoEsercizio());
//			siacRIvaGruppoProrata.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
//			siacRIvaGruppoProrata.setLoginOperazione(dest.getLoginOperazione());
//			
//			siacRIvaGruppoProratas.add(siacRIvaGruppoProrata);
//		}
//		
//		
//		dest.setSiacRIvaGruppoProratas(siacRIvaGruppoProratas);
		
		return dest;
	}



	

}
