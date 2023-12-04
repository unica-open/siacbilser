/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTCassaEconStampa;
import it.csi.siac.siacbilser.integration.entity.SiacTCassaEconStampaValore;
import it.csi.siac.siaccecser.model.StampaGiornale;
import it.csi.siac.siaccecser.model.StampaRendiconto;
import it.csi.siac.siaccecser.model.StampeCassaFile;
import it.csi.siac.siaccecser.model.TipoDocumento;

@Component
public class StampeCassaFileValoreConverter extends ExtendedDozerConverter<StampeCassaFile,SiacTCassaEconStampa > {

	
	/**
	 * Instantiates a new gruppo attivita iva registro iva converter.
	 */
	public StampeCassaFileValoreConverter() {
		super(StampeCassaFile.class, SiacTCassaEconStampa.class);
	}

	@Override
	public StampeCassaFile convertFrom(SiacTCassaEconStampa src, StampeCassaFile dest) {
		if(src.getSiacTCassaEconStampaValores()!=null){
			for(SiacTCassaEconStampaValore siacTCassaEconStampaValore: src.getSiacTCassaEconStampaValores()){
				if(siacTCassaEconStampaValore.getDataCancellazione()==null){				
					
					if(TipoDocumento.GIORNALE_CASSA.equals(dest.getTipoDocumento())){
						StampaGiornale stampaGiornale = new StampaGiornale();
						stampaGiornale.setDataUltimaStampa(siacTCassaEconStampaValore.getGioUltimadatastampadef());
						stampaGiornale.setUltimaPaginaStampataDefinitiva(siacTCassaEconStampaValore.getGioUltimapaginastampadef());
						stampaGiornale.setUltimoImportoEntrataCC(siacTCassaEconStampaValore.getGioUltimoimportoentrataCc());
						stampaGiornale.setUltimoImportoEntrataContanti(siacTCassaEconStampaValore.getGioUltimoimportoentrataContanti());
						stampaGiornale.setUltimoImportoUscitaCC(siacTCassaEconStampaValore.getGioUltimoimportouscitaCc());
						stampaGiornale.setUltimoImportoUscitaContanti(siacTCassaEconStampaValore.getGioUltimoimportouscitaContanti());
						dest.setStampaGiornale(stampaGiornale);
					}
					
					if(TipoDocumento.RENDICONTO.equals(dest.getTipoDocumento())){
						StampaRendiconto stampaRendiconto = new StampaRendiconto();
						stampaRendiconto.setNumeroRendiconto(siacTCassaEconStampaValore.getRenNum());
						stampaRendiconto.setPeriodoDataInizio(siacTCassaEconStampaValore.getRenPeriodoinizio());
						stampaRendiconto.setPeriodoDataFine(siacTCassaEconStampaValore.getRenPeriodofine());
						stampaRendiconto.setDataRendiconto(siacTCassaEconStampaValore.getRenData());
						dest.setStampaRendiconto(stampaRendiconto);
					}
					
					if(TipoDocumento.RICEVUTA.equals(dest.getTipoDocumento())){
						dest.setNumeroMovimento(siacTCassaEconStampaValore.getRicNummovimento());
					}
					
				}
			}
		}
		
		return dest;
	}


	@Override
	public SiacTCassaEconStampa convertTo(StampeCassaFile src, SiacTCassaEconStampa dest) {
		List<SiacTCassaEconStampaValore> siacTCassaEconStampaValores = new ArrayList<SiacTCassaEconStampaValore>();
		SiacTCassaEconStampaValore siacTCassaEconStampaValore = new SiacTCassaEconStampaValore();
		if (src.getStampaGiornale()!=null) {
			siacTCassaEconStampaValore.setGioUltimadatastampadef(src.getStampaGiornale().getDataUltimaStampa());
			siacTCassaEconStampaValore.setGioUltimapaginastampadef(src.getStampaGiornale().getUltimaPaginaStampataDefinitiva());
			siacTCassaEconStampaValore.setGioUltimoimportoentrataCc(src.getStampaGiornale().getUltimoImportoEntrataCC());
			siacTCassaEconStampaValore.setGioUltimoimportoentrataContanti(src.getStampaGiornale().getUltimoImportoEntrataContanti());
			siacTCassaEconStampaValore.setGioUltimoimportouscitaCc(src.getStampaGiornale().getUltimoImportoUscitaCC());
			siacTCassaEconStampaValore.setGioUltimoimportouscitaContanti(src.getStampaGiornale().getUltimoImportoUscitaContanti());
		}
		if (src.getStampaRendiconto()!=null) {
			siacTCassaEconStampaValore.setRenNum(src.getStampaRendiconto().getNumeroRendiconto());
			siacTCassaEconStampaValore.setRenData(src.getStampaRendiconto().getDataRendiconto());
			siacTCassaEconStampaValore.setRenPeriodoinizio(src.getStampaRendiconto().getPeriodoDataInizio());
			siacTCassaEconStampaValore.setRenPeriodofine(src.getStampaRendiconto().getPeriodoDataFine());
		}
		if (src.getNumeroMovimento() != null) {
			siacTCassaEconStampaValore.setRicNummovimento(src.getNumeroMovimento());
		}
		siacTCassaEconStampaValore.setSiacTCassaEconStampa(dest);
		siacTCassaEconStampaValore.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		siacTCassaEconStampaValore.setLoginOperazione(dest.getLoginOperazione());

		
		siacTCassaEconStampaValores.add(siacTCassaEconStampaValore);
		dest.setSiacTCassaEconStampaValores(siacTCassaEconStampaValores);
		return dest;
	}



}
