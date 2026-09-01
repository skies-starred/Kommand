@file:Suppress("Unused")

package foo.starred.kommand.nodes.base

import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider

abstract class IKommandNode<S>(val name: String) {
    val children: MutableList<IKommandNode<S>> = mutableListOf()
    var executor: ((CommandContext<S>) -> Unit)? = null
    var requires: ((S) -> Boolean)? = null
    var suggests: SuggestionProvider<S>? = null

    fun suggests(block: () -> Collection<String>): IKommandNode<S> {
        suggests = SuggestionProvider { _, builder ->
            block().filter { it.contains(builder.remaining, true) }.forEach { builder.suggest(it) }
            builder.buildFuture()
        }

        return this
    }

    fun suggests(provider: SuggestionProvider<S>): IKommandNode<S> {
        suggests = provider
        return this
    }

    fun requires(predicate: (S) -> Boolean): IKommandNode<S> {
        requires = predicate
        return this
    }
}
