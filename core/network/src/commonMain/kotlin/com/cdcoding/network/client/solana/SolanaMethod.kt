package com.cdcoding.network.client.solana

import com.cdcoding.network.model.JSONRpcMethod

enum class SolanaMethod(val value: String) : JSONRpcMethod {
    GetBalance("getBalance"),
    GetTokenBalance("getTokenAccountBalance"),
    GetTokenAccountByOwner("getTokenAccountsByOwner"),
    GetFees("getFees"),
    RentExemption("getMinimumBalanceForRentExemption"),
    GetLatestBlockhash("getLatestBlockhash"),
    GetPriorityFee("getRecentPrioritizationFees"),
    SendTransaction("sendTransaction"),
    GetTransaction("getTransaction"),
    GetValidators("getVoteAccounts"),
    GetDelegations("getProgramAccounts"),
    GetEpoch("getEpochInfo"),
    GetAccountInfo("getAccountInfo")
    ;

    override fun value(): String = value
}