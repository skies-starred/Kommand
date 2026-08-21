@file:Suppress("Unused")

package foo.starred.kommand.scopes

import com.mojang.brigadier.arguments.*
import foo.starred.kommand.nodes.base.IKommandNode
import foo.starred.kommand.nodes.impl.KommandArgumentNode
import foo.starred.kommand.nodes.impl.KommandLiteralNode

class KommandBuilderScope<S>(private val parent: IKommandNode<S>) {
    fun <T> argument(
        name: String,
        type: ArgumentType<T>,
        block: (KommandExecutionScope<S>.() -> Unit)? = null
    ): KommandArgumentNode<S, T> {
        return KommandArgumentNode<S, T>(name, type).apply {
            block?.let { executor = { KommandExecutionScope(it).block() } }
            parent.children += this
        }
    }

    fun string(
        name: String,
        block: (KommandExecutionScope<S>.() -> Unit)? = null
    ) = argument(name, StringArgumentType.string(), block)

    fun greedyString(
        name: String,
        block: (KommandExecutionScope<S>.() -> Unit)? = null
    ) = argument(name, StringArgumentType.greedyString(), block)

    fun word(
        name: String,
        block: (KommandExecutionScope<S>.() -> Unit)? = null
    ) = argument(name, StringArgumentType.word(), block)

    fun int(
        name: String,
        min: Int = Int.MIN_VALUE,
        max: Int = Int.MAX_VALUE,
        block: (KommandExecutionScope<S>.() -> Unit)? = null
    ) = argument(name, IntegerArgumentType.integer(min, max), block)

    fun long(
        name: String,
        min: Long = Long.MIN_VALUE,
        max: Long = Long.MAX_VALUE,
        block: (KommandExecutionScope<S>.() -> Unit)? = null
    ) = argument(name, LongArgumentType.longArg(min, max), block)

    fun double(
        name: String,
        min: Double = -Double.MAX_VALUE,
        max: Double = Double.MAX_VALUE,
        block: (KommandExecutionScope<S>.() -> Unit)? = null
    ) = argument(name, DoubleArgumentType.doubleArg(min, max), block)

    fun float(
        name: String,
        min: Float = -Float.MAX_VALUE,
        max: Float = Float.MAX_VALUE,
        block: (KommandExecutionScope<S>.() -> Unit)? = null
    ) = argument(name, FloatArgumentType.floatArg(min, max), block)

    fun bool(
        name: String,
        block: (KommandExecutionScope<S>.() -> Unit)? = null
    ) = argument(name, BoolArgumentType.bool(), block)

    fun executes(block: KommandExecutionScope<S>.() -> Unit) {
        parent.executor = { KommandExecutionScope(it).block() }
    }

    fun requires(predicate: (S) -> Boolean) {
        parent.requires(predicate)
    }

    fun alias(vararg names: String) {
        (parent as? KommandLiteralNode<S>)?.alias(*names)
    }

    fun String.requires(predicate: (S) -> Boolean): KommandLiteralNode<S> {
        return literal(this).also { it.requires(predicate) }
    }

    fun String.alias(vararg names: String): KommandLiteralNode<S> {
        return literal(this).also { it.alias(*names) }
    }

    fun String.then(block: KommandBuilderScope<S>.() -> Unit): KommandLiteralNode<S> {
        return literal(this).apply {
            KommandBuilderScope(this).block()
        }
    }

    fun <N : IKommandNode<S>> N.then(block: KommandBuilderScope<S>.() -> Unit): N {
        KommandBuilderScope(this).block()
        return this
    }

    operator fun IKommandNode<S>.div(other: IKommandNode<S>): IKommandNode<S> {
        parent.children -= other
        children += other
        return other
    }

    operator fun IKommandNode<S>.div(other: String): KommandLiteralNode<S> {
        return KommandLiteralNode<S>(other).also(children::add)
    }

    operator fun String.div(other: IKommandNode<S>): IKommandNode<S> {
        parent.children -= other

        literal(this).apply {
            children += other
        }

        return other
    }

    operator fun String.div(other: String): KommandLiteralNode<S> {
        return KommandLiteralNode<S>(this).apply {
            children += KommandLiteralNode(other)
            parent.children += this
        }.children.last() as KommandLiteralNode<S>
    }

    operator fun String.invoke(block: KommandExecutionScope<S>.() -> Unit): KommandLiteralNode<S> {
        return literal(this).apply {
            executor = { KommandExecutionScope(it).block() }
        }
    }

    private fun literal(name: String): KommandLiteralNode<S> {
        return KommandLiteralNode<S>(name).also(parent.children::add)
    }
}