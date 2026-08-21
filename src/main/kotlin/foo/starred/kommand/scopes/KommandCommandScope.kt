@file:Suppress("Unused")

package foo.starred.kommand.scopes

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import foo.starred.kommand.nodes.base.IKommandNode
import foo.starred.kommand.nodes.impl.KommandArgumentNode
import foo.starred.kommand.nodes.impl.KommandLiteralNode

open class KommandCommandScope<S> {
    val roots = mutableListOf<KommandLiteralNode<S>>()

    fun command(name: String, block: KommandBuilderScope<S>.() -> Unit): KommandLiteralNode<S> {
        val root = KommandLiteralNode<S>(name)
        KommandBuilderScope(root).block()
        roots += root
        return root
    }

    fun register(dispatcher: CommandDispatcher<S>) {
        for (r in roots) {
            val target = dispatcher.register(literal(r))
            for (name in r.aliases) dispatcher.register(LiteralArgumentBuilder.literal<S>(name).redirect(target))
        }
    }

    fun register(dispatcher: CommandDispatcher<S>, vararg nodes: KommandLiteralNode<S>) {
        for (root in nodes) {
            val target = dispatcher.register(literal(root))
            for (name in root.aliases) dispatcher.register(LiteralArgumentBuilder.literal<S>(name).redirect(target))
        }
    }

    fun literal(node: KommandLiteralNode<S>): LiteralArgumentBuilder<S> {
        val builder = LiteralArgumentBuilder.literal<S>(node.name)
        build(builder, node)
        return builder
    }

    fun <T> argument(node: KommandArgumentNode<S, T>): RequiredArgumentBuilder<S, T> {
        val builder = RequiredArgumentBuilder.argument<S, T>(node.name, node.type)
        node.suggests?.let { builder.suggests(it) }
        build(builder, node)
        return builder
    }

    private fun <B : ArgumentBuilder<S, B>> build(builder: B, node: IKommandNode<S>) {
        node.requires?.let { a ->
            builder.requires { b ->
                a(b)
            }
        }

        node.executor?.let { a ->
            builder.executes { b ->
                a(b)
                Command.SINGLE_SUCCESS
            }
        }

        for (child in node.children) {
            when (child) {
                is KommandLiteralNode<S> -> {
                    val root = literal(child)
                    val target = root.build()
                    builder.then(target)

                    for (name in child.aliases) builder.then(LiteralArgumentBuilder.literal<S>(name).redirect(target))
                }

                is KommandArgumentNode<S, *> -> {
                    builder.then(argument(child))
                }
            }
        }
    }
}