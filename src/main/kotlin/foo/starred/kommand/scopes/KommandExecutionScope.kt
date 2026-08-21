@file:Suppress("Unused")

package foo.starred.kommand.scopes

import com.mojang.brigadier.arguments.*
import com.mojang.brigadier.context.CommandContext

class KommandExecutionScope<S>(val context: CommandContext<S>) {
    fun string(name: String): String {
        return StringArgumentType.getString(context, name)
    }

    fun bool(name: String): Boolean {
        return BoolArgumentType.getBool(context, name)
    }

    fun int(name: String): Int {
        return IntegerArgumentType.getInteger(context, name)
    }

    fun long(name: String): Long {
        return LongArgumentType.getLong(context, name)
    }

    fun double(name: String): Double {
        return DoubleArgumentType.getDouble(context, name)
    }

    fun float(name: String): Float {
        return FloatArgumentType.getFloat(context, name)
    }

    inline fun <reified T> argument(name: String): T {
        return context.getArgument(name, T::class.java)
    }
}