creatUI("title", 6) {

    fill {
        item(Material.STONE) {
            display = ""
        }
    }

    +item(Material.PAPER) {
        display = "this is a paper"
    }

    slot(3 row 6) {
        item(Material.APPLE) {
            display = "this is an apple"
        }
    }

    slot(3 row 6, 6 row 4) {
        item(Material.BREAD) {
            display = "this is a bread"
        }
    }

    fill(row = 1..3, column = 2..6) { item }

    fillRow(row = 1..3) { item2 }

    fillColumn(column = 2..6) { item3 }

    ring(padding = 1) { item5 }

    center { item6 }
}