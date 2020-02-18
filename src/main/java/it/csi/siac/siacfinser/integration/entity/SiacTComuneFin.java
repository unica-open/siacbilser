/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.csi.siac.siacfinser.integration.entity.base.SiacTEnteBase;

/**
 * The persistent class for the siac_t_comune database table.
 * 
 */
@Entity
@Table(name = "siac_t_comune")
public class SiacTComuneFin extends SiacTEnteBase
{
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "SIAC_T_COMUNE_COMUNE_ID_GENERATOR", allocationSize = 1, sequenceName = "siac_t_comune_comune_id_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SIAC_T_COMUNE_COMUNE_ID_GENERATOR")
	@Column(name = "comune_id")
	private Integer comuneId;

	@Column(name = "comune_desc")
	private String comuneDesc;

	@Column(name = "comune_istat_code")
	private String comuneIstatCode;

	@Column(name = "comune_belfiore_catastale_code")
	private String codiceCatastale;

	// bi-directional many-to-one association to SiacRComuneProvinciaFin
	@OneToMany(mappedBy = "siacTComune")
	private List<SiacRComuneProvinciaFin> siacRComuneProvincias;

	// bi-directional many-to-one association to SiacRComuneRegioneFin
	@OneToMany(mappedBy = "siacTComune")
	private List<SiacRComuneRegioneFin> siacRComuneRegiones;

	// bi-directional many-to-one association to SiacTNazioneFin
	@ManyToOne
	@JoinColumn(name = "nazione_id")
	private SiacTNazioneFin siacTNazione;

	// bi-directional many-to-one association to SiacTIndirizzoSoggettoFin
	@OneToMany(mappedBy = "siacTComune")
	private List<SiacTIndirizzoSoggettoFin> siacTIndirizzoSoggettos;

	// bi-directional many-to-one association to SiacTIndirizzoSoggettoModFin
	@OneToMany(mappedBy = "siacTComune")
	private List<SiacTIndirizzoSoggettoModFin> siacTIndirizzoSoggettoMods;

	// bi-directional many-to-one association to SiacTPersonaFisicaFin
	@OneToMany(mappedBy = "siacTComune")
	private List<SiacTPersonaFisicaFin> siacTPersonaFisicas;

	// bi-directional many-to-one association to SiacTPersonaFisicaModFin
	@OneToMany(mappedBy = "siacTComune")
	private List<SiacTPersonaFisicaModFin> siacTPersonaFisicaMods;

	public SiacTComuneFin()
	{
	}

	public Integer getComuneId()
	{
		return this.comuneId;
	}

	public void setComuneId(Integer comuneId)
	{
		this.comuneId = comuneId;
	}

	public String getComuneDesc()
	{
		return this.comuneDesc;
	}

	public void setComuneDesc(String comuneDesc)
	{
		this.comuneDesc = comuneDesc;
	}

	public String getComuneIstatCode()
	{
		return this.comuneIstatCode;
	}

	public void setComuneIstatCode(String comuneIstatCode)
	{
		this.comuneIstatCode = comuneIstatCode;
	}

	public List<SiacRComuneProvinciaFin> getSiacRComuneProvincias()
	{
		return this.siacRComuneProvincias;
	}

	public void setSiacRComuneProvincias(List<SiacRComuneProvinciaFin> siacRComuneProvincias)
	{
		this.siacRComuneProvincias = siacRComuneProvincias;
	}

	public SiacRComuneProvinciaFin addSiacRComuneProvincia(SiacRComuneProvinciaFin siacRComuneProvincia)
	{
		getSiacRComuneProvincias().add(siacRComuneProvincia);
		siacRComuneProvincia.setSiacTComune(this);

		return siacRComuneProvincia;
	}

	public SiacRComuneProvinciaFin removeSiacRComuneProvincia(SiacRComuneProvinciaFin siacRComuneProvincia)
	{
		getSiacRComuneProvincias().remove(siacRComuneProvincia);
		siacRComuneProvincia.setSiacTComune(null);

		return siacRComuneProvincia;
	}

	public List<SiacRComuneRegioneFin> getSiacRComuneRegiones()
	{
		return this.siacRComuneRegiones;
	}

	public void setSiacRComuneRegiones(List<SiacRComuneRegioneFin> siacRComuneRegiones)
	{
		this.siacRComuneRegiones = siacRComuneRegiones;
	}

	public SiacRComuneRegioneFin addSiacRComuneRegione(SiacRComuneRegioneFin siacRComuneRegione)
	{
		getSiacRComuneRegiones().add(siacRComuneRegione);
		siacRComuneRegione.setSiacTComune(this);

		return siacRComuneRegione;
	}

	public SiacRComuneRegioneFin removeSiacRComuneRegione(SiacRComuneRegioneFin siacRComuneRegione)
	{
		getSiacRComuneRegiones().remove(siacRComuneRegione);
		siacRComuneRegione.setSiacTComune(null);

		return siacRComuneRegione;
	}

	public SiacTNazioneFin getSiacTNazione()
	{
		return this.siacTNazione;
	}

	public void setSiacTNazione(SiacTNazioneFin siacTNazione)
	{
		this.siacTNazione = siacTNazione;
	}

	public List<SiacTIndirizzoSoggettoFin> getSiacTIndirizzoSoggettos()
	{
		return this.siacTIndirizzoSoggettos;
	}

	public void setSiacTIndirizzoSoggettos(List<SiacTIndirizzoSoggettoFin> siacTIndirizzoSoggettos)
	{
		this.siacTIndirizzoSoggettos = siacTIndirizzoSoggettos;
	}

	public SiacTIndirizzoSoggettoFin addSiacTIndirizzoSoggetto(
			SiacTIndirizzoSoggettoFin siacTIndirizzoSoggetto)
	{
		getSiacTIndirizzoSoggettos().add(siacTIndirizzoSoggetto);
		siacTIndirizzoSoggetto.setSiacTComune(this);

		return siacTIndirizzoSoggetto;
	}

	public SiacTIndirizzoSoggettoFin removeSiacTIndirizzoSoggetto(
			SiacTIndirizzoSoggettoFin siacTIndirizzoSoggetto)
	{
		getSiacTIndirizzoSoggettos().remove(siacTIndirizzoSoggetto);
		siacTIndirizzoSoggetto.setSiacTComune(null);

		return siacTIndirizzoSoggetto;
	}

	public List<SiacTIndirizzoSoggettoModFin> getSiacTIndirizzoSoggettoMods()
	{
		return this.siacTIndirizzoSoggettoMods;
	}

	public void setSiacTIndirizzoSoggettoMods(
			List<SiacTIndirizzoSoggettoModFin> siacTIndirizzoSoggettoMods)
	{
		this.siacTIndirizzoSoggettoMods = siacTIndirizzoSoggettoMods;
	}

	public SiacTIndirizzoSoggettoModFin addSiacTIndirizzoSoggettoMod(
			SiacTIndirizzoSoggettoModFin siacTIndirizzoSoggettoMod)
	{
		getSiacTIndirizzoSoggettoMods().add(siacTIndirizzoSoggettoMod);
		siacTIndirizzoSoggettoMod.setSiacTComune(this);

		return siacTIndirizzoSoggettoMod;
	}

	public SiacTIndirizzoSoggettoModFin removeSiacTIndirizzoSoggettoMod(
			SiacTIndirizzoSoggettoModFin siacTIndirizzoSoggettoMod)
	{
		getSiacTIndirizzoSoggettoMods().remove(siacTIndirizzoSoggettoMod);
		siacTIndirizzoSoggettoMod.setSiacTComune(null);

		return siacTIndirizzoSoggettoMod;
	}

	public List<SiacTPersonaFisicaFin> getSiacTPersonaFisicas()
	{
		return this.siacTPersonaFisicas;
	}

	public void setSiacTPersonaFisicas(List<SiacTPersonaFisicaFin> siacTPersonaFisicas)
	{
		this.siacTPersonaFisicas = siacTPersonaFisicas;
	}

	public SiacTPersonaFisicaFin addSiacTPersonaFisica(SiacTPersonaFisicaFin siacTPersonaFisica)
	{
		getSiacTPersonaFisicas().add(siacTPersonaFisica);
		siacTPersonaFisica.setSiacTComune(this);

		return siacTPersonaFisica;
	}

	public SiacTPersonaFisicaFin removeSiacTPersonaFisica(SiacTPersonaFisicaFin siacTPersonaFisica)
	{
		getSiacTPersonaFisicas().remove(siacTPersonaFisica);
		siacTPersonaFisica.setSiacTComune(null);

		return siacTPersonaFisica;
	}

	public List<SiacTPersonaFisicaModFin> getSiacTPersonaFisicaMods()
	{
		return this.siacTPersonaFisicaMods;
	}

	public void setSiacTPersonaFisicaMods(List<SiacTPersonaFisicaModFin> siacTPersonaFisicaMods)
	{
		this.siacTPersonaFisicaMods = siacTPersonaFisicaMods;
	}

	public SiacTPersonaFisicaModFin addSiacTPersonaFisicaMod(
			SiacTPersonaFisicaModFin siacTPersonaFisicaMod)
	{
		getSiacTPersonaFisicaMods().add(siacTPersonaFisicaMod);
		siacTPersonaFisicaMod.setSiacTComune(this);

		return siacTPersonaFisicaMod;
	}

	public SiacTPersonaFisicaModFin removeSiacTPersonaFisicaMod(
			SiacTPersonaFisicaModFin siacTPersonaFisicaMod)
	{
		getSiacTPersonaFisicaMods().remove(siacTPersonaFisicaMod);
		siacTPersonaFisicaMod.setSiacTComune(null);

		return siacTPersonaFisicaMod;
	}

	public String getCodiceCatastale()
	{
		return codiceCatastale;
	}

	public void setCodiceCatastale(String codiceCatastale)
	{
		this.codiceCatastale = codiceCatastale;
	}

	@Override
	public Integer getUid()
	{
		// TODO Auto-generated method stub
		return this.comuneId;
	}

	@Override
	public void setUid(Integer uid)
	{
		// TODO Auto-generated method stub
		this.comuneId = uid;
	}
}