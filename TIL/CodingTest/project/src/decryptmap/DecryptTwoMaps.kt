package decryptmap

fun decryptTwoMaps() {
    val crypt1 = arrayOf(159, 161, 39, 164, 135, 145, 182, 224)
    val crypt2 = arrayOf(241, 129, 228, 165, 3, 1, 160, 25)

    for (i in crypt1.indices){
        println("crypt1[$i] = ${crypt1[i].toString(2)} | crypt2[$i] = ${crypt2[i].toString(2)}")
    }

    var orcrypt = arrayOfNulls<Int>(8)
    for (i in crypt1.indices){
        orcrypt[i] = crypt1[i] or crypt2[i]
        println((crypt1[i] or crypt2[i])) // .toString(2)
    }
    println("-----------------------------------")
    for (i in orcrypt.indices) {
        println(orcrypt[i]!!.toString(2))
    }


}