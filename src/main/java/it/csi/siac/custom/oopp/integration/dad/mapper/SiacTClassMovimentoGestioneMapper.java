/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.custom.oopp.integration.dad.mapper;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacDClassTipo;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDClassTipoEnum;
import it.csi.siac.siaccommon.util.mapper.BaseMapper;
import it.csi.siac.siacintegser.model.custom.oopp.MovimentoGestione;
import it.csi.siac.siacintegser.model.integ.Cofog;
import it.csi.siac.siacintegser.model.integ.NaturaRicorrente;
import it.csi.siac.siacintegser.model.integ.PianoDeiContiFinanziario;
import it.csi.siac.siacintegser.model.integ.TransazioneUnioneEuropea;

@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SiacTClassMovimentoGestioneMapper extends BaseMapper<SiacTClass, MovimentoGestione> {

	@Override
	public void map(SiacTClass o1, MovimentoGestione o2) {
		if (o1 == null || o2 == null) {
			return;
		}

		if (check(o1.getSiacDClassTipo(), SiacDClassTipoEnum.CofogGruppo)) {
			o2.setCofog(new Cofog(o1.getClassifCode(), o1.getClassifDesc()));
			return;
		}

		if (check(o1.getSiacDClassTipo(), SiacDClassTipoEnum.TransazioneUnioneEuropeaEntrata)) {
			o2.setTransazioneUnioneEuropeaEntrata(new TransazioneUnioneEuropea(o1.getClassifCode(), o1.getClassifDesc()));
			return;
		}

		if (check(o1.getSiacDClassTipo(), SiacDClassTipoEnum.TransazioneUnioneEuropeaSpesa)) {
			o2.setTransazioneUnioneEuropeaSpesa(new TransazioneUnioneEuropea(o1.getClassifCode(), o1.getClassifDesc()));
			return;
		}

		if (check(o1.getSiacDClassTipo(), SiacDClassTipoEnum.RicorrenteEntrata)) {
			o2.setNaturaRicorrenteEntrata(new NaturaRicorrente(o1.getClassifCode(), o1.getClassifDesc()));
			return;
		}

		if (check(o1.getSiacDClassTipo(), SiacDClassTipoEnum.RicorrenteSpesa)) {
			o2.setNaturaRicorrenteSpesa(new NaturaRicorrente(o1.getClassifCode(), o1.getClassifDesc()));
			return;
		}

		if (check(o1.getSiacDClassTipo(), SiacDClassTipoEnum.QuintoLivelloPdc)) {
			o2.setPianoDeiContiFinanziario(new PianoDeiContiFinanziario(o1.getClassifCode(), o1.getClassifDesc()));
			return;
		}

	/*
		if (check(o1.getSiacDClassTipo(), SiacDClassTipoEnum.CofogGruppo)) {
			o2.setCofog(new Cofog(o1.getClassifCode(), o1.getClassifDesc()));
			return;
		}

		if (check(o1.getSiacDClassTipo(), SiacDClassTipoEnum.Macroaggregato)) {
			o2.setMacroaggregato(new Macroaggregato(o1.getClassifCode(), o1.getClassifDesc()));
			return;
		}

		if (check(o1.getSiacDClassTipo(), SiacDClassTipoEnum.Categoria)) {
			o2.setCategoria(new Categoria(o1.getClassifCode(), o1.getClassifDesc()));
			return;
		}

		if (check(o1.getSiacDClassTipo(), SiacDClassTipoEnum.Programma)) {
			o2.setProgramma(new Programma(o1.getClassifCode(), o1.getClassifDesc()));
			return;
		}

		if (check(o1.getSiacDClassTipo(), SiacDClassTipoEnum.Missione)) {
			o2.setMissione(new Missione(o1.getClassifCode(), o1.getClassifDesc()));
			return;
		}

		if (check(o1.getSiacDClassTipo(), SiacDClassTipoEnum.Tipologia)) {
			o2.setTipologia(new Tipologia(o1.getClassifCode(), o1.getClassifDesc()));
			return;
		}

		if (check(o1.getSiacDClassTipo(), SiacDClassTipoEnum.TipoFinanziamento)) {
			o2.setTipoFinanziamento(new TipoFinanziamento(o1.getClassifCode(), o1.getClassifDesc()));
			return;
		}

		if (check(o1.getSiacDClassTipo(), SiacDClassTipoEnum.TitoloEntrata)) {
			o2.setTitoloEntrata(new Titolo(o1.getClassifCode(), o1.getClassifDesc()));
			return;
		}

		if (check(o1.getSiacDClassTipo(), SiacDClassTipoEnum.TitoloSpesa)) {
			o2.setTitoloSpesa(new Titolo(o1.getClassifCode(), o1.getClassifDesc()));
			return;
		}

*/	}

	private boolean check(SiacDClassTipo siacDClassTipo, SiacDClassTipoEnum siacDClassTipoEnum) {
		return 
			siacDClassTipo != null && 
			siacDClassTipo.isEntitaValida() && 
			siacDClassTipoEnum.getCodice().equals(siacDClassTipo.getClassifTipoCode());
	}
}
