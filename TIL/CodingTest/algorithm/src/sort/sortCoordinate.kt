package sort

fun sortCoordinate(){
    // 틀림 !!
    val size: Int = readLine()!!.toInt()
    val xArr = mutableListOf<Int>()
    val yArr = mutableListOf<Int>()
    for (i in 0 until size) {
        val input = readLine()!!.split(" ").map { it.toInt() }
        xArr.add(input[0])
        yArr.add(input[1])
    }

    xArr.sort()
    yArr.sort()

    for (i in 0 until size){
        println("${xArr[i]} ${yArr[i]}")
    }
}