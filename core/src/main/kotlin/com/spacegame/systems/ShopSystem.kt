package com.spacegame.systems

import com.artemis.Aspect
import com.artemis.BaseSystem
import com.artemis.EntitySubscription
import com.artemis.annotations.Wire
import com.artemis.utils.IntBag
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.spacegame.Assets
import com.spacegame.Structure
import com.spacegame.components.*
import com.spacegame.events.PurchaseStructureEvent
import com.spacegame.ui.GameDialog
import net.mostlyoriginal.api.event.common.Subscribe

class ShopSystem : BaseSystem() {
    @Wire
    private lateinit var stage: Stage

    @Wire
    private lateinit var assetManager: Assets

    private lateinit var resources: Resources

    private lateinit var skin: Skin

    // TODO delegate-ify this or something
    private lateinit var placedEntitiesSubscription: EntitySubscription
    private var placedEntities = IntBag()

    override fun initialize() {
        skin = assetManager.get(Assets.UI_SKIN)

        placedEntitiesSubscription = world.aspectSubscriptionManager.get(Aspect.all(BeingPlaced::class.java))
    }

    override fun begin() {
        placedEntities = placedEntitiesSubscription.entities
    }

    override fun processSystem() {}

    @Subscribe
    fun onPurchaseStructure(e: PurchaseStructureEvent) {

        // we're already dropping a miner or something else
        if (!placedEntities.isEmpty) return

        if (resources.minerals < e.structure.price)
            return notEnoughFunds(e.structure)

        resources.minerals -= e.structure.price

        when (e.structure) {
            Structure.MINERAL_MINER -> placeMiner()
            Structure.ENERGY_RELAY -> placeEnergyRelay()
            Structure.SOLAR_STATION -> placeSolarStation()
            Structure.REPAIR_STATION -> placeRepairStation()
            Structure.MISSILE_LAUNCHER -> placeMissileLauncher()
            Structure.LASER_CANNON -> placeLaserCannon()
            Structure.BATTERY -> placeBattery()
        }
    }

    private fun placeBattery() {
        world.edit(world.create())
            .add(BeingPlaced())
            .add(Size())
            .add(Battery())
            .add(EnergyStorer())
            .add(Texture(Assets.BATTERY))
    }

    private fun placeLaserCannon() {}

    private fun placeMissileLauncher() {}

    private fun placeRepairStation() {}

    private fun placeEnergyRelay() {
        world.edit(world.create())
            .add(BeingPlaced())
            .add(Size())
            .add(Powerline())
            .add(Range(200f))
            .add(Texture(Assets.ENERGY_RELAY))
    }

    private fun placeMiner() {
        world.edit(world.create())
            .add(BeingPlaced())
            .add(Size())
            .add(EnergyConsumer())
            .add(MineralMiner())
            .add(AsteroidMiner())
            .add(Range(75f))
            .add(Texture(Assets.MINERAL_MINER))
    }

    private fun placeSolarStation() {
        world.edit(world.create())
            .add(BeingPlaced())
            .add(Size())
            .add(Powerline())
            .add(SolarStation())
            .add(EnergyGenerator())
            .add(EnergyStorer())
            .add(Range(200f))
            .add(Texture(Assets.SOLAR_STATION))
    }

    private fun notEnoughFunds(structure: Structure) {
        GameDialog(skin).also {
            it.text("Not enough funds!")
            it.text("Minerals Needed: ${structure.price}")
            it.button("Sorry")
            it.show(stage)
        }
    }
}