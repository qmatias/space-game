package com.spacegame.systems

import com.artemis.ComponentMapper
import com.artemis.annotations.All
import com.artemis.systems.IteratingSystem
import com.spacegame.components.Connection
import com.spacegame.util.getNullable
import com.spacegame.util.iterator
import com.spacegame.util.toList

class ConnectionParityException : IllegalStateException()

@All(Connection::class)
class ConnectionManager : IteratingSystem() {
    private lateinit var connectionMapper: ComponentMapper<Connection>
    override fun process(e: Int) {
        updateConnection(e)
    }

    override fun inserted(e: Int) {
        updateConnection(e)
    }

    private fun updateConnection(self: Int) {
        val connection = connectionMapper.get(self)
        if (connection.entities.isEmpty) {
            // no more connections; we can remove the component
            connectionMapper.remove(self)
            return
        }

        // check for parity
        for (other in connection.entities) {
            val otherConnections = connectionMapper.getNullable(other)?.entities?.toList() ?: listOf()
            if (self !in otherConnections) {
                throw ConnectionParityException()
            }
        }
    }

    fun addConnection(from: Int, to: Int) {
        val connectionFrom = connectionMapper.create(from)
        val connectionTo = connectionMapper.create(to)

        connectionFrom.entities.add(to)
        connectionTo.entities.add(from)
    }

    fun hasConnection(from: Int, to: Int): Boolean =
        to in getConnections(from)

    fun getConnections(e: Int): List<Int> =
        connectionMapper.getNullable(e)?.entities?.toList() ?: listOf()

    fun removeConnection(from: Int, to: Int) {
        connectionMapper.getNullable(from)?.entities?.removeValue(to)
        connectionMapper.getNullable(to)?.entities?.removeValue(from)
    }
}