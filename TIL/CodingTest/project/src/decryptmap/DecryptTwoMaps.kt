package decryptmap

fun decryptTwoMaps() {
    val crypt1 = arrayOf(159, 161, 39, 164, 135, 145, 182, 224)
    val crypt2 = arrayOf(241, 129, 228, 165, 3, 1, 160, 25)

    for (i in crypt1.indices){
        println("crypt1[$i] = ${crypt1[i].toString(2)}")
    }
    for (i in crypt2.indices){
        println("crypt2[$i] = ${crypt2[i].toString(2)}")
    }
    println("단순 더하기 -----------------------------------------------")
    for (i in crypt1.indices){
        println("crypt1[$i] + crypt2[$i]= ${(crypt1[i]+crypt2[i]).toString(2)}")
    }
    println("or 연산자 -----------------------------------------------")

    for (i in crypt1.indices){
        println((crypt1[i] or crypt2[i]).toString(2))
    }

    println("and 연산자 -----------------------------------------------")

    for (i in crypt1.indices){
        println((crypt1[i] and crypt2[i]).toString(2))
    }

    println("xor 연산자 -----------------------------------------------")

    for (i in crypt1.indices){
        println((crypt1[i] xor crypt2[i]).toString(2))
    }

}