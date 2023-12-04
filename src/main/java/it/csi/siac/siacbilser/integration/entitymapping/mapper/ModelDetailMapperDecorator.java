/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.mapper;

import java.util.Map;

import it.csi.siac.siacbilser.integration.dad.mapper.capitolo.CapitoloEntrataGestioneClassificatoriDecorator;
import it.csi.siac.siacbilser.integration.dad.mapper.capitolo.CapitoloUscitaGestioneClassificatoriDecorator;
import it.csi.siac.siacbilser.integration.dad.mapper.movimentogestione.decorator.AccertamentoAttoAmministrativoDecorator;
import it.csi.siac.siacbilser.integration.dad.mapper.movimentogestione.decorator.AccertamentoCapitoloDecorator;
import it.csi.siac.siacbilser.integration.dad.mapper.movimentogestione.decorator.AccertamentoDettagliPerBilancioDecorator;
import it.csi.siac.siacbilser.integration.dad.mapper.movimentogestione.decorator.AccertamentoDettaglioImportiDecorator;
import it.csi.siac.siacbilser.integration.dad.mapper.movimentogestione.decorator.AccertamentoImportiDecorator;
import it.csi.siac.siacbilser.integration.dad.mapper.movimentogestione.decorator.AccertamentoMutuiAssociatiDecorator;
import it.csi.siac.siacbilser.integration.dad.mapper.movimentogestione.decorator.AccertamentoSoggettoDecorator;
import it.csi.siac.siacbilser.integration.dad.mapper.movimentogestione.decorator.AccertamentoStatoOperativoDecorator;
import it.csi.siac.siacbilser.integration.dad.mapper.movimentogestione.decorator.AccertamentoSubaccertamentiDecorator;
import it.csi.siac.siacbilser.integration.dad.mapper.movimentogestione.decorator.AccertamentoTotaleMutuiAssociatiDecorator;
import it.csi.siac.siacbilser.integration.dad.mapper.movimentogestione.decorator.ImpegnoAttoAmministrativoDecorator;
import it.csi.siac.siacbilser.integration.dad.mapper.movimentogestione.decorator.ImpegnoAttributiDecorator;
import it.csi.siac.siacbilser.integration.dad.mapper.movimentogestione.decorator.ImpegnoCapitoloDecorator;
import it.csi.siac.siacbilser.integration.dad.mapper.movimentogestione.decorator.ImpegnoComponenteDecorator;
import it.csi.siac.siacbilser.integration.dad.mapper.movimentogestione.decorator.ImpegnoDettagliPerBilancioDecorator;
import it.csi.siac.siacbilser.integration.dad.mapper.movimentogestione.decorator.ImpegnoDettaglioImportiDecorator;
import it.csi.siac.siacbilser.integration.dad.mapper.movimentogestione.decorator.ImpegnoImportiDecorator;
import it.csi.siac.siacbilser.integration.dad.mapper.movimentogestione.decorator.ImpegnoMutuiAssociatiDecorator;
import it.csi.siac.siacbilser.integration.dad.mapper.movimentogestione.decorator.ImpegnoSoggettoDecorator;
import it.csi.siac.siacbilser.integration.dad.mapper.movimentogestione.decorator.ImpegnoStatoOperativoDecorator;
import it.csi.siac.siacbilser.integration.dad.mapper.movimentogestione.decorator.ImpegnoSubimpegniDecorator;
import it.csi.siac.siacbilser.integration.dad.mapper.movimentogestione.decorator.ImpegnoTotaleMutuiAssociatiDecorator;
import it.csi.siac.siacbilser.integration.dad.mapper.movimentogestione.decorator.MovimentoGestioneStatoOperativoDecorator;
import it.csi.siac.siacbilser.integration.dad.mapper.mutuo.decorator.MutuoAttoAmministrativoDecorator;
import it.csi.siac.siacbilser.integration.dad.mapper.mutuo.decorator.MutuoContoTesoreriaDecorator;
import it.csi.siac.siacbilser.integration.dad.mapper.mutuo.decorator.MutuoDebitoResiduoDecorator;
import it.csi.siac.siacbilser.integration.dad.mapper.mutuo.decorator.MutuoMovimentiGestioneAssociatiDecorator;
import it.csi.siac.siacbilser.integration.dad.mapper.mutuo.decorator.MutuoPeriodoRimborsoDecorator;
import it.csi.siac.siacbilser.integration.dad.mapper.mutuo.decorator.MutuoProgettiAssociatiDecorator;
import it.csi.siac.siacbilser.integration.dad.mapper.mutuo.decorator.MutuoRataDecorator;
import it.csi.siac.siacbilser.integration.dad.mapper.mutuo.decorator.MutuoRipartizioneCapitoliDecorator;
import it.csi.siac.siacbilser.integration.dad.mapper.mutuo.decorator.MutuoSoggettoDecorator;
import it.csi.siac.siacbilser.integration.dad.mapper.mutuo.decorator.MutuoStatoDecorator;
import it.csi.siac.siacbilser.integration.dad.mapper.mutuo.decorator.MutuoTipoFinanziamentoDecorator;
import it.csi.siac.siacbilser.integration.dad.mapper.mutuo.decorator.MutuoTotaliMovimentiGestioneAssociatiDecorator;
import it.csi.siac.siacbilser.integration.dad.mapper.mutuo.decorator.MutuoTotaliPianoAmmortamentoDecorator;
import it.csi.siac.siacbilser.integration.dad.mapper.mutuo.decorator.MutuoTotaliProgettiAssociatiDecorator;
import it.csi.siac.siacbilser.integration.dad.mapper.progetto.decorator.ProgettoAttoAmministrativoDecorator;
import it.csi.siac.siacbilser.integration.dad.mapper.progetto.decorator.ProgettoAttributiDecorator;
import it.csi.siac.siacbilser.integration.dad.mapper.progetto.decorator.ProgettoClassificatoriDecorator;
import it.csi.siac.siacbilser.integration.dad.mapper.progetto.decorator.ProgettoMutuiAssociatiDecorator;
import it.csi.siac.siacbilser.integration.dad.mapper.progetto.decorator.ProgettoStatoDecorator;
import it.csi.siac.siacbilser.model.movimentogestione.MovimentoGestioneModelDetail;
import it.csi.siac.siacbilser.model.mutuo.MutuoModelDetail;
import it.csi.siac.siaccommon.model.ModelDetailEnum;
import it.csi.siac.siaccommon.util.CoreUtil;
import it.csi.siac.siaccommon.util.collections.Function;
import it.csi.siac.siaccommon.util.mapper.MapperDecorator;
import it.csi.siac.siacfin2ser.model.AccertamentoModelDetail;
import it.csi.siac.siacfin2ser.model.CapitoloEntrataGestioneModelDetail;
import it.csi.siac.siacfin2ser.model.CapitoloUscitaGestioneModelDetail;
import it.csi.siac.siacfin2ser.model.ImpegnoModelDetail;
import it.csi.siac.siacgenser.model.ProgettoModelDetail;

