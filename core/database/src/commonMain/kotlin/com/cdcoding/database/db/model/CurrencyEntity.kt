package com.cdcoding.database.db.model

import com.cdcoding.model.Currency


enum class CurrencyEntity(val string: String) {
	MXN("MXN"),
	CHF("CHF"),
	CNY("CNY"),
	THB("THB"),
	HUF("HUF"),
	AUD("AUD"),
	IDR("IDR"),
	RUB("RUB"),
	ZAR("ZAR"),
	EUR("EUR"),
	NZD("NZD"),
	SAR("SAR"),
	SGD("SGD"),
	BMD("BMD"),
	KWD("KWD"),
	HKD("HKD"),
	JPY("JPY"),
	GBP("GBP"),
	DKK("DKK"),
	KRW("KRW"),
	PHP("PHP"),
	CLP("CLP"),
	TWD("TWD"),
	PKR("PKR"),
	BRL("BRL"),
	CAD("CAD"),
	BHD("BHD"),
	MMK("MMK"),
	VEF("VEF"),
	VND("VND"),
	CZK("CZK"),
	TRY("TRY"),
	INR("INR"),
	ARS("ARS"),
	BDT("BDT"),
	NOK("NOK"),
	USD("USD"),
	LKR("LKR"),
	ILS("ILS"),
	PLN("PLN"),
	NGN("NGN"),
	UAH("UAH"),
	XDR("XDR"),
	MYR("MYR"),
	AED("AED"),
	SEK("SEK");

	companion object {
		operator fun invoke(string: String): ChainEntity? {
			return fromRaw(string)
		}

		private fun fromRaw(string: String): ChainEntity? {
			return ChainEntity.entries.find { it.string == string }
		}
	}
}

fun CurrencyEntity.asExternal(): Currency {
	return when (this) {
		CurrencyEntity.MXN -> Currency.MXN
		CurrencyEntity.CHF -> Currency.CHF
		CurrencyEntity.CNY -> Currency.CNY
		CurrencyEntity.THB -> Currency.THB
		CurrencyEntity.HUF -> Currency.HUF
		CurrencyEntity.AUD -> Currency.AUD
		CurrencyEntity.IDR -> Currency.IDR
		CurrencyEntity.RUB -> Currency.RUB
		CurrencyEntity.ZAR -> Currency.ZAR
		CurrencyEntity.EUR -> Currency.EUR
		CurrencyEntity.NZD -> Currency.NZD
		CurrencyEntity.SAR -> Currency.SAR
		CurrencyEntity.SGD -> Currency.SGD
		CurrencyEntity.BMD -> Currency.BMD
		CurrencyEntity.KWD -> Currency.KWD
		CurrencyEntity.HKD -> Currency.HKD
		CurrencyEntity.JPY -> Currency.JPY
		CurrencyEntity.GBP -> Currency.GBP
		CurrencyEntity.DKK -> Currency.DKK
		CurrencyEntity.KRW -> Currency.KRW
		CurrencyEntity.PHP -> Currency.PHP
		CurrencyEntity.CLP -> Currency.CLP
		CurrencyEntity.TWD -> Currency.TWD
		CurrencyEntity.PKR -> Currency.PKR
		CurrencyEntity.BRL -> Currency.BRL
		CurrencyEntity.CAD -> Currency.CAD
		CurrencyEntity.BHD -> Currency.BHD
		CurrencyEntity.MMK -> Currency.MMK
		CurrencyEntity.VEF -> Currency.VEF
		CurrencyEntity.VND -> Currency.VND
		CurrencyEntity.CZK -> Currency.CZK
		CurrencyEntity.TRY -> Currency.TRY
		CurrencyEntity.INR -> Currency.INR
		CurrencyEntity.ARS -> Currency.ARS
		CurrencyEntity.BDT -> Currency.BDT
		CurrencyEntity.NOK -> Currency.NOK
		CurrencyEntity.USD -> Currency.USD
		CurrencyEntity.LKR -> Currency.LKR
		CurrencyEntity.ILS -> Currency.ILS
		CurrencyEntity.PLN -> Currency.PLN
		CurrencyEntity.NGN -> Currency.NGN
		CurrencyEntity.UAH -> Currency.UAH
		CurrencyEntity.XDR -> Currency.XDR
		CurrencyEntity.MYR -> Currency.MYR
		CurrencyEntity.AED -> Currency.AED
		CurrencyEntity.SEK -> Currency.SEK
	}
}


fun Currency.asEntity(): CurrencyEntity {
	return when (this) {
		Currency.MXN -> CurrencyEntity.MXN
		Currency.CHF -> CurrencyEntity.CHF
		Currency.CNY -> CurrencyEntity.CNY
		Currency.THB -> CurrencyEntity.THB
		Currency.HUF -> CurrencyEntity.HUF
		Currency.AUD -> CurrencyEntity.AUD
		Currency.IDR -> CurrencyEntity.IDR
		Currency.RUB -> CurrencyEntity.RUB
		Currency.ZAR -> CurrencyEntity.ZAR
		Currency.EUR -> CurrencyEntity.EUR
		Currency.NZD -> CurrencyEntity.NZD
		Currency.SAR -> CurrencyEntity.SAR
		Currency.SGD -> CurrencyEntity.SGD
		Currency.BMD -> CurrencyEntity.BMD
		Currency.KWD -> CurrencyEntity.KWD
		Currency.HKD -> CurrencyEntity.HKD
		Currency.JPY -> CurrencyEntity.JPY
		Currency.GBP -> CurrencyEntity.GBP
		Currency.DKK -> CurrencyEntity.DKK
		Currency.KRW -> CurrencyEntity.KRW
		Currency.PHP -> CurrencyEntity.PHP
		Currency.CLP -> CurrencyEntity.CLP
		Currency.TWD -> CurrencyEntity.TWD
		Currency.PKR -> CurrencyEntity.PKR
		Currency.BRL -> CurrencyEntity.BRL
		Currency.CAD -> CurrencyEntity.CAD
		Currency.BHD -> CurrencyEntity.BHD
		Currency.MMK -> CurrencyEntity.MMK
		Currency.VEF -> CurrencyEntity.VEF
		Currency.VND -> CurrencyEntity.VND
		Currency.CZK -> CurrencyEntity.CZK
		Currency.TRY -> CurrencyEntity.TRY
		Currency.INR -> CurrencyEntity.INR
		Currency.ARS -> CurrencyEntity.ARS
		Currency.BDT -> CurrencyEntity.BDT
		Currency.NOK -> CurrencyEntity.NOK
		Currency.USD -> CurrencyEntity.USD
		Currency.LKR -> CurrencyEntity.LKR
		Currency.ILS -> CurrencyEntity.ILS
		Currency.PLN -> CurrencyEntity.PLN
		Currency.NGN -> CurrencyEntity.NGN
		Currency.UAH -> CurrencyEntity.UAH
		Currency.XDR -> CurrencyEntity.XDR
		Currency.MYR -> CurrencyEntity.MYR
		Currency.AED -> CurrencyEntity.AED
		Currency.SEK -> CurrencyEntity.SEK
	}
}

