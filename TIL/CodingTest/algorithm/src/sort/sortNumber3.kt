package sort

fun sortNumber3() {

    val bw = System.`out`.bufferedWriter()
    val size: Int = readLine()!!.toInt()

    val array = IntArray(10001) // size 로 바꾸면 시간초과
    for (i in 0 until size) {
        array[readLine()!!.toInt()]++
    }

    // indices는 collection의 유효한 인덱스들의 범위를 리턴함
    for (i in array.indices) {
        bw.write("$i\n".repeat(array[i]))
//        println(array[i])
    }
    bw.close()
}