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

// TODO: Auto-generated Javadoc
/**
 * The Class ProrataEChiusuraGruppoIvaGruppoAttivitaIvaConverter.
 */
@Component
public class ProrataEChiusuraGruppoIvaGruppoAttivitaIvaConverter extends ExtendedDozerConverter<ProRataEChiusuraGruppoIva,SiacTIvaProrata> {

	/**
	 * Instantiates a new prorata e chiusura gruppo iva gruppo attivita iva converter.
	 */
	public ProrataEChiusuraGruppoIvaGruppoAttivitaIvaConverter() {
		super(ProRataEChiusuraGruppoIva.class, SiacTIvaProrata.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public ProRataEChiusuraGruppoIva convertFrom(SiacTIvaProrata src, ProRataEChiusuraGruppoIva dest) {
		if (src.getSiacRIvaGruppoProratas() != null) {
			for (SiacRIvaGruppoProrata siacRIvaGruppoProrata : src.getSiacRIvaGruppoProratas()) {
				
				//se ho la data di cancellazione passo alla relazione successiva
				if(siacRIvaGruppoProrata.getDataCancellazione()!=null){
					continue;
				}

				SiacTIvaGruppo siacTIvaGruppo = siacRIvaGruppoProrata.getSiacTIvaGruppo();

				GruppoAttivitaIva gruppoAttivitaIva = null;
				gruppoAttivitaIva = new GruppoAttivitaIva();
				gruppoAttivitaIva.setUid(siacTIvaGruppo.getUid());
				dest.setGruppoAttivitaIva(gruppoAttivitaIva);
				break;
			}
		}

		return dest;

	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTIvaProrata convertTo(ProRataEChiusuraGruppoIva src, SiacTIvaProrata dest) {
		if(dest == null || src == null || src.getGruppoAttivitaIva() == null) {
			return dest;
		}
		
		List<SiacRIvaGruppoProrata> siacRIvaGruppoProratas = new ArrayList<SiacRIvaGruppoProrata>();
		SiacRIvaGruppoProrata siacRIvaGruppoProrata = new SiacRIvaGruppoProrata();
		
		
		SiacTIvaProrata siacTIvaProrata = new SiacTIvaProrata();
		SiacTIvaGruppo siacTIvaGruppo = new SiacTIvaGruppo();
		
		siacTIvaProrata.setIvaproPerc(src.getPercentualeProRata());
		siacTIvaProrata.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		siacTIvaProrata.setLoginOperazione(dest.getLoginOperazione());
		siacTIvaGruppo.setUid(src.getGruppoAttivitaIva().getUid());
		
		siacRIvaGruppoProrata.setIvagruproAnno(src.getAnnoEsercizio());
		siacRIvaGruppoProrata.setSiacTIvaProrata(dest);
		siacRIvaGruppoProrata.setSiacTIvaGruppo(siacTIvaGruppo);
		siacRIvaGruppoProrata.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		siacRIvaGruppoProrata.setLoginOperazione(dest.getLoginOperazione());
		siacRIvaGruppoProrata.setIvagruIvaprecedente(src.getIvaPrecedente());
			
		siacRIvaGruppoProratas.add(siacRIvaGruppoProrata);
		
		dest.setSiacRIvaGruppoProratas(siacRIvaGruppoProratas);
		
		return dest;
	}



	

}
