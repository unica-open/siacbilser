/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.csi.siac.siacbilser.integration.entity.SiacRLiquidazioneStato;
import it.csi.siac.siacbilser.integration.entity.SiacRSubdocLiquidazione;
import it.csi.siac.siacbilser.integration.entity.SiacTLiquidazione;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDLiquidazioneStatoEnum;
import it.csi.siac.siaccommonser.util.dozer.MapId;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione.StatoOperativoLiquidazione;

/**
 * The Class SubdocumentoSpesaLiquidazioneBaseConverter
 */
public abstract class SubdocumentoSpesaLiquidazioneBaseConverter extends ExtendedDozerConverter<SubdocumentoSpesa, SiacTSubdoc> {

	/**
	 * Instantiates a new subdocumento spesa liquidazione base converter.
	 */
	protected SubdocumentoSpesaLiquidazioneBaseConverter() {
		super(SubdocumentoSpesa.class, SiacTSubdoc.class);
	}

	@Override
	public SubdocumentoSpesa convertFrom(SiacTSubdoc src, SubdocumentoSpesa dest) {
		final String methodName = "convertFrom";
		
		if(src.getSiacRSubdocLiquidaziones()!=null){
			for (SiacRSubdocLiquidazione  siacRSubdocLiquidazione : src.getSiacRSubdocLiquidaziones()) {
				if(siacRSubdocLiquidazione.getDataCancellazione() != null){
					continue;
				}
				
				SiacTLiquidazione siacTLiquidazione = siacRSubdocLiquidazione.getSiacTLiquidazione();

				if(siacTLiquidazione.getSiacRLiquidazioneStatos() == null){
					log.debug(methodName , "siacTLiquidazione.getSiacRLiquidazioneStatos() == null");
					continue;
				}
				
				if(!isLiquidazioneAnnullata(siacTLiquidazione)) {
					Liquidazione liquidazione = map(siacRSubdocLiquidazione.getSiacTLiquidazione(), Liquidazione.class, getMapId());
					dest.setLiquidazione(liquidazione);
					break;
				}
			}
		}
		return dest;
	}
	
	/**
	 * Controlla se la liquidazione sia o meno annullata.
	 *
	 * @param siacTLiquidazione the siac T liquidazione di cui controllare lo stato
	 * @return <code>true</code>, se la liquidazione &egrave; annullata, <code>false</code> altrimenti
	 */
	private boolean isLiquidazioneAnnullata(SiacTLiquidazione siacTLiquidazione) {
		Date now = new Date();
		String annullatoCode = SiacDLiquidazioneStatoEnum.byStatoOperativoLiquidazione(StatoOperativoLiquidazione.ANNULLATO).getCodice();
		
		for(SiacRLiquidazioneStato srls : siacTLiquidazione.getSiacRLiquidazioneStatos()){
			if(srls.getDataCancellazione() == null && (srls.getDataFineValidita() == null || !now.after(srls.getDataFineValidita()))){
				return annullatoCode.equals(srls.getSiacDLiquidazioneStato().getLiqStatoCode());
			}
		}
		return false;
	}
	
	/**
	 * Ottiene l'id del mapping
	 * @return l'id di mappatura
	 */
	protected abstract MapId getMapId();

	@Override
	public SiacTSubdoc convertTo(SubdocumentoSpesa src, SiacTSubdoc dest) {
		
		if(src.getLiquidazione() != null){
			List<SiacRSubdocLiquidazione> siacRSubdocLiquidaziones = new ArrayList<SiacRSubdocLiquidazione>();
			
			SiacTLiquidazione siacTLiquidazione = new SiacTLiquidazione();
			siacTLiquidazione.setUid(src.getLiquidazione().getUid());
			SiacRSubdocLiquidazione siacRSubdocLiquidazione = new SiacRSubdocLiquidazione();
			siacRSubdocLiquidazione.setSiacTLiquidazione(siacTLiquidazione);
			siacRSubdocLiquidazione.setSiacTSubdoc(dest);
			siacRSubdocLiquidazione.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
			siacRSubdocLiquidazione.setLoginOperazione(dest.getLoginOperazione());
			siacRSubdocLiquidaziones.add(siacRSubdocLiquidazione);
			
			dest.setSiacRSubdocLiquidaziones(siacRSubdocLiquidaziones);
		}
		
		return dest;
	}

}
