package com.cdcoding.core.navigation.tab.registry

private typealias TabModule = TabRegistry.() -> Unit

public fun tabModule(block: TabModule): TabModule =
    { block() }