@file:Suppress("Unused")

package foo.starred.kommand.nodes.impl

import foo.starred.kommand.nodes.base.IKommandNode

class KommandLiteralNode<S>(
    name: String
) : IKommandNode<S>(name) {
    val aliases: MutableList<String> = mutableListOf()

    fun alias(vararg names: String): KommandLiteralNode<S> {
        aliases += names
        return this
    }
}