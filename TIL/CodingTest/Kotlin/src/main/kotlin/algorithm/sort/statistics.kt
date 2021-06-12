package algorithm.sort

import kotlin.math.roundToInt

fun statistics() {

    val n = readLine()!!.toInt()
    val numList = emptyList<Int>().toMutableList()

    for(i in 0 until n)
    { numList.add(readLine()!!.toInt()) }

    numList.sort()

    var sum = 0.0
    numList.forEach { sum+= it.toDouble()}

    sum/=(n.toDouble())
    println("산술평균: ${sum.roundToInt()}")

    println(sum.roundToInt())
    println("중앙값: ${numList[(numList.size)/2]}")

    println(numList[(numList.size)/2])
    val numGroup = numList.groupBy { it }

    println("numGroup: $numGroup")
    numGroup.map{println(it)}

    val maxSize = (numGroup.values.maxByOrNull{ it.size })!!.size
    println("maxSize: $maxSize")

    val newList = numGroup.filter{it.value.size == maxSize }.values.map{it[0]}.sorted()
    val freqEst = if(newList.size > 1) newList[1] else newList[0]
    println("최빈값: $freqEst")
    println(freqEst)
    println(numList.maxOrNull()!!.toInt() - numList.minOrNull()!!.toInt())
    println("범위: ${numList.maxOrNull()!!.toInt() - numList.minOrNull()!!.toInt()}")
}