@file:Suppress("Unused")

package foo.starred.kommand

import foo.starred.kommand.scopes.KommandBuilderScope
import foo.starred.kommand.scopes.KommandCommandScope

interface IKommand<S> {
    val loader: KommandCommandScope<S>

    fun command(name: String, block: KommandBuilderScope<S>.() -> Unit) {
        loader.command(name, block)
    }
}