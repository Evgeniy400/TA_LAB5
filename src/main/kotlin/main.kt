import kotlin.collections.ArrayList

public class MP_up {


// "" - отвергнуть
// "П" - перенос
// "ОП" - опознание
// "Д" - допуск

    // константы определяющие восходящий мп преобразователь
    private val MP = arrayOf(
        arrayOf("", "", "", "", "", "", "", "", "", "", "", "", "", "ОП"),
        arrayOf("", "", "П", "", "", "", "", "", "", "", "", "", "П", "ОП"),
        arrayOf("", "", "ОП", "", "", "", "", "", "", "", "", "", "П", "ОП"),
        arrayOf("", "П", "ОП", "П", "", "", "", "", "", "П", "П", "П", "ОП", "ОП"),
        arrayOf("", "П", "", "", "", "", "", "П", "П", "", "", "", "", ""),
        arrayOf("П", "", "", "", "П", "", "", "П", "П", "", "", "", "", ""),
        arrayOf("", "П", "ОП", "П", "", "", "", "П", "П", "", "", "", "ОП", "ОП"),
        arrayOf("", "", "ОП", "", "", "", "", "", "", "", "", "", "ОП", "ОП"),
        arrayOf("", "", "", "", "", "П", "П", "", "", "П", "", "", "", ""),
        arrayOf("", "", "", "", "П", "", "", "П", "П", "", "", "", "", ""),
        arrayOf("", "", "", "", "П", "", "", "", "", "", "", "", "", ""),
        arrayOf("", "", "", "", "", "", "", "", "П", "", "", "", "", ""),
        arrayOf("", "", "", "", "П", "", "", "П", "П", "", "", "", "", ""),
        arrayOf("", "ОП", "ОП", "ОП", "", "", "", "", "", "ОП", "ОП", "ОП", "ОП", "ОП"),
        arrayOf("", "", "", "", "", "", "", "П", "П", "", "", "", "", ""),
        arrayOf("", "", "", "", "", "", "", "П", "П", "", "", "", "", ""),
        arrayOf("П", "", "ОП", "", "П", "", "", "П", "П", "", "", "", "ОП", "ОП"),
        arrayOf("П", "", "", "", "П", "", "", "П", "П", "", "", "", "", "Д")
    )
    private val cols = arrayOf("{", "[", "]", "}", "a", "=", "<", "!", "(", ")", "|", "&", ";", "┤")
    private val rows =
        arrayOf("S'", "S", "O", "Y", "{", "[", "]", "}", "a", "=", "<", "!", "(", ")", "|", "&", ";", "∇")

    private var mag = ArrayList<String>(arrayListOf("∇"))


    private var rules: ArrayList<Int> = arrayListOf()
    private var expr = "";

