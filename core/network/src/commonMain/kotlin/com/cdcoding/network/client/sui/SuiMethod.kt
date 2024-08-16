package com.cdcoding.network.client.sui

import com.cdcoding.network.model.JSONRpcMethod

enum class SuiMethod(val value: String) : JSONRpcMethod {
    Coins("suix_getCoins"),
    Balance("suix_getBalance"),
    Balances("suix_getAllBalances"),
    GasPrice("suix_getReferenceGasPrice"),
    Pay("unsafe_pay"),
    PaySui("unsafe_paySui"),
    PayAllSui("unsafe_payAllSui"),
    DryRun("sui_dryRunTransactionBlock"),
    Transaction("sui_getTransactionBlock"),
    Broadcast("sui_executeTransactionBlock"),
    Validators("suix_getValidatorsApy"),
    Delegations("suix_getStakes"),
    GetObject("sui_getObject"),
    CoinMetadata("suix_getCoinMetadata"),
    SystemState("suix_getLatestSuiSystemState"),
    ;

    override fun value(): String = value
}