/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entitymapping.converter.base;

import java.util.EnumSet;
import java.util.Set;

import org.dozer.CustomConverter;
import org.dozer.DozerConverter;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siaccommon.model.ModelDetailEnum;
import it.csi.siac.siaccommonser.integration.entitymapping.Converter;
import it.csi.siac.siacfinser.integration.entitymapping.converter.BilancioAccertamentoConverter;
import it.csi.siac.siacfinser.integration.entitymapping.converter.BilancioImpegnoConverter;
import it.csi.siac.siacfinser.integration.entitymapping.converter.ModificaMovimentoGestioneAccertamentoConverter;
import it.csi.siac.siacfinser.integration.entitymapping.converter.ModificaMovimentoGestioneEntrataImportoConverter;
import it.csi.siac.siacfinser.integration.entitymapping.converter.ModificaMovimentoGestioneImpegnoConverter;
import it.csi.siac.siacfinser.integration.entitymapping.converter.ModificaMovimentoGestioneSpesaCollegataImportoResiduoCollegareConverter;
import it.csi.siac.siacfinser.integration.entitymapping.converter.ModificaMovimentoGestioneSpesaCollegataModificaMovimentoGestioneEntrataConverter;
import it.csi.siac.siacfinser.integration.entitymapping.converter.ModificaMovimentoGestioneSpesaCollegataModificaMovimentoGestioneSpesaConverter;
import it.csi.siac.siacfinser.integration.entitymapping.converter.ModificaMovimentoGestioneSpesaImportoConverter;
import it.csi.siac.siacfinser.integration.entitymapping.converter.ModificaMovimentoGestioneSpesaModificaMovimentoGestioneSpesaCollegataVincoloEsplicitoConverter;
import it.csi.siac.siacfinser.integration.entitymapping.converter.ModificaMovimentoGestioneSpesaModificaMovimentoGestioneSpesaCollegataVincoloImplicitoConverter;
import it.csi.siac.siacfinser.integration.entitymapping.converter.ModificaMovimentogestioneSpesaCollegataVincoloEsplicitoConverter;
import it.csi.siac.siacfinser.integration.entitymapping.converter.VincoloAccertamentoBilancioImpegnoConverter;
import it.csi.siac.siacfinser.integration.entitymapping.converter.VincoloAccertamentoImpegnoConverter;
import it.csi.siac.siacfinser.integration.entitymapping.converter.VincoloAccertamentoImportoImpegnoConverter;
import it.csi.siac.siacfinser.integration.entitymapping.converter.VincoloAccertamentoSommaModificheReimpReannoConverter;
import it.csi.siac.siacfinser.integration.entitymapping.converter.VincoloImpegnoAccertamentoConverter;
import it.csi.siac.siacfinser.integration.entitymapping.converter.VincoloImpegnoAvanzoVincoloConverter;
import it.csi.siac.siacfinser.integration.entitymapping.converter.VincoloImpegnoBilancioAccertamentoConverter;
import it.csi.siac.siacfinser.integration.entitymapping.converter.VincoloImpegnoDiCuiPendingConverter;
import it.csi.siac.siacfinser.integration.entitymapping.converter.VincoloImpegnoImportoAccertamentoConverter;
import it.csi.siac.siacfinser.integration.entitymapping.converter.VincoloImpegnoSommaModificheReimpReannoConverter;
import it.csi.siac.siacfinser.model.movgest.AccertamentoFinModelDetail;
import it.csi.siac.siacfinser.model.movgest.ImpegnoFinModelDetail;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneEntrataFinModelDetail;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesaCollegataFinModelDetail;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesaFinModelDetail;
import it.csi.siac.siacfinser.model.movgest.VincoliMovimentoModelDetail;

/**
 * Mapping tra i ModelDetailEnum e i Converter di Dozer.
 * Permette di ottenere il converter associato ad un ModelDetailEnum.
 * 
 * 
 * @author Domenico
 *
 */

//#### Tips per popolare in "automatico" questo enum:
//## Copiarsi la lista dei converter con estensione .java uno per riga. Effettuare il seguente replace:
//## Find:         SubdocumentoSpesa(.*)Converter\.java
//## Replace With: SubdocumentoSpesa$1\(SubdocumentoSpesa$1Converter\.class, SubdocumentoSpesaModelDetail\.$1\),

public enum ConvertersFin implements Converter   {
	
