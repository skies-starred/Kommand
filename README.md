# Kommand

A kotlin-first brigadier wrapper. Built primarily for my own projects, but open for anyone to use!

## Including in dependencies

```kotlin
repositories {
    maven("https://maven.starred.foo/releases")
}

dependencies {
    implementation("foo.starred:kommand:<version>")
}
```
Latest kommand version: ``1.0.1``

## Code examples

```kotlin
import foo.starred.kommand.IKommand
import foo.starred.kommand.scopes.KommandScope
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object Example : IKommand<FabricClientCommandSource> {
    override val loader: KommandScope<FabricClientCommandSource> = KommandScope()

    init {
        ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            loader.register(dispatcher)
        }

        command("example") {
            executes { // "/example"
                println("Hello, World!")
            }

            "example1" / "example2" { // "/example example1 example2"
                println("Hello, World! Again!")
            }

            "example2" / string("words") { // "/example example2 <words>"
                val words = string("words")

                println("You typed in \"$words\"!")
            }

            "example3".then {
                "example4" {
                    println("This works as well!")
                }
            }
        }
    }
}
```