package com.spacegame.util

import net.mostlyoriginal.api.operation.common.Operation
import net.mostlyoriginal.api.operation.flow.SequenceOperation

fun sequence(vararg operations: Operation): SequenceOperation {
    val operation: SequenceOperation = Operation.prepare(SequenceOperation::class.java)
    operations.forEach(operation::add)
    return operation
}