	//SIAC-8117
	//#### Coverters dell'oggetto ModificaMovimentoGestioneSpesa
	ModificaMovimentoGestioneSpesaCollegataModificaMovimentoGestioneSpesa(ModificaMovimentoGestioneSpesaCollegataModificaMovimentoGestioneSpesaConverter.class, ModificaMovimentoGestioneSpesaCollegataFinModelDetail.ModificaMovimentoGestioneSpesa),
	//#### Coverters dell'oggetto ModificaMovimentoGestioneEntrata
	ModificaMovimentoGestioneSpesaCollegataModificaMovimentoGestioneEntrata(ModificaMovimentoGestioneSpesaCollegataModificaMovimentoGestioneEntrataConverter.class, ModificaMovimentoGestioneSpesaCollegataFinModelDetail.ModificaMovimentoGestioneEntrata),
	//#### Coverters dell'oggetto Impegno
	ModificaMovimentoGestioneSpesaCollegataImpegno(ModificaMovimentoGestioneImpegnoConverter.class, ModificaMovimentoGestioneSpesaFinModelDetail.Impegno),
	//#### Coverters dell'oggetto Accertamento
	ModificaMovimentoGestioneEntrataAccertamento(ModificaMovimentoGestioneAccertamentoConverter.class, ModificaMovimentoGestioneEntrataFinModelDetail.Accertamento),
	//#### Coverters del vincolo esplicito
	ModificaMovimentoGestioneSpesaCollegataVincoloEsplicito(ModificaMovimentogestioneSpesaCollegataVincoloEsplicitoConverter.class, ModificaMovimentoGestioneSpesaCollegataFinModelDetail.VincoloEsplicito),
	//#### Coverters dell'importo della ModificaMovimentoGestioneSpesa e di altri dati della SiacTMovgestTsDetModFin
	ModificaMovimentoGestioneSpesaDatiModificaImportoSpesa(ModificaMovimentoGestioneSpesaImportoConverter.class, ModificaMovimentoGestioneSpesaFinModelDetail.DatiModificaImportoSpesa),
	//#### Coverters dell'importo della ModificaMovimentoGestioneSpesa e di altri dati della SiacTMovgestTsDetModFin
	ModificaMovimentoGestioneEntrataDatiModificaImportoEntrata(ModificaMovimentoGestioneEntrataImportoConverter.class, ModificaMovimentoGestioneEntrataFinModelDetail.DatiModificaImportoEntrata),
	//#### Coverters della ModificaMovimentoGestioneSpesaCollegata sulla ModificaMovimentoGestione
	ModificaMovimentoGestioneSpesaModificaMovimentoGestioneSpesaCollegataVincoloEsplicito(ModificaMovimentoGestioneSpesaModificaMovimentoGestioneSpesaCollegataVincoloEsplicitoConverter.class, ModificaMovimentoGestioneSpesaFinModelDetail.ModificaMovgestCollegataEsp),
	//#### Coverters della ModificaMovimentoGestioneSpesaCollegata sulla ModificaMovimentoGestione
	ModificaMovimentoGestioneSpesaModificaMovimentoGestioneSpesaCollegataVincoloImplicito(ModificaMovimentoGestioneSpesaModificaMovimentoGestioneSpesaCollegataVincoloImplicitoConverter.class, ModificaMovimentoGestioneSpesaFinModelDetail.ModificaMovgestCollegataImp),
	//SIAC-8630
	ModificaMovimentoGestioneSpesaCollegataImportoResiduoCollegare(ModificaMovimentoGestioneSpesaCollegataImportoResiduoCollegareConverter.class, ModificaMovimentoGestioneSpesaCollegataFinModelDetail.ImportoResiduoCollegare),
	
