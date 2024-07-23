package com.cdcoding.core.navigation.tab.registry

import cafe.adriel.voyager.core.concurrent.ThreadSafeMap
import cafe.adriel.voyager.core.platform.multiplatformName
import cafe.adriel.voyager.navigator.tab.Tab
import kotlin.reflect.KClass

private typealias ProviderKey = KClass<out TabProvider>

private typealias TabFactory = (TabProvider) -> Tab

public object TabRegistry {

    @PublishedApi
    internal val factories: ThreadSafeMap<ProviderKey, TabFactory> = ThreadSafeMap()

    public operator fun invoke(block: TabRegistry.() -> Unit) {
        this.block()
    }

    public inline fun <reified T : TabProvider> register(noinline factory: (T) -> Tab) {
        factories[T::class] = factory as TabFactory
    }

    public fun get(provider: TabProvider): Tab {
        val factory = factories[provider::class]
            ?: error("TabProvider not registered: ${provider::class.multiplatformName}")
        return factory(provider)
    }
}