    /*
    возвращает строку, которую нужно втолкнуть в магазин
    или пустую строку в случе допуска
    null означает отвергнуть
     */
    private fun graph(): String? {
        var rule: Int = 0
        var ret: String = ""
        when (mag.last()) {
            "S" -> {
                mag.removeAt(mag.lastIndex)
                ret = "S'"
            }
            "S'" -> {
                mag.removeAt(mag.lastIndex)
                ret = ""
                rule = 1
            }
            "O", ";" -> {
                ret = "S"
                if (mag.last() == ";") {
                    mag.removeAt(mag.lastIndex)
                    if (mag.last() == "O")
                        mag.removeAt(mag.lastIndex)
                    else
                        return null
                    if (mag.last() == "[" || mag.last() == "∇") {
                        rule = 3
                    } else
                        return null
                }
                else if (mag.last() == "O") {
                    mag.removeAt(mag.lastIndex)
                    if (mag.last() == ";")
                        mag.removeAt(mag.lastIndex)
                    else
                        return null
                    if (mag.last() == "S") {
                        mag.removeAt(mag.lastIndex)
                        rule = 2
                    } else
                        return null

                }
            }
            ")" -> {
                ret = "Y"
                mag.removeAt(mag.lastIndex)
                if (mag.last() == "Y") {
                    mag.removeAt(mag.lastIndex)
                    when (mag.last()) {
                        "|", "&" -> {

                            rule = if (mag.last() == "|") 9 else 10
                            mag.removeAt(mag.lastIndex)
                            if (mag.last() == "Y")
                                mag.removeAt(mag.lastIndex)
                            else
                                return null
                            if (mag.last() == "(")
                                mag.removeAt(mag.lastIndex)
                            else
                                return null
                        }
                        "(" -> {
                            mag.removeAt(mag.lastIndex)
                            rule = 11
                        }
                    }
                    if (mag.last() == "!")
                        mag.removeAt(mag.lastIndex)
                    else if (!(mag.last() == "[" || mag.last() == "}" || mag.last() == ";" || mag.last() == "]" || mag.last() == "|" || mag.last() == ")" || mag.last() == "&" || mag.last() == "=" || mag.last() == "┤"))
                        return null
                } else {
                    if (mag.last() == "a")
                        mag.removeAt(mag.lastIndex)
                    else
                        return null
                    if (mag.last() == "<" || mag.last() == "=") {

                        rule = if (mag.last() == "<") 13 else 12
                        mag.removeAt(mag.lastIndex)
                    } else
                        return null
                    if (mag.last() == "a")
                        mag.removeAt(mag.lastIndex)
                    else
                        return null
                    if (mag.last() == "(")
                        mag.removeAt(mag.lastIndex)
                    else
                        return null
                }
            }
            "]" -> {
                mag.removeAt(mag.lastIndex)
                ret = "O"
                if (mag.last() == "S")
                    mag.removeAt(mag.lastIndex)
                else
                    return null
                if (mag.last() == "[")
                    mag.removeAt(mag.lastIndex)
                else
                    return null
                if (mag.last() == "Y") {
                    mag.removeAt(mag.lastIndex)
                    rule = 4
                } else {
                    if (mag.last() == "]") {
                        mag.removeAt(mag.lastIndex)
                        if (mag.last() == "S")
                            mag.removeAt(mag.lastIndex)
                        else
                            return null
                        if (mag.last() == "[")
                            mag.removeAt(mag.lastIndex)
                        else
                            return null
                        if (mag.last() == "Y") {
                            mag.removeAt(mag.lastIndex)
                            rule = 5
                        }
                    } else
                        return null
                }
            }
            "Y" -> {
                ret = "O"
                rule = 8
                mag.removeAt(mag.lastIndex)
                if (mag.last() == "=")
                    mag.removeAt(mag.lastIndex)
                else
                    return null
                if (mag.last() == "a")
                    mag.removeAt(mag.lastIndex)
                else
                    return null
            }
            "}" -> {
                ret = "O"
                mag.removeAt(mag.lastIndex)
                if (mag.last() == "Y") {
                    mag.removeAt(mag.lastIndex)
                    rule = 6
                    if (mag.last() == "]")
                        mag.removeAt(mag.lastIndex)
                    else
                        return null
                    if (mag.last() == "S")
                        mag.removeAt(mag.lastIndex)
                    else
                        return null
                    if (mag.last() == "[")
                        mag.removeAt(mag.lastIndex)
                    else
                        return null
                    if (mag.last() == "{")
                        mag.removeAt(mag.lastIndex)
                    else
                        return null
                } else {
                    if (mag.last() == "]"){
                        mag.removeAt(mag.lastIndex)
                        rule = 7
                        if (mag.last() == "S")
                            mag.removeAt(mag.lastIndex)
                        else
                            return null
                        if (mag.last() == "[")
                            mag.removeAt(mag.lastIndex)
                        else
                            return null
                        if (mag.last() == "Y")
                            mag.removeAt(mag.lastIndex)
                        else
                            return null
                        if (mag.last() == "{")
                            mag.removeAt(mag.lastIndex)
                        else
                            return null
                    }
                    else
                        return null
                }

            }
        }
        if (rule != 0)
            rules.add(rule)
        return ret
    }


    private fun showRules() {
        for (rule in rules) {
            println("Применим правило $rule")
        }
    }


    public fun solve(expr: String) {
        var _expr = expr + "┤"
        while (_expr.isNotEmpty()) {
            when (MP[rows.indexOf(mag.last())][cols.indexOf(_expr.first().toString())]) {
                "П" -> {
                    mag.add(_expr.first().toString())
                    _expr = _expr.drop(1)
                }
                "ОП" -> {
                    when (val tmp = graph()) {
                        "" -> {
                        }
                        null -> {
                            println("Не допущено")
                            return
                        }
                        else -> {
                                mag.add(tmp)
                        }
                    }
                }
                "Д" -> {
                    showRules()
                    println("Допущено")
                    return
                }
                "" -> {
                    println("Не допущено")
                    return
                }
            }
        }
    }
}

fun main(args: Array<String>) {
    val m = MP_up()
    val expr = "a=(a=a);"
    println(expr)
    m.solve(expr)


}