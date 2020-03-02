package com.hypernite.mc.kotlinex.dsl.command.element

import org.bukkit.Location
import org.bukkit.World

class LocationSelf(world: World, x: Double, y: Double, z: Double) : Location(world, x, y, z)