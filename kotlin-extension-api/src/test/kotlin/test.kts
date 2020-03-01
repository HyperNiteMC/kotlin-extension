command("test") {

    permission("test.use")
    description("test command")
    alias("tester", "testing")

    childs {
        +command("say"){
            permission("test.use.say")
            description("test say command")

            placeholders {
                - cast<Player>("player")
                - cast<Location>("location")
            }

            execute { sender, args ->
                sender.sendMessage("you say ${args["player"]} is shit!")
                sender.sendMessage("you are in ${args<Location>().world.name}")
            }
        }
    }


}