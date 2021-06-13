package sort

fun sortInside() {
//    val input = readLine()!!.split(" ").map { it.toInt() }
    val input = readLine()!!.split("")
    val numArray = mutableListOf<Int>()

    for( i in input) {
        if (i.isNotBlank()) {
            numArray.add(i.toInt())
        }
    }

    numArray.sortDescending()
//    println(numArray)

    // 값이 잘 들어왔는지 확인
    numArray.forEach { print(it) }
}

fun sortInside2() {
    val input = readLine()!!

    for (i in 9 downTo 0) {
        for (j in input.indices) {
            if (input[j].toString().toInt() == i) print(i)
        }
    }
}