package tk.jonathancowling.inventorytracker.inventorylist

import tk.jonathancowling.inventorytracker.listclient.Item

class AndroidListItem(private val item: Item, var isInUse: Boolean = true) : Item(item.id, item.name, item.quantity) {
    constructor(id: String, name: String, quantity: Int, isInUse: Boolean = true)
            : this(Item(id, name, quantity), isInUse)
}