package com.hypernite.mc.kotlinex.dsl.command.element

import org.bukkit.OfflinePlayer

/*
    User = OfflinePlayer
 */
class UserOrSource(player: OfflinePlayer) : OfflinePlayer by player