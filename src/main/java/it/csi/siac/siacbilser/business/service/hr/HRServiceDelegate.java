/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.hr;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.srvpers.rapws.interfaccecxf.services.missioni.IMissioniService;
import it.csi.srvpers.rapws.interfaccecxf.services.missioni.InsMD060Type;
import it.csi.srvpers.rapws.interfaccecxf.services.missioni.MissioneConsuntivoType;
import it.csi.srvpers.rapws.interfaccecxf.services.missioni.MissioneContabileType;
import it.csi.srvpers.rapws.interfaccecxf.services.missioni.MissioneDataType;
import it.csi.srvpers.rapws.interfaccecxf.services.missioni.MissioniFaultException;

@Component
public class HRServiceDelegate {
	@Autowired
	protected IMissioniService iMissioniService;

	public List<MissioneDataType> viDipendentiMatr(String arg0) {
		try {
			return iMissioniService.viDipendentiMatr(arg0);
		} catch (MissioniFaultException e) {
			throw new BusinessException(e);
		}
	}

	public List<MissioneDataType> viDipendentiCogn(String arg0) {
		try {
			return iMissioniService.viDipendentiCogn(arg0);
		} catch (MissioniFaultException e) {
			throw new BusinessException(e);
		}
	}

	public void updMissioni(String arg0) {
		try {
			iMissioniService.updMissioni(arg0);
		} catch (MissioniFaultException e) {
			throw new BusinessException(e);
		}
	}

	public void insMD060(InsMD060Type arg0) {
		try {
			iMissioniService.insMD060(arg0);
		} catch (MissioniFaultException e) {
			throw new BusinessException(e);
		}
	}

	public List<MissioneConsuntivoType> vm160() {
		try {
			return iMissioniService.vm160();
		} catch (MissioniFaultException e) {
			throw new BusinessException(e);
		}
	}

	public List<MissioneContabileType> vm140() {
		try {
			return iMissioniService.vm140();
		} catch (MissioniFaultException e) {
			throw new BusinessException(e);
		}
	}

}
