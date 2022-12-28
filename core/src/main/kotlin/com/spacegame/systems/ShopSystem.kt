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
            Structure.MINER -> onPurchaseMiner()
            Structure.SOLAR_STATION -> onPurchaseSolarStation()
            Structure.SHIP_1 -> onPurchaseShip()
            Structure.SHIP_2 -> onPurchaseShip()
            Structure.SHIP_3 -> onPurchaseShip()
        }
    }

    private fun onPurchaseShip() {
        resources.ships += 1
    }

    private fun onPurchaseMiner() {
        val entity = world.create()
        world.edit(entity).create(BeingPlaced::class.java)
        world.edit(entity).create(Spinning::class.java)
        world.edit(entity).create(Size::class.java)
        world.edit(entity).create(Miner::class.java)
    }

    private fun onPurchaseSolarStation() {
        val entity = world.create()
        world.edit(entity).create(BeingPlaced::class.java)
        world.edit(entity).create(Spinning::class.java)
        world.edit(entity).create(Size::class.java)
        world.edit(entity).create(SolarStation::class.java)
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