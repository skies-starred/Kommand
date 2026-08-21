@file:Suppress("Unused")

package foo.starred.kommand.nodes.impl

import com.mojang.brigadier.arguments.ArgumentType
import foo.starred.kommand.nodes.base.IKommandNode

class KommandArgumentNode<S, T>(
    name: String,
    val type: ArgumentType<T>
) : IKommandNode<S>(name)