public enum ModelDetailMapperDecorator {

	MutuoPeriodoRimborso(MutuoModelDetail.PeriodoRimborso, MutuoPeriodoRimborsoDecorator.class),
	MutuoStato(MutuoModelDetail.Stato, MutuoStatoDecorator.class),
	PianoAmmortamento(MutuoModelDetail.PianoAmmortamento, MutuoRataDecorator.class),
	MutuoSoggetto(MutuoModelDetail.Soggetto, MutuoSoggettoDecorator.class),
	MutuoAttoAmministrativo(MutuoModelDetail.AttoAmministrativo, MutuoAttoAmministrativoDecorator.class),
	MutuoDebitoResiduo(MutuoModelDetail.DebitoResiduo, MutuoDebitoResiduoDecorator.class),
	MutuoContoTesoreria(MutuoModelDetail.ContoTesoreria, MutuoContoTesoreriaDecorator.class),
	MutuoTipoFinanziamento(MutuoModelDetail.TipoFinanziamento, MutuoTipoFinanziamentoDecorator.class),
	TotaliPianoAmmortamento(MutuoModelDetail.TotaliPianoAmmortamento, MutuoTotaliPianoAmmortamentoDecorator.class),
	MutuoMovimentiGestioneAssociati(MutuoModelDetail.MovimentiGestioneAssociati, MutuoMovimentiGestioneAssociatiDecorator.class),
	TotaliMovimentiGestioneAssociati(MutuoModelDetail.TotaliMovimentiGestioneAssociati, MutuoTotaliMovimentiGestioneAssociatiDecorator.class),
	MutuoRiprtizioneCapitoli(MutuoModelDetail.RipartizioneCapitoliConTotali, MutuoRipartizioneCapitoliDecorator.class),
	MutuoProgettiAssociati(MutuoModelDetail.ProgettiAssociati, MutuoProgettiAssociatiDecorator.class),
	TotaliProgettiAssociati(MutuoModelDetail.TotaliProgettiAssociati, MutuoTotaliProgettiAssociatiDecorator.class),
	
	ProgettoAttoAmministrativo(ProgettoModelDetail.AttoAmministrativo, ProgettoAttoAmministrativoDecorator.class),
	ProgettoClassificatori(ProgettoModelDetail.Classificatori, ProgettoClassificatoriDecorator.class),
	ProgettoAttributi(ProgettoModelDetail.Attributi, ProgettoAttributiDecorator.class),
	ProgettoStato(ProgettoModelDetail.Stato, ProgettoStatoDecorator.class),
	ProgettoMutuiAssociati(ProgettoModelDetail.MutuiAssociati, ProgettoMutuiAssociatiDecorator.class),
	