	//SIAC-8650
	//#### Coverters dell'oggetto Impegno
	VincoloAccertamentoImpegno(VincoloAccertamentoImpegnoConverter.class, VincoliMovimentoModelDetail.Impegno),
	//#### Coverters dell'oggetto Accertamento
	VincoloImpegnoAccertamento(VincoloImpegnoAccertamentoConverter.class, VincoliMovimentoModelDetail.Accertamento),
	//#### Coverters dell'oggetto AvanzoVincolo
	VincoloImpegnoAvanzoVincolo(VincoloImpegnoAvanzoVincoloConverter.class, VincoliMovimentoModelDetail.AvanzoVincolo),
	//#### Coverters dell'importo diCuiPending
	VincoloImpegnoDiCuiPending(VincoloImpegnoDiCuiPendingConverter.class, VincoliMovimentoModelDetail.DiCuiPending),	
	//#### Coverters dell'importo sommaImportiModReimpReanno
	VincoloAccertamentoSommaImportiModReimpReanno(VincoloAccertamentoSommaModificheReimpReannoConverter.class, VincoliMovimentoModelDetail.SommaImportiModVincoloAccertamento),	
	//#### Coverters dell'importo sommaImportiModReimpReanno
	VincoloImpegnoSommaImportiModReimpReanno(VincoloImpegnoSommaModificheReimpReannoConverter.class, VincoliMovimentoModelDetail.SommaImportiModVincoloImpegno),	
	//#### Coverters dell'importo dell'impegno nel vincolo
	VincoloAccertamentoImportoImpegno(VincoloAccertamentoImportoImpegnoConverter.class, VincoliMovimentoModelDetail.ImportoImpegno),	
	//#### Coverters dell'importo dell'accertamento nel vincolo
	VincoloImpegnoImportoAccertamento(VincoloImpegnoImportoAccertamentoConverter.class, VincoliMovimentoModelDetail.ImportoAccertamento),	
	//#### Coverters dell'oggetto Bilancio per l'accertamento nel vincolo
	VincoloAccertamentoBilancioImpegno(VincoloAccertamentoBilancioImpegnoConverter.class, VincoliMovimentoModelDetail.BilancioImpegno),	
	//#### Coverters dell'oggetto Bilancio per l'impegno nel vincolo
	VincoloImpegnoBilancioAccertamento(VincoloImpegnoBilancioAccertamentoConverter.class, VincoliMovimentoModelDetail.BilancioAccertamento),	
	
	
	//#### Coverters dell'oggetto Bilancio
	AccertamentoBilancioConverter(BilancioAccertamentoConverter.class, AccertamentoFinModelDetail.Bilancio),	
	//#### Coverters dell'oggetto Bilancio
	ImpegnoBilancioConverter(BilancioImpegnoConverter.class, ImpegnoFinModelDetail.Bilancio),	
	;
	
	private final Class<? extends CustomConverter> converterClass;
	private final ModelDetailEnum modelDetailEnum;
	
	private ConvertersFin(Class<? extends CustomConverter> converterClass, ModelDetailEnum modelDetailEnum){
		this.converterClass = converterClass;
		this.modelDetailEnum = modelDetailEnum;
	}
	

	@Override
	public Class<? extends CustomConverter> getCustomConverterClass() {
		return converterClass;
	}

	/**
	 * @return the allegatoAttoModelDetail
	 */
	public ModelDetailEnum getModelDetail() {
		return modelDetailEnum;
	}
	
	
	public static ConvertersFin byModelDetail(ModelDetailEnum modelDetailEnum){
		for(ConvertersFin e : ConvertersFin.values()){
			if(e.getModelDetail().equals(modelDetailEnum)){
				return e;
			}
		}
		
		throw new IllegalStateException("Impossibile trovare un enum Converters con modelDetailEnum: "+modelDetailEnum);
		
	}
	
	/**
	 * Restituisce l'elenco dei Converter associati ai modelDetails passati come parametro.
	 * L'elenco restituito viene pulito dai duplicati ed ordinato in base all'ordine di dichiarazione dei field nell'Enum.
	 * 
	 * @param modelDetails
	 * @return elenco dei Converters
	 */
	public static ConvertersFin[] byModelDetails(ModelDetailEnum... modelDetails) {
		Set<ConvertersFin> enums = EnumSet.noneOf(ConvertersFin.class);
		
		if(modelDetails!=null){
			for(ModelDetailEnum modelDetailEnum: modelDetails){
				enums.add(byModelDetail(modelDetailEnum));
			}
		}
		
		return enums.toArray(new ConvertersFin[enums.size()]);
		
	}
	
	/**
	 * Ottiene il GeneriType A di un CustomConverter
	 * 
	 * @param customConverter
	 * @return
	 */
	public Class<?> getAType() {
		try {
			return Utility.findGenericType(converterClass, DozerConverter.class, 0);
		} catch(IllegalArgumentException iae) {
			// Errore nella derivazione del tipo generico: ignoro
			return null;
		}
	}


	/**
	 * Ottiene il GeneriType B di un CustomConverter
	 * 
	 * @param customConverter
	 * @return
	 */
	public Class<?> getBType() {
		try {
			return Utility.findGenericType(converterClass, DozerConverter.class, 1);
		} catch(IllegalArgumentException iae) {
			// Errore nella derivazione del tipo generico: ignoro
			return null;
		}
	}


}
