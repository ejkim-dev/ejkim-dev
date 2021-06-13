package algorithm.sort


fun sortNumber2():Unit {
    val size : Int = readLine()!!.toInt()

/*    val arr = Array<Int>(size) { readLine()!!.toInt() }

    for (i in 0 until size)
        println(arr[i])*/


//     val br = System.`in`.bufferedReader()
//    val size : Int = br.readLine()!!.toInt()

    val arr = mutableListOf<Int>()

    for (i in 1..size)
        arr.add(readLine()!!.toInt())

    arr.sort()

    for (i in 0 until size)
        println(arr[i])
}