	ImpegnoStato(ImpegnoModelDetail.Stato, ImpegnoStatoOperativoDecorator.class),
	ImpegnoCapitolo(ImpegnoModelDetail.Capitolo, ImpegnoCapitoloDecorator.class),
	ImpegnoSoggetto(ImpegnoModelDetail.Soggetto, ImpegnoSoggettoDecorator.class),
	ImpegnoAttoAmministrativo(ImpegnoModelDetail.AttoAmministrativo, ImpegnoAttoAmministrativoDecorator.class),
	ImpegnoAttributi(ImpegnoModelDetail.Attributi, ImpegnoAttributiDecorator.class),
	ImpegnoImporti(ImpegnoModelDetail.Importi, ImpegnoImportiDecorator.class),
	ImpegnoSubimpegni(ImpegnoModelDetail.Subimpegni, ImpegnoSubimpegniDecorator.class),
	ImpegnoComponente(ImpegnoModelDetail.Componente, ImpegnoComponenteDecorator.class),
	ImpegnoTotaleMutuiAssociati(ImpegnoModelDetail.TotaleMutuiAssociati, ImpegnoTotaleMutuiAssociatiDecorator.class),
	ImpegnoMutuiAssociati(ImpegnoModelDetail.MutuiAssociati, ImpegnoMutuiAssociatiDecorator.class),
	ImpegnoDettaglioImporti(ImpegnoModelDetail.DettaglioImporti, ImpegnoDettaglioImportiDecorator.class),
	ImpegnoDettagliPerBilancio(ImpegnoModelDetail.ElencoDettagliPerBilancio, ImpegnoDettagliPerBilancioDecorator.class),

	AccertamentoStato(AccertamentoModelDetail.Stato, AccertamentoStatoOperativoDecorator.class),
	AccertamentoCapitolo(AccertamentoModelDetail.Capitolo, AccertamentoCapitoloDecorator.class),
	AccertamentoSoggetto(AccertamentoModelDetail.Soggetto, AccertamentoSoggettoDecorator.class),
	AccertamentoAttoAmministrativo(AccertamentoModelDetail.AttoAmministrativo, AccertamentoAttoAmministrativoDecorator.class),
	AccertamentoImporti(AccertamentoModelDetail.Importi, AccertamentoImportiDecorator.class),
	AccertamentoSubaccertamenti(AccertamentoModelDetail.Subaccertamenti, AccertamentoSubaccertamentiDecorator.class),
	AccertamentoTotaleMutuiAssociati(AccertamentoModelDetail.TotaleMutuiAssociati, AccertamentoTotaleMutuiAssociatiDecorator.class),
	AccertamentoMutuiAssociati(AccertamentoModelDetail.MutuiAssociati, AccertamentoMutuiAssociatiDecorator.class),
	AccertamentoDettaglioImporti(AccertamentoModelDetail.DettaglioImporti, AccertamentoDettaglioImportiDecorator.class),
	AccertamentoDettagliPerBilancio(AccertamentoModelDetail.ElencoDettagliPerBilancio, AccertamentoDettagliPerBilancioDecorator.class),
	
	CapitoloUscitaClassificatori(CapitoloUscitaGestioneModelDetail.Classificatori, CapitoloUscitaGestioneClassificatoriDecorator.class), 
	CapitoloEntrataClassificatori(CapitoloEntrataGestioneModelDetail.Classificatori, CapitoloEntrataGestioneClassificatoriDecorator.class),  
	
	MovimentoGestioneStato(MovimentoGestioneModelDetail.Stato, MovimentoGestioneStatoOperativoDecorator.class),
	;

	private ModelDetailEnum modelDetailEnum;
	private Class<? extends MapperDecorator<?, ?>> mapperDecoratorClass;
	
	ModelDetailMapperDecorator(ModelDetailEnum modelDetailEnum, Class<? extends MapperDecorator<?, ?>> mapperDecoratorClass) {
		this.modelDetailEnum = modelDetailEnum;
		this.mapperDecoratorClass = mapperDecoratorClass;
	}

	private static final Map<ModelDetailEnum, ModelDetailMapperDecorator> REVERSE_MAP = 
			CoreUtil.getEnumMap(ModelDetailMapperDecorator.class, new Function<ModelDetailMapperDecorator, ModelDetailEnum>(){
				@Override
				public ModelDetailEnum map(ModelDetailMapperDecorator source) {
					return source.modelDetailEnum;
				}});
	
	public static Class<? extends MapperDecorator<?, ?>> fromModelDetailEnum(ModelDetailEnum modelDetailEnum) {
		ModelDetailMapperDecorator modelDetailMapperDecorator = REVERSE_MAP.get(modelDetailEnum);
		
		if (modelDetailMapperDecorator == null) {
			throw new IllegalStateException(modelDetailEnum + " not mapped in " + ModelDetailMapperDecorator.class.getName());
		}
		
		return modelDetailMapperDecorator.mapperDecoratorClass;
	}

}
