package com.ahmad.satkerpolri.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PnbpPolriModel {
	private String kodebank;
	private String tahun;
	private String bulan;
	private String tglbayarbpn;
	private String tglbuku;
	private String kodebilling;
	private String ntpn;
	private String kl;
	private String satker;
	private String akun;
	private String description;
	private String nominalakun;
}
